package net.blazinblaze.render.renderer;

import net.blazinblaze.BlazinChristmasMod;
import net.blazinblaze.entity.custom.Reindeer;
import net.blazinblaze.render.model.ReindeerModel;
import net.blazinblaze.render.state.ReindeerState;
import net.minecraft.client.model.LlamaModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LlamaDecorLayer;
import net.minecraft.client.renderer.entity.state.LlamaRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Llama;

public class ReindeerRenderer extends AgeableMobRenderer<Reindeer, ReindeerState, ReindeerModel> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "textures/entity/reindeer.png");
    private static final ResourceLocation TEXTURE_RED = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "textures/entity/reindeer_red.png");

    public ReindeerRenderer(EntityRendererProvider.Context context) {
        super(context, new ReindeerModel(context.bakeLayer(BCMModelLayers.REINDEER)), new ReindeerModel(context.bakeLayer(BCMModelLayers.REINDEER_BABY)), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(ReindeerState livingEntityRenderState) {
        if(livingEntityRenderState.isSpecialRed) {
            return TEXTURE_RED;
        }else {
            return TEXTURE;
        }
    }

    public ReindeerState createRenderState() {
        return new ReindeerState();
    }

    public void extractRenderState(Reindeer reindeer, ReindeerState reindeerState, float f) {
        super.extractRenderState(reindeer, reindeerState, f);
        reindeerState.variant = reindeer.getVariant();
        reindeerState.hasChest = !reindeer.isBaby() && reindeer.hasChest();
        reindeerState.bodyItem = reindeer.getBodyArmorItem();
        reindeerState.isTraderLlama = reindeer.isTraderLlama();
        reindeerState.isSpecialRed = reindeer.getSpecialRed();
    }
}
