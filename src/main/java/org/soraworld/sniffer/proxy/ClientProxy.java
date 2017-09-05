package org.soraworld.sniffer.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.soraworld.sniffer.client.command.CommandSniffer;
import org.soraworld.sniffer.client.gui.HudRenderer;
import org.soraworld.sniffer.config.Config;
import org.soraworld.sniffer.core.BlockSniffer;
import org.soraworld.sniffer.handler.EventBusHandler;
import org.soraworld.sniffer.handler.FMLEventHandler;

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
