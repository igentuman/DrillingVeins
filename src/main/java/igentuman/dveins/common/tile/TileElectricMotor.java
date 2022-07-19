package igentuman.dveins.common.tile;

import igentuman.dveins.network.ModPacketHandler;
import igentuman.dveins.network.TileProcessUpdatePacket;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import java.util.Arrays;

import static igentuman.dveins.ModConfig.electricMotor;

public class TileElectricMotor extends TileEntity implements ITickable, IEnergyStorage {

    private final EnergyStorage storage;

    public boolean isRedstonePowered() {
        return isRedstonePowered;
    }

    public boolean isWorking() {
        return isWorking;
    }

    private boolean isRedstonePowered = false;
    private boolean isWorking = false;

    public TileEntity getTopTe() {
        return topTe;
    }

    public TileEntity getBottomTe() {
        return bottomTe;
    }

    protected TileEntity topTe;
    protected TileEntity bottomTe;

    public TileElectricMotor() {
        this(electricMotor.rf_per_tick*20, electricMotor.rf_per_tick*20);
    }

    public TileElectricMotor(int capacity, int maxTransfer) {
        this.storage = new EnergyStorage(capacity, maxTransfer);
    }


    public EnergyStorage getEnergyStorage() {
        return this.storage;
    }

    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
        return capability == CapabilityEnergy.ENERGY;
    }

    boolean hasEnergySideCapability(@Nullable EnumFacing side) {
        return true;
    }

    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
        if (capability == CapabilityEnergy.ENERGY) {
            return this.hasEnergySideCapability(side) ? CapabilityEnergy.ENERGY.cast(storage) : null;
        }  else {
            return super.getCapability(capability, side);
        }
    }

    public void setEnergyStored(int amount)
    {
        storage.extractEnergy(getEnergyStored(), false);
        storage.receiveEnergy(amount, false);
    }

    @Override
    public void update() {

        topTe = world.getTileEntity(getPos().add(0,1,0));
        bottomTe = world.getTileEntity(getPos().add(0,-1,0));
        if(world.isRemote) return;
        if(world.getRedstonePowerFromNeighbors(getPos()) > 0) {
            if(isWorking) {
                isWorking = false;
                ModPacketHandler.instance.sendToAll(this.getTileUpdatePacket());

            }
            return;
        }
        if (getEnergyStored() < electricMotor.rf_per_tick) return;
        boolean wasWorking = isWorking;

        if(isWorking || (isWorking != wasWorking)) {
            if( !world.isRemote) {
                ModPacketHandler.instance.sendToAll(this.getTileUpdatePacket());
            }
            markDirty();
        }
    }

    public TileProcessUpdatePacket getTileUpdatePacket() {
        return new TileProcessUpdatePacket(
                this.pos,
                0,
                0,
                getEnergyStored()
        );
    }

    public void onTileUpdatePacket(TileProcessUpdatePacket message)
    {
        setEnergyStored(message.energyStored);
       // isWorking = message.isWorking;
        //isRedstonePowered = message.isRedstonePowered;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if(getEnergyStored() < getMaxEnergyStored()) {

            int received = storage.receiveEnergy(maxReceive, simulate);
            if( !world.isRemote) {
                ModPacketHandler.instance.sendToAll(this.getTileUpdatePacket());
            }
            return received;
        }
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return storage.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    @NotNull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("isWorking", isWorking);
        compound.setBoolean("isRedstonePowered", isRedstonePowered);
        compound.setInteger("energyStored", getEnergyStored());
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        isWorking = compound.getBoolean("isWorking");
        isRedstonePowered = compound.getBoolean("isRedstonePowered");
        setEnergyStored(compound.getInteger("energyStored"));
    }
}
