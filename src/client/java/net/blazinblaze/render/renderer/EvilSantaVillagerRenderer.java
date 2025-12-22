package net.blazinblaze.render.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blazinblaze.BlazinChristmasMod;
import net.blazinblaze.entity.custom.EvilSantaVillager;
import net.blazinblaze.render.model.EvilSantaVillagerModel;
import net.blazinblaze.render.state.EvilSantaVillagerState;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.entity.state.EvokerRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.item.CrossbowItem;

public class EvilSantaVillagerRenderer extends MobRenderer<EvilSantaVillager, EvilSantaVillagerState, EvilSantaVillagerModel> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "textures/entity/evil_santa_villager.png");

    public EvilSantaVillagerRenderer(EntityRendererProvider.Context context) {
        super(context, new EvilSantaVillagerModel(context.bakeLayer(BCMModelLayers.EVIL_SANTA_VILLAGER)), 0.5f);
        this.addLayer(
                new ItemInHandLayer<EvilSantaVillagerState, EvilSantaVillagerModel>(this) {
                    public void render(
                            PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, EvilSantaVillagerState evilSantaVillagerState, float f, float g
                    ) {
                        if (evilSantaVillagerState.isCastingSpell) {
                            super.render(matrixStack, vertexConsumerProvider, i, evilSantaVillagerState, f, g);
                        }
                    }
                }
        );
    }

    @Override
    public ResourceLocation getTextureLocation(EvilSantaVillagerState state) {
        return TEXTURE;
    }

    @Override
    public EvilSantaVillagerState createRenderState() {
        return new EvilSantaVillagerState();
    }

    public void extractRenderState(EvilSantaVillager villager, EvilSantaVillagerState evilSantaVillagerState, float f) {
        super.extractRenderState(villager, evilSantaVillagerState, f);
        ArmedEntityRenderState.extractArmedEntityRenderState(villager, evilSantaVillagerState, this.itemModelResolver);
        evilSantaVillagerState.isRiding = villager.isPassenger();
        evilSantaVillagerState.mainArm = villager.getMainArm();
        evilSantaVillagerState.armPose = villager.getArmPose();
        evilSantaVillagerState.maxCrossbowChargeDuration = evilSantaVillagerState.armPose == AbstractIllager.IllagerArmPose.CROSSBOW_CHARGE ? CrossbowItem.getChargeDuration(villager.getUseItem(), villager) : 0;
        evilSantaVillagerState.ticksUsingItem = villager.getTicksUsingItem();
        evilSantaVillagerState.attackAnim = villager.getAttackAnim(f);
        evilSantaVillagerState.isAggressive = villager.isAggressive();
        evilSantaVillagerState.isCastingSpell = villager.isCastingSpell();
    }
}
