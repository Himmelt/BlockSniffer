package him.sniffer.client.gui;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import him.sniffer.core.ScanResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import static him.sniffer.Sniffer.*;

@SideOnly(Side.CLIENT)
public class SnifferHud {

    private final HudRenderer itemRenderer = new HudRenderer();

    private static double getHudX() {
        return proxy.config.hudX.get();
    }

    private static double getHudY() {
        return proxy.config.hudY.get();
    }

    public void draw() {
        Minecraft mc = FMLClientHandler.instance().getClient();
        ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        String name = proxy.sniffer.getTarget().getName();
        String label = String.format("%s: ---", name);
        ScanResult result = proxy.sniffer.result;
        if (result != null && result.getDistance() >= 1.0D) {
            label = String.format("%s: %.2f", result.getItemStack().getDisplayName(), result.getDistance() - 1.0D);
        }
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        byte iconHeight = 20;
        byte iconWidth = 20;
        byte lbHeight = 10;
        FontRenderer fontrenderer = mc.fontRenderer;
        int lbWidth = fontrenderer.getStringWidth(label + ' ');
        int x = (int) (getHudX() * (width - iconWidth));
        int y = (int) (getHudY() * (height - iconHeight - lbHeight));
        mc.entityRenderer.setupOverlayRendering();
        drawRect(x - 2, y - 2, 20, 20, 1342177280);
        if (result != null) {
            itemRenderer.renderItem(result.getItemStack(), x, y);
        } else {
            itemRenderer.renderItem(proxy.sniffer.getTarget().getDelegate().getItemStack(), x, y);
        }
        int maxX = width - lbWidth;
        int lbX = Math.max(0, Math.min(x, maxX));
        int lbY = y + iconHeight;
        fontrenderer.drawString(label, lbX, lbY, 16777215);
    }

    private static void drawRect(int x, int y, int width, int height, int color) {
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        double red = (color >> 16 & 255) / 255.0D;
        double green = (color >> 8 & 255) / 255.0D;
        double blue = (color & 255) / 255.0D;
        double alpha = (color >> 24 & 255) / 255.0D;
        GL11.glColor4d(red, green, blue, alpha);
        GL11.glBegin(7);
        GL11.glVertex2i(x, y);
        GL11.glVertex2i(x, y + height);
        GL11.glVertex2i(x + width, y + height);
        GL11.glVertex2i(x + width, y);
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glEnable(3553);
    }
}
