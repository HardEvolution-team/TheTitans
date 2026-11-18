package com.electrifiedded.thetitans.entity.titan.anim.skeletontitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitan;

public class AnimationSkeletonTitanRangedAttack2 extends AIAnimation {
    private EntitySkeletonTitan entity;

    public AnimationSkeletonTitanRangedAttack2(EntitySkeletonTitan test) {
        super(test);
        this.entity = test;
    }

    public int getAnimID() {
        return 11;
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return 120;
    }

    public boolean shouldContinueExecuting() {
        return (this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs) || (this.entity.fetchKindSkeleton() == 1) ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if (this.entity.getAttackTarget() != null)
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
    }
}
