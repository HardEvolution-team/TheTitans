package com.electrifiedded.thetitans.entity.titan.anim.gargoyletitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityGargoyleTitan;

import net.minecraft.entity.Entity;

import java.util.List;

public class AnimationGargoyleTitanAntiTitanAttack extends AIAnimation {
    private EntityGargoyleTitan entity;

    public AnimationGargoyleTitanAntiTitanAttack(EntityGargoyleTitan test) {
        super(test);
        this.entity = test;
    }

    public int getAnimID() {
        return 1;
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return 30;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > 30 ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if (this.entity.getAttackTarget() != null)
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        if ((this.entity.getAnimTick() == 12) && (this.entity.getAttackTarget() != null)) {
            this.entity.shakeUserCamerasNearby(10D);
            float f = (float)entity.fetchAmountStrike(1.0F);
            int i = this.entity.fetchAmountKnockback();
            this.entity.strikeMobChoosen(this.entity.getAttackTarget(), f, i);
            List<?> found11 = this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity.getAttackTarget(), this.entity.getAttackTarget().getEntityBoundingBox().expand(8.0D, 8.0D, 8.0D));
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
