package com.electrifiedded.thetitans.entity.titan.anim.zombietitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class AnimationZombieTitan7DownwardSlash extends AIAnimation {
    private EntityZombieTitan entity;

    public AnimationZombieTitan7DownwardSlash(EntityZombieTitan test) {
        super(test);
        this.entity = test;
    }

    public int getAnimID() {
        return 7;
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return 230;
    }

    public boolean shouldContinueExecuting() {
        return (this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs || !this.entity.armedIs()) ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        this.entity.renderYawOffset = this.entity.rotationYaw = this.entity.rotationYawHead;
        if(this.entity.getAnimTick() < 10 && this.entity.getAttackTarget() != null) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 180.0F, 40.0F);
        }

        if(this.entity.getAnimTick() == 120) {
            this.entity.shakeUserCamerasNearby(10.0D);

            for(int r = 0; r <= 10; ++r) {
                this.entity.performStrikeArea(this.entity, this.entity.body, r == 10?50.0D:0.0D, 2.0F + this.entity.getRNG().nextFloat(), 7.0F, 0, 2.25F, 1.25F, 0.1F, false);
            }

            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSlam", this.entity.world, this.entity, 100.0F, 1.0F);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanPress", this.entity.world, this.entity, 100.0F, 1.0F);
        }
    }
}
