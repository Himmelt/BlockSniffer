package org.soraworld.sniffer.config;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.soraworld.sniffer.config.property.IProperty.PropertyD;
import org.soraworld.sniffer.constant.Constants;

import java.io.File;

@SideOnly(Side.CLIENT)
public class Config {

    public final PropertyD hudX = new PropertyD(Constants.MODID, "hudX", 0.05D);
    public final PropertyD hudY = new PropertyD(Constants.MODID, "hudY", 0.05D);

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

    public void comments() {
        hudX.setComment(I18n.format("sf.cfg.hudX"));
        hudY.setComment(I18n.format("sf.cfg.hudY"));
    }

    public void save() {
        config.save();
    }

    private void bind() {
        hudX.bind(config);
        hudY.bind(config);
    }

}
