package com.electrifiedded.thetitans.entity.titan.anim.skeletontitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitan;

public class AnimationSkeletonTitanStun extends AIAnimation {
    private EntitySkeletonTitan entity;

    public AnimationSkeletonTitanStun(EntitySkeletonTitan test) {
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
        return 540;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > 540 ? false : super.shouldContinueExecuting();
    }
}
