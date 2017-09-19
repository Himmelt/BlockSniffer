package org.soraworld.sniffer.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.soraworld.sniffer.config.Config;
import org.soraworld.sniffer.constant.Constants;
import org.soraworld.sniffer.core.ScanResult;
import org.soraworld.sniffer.core.TBlock;
import org.soraworld.sniffer.core.Target;
import org.soraworld.sniffer.gui.ParticleEffect;
import org.soraworld.sniffer.util.I19n;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SideOnly(Side.CLIENT)
public class SnifferAPI {

    private int index;
    private int count;
    private long clickLast;
    private long guiLast;
    private final ParticleEffect particle = new ParticleEffect();
    private final HashMap<Integer, Target> targets = new HashMap<>();
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(Target.class, new Target.Adapter()).registerTypeAdapter(TBlock.class, new TBlock.Adapter()).setPrettyPrinting().create();

    public Config config;
    public File jsonFile;
    public Target current;
    public boolean active = false;
    public final ScanResult result = new ScanResult();
    public final Minecraft mc = Minecraft.getMinecraft();
    public final Logger LOGGER = LogManager.getLogger(Constants.NAME);
    public final KeyBinding KEY_SWITCH = new KeyBinding(I18n.format("sf.key.switch"), Keyboard.KEY_O, "key.categories.gameplay");

    public void sendChat(String key, Object... args) {
        if (mc.player != null) {
            mc.player.sendMessage(I19n.format(I18n.format("sf.chat.head") + I18n.format(key, args)));
        }
    }

    public void setGamma(int gamma) {
        mc.gameSettings.gammaSetting = gamma >= 15 ? 15 : gamma;
    }

    public float getGamma() {
        return mc.gameSettings.gammaSetting;
    }

    public boolean clickTimeOut() {
        long time = System.currentTimeMillis();
        if (clickLast + config.clickDelay.get() < time) {
            clickLast = time;
            guiLast = clickLast;
            return true;
        }
        return false;
    }

    public boolean guiInTime() {
        return config.guiDelay.get() <= 500 || guiLast + config.guiDelay.get() > System.currentTimeMillis();
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
        } catch (IOException e) {
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
        setGamma(1);
        LOGGER.info("config reloaded!");
    }

    public void save() {
        config.save();
        try {
            FileUtils.writeStringToFile(jsonFile, GSON.toJson(targets.values()), "UTF-8");
        } catch (IOException e) {
            LOGGER.catching(e);
        }
    }

    public void switchTarget() {
        int index = next(active ? this.index : count);
        if (index == -1) {
            reset();
            sendChat("sf.empty");
        } else {
            if (active && index == next(count)) {
                reset();
                sendChat("sf.inactive");
            } else {
                clickLast = System.currentTimeMillis();
                guiLast = clickLast;
                this.index = index;
                current = targets.get(index);
                if (!active) {
                    active = true;
                    sendChat("sf.active");
                }
            }
        }
    }

    public void scanWorld(EntityPlayer player) {
        result.found = false;
        if (current != null && player != null) {
            int chunkX = player.chunkCoordX;
            int chunkZ = player.chunkCoordZ;
            int hRange = current.getHRange();
            int length = Constants.RANGE.length;
            for (int i = 0; i < length && Constants.RANGE[i][0] >= -hRange && Constants.RANGE[i][0] <= hRange && Constants.RANGE[i][1] >= -hRange && Constants.RANGE[i][1] <= hRange; i++) {
                Chunk chunk = player.getEntityWorld().getChunkFromChunkCoords(chunkX + Constants.RANGE[i][0], chunkZ + Constants.RANGE[i][1]);
                if (!(chunk instanceof EmptyChunk)) {
                    scanChunk(chunk, player);
                    if (result.found) {
                        return;
                    }
                }
            }
        }
    }

    public void removeTarget() {
        targets.remove(index);
        sendChat("sf.target.rm.ok", current.displayName());
        if (targets.isEmpty()) {
            clearTargets();
        } else {
            switchTarget();
        }
    }

    public void spawnParticle(EntityPlayer player) {
        particle.spawn(player.getEntityWorld(), player.posX, player.posY + player.getEyeHeight(), player.posZ, result.x, result.y, result.z, result.getColor(), config.particleDelay.get());
    }

    public void clearTargets() {
        targets.clear();
        count = 0;
        reset();
        sendChat("sf.target.cla.ok");
    }

    public void addTarget(Target target) {
        if (!target.invalid() && !targets.containsValue(target)) {
            targets.put(count++, target);
        }
    }

    public void renderItem(ItemStack itemStack, int x, int y) {
        GlStateManager.enableRescaleNormal();
        RenderHelper.enableGUIStandardItemLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y);
        RenderHelper.disableStandardItemLighting();
    }

    public void drawRect(int x, int y, int width, int height, int color) {
        GL11.glPushMatrix();
        GL11.glPushClientAttrib(-1);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        double red = (double) (color >> 16 & 255) / 255.0D;
        double green = (double) (color >> 8 & 255) / 255.0D;
        double blue = (double) (color & 255) / 255.0D;
        double alpha = (double) (color >> 24 & 255) / 255.0D;
        GL11.glColor4d(red, green, blue, alpha);
        GL11.glBegin(7);
        GL11.glVertex2i(x, y);
        GL11.glVertex2i(x, y + height);
        GL11.glVertex2i(x + width, y + height);
        GL11.glVertex2i(x + width, y);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glPopClientAttrib();
        GL11.glPopMatrix();
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
            }
        }
    }

    private void scanChunk(Chunk chunk, EntityPlayer player) {
        int yl = current.getMode() == 0 ? current.getDepthL() : (int) (player.posY - current.getVRange());
        int yh = current.getMode() == 0 ? current.getDepthH() : (int) (player.posY + current.getVRange());
        for (int y = yh; y > 0 && y < 255 && y >= yl; y--) {
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
