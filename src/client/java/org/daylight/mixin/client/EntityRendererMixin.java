package org.daylight.mixin.client;

import net.minecraft.client.render.entity.CatEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.CatEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import org.daylight.CustomCatState;
import org.daylight.IShadowHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity, S extends EntityRenderState> implements IShadowHolder {
    @Shadow
    public abstract float getShadowRadius(S state);

    @Shadow
    public abstract float getShadowOpacity(S state);

    public float getShadowRadiusAccessor(CatEntityRenderState state) {
        return getShadowRadius((S) state);
    }

    public float getShadowOpacityAccessor(CatEntityRenderState state) {
        return getShadowOpacity((S) state);
    }

    @Inject(method = "getAndUpdateRenderState", at = @At("RETURN"))
    private void onGetAndUpdateRenderState(Entity entity, float tickDelta, CallbackInfoReturnable<EntityRenderState> cir) {
        EntityRenderState state = cir.getReturnValue();
        if (state instanceof CustomCatState customCatState) {
            customCatState.catmodel$setCurrentEntityId(entity.getUuid());
        }
    }
}
