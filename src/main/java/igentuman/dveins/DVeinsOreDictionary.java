package igentuman.dveins;

import net.minecraftforge.oredict.OreDictionary;

public class DVeinsOreDictionary {
    public static void registerOres()
    {
        OreDictionary.registerOre("oreCoalHardened", RegistryHandler.COAL_ORE);
        OreDictionary.registerOre("oreRedstoneHardened", RegistryHandler.REDSTONE_ORE);
        OreDictionary.registerOre("oreIronHardened", RegistryHandler.IRON_ORE);
        OreDictionary.registerOre("oreCopperHardened", RegistryHandler.COPPER_ORE);
        OreDictionary.registerOre("oreTinHardened", RegistryHandler.TIN_ORE);
        OreDictionary.registerOre("oreLeadHardened", RegistryHandler.LEAD_ORE);
        OreDictionary.registerOre("oreGoldHardened", RegistryHandler.GOLD_ORE);


    }

    public static void registerChunks()
    {
        OreDictionary.registerOre("chunkIron", RegistryHandler.CHUNK_IRON);
        OreDictionary.registerOre("chunkCopper", RegistryHandler.CHUNK_COPPER);
        OreDictionary.registerOre("chunkLead", RegistryHandler.CHUNK_LEAD);
        OreDictionary.registerOre("chunkTin", RegistryHandler.CHUNK_TIN);
        OreDictionary.registerOre("chunkGold", RegistryHandler.CHUNK_GOLD);
    }
}
