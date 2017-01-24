package him.sniffer.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import him.sniffer.config.Target;
import him.sniffer.config.TargetJson;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;

import java.awt.Color;
import java.util.Iterator;

import static him.sniffer.Sniffer.*;
import static him.sniffer.constant.ModInfo.*;

@SideOnly(Side.CLIENT)
public class BlockSniffer {

    private Target target;
    private boolean active;
    private Iterator<Target> iterator;


    public long last;
    public TargetJson targetJson;
    public static final long delay = 500;
    public final SnifferHud Hud = new SnifferHud();
    public final ParticleEffect particle = new ParticleEffect();
    public ScanResult result;
    public boolean forbid;

    public void spawnParticles(World worldObj, double fromX, double fromY, double fromZ, double toX, double toY,
                               double toZ, Color color) {
        particle.spawn(worldObj, fromX, fromY, fromZ, toX, toY, toZ, color);
    }

    public Target getTarget() {
        return target;
    }

    public void reset() {
        active = false;
        iterator = null;
        target = null;
        result = null;
    }

    public void removeTarget() {

    }

    public void switchTarget() {
        if (iterator == null) {
            iterator = targetJson.iterator();
        }
        if (iterator.hasNext()) {
            result = null;
            last = System.currentTimeMillis();
            if (!active) {
                active = true;
                if (target == null) {
                    target = iterator.next();
                }
                proxy.addChatMessage(I18n.format("sniffer.chat.avtive"));
            } else {
                target = iterator.next();
            }
        } else {
            reset();
            proxy.addChatMessage(I18n.format("sniffer.chat.inactive"));
        }
    }

    public void scanWorld(EntityPlayer player) {
        result = null;
        if (target != null && player != null) {
            int chunkX = player.chunkCoordX;
            int chunkZ = player.chunkCoordZ;
            int hRange = target.hRange;
            int length = RANGE.length;
            for (int i = 0; i < length && RANGE[i][0] >= -hRange && RANGE[i][0] <= hRange &&
                            RANGE[i][1] >= -hRange && RANGE[i][1] <= hRange; i++) {
                Chunk chunk = player.worldObj.getChunkFromChunkCoords(chunkX + RANGE[i][0], chunkZ + RANGE[i][1]);
                if (!(chunk instanceof EmptyChunk)) {
                    scanChunk(chunk, player);
                    if (result != null) {
                        return;
                    }
                }
            }
        }
    }

    private void scanChunk(Chunk chunk, EntityPlayer player) {
        int y_l = target.mode == 0? target.depth[0] : (int) (player.posY - target.vRange);
        int y_h = target.mode == 0? target.depth[1] : (int) (player.posY + target.vRange);
        for (int y = y_h; y > 0 && y < 255 && y >= y_l; y--) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    Block block = chunk.getBlock(x, y, z);
                    if (block.equals(Blocks.air)) {
                        continue;
                    }
                    int meta = chunk.getBlockMetadata(x, y, z);
                    if (target.match(block, meta)) {
                        int blockX = chunk.xPosition * 16 + x;
                        int blockZ = chunk.zPosition * 16 + z;
                        result = new ScanResult(player, block, meta, blockX, y, blockZ);
                        return;
                    }
                }
            }
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        proxy.addChatMessage(I18n.format("sniffer.chat.inactive"));
    }

    public void addTarget(Target target) {
        targetJson.addTarget(target);
    }

    public static class ScanResult {

        private final int meta;
        private final Block block;
        private final EntityPlayer player;
        public final int x, y, z;

        public ScanResult(EntityPlayer player, Block block, int meta, int x, int y, int z) {
            this.player = player;
            this.block = block;
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

        public Color getMapColor() {
            return new Color(block.getMapColor(meta).colorValue);
        }
    }
}
