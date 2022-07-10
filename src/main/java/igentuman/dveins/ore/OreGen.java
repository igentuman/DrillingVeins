package igentuman.dveins.ore;

import com.google.common.base.Predicate;
import igentuman.dveins.ModConfig;
import igentuman.dveins.RegistryHandler;
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
    private long xSeed = 6787867678L;
    private long zSeed = 221233245549L;
    private int chunkID = 0;
    private int blocksCounter = 0;

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

    private boolean chunkHasVein(int chunkX, int chunkZ, long worldSeed)
    {
        Random random = new Random(worldSeed + chunkX * xSeed + chunkZ * zSeed);
        if (random.nextInt(chances) == 0)
        {
            return true;
        }
        return false;
    }

    private boolean shouldSpawn(int x, int y, int z, World world)
    {
        Random rand = new Random(world.getSeed()/10000 * chunkID * (x/2+1) * (y/2+1) * (z/2 + 1));
        int xShift = Math.abs(8 - x);
        int zShift = Math.abs(8 - z);
        int yShift = Math.abs(30 - y);
        int radius = (int) Math.sqrt(xShift*xShift+zShift*zShift);
        int horizontal = 0;
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
        return horizontal < 1 && vertical < 15;
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
                         IChunkProvider chunkProvider) {
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
            int x = chunk_X * 16 + 1;
            int y = maxHeight;
            int z = chunk_Z * 16 + 1;
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
            for (int x = xOff; x < 16 + xOff; x++) {
                for (int z = zOff; z < 16 + zOff; z++) {
                    if(blocksCounter > amount) return;
                    if(
                            (x-xOff == 0 && (z-zOff == 0 || z-zOff == 15)) ||
                            (x-xOff == 15 && (z-zOff == 0 || z-zOff == 15))
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