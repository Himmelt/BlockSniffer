package him.sniffer.core;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import him.sniffer.constant.Constant;
import him.sniffer.constant.Mod;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.awt.Color;
import java.io.IOException;

public class TBlock {

    private final Block block;
    private final Integer meta;
    private final ItemStack itemStack;

    public TBlock(Block block, Integer meta) {
        this.block = block;
        this.meta = meta == null? null : meta < 0? 0 : meta > 15? 15 : meta;
        itemStack = new ItemStack(block, 1, meta == null? 0 : meta);
    }

    public TBlock(String name, Integer meta) {
        this((Block) Block.blockRegistry.getObject(name), meta);
    }

    @Override
    public int hashCode() {
        return block.hashCode();// * 17 + (meta == null? -1 : meta);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TBlock) {
            TBlock blk = (TBlock) obj;
            return block.equals(blk.block) && (meta == null? blk.meta == null : blk.meta == null? false : meta.equals(blk.meta));
        }
        return false;
    }

    public boolean invalid() {
        return block == null;
    }

    public Block getBlock() {
        return block;
    }

    public Integer getMeta() {
        return meta;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getName() {
        String name = itemStack.getDisplayName();
        if (name != null && !name.isEmpty() && !Constant.PATTERN_NAME.matcher(name).matches()) {
            return name;
        }
        name = block.getLocalizedName();
        if (name != null && !name.isEmpty() && !Constant.PATTERN_NAME.matcher(name).matches()) {
            return name;
        }
        return I18n.format("sf.unknow.block");
    }

    public Color getMapColor() {
        return new Color(block.getMapColor(meta == null? 0 : meta).colorValue);
    }

    @Override
    public String toString() {
        String name = Block.blockRegistry.getNameForObject(block);
        return meta == null? name : String.format("%s/%d", name, meta);
    }

    public static class Adapter extends TypeAdapter<TBlock> {
        @Override
        public void write(JsonWriter out, TBlock block) throws IOException {
            try {
                if (block == null || block.invalid()) {
                    out.nullValue();
                    return;
                }
                out.value(block.toString());
            } catch (Exception e) {
                Mod.logger.catching(e);
                throw e;
            }
        }

        @Override
        public TBlock read(JsonReader in) throws IOException {
            TBlock block = null;
            try {
                String[] s = in.nextString().split("/");
                if (s.length >= 1) {
                    Block blk = (Block) Block.blockRegistry.getObject(s[0]);
                    if (blk != null) {
                        Integer meta = null;
                        if (s.length >= 2 && Constant.PATTERN_NUM.matcher(s[1]).matches()) {
                            meta = Integer.valueOf(s[1]);
                            if (meta < 0 || meta > 15) {
                                meta = 0;
                            }
                        }
                        block = new TBlock(blk, meta);
                        if (block.invalid()) {
                            return null;
                        }
                    }
                }
            } catch (Exception e) {
                Mod.logger.catching(e);
                throw e;
            }
            return block;
        }
    }
}
