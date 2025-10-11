package org.daylight.mixin.client;

import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.gui.render.state.special.EntityGuiElementRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.daylight.util.StateStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiRenderer.class)
public class GuiRendererMixin {
    @Shadow
    @Final
    private GuiRenderState state;

    @Inject(
            method = "prepareSpecialElements",
            at = @At("TAIL")
    )
    private void prepareSpecialElements(CallbackInfo ci) {
        this.state.forEachSpecialElement(state -> {
            if(state instanceof EntityGuiElementRenderState entityGuiElementRenderState) {
                if(entityGuiElementRenderState.renderState() instanceof PlayerEntityRenderState) {
//                    System.out.println("Removing from GuiRenderer " + entityGuiElementRenderState.renderState());
                    StateStorage.currentStates.remove(entityGuiElementRenderState.renderState());
                }
            }
        });
    }
}
