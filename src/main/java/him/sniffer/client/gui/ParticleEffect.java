package him.sniffer.client.gui;

import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

import static him.sniffer.Sniffer.proxy;

@SideOnly(Side.CLIENT)
public class ParticleEffect {

    private final ParticleFX.Factory factory = new ParticleFX.Factory();

    public void spawn(World worldObj, double fromX, double fromY, double fromZ, double toX, double toY, double toZ, Color color) {
        spawnSingleParticle(worldObj, 0.5D + toX, 0.5D + toY, 0.5D + toZ, 1.0F, color, 0.0D, 0.0D, 0.0D);
        intSpawnParticleTrail(worldObj, fromX, fromY, fromZ, toX + 0.5D, toY + 0.5D, toZ + 0.5D, color);
    }

    private void intSpawnParticleTrail(World theWorld, double fromX, double fromY, double fromZ, double toX, double toY, double toZ, Color color) {
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
            spawnSingleParticle(theWorld, x, y, z, (float) strength, color, vx, vy, vz);
        }
    }

    private void spawnSingleParticle(World theWorld, double x, double y, double z, float alpha, Color color, double vx, double vy, double vz) {
        int unused = 0;
        ParticleFX particleFX = factory.getEntityFX(unused, theWorld, x, y, z, vx, vy, vz, color, alpha);
        proxy.client.effectRenderer.addEffect(particleFX);
    }
}
