package com.electrifiedded.thetitans.entity.titan.anim.pigzombietitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityPigZombieTitan;

public class AnimationPigZombieTitanCreation extends AIAnimation {
    private EntityPigZombieTitan entity;

    public AnimationPigZombieTitanCreation(EntityPigZombieTitan entityPigZombieTitan) {
        super(entityPigZombieTitan);
        this.entity = entityPigZombieTitan;
    }

    public int getAnimID() {
        return 13;
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return 360;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > 360 ? false : super.shouldContinueExecuting();
    }
}
