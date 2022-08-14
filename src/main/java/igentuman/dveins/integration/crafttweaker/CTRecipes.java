package igentuman.dveins.integration.crafttweaker;

import com.google.common.collect.Lists;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import igentuman.dveins.recipe.BasicRecipeHandler;
import igentuman.dveins.recipe.RecipeHandler;
import igentuman.dveins.recipe.ingridient.IngredientSorption;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

public class CTRecipes {
	/*@ZenClass("mods.dveins.ForgeHammer")
	@ZenRegister
	public static class ForgeHammer {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return RecipeHandler.FORGE_HAMMER;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new CTAddRecipe(NCRecipes.alloy_furnace, Lists.newArrayList(input1, input2, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new CTRemoveRecipe(NCRecipes.alloy_furnace, IngredientSorption.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new CTRemoveRecipe(NCRecipes.alloy_furnace, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new CTClearRecipes(NCRecipes.alloy_furnace));
		}
	}*/
}
