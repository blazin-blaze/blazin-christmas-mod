package net.blazinblaze.render.renderer;

import net.blazinblaze.BlazinChristmasMod;
import net.blazinblaze.entity.custom.SantaVillager;
import net.blazinblaze.render.model.SantaVillagerModel;
import net.blazinblaze.render.state.SantaVillagerState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
import net.minecraft.resources.ResourceLocation;

public class SantaVillagerRenderer extends MobRenderer<SantaVillager, SantaVillagerState, SantaVillagerModel> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "textures/entity/santa_villager.png");

    public SantaVillagerRenderer(EntityRendererProvider.Context context) {
        super(context, new SantaVillagerModel(context.bakeLayer(BCMModelLayers.SANTA_VILLAGER)), 0.5F);
        this.addLayer(new CrossedArmsItemLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(SantaVillagerState state) {
        return TEXTURE;
    }

    @Override
    public SantaVillagerState createRenderState() {
        return new SantaVillagerState();
    }

    @Override
    public void extractRenderState(SantaVillager livingEntity, SantaVillagerState livingEntityRenderState, float f) {
        super.extractRenderState(livingEntity, livingEntityRenderState, f);
        HoldingEntityRenderState.extractHoldingEntityRenderState(livingEntity, livingEntityRenderState, this.itemModelResolver);
    }
}
