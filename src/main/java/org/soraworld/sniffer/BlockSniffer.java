package org.soraworld.sniffer;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Contract;
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

    @Contract(pure = true)
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
        MinecraftForge.EVENT_BUS.register(new FMLEventHandler());
    }
}
