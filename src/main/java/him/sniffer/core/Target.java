package him.sniffer.core;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import him.sniffer.constant.ColorHelper;
import him.sniffer.constant.Mod;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import static him.sniffer.Sniffer.*;
import static him.sniffer.constant.Constant.*;

public class Target {

    private int mode;
    private int depthL;
    private int depthH;
    private int hrange;
    private int vrange;
    private int index;
    private int count;
    private String color;
    private final HashMap<Integer, TBlock> blocks = new HashMap<>();

    private Target(List<TBlock> blocks) {
        this.blocks.clear();
        for (TBlock block : blocks) {
            addBlock(block);
        }
        mode = 0;
        depthL = 0;
        depthH = 64;
        hrange = 1;
        vrange = 16;
        color = "map";
    }

    public Target(TBlock block) {
        blocks.clear();
        addBlock(block);
        mode = 0;
        depthL = 0;
        depthH = 64;
        hrange = 1;
        vrange = 16;
        color = "map";
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Target) {
            Target target = (Target) obj;
            if (blocks == target.blocks) {
                return true;
            }
            return blocks.values().containsAll(target.blocks.values()) && target.blocks.values().containsAll(blocks.values());
        }
        return false;
    }

    public int getMode() {
        return mode;
    }

    public Target setMode(int mode) {
        this.mode = mode == 0? 0 : 1;
        return this;
    }

    public int getDepthL() {
        return depthL;
    }

    public int getDepthH() {
        return depthH;
    }

    public Target setDepth(int dl, int dh) {
        dl = dl < 0? 0 : dl > 255? 255 : dl;
        dh = dh < 0? 0 : dh > 255? 255 : dh;
        if (dl < dh) {
            depthL = dl;
            depthH = dh;
        } else {
            depthL = dh;
            depthH = dl;
        }
        return this;
    }

    public int getHrange() {
        return hrange;
    }

    public Target setHrange(int hrange) {
        this.hrange = hrange < 0? 0 : hrange > 15? 15 : hrange;
        return this;
    }

    public int getVrange() {
        return vrange;
    }

    public Target setVrange(int vrange) {
        this.vrange = vrange < 0? 0 : vrange > 255? 255 : vrange;
        return this;
    }

    public String getColorValue() {
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

    public boolean addBlock(TBlock block) {
        if (!block.invalid() && !blocks.containsValue(block)) {
            blocks.put(count++, block);
            return true;
        }
        return false;
    }

    public void removeBlock(int uid) {
        if (blocks.containsKey(uid)) {
            TBlock block = blocks.get(uid);
            blocks.remove(uid);
            proxy.addChatMessage("sf.sub.rm.ok", block.getName());
            if (blocks.size() >= 1) {
                if (index == uid) {
                    loop();
                }
            } else {
                proxy.addChatMessage("sf.sub.rm.t");
                proxy.sniffer.removeTarget();
            }
        } else {
            proxy.addChatMessage("sf.sub.rm.fail");
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
        String name = getDelegate().getBlock().getLocalizedName();
        if (name != null && !PATTERN_NAME.matcher(name).matches()) {
            return name;
        }
        name = getDelegate().getItemStack().getDisplayName();
        if (name != null && !PATTERN_NAME.matcher(name).matches()) {
            return name;
        }
        return I18n.format("sf.unknow.block");
    }

    public static class Adapter extends TypeAdapter<Target> {

        private static final TBlock.Adapter BLCOK_ADAPTER = new TBlock.Adapter();

        @Override
        public void write(JsonWriter out, Target target) throws IOException {
            try {
                if (target == null || target.invalid()) {
                    out.nullValue();
                    return;
                }
                out.beginObject();
                out.name("blocks");
                out.beginArray();
                for (TBlock block : target.blocks.values()) {
                    BLCOK_ADAPTER.write(out, block);
                }
                out.endArray();
                out.name("mode").value(target.getMode());
                out.name("depthL").value(target.getDepthL());
                out.name("depthH").value(target.getDepthH());
                out.name("hrange").value(target.getHrange());
                out.name("vrange").value(target.getVrange());
                out.name("color").value(target.getColorValue());
                out.endObject();
            } catch (Exception e) {
                Mod.logger.catching(e);
            }
        }

        @Override
        public Target read(JsonReader in) throws IOException {
            Target target;
            try {
                List<TBlock> blocks = new ArrayList<>();
                int mode = 0, depth0 = 0, depth1 = 64, hrange = 1, vrange = 16;
                String color = "map";
                in.beginObject();
                while (in.hasNext()) {
                    switch (in.nextName()) {
                    case "blocks":
                        in.beginArray();
                        while (in.hasNext()) {
                            blocks.add(BLCOK_ADAPTER.read(in));
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
                    case "hrange":
                        hrange = in.nextInt();
                        break;
                    case "vrange":
                        vrange = in.nextInt();
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
                target.setMode(mode).setDepth(depth0, depth1).setHrange(hrange).setVrange(vrange).setColor(color);
                in.endObject();
            } catch (Exception e) {
                Mod.logger.catching(e);
                throw e;
            }
            return target;
        }
    }

}
