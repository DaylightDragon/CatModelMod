package org.daylight;

import net.minecraft.client.render.entity.state.EntityRenderState;

public interface IShadowHolder<S extends EntityRenderState> {
    float getShadowRadiusAccessor(S state);

    float catModel$getShadowOpacityAccessor(S state);
}
