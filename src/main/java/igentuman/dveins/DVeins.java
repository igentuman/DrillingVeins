package igentuman.dveins;

import igentuman.dveins.command.CommandHandler;
import igentuman.dveins.recipe.DveinsRecipes;
import igentuman.dveins.network.GuiProxy;
import igentuman.dveins.network.ModPacketHandler;
import igentuman.dveins.ore.OreGen;
import igentuman.dveins.util.OreDictHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLModIdMappingEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = igentuman.dveins.DVeins.MODID,
        name = igentuman.dveins.DVeins.NAME,
        version = igentuman.dveins.DVeins.VERSION,
        dependencies = "required-after:mysticalmechanics;"
)
@Mod.EventBusSubscriber
public class DVeins
{
    public static final String MODID = "dveins";
    public static final String NAME = "Drilling Veins";
    public static final String VERSION = "1.0.0";

    @Mod.Instance(MODID)
    public static DVeins instance;
        
    @SidedProxy(serverSide="igentuman.dveins.common.CommonProxy", clientSide="igentuman.dveins.client.ClientProxy")
    public static ISidedProxy proxy;

    public Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        MinecraftForge.EVENT_BUS.register(new RegistryHandler());
        MinecraftForge.EVENT_BUS.register(this);
        ModPacketHandler.registerMessages(MODID);

        GameRegistry.registerWorldGenerator(new OreGen(), 0);
        MinecraftForge.EVENT_BUS.register(new DveinsRecipes());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)  {
        logger.info("Starting Initialization.");
        proxy.init(event);
        ConfigManager.sync(MODID, Config.Type.INSTANCE);
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiProxy());
        DVeinsOreDictionary.registerChunks();
        DVeinsOreDictionary.registerOres();
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if(event.getModID().equals(MODID)) {
            ConfigManager.sync(MODID, Config.Type.INSTANCE);
        }
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent serverStartEvent) {
        CommandHandler.registerCommands(serverStartEvent);
    }

    @EventHandler
    public void onIdMapping(FMLModIdMappingEvent idMappingEvent) {
        OreDictHelper.refreshOreDictCache();
        DveinsRecipes.refreshRecipeCaches();
    }
}
