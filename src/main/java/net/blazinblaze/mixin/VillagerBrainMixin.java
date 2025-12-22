package net.blazinblaze.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.blazinblaze.entity.task.BCMMemoryModules;
import net.blazinblaze.entity.task.BCMSensorTypes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(Villager.class)
public class VillagerBrainMixin {

    @Shadow @Final private static ImmutableList<MemoryModuleType<?>> MEMORY_TYPES;

    @Shadow @Final private static ImmutableList<SensorType<? extends Sensor<? super Villager>>> SENSOR_TYPES;

    static {
        List<SensorType<? extends Sensor<? super Villager>>> tempList = new ArrayList<>(SENSOR_TYPES);
        List<MemoryModuleType<?>> tempList2 = new ArrayList<>(MEMORY_TYPES);
        tempList.add(BCMSensorTypes.SNOW_BLOCK_SENSOR);
        tempList2.add(BCMMemoryModules.NEAREST_SNOW_BLOCK);
        MEMORY_TYPES = ImmutableList.copyOf(tempList2);
        SENSOR_TYPES = ImmutableList.copyOf(tempList);
    }
}
