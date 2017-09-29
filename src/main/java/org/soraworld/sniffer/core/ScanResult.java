package org.soraworld.sniffer.core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class ScanResult {

    public boolean found = false;
    private Target target;
    private EntityPlayer player;
    private int x, y, z;
    private ItemStack stack = new ItemStack(Blocks.air);

    public Vec3 getV3d() {
        return Vec3.createVectorHelper(x, y, z);
    }

    public double getDistance() {
        double lx = player.posX - x;
        double ly = player.posY - y;
        double lz = player.posZ - z;
        return Math.sqrt(lx * lx + ly * ly + lz * lz);
    }

    public ItemStack getItemStack() {
        return stack;
    }

    public Color getColor() {
        Color color = target.getColor();
        if (color != null) return color;
        return new Color(player.worldObj.getBlock(x, y, z).getMapColor(player.worldObj.getBlockMetadata(x, y, z)).colorValue);
    }

    public void update(EntityPlayer player, Target current, int blockX, int blockY, int blockZ) {
        this.player = player;
        this.target = current;
        this.x = blockX;
        this.y = blockY;
        this.z = blockZ;
        this.found = true;
        stack = player.worldObj.getBlock(x, y, z).getPickBlock(Minecraft.getMinecraft().objectMouseOver, player.worldObj, x, y, z, player);
        if (stack == null || stack.getItem() == null) {
            stack = new ItemStack(player.worldObj.getBlock(x, y, z), 1, player.worldObj.getBlockMetadata(x, y, z));
        }
    }

    public String displayName() {
        if (stack == null || stack.getItem() == null) {
            return target.displayName();
        }
        return stack.getDisplayName();
    }

    public Block getBlock() {
        return player.worldObj.getBlock(x, y, z);
    }

    public Integer getMeta() {
        return player.worldObj.getBlockMetadata(x, y, z);
    }
}
