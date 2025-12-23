package net.blazinblaze.entity.ai;

import net.blazinblaze.advancement.BCMCriteria;
import net.blazinblaze.block.BCMBlocks;
import net.blazinblaze.block.custom.GlassAndPlate;
import net.blazinblaze.entity.custom.SantaVillager;
import net.blazinblaze.misc.RandomBCMProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.jetbrains.annotations.Nullable;

public class EatCookiesGoal extends MoveToBlockGoal {
    private final SantaVillager santaVillager;

    public EatCookiesGoal(SantaVillager santaVillager, double d, int i) {
        super(santaVillager, d, i);
        this.santaVillager = santaVillager;
    }

    @Override
    public boolean canUse() {
        if (!getServerLevel(this.santaVillager).getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            return false;
        } else if (this.nextStartTick > 0) {
            this.nextStartTick--;
            return false;
        } else if (this.findNearestBlock() && this.santaVillager.getCookiesEaten() < 1) {
            this.nextStartTick = reducedTickDelay(20);
            return true;
        } else {
            this.nextStartTick = this.nextStartTick(this.mob);
            return false;
        }
    }

    @Override
    public void tick() {
        super.tick();
        Level level = this.santaVillager.level();
        BlockPos blockPos = this.santaVillager.blockPosition();
        BlockPos blockPos2 = this.getPosWithBlock(blockPos, level);
        if (this.isReachedTarget() && blockPos2 != null && level instanceof ServerLevel serverLevel) {
            serverLevel.playSound(this.santaVillager, blockPos, SoundEvents.PLAYER_BURP, SoundSource.AMBIENT, 1.0F, 1.0F);
            serverLevel.playSound(this.santaVillager, blockPos, SoundEvents.GENERIC_DRINK.value(), SoundSource.AMBIENT, 1.0F, 1.0F);
            BlockState original = serverLevel.getBlockState(blockPos2);
            serverLevel.setBlockAndUpdate(blockPos2, BCMBlocks.GLASS_AND_PLATE.defaultBlockState().setValue(GlassAndPlate.FACING, original.getValue(GlassAndPlate.FACING)));
            this.santaVillager.heal(1.5f);
            this.santaVillager.setCookiesEaten(this.santaVillager.getCookiesEaten() + 1);
            for(ServerPlayer serverPlayer : serverLevel.getPlayers((serverPlayer1) -> true)) {
                BCMCriteria.SANTA_EAT.trigger(serverPlayer);
            }
        }
    }

    @Nullable
    private BlockPos getPosWithBlock(BlockPos blockPos, BlockGetter blockGetter) {
        if (blockGetter.getBlockState(blockPos).is(BCMBlocks.EGG_NOG_AND_COOKIES) || blockGetter.getBlockState(blockPos).is(BCMBlocks.MILK_AND_COOKIES)) {
            return blockPos;
        } else {
            BlockPos[] blockPoss = new BlockPos[]{blockPos.below(), blockPos.west(), blockPos.east(), blockPos.north(), blockPos.south(), blockPos.below().below()};

            for (BlockPos blockPos2 : blockPoss) {
                if (blockGetter.getBlockState(blockPos2).is(BCMBlocks.EGG_NOG_AND_COOKIES) || blockGetter.getBlockState(blockPos2).is(BCMBlocks.MILK_AND_COOKIES)) {
                    return blockPos2;
                }
            }

            return null;
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader levelReader, BlockPos blockPos) {
        ChunkAccess chunkAccess = levelReader.getChunk(
                SectionPos.blockToSectionCoord(blockPos.getX()), SectionPos.blockToSectionCoord(blockPos.getZ()), ChunkStatus.FULL, false
        );
        return chunkAccess == null ? false : levelReader.getBlockState(blockPos).is(BCMBlocks.EGG_NOG_AND_COOKIES) || levelReader.getBlockState(blockPos).is(BCMBlocks.MILK_AND_COOKIES);
    }

    @Override
    public double acceptedDistance() {
        return 3;
    }
}
