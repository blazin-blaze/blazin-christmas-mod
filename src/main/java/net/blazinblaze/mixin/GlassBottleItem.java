package net.blazinblaze.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.blazinblaze.block.BCMBlocks;
import net.blazinblaze.block.custom.GlassBottle;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.world.item.BottleItem.class)
public class GlassBottleItem {
    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/BlockHitResult;getType()Lnet/minecraft/world/phys/HitResult$Type;", shift = At.Shift.AFTER), cancellable = true)
    private void addBlockPlace(Level world, Player user, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir, @Local BlockHitResult result, @Local ItemStack stack) {
        BlockState state = world.getBlockState(result.getBlockPos());
        if(!state.is(BlockTags.AIR) && state.isRedstoneConductor(world, result.getBlockPos()) && !world.getFluidState(result.getBlockPos()).is(FluidTags.WATER)) {
            BlockPos up = result.getBlockPos().above();
            if(world.getBlockState(up).is(BlockTags.AIR)) {
                world.setBlockAndUpdate(up, BCMBlocks.GLASS_BOTTLE.defaultBlockState());
                world.playLocalSound(up, SoundEvents.GLASS_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F, true);
                stack.consume(1, user);
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }
}
