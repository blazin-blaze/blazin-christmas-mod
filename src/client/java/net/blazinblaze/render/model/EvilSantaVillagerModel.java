package net.blazinblaze.render.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blazinblaze.render.state.EvilSantaVillagerState;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.monster.AbstractIllager;

public class EvilSantaVillagerModel extends EntityModel<EvilSantaVillagerState> implements HeadedModel, ArmedModel {
    private final ModelPart head;
    private final ModelPart hat;
    private final ModelPart body;
    private final ModelPart arms;
    private final ModelPart arms_rotation;
    private final ModelPart arms_flipped;
    private final ModelPart left_arm;
    private final ModelPart right_arm;
    private final ModelPart left_leg;
    private final ModelPart right_leg;
    public EvilSantaVillagerModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.hat = root.getChild("hat");
        this.body = root.getChild("body");
        this.arms = root.getChild("arms");
        this.arms_rotation = this.arms.getChild("arms_rotation");
        this.arms_flipped = this.arms_rotation.getChild("arms_flipped");
        this.left_arm = root.getChild("left_arm");
        this.right_arm = root.getChild("right_arm");
        this.left_leg = root.getChild("left_leg");
        this.right_leg = root.getChild("right_leg");
    }
    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition head = modelPartData.addOrReplaceChild("head", CubeListBuilder.create().texOffs(28, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 24).addBox(-5.0F, -11.5F, -5.0F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(28, 36).addBox(-4.0F, -12.5F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(40, 18).addBox(-3.0F, -14.5F, -4.0F, 5.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(60, 0).addBox(-1.0F, -16.5F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(16, 54).addBox(0.0F, -17.5F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(28, 18).addBox(3.0F, -19.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(60, 7).addBox(-4.0F, -2.5F, -5.0F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(60, 10).addBox(-3.0F, -0.5F, -5.0F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(60, 13).addBox(-2.0F, 1.5F, -5.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 58).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition hat = modelPartData.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = modelPartData.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 36).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition arms = modelPartData.addOrReplaceChild("arms", CubeListBuilder.create(), PartPose.offset(0.0F, 3.5F, 0.3F));

        PartDefinition arms_rotation = arms.addOrReplaceChild("arms_rotation", CubeListBuilder.create().texOffs(0, 54).addBox(-8.0F, 0.0F, -2.05F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(40, 28).addBox(-4.0F, 4.0F, -2.05F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.05F, -0.7505F, 0.0F, 0.0F));

        PartDefinition arms_flipped = arms_rotation.addOrReplaceChild("arms_flipped", CubeListBuilder.create().texOffs(0, 54).mirror().addBox(4.0F, -24.0F, -2.05F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition left_arm = modelPartData.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(28, 45).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition right_arm = modelPartData.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(28, 45).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition left_leg = modelPartData.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(44, 45).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 12.0F, 0.0F));

        PartDefinition right_leg = modelPartData.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(44, 45).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 12.0F, 0.0F));
        return LayerDefinition.create(modelData, 128, 128);
    }

    public void setupAnim(EvilSantaVillagerState evilSantaVillagerState) {
        super.setupAnim(evilSantaVillagerState);
        this.head.xRot = evilSantaVillagerState.xRot * (float) (Math.PI / 180.0);
        this.head.yRot = evilSantaVillagerState.yRot * (float) (Math.PI / 180.0);
        if (evilSantaVillagerState.isRiding) {
            this.right_arm.yRot = (float) (-Math.PI / 5);
            this.right_arm.xRot = 0.0F;
            this.right_arm.zRot = 0.0F;
            this.left_arm.yRot = (float) (-Math.PI / 5);
            this.left_arm.xRot = 0.0F;
            this.left_arm.zRot = 0.0F;
            this.right_leg.yRot = -1.4137167F;
            this.right_leg.xRot = (float) (Math.PI / 10);
            this.right_leg.zRot = 0.07853982F;
            this.left_leg.yRot = -1.4137167F;
            this.left_leg.xRot = (float) (-Math.PI / 10);
            this.left_leg.zRot = -0.07853982F;
        } else {
            float f = evilSantaVillagerState.walkAnimationSpeed;
            float g = evilSantaVillagerState.walkAnimationPos;
            this.right_arm.xRot = Mth.cos(g * 0.6662F + (float) Math.PI) * 2.0F * f * 0.5F;
            this.right_arm.yRot = 0.0F;
            this.right_arm.zRot = 0.0F;
            this.left_arm.xRot = Mth.cos(g * 0.6662F) * 2.0F * f * 0.5F;
            this.left_arm.yRot = 0.0F;
            this.left_arm.zRot = 0.0F;
            this.right_leg.xRot = Mth.cos(g * 0.6662F) * 1.4F * f * 0.5F;
            this.right_leg.yRot = 0.0F;
            this.right_leg.zRot = 0.0F;
            this.left_leg.xRot = Mth.cos(g * 0.6662F + (float) Math.PI) * 1.4F * f * 0.5F;
            this.left_leg.yRot = 0.0F;
            this.left_leg.zRot = 0.0F;
        }

        AbstractIllager.IllagerArmPose state = evilSantaVillagerState.armPose;
        if (state == AbstractIllager.IllagerArmPose.ATTACKING) {
            if (evilSantaVillagerState.getMainHandItem().isEmpty()) {
                AnimationUtils.animateZombieArms(this.left_arm, this.right_arm, true, evilSantaVillagerState.attackAnim, evilSantaVillagerState.ageInTicks);
            } else {
                AnimationUtils.swingWeaponDown(this.right_arm, this.left_arm, evilSantaVillagerState.mainArm, evilSantaVillagerState.attackAnim, evilSantaVillagerState.ageInTicks);
            }
        }else if (state == AbstractIllager.IllagerArmPose.SPELLCASTING) {
            this.right_arm.z = 0.0F;
            this.right_arm.x = -5.0F;
            this.left_arm.z = 0.0F;
            this.left_arm.x = 5.0F;
            this.right_arm.xRot = Mth.cos(evilSantaVillagerState.ageInTicks * 0.6662F) * 0.25F;
            this.left_arm.xRot = Mth.cos(evilSantaVillagerState.ageInTicks * 0.6662F) * 0.25F;
            this.right_arm.zRot = (float) (Math.PI * 3.0 / 4.0);
            this.left_arm.zRot = (float) (-Math.PI * 3.0 / 4.0);
            this.right_arm.yRot = 0.0F;
            this.left_arm.yRot = 0.0F;
        } else if (state == AbstractIllager.IllagerArmPose.CELEBRATING) {
            this.right_arm.z = 0.0F;
            this.right_arm.x = -5.0F;
            this.right_arm.xRot = Mth.cos(evilSantaVillagerState.ageInTicks * 0.6662F) * 0.05F;
            this.right_arm.zRot = 2.670354F;
            this.right_arm.yRot = 0.0F;
            this.left_arm.z = 0.0F;
            this.left_arm.x = 5.0F;
            this.left_arm.xRot = Mth.cos(evilSantaVillagerState.ageInTicks * 0.6662F) * 0.05F;
            this.left_arm.zRot = (float) (-Math.PI * 3.0 / 4.0);
            this.left_arm.yRot = 0.0F;
        }

        boolean bl = state == AbstractIllager.IllagerArmPose.CROSSED;
        this.arms.visible = bl;
        this.left_arm.visible = !bl;
        this.right_arm.visible = !bl;
    }

    private ModelPart getAttackingArm(HumanoidArm arm) {
        return arm == HumanoidArm.LEFT ? this.left_arm : this.right_arm;
    }

    public ModelPart getHat() {
        return this.hat;
    }

    @Override
    public ModelPart getHead() {
        return this.head;
    }

    @Override
    public void translateToHand(HumanoidArm arm, PoseStack matrices) {
        this.root.translateAndRotate(matrices);
        this.getAttackingArm(arm).translateAndRotate(matrices);
    }
}
