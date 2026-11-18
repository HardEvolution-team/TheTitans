package com.electrifiedded.thetitans.entity.titan.anim.omegafish;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntitySilverfishTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class AnimationOmegafishUnburrow extends AIAnimation {
    private EntitySilverfishTitan entity;

    public AnimationOmegafishUnburrow(EntitySilverfishTitan test) {
        super(test);
        this.entity = test;
    }

    public int getAnimID() {
        return 2;
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
        if(this.entity.getAnimTick() == 10) {
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:quickApperence", this.entity.world, this.entity, 20.0F, 1.0F);
            this.entity.performStrikeArea(this.entity, this.entity.body, 0.0D, 4.0F + this.entity.getRNG().nextFloat(), 1.0F, 1, 0.0F, 1.0F, 1.0F, false);
            this.entity.motionX = (double)(-net.minecraft.util.math.MathHelper.sin(this.entity.rotationYawHead * 3.1415927F / 180.0F) * 1.5F);
            this.entity.motionY = 1.0D;
            this.entity.motionZ = (double)(net.minecraft.util.math.MathHelper.cos(this.entity.rotationYawHead * 3.1415927F / 180.0F) * 1.5F);
        }

        if(this.entity.getAnimTick() == 45) {
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:groundSmash", this.entity.world, this.entity, 20.0F, 1.25F);
            this.entity.performStrikeArea(this.entity, this.entity.body, 10.0D, 4.0F + this.entity.getRNG().nextFloat(), 1.0F, 1, 0.0F, 1.0F, 1.0F, false);
        }
    }
}
