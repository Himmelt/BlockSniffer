package org.soraworld.sniffer.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import org.soraworld.sniffer.BlockSniffer;
import org.soraworld.sniffer.api.SnifferAPI;
import org.soraworld.sniffer.constant.Constants;

@SideOnly(Side.CLIENT)
public class FMLEventHandler {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private final SnifferAPI api = BlockSniffer.getAPI();

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
