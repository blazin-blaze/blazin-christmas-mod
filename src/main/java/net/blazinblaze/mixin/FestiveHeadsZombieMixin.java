package net.blazinblaze.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.blazinblaze.misc.RandomBCMProvider;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

@Mixin(Zombie.class)
public class FestiveHeadsZombieMixin {
    @Inject(method = "finalizeSpawn", at = @At(value = "INVOKE", target = "Ljava/time/LocalDate;get(Ljava/time/temporal/TemporalField;)I", shift = At.Shift.AFTER))
    private void addFestiveHeads(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, EntitySpawnReason entitySpawnReason, SpawnGroupData spawnGroupData, CallbackInfoReturnable<SpawnGroupData> cir, @Local LocalDate localDate, @Local RandomSource randomSource) {
        int day = localDate.get(ChronoField.DAY_OF_MONTH);
        int month = localDate.get(ChronoField.MONTH_OF_YEAR);
        if(!(month == 10 && day == 31)) {
            if(month == 12 && day == 25 ? randomSource.nextFloat() < 0.5F : randomSource.nextFloat() < 0.1F) {
                Block randomFestiveHead = RandomBCMProvider.getRandomFestiveHead();
                ((Zombie)(Object)this).setItemSlot(EquipmentSlot.HEAD, new ItemStack(randomFestiveHead));
                ((Zombie)(Object)this).setDropChance(EquipmentSlot.HEAD, 0.1F);
            }
        }
    }
}
