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
import org.soraworld.sniffer.command.*;
import org.soraworld.sniffer.constant.Constants;
import org.soraworld.sniffer.handler.EventBusHandler;
import org.soraworld.sniffer.handler.FMLEventHandler;
import org.soraworld.sniffer.util.I19n;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        CommandManager.INSTANCE.registerCommand(new CommandTarget());
        CommandManager.INSTANCE.registerCommand(new CommandSub());
        CommandManager.INSTANCE.registerCommand(new ISubCommand() {
            @Override
            public String name() {
                return "reload";
            }

            @Override
            public List<String> aliases() {
                return Collections.emptyList();
            }

            @Override
            public void execute(ArrayList<String> args) {
                api.reload();
                I19n.sendChat("sf.reload");
            }

            @Override
            public List<String> tabCompletions(ArrayList<String> args) {
                return Collections.emptyList();
            }
        });
        CommandManager.INSTANCE.registerCommand(new ISubCommand() {
            @Override
            public String name() {
                return "reset";
            }

            @Override
            public List<String> aliases() {
                return Collections.emptyList();
            }

            @Override
            public void execute(ArrayList<String> args) {
                api.reset();
                I19n.sendChat("sf.reset");
            }

            @Override
            public List<String> tabCompletions(ArrayList<String> args) {
                return Collections.emptyList();
            }
        });
        CommandManager.INSTANCE.registerCommand(new ISubCommand() {
            @Override
            public String name() {
                return "gamma";
            }

            @Override
            public List<String> aliases() {
                return Collections.singletonList("g");
            }

            @Override
            public void execute(ArrayList<String> args) {
                if (args.isEmpty()) {
                    I19n.sendChat("sf.gamma.get", api.getGamma());
                    return;
                }
                if (Constants.PATTERN_NUM.matcher(args.get(0)).matches()) {
                    api.setGamma(Integer.valueOf(args.get(0)));
                    I19n.sendChat("sf.gamma.set", api.getGamma());
                } else {
                    I19n.sendChat("sf.invalid.num");
                }
            }

            @Override
            public List<String> tabCompletions(ArrayList<String> args) {
                return Collections.emptyList();
            }
        });
    }

    @EventHandler
    public void Init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventBusHandler());
        MinecraftForge.EVENT_BUS.register(new FMLEventHandler());
    }
}
