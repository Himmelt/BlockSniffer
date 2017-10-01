package org.soraworld.sniffer.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class GuiRender {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void renderItem(ItemStack itemStack, int x, int y) {
        //RenderHelper.enableRescaleNormal();
        RenderHelper.enableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, itemStack, x, y);
        RenderHelper.disableStandardItemLighting();
    }

    public static void drawRect(int x, int y, int width, int height, int color) {
        GL11.glPushMatrix();
        GL11.glPushClientAttrib(-1);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        double red = (double) (color >> 16 & 255) / 255.0D;
        double green = (double) (color >> 8 & 255) / 255.0D;
        double blue = (double) (color & 255) / 255.0D;
        double alpha = (double) (color >> 24 & 255) / 255.0D;
        GL11.glColor4d(red, green, blue, alpha);
        GL11.glBegin(7);
        GL11.glVertex2i(x, y);
        GL11.glVertex2i(x, y + height);
        GL11.glVertex2i(x + width, y + height);
        GL11.glVertex2i(x + width, y);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glPopClientAttrib();
        GL11.glPopMatrix();
    }

    public static void spawnParticle(EntityPlayer player, Vec3d to, Color color, int lifetime) {
        Vec3 look = player.getLookVec();
        Vec3d src = new Vec3d(look.xCoord + player.posX, look.yCoord + player.posY + player.getEyeHeight(), look.zCoord + player.posZ);
        Vec3f rgb = new Vec3f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F);
        spawnParticle(player.getEntityWorld(), src, to, rgb, lifetime);
    }

    private static void spawnParticle(World world, Vec3d src, Vec3d to, Vec3f rgb, int lifetime) {
        mc.effectRenderer.addEffect(new ParticleFX(world, src, to, rgb, lifetime));
        double dx = to.x - src.x;
        double dy = to.y - src.y;
        double dz = to.z - src.z;
        double steps = Math.max(Math.abs(dx), Math.max(Math.abs(dy), Math.abs(dz))) * 3.0D;
        for (int i = 0; i < steps; ++i) {
            Vec3d from = new Vec3d(src.x + dx / steps * i, src.y + dy / steps * i, src.z + dz / steps * i);
            mc.effectRenderer.addEffect(new ParticleFX(world, from, to, rgb, lifetime));
        }
    }
}
