package net.blazinblaze.block;

import net.blazinblaze.BlazinChristmasMod;
import net.blazinblaze.block.custom.*;
import net.blazinblaze.component.BCMComponents;
import net.blazinblaze.item.component.BCMFoodComponents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import java.util.function.Function;

public class BCMBlocks {
    private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings, boolean shouldRegisterItem) {
        // Create a registry key for the blocks
        ResourceKey<Block> blockKey = keyOfBlock(name);
        // Create the blocks instance
        Block block = blockFactory.apply(settings.setId(blockKey));

        // Sometimes, you may not want to register an item for the blocks.
        // Eg: if it's a technical blocks like `minecraft:moving_piston` or `minecraft:end_gateway`
        if (shouldRegisterItem) {
            // Items need to be registered with a different type of registry key, but the ID
            // can be the same.
            ResourceKey<Item> itemKey = keyOfItem(name);

            BlockItem blockItem;

            if(name.matches("glass_and_plate")) {
                blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).stacksTo(1));
            }else if(name.matches("glass_of_milk")) {
                blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).stacksTo(1).component(DataComponents.CONSUMABLE, Consumables.MILK_BUCKET));
            }else if(name.matches("glass_of_egg_nog")) {
                blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).stacksTo(1).component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK));
            }else if(name.matches("milk_and_cookies")) {
                blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).stacksTo(1).component(DataComponents.CONSUMABLE, Consumables.MILK_BUCKET).food(BCMFoodComponents.COOKIES_PLATE));
            }else if(name.matches("egg_nog_and_cookies")) {
                blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).stacksTo(1).component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK).food(BCMFoodComponents.COOKIES_PLATE));
            }else if(name.matches("white_present") ||
                    name.matches("light_gray_present") ||
                    name.matches("gray_present") ||
                    name.matches("black_present") ||
                    name.matches("brown_present") ||
                    name.matches("red_present") ||
                    name.matches("orange_present") ||
                    name.matches("yellow_present") ||
                    name.matches("lime_present") ||
                    name.matches("green_present") ||
                    name.matches("light_blue_present") ||
                    name.matches("blue_present") ||
                    name.matches("purple_present") ||
                    name.matches("magenta_present") ||
                    name.matches("pink_present") ||
                    name.matches("cyan_present")) {
                blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).stacksTo(1).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY).component(BCMComponents.PRESENT_SIGNER, "test"));
            }else if(name.matches("snowman_head") || name.matches("gingerbread_man_head") || name.matches("santa_head")) {
                blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).equippable(EquipmentSlot.HEAD));
            }else {
                blockItem = new BlockItem(block, new Item.Properties().setId(itemKey));
            }

            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);

        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    private static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, name));
    }

    private static ResourceKey<Item> keyOfItem(String name) {
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, name));
    }

    public static final Block SNOWBALL_PATCH = register(
            "snowball_patch",
            SnowballPatch::new,
            BlockBehaviour.Properties.of().sound(SoundType.SNOW).pushReaction(PushReaction.DESTROY).instabreak().replaceable().noCollission().randomTicks(),
            true
    );

    public static final Block WREATH = register(
            "wreath",
            Wreath::new,
            BlockBehaviour.Properties.of().sound(SoundType.GRASS).noOcclusion().lightLevel(state -> 10).noCollission().strength(0.5F),
            true
    );

    public static final Block GLASS_BOTTLE = register(
            "glass_bottle",
            GlassBottle::new,
            BlockBehaviour.Properties.of().sound(SoundType.GLASS).noOcclusion().strength(0.5F),
            false
    );

    public static final Block GLASS_OF_MILK = register(
            "glass_of_milk",
            ConsumableGlassBottle::new,
            BlockBehaviour.Properties.of().sound(SoundType.GLASS).noOcclusion().strength(0.5F),
            true
    );

    public static final Block GLASS_OF_EGG_NOG = register(
            "glass_of_egg_nog",
            ConsumableGlassBottle::new,
            BlockBehaviour.Properties.of().sound(SoundType.GLASS).noOcclusion().strength(0.5F),
            true
    );

    public static final Block GLASS_AND_PLATE = register(
            "glass_and_plate",
            GlassAndPlate::new,
            BlockBehaviour.Properties.of().sound(SoundType.GLASS).noOcclusion().strength(0.5F),
            true
    );

    public static final Block MILK_AND_COOKIES = register(
            "milk_and_cookies",
            ConsumableGlassAndPlate::new,
            BlockBehaviour.Properties.of().sound(SoundType.GLASS).noOcclusion().strength(0.5F),
            true
    );

    public static final Block EGG_NOG_AND_COOKIES = register(
            "egg_nog_and_cookies",
            ConsumableGlassAndPlate::new,
            BlockBehaviour.Properties.of().sound(SoundType.GLASS).noOcclusion().strength(0.5F),
            true
    );

    public static final Block CHRISTMAS_CAKE = register(
            "christmas_cake",
            CakeBlock::new,
            BlockBehaviour.Properties.of().sound(SoundType.WOOL).forceSolidOn().pushReaction(PushReaction.DESTROY).strength(0.5F),
            true
    );

    public static final Block CANDY_CANE_BLOCK = register(
            "candy_cane_block",
            Block::new,
            BlockBehaviour.Properties.of().sound(SoundType.CANDLE).forceSolidOn().strength(0.5F),
            true
    );

    public static final Block CANDY_CANE_BLOCK_CR = register(
            "candy_cane_block_cr",
            Block::new,
            BlockBehaviour.Properties.of().sound(SoundType.CANDLE).forceSolidOn().strength(0.5F),
            true
    );

    public static final Block ETHEREAL_SNOW = register(
            "ethereal_snow",
            Block::new,
            BlockBehaviour.Properties.of().sound(SoundType.SNOW).forceSolidOn().strength(0.5F).requiresCorrectToolForDrops(),
            true
    );

    public static final Block CANDY_CANE_SNOW = register(
            "candy_cane_snow",
            Block::new,
            BlockBehaviour.Properties.of().sound(SoundType.CALCITE).forceSolidOn().strength(0.5F),
            true
    );

    public static final Block WITHERED_SNOW = register(
            "withered_snow",
            Block::new,
            BlockBehaviour.Properties.of().sound(SoundType.SOUL_SOIL).forceSolidOn().strength(0.1F),
            true
    );

    public static final Block FOULED_SNOW = register(
            "fouled_snow",
            Block::new,
            BlockBehaviour.Properties.of().sound(SoundType.SOUL_SAND).forceSolidOn().strength(0.1F),
            true
    );

    public static final Block BLOCK_OF_GINGERBREAD = register(
            "block_of_gingerbread",
            Block::new,
            BlockBehaviour.Properties.of().sound(SoundType.CROP).forceSolidOn().strength(0.5F),
            true
    );

    public static final Block NORTH_POLE_PORTAL = register(
            "north_pole_portal",
            NorthPolePortal::new,
            BlockBehaviour.Properties.of().sound(SoundType.SNOW).noCollission().strength(-1.0F).randomTicks().lightLevel(state -> 11).pushReaction(PushReaction.BLOCK),
            true
    );

    public static final Block LICORICE_LOG = register(
            "licorice_log",
            RotatedPillarBlock::new,
            Blocks.logProperties(MapColor.TERRACOTTA_BLACK, MapColor.COLOR_BLACK, SoundType.WOOD),
            true
    );

    public static final Block LICORICE_PLANKS = register(
            "licorice_planks",
            Block::new,
            BlockBehaviour.Properties.of().sound(SoundType.WOOD).strength(2.0F, 3.0F).ignitedByLava(),
            true
    );

    public static final Block COMPRESSED_LICORICE = register(
            "compressed_licorice",
            Block::new,
            BlockBehaviour.Properties.of().sound(SoundType.SLIME_BLOCK).strength(2.0F, 3.0F).ignitedByLava(),
            true
    );

    public static final Block LICORICE_STAIRS = register(
            "licorice_stairs",
            (settings) -> new StairBlock(LICORICE_PLANKS.defaultBlockState(), settings),
            BlockBehaviour.Properties.ofLegacyCopy(BCMBlocks.LICORICE_PLANKS).strength(2.0F, 3.0F).ignitedByLava(),
            true
    );

    public static final Block LICORICE_SLAB = register(
            "licorice_slab",
            SlabBlock::new,
            BlockBehaviour.Properties.of().sound(SoundType.WOOD).strength(2.0F, 3.0F).ignitedByLava(),
            true
    );

    public static final Block COMPRESSED_LICORICE_STAIRS = register(
            "compressed_licorice_stairs",
            (settings) -> new StairBlock(LICORICE_PLANKS.defaultBlockState(), settings),
            BlockBehaviour.Properties.ofLegacyCopy(BCMBlocks.COMPRESSED_LICORICE).ignitedByLava(),
            true
    );

    public static final Block COMPRESSED_LICORICE_SLAB = register(
            "compressed_licorice_slab",
            SlabBlock::new,
            BlockBehaviour.Properties.of().sound(SoundType.SLIME_BLOCK).ignitedByLava(),
            true
    );

    public static final Block CHRISTMAS_TREE_H1 = register(
            "christmas_tree_h1",
            ChristmasTreeH1::new,
            BlockBehaviour.Properties.of().sound(SoundType.WOOD).noOcclusion().pushReaction(PushReaction.DESTROY).strength(0.5F),
            true
    );

    public static final Block CHRISTMAS_TREE_H2 = register(
            "christmas_tree_h2",
            ChristmasTreeH2::new,
            BlockBehaviour.Properties.of().sound(SoundType.WOOD).noOcclusion().lightLevel(state -> {
                boolean ornament1 = state.getValue(ChristmasTreeH2.SILVER_AND_GOLD).booleanValue();
                boolean ornament2 = state.getValue(ChristmasTreeH2.WHITE_AND_BLUE).booleanValue();
                boolean ornament3 = state.getValue(ChristmasTreeH2.GREEN_AND_RED).booleanValue();
                if(ornament1 || ornament2 || ornament3) {
                    return 10;
                }
                return 0;
            }).pushReaction(PushReaction.DESTROY).strength(0.5F),
            false
    );

    public static final Block BLACK_PRESENT = register(
            "black_present",
            (settings) -> new PresentBlock(DyeColor.BLACK, settings),
            BlockBehaviour.Properties.of().sound(SoundType.WOOL).noOcclusion().pushReaction(PushReaction.DESTROY).strength(0.5F),
            true
    );

    public static final Block LIGHT_GRAY_PRESENT = register(
            "light_gray_present",
            (settings) -> new PresentBlock(DyeColor.LIGHT_GRAY, settings),
            BlockBehaviour.Properties.of().sound(SoundType.WOOL).noOcclusion().pushReaction(PushReaction.DESTROY).strength(0.5F),
            true
    );

    public static final Block GRAY_PRESENT = register(
            "gray_present",
            (settings) -> new PresentBlock(DyeColor.GRAY, settings),
            BlockBehaviour.Properties.of().sound(SoundType.WOOL).noOcclusion().pushReaction(PushReaction.DESTROY).strength(0.5F),
            true
    );

    public static final Block WHITE_PRESENT = register(
            "white_present",
            (settings) -> new PresentBlock(DyeColor.WHITE, settings),
            BlockBehaviour.Properties.of().sound(SoundType.WOOL).noOcclusion().pushReaction(PushReaction.DESTROY).strength(0.5F),
            true
    );

    public static final Block BROWN_PRESENT = register(
            "brown_present",
            (settings) -> new PresentBlock(DyeColor.BROWN, settings),
            BlockBehaviour.Properties.of().sound(SoundType.WOOL).noOcclusion().pushReaction(PushReaction.DESTROY).strength(0.5F),
            true
    );

    public static final Block RED_PRESENT = register(
            "red_present",
            (settings) -> new PresentBlock(DyeColor.RED, settings),
            BlockBehaviour.Properties.of().sound(SoundType.WOOL).noOcclusion().pushReaction(PushReaction.DESTROY).strength(0.5F),
            true
    );

    public static final Block ORANGE_PRESENT = register(
            "orange_present",
            (settings) -> new PresentBlock(DyeColor.ORANGE, settings),
            BlockBehaviour.Properties.of().sound(SoundType.WOOL).noOcclusion().pushReaction(PushReaction.DESTROY).strength(0.5F),
            true
    );

    public static final Block YELLOW_PRESENT = register(
            "yellow_present",
            (settings) -> new PresentBlock(DyeColor.YELLOW, settings),
            BlockBehaviour.Properties.of().sound(SoundType.WOOL).noOcclusion().pushReaction(PushReaction.DESTROY).strength(0.5F),
            true
    );

    public static final Block LIME_PRESENT = register(
            "lime_present",
            (settings) -> new PresentBlock(DyeColor.LIME, settings),
            BlockBehaviour.Properties.of().sound(SoundType.WOOL).noOcclusion().pushReaction(PushReaction.DESTROY).strength(0.5F),
            true
    );

    public static final Block GREEN_PRESENT = register(
            "green_present",
            (settings) -> new PresentBlock(DyeColor.GREEN, settings),
            BlockBehaviour.Properties.of().sound(SoundType.WOOL).noOcclusion().pushReaction(PushReaction.DESTROY).strength(0.5F),
            true
    );

    public static final Block CYAN_PRESENT = register(
            "cyan_present",
            (settings) -> new PresentBlock(DyeColor.CYAN, settings),
            BlockBehaviour.Properties.of().sound(SoundType.WOOL).noOcclusion().pushReaction(PushReaction.DESTROY).strength(0.5F),
            true
    );

    public static final Block LIGHT_BLUE_PRESENT = register(
            "light_blue_present",
            (settings) -> new PresentBlock(DyeColor.LIGHT_BLUE, settings),
            BlockBehaviour.Properties.of().sound(SoundType.WOOL).noOcclusion().pushReaction(PushReaction.DESTROY).strength(0.5F),
            true
    );

    public static final Block BLUE_PRESENT = register(
            "blue_present",
            (settings) -> new PresentBlock(DyeColor.BLUE, settings),
            BlockBehaviour.Properties.of().sound(SoundType.WOOL).noOcclusion().pushReaction(PushReaction.DESTROY).strength(0.5F),
            true
    );

    public static final Block PURPLE_PRESENT = register(
            "purple_present",
            (settings) -> new PresentBlock(DyeColor.PURPLE, settings),
            BlockBehaviour.Properties.of().sound(SoundType.WOOL).noOcclusion().pushReaction(PushReaction.DESTROY).strength(0.5F),
            true
    );

    public static final Block MAGENTA_PRESENT = register(
            "magenta_present",
            (settings) -> new PresentBlock(DyeColor.MAGENTA, settings),
            BlockBehaviour.Properties.of().sound(SoundType.WOOL).noOcclusion().pushReaction(PushReaction.DESTROY).strength(0.5F),
            true
    );

    public static final Block PINK_PRESENT = register(
            "pink_present",
            (settings) -> new PresentBlock(DyeColor.PINK, settings),
            BlockBehaviour.Properties.of().sound(SoundType.WOOL).noOcclusion().pushReaction(PushReaction.DESTROY).strength(0.5F),
            true
    );

    public static final Block EVIL_SANTA_SPAWNER = register(
            "evil_santa_spawner",
            EvilSantaSpawner::new,
            BlockBehaviour.Properties.of().sound(SoundType.IRON),
            true
    );

    public static final Block DEPLETABLE_WATER = register("depletable_water",
            settings -> new FluidBlockDepletable(Fluids.WATER, settings),
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WATER)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .pushReaction(PushReaction.DESTROY)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .randomTicks(),
            false
    );

    public static final Block FIERY_SNOW = register("fiery_snow",
            Block::new,
            BlockBehaviour.Properties.of().forceSolidOn().sound(SoundType.NETHERRACK).lightLevel(state -> 5).strength(1.0F).requiresCorrectToolForDrops(),
            true
    );

    public static final Block SNOWMAN_HEAD = register("snowman_head",
            FestiveHead::new,
            BlockBehaviour.Properties.of().noOcclusion().sound(SoundType.SNOW).strength(1.0F).pushReaction(PushReaction.DESTROY),
            true
    );

    public static final Block GINGERBREAD_HEAD = register("gingerbread_man_head",
            FestiveHead::new,
            BlockBehaviour.Properties.of().noOcclusion().sound(SoundType.SNOW).strength(1.0F).pushReaction(PushReaction.DESTROY),
            true
    );

    public static final Block SANTA_HEAD = register("santa_head",
            FestiveHead::new,
            BlockBehaviour.Properties.of().noOcclusion().sound(SoundType.SNOW).strength(1.0F).pushReaction(PushReaction.DESTROY),
            true
    );

    public static final Block BLUE_WHITE_LIGHTS = register("blue_white_lights",
            ChristmasLights::new,
            BlockBehaviour.Properties.of().noOcclusion().sound(SoundType.GLASS).strength(0.5F).pushReaction(PushReaction.DESTROY).lightLevel(state -> 13),
            true
    );

    public static final Block RED_GREEN_LIGHTS = register("red_green_lights",
            ChristmasLights::new,
            BlockBehaviour.Properties.of().noOcclusion().sound(SoundType.GLASS).strength(0.5F).pushReaction(PushReaction.DESTROY).lightLevel(state -> 13),
            true
    );

    public static final Block SILVER_GOLD_LIGHTS = register("silver_gold_lights",
            ChristmasLights::new,
            BlockBehaviour.Properties.of().noOcclusion().sound(SoundType.GLASS).strength(0.5F).pushReaction(PushReaction.DESTROY).lightLevel(state -> 13),
            true
    );

    public static void initialize() {
    }
}
