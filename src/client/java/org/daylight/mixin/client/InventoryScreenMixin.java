package org.daylight.mixin.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.daylight.util.StateStorage;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {
    @ModifyVariable(
            method = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;drawEntity(Lnet/minecraft/client/gui/DrawContext;IIIIFLorg/joml/Vector3f;Lorg/joml/Quaternionf;Lorg/joml/Quaternionf;Lnet/minecraft/entity/LivingEntity;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;addEntity(Lnet/minecraft/client/render/entity/state/EntityRenderState;FLorg/joml/Vector3f;Lorg/joml/Quaternionf;Lorg/joml/Quaternionf;IIII)V",
                    shift = At.Shift.AFTER
            ),
            ordinal = 0
    )
    private static EntityRenderState captureState(EntityRenderState state, DrawContext drawer,
                                                  int x1, int y1, int x2, int y2,
                                                  float scale, Vector3f translation,
                                                  Quaternionf rotation,
                                                  @Nullable Quaternionf overrideCameraAngle,
                                                  LivingEntity entity
    ) {
        if(state instanceof PlayerEntityRenderState) {
//            System.out.println("Adding from InventoryScreen " + state);
            StateStorage.currentStates.put(state, entity.getUuid());
        }
        return state;
    }
}
