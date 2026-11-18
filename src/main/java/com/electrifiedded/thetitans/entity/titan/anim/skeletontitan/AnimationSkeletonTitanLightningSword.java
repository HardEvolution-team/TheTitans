package com.electrifiedded.thetitans.entity.titan.anim.skeletontitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitan;

public class AnimationSkeletonTitanLightningSword extends AIAnimation {
    private EntitySkeletonTitan entity;

    public AnimationSkeletonTitanLightningSword(EntitySkeletonTitan test) {
        super(test);
        this.entity = test;
    }

    public int getAnimID() {
        return 9;
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return 130;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if (this.entity.getAttackTarget() != null)
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
    }
}
