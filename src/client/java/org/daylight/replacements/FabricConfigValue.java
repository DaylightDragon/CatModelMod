package org.daylight.replacements;

import org.daylight.config.SimpleConfig;
import org.daylight.replacements.common.IConfigValue;

public class FabricConfigValue<T> implements IConfigValue<T> {
    private final SimpleConfig config;
    private final String key;
    private final T defaultValue;

    public FabricConfigValue(SimpleConfig config, String key, T defaultValue) {
        this.config = config;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get() {
        Object raw = config.get(key, defaultValue);
        if (raw == null) {
            return defaultValue;
        }

        if (defaultValue instanceof Integer && raw instanceof Number num) {
            return (T) Integer.valueOf(num.intValue());
        }
        if (defaultValue instanceof Boolean && raw instanceof Boolean b) {
            return (T) b;
        }
        if (defaultValue instanceof Double && raw instanceof Number num) {
            return (T) Double.valueOf(num.doubleValue());
        }
        if (defaultValue instanceof String && !(raw instanceof String)) {
            return (T) raw.toString();
        }

        // fallback
        try {
            return (T) raw;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    @Override
    public void set(T value) {
        config.set(key, value);
    }

    @Override
    public void save() {
        config.save();
    }
}
