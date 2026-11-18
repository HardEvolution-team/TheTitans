package com.electrifiedded.thetitans.entity.titan.anim.omegafish;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntitySilverfishTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class AnimationOmegafishAttack3 extends AIAnimation {
    private EntitySilverfishTitan entity;

    public AnimationOmegafishAttack3(EntitySilverfishTitan test) {
        super(test);
        this.entity = test;
    }

    public int getAnimID() {
        return 6;
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return 60;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if ((this.entity.getAttackTarget() != null)) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        }

        float f = (float)this.entity.fetchAmountStrike(2.0D);
        int i = this.entity.fetchAmountKnockback();

        if (this.entity.getAnimTick() == 24) {
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSwing", this.entity.world, this.entity, 10F, 1.75F);
            double d8 = this.entity.fetchMPArea(0.5F);
            Vec3d vec3 = this.entity.getLook(1.0F);
            double dx = vec3.x * d8;
            double dz = vec3.z * d8;
            List<?> found11 = this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity, this.entity.getEntityBoundingBox().expand(1.0D, 1.0D, 1.0D).offset(dx, 0.0D, dz));
            if ((found11 != null) && (!found11.isEmpty())) {
                for (Object mobRef1Obj : found11) {
                    Entity mobRef1 = (Entity) mobRef1Obj;
                    if (this.entity.allowStrikeGiant(mobRef1.getClass())) {
                        mobRef1.world.playSound(null, this.entity.posX + dx, this.entity.posY, this.entity.posZ + dz, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 4.0F, (1.0F + (this.entity.world.rand.nextFloat() - this.entity.world.rand.nextFloat()) * 0.2F) * 0.7F);
                        this.entity.strikeMobChoosen(mobRef1, f * 2.0F, i);
                        mobRef1.addVelocity(-MathHelper.sin(this.entity.rotationYawHead * 3.1415927F / 180.0F) * i, i * 0.5D, MathHelper.cos(this.entity.rotationYawHead * 3.1415927F / 180.0F) * i);
                    }
                }
            }
        }

        if (this.entity.getAnimTick() == 26) {
            double d8 = this.entity.fetchMPArea(1.0F);
            Vec3d vec3 = this.entity.getLook(1.0F);
            double dx = vec3.x * d8;
            double dz = vec3.z * d8;
            List<?> found11 = this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity, this.entity.getEntityBoundingBox().expand(1.0D, 1.0D, 1.0D).offset(dx, 0.0D, dz));
            if ((found11 != null) && (!found11.isEmpty())) {
                for (Object mobRef1Obj : found11) {
                    Entity mobRef1 = (Entity) mobRef1Obj;
                    if (this.entity.allowStrikeGiant(mobRef1.getClass())) {
                        mobRef1.world.playSound(null, this.entity.posX + dx, this.entity.posY, this.entity.posZ + dz, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 4.0F, (1.0F + (this.entity.world.rand.nextFloat() - this.entity.world.rand.nextFloat()) * 0.2F) * 0.7F);
                        this.entity.strikeMobChoosen(mobRef1, f * 2.0F, i);
                        mobRef1.addVelocity(-MathHelper.sin(this.entity.rotationYawHead * 3.1415927F / 180.0F) * i, i * 0.5D, MathHelper.cos(this.entity.rotationYawHead * 3.1415927F / 180.0F) * i);
                    }
                }
            }
        }

        if (this.entity.getAnimTick() == 28) {
            double d8 = this.entity.fetchMPArea(1.5F);
            Vec3d vec3 = this.entity.getLook(1.0F);
            double dx = vec3.x * d8;
            double dz = vec3.z * d8;
            List<?> found11 = this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity, this.entity.getEntityBoundingBox().expand(1.0D, 1.0D, 1.0D).offset(dx, 0.0D, dz));
            if ((found11 != null) && (!found11.isEmpty())) {
                for (Object mobRef1Obj : found11) {
                    Entity mobRef1 = (Entity) mobRef1Obj;
                    if (this.entity.allowStrikeGiant(mobRef1.getClass())) {
                        mobRef1.world.playSound(null, this.entity.posX + dx, this.entity.posY, this.entity.posZ + dz, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 4.0F, (1.0F + (this.entity.world.rand.nextFloat() - this.entity.world.rand.nextFloat()) * 0.2F) * 0.7F);
                        this.entity.strikeMobChoosen(mobRef1, f * 2.0F, i);
                        mobRef1.addVelocity(-MathHelper.sin(this.entity.rotationYawHead * 3.1415927F / 180.0F) * i, i * 0.5D, MathHelper.cos(this.entity.rotationYawHead * 3.1415927F / 180.0F) * i);
                    }
                }
            }
        }

        if (this.entity.getAnimTick() == 30) {
            double d8 = this.entity.fetchMPArea(2.0F);
            Vec3d vec3 = this.entity.getLook(1.0F);
            double dx = vec3.x * d8;
            double dz = vec3.z * d8;
            List<?> found11 = this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity, this.entity.getEntityBoundingBox().expand(1.0D, 1.0D, 1.0D).offset(dx, 0.0D, dz));
            if ((found11 != null) && (!found11.isEmpty())) {
                for (Object mobRef1Obj : found11) {
                    Entity mobRef1 = (Entity) mobRef1Obj;
                    if (this.entity.allowStrikeGiant(mobRef1.getClass())) {
                        mobRef1.world.playSound(null, this.entity.posX + dx, this.entity.posY, this.entity.posZ + dz, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 4.0F, (1.0F + (this.entity.world.rand.nextFloat() - this.entity.world.rand.nextFloat()) * 0.2F) * 0.7F);
                        this.entity.strikeMobChoosen(mobRef1, f * 2.0F, i);
                        mobRef1.addVelocity(-MathHelper.sin(this.entity.rotationYawHead * 3.1415927F / 180.0F) * i, i * 0.5D, MathHelper.cos(this.entity.rotationYawHead * 3.1415927F / 180.0F) * i);
                    }
                }
            }
        }
    }
}
