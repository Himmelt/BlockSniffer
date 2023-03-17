package org.soraworld.sniffer.gui;

import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.Vec3d;
import org.soraworld.sniffer.util.Color3f;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
class ParticleFX extends Particle {

    private final Vec3d destination;

    ParticleFX(World world, Vec3d from, Vec3d to, Color3f rgb, int life) {
        super(world, from.x, from.y, from.z, 0, 0, 0);
        this.particleRed = rgb.red;
        this.particleGreen = rgb.blue;
        this.particleBlue = rgb.green;
        this.particleAlpha = 0.8F;
        this.setParticleTextureIndex(147);
        this.setSize(0.02F, 0.02F);
        this.particleScale = 1.0F;
        this.particleMaxAge = life * 20;
        this.destination = to;
        this.canCollide = false;
        double dx = to.x - from.x;
        double dy = to.y - from.y;
        double dz = to.z - from.z;
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
        double dx = this.posX - destination.x;
        double dy = this.posY - destination.y;
        double dz = this.posZ - destination.z;
        if (Math.sqrt(dx * dx + dy * dy + dz * dz) < 0.15) {
            this.setExpired();
        }
    }
}
