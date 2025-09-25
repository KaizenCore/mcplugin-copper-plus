package extra.qol.copperPlus.utils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;

public class SoundEffects {

    public static void playOxidationSound(Location location) {
        World world = location.getWorld();
        if (world == null) return;

        world.playSound(location, Sound.BLOCK_FIRE_EXTINGUISH, 0.3f, 1.2f);
        world.playSound(location, Sound.BLOCK_BREWING_STAND_BREW, 0.5f, 1.5f);
    }

    public static void playStartSound(Location location, float volume, float pitch) {
        World world = location.getWorld();
        if (world == null) return;

        world.playSound(location, Sound.ITEM_BUCKET_EMPTY, volume, pitch);
        world.playSound(location, Sound.BLOCK_FURNACE_FIRE_CRACKLE, volume * 0.8f, pitch * 1.2f);
    }

    public static void playCompletionSound(Location location, float volume, float pitch) {
        World world = location.getWorld();
        if (world == null) return;

        world.playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, volume, pitch);
        world.playSound(location, Sound.BLOCK_COPPER_PLACE, volume * 1.4f, pitch * 1.1f);
    }

    public static void playWaterEvaporationSound(Location location) {
        World world = location.getWorld();
        if (world == null) return;

        world.playSound(location, Sound.BLOCK_LAVA_EXTINGUISH, 0.2f, 1.5f);
    }

    public static void playCampfireActiveSound(Location location) {
        World world = location.getWorld();
        if (world == null) return;

        world.playSound(location, Sound.BLOCK_CAMPFIRE_CRACKLE, 0.3f, 1.0f);
    }
}