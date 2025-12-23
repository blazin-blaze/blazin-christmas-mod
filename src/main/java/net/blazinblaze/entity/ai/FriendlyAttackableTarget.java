package net.blazinblaze.entity.ai;

import net.blazinblaze.entity.custom.FriendlyElfVex;
import net.blazinblaze.entity.custom.FriendlyGingerbreadMan;
import net.blazinblaze.entity.custom.GingerbreadMan;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;

public class FriendlyAttackableTarget extends NearestAttackableTargetGoal {
    public FriendlyAttackableTarget(Mob mob, Class class_, boolean bl) {
        super(mob, class_, bl);
    }

    protected void findTarget() {
        ServerLevel serverLevel = getServerLevel(this.mob);
        if (this.targetType != Player.class && this.targetType != ServerPlayer.class) {
            this.target = serverLevel.getNearestEntity(
                    this.mob.level().getEntitiesOfClass(this.targetType, this.getTargetSearchArea(this.getFollowDistance()), livingEntity -> true),
                    this.targetConditions.range(this.getFollowDistance()),
                    this.mob,
                    this.mob.getX(),
                    this.mob.getEyeY(),
                    this.mob.getZ()
            );
            if(this.target instanceof FriendlyElfVex || this.target instanceof FriendlyGingerbreadMan) {
                this.target = null;
            }
        } else {
            this.target = null;
        }
    }
}
