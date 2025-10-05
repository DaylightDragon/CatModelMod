package org.daylight.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.state.EntityHitboxAndView;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.RotationAxis;
import org.daylight.CatModelModClient;
import org.daylight.CustomCatTextureHolder;
import org.daylight.IFeatureManager;
import org.daylight.InvisibilityBehaviour;
import org.daylight.config.ConfigHandler;
import org.daylight.config.Data;
import org.daylight.features.CatChargeFeatureRenderer;
import org.daylight.util.CatVariantUtils;
import org.daylight.util.PlayerToCatReplacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @SuppressWarnings("unchecked")
    @Inject(
            method = "render(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private <E extends Entity> void onRenderEntity(
            E entity,
            double x, double y, double z,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            CallbackInfo ci
    ) {
        if (ConfigHandler.replacementActive.getCached() && entity instanceof AbstractClientPlayerEntity player &&
                PlayerToCatReplacer.shouldReplace(player)) {
            CatEntity existingCat = (CatEntity) PlayerToCatReplacer.getCatForPlayer(player);

            if (existingCat != null) {
                PlayerToCatReplacer.syncEntity2(player, existingCat);

                matrices.push();
                matrices.translate(x, y, z);

                try {
                    EntityRenderer<CatEntity, EntityRenderState> catRenderer = this.getRenderer(existingCat);
                    if(catRenderer instanceof CustomCatTextureHolder customCatTextureHolder) {
                        if(customCatTextureHolder.catModel$shouldUpdateCustomTexture()) {
                            CatModelModClient.LOGGER.info("RenderDispatcher updates CatRenderer state");
                            catRenderer.getAndUpdateRenderState(existingCat, 0);
                        }
                    }
                    ci.cancel();

                    var catState = catRenderer.getAndUpdateRenderState(existingCat, tickDelta);
                    if(!(player.isInvisible() && ConfigHandler.invisibilityBehaviour != InvisibilityBehaviour.IGNORE)) {
                        catRenderer.render(catState, matrices, vertexConsumers, light);
                    } else {
                        if(catRenderer instanceof IFeatureManager featureManager) {
                            matrices.push();

                            // применяем такие же повороты, как в LivingEntityRenderer
                            float bodyYaw = ((LivingEntityRenderState) catState).bodyYaw; // или entity.getBodyYaw(tickDelta)
                            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - bodyYaw));
                            matrices.scale(-1.0F, -1.0F, 1.0F);
                            matrices.translate(0.0f, -1.501f, 0.0f);

                            featureManager.renderAllFeatures((LivingEntityRenderState) catState, matrices, vertexConsumers, light, featureRenderer -> featureRenderer instanceof CatChargeFeatureRenderer);

                            matrices.pop();
                        }
                    }

                    Data.shouldRenderCharge = player.isInvisible() && ConfigHandler.invisibilityBehaviour == InvisibilityBehaviour.CHARGED;

                    if (shouldRenderHitboxes()) {
                        var playerState = getRenderer(player).getAndUpdateRenderState(player, tickDelta);
                        if(playerState.hitbox != null) renderHitboxes(matrices, playerState, playerState.hitbox, vertexConsumers);
                    }
                } catch (ClassCastException e) {
                    CatModelModClient.LOGGER.error("The renderer is most likely not a EntityRenderer<CatEntity, EntityRenderState>", e);
                } finally {
                    matrices.pop();
                }
            }
        }
    }

    @Shadow
    public abstract <T extends Entity> EntityRenderer getRenderer(T entity);

    @Shadow
    public abstract boolean shouldRenderHitboxes();

    @Shadow
    protected abstract void renderHitboxes(MatrixStack matrices, EntityRenderState state, EntityHitboxAndView hitbox, VertexConsumerProvider vertexConsumers);
}