package him.sniffer.config.property;

import net.minecraftforge.common.config.Configuration;

public class PropertyD extends IProperty {

    private final double defaultValue;

    public PropertyD(String category, String key, double defaultValue) {
        super(category, key);
        this.defaultValue = defaultValue;
    }

    public double get() {
        return property.getDouble();
    }

    public void set(double value) {
        property.set(value);
    }

    @Override
    public void bind(Configuration config) {
        property = config.get(category, key, defaultValue);
    }
}
