package net.blazinblaze;

import net.blazinblaze.block.BCMBlocks;
import net.blazinblaze.entity.BCMEntities;
import net.blazinblaze.render.renderer.*;
import net.blazinblaze.screen.ChristmasTreeScreen;
import net.blazinblaze.screen.PresentScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class BlazinChristmasModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.putBlock(BCMBlocks.SNOWBALL_PATCH, ChunkSectionLayer.TRANSLUCENT);
		BlockRenderLayerMap.putBlock(BCMBlocks.GLASS_BOTTLE, ChunkSectionLayer.TRANSLUCENT);
		BlockRenderLayerMap.putBlock(BCMBlocks.GLASS_OF_MILK, ChunkSectionLayer.TRANSLUCENT);
		BlockRenderLayerMap.putBlock(BCMBlocks.GLASS_OF_EGG_NOG, ChunkSectionLayer.TRANSLUCENT);
		BlockRenderLayerMap.putBlock(BCMBlocks.GLASS_AND_PLATE, ChunkSectionLayer.TRANSLUCENT);
		BlockRenderLayerMap.putBlock(BCMBlocks.EGG_NOG_AND_COOKIES, ChunkSectionLayer.TRANSLUCENT);
		BlockRenderLayerMap.putBlock(BCMBlocks.MILK_AND_COOKIES, ChunkSectionLayer.TRANSLUCENT);
		BlockRenderLayerMap.putBlock(BCMBlocks.BLUE_WHITE_LIGHTS, ChunkSectionLayer.TRANSLUCENT);
		BlockRenderLayerMap.putBlock(BCMBlocks.SILVER_GOLD_LIGHTS, ChunkSectionLayer.TRANSLUCENT);
		BlockRenderLayerMap.putBlock(BCMBlocks.RED_GREEN_LIGHTS, ChunkSectionLayer.TRANSLUCENT);
		BlockRenderLayerMap.putBlock(BCMBlocks.WREATH, ChunkSectionLayer.CUTOUT);
		BlockRenderLayerMap.putBlock(BCMBlocks.CHRISTMAS_TREE_H1, ChunkSectionLayer.CUTOUT);
		BlockRenderLayerMap.putBlock(BCMBlocks.CHRISTMAS_TREE_H2, ChunkSectionLayer.CUTOUT);

		BCMModelLayers.registerModelLayers();
		EntityRendererRegistry.register(BCMEntities.SANTA_VILLAGER, SantaVillagerRenderer::new);
		EntityRendererRegistry.register(BCMEntities.EGG_NOG_COW, EggNogCowRenderer::new);
		EntityRendererRegistry.register(BCMEntities.ELF_VEX, ElfVexRenderer::new);
		EntityRendererRegistry.register(BCMEntities.FRIENDLY_ELF_VEX, FriendlyElfVexRenderer::new);
		EntityRendererRegistry.register(BCMEntities.EVIL_SANTA_VILLAGER, EvilSantaVillagerRenderer::new);
		EntityRendererRegistry.register(BCMEntities.REINDEER, ReindeerRenderer::new);
		EntityRendererRegistry.register(BCMEntities.GINGERBREAD_MAN, GingerbreadManRenderer::new);
		EntityRendererRegistry.register(BCMEntities.FRIENDLY_GINGERBREAD_MAN, FriendlyGingerbreadManRenderer::new);

		EntityRendererRegistry.register(BCMEntities.ETHEREAL_SNOWBALL_ENTITY, ThrownItemRenderer::new);
		EntityRendererRegistry.register(BCMEntities.FIERY_SNOWBALL_ENTITY, ThrownItemRenderer::new);
		EntityRendererRegistry.register(BCMEntities.WITHERED_SNOWBALL_ENTITY, ThrownItemRenderer::new);
		EntityRendererRegistry.register(BCMEntities.CANDY_CANE_SNOWBALL_ENTITY, ThrownItemRenderer::new);

		MenuScreens.register(BlazinChristmasMod.CHRISTMAS_TREE_HANDLER, ChristmasTreeScreen::new);
		MenuScreens.register(BlazinChristmasMod.PRESENT_HANDLER, PresentScreen::new);

		/*/ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
			int defaultColor = getDefaultLeafColor(state, world, pos);
			return SnowColorChanger.getColorForMultiplier(defaultColor);
		}, Blocks.OAK_LEAVES, Blocks.MANGROVE_LEAVES, Blocks.BIRCH_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.ACACIA_LEAVES, Blocks.AZALEA_LEAVES, Blocks.FLOWERING_AZALEA_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.GRASS_BLOCK, Blocks.SHORT_GRASS, Blocks.TALL_GRASS, Blocks.BUSH);/*/

		//ClientTickEvents.START_WORLD_TICK.register((SnowColorChanger::currentSnowColor));

		/*/WorldRenderEvents.START.register((worldRenderContext -> {
			WorldRenderer renderer = worldRenderContext.worldRenderer();
			BlockPos pos = worldRenderContext.camera().getBlockPos();
			int renderDistance = MinecraftClient.getInstance().options.getViewDistance().getValue();
			if(renderer != null) {
				for(int x = 0; x < renderDistance; x++) {
					for(int z = 0; z < renderDistance; z++) {
						int currentX = pos.getX()+(x*16);
						int currentZ = pos.getZ()+(z*16);
						BlockPos blockPos = new BlockPos(currentX, worldRenderContext.world().getTopY(Heightmap.Type.WORLD_SURFACE, currentX, currentZ), currentZ);
						ChunkSectionPos.forEachChunkSectionAround(blockPos, chunk -> {
							if(!SnowColorChanger.isChunkInList(chunk)) {
								((ScheduleChunkInvoker)renderer).invokeScheduleChunkRender(ChunkSectionPos.unpackX(chunk), ChunkSectionPos.unpackY(chunk), ChunkSectionPos.unpackZ(chunk), true);
								SnowColorChanger.addChunkToList(chunk);
							}
						});
					}
				}
			}
		}));/*/

	}

	/*/private int getDefaultLeafColor(BlockState state, BlockRenderView world, BlockPos pos) {
		if(state.getBlock() == Blocks.SPRUCE_LEAVES) {
			return FoliageColors.SPRUCE;
		}else if(state.getBlock() == Blocks.BIRCH_LEAVES) {
			return FoliageColors.BIRCH;
		}else if(state.getBlock() == Blocks.MANGROVE_LEAVES) {
			return FoliageColors.MANGROVE;
		}else if(world != null && pos != null) {
			return BiomeColors.getFoliageColor(world, pos);
		}else {
			return FoliageColors.DEFAULT;
		}
	}/*/
}