package igentuman.dveins.ore;

import com.google.common.base.Predicate;
import igentuman.dveins.ModConfig;
import igentuman.dveins.RegistryHandler;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

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

    private boolean chunkHasVein(int chunkX, int chunkZ, long worldSeed)
    {
        Random random = new Random(worldSeed + chunkX * xSeed + chunkZ * zSeed);
        if (random.nextInt(chances) == 0)
        {
            return true;
        }
        return false;
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
                         IChunkProvider chunkProvider) {
        if(!enableGen) return;
        String dim = String.valueOf(world.provider.getDimension());
        if(!dimensions.contains(dim)) return;
        if(!chunkHasVein(chunkX, chunkZ, world.getSeed())) return;
        int blocks = Math.min(maxBlocks, Math.max(random.nextInt()+minBlocks, minBlocks));
        runGenerator(RegistryHandler.COAL_ORE.getDefaultState(), blocks, 1, 1, 120, BlockMatcher.forBlock(Blocks.STONE), world, random, chunkX, chunkZ);

    }

    private void runGenerator(IBlockState blockToGen, int blockAmount, int chancesToSpawn, int minHeight, int maxHeight, Predicate<IBlockState> blockToReplace, World world, Random rand, int chunk_X, int chunk_Z){
        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        WorldGenMinable generator = new WorldGenMinable(blockToGen, blockAmount, new BlockPredicate());
        int heightdiff = maxHeight - minHeight +1;
        for (int i=0; i<chancesToSpawn; i++){
            int x = chunk_X * 16 +rand.nextInt(16);
            int y = minHeight + rand.nextInt(heightdiff);
            int z = chunk_Z * 16 + rand.nextInt(16);

            generator.generate(world, rand, new BlockPos(x, y, z));
        }
    }

    public static class BlockPredicate implements Predicate<IBlockState>
    {
        private BlockPredicate()
        {
        }

        public boolean apply(IBlockState state)
        {
            if(state == null) return false;

            if (state.getBlock() == Blocks.STONE)
            {
                BlockStone.EnumType blockstone$enumtype = (BlockStone.EnumType)state.getValue(BlockStone.VARIANT);
                return blockstone$enumtype.isNatural();
            }
            if(state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.SAND || state.getBlock() == Blocks.GRAVEL)
            {
                return true;
            } else {
                return false;
            }
        }
    }
}