package com.electrifiedded.thetitans.entity.titan.anim.gargoyletitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityGargoyleTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class AnimationGargoyleTitanAttack1 extends AIAnimation {
    private EntityGargoyleTitan entity;

    public AnimationGargoyleTitanAttack1(EntityGargoyleTitan test) {
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
        return 70;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > 70 ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if ((this.entity.getAnimTick() < 30) && (this.entity.getAttackTarget() != null)) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        }

        if (this.entity.getAnimTick() == 28)
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSwing", this.entity.world, this.entity, 10F, 1F);
        if (this.entity.getAnimTick() == 32) {
            double d8 = 12.0D;
            Vec3d vec3 = this.entity.getLook(1.0F);
            double dx = vec3.x * d8;
            double dz = vec3.z * d8;
            float f = (float)entity.fetchAmountStrike(1.0F);
            int i = this.entity.fetchAmountKnockback();
            this.entity.shakeUserCamerasNearby(10D);
            List<?> found11 = this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity, this.entity.getEntityBoundingBox().expand(32D, 32D, 32D).offset(dx, -16D, dz));
            if ((found11 != null) && (!found11.isEmpty())) {
                for (Object mobRef1Obj : found11) {
                    Entity mobRef1 = (Entity) mobRef1Obj;
                    if (this.entity.allowStrikeGiant(mobRef1.getClass())) {
                        this.entity.strikeMobChoosen(mobRef1, f, i);
                    }
                }
            }

            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanStrike", this.entity.world, this.entity, 20.0F, 1.0F);
        }
    }
}
