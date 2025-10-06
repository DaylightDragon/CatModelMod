package org.daylight.features;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.EnergySwirlOverlayFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.CatEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.entity.state.CatEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.daylight.CustomCatState;
import org.daylight.config.Data;
import org.daylight.util.PlayerToCatReplacer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Environment(EnvType.CLIENT)
public class CatChargeFeatureRenderer
        extends EnergySwirlOverlayFeatureRenderer<CatEntityRenderState, CatEntityModel> {
    private final Identifier texture;
    private final CatEntityModel model;

    public static class CatChargeData {
        public boolean chargeActive = false;
        public float chargeProgress = 0f;
        public Float customDelta = null;
    }

    public static final Map<UUID, CatChargeData> CHARGE_DATA = new HashMap<>();

    public static CatChargeData getChargeData(Entity entity) {
        return CHARGE_DATA.computeIfAbsent(entity.getUuid(), k -> new CatChargeData());
    }

    public CatChargeFeatureRenderer(
            FeatureRendererContext<CatEntityRenderState, CatEntityModel> context,
            LoadedEntityModels loader,
            Identifier texture
    ) {
        super(context);
        this.texture = texture;
        this.model = new CatEntityModel(loader.getModelPart(EntityModelLayers.CAT));
    }

    @Override
    protected boolean shouldRender(CatEntityRenderState state) {
        if(state instanceof CustomCatState customCatState) {
            UUID entityId = customCatState.catmodel$getCurrentEntityId();
            if (entityId == null) return false;
            Entity entity = PlayerToCatReplacer.findAsCat(entityId);
            if (entity == null) return false;

            CatChargeData data = getChargeData(entity);
            return data.chargeActive;
        }
        return false;
    }

    private CatEntityRenderState currentState = null;

    private static float mainGlobalCatProgress = 0;
    @Override
    protected float getEnergySwirlX(float partialAge) {
        if(currentState instanceof CustomCatState customCatState) {
            UUID entityId = customCatState.catmodel$getCurrentEntityId();
            if (entityId == null) return 0f;
            Entity entity = PlayerToCatReplacer.findAsCat(entityId);
            if (entity == null) return 0f;

            CatChargeData data = getChargeData(entity);
            if (data.customDelta != null) {
                data.chargeProgress += data.customDelta * 0.008f;
            } else {
                data.chargeProgress += partialAge * 0.008f;
            }
            return data.chargeProgress;
        } return 0;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CatEntityRenderState state, float limbAngle, float limbDistance) {
        this.currentState = state; // temporary

        if (state instanceof CustomCatState custom) {
            float progress = (state.age + limbAngle) * 0.01f;
            custom.catmodel$setChargeProgress(progress);
        }

        super.render(matrices, vertexConsumers, light, state, limbAngle, limbDistance);
        this.currentState = null;
    }

    @Override
    protected Identifier getEnergySwirlTexture() {
        return texture;
    }

    @Override
    protected CatEntityModel getEnergySwirlModel() {
        return model;
    }

    // something "synthetic" in sources too
}

