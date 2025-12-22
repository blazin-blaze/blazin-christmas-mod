package net.blazinblaze.block.custom;

import net.blazinblaze.block.BCMBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ConsumableGlassBottle extends GlassBottle {
    public ConsumableGlassBottle(Properties settings) {
        super(settings);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if(world instanceof ServerLevel serverWorld) {
            if(player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && player.isShiftKeyDown()) {
                BlockState original = serverWorld.getBlockState(pos);
                player.playNotifySound(SoundEvents.GENERIC_DRINK.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
                serverWorld.setBlockAndUpdate(pos, BCMBlocks.GLASS_BOTTLE.defaultBlockState());
                if(original.is(BCMBlocks.GLASS_OF_MILK)) {
                    player.removeAllEffects();
                }else {
                    //add code for egg nog status effect
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }
}
