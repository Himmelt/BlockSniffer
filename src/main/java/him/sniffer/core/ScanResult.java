package him.sniffer.core;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.awt.Color;

public class ScanResult {

    private final int meta;
    private final Block block;
    private final EntityPlayer player;
    private final Target target;

    public final int x, y, z;

    public ScanResult(EntityPlayer player, Block block, Target target, int meta, int x, int y, int z) {
        this.player = player;
        this.block = block;
        this.target = target;
        this.meta = meta;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ItemStack getItemStack() {
        return new ItemStack(block, 1, meta);
    }

    public double getDistance() {
        double lx = player.posX - x;
        double ly = player.posY - y;
        double lz = player.posZ - z;
        return Math.sqrt(lx * lx + ly * ly + lz * lz);
    }

    public Color getColor() {
        return target.getColor() != null? target.getColor() : new Color(block.getMapColor(meta).colorValue);
    }
}
