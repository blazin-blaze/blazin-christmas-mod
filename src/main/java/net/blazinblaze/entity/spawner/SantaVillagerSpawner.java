package net.blazinblaze.entity.spawner;

import net.blazinblaze.data.BCMAttachmentTypes;
import net.blazinblaze.data.EvilRemovedAttachmentData;
import net.blazinblaze.data.LastPresentAttachmentData;
import net.blazinblaze.entity.BCMEntities;
import net.blazinblaze.entity.custom.Reindeer;
import net.blazinblaze.entity.custom.SantaVillager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.ServerLevelData;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Optional;

public class SantaVillagerSpawner implements CustomSpawner {
    private int cooldown;
    private final RandomSource random = RandomSource.create();

    @Override
    public void tick(ServerLevel serverLevel, boolean bl, boolean bl2) {
        if(bl && serverLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING) && serverLevel.getAttachedOrCreate(BCMAttachmentTypes.EVIL_REMOVED_ATTACHMENT_TYPE, () -> EvilRemovedAttachmentData.DEFAULT).aBoolean()) {
            LocalDate localDate = LocalDate.now();
            int day = localDate.get(ChronoField.DAY_OF_MONTH);
            int month = localDate.get(ChronoField.MONTH_OF_YEAR);
            if(month == 12 && day == 25) {
                this.cooldown = this.cooldown - 5;
            }else {
                this.cooldown--;
            }
            if(this.cooldown <= 0) {
                long l = serverLevel.getDayTime() / 24000L;
                if (l >= 5L && serverLevel.isBrightOutside()) {
                    this.cooldown = this.cooldown + 12000 + random.nextInt(1200);
                    int i = serverLevel.players().size();
                    if (i >= 1) {
                        Player player = (Player)serverLevel.players().get(random.nextInt(i));
                        if (!player.isSpectator()) {
                            int j = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                            int k = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                            BlockPos.MutableBlockPos mutableBlockPos = player.blockPosition().mutable().move(j, 0, k);
                            int m = 10;
                            if (serverLevel.hasChunksAt(mutableBlockPos.getX() - 10, mutableBlockPos.getZ() - 10, mutableBlockPos.getX() + 10, mutableBlockPos.getZ() + 10)) {
                                Holder<Biome> holder = serverLevel.getBiome(mutableBlockPos);
                                if (!holder.is(BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS)) {
                                    this.spawn(serverLevel);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean spawn(ServerLevel serverLevel) {
        if(serverLevel.getAttachedOrCreate(BCMAttachmentTypes.EVIL_REMOVED_ATTACHMENT_TYPE, () -> EvilRemovedAttachmentData.DEFAULT).aBoolean()) {
            Player player = serverLevel.getRandomPlayer();
            if (player == null) {
                return true;
            } else if (this.random.nextInt(10) != 0) {
                return false;
            } else {
                BlockPos blockPos = player.blockPosition();
                int i = 48;
                PoiManager poiManager = serverLevel.getPoiManager();
                Optional<BlockPos> optional = poiManager.find(holder -> holder.is(PoiTypes.MEETING), blockPosx -> true, blockPos, 48, PoiManager.Occupancy.ANY);
                BlockPos blockPos2 = (BlockPos)optional.orElse(blockPos);
                BlockPos blockPos3 = this.findSpawnPositionNear(serverLevel, blockPos2, 48);
                if (blockPos3 != null && this.hasEnoughSpace(serverLevel, blockPos3)) {
                    SantaVillager santaVillager = BCMEntities.SANTA_VILLAGER.spawn(serverLevel, blockPos3, EntitySpawnReason.EVENT);
                    if (santaVillager != null) {
                        for (int j = 0; j < 2; j++) {
                            this.tryToSpawnReindeerFor(serverLevel, santaVillager, 4);
                        }

                        //this.serverLevelData.setWanderingTraderId(wanderingTrader.getUUID());
                        santaVillager.setDespawnDelay(48000);
                        santaVillager.setWanderTarget(blockPos2);
                        santaVillager.setHomeTo(blockPos2, 16);
                        return true;
                    }
                }

                return false;
            }
        }else {
            return false;
        }
    }

    private void tryToSpawnReindeerFor(ServerLevel serverLevel, SantaVillager santaVillager, int i) {
        BlockPos blockPos = this.findSpawnPositionNear(serverLevel, santaVillager.blockPosition(), i);
        if (blockPos != null) {
            Reindeer reindeer = BCMEntities.REINDEER.spawn(serverLevel, blockPos, EntitySpawnReason.EVENT);
            if (reindeer != null) {
                reindeer.setLeashedTo(santaVillager, true);
            }
        }
    }

    @Nullable
    private BlockPos findSpawnPositionNear(LevelReader levelReader, BlockPos blockPos, int i) {
        BlockPos blockPos2 = null;
        SpawnPlacementType spawnPlacementType = SpawnPlacements.getPlacementType(EntityType.WANDERING_TRADER);

        for (int j = 0; j < 10; j++) {
            int k = blockPos.getX() + this.random.nextInt(i * 2) - i;
            int l = blockPos.getZ() + this.random.nextInt(i * 2) - i;
            int m = levelReader.getHeight(Heightmap.Types.WORLD_SURFACE, k, l);
            BlockPos blockPos3 = new BlockPos(k, m, l);
            if (spawnPlacementType.isSpawnPositionOk(levelReader, blockPos3, EntityType.WANDERING_TRADER)) {
                blockPos2 = blockPos3;
                break;
            }
        }

        return blockPos2;
    }

    private boolean hasEnoughSpace(BlockGetter blockGetter, BlockPos blockPos) {
        for (BlockPos blockPos2 : BlockPos.betweenClosed(blockPos, blockPos.offset(1, 2, 1))) {
            if (!blockGetter.getBlockState(blockPos2).getCollisionShape(blockGetter, blockPos2).isEmpty()) {
                return false;
            }
        }

        return true;
    }
}
