package org.daylight.mixin.client;

import net.minecraft.client.gui.render.state.special.EntityGuiElementRenderState;
import org.daylight.IModifiableGuiElement;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityGuiElementRenderState.class)
public class EntityGuiElementRenderStateMixin implements IModifiableGuiElement {
    @Mutable
    @Final
    @Shadow
    private Quaternionf rotation;

    public void setRotation(Quaternionf rotation) {
        this.rotation = rotation;
    }
}
