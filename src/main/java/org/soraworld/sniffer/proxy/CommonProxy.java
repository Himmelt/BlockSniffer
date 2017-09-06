package org.soraworld.sniffer.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.soraworld.sniffer.client.gui.HudRenderer;
import org.soraworld.sniffer.config.Config;
import org.soraworld.sniffer.core.Sniffer;

import java.io.File;

public abstract class CommonProxy {

    public Config config;
    public Minecraft client;
    public Sniffer sniffer;
    public HudRenderer hudRenderer;
    public final KeyBinding keySwitch = new KeyBinding(I18n.format("sf.key.switch"), Keyboard.KEY_O, "key.categories.gameplay");

    public abstract void init();

    public abstract void loadConfig(File cfgDir);

    public abstract void registCommand();

    public abstract void registKeyBinding();

    public abstract void registEventHandler();

    public abstract void addChatMessage(String key, Object... objects);

    public void setGamma(int gamma) {
        client.gameSettings.gammaSetting = gamma >= 15 ? 15 : gamma;
    }

    public float getGamma() {
        return client.gameSettings.gammaSetting;
    }
}
