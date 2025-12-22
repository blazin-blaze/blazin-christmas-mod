package net.blazinblaze.entity.custom;

import net.blazinblaze.entity.BCMEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class FriendlyElfVex extends Vex {
    public FriendlyElfVex(EntityType<? extends Vex> entityType, Level world) {
        super(BCMEntities.ELF_VEX, world);
    }

    private boolean hasLimitedLife;
    private int limitedLifeTicks;

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(4, new VexChargeAttackGoal());
        this.goalSelector.addGoal(8, new VexRandomMoveGoal());
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[]{Raider.class})).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Monster.class, true));
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        valueInput.getInt("life_ticks").ifPresentOrElse(this::setLimitedLifeElf, () -> this.hasLimitedLife = false);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        if (this.hasLimitedLife) {
            valueOutput.putInt("life_ticks", this.limitedLifeTicks);
        }
    }

    public void setLimitedLifeElf(int i) {
        this.hasLimitedLife = true;
        this.limitedLifeTicks = i;
    }

    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);
        if (this.hasLimitedLife && --this.limitedLifeTicks <= 0) {
            this.limitedLifeTicks = 20;
            this.hurt(this.damageSources().starve(), 1.0F);
        }

    }

    class VexRandomMoveGoal extends Goal {
        public VexRandomMoveGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return !FriendlyElfVex.this.getMoveControl().hasWanted() && FriendlyElfVex.this.random.nextInt(reducedTickDelay(7)) == 0;
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            BlockPos blockPos = FriendlyElfVex.this.getBoundOrigin();
            if (blockPos == null) {
                blockPos = FriendlyElfVex.this.blockPosition();
            }

            for(int i = 0; i < 3; ++i) {
                BlockPos blockPos2 = blockPos.offset(FriendlyElfVex.this.random.nextInt(15) - 7, FriendlyElfVex.this.random.nextInt(11) - 5, FriendlyElfVex.this.random.nextInt(15) - 7);
                if (FriendlyElfVex.this.level().isEmptyBlock(blockPos2)) {
                    FriendlyElfVex.this.moveControl.setWantedPosition((double)blockPos2.getX() + (double)0.5F, (double)blockPos2.getY() + (double)0.5F, (double)blockPos2.getZ() + (double)0.5F, (double)0.25F);
                    if (FriendlyElfVex.this.getTarget() == null) {
                        FriendlyElfVex.this.getLookControl().setLookAt((double)blockPos2.getX() + (double)0.5F, (double)blockPos2.getY() + (double)0.5F, (double)blockPos2.getZ() + (double)0.5F, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }

    class VexChargeAttackGoal extends Goal {
        public VexChargeAttackGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            LivingEntity livingEntity = FriendlyElfVex.this.getTarget();
            if (livingEntity != null && livingEntity.isAlive() && !FriendlyElfVex.this.getMoveControl().hasWanted() && FriendlyElfVex.this.random.nextInt(reducedTickDelay(7)) == 0) {
                return FriendlyElfVex.this.distanceToSqr(livingEntity) > (double)4.0F;
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return FriendlyElfVex.this.getMoveControl().hasWanted() && FriendlyElfVex.this.isCharging() && FriendlyElfVex.this.getTarget() != null && FriendlyElfVex.this.getTarget().isAlive();
        }

        public void start() {
            LivingEntity livingEntity = FriendlyElfVex.this.getTarget();
            if (livingEntity != null) {
                Vec3 vec3 = livingEntity.getEyePosition();
                FriendlyElfVex.this.moveControl.setWantedPosition(vec3.x, vec3.y, vec3.z, (double)1.0F);
            }

            FriendlyElfVex.this.setIsCharging(true);
            FriendlyElfVex.this.playSound(SoundEvents.VEX_CHARGE, 1.0F, 1.0F);
        }

        public void stop() {
            FriendlyElfVex.this.setIsCharging(false);
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingEntity = FriendlyElfVex.this.getTarget();
            if (livingEntity != null) {
                if (FriendlyElfVex.this.getBoundingBox().intersects(livingEntity.getBoundingBox())) {
                    FriendlyElfVex.this.doHurtTarget(getServerLevel(FriendlyElfVex.this.level()), livingEntity);
                    FriendlyElfVex.this.setIsCharging(false);
                } else {
                    double d = FriendlyElfVex.this.distanceToSqr(livingEntity);
                    if (d < (double)9.0F) {
                        Vec3 vec3 = livingEntity.getEyePosition();
                        FriendlyElfVex.this.moveControl.setWantedPosition(vec3.x, vec3.y, vec3.z, (double)1.0F);
                    }
                }

            }
        }
    }
}
