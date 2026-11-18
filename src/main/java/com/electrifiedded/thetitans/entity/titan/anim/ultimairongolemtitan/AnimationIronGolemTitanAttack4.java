package com.electrifiedded.thetitans.entity.titan.anim.ultimairongolemtitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityIronGolemTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class AnimationIronGolemTitanAttack4 extends AIAnimation {
    private EntityIronGolemTitan entity;

    public AnimationIronGolemTitanAttack4(EntityIronGolemTitan test) {
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
        return 90;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > 90 ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if ((this.entity.getAnimTick() < 40) && (this.entity.getAttackTarget() != null)) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        }

        if (entity.isClient() && this.entity.getAnimTick() == 44) {
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSwing", this.entity.world, this.entity, 100F, 1F);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("mob.irongolem.throw", this.entity.world, this.entity, 100.0F, 0.5F);
        }

        if (this.entity.getAnimTick() == 48) {
            if (entity.isClient())
                this.entity.shakeUserCamerasNearby(10D);
            double d8 = entity.fetchScaleMultiplierGiant();
            Vec3d vec3 = this.entity.getLook(1.0F);
            double dx = vec3.x * d8;
            double dz = vec3.z * d8;
            this.entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
            this.entity.fetchAmountKnockback();
            List<?> found11 = this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity, this.entity.getEntityBoundingBox().expand(32.0D, 4.0D, 32.0D).offset(dx, -2.0D, dz));
            if ((found11 != null) && (!found11.isEmpty())) {
                for (Object mobRef1Obj : found11) {
                    Entity mobRef1 = (Entity) mobRef1Obj;
                    if (this.entity.allowStrikeGiant(mobRef1.getClass())) {
                        this.entity.attackEntityAsMob(mobRef1);
                        mobRef1.addVelocity(-MathHelper.sin(this.entity.rotationYawHead * 3.1415927F / 180.0F) * 3F, 0D, MathHelper.cos(this.entity.rotationYawHead * 3.1415927F / 180.0F) * 3F);
                    }
                }
            }

            if (entity.isClient()) {
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSlam", this.entity.world, this.entity, 20.0F, 1.0F);
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanStep", this.entity.world, this.entity, 20.0F, 1.0F);
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanStep", this.entity.world, this.entity, 20.0F, 1.0F);
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanStep", this.entity.world, this.entity, 20.0F, 1.0F);
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanStep", this.entity.world, this.entity, 20.0F, 1.0F);
            }
        }
    }
}
