package net.blazinblaze.entity.custom;

import net.blazinblaze.entity.BCMEntities;
import net.blazinblaze.item.BCMItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class FierySnowballEntity extends ThrowableItemProjectile {
    public FierySnowballEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
        super(entityType, world);
    }

    public FierySnowballEntity(Level world, LivingEntity owner, ItemStack stack) {
        super(BCMEntities.FIERY_SNOWBALL_ENTITY, owner, world, stack);
    }

    public FierySnowballEntity(Level world, double x, double y, double z, ItemStack stack) {
        super(BCMEntities.FIERY_SNOWBALL_ENTITY, z, y, x, world, stack);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if(!this.level().isClientSide) {
            ServerLevel serverLevel = (ServerLevel)this.level();
            BlockPos pos = entityHitResult.getEntity().getOnPos();
            RegistryAccess registryManager = serverLevel.registryAccess();
            serverLevel.explode(this,
                    new DamageSource(registryManager.lookupOrThrow(Registries.DAMAGE_TYPE).get(DamageTypes.EXPLOSION.location()).get(), new Vec3(pos.getX(), pos.getY(), pos.getZ())),
                    null,
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    1.0F,
                    true,
                    Level.ExplosionInteraction.MOB);
            Entity entity = entityHitResult.getEntity();
            entity.hurtServer(serverLevel, this.damageSources().source(DamageTypes.MOB_PROJECTILE), 2);
            if(entity instanceof LivingEntity livingEntity) {
                livingEntity.setRemainingFireTicks(20*3);
            }
            this.discard();
        }
        super.onHitEntity(entityHitResult);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if(!this.level().isClientSide && hitResult.getType() == HitResult.Type.BLOCK) {
            ServerLevel serverLevel = (ServerLevel)this.level();
            BlockHitResult blockHitResult = (BlockHitResult)hitResult;
            BlockPos pos = blockHitResult.getBlockPos();
            BlockState state = serverLevel.getBlockState(pos);
            RegistryAccess registryManager = serverLevel.registryAccess();
            if(!state.is(BlockTags.AIR) && state.isFaceSturdy(serverLevel, pos, blockHitResult.getDirection(), SupportType.FULL)) {
                serverLevel.explode(this,
                        new DamageSource(registryManager.lookupOrThrow(Registries.DAMAGE_TYPE).get(DamageTypes.EXPLOSION.location()).get(), new Vec3(pos.getX(), pos.getY(), pos.getZ())),
                        null,
                        pos.getX(),
                        pos.getY(),
                        pos.getZ(),
                        1.0F,
                        true,
                        Level.ExplosionInteraction.MOB);
                this.discard();
            }
        }
        super.onHit(hitResult);
    }

    @Override
    protected Item getDefaultItem() {
        return BCMItems.FIERY_SNOWBALL;
    }
}
