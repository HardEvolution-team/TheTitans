package com.electrifiedded.thetitans.entity.titan.anim.pigzombietitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityPigZombieTitan;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class AnimationPigZombieTitanAttack4 extends AIAnimation {
    private EntityPigZombieTitan entity;

    public AnimationPigZombieTitanAttack4(EntityPigZombieTitan test) {
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
        return 190;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if(this.entity.getAnimTick() < 80 && this.entity.getAttackTarget() != null) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        }

        if(this.entity.getAnimTick() >= 100 && this.entity.getAnimTick() <= 104) {
            this.entity.performStrikeArea(this.entity, this.entity.body, 0.0D, 2.0F + this.entity.getRNG().nextFloat(), 6.0F, 2, 1.75F, 1.0F, 0.125F, false);
        }
    }
}
