package extra.qol.copperPlus.listeners;

import extra.qol.copperPlus.CopperPlus;
import extra.qol.copperPlus.config.ConfigManager;
import extra.qol.copperPlus.managers.CopperBlockOxidationManager;
import extra.qol.copperPlus.managers.LanguageManager;
import extra.qol.copperPlus.utils.CopperUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.BlockDispenseEvent;

public class CopperBlockListener implements Listener {
    private final CopperPlus plugin;
    private final ConfigManager configManager;
    private final CopperBlockOxidationManager oxidationManager;
    private final LanguageManager languageManager;

    public CopperBlockListener(CopperPlus plugin, ConfigManager configManager,
                              CopperBlockOxidationManager oxidationManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.oxidationManager = oxidationManager;
        this.languageManager = plugin.getLanguageManager();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!configManager.isEnabled()) return;

        Block placed = event.getBlock();
        Material type = placed.getType();

        // When copper is placed, check if it should start oxidizing
        if (CopperUtils.canOxidize(type)) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                oxidationManager.checkAndAddCopperBlock(placed.getLocation());
            }, 1L);
        }

        // When campfire is placed, check copper above
        if (type == Material.CAMPFIRE || type == Material.SOUL_CAMPFIRE) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                Location above = placed.getLocation().clone().add(0, 1, 0);
                oxidationManager.checkAndAddCopperBlock(above);
            }, 1L);
        }

        // When water-containing block is placed, check nearby copper
        if (type == Material.WATER || type == Material.WATER_CAULDRON ||
            isWaterloggable(placed)) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                oxidationManager.checkNearbyCopper(placed.getLocation());
            }, 1L);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!configManager.isEnabled()) return;

        Block broken = event.getBlock();
        Location loc = broken.getLocation();

        // Remove copper from tracking if broken
        if (CopperUtils.isCopperBlock(broken.getType())) {
            oxidationManager.removeCopperBlock(loc);
        }

        // If campfire is broken, stop oxidation of copper above
        if (broken.getType() == Material.CAMPFIRE ||
            broken.getType() == Material.SOUL_CAMPFIRE) {
            Location above = loc.clone().add(0, 1, 0);
            oxidationManager.removeCopperBlock(above);
        }

        // If water is removed, check nearby copper blocks
        if (broken.getType() == Material.WATER ||
            broken.getType() == Material.WATER_CAULDRON) {
            for (Location activeLoc : oxidationManager.getActiveCopperBlocks()) {
                if (activeLoc.distance(loc) <= 2) {
                    // Re-check this copper block
                    oxidationManager.removeCopperBlock(activeLoc);
                    plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                        oxidationManager.checkAndAddCopperBlock(activeLoc);
                    }, 1L);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWaterPlace(PlayerBucketEmptyEvent event) {
        if (!configManager.isEnabled()) return;

        if (event.getBucket() == Material.WATER_BUCKET) {
            Block block = event.getBlockClicked().getRelative(event.getBlockFace());
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                oxidationManager.checkNearbyCopper(block.getLocation());
            }, 1L);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onLiquidFlow(BlockFromToEvent event) {
        if (!configManager.isEnabled()) return;

        // When water flows to a new location, check for copper
        if (event.getBlock().getType() == Material.WATER) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                oxidationManager.checkNearbyCopper(event.getToBlock().getLocation());
            }, 5L);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDispenserPlace(BlockDispenseEvent event) {
        if (!configManager.isEnabled()) return;
        if (!configManager.isDispenserPlacementEnabled()) return;

        // Check if a dispenser is dispensing a copper block
        ItemStack item = event.getItem();
        if (item == null) return;

        Material itemType = item.getType();

        // Check if it's a copper block item
        if (!isCopperBlockItem(itemType)) return;

        // Get the block where the item will be placed
        Block dispenser = event.getBlock();
        org.bukkit.block.data.type.Dispenser dispenserData =
            (org.bukkit.block.data.type.Dispenser) dispenser.getBlockData();
        Block targetBlock = dispenser.getRelative(dispenserData.getFacing());

        // Only proceed if the target location is air or replaceable
        if (!targetBlock.getType().isAir() && !targetBlock.isReplaceable()) return;

        // Cancel the default dispense behavior
        event.setCancelled(true);

        // Get the material to place based on the item
        Material blockMaterial = getBlockMaterialFromItem(itemType);
        if (blockMaterial == null) return;

        // Place the block
        targetBlock.setType(blockMaterial);

        // Remove one item from the dispenser
        org.bukkit.block.Container container = (org.bukkit.block.Container) dispenser.getState();
        for (int i = 0; i < container.getInventory().getSize(); i++) {
            ItemStack slot = container.getInventory().getItem(i);
            if (slot != null && slot.getType() == itemType) {
                if (slot.getAmount() > 1) {
                    slot.setAmount(slot.getAmount() - 1);
                } else {
                    container.getInventory().setItem(i, null);
                }
                break;
            }
        }

        // Check for oxidation setup
        if (CopperUtils.canOxidize(blockMaterial)) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                oxidationManager.checkAndAddCopperBlock(targetBlock.getLocation());

                // Also check if there's a campfire below
                Block below = targetBlock.getRelative(0, -1, 0);
                if (below.getType() == Material.CAMPFIRE ||
                    below.getType() == Material.SOUL_CAMPFIRE) {

                    int waterCount = countWaterNearby(targetBlock.getLocation());
                    if (waterCount > 0 && configManager.isDebug()) {
                        plugin.getLogger().info("Dispenser placed copper block at " +
                            targetBlock.getLocation() + " with campfire below and " +
                            waterCount + " water blocks nearby - starting oxidation");
                    }
                }
            }, 1L);
        }
    }

    private boolean isCopperBlockItem(Material material) {
        String name = material.name();
        return name.contains("COPPER") &&
               (name.contains("BLOCK") || name.contains("STAIRS") ||
                name.contains("SLAB") || name.equals("CUT_COPPER"));
    }

    private Material getBlockMaterialFromItem(Material itemMaterial) {
        // Items and blocks have the same material enum in modern Minecraft
        // Just return the same material since copper items are also block materials
        return itemMaterial;
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {
        if (!configManager.isEnabled()) return;

        if (event.getAction() != org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;

        Block clicked = event.getClickedBlock();
        Player player = event.getPlayer();

        // Show info when right-clicking copper with campfire below
        if (CopperUtils.canOxidize(clicked.getType())) {
            Location loc = clicked.getLocation();
            Block below = loc.clone().subtract(0, 1, 0).getBlock();

            if (below.getType() == Material.CAMPFIRE ||
                below.getType() == Material.SOUL_CAMPFIRE) {

                int waterCount = countWaterNearby(loc);
                boolean isActive = oxidationManager.getActiveCopperBlocks().contains(loc);

                if (isActive) {
                    double speedMultiplier = 1.0 + (waterCount * configManager.getWaterSpeedBonus());
                    Map<String, String> placeholders = new HashMap<>();
                    placeholders.put("water", String.valueOf(waterCount));
                    placeholders.put("speed", String.format("%.1f", speedMultiplier));
                    player.sendMessage(languageManager.getMessage(player, "oxidation.progress", placeholders));
                } else if (waterCount == 0) {
                    player.sendMessage(languageManager.getMessage(player, "oxidation.no-water"));
                } else {
                    player.sendMessage(languageManager.getMessage(player, "oxidation.checking-setup"));
                    oxidationManager.checkAndAddCopperBlock(loc);
                }
            }
        }
    }

    private boolean isWaterloggable(Block block) {
        return block.getBlockData() instanceof org.bukkit.block.data.Waterlogged;
    }

    private int countWaterNearby(Location location) {
        int waterCount = 0;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;

                    Block nearby = location.clone().add(x, y, z).getBlock();
                    if (nearby.getType() == Material.WATER ||
                        nearby.getType() == Material.WATER_CAULDRON ||
                        nearby.getType() == Material.BUBBLE_COLUMN) {
                        waterCount++;
                    } else if (nearby.getBlockData() instanceof org.bukkit.block.data.Waterlogged) {
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
}