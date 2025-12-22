package net.blazinblaze.entity.task;

import com.google.common.collect.ImmutableMap;
import net.blazinblaze.BlazinChristmasMod;
import net.blazinblaze.entity.custom.SnowballVillager;
import net.jcip.annotations.Immutable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Items;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SnowballFightTask extends Behavior<Mob> {
    private BlockPos snowPos;
    private int snowballsRemaining;
    private int ticksToNextSnowball;
    private Optional<List<LivingEntity>> villagerEntities;
    private final float walkSpeed;
    private Villager currentTarget;

    public SnowballFightTask(float walkSpeed) {
        super(ImmutableMap.of(BCMMemoryModules.NEAREST_SNOW_BLOCK, MemoryStatus.VALUE_PRESENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryStatus.VALUE_PRESENT));
        this.walkSpeed = walkSpeed;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel world, Mob entity) {
        return entity.isBaby() && shouldStartSnowball(world, entity) && getVisibleVillagers(entity).isPresent();
    }

    @Override
    protected boolean canStillUse(ServerLevel world, Mob entity, long time) {
        return entity.isBaby()
                && this.snowPos != null
                && this.isSnow(world, this.snowPos)
                && snowballsRemaining > 0
                && villagerEntities.isPresent();
    }

    @Override
    protected boolean timedOut(long time) {
        return false;
    }

    @Override
    protected void start(ServerLevel world, Mob entity, long time) {
        super.start(world, entity, time);
        this.getNearestSnow(entity).ifPresent(pos -> {
            this.snowPos = pos;
            this.snowballsRemaining = 3 + world.random.nextInt(4);
            this.ticksToNextSnowball = 8;
            this.villagerEntities = getVisibleVillagers(entity);
            this.setWalkTarget(entity, pos);
        });
    }

    @Override
    protected void stop(ServerLevel world, Mob entity, long time) {
        super.stop(world, entity, time);
        this.snowPos = null;
        this.snowballsRemaining = 0;
        this.ticksToNextSnowball = 0;
        this.villagerEntities = Optional.empty();
    }

    @Override
    protected void tick(ServerLevel world, Mob entity, long time) {
        if(this.ticksToNextSnowball > 0) {
            this.ticksToNextSnowball--;
            if(this.ticksToNextSnowball <= 8) {
                if(this.currentTarget == null) {
                    this.villagerEntities = getVisibleVillagers(entity);
                    if (this.villagerEntities.isPresent()) {
                        List<LivingEntity> currentVillagers = this.villagerEntities.get();
                        LivingEntity randomVillager = currentVillagers.get(world.random.nextInt(currentVillagers.size()));
                        if (randomVillager.distanceTo(entity) < 5.0F) {
                            this.currentTarget = (Villager) randomVillager;
                            entity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(randomVillager, true));
                            this.setWalkTarget(entity, randomVillager.blockPosition());
                        }
                    }
                }else {
                    if(!this.currentTarget.isLookingAtMe(entity, 0.025, true, false, currentTarget.getEyeY())) {
                        entity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(this.currentTarget, true));
                    }
                }
            }
        }else {
            if(this.currentTarget != null) {
                if(!this.currentTarget.isLookingAtMe(entity, 0.025, true, false, currentTarget.getEyeY())) {
                    entity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(this.currentTarget, true));
                }
                Projectile.spawnProjectileFromRotation(SnowballVillager::new, world, Items.SNOWBALL.getDefaultInstance().copy(), entity, 0.0F, (float)entity.leashDistanceTo(this.currentTarget)*0.5F, 0.5F);
                world.playSound(entity, entity.blockPosition(), SoundEvents.EGG_THROW, SoundSource.AMBIENT, 0.5F, 1.0F);
                this.currentTarget = null;
                this.snowballsRemaining--;
                this.ticksToNextSnowball = 40;
            }else {
                this.ticksToNextSnowball = 1;
            }
        }
    }

    private boolean isOnSnow(ServerLevel world, Mob entity) {
        BlockPos blockPos = entity.blockPosition();
        BlockPos blockPos2 = blockPos.below();
        return this.isSnow(world, blockPos) || this.isSnow(world, blockPos2);
    }

    private boolean isSnow(ServerLevel world, BlockPos pos) {
        return world.getBlockState(pos).is(BlockTags.SNOW);
    }

    private Optional<BlockPos> getNearestSnow(Mob mob) {
        return mob.getBrain().getMemory(BCMMemoryModules.NEAREST_SNOW_BLOCK);
    }

    private Optional<List<LivingEntity>> getVisibleVillagers(Mob mob) {
        return mob.getBrain().getMemory(MemoryModuleType.VISIBLE_VILLAGER_BABIES);
    }

    private void setWalkTarget(Mob mob, BlockPos pos) {
        mob.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(pos, this.walkSpeed, 0));
    }

    private boolean shouldStartSnowball(ServerLevel world, Mob mob) {
        return this.isOnSnow(world, mob) || this.getNearestSnow(mob).isPresent();
    }
}
