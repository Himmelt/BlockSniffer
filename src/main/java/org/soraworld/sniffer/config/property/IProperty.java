package org.soraworld.sniffer.config.property;

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
        property.setComment(comment);
    }

    abstract void bind(Configuration config);

    public static class PropertyB extends IProperty {

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

    public static class PropertyD extends IProperty {

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

    public static class PropertyS extends IProperty {

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

    public static class PropertyL extends IProperty {

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

    public static class PropertyI extends IProperty {

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
}
