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
import java.util.HashMap;
import java.util.HashSet;

import static him.sniffer.Sniffer.*;
import static him.sniffer.constant.Constant.*;

public class Target {

    private int mode;
    private int depth0;
    private int depth1;
    private int hrange;
    private int vrange;
    private String color;
    private TBlock delegate;
    private final HashSet<TBlock> blocks;
    private final HashMap<Integer, TBlock> map = new HashMap<>();

    private Target(HashSet<TBlock> blocks) {
        this.blocks = blocks;
        delegate = blocks.iterator().next();
        mode = 0;
        depth0 = 0;
        depth1 = 64;
        hrange = 1;
        vrange = 16;
        color = "map";
    }

    public Target(TBlock blk) {
        blocks = new HashSet<TBlock>();
        blocks.add(blk);
        delegate = blocks.iterator().next();
        mode = 0;
        depth0 = 0;
        depth1 = 64;
        hrange = 1;
        vrange = 16;
        color = "map";
    }

    @Override
    public int hashCode() {
        return 0;//blocks.hashCode();
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
            return blocks.containsAll(target.blocks) && target.blocks.containsAll(blocks);
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

    public int getDepth0() {
        return depth0;
    }

    public int getDepth1() {
        return depth1;
    }

    public Target setDepth(int dl, int dh) {
        dl = dl < 0? 0 : dl > 255? 255 : dl;
        dh = dh < 0? 0 : dh > 255? 255 : dh;
        if (dl < dh) {
            depth0 = dl;
            depth1 = dh;
        } else {
            depth0 = dh;
            depth1 = dl;
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
        this.vrange = vrange < 0? 0 : vrange > 15? 15 : vrange;
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
        for (TBlock block : blocks) {
            if (block.getMeta() == null) {
                match = blk.equals(block.getBlock());
            } else {
                match = blk.equals(block.getBlock()) && meta == block.getMeta();
            }
            if (match) {
                delegate = block;
                break;
            }
        }
        return match;
    }

    public void addBlock(TBlock block) {
        blocks.add(block);
    }

    public void removeBlock(int uid) {
        getBlocks();
        if (map.containsKey(uid)) {
            TBlock block = map.get(uid);
            blocks.remove(block);
            map.remove(uid);
            proxy.addChatMessage("sf.sub.rm.ok", block.getName());
            if (blocks.size() >= 1) {
                delegate = blocks.iterator().next();
            } else {
                proxy.addChatMessage("sf.sub.rm.t");
                proxy.sniffer.removeTarget();
            }
        } else {
            proxy.addChatMessage("sf.sub.rm.fail");
        }
    }

    public HashMap<Integer, TBlock> getBlocks() {
        map.clear();
        int i = 0;
        for (TBlock block : blocks) {
            map.put(i, block);
            i++;
        }
        return map;
    }

    public boolean invalid() {
        blocks.removeIf(TBlock::invalid);
        return blocks.isEmpty();
    }

    public TBlock getDelegate() {
        return delegate;
    }

    public String displayName() {
        String name = delegate.getBlock().getLocalizedName();
        if (name != null && !PATTERN_NAME.matcher(name).matches()) {
            return name;
        }
        name = delegate.getItemStack().getDisplayName();
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
                for (TBlock block : target.blocks) {
                    BLCOK_ADAPTER.write(out, block);
                }
                out.endArray();
                out.name("mode").value(target.getMode());
                out.name("depth0").value(target.getDepth0());
                out.name("depth1").value(target.getDepth1());
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
                HashSet<TBlock> blocks = new HashSet<>();
                int mode = 0, depth0 = 0, depth1 = 64, hrange = 1, vrange = 16;
                String color = "map";
                in.beginObject();
                while (in.hasNext()) {
                    switch (in.nextName()) {
                    case "blocks":
                        in.beginArray();
                        while (in.hasNext()) {
                            TBlock block = BLCOK_ADAPTER.read(in);
                            if (block != null && !block.invalid()) {
                                blocks.add(block);
                            }
                        }
                        in.endArray();
                        break;
                    case "mode":
                        mode = in.nextInt();
                        break;
                    case "depth0":
                        depth0 = in.nextInt();
                        break;
                    case "depth1":
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
                target.delegate = target.blocks.iterator().next();
                in.endObject();
            } catch (Exception e) {
                Mod.logger.catching(e);
                throw e;
            }
            return target;
        }
    }

}
