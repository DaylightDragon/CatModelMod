package org.daylight;

import net.minecraft.client.render.entity.state.CatEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import org.spongepowered.asm.mixin.Shadow;

public interface IShadowHolder {
    float getShadowRadiusAccessor(CatEntityRenderState state);

    float getShadowOpacityAccessor(CatEntityRenderState state);
}
