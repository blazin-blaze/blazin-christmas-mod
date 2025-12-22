package net.blazinblaze.mixin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.datafixers.util.Pair;
import net.blazinblaze.entity.task.SnowballFightTask;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.VillagerGoalPackages;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(VillagerGoalPackages.class)
public class SnowballFightTaskMixin {
    @ModifyReturnValue(method = "getPlayPackage", at = @At("RETURN"))
    private static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> changePlayTasks(ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> original) {
        List<Pair<Integer, ? extends BehaviorControl<? super Villager>>> tempList = new ArrayList<>(original);
        tempList.add(Pair.of(5, new SnowballFightTask(0.5F)));
        return ImmutableList.copyOf(tempList);
    }

    /*/@ModifyReturnValue(method = "createIdleTasks", at = @At("RETURN"))
    private static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> changeIdleTasks(ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> original) {
        List<Pair<Integer, ? extends Task<? super VillagerEntity>>> tempList = new ArrayList<>(original);
        tempList.add(Pair.of(2, new SnowballFightTask(0.5F)));
        return ImmutableList.copyOf(tempList);
    }/*/

    /*/@Unique
    private static Pair<Integer,? extends Task<? super VillagerEntity>> createFreeFollowTask() {
        return Pair.of(
                5,
                new RandomTask<>(
                        ImmutableList.of(
                                Pair.of(LookAtMobTask.create(EntityType.CAT, 8.0F), 8),
                                Pair.of(LookAtMobTask.create(EntityType.VILLAGER, 8.0F), 2),
                                Pair.of(LookAtMobTask.create(EntityType.PLAYER, 8.0F), 2),
                                Pair.of(LookAtMobTask.create(SpawnGroup.CREATURE, 8.0F), 1),
                                Pair.of(LookAtMobTask.create(SpawnGroup.WATER_CREATURE, 8.0F), 1),
                                Pair.of(LookAtMobTask.create(SpawnGroup.AXOLOTLS, 8.0F), 1),
                                Pair.of(LookAtMobTask.create(SpawnGroup.UNDERGROUND_WATER_CREATURE, 8.0F), 1),
                                Pair.of(LookAtMobTask.create(SpawnGroup.WATER_AMBIENT, 8.0F), 1),
                                Pair.of(LookAtMobTask.create(SpawnGroup.MONSTER, 8.0F), 1),
                                Pair.of(new WaitTask(30, 60), 2)
                        )
                )
        );
    }/*/
}
