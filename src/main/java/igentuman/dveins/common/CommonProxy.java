package igentuman.dveins.common;

import igentuman.dveins.DVeins;
import igentuman.dveins.DVeinsOreDictionary;
import igentuman.dveins.ISidedProxy;
import igentuman.dveins.network.PacketUpdateItemStack;
import igentuman.dveins.network.TileProcessUpdatePacket;
import igentuman.dveins.recipe.DveinsRecipes;
import igentuman.dveins.util.OreDictHelper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLModIdMappingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy implements ISidedProxy {

    public void init(FMLInitializationEvent event)
    {
    }

    @Override
    public void handleUpdateItemStack(PacketUpdateItemStack message, MessageContext context) {

    }

    @Override
    public void handleProcessUpdatePacket(TileProcessUpdatePacket message, MessageContext ctx) {
        DVeins.instance.logger.error("Got PacketUpdateItemStack on wrong side!");
    }
}