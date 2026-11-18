package com.electrifiedded.thetitans.entity.titan.anim.omegafish;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntitySilverfishTitan;

public class AnimationOmegafishDeath extends AIAnimation {
    private EntitySilverfishTitan entity;

    public AnimationOmegafishDeath(EntitySilverfishTitan test) {
        super(test);
        this.entity = test;
    }

    public int getAnimID() {
        return 10;
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return 2000;
    }

    public boolean shouldExecute() {
        return this.entity.deathTicks <= 0 || this.entity.isEntityAlive() ? false : super.shouldExecute();
    }

    public boolean shouldContinueExecuting() {
        return this.entity.deathTicks <= 0 || this.entity.isEntityAlive() ? false : super.shouldContinueExecuting();
    }
}
