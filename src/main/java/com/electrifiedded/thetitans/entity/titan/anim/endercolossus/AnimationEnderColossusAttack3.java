package com.electrifiedded.thetitans.entity.titan.anim.endercolossus;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossus;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class AnimationEnderColossusAttack3 extends AIAnimation {
    private EntityEnderColossus entity;

    public AnimationEnderColossusAttack3(EntityEnderColossus test) {
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
        return 70;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if(this.entity.getAnimTick() > 70) {
            this.entity.setAttackTarget((net.minecraft.entity.EntityLivingBase)null);
        }

        if(this.entity.getAnimTick() < 30 && this.entity.getAttackTarget() != null) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        }

        if(this.entity.getAnimTick() == 10) {
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanEnderColossusScream", this.entity.world, this.entity, 100.0F, (this.entity.getRNG().nextFloat() - this.entity.getRNG().nextFloat()) * 0.2F + 1.1F);
        }

        if(this.entity.getAnimTick() == 32) {
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSwing", this.entity.world, this.entity, 10.0F, 1.0F);
        }

        if(this.entity.getAnimTick() == 36) {
            this.entity.performStrikeArea(this.entity, this.entity.body, 20.0D, 3.0F + this.entity.getRNG().nextFloat(), 20.0F, this.entity.fetchAmountKnockback(), 1.5F, 2.0F, 1.5F, false);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanStrike", this.entity.world, this.entity, 20.0F, 1.0F);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSlam", this.entity.world, this.entity, 20.0F, 1.0F);
        }
    }
}
