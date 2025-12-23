package net.blazinblaze.entity.custom;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class GingerbreadMan extends Zombie {
    public GingerbreadMan(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.GRASS_HIT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CROP_BREAK;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.CROP_PLANTED;
    }

    @Override
    public boolean isUnderWaterConverting() {
        return false;
    }
}
