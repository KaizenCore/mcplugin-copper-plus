package extra.qol.copperPlus.managers;

import extra.qol.copperPlus.CopperPlus;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerPreferenceManager {
    private final CopperPlus plugin;
    private final Map<UUID, Boolean> messagePreferences = new HashMap<>();

    public PlayerPreferenceManager(CopperPlus plugin) {
        this.plugin = plugin;
    }

    /**
     * Check if a player has messages enabled (default is true)
     */
    public boolean hasMessagesEnabled(Player player) {
        return messagePreferences.getOrDefault(player.getUniqueId(), true);
    }

    /**
     * Toggle messages for a player and return the new state
     */
    public boolean toggleMessages(Player player) {
        UUID uuid = player.getUniqueId();
        boolean newState = !hasMessagesEnabled(player);
        messagePreferences.put(uuid, newState);
        return newState;
    }

    /**
     * Set messages enabled/disabled for a player
     */
    public void setMessagesEnabled(Player player, boolean enabled) {
        messagePreferences.put(player.getUniqueId(), enabled);
    }

    /**
     * Clean up player data when they leave
     */
    public void removePlayer(Player player) {
        messagePreferences.remove(player.getUniqueId());
    }

    /**
     * Clear all preferences
     */
    public void clearAll() {
        messagePreferences.clear();
    }
}