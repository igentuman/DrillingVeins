package igentuman.dveins.network;

import igentuman.dveins.DVeins;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;

public class PacketUpdateItemStack implements IMessage {
    private int windowID;
    private int stackIndex;
    private ItemStack stack = ItemStack.EMPTY;

    public PacketUpdateItemStack() {
    }

    public PacketUpdateItemStack(Container container, int stackIndex, ItemStack stack) {
        this.windowID = container.windowId;
        this.stackIndex = stackIndex;
        this.stack = stack;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer b = new PacketBuffer(buf);
        windowID = b.readInt();
        stackIndex = b.readInt();
        try {
            stack = b.readItemStack();
        } catch (IOException e) {
            stack = ItemStack.EMPTY;
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer b = new PacketBuffer(buf);
        b.writeInt(windowID);
        b.writeInt(stackIndex);
        b.writeItemStack(stack);
    }

    public int getWindowID() {
        return windowID;
    }

    public int getStackIndex() {
        return stackIndex;
    }

    public ItemStack getStack() {
        return stack;
    }

    public String toString() {
        return String.format("{window: %d, idx: %d, item: %s}", windowID, stackIndex, stack);
    }

    public static class Handler implements IMessageHandler<PacketUpdateItemStack, IMessage> {

        @Override
        public IMessage onMessage(PacketUpdateItemStack message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                 DVeins.proxy.handleUpdateItemStack(message, ctx);
            });
            return null;
        }
    }

    public interface IUpdateNonSlotItemStack {
        void updateItem(int idx, ItemStack stack);
    }
}