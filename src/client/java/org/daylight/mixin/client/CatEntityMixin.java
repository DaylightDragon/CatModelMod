package org.daylight.mixin.client;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SkinOverlayOwner;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.Identifier;
import org.daylight.CustomCatTextureHolder;
import org.daylight.features.CatChargeFeatureRenderer;
import org.daylight.util.PlayerToCatReplacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(CatEntity.class)
public class CatEntityMixin implements CustomCatTextureHolder, SkinOverlayOwner {
    @Unique
    private Identifier customTexture = null;
    @Unique
    private boolean customTextureUpdateRequired = false;
//    @Unique
//    private float catmodel$chargeProgress = 0;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void cancelTick(CallbackInfo ci) {
        if (PlayerToCatReplacer.isDummyCat((CatEntity)(Object)this)) {
            ci.cancel();
        }
    }

    @Override
    public Identifier catModel$getCustomTexture() {
        return customTexture;
    }

    @Override
    public void catModel$setCustomTexture(Identifier texture) {
        this.customTexture = texture;
    }

    @Override
    public boolean catModel$shouldUpdateCustomTexture() {
        return customTextureUpdateRequired;
    }

    @Override
    public void catModel$requestCustomTextureUpdate() {
        customTextureUpdateRequired = true;
    }

//    @Override
//    public float catmodel$getChargeProgress() {
//        return catmodel$chargeProgress;
//    }
//
//    @Override
//    public void catmodel$setChargeProgress(float value) {
//        this.catmodel$chargeProgress = value;
//    }


    @Override
    public boolean shouldRenderOverlay() {
        if((Object) this instanceof CatEntity cat) {
            CatChargeFeatureRenderer.CatChargeData data = CatChargeFeatureRenderer.getChargeData(cat);
            return data.chargeActive;
        }
        return false;
    }
}
