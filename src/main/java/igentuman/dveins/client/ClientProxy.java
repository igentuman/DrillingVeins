package igentuman.dveins.client;

import igentuman.dveins.ISidedProxy;
import igentuman.dveins.client.sound.SoundHandler;
import igentuman.dveins.common.tile.TileDrillBase;
import igentuman.dveins.network.TileProcessUpdatePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy implements ISidedProxy {


    public void init(FMLInitializationEvent e)
    {
        MinecraftForge.EVENT_BUS.register(SoundHandler.class);
    }

    @Override
    public void handleProcessUpdatePacket(TileProcessUpdatePacket message, MessageContext ctx) {
        TileEntity te = Minecraft.getMinecraft().world.getTileEntity(message.pos);
        if(te instanceof TileDrillBase) {
            ((TileDrillBase) te).onTileUpdatePacket(message);
        }
    }
}