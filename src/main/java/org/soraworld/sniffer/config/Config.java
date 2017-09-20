package org.soraworld.sniffer.config;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.soraworld.sniffer.config.property.IProperty;
import org.soraworld.sniffer.constant.Constants;

import java.io.File;

@SideOnly(Side.CLIENT)
public class Config {

    private static final String catGui = "gui";
    private static final String catDelay = "delay";

    public final IProperty.D hudX = new IProperty.D(catGui, "hudX", 0.05D);
    public final IProperty.D hudY = new IProperty.D(catGui, "hudY", 0.05D);
    public final IProperty.F gamma = new IProperty.F(catGui, "gamma", 1.0F, 0.0F, 15.0F);
    public final IProperty.I hudDelay = new IProperty.I(catDelay, "hudDelay", 3000, 0, Integer.MAX_VALUE);
    public final IProperty.I scanDelay = new IProperty.I(catDelay, "scanDelay", 500, 100, Integer.MAX_VALUE);
    public final IProperty.I particleDelay = new IProperty.I(catDelay, "particleDelay", 5, 1, Integer.MAX_VALUE);

    private final Configuration config;

    public Config(File configDir) {
        config = new Configuration(new File(configDir, Constants.MODID + ".cfg"), Constants.VERSION);
        reload();
        save();
    }

    public void reload() {
        config.load();
        bind();
        comments();
    }

    private void comments() {
        hudX.setComment(I18n.format("sf.cfg.hudX"));
        hudY.setComment(I18n.format("sf.cfg.hudY"));
        gamma.setComment(I18n.format("sf.cfg.gamma"));
        hudDelay.setComment(I18n.format("sf.cfg.hudDelay"));
        scanDelay.setComment(I18n.format("sf.cfg.scanDelay"));
        particleDelay.setComment(I18n.format("sf.cfg.particleDelay"));
    }

    public void save() {
        config.save();
    }

    private void bind() {
        hudX.bind(config);
        hudY.bind(config);
        gamma.bind(config);
        hudDelay.bind(config);
        scanDelay.bind(config);
        particleDelay.bind(config);
    }
}
