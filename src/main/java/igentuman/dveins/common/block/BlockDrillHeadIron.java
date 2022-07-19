
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

public class BlockDrillHeadIron extends Block {

    public BlockDrillHeadIron() {
        super(Material.IRON);
        this.setHardness(3.5f);
        this.setResistance(17.5f);
        this.setTranslationKey("drill_iron_head");
        this.setRegistryName(DVeins.MODID, "drill_iron_head");
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

}
