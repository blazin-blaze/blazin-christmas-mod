package net.blazinblaze.render.renderer;

import net.blazinblaze.BlazinChristmasMod;
import net.blazinblaze.entity.custom.ElfVex;
import net.blazinblaze.render.model.ElfVexModel;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.entity.state.VexRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Vex;

public class ElfVexRenderer extends MobRenderer<ElfVex, VexRenderState, ElfVexModel> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "textures/entity/elf_vex.png");
    private static final ResourceLocation CHARGING_TEXTURE = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "textures/entity/elf_vex_charging.png");

    public ElfVexRenderer(EntityRendererProvider.Context context) {
        super(context, new ElfVexModel(context.bakeLayer(BCMModelLayers.ELF_VEX)), 0.3F);
        this.addLayer(new ItemInHandLayer<>(this));
    }

    protected int getBlockLightLevel(ElfVex vexEntity, BlockPos blockPos) {
        return 15;
    }

    public ResourceLocation getTextureLocation(VexRenderState vexEntityRenderState) {
        return vexEntityRenderState.isCharging ? CHARGING_TEXTURE : TEXTURE;
    }

    public VexRenderState createRenderState() {
        return new VexRenderState();
    }

    public void extractRenderState(ElfVex vexEntity, VexRenderState vexEntityRenderState, float f) {
        super.extractRenderState(vexEntity, vexEntityRenderState, f);
        ArmedEntityRenderState.extractArmedEntityRenderState(vexEntity, vexEntityRenderState, this.itemModelResolver);
        vexEntityRenderState.isCharging = vexEntity.isCharging();
    }
}
