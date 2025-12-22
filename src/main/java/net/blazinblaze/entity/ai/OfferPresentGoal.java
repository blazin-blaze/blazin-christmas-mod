package net.blazinblaze.entity.ai;

import net.blazinblaze.entity.custom.SantaVillager;
import net.blazinblaze.misc.RandomBCMProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;

public class OfferPresentGoal extends Goal {

    private final SantaVillager santaVillager;
    private Villager babyVillager;
    private int tick;

    public OfferPresentGoal(SantaVillager villager) {
        this.santaVillager = villager;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!this.santaVillager.level().isBrightOutside()) {
            return false;
        } else if (this.santaVillager.getRandom().nextInt(8000) != 0) {
            return false;
        } else {
            this.babyVillager = getServerLevel(this.santaVillager)
                    .getNearestEntity(
                            Villager.class,
                            TargetingConditions.forNonCombat().range(6.0),
                            this.santaVillager,
                            this.santaVillager.getX(),
                            this.santaVillager.getY(),
                            this.santaVillager.getZ(),
                            this.santaVillager.getBoundingBox().inflate(6.0, 2.0, 6.0)
                    );
            return this.babyVillager != null && this.babyVillager.isBaby();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.tick > 0;
    }

    @Override
    public void start() {
        this.tick = this.adjustedTickDelay(400);
        this.santaVillager.setItemSlot(EquipmentSlot.MAINHAND, RandomBCMProvider.getRandomPresentWithoutLoot());
    }

    @Override
    public void stop() {
        this.santaVillager.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        this.babyVillager = null;
    }

    @Override
    public void tick() {
        this.santaVillager.getLookControl().setLookAt(this.babyVillager, 30.0F, 30.0F);
        this.tick--;
    }
}
