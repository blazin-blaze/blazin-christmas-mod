package net.blazinblaze.render.renderer;

import net.blazinblaze.BlazinChristmasMod;
import net.blazinblaze.entity.custom.GingerbreadMan;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.ResourceLocation;

public class GingerbreadManRenderer extends AbstractZombieRenderer<GingerbreadMan, ZombieRenderState, ZombieModel<ZombieRenderState>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "textures/entity/gingerbread_man.png");

    public GingerbreadManRenderer(EntityRendererProvider.Context context) {
        this(context, ModelLayers.ZOMBIE, ModelLayers.ZOMBIE_BABY, ModelLayers.ZOMBIE_INNER_ARMOR, ModelLayers.ZOMBIE_OUTER_ARMOR, ModelLayers.ZOMBIE_BABY_INNER_ARMOR, ModelLayers.ZOMBIE_BABY_OUTER_ARMOR);
    }

    public GingerbreadManRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelLayerLocation, ModelLayerLocation modelLayerLocation2, ModelLayerLocation modelLayerLocation3, ModelLayerLocation modelLayerLocation4, ModelLayerLocation modelLayerLocation5, ModelLayerLocation modelLayerLocation6) {
        super(context, new ZombieModel(context.bakeLayer(modelLayerLocation)), new ZombieModel(context.bakeLayer(modelLayerLocation2)), new ZombieModel(context.bakeLayer(modelLayerLocation3)), new ZombieModel(context.bakeLayer(modelLayerLocation4)), new ZombieModel(context.bakeLayer(modelLayerLocation5)), new ZombieModel(context.bakeLayer(modelLayerLocation6)));
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieRenderState zombieRenderState) {
        return TEXTURE;
    }

    @Override
    public ZombieRenderState createRenderState() {
        return new ZombieRenderState();
    }
}
