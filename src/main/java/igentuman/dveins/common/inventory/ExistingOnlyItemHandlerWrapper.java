package igentuman.dveins.common.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class ExistingOnlyItemHandlerWrapper implements IItemHandler {
    private final IItemHandler wrapped;

    public ExistingOnlyItemHandlerWrapper(IItemHandler wraps) {
        this.wrapped = wraps;
    }

    @Override
    public int getSlots() {
        return wrapped.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return wrapped.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if(wrapped.getStackInSlot(slot).isEmpty()) {
            return stack;
        }

        int insertLimit = getSlotLimit(slot) - getStackInSlot(slot).getCount();
        int overflow = stack.getCount() - insertLimit;

        if(insertLimit <= 0) return stack;

        ItemStack insertStack = ItemHandlerHelper.copyStackWithSize(stack, insertLimit);
        insertStack = wrapped.insertItem(slot, insertStack, simulate);

        return ItemHandlerHelper.copyStackWithSize(stack, overflow + insertStack.getCount());
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return wrapped.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 2;
    }
}
