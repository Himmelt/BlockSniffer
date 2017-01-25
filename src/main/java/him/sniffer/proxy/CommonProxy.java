package him.sniffer.proxy;

import him.sniffer.config.Config;
import him.sniffer.core.BlockSniffer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.io.File;

public abstract class CommonProxy {

    public KeyBinding keySwitch =
            new KeyBinding(I18n.format("sniffer.switch"), Keyboard.KEY_O, "key.categories.gameplay");

    public final BlockSniffer sniffer = new BlockSniffer();
    public Config config;

    public abstract void loadConfig(File cfgDir);

    public abstract void registCommand();

    public abstract void registKeyBinding();

    public abstract void registEventHandler();

    public abstract void addChatMessage(String message);

    /**
     * 安全检查,可以在生产环境中移除该检查.
     *
     * @param checkout 是否完成检查
     */
    public abstract void checkout(boolean checkout);
}
