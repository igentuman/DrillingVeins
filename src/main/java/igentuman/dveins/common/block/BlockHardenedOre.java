package igentuman.dveins.common.block;

import igentuman.dveins.ModConfig;
import igentuman.dveins.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

import static igentuman.dveins.DVeins.MODID;

public class BlockHardenedOre extends Block {

    public BlockHardenedOre() {
        super(Material.ROCK);
        this.setHardness(50f);
        this.setResistance(2000f);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public BlockHardenedOre setName(String name)
    {
        setTranslationKey(name);
        setRegistryName(MODID, name);
        return this;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
       switch (getRegistryName().getPath()) {
           case "iron_ore":
               return RegistryHandler.CHUNK_IRON;
           case "tin_ore":
               return RegistryHandler.CHUNK_TIN;
           case "lead_ore":
               return RegistryHandler.CHUNK_LEAD;
           case "gold_ore":
               return RegistryHandler.CHUNK_GOLD;
           case "copper_ore":
               return RegistryHandler.CHUNK_COPPER;
           case "coal_ore":
               return Items.COAL;
           case "redstone_ore":
               return Items.REDSTONE;
       }
       return Item.getItemFromBlock(this);
    }

    public int quantityDropped(Random random) {
        return ModConfig.oreGeneration.ore_chunks_per_block;
    }

}
