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

import java.util.Optional;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {
    @ModifyArg(
            method = "renderRightArm",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;renderArm(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/util/Identifier;Lnet/minecraft/client/model/ModelPart;Z)V")
    )
    private Identifier replaceRightArmSkin(Identifier skinTexture) {
        System.out.println(MinecraftClient.getInstance().getTextureManager().getTexture(ModResources.CAT_HAND_TEXTURE));
        if (PlayerToCatReplacer.shouldReplace(getPlayer())) {
            Optional<Resource> resource = MinecraftClient.getInstance().getResourceManager().getResource(ModResources.CAT_HAND_TEXTURE);
            if(resource.isEmpty()) {
                MinecraftClient.getInstance().getTextureManager().registerTexture(ModResources.CAT_HAND_TEXTURE, new ResourceTexture(ModResources.CAT_HAND_TEXTURE));
            }
            return ModResources.CAT_HAND_TEXTURE; // твой кастомный текстурный идентификатор
        }
        return skinTexture;
    }
//
//    @ModifyArg(
//            method = "renderLeftArm",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;renderArm(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/util/Identifier;Lnet/minecraft/client/model/ModelPart;Z)V")
//    )
//    private Identifier replaceLeftArmSkin(Identifier skinTexture) {
//        if (PlayerToCatReplacer.shouldReplace(getPlayer())) {
//            return ModResources.CAT_HAND_TEXTURE;
//        }
//        return skinTexture;
//    }

//    @ModifyArg(
//            method = "renderArm",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/client/render/VertexConsumerProvider;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/render/VertexConsumer;"
//            ),
//            index = 0
//    )
//    private RenderLayer replaceRenderLayer(RenderLayer layer) {
//        assert MinecraftClient.getInstance().player != null;
//        System.out.println("Texture exists: " + ModResources.CAT_HAND_TEXTURE);
//        MinecraftClient.getInstance().execute(() -> {
//            System.out.println("Resource loaded: " + MinecraftClient.getInstance().getResourceManager()
//                    .getAllResources(ModResources.CAT_HAND_TEXTURE));
//        });
//
//        if (PlayerToCatReplacer.shouldReplace(MinecraftClient.getInstance().player)) {
//            System.out.println("replaceRenderLayer is called");
//            return RenderLayer.getEntityCutoutNoCull(ModResources.CAT_HAND_TEXTURE);
//        }
//        return layer;
//    }



    @Unique
    private AbstractClientPlayerEntity getPlayer() {
        return MinecraftClient.getInstance().player;
    }
}
