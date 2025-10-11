package org.daylight.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.state.WorldRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.tick.TickManager;
import org.daylight.features.CatChargeFeatureRenderer;
import org.daylight.util.StateStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(
            method = "pushEntityRenders",
            at = @At("HEAD")
    )
    private void pushEntityRenders(CallbackInfo ci) {
        if(!MinecraftClient.getInstance().isPaused()) {
            CatChargeFeatureRenderer.moveGlobalTextureForward(MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(true));
        }
    }

    @Inject(
            method = "pushEntityRenders",
            at = @At("TAIL")
    )
    private void pushEntityRenders(MatrixStack matrices, WorldRenderState renderStates, OrderedRenderCommandQueue queue, CallbackInfo ci) {
        for (EntityRenderState entityRenderState : renderStates.entityRenderStates) {
            if(entityRenderState instanceof PlayerEntityRenderState) {
//                System.out.println("Removing from WorldRenderer: " + entityRenderState);
                StateStorage.currentStates.remove(entityRenderState);
            }
        }
    }

//    @Inject(
//            method = "fillEntityRenderStates",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/client/render/WorldRenderer;getAndUpdateRenderState(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/client/render/entity/state/EntityRenderState;",
//                    shift = At.Shift.AFTER
//            ),
//            locals = LocalCapture.CAPTURE_FAILSOFT
//    )
//    private void captureState(
//            Camera camera, Frustum frustum, RenderTickCounter tickCounter, WorldRenderState renderStates,
//            CallbackInfo ci,
//            Vec3d vec3d,
//            double d, double e, double f,
//            TickManager tickManager,
//            boolean bl,
//            Entity entity,
//            BlockPos blockPos,
//            float g,
//            EntityRenderState entityRenderState) {
//        System.out.println("Added from World: " + entityRenderState);
//        StateStorage.currentStates.put(entityRenderState, entity.getUuid());
//    }

//    @ModifyVariable(
//            method = "fillEntityRenderStates",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/client/render/WorldRenderer;getAndUpdateRenderState(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/client/render/entity/state/EntityRenderState;",
//                    shift = At.Shift.AFTER
//            ),
//            ordinal = 0
//    )
//    private EntityRenderState onCaptureState(EntityRenderState state, Entity entity, CallbackInfo ci) {
//        if (state != null) {
//            StateStorage.currentStates.put(state, entity.getUuid());
//            System.out.println("Captured EntityRenderState: " + state + " ← " + entity.getName().getString());
//        }
//        return state;
//    }
}
