package com.electrifiedded.thetitans.entity.titan.anim.ultimairongolemtitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityIronGolemTitan;

public class AnimationIronGolemTitanRangedAttack extends AIAnimation {
    private EntityIronGolemTitan entity;

    public AnimationIronGolemTitanRangedAttack(EntityIronGolemTitan test) {
        super(test);
        this.entity = test;
    }

    public int getAnimID() {
        return 5;
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return 80;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > this.getDuration() ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if (this.entity.getAttackTarget() != null)
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
    }
}
