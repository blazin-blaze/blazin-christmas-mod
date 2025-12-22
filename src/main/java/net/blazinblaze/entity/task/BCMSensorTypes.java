package net.blazinblaze.entity.task;

import net.blazinblaze.BlazinChristmasMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import java.util.Optional;
import java.util.function.Supplier;

public class BCMSensorTypes {

    public static final SensorType<NearestSnowSensor> SNOW_BLOCK_SENSOR = registerSensor("nearest_snow_sensor", NearestSnowSensor::new);

    public static <U extends Sensor<?>> SensorType<U> registerSensor(String id, Supplier<U> factory) {
        return Registry.register(BuiltInRegistries.SENSOR_TYPE, ResourceLocation.fromNamespaceAndPath(BlazinChristmasMod.MOD_ID, id), new SensorType<>(factory));
    }

    public static void initialize() {}
}
