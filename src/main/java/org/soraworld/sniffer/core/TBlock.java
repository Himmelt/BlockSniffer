package org.soraworld.sniffer.core;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.soraworld.sniffer.constant.Constants;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class TBlock {

    private final Block block;
    private final Integer meta;
    private final ItemStack itemStack;
    private String name;

    public TBlock(Block block, Integer meta) {
        this.block = block;
        this.meta = meta == null ? null : meta < 0 ? 0 : meta > 15 ? 15 : meta;
        ItemStack stack;
        try {
            stack = block.getPickBlock(meta == null ? block.getDefaultState() : block.getStateFromMeta(meta), null, null, null, null);
        } catch (Exception e) {
            stack = new ItemStack(Item.getItemFromBlock(block), 1, meta == null ? 0 : meta);
        }
        if (stack == null) {
            stack = new ItemStack(Item.getItemFromBlock(block), 1, meta == null ? 0 : meta);
        }
        itemStack = stack;
    }

    public TBlock(String name, Integer meta) {
        this(Block.REGISTRY.getObject(new ResourceLocation(name)), meta);
    }

    @Override
    public int hashCode() {
        return block.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TBlock) {
            TBlock blk = (TBlock) obj;
            return block.equals(blk.block) && (meta == null ? blk.meta == null : blk.meta != null && meta.equals(blk.meta));
        }
        return false;
    }

    boolean invalid() {
        return block == null || block == Blocks.AIR;
    }

    public Block getBlock() {
        return block;
    }

    Integer getMeta() {
        return meta;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getName() {
        if (name == null || name.isEmpty()) {
            if (itemStack.getItem() != null) {
                name = itemStack.getItem().getItemStackDisplayName(itemStack);
            }
            if (name == null || name.isEmpty() || Constants.PATTERN_NAME.matcher(name).matches()) {
                name = block.getLocalizedName();
                if (name.isEmpty() || Constants.PATTERN_NAME.matcher(name).matches()) {
                    name = I18n.format("sf.unknown.block");
                }
            }
        }
        return name;
    }

    @Override
    public String toString() {
        String name = Block.REGISTRY.getNameForObject(block).toString();
        return meta == null ? name : String.format("%s/%d", name, meta);
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
                e.printStackTrace();
                throw e;
            }
        }

        @Override
        public TBlock read(JsonReader in) throws IOException {
            TBlock block = null;
            try {
                String[] s = in.nextString().split("/");
                if (s.length >= 1) {
                    Block blk = Block.REGISTRY.getObject(new ResourceLocation(s[0]));
                    Integer meta = null;
                    if (s.length >= 2 && Constants.PATTERN_NUM.matcher(s[1]).matches()) {
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
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
            return block;
        }
    }
}
