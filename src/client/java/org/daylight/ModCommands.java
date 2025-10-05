package org.daylight;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.passive.CatVariant;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import org.daylight.config.ConfigHandler;
import org.daylight.config.Data;
import org.daylight.util.CatSkinManager;
import org.daylight.util.CatVariantUtils;
import org.daylight.util.PlayerToCatReplacer;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class ModCommands {


    private static final List<String> MODES_OF_OFF = List.of(
            "on", "off"
    );

    private static final List<String> INVISIBILITY_MODES = List.of(
            "never", "vanilla", "charged"
    );

    private static CompletableFuture<Suggestions> suggestVariants(SuggestionsBuilder builder, Collection<String> variants) {
        String input = builder.getRemaining().toLowerCase(Locale.ROOT);

        for (String v : variants) {
            if (v.toLowerCase(Locale.ROOT).startsWith(input)) {
                builder.suggest(v);
            }
        }

        return builder.buildFuture();
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("catvariant")
                    .then(ClientCommandManager.argument("variant", StringArgumentType.word())
                            .suggests((context, builder) -> suggestVariants(builder, CatSkinManager.ALL_VARIANTS))
                            .executes(context -> {
                                MinecraftClient client = MinecraftClient.getInstance();
                                if (client.player == null) return 0;

                                String variantName = StringArgumentType.getString(context, "variant");

                                try {
                                    if(CatSkinManager.isVanillaVariant(variantName)) {
                                        RegistryKey<CatVariant> variant = CatVariantUtils.deserializeVariant(variantName);
                                        String name = CatVariantUtils.serializeVariant(variant);
//                                      PlayerCatData.setCatVariant(client.player.getUuid(), variant);

                                        ConfigHandler.catVariant.set(name);
                                        ConfigHandler.catVariantVanilla.set(true);
                                        PlayerToCatReplacer.setLocalCatVariant(variant);
                                        ConfigHandler.CONFIG.save();

                                        client.player.sendMessage(
                                                Text.literal("§6Set variant: §l§f" + name.toLowerCase(Locale.ROOT)),
                                                false
                                        );
                                        return 1;
                                    } else if(CatSkinManager.getActualCustomFileName(variantName) != null) {
                                        MinecraftClient mc = MinecraftClient.getInstance();
                                        String finalVariantName = CatSkinManager.getActualCustomFileName(variantName);

                                        ConfigHandler.catVariant.set(finalVariantName);
                                        ConfigHandler.catVariantVanilla.set(false);
                                        ConfigHandler.CONFIG.save();

                                        if(PlayerToCatReplacer.setCustomCatEntityTexture(MinecraftClient.getInstance().player, finalVariantName)) {
                                            if(mc.player != null) mc.player.sendMessage(
                                                    Text.literal("§6Set variant: §l§f" + variantName),
                                                    false
                                            );
                                            if(!PlayerToCatReplacer.setCustomCatHandTexture(finalVariantName) && ConfigHandler.catHandActive.get()) {
                                                if(mc.player != null) mc.player.sendMessage(
                                                        Text.literal("§eWarning: Couldn't find a texture for custom hand §l§f" + variantName),
                                                        false
                                                );
                                            }
                                            return 1;
                                        } else {
                                            if(mc.player != null) mc.player.sendMessage(
                                                    Text.literal("§cCouldn't find §l§f" + variantName + ".png §r§c in §l§f/data/cat_model_custom/cat_enitity_skins"),
                                                    false
                                            );
                                            return 0;
                                        }
                                    } else {
                                        client.player.sendMessage(
                                                Text.literal("§cUnknown variant: §l§f" + ConfigHandler.catVariant.get()),
                                                false
                                        );
                                        return 0;
                                    }
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
                    .suggests((context, builder) -> suggestVariants(builder, MODES_OF_OFF))
                    .executes(context -> {
                        MinecraftClient mc = MinecraftClient.getInstance();
                        String stateStr = StringArgumentType.getString(context, "state");

                        boolean state;
                        if(stateStr.equalsIgnoreCase("on")) {
                            state = true;
                        } else if(stateStr.equalsIgnoreCase("off")) {
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
                            .suggests((context, builder) -> suggestVariants(builder, MODES_OF_OFF))
                            .executes(context -> {
                                MinecraftClient mc = MinecraftClient.getInstance();
                                String stateStr = StringArgumentType.getString(context, "state");

                                boolean state;
                                if(stateStr.equalsIgnoreCase("on")) {
                                    state = true;
                                    if(Data.catHandTexture == null && !ConfigHandler.catVariantVanilla.get()) {
                                        if(mc.player != null) mc.player.sendMessage(
                                                Text.literal("§eWarning: Couldn't find a texture for the custom hand"),
                                                false
                                        );
                                    }
                                } else if(stateStr.equalsIgnoreCase("off")) {
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

            dispatcher.register(ClientCommandManager.literal("catinvisibility")
                    .then(ClientCommandManager.argument("mode", StringArgumentType.word())
                            .suggests((context, builder) -> suggestVariants(builder, INVISIBILITY_MODES))
                            .executes(context -> {
                                MinecraftClient mc = MinecraftClient.getInstance();
                                String stateStr = StringArgumentType.getString(context, "mode");

                                InvisibilityBehaviour behaviour;
                                if(stateStr.equalsIgnoreCase("never")) {
                                    behaviour = InvisibilityBehaviour.NEVER;
                                } else if(stateStr.equals("vanilla")) {
                                    behaviour = InvisibilityBehaviour.USUAL;
                                } else if(stateStr.equals("charged")) {
                                    behaviour = InvisibilityBehaviour.CHARGED;
                                } else {
                                    if(mc != null && mc.player != null) mc.player.sendMessage(
                                            Text.literal("§cUnexpected value: §l§f" + stateStr),
                                            false
                                    );
                                    return 0;
                                }

                                return performSetInvisibilityBehaviour(behaviour);
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

    private static int performSetInvisibilityBehaviour(InvisibilityBehaviour behaviour) {
        MinecraftClient mc = MinecraftClient.getInstance();

        ConfigHandler.invisibilityBehaviour.set(behaviour);
        ConfigHandler.CONFIG.save();

        if(mc != null && mc.player != null) mc.player.sendMessage(
                Text.literal("§6Set invisibility mode to: §l§f" +
                        ConfigHandler.invisibilityBehaviour.get().toString().toLowerCase(Locale.ROOT)),
                false
        );
        return 1;
    }
}
