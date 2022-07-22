package igentuman.dveins.common.recipe;

import igentuman.dveins.util.ItemHelper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class RecipeCore {
    protected final IRecipeIngredient ingredient;
    protected final ItemStack outputStack;
    protected final int inputAmount;

    public RecipeCore(ItemStack outputStack, ItemStack inputStack)
    {
        this.outputStack = outputStack;
        this.inputAmount = inputStack.isEmpty() ? 0 : inputStack.getCount();
        this.ingredient = IRecipeIngredient.of(inputStack);
    }


    public boolean test(Object input)
    {
        return ingredient.test(input);
    }


    @Deprecated
    public boolean test(Object... inputs)
    {
        throw new UnsupportedOperationException("This recipe does not support access by multiple inputs");
    }


    public boolean matches(Object input)
    {
        return input instanceof IRecipeIngredient && ingredient.matches((IRecipeIngredient) input);
    }


    @Deprecated
    public boolean matches(Object... inputs)
    {
        throw new UnsupportedOperationException("This recipe does not support access by multiple inputs");
    }

    public ItemStack consumeInput(ItemStack stack)
    {
        return ItemHelper.consumeItem(stack, inputAmount);
    }

    @Nonnull
    public ItemStack getOutput()
    {
        return outputStack.copy();
    }


    public IRecipeIngredient getInput()
    {
        return ingredient;
    }


    public String getName()
    {
        return ingredient.getName();
    }
}
