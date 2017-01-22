package him.sniffer.config.property;

import net.minecraftforge.common.config.Configuration;

public class PropertyS extends IProperty {

    private final String defaultValue;

    PropertyS(String category, String key, String defaultValue) {
        super(category, key);
        this.defaultValue = defaultValue;
    }

    public String get() {
        return property.getString();
    }

    public void set(String value) {
        property.set(value);
    }

    @Override
    public void bind(Configuration config) {
        property = config.get(category, key, defaultValue);
    }
}
