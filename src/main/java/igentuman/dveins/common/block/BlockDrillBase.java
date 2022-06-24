
package igentuman.dveins.common.block;

import igentuman.dveins.DVeins;
import igentuman.dveins.common.tile.TileDrillBase;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static net.minecraft.inventory.InventoryHelper.spawnItemStack;
import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

public class BlockDrillBase extends BlockHorizontal {

    public BlockDrillBase() {
        super(Material.IRON);
        this.setHardness(3.5f);
        this.setResistance(17.5f);
        this.setTranslationKey("drill_base");
        this.setRegistryName(DVeins.MODID, "drill_base");
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    @NotNull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
    }

    @NotNull
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing face, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@NotNull World world, @NotNull IBlockState state) {
        return new TileDrillBase();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity te = worldIn.getTileEntity(pos);
        if(playerIn.getHeldItemMainhand().getItem().getRegistryName().toString().equals("immersiveengineering:tool")) {
            rotateBlock(worldIn,pos,facing.getOpposite());
            return false;
        }
        if(!(te instanceof TileDrillBase)) {
            return true;
        }

        if(worldIn.isRemote) {
            return true;
        }

        playerIn.openGui(DVeins.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    public void breakBlock(World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state)
    {
        TileEntity te = worldIn.getTileEntity(pos);

        if(te instanceof TileDrillBase) {
            TileDrillBase temw = (TileDrillBase) te;
            IItemHandler inventory = temw.getCapability(ITEM_HANDLER_CAPABILITY, null);
            assert inventory != null;
            dropInventoryItems(worldIn, pos.getX(), pos.getY(), pos.getZ(), inventory);
            dropInventoryItems(worldIn, pos.getX(), pos.getY(), pos.getZ(), temw.outputQueue);
        }

        super.breakBlock(worldIn, pos, state);
    }

    private static void dropInventoryItems(World worldIn, double x, double y, double z, IItemHandler inventory) {
        for (int i = 0; i < inventory.getSlots(); ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);

            if (!itemstack.isEmpty()) {
                spawnItemStack(worldIn, x, y, z, itemstack);
            }
        }
    }
}
