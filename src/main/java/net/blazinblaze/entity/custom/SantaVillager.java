package net.blazinblaze.entity.custom;

import net.blazinblaze.entity.BCMEntities;
import net.blazinblaze.entity.ai.AddPresentsGoal;
import net.blazinblaze.entity.ai.EatCookiesGoal;
import net.blazinblaze.entity.ai.OfferPresentGoal;
import net.blazinblaze.sound.BCMSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.DefendVillageTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class SantaVillager extends PathfinderMob {

    private static final EntityDataAccessor<Integer> COOKIES_EATEN = SynchedEntityData.defineId(SantaVillager.class, EntityDataSerializers.INT);
    @Nullable
    private BlockPos wanderTarget;
    private int despawnDelay = 0;

    public SantaVillager(EntityType<?> type, Level world) {
        super(BCMEntities.SANTA_VILLAGER, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector
                .addGoal(
                        0,
                        new UseItemGoal<>(
                                this,
                                PotionContents.createItemStack(Items.POTION, Potions.INVISIBILITY),
                                SoundEvents.GENERIC_DRINK.value(),
                                wanderingTrader -> this.level().isDarkOutside() && !wanderingTrader.isInvisible()
                        )
                );
        this.goalSelector
                .addGoal(
                        0,
                        new UseItemGoal<>(
                                this,
                                new ItemStack(Items.MILK_BUCKET),
                                SoundEvents.GENERIC_DRINK.value(),
                                wanderingTrader -> this.level().isBrightOutside() && wanderingTrader.isInvisible()
                        )
                );
        this.goalSelector.addGoal(1, new AvoidEntityGoal(this, Zombie.class, 8.0F, 0.5, 0.5));
        this.goalSelector.addGoal(1, new AvoidEntityGoal(this, Evoker.class, 12.0F, 0.5, 0.5));
        this.goalSelector.addGoal(1, new AvoidEntityGoal(this, Vindicator.class, 8.0F, 0.5, 0.5));
        this.goalSelector.addGoal(1, new AvoidEntityGoal(this, Vex.class, 8.0F, 0.5, 0.5));
        this.goalSelector.addGoal(1, new AvoidEntityGoal(this, Pillager.class, 15.0F, 0.5, 0.5));
        this.goalSelector.addGoal(1, new AvoidEntityGoal(this, Illusioner.class, 12.0F, 0.5, 0.5));
        this.goalSelector.addGoal(1, new AvoidEntityGoal(this, Zoglin.class, 10.0F, 0.5, 0.5));
        this.goalSelector.addGoal(1, new AvoidEntityGoal(this, EvilSantaVillager.class, 10.0F, 0.5, 0.5));
        this.goalSelector.addGoal(1, new PanicGoal(this, 0.5));
        this.goalSelector.addGoal(2, new WanderToPositionGoal(this, 2.0, 0.35));
        this.goalSelector.addGoal(2, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(3, new AddPresentsGoal(this, 0.5F, 8));
        this.goalSelector.addGoal(4, new EatCookiesGoal(this, 0.5F, 8));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.35));
        this.goalSelector.addGoal(6, new OfferPresentGoal(this));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.5);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        valueOutput.putInt("cookies_eaten", getCookiesEaten());
        valueOutput.putInt("DespawnDelay", this.despawnDelay);
        valueOutput.storeNullable("wander_target", BlockPos.CODEC, this.wanderTarget);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        setCookiesEaten(valueInput.getIntOr("cookies_eaten", 0));
        this.despawnDelay = valueInput.getIntOr("DespawnDelay", 0);
        this.wanderTarget = (BlockPos)valueInput.read("wander_target", BlockPos.CODEC).orElse(null);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(COOKIES_EATEN, 0);
    }

    public int getCookiesEaten() {
        return this.entityData.get(COOKIES_EATEN);
    }

    public void setCookiesEaten(int cookiesEaten) {
        this.entityData.set(COOKIES_EATEN, cookiesEaten);
    }

    public void setDespawnDelay(int i) {
        this.despawnDelay = i;
    }

    public int getDespawnDelay() {
        return this.despawnDelay;
    }

    public void setWanderTarget(@Nullable BlockPos blockPos) {
        this.wanderTarget = blockPos;
    }

    @Nullable
    BlockPos getWanderTarget() {
        return this.wanderTarget;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return BCMSounds.SANTA_VILLAGER_AMBIENT;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.VILLAGER_HURT;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }

    @Override
    public boolean removeWhenFarAway(double d) {
        return false;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            this.maybeDespawn();
        }
    }

    private void maybeDespawn() {
        if (this.despawnDelay > 0 && --this.despawnDelay == 0) {
            this.discard();
        }
    }

    class WanderToPositionGoal extends Goal {
        final SantaVillager santaVillager;
        final double stopDistance;
        final double speedModifier;

        WanderToPositionGoal(final SantaVillager santaVillager, final double d, final double e) {
            this.santaVillager = santaVillager;
            this.stopDistance = d;
            this.speedModifier = e;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public void stop() {
            this.santaVillager.setWanderTarget(null);
            SantaVillager.this.navigation.stop();
        }

        @Override
        public boolean canUse() {
            BlockPos blockPos = this.santaVillager.getWanderTarget();
            return blockPos != null && this.isTooFarAway(blockPos, this.stopDistance);
        }

        @Override
        public void tick() {
            BlockPos blockPos = this.santaVillager.getWanderTarget();
            if (blockPos != null && SantaVillager.this.navigation.isDone()) {
                if (this.isTooFarAway(blockPos, 10.0)) {
                    Vec3 vec3 = new Vec3(blockPos.getX() - this.santaVillager.getX(), blockPos.getY() - this.santaVillager.getY(), blockPos.getZ() - this.santaVillager.getZ()).normalize();
                    Vec3 vec32 = vec3.scale(10.0).add(this.santaVillager.getX(), this.santaVillager.getY(), this.santaVillager.getZ());
                    SantaVillager.this.navigation.moveTo(vec32.x, vec32.y, vec32.z, this.speedModifier);
                } else {
                    SantaVillager.this.navigation.moveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), this.speedModifier);
                }
            }
        }

        private boolean isTooFarAway(BlockPos blockPos, double d) {
            return !blockPos.closerToCenterThan(this.santaVillager.position(), d);
        }
    }
}
