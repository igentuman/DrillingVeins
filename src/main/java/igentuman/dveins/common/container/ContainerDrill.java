package igentuman.dveins.common.container;

import igentuman.dveins.common.tile.TileDrillBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;


public class ContainerDrill extends Container {
    private final TileDrillBase drill;

    public int progress;
    public ItemStack ore;

    public ContainerDrill(TileDrillBase drill) {
        this.drill = drill;
        this.ore = drill.getOre();
    }
    public int getKineticEnergy()
    {
        return drill.kineticEnergy;
    }

    public boolean hasDrillHead()
    {
        return drill.hasDrillHead();
    }

    public int getCurrentY()
    {
        return drill.getCurrentY();
    }

    public int getBlocksLeft()
    {
        return drill.blocksInVein();
    }
    @Override
    public boolean canInteractWith(@NotNull EntityPlayer playerIn) {
        return true;
    }
}
