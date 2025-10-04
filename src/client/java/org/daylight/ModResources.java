package org.daylight;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.io.InputStream;

public class ModResources {
    public static final Identifier CAT_HAND_TEXTURE = Identifier.of(CatModelMod.MOD_ID, "textures/entity/cat_hand0.png");

    public static void init() {
    }

    public static void postInit() {
//        MinecraftClient.getInstance().getTextureManager().registerTexture(ModResources.CAT_HAND_TEXTURE, new ResourceTexture(ModResources.CAT_HAND_TEXTURE));
    }
}
