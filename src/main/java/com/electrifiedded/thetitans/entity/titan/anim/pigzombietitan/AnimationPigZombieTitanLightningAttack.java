package com.electrifiedded.thetitans.entity.titan.anim.pigzombietitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityPigZombieTitan;

public class AnimationPigZombieTitanLightningAttack extends AIAnimation {
    private EntityPigZombieTitan entity;

    public AnimationPigZombieTitanLightningAttack(EntityPigZombieTitan test) {
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
        return 110;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if ((this.entity.getAnimTick() < 40) && (this.entity.getAttackTarget() != null)) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        }
    }
}
