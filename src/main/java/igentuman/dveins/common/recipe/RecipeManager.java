package igentuman.dveins.common.recipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecipeManager<T extends RecipeCore> {
    private final List<T> recipes;

    public RecipeManager()
    {
        recipes = new ArrayList<>();
    }

    public RecipeManager(int size)
    {
        recipes = new ArrayList<>(size);
    }

    public void add(T recipe)
    {
        recipes.add(recipe);
    }

    @Nullable
    @Deprecated
    public T get(Object... inputs)
    {
        return recipes.stream().filter(x -> x.test(inputs)).findFirst().orElse(null);
    }

    @Nullable
    public T get(Object input)
    {
        return recipes.stream().filter(x -> x.test(input)).findFirst().orElse(null);
    }

    @Nonnull
    public List<T> getAll()
    {
        return Collections.unmodifiableList(recipes);
    }

    @Deprecated
    public void remove(Object... inputs)
    {
        recipes.removeIf(x -> x.matches(inputs));
    }

    public void remove(Object input)
    {
        recipes.removeIf(x -> x.matches(input));
    }
}
