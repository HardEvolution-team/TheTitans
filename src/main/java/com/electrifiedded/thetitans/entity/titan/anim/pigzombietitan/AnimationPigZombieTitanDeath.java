package com.electrifiedded.thetitans.entity.titan.anim.pigzombietitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityPigZombieTitan;

public class AnimationPigZombieTitanDeath extends AIAnimation {
    private EntityPigZombieTitan entity;

    public AnimationPigZombieTitanDeath(EntityPigZombieTitan test) {
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
