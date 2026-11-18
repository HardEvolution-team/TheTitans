package com.electrifiedded.thetitans.entity.titan.anim.pigzombietitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityPigZombieTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class AnimationPigZombieTitanAttack3 extends AIAnimation {
    private EntityPigZombieTitan entity;

    public AnimationPigZombieTitanAttack3(EntityPigZombieTitan test) {
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
        return (this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs || this.entity.world.getBlockState(new net.minecraft.util.math.BlockPos((int)this.entity.posX, (int)this.entity.posY - 1, (int)this.entity.posZ)).getBlock().getExplosionResistance(this.entity) <= 1.5F) ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        this.entity.renderYawOffset = this.entity.rotationYaw = this.entity.rotationYawHead;
        if(this.entity.getAnimTick() == 120) {
            this.entity.shakeUserCamerasNearby(10.0D);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSlam", this.entity.world, this.entity, 100.0F, 1.0F);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanPress", this.entity.world, this.entity, 100.0F, 1.0F);

            for(int r = 0; r <= 10; ++r) {
                this.entity.performStrikeArea(this.entity, this.entity.body, r == 10?50.0D:0.0D, 2.0F + this.entity.getRNG().nextFloat(), 9.0F, 0, 2.25F, 1.25F, 0.1F, false);
            }
        }
    }
}
