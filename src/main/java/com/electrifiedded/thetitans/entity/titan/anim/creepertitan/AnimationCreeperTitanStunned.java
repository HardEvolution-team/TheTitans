package com.electrifiedded.thetitans.entity.titan.anim.creepertitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityCreeperTitan;

public class AnimationCreeperTitanStunned extends AIAnimation {
    private EntityCreeperTitan entity;

    public AnimationCreeperTitanStunned(EntityCreeperTitan test) {
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
        return 520;
    }

    public boolean shouldContinueExecuting() {
        return super.shouldContinueExecuting();
    }

    public void updateTask() {
        if (this.entity.getAnimTick() == 520) {
            this.entity.setAnimID(0);
            this.entity.setAnimTick(0);
        }

        if (this.entity.getAnimTick() == 460) {
            this.entity.stunnedIs = false;
        }

        else {
            this.entity.setAttackTarget(null);
        }
    }
}
