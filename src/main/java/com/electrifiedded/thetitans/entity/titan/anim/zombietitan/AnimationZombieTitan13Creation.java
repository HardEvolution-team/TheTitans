package com.electrifiedded.thetitans.entity.titan.anim.zombietitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

public class AnimationZombieTitan13Creation extends AIAnimation {
    private EntityZombieTitan entity;

    public AnimationZombieTitan13Creation(EntityZombieTitan test) {
        super(test);
        this.entity = test;
    }

    public int getAnimID() {
        return 13;
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return 560;
    }

    public void startExecuting() {
        super.startExecuting();
    }

    public void updateTask() {
        this.entity.motionX = 0D;
        this.entity.motionZ = 0D;
        if (this.entity.motionY > 0D)
            this.entity.motionY = 0D;
        if (entity.isClient()) {
            if (this.entity.getAnimTick() == 1)
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanBirth", this.entity.world, this.entity, 1000.0F, 1.0F);
            if (this.entity.getAnimTick() == 10)
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanRumble", this.entity.world, this.entity, this.entity.fetchScaleMultiplierGiant(), 1F);
            if (this.entity.getAnimTick() == 160)
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanZombieCreation", this.entity.world, this.entity, this.entity.fetchScaleMultiplierGiant(), this.entity.isChild() ? 1.5F : 1F);
            if (this.entity.getAnimTick() == 300)
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanQuake", this.entity.world, this.entity, this.entity.fetchScaleMultiplierGiant(), 1F);
            if (this.entity.getAnimTick() == 420)
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSkeletonGetUp", this.entity.world, this.entity, this.entity.fetchScaleMultiplierGiant(), 1F);
        }
    }
}
