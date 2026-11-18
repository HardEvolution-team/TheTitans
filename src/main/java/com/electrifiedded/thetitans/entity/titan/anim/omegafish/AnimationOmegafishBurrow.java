package com.electrifiedded.thetitans.entity.titan.anim.omegafish;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntitySilverfishTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;

import java.util.List;

public class AnimationOmegafishBurrow extends AIAnimation {
    private EntitySilverfishTitan entity;

    public AnimationOmegafishBurrow(EntitySilverfishTitan test) {
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
        return 100;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if(this.entity.getAnimTick() == 50) {
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:groundSmash", this.entity.world, this.entity, 20.0F, 1.25F);
            this.entity.performStrikeArea(this.entity, this.entity.body, 5.0D, 6.0F, (float)this.entity.fetchAmountStrike(1.0D), this.entity.fetchAmountKnockback() * 3, 0.0F, 4.0F, 0.5F, false);
        }
    }
}
