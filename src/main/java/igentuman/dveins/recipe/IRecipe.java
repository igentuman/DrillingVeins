package igentuman.dveins.recipe;

import crafttweaker.annotations.ZenRegister;
import igentuman.dveins.recipe.ingridient.IFluidIngredient;
import igentuman.dveins.recipe.ingridient.IItemIngredient;
import igentuman.dveins.util.Tank;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

@ZenClass("mods.dveins.IRecipe")
@ZenRegister
public interface IRecipe {
	
	public List<IItemIngredient> getItemIngredients();
	
	public List<IFluidIngredient> getFluidIngredients();
	
	public List<IItemIngredient> getItemProducts();
	
	public List<IFluidIngredient> getFluidProducts();
	
	@ZenMethod("getItemIngredient")
	@Optional.Method(modid = "crafttweaker")
	public default crafttweaker.api.item.IIngredient ctItemIngredient(int index) {
		return getItemIngredients().get(index).ct();
	}
	
	@ZenMethod("getFluidIngredient")
	@Optional.Method(modid = "crafttweaker")
	public default crafttweaker.api.item.IIngredient ctFluidIngredient(int index) {
		return getFluidIngredients().get(index).ct();
	}
	
	@ZenMethod("getItemProduct")
	@Optional.Method(modid = "crafttweaker")
	public default crafttweaker.api.item.IIngredient ctItemProduct(int index) {
		return getItemProducts().get(index).ct();
	}
	
	@ZenMethod("getFluidProduct")
	@Optional.Method(modid = "crafttweaker")
	public default crafttweaker.api.item.IIngredient ctFluidProduct(int index) {
		return getFluidProducts().get(index).ct();
	}
	
	@ZenMethod
	public List<Object> getExtras();
	
	public RecipeMatchResult matchInputs(List<ItemStack> itemInputs, List<Tank> fluidInputs);
	
	public RecipeMatchResult matchOutputs(List<ItemStack> itemOutputs, List<Tank> fluidOutputs);
	
	public RecipeMatchResult matchIngredients(List<IItemIngredient> itemIngredientsIn, List<IFluidIngredient> fluidIngredientsIn);
	
	public RecipeMatchResult matchProducts(List<IItemIngredient> itemProductsIn, List<IFluidIngredient> fluidProductsIn);
}
