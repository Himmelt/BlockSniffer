package org.soraworld.sniffer.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class ScanResult {

    private Target target;
    private EntityPlayer player;
    private TBlock block;

    public int x, y, z;
    public boolean found = false;

    public double getDistance() {
        double lx = player.posX - x;
        double ly = player.posY - y;
        double lz = player.posZ - z;
        return Math.sqrt(lx * lx + ly * ly + lz * lz);
    }

    public ItemStack getItemStack() {
        return block.getItemStack();
    }

    public Color getColor() {
        Color color = target.getColor();
        return color != null ? color : block.getMapColor();
    }

    public void update(EntityPlayer player, Target current, int blockX, int blockY, int blockZ) {
        this.player = player;
        this.target = current;
        this.block = target.getDelegate();
        this.x = blockX;
        this.y = blockY;
        this.z = blockZ;
        this.found = true;
    }
}
