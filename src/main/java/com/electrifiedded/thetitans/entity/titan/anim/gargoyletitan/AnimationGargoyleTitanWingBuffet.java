package com.electrifiedded.thetitans.entity.titan.anim.gargoyletitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityGargoyleTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class AnimationGargoyleTitanWingBuffet extends AIAnimation {
    private EntityGargoyleTitan entity;

    public AnimationGargoyleTitanWingBuffet(EntityGargoyleTitan test) {
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
        return 140;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > 140 ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if (this.entity.getAttackTarget() != null)
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        if (this.entity.getAnimTick() == 16) {
            entity.appendVelocityGiant(-MathHelper.sin(entity.renderYawOffset * 3.1415927F / 180.0F) * -5F, 1D, MathHelper.cos(entity.renderYawOffset * 3.1415927F / 180.0F) * -5F);
            double d8 = 24D;
            Vec3d vec3 = this.entity.getLook(1.0F);
            double dx = vec3.x * d8;
            double dz = vec3.z * d8;
            this.entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
            this.entity.fetchAmountKnockback();
            List<?> found11 = this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity, this.entity.getEntityBoundingBox().grow(64D, 16D, 64D).offset(dx, 0.0D, dz));
            if ((found11 != null) && (!found11.isEmpty())) {
                for (Object mobRef1Obj : found11) {
                    Entity mobRef1 = (Entity) mobRef1Obj;
                    if (this.entity.allowStrikeGiant(mobRef1.getClass())) {
                        mobRef1.motionY += entity.getRNG().nextDouble();
                        if (mobRef1 instanceof EntityTitan)
                            ((EntityTitan)mobRef1).appendVelocityGiant(-MathHelper.sin(entity.renderYawOffset * 3.1415927F / 180.0F) * 15F, 2D, MathHelper.cos(entity.renderYawOffset * 3.1415927F / 180.0F) * 15F);
                        mobRef1.addVelocity(-MathHelper.sin(entity.renderYawOffset * 3.1415927F / 180.0F) * 15F, 2D, MathHelper.cos(entity.renderYawOffset * 3.1415927F / 180.0F) * 15F);
                    }
                }
            }
        }
    }
}
