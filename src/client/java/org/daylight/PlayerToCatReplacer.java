package org.daylight;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.CatVariant;
import net.minecraft.entity.passive.CatVariants;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import org.daylight.mixin.client.CatEntityAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class PlayerToCatReplacer {
    public static final Logger LOGGER = LoggerFactory.getLogger(ModernAtt1Client.MOD_ID);
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final Map<AbstractClientPlayerEntity, Entity> dummyModelMap = new HashMap<>();
    private static World fakeWorld; // Локальный мир для entity

    // Инициализация фейкового мира
    public static void initFakeWorld() {
        try {
            if (fakeWorld != null) return; // TODO своё добавлено
            if (client == null || client.world == null) return;

            fakeWorld = client.world;
            if(true) return;

            // Получаем RegistryEntry для DimensionType
            RegistryEntry<DimensionType> dimensionEntry = client.world.getRegistryManager()
                    .getOrThrow(RegistryKeys.DIMENSION_TYPE)
                    .getEntry(DimensionTypes.OVERWORLD.getValue())
                    .orElseThrow(() -> new IllegalStateException("Overworld dimension not found"));

//            ClientPlayNetworkHandler networkHandler = client.getNetworkHandler();
//            if (networkHandler == null) {
//                networkHandler = new ClientPlayNetworkHandler(
//                        client,
//                        client.currentScreen,
//                        new ServerAddress("localhost", 25565), // Фиктивные значения
//                        client.getSession().getProfile()
//                );
//            }

            fakeWorld = new ClientWorld(
                    null,
                    new ClientWorld.Properties(
                            Difficulty.PEACEFUL, // client.world.getLevelProperties().getDifficulty(),
                            false, //client.world.getLevelProperties().isHardcore(),
                            false
                    ),
                    World.OVERWORLD,
                    dimensionEntry,
                    0, // loadDistance (не влияет на фейковый мир)
                    0, // simulationDistance (не влияет)
                    client.worldRenderer, // Используем основной рендер
                    false, // debugWorld
                    0L, // seed
                    62 // client.world.getSeaLevel()
            );
        } catch (Exception e) {
            LOGGER.error("Failed to create fake world: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    public static void replaceWithCat(AbstractClientPlayerEntity player) {
        if (dummyModelMap.containsKey(player)) return;

        // Создаём кота (в Fabric 1.21.5 используется CatEntity)
        CatEntity cat = new CatEntity(EntityType.CAT, fakeWorld);
        RegistryEntry<CatVariant> variant = getCatVariant(CatVariants.BLACK);
        ((CatEntityAccessor) cat).invokeSetVariant(variant); // Вариант текстуры
        cat.setTamed(true, false); // TODO
        cat.setOwner(player); // Опционально: привязка к игроку

        // Настройка "невидимости" для клиента
//        cat.setInvisible(true); // Сама модель видна, но не мешает взаимодействию
        cat.setNoGravity(true);
        cat.noClip = true;
        cat.setAiDisabled(true);
        cat.setPosition(player.getX(), player.getY(), player.getZ());

        dummyModelMap.put(player, cat);

        LOGGER.info("Created cat entity for player: {}", player.getName().getString());
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

    public static void syncEntity(AbstractClientPlayerEntity player, CatEntity cat) {
//        for (Map.Entry<AbstractClientPlayerEntity, Entity> entry : dummyModelMap.entrySet()) {
//        AbstractClientPlayerEntity player = entry.getKey();
//        Entity cat = entry.getValue();
//
//        cat.setBodyYaw(player.bodyYaw);
//        cat.setHeadYaw(player.getHeadYaw());

        // v1

//        cat.setPosition(player.getPos());
//        cat.setBodyYaw(player.getBodyYaw());
//        cat.setPitch(player.getPitch());

        // v1

        // v2

        if (cat == null || player == null) return;

        // Прямая синхронизация позиции и углов
        Vec3d playerPos = player.getPos();
        cat.setPosition(playerPos);

        // Копируем все углы напрямую
        cat.setYaw(player.getYaw());
        cat.setHeadYaw(player.getHeadYaw());
        cat.setBodyYaw(player.getBodyYaw());
        cat.setPitch(player.getPitch());

        // Предотвращаем любые авто-обновления
        cat.lastX = player.lastX;
        cat.lastY = player.lastY;
        cat.lastZ = player.lastZ;
        cat.lastYaw = player.lastYaw;
        cat.lastPitch = player.lastPitch;
        cat.lastBodyYaw = player.lastBodyYaw;
        cat.lastHeadYaw = player.lastHeadYaw;

        // v2

//        cat.setLastPositionAndAngles(
//                new Vec3d(
//                    player.getX(),
//                    player.getY(),
//                    player.getZ()
//                ),
//                player.getYaw(),
//                player.getPitch()
//        );
//
//        cat.updatePositionAndAngles(
//                player.getX(),
//                player.getY(),
//                player.getZ(),
//                player.getYaw(),
//                player.getPitch()
//        );

        // Анимации
//            cat.limbAnimator.setPos(player.limbAnimator.getPos());
//            cat.limbAnimator.setSpeed(player.limbAnimator.getSpeed());

        // Специфичные для кота
        if (cat instanceof CatEntity catEntity) {
//                catEntity.setHeadRolling(player.isUsingItem());
        }

//            cat.setPosition(player.getX(), player.getY(), player.getZ());
//            cat.setBodyYaw(player.getBodyYaw());
////            cat.setHeadYaw(player.getHeadYaw());
//            cat.setPitch(player.getPitch());
////            cat.setYaw(player.getYaw());
//
//
//            // Копируем позицию, поворот и анимации
////            cat.updatePositionAndAngles(
////                    player.getX(),
////                    player.getY(),
////                    player.getZ(),
////                    player.getYaw(),
////                    player.getPitch()
////            );
//
//            // Синхронизация анимаций
//            if (cat instanceof LivingEntity livingCat && player instanceof LivingEntity livingPlayer) {
////                livingCat.limbAnimator.setPos(livingPlayer.limbAnimator.getPos());
////                livingCat.limbAnimator.setSpeed(livingPlayer.limbAnimator.getSpeed());
//                livingCat.limbAnimator.updateLimbs(
//                        livingPlayer.limbAnimator.getSpeed(),
//                        0.5f, // Плавность изменения
//                        1.0f  // Скорость анимации
//                );
//
//                // Специально для кота - синхронизация поворота головы
//                if (cat instanceof CatEntity catEntity) {
////                    catEntity.setHeadYaw().HeadRolling(livingPlayer.isUsingItem());
////                    catEntity.setHeadYaw(player.getHeadYaw());
//                }
//            }
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

    public static Entity getCatForPlayer(AbstractClientPlayerEntity player) {
        return dummyModelMap.get(player);
    }

    public static boolean isDummyCat(CatEntity catEntity) {
        return dummyModelMap.containsValue(catEntity); // && player == MinecraftClient.getInstance().player;
    }
}
