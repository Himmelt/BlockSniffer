package org.soraworld.sniffer.gui;

import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
class ParticleFX extends Particle {

    private final Vec3d destination;

    ParticleFX(World world, Vec3d from, Vec3d to, Vec3f rgb, int life) {
        super(world, from.xCoord, from.yCoord, from.zCoord, 0, 0, 0);
        this.particleRed = rgb.x;
        this.particleGreen = rgb.y;
        this.particleBlue = rgb.z;
        this.particleAlpha = 0.8F;
        this.setParticleTextureIndex(147);
        this.setSize(0.02F, 0.02F);
        this.particleScale = 1.0F;
        this.particleMaxAge = life * 20;
        this.destination = to;
        this.canCollide = false;
        double dx = to.xCoord - from.xCoord;
        double dy = to.yCoord - from.yCoord;
        double dz = to.zCoord - from.zCoord;
        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        this.motionX = dx / dist * 0.01D;
        this.motionY = dy / dist * 0.01D;
        this.motionZ = dz / dist * 0.01D;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        if (this.particleMaxAge-- <= 0) {
            this.setExpired();
        }
        double dx = this.posX - destination.xCoord;
        double dy = this.posY - destination.yCoord;
        double dz = this.posZ - destination.zCoord;
        if (Math.sqrt(dx * dx + dy * dy + dz * dz) < 0.15) this.setExpired();
    }
}
