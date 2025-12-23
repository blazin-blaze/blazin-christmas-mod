package net.blazinblaze.entity.spawner;

import net.blazinblaze.entity.BCMEntities;
import net.blazinblaze.item.BCMItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;

public class SnowflakeSpawner implements CustomSpawner {
    private int cooldown;

    @Override
    public void tick(ServerLevel world, boolean spawnMonsters, boolean spawnAnimals) {
        if(spawnAnimals && world.isRaining() && world.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            RandomSource random = world.getRandom();
            this.cooldown--;
            if(this.cooldown <= 0) {
                this.cooldown = 20*30;
                int i = world.players().size();
                if (i >= 1) {
                    Player player = (Player)world.players().get(random.nextInt(i));
                    if (!player.isSpectator()) {
                        BlockPos pos = player.blockPosition();
                        Biome biome = world.getBiome(pos).value();
                        if(biome.coldEnoughToSnow(pos, world.getSeaLevel())) {
                            int j = (10 + random.nextInt(15)) * (random.nextBoolean() ? -1 : 1);
                            int k = (10 + random.nextInt(15)) * (random.nextBoolean() ? -1 : 1);
                            BlockPos.MutableBlockPos mutableBlockPos = player.blockPosition().mutable().move(j, 0, k);
                            int m = 10;
                            if (world.hasChunksAt(mutableBlockPos.getX() - 10, mutableBlockPos.getZ() - 10, mutableBlockPos.getX() + 10, mutableBlockPos.getZ() + 10)) {
                                mutableBlockPos.setY(world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, mutableBlockPos).getY());
                                ItemEntity itemEntity = new ItemEntity(world, mutableBlockPos.getX(), mutableBlockPos.getY() + 10, mutableBlockPos.getZ(), new ItemStack(BCMItems.SNOWFLAKE));
                                itemEntity.snapTo(new BlockPos(mutableBlockPos.getX(), mutableBlockPos.getY() + 10, mutableBlockPos.getZ()), 0.0F, 0.0F);
                                world.addFreshEntity(itemEntity);
                            }
                        }
                    }
                }
            }
        }
    }
}
