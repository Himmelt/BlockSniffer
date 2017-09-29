package org.soraworld.sniffer.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.soraworld.sniffer.config.Config;
import org.soraworld.sniffer.constant.Constants;
import org.soraworld.sniffer.core.ScanResult;
import org.soraworld.sniffer.core.TBlock;
import org.soraworld.sniffer.core.Target;
import org.soraworld.sniffer.util.I19n;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SideOnly(Side.CLIENT)
public class SnifferAPI {

    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(Target.class, new Target.Adapter()).registerTypeAdapter(TBlock.class, new TBlock.Adapter()).setPrettyPrinting().create();
    public final ScanResult result = new ScanResult();
    public final Logger LOGGER = LogManager.getLogger(Constants.NAME);
    private final Minecraft mc = Minecraft.getMinecraft();
    private final HashMap<Integer, Target> targets = new HashMap<>();
    public Config config;
    public Target current;
    public boolean active = false;
    private int index;
    private int count;
    private long clickLast;
    private long guiLast;
    private File jsonFile;

    private SnifferAPI() {
    }

    public SnifferAPI(File cfgDir) {
        config = new Config(cfgDir);
        jsonFile = new File(new File(cfgDir, Constants.MODID), "target.json");
    }

    public double getGamma() {
        return config.gamma.get();
    }

    public void setGamma(int gamma) {
        if (gamma >= 0) config.gamma.set(gamma);
        mc.gameSettings.gammaSetting = config.gamma.get();
    }

    public boolean clickTimeOut() {
        long time = System.currentTimeMillis();
        if (clickLast + config.scanDelay.get() < time) {
            clickLast = time;
            guiLast = clickLast;
            return true;
        }
        return false;
    }

    public boolean guiInTime() {
        return config.hudDelay.get() <= 500 || guiLast + config.hudDelay.get() > System.currentTimeMillis();
    }

    public void reset() {
        active = false;
        index = next(count);
        current = targets.get(index);
        result.found = false;
    }

    public void reload() {
        config.reload();
        List<Target> list = new ArrayList<>();
        try {
            list = GSON.fromJson(FileUtils.readFileToString(jsonFile, "UTF-8"), Constants.LIST_TARGET);
        } catch (Exception e) {
            LOGGER.catching(e);
        } finally {
            targets.clear();
            count = 0;
            if (list.isEmpty()) {
                list.add(new Target(new TBlock(Blocks.DIAMOND_ORE, 0)));
            }
            for (Target target : list) {
                addTarget(target);
            }
            reset();
        }
        setGamma(-1);
        LOGGER.info("config reloaded!");
    }

    public void save() {
        config.save();
        try {
            jsonFile.delete();
            FileUtils.writeStringToFile(jsonFile, GSON.toJson(targets.values()), "UTF-8");
            LOGGER.info("config saved.");
        } catch (Exception e) {
            LOGGER.catching(e);
        }
    }

    public void switchTarget() {
        result.found = false;
        int index = next(active ? this.index : count);
        if (index == -1) {
            reset();
            I19n.sendChat("sf.empty");
        } else {
            if (active && index == next(count)) {
                reset();
                I19n.sendChat("sf.inactive");
            } else {
                clickLast = System.currentTimeMillis();
                guiLast = clickLast;
                this.index = index;
                current = targets.get(index);
                if (!active) {
                    active = true;
                    I19n.sendChat("sf.active");
                }
            }
        }
    }

    public void scanWorld(EntityPlayer player) {
        if (current != null && player != null) {
            if (result.found && current.match(result)) return;
            int chunkX = player.chunkCoordX;
            int chunkZ = player.chunkCoordZ;
            int hRange = current.getHRange();
            int length = Constants.RANGE.length;
            int yl = current.getMode() == 0 ? current.getDepthL() : (int) (player.posY - current.getVRange());
            int yh = current.getMode() == 0 ? current.getDepthH() : (int) (player.posY + current.getVRange());
            for (int i = 0; i < length && Constants.RANGE[i][0] >= -hRange && Constants.RANGE[i][0] <= hRange && Constants.RANGE[i][1] >= -hRange && Constants.RANGE[i][1] <= hRange; i++) {
                Chunk chunk = player.getEntityWorld().getChunkFromChunkCoords(chunkX + Constants.RANGE[i][0], chunkZ + Constants.RANGE[i][1]);
                if (!(chunk instanceof EmptyChunk)) {
                    scanChunk(chunk, yl, yh, player);
                    if (result.found) {
                        return;
                    }
                }
            }
        }
    }

    public void removeTarget() {
        active = false;
        targets.remove(index);
        I19n.sendChat("sf.target.rm.ok");
        if (targets.isEmpty()) {
            clearTargets();
        } else {
            active = true;
            switchTarget();
        }
    }

    public void clearTargets() {
        targets.clear();
        count = 0;
        reset();
        I19n.sendChat("sf.target.cla.ok");
    }

    public void addTarget(Target target) {
        if (!target.invalid() && !targets.containsValue(target)) {
            targets.put(count++, target);
        }
    }

    private int next(int start) {
        start = start >= count - 1 ? 0 : start + 1;
        int time = 0, index = start;
        while (true) {
            if (index == start) {
                time++;
                if (time >= 2) {
                    return -1;
                }
            }
            if (targets.containsKey(index)) {
                return index;
            }
            if (index >= count - 1) {
                index = 0;
            } else {
                index++;
            }
        }
    }

    private void scanChunk(Chunk chunk, int yl, int yh, EntityPlayer player) {
        for (int y = yh; y >= 0 && y <= 255 && y >= yl; y--) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    IBlockState state = chunk.getBlockState(x, y, z);
                    Block block = state.getBlock();
                    if (block.equals(Blocks.AIR)) {
                        continue;
                    }
                    int meta = block.getMetaFromState(state);
                    if (current.match(block, meta)) {
                        int blockX = chunk.xPosition * 16 + x;
                        int blockZ = chunk.zPosition * 16 + z;
                        result.update(player, current, blockX, y, blockZ);
                        return;
                    }
                }
            }
        }
    }
}
