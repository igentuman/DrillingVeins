package igentuman.dveins;

import igentuman.dveins.common.block.*;
import igentuman.dveins.common.item.ItemOreChunk;
import igentuman.dveins.common.tile.TileDrillBase;
import igentuman.dveins.common.tile.TileElectricMotor;
import igentuman.dveins.common.tile.TileForgeHammer;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class RegistryHandler {

    @ObjectHolder("dveins:coal_ore")
    public static Block COAL_ORE = new BlockHardenedOre().setName("coal_ore");

    @ObjectHolder("dveins:iron_ore")
    public static Block IRON_ORE = new BlockHardenedOre().setName("iron_ore");

    @ObjectHolder("dveins:copper_ore")
        public static Block COPPER_ORE = new BlockHardenedOre().setName("copper_ore");

    @ObjectHolder("dveins:tin_ore")
    public static Block TIN_ORE = new BlockHardenedOre().setName("tin_ore");

    @ObjectHolder("dveins:lead_ore")
    public static Block LEAD_ORE = new BlockHardenedOre().setName("lead_ore");

    @ObjectHolder("dveins:redstone_ore")
    public static Block REDSTONE_ORE = new BlockHardenedOre().setName("redstone_ore");

    @ObjectHolder("dveins:gold_ore")
    public static Block GOLD_ORE = new BlockHardenedOre().setName("gold_ore");

    @ObjectHolder("dveins:forge_hammer")
    public static Block FORGE_HAMMER = new BlockForgeHammer();

    @ObjectHolder("dveins:drill_base")
    public static Block DRILL_BASE = new BlockDrillBase();

    @ObjectHolder("dveins:electric_motor")
    public static Block ELECTRIC_MOTOR = new BlockElectricMotor();

    @ObjectHolder("dveins:drill_iron_head")
    public static Block DRILL_IRON_HEAD = new BlockDrillHeadIron();

    @ObjectHolder("dveins:drill_diamond_head")
    public static Block DRILL_DIAMOND_HEAD = new BlockDrillHeadDiamond();

    @ObjectHolder("dveins:drill_emerald_head")
    public static Block DRILL_EMERALD_HEAD = new BlockDrillHeadEmerald();

    @ObjectHolder("dveins:copper_chunk")
    public static ItemOreChunk CHUNK_COPPER = new ItemOreChunk();

    @ObjectHolder("dveins:iron_chunk")
    public static ItemOreChunk CHUNK_IRON = new ItemOreChunk();

    @ObjectHolder("dveins:tin_chunk")
    public static ItemOreChunk CHUNK_TIN = new ItemOreChunk();

    @ObjectHolder("dveins:lead_chunk")
    public static ItemOreChunk CHUNK_LEAD = new ItemOreChunk();

    @ObjectHolder("dveins:gold_chunk")
    public static ItemOreChunk CHUNK_GOLD = new ItemOreChunk();

    public static List<Block> oreBlocks = new ArrayList<>();

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(COAL_ORE);
        event.getRegistry().register(IRON_ORE);
        event.getRegistry().register(COPPER_ORE);
        event.getRegistry().register(GOLD_ORE);
        event.getRegistry().register(TIN_ORE);
        event.getRegistry().register(LEAD_ORE);
        event.getRegistry().register(REDSTONE_ORE);

        oreBlocks.add(COAL_ORE);
        oreBlocks.add(IRON_ORE);
        oreBlocks.add(COPPER_ORE);
        oreBlocks.add(GOLD_ORE);
        oreBlocks.add(TIN_ORE);
        oreBlocks.add(LEAD_ORE);
        oreBlocks.add(REDSTONE_ORE);

        event.getRegistry().register(DRILL_BASE);
        event.getRegistry().register(DRILL_IRON_HEAD);
        event.getRegistry().register(DRILL_DIAMOND_HEAD);
        event.getRegistry().register(DRILL_EMERALD_HEAD);
        event.getRegistry().register(ELECTRIC_MOTOR);
        event.getRegistry().register(FORGE_HAMMER);

        GameRegistry.registerTileEntity(
                TileDrillBase.class,
                DRILL_BASE.getRegistryName()
        );

        GameRegistry.registerTileEntity(
                TileElectricMotor.class,
                ELECTRIC_MOTOR.getRegistryName()
        );

        GameRegistry.registerTileEntity(
                TileForgeHammer.class,
                FORGE_HAMMER.getRegistryName()
        );
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(CHUNK_COPPER.setItemName("copper_chunk"));
        event.getRegistry().register(CHUNK_IRON.setItemName("iron_chunk"));
        event.getRegistry().register(CHUNK_TIN.setItemName("tin_chunk"));
        event.getRegistry().register(CHUNK_LEAD.setItemName("lead_chunk"));
        event.getRegistry().register(CHUNK_GOLD.setItemName("gold_chunk"));
        event.getRegistry().register(new ItemBlock(COAL_ORE).setRegistryName(COAL_ORE.getRegistryName()));
        event.getRegistry().register(new ItemBlock(IRON_ORE).setRegistryName(IRON_ORE.getRegistryName()));
        event.getRegistry().register(new ItemBlock(COPPER_ORE).setRegistryName(COPPER_ORE.getRegistryName()));
        event.getRegistry().register(new ItemBlock(LEAD_ORE).setRegistryName(LEAD_ORE.getRegistryName()));
        event.getRegistry().register(new ItemBlock(TIN_ORE).setRegistryName(TIN_ORE.getRegistryName()));
        event.getRegistry().register(new ItemBlock(GOLD_ORE).setRegistryName(GOLD_ORE.getRegistryName()));
        event.getRegistry().register(new ItemBlock(REDSTONE_ORE).setRegistryName(REDSTONE_ORE.getRegistryName()));
        event.getRegistry().register(new ItemBlock(DRILL_BASE).setRegistryName(DRILL_BASE.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ELECTRIC_MOTOR).setRegistryName(ELECTRIC_MOTOR.getRegistryName()));
        event.getRegistry().register(new ItemBlock(FORGE_HAMMER).setRegistryName(FORGE_HAMMER.getRegistryName()));
        event.getRegistry().register(new ItemBlock(DRILL_DIAMOND_HEAD).setRegistryName(DRILL_DIAMOND_HEAD.getRegistryName()));
        event.getRegistry().register(new ItemBlock(DRILL_EMERALD_HEAD).setRegistryName(DRILL_EMERALD_HEAD.getRegistryName()));
        event.getRegistry().register(new ItemBlock(DRILL_IRON_HEAD).setRegistryName(DRILL_IRON_HEAD.getRegistryName()));
     }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
        registerItemModel(CHUNK_COPPER, 0, "inventory");
        registerItemModel(CHUNK_LEAD, 0, "inventory");
        registerItemModel(CHUNK_TIN, 0, "inventory");
        registerItemModel(CHUNK_IRON, 0, "inventory");
        registerItemModel(CHUNK_GOLD, 0, "inventory");
        registerItemModel(Item.getItemFromBlock(COAL_ORE), 0, "inventory");
        registerItemModel(Item.getItemFromBlock(IRON_ORE), 0, "inventory");
        registerItemModel(Item.getItemFromBlock(COPPER_ORE), 0, "inventory");
        registerItemModel(Item.getItemFromBlock(LEAD_ORE), 0, "inventory");
        registerItemModel(Item.getItemFromBlock(TIN_ORE), 0, "inventory");
        registerItemModel(Item.getItemFromBlock(GOLD_ORE), 0, "inventory");
        registerItemModel(Item.getItemFromBlock(REDSTONE_ORE), 0, "inventory");
        registerItemModel(Item.getItemFromBlock(DRILL_BASE), 0, "inventory");
        registerItemModel(Item.getItemFromBlock(ELECTRIC_MOTOR), 0, "inventory");
        registerItemModel(Item.getItemFromBlock(FORGE_HAMMER), 0, "inventory");
        registerItemModel(Item.getItemFromBlock(DRILL_DIAMOND_HEAD), 0, "inventory");
        registerItemModel(Item.getItemFromBlock(DRILL_EMERALD_HEAD), 0, "inventory");
        registerItemModel(Item.getItemFromBlock(DRILL_IRON_HEAD), 0, "inventory");
        //ClientRegistry.bindTileEntitySpecialRenderer(TileDrill.class, new DrillTESR());
    }

    @SideOnly(Side.CLIENT)
    public void registerItemModel(@Nonnull Item item, int meta, String variant) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), variant));
    }
}