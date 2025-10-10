package org.daylight.config;

import org.daylight.InvisibilityBehaviour;
import org.daylight.replacements.FabricBooleanConfigValue;
import org.daylight.replacements.FabricEnumConfigValue;
import org.daylight.replacements.FabricStringConfigValue;
import org.daylight.replacements.common.IBooleanConfigValue;
import org.daylight.replacements.common.IEnumConfigValue;
import org.daylight.replacements.common.IStringConfigValue;

public class ConfigHandler {
    public static final SimpleConfig CONFIG = new SimpleConfig();

    public static IStringConfigValue catVariant;
    public static IBooleanConfigValue catVariantVanilla;
    public static IBooleanConfigValue replacementActive;
    public static IBooleanConfigValue catDamageVisible;
    public static IBooleanConfigValue catHandActive;
    public static IEnumConfigValue invisibilityBehaviour;

    public static void init() {
        CONFIG.load();

        catVariant = new FabricStringConfigValue(CONFIG, "catVariant", "JELLIE");
        catVariantVanilla = new FabricBooleanConfigValue(CONFIG, "catVariantIsVanilla", true);
        replacementActive = new FabricBooleanConfigValue(CONFIG, "replacementActive", true);
        catHandActive = new FabricBooleanConfigValue(CONFIG, "catHandActive", true);
        catDamageVisible = new FabricBooleanConfigValue(CONFIG, "catDamageVisible", true);
        invisibilityBehaviour = new FabricEnumConfigValue(CONFIG, "invisibilityBehaviour", InvisibilityBehaviour.USUAL);
    }
}
