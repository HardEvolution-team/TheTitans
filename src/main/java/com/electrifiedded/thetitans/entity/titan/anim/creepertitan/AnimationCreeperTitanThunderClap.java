package com.electrifiedded.thetitans.entity.titan.anim.creepertitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityCreeperTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class AnimationCreeperTitanThunderClap extends AIAnimation {
    private EntityCreeperTitan entity;

    public AnimationCreeperTitanThunderClap(EntityCreeperTitan test) {
        super(test);
        this.entity = test;
    }

    public int getAnimID() {
        return 7;
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return 230;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if(this.entity.getAnimTick() < 100 && this.entity.getAttackTarget() != null) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        }

        if(this.entity.getAnimTick() == 100) {
            if(this.entity.getAttackTarget() != null) {
                this.entity.performStrikeToMobLightning(this.entity.getAttackTarget());
            }

            this.entity.performStrikeArea(this.entity, this.entity.head, 20.0D, 2.0F + this.entity.getRNG().nextFloat(), 4.0F, 6, 1.5F, 1.0F, 1.0F, false);
            double d8 = (double)this.entity.fetchMPArea(0.75F);
            net.minecraft.util.math.Vec3d vec3 = this.entity.getLook(1.0F);
            double dx = vec3.x * d8;
            double dz = vec3.z * d8;
            this.entity.world.newExplosion(this.entity, this.entity.posX + dx, this.entity.posY + 10.0D, this.entity.posZ + dz, 3.0F, false, false);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanStrike", this.entity.world, this.entity, 20.0F, 1.1F);
        }

        if(this.entity.getAnimTick() > 100 && this.entity.getAnimTick() <= 110) {
            this.entity.performStrikeArea(this.entity, this.entity.head, 0.0D, 2.0F + this.entity.getRNG().nextFloat(), 4.0F - (float)(this.entity.getAnimTick() - 100) * 0.1F, 1 + (this.entity.getAnimTick() - 100), 1.5F + (float)(this.entity.getAnimTick() - 100) * 0.25F, 1.0F + (float)(this.entity.getAnimTick() - 100) * 0.05F, 1.0F + (float)(this.entity.getAnimTick() - 100) * 0.025F, false);
        }

        if(this.entity.getAnimTick() == 150) {
            this.entity.performStrikeArea(this.entity, this.entity.body, 10.0D, 1.0F + this.entity.getRNG().nextFloat(), 8.0F, 1, 0.75F, 2.0F, 0.1F, false);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSlam", this.entity.world, this.entity, 20.0F, 1.0F);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:groundSmash", this.entity.world, this.entity, 20.0F, 1.0F);
        }
    }
}
