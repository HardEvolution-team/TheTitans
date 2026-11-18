package com.electrifiedded.thetitans.entity.titan.anim.ultimairongolemtitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityIronGolemTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;

import java.util.List;

public class AnimationIronGolemTitanAttack2 extends AIAnimation {
    private EntityIronGolemTitan entity;

    public AnimationIronGolemTitanAttack2(EntityIronGolemTitan test) {
        super(test);
        this.entity = test;
    }

    public int getAnimID() {
        return 7;
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return 150;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > 150 ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if ((this.entity.getAnimTick() < 60) && (this.entity.getAttackTarget() != null)) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        }

        if ((this.entity.getAnimTick() == 60) || (this.entity.getAnimTick() == 104)) {
            if (entity.isClient())
                this.entity.shakeUserCamerasNearby(10D);
            this.entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
            List<?> found11 = this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity, this.entity.getEntityBoundingBox().expand(48.0D, 16.0D, 48.0D));
            if ((found11 != null) && (!found11.isEmpty())) {
                for (Object mobRef1Obj : found11) {
                    Entity mobRef1 = (Entity) mobRef1Obj;
                    if (this.entity.allowStrikeGiant(mobRef1.getClass())) {
                        mobRef1.motionY += 2D + entity.getRNG().nextDouble() + entity.getRNG().nextDouble();
                        this.entity.attackEntityAsMob(mobRef1);
                    }
                }
            }

            if (entity.isClient())
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSlam", this.entity.world, this.entity, 20.0F, 1.0F);
        }
    }
}
