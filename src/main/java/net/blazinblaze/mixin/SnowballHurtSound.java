package net.blazinblaze.mixin;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.projectile.Snowball;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class SnowballHurtSound {
    @Inject(method = "playHurtSound", at = @At("HEAD"), cancellable = true)
    private void changeSnowballHurt(DamageSource damageSource, CallbackInfo ci) {
        LivingEntity currentEntity = (LivingEntity)(Object)this;
        Entity attacker = damageSource.getEntity();
        Entity source = damageSource.getDirectEntity();
        if(currentEntity instanceof Villager villager1 && attacker instanceof Villager villager2 && source instanceof Snowball) {
            if(villager1.isBaby() && villager2.isBaby()) {
                currentEntity.makeSound(SoundEvents.SNOW_BREAK);
                ci.cancel();
            }
        }
    }
}
