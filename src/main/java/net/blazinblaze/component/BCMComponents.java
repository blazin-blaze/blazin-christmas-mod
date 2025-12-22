package net.blazinblaze.component;

import com.mojang.serialization.Codec;
import net.blazinblaze.BlazinChristmasMod;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class BCMComponents {

    public static final DataComponentType<String> PRESENT_SIGNER = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "present_signer"),
            DataComponentType.<String>builder().persistent(Codec.STRING).build()
    );

    public static final DataComponentType<Boolean> IS_SNOWBALL_LOADED = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "is_snowball_loaded"),
            DataComponentType.<Boolean>builder().persistent(Codec.BOOL).build()
    );

    public static final DataComponentType<String> SNOWBALL_STAFF_TYPE = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, "snowball_staff_type"),
            DataComponentType.<String>builder().persistent(Codec.STRING).build()
    );

    public static void initialize() {}
}
