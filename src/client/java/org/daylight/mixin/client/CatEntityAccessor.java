package org.daylight.mixin.client;

import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.CatVariant;
import net.minecraft.entity.passive.CatVariants;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CatEntity.class)
public interface CatEntityAccessor {
    @Invoker("setVariant")
    void invokeSetVariant(RegistryEntry<CatVariant> variant);
}

