package igentuman.dveins.common.capability;

import mysticalmechanics.api.DefaultMechCapability;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.EnumMap;

public class InputMechCapability extends DefaultMechCapability implements INBTSerializable<NBTTagCompound> {
    private EnumMap<EnumFacing, Double> powerMap = new EnumMap<>(EnumFacing.class);

    @Override
    public boolean isOutput(EnumFacing from) {
        return false;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        for (EnumFacing face: EnumFacing.values()) {
            tag.setDouble(face.getName(), powerMap.getOrDefault(face, 0.0));
        }
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        for (EnumFacing face: EnumFacing.values()) {
            powerMap.put(face, nbt.getDouble(face.getName()));
        }
        this.power = powerMap.values().stream().mapToDouble(d->d).sum();
    }

    @Override
    public void setPower(double value, EnumFacing from) {
        powerMap.put(from, value);
        double newPower = powerMap.values().stream().mapToDouble(d->d).sum();
        super.setPower(newPower, null);
    }

    @Override
    public double getPower(EnumFacing from) {
        return from == null ? this.power : powerMap.getOrDefault(from, 0.0);
    }
}
