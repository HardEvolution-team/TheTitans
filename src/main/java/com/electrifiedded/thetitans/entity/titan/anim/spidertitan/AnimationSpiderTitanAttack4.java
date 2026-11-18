package com.electrifiedded.thetitans.entity.titan.anim.spidertitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntitySpiderTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;

import java.util.List;

public class AnimationSpiderTitanAttack4 extends AIAnimation {
    private EntitySpiderTitan entity;

    public AnimationSpiderTitanAttack4(EntitySpiderTitan test) {
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
        if(this.entity.getAnimTick() < 20 && this.entity.getAttackTarget() != null) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 180.0F, 40.0F);
            this.entity.renderYawOffset = this.entity.rotationYaw = this.entity.rotationYawHead;
        }

        if(this.entity.getAnimTick() == 20) {
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanEnderColossusChomp", this.entity.world, this.entity, 10.0F, 1.1F);
        }

        if(this.entity.getAnimTick() == 26 && this.entity.getAttackTarget() != null) {
            this.entity.performStrikeArea(this.entity.getAttackTarget(), this.entity.head, 0.0D, 2.0F + this.entity.getRNG().nextFloat(), 4.0F, 10, 0.0F, 0.25F, 0.25F, false);
        }
    }
}
