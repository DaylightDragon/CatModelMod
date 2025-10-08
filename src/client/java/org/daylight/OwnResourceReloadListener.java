package org.daylight;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.daylight.util.CatSkinManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

public class OwnResourceReloadListener implements SimpleSynchronousResourceReloadListener {
    @Override
    public Identifier getFabricId() {
        return Identifier.of(CatModelModClient.MOD_ID, "example_cat_texture_extractor");
    }

    @Override
    public void reload(ResourceManager manager) {
        try {
            extractCatTextures(manager);
            extractHandTextures(manager);
        } catch (Throwable t) {
            CatModelModClient.LOGGER.error("Failed to reload texture extractor, report this to the mod developer", t);
        }
    }

    private void extractCatTextures(ResourceManager manager) {
        Path outputDir = createOutputDir("example_cats");

        for (String name : CatSkinManager.STATIC_VARIANTS) {
            Identifier id = Identifier.ofVanilla("textures/entity/cat/" + name + ".png");
            copyResource(manager, id, outputDir, name);
        }
    }

    private void extractHandTextures(ResourceManager manager) {
        Path outputDir = createOutputDir("example_hands");

        for (Map.Entry<?, Identifier> entry : ModResources.CAT_HAND_BY_VARIANT.entrySet()) {
            Identifier textureId = entry.getValue();
            String name = extractBaseName(textureId);
            copyResource(manager, textureId, outputDir, name);
        }
    }

    private Path createOutputDir(String folderName) {
        Path outputDir = MinecraftClient.getInstance().runDirectory.toPath()
                .resolve("data/cat_model_custom/example_skins/" + folderName);
        try {
            Files.createDirectories(outputDir);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create output folder: " + outputDir, e);
        }
        return outputDir;
    }

    private void copyResource(ResourceManager manager, Identifier resourceId, Path outputDir, String fileName) {
        try {
            Resource resource = manager.getResource(resourceId).orElse(null);
            if (resource == null) {
                CatModelModClient.LOGGER.error("Not found: {}", resourceId);
                return;
            }

            Path outFile = outputDir.resolve(fileName + ".png");
            try (InputStream input = resource.getInputStream()) {
                Files.copy(input, outFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            CatModelModClient.LOGGER.error("Error extracting {}: {} ", resourceId, e);
        }
    }

    private String extractBaseName(Identifier id) {
        String path = id.getPath(); // textures/entity/tabby_hand.png
        int lastSlash = path.lastIndexOf('/');
        String file = (lastSlash >= 0 ? path.substring(lastSlash + 1) : path);
        return file.replace("_hand.png", "");
    }

    public static void register() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
                .registerReloadListener(new OwnResourceReloadListener());
    }
}
