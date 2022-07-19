package igentuman.dveins.common.container;

import igentuman.dveins.DVeins;
import igentuman.dveins.common.tile.TileDrillBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;


public class ContainerDrill extends Container {
    private final TileDrillBase drill;

    public int progress;
    public ItemStack ore;

    public ContainerDrill(TileDrillBase drill) {
        this.drill = drill;
       // this.ore = drill.getOre();
    }
    public int getKineticEnergy()
    {
        return drill.kineticEnergy;
    }
    @Override
    public boolean canInteractWith(@NotNull EntityPlayer playerIn) {
        return false;
    }
}
