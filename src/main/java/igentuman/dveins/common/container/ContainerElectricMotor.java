package igentuman.dveins.common.container;

import igentuman.dveins.common.tile.TileElectricMotor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import org.jetbrains.annotations.NotNull;

public class ContainerElectricMotor extends Container {
    private final TileElectricMotor motor;

    public ContainerElectricMotor(TileElectricMotor motor) {
        this.motor = motor;
    }

    public int getEnergyStored()
    {
        return motor.getEnergyStored();
    }


    public boolean isActive()
    {
        return motor.isActive();
    }


    @Override
    public boolean canInteractWith(@NotNull EntityPlayer playerIn) {
        return true;
    }

}
