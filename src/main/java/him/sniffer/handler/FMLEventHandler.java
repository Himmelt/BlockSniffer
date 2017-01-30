package him.sniffer.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import him.sniffer.core.BlockSniffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import static him.sniffer.Sniffer.*;

@SideOnly(Side.CLIENT)
public class FMLEventHandler {
    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if (screen == null && proxy.keySwitch.isPressed()) {
            proxy.sniffer.switchTarget();
        }
    }

    @SubscribeEvent
    public void onTickEnd(TickEvent event) {
        BlockSniffer sniffer = proxy.sniffer;
        if (event.side == Side.CLIENT) {
            if (event.type == Type.RENDER && event.phase == Phase.END) {
                GuiScreen guiscreen = Minecraft.getMinecraft().currentScreen;
                if (guiscreen == null && sniffer.isActive() && sniffer.last + 2000L > System.currentTimeMillis()) {
                    sniffer.drawHUD();
                }
            }
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
