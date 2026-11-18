package com.electrifiedded.thetitans.entity.titan.anim.creepertitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityCreeperTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitanPart;

import net.minecraft.entity.Entity;

import java.util.List;

public class AnimationCreeperTitanAttack4 extends AIAnimation {
    private EntityCreeperTitan entity;

    public AnimationCreeperTitanAttack4(EntityCreeperTitan test) {
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
        return 60;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.stunnedIs ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if(this.entity.getAnimTick() < 30 && this.entity.getAttackTarget() != null) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        }

        if(this.entity.getAnimTick() == 30 && this.entity.getAttackTarget() != null && this.entity.getDistance(this.entity.getAttackTarget()) <= this.entity.fetchMPArea(1.0F)) {
            this.entity.performStrikeArea(this.entity.getAttackTarget(), (com.electrifiedded.thetitans.entity.titan.EntityTitanPart)null, 0.0D, 2.0F + this.entity.getRNG().nextFloat(), 1.0F, this.entity.fetchAmountKnockback(), 0.125F, 0.125F, 0.0F, false);
        }
    }
}
