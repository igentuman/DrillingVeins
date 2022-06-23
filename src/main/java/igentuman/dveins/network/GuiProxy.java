package igentuman.dveins.network;


import igentuman.evtweaks.container.ContainerMechanicalForgehammer;
import igentuman.evtweaks.gui.GuiMechanicalForgeHammer;
import igentuman.evtweaks.tile.TileEntityMechanicalForgeHammer;
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
        if (te instanceof TileEntityMechanicalForgeHammer) {
            return new ContainerMechanicalForgehammer(player.inventory, (TileEntityMechanicalForgeHammer) te);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityMechanicalForgeHammer) {
            TileEntityMechanicalForgeHammer containerTileEntity = (TileEntityMechanicalForgeHammer) te;
            return new GuiMechanicalForgeHammer(new ContainerMechanicalForgehammer(player.inventory, containerTileEntity));
        }
        return null;
    }
}