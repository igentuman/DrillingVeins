package igentuman.dveins.recipe;

import crafttweaker.annotations.ZenRegister;
import igentuman.dveins.recipe.ingridient.IFluidIngredient;
import igentuman.dveins.recipe.ingridient.IItemIngredient;
import igentuman.dveins.recipe.ingridient.IngredientSorption;
import igentuman.dveins.util.Tank;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import stanhebben.zenscript.annotations.ZenClass;

import java.util.List;


@ZenClass("mods.dveins.MechanismRecipe")
@ZenRegister
public class BasicRecipe implements IRecipe {
	
	protected List<IItemIngredient> itemIngredients, itemProducts;
	protected List<IFluidIngredient> fluidIngredients, fluidProducts;

	protected List<Object> extras;
	protected boolean isShapeless;
	
	public BasicRecipe(List<IItemIngredient> itemIngredients, List<IFluidIngredient> fluidIngredients, List<IItemIngredient> itemProducts, List<IFluidIngredient> fluidProducts, List<Object> extras, boolean shapeless) {
		this.itemIngredients = itemIngredients;
		this.fluidIngredients = fluidIngredients;
		this.itemProducts = itemProducts;
		this.fluidProducts = fluidProducts;

		this.extras = extras;
		isShapeless = shapeless;
	}
	
	@Override
	public List<IItemIngredient> getItemIngredients() {
		return itemIngredients;
	}

	@Override
	public List<IFluidIngredient> getFluidIngredients() {
		return fluidIngredients;
	}
	
	@Override
	public List<IItemIngredient> getItemProducts() {
		return itemProducts;
	}

	@Override
	public List<IFluidIngredient> getFluidProducts() {
		return fluidProducts;
	}
	
	@Override
	public List<Object> getExtras() {
		return extras;
	}
	
	public boolean isShapeless() {
		return isShapeless;
	}
	
	@Override
	public RecipeMatchResult matchInputs(List<ItemStack> itemInputs, List<Tank> fluidInputs) {
		return RecipeHelper.matchIngredients(IngredientSorption.INPUT, itemIngredients, fluidIngredients, itemInputs, fluidInputs, isShapeless);
	}
	
	@Override
	public RecipeMatchResult matchOutputs(List<ItemStack> itemOutputs, List<Tank> fluidOutputs) {
		return RecipeHelper.matchIngredients(IngredientSorption.OUTPUT, itemProducts, fluidProducts, itemOutputs, fluidOutputs, isShapeless);
	}
	
	@Override
	public RecipeMatchResult matchIngredients(List<IItemIngredient> itemIngredientsIn, List<IFluidIngredient> fluidIngredientsIn) {
		return RecipeHelper.matchIngredients(IngredientSorption.INPUT, itemIngredients, fluidIngredients, itemIngredientsIn, fluidIngredientsIn, isShapeless);
	}
	
	@Override
	public RecipeMatchResult matchProducts(List<IItemIngredient> itemProductsIn, List<IFluidIngredient> fluidProductsIn) {
		return RecipeHelper.matchIngredients(IngredientSorption.OUTPUT, itemProducts, fluidProducts, itemProductsIn, fluidProductsIn, isShapeless);
	}
	
	// Recipe Extras
	
	// Processors
	
	public double getBaseProcessTime(double defaultProcessTime) {
		return (double) extras.get(0) * defaultProcessTime;
	}
	
	public double getBaseProcessPower(double defaultProcessPower) {
		return (double) extras.get(1) * defaultProcessPower;
	}
	
	public double getBaseProcessRadiation() {
		return (double) extras.get(2);
	}

}
