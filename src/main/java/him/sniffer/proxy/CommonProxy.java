package him.sniffer.proxy;

import him.sniffer.client.BlockSniffer;
import him.sniffer.config.Config;
import net.minecraft.client.settings.KeyBinding;

import java.io.File;

public abstract class CommonProxy {

    public KeyBinding keySwitch = new KeyBinding("Enable Scenter / Next Block", 24, "key.categories.gameplay");

    public final BlockSniffer sniffer = new BlockSniffer();
    public Config config;

    public abstract void loadConfig(File cfgDir);

    public abstract void registKeyBinding();

    public abstract void registEventHandler();
}
