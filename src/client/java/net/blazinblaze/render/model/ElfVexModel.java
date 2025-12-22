package net.blazinblaze.render.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.state.VexRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class ElfVexModel extends EntityModel<VexRenderState> implements ArmedModel {
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart right_arm;
    private final ModelPart left_arm;
    private final ModelPart left_wing;
    private final ModelPart right_wing;
    public ElfVexModel(ModelPart root) {
        super(root.getChild("root"), RenderType::entityTranslucent);
        this.body = this.root.getChild("body");
        this.right_arm = this.body.getChild("right_arm");
        this.left_arm = this.body.getChild("left_arm");
        this.left_wing = this.body.getChild("left_wing");
        this.right_wing = this.body.getChild("right_wing");
        this.head = this.root.getChild("head");
    }
    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition root = modelPartData.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, -2.5F, 0.0F));
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 7).addBox(-2.5F, -5.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-3.0F, -5.5F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(16, 17).addBox(-2.0F, -6.5F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(20, 7).addBox(-1.0F, -7.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(28, 12).addBox(0.0F, -8.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 12).addBox(1.0F, -10.5F, -1.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(24, 0).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(16, 22).addBox(-1.5F, 1.0F, -1.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, 20.0F, 0.0F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(26, 22).addBox(-1.5F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.75F, 0.25F, 0.0F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(26, 28).addBox(-0.5F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.75F, 0.25F, 0.0F));

        PartDefinition left_wing = body.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(0, 17).addBox(0.0F, 0.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 1.0F, 1.0F));

        PartDefinition right_wing = body.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 17).addBox(0.0F, 0.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 1.0F, 1.0F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public void setupAnim(VexRenderState vexEntityRenderState) {
        super.setupAnim(vexEntityRenderState);
        this.head.yRot = vexEntityRenderState.yRot * (float) (Math.PI / 180.0);
        this.head.xRot = vexEntityRenderState.xRot * (float) (Math.PI / 180.0);
        float f = Mth.cos(vexEntityRenderState.ageInTicks * 5.5F * (float) (Math.PI / 180.0)) * 0.1F;
        this.right_arm.zRot = (float) (Math.PI / 5) + f;
        this.left_arm.zRot = -((float) (Math.PI / 5) + f);
        if (vexEntityRenderState.isCharging) {
            this.body.yRot = 0.0F;
            this.setArmsChanging(!vexEntityRenderState.rightHandItem.isEmpty(), !vexEntityRenderState.leftHandItem.isEmpty(), f);
        } else {
            this.body.yRot = (float) (Math.PI / 20);
        }

        this.left_wing.yRot = 1.0995574F + Mth.cos(vexEntityRenderState.ageInTicks * 45.836624F * (float) (Math.PI / 180.0)) * (float) (Math.PI / 180.0) * 16.2F;
        this.right_wing.yRot = -this.left_wing.yRot;
        this.left_wing.xRot = 0.47123888F;
        this.left_wing.zRot = -0.47123888F;
        this.right_wing.xRot = 0.47123888F;
        this.right_wing.zRot = 0.47123888F;
    }

    private void setArmsChanging(boolean bl, boolean bl2, float f) {
        if (!bl && !bl2) {
            this.right_arm.xRot = -1.2217305F;
            this.right_arm.yRot = (float) (Math.PI / 12);
            this.right_arm.zRot = -0.47123888F - f;
            this.left_arm.xRot = -1.2217305F;
            this.left_arm.yRot = (float) (-Math.PI / 12);
            this.left_arm.zRot = 0.47123888F + f;
        } else {
            if (bl) {
                this.right_arm.xRot = (float) (Math.PI * 7.0 / 6.0);
                this.right_arm.yRot = (float) (Math.PI / 12);
                this.right_arm.zRot = -0.47123888F - f;
            }

            if (bl2) {
                this.left_arm.xRot = (float) (Math.PI * 7.0 / 6.0);
                this.left_arm.yRot = (float) (-Math.PI / 12);
                this.left_arm.zRot = 0.47123888F + f;
            }
        }
    }

    @Override
    public void translateToHand(HumanoidArm arm, PoseStack matrices) {
        boolean bl = arm == HumanoidArm.RIGHT;
        ModelPart modelPart = bl ? this.right_arm : this.left_arm;
        this.root.translateAndRotate(matrices);
        this.body.translateAndRotate(matrices);
        modelPart.translateAndRotate(matrices);
        matrices.scale(0.55F, 0.55F, 0.55F);
        this.translateForHand(matrices, bl);
    }

    private void translateForHand(PoseStack matrices, boolean mainHand) {
        if (mainHand) {
            matrices.translate(0.046875, -0.15625, 0.078125);
        } else {
            matrices.translate(-0.046875, -0.15625, 0.078125);
        }
    }
}
