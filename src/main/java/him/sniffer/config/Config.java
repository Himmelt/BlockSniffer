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
    private final File targetJsonFile;
    private static final Gson GSON = new GsonBuilder().create();

    public Config(File configDir) {
        config = new Configuration(new File(configDir, ModInfo.MODID + ".cfg"), ModInfo.VERSION);
        targetJsonFile = new File(new File(configDir, ModInfo.MODID), "target.json");
        reload();
        loadComment(false);
        save();
    }

    public void reload() {
        config.load();
        bind();
        loadTargets();
        logger.info("config reloaded!");
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
            if (!targetJsonFile.exists() || !targetJsonFile.isFile()) {
                targetJsonFile.delete();
                targetJsonFile.createNewFile();
                proxy.sniffer.targetJson = new TargetJson();
                proxy.sniffer.targetJson.checkout();
            } else {
                proxy.sniffer.targetJson = GSON.fromJson(FileUtils.readFileToString(targetJsonFile), TargetJson.class);
                proxy.sniffer.targetJson.checkout();
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            proxy.sniffer.reset();
        }
    }

    private void saveTargets() {
        try {
            FileUtils.writeStringToFile(targetJsonFile, GSON.toJson(proxy.sniffer.targetJson));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
