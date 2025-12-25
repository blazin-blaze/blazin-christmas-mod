package net.blazinblaze.entity.custom;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class Reindeer extends Llama {
    private static final EntityDataAccessor<Boolean> SPECIAL_RED = SynchedEntityData.defineId(Reindeer.class, EntityDataSerializers.BOOLEAN);

    public Reindeer(EntityType<? extends Llama> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, EntitySpawnReason entitySpawnReason, @Nullable SpawnGroupData spawnGroupData) {
        RandomSource random = this.getRandom();
        if(random.nextFloat() < 0.1) {
            this.setSpecialRed(true);
        }
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, entitySpawnReason, spawnGroupData);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        valueOutput.putBoolean("isSpecialRed", getSpecialRed());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        RandomSource random = this.getRandom();
        setSpecialRed(valueInput.getBooleanOr("isSpecialRed", random.nextFloat() < 0.1));
    }

    public void setSpecialRed(boolean specialRed) {
        this.entityData.set(SPECIAL_RED, specialRed);
    }

    public boolean getSpecialRed() {
        return this.entityData.get(SPECIAL_RED);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SPECIAL_RED, false);
    }
}
