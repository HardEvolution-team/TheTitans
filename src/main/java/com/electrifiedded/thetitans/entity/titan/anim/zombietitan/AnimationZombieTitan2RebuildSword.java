package com.electrifiedded.thetitans.entity.titan.anim.zombietitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class AnimationZombieTitan2RebuildSword extends AIAnimation {
    private EntityZombieTitan entity;

    public AnimationZombieTitan2RebuildSword(EntityZombieTitan test) {
        super(test);
        this.entity = test;
    }

    public int getAnimID() {
        return 2;
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return 210;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if(this.entity.getAnimTick() == 160) {
            this.entity.assignArmed(true);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titansummonminion", this.entity.world, this.entity, 10.0F, 0.5F);
        }

        if(this.entity.getAnimTick() == 50) {
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanRumble", this.entity.world, this.entity, 10.0F, 1.0F);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanQuake", this.entity.world, this.entity, 10.0F, 1.0F);
            this.entity.performStrikeArea(this.entity, this.entity.body, 10.0D, 1.0F + this.entity.getRNG().nextFloat(), 3.0F, 4, 0.5F, 1.0F, 0.5F, false);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanStrike", this.entity.world, this.entity, 20.0F, 1.0F);
        }
    }
}
