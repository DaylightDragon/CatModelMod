package org.daylight.mixin.client;

import net.minecraft.entity.passive.CatEntity;
import org.daylight.util.PlayerToCatReplacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CatEntity.class)
public class CatEntityMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void cancelTick(CallbackInfo ci) {
        if (PlayerToCatReplacer.isDummyCat((CatEntity)(Object)this)) {
            ci.cancel();
        }
    }
}
