package igentuman.dveins.common.block;

import igentuman.dveins.common.tile.TileDrillBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public abstract class DrillHead extends Block {

    public static enum  EnumRotation implements IStringSerializable {
        ONE("1"),
        TWO("2"),
        THREE("3"),
        FOUR("4");

        private final String name;

        private EnumRotation(String name)
        {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public DrillHead(Material materialIn) {
        super(materialIn);
    }

    public static final PropertyEnum<EnumRotation> rotation = PropertyEnum.create("rotation",EnumRotation.class);


    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{rotation});
    }

    public int getMetaFromState(IBlockState state) {
        return ((EnumRotation)state.getValue(rotation)).ordinal();
    }

    public IBlockState getStateFromMeta(int meta) {
        EnumRotation[] rotations = EnumRotation.values();
        return this.getDefaultState().withProperty(rotation, rotations[meta % rotations.length]);
    }

    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing face, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(rotation, EnumRotation.ONE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand)
    {
        TileEntity te = world.getTileEntity(pos.up());
        if(te instanceof TileDrillBase && ((TileDrillBase) te).isActive()) {
            double x = pos.getX();
            double y = pos.getY();
            double z = pos.getZ();

            world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, x + 0.5D, y, z + 0.5D, 0, 0.1, 0, 1);
            world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, x + 0.5D, y + 0.9D, z + 0.2D, 0, 0.1, 0, 1);
            world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, x + 0.5D, y + 0.9D, z + 0.8D, 0, 0.1, 0, 1);
            world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, x + 0.5D, y + 0.9D, z + 0.2D, 0, 0.1, 0, 49);
            world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, x + 0.5D, y + 0.9D, z + 0.8D, 0, 0.1, 0, 49);
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.9D, y + 0.5D, z + 0.5D, 0.02, 0, 0);
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.1D, y + 0.5D, z + 0.5D, -0.02, 0, 0);
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.5D, y + 0.5D, z + 0.9D, 0, 0, 0.02);
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + 0.5D, y + 0.5D, z + 0.1D, 0, 0, -0.02);
        }
    }
}
