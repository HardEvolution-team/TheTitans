package com.electrifiedded.thetitans.entity.titan.anim.pigzombietitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityPigZombieTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class AnimationPigZombieTitanAttack6 extends AIAnimation {
    private EntityPigZombieTitan entity;

    public AnimationPigZombieTitanAttack6(EntityPigZombieTitan test) {
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
        return 150;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if(this.entity.getAnimTick() < 40 && this.entity.getAttackTarget() != null) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        }

        if(this.entity.getAnimTick() == 54) {
            this.entity.performStrikeArea(this.entity, this.entity.body, 10.0D, 2.0F + this.entity.getRNG().nextFloat(), 12.0F, this.entity.fetchAmountKnockback(), 0.25F, 1.0F, 0.125F, false);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanStrike", this.entity.world, this.entity, 20.0F, 1.0F);
        }
    }
}
