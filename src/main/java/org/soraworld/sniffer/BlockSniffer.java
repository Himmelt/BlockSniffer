package org.soraworld.sniffer;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import org.soraworld.sniffer.api.SnifferAPI;
import org.soraworld.sniffer.command.CommandSniffer;
import org.soraworld.sniffer.constant.Constants;
import org.soraworld.sniffer.handler.EventBusHandler;
import org.soraworld.sniffer.handler.FMLEventHandler;

@Mod(
        modid = Constants.MODID,
        name = Constants.NAME,
        version = Constants.VERSION,
        acceptedMinecraftVersions = Constants.ACMCVERSION
)
@SideOnly(Side.CLIENT)
public class BlockSniffer {

    private static SnifferAPI api;

    public static SnifferAPI getAPI() {
        return api;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        api = new SnifferAPI(event.getModConfigurationDirectory());
        ClientRegistry.registerKeyBinding(Constants.KEY_SWITCH);
        ClientCommandHandler.instance.registerCommand(new CommandSniffer());
    }

    @EventHandler
    public void Init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventBusHandler());
        FMLCommonHandler.instance().bus().register(new FMLEventHandler());
    }
}
