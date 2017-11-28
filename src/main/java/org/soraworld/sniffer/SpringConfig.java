package org.soraworld.sniffer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.soraworld.sniffer.constant.Constants;
import org.soraworld.sniffer.core.TBlock;
import org.soraworld.sniffer.core.Target;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.soraworld.sniffer")
public class SpringConfig {

    @Bean("logger")
    public Logger logger() {
        return LogManager.getLogger(Constants.NAME);
    }

    @Bean
    public Minecraft minecraft() {
        return Minecraft.getMinecraft();
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(Target.class, new Target.Adapter())
                .registerTypeAdapter(TBlock.class, new TBlock.Adapter())
                .setPrettyPrinting().create();
    }
}
