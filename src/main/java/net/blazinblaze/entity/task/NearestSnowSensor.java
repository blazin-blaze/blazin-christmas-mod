package net.blazinblaze.entity.task;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import net.blazinblaze.BlazinChristmasMod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class NearestSnowSensor extends Sensor<Mob> {

    private final Long2LongMap positionToExpiryTime = new Long2LongOpenHashMap();
    private int tries;
    private long expiryTime;

    public NearestSnowSensor() {
        super(20);
    }

    @Override
    protected void doTick(ServerLevel serverLevel, Mob livingEntity) {
        if (livingEntity.isBaby()) {
            this.tries = 0;
            this.expiryTime = serverLevel.getGameTime() + serverLevel.getRandom().nextInt(20);
            Predicate<BlockPos> predicate = pos -> {
                long l = pos.asLong();
                if (this.positionToExpiryTime.containsKey(l)) {
                    return false;
                } else if (++this.tries >= 5) {
                    return false;
                } else if(serverLevel.getBlockState(pos).is(BlockTags.SNOW)){
                    this.positionToExpiryTime.put(l, this.expiryTime + 40L);
                    return true;
                }
                return false;
            };

            BlockPos pos = livingEntity.blockPosition();
            Optional<BlockPos> snowBlockPos = BlockPos.findClosestMatch(pos, 20, 20, predicate);
            if(snowBlockPos.isPresent()) {
                livingEntity.getBrain().setMemory(BCMMemoryModules.NEAREST_SNOW_BLOCK, snowBlockPos.get());
            }else {
                this.positionToExpiryTime.long2LongEntrySet().removeIf(entry -> entry.getLongValue() < this.expiryTime);
            }
        }
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(BCMMemoryModules.NEAREST_SNOW_BLOCK);
    }
}
