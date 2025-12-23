package net.blazinblaze.item.custom;

import net.blazinblaze.BlazinChristmasMod;
import net.blazinblaze.advancement.BCMCriteria;
import net.blazinblaze.component.BCMComponents;
import net.blazinblaze.entity.custom.CandyCaneSnowballEntity;
import net.blazinblaze.entity.custom.EtherealSnowballEntity;
import net.blazinblaze.entity.custom.FierySnowballEntity;
import net.blazinblaze.entity.custom.WitheredSnowballEntity;
import net.blazinblaze.item.BCMItems;
import net.blazinblaze.item.tags.BCMItemTags;
import net.blazinblaze.misc.SnowballPlaySound;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class SnowballStaff extends ProjectileWeaponItem {
    public SnowballStaff(Properties settings) {
        super(settings);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return (itemStack -> itemStack.is(BCMItemTags.SNOWBALL_LAUNCHABLE));
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }

    @Override
    protected Projectile createProjectile(Level level, LivingEntity livingEntity, ItemStack itemStack, ItemStack itemStack2, boolean bl) {
        if(itemStack2.is(BCMItems.CANDY_CANE_SNOWBALL)) {
            return new CandyCaneSnowballEntity(level, livingEntity, itemStack2.copyWithCount(1));
        }else if(itemStack2.is(BCMItems.WITHERED_SNOWBALL)) {
            return new WitheredSnowballEntity(level, livingEntity, itemStack2.copyWithCount(1));
        }else if(itemStack2.is(BCMItems.ETHEREAL_SNOWBALL)) {
            return new EtherealSnowballEntity(level, livingEntity, itemStack2.copyWithCount(1));
        }else if(itemStack2.is(BCMItems.FIERY_SNOWBALL)) {
            return new FierySnowballEntity(level, livingEntity, itemStack2.copyWithCount(1));
        }else {
            return new Snowball(level, livingEntity, itemStack2.copyWithCount(1));
        }
    }

    @Override
    protected void shoot(ServerLevel serverLevel, LivingEntity livingEntity, InteractionHand interactionHand, ItemStack itemStack, List<ItemStack> list, float f, float g, boolean bl, @Nullable LivingEntity livingEntity2) {
        float h = EnchantmentHelper.processProjectileSpread(serverLevel, itemStack, livingEntity, 0.0F);
        float i = list.size() == 1 ? 0.0F : 2.0F * h / (list.size() - 1);
        float j = (list.size() - 1) % 2 * i / 2.0F;
        float k = 1.0F;

        for (int l = 0; l < list.size(); l++) {
            ItemStack itemStack2 = (ItemStack)list.get(l);
            if (!itemStack2.isEmpty()) {
                float m = j + k * ((l + 1) / 2) * i;
                k = -k;
                int n = l;
                Projectile.spawnProjectile(
                        this.createProjectile(serverLevel, livingEntity, itemStack, itemStack2, bl),
                        serverLevel,
                        itemStack2,
                        projectile -> this.shootProjectile(livingEntity, projectile, n, f, g, m, livingEntity2)
                );
                itemStack.hurtAndBreak(this.getDurabilityUse(itemStack2), livingEntity, LivingEntity.getSlotForHand(interactionHand));
                if (itemStack.isEmpty()) {
                    break;
                }
            }
        }
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
                            if(list.size() >= 3) {
                                this.shoot(serverLevel, player, player.getUsedItemHand(), itemStack, list, f * 3.0F, 1.0F, f == 1.0F, (LivingEntity)null);
                                SnowballPlaySound.INSTANCE.setSnowballTimer(5L, 2, player, level, f);
                            }else if(list.size() >= 2) {
                                this.shoot(serverLevel, player, player.getUsedItemHand(), itemStack, list, f * 3.0F, 1.0F, f == 1.0F, (LivingEntity)null);
                                SnowballPlaySound.INSTANCE.setSnowballTimer(5L, 1, player, level, f);
                            }else {
                                this.shoot(serverLevel, player, player.getUsedItemHand(), itemStack, list, f * 3.0F, 1.0F, f == 1.0F, (LivingEntity)null);
                                SnowballPlaySound.INSTANCE.setSnowballTimer(5L, 0, player, level, f);
                            }
                        }
                        BCMCriteria.SNOWBALL_LAUNCHER.trigger((ServerPlayer)player);
                    }

                    itemStack.set(BCMComponents.IS_SNOWBALL_LOADED, false);
                    return true;
                }
            }
        }
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        boolean bl = !player.getProjectile(itemStack).isEmpty();
        if(!bl && !player.hasInfiniteMaterials()) {
            return InteractionResult.FAIL;
        }else {
            ItemStack itemStack2 = player.getProjectile(itemStack);
            player.startUsingItem(interactionHand);
            if(itemStack2.getItem() instanceof CandyCaneSnowball) {
                itemStack.set(BCMComponents.SNOWBALL_STAFF_TYPE, "candy_cane");
            }else if(itemStack2.getItem() instanceof WitheredSnowball) {
                itemStack.set(BCMComponents.SNOWBALL_STAFF_TYPE, "withered");
            }else if(itemStack2.getItem() instanceof EtherealSnowball) {
                itemStack.set(BCMComponents.SNOWBALL_STAFF_TYPE, "ethereal");
            }else if(itemStack2.getItem() instanceof FierySnowball) {
                itemStack.set(BCMComponents.SNOWBALL_STAFF_TYPE, "fiery");
            }else {
                itemStack.set(BCMComponents.SNOWBALL_STAFF_TYPE, "normal");
            }
            itemStack.set(BCMComponents.IS_SNOWBALL_LOADED, true);
            return InteractionResult.CONSUME;
        }
    }

    public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
        return ItemUseAnimation.BOW;
    }

    public int getUseDuration(ItemStack itemStack, LivingEntity livingEntity) {
        return 18000;
    }

    protected static List<ItemStack> draw(ItemStack itemStack, ItemStack itemStack2, LivingEntity livingEntity) {
        if (itemStack2.isEmpty()) {
            return List.of();
        }else if(livingEntity.hasInfiniteMaterials()) {
            return List.of(itemStack2.copy(), itemStack2.copy(), itemStack2.copy());
        } else {
            int i = Math.min(itemStack2.getCount(), 3);
            List<ItemStack> list = new ArrayList(i);
            ItemStack itemStack3 = itemStack2.copy();

            for (int j = 0; j < i; j++) {
                ItemStack itemStack4 = useAmmo(itemStack, j == 0 ? itemStack2 : itemStack3, livingEntity, j > 0);
                if (!itemStack4.isEmpty()) {
                    list.add(itemStack4);
                }
            }

            return list;
        }
    }

    protected static ItemStack useAmmo(ItemStack itemStack, ItemStack itemStack2, LivingEntity livingEntity, boolean bl) {
        int i = itemStack2.getCount() >= 3 || livingEntity.hasInfiniteMaterials() ? 3 : itemStack2.getCount();
        if (i > itemStack2.getCount()) {
            return ItemStack.EMPTY;
        } else if (i == 0) {
            ItemStack itemStack3 = itemStack2.copyWithCount(1);
            itemStack3.set(DataComponents.INTANGIBLE_PROJECTILE, Unit.INSTANCE);
            return itemStack3;
        } else {
            ItemStack itemStack3;
            if(!livingEntity.hasInfiniteMaterials()) {
                itemStack3 = itemStack2.split(i);
            }else {
                itemStack3 = itemStack2.copy();
            }
            if (itemStack2.isEmpty() && livingEntity instanceof Player player) {
                player.getInventory().removeItem(itemStack2);
            }

            return itemStack3;
        }
    }
}
