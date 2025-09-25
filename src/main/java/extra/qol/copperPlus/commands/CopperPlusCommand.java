package extra.qol.copperPlus.commands;

import extra.qol.copperPlus.CopperPlus;
import extra.qol.copperPlus.managers.LanguageManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class CopperPlusCommand implements CommandExecutor, TabCompleter {
    private final CopperPlus plugin;
    private final LanguageManager languageManager;

    public CopperPlusCommand(CopperPlus plugin) {
        this.plugin = plugin;
        this.languageManager = plugin.getLanguageManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "language":
            case "lang":
                return handleLanguageCommand(sender, Arrays.copyOfRange(args, 1, args.length));

            case "kit":
                return handleKitCommand(sender);

            case "togglemsg":
            case "toggle":
                return handleToggleCommand(sender);

            case "reload":
                return handleReloadCommand(sender);

            case "help":
            default:
                sendHelp(sender);
                return true;
        }
    }

    private boolean handleLanguageCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(languageManager.getMessage("en_US", "language.player-only"));
            return true;
        }

        if (args.length == 0) {
            // Show current language
            String currentLang = languageManager.getPlayerLanguage(player);
            String langName = languageManager.getLanguageName(currentLang);
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("lang", langName + " (" + currentLang + ")");
            player.sendMessage(languageManager.getMessage(player, "language.current", placeholders));
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "set":
                if (args.length < 2) {
                    sendLanguageHelp(player);
                    return true;
                }
                String langCode = args[1];
                if (!languageManager.isValidLanguage(langCode)) {
                    Map<String, String> placeholders = new HashMap<>();
                    placeholders.put("languages", String.join(", ", languageManager.getAvailableLanguages()));
                    player.sendMessage(languageManager.getMessage(player, "language.invalid", placeholders));
                    return true;
                }
                languageManager.setPlayerLanguage(player, langCode);
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("lang", languageManager.getLanguageName(langCode) + " (" + langCode + ")");
                player.sendMessage(languageManager.getMessage(player, "language.changed", placeholders));
                break;

            case "list":
                player.sendMessage(languageManager.getMessage(player, "language.list-header"));
                for (String code : languageManager.getAvailableLanguages()) {
                    Map<String, String> listPlaceholders = new HashMap<>();
                    listPlaceholders.put("code", code);
                    listPlaceholders.put("name", languageManager.getLanguageName(code));
                    String message = languageManager.getMessage(player, "language.list-item", listPlaceholders);
                    // Remove prefix for list items
                    message = message.replace(languageManager.getPrefix(languageManager.getPlayerLanguage(player)), "").trim();
                    player.sendMessage(message);
                }
                break;

            case "admin":
                if (!player.hasPermission("copperplus.admin")) {
                    player.sendMessage(languageManager.getMessage(player, "language.admin-no-permission"));
                    return true;
                }
                if (args.length < 2) {
                    sendLanguageHelp(player);
                    return true;
                }
                String adminLangCode = args[1];
                if (!languageManager.isValidLanguage(adminLangCode)) {
                    Map<String, String> adminPlaceholders = new HashMap<>();
                    adminPlaceholders.put("languages", String.join(", ", languageManager.getAvailableLanguages()));
                    player.sendMessage(languageManager.getMessage(player, "language.invalid", adminPlaceholders));
                    return true;
                }
                languageManager.setDefaultLanguage(adminLangCode);
                Map<String, String> adminPlaceholders = new HashMap<>();
                adminPlaceholders.put("lang", languageManager.getLanguageName(adminLangCode) + " (" + adminLangCode + ")");
                player.sendMessage(languageManager.getMessage(player, "language.admin-set-default", adminPlaceholders));
                break;

            default:
                sendLanguageHelp(player);
                break;
        }

        return true;
    }

    private boolean handleKitCommand(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(languageManager.getMessage("en_US", "kit.player-only"));
            return true;
        }

        if (!player.hasPermission("copperplus.kit")) {
            player.sendMessage(languageManager.getMessage(player, "no-permission"));
            return true;
        }

        player.getInventory().addItem(
            new ItemStack(Material.CAMPFIRE, plugin.getConfigManager().getKitCampfire()),
            new ItemStack(Material.COPPER_BLOCK, plugin.getConfigManager().getKitCopperBlock()),
            new ItemStack(Material.WATER_BUCKET, plugin.getConfigManager().getKitWaterBucket()),
            new ItemStack(Material.FLINT_AND_STEEL, plugin.getConfigManager().getKitFlintAndSteel())
        );

        player.sendMessage(languageManager.getMessage(player, "kit.success"));
        player.sendMessage(languageManager.getMessage(player, "kit.help"));

        return true;
    }

    private boolean handleToggleCommand(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(languageManager.getMessage("en_US", "toggle.player-only"));
            return true;
        }

        boolean newState = plugin.getPreferenceManager().toggleMessages(player);

        if (newState) {
            player.sendMessage(languageManager.getMessage(player, "toggle.enabled"));
        } else {
            player.sendMessage(languageManager.getMessage(player, "toggle.disabled"));
        }

        return true;
    }

    private boolean handleReloadCommand(CommandSender sender) {
        if (!sender.hasPermission("copperplus.admin")) {
            if (sender instanceof Player player) {
                player.sendMessage(languageManager.getMessage(player, "no-permission"));
            } else {
                sender.sendMessage("You don't have permission to use this command!");
            }
            return true;
        }

        plugin.getConfigManager().reload();
        languageManager.reload();

        if (sender instanceof Player player) {
            player.sendMessage(languageManager.getMessage(player, "reload.success"));
        } else {
            sender.sendMessage("CopperPlus configuration reloaded!");
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        if (sender instanceof Player player) {
            player.sendMessage(languageManager.getMessage(player, "help.header"));
            player.sendMessage(languageManager.getMessage(player, "help.kit"));
            player.sendMessage(languageManager.getMessage(player, "help.language"));
            player.sendMessage(languageManager.getMessage(player, "help.togglemsg"));

            if (player.hasPermission("copperplus.admin")) {
                player.sendMessage(languageManager.getMessage(player, "help.reload"));
            }
        } else {
            sender.sendMessage("=== CopperPlus Commands ===");
            sender.sendMessage("/copperplus kit - Give copper oxidation kit");
            sender.sendMessage("/copperplus language <subcommand> - Manage language settings");
            sender.sendMessage("/copperplus togglemsg - Toggle oxidation messages");
            sender.sendMessage("/copperplus reload - Reload configuration");
        }
    }

    private void sendLanguageHelp(Player player) {
        player.sendMessage(languageManager.getMessage(player, "language.help.header"));
        String[] helpMessages = {
            languageManager.getMessage(player, "language.help.user"),
            languageManager.getMessage(player, "language.help.set"),
            languageManager.getMessage(player, "language.help.list")
        };

        for (String msg : helpMessages) {
            player.sendMessage(msg.replace(languageManager.getPrefix(languageManager.getPlayerLanguage(player)), "").trim());
        }

        if (player.hasPermission("copperplus.admin")) {
            String adminHelp = languageManager.getMessage(player, "language.help.admin");
            player.sendMessage(adminHelp.replace(languageManager.getPrefix(languageManager.getPlayerLanguage(player)), "").trim());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(Arrays.asList("kit", "language", "togglemsg", "help"));
            if (sender.hasPermission("copperplus.admin")) {
                completions.add("reload");
            }
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("language") || args[0].equalsIgnoreCase("lang"))) {
            completions.addAll(Arrays.asList("set", "list"));
            if (sender.hasPermission("copperplus.admin")) {
                completions.add("admin");
            }
        } else if (args.length == 3) {
            if ((args[0].equalsIgnoreCase("language") || args[0].equalsIgnoreCase("lang"))) {
                if (args[1].equalsIgnoreCase("set") ||
                    (args[1].equalsIgnoreCase("admin") && sender.hasPermission("copperplus.admin"))) {
                    completions.addAll(languageManager.getAvailableLanguages());
                }
            }
        }

        return completions.stream()
                .filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }
}