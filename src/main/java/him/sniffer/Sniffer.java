package him.sniffer;

import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import him.sniffer.constant.Mod;
import him.sniffer.proxy.CommonProxy;

@cpw.mods.fml.common.Mod(
        modid = Mod.MODID,
        name = Mod.NAME,
        version = Mod.VERSION,
        acceptedMinecraftVersions = Mod.ACMCVERSION
)
public class Sniffer {

    @SidedProxy(clientSide = Mod.CLIENT_PROXY_CLASS, serverSide = Mod.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.loadConfig(event.getModConfigurationDirectory());
        proxy.registKeyBinding();
        proxy.registCommand();
    }

    @EventHandler
    public void Init(FMLInitializationEvent event) {
        proxy.registEventHandler();
    }
}
