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
    public static boolean loaded = false;

    public static void init() {
        System.out.println(ModResources.CAT_HAND_TEXTURE.toString());
        InputStream stream = MinecraftClient.class.getResourceAsStream("/assets/catmodel/textures/entity/cat_hand0.png");
        System.out.println(stream != null ? "Found!" : "Not found");

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return Identifier.of("catmodel", "hand_reload_listener");
            }

            @Override
            public void reload(ResourceManager manager) {
//                manager.registerBuiltinPack(new Identifier("catmodel", "cat_hand_pack"), ModResources::createResourcePack);
                loaded = true;
                System.out.println("Resources loaded!");
            }
        });
    }

    public static void postInit() {
//        MinecraftClient.getInstance().getTextureManager().registerTexture(ModResources.CAT_HAND_TEXTURE, new ResourceTexture(ModResources.CAT_HAND_TEXTURE));
    }
}
