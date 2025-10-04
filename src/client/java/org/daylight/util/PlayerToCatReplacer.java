package org.daylight.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.CatVariant;
import net.minecraft.entity.passive.CatVariants;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.daylight.CatModelModClient;
import org.daylight.config.ConfigHandler;
import org.daylight.mixin.client.CatEntityAccessor;
import org.daylight.mixin.client.LimbAnimatorAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class PlayerToCatReplacer {
    public static final Logger LOGGER = LoggerFactory.getLogger(CatModelModClient.MOD_ID);
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final Map<AbstractClientPlayerEntity, LivingEntity> dummyModelMap = new HashMap<>();
    private static World targetWorld;

    public static void init() {
        if (client == null || client.world == null) return;
        targetWorld = client.world;

//        CatVariants.ALL_BLACK.getValue().getPath()
    }

    public static void replaceWithCat(AbstractClientPlayerEntity player) {
        if (dummyModelMap.containsKey(player)) return;

        CatEntity cat = new CatEntity(EntityType.CAT, targetWorld);
        changeCatVariant(cat, CatVariantUtils.deserializeVariant(ConfigHandler.catVariant.get()));
        cat.setTamed(true, false);
//        cat.setOwner(player);

        cat.setNoGravity(true);
        cat.noClip = true;
        cat.setAiDisabled(true);
        cat.setPosition(player.getX(), player.getY(), player.getZ());

        dummyModelMap.put(player, cat);
    }

    private static RegistryEntry<CatVariant> getCatVariant(RegistryKey<CatVariant> variantKey) {
        if(client.world == null) {
            LOGGER.warn("Client world is null");
            return null;
        }

        return client.world.getRegistryManager()
                .getOrThrow(RegistryKeys.CAT_VARIANT)
                .getEntry(variantKey.getValue())
                .orElseThrow(() -> new IllegalStateException("Cat variant not found: " + variantKey));
    }

    public static void changeCatVariant(CatEntity cat, RegistryKey<CatVariant> variantKey) {
        RegistryEntry<CatVariant> variant = getCatVariant(variantKey);
        ((CatEntityAccessor) cat).invokeSetVariant(variant);
    }

    private static float lerpAngle(float current, float target, float factor) {
        float delta = MathHelper.wrapDegrees(target - current);
        return current + delta * factor;
    }

    public static void cleanup() {
        dummyModelMap.values().forEach(Entity::discard);
        dummyModelMap.clear();
    }

    public static boolean shouldReplace(AbstractClientPlayerEntity player) {
        return dummyModelMap.containsKey(player) &&
                player == MinecraftClient.getInstance().player;
    }

    public static LivingEntity getCatForPlayer(AbstractClientPlayerEntity player) {
        return dummyModelMap.get(player);
    }

    public static boolean isDummyCat(CatEntity catEntity) {
        return dummyModelMap.containsValue(catEntity); // && player == MinecraftClient.getInstance().player;
    }

    public static void syncEntity2(AbstractClientPlayerEntity player, CatEntity existingCat) {
        existingCat.lastX = player.lastX;
        existingCat.lastY = player.lastY;
        existingCat.lastZ = player.lastZ;
        existingCat.setPos(player.getX(), player.getY(), player.getZ());

        existingCat.lastYaw = player.lastYaw;
        existingCat.setYaw(player.getYaw());

        existingCat.lastPitch = player.lastPitch;
        existingCat.setPitch(player.getPitch());

        existingCat.lastBodyYaw = player.lastBodyYaw;
        existingCat.bodyYaw = player.bodyYaw;

        existingCat.lastHeadYaw = player.lastHeadYaw;
        existingCat.headYaw = player.headYaw;

        // Sitting
        double dx = player.getX() - player.lastX;
        double dz = player.getZ() - player.lastZ;
        double horizontalSpeed = Math.sqrt(dx * dx + dz * dz);

        boolean sneaking = player.isSneaking(); // or isInSneakingPose()
        boolean slowEnough = horizontalSpeed < 0.01;

        boolean sitting = sneaking && slowEnough;
        existingCat.setInSittingPose(sitting);

//        if (sitting) {
//            // сбросить движение лап
//            existingCat.limbAnimator.updateLimbs(0.0f, 1.0f, 1.0f);
//
//            if (existingCat.limbAnimator instanceof LimbAnimatorAccessor acc) {
//                acc.setAnimationProgress(0.0f);
//            }
//        } else {

//        System.out.println(getPlayerMovementSpeed(player) + " " + player.limbAnimator.getAnimationProgress());

        existingCat.limbAnimator.updateLimbs(
                getPlayerMovementSpeed(player),
                0.9f,
                1.0f
        );

        if (existingCat.limbAnimator instanceof LimbAnimatorAccessor acc) {
            acc.setAnimationProgress(player.limbAnimator.getAnimationProgress());
        }
//        }
    }

    private static float getPlayerMovementSpeed(AbstractClientPlayerEntity player) {
        // Вычисляем скорость движения игрока
        double dx = player.getX() - player.lastX;
        double dz = player.getZ() - player.lastZ;
        double horizontalSpeed = Math.sqrt(dx * dx + dz * dz);

        // Нормализуем скорость для анимаций
        float speed = (float) horizontalSpeed * 4f; // 20.0f;

//        System.out.println(horizontalSpeed);

        // Ограничиваем максимальную скорость
        return Math.min(speed, 1.0f);
    }

    public static void setLocalCatVariant(RegistryKey<CatVariant> variant) {
        LivingEntity catLivingEntity = getCatForPlayer(client.player);
        if(catLivingEntity instanceof CatEntity catEntity) {
            changeCatVariant(catEntity, variant);
        }
    }
}
