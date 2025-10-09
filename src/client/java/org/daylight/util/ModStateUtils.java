package org.daylight.util;

import net.minecraft.entity.player.PlayerEntity;
import org.daylight.InvisibilityBehaviour;
import org.daylight.config.ConfigHandler;

public class ModStateUtils {
    public static boolean shouldRenderCat(PlayerEntity player) {
        boolean visible = !player.isInvisible();
        InvisibilityBehaviour behaviour = (InvisibilityBehaviour) ConfigHandler.invisibilityBehaviour.getCached();
        return visible || behaviour == InvisibilityBehaviour.NEVER;
    }

    public static boolean shouldRenderShadow(PlayerEntity player) {
        boolean visible = !player.isInvisible();
        InvisibilityBehaviour behaviour = (InvisibilityBehaviour) ConfigHandler.invisibilityBehaviour.getCached();
        return visible || behaviour == InvisibilityBehaviour.NEVER || behaviour == InvisibilityBehaviour.CHARGED;
    }

    public static boolean shouldRenderCharge(PlayerEntity player) {
        boolean visible = !player.isInvisible();
        InvisibilityBehaviour behaviour = (InvisibilityBehaviour) ConfigHandler.invisibilityBehaviour.getCached();
        return !visible && behaviour == InvisibilityBehaviour.CHARGED;
    }
}
