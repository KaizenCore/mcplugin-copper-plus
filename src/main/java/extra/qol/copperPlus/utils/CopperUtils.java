package extra.qol.copperPlus.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CopperUtils {
    private static final Map<Material, Material> OXIDATION_MAP = new HashMap<>();
    private static final Map<Material, Integer> OXIDATION_LEVEL = new HashMap<>();

    static {
        OXIDATION_MAP.put(Material.COPPER_BLOCK, Material.EXPOSED_COPPER);
        OXIDATION_MAP.put(Material.EXPOSED_COPPER, Material.WEATHERED_COPPER);
        OXIDATION_MAP.put(Material.WEATHERED_COPPER, Material.OXIDIZED_COPPER);

        OXIDATION_MAP.put(Material.CUT_COPPER, Material.EXPOSED_CUT_COPPER);
        OXIDATION_MAP.put(Material.EXPOSED_CUT_COPPER, Material.WEATHERED_CUT_COPPER);
        OXIDATION_MAP.put(Material.WEATHERED_CUT_COPPER, Material.OXIDIZED_CUT_COPPER);

        OXIDATION_MAP.put(Material.CUT_COPPER_STAIRS, Material.EXPOSED_CUT_COPPER_STAIRS);
        OXIDATION_MAP.put(Material.EXPOSED_CUT_COPPER_STAIRS, Material.WEATHERED_CUT_COPPER_STAIRS);
        OXIDATION_MAP.put(Material.WEATHERED_CUT_COPPER_STAIRS, Material.OXIDIZED_CUT_COPPER_STAIRS);

        OXIDATION_MAP.put(Material.CUT_COPPER_SLAB, Material.EXPOSED_CUT_COPPER_SLAB);
        OXIDATION_MAP.put(Material.EXPOSED_CUT_COPPER_SLAB, Material.WEATHERED_CUT_COPPER_SLAB);
        OXIDATION_MAP.put(Material.WEATHERED_CUT_COPPER_SLAB, Material.OXIDIZED_CUT_COPPER_SLAB);

        OXIDATION_LEVEL.put(Material.COPPER_BLOCK, 0);
        OXIDATION_LEVEL.put(Material.EXPOSED_COPPER, 1);
        OXIDATION_LEVEL.put(Material.WEATHERED_COPPER, 2);
        OXIDATION_LEVEL.put(Material.OXIDIZED_COPPER, 3);

        OXIDATION_LEVEL.put(Material.CUT_COPPER, 0);
        OXIDATION_LEVEL.put(Material.EXPOSED_CUT_COPPER, 1);
        OXIDATION_LEVEL.put(Material.WEATHERED_CUT_COPPER, 2);
        OXIDATION_LEVEL.put(Material.OXIDIZED_CUT_COPPER, 3);

        OXIDATION_LEVEL.put(Material.CUT_COPPER_STAIRS, 0);
        OXIDATION_LEVEL.put(Material.EXPOSED_CUT_COPPER_STAIRS, 1);
        OXIDATION_LEVEL.put(Material.WEATHERED_CUT_COPPER_STAIRS, 2);
        OXIDATION_LEVEL.put(Material.OXIDIZED_CUT_COPPER_STAIRS, 3);

        OXIDATION_LEVEL.put(Material.CUT_COPPER_SLAB, 0);
        OXIDATION_LEVEL.put(Material.EXPOSED_CUT_COPPER_SLAB, 1);
        OXIDATION_LEVEL.put(Material.WEATHERED_CUT_COPPER_SLAB, 2);
        OXIDATION_LEVEL.put(Material.OXIDIZED_CUT_COPPER_SLAB, 3);
    }

    public static boolean isCopperBlock(Material material) {
        return OXIDATION_MAP.containsKey(material) || OXIDATION_LEVEL.containsKey(material);
    }

    public static boolean isWaxedCopper(Material material) {
        String name = material.name();
        return name.contains("WAXED");
    }

    public static boolean canOxidize(Material material) {
        return OXIDATION_MAP.containsKey(material) && !isWaxedCopper(material);
    }

    public static Material getNextOxidationStage(Material material) {
        if (!canOxidize(material)) {
            return null;
        }
        return OXIDATION_MAP.get(material);
    }

    public static int getOxidationLevel(Material material) {
        return OXIDATION_LEVEL.getOrDefault(material, -1);
    }

    public static boolean isWaterItem(Material material) {
        return material == Material.WATER_BUCKET || material == Material.POTION;
    }

    public static ItemStack getEmptyContainer(Material waterItem) {
        if (waterItem == Material.WATER_BUCKET) {
            return new ItemStack(Material.BUCKET);
        } else if (waterItem == Material.POTION) {
            return new ItemStack(Material.GLASS_BOTTLE);
        }
        return null;
    }

    public static boolean isFullyOxidized(Material material) {
        return getOxidationLevel(material) == 3;
    }
}