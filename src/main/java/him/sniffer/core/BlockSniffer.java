package him.sniffer.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import him.sniffer.client.gui.ParticleEffect;
import him.sniffer.client.gui.SnifferHud;
import him.sniffer.constant.Constant;
import him.sniffer.constant.Mod;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import static him.sniffer.Sniffer.*;

@SideOnly(Side.CLIENT)
public class BlockSniffer {

    private boolean active;
    private Iterator<Target> iterator;
    private final SnifferHud Hud = new SnifferHud();
    private final HashSet<Target> targets = new HashSet<>();
    private final ParticleEffect particle = new ParticleEffect();
    private static final Gson GSON = new GsonBuilder().create();

    public long last;
    public Target target;
    public boolean forbid;
    public long delay = 500;
    public ScanResult result;

    public void reset() {
        active = false;
        iterator = null;
        target = null;
        result = null;
    }

    public void drawHUD() {
        Hud.draw();
    }

    public void reload(File file) {
        HashSet<Target> set = new HashSet<>();
        try {
            if (!file.exists() || !file.isFile()) {
                file.delete();
                file.createNewFile();
                targets.clear();
                targets.add(new Target(Blocks.diamond_ore, 0));
            } else {
                set = GSON.fromJson(FileUtils.readFileToString(file), new TypeToken<HashSet<Target>>() {
                }.getType());
                if (set != null && !set.isEmpty()) {
                    set.removeIf(Target::invalid);
                } else {
                    set = new HashSet<Target>();
                }
            }
        } catch (IOException e) {
            Mod.logger.catching(e);
            set = new HashSet<Target>();
        } finally {
            targets.clear();
            targets.addAll(set);
            reset();
        }
    }

    public void switchTarget() {
        if (targets.size() >= 1) {
            if (iterator == null) {
                iterator = targets.iterator();
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
            int hRange = target.getHrange();
            int length = Constant.RANGE.length;
            for (int i = 0; i < length && Constant.RANGE[i][0] >= -hRange && Constant.RANGE[i][0] <= hRange && Constant.RANGE[i][1] >= -hRange && Constant.RANGE[i][1] <= hRange; i++) {
                Chunk chunk = player.worldObj.getChunkFromChunkCoords(chunkX + Constant.RANGE[i][0], chunkZ + Constant.RANGE[i][1]);
                if (!(chunk instanceof EmptyChunk)) {
                    scanChunk(chunk, player);
                    if (result != null) {
                        return;
                    }
                }
            }
        }
    }

    public void inActive() {
        active = false;
        proxy.addChatMessage("sf.inactive");
    }

    public boolean isActive() {
        return active;
    }

    public int removeTarget() {
        if (target != null && iterator != null) {
            Mod.logger.info("before:" + target.hashCode());
            iterator.remove();
            if (iterator.hasNext()) {
                Mod.logger.info("has next");
                result = null;
                last = System.currentTimeMillis();
                target = iterator.next();
            } else {
                Mod.logger.info("no next");
                reset();
            }
            Mod.logger.info("check out...active" + isActive());
            if (!targets.contains(target)) {
                Mod.logger.info("not contains");
                reset();
            }
            Mod.logger.info("size after check:" + targets.size());
            if (targets.size() >= 1) {
                Target target = targets.iterator().next();
                System.out.println(target.hashCode());
            }
            return targets.size();
        }
        return -1;
    }

    private void scanChunk(Chunk chunk, EntityPlayer player) {
        int yl = target.getMode() == 0? target.getDepth0() : (int) (player.posY - target.getVrange());
        int yh = target.getMode() == 0? target.getDepth1() : (int) (player.posY + target.getVrange());
        for (int y = yh; y > 0 && y < 255 && y >= yl; y--) {
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
                        result = new ScanResult(player, target, blockX, y, blockZ);
                        return;
                    }
                }
            }
        }
    }

    public void spawn(EntityPlayer player, ScanResult result) {
        particle.spawn(player.worldObj, player.posX, player.posY, player.posZ, result.x, result.y, result.z, result.getColor());
    }

    public void save(File jsonFile) {

    }
}
