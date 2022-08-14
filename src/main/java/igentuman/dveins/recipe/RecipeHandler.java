package igentuman.dveins.recipe;

import igentuman.dveins.ModConfig;
import igentuman.dveins.RegistryHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.minecraftforge.oredict.OreDictionary.EMPTY_LIST;

public class RecipeHandler extends BasicRecipeHandler{

    public static final RecipeManager<ForgeHammerRecipe> FORGE_HAMMER = new RecipeManager<>();

    public RecipeHandler(@NotNull String name, int itemInputSize, int fluidInputSize, int itemOutputSize, int fluidOutputSize) {
        super(name, itemInputSize, fluidInputSize, itemOutputSize, fluidOutputSize);
    }

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
        new RecipeHandler("forge_hammer", 1, 0, 1, 0);
    }


    public static void registerForgeHammer()
    {
        addForgeHammerRecipeForOre("ingotIron", 1,"plateIron", 1);
        addForgeHammerRecipeForOre("ingotCopper", 1,"plateCopper", 1);
        addForgeHammerRecipeForOre("ingotTin", 1,"plateTin", 1);
        addForgeHammerRecipeForOre("ingotLead", 1,"plateLead", 1);
        addForgeHammerRecipeForOre("ingotBronze", 1,"plateBronze", 1);
        addForgeHammerRecipeForOre("ingotGold", 1,"plateGold", 1);

        addForgeHammerRecipeForOre("plateGold", 1,"casingGold", 2);
        addForgeHammerRecipeForOre("plateIron", 1,"casingIron", 2);
        addForgeHammerRecipeForOre("plateTin", 1,"casingTin", 2);
        addForgeHammerRecipeForOre("plateCopper", 1,"casingCopper", 2);
        addForgeHammerRecipeForOre("plateBronze", 1,"casingBronze", 2);

        addForgeHammerRecipeForOre("chunkIron", 1,"chunkDustIron", 2);
        addForgeHammerRecipeForOre("chunkTin", 1,"chunkDustTin", 2);
        addForgeHammerRecipeForOre("chunkGold", 1,"chunkDustGold", 2);
        addForgeHammerRecipeForOre("chunkCopper", 1,"chunkDustCopper", 2);
        addForgeHammerRecipeForOre("chunkLead", 1,"chunkDustLead", 2);
    }

    private static void addForgeHammerRecipeForOre(String input, int inputQty, String output, int outputQty)
    {
        ItemStack in =  getStackFromOreDict(input);
        ItemStack out =  getStackFromOreDict(output);
        if(out == null || in == null) return;
        in.setCount(inputQty);
        out.setCount(outputQty);
        FORGE_HAMMER.add(new ForgeHammerRecipe(
                in,
                out
        ));
    }

    public void registerSmelting() {
        String prefix = ModConfig.drilling.chunk_smelting_product_type;
        addChunkSmeltingForOre(RegistryHandler.CHUNK_IRON, prefix+"Iron");
        addChunkSmeltingForOre(RegistryHandler.CHUNK_COPPER, prefix+"Copper");
        addChunkSmeltingForOre(RegistryHandler.CHUNK_TIN, prefix+"Tin");
        addChunkSmeltingForOre(RegistryHandler.CHUNK_LEAD, prefix+"Lead");
        addChunkSmeltingForOre(RegistryHandler.CHUNK_GOLD, prefix+"Gold");
    }

    private void addChunkSmeltingForOre(Item chunk, String name)
    {
        int i = ModConfig.drilling.chunk_smelting_product_qty;
        ItemStack out =  getStackFromOreDict(name);
        if(out == null) return;
        out.setCount(i);
        GameRegistry.addSmelting(new ItemStack(chunk, 1), out, 1.0f);
    }

    @Override
    public void addRecipes() {
        registerSmelting();
        registerForgeHammer();
    }

    @Override
    public List<Object> fixExtras(List<Object> extras) {
        return null;
    }
}