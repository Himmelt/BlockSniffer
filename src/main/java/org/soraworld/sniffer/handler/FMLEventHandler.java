package org.soraworld.sniffer.handler;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
