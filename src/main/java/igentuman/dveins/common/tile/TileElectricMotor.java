package igentuman.dveins.common.tile;

import igentuman.dveins.RegistryHandler;
import igentuman.dveins.common.block.BlockDrillBase;
import igentuman.dveins.common.capability.InputMechCapability;
import igentuman.dveins.network.TileProcessUpdatePacket;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static mysticalmechanics.api.MysticalMechanicsAPI.MECH_CAPABILITY;
import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

public class TileElectricMotor extends TileEntity implements ITickable {
    public InputMechCapability mechCapability;
    ItemStack result;
    public int kineticEnergy;
    public int energyStored;
    public int maxEnergy = 100000;
    public TileElectricMotor() {
        super();
        mechCapability = new InputMechCapability();
        result = ItemStack.EMPTY;
        kineticEnergy = 0;
        energyStored = 0;
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == ITEM_HANDLER_CAPABILITY) {
            return true;
        }

        if (capability == MECH_CAPABILITY && facing != null && facing.getAxis() == findDirection().rotateY().getAxis()) {
            return true;
        }

        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == MECH_CAPABILITY && facing != null && facing.getAxis() == findDirection().rotateY().getAxis()) {
            return (T) mechCapability;
        }
        return super.getCapability(capability, facing);
    }




    public double getScaledProgress() {
        return energyStored / (double) maxEnergy;
    }

    public int getAdjustedProgress() {
        return energyStored;
    }

    public ItemStack getResult() {
        return result;
    }

    public TileProcessUpdatePacket getTileUpdatePacket() {
        return new TileProcessUpdatePacket(
                this.pos,
                this.kineticEnergy,
                0,
                energyStored

        );
    }

    public void onTileUpdatePacket(TileProcessUpdatePacket message) {
        this.kineticEnergy = (int)message.kineticEnergy;
        this.energyStored = (int)message.energyStored;
    }

    @Override
    public void update() {




    }



    public EnumFacing findDirection() {
        IBlockState bs = world.getBlockState(pos);
        return bs.getBlock() == RegistryHandler.DRILL_BASE ?
                bs.getValue(BlockDrillBase.FACING) : EnumFacing.DOWN;
    }


    public void process() {


    }

    @SideOnly(Side.CLIENT)
    private void playForgeSound()
    {
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvent.REGISTRY.getObject(new ResourceLocation("block.anvil.land")),SoundCategory.BLOCKS,0.2f,1,false);
    }






    public void handleUpdateTag(@Nonnull NBTTagCompound tag) {
        this.readFromNBT(tag);
    }

    @NotNull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("mech", mechCapability.serializeNBT());
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        mechCapability.deserializeNBT(compound.getCompoundTag("mech"));
    }
}
