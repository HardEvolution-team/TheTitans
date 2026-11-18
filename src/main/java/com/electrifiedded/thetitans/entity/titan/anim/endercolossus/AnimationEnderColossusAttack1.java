package com.electrifiedded.thetitans.entity.titan.anim.endercolossus;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossus;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;

import java.util.List;

public class AnimationEnderColossusAttack1 extends AIAnimation {
    private EntityEnderColossus entity;

    public AnimationEnderColossusAttack1(EntityEnderColossus test) {
        super(test);
        this.entity = test;
    }

    public int getAnimID() {
        return 9;
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
        if(this.entity.getAnimTick() > 60) {
            this.entity.setAttackTarget((net.minecraft.entity.EntityLivingBase)null);
        }

        if(this.entity.getAnimTick() < 30 && this.entity.getAttackTarget() != null) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 180.0F, 180.0F);
        }

        if(this.entity.getAnimTick() == 26) {
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSwing", this.entity.world, this.entity, 10.0F, 1.0F);
        }

        if(this.entity.getAnimTick() == 28 && this.entity.getAttackTarget() != null) {
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSwing", this.entity.world, this.entity, 10.0F, 1.0F);
            this.entity.performStrikeArea(this.entity.getAttackTarget(), this.entity.body, 0.0D, 2.0F + this.entity.getRNG().nextFloat(), 10.0F, 1, 0.0F, 0.25F, 0.25F, false);
        }
    }
}
