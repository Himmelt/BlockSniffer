package him.sniffer.proxy;

import him.sniffer.config.Config;
import him.sniffer.constant.Constant;
import him.sniffer.core.BlockSniffer;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

import java.io.File;

public abstract class CommonProxy {

    public Config config;
    public final BlockSniffer sniffer = new BlockSniffer();
    public final KeyBinding keySwitch = new KeyBinding(I18n.format("sf.key.switch"), Keyboard.KEY_O, "key.categories.gameplay");

    public String getBlockName(Block block, Integer meta) {
        if (block == null) {
            return I18n.format("sf.unknow.block");
        }
        ItemStack itemStack = new ItemStack(block);
        if (meta != null) {
            itemStack.setItemDamage(meta);
        }
        String name = itemStack.getDisplayName();
        if (name != null && !name.isEmpty()) {
            return name;
        }
        name = block.getLocalizedName();
        if (name != null && !name.isEmpty() && !Constant.PATTERN_NAME.matcher(name).matches()) {
            return name;
        }
        return I18n.format("sf.unknow.block");
    }

    public abstract void loadConfig(File cfgDir);

    public abstract void registCommand();

    public abstract void registKeyBinding();

    public abstract void registEventHandler();

    public abstract void addChatMessage(String key, Object... objects);

}
