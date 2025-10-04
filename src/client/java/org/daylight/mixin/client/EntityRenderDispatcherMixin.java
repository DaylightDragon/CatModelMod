package org.daylight.mixin.client;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.math.Vec3d;
import org.daylight.ModernAtt1Client;
import org.daylight.PlayerToCatReplacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @Inject(
            method = "render(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private <E extends Entity> void onRenderEntity(
            E entity,
            double x,
            double y,
            double z,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            CallbackInfo ci
    ) {
        if (entity instanceof AbstractClientPlayerEntity player &&
                PlayerToCatReplacer.shouldReplace(player)) {
            ci.cancel();

            CatEntity cat = (CatEntity) PlayerToCatReplacer.getCatForPlayer(player);

            if (cat != null) {
                ci.cancel(); // Отменяем рендер игрока

                // Сохраняем текущую матрицу
                matrices.push();

                try {
                    // Сбрасываем трансформацию игрока
                    matrices.loadIdentity();

                    // Используем РЕАЛЬНУЮ позицию кота, а не игрока
                    Vec3d catPos = cat.getPos();
                    double catX = catPos.x;
                    double catY = catPos.y;
                    double catZ = catPos.z;

                    // Получаем рендерер для кота
                    EntityRenderer<?, ?> catRenderer = this.getRenderer(cat);

                    // Рендерим кота в его реальной позиции
                    this.render(
                            cat,
                            x, y, z,
                            tickDelta,
                            matrices,
                            vertexConsumers,
                            light,
                            catRenderer
                    );
                } finally {
                    matrices.pop();
                }
            }
        }
    }

    @Shadow
    public abstract <T extends Entity> EntityRenderer getRenderer(T entity);

    @Shadow
    private <E extends Entity> void render(
            E entity,
            double x, double y, double z,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            EntityRenderer renderer
    ) {}

    private static LivingEntityRenderState createRenderState(LivingEntity entity) {
        LivingEntityRenderState state = new LivingEntityRenderState();

        // Заполняем основные параметры
        state.bodyYaw = entity.bodyYaw;
        state.relativeHeadYaw = entity.getHeadYaw();
        state.pitch = entity.getPitch();
        state.limbSwingAnimationProgress = entity.limbAnimator.getAnimationProgress();
        state.limbSwingAmplitude = entity.limbAnimator.getSpeed();

        // Специфичные параметры
        state.hurt = entity.hurtTime > 0;
        state.pose = entity.getPose();

        return state;
    }

    private void copyRenderState(LivingEntityRenderState source, LivingEntityRenderState target) {
        // Копируем все видимые поля
        target.bodyYaw = source.bodyYaw;
        target.relativeHeadYaw = source.relativeHeadYaw;
        target.pitch = source.pitch;
        target.deathTime = source.deathTime;
        target.limbSwingAnimationProgress = source.limbSwingAnimationProgress;
        target.limbSwingAmplitude = source.limbSwingAmplitude;
        target.baseScale = source.baseScale;
        target.ageScale = source.ageScale;
        target.flipUpsideDown = source.flipUpsideDown;
        target.shaking = source.shaking;
        target.baby = source.baby;
        target.touchingWater = source.touchingWater;
        target.usingRiptide = source.usingRiptide;
        target.hurt = source.hurt;
        target.invisibleToPlayer = source.invisibleToPlayer;
        target.hasOutline = source.hasOutline;
        target.sleepingDirection = source.sleepingDirection;
        target.customName = source.customName;
        target.pose = source.pose;
        target.headItemAnimationProgress = source.headItemAnimationProgress;
        target.wearingSkullType = source.wearingSkullType;
        target.wearingSkullProfile = source.wearingSkullProfile;

//        ((LivingEntityRenderStateAccessor) target).setEntity(newEntity);
    }
}