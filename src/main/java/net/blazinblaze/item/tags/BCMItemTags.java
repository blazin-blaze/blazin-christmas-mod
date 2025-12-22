package net.blazinblaze.item.tags;

import net.blazinblaze.BlazinChristmasMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class BCMItemTags {
    public static final TagKey<Item> SNOWBALL_LAUNCHABLE = bind("snowball_launchable");

    private static TagKey<Item> bind(String string) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, string));
    }

    public static void initialize() {}
}
