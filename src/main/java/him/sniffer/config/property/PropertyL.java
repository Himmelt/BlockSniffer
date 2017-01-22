package him.sniffer.config.property;

import net.minecraftforge.common.config.Configuration;

public class PropertyL extends IProperty {

    private final long defaultValue;

    PropertyL(String category, String key, long defaultValue) {
        super(category, key);
        this.defaultValue = defaultValue;
    }

    public long get() {
        return (long) property.getDouble();
    }

    public void set(long value) {
        property.set(value);
    }

    @Override
    public void bind(Configuration config) {
        property = config.get(category, key, defaultValue);
    }
}
