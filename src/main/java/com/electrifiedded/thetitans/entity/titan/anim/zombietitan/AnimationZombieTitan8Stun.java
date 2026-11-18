package com.electrifiedded.thetitans.entity.titan.anim.zombietitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;

public class AnimationZombieTitan8Stun extends AIAnimation {
    private EntityZombieTitan entity;

    public AnimationZombieTitan8Stun(EntityZombieTitan test) {
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
        return 180;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > 180 ? false : super.shouldContinueExecuting();
    }
}
