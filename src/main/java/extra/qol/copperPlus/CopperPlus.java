package extra.qol.copperPlus;

import extra.qol.copperPlus.commands.CopperPlusCommand;
import extra.qol.copperPlus.config.ConfigManager;
import extra.qol.copperPlus.listeners.CopperBlockListener;
import extra.qol.copperPlus.managers.CopperBlockOxidationManager;
import extra.qol.copperPlus.managers.LanguageManager;
import extra.qol.copperPlus.managers.PlayerPreferenceManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CopperPlus extends JavaPlugin {

    private ConfigManager configManager;
    private CopperBlockOxidationManager oxidationManager;
    private PlayerPreferenceManager preferenceManager;
    private LanguageManager languageManager;

    @Override
    public void onEnable() {
        // Initialize configuration
        configManager = new ConfigManager(this);

        // Check if plugin is enabled in config
        if (!configManager.isEnabled()) {
            getLogger().warning("CopperPlus is disabled in config.yml! Enable it to use the plugin.");
            return;
        }

        // Initialize language manager
        languageManager = new LanguageManager(this);

        // Initialize player preference manager
        preferenceManager = new PlayerPreferenceManager(this);

        // Initialize the copper block oxidation manager
        oxidationManager = new CopperBlockOxidationManager(this, configManager);
        oxidationManager.setPreferenceManager(preferenceManager);

        // Register the block listener
        CopperBlockListener blockListener = new CopperBlockListener(this, configManager, oxidationManager);
        getServer().getPluginManager().registerEvents(blockListener, this);

        // Register main command
        CopperPlusCommand mainCommand = new CopperPlusCommand(this);
        getCommand("copperplus").setExecutor(mainCommand);
        getCommand("copperplus").setTabCompleter(mainCommand);

        getLogger().info("CopperPlus has been enabled!");
        getLogger().info("Place copper blocks with a campfire below and water nearby for rapid oxidation!");

        if (configManager.isDebug()) {
            getLogger().info("Debug mode is enabled - showing detailed information.");
        }
    }

    @Override
    public void onDisable() {
        if (oxidationManager != null) {
            oxidationManager.stopAll();
        }

        if (preferenceManager != null) {
            preferenceManager.clearAll();
        }

        getLogger().info("CopperPlus has been disabled!");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CopperBlockOxidationManager getOxidationManager() {
        return oxidationManager;
    }

    public PlayerPreferenceManager getPreferenceManager() {
        return preferenceManager;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }
}
