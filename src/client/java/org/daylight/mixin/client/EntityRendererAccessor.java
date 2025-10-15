package org.daylight.mixin.client;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererAccessor<T extends Entity> {
    @Shadow
    protected float shadowRadius;

    @Shadow
    protected abstract float getShadowRadius(T entity);

    @Shadow
    protected float shadowOpacity;

//    @Invoker("getShadowOpacity")
//    float callGetShadowOpacity(EntityRenderState state);
}
