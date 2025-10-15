package org.daylight.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.daylight.ModResources;
import org.daylight.config.ConfigHandler;
import org.daylight.config.Data;
import org.daylight.util.CatVariantUtils;
import org.daylight.util.PlayerToCatReplacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {
    @ModifyVariable(
            method = "renderArm(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;)V",
            at = @At("STORE"),
            ordinal = 0
    )
    private Identifier replaceArmSkin(Identifier skinTexture) {
        if (ConfigHandler.catHandActive.getCached() && ConfigHandler.replacementActive.getCached() &&
                PlayerToCatReplacer.shouldReplace(getPlayer())) {
//            Optional<Resource> resource = MinecraftClient.getInstance().getResourceManager().getResource(ModResources.CAT_HAND_TEXTURE);
//            if(resource.isEmpty()) {
//                MinecraftClient.getInstance().getTextureManager().registerTexture(ModResources.CAT_HAND_TEXTURE, new ResourceTexture(ModResources.CAT_HAND_TEXTURE));
//            }
//            return ModResources.CAT_HAND_TEXTURE; // твой кастомный текстурный идентификатор
            return getHandTexture(skinTexture);
        }
        return skinTexture;
    }

    @Unique
    private Identifier getHandTexture(Identifier defaultValue) {
        if(!ConfigHandler.catVariantVanilla.getCached()) {
            if(Data.catHandTexture != null) return Data.catHandTexture;
            else return defaultValue;
        }
        return ModResources.CAT_HAND_BY_VARIANT.get(CatVariantUtils.deserializeVariant(ConfigHandler.catVariant.getCached()));
    }

    @Unique
    private AbstractClientPlayerEntity getPlayer() {
        return MinecraftClient.getInstance().player;
    }
}
