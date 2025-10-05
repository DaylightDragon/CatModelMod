package org.daylight;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.entity.passive.CatVariant;
import net.minecraft.entity.passive.CatVariants;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.util.Map;

public class ModResources {
    public static final Identifier ALL_BLACK_HAND_TEXTURE   = Identifier.of(CatModelMod.MOD_ID, "textures/entity/all_black_hand.png");
    public static final Identifier BLACK_HAND_TEXTURE       = Identifier.of(CatModelMod.MOD_ID, "textures/entity/black_hand.png");
    public static final Identifier BRITISH_SHORTHAIR_HAND_TEXTURE = Identifier.of(CatModelMod.MOD_ID, "textures/entity/british_shorthair_hand.png");
    public static final Identifier CALICO_HAND_TEXTURE      = Identifier.of(CatModelMod.MOD_ID, "textures/entity/calico_hand.png");
    public static final Identifier JELLIE_HAND_TEXTURE      = Identifier.of(CatModelMod.MOD_ID, "textures/entity/jellie_hand.png");
    public static final Identifier PERSIAN_HAND_TEXTURE     = Identifier.of(CatModelMod.MOD_ID, "textures/entity/persian_hand.png");
    public static final Identifier RAGDOLL_HAND_TEXTURE     = Identifier.of(CatModelMod.MOD_ID, "textures/entity/ragdoll_hand.png");
    public static final Identifier RED_HAND_TEXTURE         = Identifier.of(CatModelMod.MOD_ID, "textures/entity/red_hand.png");
    public static final Identifier SIAMESE_HAND_TEXTURE     = Identifier.of(CatModelMod.MOD_ID, "textures/entity/siamese_hand.png");
    public static final Identifier TABBY_HAND_TEXTURE       = Identifier.of(CatModelMod.MOD_ID, "textures/entity/tabby_hand.png");
    public static final Identifier WHITE_HAND_TEXTURE       = Identifier.of(CatModelMod.MOD_ID, "textures/entity/white_hand.png");

    public static final Map<RegistryKey<CatVariant>, Identifier> CAT_HAND_BY_VARIANT = Map.ofEntries(
            Map.entry(CatVariants.ALL_BLACK, ALL_BLACK_HAND_TEXTURE),
            Map.entry(CatVariants.BLACK, BLACK_HAND_TEXTURE),
            Map.entry(CatVariants.BRITISH_SHORTHAIR, BRITISH_SHORTHAIR_HAND_TEXTURE),
            Map.entry(CatVariants.CALICO, CALICO_HAND_TEXTURE),
            Map.entry(CatVariants.JELLIE, JELLIE_HAND_TEXTURE),
            Map.entry(CatVariants.PERSIAN, PERSIAN_HAND_TEXTURE),
            Map.entry(CatVariants.RAGDOLL, RAGDOLL_HAND_TEXTURE),
            Map.entry(CatVariants.RED, RED_HAND_TEXTURE),
            Map.entry(CatVariants.SIAMESE, SIAMESE_HAND_TEXTURE),
            Map.entry(CatVariants.TABBY, TABBY_HAND_TEXTURE),
            Map.entry(CatVariants.WHITE, WHITE_HAND_TEXTURE)
    );

    public static void init() {
    }

    public static void postInit() {
//        MinecraftClient.getInstance().getTextureManager().registerTexture(ModResources.CAT_HAND_TEXTURE, new ResourceTexture(ModResources.CAT_HAND_TEXTURE));
    }
}
