package net.blazinblaze.render.model;

import net.blazinblaze.render.state.ReindeerState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.LlamaModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.LlamaRenderState;
import net.minecraft.client.renderer.entity.state.VexRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import java.util.Map;
import java.util.function.UnaryOperator;

public class ReindeerModel extends EntityModel<ReindeerState> {
    public static final MeshTransformer BABY_TRANSFORMER = ReindeerModel::transformToBaby;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart chest_left;
    private final ModelPart chest_right;

    public ReindeerModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
        this.chest_left = root.getChild("chest_left");
        this.chest_right = root.getChild("chest_right");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(28, 28).addBox(-2.0F, -14.0F, -10.0F, 4.0F, 4.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(0, 28).addBox(-4.0F, -16.0F, -6.0F, 8.0F, 18.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(44, 22).addBox(1.0F, -19.0F, -4.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(44, 22).addBox(-4.0F, -19.0F, -4.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(44, 41).addBox(-6.0F, -17.0F, -4.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(44, 41).addBox(4.0F, -17.0F, -4.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(44, 41).addBox(-8.0F, -20.0F, -4.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(44, 41).addBox(6.0F, -20.0F, -4.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(44, 47).addBox(-9.0F, -23.0F, -4.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(44, 47).addBox(-6.0F, -23.0F, -4.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(44, 47).addBox(-5.0F, -24.0F, -4.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(44, 47).addBox(-10.0F, -24.0F, -4.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(44, 47).addBox(5.0F, -23.0F, -4.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(44, 47).addBox(4.0F, -24.0F, -4.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(44, 47).addBox(8.0F, -23.0F, -4.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(44, 47).addBox(9.0F, -24.0F, -4.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, -6.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition leg1 = partdefinition.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(28, 41).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 10.0F, 6.0F));

        PartDefinition leg2 = partdefinition.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(28, 41).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 10.0F, 6.0F));

        PartDefinition leg3 = partdefinition.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(28, 41).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 10.0F, -5.0F));

        PartDefinition leg4 = partdefinition.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(28, 41).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 10.0F, -5.0F));

        PartDefinition chest_left = partdefinition.addOrReplaceChild("chest_left", CubeListBuilder.create().texOffs(44, 0).addBox(-3.0F, 0.0F, 0.0F, 8.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, 3.0F, 3.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition chest_right = partdefinition.addOrReplaceChild("chest_right", CubeListBuilder.create().texOffs(44, 11).addBox(-3.0F, 0.0F, 0.0F, 8.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.5F, 3.0F, 3.0F, 0.0F, 1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    private static MeshDefinition transformToBaby(MeshDefinition meshDefinition) {
        float f = 2.0F;
        float g = 0.7F;
        float h = 1.1F;
        UnaryOperator<PartPose> unaryOperator = (partPose) -> partPose.translated(0.0F, 21.0F, 3.52F).scaled(0.71428573F, 0.64935064F, 0.7936508F);
        UnaryOperator<PartPose> unaryOperator2 = (partPose) -> partPose.translated(0.0F, 33.0F, 0.0F).scaled(0.625F, 0.45454544F, 0.45454544F);
        UnaryOperator<PartPose> unaryOperator3 = (partPose) -> partPose.translated(0.0F, 33.0F, 0.0F).scaled(0.45454544F, 0.41322312F, 0.45454544F);
        MeshDefinition meshDefinition2 = new MeshDefinition();

        for(Map.Entry<String, PartDefinition> entry : meshDefinition.getRoot().getChildren()) {
            String string = (String)entry.getKey();
            PartDefinition partDefinition = (PartDefinition)entry.getValue();
            UnaryOperator var10000;
            switch (string) {
                case "head" -> var10000 = unaryOperator;
                case "body" -> var10000 = unaryOperator2;
                default -> var10000 = unaryOperator3;
            }

            UnaryOperator<PartPose> unaryOperator4 = var10000;
            meshDefinition2.getRoot().addOrReplaceChild(string, partDefinition.transformed(unaryOperator4));
        }

        return meshDefinition2;
    }

    public void setupAnim(ReindeerState reindeerState) {
        super.setupAnim(reindeerState);
        this.head.xRot = reindeerState.xRot * ((float)Math.PI / 180F);
        this.head.yRot = reindeerState.yRot * ((float)Math.PI / 180F);
        float f = reindeerState.walkAnimationSpeed;
        float g = reindeerState.walkAnimationPos;
        this.leg1.xRot = Mth.cos(g * 0.6662F) * 1.4F * f;
        this.leg2.xRot = Mth.cos(g * 0.6662F + (float)Math.PI) * 1.4F * f;
        this.leg3.xRot = Mth.cos(g * 0.6662F + (float)Math.PI) * 1.4F * f;
        this.leg4.xRot = Mth.cos(g * 0.6662F) * 1.4F * f;
        this.chest_right.visible = reindeerState.hasChest;
        this.chest_left.visible = reindeerState.hasChest;
    }
}
