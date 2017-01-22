package him.sniffer.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import him.sniffer.config.Target;
import him.sniffer.config.Targets;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;

import java.awt.Color;
import java.util.Iterator;

import static him.sniffer.constant.ModInfo.*;

@SideOnly(Side.CLIENT)
public class BlockSniffer {

    private Target target;
    public Targets targets;
    private boolean active;
    public long last;
    public long delay = 500;
    private Iterator<Target> iterator;
    public final SnifferHud Hud = new SnifferHud();

    private final ParticleEffect particleEffect = new ParticleEffect();
    private static final String MINECRAFT_KEY_PREFIX = "minecraft:";
    public ScanResult result = new ScanResult(null, false, 0, 0, 0);

    public void spawnParticles(World worldObj, double fromX, double fromY, double fromZ, double toX, double toY,
                               double toZ, Color color) {
        particleEffect.spawnParticles(worldObj, fromX, fromY, fromZ, toX, toY, toZ, color);
    }


    public void setTarget(Target target) {
        this.target = target;
    }

    public Target getTarget() {
        return target;
    }

    public void switchTarget() {
        if (iterator == null) {
            iterator = targets.getIterator();
        }
        if (iterator.hasNext()) {
            active = true;
            target = iterator.next();
        } else {
            iterator = null;
            active = false;
            target = null;
        }
    }

    public ScanResult scanWorld(EntityPlayer player) {
        if (target != null && player != null) {
            int chunkX = player.chunkCoordX;
            int chunkZ = player.chunkCoordZ;
            int hRange = target.hRange;
            int length = RANGE.length;
            for (int i = 0; RANGE[i][0] >= -hRange && RANGE[i][0] <= hRange &&
                            RANGE[i][1] >= -hRange && RANGE[i][1] <= hRange && i < length; i++) {
                Chunk chunk = player.worldObj.getChunkFromChunkCoords(chunkX + RANGE[i][0], chunkZ + RANGE[i][1]);
                if (!(chunk instanceof EmptyChunk)) {
                    ScanResult result = scanChunk(chunk, player);
                    if (result != null && result.found) {
                        return result;
                    }
                }
            }
        }
        return new ScanResult(null, false, 0, 0, 0);
    }

    private ScanResult scanChunk(Chunk chunk, EntityPlayer player) {
        if (target.mode == 0) {
            for (int y = target.depth[1]; y > 0 && y < 255 && y >= target.depth[0]; y--) {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        Block block = chunk.getBlock(x, y, z);
                        int meta = chunk.getBlockMetadata(x, y, z);
                        if (target.match(block, meta)) {
                            int blockX = chunk.xPosition * 16 + x;
                            int blockZ = chunk.zPosition * 16 + z;
                            return new ScanResult(player, true, blockX, y, blockZ);
                        }
                    }
                }
            }
        } else if (target.mode == 1) {
            for (int y = (int) player.posY + target.vRange; y > 0 && y < 255 && y >= (int) player.posY - target.vRange;
                 y--) {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        Block block = chunk.getBlock(x, y, z);
                        int meta = chunk.getBlockMetadata(x, y, z);
                        if (target.match(block, meta)) {
                            int blockX = chunk.xPosition * 16 + x;
                            int blockZ = chunk.zPosition * 16 + z;
                            return new ScanResult(player, true, blockX, y, blockZ);
                        }
                    }
                }
            }
        }
        return null;
    }

    public boolean isActive() {
        return active;
    }

    public static class ScanResult {
        public final boolean found;
        public final EntityPlayer player;
        public final int x, y, z;

        public ScanResult(EntityPlayer player, boolean found, int x, int y, int z) {
            this.player = player;
            this.found = found;
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
    }
}
