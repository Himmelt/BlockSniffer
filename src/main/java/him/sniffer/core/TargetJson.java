package him.sniffer.core;

import com.google.gson.annotations.SerializedName;
import net.minecraft.init.Blocks;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;

import static him.sniffer.Sniffer.*;

/**
 * 探测目标Json配置.
 */
public class TargetJson implements Serializable {
    @SerializedName("comment")
    private final String comment;
    @SerializedName("version")
    private final String version;
    @SerializedName("targets")
    private final HashSet<Target> targets;

    private transient boolean checkout;
    private static final long serialVersionUID = -569722550359231209L;

    /**
     * 探测目标json配置的构造函数.
     */
    public TargetJson() {
        comment = "targets";
        version = "1.7.10";
        targets = new HashSet<Target>();
        targets.add(new Target(Blocks.diamond_block, null));
    }

    /**
     * 初始化各成员, 检查对象合法性与安全性, 移除非法对象.
     * 每次创建或新增都要检查!!!
     */
    public void checkout() {
        targets.removeIf(target -> !target.checkout());//检查探测目标,不符合则删除
        checkout = true;
    }

    /**
     * 获取targets的迭代器.
     *
     * @return 迭代器 iterator
     */
    public Iterator<Target> iterator() {
        proxy.checkout(checkout);
        return targets.iterator();
    }

    /**
     * 新增探测目标.
     *
     * @param target 新目标
     */
    public void addTarget(Target target) {
        proxy.checkout(checkout);
        targets.add(target);
        checkout();
    }

    @Override
    public int hashCode() {
        return targets.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TargetJson) {
            TargetJson json = (TargetJson) obj;
            return targets.equals(json.targets);
        }
        return false;
    }

    public int size() {
        return targets.size();
    }
}
