package org.daylight.config;

import org.daylight.replacements.FabricStringConfigValue;
import org.daylight.replacements.common.IStringConfigValue;

public class ConfigHandler {
    public static final SimpleConfig CONFIG = new SimpleConfig();

    public static IStringConfigValue catVariant;

    public static void init() {
        CONFIG.load();

        catVariant = new FabricStringConfigValue(CONFIG, "catVariant", "SIAMESE");
    }
}
