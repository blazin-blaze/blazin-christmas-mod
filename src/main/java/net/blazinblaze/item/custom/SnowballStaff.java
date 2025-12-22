package net.blazinblaze.item.custom;

import net.blazinblaze.component.BCMComponents;
import net.blazinblaze.item.BCMItems;
import net.blazinblaze.item.tags.BCMItemTags;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class SnowballStaff extends ProjectileWeaponItem {
    public SnowballStaff(Properties settings) {
        super(settings);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return (itemStack) -> {
            String type = null;
            for(TypedDataComponent<?> componentType : itemStack.getComponents()) {
                if(componentType.type().equals(BCMComponents.SNOWBALL_STAFF_TYPE)) {
                    type = (String)componentType.value();
                }
            }
            if(type != null) {
                if(type.matches("normal")) {
                    return itemStack.is(Items.SNOWBALL);
                }else if(type.matches("fiery")) {
                    return itemStack.is(BCMItems.FIERY_SNOWBALL);
                }else if(type.matches("withered")) {
                    return itemStack.is(BCMItems.WITHERED_SNOWBALL);
                }else if(type.matches("ethereal")) {
                    return itemStack.is(BCMItems.ETHEREAL_SNOWBALL);
                }else if(type.matches("candy_cane")) {
                    return itemStack.is(BCMItems.CANDY_CANE);
                }else {
                    return itemStack.is(Items.SNOWBALL);
                }
            }else {
                return itemStack.is(Items.SNOWBALL);
            }
        };
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }

    @Override
    protected void shootProjectile(LivingEntity livingEntity, Projectile projectile, int i, float f, float g, float h, @Nullable LivingEntity livingEntity2) {
        projectile.shootFromRotation(livingEntity, livingEntity.getXRot(), livingEntity.getYRot() + h, 0.0F, f, g);
    }

    public static float getPowerForTime(int i) {
        float f = (float)i / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int i) {
        if (!(livingEntity instanceof Player player)) {
            return false;
        } else {
            ItemStack itemStack2 = player.getProjectile(itemStack);
            if (itemStack2.isEmpty()) {
                return false;
            } else {
                int j = this.getUseDuration(itemStack, livingEntity) - i;
                float f = getPowerForTime(j);
                if ((double)f < 0.1) {
                    return false;
                } else {
                    List<ItemStack> list = draw(itemStack, itemStack2, player);
                    if (level instanceof ServerLevel) {
                        ServerLevel serverLevel = (ServerLevel)level;
                        if (!list.isEmpty()) {
                            this.shoot(serverLevel, player, player.getUsedItemHand(), itemStack, list, f * 3.0F, 1.0F, f == 1.0F, (LivingEntity)null);
                        }
                    }

                    level.playSound((Entity)null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    return true;
                }
            }
        }
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        if(player.isCrouching()) {
            ItemStack itemStack = player.getItemInHand(interactionHand);
            String type = null;
            boolean isFull = false;
            for(TypedDataComponent<?> componentType : itemStack.getComponents()) {
                if(componentType.type().equals(BCMComponents.SNOWBALL_STAFF_TYPE)) {
                    type = (String)componentType.value();
                }else if(componentType.type().equals(BCMComponents.IS_SNOWBALL_LOADED)) {
                    isFull = (boolean) componentType.value();
                }
            }
            if(type != null) {
                String nextType = switch (type) {
                    case "candy_cane" -> "fiery";
                    case "fiery" -> "withered";
                    case "withered" -> "ethereal";
                    case "ethereal" -> "normal";
                    case "normal" -> "candy_cane";
                    default -> "normal";
                };
                itemStack.applyComponents(DataComponentMap.builder().set(BCMComponents.SNOWBALL_STAFF_TYPE, nextType).set(BCMComponents.IS_SNOWBALL_LOADED, isFull).build());
                return InteractionResult.SUCCESS;
            }else {
                return InteractionResult.FAIL;
            }
        }else {
            ItemStack itemStack = player.getItemInHand(interactionHand);
            boolean bl = !player.getProjectile(itemStack).isEmpty();
            if(!bl && !player.hasInfiniteMaterials()) {
                return InteractionResult.FAIL;
            }else {
                player.startUsingItem(interactionHand);
                return InteractionResult.CONSUME;
            }
        }
    }

    public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
        return ItemUseAnimation.BOW;
    }

    public int getUseDuration(ItemStack itemStack, LivingEntity livingEntity) {
        return 18000;
    }

}
