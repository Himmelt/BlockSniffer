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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static him.sniffer.Sniffer.*;

@SideOnly(Side.CLIENT)
public class BlockSniffer {

    private int index;
    private int count;
    private boolean active;
    private final SnifferHud Hud = new SnifferHud();
    private final HashMap<Integer, Target> targets = new HashMap<>();
    private final ParticleEffect particle = new ParticleEffect();
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(Target.class, new Target.Adapter()).registerTypeAdapter(TBlock.class, new TBlock.Adapter()).setPrettyPrinting().create();

    public long last;
    public long delay = 500;
    public ScanResult result;

    public void reset() {
        active = false;
        index = next(count);
        result = null;
    }

    public Target getTarget() {
        return targets.get(index);
    }

    private int next(final int start) {
        for (int index = start + 1; index != start; index++) {
            if (targets.containsKey(index)) {
                return index;
            }
            if (index >= count - 1) {
                index = -1;
            }
        }
        return -1;
    }

    public void drawHUD() {
        Hud.draw();
    }

    public void reload(File file) {
        List<Target> list = null;
        try {
            if (!file.exists() || !file.isFile()) {
                if (file.delete()) {
                    //
                }
                if (file.createNewFile()) {
                    //
                }
            } else {
                list = GSON.fromJson(FileUtils.readFileToString(file), new TypeToken<ArrayList<Target>>() {
                }.getType());
            }
        } catch (Exception e) {
            Mod.logger.catching(e);
        } finally {
            targets.clear();
            if (list == null) {
                list = new ArrayList<>();
                list.add(new Target(new TBlock(Blocks.diamond_ore, 0)));
            }
            for (Target target : list) {
                addTarget(target);
            }
            reset();
        }
    }

    public void switchTarget() {
        int index = next(active? this.index : count);
        if (index == -1) {
            reset();
            proxy.addChatMessage("sf.empty");
        } else {
            if (active && index == next(count)) {
                reset();
                proxy.addChatMessage("sf.inactive");
            } else {
                last = System.currentTimeMillis();
                this.index = index;
                if (!active) {
                    active = true;
                    proxy.addChatMessage("sf.avtive");
                }
            }
        }
    }

    public void scanWorld(EntityPlayer player) {
        result = null;
        if (getTarget() != null && player != null) {
            int chunkX = player.chunkCoordX;
            int chunkZ = player.chunkCoordZ;
            int hRange = getTarget().getHrange();
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

    public boolean isActive() {
        return active;
    }

    public void removeTarget() {
        Target target = targets.get(index);
        targets.remove(index);
        proxy.addChatMessage("sf.target.rm.ok", target.displayName());
        if (targets.isEmpty()) {
            clearTargets();
        } else {
            switchTarget();
        }
    }

    private void scanChunk(Chunk chunk, EntityPlayer player) {
        Target target = getTarget();
        int yl = target.getMode() == 0? target.getDepthL() : (int) (player.posY - target.getVrange());
        int yh = target.getMode() == 0? target.getDepthH() : (int) (player.posY + target.getVrange());
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
        try {
            FileUtils.writeStringToFile(jsonFile, GSON.toJson(targets));
        } catch (IOException e) {
            Mod.logger.catching(e);
        }
    }

    public void clearTargets() {
        targets.clear();
        reset();
        proxy.addChatMessage("sf.target.cla.ok");
    }

    public boolean addTarget(Target target) {
        if (!target.invalid() && !targets.containsValue(target)) {
            targets.put(count++, target);
            return true;
        }
        return false;
    }
}
