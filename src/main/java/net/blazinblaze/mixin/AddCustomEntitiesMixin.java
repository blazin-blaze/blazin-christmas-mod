package net.blazinblaze.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.blazinblaze.block.custom.EvilSantaSpawner;
import net.blazinblaze.entity.spawner.EggNogCowSpawner;
import net.blazinblaze.entity.spawner.SantaVillagerSpawner;
import net.blazinblaze.entity.spawner.SnowflakeSpawner;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.CustomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(MinecraftServer.class)
public class AddCustomEntitiesMixin {
    @Inject(method = "createLevels", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Registry;getValue(Lnet/minecraft/resources/ResourceKey;)Ljava/lang/Object;", shift = At.Shift.AFTER))
    private void addEggNogCowSpawner(ChunkProgressListener worldGenerationProgressListener, CallbackInfo ci, @Local LocalRef<List<CustomSpawner>> list) {
        List<CustomSpawner> tempList = new ArrayList<>(list.get());
        tempList.add(new EggNogCowSpawner());
        tempList.add(new SantaVillagerSpawner());
        tempList.add(new SnowflakeSpawner());
        list.set(ImmutableList.copyOf(tempList));
    }
}
