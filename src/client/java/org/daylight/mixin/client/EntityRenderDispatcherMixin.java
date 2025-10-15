package org.daylight.mixin.client;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.WorldView;
import org.daylight.*;
import org.daylight.config.ConfigHandler;
import org.daylight.features.CatChargeFeatureRenderer;
import org.daylight.util.PlayerToCatReplacer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @Shadow
    public abstract double getSquaredDistanceToCamera(double x, double y, double z);

    @Shadow
    protected static void renderShadow(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity, float opacity, float tickDelta, WorldView world, float radius) {
    }

    @Shadow
    protected static void renderHitbox(MatrixStack matrices, VertexConsumer vertices, Entity entity, float tickDelta, float red, float green, float blue) {
    }

    @Shadow
    private boolean renderShadows;
    @Shadow
    @Final
    public GameOptions gameOptions;

    @SuppressWarnings("unchecked")
    @Inject(
            method = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;render(Lnet/minecraft/entity/Entity;DDDFFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private <E extends Entity> void onRenderEntity(E entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
//        renderShadow(matrices, vertexConsumers, entity, );
        if (ConfigHandler.replacementActive.getCached() && entity instanceof AbstractClientPlayerEntity player &&
                PlayerToCatReplacer.shouldReplace(player)) {
            CatEntity existingCat = (CatEntity) PlayerToCatReplacer.getCatForPlayer(player);
            EntityRenderer<CatEntity> catRenderer = null;
            EntityRenderer<PlayerEntity> playerRenderer = null;
            boolean visible = !player.isInvisible();
            InvisibilityBehaviour behaviour = (InvisibilityBehaviour) ConfigHandler.invisibilityBehaviour.getCached();

            if (existingCat != null) {
                PlayerToCatReplacer.syncEntity2(player, existingCat, tickDelta);

                matrices.push();
                matrices.translate(x, y, z);

                try {
                    catRenderer = (EntityRenderer<CatEntity>) this.getRenderer(existingCat);
                    if(catRenderer instanceof CustomCatTextureHolder customCatTextureHolder) {
                        if(customCatTextureHolder.catModel$shouldUpdateCustomTexture()) {
                            CatifyModClient.LOGGER.info("RenderDispatcher updates CatRenderer state"); // TODO UPDATE SKIN
//                            catRenderer.getAndUpdateRenderState(existingCat, 0);
                        }
                    }
                    ci.cancel();

//                    catState = (CatEntityRenderState) catRenderer.getAndUpdateRenderState(existingCat, tickDelta);
//                    if(catState instanceof CustomCatState customCatState) customCatState.catmodel$setChargeActive(false);
                    CatChargeFeatureRenderer.getChargeData(existingCat).chargeActive = false;

//                    if(playerState == null) playerState = (PlayerEntityRenderState) getRenderer(player).getAndUpdateRenderState(player, tickDelta);
                    if(ConfigHandler.catDamageVisible.getCached()) {
                        existingCat.hurtTime = player.hurtTime;
                        existingCat.maxHurtTime = player.maxHurtTime; //catState.hurt = playerState.hurt; // TODO HURT
                    } else {
                        existingCat.hurtTime = 0;
                    }
//                    else catState.hurt = false;

                    if((visible || behaviour == InvisibilityBehaviour.NEVER)) {
                        // Just cat
                        catRenderer.render(existingCat, existingCat.bodyYaw, tickDelta, matrices, vertexConsumers, light);
                    }
                } catch (ClassCastException e) {
                    CatifyModClient.LOGGER.error("The renderer is most likely not a EntityRenderer<CatEntity, EntityRenderState>", e);
                } finally {
                    matrices.pop();
                }

                // Shadow, after the cat render

                if((visible || behaviour == InvisibilityBehaviour.NEVER || behaviour == InvisibilityBehaviour.CHARGED)) {
//                    if(catRenderer == null) catRenderer = (EntityRenderer<CatEntity, EntityRenderState>) this.getRenderer(existingCat);
                    if(playerRenderer == null) playerRenderer = this.getRenderer(player);
//                    if(catState == null) catState = (CatEntityRenderState) catRenderer.getAndUpdateRenderState(existingCat, tickDelta);
//                    playerState = (PlayerEntityRenderState) getRenderer(player).getAndUpdateRenderState(player, tickDelta);
                        try {
//                        Vec3d vec3d = playerRenderer.getPositionOffset(playerState);

                        matrices.push();
                        matrices.translate(x, y, z);

                        if (playerRenderer instanceof IShadowHolder shadowHolder) {
                            if ((Boolean)this.gameOptions.getEntityShadows().getValue() && this.renderShadows) {
                                float g = shadowHolder.getShadowRadiusAccessor(player);
                                if (g > 0.0F) {
                                    double h = getSquaredDistanceToCamera(entity.getX(), entity.getY(), entity.getZ());
                                    float i = (float)(((double)1.0F - h / (double)256.0F) * (double) shadowHolder.catModel$getShadowOpacityAccessor()); // playerRenderer.getShadowOpacity(playerState) replaced with 1f in 1.21.3
                                    if (i > 0.0F) {
                                        renderShadow(matrices, vertexConsumers, player, i, tickDelta, player.getWorld(), Math.min(g, 32.0F));
                                    }
                                }
                            }
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    } finally {
                        matrices.pop();
                    }
                }

//                CatEntityRenderer
//                PlayerEntityRenderer

                // Charge

                if(!visible && behaviour == InvisibilityBehaviour.CHARGED) {
                    if(catRenderer instanceof IFeatureManager featureManager) {
                        matrices.push();

                        try {
                            float bodyYaw = existingCat.bodyYaw; // or entity.getBodyYaw(tickDelta)
                            matrices.translate(x, y, z); // inherited from normal cat render
                            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - bodyYaw));
                            matrices.scale(-1.0F, -1.0F, 1.0F);
                            matrices.translate(0.0f, -1f, 0.0f); // -1.501f

                            CatChargeFeatureRenderer.getChargeData(existingCat).chargeActive = true;

                            featureManager.catmodel$renderAllFeatures(existingCat, matrices, vertexConsumers, light, featureRenderer -> featureRenderer instanceof CatChargeFeatureRenderer);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        } finally {
                            matrices.pop();
                        }
                    }
                }

                // Hitboxes

                if (shouldRenderHitboxes()) {
                    if(vertexConsumers.getBuffer(RenderLayer.LINES) != null) {
                        try {
                            matrices.push();
                            matrices.translate(x, y, z);
                            renderHitbox(matrices, vertexConsumers.getBuffer(RenderLayer.LINES), player, tickDelta, 1.0f, 1.0f, 1.0f);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        } finally {
                            matrices.pop();
                        }
                    }
                }
            }
        }
    }

    @Shadow
    public abstract <T extends Entity> EntityRenderer getRenderer(T entity);

    @Shadow
    public abstract boolean shouldRenderHitboxes();
}