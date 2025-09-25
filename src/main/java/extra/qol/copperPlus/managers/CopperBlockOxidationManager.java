package extra.qol.copperPlus.managers;

import extra.qol.copperPlus.CopperPlus;
import extra.qol.copperPlus.config.ConfigManager;
import extra.qol.copperPlus.utils.CopperUtils;
import extra.qol.copperPlus.utils.ParticleEffects;
import extra.qol.copperPlus.utils.SoundEffects;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Campfire;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class CopperBlockOxidationManager {
    private final CopperPlus plugin;
    private final ConfigManager configManager;
    private final Map<Location, Integer> oxidationProgress = new HashMap<>();
    private final Set<Location> activeCopperBlocks = new HashSet<>();
    private BukkitRunnable oxidationTask;
    private PlayerPreferenceManager preferenceManager;
    private LanguageManager languageManager;

    public CopperBlockOxidationManager(CopperPlus plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        startOxidationTask();
    }

    public void setPreferenceManager(PlayerPreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;
        this.languageManager = plugin.getLanguageManager();
    }

    private void startOxidationTask() {
        oxidationTask = new BukkitRunnable() {
            @Override
            public void run() {
                // Process all active copper blocks
                Set<Location> toRemove = new HashSet<>();

                for (Location loc : new HashSet<>(activeCopperBlocks)) {
                    if (!processOxidation(loc)) {
                        toRemove.add(loc);
                    }
                }

                activeCopperBlocks.removeAll(toRemove);
                oxidationProgress.keySet().removeAll(toRemove);
            }
        };
        // Use config check interval
        long interval = configManager.getCheckInterval();
        oxidationTask.runTaskTimer(plugin, interval, interval);
    }

    private boolean processOxidation(Location copperLocation) {
        Block copperBlock = copperLocation.getBlock();

        // Check if still a copper block
        if (!CopperUtils.canOxidize(copperBlock.getType())) {
            return false;
        }

        // Check for campfire below
        if (!hasCampfireBelow(copperLocation)) {
            return false;
        }

        // Count water blocks nearby
        int waterCount = countWaterNearby(copperLocation);
        if (waterCount == 0) {
            return false;
        }

        // Calculate speed multiplier based on water count
        // Base speed + bonus for each water block (max 26 water blocks in 3x3x3)
        double speedMultiplier = 1.0 + (waterCount * configManager.getWaterSpeedBonus());

        // Get or initialize progress
        int progress = oxidationProgress.getOrDefault(copperLocation, 0);
        progress += speedMultiplier;
        oxidationProgress.put(copperLocation, (int) progress);

        // Play effects every 5 seconds
        if (progress % 5 == 0) {
            ParticleEffects.playOxidationEffect(copperLocation.clone().add(0.5, 0.5, 0.5), configManager);
        }

        // Check if oxidation is complete
        if (progress >= configManager.getOxidationDuration()) {
            return completeOxidation(copperLocation);
        }

        return true;
    }

    private boolean completeOxidation(Location copperLocation) {
        Block copperBlock = copperLocation.getBlock();
        Material currentType = copperBlock.getType();
        Material nextStage = CopperUtils.getNextOxidationStage(currentType);

        if (nextStage == null) {
            return false;
        }

        // Change the block type
        copperBlock.setType(nextStage);

        if (configManager.isDebug()) {
            plugin.getLogger().info("Oxidized " + currentType + " to " + nextStage + " at " + copperLocation);
        }

        // Play effects
        ParticleEffects.playCompletionEffect(copperLocation.clone().add(0.5, 0.5, 0.5), configManager);

        if (configManager.isSoundEffectsEnabled()) {
            SoundEffects.playCompletionSound(copperLocation, configManager.getSoundVolume(), configManager.getSoundPitch());
        }

        // Reset progress for the next oxidation stage
        oxidationProgress.put(copperLocation, 0);

        // Notify nearby players with localized messages
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("from", currentType.toString().replace("_", " "));
        placeholders.put("to", nextStage.toString().replace("_", " "));
        notifyNearbyPlayersLocalized(copperLocation, "oxidation.completed", placeholders);

        // Check if the new block can oxidize further
        if (CopperUtils.canOxidize(nextStage)) {
            // Continue oxidation immediately
            if (configManager.isDebug()) {
                plugin.getLogger().info("Continuing oxidation for " + nextStage + " at " + copperLocation);
            }
            return true; // Keep in active list to continue oxidation
        }

        // Fully oxidized - notify players with localized message
        notifyNearbyPlayersLocalized(copperLocation, "oxidation.fully-complete", new HashMap<>());

        // Remove from tracking if fully oxidized
        oxidationProgress.remove(copperLocation);
        return false;
    }

    private boolean hasCampfireBelow(Location location) {
        Block blockBelow = location.clone().subtract(0, 1, 0).getBlock();

        if (blockBelow.getType() != Material.CAMPFIRE &&
            blockBelow.getType() != Material.SOUL_CAMPFIRE) {
            return false;
        }

        // Check if campfire is lit
        if (configManager.isRequireLitCampfire()) {
            Campfire campfire = (Campfire) blockBelow.getBlockData();
            return campfire.isLit();
        }

        return true;
    }

    private boolean hasWaterNearby(Location location) {
        return countWaterNearby(location) > 0;
    }

    private int countWaterNearby(Location location) {
        int waterCount = 0;
        int radius = configManager.getWaterCheckRadius();
        int maxWater = configManager.getMaxWaterBlocks();

        // Check all blocks in the configured radius around the copper block
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x == 0 && y == 0 && z == 0) continue; // Skip the copper block itself
                    if (waterCount >= maxWater) return waterCount; // Don't exceed max

                    Block nearby = location.clone().add(x, y, z).getBlock();
                    Material type = nearby.getType();

                    // Check for water source or flowing water
                    if (type == Material.WATER ||
                        type == Material.WATER_CAULDRON ||
                        type == Material.BUBBLE_COLUMN) {
                        waterCount++;
                    }

                    // Check for waterlogged blocks
                    else if (nearby.getBlockData() instanceof org.bukkit.block.data.Waterlogged) {
                        org.bukkit.block.data.Waterlogged waterlogged =
                            (org.bukkit.block.data.Waterlogged) nearby.getBlockData();
                        if (waterlogged.isWaterlogged()) {
                            waterCount++;
                        }
                    }
                }
            }
        }
        return waterCount;
    }

    public void checkAndAddCopperBlock(Location location) {
        Block block = location.getBlock();

        // Only add if it's an oxidizable copper block
        if (!CopperUtils.canOxidize(block.getType())) {
            return;
        }

        // Only add if setup is valid
        if (!hasCampfireBelow(location) || !hasWaterNearby(location)) {
            return;
        }

        // Add to active tracking
        if (!activeCopperBlocks.contains(location)) {
            activeCopperBlocks.add(location);
            if (configManager.isDebug()) {
                plugin.getLogger().info("Started accelerated oxidation for copper at " + location);
            }

            // Notify nearby players with localized message
            notifyNearbyPlayersLocalized(location, "oxidation.started", new HashMap<>());

            if (configManager.isSoundEffectsEnabled()) {
                SoundEffects.playStartSound(location, configManager.getSoundVolume(), configManager.getSoundPitch());
            }
        }
    }

    public void removeCopperBlock(Location location) {
        activeCopperBlocks.remove(location);
        oxidationProgress.remove(location);
    }

    public void checkNearbyCopper(Location centerLocation) {
        // When water or campfire is placed, check nearby copper blocks
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Location checkLoc = centerLocation.clone().add(x, y, z);
                    checkAndAddCopperBlock(checkLoc);
                }
            }
        }
    }

    public void stopAll() {
        if (oxidationTask != null) {
            oxidationTask.cancel();
        }
        activeCopperBlocks.clear();
        oxidationProgress.clear();
    }

    public Set<Location> getActiveCopperBlocks() {
        return new HashSet<>(activeCopperBlocks);
    }

    /**
     * Notify all players within 16 blocks of the location about oxidation events with localized messages
     */
    private void notifyNearbyPlayersLocalized(Location location, String messageKey, Map<String, String> placeholders) {
        if (languageManager == null) {
            return; // Language manager not initialized yet
        }

        // Find nearby players within 16 blocks
        for (Player player : location.getWorld().getPlayers()) {
            if (player.getLocation().distance(location) <= 16.0) {
                // Check if player has messages enabled
                if (preferenceManager == null || preferenceManager.hasMessagesEnabled(player)) {
                    String message = languageManager.getMessage(player, messageKey, placeholders);
                    player.sendMessage(message);
                }
            }
        }
    }
}