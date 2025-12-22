package net.blazinblaze.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Biome.class)
public abstract class SnowEverywhereMixin {
    @Shadow protected abstract int getGrassColorFromTexture();

    @Shadow protected abstract int getFoliageColorFromTexture();

    @ModifyReturnValue(method = "coldEnoughToSnow", at = @At("RETURN"))
    private boolean alwaysCold(boolean original) {
        return true;
    }

    @ModifyReturnValue(method = "warmEnoughToRain", at = @At("RETURN"))
    private boolean alwaysSnows(boolean original) {
        return false;
    }

    @ModifyReturnValue(method = "hasPrecipitation", at = @At("RETURN"))
    private boolean alwaysPrecipitates(boolean original) {
        return true;
    }

    /*/@Inject(method = "getGrassColor", at = @At("RETURN"), cancellable = true)
    private void setSnowGrassColor(CallbackInfoReturnable<Integer> cir) {
        Biome biome = (Biome)(Object)this;
        Optional<Integer> optional = biome.getEffects().getGrassColor();
        optional.ifPresent(integer -> cir.setReturnValue(SnowColorChanger.getColorForMultiplier(integer)));
        cir.setReturnValue(SnowColorChanger.getColorForMultiplier(getDefaultGrassColor()));
    }/*/

    /*/@Inject(method = "getFoliageColor", at = @At("RETURN"), cancellable = true)
    private void setSnowFoliageColor(CallbackInfoReturnable<Integer> cir) {
        Biome biome = (Biome)(Object)this;
        Optional<Integer> optional = biome.getEffects().getFoliageColor();
        optional.ifPresent(integer -> cir.setReturnValue(SnowColorChanger.getColorForMultiplier(integer)));
        cir.setReturnValue(SnowColorChanger.getColorForMultiplier(getDefaultFoliageColor()));
    }/*/
}
