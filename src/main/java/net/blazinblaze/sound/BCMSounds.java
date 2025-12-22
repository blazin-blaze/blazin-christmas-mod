package net.blazinblaze.sound;

import net.blazinblaze.BlazinChristmasMod;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class BCMSounds {

    public static final SoundEvent SANTA_VILLAGER_AMBIENT = register(ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "entity-santa_villager-ambient"));
    public static final SoundEvent EVIL_SANTA_VILLAGER_AMBIENT = register(ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "entity-evil_santa_villager-ambient"));

    private static SoundEvent register(ResourceLocation resourceLocation) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, resourceLocation, SoundEvent.createVariableRangeEvent(resourceLocation));
    }

    public static void initialize() {}
}
