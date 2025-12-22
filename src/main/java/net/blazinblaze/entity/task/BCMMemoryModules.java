package net.blazinblaze.entity.task;

import net.blazinblaze.BlazinChristmasMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Optional;

public class BCMMemoryModules {

    public static final MemoryModuleType<BlockPos> NEAREST_SNOW_BLOCK = registerMemory("nearest_snow_block");

    public static <T> MemoryModuleType<T> registerMemory(String id) {
        return Registry.register(BuiltInRegistries.MEMORY_MODULE_TYPE, ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, id), new MemoryModuleType<>(Optional.empty()));
    }

    public static void initialize() {}
}
