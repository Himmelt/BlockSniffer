package org.soraworld.sniffer;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.soraworld.sniffer.command.CommandSniffer;
import org.soraworld.sniffer.constant.Constants;
import org.soraworld.sniffer.proxy.ClientProxy;
import org.soraworld.sniffer.schedule.Scheduler;

/**
 * @author Himmelt
 */
@Mod(
        modid = BlockSniffer.MODID,
        name = BlockSniffer.MOD_NAME,
        version = "1.3.0",
        acceptableRemoteVersions = "*",
        acceptedMinecraftVersions = "[1.12,1.12.2]",
        clientSideOnly = true
)
@SideOnly(Side.CLIENT)
public class BlockSniffer {

    public static final String MODID = "sniffer";
    public static final String MOD_NAME = "BlockSniffer";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ClientProxy proxy = new ClientProxy(event.getModConfigurationDirectory());
        ClientRegistry.registerKeyBinding(Constants.KEY_SWITCH);
        ClientCommandHandler.instance.registerCommand(new CommandSniffer());
        MinecraftForge.EVENT_BUS.register(Scheduler.class);
    }
}
