package org.soraworld.sniffer.core;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import org.soraworld.sniffer.BlockSniffer;
import org.soraworld.sniffer.api.SnifferAPI;
import org.soraworld.sniffer.util.ColorHelper;
import org.soraworld.sniffer.util.I19n;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

@SideOnly(Side.CLIENT)
public class Target {

    private final HashMap<Integer, TBlock> blocks = new HashMap<>();
    private final SnifferAPI api = BlockSniffer.getAPI();
    private int mode;
    private int depthL;
    private int depthH;
    private int hRange;
    private int vRange;
    private int index;
    private int count;
    private String color;

    private Target(List<TBlock> blocks) {
        this.blocks.clear();
        for (TBlock block : blocks) {
            addBlock(block);
        }
        mode = 0;
        depthL = 0;
        depthH = 64;
        hRange = 1;
        vRange = 16;
        color = "map";
    }

    public Target(TBlock block) {
        blocks.clear();
        addBlock(block);
        mode = 0;
        depthL = 0;
        depthH = 64;
        hRange = 1;
        vRange = 16;
        color = "map";
    }

    public int getMode() {
        return mode;
    }

    public Target setMode(int mode) {
        this.mode = mode == 0 ? 0 : 1;
        return this;
    }

    public int getDepthL() {
        return depthL;
    }

    public int getDepthH() {
        return depthH;
    }

    public Target setDepth(int dl, int dh) {
        dl = dl < 0 ? 0 : dl > 255 ? 255 : dl;
        dh = dh < 0 ? 0 : dh > 255 ? 255 : dh;
        if (dl < dh) {
            depthL = dl;
            depthH = dh;
        } else {
            depthL = dh;
            depthH = dl;
        }
        return this;
    }

    public int getHRange() {
        return hRange;
    }

    public Target setHRange(int hRange) {
        this.hRange = hRange < 0 ? 0 : hRange > 15 ? 15 : hRange;
        return this;
    }

    public int getVRange() {
        return vRange;
    }

    public Target setVRange(int vRange) {
        this.vRange = vRange < 0 ? 0 : vRange > 255 ? 255 : vRange;
        return this;
    }

    public String getChatColor() {
        return color.replaceAll("&", "&&");
    }

    String getColorValue() {
        return color;
    }

    public Color getColor() {
        return ColorHelper.getColor(color);
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean match(Block blk, int meta) {
        boolean match = false;
        TBlock block;
        for (Entry<Integer, TBlock> entry : blocks.entrySet()) {
            block = entry.getValue();
            if (block.getMeta() == null) {
                match = blk.equals(block.getBlock());
            } else {
                match = blk.equals(block.getBlock()) && meta == block.getMeta();
            }
            if (match) {
                index = entry.getKey();
                break;
            }
        }
        return match;
    }

    public boolean match(ScanResult result) {
        return match(result.getBlock(), result.getMeta());
    }

    public void addBlock(TBlock block) {
        if (!block.invalid() && !blocks.containsValue(block)) {
            blocks.put(count++, block);
        }
    }

    public void removeBlock(int uid) {
        if (blocks.containsKey(uid)) {
            TBlock block = blocks.get(uid);
            blocks.remove(uid);
            I19n.sendChat("sf.sub.rm.ok", block.getName());
            if (blocks.size() >= 1) {
                if (index == uid) {
                    loop();
                }
            } else {
                I19n.sendChat("sf.sub.rm.t");
                api.removeTarget();
            }
        } else {
            I19n.sendChat("sf.sub.rm.fail");
        }
    }

    private void loop() {
        for (int i = 0; i < count; i++) {
            if (blocks.containsKey(i)) {
                index = i;
                break;
            }
        }
    }

    public HashMap<Integer, TBlock> getBlocks() {
        return blocks;
    }

    public boolean invalid() {
        return blocks.isEmpty();
    }

    public TBlock getDelegate() {
        return blocks.get(index);
    }

    public String displayName() {
        return getDelegate().getName();
    }

    @Override
    public int hashCode() {
        return blocks.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Target) {
            Target target = (Target) obj;
            return blocks == target.blocks || blocks.values().containsAll(target.blocks.values()) && target.blocks.values().containsAll(blocks.values());
        }
        return false;
    }

    @Override
    public String toString() {
        return getDelegate().toString();
    }

    public static class Adapter extends TypeAdapter<Target> {

        private static final TBlock.Adapter BLOCK_ADAPTER = new TBlock.Adapter();

        @Override
        public void write(JsonWriter out, Target target) throws IOException {
            try {
                out.setHtmlSafe(false);
                if (target == null || target.invalid()) {
                    out.nullValue();
                    return;
                }
                out.beginObject();
                out.name("blocks");
                out.beginArray();
                for (TBlock block : target.blocks.values()) {
                    BLOCK_ADAPTER.write(out, block);
                }
                out.endArray();
                out.name("mode").value(target.getMode());
                out.name("depthL").value(target.getDepthL());
                out.name("depthH").value(target.getDepthH());
                out.name("hRange").value(target.getHRange());
                out.name("vRange").value(target.getVRange());
                out.name("color").value(target.getColorValue());
                out.endObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public Target read(JsonReader in) throws IOException {
            Target target;
            try {
                List<TBlock> blocks = new ArrayList<>();
                int mode = 0, depth0 = 0, depth1 = 64, hRange = 1, vRange = 16;
                String color = "map";
                in.beginObject();
                while (in.hasNext()) {
                    switch (in.nextName()) {
                        case "blocks":
                            in.beginArray();
                            while (in.hasNext()) {
                                TBlock block = BLOCK_ADAPTER.read(in);
                                if (block != null && !block.invalid()) blocks.add(block);
                            }
                            in.endArray();
                            break;
                        case "mode":
                            mode = in.nextInt();
                            break;
                        case "depthL":
                            depth0 = in.nextInt();
                            break;
                        case "depthH":
                            depth1 = in.nextInt();
                            break;
                        case "hRange":
                            hRange = in.nextInt();
                            break;
                        case "vRange":
                            vRange = in.nextInt();
                            break;
                        case "color":
                            color = in.nextString();
                            break;
                    }
                }
                target = new Target(blocks);
                if (target.invalid()) {
                    return null;
                }
                target.setMode(mode).setDepth(depth0, depth1).setHRange(hRange).setVRange(vRange).setColor(color);
                in.endObject();
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
            return target;
        }
    }
}
