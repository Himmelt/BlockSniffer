package him.sniffer;

import him.sniffer.constant.IMod;
import him.sniffer.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = IMod.MODID,
        name = IMod.NAME,
        version = IMod.VERSION,
        acceptedMinecraftVersions = IMod.ACMCVERSION
)
public class Sniffer {

    @SidedProxy(clientSide = IMod.CLIENT_PROXY_CLASS, serverSide = IMod.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.loadConfig(event.getModConfigurationDirectory());
        proxy.registKeyBinding();
        proxy.registCommand();
    }

    @EventHandler
    public void Init(FMLInitializationEvent event) {
        proxy.init();
        proxy.registEventHandler();
    }
}
