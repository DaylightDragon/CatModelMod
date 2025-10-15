package org.daylight;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Unique;

public interface IShadowHolder<T extends Entity> {
    float catModel$getShadowOpacityAccessor();

    @Unique
    float getShadowRadiusAccessor(T entity);
}
