package org.soraworld.sniffer;

import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.soraworld.sniffer.api.SnifferAPI;
import org.soraworld.sniffer.command.CommandSniffer;
import org.soraworld.sniffer.constant.Constants;
import org.soraworld.sniffer.handler.EventBusHandler;
import org.soraworld.sniffer.handler.FMLEventHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Mod(
        modid = Constants.MODID,
        name = Constants.NAME,
        version = Constants.VERSION,
        acceptedMinecraftVersions = Constants.ACMCVERSION
)
@SideOnly(Side.CLIENT)
public class BlockSniffer {

    private final SnifferAPI api;
    private final ICommand command;
    private final FMLEventHandler fmlHandler;
    private final EventBusHandler busHandler;

    public BlockSniffer() {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        this.api = context.getBean(SnifferAPI.class);
        this.command = context.getBean(CommandSniffer.class);
        this.fmlHandler = context.getBean(FMLEventHandler.class);
        this.busHandler = context.getBean(EventBusHandler.class);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        api.setConfigPath(event.getModConfigurationDirectory(), "targets.json");
        ClientRegistry.registerKeyBinding(Constants.KEY_SWITCH);
        ClientCommandHandler.instance.registerCommand(command);
    }

    @EventHandler
    public void Init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(fmlHandler);
        MinecraftForge.EVENT_BUS.register(busHandler);
    }
}
