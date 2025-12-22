package net.blazinblaze.entity.custom;

import net.blazinblaze.data.BCMAttachmentTypes;
import net.blazinblaze.data.EvilRemovedAttachmentData;
import net.blazinblaze.entity.BCMEntities;
import net.blazinblaze.item.BCMItems;
import net.blazinblaze.misc.RandomBCMProvider;
import net.blazinblaze.sound.BCMSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.Nullable;

public class EvilSantaVillager extends SpellcasterIllager {

    private static final EntityDataAccessor<Integer> INVINCIBLE = SynchedEntityData.defineId(EvilSantaVillager.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DAMAGE_INTERVAL = SynchedEntityData.defineId(EvilSantaVillager.class, EntityDataSerializers.FLOAT);
    private static final int CURRENT_INVINCIBLE_TICKS = 220;
    private static final int DEFAULT_INVULNERABLE_TICKS = 0;

    private final ServerBossEvent bossEvent = (ServerBossEvent)new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(false);

    public EvilSantaVillager(EntityType<? extends Monster> entityType, Level world) {
        super(BCMEntities.EVIL_SANTA_VILLAGER, world);
        this.setHealth(this.getMaxHealth());
        this.xpReward = 50;
        this.setInvulnerableTicks(220);
    }

    @Override
    protected void registerGoals() {
        //super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new EvilSantaAttackBeginGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal(this, Player.class, 8.0F, 0.6, 1.0));
        this.goalSelector.addGoal(4, new EvilSantaSnowballAttackGoal());
        this.goalSelector.addGoal(5, new EvilSantaHealGoal());
        this.goalSelector.addGoal(7, new EvilSantaSnowballSpiralAttackGoal());
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true).setUnseenMemoryTicks(300));
    }

    public static AttributeSupplier.Builder createEvilSantaAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 300.0)
                .add(Attributes.MOVEMENT_SPEED, 0.5F)
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.ARMOR, 4.0);
    }

    @Override
    protected void customServerAiStep(ServerLevel world) {
        if (this.getInvulnerableTicks() > 0) {
            this.setInvulnerableTicks(this.getInvulnerableTicks() - 1);
            this.bossEvent.setColor(BossEvent.BossBarColor.RED);
        }else {
            this.setInvulnerableTicks(this.getInvulnerableTicks() - 1);
            this.bossEvent.setColor(BossEvent.BossBarColor.GREEN);
            if(this.getInvulnerableTicks() <= (-20*10)) {
                this.setInvulnerableTicks(220);
                this.setDamageInterval(0);
            }
        }
        if(this.getDamageInterval() >= this.getMaxHealth() / 5) {
            this.setInvulnerableTicks(220);
            this.setDamageInterval(0);
        }
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        super.customServerAiStep(world);
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    @Override
    public void applyRaidBuffs(ServerLevel world, int wave, boolean unused) {
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return null;
    }

    @Override
    public boolean canUsePortal(boolean allowVehicles) {
        return false;
    }

    @Override
    protected boolean canRide(Entity entity) {
        return false;
    }

    @Override
    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        this.bossEvent.addPlayer(serverPlayer);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        super.stopSeenByPlayer(serverPlayer);
        this.bossEvent.removePlayer(serverPlayer);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        valueOutput.putInt("Invul", this.getInvulnerableTicks());
        valueOutput.putFloat("damage_interval", this.getDamageInterval());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        this.setInvulnerableTicks(valueInput.getIntOr("Invul", 0));
        this.setDamageInterval(valueInput.getFloatOr("damage_interval", 0));
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float f) {
        if (this.isInvulnerableTo(serverLevel, damageSource)) {
            return false;
        } else if (this.getInvulnerableTicks() > 0 && !damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        }else {
            this.setDamageInterval(this.getDamageInterval() + f);
            return super.hurtServer(serverLevel, damageSource, f);
        }
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if(this.level() instanceof ServerLevel serverLevel) {
            if(!serverLevel.getAttachedOrCreate(BCMAttachmentTypes.EVIL_REMOVED_ATTACHMENT_TYPE, () -> EvilRemovedAttachmentData.DEFAULT).aBoolean()) {
                serverLevel.setAttached(BCMAttachmentTypes.EVIL_REMOVED_ATTACHMENT_TYPE, new EvilRemovedAttachmentData(true));
                if(this.getServer() != null) {
                    MinecraftServer server = this.getServer();
                    ServerLevel overworld = server.overworld();
                    overworld.setAttached(BCMAttachmentTypes.EVIL_REMOVED_ATTACHMENT_TYPE, new EvilRemovedAttachmentData(true));
                    for(Player player : this.getServer().getPlayerList().getPlayers()) {
                        player.displayClientMessage(Component.literal("The spirits of Christmas joy have been released!").withStyle(ChatFormatting.LIGHT_PURPLE).withStyle(ChatFormatting.ITALIC), false);
                        player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                    }
                }
            }
        }
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource damageSource, boolean bl) {
        super.dropCustomDeathLoot(serverLevel, damageSource, bl);
        ItemEntity itemEntity = this.spawnAtLocation(serverLevel, BCMItems.SNOWBALL_STAFF);
        if (itemEntity != null) {
            itemEntity.setExtendedLifetime();
        }
    }
    @Override
    public void checkDespawn() {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
            this.discard();
        }
    }

    public int getInvulnerableTicks() {
        return this.entityData.get(INVINCIBLE);
    }

    public void setInvulnerableTicks(int i) {
        this.entityData.set(INVINCIBLE, i);
    }

    public float getDamageInterval() {
        return this.entityData.get(DAMAGE_INTERVAL);
    }

    public void setDamageInterval(float i) {
        this.entityData.set(DAMAGE_INTERVAL, i);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(INVINCIBLE, 0);
        builder.define(DAMAGE_INTERVAL, 0F);
    }

    class EvilSantaSnowballAttackGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
        @Override
        protected int getCastingTime() {
            return 20;
        }

        @Override
        protected int getCastingInterval() {
            return 60;
        }

        @Override
        protected void performSpellCasting() {
            LivingEntity livingEntity = EvilSantaVillager.this.getTarget();
            double d = livingEntity.getX() - EvilSantaVillager.this.getX();
            double e = livingEntity.getEyeY() - 1.1F;
            double g = livingEntity.getZ() - EvilSantaVillager.this.getZ();
            double h = Math.sqrt(d * d + g * g) * 0.2F;
            float power = EvilSantaVillager.this.distanceTo(livingEntity) * .15F;
            if (EvilSantaVillager.this.level() instanceof ServerLevel serverLevel) {
                RandomBCMProvider.createRandomSnowball(EvilSantaVillager.this, serverLevel, d, e, g, h, power, true);
            }
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected SpellcasterIllager.IllagerSpell getSpell() {
            return SpellcasterIllager.IllagerSpell.FANGS;
        }
    }

    class EvilSantaSnowballSpiralAttackGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
        @Override
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else {
                return EvilSantaVillager.this.getHealth() < (EvilSantaVillager.this.getMaxHealth() / 2);
            }
        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected int getCastingInterval() {
            return 150;
        }

        @Override
        protected void performSpellCasting() {
            LivingEntity livingEntity = EvilSantaVillager.this.getTarget();
            double d = livingEntity.getX() - EvilSantaVillager.this.getX();
            double e = livingEntity.getEyeY() - 1.1F;
            double g = livingEntity.getZ() - EvilSantaVillager.this.getZ();
            double h = Math.sqrt(d * d + g * g) * 0.2F;
            if (EvilSantaVillager.this.level() instanceof ServerLevel serverLevel) {
                RandomBCMProvider.createRandomSnowballSpiral(EvilSantaVillager.this, serverLevel, d, e, g, h, false);
            }
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected SpellcasterIllager.IllagerSpell getSpell() {
            return IllagerSpell.SUMMON_VEX;
        }
    }

    class EvilSantaAttackBeginGoal extends SpellcasterIllager.SpellcasterCastingSpellGoal {
        @Override
        public void tick() {
            if (EvilSantaVillager.this.getTarget() != null) {
                EvilSantaVillager.this.getLookControl().setLookAt(EvilSantaVillager.this.getTarget(), EvilSantaVillager.this.getMaxHeadYRot(), EvilSantaVillager.this.getMaxHeadXRot());
            }
        }
    }

    class EvilSantaHealGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
        @Override
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else {
                return EvilSantaVillager.this.getHealth() < (EvilSantaVillager.this.getMaxHealth() / 2) && EvilSantaVillager.this.getInvulnerableTicks() <= 0;
            }
        }

        @Override
        protected int getCastingTime() {
            return 50;
        }

        @Override
        protected int getCastingInterval() {
            return 360;
        }

        @Override
        protected void performSpellCasting() {
            ServerLevel serverLevel = (ServerLevel)EvilSantaVillager.this.level();
            EvilSantaVillager.this.heal(10 + serverLevel.getRandom().nextFloat()*5);
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.WANDERING_TRADER_DRINK_POTION;
        }

        @Override
        protected SpellcasterIllager.IllagerSpell getSpell() {
            return IllagerSpell.WOLOLO;
        }
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return BCMSounds.EVIL_SANTA_VILLAGER_AMBIENT;
    }
}
