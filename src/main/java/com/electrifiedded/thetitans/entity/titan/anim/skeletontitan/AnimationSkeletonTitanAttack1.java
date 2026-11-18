package com.electrifiedded.thetitans.entity.titan.anim.skeletontitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class AnimationSkeletonTitanAttack1 extends AIAnimation {
    private EntitySkeletonTitan entity;

    public AnimationSkeletonTitanAttack1(EntitySkeletonTitan test) {
        super(test);
        this.entity = test;
    }

    public int getAnimID() {
        return 3;
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
        if(this.entity.getAnimTick() < 20 && this.entity.getAttackTarget() != null) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        }

        if(this.entity.getAnimTick() == 58) {
            this.entity.performStrikeArea(this.entity, this.entity.pelvis, 10.0D, 1.0F + this.entity.getRNG().nextFloat(), this.entity.fetchKindSkeleton() == 1?8.0F:2.0F, 4, 0.5F, 1.0F, 0.5F, false);
            float d0 = 0.5F * this.entity.fetchScaleMultiplierGiant();
            float f3 = this.entity.renderYawOffset * 3.1415927F / 180.0F;
            float f11 = net.minecraft.util.math.MathHelper.sin(f3);
            float f4 = net.minecraft.util.math.MathHelper.cos(f3);
            this.entity.world.newExplosion(this.entity, this.entity.posX - (double)(f11 * d0), this.entity.posY, this.entity.posZ + (double)(f4 * d0), 5.0F, false, false);
            if(this.entity.fetchKindSkeleton() == 1) {
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanStrike", this.entity.world, this.entity, 20.0F, 0.9F);
            } else {
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanStrike", this.entity.world, this.entity, 20.0F, 1.0F);
            }
        }
    }
}
