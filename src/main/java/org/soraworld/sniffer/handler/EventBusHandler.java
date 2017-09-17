package org.soraworld.sniffer.handler;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.soraworld.sniffer.BlockSniffer;
import org.soraworld.sniffer.api.SnifferAPI;

@SideOnly(Side.CLIENT)
public class EventBusHandler {

    private final SnifferAPI api = BlockSniffer.getAPI();

    // TODO 减少触发
    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent event) {
        if (event.getHand() == EnumHand.MAIN_HAND
                && (event instanceof PlayerInteractEvent.LeftClickBlock
                || event instanceof PlayerInteractEvent.RightClickBlock
                || event instanceof PlayerInteractEvent.RightClickEmpty)
                && api.active && api.timeout()) {
            EntityPlayer player = event.getEntityPlayer();
            new Thread(() -> {
                api.scanWorld(player);
                if (api.result.found) {
                    api.spawnParticle(player);
                }
            }).start();
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (api.active && api.current != null && api.timein() && !event.isCancelable() && event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            ScaledResolution scale = new ScaledResolution(api.mc);
            String label = String.format("%s: ---", api.current.displayName());
            if (api.result.found && api.result.getDistance() >= 1.0D) {
                label = String.format("%s: %.2f", api.result.getItemStack().getDisplayName(), api.result.getDistance() - 1.0D);
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
            if (api.result.found) {
                api.renderItem(api.result.getItemStack(), x, y);
            } else {
                api.renderItem(api.current.getDelegate().getItemStack(), x, y);
            }
            int maxX = width - lbWidth;
            int lbX = Math.max(0, Math.min(x, maxX));
            int lbY = y + iconHeight;
            api.mc.fontRenderer.drawString(label, lbX, lbY, 16777215);
        }
    }
}
