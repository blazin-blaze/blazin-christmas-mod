package net.blazinblaze.advancement;

import net.blazinblaze.BlazinChristmasMod;
import net.minecraft.advancements.CriteriaTriggers;

public class BCMCriteria {
    public static final SpawnedSantaCriterion SPAWNED_SANTA = CriteriaTriggers.register(BlazinChristmasMod.MOD_ID + ":spawned_santa_criterion", new SpawnedSantaCriterion());
    public static final SpawnedEvilSantaCriterion SPAWNED_EVIL_SANTA = CriteriaTriggers.register(BlazinChristmasMod.MOD_ID + ":spawned_evil_santa_criterion", new SpawnedEvilSantaCriterion());
    public static final GiftedSantaCriterion GIFTED_SANTA = CriteriaTriggers.register(BlazinChristmasMod.MOD_ID + ":gifted_santa_criterion", new GiftedSantaCriterion());
    public static final SnowballLauncherCriterion SNOWBALL_LAUNCHER = CriteriaTriggers.register(BlazinChristmasMod.MOD_ID + ":snowball_launcher_criterion", new SnowballLauncherCriterion());
    public static final SignGiftCriterion SIGN_GIFT = CriteriaTriggers.register(BlazinChristmasMod.MOD_ID + ":sign_gift_criterion", new SignGiftCriterion());
    public static final SantaEatCriterion SANTA_EAT = CriteriaTriggers.register(BlazinChristmasMod.MOD_ID + ":santa_eat_criterion", new SantaEatCriterion());

    public static void init() {
    }

}
