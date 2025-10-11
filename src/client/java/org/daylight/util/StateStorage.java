package org.daylight.util;

import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class StateStorage {
    public static Map<EntityRenderState, PlayerEntity> currentStates = new HashMap<>();
}
