package him.sniffer;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import him.sniffer.constant.ModInfo;
import him.sniffer.proxy.CommonProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = ModInfo.MODID,
        name = ModInfo.NAME,
        version = ModInfo.VERSION,
        acceptedMinecraftVersions = ModInfo.ACMCVERSION
)
public class Sniffer {
    @SidedProxy(clientSide = ModInfo.CLIENT_PROXY_CLASS, serverSide = ModInfo.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;
    public static final Logger logger = LogManager.getLogger("Sniffer");

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.loadConfig(event.getModConfigurationDirectory());
        proxy.registKeyBinding();
        proxy.registCommand();
    }

    @Mod.EventHandler
    public void Init(FMLInitializationEvent event) {
        proxy.registEventHandler();
    }
}
