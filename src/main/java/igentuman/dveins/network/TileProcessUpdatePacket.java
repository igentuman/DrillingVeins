package igentuman.dveins.network;

import igentuman.dveins.DVeins;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class TileProcessUpdatePacket implements IMessage {
    public double progress;
    public double requiredProgress;
    public int energyStored;
    public double baseProcessTime;
    public double baseProcessPower;
    public BlockPos pos;

    public TileProcessUpdatePacket() {
    }

    public TileProcessUpdatePacket(BlockPos pos, double requiredProgress, double progress) {
        this.pos = pos;
        this.requiredProgress = requiredProgress;
        this.progress = progress;
    }

    public void fromBytes(ByteBuf buf) {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.requiredProgress = buf.readDouble();
        this.progress = buf.readDouble();
        this.energyStored = buf.readInt();
        this.baseProcessTime = buf.readDouble();
        this.baseProcessPower = buf.readDouble();

    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
        buf.writeDouble(this.requiredProgress);
        buf.writeDouble(this.progress);

        buf.writeInt(this.energyStored);
        buf.writeDouble(this.baseProcessTime);
        buf.writeDouble(this.baseProcessPower);

    }

    public static class Handler implements IMessageHandler<TileProcessUpdatePacket, IMessage> {

        @Override
        public IMessage onMessage(TileProcessUpdatePacket message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                DVeins.proxy.handleProcessUpdatePacket(message, ctx);
            });
            return null;
        }
    }
}
