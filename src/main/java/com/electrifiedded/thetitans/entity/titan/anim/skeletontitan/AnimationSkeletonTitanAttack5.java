package com.electrifiedded.thetitans.entity.titan.anim.skeletontitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;

import java.util.List;

public class AnimationSkeletonTitanAttack5 extends AIAnimation {
    private EntitySkeletonTitan entity;

    public AnimationSkeletonTitanAttack5(EntitySkeletonTitan test) {
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
        if(this.entity.getAnimTick() < 24 && this.entity.getAttackTarget() != null) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        }

        if(this.entity.getAnimTick() == 24 && this.entity.getAttackTarget() != null) {
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSwing", this.entity.world, this.entity, 5.0F, 1.0F);
        }

        if(this.entity.getAnimTick() == 26 && this.entity.getAttackTarget() != null) {
            this.entity.performStrikeArea(this.entity.getAttackTarget(), this.entity.pelvis, 0.0D, 1.0F + this.entity.getRNG().nextFloat(), this.entity.fetchKindSkeleton() == 1?8.0F:2.0F, 4, 0.0F, 0.25F, 0.25F, false);
        }
    }
}
