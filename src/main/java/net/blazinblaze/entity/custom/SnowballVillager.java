package net.blazinblaze.entity.custom;

import net.blazinblaze.entity.BCMEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class SnowballVillager extends Snowball {
    public SnowballVillager(EntityType<SnowballVillager> entityType, Level world) {
        super(BCMEntities.SNOWBALL_VILLAGER, world);
    }

    public SnowballVillager(Level world, LivingEntity owner, ItemStack stack) {
        super(world, owner, stack);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        if(entity instanceof Villager villager) {
            if(villager.isBaby()) {
                super.onHitEntity(entityHitResult);
                villager.heal(0.1F);
            }
        }else if(!(entity instanceof Monster || entity instanceof IronGolem)){
            super.onHitEntity(entityHitResult);
        }
    }
}
