package net.blazinblaze.entity.ai;

import net.blazinblaze.block.BCMBlocks;
import net.blazinblaze.block.custom.ChristmasTreeBlockEntity;
import net.blazinblaze.block.custom.PresentBlockEntity;
import net.blazinblaze.entity.custom.SantaVillager;
import net.blazinblaze.misc.RandomBCMProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.jetbrains.annotations.Nullable;

public class AddPresentsGoal extends MoveToBlockGoal {
    private final SantaVillager santaVillager;

    public AddPresentsGoal(SantaVillager santaVillager, double d, int i) {
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
        } else if (this.findNearestBlock() && this.santaVillager.getCookiesEaten() >= 1) {
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
        if (this.isReachedTarget() && blockPos2 != null && level instanceof ServerLevel serverLevel && this.santaVillager.getCookiesEaten() >= 1) {
            BlockEntity blockEntity = serverLevel.getBlockEntity(blockPos2);;
            if(blockEntity instanceof ChristmasTreeBlockEntity christmasTreeBlockEntity) {
                int numOfStacks = 0;
                ItemStack randomPresent = RandomBCMProvider.getRandomPresent();
                NonNullList<ItemStack> oldInventory = christmasTreeBlockEntity.getItems();
                NonNullList<ItemStack> newInventory = NonNullList.withSize(4, ItemStack.EMPTY);
                for(int i = 0; i < christmasTreeBlockEntity.getContainerSize(); i++) {
                    ItemStack stack = oldInventory.get(i);
                    if(!stack.isEmpty()) {
                        numOfStacks++;
                        newInventory.set(i, stack);
                    }else {
                        newInventory.set(i, randomPresent);
                        break;
                    }
                }
                if(numOfStacks < christmasTreeBlockEntity.getContainerSize()) {
                    christmasTreeBlockEntity.setItems(newInventory);
                    serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, randomPresent.getItem().getDefaultInstance().copy()), blockPos2.getX(), blockPos2.getY(), blockPos2.getZ(), 6, 0,0,0, 0.5F);
                    serverLevel.playSound(this.santaVillager, blockPos, SoundEvents.ITEM_PICKUP, SoundSource.AMBIENT, 1.0F, 1.0F);
                    this.santaVillager.setCookiesEaten(this.santaVillager.getCookiesEaten() - 1);
                }
            }
        }
    }

    @Nullable
    private BlockPos getPosWithBlock(BlockPos blockPos, BlockGetter blockGetter) {
        if (blockGetter.getBlockState(blockPos).is(BCMBlocks.CHRISTMAS_TREE_H1)) {
            return blockPos;
        } else {
            BlockPos[] blockPoss = new BlockPos[]{blockPos.below(), blockPos.west(), blockPos.east(), blockPos.north(), blockPos.south(), blockPos.below().below()};

            for (BlockPos blockPos2 : blockPoss) {
                if (blockGetter.getBlockState(blockPos2).is(BCMBlocks.CHRISTMAS_TREE_H1)) {
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
        return chunkAccess == null ? false : levelReader.getBlockState(blockPos).is(BCMBlocks.CHRISTMAS_TREE_H1);
    }

    @Override
    public double acceptedDistance() {
        return 3;
    }
}
