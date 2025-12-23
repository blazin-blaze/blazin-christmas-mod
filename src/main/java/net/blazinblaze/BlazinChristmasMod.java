package net.blazinblaze;

import net.blazinblaze.advancement.BCMCriteria;
import net.blazinblaze.block.BCMBlockEntities;
import net.blazinblaze.block.BCMBlocks;
import net.blazinblaze.block.BCMTags;
import net.blazinblaze.block.custom.PresentBlockEntity;
import net.blazinblaze.component.BCMComponents;
import net.blazinblaze.data.BCMAttachmentTypes;
import net.blazinblaze.data.LastPresentAttachmentData;
import net.blazinblaze.entity.BCMEntities;
import net.blazinblaze.entity.task.BCMMemoryModules;
import net.blazinblaze.entity.task.BCMSensorTypes;
import net.blazinblaze.item.BCMItems;
import net.blazinblaze.item.component.BCMFoodComponents;
import net.blazinblaze.item.tags.BCMItemTags;
import net.blazinblaze.misc.SnowballPlaySound;
import net.blazinblaze.networking.PresentAdvancementC2SPayload;
import net.blazinblaze.networking.PresentSignerC2SPayload;
import net.blazinblaze.screen.ChristmasTreeHandler;
import net.blazinblaze.screen.PresentHandler;
import net.blazinblaze.sound.BCMSounds;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlazinChristmasMod implements ModInitializer {
	public static final String MOD_ID = "blazin-christmas-mod";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final ResourceKey<CreativeModeTab> CUSTOM_ITEM_GROUP_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "item_group"));
	public static final CreativeModeTab CUSTOM_ITEM_GROUP = FabricItemGroup.builder()
			.icon(() -> new ItemStack(BCMBlocks.WREATH))
			.title(Component.translatable("itemGroup.bcm-item-group"))
			.build();

	public static final MenuType<ChristmasTreeHandler> CHRISTMAS_TREE_HANDLER = Registry.register(BuiltInRegistries.MENU, ResourceLocation.fromNamespaceAndPath(MOD_ID, "christmas_tree_handler"), new MenuType<>(ChristmasTreeHandler::new, FeatureFlagSet.of()));
	public static final MenuType<PresentHandler> PRESENT_HANDLER = Registry.register(BuiltInRegistries.MENU, ResourceLocation.fromNamespaceAndPath(MOD_ID, "present_handler"), new MenuType<>(PresentHandler::new, FeatureFlagSet.of()));
	public static MinecraftServer serverRef;

	@Override
	public void onInitialize() {
		BCMFoodComponents.initialize();
		BCMTags.initTags();
		BCMBlocks.initialize();
		BCMBlockEntities.initializeBlckEntities();
		BCMItemTags.initialize();
		BCMItems.initialize();
		BCMEntities.initializeAttributes();
		BCMEntities.initializeMobs();
		BCMSounds.initialize();
		BCMMemoryModules.initialize();
		BCMSensorTypes.initialize();
		BCMComponents.initialize();
		BCMAttachmentTypes.initialize();
		BCMCriteria.init();

		SnowballPlaySound.register();

		CustomPortalBuilder.beginPortal()
				.frameBlock(BCMBlocks.ETHEREAL_SNOW)
				.lightWithItem(BCMItems.JOLLY_ESSENCE)
				.destDimID(ResourceLocation.fromNamespaceAndPath(MOD_ID, "north_pole"))
				.customPortalBlock((CustomPortalBlock) BCMBlocks.NORTH_POLE_PORTAL)
				.returnDim(Level.OVERWORLD.location(), false)
				.tintColor(64, 196, 255)
				.registerPortal();

		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CUSTOM_ITEM_GROUP_KEY, CUSTOM_ITEM_GROUP);

		PayloadTypeRegistry.playC2S().register(PresentSignerC2SPayload.ID, PresentSignerC2SPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(PresentAdvancementC2SPayload.ID, PresentAdvancementC2SPayload.CODEC);

		ItemGroupEvents.modifyEntriesEvent(CUSTOM_ITEM_GROUP_KEY).register(itemGroup -> {
			itemGroup.accept(BCMBlocks.WREATH);
			itemGroup.accept(BCMBlocks.SNOWBALL_PATCH);
			itemGroup.accept(BCMItems.EGG_NOG_BUCKET);
			itemGroup.accept(BCMBlocks.GLASS_OF_MILK);
			itemGroup.accept(BCMBlocks.GLASS_OF_EGG_NOG);
			itemGroup.accept(BCMBlocks.GLASS_AND_PLATE);
			itemGroup.accept(BCMBlocks.MILK_AND_COOKIES);
			itemGroup.accept(BCMBlocks.EGG_NOG_AND_COOKIES);
			itemGroup.accept(BCMBlocks.CHRISTMAS_CAKE);
			itemGroup.accept(BCMItems.CANDY_CANE);
			itemGroup.accept(BCMBlocks.CANDY_CANE_BLOCK);
			itemGroup.accept(BCMBlocks.CANDY_CANE_BLOCK_CR);
			itemGroup.accept(BCMBlocks.CANDY_CANE_SNOW);
			itemGroup.accept(BCMItems.CANDY_CANE_SNOWBALL);
			itemGroup.accept(BCMBlocks.ETHEREAL_SNOW);
			itemGroup.accept(BCMItems.ETHEREAL_SNOWBALL);
			itemGroup.accept(BCMItems.SNOWFLAKE);
			itemGroup.accept(BCMItems.JOLLY_ESSENCE);
			itemGroup.accept(BCMBlocks.WITHERED_SNOW);
			itemGroup.accept(BCMItems.WITHERED_SNOWBALL);
			itemGroup.accept(BCMBlocks.FOULED_SNOW);
			itemGroup.accept(BCMItems.GINGERBREAD);
			itemGroup.accept(BCMBlocks.BLOCK_OF_GINGERBREAD);
			itemGroup.accept(BCMBlocks.FIERY_SNOW);
			itemGroup.accept(BCMItems.FIERY_SNOWBALL);
			itemGroup.accept(BCMBlocks.LICORICE_LOG);
			itemGroup.accept(BCMBlocks.LICORICE_PLANKS);
			itemGroup.accept(BCMBlocks.COMPRESSED_LICORICE);
			itemGroup.accept(BCMBlocks.LICORICE_STAIRS);
			itemGroup.accept(BCMBlocks.LICORICE_SLAB);
			itemGroup.accept(BCMBlocks.COMPRESSED_LICORICE_STAIRS);
			itemGroup.accept(BCMBlocks.COMPRESSED_LICORICE_SLAB);
			itemGroup.accept(BCMBlocks.CHRISTMAS_TREE_H1);
			itemGroup.accept(BCMItems.BLUE_WHITE_ORNAMENT);
			itemGroup.accept(BCMItems.SILVER_GOLD_ORNAMENT);
			itemGroup.accept(BCMItems.RED_GREEN_ORNAMENT);
			itemGroup.accept(BCMBlocks.BLUE_WHITE_LIGHTS);
			itemGroup.accept(BCMBlocks.SILVER_GOLD_LIGHTS);
			itemGroup.accept(BCMBlocks.RED_GREEN_LIGHTS);
			itemGroup.accept(BCMBlocks.WHITE_PRESENT);
			itemGroup.accept(BCMBlocks.LIGHT_GRAY_PRESENT);
			itemGroup.accept(BCMBlocks.GRAY_PRESENT);
			itemGroup.accept(BCMBlocks.BLACK_PRESENT);
			itemGroup.accept(BCMBlocks.BROWN_PRESENT);
			itemGroup.accept(BCMBlocks.RED_PRESENT);
			itemGroup.accept(BCMBlocks.ORANGE_PRESENT);
			itemGroup.accept(BCMBlocks.YELLOW_PRESENT);
			itemGroup.accept(BCMBlocks.LIME_PRESENT);
			itemGroup.accept(BCMBlocks.GREEN_PRESENT);
			itemGroup.accept(BCMBlocks.CYAN_PRESENT);
			itemGroup.accept(BCMBlocks.LIGHT_BLUE_PRESENT);
			itemGroup.accept(BCMBlocks.BLUE_PRESENT);
			itemGroup.accept(BCMBlocks.PURPLE_PRESENT);
			itemGroup.accept(BCMBlocks.MAGENTA_PRESENT);
			itemGroup.accept(BCMBlocks.PINK_PRESENT);
			itemGroup.accept(BCMBlocks.GINGERBREAD_HEAD);
			itemGroup.accept(BCMBlocks.SANTA_HEAD);
			itemGroup.accept(BCMBlocks.SNOWMAN_HEAD);
			itemGroup.accept(BCMItems.SNOWBALL_STAFF);
			itemGroup.accept(BCMBlocks.EVIL_SANTA_SPAWNER);
		});

		ServerPlayNetworking.registerGlobalReceiver(PresentSignerC2SPayload.ID, (payload, context) -> {
			ServerPlayer playerEntity = context.player();
			ServerLevel world = playerEntity.level();
			String singer = payload.signer();
			LastPresentAttachmentData data = playerEntity.getAttachedOrCreate(BCMAttachmentTypes.LAST_PRESENT_ATTACHMENT_TYPE, () -> LastPresentAttachmentData.DEFAULT);
			if(data.pos() != null) {
				if(world.getBlockEntity(data.pos()) instanceof PresentBlockEntity entity) {
					entity.setGifterName(singer);
				}
			}
		});

		ServerPlayNetworking.registerGlobalReceiver(PresentAdvancementC2SPayload.ID, (payload, context) -> {
			ServerPlayer playerEntity = context.player();
			BCMCriteria.SIGN_GIFT.trigger(playerEntity);
		});

		ServerLifecycleEvents.SERVER_STARTED.register((server -> {
			serverRef = server;
		}));

		ServerLifecycleEvents.SERVER_STOPPED.register((server -> {
			serverRef = null;
		}));
	}
}