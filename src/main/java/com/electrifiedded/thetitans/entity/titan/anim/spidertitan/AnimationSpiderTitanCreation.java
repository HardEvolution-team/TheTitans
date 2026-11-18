package com.electrifiedded.thetitans.entity.titan.anim.spidertitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntitySpiderTitan;

public class AnimationSpiderTitanCreation extends AIAnimation {
    private EntitySpiderTitan entity;

    public AnimationSpiderTitanCreation(EntitySpiderTitan test) {
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
        return 260;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > 260 ? false : super.shouldContinueExecuting();
    }
}
