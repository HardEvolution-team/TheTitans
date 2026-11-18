package com.electrifiedded.thetitans.entity.titan.anim.omegafish;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntitySilverfishTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;

import java.util.List;

public class AnimationOmegafishAttack1 extends AIAnimation {
    private EntitySilverfishTitan entity;

    public AnimationOmegafishAttack1(EntitySilverfishTitan test) {
        super(test);
        this.entity = test;
    }

    public int getAnimID() {
        return 4;
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return 30;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if(this.entity.getAnimTick() < 12 && this.entity.getAttackTarget() != null) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        }

        if(this.entity.getAnimTick() == 12 && this.entity.getAttackTarget() != null) {
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSwing", this.entity.world, this.entity, 10.0F, 1.75F);
            this.entity.performStrikeArea(this.entity.getAttackTarget(), this.entity.head, 0.0D, 1.0F + this.entity.getRNG().nextFloat(), 1.0F, 1, 0.0F, 0.5F, 0.5F, false);
        }
    }
}
