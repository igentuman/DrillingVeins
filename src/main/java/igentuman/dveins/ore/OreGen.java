package igentuman.dveins.ore;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.common.IESaveData;
import com.google.common.base.Predicate;
import igentuman.dveins.ModConfig;
import igentuman.dveins.RegistryHandler;
import igentuman.dveins.util.ModCheck;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import javax.print.DocFlavor;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class OreGen implements IWorldGenerator {

    public static final boolean enableGen = ModConfig.oreGeneration.enable_ore_generation;
    public static final int minBlocks = ModConfig.oreGeneration.min_vein_size;
    public static final int maxBlocks = ModConfig.oreGeneration.max_vein_size;
    public static final int chances = ModConfig.oreGeneration.vein_chance;
    public static final List<String> dimensions = Arrays.asList(ModConfig.oreGeneration.dimensions);
    public static long xSeed = 6787867678L;
    public static long zSeed = 2212332449L;
    private int chunkID = 0;
    private int blocksCounter = 0;
    public static int veinExtraBlocks = ModConfig.oreGeneration.vein_extra_blocks_outside_chunk;
    public static World world;

    private Block[] blocksToReplace = new Block[] {
            Blocks.STONE,
            Blocks.GRAVEL,
            Blocks.AIR,
            Blocks.SAND,
            Blocks.DIRT,
            Blocks.SANDSTONE,
            Blocks.GRASS
    };

    private Block getRandomOre(World world)
    {
        Random r = new Random(world.getSeed() * chunkID);
        Block[] ores = new Block[] {
                RegistryHandler.COAL_ORE,
                RegistryHandler.REDSTONE_ORE,
                RegistryHandler.IRON_ORE,
                RegistryHandler.COPPER_ORE,
                RegistryHandler.TIN_ORE,
                RegistryHandler.LEAD_ORE,
                RegistryHandler.GOLD_ORE
        };
        return ores[r.nextInt(ores.length)];
    }

    protected static boolean chunkHasDeposit(int chunkX, int chunkZ)
    {
        ExcavatorHandler.MineralWorldInfo info = ExcavatorHandler.getMineralWorldInfo(world,
                chunkX, chunkZ);
        return info.mineral != null;
    }

    public static void generateDeposit(int chunkX, int chunkZ)
    {
        if(!ModCheck.ieLoaded()) return;
        ExcavatorHandler.MineralWorldInfo info = ExcavatorHandler.getMineralWorldInfo(world,
                chunkX, chunkZ);
        Random rnd = new Random(chunkX+chunkZ);
        int id = rnd.nextInt(ExcavatorHandler.mineralList.size()-1);
        ExcavatorHandler.MineralMix mix = (ExcavatorHandler.MineralMix) ExcavatorHandler.mineralList.keySet().toArray()[id];
        info.mineralOverride = mix;
        IESaveData.setDirty(world.provider.getDimension());
    }

    public static boolean chunkHasVein(int chunkX, int chunkZ, long worldSeed)
    {
        if(ModConfig.immersiveEngineering.generate_veins_only_at_ie_deposits_chunks &&
                ModCheck.ieLoaded()
        ) {
            return chunkHasDeposit(chunkX, chunkZ);
        }
        Random random = new Random(worldSeed + chunkX * xSeed + chunkZ * zSeed);
        if (random.nextInt(chances) == 0)
        {
            if(ModConfig.immersiveEngineering.generate_ie_deposits_at_vein_chunk) {
                generateDeposit(chunkX, chunkZ);
            }
            return true;
        }
        return false;
    }

    private boolean shouldSpawn(int x, int y, int z, World world)
    {
        Random rand = new Random(world.getSeed()/10000 * chunkID * (x/2+1) * (y/2+1) * (z/2 + 1));
        int xShift = Math.abs(8+veinExtraBlocks - x);
        int zShift = Math.abs(8+veinExtraBlocks - z);
        int yShift = Math.abs(30 - y);
        int radius = (int) Math.sqrt(xShift*xShift+zShift*zShift);
        int horizontal = 0;
        //always generate in center
        if(xShift+zShift < 4) return true;
        int vertical = 0;
        try {
            if (xShift + zShift > 0) {
                horizontal = (rand.nextInt(xShift + zShift))/5+rand.nextInt(radius+1);
            }
            if (yShift > 0) {
                vertical = rand.nextInt(yShift)+rand.nextInt(60/(y+1))/2;
            }
        } catch (IllegalArgumentException ignored) {

        }
        return horizontal < 2 && vertical < 25;
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
                         IChunkProvider chunkProvider) {
        ModCheck.init();
        OreGen.world = world;
        if(!enableGen) return;
        String dim = String.valueOf(world.provider.getDimension());
        if(!dimensions.contains(dim)) return;
        if(!chunkHasVein(chunkX, chunkZ, world.getSeed())) return;
        int blocks = Math.min(maxBlocks, Math.max(random.nextInt()+minBlocks, minBlocks));
        chunkID = chunkX+chunkZ;
        blocksCounter = 0;
        runGenerator(getRandomOre(world).getDefaultState(), blocks, 1, 1, 120, BlockMatcher.forBlock(Blocks.STONE), world, random, chunkX, chunkZ);

    }

    private void runGenerator(IBlockState blockToGen, int blockAmount, int chancesToSpawn, int minHeight, int maxHeight, Predicate<IBlockState> blockToReplace, World world, Random rand, int chunk_X, int chunk_Z){
        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");
        for (int i=0; i<chancesToSpawn; i++){

            int x = chunk_X * 16-veinExtraBlocks;
            int y = maxHeight;
            int z = chunk_Z * 16-veinExtraBlocks;
            generateVein(world, rand, blockAmount, x, y ,z , blockToGen);
        }
    }

    private boolean canPlace(BlockPos pos, World world)
    {
        Block block = world.getBlockState(pos).getBlock();

        if(Arrays.asList(blocksToReplace).contains(block)) {
            if(block == Blocks.AIR) {
                return pos.getY() < 60 && !world.canSeeSky(pos);
            }
            if (!ModConfig.oreGeneration.visible_on_surface)
            {
                return !world.canSeeSky(pos) && !block.equals(Blocks.GRASS);
            }
            return true;
        }
        return false;
    }

    private void generateVein(World world, Random rand, int amount, int xOff, int yOff, int zOff, IBlockState toPlace)
    {

        for (int y = yOff; y > 1; y--) {
            for (int x = xOff; x < 16 + veinExtraBlocks*2 + xOff; x++) {
                for (int z = zOff; z < 16 + veinExtraBlocks*2 + zOff; z++) {
                    if(blocksCounter > amount) return;
                    if(
                            (x-xOff == 0 && (z-zOff == 0 || z-zOff == 15 + veinExtraBlocks*2 )) ||
                            (x-xOff == 15 + veinExtraBlocks*2 && (z-zOff == 0 || z-zOff == 15 + veinExtraBlocks*2 ))
                    ) continue;
                    BlockPos pos = new BlockPos(x, y, z);
                    if(canPlace(pos, world) && shouldSpawn(x-xOff, y, z-zOff, world)) {
                        world.setBlockState(pos, toPlace, 2);
                        blocksCounter++;
                    }
                }
            }
        }
    }
}