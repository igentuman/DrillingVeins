package igentuman.dveins;

import net.minecraftforge.common.config.Config;

@Config(modid = DVeins.MODID)
public class ModConfig {
    public static OreGeneration oreGeneration = new OreGeneration();
    public static Drilling drilling = new Drilling();

    public static class OreGeneration {

        @Config.Name("enable_ore_generation")
        @Config.Comment({
                "The base amount of power required to perform one crafting operation."
        })
        public boolean enable_ore_generation = true;

        @Config.Name("visible_on_surface")
        @Config.Comment({
                "If disabled, veins will be hidden underground (canSeeSky checks)."
        })
        public boolean visible_on_surface = true;

        @Config.Name("dimensions")
        @Config.Comment({
                "List of dimensions where ores can appear"
        })

        public String[] dimensions = new String[]{
                "0"
        };

        @Config.Name("vein_chance")
        @Config.Comment({
                "How often veins will appear in world",
                "Bigger values means more rare veins (= 1/vein_channce)"
        })
        public int vein_chance = 2000;

        @Config.Name("max_vein_size")
        @Config.Comment({
                "Maximum ore blocks per vein"
        })
        public int max_vein_size = 8000;

        @Config.Name("min_vein_size")
        @Config.Comment({
                "Minimum ore blocks per vein"
        })
        public int min_vein_size = 6000;

        @Config.Name("small_piles_per_block")
        @Config.Comment({
                "How many pliles should drop from ore block",
                "One pile equals one nugget"
        })

        public int small_piles_per_block = 3;
    }

    public static class Drilling {

        @Config.Name("energy_for_one_block")
        @Config.Comment({
                "How much kinetic (rotation) energy you need to harvest one block"
        })
        public int energy_for_one_block = 1000;

        @Config.Name("diamond_drill_head_multiplier")
        @Config.Comment({
                "Speed multiplier to harvest block with diamond head"
        })
        public double diamond_drill_head_multiplier = 1.5D;

        @Config.Name("ores_whitelist")
        @Config.Comment({
                "List of ores allowed to harvest"
        })

        public String[] ores_whitelist = new String[]{
                "oreCoalHardened",
                "oreGoldHardened",
                "oreCopperHardened",
                "oreTinHardened",
                "oreLeadHardened",
                "oreGoldHardened",
                "oreRedstoneHardened"
        };
    }

    public static class ElectricMotor {

        @Config.Name("rf_per_tick")
        @Config.Comment({
                "RF energy consumption"
        })
        public int rf_per_tick = 1000;

        @Config.Name("kinetic_energy_per_tick")
        @Config.Comment({
                "Kinetic (rotation) energy per tick"
        })
        public int kinetic_energy_per_tick = 50;
    }

}
