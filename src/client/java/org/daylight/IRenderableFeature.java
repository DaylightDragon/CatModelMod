package org.daylight;

import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.state.CatEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;

public interface IRenderableFeature<S extends EntityRenderState> {
    void catify$render(MatrixStack matrices, OrderedRenderCommandQueue queue, int light, CatEntityRenderState state, float limbAngle, float limbDistance);
}
