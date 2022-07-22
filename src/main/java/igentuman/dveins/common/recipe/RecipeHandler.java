package igentuman.dveins.common.recipe;

import igentuman.dveins.ModConfig;
import igentuman.dveins.RegistryHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import static net.minecraftforge.oredict.OreDictionary.EMPTY_LIST;

public class RecipeHandler {

    public static ItemStack getStackFromOreDict(String oredict)
    {
        NonNullList<ItemStack> ent = OreDictionary.getOres(oredict);
        if(ent.equals(EMPTY_LIST)) {
            return null;
        }
        return ent.get(0);
    }

    public static void register()
    {
        registerSmelting();
    }

    public static void registerSmelting() {
        String prefix = ModConfig.drilling.chunk_smelting_product_type;
        addChunkSmeltingForOre(RegistryHandler.CHUNK_IRON, prefix+"Iron");
        addChunkSmeltingForOre(RegistryHandler.CHUNK_COPPER, prefix+"Copper");
        addChunkSmeltingForOre(RegistryHandler.CHUNK_TIN, prefix+"Tin");
        addChunkSmeltingForOre(RegistryHandler.CHUNK_LEAD, prefix+"Lead");
        addChunkSmeltingForOre(RegistryHandler.CHUNK_GOLD, prefix+"Gold");
    }

    private static void addChunkSmeltingForOre(Item chunk, String name)
    {
        int i = ModConfig.drilling.chunk_smelting_product_qty;
        ItemStack out =  getStackFromOreDict(name);
        if(out == null) return;
        out.setCount(i);
        GameRegistry.addSmelting(new ItemStack(chunk, 1), out, 1.0f);
    }
}