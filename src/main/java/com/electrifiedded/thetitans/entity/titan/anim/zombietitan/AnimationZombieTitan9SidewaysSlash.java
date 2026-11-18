package com.electrifiedded.thetitans.entity.titan.anim.zombietitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class AnimationZombieTitan9SidewaysSlash extends AIAnimation {
    private EntityZombieTitan entity;

    public AnimationZombieTitan9SidewaysSlash(EntityZombieTitan test) {
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
        if(this.entity.getAnimTick() < 40 && this.entity.getAttackTarget() != null) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        }

        if(this.entity.getAnimTick() >= 100 && this.entity.getAnimTick() <= 104) {
            this.entity.performStrikeArea(this.entity, this.entity.body, 0.0D, 2.0F + this.entity.getRNG().nextFloat(), this.entity.armedIs()?7.0F:3.0F, 8, this.entity.armedIs()?1.75F:0.375F, 1.0F, 0.125F, false);
            double d8 = this.entity.armedIs()?(double)this.entity.fetchMPArea(1.75F):(double)this.entity.fetchMPArea(0.375F);
            net.minecraft.util.math.Vec3d vec3 = this.entity.getLook(1.0F);
            double dx = vec3.x * d8;
            double dz = vec3.z * d8;
            float f = (float)this.entity.fetchAmountStrike(this.entity.armedIs()?7.0D:3.0D);
            int i = this.entity.fetchAmountKnockback();
            this.entity.collideWithEntities(this.entity.body, this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity, this.entity.body.getEntityBoundingBox().expand((double)this.entity.fetchMPArea(1.0F), (double)this.entity.fetchMPArea(0.1F), (double)this.entity.fetchMPArea(1.0F))));
            java.util.List found11 = this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity, this.entity.getEntityBoundingBox().expand((double)this.entity.fetchMPArea(1.0F), (double)this.entity.fetchMPArea(0.1F), (double)this.entity.fetchMPArea(1.0F)).offset(dx, (double)(-this.entity.fetchMPArea(0.5F)), dz));
            if(found11 != null && !found11.isEmpty()) {
                for(int i1 = 0; i1 < found11.size(); ++i1) {
                    Entity mobRef1 = (Entity)found11.get(i1);
                    if(this.entity.allowStrikeGiant(mobRef1.getClass())) {
                        this.entity.strikeMobChoosen(mobRef1, f, i);
                        if(this.entity.armedIs()) {
                            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:slashFlesh", this.entity.world, mobRef1, mobRef1.height, 1.0F);
                        }
                    }
                }
            }
        }
    }
}
