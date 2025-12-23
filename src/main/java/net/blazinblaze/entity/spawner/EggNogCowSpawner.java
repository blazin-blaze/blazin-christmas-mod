package net.blazinblaze.entity.spawner;

import net.blazinblaze.BlazinChristmasMod;
import net.blazinblaze.advancement.BCMCriteria;
import net.blazinblaze.advancement.SpawnedSantaCriterion;
import net.blazinblaze.entity.BCMEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;

public class EggNogCowSpawner implements CustomSpawner {
    private int cooldown;

    @Override
    public void tick(ServerLevel world, boolean spawnMonsters, boolean spawnAnimals) {
        if(spawnAnimals && world.isRaining() && world.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            RandomSource random = world.getRandom();
            this.cooldown--;
            if(this.cooldown <= 0) {
                this.cooldown = this.cooldown + 12000 + random.nextInt(1200);
                long l = world.getDayTime() / 24000L;
                if (l >= 5L) {
                    if (random.nextInt(5) == 0) {
                        int i = world.players().size();
                        if (i >= 1) {
                            Player player = (Player)world.players().get(random.nextInt(i));
                            if (!player.isSpectator()) {
                                BlockPos pos = player.blockPosition();
                                Biome biome = world.getBiome(pos).value();
                                if(biome.coldEnoughToSnow(pos, world.getSeaLevel())) {
                                    int j = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                                    int k = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                                    BlockPos.MutableBlockPos mutableBlockPos = player.blockPosition().mutable().move(j, 0, k);
                                    int m = 10;
                                    if (world.hasChunksAt(mutableBlockPos.getX() - 10, mutableBlockPos.getZ() - 10, mutableBlockPos.getX() + 10, mutableBlockPos.getZ() + 10)) {
                                        mutableBlockPos.setY(world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, mutableBlockPos).getY());
                                        BCMEntities.EGG_NOG_COW.spawn(world, mutableBlockPos, EntitySpawnReason.NATURAL);
                                        if(BlazinChristmasMod.serverRef != null) {
                                            for(ServerPlayer serverPlayer : BlazinChristmasMod.serverRef.getPlayerList().getPlayers()) {
                                                BCMCriteria.SPAWNED_SANTA.trigger(serverPlayer);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
