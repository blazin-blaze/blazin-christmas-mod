package net.blazinblaze.block.custom;

import net.blazinblaze.entity.BCMEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class EvilSantaSpawner extends Block {
    public EvilSantaSpawner(Properties settings) {
        super(settings);
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if(world instanceof ServerLevel serverWorld) {
            BCMEntities.EVIL_SANTA_VILLAGER.spawn(serverWorld, pos, EntitySpawnReason.MOB_SUMMONED);
        }
        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if(player.isShiftKeyDown()) {
            if(world instanceof ServerLevel serverWorld) {
                BCMEntities.EVIL_SANTA_VILLAGER.spawn(serverWorld, pos, EntitySpawnReason.MOB_SUMMONED);
                serverWorld.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                return InteractionResult.SUCCESS;
            }
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }
}
