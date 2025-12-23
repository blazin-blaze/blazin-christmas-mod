package net.blazinblaze.entity.custom;

import net.blazinblaze.BlazinChristmasMod;
import net.blazinblaze.block.BCMBlocks;
import net.blazinblaze.entity.BCMEntities;
import net.blazinblaze.item.BCMItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class EtherealSnowballEntity extends ThrowableItemProjectile {
    public EtherealSnowballEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
        super(entityType, world);
    }

    public EtherealSnowballEntity(Level world, LivingEntity owner, ItemStack stack) {
        super(BCMEntities.ETHEREAL_SNOWBALL_ENTITY, owner, world, stack);
    }

    public EtherealSnowballEntity(Level world, double x, double y, double z, ItemStack stack) {
        super(BCMEntities.ETHEREAL_SNOWBALL_ENTITY, z, y, x, world, stack);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if(!this.level().isClientSide) {
            ServerLevel serverLevel = (ServerLevel)this.level();
            BlockPos pos = entityHitResult.getEntity().getOnPos();
            BlockState state = serverLevel.getBlockState(pos);
            RandomSource random = serverLevel.getRandom();
            int x;
            int z;
            int y;
            for(int i = -1 - random.nextInt(0, 2); i < random.nextInt(1, 2); i++) {
                for(int i2 = -1 - random.nextInt(0, 2); i2 < random.nextInt(1, 2); i2++) {
                    x = (int) pos.getCenter().x();
                    z = (int) pos.getCenter().z();
                    x = x + i;
                    z = z + i2;
                    for(int i3 = random.nextInt(1, 2); i3 < 3; i3++) {
                        y = (int) pos.getCenter().y();
                        y = y + i3;
                        if(i % 2 == 0 && i2 % 2 == 0 && i3 % 2 == 0) {
                            BlockState currentState = serverLevel.getBlockState(new BlockPos(x, y, z));
                            if(!currentState.isSuffocating(this.level(), new BlockPos(x,y,z))) {
                                serverLevel.setBlockAndUpdate(new BlockPos(x, y, z), BCMBlocks.DEPLETABLE_WATER.defaultBlockState());
                                serverLevel.playLocalSound(new BlockPos(x, y, z), SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 0.5F, 1.0F, true);
                            }
                        }
                    }
                }
            }
            Entity entity = entityHitResult.getEntity();
            entity.hurtServer(serverLevel, this.damageSources().source(DamageTypes.MOB_PROJECTILE), 1.5F);
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
            RandomSource random = serverLevel.getRandom();
            int x;
            int z;
            int y;
            for(int i = -1 - random.nextInt(0, 2); i < random.nextInt(1, 2); i++) {
                for(int i2 = -1 - random.nextInt(0, 2); i2 < random.nextInt(1, 2); i2++) {
                    x = (int) pos.getCenter().x();
                    z = (int) pos.getCenter().z();
                    x = x + i;
                    z = z + i2;
                    for(int i3 = random.nextInt(1, 2); i3 < 3; i3++) {
                        y = (int) pos.getCenter().y();
                        y = y + i3;
                        BlockState currentState = serverLevel.getBlockState(new BlockPos(x, y, z));
                        if(!currentState.isSuffocating(this.level(), new BlockPos(x,y,z))) {
                            serverLevel.setBlockAndUpdate(new BlockPos(x, y, z), BCMBlocks.DEPLETABLE_WATER.defaultBlockState());
                            serverLevel.playLocalSound(new BlockPos(x, y, z), SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 0.5F, 1.0F, true);
                        }
                    }
                }
            }
            this.discard();
        }
        super.onHit(hitResult);
    }

    @Override
    protected Item getDefaultItem() {
        return BCMItems.ETHEREAL_SNOWBALL;
    }
}
