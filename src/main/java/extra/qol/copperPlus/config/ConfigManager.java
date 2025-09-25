package extra.qol.copperPlus.config;

import extra.qol.copperPlus.CopperPlus;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final CopperPlus plugin;
    private FileConfiguration config;

    // General settings
    private boolean enabled;
    private boolean debug;

    // Oxidation settings
    private int oxidationDuration;
    private double waterSpeedBonus;
    private int maxWaterBlocks;
    private boolean requireLitCampfire;
    private boolean dispenserPlacement;
    private int waterCheckRadius;

    // Effects settings
    private boolean particleEffects;
    private String particleType;
    private int particleCount;
    private boolean soundEffects;
    private float soundVolume;
    private float soundPitch;

    // Performance settings
    private int checkInterval;
    private int maxPerChunk;
    private boolean clearOnUnload;

    // Kit settings
    private int kitCampfire;
    private int kitCopperBlock;
    private int kitWaterBucket;
    private int kitFlintAndSteel;
    private int kitCooldown;

    // Messages
    private String oxidationStartedMsg;
    private String oxidationProgressMsg;
    private String oxidationCompletedMsg;
    private String oxidationFullyCompleteMsg;
    private String noWaterMsg;
    private String campfireNotLitMsg;
    private String checkingSetupMsg;
    private String noPermissionMsg;
    private String kitCooldownMsg;
    private String kitPlayerOnlyMsg;
    private String kitSuccessMsg;
    private String kitHelpMsg;

    public ConfigManager(CopperPlus plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();

        // General settings
        enabled = config.getBoolean("general.enabled", true);
        debug = config.getBoolean("general.debug", false);

        // Oxidation settings
        oxidationDuration = config.getInt("oxidation.duration-seconds", 60);
        waterSpeedBonus = config.getDouble("oxidation.water-speed-bonus", 0.25);
        maxWaterBlocks = config.getInt("oxidation.max-water-blocks", 26);
        requireLitCampfire = config.getBoolean("oxidation.require-lit-campfire", true);
        dispenserPlacement = config.getBoolean("oxidation.dispenser-placement", true);
        waterCheckRadius = config.getInt("oxidation.water-check-radius", 1);

        // Effects settings
        particleEffects = config.getBoolean("effects.particles", true);
        particleType = config.getString("effects.particle-type", "WAX_ON");
        particleCount = config.getInt("effects.particle-count", 10);
        soundEffects = config.getBoolean("effects.sounds", true);
        soundVolume = (float) config.getDouble("effects.sound-volume", 0.5);
        soundPitch = (float) config.getDouble("effects.sound-pitch", 1.0);

        // Performance settings
        checkInterval = config.getInt("performance.check-interval", 20);
        maxPerChunk = config.getInt("performance.max-per-chunk", 50);
        clearOnUnload = config.getBoolean("performance.clear-on-unload", true);

        // Kit settings
        kitCampfire = config.getInt("kit.items.campfire", 4);
        kitCopperBlock = config.getInt("kit.items.copper-block", 64);
        kitWaterBucket = config.getInt("kit.items.water-bucket", 2);
        kitFlintAndSteel = config.getInt("kit.items.flint-and-steel", 1);
        kitCooldown = config.getInt("kit.cooldown", 0);
        kitSuccessMsg = config.getString("kit.success-message", "&a[CopperPlus] &eYou received the copper oxidation kit!");
        kitHelpMsg = config.getString("kit.help-message", "&7Place campfire, copper block above it, and water nearby!");

        // Messages
        oxidationStartedMsg = config.getString("messages.oxidation-started", "&a[CopperPlus] &eStarted accelerated oxidation for copper!");
        oxidationProgressMsg = config.getString("messages.oxidation-progress", "&a[CopperPlus] &eOxidizing rapidly! Water blocks: &b%water% &e(Speed: &b%speed%x&e)");
        oxidationCompletedMsg = config.getString("messages.oxidation-completed", "&a[CopperPlus] &eOxidized &b%from% &eto &b%to%");
        oxidationFullyCompleteMsg = config.getString("messages.oxidation-fully-complete", "&a[CopperPlus] &6Copper fully oxidized!");
        noWaterMsg = config.getString("messages.no-water", "&a[CopperPlus] &cNeed water nearby for rapid oxidation!");
        campfireNotLitMsg = config.getString("messages.campfire-not-lit", "&a[CopperPlus] &cThe campfire must be lit!");
        checkingSetupMsg = config.getString("messages.checking-setup", "&a[CopperPlus] &7Checking oxidation setup...");
        noPermissionMsg = config.getString("messages.no-permission", "&cYou don't have permission to use this!");
        kitCooldownMsg = config.getString("messages.kit-cooldown", "&cYou must wait %time% seconds before using the kit again!");
        kitPlayerOnlyMsg = config.getString("messages.kit-player-only", "&cThis command can only be used by players!");
    }

    public void reload() {
        loadConfig();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getOxidationDuration() {
        return oxidationDuration;
    }

    public boolean isParticleEffectsEnabled() {
        return particleEffects;
    }

    public boolean isSoundEffectsEnabled() {
        return soundEffects;
    }

    public double getWaterSpeedBonus() {
        return waterSpeedBonus;
    }

    public boolean isRequireLitCampfire() {
        return requireLitCampfire;
    }

    public boolean isDispenserPlacementEnabled() {
        return dispenserPlacement;
    }

    public boolean isDebug() {
        return debug;
    }

    public int getMaxWaterBlocks() {
        return maxWaterBlocks;
    }

    public int getWaterCheckRadius() {
        return waterCheckRadius;
    }

    public String getParticleType() {
        return particleType;
    }

    public int getParticleCount() {
        return particleCount;
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    public float getSoundPitch() {
        return soundPitch;
    }

    public int getCheckInterval() {
        return checkInterval;
    }

    public int getMaxPerChunk() {
        return maxPerChunk;
    }

    public boolean isClearOnUnload() {
        return clearOnUnload;
    }

    // Kit getters
    public int getKitCampfire() {
        return kitCampfire;
    }

    public int getKitCopperBlock() {
        return kitCopperBlock;
    }

    public int getKitWaterBucket() {
        return kitWaterBucket;
    }

    public int getKitFlintAndSteel() {
        return kitFlintAndSteel;
    }

    public int getKitCooldown() {
        return kitCooldown;
    }

    // Message getters
    public String getOxidationStartedMsg() {
        return oxidationStartedMsg;
    }

    public String getOxidationProgressMsg() {
        return oxidationProgressMsg;
    }

    public String getOxidationCompletedMsg() {
        return oxidationCompletedMsg;
    }

    public String getOxidationFullyCompleteMsg() {
        return oxidationFullyCompleteMsg;
    }

    public String getNoWaterMsg() {
        return noWaterMsg;
    }

    public String getCampfireNotLitMsg() {
        return campfireNotLitMsg;
    }

    public String getCheckingSetupMsg() {
        return checkingSetupMsg;
    }

    public String getNoPermissionMsg() {
        return noPermissionMsg;
    }

    public String getKitCooldownMsg() {
        return kitCooldownMsg;
    }

    public String getKitPlayerOnlyMsg() {
        return kitPlayerOnlyMsg;
    }

    public String getKitSuccessMsg() {
        return kitSuccessMsg;
    }

    public String getKitHelpMsg() {
        return kitHelpMsg;
    }
}