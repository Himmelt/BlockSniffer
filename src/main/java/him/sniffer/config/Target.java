package him.sniffer.config;

import com.google.gson.annotations.SerializedName;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class Target implements Serializable {
    private static final transient long serialVersionUID = 5468612871816526011L;
    private static final Pattern PATTERN_NAME = Pattern.compile("^tile.*name$");
    @SerializedName("subs")
    private final Set<SubTarget> subs;
    @SerializedName("colorValue")
    private String colorValue;
    @SerializedName("mode")
    public int mode;
    @SerializedName("depth")
    public int[] depth;
    @SerializedName("h_range")
    public int hRange;
    @SerializedName("v_range")
    public int vRange;

    private transient SubTarget delegate;
    private transient Color color;

    public Target(String name, String color) {
        subs = new HashSet<SubTarget>();
        subs.add(new SubTarget(name));
        colorValue = color;
        mode = 0;
        depth = new int[] { 0, 64 };
        hRange = 1;
        vRange = 16;
        try {
            if (color.startsWith("#")) {
                this.color = Color.decode(color);
            } else if ("map".equals(color)) {
                this.color = null;
            } else {
                this.color = Color.getColor(color, Color.CYAN);
            }
        } catch (Exception ignored) {
            colorValue = "map";
        }
    }

    public Target(String name, Color color) {
        this(name, '#' + Integer.toHexString(color.getRGB() & 0x00ffffff));
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

    public String getName() {
        String name = getDelegate().getBlock().getLocalizedName();
        if (name != null && !PATTERN_NAME.matcher(name).matches()) {
            return name;
        }
        name = getDelegate().getItemStack().getDisplayName();
        if (name != null && !PATTERN_NAME.matcher(name).matches()) {
            return name;
        }
        return I18n.format("sniffer.unknowBlock");
    }

    public Color getColor() {
        if (colorValue == null || "map".equals(colorValue) || colorValue.isEmpty()) {
            colorValue = "map";
            return null;
        }
        if (color == null && colorValue != null) {
            try {
                if (colorValue.startsWith("#")) {
                    color = Color.decode(colorValue);
                } else {
                    color = Color.getColor(colorValue, Color.CYAN);
                }
            } catch (Exception ignored) {
                colorValue = "map";
            }
        }
        return color;
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
