package him.sniffer.proxy;

import him.sniffer.config.Config;
import him.sniffer.core.BlockSniffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.io.File;

public abstract class CommonProxy {

    public Config config;
    public final BlockSniffer sniffer = new BlockSniffer();
    public final KeyBinding keySwitch = new KeyBinding(I18n.format("sf.key.switch"), Keyboard.KEY_O, "key.categories.gameplay");
    public final Minecraft client = Minecraft.getMinecraft();

    public abstract void loadConfig(File cfgDir);

    public abstract void registCommand();

    public abstract void registKeyBinding();

    public abstract void registEventHandler();

    public abstract void addChatMessage(String key, Object... objects);

    public void setGamma(int gamma) {
        client.gameSettings.gammaSetting = gamma >= 15? 15 : gamma;
    }

    public float getGamma() {
        return client.gameSettings.gammaSetting;
    }
}
