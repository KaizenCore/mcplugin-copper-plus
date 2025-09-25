package extra.qol.copperPlus.utils;

import extra.qol.copperPlus.config.ConfigManager;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public class ParticleEffects {

    public static void playOxidationEffect(Location location, ConfigManager config) {
        World world = location.getWorld();
        if (world == null || !config.isParticleEffectsEnabled()) return;

        Location particleLocation = location.clone().add(0.5, 1.2, 0.5);
        int count = config.getParticleCount() / 3; // Distribute particles across types

        // Get particle type from config with fallback
        Particle particleType;
        try {
            particleType = Particle.valueOf(config.getParticleType());
        } catch (IllegalArgumentException e) {
            particleType = Particle.WAX_ON; // Fallback
        }

        world.spawnParticle(particleType, particleLocation, count, 0.2, 0.2, 0.2, 0.02);
        world.spawnParticle(Particle.WHITE_SMOKE, particleLocation, count, 0.1, 0.2, 0.1, 0.01);
        world.spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, particleLocation, count, 0.1, 0.3, 0.1, 0.01);
    }

    public static void playCompletionEffect(Location location, ConfigManager config) {
        World world = location.getWorld();
        if (world == null || !config.isParticleEffectsEnabled()) return;

        Location particleLocation = location.clone().add(0.5, 0.5, 0.5);
        int count = config.getParticleCount();

        world.spawnParticle(Particle.HAPPY_VILLAGER, particleLocation, count, 0.3, 0.3, 0.3, 0.1);
        world.spawnParticle(Particle.GLOW, particleLocation, count / 2, 0.2, 0.2, 0.2, 0.05);
    }

    public static void playWaterEvaporationEffect(Location location) {
        World world = location.getWorld();
        if (world == null) return;

        Location particleLocation = location.clone().add(0.5, 0.8, 0.5);

        world.spawnParticle(Particle.SPLASH, particleLocation, 5, 0.2, 0.1, 0.2, 0.05);
        world.spawnParticle(Particle.BUBBLE_POP, particleLocation, 3, 0.1, 0.1, 0.1, 0.01);
    }

    public static void playCampfireSmokeEffect(Location location) {
        World world = location.getWorld();
        if (world == null) return;

        Location particleLocation = location.clone().add(0.5, 0.2, 0.5);

        world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, particleLocation, 2, 0.2, 0.1, 0.2, 0.01);
    }
}