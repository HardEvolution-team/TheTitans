package com.electrifiedded.thetitans.entity.titan.anim.skeletontitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitan;

public class AnimationSkeletonTitanRangedAttack1 extends AIAnimation {
    private EntitySkeletonTitan entity;

    public AnimationSkeletonTitanRangedAttack1(EntitySkeletonTitan test) {
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
        return 400;
    }

    public boolean shouldContinueExecuting() {
        return (this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs) || (this.entity.fetchKindSkeleton() == 1) || (this.entity.getAttackTarget() != null && this.entity.getDistanceSq(this.entity.getAttackTarget()) < 800D) ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if (this.entity.getAttackTarget() != null)
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
    }
}
