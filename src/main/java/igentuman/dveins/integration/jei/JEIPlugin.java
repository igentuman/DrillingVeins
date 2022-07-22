package igentuman.dveins.integration.jei;

import igentuman.dveins.RegistryHandler;
import igentuman.dveins.common.recipe.ForgeHammerRecipe;
import igentuman.dveins.common.recipe.RecipeHandler;
import igentuman.dveins.util.ItemHelper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

import static igentuman.dveins.DVeins.MODID;

@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin {
    public JEIPlugin() {
        super();
    }

    @Override
    public void register(IModRegistry registry) {
        registry.handleRecipes(ForgeHammerRecipe.class, ForgeHammerRecipeCategory.Wrapper::new, MODID+"_forgehammer");
        registry.addRecipeCatalyst(new ItemStack(RegistryHandler.FORGE_HAMMER),MODID+"_forgehammer");
        registry.addRecipes(RecipeHandler.FORGE_HAMMER.getAll(), MODID+"_forgehammer");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new ForgeHammerRecipeCategory(guiHelper));
    }

}
