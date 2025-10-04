package org.daylight.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.CatVariants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
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
            double x, double y, double z,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            CallbackInfo ci
    ) {
        if (entity instanceof AbstractClientPlayerEntity player &&
                PlayerToCatReplacer.shouldReplace(player)) {

            CatEntity existingCat = (CatEntity) PlayerToCatReplacer.getCatForPlayer(player);

            if (existingCat != null) {

                PlayerToCatReplacer.syncEntity2(player, existingCat);

                matrices.push();
                matrices.translate(x, y, z);

                ci.cancel();

                EntityRenderer<CatEntity, EntityRenderState> catRenderer = this.getRenderer(existingCat);
                var catState = catRenderer.getAndUpdateRenderState(existingCat, tickDelta);
                catRenderer.render(catState, matrices, vertexConsumers, light);

                matrices.pop();
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
    
    private void copyValues(CatEntity existingCat, PlayerEntity player) {
        existingCat.setPos(player.getX(), player.getY(), player.getZ());

        existingCat.lastYaw = player.lastYaw;
        existingCat.setYaw(player.getYaw());

        existingCat.lastPitch = player.lastPitch;
        existingCat.setPitch(player.getPitch());

        existingCat.bodyYaw = player.bodyYaw;
        existingCat.lastBodyYaw = player.lastBodyYaw;

        existingCat.headYaw = player.headYaw;
        existingCat.lastHeadYaw = player.lastHeadYaw;
    }

    private CatEntity createRenderCat(AbstractClientPlayerEntity player, float tickDelta) {
        // Создаем кота без добавления в мир
        CatEntity cat = new CatEntity(EntityType.CAT, MinecraftClient.getInstance().world);

        // Базовая настройка
        setupRenderCat(cat);

        // Синхронизация с игроком
        syncRenderCat(cat, player, tickDelta);

        return cat;
    }

    private void setupRenderCat(CatEntity cat) {
        // Настройка внешности (вариант, имя и т.д.)
        try {
            var registry = MinecraftClient.getInstance().world.getRegistryManager().getOrThrow(RegistryKeys.CAT_VARIANT);
            var variant = registry.getEntry(CatVariants.ALL_BLACK.getRegistry());
            if (variant.isPresent() && cat instanceof CatEntityAccessor catEntityAccessor) {
                catEntityAccessor.invokeSetVariant(variant.get());
            }
        } catch (Exception e) {
            // Игнорируем ошибки
        }

//        cat.setTamed(true, false);
    }

    private void syncRenderCat(CatEntity cat, AbstractClientPlayerEntity player, float tickDelta) {
        // Только визуальные параметры - позиция не нужна, так как используем translate
        cat.setYaw(player.getYaw());
        cat.setPitch(player.getPitch());
        cat.setHeadYaw(player.getHeadYaw());
        cat.setBodyYaw(player.getBodyYaw());

        // Анимации
        double dx = player.getX() - player.lastX;
        double dz = player.getZ() - player.lastZ;
        double horizontalSpeed = Math.sqrt(dx * dx + dz * dz);
        float targetSpeed = Math.min((float)horizontalSpeed * 20.0f, 1.0f);
        cat.limbAnimator.updateLimbs(targetSpeed, 0.5f, 1.0f);
    }
}