package net.blazinblaze.block.custom;

import net.blazinblaze.block.BCMBlocks;
import net.blazinblaze.item.component.BCMFoodComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ConsumableGlassAndPlate extends GlassAndPlate {
    public ConsumableGlassAndPlate(Properties settings) {
        super(settings);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if(world instanceof ServerLevel serverWorld) {
            if(player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && player.isShiftKeyDown()) {
                BlockState original = serverWorld.getBlockState(pos);
                player.playNotifySound(SoundEvents.PLAYER_BURP, SoundSource.BLOCKS, 1.0F, 1.0F);
                player.playNotifySound(SoundEvents.GENERIC_DRINK.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
                serverWorld.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, Items.COOKIE.getDefaultInstance().copy()), pos.getX(), pos.getY(), pos.getZ(), 6, 0,0,0, 0.5F);
                if(original.is(BCMBlocks.MILK_AND_COOKIES)) {
                    player.removeAllEffects();
                    player.getFoodData().eat(BCMFoodComponents.COOKIES_PLATE);
                }else {
                    //add code for egg nog effect
                    player.getFoodData().eat(BCMFoodComponents.COOKIES_PLATE);
                }
                serverWorld.setBlockAndUpdate(pos, BCMBlocks.GLASS_AND_PLATE.defaultBlockState().setValue(FACING, original.getValue(FACING)));
                return InteractionResult.SUCCESS;
            }
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }
}
