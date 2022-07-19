package igentuman.dveins.client;

import igentuman.dveins.ISidedProxy;
import igentuman.dveins.common.tile.TileDrillBase;
import igentuman.dveins.network.TileProcessUpdatePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy implements ISidedProxy {


    @Override
    public void handleProcessUpdatePacket(TileProcessUpdatePacket message, MessageContext ctx) {
        TileEntity te = Minecraft.getMinecraft().world.getTileEntity(message.pos);
        if(te instanceof TileDrillBase) {
            ((TileDrillBase) te).onTileUpdatePacket(message);
        }
    }
}