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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.logging.Logger;

public class CandyCaneSnowballEntity extends ThrowableItemProjectile {
    public CandyCaneSnowballEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
        super(entityType, world);
    }

    public CandyCaneSnowballEntity(Level world, LivingEntity owner, ItemStack stack) {
        super(BCMEntities.CANDY_CANE_SNOWBALL_ENTITY, owner, world, stack);
    }

    public CandyCaneSnowballEntity(Level world, double x, double y, double z, ItemStack stack) {
        super(BCMEntities.CANDY_CANE_SNOWBALL_ENTITY, z, y, x, world, stack);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if(!this.level().isClientSide) {
            ServerLevel serverLevel = (ServerLevel)this.level();
            BlockPos pos = entityHitResult.getEntity().getOnPos();
            RandomSource random = serverLevel.getRandom();
            int x;
            int z;
            for(int i = -1 - random.nextInt(0, 2); i < random.nextInt(1, 2); i++) {
                for(int i2 = -1 - random.nextInt(0, 2); i2 < random.nextInt(1, 2); i2++) {
                    x = (int) pos.getCenter().x();
                    z = (int) pos.getCenter().z();
                    x = x + i;
                    z = z + i2;
                    int y = (int) pos.getCenter().y() - 1;
                    BlockState currentState = serverLevel.getBlockState(new BlockPos(x, y, z));
                    if(!currentState.is(BlockTags.AIR)) {
                        BlazinChristmasMod.LOGGER.info(String.format("%s, %s, %s", x, y, z));
                        serverLevel.setBlockAndUpdate(new BlockPos(x, y, z), BCMBlocks.BLOCK_OF_GINGERBREAD.defaultBlockState());
                        serverLevel.playLocalSound(new BlockPos(x, y, z), SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 0.5F, 1.0F, true);
                    }
                }
            }
            Entity entity = entityHitResult.getEntity();
            entity.hurtServer(serverLevel, this.damageSources().source(DamageTypes.MOB_PROJECTILE), 1);
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
            if(!state.is(BlockTags.AIR) && state.isFaceSturdy(serverLevel, pos, blockHitResult.getDirection(), SupportType.FULL)) {
                int x;
                int z;
                for(int i = -1 - random.nextInt(0, 2); i < random.nextInt(1, 2); i++) {
                    for(int i2 = -1 - random.nextInt(0, 2); i2 < random.nextInt(1, 2); i2++) {
                        x = (int) pos.getCenter().x();
                        z = (int) pos.getCenter().z();
                        x = x + i;
                        z = z + i2;
                        int y = serverLevel.getHeight(Heightmap.Types.WORLD_SURFACE, x, z) - 1;
                        BlockState currentState = serverLevel.getBlockState(new BlockPos(x, y, z));
                        if(!currentState.is(BlockTags.AIR)) {
                            BlazinChristmasMod.LOGGER.info(String.format("%s, %s, %s", x, y, z));
                            serverLevel.setBlockAndUpdate(new BlockPos(x, y, z), BCMBlocks.BLOCK_OF_GINGERBREAD.defaultBlockState());
                            serverLevel.playLocalSound(new BlockPos(x, y, z), SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 0.5F, 1.0F, true);
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
        return BCMItems.CANDY_CANE_SNOWBALL;
    }
}
