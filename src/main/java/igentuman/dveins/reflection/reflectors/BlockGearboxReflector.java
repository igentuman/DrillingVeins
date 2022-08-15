package igentuman.dveins.reflection.reflectors;

import mysticalmechanics.block.BlockGearbox;
import mysticalmechanics.tileentity.TileEntityGearbox;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockGearboxReflector {
    public static boolean rotateBlock(BlockGearbox instance, World world, BlockPos pos, EnumFacing side) {
        TileEntityGearbox tile = (TileEntityGearbox)world.getTileEntity(pos);
        //just to avoid nullpointerexception
        tile.updateNeighbors();
        //
        tile.rotateTile(world, pos, side);
        return true;
    }
}
