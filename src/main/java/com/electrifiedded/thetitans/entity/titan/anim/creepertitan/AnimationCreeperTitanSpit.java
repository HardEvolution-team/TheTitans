package com.electrifiedded.thetitans.entity.titan.anim.creepertitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityCreeperTitan;

public class AnimationCreeperTitanSpit extends AIAnimation {
    private EntityCreeperTitan entity;

    public AnimationCreeperTitanSpit(EntityCreeperTitan test) {
        super(test);
        this.entity = test;
    }

    public int getAnimID() {
        return 6;
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return 60;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.stunnedIs ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if (this.entity.getAttackTarget() != null)
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
    }
}
