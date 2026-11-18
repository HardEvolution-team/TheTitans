package com.electrifiedded.thetitans.entity.titan.ai;

import com.electrifiedded.thetitans.entity.titan.EntityTitan;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAITitanWatchClosest extends net.minecraft.entity.ai.EntityAIBase {
    private EntityTitan watcherThe;
    protected Entity closestEntity;
    private float gapForUserCap;
    private int lookTime;
    private float fld75331e;
    private Class<?> watchedClass;

    public EntityAITitanWatchClosest(EntityTitan p_i1631_1_, Class<?> p_i1631_2_, float p_i1631_3_) {
        this.watcherThe = p_i1631_1_;
        this.watchedClass = p_i1631_2_;
        this.gapForUserCap = p_i1631_3_;
        this.fld75331e = 0.025F;
        setMutexBits(2);
    }

    public EntityAITitanWatchClosest(EntityTitan p_i1632_1_, Class<?> p_i1632_2_, float p_i1632_3_, float p_i1632_4_) {
        this.watcherThe = p_i1632_1_;
        this.watchedClass = p_i1632_2_;
        this.gapForUserCap = p_i1632_3_;
        this.fld75331e = p_i1632_4_;
        setMutexBits(2);
    }

    @SuppressWarnings("unchecked")
    public boolean shouldExecute() {
        if (this.watcherThe.getRNG().nextFloat() >= this.fld75331e) {
            return false;
        }

        if (this.watcherThe.motionX != 0 || this.watcherThe.motionZ != 0) {
            return false;
        }

        if (this.watchedClass == EntityPlayer.class) {
            this.closestEntity = this.watcherThe.world.getClosestPlayerToEntity(this.watcherThe, this.gapForUserCap);
        }

        else {
            this.closestEntity = this.watcherThe.world.findNearestEntityWithinAABB((Class<Entity>)this.watchedClass, this.watcherThe.getEntityBoundingBox().grow(this.gapForUserCap, this.gapForUserCap, this.gapForUserCap), this.watcherThe);
        }

        return !this.watcherThe.fetchWaiting() && this.watcherThe.getAnimID() != 13 && (this.watcherThe.getAttackTarget() == null) && (this.closestEntity != null);
    }

    public boolean shouldContinueExecuting() {
        return this.closestEntity.isEntityAlive() && this.lookTime > 0;
    }

    public void startExecuting() {
        this.lookTime = (40 + this.watcherThe.getRNG().nextInt(40));
        if (closestEntity instanceof EntityGiantZombie)
            ((EntityGiantZombie)closestEntity).faceEntity(watcherThe, 10F, 40F);
    }

    public void resetTask() {
        this.closestEntity = null;
    }

    public void updateTask() {
        if (watcherThe.getAttackTarget() != null)
            closestEntity = watcherThe.getAttackTarget();
        this.watcherThe.getLookHelper().setLookPosition(this.closestEntity.posX, this.closestEntity.posY + this.closestEntity.getEyeHeight(), this.closestEntity.posZ, 64F / this.watcherThe.fetchScaleMultiplierGiant(), this.watcherThe.getVerticalFaceSpeed());
        this.lookTime -= 1;
    }
}
