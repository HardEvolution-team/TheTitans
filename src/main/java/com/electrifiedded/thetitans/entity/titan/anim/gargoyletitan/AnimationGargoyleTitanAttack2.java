package com.electrifiedded.thetitans.entity.titan.anim.gargoyletitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityGargoyleTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;

import java.util.List;

public class AnimationGargoyleTitanAttack2 extends AIAnimation {
    private EntityGargoyleTitan entity;

    public AnimationGargoyleTitanAttack2(EntityGargoyleTitan test) {
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
            this.entity.shakeUserCamerasNearby(10D);
            float f = (float)entity.fetchAmountStrike(1.0F);
            int i = 0;
            List<?> found11 = this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity, this.entity.getEntityBoundingBox().expand(32.0D, 8.0D, 32.0D));
            if ((found11 != null) && (!found11.isEmpty())) {
                for (Object mobRef1Obj : found11) {
                    Entity mobRef1 = (Entity) mobRef1Obj;
                    if (this.entity.allowStrikeGiant(mobRef1.getClass())) {
                        mobRef1.motionY += 1.0F + entity.getRNG().nextFloat() + entity.getRNG().nextFloat();
                        this.entity.strikeMobChoosen(mobRef1, f, i);
                    }
                }
            }

            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSlam", this.entity.world, this.entity, 20.0F, 1.0F);
        }
    }
}
