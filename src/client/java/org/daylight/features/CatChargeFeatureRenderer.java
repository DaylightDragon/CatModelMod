package org.daylight.features;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.feature.EnergySwirlOverlayFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.CatEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.entity.state.CatEntityRenderState;
import net.minecraft.util.Identifier;
import org.daylight.config.Data;

@Environment(EnvType.CLIENT)
public class CatChargeFeatureRenderer
        extends EnergySwirlOverlayFeatureRenderer<CatEntityRenderState, CatEntityModel> {
    private final Identifier texture;
    private final CatEntityModel model;
    private float swirlProgress = 0f;

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
        return Data.shouldRenderCharge; // Only correct for local player
    }

    @Override
    protected float getEnergySwirlX(float partialAge) {
        swirlProgress += partialAge * 0.008f;
        return swirlProgress;
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

