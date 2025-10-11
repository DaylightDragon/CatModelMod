package org.daylight.mixin.client;

import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.CatEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.daylight.IRenderableFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FeatureRenderer.class)
public abstract class FeatureRendererAccessor<S extends EntityRenderState, M extends EntityModel<? super S>> implements IRenderableFeature<S> {
    @Shadow
    public abstract void render(MatrixStack matrices, OrderedRenderCommandQueue queue, int light, S state, float limbAngle, float limbDistance);

    @Override
    public void catify$render(MatrixStack matrices, OrderedRenderCommandQueue queue, int light, CatEntityRenderState state, float limbAngle, float limbDistance) {
        render(matrices, queue, light, (S) state, limbAngle, limbDistance);
    }
}
