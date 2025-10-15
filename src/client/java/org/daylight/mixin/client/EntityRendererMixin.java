package org.daylight.mixin.client;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import org.daylight.IShadowHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> implements IShadowHolder<T> {
    @Override
    public float catModel$getShadowOpacityAccessor() {
        return shadowOpacity;
    }

    @Shadow
    protected float shadowOpacity;

    @Shadow
    protected abstract float getShadowRadius(T entity);

    @Unique
    @Override
    public float getShadowRadiusAccessor(T entity) {
        return getShadowRadius(entity);
    }

//    public float getShadowOpacityAccessor(CatEntityRenderState state) {
//        return getShadowOpacity((S) state);
//    }

//    @Inject(method = "getAndUpdateRenderState", at = @At("RETURN")) // TODO FIX SKINS
//    private void onGetAndUpdateRenderState(Entity entity, float tickDelta, CallbackInfoReturnable<EntityRenderState> cir) {
//        EntityRenderState state = cir.getReturnValue();
//        if (state instanceof CustomCatState customCatState) {
//            customCatState.catmodel$setCurrentEntityId(entity.getUuid());
//        }
//    }
}
