package org.daylight;

import net.minecraft.entity.passive.CatVariant;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.Map;

public class ModResources {
    public static final Identifier ALL_BLACK_HAND_TEXTURE   = Identifier.of(CatifyMod.MOD_ID, "textures/entity/all_black_hand.png");
    public static final Identifier BLACK_HAND_TEXTURE       = Identifier.of(CatifyMod.MOD_ID, "textures/entity/black_hand.png");
    public static final Identifier BRITISH_SHORTHAIR_HAND_TEXTURE = Identifier.of(CatifyMod.MOD_ID, "textures/entity/british_shorthair_hand.png");
    public static final Identifier CALICO_HAND_TEXTURE      = Identifier.of(CatifyMod.MOD_ID, "textures/entity/calico_hand.png");
    public static final Identifier JELLIE_HAND_TEXTURE      = Identifier.of(CatifyMod.MOD_ID, "textures/entity/jellie_hand.png");
    public static final Identifier PERSIAN_HAND_TEXTURE     = Identifier.of(CatifyMod.MOD_ID, "textures/entity/persian_hand.png");
    public static final Identifier RAGDOLL_HAND_TEXTURE     = Identifier.of(CatifyMod.MOD_ID, "textures/entity/ragdoll_hand.png");
    public static final Identifier RED_HAND_TEXTURE         = Identifier.of(CatifyMod.MOD_ID, "textures/entity/red_hand.png");
    public static final Identifier SIAMESE_HAND_TEXTURE     = Identifier.of(CatifyMod.MOD_ID, "textures/entity/siamese_hand.png");
    public static final Identifier TABBY_HAND_TEXTURE       = Identifier.of(CatifyMod.MOD_ID, "textures/entity/tabby_hand.png");
    public static final Identifier WHITE_HAND_TEXTURE       = Identifier.of(CatifyMod.MOD_ID, "textures/entity/white_hand.png");

    public static final Map<RegistryKey<CatVariant>, Identifier> CAT_HAND_BY_VARIANT = Map.ofEntries(
            Map.entry(CatVariant.ALL_BLACK, ALL_BLACK_HAND_TEXTURE),
            Map.entry(CatVariant.BLACK, BLACK_HAND_TEXTURE),
            Map.entry(CatVariant.BRITISH_SHORTHAIR, BRITISH_SHORTHAIR_HAND_TEXTURE),
            Map.entry(CatVariant.CALICO, CALICO_HAND_TEXTURE),
            Map.entry(CatVariant.JELLIE, JELLIE_HAND_TEXTURE),
            Map.entry(CatVariant.PERSIAN, PERSIAN_HAND_TEXTURE),
            Map.entry(CatVariant.RAGDOLL, RAGDOLL_HAND_TEXTURE),
            Map.entry(CatVariant.RED, RED_HAND_TEXTURE),
            Map.entry(CatVariant.SIAMESE, SIAMESE_HAND_TEXTURE),
            Map.entry(CatVariant.TABBY, TABBY_HAND_TEXTURE),
            Map.entry(CatVariant.WHITE, WHITE_HAND_TEXTURE)
    );
    public static final Identifier GHOST_TEXTURE = Identifier.of(CatifyMod.MOD_ID, "textures/entity/cat_charge.png");;

    public static void init() {
    }

    public static void postInit() {
//        MinecraftClient.getInstance().getTextureManager().registerTexture(ModResources.CAT_HAND_TEXTURE, new ResourceTexture(ModResources.CAT_HAND_TEXTURE));
    }
}
