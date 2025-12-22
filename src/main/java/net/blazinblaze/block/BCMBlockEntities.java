package net.blazinblaze.block;

import net.blazinblaze.BlazinChristmasMod;
import net.blazinblaze.block.custom.ChristmasTreeBlockEntity;
import net.blazinblaze.block.custom.DepletableWaterEntity;
import net.blazinblaze.block.custom.PresentBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BCMBlockEntities {
    public static final BlockEntityType<ChristmasTreeBlockEntity> CHRISTMAS_TREE_BLOCK_ENTITY =
            register("christmas_tree_block_entity", ChristmasTreeBlockEntity::new, BCMBlocks.CHRISTMAS_TREE_H1);

    public static final BlockEntityType<PresentBlockEntity> PRESENT_BLOCK_ENTITY =
            register("present_block_entity", PresentBlockEntity::new,
                    new Block[]{BCMBlocks.WHITE_PRESENT,
                            BCMBlocks.LIGHT_GRAY_PRESENT,
                            BCMBlocks.GRAY_PRESENT,
                            BCMBlocks.BLACK_PRESENT,
                            BCMBlocks.BROWN_PRESENT,
                            BCMBlocks.RED_PRESENT,
                            BCMBlocks.ORANGE_PRESENT,
                            BCMBlocks.YELLOW_PRESENT,
                            BCMBlocks.LIME_PRESENT,
                            BCMBlocks.GREEN_PRESENT,
                            BCMBlocks.CYAN_PRESENT,
                            BCMBlocks.LIGHT_BLUE_PRESENT,
                            BCMBlocks.BLUE_PRESENT,
                            BCMBlocks.PURPLE_PRESENT,
                            BCMBlocks.MAGENTA_PRESENT,
                            BCMBlocks.PINK_PRESENT});

    public static final BlockEntityType<DepletableWaterEntity> DEPLETABLE_WATER_ENTITY =
            register("depletable_water_entity", DepletableWaterEntity::new, BCMBlocks.DEPLETABLE_WATER);

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, name);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }

    public static void initializeBlckEntities() {}
}
