package igentuman.dveins.recipe;

import igentuman.dveins.ModConfig;
import igentuman.dveins.RegistryHandler;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

import static net.minecraftforge.oredict.OreDictionary.EMPTY_LIST;

public class DveinsRecipes {

    public static final Object2ObjectMap<String, BasicRecipeHandler> RECIPE_HANDLER_MAP = new Object2ObjectOpenHashMap<>();
    private static boolean initialized = false;
    public static ForgeHammerRecipes forgeHammerRecipes;
    public static BasicRecipeHandler getHandler(String name) {
        return RECIPE_HANDLER_MAP.get(name);
    }

    public static List<BasicRecipe> getRecipeList(String name) {
        return getHandler(name).recipeList;
    }

    public static List<List<String>> getValidFluids(String name) {
        return getHandler(name).validFluids;
    }

    public static ItemStack getStackFromOreDict(String oredict)
    {
        NonNullList<ItemStack> ent = OreDictionary.getOres(oredict);
        if(ent.equals(EMPTY_LIST)) {
            return null;
        }
        return ent.get(0);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void registerRecipes(RegistryEvent.Register<IRecipe> event)
    {
        if (initialized) {
            return;
        }
        forgeHammerRecipes = new ForgeHammerRecipes();
        RECIPE_HANDLER_MAP.put("forge_hammer", forgeHammerRecipes);
        registerSmelting();
        initialized = true;
    }

    public static void refreshRecipeCaches()
    {
        for (BasicRecipeHandler re: RECIPE_HANDLER_MAP.values()) {
            re.refreshCache();
        }
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
}