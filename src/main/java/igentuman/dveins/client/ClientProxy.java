package igentuman.dveins.client;

import igentuman.dveins.ISidedProxy;
import igentuman.dveins.common.tile.TileDrillBase;
import igentuman.dveins.network.PacketUpdateItemStack;
import igentuman.dveins.network.PacketUpdateItemStack.IUpdateNonSlotItemStack;
import igentuman.dveins.network.TileProcessUpdatePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy implements ISidedProxy {
    
    @Override
    public void handleUpdateItemStack(PacketUpdateItemStack message, MessageContext ctx) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (message.getWindowID() == player.openContainer.windowId
                && message.getWindowID() != 0
                && player.openContainer instanceof IUpdateNonSlotItemStack) {
            ((IUpdateNonSlotItemStack) player.openContainer).updateItem(message.getStackIndex(), message.getStack());
        }
    }
    @Override
    public void handleProcessUpdatePacket(TileProcessUpdatePacket message, MessageContext ctx) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        TileEntity te = Minecraft.getMinecraft().world.getTileEntity(message.pos);
        if(te instanceof TileDrillBase) {
            ((TileDrillBase) te).onTileUpdatePacket(message);
        }
    }
}