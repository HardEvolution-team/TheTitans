package com.electrifiedded.thetitans.entity.titan.anim.creepertitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityCreeperTitan;

import net.minecraft.entity.Entity;

import java.util.List;

public class AnimationCreeperTitanAntiTitanAttack extends AIAnimation {
    private EntityCreeperTitan entity;

    public AnimationCreeperTitanAntiTitanAttack(EntityCreeperTitan test) {
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
        return 60;
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

        if(this.entity.getAnimTick() == 24 && this.entity.getAttackTarget() != null) {
            this.entity.performStrikeArea(this.entity.getAttackTarget(), this.entity.body, 10.0D, 1.0F + this.entity.getRNG().nextFloat(), 2.0F, 1, 0.0F, 1.0F, 0.5F, false);
        }
    }
}
