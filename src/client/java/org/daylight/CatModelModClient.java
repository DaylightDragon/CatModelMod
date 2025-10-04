package org.daylight;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.client.MinecraftClient;
import org.daylight.config.ConfigHandler;
import org.daylight.util.PlayerToCatReplacer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

@Environment(EnvType.CLIENT)
public class CatModelModClient implements ClientModInitializer {
	public static final String MOD_ID = "modernatt1";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final MinecraftClient client = MinecraftClient.getInstance();
    AtomicBoolean checked = new AtomicBoolean(false);

	@Override
	public void onInitializeClient() {
        ModCommands.register();
        ConfigHandler.init();

		ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register((handler, world) -> {
			if (world != null) {
				PlayerToCatReplacer.initWorld();
                checked.set(false);
			}
		});

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (!checked.get() && client.player != null) {
                PlayerToCatReplacer.replaceWithCat(client.player);
                checked.set(true);
            }
        });

		//ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
//		ServerWorldEvents.LOAD.register((server, world) -> {
//			LOGGER.info("World loaded, initializing fake world");
//			if (world != null) {
//				PlayerToCatReplacer.initFakeWorld();
//				if (client.player != null) {
//					PlayerToCatReplacer.replaceWithCat(client.player);
//				}
//			}
//		});

		ServerWorldEvents.UNLOAD.register((server, world) -> {
			PlayerToCatReplacer.cleanup();
		});

		// Синхронизация каждый тик
//		ClientTickEvents.START_CLIENT_TICK.register(client -> {
//			if (client.player != null) {
//				PlayerToCatReplacer.syncEntities();
//			}
//		});

	}
}
