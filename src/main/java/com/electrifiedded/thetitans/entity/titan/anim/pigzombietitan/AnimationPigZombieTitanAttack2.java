package com.electrifiedded.thetitans.entity.titan.anim.pigzombietitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityPigZombieTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;

import java.util.List;

public class AnimationPigZombieTitanAttack2 extends AIAnimation {
    private EntityPigZombieTitan entity;

    public AnimationPigZombieTitanAttack2(EntityPigZombieTitan test) {
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
        return 150;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if(this.entity.getAnimTick() < 60 && this.entity.getAttackTarget() != null) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        }

        if(this.entity.getAnimTick() == 60 || this.entity.getAnimTick() == 104) {
            this.entity.performStrikeArea(this.entity, this.entity.body, 20.0D, 2.0F + this.entity.getRNG().nextFloat(), 6.0F, 3, 0.0F, 3.0F, 0.5F, false);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSlam", this.entity.world, this.entity, 100.0F, 1.0F);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanPress", this.entity.world, this.entity, 100.0F, 1.0F);
        }
    }
}
