package him.sniffer.config;

import com.google.gson.annotations.SerializedName;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Target implements Serializable {
    private static final transient long serialVersionUID = 5468612871816526011L;
    @SerializedName("subs")
    private final Set<SubTarget> subs;
    @SerializedName("color")
    private final String color;
    @SerializedName("mode")
    public int mode;
    @SerializedName("depth")
    public int[] depth;
    @SerializedName("h_range")
    public int hRange;
    @SerializedName("v_range")
    public int vRange;

    private transient SubTarget delegate;

    public Target(String name, String color) {
        this(name, Color.decode(color));
    }

    public Target(String name, Color color) {
        subs = new HashSet<SubTarget>();
        subs.add(new SubTarget(name));
        this.color = '#' + Integer.toHexString(color.getRGB() & 0x00ffffff);
        mode = 0;
        depth = new int[] { 0, 64 };
        hRange = 1;
        vRange = 16;
    }

    public void addSub(SubTarget sub) {
        subs.add(sub);
    }

    public boolean match(Block block, int meta) {
        boolean match = false;
        for (SubTarget sub : subs) {
            if (sub.getMeta() == null) {
                match = block.equals(sub.getBlock());
            } else {
                match = block.equals(sub.getBlock()) && meta == sub.getMeta();
            }
            if (match) {
                delegate = sub;
                break;
            }
        }
        return match;
    }

    public String getDefaultName() {
        String name = getDelegate().getItemStack().getDisplayName();
        if (name != null) {
            return name;
        } else {
            name = getDelegate().getBlock().getLocalizedName();
            if (name != null) {
                return name;
            }
            return "";
        }
    }

    public Color getColor() {
        try {
            return Color.decode(color);
        } catch (NumberFormatException ignored) {
        }
        return Color.CYAN;
    }

    public SubTarget getDelegate() {
        if (delegate == null && subs.iterator().hasNext()) {
            delegate = subs.iterator().next();
        }
        return delegate;
    }

    public static class SubTarget implements Serializable {
        private static final transient long serialVersionUID = -7506408081661144887L;
        @SerializedName("name")
        private final String name;
        @SerializedName("meta")
        private final Integer meta;

        private transient Block block;
        private transient ItemStack itemStack;

        public SubTarget(String name) {
            this.name = name;
            meta = null;
        }

        public Block getBlock() {
            if (block == null) {
                block = Block.getBlockFromName(name);
            }
            return block;
        }

        public ItemStack getItemStack() {
            if (itemStack == null) {
                itemStack = new ItemStack(getBlock());
                if (meta != null) {
                    itemStack.setItemDamage(meta);
                }
            }
            return itemStack;
        }

        public Integer getMeta() {
            return meta;
        }
    }
}
