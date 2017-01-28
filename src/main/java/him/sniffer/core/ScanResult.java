package him.sniffer.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.awt.Color;

public class ScanResult {

    private final Target target;
    private final EntityPlayer player;
    private final TBlock block;

    public final int x, y, z;

    public ScanResult(EntityPlayer player, Target target, int x, int y, int z) {
        this.player = player;
        this.target = target;
        block = target.getDelegate();
        this.x = x;
        this.y = y;
        this.z = z;
    }

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
        return target.getColor() != null? target.getColor() : new Color(block.getBlock().getMapColor(block.getMeta()).colorValue);
    }
}
