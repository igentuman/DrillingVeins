
package igentuman.dveins.common.block;

import igentuman.dveins.DVeins;
import igentuman.dveins.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

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

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, @Nullable World world, List<String> currentTooltip, ITooltipFlag flag) {
        super.addInformation(itemStack, world, currentTooltip, flag);
        currentTooltip.add(TextFormatting.AQUA + I18n.format("description.diamond_drill_head", ModConfig.drilling.diamond_drill_head_multiplier));
    }
}
