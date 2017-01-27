package him.sniffer.core;

import com.google.gson.annotations.SerializedName;
import him.sniffer.constant.ColorHelper;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static him.sniffer.Sniffer.*;
import static him.sniffer.constant.ModInfo.*;

/**
 * 探测目标.
 */
public class Target implements Serializable {

    @SerializedName("subs")
    private final HashSet<SubTarget> subs;
    @SerializedName("color")
    public String colorValue;
    /**
     * 工作模式:
     * 0 -- [depth_0 , depth_1]
     * 1 -- [posY-vRange , posY+vRange]
     */
    @SerializedName("mode")
    public int mode;
    /**
     * 探测范围[0-255].
     */
    @SerializedName("depth")
    public int[] depth;
    /**
     * 水平探测范围(单位:区块).
     */
    @SerializedName("h_range")
    public int hRange;
    /**
     * 垂直探测范围(单位:方块).
     */
    @SerializedName("v_range")
    public int vRange;

    private transient Color color;
    private transient boolean checkout;
    private transient SubTarget delegate;
    private transient HashMap<Integer, SubTarget> map;
    private static final transient long serialVersionUID = 5468612871816526011L;

    /**
     * 探测目标构造函数.
     *
     * @param name 子目标名称
     * @param meta 子目标元数据
     */
    public Target(String name, Integer meta) {
        subs = new HashSet<SubTarget>();
        SubTarget sub = new SubTarget(name, meta);
        subs.add(sub);
        colorValue = "map";
        mode = 0;
        depth = new int[] { 0, 64 };
        hRange = 1;
        vRange = 16;
    }

    /**
     * 探测目标构造函数.
     *
     * @param block 子目标方块
     * @param meta 子目标元数据
     */
    public Target(Block block, Integer meta) {
        this(Block.blockRegistry.getNameForObject(block), meta);
    }

    @Override
    public int hashCode() {
        return subs.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Target) {
            Target target = (Target) obj;
            return target.subs.equals(obj);
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("subs:").append(subs.size()).append(";color:")
               .append(colorValue).append(";mode:").append(mode).append(";delegate:").append(delegate);
        return builder.toString();
    }

    /**
     * 初始化各成员, 检查对象合法性与安全性, 移除非法对象.
     * 每次创建或新增都要检查!!!
     *
     * @return 是否符合要求 boolean
     */
    public boolean checkout() {
        logger.info("target checkout");
        subs.removeIf(subTarget -> !subTarget.checkout());//检查子目标,不符合则删除
        if (subs.size() >= 1) {
            // 设置代理
            delegate = subs.iterator().next();
            // 检查颜色
            color = ColorHelper.getColor(colorValue);
            if (color == null) {
                colorValue = "map";
            }
            // 检查模式
            if (mode != 0) {
                mode = 1;
            }
            // 检查探测深度
            if (depth == null || depth.length != 2 || depth[0] < 0 || depth[1] < 0) {
                depth = new int[] { 0, 64 };
            } else if (depth[0] > depth[1]) {
                int temp = depth[0];
                depth[0] = depth[1];
                depth[1] = temp;
            }
            // 检查水平垂直探测范围
            if (hRange < 0 || hRange > 15 || vRange <= 0 || vRange >= 255) {
                hRange = 1;
                vRange = 16;
            }
            checkout = true;
            return true;
        }
        logger.info(subs.size() + "false");
        return false;
    }

    /**
     * 方块匹配检查.
     *
     * @param block 目标方块
     * @param meta 元数据
     *
     * @return 是否匹配 boolean
     */
    public boolean match(Block block, int meta) {
        proxy.checkout(checkout);
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

    /**
     * 获取展示名称.
     *
     * @return 展示名 name
     */
    public String getName() {
        proxy.checkout(checkout);
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

    /**
     * 获取粒子颜色.
     * 如果颜色无效或使用MapColor,则返回null
     *
     * @return 颜色 color
     */
    public Color getColor() {
        proxy.checkout(checkout);
        return color;
    }

    /**
     * 获取Target的代理(第一个子目标).
     *
     * @return 代理 delegate
     */
    public SubTarget getDelegate() {
        proxy.checkout(checkout);
        return delegate;
    }

    public void addSubTarget(SubTarget sub) {
        subs.add(sub);
        checkout();
    }

    public Map<Integer, SubTarget> getSublist() {
        if (map == null) {
            map = new HashMap<>();
        }
        map.clear();
        int i = 0;
        for (SubTarget sub : subs) {
            map.put(i, sub);
            i++;
        }
        return map;
    }

    public int removeSubTarget(int uid) {
        Map<Integer, SubTarget> map = getSublist();
        if (map.size() >= 1 && map.containsKey(uid)) {
            subs.remove(map.get(uid));
            map.remove(uid);
            if (subs.size() >= 1) {
                delegate = subs.iterator().next();
            } else {
                delegate = null;
            }
            return subs.size();
        }
        return -1;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
