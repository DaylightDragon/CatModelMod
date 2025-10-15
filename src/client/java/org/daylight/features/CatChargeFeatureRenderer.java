package org.daylight.features;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.EnergySwirlOverlayFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.CatEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SkinOverlayOwner;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.daylight.CustomCatState;
import org.daylight.config.Data;
import org.daylight.util.PlayerToCatReplacer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Environment(EnvType.CLIENT)
public class CatChargeFeatureRenderer<T extends Entity & SkinOverlayOwner, M extends EntityModel<T>>
        extends EnergySwirlOverlayFeatureRenderer {
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
            FeatureRendererContext<T, M> context,
//            LoadedEntityModels loader,
            Identifier texture
    ) {
        super(context);
        this.texture = texture;
        this.model = new CatEntityModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(EntityModelLayers.CAT));
    }

//    private CatEntityRenderState currentState = null;

    public static float globalProgress = 0;
    public static void moveGlobalTextureForward(float tickDelta) {
        globalProgress += tickDelta * 0.008f;
        if(globalProgress >= 8000f) {
            globalProgress = 0f;
        }
    }

    @Override
    protected float getEnergySwirlX(float partialAge) {
        return globalProgress;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
//        super.render(matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
        if(!(entity instanceof CatEntity cat)) return;
        try {
            matrices.push();

//            float bodyYaw = cat.bodyYaw;
//            matrices.translate(x, y, z); // inherited from normal cat render
//            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - bodyYaw));
//            matrices.scale(-1.0F, -1.0F, 1.0F);
//            matrices.translate(0.0f, -1.501f, 0.0f);

            if (((SkinOverlayOwner) entity).shouldRenderOverlay()) {
                float f = (float) entity.age + tickDelta;
                EntityModel<T> entityModel = this.getEnergySwirlModel();
                entityModel.animateModel((T) entity, limbAngle, limbDistance, tickDelta);
                this.getContextModel().copyStateTo(entityModel);
                entityModel.child = false;
                VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEnergySwirl(this.getEnergySwirlTexture(), this.getEnergySwirlX(f) % 1.0F, f * 0.01F % 1.0F));
                entityModel.setAngles((T) entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
                entityModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, -8355712);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            matrices.pop();
        }
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

