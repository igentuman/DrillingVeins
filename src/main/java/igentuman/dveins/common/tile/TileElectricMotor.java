package igentuman.dveins.common.tile;

import igentuman.dveins.ModConfig;
import igentuman.dveins.RegistryHandler;
import igentuman.dveins.common.block.BlockDrillBase;
import igentuman.dveins.common.capability.InputMechCapability;
import igentuman.dveins.common.inventory.ExistingOnlyItemHandlerWrapper;
import igentuman.dveins.common.inventory.InventoryCraftingWrapper;
import igentuman.dveins.common.inventory.QueueItemHandler;
import igentuman.dveins.network.ModPacketHandler;
import igentuman.dveins.network.TileProcessUpdatePacket;
import igentuman.dveins.util.ItemHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static mysticalmechanics.api.MysticalMechanicsAPI.MECH_CAPABILITY;
import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

public class TileElectricMotor extends TileEntity implements ITickable {
    public InputMechCapability mechCapability;
    ItemStack result;
    public int progress;
    public int requiredProgress;
    public int outputCooldown;
    public QueueItemHandler outputQueue;

    public TileElectricMotor() {
        super();
        mechCapability = new InputMechCapability();
        result = ItemStack.EMPTY;
        progress = 0;
        requiredProgress = 0;
        outputCooldown = 0;
        outputQueue = new QueueItemHandler();
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



    public int getProgressRequired() {
        return ModConfig.mechanicalCrafter.basePowerCost + ModConfig.mechanicalCrafter.ingredientPowerCost;
    }

    public double getScaledProgress() {
        return progress / (double) requiredProgress;
    }

    public int getAdjustedProgress() {
        if(outputQueue.isEmpty()) {
            return progress;
        }
        else {
            return requiredProgress;
        }
    }

    public ItemStack getResult() {
        return result;
    }

    public TileProcessUpdatePacket getTileUpdatePacket() {
        return new TileProcessUpdatePacket(
                this.pos,
                this.requiredProgress,
                this.progress
        );
    }

    public void onTileUpdatePacket(TileProcessUpdatePacket message) {
        this.progress = (int)message.progress;
        this.requiredProgress = (int)message.requiredProgress;
    }

    @Override
    public void update() {


        if(outputCooldown > 0 && !world.isRemote) {
            outputCooldown--;
        }

        if(!outputQueue.isEmpty() && !world.isRemote) {
            if(outputCooldown <= 0) {
                outputCooldown = ModConfig.mechanicalCrafter.outputCooldown;
            }
            return;
        }


    }



    public EnumFacing findDirection() {
        IBlockState bs = world.getBlockState(pos);
        return bs.getBlock() == RegistryHandler.DRILL_BASE ?
                bs.getValue(BlockDrillBase.FACING) : EnumFacing.DOWN;
    }


    public void process() {


        if(progress >= requiredProgress && progress > 0) {
            playForgeSound();
        }
        progress = 0;
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
