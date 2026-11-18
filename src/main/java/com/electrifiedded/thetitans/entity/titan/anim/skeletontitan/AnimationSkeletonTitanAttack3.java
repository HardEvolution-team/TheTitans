package com.electrifiedded.thetitans.entity.titan.anim.skeletontitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class AnimationSkeletonTitanAttack3 extends AIAnimation {
    private EntitySkeletonTitan entity;

    public AnimationSkeletonTitanAttack3(EntitySkeletonTitan test) {
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
        return 260;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs ||this.entity.world.getBlockState(new net.minecraft.util.math.BlockPos((int)this.entity.posX, (int)this.entity.posY - 1, (int)this.entity.posZ)).getBlock().getExplosionResistance(this.entity) > 500.0F ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        this.entity.renderYawOffset = this.entity.rotationYaw = this.entity.rotationYawHead;
        if(this.entity.getAnimTick() < 10 && this.entity.getAttackTarget() != null) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 180.0F, 40.0F);
        }

        if(this.entity.getAnimTick() == 160 && this.entity.fetchKindSkeleton() == 1) {
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:swordDrag", this.entity.world, this.entity, 10.0F, 1.0F);
        }

        if(this.entity.getAnimTick() == 90) {
            this.entity.shakeUserCamerasNearby(10.0D);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSlam", this.entity.world, this.entity, 100.0F, 1.0F);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanPress", this.entity.world, this.entity, 100.0F, 1.0F);

            for(int r = 0; r <= 10; ++r) {
                this.entity.performStrikeArea(this.entity, this.entity.pelvis, r == 10?50.0D:0.0D, 2.0F + this.entity.getRNG().nextFloat(), this.entity.fetchKindSkeleton() == 1?11.0F:2.0F, 0, 2.25F, 1.25F, 0.1F, false);
            }
        }
    }
}
