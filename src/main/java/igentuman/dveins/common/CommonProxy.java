package igentuman.dveins.common;

import igentuman.dveins.DVeins;
import igentuman.dveins.DVeinsOreDictionary;
import igentuman.dveins.ISidedProxy;
import igentuman.dveins.network.TileProcessUpdatePacket;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy implements ISidedProxy {

    public void init(FMLInitializationEvent event)
    {
        DVeinsOreDictionary.register();
    }

    @Override
    public void handleProcessUpdatePacket(TileProcessUpdatePacket message, MessageContext ctx) {
        DVeins.instance.logger.error("Got PacketUpdateItemStack on wrong side!");
    }
}