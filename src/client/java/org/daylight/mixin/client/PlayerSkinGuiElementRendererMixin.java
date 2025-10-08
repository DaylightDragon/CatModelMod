package org.daylight.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.render.PlayerSkinGuiElementRenderer;
import net.minecraft.client.gui.render.state.special.PlayerSkinGuiElementRenderState;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.CatEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.CatEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
import org.daylight.IElementWVertexConsumerProvider;
import org.daylight.config.ConfigHandler;
import org.daylight.util.PlayerToCatReplacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerSkinGuiElementRenderer.class)
public abstract class PlayerSkinGuiElementRendererMixin {
    @Inject(
            method = "Lnet/minecraft/client/gui/render/PlayerSkinGuiElementRenderer;render(Lnet/minecraft/client/gui/render/state/special/PlayerSkinGuiElementRenderState;Lnet/minecraft/client/util/math/MatrixStack;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRender(PlayerSkinGuiElementRenderState state, MatrixStack matrices, CallbackInfo ci) {
        if(MinecraftClient.getInstance().player == null) return;

        if (ConfigHandler.replacementActive.getCached()) {
            if (state.playerModel() instanceof PlayerEntityModel && (Object) this instanceof IElementWVertexConsumerProvider elementWVertexes) {
                MinecraftClient mc = MinecraftClient.getInstance();
                EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
                CatEntity cat = (CatEntity) PlayerToCatReplacer.getCatForPlayer(mc.player);
                if(cat == null) return;

                mc.gameRenderer.getDiffuseLighting().setShaderLights(DiffuseLighting.Type.PLAYER_SKIN);

                LivingEntityRenderer<CatEntity, CatEntityRenderState, CatEntityModel> renderer = (LivingEntityRenderer<CatEntity, CatEntityRenderState, CatEntityModel>) dispatcher.getRenderer(cat);
                CatEntityRenderState renderState = renderer.createRenderState();
                renderer.updateRenderState(cat, renderState, mc.getRenderTickCounter().getTickProgress(true));

//                matrices.push();
//                matrices.translate(0.0F, -1.6F, 0.0F);
//                matrices.scale(30.0f, 30.0f, 30.0f);

                ci.cancel();
                renderer.render(renderState, matrices, elementWVertexes.getVertexConsumers(), LightmapTextureManager.MAX_LIGHT_COORDINATE);
//                matrices.pop();

//                CatEntityModel catModel = renderer.getModel();
            }
        }
    }

//    private void onRender(PlayerSkinGuiElementRenderState state, MatrixStack matrixStack, CallbackInfo ci) {
//        if(MinecraftClient.getInstance().player == null) return;
//
//        // Подмена модели перед тем, как она используется
//        if (ConfigHandler.replacementActive.getCached()) {
//            // Например, заменить playerModel на свой кастомный
//            if (state.playerModel() instanceof PlayerEntityModel) {
//                MinecraftClient mc = MinecraftClient.getInstance();
//                EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
//
//                CatEntity cat = (CatEntity) PlayerToCatReplacer.getCatForPlayer(mc.player);
//                LivingEntityRenderer<CatEntity, CatEntityRenderState, CatEntityModel> renderer = (LivingEntityRenderer<CatEntity, CatEntityRenderState, CatEntityModel>) dispatcher.getRenderer(cat);
//                // Здесь можно подменить поля, позы или текстуру
//                // либо вообще подменить объект модели
//
//
//                CatEntityModel catModel = renderer.getModel();
////                state
////                        .catify$setPlayerModel(catModel);
//            }
//        }
//    }
}
