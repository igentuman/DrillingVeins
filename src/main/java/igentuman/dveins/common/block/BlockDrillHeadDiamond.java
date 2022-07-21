
package igentuman.dveins.common.block;

import igentuman.dveins.DVeins;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class BlockDrillHeadDiamond extends DrillHead {

    public BlockDrillHeadDiamond() {
        super(Material.IRON);
        this.setHardness(4.5f);
        this.setResistance(20.5f);
        this.setTranslationKey("drill_diamond_head");
        this.setRegistryName(DVeins.MODID, "drill_diamond_head");
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

}
