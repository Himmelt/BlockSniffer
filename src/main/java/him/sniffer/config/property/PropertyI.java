package him.sniffer.config.property;

import net.minecraftforge.common.config.Configuration;

public class PropertyI extends IProperty {

    private final int defaultValue;

    public PropertyI(String category, String key, int defaultValue) {
        super(category, key);
        this.defaultValue = defaultValue;
    }

    public int get() {
        return property.getInt();
    }

    public void set(int value) {
        property.set(value);
    }

    @Override
    public void bind(Configuration config) {
        property = config.get(category, key, defaultValue);
    }
}
