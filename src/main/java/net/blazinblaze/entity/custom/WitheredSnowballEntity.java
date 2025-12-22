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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class WitheredSnowballEntity extends ThrowableItemProjectile {
    @Nullable
    private EntityReference<LivingEntity> owner;
    public WitheredSnowballEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
        super(entityType, world);
    }

    public WitheredSnowballEntity(Level world, LivingEntity owner, ItemStack stack) {
        super(BCMEntities.WITHERED_SNOWBALL_ENTITY, owner, world, stack);
        this.owner = new EntityReference<>(owner);
    }

    public WitheredSnowballEntity(Level world, double x, double y, double z, ItemStack stack) {
        super(BCMEntities.WITHERED_SNOWBALL_ENTITY, z, y, x, world, stack);
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
                    int y = (int)pos.getCenter().y() - 1;
                    BlockState currentState = serverLevel.getBlockState(new BlockPos(x, y, z));
                    if(!currentState.is(BlockTags.AIR)) {
                        BlockPos newBlockPos = new BlockPos(x, y, z);
                        serverLevel.setBlockAndUpdate(newBlockPos, BCMBlocks.FOULED_SNOW.defaultBlockState());
                        LivingEntity entity = EntityReference.get(this.owner, this.level(), LivingEntity.class);
                        if(entity instanceof EvilSantaVillager) {
                            if(random.nextFloat() < 0.2F) {
                                ElfVex elfVex = BCMEntities.ELF_VEX.create(serverLevel, EntitySpawnReason.NATURAL);
                                elfVex.snapTo(newBlockPos, 0.0F, 0.0F);
                                elfVex.finalizeSpawn(serverLevel, entity.level().getCurrentDifficultyAt(newBlockPos), EntitySpawnReason.MOB_SUMMONED, null);
                                elfVex.setOwner((Mob) entity);
                                elfVex.setBoundOrigin(newBlockPos);
                                elfVex.setLimitedLifeElf(20 * (30 + entity.getRandom().nextInt(90)));

                                serverLevel.addFreshEntityWithPassengers(elfVex);
                                serverLevel.gameEvent(GameEvent.ENTITY_PLACE, newBlockPos, GameEvent.Context.of(entity));
                            }
                        }else {
                            if(random.nextFloat() < 0.05F) {
                                FriendlyElfVex elfVex = BCMEntities.FRIENDLY_ELF_VEX.create(serverLevel, EntitySpawnReason.NATURAL);
                                elfVex.snapTo(newBlockPos, 0.0F, 0.0F);
                                elfVex.finalizeSpawn(serverLevel, entity.level().getCurrentDifficultyAt(newBlockPos), EntitySpawnReason.MOB_SUMMONED, null);
                                elfVex.setOwner((Mob) entity);
                                elfVex.setBoundOrigin(newBlockPos);
                                elfVex.setLimitedLifeElf(20 * (30 + entity.getRandom().nextInt(90)));

                                serverLevel.addFreshEntityWithPassengers(elfVex);
                                serverLevel.gameEvent(GameEvent.ENTITY_PLACE, newBlockPos, GameEvent.Context.of(entity));
                            }
                        }
                        serverLevel.playLocalSound(newBlockPos, SoundEvents.SOUL_SAND_BREAK, SoundSource.BLOCKS, 0.5F, 1.0F, true);
                    }
                }
            }
            Entity entity = entityHitResult.getEntity();
            entity.hurtServer(serverLevel, this.damageSources().source(DamageTypes.MOB_PROJECTILE), 1);
            if(entity instanceof LivingEntity livingEntity) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 20*5, 1, true, true, true));
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
                            serverLevel.setBlockAndUpdate(new BlockPos(x, y, z), BCMBlocks.FOULED_SNOW.defaultBlockState());
                            if(random.nextFloat() < 0.05F) {
                                LivingEntity entity = EntityReference.get(this.owner, this.level(), LivingEntity.class);
                                if(entity instanceof EvilSantaVillager) {
                                    BCMEntities.ELF_VEX.spawn(serverLevel, new BlockPos(x,y,z), EntitySpawnReason.NATURAL);
                                }else {
                                    BCMEntities.FRIENDLY_ELF_VEX.spawn(serverLevel, new BlockPos(x,y,z), EntitySpawnReason.NATURAL);
                                }
                            }
                            serverLevel.playLocalSound(new BlockPos(x, y, z), SoundEvents.SOUL_SAND_BREAK, SoundSource.BLOCKS, 0.5F, 1.0F, true);
                        }
                    }
                }
            }
            this.discard();
        }
        super.onHit(hitResult);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        this.owner = EntityReference.read(valueInput, "owner");
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        EntityReference.store(this.owner, valueOutput, "owner");
    }

    @Override
    protected Item getDefaultItem() {
        return BCMItems.WITHERED_SNOWBALL;
    }
}
