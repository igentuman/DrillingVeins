package igentuman.dveins.network;

import igentuman.dveins.DVeins;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class TileProcessUpdatePacket implements IMessage {
    public int kineticEnergy;
    public int energyStored;
    public BlockPos pos;
    public int currentY;


    public TileProcessUpdatePacket() {
    }

    public TileProcessUpdatePacket(BlockPos pos, int kineticEnergy, int currentY, int energyStored) {
        this.pos = pos;
        this.kineticEnergy = kineticEnergy;
        this.currentY = currentY;
        this.energyStored = energyStored;
    }

    public void fromBytes(ByteBuf buf) {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.kineticEnergy = buf.readInt();
        this.currentY = buf.readInt();
        this.energyStored = buf.readInt();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
        buf.writeInt(this.kineticEnergy);
        buf.writeInt(this.currentY);
        buf.writeInt(this.energyStored);
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
