package igentuman.dveins.recipe.ingridient;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraftforge.fml.common.Optional;

import java.util.List;

public interface IIngredient<T> {
	
	public default void init() {
		
	}
	
	public T getStack();
	
	public T getNextStack(int ingredientNumber);
	
	public List<T> getInputStackList();
	
	public List<T> getInputStackHashingList();
	
	public List<T> getOutputStackList();
	
	public int getMaxStackSize(int ingredientNumber);
	
	public void setMaxStackSize(int stackSize);
	
	public default int getNextStackSize(int ingredientNumber) {
		return getMaxStackSize(ingredientNumber);
	}
	
	public String getIngredientName();
	
	public String getIngredientNamesConcat();
	
	public IntList getFactors();
	
	public IIngredient<T> getFactoredIngredient(int factor);
	
	public IngredientMatchResult match(Object object, IngredientSorption sorption);
	
	public boolean isValid();
	
	// CraftTweaker
	
	@Optional.Method(modid = "crafttweaker")
	public crafttweaker.api.item.IIngredient ct();
}
