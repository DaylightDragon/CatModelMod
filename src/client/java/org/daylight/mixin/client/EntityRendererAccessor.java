package org.daylight.mixin.client;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityRenderer.class)
public interface EntityRendererAccessor {
    @Invoker("getShadowRadius")
    float callGetShadowRadius(EntityRenderState state);

//    @Invoker("getShadowOpacity")
//    float callGetShadowOpacity(EntityRenderState state);
}
