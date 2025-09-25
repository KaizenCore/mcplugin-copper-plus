package extra.qol.copperPlus.managers;

import extra.qol.copperPlus.CopperPlus;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;

public class LanguageManager {
    private final CopperPlus plugin;
    private final Map<String, FileConfiguration> languages = new HashMap<>();
    private final Map<UUID, String> playerLanguages = new HashMap<>();
    private final Map<String, String> languageNames = new HashMap<>();
    private String defaultLanguage = "en_US";
    private FileConfiguration languageConfig;
    private File languageConfigFile;

    public LanguageManager(CopperPlus plugin) {
        this.plugin = plugin;
        loadLanguageConfig();
        loadLanguages();
        setupLanguageNames();
    }

    private void setupLanguageNames() {
        languageNames.put("en_US", "English (US)");
        languageNames.put("zh_CN", "简体中文 (Chinese Simplified)");
        languageNames.put("es_ES", "Español (Spanish)");
        languageNames.put("hi_IN", "हिन्दी (Hindi)");
        languageNames.put("ar_SA", "العربية (Arabic)");
        languageNames.put("pt_BR", "Português (Portuguese)");
        languageNames.put("ru_RU", "Русский (Russian)");
        languageNames.put("ja_JP", "日本語 (Japanese)");
        languageNames.put("de_DE", "Deutsch (German)");
        languageNames.put("fr_FR", "Français (French)");
        languageNames.put("ko_KR", "한국어 (Korean)");
    }

    private void loadLanguageConfig() {
        languageConfigFile = new File(plugin.getDataFolder(), "languages.yml");
        if (!languageConfigFile.exists()) {
            languageConfigFile.getParentFile().mkdirs();
            try {
                languageConfigFile.createNewFile();
                languageConfig = YamlConfiguration.loadConfiguration(languageConfigFile);
                languageConfig.set("default-language", "en_US");
                languageConfig.set("player-languages", new HashMap<String, String>());
                saveLanguageConfig();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not create language config file", e);
            }
        }
        languageConfig = YamlConfiguration.loadConfiguration(languageConfigFile);
        defaultLanguage = languageConfig.getString("default-language", "en_US");

        // Load player languages
        if (languageConfig.contains("player-languages")) {
            for (String key : languageConfig.getConfigurationSection("player-languages").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(key);
                    String lang = languageConfig.getString("player-languages." + key);
                    playerLanguages.put(uuid, lang);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid UUID in language config: " + key);
                }
            }
        }
    }

    private void saveLanguageConfig() {
        try {
            languageConfig.save(languageConfigFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save language config", e);
        }
    }

    private void loadLanguages() {
        // First, ensure language files are extracted
        extractLanguageFiles();

        // Load all language files
        File languagesDir = new File(plugin.getDataFolder(), "languages");
        if (!languagesDir.exists()) {
            languagesDir.mkdirs();
        }

        File[] files = languagesDir.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files != null) {
            for (File file : files) {
                String langCode = file.getName().replace(".yml", "");
                try {
                    FileConfiguration config = YamlConfiguration.loadConfiguration(
                        new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)
                    );
                    languages.put(langCode, config);
                    plugin.getLogger().info("Loaded language: " + langCode);
                } catch (Exception e) {
                    plugin.getLogger().log(Level.WARNING, "Failed to load language file: " + file.getName(), e);
                }
            }
        }

        // Ensure default language exists
        if (!languages.containsKey(defaultLanguage)) {
            plugin.getLogger().warning("Default language " + defaultLanguage + " not found, falling back to en_US");
            defaultLanguage = "en_US";
        }
    }

    private void extractLanguageFiles() {
        String[] defaultLangs = {
            "en_US.yml", "zh_CN.yml", "es_ES.yml", "hi_IN.yml", "ar_SA.yml",
            "pt_BR.yml", "ru_RU.yml", "ja_JP.yml", "de_DE.yml", "fr_FR.yml", "ko_KR.yml"
        };

        for (String langFile : defaultLangs) {
            File targetFile = new File(plugin.getDataFolder(), "languages/" + langFile);
            if (!targetFile.exists()) {
                try {
                    targetFile.getParentFile().mkdirs();
                    InputStream inputStream = plugin.getResource("languages/" + langFile);
                    if (inputStream != null) {
                        try (OutputStream outputStream = new FileOutputStream(targetFile)) {
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = inputStream.read(buffer)) > 0) {
                                outputStream.write(buffer, 0, length);
                            }
                        }
                        inputStream.close();
                    }
                } catch (IOException e) {
                    plugin.getLogger().log(Level.WARNING, "Could not extract language file: " + langFile, e);
                }
            }
        }
    }

    public String getMessage(Player player, String path) {
        return getMessage(player, path, new HashMap<>());
    }

    public String getMessage(Player player, String path, Map<String, String> placeholders) {
        String langCode = getPlayerLanguage(player);
        return getMessage(langCode, path, placeholders);
    }

    public String getMessage(String langCode, String path) {
        return getMessage(langCode, path, new HashMap<>());
    }

    public String getMessage(String langCode, String path, Map<String, String> placeholders) {
        FileConfiguration langConfig = languages.getOrDefault(langCode, languages.get(defaultLanguage));
        if (langConfig == null) {
            return path; // Fallback to path if no language loaded
        }

        String message = langConfig.getString(path, path);

        // Apply placeholders
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            message = message.replace("%" + entry.getKey() + "%", entry.getValue());
        }

        // Apply prefix if needed
        if (!path.equals("prefix") && !message.contains(getPrefix(langCode))) {
            String prefix = getPrefix(langCode);
            if (!prefix.isEmpty()) {
                message = prefix + " " + message;
            }
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getPrefix(String langCode) {
        FileConfiguration langConfig = languages.getOrDefault(langCode, languages.get(defaultLanguage));
        if (langConfig == null) {
            return "[CopperPlus]";
        }
        return langConfig.getString("prefix", "[CopperPlus]");
    }

    public String getPlayerLanguage(Player player) {
        return playerLanguages.getOrDefault(player.getUniqueId(), defaultLanguage);
    }

    public void setPlayerLanguage(Player player, String langCode) {
        if (!languages.containsKey(langCode)) {
            return;
        }
        playerLanguages.put(player.getUniqueId(), langCode);
        languageConfig.set("player-languages." + player.getUniqueId().toString(), langCode);
        saveLanguageConfig();
    }

    public void setDefaultLanguage(String langCode) {
        if (!languages.containsKey(langCode)) {
            return;
        }
        defaultLanguage = langCode;
        languageConfig.set("default-language", langCode);
        saveLanguageConfig();
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public Set<String> getAvailableLanguages() {
        return languages.keySet();
    }

    public String getLanguageName(String langCode) {
        return languageNames.getOrDefault(langCode, langCode);
    }

    public boolean isValidLanguage(String langCode) {
        return languages.containsKey(langCode);
    }

    public void removePlayerData(Player player) {
        playerLanguages.remove(player.getUniqueId());
        languageConfig.set("player-languages." + player.getUniqueId().toString(), null);
        saveLanguageConfig();
    }

    public void reload() {
        languages.clear();
        playerLanguages.clear();
        loadLanguageConfig();
        loadLanguages();
    }
}