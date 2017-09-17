package org.soraworld.sniffer.handler;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.soraworld.sniffer.BlockSniffer;
import org.soraworld.sniffer.api.SnifferAPI;

@SideOnly(Side.CLIENT)
public class FMLEventHandler {

    private final SnifferAPI api = BlockSniffer.getAPI();

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        if (api.mc.currentScreen == null && api.KEY_SWITCH.isPressed()) {
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
