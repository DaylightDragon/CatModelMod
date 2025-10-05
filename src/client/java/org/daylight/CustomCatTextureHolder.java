package org.daylight;

import net.minecraft.util.Identifier;

public interface CustomCatTextureHolder {
    Identifier catModel$getCustomTexture();
    void catModel$setCustomTexture(Identifier texture);
    boolean catModel$shouldUpdateCustomTexture();
    void catModel$requestCustomTextureUpdate();
}
