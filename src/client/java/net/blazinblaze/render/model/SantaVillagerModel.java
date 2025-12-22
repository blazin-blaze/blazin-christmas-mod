package net.blazinblaze.render.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blazinblaze.render.renderer.SantaVillagerRenderer;
import net.blazinblaze.render.state.SantaVillagerState;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class SantaVillagerModel extends net.minecraft.client.model.EntityModel<SantaVillagerState> implements VillagerLikeModel {
    private final ModelPart head;
    private final ModelPart headwear;
    private final ModelPart headwear2;
    private final ModelPart body;
    private final ModelPart bodywear;
    private final ModelPart arms;
    private final ModelPart mirrored;
    private final ModelPart right_leg;
    private final ModelPart left_leg;
    public SantaVillagerModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.headwear = root.getChild("headwear");
        this.headwear2 = root.getChild("headwear2");
        this.body = root.getChild("body");
        this.bodywear = root.getChild("bodywear");
        this.arms = root.getChild("arms");
        this.mirrored = this.arms.getChild("mirrored");
        this.right_leg = root.getChild("right_leg");
        this.left_leg = root.getChild("left_leg");
    }
    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition head = modelPartData.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 38).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 83).addBox(0.0F, -17.5F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(81, 17).addBox(3.0F, -19.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(83, 47).addBox(-1.0F, -16.5F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(82, 54).addBox(-3.0F, -14.5F, -4.0F, 5.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(49, 89).addBox(-4.0F, -12.5F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 87).addBox(-5.0F, -11.5F, -5.0F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(12, 65).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(40, 35).addBox(-4.0F, -2.5F, -5.0F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(58, 64).addBox(-3.0F, -0.5F, -5.0F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 65).addBox(-2.0F, 1.5F, -5.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition headwear = modelPartData.addOrReplaceChild("headwear", CubeListBuilder.create().texOffs(32, 38).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.51F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition headwear2 = modelPartData.addOrReplaceChild("headwear2", CubeListBuilder.create().texOffs(28, 0).addBox(-8.0F, -8.0F, -6.0F, 16.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition body = modelPartData.addOrReplaceChild("body", CubeListBuilder.create().texOffs(40, 17).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bodywear = modelPartData.addOrReplaceChild("bodywear", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 20.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition arms = modelPartData.addOrReplaceChild("arms", CubeListBuilder.create().texOffs(58, 56).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(64, 35).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.95F, -1.05F, -0.7505F, 0.0F, 0.0F));

        PartDefinition mirrored = arms.addOrReplaceChild("mirrored", CubeListBuilder.create().texOffs(64, 35).mirror().addBox(4.0F, -23.05F, -3.05F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 21.05F, 1.05F));

        PartDefinition right_leg = modelPartData.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(62, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 12.0F, 0.0F));

        PartDefinition left_leg = modelPartData.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(62, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 12.0F, 0.0F));
        return LayerDefinition.create(modelData, 128, 128);
    }

    @Override
    public void setupAnim(SantaVillagerState state) {
        this.head.yRot = state.yRot * (float) (Math.PI / 180.0);
        this.head.xRot = state.xRot * (float) (Math.PI / 180.0);

        this.right_leg.xRot = Mth.cos(state.walkAnimationPos * 0.6662F)
                * 1.4F
                * state.walkAnimationSpeed
                * 0.5F;
        this.left_leg.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (float) Math.PI)
                * 1.4F
                * state.walkAnimationSpeed
                * 0.5F;
        this.right_leg.yRot = 0.0F;
        this.left_leg.yRot = 0.0F;
    }

    @Override
    public void hatVisible(boolean bl) {
        this.head.visible = bl;
    }

    @Override
    public void translateToArms(PoseStack poseStack) {
        this.root.translateAndRotate(poseStack);
        this.arms.translateAndRotate(poseStack);
    }
}
