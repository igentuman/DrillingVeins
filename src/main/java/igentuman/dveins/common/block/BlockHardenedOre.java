package igentuman.dveins.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import static igentuman.dveins.DVeins.MODID;

public class BlockHardenedOre extends Block {

    public BlockHardenedOre() {
        super(Material.ROCK);
        this.setHardness(50f);
        this.setResistance(2000f);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public BlockHardenedOre setName(String name)
    {
        setTranslationKey(name);
        setRegistryName(MODID, name);
        return this;
    }

    public void breakBlock(World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
    }

}
