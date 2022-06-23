package igentuman.dveins.common.tile;

import igentuman.dveins.ModConfig;
import igentuman.dveins.RegistryHandler;
import igentuman.dveins.common.block.BlockDrill;
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

public class TileDrill extends TileEntity implements ITickable {
    public InventoryCraftingWrapper inventory;
    public InputMechCapability mechCapability;
    ItemStack result;
    public int progress;
    public int requiredProgress;
    public int outputCooldown;
    public QueueItemHandler outputQueue;

    public TileDrill() {
        super();
        inventory = new MechanicalForgeHammerItemCapabillity();
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

    public int getProgressRequired() {
        return ModConfig.mechanicalCrafter.basePowerCost + ModConfig.mechanicalCrafter.ingredientPowerCost * getNumberOfIngredients();
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
                outputItemFromQueue();
                outputCooldown = ModConfig.mechanicalCrafter.outputCooldown;
            }
            return;
        }

        if(canProgress()) {
            progress += mechCapability.power;
            if( !world.isRemote) {
                ModPacketHandler.instance.sendToAll(this.getTileUpdatePacket());
            }

            if(progress >= requiredProgress) {
                process();
            }

        } else {
            if( !world.isRemote)
            progress = 0;
        }
    }

    public boolean canProgress() {
        ItemStack stack = ForgeringRecipeOutput(inventory.getCraftingGrid().getStackInSlot(0));
        return !stack.isEmpty();
    }

    public EnumFacing findDirection() {
        IBlockState bs = world.getBlockState(pos);
        return bs.getBlock() == RegistryHandler.DRILL ?
                bs.getValue(BlockDrill.FACING) : EnumFacing.DOWN;
    }



    public ItemStack ForgeringRecipeOutput(ItemStack input)
    {
        String itemName =  input.getItem().getRegistryName().toString();
        itemName += input.getMetadata() != 0 ? ":"+input.getMetadata() : "";
        switch (itemName) {
            case "minecraft:iron_ingot":
                return ItemHelper.getStackFromString("ic2:plate",3);
            case "minecraft:iron_block":
                return ItemHelper.getStackFromString("ic2:plate",12);
            case "ic2:resource:6"://copper block
                return ItemHelper.getStackFromString("ic2:plate",10);
            case "ic2:ingot:2"://copper ingot
                return ItemHelper.getStackFromString("ic2:plate",1);
            case "ic2:ingot:6"://tin ingot
                return ItemHelper.getStackFromString("ic2:plate",8);
            case "ic2:resource:9"://tin block
                return ItemHelper.getStackFromString("ic2:plate",17);
            case "ic2:resource:7"://lead block
                return ItemHelper.getStackFromString("ic2:plate",14);
            case "ic2:ingot:3"://lead ingot
                return ItemHelper.getStackFromString("ic2:plate",5);
            case "ic2:plate:1"://copper plate
                return ItemHelper.getStackFromString("ic2:casing",1);
            case "ic2:plate:8"://tin plate
                return ItemHelper.getStackFromString("ic2:casing",6);
            case "ic2:plate:5"://lead plate
                return ItemHelper.getStackFromString("ic2:casing",4);
            case "ic2:plate:3"://iron plate
                return ItemHelper.getStackFromString("ic2:casing",3);
        }
        return ItemStack.EMPTY;
    }


    public void process() {

        if(inventory.getCraftingGrid().getStackInSlot(0) == null) return;
        ItemStack output = ForgeringRecipeOutput(inventory.getCraftingGrid().getStackInSlot(0));
        if(output == null || output.equals(ItemStack.EMPTY)) return;
        if( !world.isRemote) {
            outputQueue.push(output);
            inventory.extractItem(0, 1, false);
        }
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

    public void setProgress(int progress) {
        this.progress = progress;
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
        double speed = ModConfig.mechanicalCrafter.ejectionVelocity;
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

            ItemStack newResult = ForgeringRecipeOutput(inventory.getCraftingGrid().getStackInSlot(0));
            int newRequiredProgress = getProgressRequired();

            if(!ItemStack.areItemStackTagsEqual(newResult, getResult())
                    || newRequiredProgress != requiredProgress) {
                result = newResult;
                progress = 0;
                requiredProgress = getProgressRequired();
            }

            TileDrill.this.markDirty();
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
        compound.setInteger("progress", progress);
        compound.setInteger("required_progress", requiredProgress);
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
        progress = compound.getInteger("progress");
        requiredProgress = compound.getInteger("required_progress");
        result = new ItemStack(compound.getCompoundTag("result"));
        outputQueue.deserializeNBT(compound.getTagList("output_queue", 10));
        mechCapability.deserializeNBT(compound.getCompoundTag("mech"));
    }
}
