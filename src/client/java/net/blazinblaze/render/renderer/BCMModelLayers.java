package net.blazinblaze.render.renderer;

import net.blazinblaze.BlazinChristmasMod;
import net.blazinblaze.render.model.ElfVexModel;
import net.blazinblaze.render.model.EvilSantaVillagerModel;
import net.blazinblaze.render.model.ReindeerModel;
import net.blazinblaze.render.model.SantaVillagerModel;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class BCMModelLayers {
    public static final ModelLayerLocation SANTA_VILLAGER = createMain("santa_villager");
    public static final ModelLayerLocation ELF_VEX = createMain("elf_vex");
    public static final ModelLayerLocation EVIL_SANTA_VILLAGER = createMain("evil_santa_villager");
    public static final ModelLayerLocation REINDEER = createMain("reindeer");
    public static final ModelLayerLocation REINDEER_BABY = createMain("reindeer_baby");

    private static ModelLayerLocation createMain(String name) {
        return new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, name), "main");
    }

    public static void registerModelLayers() {
        EntityModelLayerRegistry.registerModelLayer(BCMModelLayers.SANTA_VILLAGER, SantaVillagerModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(BCMModelLayers.ELF_VEX, ElfVexModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(BCMModelLayers.EVIL_SANTA_VILLAGER, EvilSantaVillagerModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(BCMModelLayers.REINDEER, ReindeerModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(BCMModelLayers.REINDEER_BABY, ReindeerModel::createBodyLayer);
    }
}
