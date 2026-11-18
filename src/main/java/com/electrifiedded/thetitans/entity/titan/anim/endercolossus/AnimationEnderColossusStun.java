package com.electrifiedded.thetitans.entity.titan.anim.endercolossus;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossus;

public class AnimationEnderColossusStun extends AIAnimation {
    private EntityEnderColossus entity;

    public AnimationEnderColossusStun(EntityEnderColossus test) {
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
        return 400;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > 400 ? false : super.shouldContinueExecuting();
    }
}
