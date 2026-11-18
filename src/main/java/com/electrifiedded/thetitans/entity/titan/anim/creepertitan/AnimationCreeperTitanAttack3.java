package com.electrifiedded.thetitans.entity.titan.anim.creepertitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityCreeperTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class AnimationCreeperTitanAttack3 extends AIAnimation {
    private EntityCreeperTitan entity;

    public AnimationCreeperTitanAttack3(EntityCreeperTitan test) {
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
        return 70;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if(this.entity.getAnimTick() < 32 && this.entity.getAttackTarget() != null) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        }

        if(this.entity.getAnimTick() == 32) {
            if(this.entity.getAttackTarget() != null) {
                this.entity.performStrikeToMobLightning(this.entity.getAttackTarget());
            }

            this.entity.performStrikeArea(this.entity, this.entity.body, 10.0D, 1.0F + this.entity.getRNG().nextFloat(), 2.0F, 4, 0.5F, 1.5F, 0.5F, false);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanStrike", this.entity.world, this.entity, 20.0F, 1.0F);
        }
    }
}
