package net.blazinblaze.mixin.client;

import com.mojang.serialization.Lifecycle;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.world.level.storage.WorldData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldOpenFlows.class)
public abstract class RemoveExperimentalMixin {

    // Make SaveProperties.getLifecycle() always return Lifecycle.stable()
    @Redirect(
            method = "openWorldCheckWorldStemCompatibility",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/storage/WorldData;worldGenSettingsLifecycle()Lcom/mojang/serialization/Lifecycle;"
            )
    )
    private Lifecycle removeAdviceOnLoad(WorldData worldData) {
        return Lifecycle.stable();
    }

    // Set bypassWarnings = true
    @ModifyVariable(
            method = "confirmWorldCreation",
            at = @At("HEAD"),
            argsOnly = true,
            index = 4
    )
    private static boolean removeAdviceOnCreation(boolean original) {
        return true;
    }
}
