package net.blazinblaze.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.blazinblaze.block.BCMBlocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.BlockBlobFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBlobFeature.class)
public class ForestRockMixin {
    @WrapOperation(method = "place(Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/feature/BlockBlobFeature;isDirt(Lnet/minecraft/world/level/block/state/BlockState;)Z"))
    private boolean includeSnowBlocks(BlockState blockState, Operation<Boolean> original) {
        if(blockState.is(BCMBlocks.CANDY_CANE_SNOW)) {
            return true;
        }
        return original.call(blockState);
    }
}
