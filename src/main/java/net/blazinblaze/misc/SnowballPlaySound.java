package net.blazinblaze.misc;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SnowballPlaySound implements ServerTickEvents.EndTick {
    public static final SnowballPlaySound INSTANCE = new SnowballPlaySound();
    private ArrayList<SnowballTimer> playerSnowballTimers = new ArrayList<>();
    public void setSnowballTimer(long ticksUntilSomething, int repeatTimes, Player player, Level level, float f) {
        playerSnowballTimers.add(new SnowballTimer(player, ticksUntilSomething, level, repeatTimes, f));
    }

    @Override
    public void onEndTick(MinecraftServer minecraftServer) {
        for (var iter = playerSnowballTimers.listIterator(); iter.hasNext(); ) {
            SnowballTimer currentTimer = iter.next();
            if ((currentTimer.ticksUntilSomething - 1) <= 0L) {
                if(currentTimer.level != null && currentTimer.player != null) {
                    currentTimer.level.playSound((Entity)null, currentTimer.player.getX(), currentTimer.player.getY(), currentTimer.player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 1.0F, 1.0F / (currentTimer.level.getRandom().nextFloat() * 0.4F + 1.2F) + currentTimer.f * 0.5F);
                }
                if(currentTimer.repeatTimes > 0) {
                    iter.set(new SnowballTimer(currentTimer.player, currentTimer.ticksUntilSomething, currentTimer.level, currentTimer.repeatTimes - 1, currentTimer.f));
                }else {
                    iter.remove();
                }
            }else {
                iter.set(new SnowballTimer(currentTimer.player, currentTimer.ticksUntilSomething - 1, currentTimer.level, currentTimer.repeatTimes, currentTimer.f));
            }
        }
    }

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(INSTANCE);
    }

    record SnowballTimer(Player player, long ticksUntilSomething, Level level, int repeatTimes, float f) {}
}
