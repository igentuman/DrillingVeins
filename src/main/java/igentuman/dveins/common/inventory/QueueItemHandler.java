package igentuman.dveins.common.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.LinkedList;

public class QueueItemHandler implements IItemHandler, INBTSerializable<NBTTagList> {
    private LinkedList<ItemStack> queue = new LinkedList<>();

    @Override
    public int getSlots() {
        return queue.size();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return queue.get(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if(stack.isEmpty()) return ItemStack.EMPTY;

        if (!simulate) queue.add(stack);

        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (queue.isEmpty()) return ItemStack.EMPTY;

        if (simulate)
            return ItemHandlerHelper.copyStackWithSize(queue.peek(), Math.min(amount, queue.peek().getCount()));

        ItemStack stack = queue.peek();

        if (amount >= stack.getCount()) {
            return queue.poll();
        }
        else {
            stack.shrink(amount);
            return ItemHandlerHelper.copyStackWithSize(stack, amount);
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    public void push(ItemStack stack) {
        queue.add(stack);
    }

    @Nonnull
    public ItemStack peek() {
        return queue.isEmpty() ? ItemStack.EMPTY : queue.peek();
    }

    @Nonnull
    public ItemStack pop() {
        return queue.isEmpty() ? ItemStack.EMPTY : queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public NBTTagList serializeNBT() {
        NBTTagList list = new NBTTagList();
        for (ItemStack stack: queue) {
            list.appendTag(stack.serializeNBT());
        }
        return list;
    }

    @Override
    public void deserializeNBT(NBTTagList nbt) {
        queue.clear();
        nbt.forEach(tag->queue.add(new ItemStack((NBTTagCompound) tag)));
    }

    public void replaceHead(ItemStack stack) {
        queue.removeFirst();
        queue.addFirst(stack);
    }
}
