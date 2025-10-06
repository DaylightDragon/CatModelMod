package org.daylight.mixin.client;

import net.minecraft.client.render.entity.state.CatEntityRenderState;
import net.minecraft.entity.Entity;
import org.daylight.CustomCatState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.UUID;

@Mixin(CatEntityRenderState.class)
public class CatEntityRenderStateMixin implements CustomCatState {
    @Unique
    private boolean catmodel$chargeActive = false;
    @Unique
    private float catmodel$chargeProgress = 0;
    @Unique
    private Float catmodel$customDelta = null;
    @Unique
    private boolean isMainSpecialCat = false;
    @Unique
    private UUID catmodel$entity;

    @Override
    public boolean catmodel$getChargeActive() {
        return catmodel$chargeActive;
    }

    @Override
    public void catmodel$setChargeActive(boolean catmodel$chargeActive) {
        this.catmodel$chargeActive = catmodel$chargeActive;
    }

    @Override
    public boolean catmodel$isMainSpecialCat() {
        return isMainSpecialCat;
    }

    @Override
    public void catmodel$setAsMainSpecialCat(boolean value) {
        this.isMainSpecialCat = value;
    }

    @Override
    public float catmodel$getChargeProgress() {
        return catmodel$chargeProgress;
    }

    @Override
    public void catmodel$setChargeProgress(float value) {
        this.catmodel$chargeProgress = value;
    }

    @Override
    public UUID catmodel$getCurrentEntityId() {
        return catmodel$entity;
    }

    @Override
    public void catmodel$setCurrentEntityId(UUID entity) {
        this.catmodel$entity = entity;
    }

    //    @Override
//    public Float catmodel$getCustomTimeDelta() {
//        return catmodel$customDelta;
//    }
//
//    @Override
//    public void catmodel$setCustomTimeDelta(Float value) {
//        this.catmodel$customDelta = value;
//    }
}

