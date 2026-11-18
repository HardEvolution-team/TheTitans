package com.electrifiedded.thetitans.entity.titan.anim.zombietitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;

public class AnimationZombieTitan1AntiTitanAttack extends AIAnimation {
    private EntityZombieTitan entity;

    public AnimationZombieTitan1AntiTitanAttack(EntityZombieTitan test) {
        super(test);
        this.entity = test;
    }

    public int getAnimID() {
        return 1;
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return 30;
    }

    public void startExecuting() {
        super.startExecuting();
        this.entity.assignATAAID(this.entity.getRNG().nextInt(4));
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if(this.entity.getAttackTarget() != null) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        }

        if(this.entity.getAnimTick() == 12 && this.entity.getAttackTarget() != null) {
            this.entity.performStrikeArea(this.entity.getAttackTarget(), this.entity.body, 10.0D, 1.0F + this.entity.getRNG().nextFloat(), 3.0F, 1, 0.0F, 1.0F, 0.5F, false);
        }
    }
}
