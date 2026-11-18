package com.electrifiedded.thetitans.entity.titan.anim.creepertitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityCreeperTitan;

public class AnimationCreeperTitanCreation extends AIAnimation {
    private EntityCreeperTitan entity;

    public AnimationCreeperTitanCreation(EntityCreeperTitan test) {
        super(test);
        this.entity = test;
    }

    public int getAnimID() {
        return 13;
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return 480;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > 480 ? false : super.shouldContinueExecuting();
    }
}
