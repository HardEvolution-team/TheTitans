package com.electrifiedded.thetitans.entity.titan.anim.creepertitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityCreeperTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class AnimationCreeperTitanAttack2 extends AIAnimation {
    private EntityCreeperTitan entity;

    public AnimationCreeperTitanAttack2(EntityCreeperTitan test) {
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
        return 170;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if(this.entity.getAnimTick() < 60 && this.entity.getAttackTarget() != null) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        }

        if(this.entity.getAnimTick() == 90) {
            this.entity.performStrikeArea(this.entity, this.entity.body, 10.0D, 1.0F + this.entity.getRNG().nextFloat(), 8.0F, 1, 0.75F, 2.0F, 0.1F, false);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSlam", this.entity.world, this.entity, 20.0F, 1.0F);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:groundSmash", this.entity.world, this.entity, 20.0F, 1.0F);
        }
    }
}
