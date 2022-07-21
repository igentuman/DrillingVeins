
package igentuman.dveins.common.block;

import igentuman.dveins.DVeins;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;

public class BlockDrillHeadEmerald extends DrillHead {

    public BlockDrillHeadEmerald() {
        super(Material.IRON);
        this.setHardness(4.5f);
        this.setResistance(20.5f);
        this.setTranslationKey("drill_emerald_head");
        this.setRegistryName(DVeins.MODID, "drill_emerald_head");
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

}
