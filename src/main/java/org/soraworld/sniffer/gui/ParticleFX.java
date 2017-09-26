package org.soraworld.sniffer.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntityAuraFX;
import net.minecraft.world.World;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class ParticleFX extends EntityAuraFX {

    private float targetAlpha;

    ParticleFX(World world, double x, double y, double z, double vx, double vy, double vz, Color color, float alpha, int delay) {
        super(world, x, y, z, vx, vy, vz);
        this.targetAlpha = alpha;
        this.setParticleTextureIndex(147);
        float[] colorComponents = new float[3];
        color.getColorComponents(colorComponents);
        this.setRBGColorF(colorComponents[0], colorComponents[1], colorComponents[2]);
        this.setAlphaF(0.0F);
        this.setSize(0.02F, 0.02F);
        this.particleScale *= this.rand.nextFloat() * 0.6F + 0.5F;
        this.particleMaxAge = delay * 20;
        this.setVelocity(vx, vy, vz);
    }

    @Override
    public void setVelocity(double vx, double vy, double vz) {
        this.motionX = vx;
        this.motionY = vy;
        this.motionZ = vz;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
    }

    @Override
    public void onUpdate() {
        if (this.particleAlpha < this.targetAlpha) {
            this.setAlphaF(this.particleAlpha + 0.1F);
        }
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        if (this.particleMaxAge-- <= 0) {
            this.setDead();
        }
    }
}
