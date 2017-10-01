package org.soraworld.sniffer.core;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class ScanResult {

    public boolean found = false;
    private Target target;
    private EntityPlayer player;
    private int x, y, z;
    private ItemStack stack = new ItemStack(Items.AIR);

    public Vec3d center() {
        return new Vec3d(x + 0.5, y + 0.5, z + 0.5);
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
        BlockPos pos = new BlockPos(x, y, z);
        IBlockState state = player.world.getBlockState(pos);
        return new Color(state.getMapColor().colorValue);
    }

    public void update(EntityPlayer player, Target current, int blockX, int blockY, int blockZ) {
        this.player = player;
        this.target = current;
        this.x = blockX;
        this.y = blockY;
        this.z = blockZ;
        this.found = true;
        IBlockState state = player.world.getBlockState(new BlockPos(x, y, z));
        stack = state.getBlock().getPickBlock(state, null, player.world, new BlockPos(x, y, z), player);
        if (stack.getItem() == Items.AIR) {
            stack = new ItemStack(state.getBlock(), 1, state.getBlock().damageDropped(state));
        }
    }

    public String displayName() {
        if (stack.getItem() == Items.AIR) {
            return target.displayName();
        }
        return stack.getDisplayName();
    }

    public Block getBlock() {
        return player.world.getBlockState(new BlockPos(x, y, z)).getBlock();
    }

    public Integer getMeta() {
        IBlockState state = player.world.getBlockState(new BlockPos(x, y, z));
        return state.getBlock().getMetaFromState(state);
    }
}
