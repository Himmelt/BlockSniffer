package him.sniffer.core;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.io.IOException;

import static him.sniffer.Sniffer.*;
import static him.sniffer.constant.ModInfo.*;

public class TBlock {

    private final Block block;
    private final Integer meta;
    private final ItemStack itemStack;

    public TBlock(Block block, Integer meta) {
        this.block = block;
        this.meta = meta;
        itemStack = new ItemStack(block, 1, meta == null? 0 : meta);
    }

    public TBlock(String name, Integer meta) {
        this((Block) Block.blockRegistry.getObject(name), meta);
    }

    public boolean invalid() {
        return block != null;
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
                logger.catching(e);
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
                        if (s.length >= 2 && PATTERN_NUM.matcher(s[1]).matches()) {
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
                logger.catching(e);
                return null;
            }
            return block;
        }
    }
}
