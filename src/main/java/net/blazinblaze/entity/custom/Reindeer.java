package net.blazinblaze.entity.custom;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class Reindeer extends Llama {
    private boolean isSpecialRed;

    public Reindeer(EntityType<? extends Llama> entityType, Level level) {
        super(entityType, level);
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
        this.isSpecialRed = specialRed;
    }

    public boolean getSpecialRed() {
        return this.isSpecialRed;
    }
}
