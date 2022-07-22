package igentuman.dveins.common.tile;

import igentuman.dveins.ModConfig;
import igentuman.dveins.RegistryHandler;
import igentuman.dveins.common.block.BlockForgeHammer;
import igentuman.dveins.common.capability.InputMechCapability;
import igentuman.dveins.common.inventory.ExistingOnlyItemHandlerWrapper;
import igentuman.dveins.common.inventory.InventoryCraftingWrapper;
import igentuman.dveins.common.inventory.QueueItemHandler;
import igentuman.dveins.network.ModPacketHandler;
import igentuman.dveins.network.TileProcessUpdatePacket;
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

public class TileForgeHammer extends TileEntity implements ITickable {
    public InventoryCraftingWrapper inventory;
    public InputMechCapability mechCapability;
    ItemStack result;
    public int kineticEnergy;
    public int requiredEnergy;
    public int outputCooldown;
    public QueueItemHandler outputQueue;

    private boolean isRedstonePowered = false;
    private boolean activeFlag = false;
    private boolean wasWorking = false;

    public TileForgeHammer() {
        super();
        inventory = new MechanicalForgeHammerItemCapabillity();
        mechCapability = new InputMechCapability();
        result = ItemStack.EMPTY;
        kineticEnergy = 0;
        requiredEnergy = 0;
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
        if (capability == ITEM_HANDLER_CAPABILITY) {
            return (T) (facing == null ? inventory : new ExistingOnlyItemHandlerWrapper(inventory));
        }

        if (capability == MECH_CAPABILITY && facing != null && facing.getAxis() == findDirection().rotateY().getAxis()) {
            return (T) mechCapability;
        }
        return super.getCapability(capability, facing);
    }

    public int getNumberOfIngredients() {
        int count = 0;
        if(!inventory.getStackInSlot(0).isEmpty()) {
            count++;
        }
        return count;
    }

    public int getEnergyRequired() {
        return ModConfig.forgeHammer.energy_per_recipe;
    }

    public double getScaledEnergy() {
        return kineticEnergy / (double) requiredEnergy;
    }

    public int getAdjustedEnergy() {
        if(outputQueue.isEmpty()) {
            return kineticEnergy;
        }
        else {
            return requiredEnergy;
        }
    }

    public ItemStack getResult() {
        return result;
    }

    public TileProcessUpdatePacket getTileUpdatePacket() {
        return new TileProcessUpdatePacket(
                this.pos,
                this.kineticEnergy,
                0,
                0,
                activeFlag,
                isRedstonePowered
        );
    }

    public void onTileUpdatePacket(TileProcessUpdatePacket message) {
        this.kineticEnergy = (int)message.kineticEnergy;
        this.activeFlag = message.activeFlag;
        this.isRedstonePowered = message.isRedstonePowered;
    }

    @Override
    public void update() {
        if(outputCooldown > 0 && !world.isRemote) {
            outputCooldown--;
        }

        if(!outputQueue.isEmpty() && !world.isRemote) {
            if(outputCooldown <= 0) {
                outputItemFromQueue();
                outputCooldown = 10;
            }
            return;
        }

        if(canEnergy()) {
            kineticEnergy += mechCapability.power;
            if( !world.isRemote) {
                ModPacketHandler.instance.sendToAll(this.getTileUpdatePacket());
            }

            if(kineticEnergy >= requiredEnergy) {
                process();
            }

        } else {
            if( !world.isRemote)
            kineticEnergy = 0;
        }
    }

    public boolean canEnergy() {
        ItemStack stack = inventory.getCraftingGrid().getStackInSlot(0);
        return !stack.isEmpty();
    }

    public EnumFacing findDirection() {
        IBlockState bs = world.getBlockState(pos);
        return bs.getBlock() == RegistryHandler.FORGE_HAMMER ?
                bs.getValue(BlockForgeHammer.FACING) : EnumFacing.DOWN;
    }
    
    public void process() {

        if(inventory.getCraftingGrid().getStackInSlot(0) == null) return;
        ItemStack output = inventory.getCraftingGrid().getStackInSlot(0);
        if(output == null || output.equals(ItemStack.EMPTY)) return;
        if( !world.isRemote) {
            outputQueue.push(output);
            inventory.extractItem(0, 1, false);
        }
        if(kineticEnergy >= requiredEnergy && kineticEnergy > 0) {
            playForgeSound();
        }
        kineticEnergy = 0;
    }

    @SideOnly(Side.CLIENT)
    private void playForgeSound()
    {
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvent.REGISTRY.getObject(new ResourceLocation("block.anvil.land")),SoundCategory.BLOCKS,0.2f,1,false);
    }

    public void setEnergy(int kineticEnergy) {
        this.kineticEnergy = kineticEnergy;
    }

    private void outputItemFromQueue() {
        if (outputQueue.isEmpty()) return;

        ItemStack stack = outputQueue.peek();
        stack = outputItem(stack);

        if (stack.isEmpty()) {
            outputQueue.pop();
        }
        else {
            outputQueue.replaceHead(stack);
        }
    }

    private ItemStack outputItem(ItemStack stack) {
        EnumFacing outputDirection = findDirection();

        TileEntity te = world.getTileEntity(pos.offset(outputDirection));
        IItemHandler destination = null;

        if(te != null && te.hasCapability(ITEM_HANDLER_CAPABILITY, outputDirection.getOpposite())) {
            destination = te.getCapability(ITEM_HANDLER_CAPABILITY, outputDirection.getOpposite());
        }

        if (destination != null) {
            stack = ItemHandlerHelper.insertItem(destination, stack, false);
            return stack;
        }

        dropItem(stack, outputDirection);
        return ItemStack.EMPTY;
    }

    private void dropItem(ItemStack stack, EnumFacing direction) {
        double x = pos.getX() + 0.5 + direction.getXOffset() * 0.625;
        double y = pos.getY() + ((direction.getAxis() == EnumFacing.Axis.Y) ? 0.5 : 0.125)
                + direction.getYOffset() * 0.625;
        double z = pos.getZ() + 0.5 + direction.getZOffset() * 0.625;
        double speed = 0.2D;
        double sx = direction.getXOffset() * speed;
        double sy = direction.getYOffset() * speed;
        double sz = direction.getZOffset() * speed;

        EntityItem entity = new EntityItem(world, x, y, z, stack);
        entity.motionX = sx;
        entity.motionY = sy;
        entity.motionZ = sz;
        world.spawnEntity(entity);
    }

    private class MechanicalForgeHammerItemCapabillity extends InventoryCraftingWrapper {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);

            if(world == null || world.isRemote) return;

            ItemStack newResult = inventory.getCraftingGrid().getStackInSlot(0);
            int newRequiredEnergy = getEnergyRequired();

            if(!ItemStack.areItemStackTagsEqual(newResult, getResult())
                    || newRequiredEnergy != requiredEnergy) {
                result = newResult;
                kineticEnergy = 0;
                requiredEnergy = getEnergyRequired();
            }

            TileForgeHammer.this.markDirty();
        }
    }

    public void handleUpdateTag(@Nonnull NBTTagCompound tag) {
        this.readFromNBT(tag);
    }

    @NotNull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("inventory", inventory.serializeNBT());
        compound.setInteger("kineticEnergy", kineticEnergy);
        compound.setInteger("required_progress", requiredEnergy);
        compound.setTag("result", result.serializeNBT());
        compound.setInteger("output_cooldown", outputCooldown);
        compound.setTag("output_queue", outputQueue.serializeNBT());
        compound.setTag("mech", mechCapability.serializeNBT());
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        kineticEnergy = compound.getInteger("kineticEnergy");
        requiredEnergy = compound.getInteger("required_progress");
        result = new ItemStack(compound.getCompoundTag("result"));
        outputQueue.deserializeNBT(compound.getTagList("output_queue", 10));
        mechCapability.deserializeNBT(compound.getCompoundTag("mech"));
    }
}
