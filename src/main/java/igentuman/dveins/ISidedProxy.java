package igentuman.dveins;

import igentuman.dveins.network.PacketUpdateItemStack;
import igentuman.dveins.network.TileProcessUpdatePacket;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface ISidedProxy {
    
    public void handleUpdateItemStack(PacketUpdateItemStack message, MessageContext context);
    void handleProcessUpdatePacket(TileProcessUpdatePacket message, MessageContext context);

}