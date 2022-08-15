package igentuman.dveins.common.tile;

import betterwithmods.api.tile.IMechanicalPower;
import igentuman.dveins.ModConfig;
import igentuman.dveins.common.block.MechanicalBlock;
import igentuman.dveins.common.capability.InputMechCapability;
import igentuman.dveins.util.ModCheck;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Optional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static mysticalmechanics.api.MysticalMechanicsAPI.MECH_CAPABILITY;

@Optional.Interface(iface = "betterwithmods.api.tile.IMechanicalPower", modid = "betterwithmods")
public class PowerBackend extends TileEntity implements ITickable, IMechanicalPower {
    public InputMechCapability mechCapability;
    @Override
    public void update() {

    }
    @Override
    public int getMechanicalOutput(EnumFacing enumFacing) {
        return 0;
    }

    public double getMechanicalInput() {
        double pow = 0;
        pow += getMysticalPower();
        pow += getBwmPower();
        return pow;
    }

    public double getMysticalPower()
    {
        double pow = 0;
        if(ModCheck.mysticalmechanicsLoaded()) {
            pow += mechCapability.power;
        }
        return pow;
    }

    public EnumFacing.Axis getAllowedAxis()
    {
       return findDirection().rotateY().getAxis();
    }

    public EnumFacing getMechanicalSide()
    {
        return findDirection().rotateY();
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {

        if (capability == MECH_CAPABILITY && facing != null && facing.getAxis().equals(getAllowedAxis())) {
            return true;
        }

        if(ModCheck.bwmLoaded()) {
            if(capability == betterwithmods.api.capabilities.CapabilityMechanicalPower.MECHANICAL_POWER && facing != null && facing.getAxis().equals(getAllowedAxis())) {
                return true;
            }
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == MECH_CAPABILITY && facing != null && facing.getAxis() == getAllowedAxis()) {
            return (T) mechCapability;
        }

        if(ModCheck.bwmLoaded()) {
            if(capability == betterwithmods.api.capabilities.CapabilityMechanicalPower.MECHANICAL_POWER && facing.getAxis().equals(getAllowedAxis())) {
                return betterwithmods.api.capabilities.CapabilityMechanicalPower.MECHANICAL_POWER.cast(this);
            }
        }
        return super.getCapability(capability, facing);
    }

    public EnumFacing findDirection() {
        IBlockState bs = world.getBlockState(pos);
        return bs.getBlock() instanceof MechanicalBlock ?
                bs.getValue(MechanicalBlock.FACING) : EnumFacing.DOWN;
    }

    public int getBwmPower()
    {
        int pow = 0;
        if(ModCheck.bwmLoaded()) {
            pow += betterwithmods.api.BWMAPI.IMPLEMENTATION.getPowerOutput(world, pos.offset(getMechanicalSide(),1), getMechanicalSide().getOpposite());
            pow += betterwithmods.api.BWMAPI.IMPLEMENTATION.getPowerOutput(world, pos.offset(getMechanicalSide(),1), getMechanicalSide());
            pow += betterwithmods.api.BWMAPI.IMPLEMENTATION.getPowerOutput(world, pos.offset(getMechanicalSide().getOpposite(),1), getMechanicalSide());
            pow += betterwithmods.api.BWMAPI.IMPLEMENTATION.getPowerOutput(world, pos.offset(getMechanicalSide().getOpposite(),1), getMechanicalSide().getOpposite());
            pow = (int) (pow * ModConfig.betterWithMods.energy_conversion_ratio);
        }
        return pow;
    }

    @Override
    public int getMechanicalInput(EnumFacing enumFacing) {
        return (int) getMechanicalInput();
    }

    @Override
    public int getMaximumInput(EnumFacing enumFacing) {
        return 100;
    }

    @Override
    public int getMinimumInput(EnumFacing enumFacing) {
        return 1;
    }

    @Override
    public Block getBlock() {
        return world.getBlockState(pos).getBlock();
    }

    @Override
    public World getBlockWorld() {
        return world;
    }

    @Override
    public BlockPos getBlockPos() {
        return pos;
    }
}
