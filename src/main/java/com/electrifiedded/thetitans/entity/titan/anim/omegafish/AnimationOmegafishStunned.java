package com.electrifiedded.thetitans.entity.titan.anim.omegafish;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntitySilverfishTitan;

public class AnimationOmegafishStunned extends AIAnimation {
    private EntitySilverfishTitan entity;

    public AnimationOmegafishStunned(EntitySilverfishTitan test) {
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
        return 500;
    }

    public boolean shouldContinueExecuting() {
        return (!this.entity.stunnedIs) && (this.entity.getAnimTick() > 500) ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if (this.entity.getAnimTick() == 500) {
            this.entity.setAnimID(0);
            this.entity.setAnimTick(0);
        }

        if (this.entity.getAnimTick() == 380) {
            this.entity.stunnedIs = false;
        }

        else {
            this.entity.setAttackTarget(null);
        }
    }
}
