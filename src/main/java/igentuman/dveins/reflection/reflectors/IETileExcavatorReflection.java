package igentuman.dveins.reflection.reflectors;
import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.common.Config;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityBucketWheel;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityExcavator;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.FakePlayerUtil;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import igentuman.dveins.ModConfig;
import igentuman.dveins.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.ArrayList;
import java.util.Iterator;

import static igentuman.dveins.ore.OreGen.veinExtraBlocks;

public class IETileExcavatorReflection  {

    public static TileEntityExcavator te;
    
    public static ItemStack digBlocksInTheWay(TileEntityBucketWheel wheel)
    {
        BlockPos pos = wheel.getPos().add(0, -4, 0);
        ItemStack s = digBlock(pos);
        if(!s.isEmpty())
            return s;
        //Backward 1
        s = digBlock(pos.offset(te.facing, -1));
        if(!s.isEmpty())
            return s;
        //Backward 2
        s = digBlock(pos.offset(te.facing, -2));
        if(!s.isEmpty())
            return s;
        //Forward 1
        s = digBlock(pos.offset(te.facing, 1));
        if(!s.isEmpty())
            return s;
        //Forward 2
        s = digBlock(pos.offset(te.facing, 2));
        if(!s.isEmpty())
            return s;

        //Backward+Sides
        s = digBlock(pos.offset(te.facing, -1).offset(te.facing.rotateY()));
        if(!s.isEmpty())
            return s;
        s = digBlock(pos.offset(te.facing, -1).offset(te.facing.rotateYCCW()));
        if(!s.isEmpty())
            return s;
        //Center Sides
        s = digBlock(pos.offset(te.facing.rotateY()));
        if(!s.isEmpty())
            return s;
        s = digBlock(pos.offset(te.facing.rotateYCCW()));
        if(!s.isEmpty())
            return s;
        //Forward+Sides
        s = digBlock(pos.offset(te.facing, 1).offset(te.facing.rotateY()));
        if(!s.isEmpty())
            return s;
        s = digBlock(pos.offset(te.facing, 1).offset(te.facing.rotateYCCW()));
        if(!s.isEmpty())
            return s;
        return ItemStack.EMPTY;
    }

    public static ItemStack digBlock(BlockPos pos)
    {
        if(!(te.getWorld() instanceof WorldServer))
            return ItemStack.EMPTY;
        FakePlayer fakePlayer = FakePlayerUtil.getFakePlayer(te.getWorld());
        fakePlayer.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.DIAMOND_PICKAXE));
        IBlockState blockstate = te.getWorld().getBlockState(pos);
        Block block = blockstate.getBlock();
        if(block!=null&&!te.getWorld().isAirBlock(pos)&&blockstate.getPlayerRelativeBlockHardness(fakePlayer, te.getWorld(), pos)!=0)
        {
            if(!block.canHarvestBlock(te.getWorld(), pos, fakePlayer))
                return ItemStack.EMPTY;
            block.onBlockHarvested(te.getWorld(), pos, blockstate, fakePlayer);
            if(block.removedByPlayer(blockstate, te.getWorld(), pos, fakePlayer, true))
            {
                block.onPlayerDestroy(te.getWorld(), pos, blockstate);
                if(block.canSilkHarvest(te.getWorld(), pos, blockstate, fakePlayer))
                {
                    ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                    Item bitem = Item.getItemFromBlock(block);
                    if(bitem== Items.AIR)
                        return ItemStack.EMPTY;
                    ItemStack itemstack = new ItemStack(bitem, 1, block.getMetaFromState(blockstate));
                    if(!itemstack.isEmpty())
                        items.add(itemstack);

                    ForgeEventFactory.fireBlockHarvesting(items, te.getWorld(), pos, blockstate, 0, 1.0f, true, fakePlayer);

                    for(int i = 0; i < items.size(); i++)
                        if(i!=0)
                        {
                            EntityItem ei = new EntityItem(te.getWorld(), pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5, items.get(i).copy());
                            te.getWorld().spawnEntity(ei);
                        }
                    te.getWorld().playEvent(2001, pos, Block.getStateId(blockstate));
                    if(items.size() > 0)
                        return items.get(0);
                }
                else
                {
                    block.harvestBlock(te.getWorld(), fakePlayer, pos, blockstate, te.getWorld().getTileEntity(pos), ItemStack.EMPTY);
                    te.getWorld().playEvent(2001, pos, Block.getStateId(blockstate));
                }
            }
        }
        return ItemStack.EMPTY;
    }
    
    public static void update(TileEntityExcavator instance)
    {
        te = instance;
        ApiUtils.checkForNeedlessTicking(instance);
        instance.tickedProcesses = 0;
        if(instance.getWorld().isRemote||instance.isDummy()||instance.isRSDisabled())
            return;

        int max = instance.getMaxProcessPerTick();
        int i = 0;
        Iterator<TileEntityMultiblockMetal.MultiblockProcess<IMultiblockRecipe>> processIterator = instance.processQueue.iterator();
        instance.tickedProcesses = 0;
        while(processIterator.hasNext()&&i++ < max)
        {
            TileEntityMultiblockMetal.MultiblockProcess<IMultiblockRecipe> process = processIterator.next();
            if(process.canProcess(instance))
            {
                process.doProcessTick(instance);
                instance.tickedProcesses++;
                instance.updateMasterBlock(null, true);
            }
            if(process.clearProcess)
                processIterator.remove();
        }
        if (!te.isDummy()) {
            BlockPos wheelPos = te.getBlockPosForPos(31);
            if (!te.getWorld().isRemote && te.getWorld().isBlockLoaded(wheelPos)) {
                TileEntity center = te.getWorld().getTileEntity(wheelPos);
                if (center instanceof TileEntityBucketWheel) {
                    float rot = 0.0F;
                    int target = -1;
                    TileEntityBucketWheel wheel = (TileEntityBucketWheel)center;
                    EnumFacing fRot = te.facing.rotateYCCW();
                    if (wheel.facing == fRot) {
                        if (te.active != wheel.active) {
                            te.getWorld().addBlockEvent(wheel.getPos(), wheel.getBlockType(), 0, te.active ? 1 : 0);
                        }

                        rot = wheel.rotation;
                        if (rot % 45.0F > 40.0F) {
                            target = Math.round(rot / 360.0F * 8.0F) % 8;
                        }
                    }

                    int consumed;
                    if (wheel.facing != fRot || wheel.mirrored != te.mirrored) {
                        for(int h = -3; h <= 3; ++h) {
                            for(consumed = -3; consumed <= 3; ++consumed) {
                                TileEntity te1 = te.getWorld().getTileEntity(wheelPos.add(0, h, 0).offset(te.facing, consumed));
                                if (te1 instanceof TileEntityBucketWheel) {
                                    ((TileEntityBucketWheel)te1).facing = fRot;
                                    ((TileEntityBucketWheel)te1).mirrored = te.mirrored;
                                    te1.markDirty();
                                    ((TileEntityBucketWheel)te1).markContainingBlockForUpdate((IBlockState)null);
                                    te.getWorld().addBlockEvent(te1.getPos(), te1.getBlockType(), 255, 0);
                                }
                            }
                        }
                    }

                    if (!te.isRSDisabled()) {
                        ExcavatorHandler.MineralMix mineral = ExcavatorHandler.getRandomMineral(te.getWorld(), wheelPos.getX() >> 4, wheelPos.getZ() >> 4);
                        consumed = Config.IEConfig.Machines.excavator_consumption;
                        int extracted = te.energyStorage.extractEnergy(consumed, true);
                        if (extracted >= consumed) {
                            te.energyStorage.extractEnergy(consumed, false);
                            te.active = true;
                            if (target >= 0) {
                                int targetDown = (target + 4) % 8;
                                NBTTagCompound packet = new NBTTagCompound();
                                if (((ItemStack)wheel.digStacks.get(targetDown)).isEmpty()) {
                                    ItemStack blocking = digBlocksInTheWay(wheel);
                                    BlockPos lowGroundPos = wheelPos.add(0, -5, 0);
                                    if (!blocking.isEmpty()) {
                                        wheel.digStacks.set(targetDown, blocking);
                                        wheel.markDirty();
                                        te.markContainingBlockForUpdate((IBlockState)null);
                                    } else if (mineral != null) {
                                        ItemStack ore = mineral.getRandomOre(Utils.RAND);
                                        float configChance = Utils.RAND.nextFloat();
                                        float failChance = Utils.RAND.nextFloat();
                                        if (!ore.isEmpty() && (double)configChance > Config.IEConfig.Machines.excavator_fail_chance && failChance > mineral.failChance) {
                                            wheel.digStacks.set(targetDown, ore);
                                            wheel.markDirty();
                                            te.markContainingBlockForUpdate((IBlockState)null);
                                        }

                                        ExcavatorHandler.depleteMinerals(te.getWorld(), wheelPos.getX() >> 4, wheelPos.getZ() >> 4);

                                    } else if(ModConfig.immersiveEngineering.allow_ie_excavator_to_dig_veins) {
                                        BlockPos blPos = getFirstOreBlockInVein(wheelPos.getX() >> 4, wheelPos.getZ() >> 4);
                                        if(blPos == null) return;
                                        ItemStack ore = digBlock(blPos);
                                        te.active = true;
                                        wheel.digStacks.set(targetDown, ore);
                                        wheel.markDirty();
                                        te.markContainingBlockForUpdate((IBlockState)null);
                                    }

                                    if (!((ItemStack)wheel.digStacks.get(targetDown)).isEmpty()) {
                                        packet.setInteger("fill", targetDown);
                                        packet.setTag("fillStack", ((ItemStack)wheel.digStacks.get(targetDown)).writeToNBT(new NBTTagCompound()));
                                    }
                                }

                                if (!((ItemStack)wheel.digStacks.get(target)).isEmpty()) {
                                    te.doProcessOutput(((ItemStack)wheel.digStacks.get(target)).copy());
                                    Block b = Block.getBlockFromItem(((ItemStack)wheel.digStacks.get(target)).getItem());
                                    if (b != null && b != Blocks.AIR) {
                                        wheel.particleStack = ((ItemStack)wheel.digStacks.get(target)).copy();
                                    }

                                    wheel.digStacks.set(target, ItemStack.EMPTY);
                                    wheel.markDirty();
                                    te.markContainingBlockForUpdate((IBlockState)null);
                                    packet.setInteger("empty", target);
                                }

                                if (!packet.isEmpty()) {
                                    ImmersiveEngineering.packetHandler.sendToAll(new MessageTileSync(wheel, packet));
                                }
                            }
                        } else if (te.active) {
                            te.active = false;
                        }
                    } else if (te.active) {
                        te.active = false;
                    }
                }
            }

        }

    }
    public static int currentY = 255;

    public static BlockPos getFirstOreBlockInVein(int chunkX, int chunkZ)
    {
        if(currentY == 255) currentY = te.getPos().getY()-1;
        BlockPos p;
        for(int y = currentY; y > 1; y--) {
            for (int x = -veinExtraBlocks; x < 16+veinExtraBlocks; x++) {
                for (int z = -veinExtraBlocks; z < 16+veinExtraBlocks; z++) {
                    p = new BlockPos((chunkX*16)+x, y, (chunkZ*16+z));
                    IBlockState st = te.getWorld().getBlockState(p);
                    if(RegistryHandler.oreBlocks.contains(st.getBlock())) {
                        return p;
                    }
                }
            }
            currentY--;
        }
        return null;
    }
}
