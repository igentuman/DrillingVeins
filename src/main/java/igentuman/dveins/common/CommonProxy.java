package igentuman.dveins.common;

import igentuman.dveins.DVeins;
import igentuman.dveins.ISidedProxy;
import igentuman.dveins.network.PacketUpdateItemStack;
import igentuman.dveins.network.TileProcessUpdatePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy implements ISidedProxy {
    
    @Override
    public void handleUpdateItemStack(PacketUpdateItemStack message, MessageContext ctx) {
        DVeins.instance.logger.error("Got PacketUpdateItemStack on wrong side!");
    }
    @Override
    public void handleProcessUpdatePacket(TileProcessUpdatePacket message, MessageContext ctx) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        DVeins.instance.logger.error("Got PacketUpdateItemStack on wrong side!");
    }
}