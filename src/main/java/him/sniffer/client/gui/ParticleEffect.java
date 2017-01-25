package him.sniffer.client.gui;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityAuraFX;
import net.minecraft.world.World;

import java.awt.Color;

@SideOnly(Side.CLIENT)
public class ParticleEffect {

    private final Minecraft minecraft = FMLClientHandler.instance().getClient();
    private final float[] colorComponents = new float[3];

    public void spawn(World worldObj, double fromX, double fromY, double fromZ, double toX, double toY,
                      double toZ, Color color) {
        spawnSingleParticle(worldObj, 0.5D + toX, 0.5D + toY, 0.5D + toZ, 1.0F, color, 0.0D, 0.0D, 0.0D);
        intSpawnParticleTrail(worldObj, fromX, fromY, fromZ, toX + 0.5D, toY + 0.5D, toZ + 0.5D, color);
    }

    private void intSpawnParticleTrail(World theWorld, double fromX, double fromY, double fromZ, double toX, double toY,
                                       double toZ, Color color) {
        double dx = toX - fromX;
        double dy = toY - fromY;
        double dz = toZ - fromZ;
        double steps = Math.max(Math.abs(dx), Math.max(Math.abs(dy), Math.abs(dz))) * 3.0D;
        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        double strength = 0.5D;
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
            spawnSingleParticle(theWorld, x, y, z, (float) strength, color, vx, vy, vz);
        }
    }

    private void spawnSingleParticle(World theWorld, double x, double y, double z, float alpha, Color color, double vx,
                                     double vy, double vz) {
        EntityAuraFX effect = new EntityAuraFX(theWorld, x, y, z, vx, vy, vz);
        effect.setParticleTextureIndex(147);
        color.getColorComponents(colorComponents);
        effect.setRBGColorF(colorComponents[0], colorComponents[1], colorComponents[2]);
        effect.setAlphaF(alpha);
        effect.setVelocity(vx, vy, vz);
        minecraft.effectRenderer.addEffect(effect);
    }
}
