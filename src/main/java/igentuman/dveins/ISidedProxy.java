package igentuman.dveins;

import igentuman.dveins.network.PacketUpdateItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface ISidedProxy {
    
    public void handleUpdateItemStack(PacketUpdateItemStack message, MessageContext context);
    
}