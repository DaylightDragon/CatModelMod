package org.daylight.util;

import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StateStorage {
    public static Map<EntityRenderState, UUID> currentStates = new HashMap<>(); // state to player uuid
    public static Float inventoryRelativeHeadYaw = null;
    public static Float inventoryBodyYaw = null;
    public static Float inventoryPitch = null;
}
