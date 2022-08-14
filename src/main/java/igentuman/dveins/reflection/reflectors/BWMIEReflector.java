package igentuman.dveins.reflection.reflectors;
import betterwithmods.module.compat.immersiveengineering.ImmersiveEngineering;
import betterwithmods.module.hardcore.needs.HCSeeds;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import static betterwithmods.module.compat.immersiveengineering.ImmersiveEngineering.HEMP_SEED;

public class BWMIEReflector {
    //avoid null pointer exception in BWM
    public static void init(ImmersiveEngineering instance, FMLInitializationEvent event)
    {
        try {
            HCSeeds.SEED_BLACKLIST.add(new ItemStack(HEMP_SEED));
        } catch (NullPointerException ignore) {}
    }
}
