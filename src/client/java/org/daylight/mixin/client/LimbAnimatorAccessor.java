package org.daylight.mixin.client;

import net.minecraft.entity.LimbAnimator;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.CatVariant;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LimbAnimator.class)
public interface LimbAnimatorAccessor {

    @Accessor("animationProgress")
    float getAnimationProgress();

    @Accessor("animationProgress")
    void setAnimationProgress(float progress);

    @Accessor("speed")
    float getSpeed();

    @Accessor("speed")
    void setSpeed(float speed);
}

