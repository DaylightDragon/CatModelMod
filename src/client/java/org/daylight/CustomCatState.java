package org.daylight;

import net.minecraft.entity.Entity;

public interface CustomCatState {
    float catmodel$getChargeProgress();
    void catmodel$setChargeProgress(float value);
//    Float catmodel$getCustomTimeDelta();
//    void catmodel$setCustomTimeDelta(Float value);
    boolean catmodel$getChargeActive();
    void catmodel$setChargeActive(boolean value);
    boolean catmodel$isMainSpecialCat();
    void catmodel$setAsMainSpecialCat(boolean value);
    Entity catmodel$getCurrentEntity();
    void catmodel$setCurrentEntity(Entity entity);
}
