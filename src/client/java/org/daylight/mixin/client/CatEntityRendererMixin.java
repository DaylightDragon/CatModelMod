package org.daylight.mixin.client;

import net.minecraft.client.render.entity.CatEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.CatEntityModel;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.Identifier;
import org.daylight.CustomCatTextureHolder;
import org.daylight.IFeatureManager;
import org.daylight.ModResources;
import org.daylight.features.CatChargeFeatureRenderer;
import org.daylight.util.PlayerToCatReplacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CatEntityRenderer.class)
public abstract class CatEntityRendererMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(EntityRendererFactory.Context context, CallbackInfo ci) {
        var feature = new CatChargeFeatureRenderer((FeatureRendererContext<CatEntity, CatEntityModel<CatEntity>>) this, ModResources.GHOST_TEXTURE); // context.getEntityModels() removed in 1.21.3
        if((Object) this instanceof IFeatureManager featureManager) {
            featureManager.catmodel$addFeature(feature);
        }
    }

//    @Inject( // TODO FIX SKIN
//            method = "updateRenderState(Lnet/minecraft/entity/passive/CatEntity;Lnet/minecraft/client/render/entity/state/CatEntityRenderState;F)V",
//            at = @At("TAIL")
//    )
//    private void onUpdateRenderState(CatEntity cat, CatEntityRenderState state, float tickDelta, CallbackInfo ci) {
//        if (isCustomCat(cat)) {
//            Identifier customTexture = getCatEntityCustomTexture(cat);
//            if(customTexture == null) return;
//            state.texture = customTexture;
//        }
//    }

    private boolean isCustomCat(CatEntity cat) {
        return PlayerToCatReplacer.isDummyCat(cat);
    }

    private Identifier getCatEntityCustomTexture(CatEntity cat) {
        if(cat instanceof CustomCatTextureHolder customCatTextureHolder) {
            return customCatTextureHolder.catModel$getCustomTexture();
        }
        return null;
    }
}
