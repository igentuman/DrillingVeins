package igentuman.dveins.common.item;


import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

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
