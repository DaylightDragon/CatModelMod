package org.daylight.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.render.EntityGuiElementRenderer;
import net.minecraft.client.gui.render.state.special.EntityGuiElementRenderState;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.CatEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.CatEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CatEntity;
import org.daylight.IElementWVertexConsumerProvider;
import org.daylight.IModifiableGuiElement;
import org.daylight.config.ConfigHandler;
import org.daylight.util.PlayerToCatReplacer;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityGuiElementRenderer.class)
public class EntityGuiElementRendererMixin { // NEW
    @Inject(
            method = "Lnet/minecraft/client/gui/render/EntityGuiElementRenderer;render(Lnet/minecraft/client/gui/render/state/special/EntityGuiElementRenderState;Lnet/minecraft/client/util/math/MatrixStack;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void render(EntityGuiElementRenderState state, MatrixStack matrices, CallbackInfo ci) {
        if(MinecraftClient.getInstance().player == null) return;

//        System.out.println(state.renderState().getClass().getSimpleNa me());
//        System.out.println(state instanceof IModifiableGuiElement);

        if (ConfigHandler.replacementActive.getCached() && state.renderState().entityType == EntityType.PLAYER) {
            if ((Object) this instanceof IElementWVertexConsumerProvider elementWVertexes
                    && (Object) state instanceof IModifiableGuiElement modifiableGuiElement
                    && (Object) state.renderState() instanceof PlayerEntityRenderState playerEntityRenderState) {
                MinecraftClient mc = MinecraftClient.getInstance();
                EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
                CatEntity cat = (CatEntity) PlayerToCatReplacer.getCatForPlayer(mc.player);
                if(cat == null) return;

                mc.gameRenderer.getDiffuseLighting().setShaderLights(DiffuseLighting.Type.PLAYER_SKIN);

                LivingEntityRenderer<CatEntity, CatEntityRenderState, CatEntityModel> renderer = (LivingEntityRenderer<CatEntity, CatEntityRenderState, CatEntityModel>) dispatcher.getRenderer(cat);
                CatEntityRenderState renderState = renderer.createRenderState();
                renderer.updateRenderState(cat, renderState, 0); // mc.getRenderTickCounter().getTickProgress(true)

                renderState.relativeHeadYaw = playerEntityRenderState.relativeHeadYaw;
                renderState.bodyYaw = playerEntityRenderState.bodyYaw;
                renderState.pitch = playerEntityRenderState.pitch;

                Vector3f translation = state.translation();
                matrices.push();
                matrices.translate(translation.x, translation.y, translation.z);
//                matrices.scale(1.0F, 1.0F, -1.0F);
                matrices.multiply(state.rotation());

                Quaternionf overrideAngle = state.overrideCameraAngle();
                if (overrideAngle != null) {
                    dispatcher.setRotation(overrideAngle.conjugate(new Quaternionf()).rotateY((float) Math.PI));
                }

//                    modifiableGuiElement.setRotation(quaternionf.conjugate(new Quaternionf()).rotateY((float) Math.PI));

                ci.cancel();
                dispatcher.setRenderShadows(false);
                renderer.render(renderState, matrices, elementWVertexes.getVertexConsumers(), LightmapTextureManager.MAX_LIGHT_COORDINATE);
                dispatcher.setRenderShadows(true);

                matrices.pop(); // TODO to finally

//                CatEntityModel catModel = renderer.getModel();
            }
        }
    }
}
