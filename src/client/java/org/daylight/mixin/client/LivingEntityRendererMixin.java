package org.daylight.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.daylight.IFeatureManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.function.Predicate;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> implements IFeatureManager<T, M> {
    @Shadow
    protected abstract boolean addFeature(FeatureRenderer<T, M> feature);

    @Shadow
    @Final
    protected List<FeatureRenderer<T, M>> features;

    @Shadow
    protected abstract float getAnimationProgress(T entity, float tickDelta);

    @Shadow
    public static boolean shouldFlipUpsideDown(LivingEntity entity) {
        return false;
    }

    @Shadow
    protected abstract void setupTransforms(T entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta, float scale);

    @Shadow
    protected abstract void scale(T entity, MatrixStack matrices, float amount);

    @Override
    public boolean catmodel$addFeature(FeatureRenderer<?, ?> feature) {
        return addFeature((FeatureRenderer<T, M>) feature);
    }

    @Override
    public void catmodel$renderAllFeatures(LivingEntity livingEntity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, Predicate<FeatureRenderer<?, ?>> filter) {
        float g = MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true);
        for(FeatureRenderer featureRenderer : features) {
            float h = MathHelper.lerpAngleDegrees(g, livingEntity.prevBodyYaw, livingEntity.bodyYaw);
            float j = MathHelper.lerpAngleDegrees(g, livingEntity.prevHeadYaw, livingEntity.headYaw);
            float k = j - h;
            if (livingEntity.hasVehicle()) {
                Entity var11 = livingEntity.getVehicle();
                if (var11 instanceof LivingEntity) {
                    LivingEntity livingEntity2 = (LivingEntity)var11;
                    h = MathHelper.lerpAngleDegrees(g, livingEntity2.prevBodyYaw, livingEntity2.bodyYaw);
                    k = j - h;
                    float l = MathHelper.wrapDegrees(k);
                    if (l < -85.0F) {
                        l = -85.0F;
                    }

                    if (l >= 85.0F) {
                        l = 85.0F;
                    }

                    h = j - l;
                    if (l * l > 2500.0F) {
                        h += l * 0.2F;
                    }

                    k = j - h;
                }
            }

            float m = MathHelper.lerp(g, livingEntity.prevPitch, livingEntity.getPitch());
            if (shouldFlipUpsideDown(livingEntity)) {
                m *= -1.0F;
                k *= -1.0F;
            }

            k = MathHelper.wrapDegrees(k);
            if (livingEntity.isInPose(EntityPose.SLEEPING)) {
                Direction direction = livingEntity.getSleepingDirection();
                if (direction != null) {
                    float n = livingEntity.getEyeHeight(EntityPose.STANDING) - 0.1F;
                    matrixStack.translate((float)(-direction.getOffsetX()) * n, 0.0F, (float)(-direction.getOffsetZ()) * n);
                }
            }

            float l = livingEntity.getScale();
            matrixStack.scale(l, l, l);
            float n = this.getAnimationProgress((T) livingEntity, g);
            this.setupTransforms((T) livingEntity, matrixStack, n, h, g, l);
            matrixStack.scale(-1.0F, -1.0F, 1.0F);
            this.scale((T) livingEntity, matrixStack, g);
            matrixStack.translate(0.0F, -1.501F, 0.0F);
            float o = 0.0F;
            float p = 0.0F;
            if (!livingEntity.hasVehicle() && livingEntity.isAlive()) {
                o = livingEntity.limbAnimator.getSpeed(g);
                p = livingEntity.limbAnimator.getPos(g);
                if (livingEntity.isBaby()) {
                    p *= 3.0F;
                }

                if (o > 1.0F) {
                    o = 1.0F;
                }
            }

            if(filter.test(featureRenderer)) featureRenderer.render(matrixStack, vertexConsumerProvider, i, livingEntity, p, o, g, n, k, m); // yawDegrees recheck 1
        }
    }
}
