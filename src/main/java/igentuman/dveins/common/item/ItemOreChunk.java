package igentuman.dveins.common.item;


import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemOreChunk extends Item {

    public ItemOreChunk() {
        super();
    }

    public CreativeTabs getCreativeTab()
    {
        return CreativeTabs.MATERIALS;
    }

    public ItemOreChunk setItemName(String name)
    {
        setRegistryName(name);
        setTranslationKey(name);
        return this;
    }
}
