package him.sniffer.config.property;

import net.minecraftforge.common.config.Configuration;

public class PropertyB extends IProperty {

    private final boolean defaultValue;

    public PropertyB(String category, String key, boolean defaultValue) {
        super(category, key);
        this.defaultValue = defaultValue;
    }

    public boolean get() {
        return property.getBoolean();
    }

    public void set(boolean value) {
        property.set(value);
    }

    @Override
    public void bind(Configuration config) {
        property = config.get(category, key, defaultValue);
    }
}
