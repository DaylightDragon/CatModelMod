package org.daylight.config;

import org.daylight.replacements.FabricBooleanConfigValue;
import org.daylight.replacements.FabricStringConfigValue;
import org.daylight.replacements.common.IBooleanConfigValue;
import org.daylight.replacements.common.IStringConfigValue;

public class ConfigHandler {
    public static final SimpleConfig CONFIG = new SimpleConfig();

    public static IStringConfigValue catVariant;
    public static IBooleanConfigValue replacementActive;

    public static boolean cachedReplacementActive = true;

    public static void init() {
        CONFIG.load();

        catVariant = new FabricStringConfigValue(CONFIG, "catVariant", "SIAMESE");
        replacementActive = new FabricBooleanConfigValue(CONFIG, "replacementActive", true);

        cachedReplacementActive = replacementActive.get();
    }
}
