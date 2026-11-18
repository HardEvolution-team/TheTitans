package com.electrifiedded.thetitans.entity.titan.anim.skeletontitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

public class AnimationSkeletonTitanCreation extends AIAnimation {
    private EntitySkeletonTitan entity;

    public AnimationSkeletonTitanCreation(EntitySkeletonTitan test) {
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
        return 540;
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
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanBirth", this.entity.world, this.entity, 1000.0F, (entity.fetchKindSkeleton() == 1 ? 0.875F : 1.0F));
            if (this.entity.getAnimTick() == 10)
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSkeletonAwaken", this.entity.world, this.entity, entity.fetchScaleMultiplierGiant(), 1F);
            if (this.entity.getAnimTick() == 40)
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanRumble", this.entity.world, this.entity, entity.fetchScaleMultiplierGiant(), 1F);
            if (this.entity.getAnimTick() == 60)
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSkeletonBeginMove", this.entity.world, this.entity, entity.fetchScaleMultiplierGiant(), 1F);
            if (this.entity.getAnimTick() == 180)
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanQuake", this.entity.world, this.entity, entity.fetchScaleMultiplierGiant(), 1F);
            if (this.entity.getAnimTick() == 200)
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanRumble", this.entity.world, this.entity, entity.fetchScaleMultiplierGiant(), 1F);
            if (this.entity.getAnimTick() == 360)
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSkeletonGetUp", this.entity.world, this.entity, entity.fetchScaleMultiplierGiant(), 1F);
        }
    }
}
