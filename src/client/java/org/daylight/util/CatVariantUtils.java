package org.daylight.util;

import net.minecraft.entity.passive.CatVariant;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.Locale;

public class CatVariantUtils {
    public static String serializeVariant(RegistryKey<CatVariant> key) {
        return key.getValue().getPath().toUpperCase(Locale.ROOT);
    }

    public static RegistryKey<CatVariant> deserializeVariant(String name) {
        String path = name.toLowerCase(Locale.ROOT);
        return RegistryKey.of(RegistryKeys.CAT_VARIANT, Identifier.of(path));
    }
}
