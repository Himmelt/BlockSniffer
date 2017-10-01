package org.soraworld.sniffer.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntityAuraFX;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
class ParticleFX extends EntityAuraFX {

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
        this.noClip = true;
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

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        if (this.particleMaxAge-- <= 0) {
            this.setDead();
        }
        double dx = this.posX - destination.x;
        double dy = this.posY - destination.y;
        double dz = this.posZ - destination.z;
        if (Math.sqrt(dx * dx + dy * dy + dz * dz) < 0.15) this.setDead();
    }
}
