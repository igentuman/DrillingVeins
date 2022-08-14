package igentuman.dveins.recipe.ingridient;

import igentuman.dveins.integration.crafttweaker.ingredient.CTChanceItemIngredient;
import igentuman.dveins.util.NCMath;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.Optional;

import java.util.ArrayList;
import java.util.List;

public class ChanceItemIngredient implements IChanceItemIngredient {
	
	// ONLY USED AS AN OUTPUT, SO INGREDIENT NUMBER DOES NOT MATTER!
	
	public IItemIngredient ingredient;
	public int chancePercent;
	public int minStackSize;
	public double meanStackSize;
	
	public ChanceItemIngredient(IItemIngredient ingredient, int chancePercent) {
		this(ingredient, chancePercent, 0);
	}
	
	public ChanceItemIngredient(IItemIngredient ingredient, int chancePercent, int minStackSize) {
		this.ingredient = ingredient;
		this.chancePercent = MathHelper.clamp(chancePercent, 0, 100);
		this.minStackSize = MathHelper.clamp(minStackSize, 0, ingredient.getMaxStackSize(0));
		
		meanStackSize = this.minStackSize + (double) (this.ingredient.getMaxStackSize(0) - this.minStackSize) * (double) this.chancePercent / 100D;
	}
	
	@Override
	public ItemStack getStack() {
		return ingredient.getStack();
	}
	
	@Override
	public List<ItemStack> getInputStackList() {
		List<ItemStack> stackList = new ArrayList<>();
		for (ItemStack stack : ingredient.getInputStackList()) {
			for (int i = minStackSize; i <= getMaxStackSize(0); ++i) {
				if (i <= 0) {
					// stackList.add(null);
				}
				else {
					ItemStack newStack = stack.copy();
					newStack.setCount(i);
					stackList.add(newStack);
				}
			}
		}
		return stackList;
	}
	
	@Override
	public List<ItemStack> getOutputStackList() {
		List<ItemStack> stackList = new ArrayList<>();
		for (int i = minStackSize; i <= getMaxStackSize(0); ++i) {
			if (i == 0) {
				if (false) {//@TODO read from config
					stackList.add(null);
				}
			}
			else {
				ItemStack newStack = getStack().copy();
				newStack.setCount(i);
				stackList.add(newStack);
			}
		}
		return stackList;
	}
	
	@Override
	public int getMaxStackSize(int ingredientNumber) {
		return ingredient.getMaxStackSize(0);
	}
	
	@Override
	public void setMaxStackSize(int stackSize) {
		ingredient.setMaxStackSize(stackSize);
	}
	
	@Override
	public int getNextStackSize(int ingredientNumber) {
		return minStackSize + NCMath.getBinomial(getMaxStackSize(0) - minStackSize, chancePercent);
	}
	
	@Override
	public String getIngredientName() {
		return ingredient.getIngredientName() + " [ " + chancePercent + "%, min: " + minStackSize + " ]";
	}
	
	@Override
	public String getIngredientNamesConcat() {
		return ingredient.getIngredientNamesConcat() + " [ " + chancePercent + "%, min: " + minStackSize + " ]";
	}
	
	@Override
	public IntList getFactors() {
		IntList list = ingredient.getFactors();
		list.add(minStackSize);
		return new IntArrayList(list);
	}
	
	@Override
	public IItemIngredient getFactoredIngredient(int factor) {
		return new ChanceItemIngredient(ingredient.getFactoredIngredient(factor), chancePercent, minStackSize / factor);
	}
	
	@Override
	public IngredientMatchResult match(Object object, IngredientSorption sorption) {
		return ingredient.match(object, sorption);
	}
	
	@Override
	public boolean isValid() {
		return ingredient.isValid();
	}
	
	// IChanceItemIngredient
	
	@Override
	public IItemIngredient getRawIngredient() {
		return ingredient;
	}
	
	@Override
	public int getChancePercent() {
		return chancePercent;
	}
	
	@Override
	public int getMinStackSize() {
		return minStackSize;
	}
	
	@Override
	public double getMeanStackSize() {
		return meanStackSize;
	}
	
	// CraftTweaker
	
	@Override
	@Optional.Method(modid = "crafttweaker")
	public crafttweaker.api.item.IIngredient ct() {
		return CTChanceItemIngredient.create(ingredient.ct(), chancePercent, minStackSize);
	}
}
