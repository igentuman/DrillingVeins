package igentuman.dveins.common.tile;

import igentuman.dveins.DVeins;
import igentuman.dveins.ModConfig;
import igentuman.dveins.client.sound.SoundHandler;
import igentuman.dveins.network.ModPacketHandler;
import igentuman.dveins.network.TileProcessUpdatePacket;
import mysticalmechanics.api.DefaultMechCapability;
import mysticalmechanics.api.IMechCapability;
import mysticalmechanics.api.MysticalMechanicsAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import java.util.Arrays;
import java.util.EnumMap;

import static igentuman.dveins.ModConfig.electricMotor;
import static mysticalmechanics.api.MysticalMechanicsAPI.MECH_CAPABILITY;
public class TileElectricMotor extends TileEntity implements ITickable, IEnergyStorage {

    private final EnergyStorage storage;

    public boolean isRedstonePowered() {
        return isRedstonePowered;
    }

    public boolean isActive() {
        return activeFlag;
    }

    private boolean isRedstonePowered = false;
    private boolean activeFlag = false;
    private boolean wasWorking = false;
    private OutputMechCapability mechCapability;

    private SoundEvent soundEvent;
    private int playSoundCooldown = 0;
    private long lastActive = -1;
    private int rapidChangeThreshold = 10;
    @SideOnly(Side.CLIENT)
    private ISound activeSound;

    public TileElectricMotor() {
        this(electricMotor.rf_per_tick*20, electricMotor.rf_per_tick*20);
    }

    public TileElectricMotor(int capacity, int maxTransfer) {
        this.storage = new EnergyStorage(capacity, maxTransfer);
        this.mechCapability = new OutputMechCapability();
        soundEvent = new SoundEvent(new ResourceLocation(DVeins.MODID, "active_motor"));

    }


    @SideOnly(Side.CLIENT)
    protected void setSoundEvent(SoundEvent event) {
        this.soundEvent = event;
        SoundHandler.stopTileSound(getPos());
    }

    @SideOnly(Side.CLIENT)
    private void updateSound() {
        if(soundEvent == null) return;
        if (isActive()) {
            if (--playSoundCooldown > 0) {
                return;
            }
            if ((activeSound == null || !Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(activeSound))) {
                activeSound = SoundHandler.startTileSound(soundEvent.getSoundName(), 0.9F, getPos());
            }
            playSoundCooldown = 20;
        } else {
            stopSound();
        }
    }


    @SideOnly(Side.CLIENT)
    protected void stopSound()
    {
        long downtime = world.getTotalWorldTime() - lastActive;
        if (activeSound != null && downtime > rapidChangeThreshold) {
            SoundHandler.stopTileSound(getPos());
            activeSound = null;
            playSoundCooldown = 0;
        }
    }

    public void invalidate()
    {
        super.invalidate();
        stopSound();
    }

    public EnergyStorage getEnergyStorage() {
        return this.storage;
    }

    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
        if (capability == MECH_CAPABILITY && side != null && side == EnumFacing.DOWN) {
            return true;
        }
        return capability == CapabilityEnergy.ENERGY && side != null && side != EnumFacing.DOWN;
    }

    boolean hasEnergySideCapability(@Nullable EnumFacing side) {
        return side != EnumFacing.DOWN;
    }

    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
        if (capability == MECH_CAPABILITY && side != null && side == EnumFacing.DOWN) {
            return (T) mechCapability;
        }
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
        if(amount == 0) {
            activeFlag = false;
        }
    }

    @Override
    public void update() {
        if(world.isRemote) {
            updateSound();
            return;
        }
        if(world.getRedstonePowerFromNeighbors(getPos()) > 0) {
            if(activeFlag) {
                activeFlag = false;
                wasWorking = activeFlag;
                mechCapability.setPower(0.0D, (EnumFacing) null);
                updateOutput();
                ModPacketHandler.instance.sendToAll(this.getTileUpdatePacket());

            }
            return;
        }
        if (getEnergyStored() < electricMotor.rf_per_tick) {

            mechCapability.setPower(0.0D, (EnumFacing) null);
            updateOutput();
            if(wasWorking) {
                wasWorking = false;
                ModPacketHandler.instance.sendToAll(this.getTileUpdatePacket());
            }
            return;
        }
        activeFlag = true;
        wasWorking = activeFlag;
        if( !world.isRemote) {
            mechCapability.setPower(electricMotor.kinetic_energy_per_tick, (EnumFacing)null);
            setEnergyStored(getEnergyStored() - electricMotor.rf_per_tick);
            updateOutput();
            ModPacketHandler.instance.sendToAll(this.getTileUpdatePacket());
        }
        markDirty();

    }

    public TileProcessUpdatePacket getTileUpdatePacket() {
        return new TileProcessUpdatePacket(
                this.pos,
                0,
                0,
                getEnergyStored(),
                activeFlag,
                isRedstonePowered
        );
    }

    public void onTileUpdatePacket(TileProcessUpdatePacket message)
    {
        activeFlag = message.activeFlag;
        setEnergyStored(message.energyStored);
        isRedstonePowered = message.isRedstonePowered;
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
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    @NotNull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("activeFlag", activeFlag);
        compound.setBoolean("isRedstonePowered", isRedstonePowered);
        compound.setInteger("energyStored", getEnergyStored());
        compound.setTag("mech", mechCapability.serializeNBT());
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        activeFlag = compound.getBoolean("activeFlag");
        isRedstonePowered = compound.getBoolean("isRedstonePowered");
        setEnergyStored(compound.getInteger("energyStored"));
        mechCapability.deserializeNBT(compound.getCompoundTag("mech"));
    }

    public void updateOutput() {
        TileEntity te = world.getTileEntity(getPos().down());
        if (te != null && te.hasCapability(MysticalMechanicsAPI.MECH_CAPABILITY, EnumFacing.UP)) {
            IMechCapability mech = (IMechCapability)te.getCapability(MysticalMechanicsAPI.MECH_CAPABILITY, EnumFacing.UP);
            if (mech.isInput(EnumFacing.UP)) {
                mech.setPower(this.mechCapability.getPower((EnumFacing)null), EnumFacing.UP);
            }
        }
    }

    class OutputMechCapability extends DefaultMechCapability  implements INBTSerializable<NBTTagCompound> {
        OutputMechCapability() {
        }
        private EnumMap<EnumFacing, Double> powerMap = new EnumMap<>(EnumFacing.class);
        public boolean isInput(EnumFacing from) {
            return false;
        }

        public boolean isOutput(EnumFacing from) {
            return true;
        }

        public void onPowerChange() {
            TileElectricMotor.this.updateOutput();
        }


        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            for (EnumFacing face: EnumFacing.values()) {
                powerMap.put(face, nbt.getDouble(face.getName()));
            }
            this.power = powerMap.values().stream().mapToDouble(d->d).sum();
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            for (EnumFacing face: EnumFacing.values()) {
                tag.setDouble(face.getName(), powerMap.getOrDefault(face, 0.0));
            }
            return tag;
        }
    }
}
