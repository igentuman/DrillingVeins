package igentuman.dveins;

import igentuman.dveins.network.PacketUpdateItemStack;
import igentuman.dveins.network.TileProcessUpdatePacket;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface ISidedProxy {

    default void init(FMLInitializationEvent event) {
    }
    void handleUpdateItemStack(PacketUpdateItemStack message, MessageContext context);
    void handleProcessUpdatePacket(TileProcessUpdatePacket message, MessageContext context);

}