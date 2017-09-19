package org.soraworld.sniffer.config;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.soraworld.sniffer.config.property.IProperty.PropertyD;
import org.soraworld.sniffer.config.property.IProperty.PropertyI;
import org.soraworld.sniffer.constant.Constants;

import java.io.File;

@SideOnly(Side.CLIENT)
public class Config {

    private static final String catGui = "gui";
    private static final String catDelay = "delay";

    public final PropertyD hudX = new PropertyD(catGui, "hudX", 0.05D);
    public final PropertyD hudY = new PropertyD(catGui, "hudY", 0.05D);
    public final PropertyI guiDelay = new PropertyI(catDelay, "guiDelay", 3000, 0, Integer.MAX_VALUE);
    public final PropertyI clickDelay = new PropertyI(catDelay, "clickDelay", 500, 100, Integer.MAX_VALUE);
    public final PropertyI particleDelay = new PropertyI(catDelay, "particleDelay", 5, 1, Integer.MAX_VALUE);

    private final Configuration config;

    public Config(File configDir) {
        config = new Configuration(new File(configDir, Constants.MODID + ".cfg"), Constants.VERSION);
        config.load();
        bind();
        comments();
        config.save();
    }

    public void reload() {
        config.load();
        bind();
    }

    private void comments() {
        hudX.setComment(I18n.format("sf.cfg.hudX"));
        hudY.setComment(I18n.format("sf.cfg.hudY"));
        guiDelay.setComment(I18n.format("sf.cfg.guiDelay"));
        clickDelay.setComment(I18n.format("sf.cfg.clickDelay"));
        particleDelay.setComment(I18n.format("sf.cfg.particleDelay"));
    }

    public void save() {
        config.save();
    }

    private void bind() {
        hudX.bind(config);
        hudY.bind(config);
        guiDelay.bind(config);
        clickDelay.bind(config);
        particleDelay.bind(config);
    }
}
