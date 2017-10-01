package org.soraworld.sniffer.gui;

import net.minecraft.client.particle.ParticleSuspendedTown;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
class ParticleFX extends ParticleSuspendedTown {

    private final Vec3d destination;

    ParticleFX(World world, Vec3d from, Vec3d to, Vec3f rgb, int life) {
        super(world, from.x, from.y, from.z, 0, 0, 0);
        this.particleRed = rgb.x;
        this.particleGreen = rgb.y;
        this.particleBlue = rgb.z;
        this.setParticleTextureIndex(147);
        this.setSize(0.02F, 0.02F);
        this.particleScale = 1.0F;
        this.particleMaxAge = life * 20;
        this.destination = to;
        double dx = to.x - from.x;
        double dy = to.y - from.y;
        double dz = to.z - from.z;
        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        double vx = dx / dist * 0.015D;
        double vy = dy / dist * 0.015D;
        double vz = dz / dist * 0.015D;
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
        double dx = this.posX - destination.x;
        double dy = this.posY - destination.y;
        double dz = this.posZ - destination.z;
        if (Math.sqrt(dx * dx + dy * dy + dz * dz) < 0.15) this.setExpired();
    }
}
