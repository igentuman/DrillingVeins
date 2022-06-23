package igentuman.dveins.common;

import igentuman.dveins.DVeins;
import igentuman.dveins.ISidedProxy;
import igentuman.dveins.network.PacketUpdateItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy implements ISidedProxy {
    
    @Override
    public void handleUpdateItemStack(PacketUpdateItemStack message, MessageContext ctx) {
        DVeins.instance.logger.error("Got PacketUpdateItemStack on wrong side!");
    }
    
}