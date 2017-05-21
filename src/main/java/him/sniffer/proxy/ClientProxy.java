package him.sniffer.proxy;

import him.sniffer.client.command.CommandSniffer;
import him.sniffer.client.gui.HudRenderer;
import him.sniffer.config.Config;
import him.sniffer.core.BlockSniffer;
import him.sniffer.handler.EventBusHandler;
import him.sniffer.handler.FMLEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void init() {
        client = Minecraft.getMinecraft();
        sniffer = new BlockSniffer();
        hudRenderer = new HudRenderer();
    }

    @Override
    public void loadConfig(File cfgDir) {
        config = new Config(cfgDir);
    }

    @Override
    public void registCommand() {
        ClientCommandHandler.instance.registerCommand(new CommandSniffer());
    }

    @Override
    public void registKeyBinding() {
        ClientRegistry.registerKeyBinding(keySwitch);
    }

    @Override
    public void registEventHandler() {
        MinecraftForge.EVENT_BUS.register(new EventBusHandler());
        MinecraftForge.EVENT_BUS.register(new FMLEventHandler());
    }

    @Override
    public synchronized void addChatMessage(String key, Object... objects) {
        if (client != null && client.player != null) {
            client.player.sendMessage(new TextComponentString(I18n.format("sf.chat.head") + ' ' + I18n.format(key, objects)));
        }
    }
}
