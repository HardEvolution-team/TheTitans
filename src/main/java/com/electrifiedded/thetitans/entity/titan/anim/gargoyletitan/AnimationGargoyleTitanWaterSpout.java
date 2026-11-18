package com.electrifiedded.thetitans.entity.titan.anim.gargoyletitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityGargoyleTitan;

public class AnimationGargoyleTitanWaterSpout extends AIAnimation {
    private EntityGargoyleTitan entity;

    public AnimationGargoyleTitanWaterSpout(EntityGargoyleTitan test) {
        super(test);
        this.entity = test;
    }

    public int getAnimID() {
        return 5;
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return 60;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > 60 ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if (this.entity.getAttackTarget() != null)
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 40F, 40F);
    }
}
