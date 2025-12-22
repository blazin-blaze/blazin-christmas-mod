package net.blazinblaze.block.custom;

import net.blazinblaze.block.BCMBlocks;
import net.blazinblaze.item.BCMItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.stream.Stream;

public class GlassBottle extends Block {
    public GlassBottle(Properties settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Stream.of(
                Block.box(6, 1, 5, 10, 4, 6),
                Block.box(6, 1, 10, 10, 4, 11),
                Block.box(5, 1, 6, 6, 4, 10),
                Block.box(10, 1, 6, 11, 4, 10),
                Block.box(6, 0, 6, 10, 5, 10),
                Block.box(6.75, 5, 6.75, 9.25, 8, 9.25)
        ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(world instanceof ServerLevel serverWorld) {
            if(stack.is(BCMBlocks.GLASS_OF_MILK.asItem())) {
                if(stack.is(BCMBlocks.GLASS_BOTTLE.asItem())) {
                    return InteractionResult.PASS;
                }
                player.playNotifySound(SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                serverWorld.setBlockAndUpdate(pos, BCMBlocks.GLASS_OF_MILK.defaultBlockState());
                return InteractionResult.CONSUME;
            }else if(stack.is(BCMItems.EGG_NOG_BUCKET)) {
                player.playNotifySound(SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                serverWorld.setBlockAndUpdate(pos, BCMBlocks.GLASS_OF_EGG_NOG.defaultBlockState());
                return InteractionResult.CONSUME;
            }else if(stack.is(BCMBlocks.GLASS_OF_EGG_NOG.asItem())) {
                player.playNotifySound(SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                serverWorld.setBlockAndUpdate(pos, BCMBlocks.GLASS_OF_EGG_NOG.defaultBlockState());
                return InteractionResult.CONSUME;
            }else if(stack.is(Items.MILK_BUCKET)) {
                player.playNotifySound(SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                serverWorld.setBlockAndUpdate(pos, BCMBlocks.GLASS_OF_MILK.defaultBlockState());
                return InteractionResult.CONSUME;
            }
        }
        return super.useItemOn(stack, state, world, pos, player, hand, hit);
    }
}
