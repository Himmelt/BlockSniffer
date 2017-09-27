package org.soraworld.sniffer.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class GuiRender {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void renderItem(ItemStack itemStack, int x, int y) {
        GlStateManager.enableRescaleNormal();
        //RenderHelper.enableGUIStandardItemLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y);
        //RenderHelper.disableStandardItemLighting();
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

    public static void spawnParticle(EntityPlayer player, Vec3d to, Color color, int delay) {
        Vec3d look = player.getLookVec();
        double fromX = look.xCoord + player.posX;
        double fromY = look.yCoord + player.posY + player.getEyeHeight();
        double fromZ = look.zCoord + player.posZ;
        spawn(player.getEntityWorld(), fromX, fromY, fromZ, to.xCoord, to.yCoord, to.zCoord, color, delay);
    }

    private static void spawn(World worldObj, double fromX, double fromY, double fromZ, double toX, double toY, double toZ, Color color, int delay) {
        spawnSingleParticle(worldObj, 0.5D + toX, 0.5D + toY, 0.5D + toZ, 1.0F, color, 0.0D, 0.0D, 0.0D, delay);
        intSpawnParticleTrail(worldObj, fromX, fromY, fromZ, toX + 0.5D, toY + 0.5D, toZ + 0.5D, color, delay);
    }

    private static void intSpawnParticleTrail(World theWorld, double fromX, double fromY, double fromZ, double toX, double toY, double toZ, Color color, int delay) {
        double dx = toX - fromX;
        double dy = toY - fromY;
        double dz = toZ - fromZ;
        double steps = Math.max(Math.abs(dx), Math.max(Math.abs(dy), Math.abs(dz))) * 3.0D;
        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        double strength = 0.8D;
        double ds = strength / 30.0D;
        double vx = dx / dist * 0.015D;
        double vy = dy / dist * 0.015D;
        double vz = dz / dist * 0.015D;

        for (int i = 0; i < steps; ++i) {
            double x = fromX + dx / steps * i;
            double y = fromY + dy / steps * i;
            double z = fromZ + dz / steps * i;
            strength -= ds;
            if (strength < 0.2D) {
                strength = 0.2D;
            }
            spawnSingleParticle(theWorld, x, y, z, (float) strength, color, vx, vy, vz, delay);
        }
    }

    private static void spawnSingleParticle(World theWorld, double x, double y, double z, float alpha, Color color, double vx, double vy, double vz, int delay) {
        mc.effectRenderer.addEffect(new ParticleFX(theWorld, x, y, z, vx, vy, vz, color, alpha, delay));
    }
}
