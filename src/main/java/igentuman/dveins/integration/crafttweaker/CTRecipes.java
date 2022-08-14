package igentuman.dveins.integration.crafttweaker;

import com.google.common.collect.Lists;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import igentuman.dveins.recipe.BasicRecipeHandler;
import igentuman.dveins.recipe.DveinsRecipes;
import igentuman.dveins.recipe.ForgeHammerRecipes;
import igentuman.dveins.recipe.ingridient.IngredientSorption;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

public class CTRecipes {
	@ZenClass("mods.dveins.ForgeHammer")
	@ZenRegister
	public static class ForgeHammer {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return DveinsRecipes.forgeHammerRecipes;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output) {
			CraftTweakerAPI.apply(new CTAddRecipe(getRecipeHandler(), Lists.newArrayList(input1, output)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new CTRemoveRecipe(getRecipeHandler(), IngredientSorption.INPUT, Lists.newArrayList(input1)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new CTRemoveRecipe(getRecipeHandler(), IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new CTClearRecipes(getRecipeHandler()));
		}
	}
}
