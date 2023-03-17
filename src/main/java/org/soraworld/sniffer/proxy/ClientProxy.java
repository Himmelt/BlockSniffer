package org.soraworld.sniffer.proxy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.soraworld.sniffer.config.Config;
import org.soraworld.sniffer.constant.Constants;
import org.soraworld.sniffer.core.ScanResult;
import org.soraworld.sniffer.core.TBlock;
import org.soraworld.sniffer.core.Target;
import org.soraworld.sniffer.gui.GuiRender;
import org.soraworld.sniffer.util.I19n;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

@SideOnly(Side.CLIENT)
public class ClientProxy {

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
    private volatile boolean lock = false;
    public static final KeyBinding KEY_SWITCH = new KeyBinding(I18n.format("sf.key.switch"), Keyboard.KEY_O, "key.categories.gameplay");
    public static final Pattern PATTERN_NUM = Pattern.compile("[0-9]{1,3}");
    public static final Pattern PATTERN_NAME = Pattern.compile("^tile.*name$");
    public static final int[][] RANGE = {
            {0, 0}, {-1, 0}, {0, -1}, {0, 1}, {1, 0}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}, {-2, 0}, {0, -2}, {0, 2}, {2, 0}, {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
            {1, -2}, {1, 2}, {2, -1}, {2, 1}, {-2, -2}, {-2, 2}, {2, -2}, {2, 2}, {-3, 0}, {0, -3}, {0, 3}, {3, 0}, {-3, -1}, {-3, 1}, {-1, -3}, {-1, 3}, {1, -3},
            {1, 3}, {3, -1}, {3, 1}, {-3, -2}, {-3, 2}, {-2, -3}, {-2, 3}, {2, -3}, {2, 3}, {3, -2}, {3, 2}, {-4, 0}, {0, -4}, {0, 4}, {4, 0}, {-4, -1}, {-4, 1},
            {-1, -4}, {-1, 4}, {1, -4}, {1, 4}, {4, -1}, {4, 1}, {-3, -3}, {-3, 3}, {3, -3}, {3, 3}, {-4, -2}, {-4, 2}, {-2, -4}, {-2, 4}, {2, -4}, {2, 4}, {4, -2},
            {4, 2}, {-5, 0}, {-4, -3}, {-4, 3}, {-3, -4}, {-3, 4}, {0, -5}, {0, 5}, {3, -4}, {3, 4}, {4, -3}, {4, 3}, {5, 0}, {-5, -1}, {-5, 1}, {-1, -5}, {-1, 5},
            {1, -5}, {1, 5}, {5, -1}, {5, 1}, {-5, -2}, {-5, 2}, {-2, -5}, {-2, 5}, {2, -5}, {2, 5}, {5, -2}, {5, 2}, {-4, -4}, {-4, 4}, {4, -4}, {4, 4}, {-5, -3},
            {-5, 3}, {-3, -5}, {-3, 5}, {3, -5}, {3, 5}, {5, -3}, {5, 3}, {-6, 0}, {0, -6}, {0, 6}, {6, 0}, {-6, -1}, {-6, 1}, {-1, -6}, {-1, 6}, {1, -6}, {1, 6},
            {6, -1}, {6, 1}, {-6, -2}, {-6, 2}, {-2, -6}, {-2, 6}, {2, -6}, {2, 6}, {6, -2}, {6, 2}, {-5, -4}, {-5, 4}, {-4, -5}, {-4, 5}, {4, -5}, {4, 5}, {5, -4},
            {5, 4}, {-6, -3}, {-6, 3}, {-3, -6}, {-3, 6}, {3, -6}, {3, 6}, {6, -3}, {6, 3}, {-7, 0}, {0, -7}, {0, 7}, {7, 0}, {-7, -1}, {-7, 1}, {-5, -5}, {-5, 5},
            {-1, -7}, {-1, 7}, {1, -7}, {1, 7}, {5, -5}, {5, 5}, {7, -1}, {7, 1}, {-6, -4}, {-6, 4}, {-4, -6}, {-4, 6}, {4, -6}, {4, 6}, {6, -4}, {6, 4}, {-7, -2},
            {-7, 2}, {-2, -7}, {-2, 7}, {2, -7}, {2, 7}, {7, -2}, {7, 2}, {-7, -3}, {-7, 3}, {-3, -7}, {-3, 7}, {3, -7}, {3, 7}, {7, -3}, {7, 3}, {-6, -5}, {-6, 5},
            {-5, -6}, {-5, 6}, {5, -6}, {5, 6}, {6, -5}, {6, 5}, {-8, 0}, {0, -8}, {0, 8}, {8, 0}, {-8, -1}, {-8, 1}, {-7, -4}, {-7, 4}, {-4, -7}, {-4, 7}, {-1, -8},
            {-1, 8}, {1, -8}, {1, 8}, {4, -7}, {4, 7}, {7, -4}, {7, 4}, {8, -1}, {8, 1}, {-8, -2}, {-8, 2}, {-2, -8}, {-2, 8}, {2, -8}, {2, 8}, {8, -2}, {8, 2},
            {-6, -6}, {-6, 6}, {6, -6}, {6, 6}, {-8, -3}, {-8, 3}, {-3, -8}, {-3, 8}, {3, -8}, {3, 8}, {8, -3}, {8, 3}, {-7, -5}, {-7, 5}, {-5, -7}, {-5, 7}, {5, -7},
            {5, 7}, {7, -5}, {7, 5}, {-8, -4}, {-8, 4}, {-4, -8}, {-4, 8}, {4, -8}, {4, 8}, {8, -4}, {8, 4}, {-9, 0}, {0, -9}, {0, 9}, {9, 0}, {-9, -1}, {-9, 1},
            {-1, -9}, {-1, 9}, {1, -9}, {1, 9}, {9, -1}, {9, 1}, {-9, -2}, {-9, 2}, {-7, -6}, {-7, 6}, {-6, -7}, {-6, 7}, {-2, -9}, {-2, 9}, {2, -9}, {2, 9}, {6, -7},
            {6, 7}, {7, -6}, {7, 6}, {9, -2}, {9, 2}, {-8, -5}, {-8, 5}, {-5, -8}, {-5, 8}, {5, -8}, {5, 8}, {8, -5}, {8, 5}, {-9, -3}, {-9, 3}, {-3, -9}, {-3, 9},
            {3, -9}, {3, 9}, {9, -3}, {9, 3}, {-9, -4}, {-9, 4}, {-4, -9}, {-4, 9}, {4, -9}, {4, 9}, {9, -4}, {9, 4}, {-7, -7}, {-7, 7}, {7, -7}, {7, 7}, {-10, 0},
            {-8, -6}, {-8, 6}, {-6, -8}, {-6, 8}, {0, -10}, {0, 10}, {6, -8}, {6, 8}, {8, -6}, {8, 6}, {10, 0}, {-10, -1}, {-10, 1}, {-1, -10}, {-1, 10}, {1, -10},
            {1, 10}, {10, -1}, {10, 1}, {-10, -2}, {-10, 2}, {-2, -10}, {-2, 10}, {2, -10}, {2, 10}, {10, -2}, {10, 2}, {-9, -5}, {-9, 5}, {-5, -9}, {-5, 9}, {5, -9},
            {5, 9}, {9, -5}, {9, 5}, {-10, -3}, {-10, 3}, {-3, -10}, {-3, 10}, {3, -10}, {3, 10}, {10, -3}, {10, 3}, {-8, -7}, {-8, 7}, {-7, -8}, {-7, 8}, {7, -8},
            {7, 8}, {8, -7}, {8, 7}, {-10, -4}, {-10, 4}, {-4, -10}, {-4, 10}, {4, -10}, {4, 10}, {10, -4}, {10, 4}, {-9, -6}, {-9, 6}, {-6, -9}, {-6, 9}, {6, -9},
            {6, 9}, {9, -6}, {9, 6}, {-11, 0}, {0, -11}, {0, 11}, {11, 0}, {-11, -1}, {-11, 1}, {-1, -11}, {-1, 11}, {1, -11}, {1, 11}, {11, -1}, {11, 1}, {-11, -2},
            {-11, 2}, {-10, -5}, {-10, 5}, {-5, -10}, {-5, 10}, {-2, -11}, {-2, 11}, {2, -11}, {2, 11}, {5, -10}, {5, 10}, {10, -5}, {10, 5}, {11, -2}, {11, 2},
            {-8, -8}, {-8, 8}, {8, -8}, {8, 8}, {-11, -3}, {-11, 3}, {-9, -7}, {-9, 7}, {-7, -9}, {-7, 9}, {-3, -11}, {-3, 11}, {3, -11}, {3, 11}, {7, -9}, {7, 9},
            {9, -7}, {9, 7}, {11, -3}, {11, 3}, {-10, -6}, {-10, 6}, {-6, -10}, {-6, 10}, {6, -10}, {6, 10}, {10, -6}, {10, 6}, {-11, -4}, {-11, 4}, {-4, -11},
            {-4, 11}, {4, -11}, {4, 11}, {11, -4}, {11, 4}, {-12, 0}, {0, -12}, {0, 12}, {12, 0}, {-12, -1}, {-12, 1}, {-9, -8}, {-9, 8}, {-8, -9}, {-8, 9}, {-1, -12},
            {-1, 12}, {1, -12}, {1, 12}, {8, -9}, {8, 9}, {9, -8}, {9, 8}, {12, -1}, {12, 1}, {-11, -5}, {-11, 5}, {-5, -11}, {-5, 11}, {5, -11}, {5, 11}, {11, -5},
            {11, 5}, {-12, -2}, {-12, 2}, {-2, -12}, {-2, 12}, {2, -12}, {2, 12}, {12, -2}, {12, 2}, {-10, -7}, {-10, 7}, {-7, -10}, {-7, 10}, {7, -10}, {7, 10},
            {10, -7}, {10, 7}, {-12, -3}, {-12, 3}, {-3, -12}, {-3, 12}, {3, -12}, {3, 12}, {12, -3}, {12, 3}, {-11, -6}, {-11, 6}, {-6, -11}, {-6, 11}, {6, -11},
            {6, 11}, {11, -6}, {11, 6}, {-12, -4}, {-12, 4}, {-4, -12}, {-4, 12}, {4, -12}, {4, 12}, {12, -4}, {12, 4}, {-9, -9}, {-9, 9}, {9, -9}, {9, 9}, {-10, -8},
            {-10, 8}, {-8, -10}, {-8, 10}, {8, -10}, {8, 10}, {10, -8}, {10, 8}, {-13, 0}, {-12, -5}, {-12, 5}, {-5, -12}, {-5, 12}, {0, -13}, {0, 13}, {5, -12},
            {5, 12}, {12, -5}, {12, 5}, {13, 0}, {-13, -1}, {-13, 1}, {-11, -7}, {-11, 7}, {-7, -11}, {-7, 11}, {-1, -13}, {-1, 13}, {1, -13}, {1, 13}, {7, -11},
            {7, 11}, {11, -7}, {11, 7}, {13, -1}, {13, 1}, {-13, -2}, {-13, 2}, {-2, -13}, {-2, 13}, {2, -13}, {2, 13}, {13, -2}, {13, 2}, {-13, -3}, {-13, 3},
            {-3, -13}, {-3, 13}, {3, -13}, {3, 13}, {13, -3}, {13, 3}, {-12, -6}, {-12, 6}, {-6, -12}, {-6, 12}, {6, -12}, {6, 12}, {12, -6}, {12, 6}, {-10, -9},
            {-10, 9}, {-9, -10}, {-9, 10}, {9, -10}, {9, 10}, {10, -9}, {10, 9}, {-13, -4}, {-13, 4}, {-11, -8}, {-11, 8}, {-8, -11}, {-8, 11}, {-4, -13}, {-4, 13},
            {4, -13}, {4, 13}, {8, -11}, {8, 11}, {11, -8}, {11, 8}, {13, -4}, {13, 4}, {-12, -7}, {-12, 7}, {-7, -12}, {-7, 12}, {7, -12}, {7, 12}, {12, -7}, {12, 7},
            {-13, -5}, {-13, 5}, {-5, -13}, {-5, 13}, {5, -13}, {5, 13}, {13, -5}, {13, 5}, {-14, 0}, {0, -14}, {0, 14}, {14, 0}, {-14, -1}, {-14, 1}, {-1, -14},
            {-1, 14}, {1, -14}, {1, 14}, {14, -1}, {14, 1}, {-14, -2}, {-14, 2}, {-10, -10}, {-10, 10}, {-2, -14}, {-2, 14}, {2, -14}, {2, 14}, {10, -10}, {10, 10},
            {14, -2}, {14, 2}, {-11, -9}, {-11, 9}, {-9, -11}, {-9, 11}, {9, -11}, {9, 11}, {11, -9}, {11, 9}, {-14, -3}, {-14, 3}, {-13, -6}, {-13, 6}, {-6, -13},
            {-6, 13}, {-3, -14}, {-3, 14}, {3, -14}, {3, 14}, {6, -13}, {6, 13}, {13, -6}, {13, 6}, {14, -3}, {14, 3}, {-12, -8}, {-12, 8}, {-8, -12}, {-8, 12},
            {8, -12}, {8, 12}, {12, -8}, {12, 8}, {-14, -4}, {-14, 4}, {-4, -14}, {-4, 14}, {4, -14}, {4, 14}, {14, -4}, {14, 4}, {-13, -7}, {-13, 7}, {-7, -13},
            {-7, 13}, {7, -13}, {7, 13}, {13, -7}, {13, 7}, {-14, -5}, {-14, 5}, {-11, -10}, {-11, 10}, {-10, -11}, {-10, 11}, {-5, -14}, {-5, 14}, {5, -14}, {5, 14},
            {10, -11}, {10, 11}, {11, -10}, {11, 10}, {14, -5}, {14, 5}, {-15, 0}, {-12, -9}, {-12, 9}, {-9, -12}, {-9, 12}, {0, -15}, {0, 15}, {9, -12}, {9, 12},
            {12, -9}, {12, 9}, {15, 0}, {-15, -1}, {-15, 1}, {-1, -15}, {-1, 15}, {1, -15}, {1, 15}, {15, -1}, {15, 1}, {-15, -2}, {-15, 2}, {-2, -15}, {-2, 15},
            {2, -15}, {2, 15}, {15, -2}, {15, 2}, {-14, -6}, {-14, 6}, {-6, -14}, {-6, 14}, {6, -14}, {6, 14}, {14, -6}, {14, 6}, {-13, -8}, {-13, 8}, {-8, -13},
            {-8, 13}, {8, -13}, {8, 13}, {13, -8}, {13, 8}, {-15, -3}, {-15, 3}, {-3, -15}, {-3, 15}, {3, -15}, {3, 15}, {15, -3}, {15, 3}, {-15, -4}, {-15, 4},
            {-4, -15}, {-4, 15}, {4, -15}, {4, 15}, {15, -4}, {15, 4}, {-11, -11}, {-11, 11}, {11, -11}, {11, 11}, {-12, -10}, {-12, 10}, {-10, -12}, {-10, 12},
            {10, -12}, {10, 12}, {12, -10}, {12, 10}, {-14, -7}, {-14, 7}, {-7, -14}, {-7, 14}, {7, -14}, {7, 14}, {14, -7}, {14, 7}, {-15, -5}, {-15, 5}, {-13, -9},
            {-13, 9}, {-9, -13}, {-9, 13}, {-5, -15}, {-5, 15}, {5, -15}, {5, 15}, {9, -13}, {9, 13}, {13, -9}, {13, 9}, {15, -5}, {15, 5}, {-14, -8}, {-14, 8},
            {-8, -14}, {-8, 14}, {8, -14}, {8, 14}, {14, -8}, {14, 8}, {-15, -6}, {-15, 6}, {-6, -15}, {-6, 15}, {6, -15}, {6, 15}, {15, -6}, {15, 6}, {-12, -11},
            {-12, 11}, {-11, -12}, {-11, 12}, {11, -12}, {11, 12}, {12, -11}, {12, 11}, {-13, -10}, {-13, 10}, {-10, -13}, {-10, 13}, {10, -13}, {10, 13}, {13, -10},
            {13, 10}, {-15, -7}, {-15, 7}, {-7, -15}, {-7, 15}, {7, -15}, {7, 15}, {15, -7}, {15, 7}, {-14, -9}, {-14, 9}, {-9, -14}, {-9, 14}, {9, -14}, {9, 14},
            {14, -9}, {14, 9}, {-12, -12}, {-12, 12}, {12, -12}, {12, 12}, {-15, -8}, {-15, 8}, {-8, -15}, {-8, 15}, {8, -15}, {8, 15}, {15, -8}, {15, 8}, {-13, -11},
            {-13, 11}, {-11, -13}, {-11, 13}, {11, -13}, {11, 13}, {13, -11}, {13, 11}, {-14, -10}, {-14, 10}, {-10, -14}, {-10, 14}, {10, -14}, {10, 14}, {14, -10},
            {14, 10}, {-15, -9}, {-15, 9}, {-9, -15}, {-9, 15}, {9, -15}, {9, 15}, {15, -9}, {15, 9}, {-13, -12}, {-13, 12}, {-12, -13}, {-12, 13}, {12, -13}, {12, 13},
            {13, -12}, {13, 12}, {-14, -11}, {-14, 11}, {-11, -14}, {-11, 14}, {11, -14}, {11, 14}, {14, -11}, {14, 11}, {-15, -10}, {-15, 10}, {-10, -15}, {-10, 15},
            {10, -15}, {10, 15}, {15, -10}, {15, 10}, {-13, -13}, {-13, 13}, {13, -13}, {13, 13}, {-14, -12}, {-14, 12}, {-12, -14}, {-12, 14}, {12, -14}, {12, 14},
            {14, -12}, {14, 12}, {-15, -11}, {-15, 11}, {-11, -15}, {-11, 15}, {11, -15}, {11, 15}, {15, -11}, {15, 11}, {-14, -13}, {-14, 13}, {-13, -14}, {-13, 14},
            {13, -14}, {13, 14}, {14, -13}, {14, 13}, {-15, -12}, {-15, 12}, {-12, -15}, {-12, 15}, {12, -15}, {12, 15}, {15, -12}, {15, 12}, {-14, -14}, {-14, 14},
            {14, -14}, {14, 14}, {-15, -13}, {-15, 13}, {-13, -15}, {-13, 15}, {13, -15}, {13, 15}, {15, -13}, {15, 13}, {-15, -14}, {-15, 14}, {-14, -15}, {-14, 15},
            {14, -15}, {14, 15}, {15, -14}, {15, 14}, {-15, -15}, {-15, 15}, {15, -15}, {15, 15}
    };




    public ClientProxy(File cfgDir) {
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

    private boolean clickTimeOut() {
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
            LOGGER.info("targets.json doesn't exist!");
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
        if (active && !lock && clickTimeOut() && current != null && player != null) {
            if (result.found && current.match(result)) {
                GuiRender.spawnParticle(player, result.center(), result.getColor(), config.particleDelay.get());
                lock = false;
                return;
            }
            new Thread(() -> {
                lock = true;
                result.found = false;
                int hRange = current.getHRange();
                int yl = current.getMode() == 0 ? current.getDepthL() : (int) (player.posY - current.getVRange());
                int yh = current.getMode() == 0 ? current.getDepthH() : (int) (player.posY + current.getVRange());
                for (int i = 0; i < Constants.RANGE.length && Constants.RANGE[i][0] >= -hRange && Constants.RANGE[i][0] <= hRange && Constants.RANGE[i][1] >= -hRange && Constants.RANGE[i][1] <= hRange; i++) {
                    Chunk chunk = player.getEntityWorld().getChunk(player.chunkCoordX + Constants.RANGE[i][0], player.chunkCoordZ + Constants.RANGE[i][1]);
                    if (!(chunk instanceof EmptyChunk)) {
                        scanChunk(chunk, yl, yh, player);
                        if (result.found) {
                            GuiRender.spawnParticle(player, result.center(), result.getColor(), config.particleDelay.get());
                            lock = false;
                            return;
                        }
                    }
                }
                lock = false;
            }).start();
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
                        int blockX = chunk.x * 16 + x;
                        int blockZ = chunk.z * 16 + z;
                        result.update(player, current, blockX, y, blockZ);
                        return;
                    }
                }
            }
        }
    }
}
