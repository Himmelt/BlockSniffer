package org.soraworld.sniffer.handler;

import net.minecraft.client.Minecraft;
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
import org.soraworld.sniffer.gui.GuiRender;

@SideOnly(Side.CLIENT)
public class EventBusHandler {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private final SnifferAPI api = BlockSniffer.getAPI();

    @SubscribeEvent
    public void onPlayerClick(PlayerInteractEvent event) {
        if (event.getHand() == EnumHand.MAIN_HAND
                && (event instanceof PlayerInteractEvent.LeftClickBlock
                || event instanceof PlayerInteractEvent.RightClickBlock
                || event instanceof PlayerInteractEvent.RightClickEmpty)
                && api.active && api.clickTimeOut()) {
            EntityPlayer player = event.getEntityPlayer();
            new Thread(() -> {
                api.scanWorld(player);
                if (api.result.found) {
                    GuiRender.spawnParticle(player, api.result.getV3d(), api.result.getColor(), api.config.particleDelay.get());
                }
            }).start();
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (api.active && api.current != null && api.guiInTime() && !event.isCancelable() && event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            ScaledResolution scale = new ScaledResolution(mc);
            String label = String.format("%s: ---", api.current.displayName());
            if (api.result.found && api.result.getDistance() >= 1.0D) {
                label = String.format("%s: %.2f", api.result.displayName(), api.result.getDistance() - 1.0D);
            }
            int width = scale.getScaledWidth();
            int height = scale.getScaledHeight();
            int iconHeight = 20;
            int iconWidth = 20;
            int lbHeight = 10;
            int lbWidth = mc.fontRenderer.getStringWidth(label + " ");
            int x = (int) (api.config.hudX.get() * (width - iconWidth));
            int y = (int) (api.config.hudY.get() * (height - iconHeight - lbHeight));
            GuiRender.drawRect(x - 2, y - 2, 20, 20, 0xffdddddd);
            if (api.result.found) {
                GuiRender.renderItem(api.result.getItemStack(), x, y);
            } else {
                GuiRender.renderItem(api.current.getDelegate().getItemStack(), x, y);
            }
            int maxX = width - lbWidth;
            int lbX = Math.max(0, Math.min(x, maxX));
            int lbY = y + iconHeight;
            mc.fontRenderer.drawString(label, lbX, lbY, 0xfffeff);
        }
    }
}
