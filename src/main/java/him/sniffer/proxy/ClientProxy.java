package him.sniffer.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import him.sniffer.config.Config;
import him.sniffer.handler.EventBusHandler;
import him.sniffer.handler.FMLEventHandler;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void loadConfig(File cfgDir) {
        config = new Config(cfgDir);
    }

    @Override
    public void registKeyBinding() {
        ClientRegistry.registerKeyBinding(keySwitch);
    }

    @Override
    public void registEventHandler() {
        MinecraftForge.EVENT_BUS.register(new EventBusHandler());
        FMLCommonHandler.instance().bus().register(new FMLEventHandler());
    }
}
