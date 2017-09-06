package org.soraworld.sniffer.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static org.soraworld.sniffer.BlockSniffer.proxy;

@SideOnly(Side.CLIENT)
public class FMLEventHandler {
    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if (screen == null && proxy.keySwitch.isPressed()) {
            //IMod.logger.info("------------proxy.sniffer.switchTarget();");
            proxy.sniffer.switchTarget();
        }
    }
    @SubscribeEvent
    public void onClientConnect(ClientConnectedToServerEvent event) {
        proxy.config.reload();
    }

    @SubscribeEvent
    public void onClientDisconnect(ClientDisconnectionFromServerEvent event) {
        proxy.config.save();
    }

}
