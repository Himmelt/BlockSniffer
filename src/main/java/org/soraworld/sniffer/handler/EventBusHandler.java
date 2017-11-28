package org.soraworld.sniffer.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.soraworld.sniffer.api.SnifferAPI;
import org.soraworld.sniffer.gui.GuiRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@SideOnly(Side.CLIENT)
public class EventBusHandler {

    private final Minecraft mc;
    private final SnifferAPI api;

    @Autowired
    public EventBusHandler(Minecraft mc, SnifferAPI api) {
        this.mc = mc;
        this.api = api;
    }

    @SubscribeEvent
    public void onPlayerClick(PlayerInteractEvent.LeftClickBlock event) {
        api.scanWorld();
    }

    @SubscribeEvent
    public void onPlayerClick(PlayerInteractEvent.RightClickBlock event) {
        api.scanWorld();
    }

    @SubscribeEvent
    public void onPlayerClick(PlayerInteractEvent.RightClickEmpty event) {
        api.scanWorld();
    }

    @SubscribeEvent
    public void onPlayerClick(PlayerInteractEvent.RightClickItem event) {
        api.scanWorld();
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
                ItemStack stack = api.result.getItemStack();
                if (stack != null && stack.getItem() != null) GuiRender.renderItem(stack, x, y);
            } else {
                ItemStack stack = api.current.getDelegate().getItemStack();
                if (stack != null && stack.getItem() != null) GuiRender.renderItem(stack, x, y);
            }
            int maxX = width - lbWidth;
            int lbX = Math.max(0, Math.min(x, maxX));
            int lbY = y + iconHeight;
            mc.fontRenderer.drawString(label, lbX, lbY, 0xfffeff);
        }
    }
}
