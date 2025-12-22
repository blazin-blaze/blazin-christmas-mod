package net.blazinblaze.block;

import net.blazinblaze.BlazinChristmasMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class BCMTags {
    public static final TagKey<Block> WITHERED_SNOW = of("withered_snow_tag");

    private static TagKey<Block> of(String id) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, id));
    }

    public static void initTags() {}
}
