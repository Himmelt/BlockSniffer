package org.soraworld.sniffer.handler;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.soraworld.sniffer.api.SnifferAPI;
import org.soraworld.sniffer.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@SideOnly(Side.CLIENT)
public class FMLEventHandler {

    private final Minecraft mc;
    private final SnifferAPI api;

    @Autowired
    public FMLEventHandler(Minecraft mc, SnifferAPI api) {
        this.mc = mc;
        this.api = api;
    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        if (mc.currentScreen == null && Constants.KEY_SWITCH.isPressed()) {
            api.switchTarget();
        }
    }

    @SubscribeEvent
    public void onClientConnect(ClientConnectedToServerEvent event) {
        api.reload();
    }

    @SubscribeEvent
    public void onClientDisconnect(ClientDisconnectionFromServerEvent event) {
        api.save();
    }
}
