package com.electrifiedded.thetitans.entity.titan.anim.endercolossus;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossus;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;

import java.util.List;

public class AnimationEnderColossusAntiTitanAttack extends AIAnimation {
    private EntityEnderColossus entity;

    public AnimationEnderColossusAntiTitanAttack(EntityEnderColossus test) {
        super(test);
        this.entity = test;
    }

    public int getAnimID() {
        return 1;
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return 30;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs ? false : super.shouldContinueExecuting();
    }

    public void startExecuting() {
        super.startExecuting();
        this.entity.assignATAAID(this.entity.getRNG().nextInt(4));
    }

    public void updateTask() {
        if(this.entity.getAnimTick() == 4 && this.entity.fetchATAAID() == 0) {
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanEnderColossusChomp", this.entity.world, this.entity, 100.0F, 1.0F);
        }

        if(this.entity.getAttackTarget() != null) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        }

        if(this.entity.getAnimTick() == 12 && this.entity.getAttackTarget() != null) {
            this.entity.performStrikeArea(this.entity.getAttackTarget(), this.entity.body, 10.0D, 2.0F + this.entity.getRNG().nextFloat(), 10.0F, 1, 0.0F, 1.0F, 0.5F, false);
        }
    }
}
