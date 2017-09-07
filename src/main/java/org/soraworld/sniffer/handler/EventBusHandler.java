package org.soraworld.sniffer.handler;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.soraworld.sniffer.BlockSniffer;
import org.soraworld.sniffer.api.SnifferAPI;
import org.soraworld.sniffer.core.ScanResult;
import org.soraworld.sniffer.core.Target;

@SideOnly(Side.CLIENT)
public class EventBusHandler {

    private final SnifferAPI api = BlockSniffer.getAPI();

    // TODO 减少触发
    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent event) {
        if (api.active && api.update()) {
            EntityPlayer player = event.getEntityPlayer();
            api.last = System.currentTimeMillis();
            new Thread(() -> {
                api.scanWorld(player);
                if (api.result != null) {
                    api.spawnParticle(player);
                }
            }).start();
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (api.active && !api.update() && !event.isCancelable() && event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            Target target = api.current;
            ScanResult result = api.result;
            ScaledResolution scale = new ScaledResolution(api.mc);
            String label = String.format("%s: ---", target.displayName());
            if (result != null && result.getDistance() >= 1.0D) {
                label = String.format("%s: %.2f", result.getItemStack().getDisplayName(), result.getDistance() - 1.0D);
            }
            int width = scale.getScaledWidth();
            int height = scale.getScaledHeight();
            int iconHeight = 20;
            int iconWidth = 20;
            int lbHeight = 10;
            int lbWidth = api.mc.fontRenderer.getStringWidth(label + " ");
            int x = (int) (api.config.hudX.get() * (width - iconWidth));
            int y = (int) (api.config.hudY.get() * (height - iconHeight - lbHeight));
            api.drawRect(x - 2, y - 2, 20, 20, 1342177280);
            if (result != null) {
                api.renderItem(result.getItemStack(), x, y);
            } else {
                api.renderItem(target.getDelegate().getItemStack(), x, y);
            }
            int maxX = width - lbWidth;
            int lbX = Math.max(0, Math.min(x, maxX));
            int lbY = y + iconHeight;
            api.mc.fontRenderer.drawString(label, lbX, lbY, 16777215);
        }
    }

}
