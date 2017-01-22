package him.sniffer.config.property;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public abstract class IProperty {
    String category;
    String key;
    Property property;

    protected IProperty(String category, String key) {
        this.category = category;
        this.key = key;
    }

    public void setComment(String comment) {
        property.comment = comment;
    }

    abstract void bind(Configuration config);
}
