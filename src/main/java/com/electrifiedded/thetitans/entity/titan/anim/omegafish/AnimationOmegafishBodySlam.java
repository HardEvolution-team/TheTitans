package com.electrifiedded.thetitans.entity.titan.anim.omegafish;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntitySilverfishTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class AnimationOmegafishBodySlam extends AIAnimation {
    private EntitySilverfishTitan entity;

    public AnimationOmegafishBodySlam(EntitySilverfishTitan test) {
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
        return 410;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if(this.entity.getAnimTick() < 80 && this.entity.getAttackTarget() != null) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        }

        if(this.entity.getAnimTick() == 80) {
            double ver = 9.0D;
            this.entity.motionY = ver;
            if(this.entity.getAttackTarget() != null) {
                double d01 = this.entity.getAttackTarget().posX - this.entity.posX;
                double d11 = this.entity.getAttackTarget().posZ - this.entity.posZ;
                float f21 = net.minecraft.util.math.MathHelper.sqrt(d01 * d01 + d11 * d11);
                double hor = (double)(f21 / 16.0F);
                this.entity.motionX = d01 / (double)f21 * hor * hor + this.entity.motionX * hor;
                this.entity.motionZ = d11 / (double)f21 * hor * hor + this.entity.motionZ * hor;
            }
        }

        if(this.entity.getAnimTick() == 240) {
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSlam", this.entity.world, this.entity, 20.0F, 1.0F);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:groundSmash", this.entity.world, this.entity, 20.0F, 1.5F);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanland", this.entity.world, this.entity, 10000.0F, 1.0F);
            this.entity.performStrikeArea(this.entity, this.entity.body, 30.0D, 6.0F + this.entity.getRNG().nextFloat(), 20.0F, this.entity.fetchAmountKnockback(), 0.0F, 6.0F, 2.0F, false);
        }

        if(this.entity.getAnimTick() == 380) {
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanPress", this.entity.world, this.entity, this.entity.fetchScaleMultiplierGiant(), 1.0F);
            this.entity.shakeUserCamerasNearby(4.0D);
        }
    }
}
