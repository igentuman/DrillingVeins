package igentuman.dveins.recipe.ingridient;

import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface IFluidIngredient extends IIngredient<FluidStack> {
	
	@Override
	public default FluidStack getNextStack(int ingredientNumber) {
		FluidStack nextStack = getStack();
		nextStack.amount = getNextStackSize(ingredientNumber);
		return nextStack;
	}
	
	@Override
	public default List<FluidStack> getInputStackHashingList() {
		return getInputStackList();
	}
	
	@Override
	public IFluidIngredient getFactoredIngredient(int factor);
}
