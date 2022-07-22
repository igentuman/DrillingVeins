package igentuman.dveins.common.recipe;

import igentuman.dveins.util.ItemHelper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class IngredientItemStack implements IRecipeIngredient {
    protected final ItemStack stack;
    private final List<ItemStack> stacks;

    IngredientItemStack(@Nonnull ItemStack stack)
    {
        this.stack = stack;
        stacks = Collections.singletonList(stack);
    }

    @Nonnull
    @Override
    public String getName()
    {
        return stack.getTranslationKey();
    }

    @Nonnull
    @Override
    public List<ItemStack> getStacks()
    {
        return stacks;
    }

    @Override
    public boolean test(Object obj)
    {
        return testIgnoreCount(obj) && ((ItemStack) obj).getCount() >= stack.getCount();
    }

    @Override
    public boolean testIgnoreCount(Object obj)
    {
        return obj instanceof ItemStack && ItemHelper.doStacksMatch(stack, (ItemStack) obj);
    }

    @Override
    public boolean matches(IRecipeIngredient other)
    {
        return other instanceof IngredientItemStack && ItemHelper.doStacksMatch(stack, ((IngredientItemStack) other).stack);
    }
}
