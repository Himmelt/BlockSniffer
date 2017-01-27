package him.sniffer.core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import him.sniffer.client.gui.ParticleEffect;
import him.sniffer.client.gui.SnifferHud;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;

import java.awt.Color;
import java.util.Iterator;

import static him.sniffer.Sniffer.*;
import static him.sniffer.constant.ModInfo.*;

@SideOnly(Side.CLIENT)
public class BlockSniffer {

    private boolean active;
    private Iterator<Target> iterator;
    private final ParticleEffect particle = new ParticleEffect();

    public long last;
    public Target target;
    public boolean forbid;
    public long delay = 500;
    public ScanResult result;
    public TargetJson targetJson;
    public final SnifferHud Hud = new SnifferHud();

    public void reset() {
        active = false;
        iterator = null;
        target = null;
        result = null;
    }

    public void switchTarget() {
        if (targetJson.size() >= 1) {
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
                    proxy.addChatMessage("sf.avtive");
                } else {
                    target = iterator.next();
                }
            } else {
                reset();
                proxy.addChatMessage("sf.inactive");
            }
        } else {
            proxy.addChatMessage("sf.empty");
        }
    }

    public void scanWorld(EntityPlayer player) {
        result = null;
        if (target != null && player != null) {
            int chunkX = player.chunkCoordX;
            int chunkZ = player.chunkCoordZ;
            int hRange = target.hRange;
            int length = RANGE.length;
            for (int i = 0; i < length && RANGE[i][0] >= -hRange && RANGE[i][0] <= hRange && RANGE[i][1] >= -hRange && RANGE[i][1] <= hRange; i++) {
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

    public void addTarget(Target target) {
        targetJson.addTarget(target);
    }

    public void ClearTarget() {
        reset();
        targetJson = new TargetJson();
        targetJson.checkout();
    }

    public void inActive() {
        active = false;
        proxy.addChatMessage("sf.inactive");
    }

    public void spawn(World w, double x1, double y1, double z1, double x2, double y2, double z2, Color c) {
        particle.spawn(w, x1, y1, z1, x2, y2, z2, c);
    }

    public boolean isActive() {
        return active;
    }

    public int removeTarget() {
        if (target != null && iterator != null) {
            logger.info("before:" + target.hashCode());
            iterator.remove();
            if (iterator.hasNext()) {
                logger.info("has next");
                result = null;
                last = System.currentTimeMillis();
                target = iterator.next();
            } else {
                logger.info("no next");
                reset();
            }
            logger.info("check out...active" + isActive());
            targetJson.checkout();
            if (!targetJson.contains(target)) {
                logger.info("not contains");
                reset();
            }
            logger.info("size after check:" + targetJson.size());
            if (targetJson.size() >= 1) {
                Target target = targetJson.iterator().next();
                System.out.println(target.hashCode());
            }
            return targetJson.size();
        }
        return -1;
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
                        result = new ScanResult(player, block, target, meta, blockX, y, blockZ);
                        return;
                    }
                }
            }
        }
    }

}
