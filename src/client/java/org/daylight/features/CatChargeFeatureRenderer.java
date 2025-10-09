package org.daylight.features;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
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

    public static float globalProgress = 0;
    public static void moveGlobalTextureForward(float tickDelta) {
        globalProgress += tickDelta * 0.008f;
        if(globalProgress >= 8000f) {
            globalProgress = 0f;
        }
    }

    @Override
    public float getEnergySwirlX(float partialAge) {
        if(true) return globalProgress;
//        if(true) return (partialAge * 0.01F) % 1.0F;
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

    public void customRender(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CatEntityRenderState state, float limbAngle, float limbDistance) {
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(
                RenderLayer.getEnergySwirl(this.getEnergySwirlTexture(), this.getEnergySwirlX(globalProgress) % 1.0F, globalProgress * 0.01F % 1.0F)
        );
        customRender(matrices, vertexConsumer, light, state, limbAngle, limbDistance);
    }

    public void customRender(MatrixStack matrices, VertexConsumer vertexConsumers, int light, CatEntityRenderState state, float limbAngle, float limbDistance) {
//        this.currentState = state; // temporary

//        if (state instanceof CustomCatState custom) {
//            float progress = (state.age + limbAngle) * 0.01f;
//            custom.catmodel$setChargeProgress(progress);
//        }

        internalRender(matrices, vertexConsumers, light, state, limbAngle, limbDistance);
//        this.currentState = null;
    }

    private void internalRender(MatrixStack matrices, VertexConsumer vertexConsumer, int light, CatEntityRenderState state, float limbAngle, float limbDistance) {
        if (this.shouldRender(state)) {
            CatEntityModel entityModel = this.getEnergySwirlModel();
            entityModel.setAngles(state);
            entityModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, -8355712);
        }
    }

    @Override
    public Identifier getEnergySwirlTexture() {
        return texture;
    }

    @Override
    protected CatEntityModel getEnergySwirlModel() {
        return model;
    }

    // something "synthetic" in sources too
}

