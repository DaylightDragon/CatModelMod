package org.daylight.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class GraphicsUtils {
    public static boolean doesTextureExist(Identifier id) {
//        System.out.println(id);
        if(id == null) return false;
        TextureManager resourceManager = MinecraftClient.getInstance().getTextureManager();
        try {
//            System.out.println(resourceManager.getTexture(id));
            return resourceManager.getTexture(id) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
