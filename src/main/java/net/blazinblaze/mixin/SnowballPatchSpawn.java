package net.blazinblaze.mixin;

import net.blazinblaze.BlazinChristmasMod;
import net.blazinblaze.block.BCMBlocks;
import net.blazinblaze.block.custom.SnowballPatch;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Snowball.class)
public class SnowballPatchSpawn {

    @Inject(method = "onHit", at = @At("TAIL"))
    public void spawnPatch(HitResult hitResult, CallbackInfo ci) {
        if(hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult)hitResult;
            Direction dir = blockHitResult.getDirection().getOpposite();
            Snowball entity = (Snowball) (Object) this;

            BlockPos blockPos = blockHitResult.getBlockPos();
            BlockState state = BCMBlocks.SNOWBALL_PATCH.defaultBlockState();

            java.util.Random random = new java.util.Random();
            int randomPos = random.nextInt(0, 4);
            BlazinChristmasMod.LOGGER.info(String.valueOf(randomPos));

            if(dir == Direction.UP) {
                BlockPos newBlockPos = blockPos.offset(0, -1, 0);
                if(entity.level().isEmptyBlock(newBlockPos)) {
                    entity.level().setBlockAndUpdate(newBlockPos, state.setValue(PipeBlock.UP, true)
                            .setValue(PipeBlock.NORTH, false)
                            .setValue(PipeBlock.SOUTH, false)
                            .setValue(PipeBlock.EAST, false)
                            .setValue(PipeBlock.WEST, false)
                            .setValue(PipeBlock.DOWN, false)
                            .setValue(SnowballPatch.SNOWBALL_POS, randomPos));
                    entity.level().playSound((Snowball)(Object)this, newBlockPos, SoundEvents.SNOW_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }else if(dir == Direction.NORTH) {
                BlockPos newBlockPos = blockPos.offset(0, 0, 1);
                if(entity.level().isEmptyBlock(newBlockPos)) {
                    entity.level().setBlockAndUpdate(newBlockPos, state.setValue(PipeBlock.UP, false)
                            .setValue(PipeBlock.NORTH, true)
                            .setValue(PipeBlock.SOUTH, false)
                            .setValue(PipeBlock.EAST, false)
                            .setValue(PipeBlock.WEST, false)
                            .setValue(PipeBlock.DOWN, false)
                            .setValue(SnowballPatch.SNOWBALL_POS, randomPos));
                    entity.level().playSound((Snowball)(Object)this, newBlockPos, SoundEvents.SNOW_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }else if(dir == Direction.SOUTH) {
                BlockPos newBlockPos = blockPos.offset(0, 0, -1);
                if(entity.level().isEmptyBlock(newBlockPos)) {
                    entity.level().setBlockAndUpdate(newBlockPos, state.setValue(PipeBlock.UP, false)
                            .setValue(PipeBlock.NORTH, false)
                            .setValue(PipeBlock.SOUTH, true)
                            .setValue(PipeBlock.EAST, false)
                            .setValue(PipeBlock.WEST, false)
                            .setValue(PipeBlock.DOWN, false)
                            .setValue(SnowballPatch.SNOWBALL_POS, randomPos));
                    entity.level().playSound((Snowball)(Object)this, newBlockPos, SoundEvents.SNOW_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }else if(dir == Direction.EAST) {
                BlockPos newBlockPos = blockPos.offset(-1, 0, 0);
                if(entity.level().isEmptyBlock(newBlockPos)) {
                    entity.level().setBlockAndUpdate(newBlockPos, state.setValue(PipeBlock.UP, false)
                            .setValue(PipeBlock.NORTH, false)
                            .setValue(PipeBlock.SOUTH, false)
                            .setValue(PipeBlock.EAST, true)
                            .setValue(PipeBlock.WEST, false)
                            .setValue(PipeBlock.DOWN, false)
                            .setValue(SnowballPatch.SNOWBALL_POS, randomPos));
                    entity.level().playSound((Snowball)(Object)this, newBlockPos, SoundEvents.SNOW_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }else if(dir == Direction.WEST) {
                BlockPos newBlockPos = blockPos.offset(1, 0, 0);
                if(entity.level().isEmptyBlock(newBlockPos)) {
                    entity.level().setBlockAndUpdate(newBlockPos, state.setValue(PipeBlock.UP, false)
                            .setValue(PipeBlock.NORTH, false)
                            .setValue(PipeBlock.SOUTH, false)
                            .setValue(PipeBlock.EAST, false)
                            .setValue(PipeBlock.WEST, true)
                            .setValue(PipeBlock.DOWN, false)
                            .setValue(SnowballPatch.SNOWBALL_POS, randomPos));
                    entity.level().playSound((Snowball)(Object)this, newBlockPos, SoundEvents.SNOW_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }else if(dir == Direction.DOWN) {
                BlockPos newBlockPos = blockPos.offset(0, 1, 0);
                if (entity.level().isEmptyBlock(newBlockPos)) {
                    entity.level().setBlockAndUpdate(newBlockPos, state.setValue(PipeBlock.UP, false)
                            .setValue(PipeBlock.NORTH, false)
                            .setValue(PipeBlock.SOUTH, false)
                            .setValue(PipeBlock.EAST, false)
                            .setValue(PipeBlock.WEST, false)
                            .setValue(PipeBlock.DOWN, true)
                            .setValue(SnowballPatch.SNOWBALL_POS, randomPos));
                    entity.level().playSound((Snowball)(Object)this, newBlockPos, SoundEvents.SNOW_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
        }
    }
}
