package org.daylight.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
import org.daylight.PlayerToCatReplacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
	/*@Inject(
			method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
			at = @At("HEAD"),
			cancellable = true
	)
	private void onRender(LivingEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		LivingEntity entity = state.getEntity();

		if (entity instanceof AbstractClientPlayerEntity player &&
				PlayerToCatReplacer.shouldReplace(player)) {

			CatEntity cat = (CatEntity) PlayerToCatReplacer.getCatForPlayer(player);
			if (cat != null) {
				// Создаем новое состояние рендеринга для кота
				LivingEntityRenderState catState = new LivingEntityRenderState();
				copyRenderState(state, catState, cat);

				// Получаем рендерер для кота
				EntityRenderer catRenderer = MinecraftClient.getInstance()
						.getEntityRenderDispatcher()
						.getRenderer(cat);

				if (catRenderer instanceof LivingEntityRenderer) {
					((LivingEntityRenderer) catRenderer).render(catState, matrices, vertexConsumers, light);
					ci.cancel();
				}
			}
		}
	}

	private void copyRenderState(LivingEntityRenderState source, LivingEntityRenderState target, LivingEntity newEntity) {
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

		// Устанавливаем новую сущность
		((LivingEntityRenderStateAccessor) target).setEntity(newEntity);
	}*/
}
