package igentuman.dveins;

import igentuman.dveins.common.block.BlockDrillBase;
import igentuman.dveins.common.block.BlockDrillHeadDiamond;
import igentuman.dveins.common.block.BlockDrillHeadIron;
import igentuman.dveins.common.block.BlockElectricMotor;
import igentuman.dveins.common.tile.TileDrillBase;
import igentuman.dveins.common.tile.TileElectricMotor;
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

@Mod.EventBusSubscriber
public class RegistryHandler {

    @ObjectHolder("dveins:drill_base")
    public static Block DRILL_BASE = new BlockDrillBase();

    @ObjectHolder("dveins:electric_motor")
    public static Block ELECTRIC_MOTOR = new BlockElectricMotor();

    @ObjectHolder("dveins:drill_iron_head")
    public static Block DRILL_IRON_HEAD = new BlockDrillHeadIron();

    @ObjectHolder("dveins:drill_diamond_head")
    public static Block DRILL_DIAMOND_HEAD = new BlockDrillHeadDiamond();

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(DRILL_BASE);
        event.getRegistry().register(DRILL_IRON_HEAD);
        event.getRegistry().register(DRILL_DIAMOND_HEAD);
        event.getRegistry().register(ELECTRIC_MOTOR);

        GameRegistry.registerTileEntity(
                TileDrillBase.class,
                DRILL_BASE.getRegistryName()
        );

        GameRegistry.registerTileEntity(
                TileElectricMotor.class,
                ELECTRIC_MOTOR.getRegistryName()
        );
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlock(DRILL_BASE).setRegistryName(DRILL_BASE.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ELECTRIC_MOTOR).setRegistryName(ELECTRIC_MOTOR.getRegistryName()));
        event.getRegistry().register(new ItemBlock(DRILL_DIAMOND_HEAD).setRegistryName(DRILL_DIAMOND_HEAD.getRegistryName()));
        event.getRegistry().register(new ItemBlock(DRILL_IRON_HEAD).setRegistryName(DRILL_IRON_HEAD.getRegistryName()));
     }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
        registerItemModel(Item.getItemFromBlock(DRILL_BASE), 0, "inventory");
        registerItemModel(Item.getItemFromBlock(ELECTRIC_MOTOR), 0, "inventory");
        registerItemModel(Item.getItemFromBlock(DRILL_DIAMOND_HEAD), 0, "inventory");
        registerItemModel(Item.getItemFromBlock(DRILL_IRON_HEAD), 0, "inventory");
        //ClientRegistry.bindTileEntitySpecialRenderer(TileDrill.class, new DrillTESR());
    }

    @SideOnly(Side.CLIENT)
    public void registerItemModel(@Nonnull Item item, int meta, String variant) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), variant));
    }
}