package igentuman.dveins.common.tile;

import igentuman.dveins.DVeins;
import igentuman.dveins.ModConfig;
import igentuman.dveins.RegistryHandler;
import igentuman.dveins.client.sound.SoundHandler;
import igentuman.dveins.common.block.*;
import igentuman.dveins.common.capability.InputMechCapability;
import igentuman.dveins.common.inventory.ExistingOnlyItemHandlerWrapper;
import igentuman.dveins.common.inventory.QueueItemHandler;
import igentuman.dveins.network.ModPacketHandler;
import igentuman.dveins.network.TileProcessUpdatePacket;
import mysticalmechanics.block.BlockAxle;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


import java.util.Random;

import static igentuman.dveins.ore.OreGen.veinExtraBlocks;
import static mysticalmechanics.api.MysticalMechanicsAPI.MECH_CAPABILITY;
import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

public class TileDrillBase extends PowerBackend {
    ItemStack result;
    public int kineticEnergy;
    public final int requiredKineticEnergy = ModConfig.drilling.energy_for_one_block;
    public int outputCooldown;
    public QueueItemHandler outputQueue;
    private int currentY = -1;
    private boolean chunkEmptyFlag = false;
    private Chunk chunk;
    private boolean activeFlag = false;
    private boolean wasWorking = false;
    private boolean isRedstonePowered = false;
    private SoundEvent soundEvent;
    private int playSoundCooldown = 0;
    private long lastActive = -1;
    private int rapidChangeThreshold = 10;
    @SideOnly(Side.CLIENT)
    private ISound activeSound;
    private IBlockState firstOreBlockInVein;
    private IBlockState drillHead = BlockAir.getStateById(0);

    public TileDrillBase() {
        super();
        mechCapability = new InputMechCapability();
        result = ItemStack.EMPTY;
        kineticEnergy = 0;
        outputCooldown = 0;
        outputQueue = new QueueItemHandler();
        soundEvent = new SoundEvent(new ResourceLocation(DVeins.MODID, "active_boring"));
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
                activeSound = SoundHandler.startTileSound(soundEvent.getSoundName(), 1F, getPos());
            }
            playSoundCooldown = 20;
        } else {
            stopSound();
        }
    }

    public void neighborChanged(BlockPos from) {
        checkDrillHead();
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

    public EnumFacing.Axis getAllowedAxis()
    {
        return EnumFacing.Axis.Y;
    }

    public EnumFacing getMechanicalSide()
    {
        return EnumFacing.UP;
    }

    public int getCurrentY()
    {
        return currentY;
    }

    public int collectedRotation = 0;

    public double getDrillHeadMultiplier()
    {
        if(world.getBlockState(pos.down()).getBlock() instanceof BlockDrillHeadDiamond) {
            return ModConfig.drilling.diamond_drill_head_multiplier;
        }
        return 1D;
    }

    public void rotateDrillHead(int power)
    {
        collectedRotation += power;
        if(collectedRotation < 45) return;
        collectedRotation = 0;
        IBlockState state = world.getBlockState(pos.down());
        if(state.getBlock() instanceof DrillHead) {
            DrillHead.EnumRotation curRotation = state.getValue(DrillHead.rotation);
            boolean keyFound = false;
            DrillHead.EnumRotation lastRotation = curRotation;
            for(DrillHead.EnumRotation val: DrillHead.EnumRotation.values()) {
                if(keyFound) {
                    curRotation = val;
                    break;
                }
                if(curRotation == val) {
                    keyFound = true;
                    continue;
                }
            }
            if(lastRotation == curRotation) {
                curRotation = DrillHead.EnumRotation.ONE;
            }
            world.setBlockState(pos.down(), state.withProperty(DrillHead.rotation, curRotation));
        }
    }

    public TileProcessUpdatePacket getTileUpdatePacket() {
        return new TileProcessUpdatePacket(
                this.pos,
                this.kineticEnergy,
                this.currentY,
                0,
                activeFlag,
                isRedstonePowered
        );
    }

    public void onTileUpdatePacket(TileProcessUpdatePacket message) {
        this.kineticEnergy = (int)message.kineticEnergy;
        this.currentY = (int)message.currentY;
        this.activeFlag = (boolean) message.activeFlag;
        this.isRedstonePowered = message.isRedstonePowered;
    }

    private void checkDrillHead()
    {
        drillHead = world.getBlockState(getPos().down());
    }

    public boolean hasDrillHead()
    {
        if(getPos().getY()==0) return false;
        if(world.isRemote) {
            checkDrillHead();
        }
        return drillHead.getBlock() instanceof DrillHead;
    }

    @Override
    public void update() {
        if (world.isRemote) {
            updateSound();
        }
        if(!world.isRemote && currentY == -1) {
            currentY = getPos().getY();
        }
        if(!world.isRemote && !hasDrillHead()) {
            checkDrillHead();
        }
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
        if(!world.isRemote) {
            double power = getMechanicalInput();
            activeFlag = false;
            if (chunkHasVein() && hasDrillHead()) {
                if (power > 0) {
                    rotateDrillHead((int) power);
                    activeFlag = true;
                    wasWorking = true;
                }
                kineticEnergy += power * getDrillHeadMultiplier();
                if (!world.isRemote) {
                    ModPacketHandler.instance.sendToAll(this.getTileUpdatePacket());
                }

                if (kineticEnergy >= requiredKineticEnergy) {
                    process();
                }
            } else {
                if (!world.isRemote) {
                    kineticEnergy = 0;
                    ModPacketHandler.instance.sendToAll(this.getTileUpdatePacket());
                }
            }
        }
    }

    public boolean isActive()
    {
        return activeFlag;
    }

    public ItemStack getOre()
    {
        if(chunkHasVein()) {
            return new ItemStack(getFirstOreBlockInVein().getBlock());
        }
        return ItemStack.EMPTY;
    }

    public int blocksInVein()
    {
        int counter = 0;
        for(int y = currentY; y > 1; y--) {
            for (int x = -veinExtraBlocks; x < 16+veinExtraBlocks; x++) {
                for (int z = -veinExtraBlocks; z < 16+veinExtraBlocks; z++) {
                    IBlockState st = world.getBlockState(new BlockPos((chunk.x*16)+x, y, (chunk.z*16+z)));
                    if(RegistryHandler.oreBlocks.contains(st.getBlock())) {
                        counter++;
                    }
                }
            }
        }
        return counter;
    }

    public BlockPos firstOreBlockPos = null;

    public IBlockState getFirstOreBlockInVein()
    {
        for(int y = currentY; y > 1; y--) {
            for (int x = -veinExtraBlocks; x < 16+veinExtraBlocks; x++) {
                for (int z = -veinExtraBlocks; z < 16+veinExtraBlocks; z++) {
                    IBlockState st = world.getBlockState(new BlockPos((chunk.x*16)+x, y, (chunk.z*16+z)));
                    if(RegistryHandler.oreBlocks.contains(st.getBlock())) {
                        firstOreBlockPos = new BlockPos((chunk.x*16)+x, y, (chunk.z*16+z));
                        firstOreBlockInVein = st;
                        return st;
                    }
                }
            }
            currentY--;
        }
        firstOreBlockInVein = null;
        firstOreBlockPos = null;
        return null;
    }

    public boolean chunkHasVein() {
        if(chunk == null) {
            chunk = world.getChunk(pos);
        }
        //do quit test first
        if(!chunkEmptyFlag /*&& OreGen.chunkHasVein(chunk.x, chunk.z, world.getSeed())*/) {
            //and then check chunk blocks actually
            IBlockState st = getFirstOreBlockInVein();
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
        int fortune = 0;
        if(world.getBlockState(pos.down()).getBlock() instanceof BlockDrillHeadEmerald) {
            fortune = ModConfig.drilling.emerald_head_drill_fortune;
        }
        Random rand = new Random();
        ItemStack output = new ItemStack(
                firstOreBlockInVein.getBlock().getItemDropped(firstOreBlockInVein, rand, fortune),
                ModConfig.oreGeneration.ore_chunks_per_block+(int)Math.ceil(rand.nextDouble()*fortune)
        );

        if(output == null || output.equals(ItemStack.EMPTY)) return;
        if( !world.isRemote) {
            if(firstOreBlockPos != null) {
                outputQueue.push(output);
                world.setBlockToAir(firstOreBlockPos);
                firstOreBlockPos = null;
            }
        }
        kineticEnergy = 0;
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
        double y = pos.getY() + 0.2 + direction.getYOffset() * 0.625;
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
        compound.setBoolean("activeFlag", activeFlag);
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
        activeFlag = compound.getBoolean("activeFlag");
    }
}
