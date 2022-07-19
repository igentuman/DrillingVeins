package igentuman.dveins.common.tile;

import igentuman.dveins.ModConfig;
import igentuman.dveins.RegistryHandler;
import igentuman.dveins.common.block.BlockDrillBase;
import igentuman.dveins.common.capability.InputMechCapability;
import igentuman.dveins.common.inventory.QueueItemHandler;
import igentuman.dveins.network.ModPacketHandler;
import igentuman.dveins.network.TileProcessUpdatePacket;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
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

public class TileDrillBase extends TileEntity implements ITickable {
    public InputMechCapability mechCapability;
    ItemStack result;
    public int kineticEnergy;
    public final int requiredKineticEnergy = ModConfig.drilling.energy_for_one_block;
    public int outputCooldown;
    public QueueItemHandler outputQueue;
    private int currentY;
    private boolean chunkEmptyFlag = false;
    private Chunk chunk;

    public TileDrillBase() {
        super();
        mechCapability = new InputMechCapability();
        result = ItemStack.EMPTY;
        kineticEnergy = 0;
        outputCooldown = 0;
        outputQueue = new QueueItemHandler();
        currentY = 1;
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == ITEM_HANDLER_CAPABILITY) {
            return true;
        }

        if (capability == MECH_CAPABILITY && facing != null && facing == EnumFacing.UP) {
            return true;
        }

        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == MECH_CAPABILITY && facing != null && facing == EnumFacing.UP) {
            return (T) mechCapability;
        }
        return super.getCapability(capability, facing);
    }


    public int getProgressRequired() {
        return ModConfig.drilling.energy_for_one_block;
    }

    public double getScaledProgress() {
        return kineticEnergy / (double) requiredKineticEnergy;
    }

    public int getAdjustedProgress() {
        if(outputQueue.isEmpty()) {
            return kineticEnergy;
        }
        else {
            return requiredKineticEnergy;
        }
    }

    public ItemStack getResult() {
        return result;
    }

    public TileProcessUpdatePacket getTileUpdatePacket() {
        return new TileProcessUpdatePacket(
                this.pos,
                this.kineticEnergy,
                this.currentY,
                0
        );
    }

    public void onTileUpdatePacket(TileProcessUpdatePacket message) {
        this.kineticEnergy = (int)message.kineticEnergy;
        this.currentY = (int)message.currentY;
    }

    @Override
    public void update() {

       /* if(outputCooldown > 0 && !world.isRemote) {
            outputCooldown--;
        }

        if(!outputQueue.isEmpty() && !world.isRemote) {
            if(outputCooldown <= 0) {
                outputItemFromQueue();
                outputCooldown = 10;
            }
            return;
        }

        if(chunkHasVein()) {
            kineticEnergy += mechCapability.power;
            if( !world.isRemote) {
                ModPacketHandler.instance.sendToAll(this.getTileUpdatePacket());
            }

            if(kineticEnergy >= requiredKineticEnergy) {
                process();
            }

        } else {
            if( !world.isRemote)
                kineticEnergy = 0;
        }*/
    }

    public ItemStack getOre()
    {
        if(chunkHasVein()) {

            return new ItemStack(getFirstOreBlockInChunk().getBlock());
        }
        return ItemStack.EMPTY;
    }



    public int blocksInVein()
    {
        int counter = 0;
        for(int y = currentY; y < pos.getY(); y ++) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    IBlockState st = world.getBlockState(new BlockPos((chunk.x*16)+x, y, (chunk.z*16+z)));
                    if(RegistryHandler.oreBlocks.contains(st.getBlock())) {
                        counter++;
                    }
                }
            }
        }
        return counter;
    }

    public IBlockState getFirstOreBlockInChunk()
    {
        for(int y = currentY; y < pos.getY(); y ++) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    IBlockState st = world.getBlockState(new BlockPos((chunk.x*16)+x, y, (chunk.z*16+z)));
                    if(RegistryHandler.oreBlocks.contains(st.getBlock())) {
                        return st;
                    }
                }
            }
            currentY++;
        }
        return null;
    }

    public boolean chunkHasVein() {
        if(chunk == null) {
            chunk = world.getChunk(pos);
        }
        //do quit test first
        if(!chunkEmptyFlag /*&& OreGen.chunkHasVein(chunk.x, chunk.z, world.getSeed())*/) {
            //and then check chunk blocks actually
            IBlockState st = getFirstOreBlockInChunk();
            return st != null;
        }
        return false;
    }

    public EnumFacing findDirection() {
        IBlockState bs = world.getBlockState(pos);
        return bs.getBlock() == RegistryHandler.DRILL_BASE ?
                bs.getValue(BlockDrillBase.FACING) : EnumFacing.DOWN;
    }


    public void process() {

        ItemStack output = getOre();
        if(output == null || output.equals(ItemStack.EMPTY)) return;
        if( !world.isRemote) {
            outputQueue.push(output);
        }
        /*if(kineticEnergy >= requiredKineticEnergy && kineticEnergy > 0) {
            playForgeSound();
        }*/
        kineticEnergy = 0;
    }

    @SideOnly(Side.CLIENT)
    private void playForgeSound()
    {
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvent.REGISTRY.getObject(new ResourceLocation("block.anvil.land")),SoundCategory.BLOCKS,0.2f,1,false);
    }

    public void setProgress(int kineticEnergy) {
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
        double speed = 0.3D;
        double sx = direction.getXOffset() * speed;
        double sy = direction.getYOffset() * speed;
        double sz = direction.getZOffset() * speed;

        EntityItem entity = new EntityItem(world, x, y, z, stack);
        entity.motionX = sx;
        entity.motionY = sy;
        entity.motionZ = sz;
        world.spawnEntity(entity);
    }

    public void handleUpdateTag(@Nonnull NBTTagCompound tag) {
        this.readFromNBT(tag);
    }

    @NotNull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("kineticEnergy", kineticEnergy);
        compound.setTag("result", result.serializeNBT());
        compound.setInteger("outputCooldown", outputCooldown);
        compound.setTag("outputQqueue", outputQueue.serializeNBT());
        compound.setTag("mech", mechCapability.serializeNBT());
        compound.setInteger("currentY", currentY);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        kineticEnergy = compound.getInteger("kineticEnergy");
        result = new ItemStack(compound.getCompoundTag("result"));
        outputQueue.deserializeNBT(compound.getTagList("outputQueue", 10));
        outputCooldown = compound.getInteger("outputCooldown");
        mechCapability.deserializeNBT(compound.getCompoundTag("mech"));
        currentY = compound.getInteger("currentY");
    }
}
