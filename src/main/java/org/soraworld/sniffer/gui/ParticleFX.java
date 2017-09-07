package org.soraworld.sniffer.gui;

import net.minecraft.client.particle.ParticleSuspendedTown;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class ParticleFX extends ParticleSuspendedTown {

    ParticleFX(World world, double x, double y, double z, double vx, double vy, double vz, Color color, float alpha) {
        super(world, x, y, z, vx, vy, vz);
        this.particleAlpha = alpha;
        this.setParticleTextureIndex(147);
        float[] colorComponents = new float[3];
        color.getColorComponents(colorComponents);
        this.setRBGColorF(colorComponents[0], colorComponents[1], colorComponents[2]);
        this.setAlphaF(0.0F);
        this.setSize(0.02F, 0.02F);
        this.particleScale *= this.rand.nextFloat() * 0.6F + 0.5F;
        this.particleMaxAge = (int) (20.0D / (Math.random() * 0.8D + 0.2D));
        this.setVelocity(vx, vy, vz);
    }

    private void setVelocity(double vx, double vy, double vz) {
        this.motionX = vx;
        this.motionY = vy;
        this.motionZ = vz;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.move(this.motionX, this.motionY, this.motionZ);
        if (this.particleMaxAge-- <= 0) {
            this.setExpired();
        }
    }
}