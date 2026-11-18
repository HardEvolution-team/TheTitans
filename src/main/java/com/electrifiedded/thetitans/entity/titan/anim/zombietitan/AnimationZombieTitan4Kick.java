package com.electrifiedded.thetitans.entity.titan.anim.zombietitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;

import java.util.List;

public class AnimationZombieTitan4Kick extends AIAnimation {
    private EntityZombieTitan entity;

    public AnimationZombieTitan4Kick(EntityZombieTitan test) {
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
        return 60;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if(this.entity.getAnimTick() < 36 && this.entity.getAttackTarget() != null) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 180.0F, 180.0F);
        }

        if(this.entity.getAnimTick() == 30 && this.entity.getAttackTarget() != null) {
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSwing", this.entity.world, this.entity, 5.0F, 1.0F);
            this.entity.performStrikeArea(this.entity.getAttackTarget(), this.entity.body, 0.0D, 4.0F + this.entity.getRNG().nextFloat(), 3.0F, 40, 0.0F, 0.25F, 0.25F, false);
        }
    }
}
