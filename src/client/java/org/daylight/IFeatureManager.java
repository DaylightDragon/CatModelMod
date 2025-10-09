package org.daylight;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.daylight.features.CatChargeFeatureRenderer;

import java.util.function.Predicate;

public interface IFeatureManager {
    boolean catmodel$addFeature(FeatureRenderer<?, ?> feature);
    void renderAllFeatures(LivingEntityRenderState livingEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, Predicate<FeatureRenderer<?, ?>> filter);
    CatChargeFeatureRenderer getCatChargeFeatureRenderer();
}
