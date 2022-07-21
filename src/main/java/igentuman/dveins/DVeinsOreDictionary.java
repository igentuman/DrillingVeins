package igentuman.dveins;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class DVeinsOreDictionary {
    public static void register()
    {
        OreDictionary.registerOre("oreCoalHardened",new ItemStack(RegistryHandler.COAL_ORE));
        OreDictionary.registerOre("oreRedstoneHardened",new ItemStack(RegistryHandler.REDSTONE_ORE));
        OreDictionary.registerOre("oreIronHardened",new ItemStack(RegistryHandler.IRON_ORE));
        OreDictionary.registerOre("oreCopperHardened",new ItemStack(RegistryHandler.COPPER_ORE));
        OreDictionary.registerOre("oreTinHardened",new ItemStack(RegistryHandler.TIN_ORE));
        OreDictionary.registerOre("oreLeadHardened",new ItemStack(RegistryHandler.LEAD_ORE));
        OreDictionary.registerOre("oreGoldHardened",new ItemStack(RegistryHandler.GOLD_ORE));

        OreDictionary.registerOre("chunkIron",new ItemStack(RegistryHandler.CHUNK_IRON));
        OreDictionary.registerOre("chunkCopper",new ItemStack(RegistryHandler.CHUNK_COPPER));
        OreDictionary.registerOre("chunkLead",new ItemStack(RegistryHandler.CHUNK_LEAD));
        OreDictionary.registerOre("chunkTin",new ItemStack(RegistryHandler.CHUNK_TIN));
        OreDictionary.registerOre("chunkGold",new ItemStack(RegistryHandler.CHUNK_GOLD));

    }
}
