package org.soraworld.sniffer.handler;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.soraworld.sniffer.client.gui.HudRenderer;
import org.soraworld.sniffer.core.BlockSniffer;
import org.soraworld.sniffer.core.ScanResult;
import org.soraworld.sniffer.core.Target;

import static org.soraworld.sniffer.Sniffer.proxy;

@SideOnly(Side.CLIENT)
public class EventBusHandler {
    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        final BlockSniffer sniffer = proxy.sniffer;
        if (event.getWorld() instanceof WorldClient) {
            if (sniffer.isActive() && sniffer.last + sniffer.delay < System.currentTimeMillis()) {
                final EntityPlayer player = event.getEntityPlayer();
                sniffer.last = System.currentTimeMillis();
                new Thread(() -> {
                    //logger.info("Sniffer Thread Started!");
                    sniffer.scanWorld(player);
                    if (sniffer.result != null) {
                        //logger.info("Sniffer Found Target!");
                        sniffer.spawn(player, sniffer.result);
                    }
                }).start();
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (!event.isCancelable() && event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            if (proxy.sniffer.last + proxy.sniffer.delay >= System.currentTimeMillis()) {
                Target target = proxy.sniffer.getTarget();
                ScanResult result = proxy.sniffer.result;
                ScaledResolution scale = new ScaledResolution(proxy.client);
                String label = String.format("%s: ---", target.displayName());
                if (result != null && result.getDistance() >= 1.0D) {
                    label = String.format("%s: %.2f", result.getItemStack().getDisplayName(), result.getDistance() - 1.0D);
                }
                int width = scale.getScaledWidth();
                int height = scale.getScaledHeight();
                int iconHeight = 20;
                int iconWidth = 20;
                int lbHeight = 10;
                FontRenderer fontrenderer = proxy.client.fontRendererObj;
                int lbWidth = fontrenderer.getStringWidth(label + " ");
                int x = (int) (proxy.config.hudX.get() * (width - iconWidth));
                int y = (int) (proxy.config.hudY.get() * (height - iconHeight - lbHeight));
                HudRenderer.drawRect(x - 2, y - 2, 20, 20, 1342177280);
                if (result != null) {
                    HudRenderer.renderItem(result.getItemStack(), x, y);
                } else {
                    HudRenderer.renderItem(target.getDelegate().getItemStack(), x, y);
                }
                int maxX = width - lbWidth;
                int lbX = Math.max(0, Math.min(x, maxX));
                int lbY = y + iconHeight;
                fontrenderer.drawString(label, lbX, lbY, 16777215);
            }
        }
    }

}
