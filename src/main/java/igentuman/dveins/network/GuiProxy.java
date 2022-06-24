package igentuman.dveins.network;

import igentuman.dveins.client.gui.GuiDrill;
import igentuman.dveins.common.container.ContainerDrill;
import igentuman.dveins.common.tile.TileDrillBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileDrillBase) {
            return new ContainerDrill(player.inventory, (TileDrillBase) te);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileDrillBase) {
            TileDrillBase containerTileEntity = (TileDrillBase) te;
            return new GuiDrill(new ContainerDrill(player.inventory, containerTileEntity));
        }
        return null;
    }
}