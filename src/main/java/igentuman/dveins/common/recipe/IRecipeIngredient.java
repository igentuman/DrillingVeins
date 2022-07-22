package igentuman.dveins.common.recipe;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Predicate;

public interface IRecipeIngredient extends Predicate<Object> {
    static IRecipeIngredient of(ItemStack stack)
    {
        return new IngredientItemStack(stack);
    }
    @Nonnull
    String getName();

    @Nonnull
    List<ItemStack> getStacks();

    @Override
    boolean test(Object input);

    default boolean testIgnoreCount(Object input)
    {
        return test(input);
    }

    boolean matches(IRecipeIngredient other);
}
