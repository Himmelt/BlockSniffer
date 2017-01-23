package him.sniffer.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import him.sniffer.config.property.PropertyD;
import him.sniffer.constant.ModInfo;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static him.sniffer.Sniffer.*;

public class Config {

    public final PropertyD hudX = new PropertyD(ModInfo.MODID, "hudX", 0.05D);
    public final PropertyD hudY = new PropertyD(ModInfo.MODID, "hudY", 0.05D);

    private final Configuration config;
    private final File targetJson;
    private static final Gson GSON = new GsonBuilder().create();

    public Config(File configDir) {
        config = new Configuration(new File(configDir, ModInfo.MODID + ".cfg"), ModInfo.VERSION);
        targetJson = new File(new File(configDir, ModInfo.MODID), "target.json");
        reload();
        loadComment(false);
        save();
    }

    public void reload() {
        config.load();
        bind();
        loadTargets();
    }

    public void loadComment(boolean server) {
        hudX.setComment(server? "HudX" : I18n.format("sniffer.config.hudX"));
        hudY.setComment(server? "HudY" : I18n.format("sniffer.config.hudY"));
    }

    public void save() {
        config.save();
        saveTargets();
        logger.info("config saved!");
    }

    private void bind() {
        hudX.bind(config);
        hudY.bind(config);
    }

    private void loadTargets() {
        try {
            if (!targetJson.exists() || !targetJson.isFile()) {
                targetJson.delete();
                targetJson.createNewFile();
                proxy.sniffer.targets = new Targets();
            } else {
                proxy.sniffer.targets = GSON.fromJson(FileUtils.readFileToString(targetJson), Targets.class);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            proxy.sniffer.reset();
        }
    }

    private void saveTargets() {
        try {
            FileUtils.writeStringToFile(targetJson, GSON.toJson(proxy.sniffer.targets));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
