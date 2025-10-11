package org.daylight.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerLikeEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.daylight.ModResources;
import org.daylight.config.ConfigHandler;
import org.daylight.config.Data;
import org.daylight.util.CatVariantUtils;
import org.daylight.util.PlayerToCatReplacer;
import org.daylight.util.StateStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin<AvatarlikeEntity extends PlayerLikeEntity & ClientPlayerLikeEntity> {
    @Inject(
            method = "updateRenderState(Lnet/minecraft/entity/PlayerLikeEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V",
            at = @At("HEAD")
    )
    public void updateRenderState(AvatarlikeEntity playerLikeEntity, PlayerEntityRenderState playerEntityRenderState, float f, CallbackInfo ci) {
        if(playerLikeEntity instanceof ClientPlayerEntity clientPlayerEntity) {
            StateStorage.currentStates.put(playerEntityRenderState, clientPlayerEntity);
        }
    }

    @ModifyArg(
            method = "renderRightArm",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;renderArm(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/util/Identifier;Lnet/minecraft/client/model/ModelPart;Z)V")
    )
    private Identifier replaceRightArmSkin(Identifier skinTexture) {
        if (ConfigHandler.catHandActive.getCached() && ConfigHandler.replacementActive.getCached() &&
                PlayerToCatReplacer.shouldReplace(getPlayer())) {
            return getHandTexture(skinTexture);
        }
        return skinTexture;
    }

    @ModifyArg(
            method = "renderLeftArm",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;renderArm(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/util/Identifier;Lnet/minecraft/client/model/ModelPart;Z)V")
    )
    private Identifier replaceLeftArmSkin(Identifier skinTexture) {
        if (ConfigHandler.catHandActive.getCached() && ConfigHandler.replacementActive.getCached()
                && PlayerToCatReplacer.shouldReplace(getPlayer())) {
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
