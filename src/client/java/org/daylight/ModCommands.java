package org.daylight;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.passive.CatVariant;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import org.daylight.config.ConfigHandler;
import org.daylight.util.CatVariantUtils;
import org.daylight.util.PlayerToCatReplacer;

import java.util.List;
import java.util.Locale;

public class ModCommands {
    private static final List<String> VARIANTS = List.of(
            "tabby", "black", "red", "siamese",
            "british_shorthair", "calico", "persian",
            "ragdoll", "white", "jellie", "all_black"
    );

    private static final List<String> MODES_OF_OFF = List.of(
            "on", "off"
    );

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("catvariant")
                    .then(ClientCommandManager.argument("variant", StringArgumentType.word())
                            .suggests((context, builder) -> {
                                for (String v : VARIANTS) {
                                    if (v.startsWith(builder.getRemaining().toUpperCase(Locale.ROOT))) {
                                        builder.suggest(v);
                                    }
                                }
                                return builder.buildFuture();
                            })
                            .executes(context -> {
                                MinecraftClient client = MinecraftClient.getInstance();
                                if (client.player == null) return 0;

                                String variantName = StringArgumentType.getString(context, "variant");

                                try {
                                    RegistryKey<CatVariant> variant = CatVariantUtils.deserializeVariant(variantName);
                                    String name = CatVariantUtils.serializeVariant(variant);
//                                    PlayerCatData.setCatVariant(client.player.getUuid(), variant);

                                    ConfigHandler.catVariant.set(name);
                                    PlayerToCatReplacer.setLocalCatVariant(variant);
                                    ConfigHandler.catVariant.save();

                                    client.player.sendMessage(
                                            Text.literal("§6Set variant: §l§f" + name.toLowerCase(Locale.ROOT)),
                                            false
                                    );
                                    return 1;
                                } catch (Exception e) {
                                    client.player.sendMessage(
                                            Text.literal("§cUnknown variant: §l§f" + ConfigHandler.catVariant.get()),
                                            false
                                    );
                                    return 0;
                                }
                            })
                    )
            );

            dispatcher.register(ClientCommandManager.literal("catmode")
                .executes(context -> performSetCatActive(!ConfigHandler.replacementActive.get()))
                .then(ClientCommandManager.argument("state", StringArgumentType.word())
                    .suggests((context, builder) -> {
                        for (String v : MODES_OF_OFF) {
                            if (v.startsWith(builder.getRemaining().toUpperCase(Locale.ROOT))) {
                                builder.suggest(v);
                            }
                        }
                        return builder.buildFuture();
                    })
                    .executes(context -> {
                        MinecraftClient mc = MinecraftClient.getInstance();
                        String stateStr = StringArgumentType.getString(context, "state");

                        boolean state;
                        if(stateStr.equals("on")) {
                            state = true;
                        } else if(stateStr.equals("off")) {
                            state = false;
                        } else {
                            if(mc != null && mc.player != null) mc.player.sendMessage(
                                Text.literal("§cUnexpected value: §l§f" + stateStr),
                                false
                            );
                            return 0;
                        }

                        return performSetCatActive(state);
                    })
                )
            );

            dispatcher.register(ClientCommandManager.literal("cathand")
                    .executes(context -> performSetCatHandActive(!ConfigHandler.replacementActive.get()))
                    .then(ClientCommandManager.argument("state", StringArgumentType.word())
                            .suggests((context, builder) -> {
                                for (String v : MODES_OF_OFF) {
                                    if (v.startsWith(builder.getRemaining().toUpperCase(Locale.ROOT))) {
                                        builder.suggest(v);
                                    }
                                }
                                return builder.buildFuture();
                            })
                            .executes(context -> {
                                MinecraftClient mc = MinecraftClient.getInstance();
                                String stateStr = StringArgumentType.getString(context, "state");

                                boolean state;
                                if(stateStr.equals("on")) {
                                    state = true;
                                } else if(stateStr.equals("off")) {
                                    state = false;
                                } else {
                                    if(mc != null && mc.player != null) mc.player.sendMessage(
                                            Text.literal("§cUnexpected value: §l§f" + stateStr),
                                            false
                                    );
                                    return 0;
                                }

                                return performSetCatHandActive(state);
                            })
                    )
            );
        });
    }

    private static int performSetCatActive(boolean state) {
        MinecraftClient mc = MinecraftClient.getInstance();

        ConfigHandler.replacementActive.set(state);
        ConfigHandler.replacementActive.save();

        if(mc != null && mc.player != null) mc.player.sendMessage(
                Text.literal("§6Set cat mode to: §l§f" +
                        (ConfigHandler.replacementActive.get() ? "Active" : "Disabled")),
                false
        );
        return 1;
    }

    private static int performSetCatHandActive(boolean state) {
        MinecraftClient mc = MinecraftClient.getInstance();

        ConfigHandler.catHandActive.set(state);
        ConfigHandler.catHandActive.save();

        if(mc != null && mc.player != null) mc.player.sendMessage(
                Text.literal("§6Now the cat hand mode is: §l§f" +
                                (ConfigHandler.catHandActive.get() ? "Active" : "Disabled")),
                false
        );
        return 1;
    }
}
