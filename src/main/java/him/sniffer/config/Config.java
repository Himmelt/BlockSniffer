package him.sniffer.config;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import him.sniffer.config.property.IProperty.PropertyD;
import him.sniffer.constant.Mod;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

import static him.sniffer.Sniffer.*;

@SideOnly(Side.CLIENT)
public class Config {

    public final PropertyD hudX = new PropertyD(Mod.MODID, "hudX", 0.05D);
    public final PropertyD hudY = new PropertyD(Mod.MODID, "hudY", 0.05D);

    private final Configuration config;
    private final File jsonFile;

    public Config(File configDir) {
        config = new Configuration(new File(configDir, Mod.MODID + ".cfg"), Mod.VERSION);
        jsonFile = new File(new File(configDir, Mod.MODID), "target.json");
        reload();
        comments();
        save();
    }

    public void reload() {
        config.load();
        bind();
        proxy.sniffer.reload(jsonFile);
        proxy.setGamma(1);
        Mod.logger.info("config reloaded!");
    }

    public void comments() {
        hudX.setComment(I18n.format("sf.cfg.hudX"));
        hudY.setComment(I18n.format("sf.cfg.hudY"));
    }

    public void save() {
        config.save();
        proxy.sniffer.save(jsonFile);
        Mod.logger.info("config saved!");
    }

    private void bind() {
        hudX.bind(config);
        hudY.bind(config);
    }

}
