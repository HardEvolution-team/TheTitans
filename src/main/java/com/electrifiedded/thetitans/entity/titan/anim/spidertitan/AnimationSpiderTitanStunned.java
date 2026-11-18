package com.electrifiedded.thetitans.entity.titan.anim.spidertitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntitySpiderTitan;

public class AnimationSpiderTitanStunned extends AIAnimation {
    private EntitySpiderTitan entity;

    public AnimationSpiderTitanStunned(EntitySpiderTitan test) {
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
        return 660;
    }

    public boolean shouldContinueExecuting() {
        return (!this.entity.stunnedIs) && (this.entity.getAnimTick() > 660) ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if (this.entity.getAnimTick() == 660) {
            this.entity.setAnimID(0);
            this.entity.setAnimTick(0);
        }

        if (this.entity.getAnimTick() == 420) {
            this.entity.stunnedIs = false;
        }

        else {
            this.entity.setAttackTarget(null);
        }
    }
}
