package him.sniffer.core;

import com.google.gson.annotations.SerializedName;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.io.Serializable;

import static him.sniffer.Sniffer.*;

/**
 * 探测子目标(对应一种具体方块).
 */
public class SubTarget implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("meta")
    private final Integer meta;

    private transient Block block;
    private transient ItemStack itemStack;
    private transient boolean checkout;
    private static final transient long serialVersionUID = -7506408081661144887L;

    /**
     * 子目标的构造函数.
     *
     * @param name 名称
     * @param meta 元数据
     */
    public SubTarget(String name, Integer meta) {
        this.name = name;
        this.meta = meta;
    }

    public SubTarget(Block block, Integer meta) {
        name = Block.blockRegistry.getNameForObject(block);
        this.meta = meta;
    }

    /**
     * 初始化各成员,检查对象合法性与安全性,移除非法对象.
     *
     * @return 是否符合要求 boolean
     */
    public boolean checkout() {
        logger.info("subtarget checkout");
        if (name != null && !name.isEmpty()) {
            block = Block.getBlockFromName(name);
            if (block != null && !block.equals(Blocks.air)) {
                name = Block.blockRegistry.getNameForObject(block);
                itemStack = new ItemStack(block);
                if (meta != null && meta >= 0 && meta <= 15) {
                    itemStack.setItemDamage(meta);
                }
                checkout = true;
                return true;
            }
        }
        return false;
    }

    /**
     * 获取子目标对应的方块.
     *
     * @return 方块 block
     */
    public Block getBlock() {
        proxy.checkout(checkout);
        return block;
    }

    /**
     * 获取子目标对应的物品.
     *
     * @return 物品 item stack
     */
    public ItemStack getItemStack() {
        proxy.checkout(checkout);
        return itemStack;
    }

    /**
     * 获取子目标元数据.
     *
     * @return 元数据 meta
     */
    public Integer getMeta() {
        return meta;
    }

    @Override
    public int hashCode() {
        if (name != null && !name.isEmpty()) {
            block = Block.getBlockFromName(name);
            if (block != null && !block.equals(Blocks.air)) {
                return block.hashCode() + (meta == null? -1 : meta.hashCode());
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SubTarget) {
            SubTarget sub = (SubTarget) obj;
            return meta == null? name.equals(sub.name) : name.equals(sub.name) && meta.equals(sub.meta);
        }
        return false;
    }
}
