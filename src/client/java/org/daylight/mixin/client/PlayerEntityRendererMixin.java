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
import org.daylight.util.PlayerToCatReplacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {
    @ModifyArg(
            method = "renderRightArm",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;renderArm(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/util/Identifier;Lnet/minecraft/client/model/ModelPart;Z)V")
    )
    private Identifier replaceRightArmSkin(Identifier skinTexture) {
        if (PlayerToCatReplacer.shouldReplace(getPlayer())) {
//            Optional<Resource> resource = MinecraftClient.getInstance().getResourceManager().getResource(ModResources.CAT_HAND_TEXTURE);
//            if(resource.isEmpty()) {
//                MinecraftClient.getInstance().getTextureManager().registerTexture(ModResources.CAT_HAND_TEXTURE, new ResourceTexture(ModResources.CAT_HAND_TEXTURE));
//            }
            return ModResources.CAT_HAND_TEXTURE; // твой кастомный текстурный идентификатор
        }
        return skinTexture;
    }

    @ModifyArg(
            method = "renderLeftArm",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;renderArm(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/util/Identifier;Lnet/minecraft/client/model/ModelPart;Z)V")
    )
    private Identifier replaceLeftArmSkin(Identifier skinTexture) {
        if (PlayerToCatReplacer.shouldReplace(getPlayer())) {
            return ModResources.CAT_HAND_TEXTURE;
        }
        return skinTexture;
    }

    @Unique
    private AbstractClientPlayerEntity getPlayer() {
        return MinecraftClient.getInstance().player;
    }
}
