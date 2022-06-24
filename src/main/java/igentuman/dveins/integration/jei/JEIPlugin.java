package igentuman.dveins.integration.jei;

import igentuman.dveins.util.ItemHelper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

import static igentuman.dveins.DVeins.MODID;

@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin {
    public JEIPlugin() {
        super();
    }

    @Override
    public void register(IModRegistry registry) {
        //registry.handleRecipes(ForgeHammerRecipe.class, DrillRecipeCategory.Wrapper::new, MODID+"_drill");
        registry.addRecipeCatalyst(ItemHelper.getStackFromString("dveins:drill",0),MODID+"_drill");
        //registry.addRecipes(EvTweaksRecipes.FORGE_HAMMER.getAll(), MODID+"_drill");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new DrillRecipeCategory(guiHelper));
    }

}
