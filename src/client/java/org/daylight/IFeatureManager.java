package org.daylight;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

import java.util.function.Predicate;

public interface IFeatureManager<T extends LivingEntity, M extends EntityModel<T>> {
    boolean catmodel$addFeature(FeatureRenderer<?, ?> feature);

    void catmodel$renderAllFeatures(LivingEntity livingEntity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, Predicate<FeatureRenderer<?, ?>> filter);
}
