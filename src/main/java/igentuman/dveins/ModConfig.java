package igentuman.dveins;

import net.minecraftforge.common.config.Config;

@Config(modid = DVeins.MODID)
public class ModConfig {
    public static MechanicalCrafter mechanicalCrafter = new MechanicalCrafter();
    public static SturdyGearbox sturdyGearbox = new SturdyGearbox();
    public static MechanicalBellows mechanicalBellows = new MechanicalBellows();

    @Config.Name("debug info")
    @Config.Comment({
            "Show debug info, for development purposes only."
    })
    public static boolean DEBUG = false;

    public static class MechanicalCrafter {
        @Config.Name("base power cost")
        @Config.Comment({
                "The base amount of power required to perform one crafting operation.",
                "Basically the rotational speed of the connected gearbox multiplied by the amount of time it takes to craft in ticks."
        })
        public int basePowerCost = 1000;

        @Config.Name("per ingredient power cost")
        @Config.Comment({
                "The amount of additional rotation required per ingredient. Can be set to 0."
        })
        public int ingredientPowerCost = 200;

        @Config.Name("ejection velocity")
        @Config.Comment({
                "How quickly items move when ejected into the world. It's recommended to not set this higher than 1.0"
        })
        @Config.RangeDouble(min = 0.0)
        public double ejectionVelocity = 0.1;

        @Config.Name("output cooldown time")
        @Config.Comment({
                "Cooldown time between item outputs in ticks.",
                "Only relevant when there are a ton of container items or when crafting at a very high speed."
        })
        @Config.RangeInt(min=0)
        public int outputCooldown = 20;
    }

    public static class SturdyGearbox {
        @Config.Name("conversion rate")
        @Config.Comment({
                "The conversion rate from Immersive Engineering rotation to mechanical power."
        })
        public double conversionRate = 4.0;

        @Config.Name("output cap")
        @Config.Comment({
                "The maximum amount of power the sturdy gearbox is allowed to output. Can be used for balance reasons."
        })
        public double outputCap = Double.POSITIVE_INFINITY;

    }

    public static class MechanicalBellows {
        @Config.Name("power factor")
        @Config.Comment({
                "A factor to multiply the mechanical bellows' rotation by.",
                "Can be used to make the bellows look lss ridiculous."
        })
        public double powerFactor = 0.5;

        @Config.Name("rotation cap")
        @Config.Comment({
                "The maximum amount of rotation per tick the bellows can turn.",
                "Can be used to cap the speed at which the bellows speed up furnaces."
        })
        public double rotationCap = 80.0;

        @Config.Name("rotation per burntick")
        @Config.Comment({
                "How much the bellows have to turn to add one burn tick.",
                "Setting it to 40 will mean that running the bellows at a speed of 40 will double the furnace speed.",
                "Running it at a speed of 80 will triple it."
        })
        public double rotationPerBurnTick = 40.0;
    }
}
