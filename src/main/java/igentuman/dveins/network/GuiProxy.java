package igentuman.dveins.network;

import igentuman.dveins.client.gui.GuiDrill;
import igentuman.dveins.client.gui.GuiElectricMotor;
import igentuman.dveins.client.gui.GuiForgeHammer;
import igentuman.dveins.common.container.ContainerDrill;
import igentuman.dveins.common.container.ContainerElectricMotor;
import igentuman.dveins.common.container.ContainerForgehammer;
import igentuman.dveins.common.tile.TileDrillBase;
import igentuman.dveins.common.tile.TileElectricMotor;
import igentuman.dveins.common.tile.TileForgeHammer;
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
        if (te instanceof TileDrillBase && id == 0) {
            return new ContainerDrill((TileDrillBase) te);
        }
        if (te instanceof TileElectricMotor && id == 1) {
            return new ContainerElectricMotor((TileElectricMotor) te);
        }
        if (te instanceof TileForgeHammer && id == 2) {
            return new ContainerForgehammer(player.inventory, (TileForgeHammer) te);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileDrillBase && id == 0) {
            return new GuiDrill(new ContainerDrill((TileDrillBase) te));
        }
        if (te instanceof TileElectricMotor && id == 1) {
            return new GuiElectricMotor(new ContainerElectricMotor((TileElectricMotor) te));
        }
        if (te instanceof TileForgeHammer && id == 2) {
            return new GuiForgeHammer(new ContainerForgehammer(player.inventory, (TileForgeHammer) te));
        }
        return null;
    }
}