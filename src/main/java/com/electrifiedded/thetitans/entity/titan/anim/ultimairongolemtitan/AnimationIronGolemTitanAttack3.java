package com.electrifiedded.thetitans.entity.titan.anim.ultimairongolemtitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityIronGolemTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;

import java.util.List;

public class AnimationIronGolemTitanAttack3 extends AIAnimation {
    private EntityIronGolemTitan entity;

    public AnimationIronGolemTitanAttack3(EntityIronGolemTitan test) {
        super(test);
        this.entity = test;
    }

    public int getAnimID() {
        return 8;
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return 60;
    }

    public boolean shouldContinueExecuting() {
        return (this.entity.getAnimTick() > 60) ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if ((this.entity.getAnimTick() < 24) && (this.entity.getAttackTarget() != null)) {
            this.entity.faceEntity(this.entity.getAttackTarget(), 180.0F, 180.0F);
        }

        if (entity.isClient() && this.entity.getAnimTick() == 26) {
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSwing", this.entity.world, this.entity, 100F, 1F);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("mob.irongolem.throw", this.entity.world, this.entity, 100.0F, 0.5F);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("mob.irongolem.throw", this.entity.world, this.entity, 100.0F, 0.5F);
        }

        if ((this.entity.getAnimTick() == 28) && (this.entity.getAttackTarget() != null)) {
            float f = (float)entity.fetchAmountStrike(1.0F);
            int i = this.entity.fetchAmountKnockback();
            this.entity.attackEntityAsMob(this.entity.getAttackTarget());
            this.entity.strikeMobChoosen(this.entity.getAttackTarget(), f, i);
            List<?> found11 = this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity.getAttackTarget(), this.entity.getAttackTarget().getEntityBoundingBox().expand(6.0D, 2.0D, 6.0D));
            if ((found11 != null) && (!found11.isEmpty())) {
                for (Object mobRef1Obj : found11) {
                    Entity mobRef1 = (Entity) mobRef1Obj;
                    if (this.entity.allowStrikeGiant(mobRef1.getClass())) {
                        this.entity.strikeMobChoosen(mobRef1, f, i);
                    }
                }
            }
        }
    }
}
