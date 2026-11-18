package com.electrifiedded.thehippomaster.AnimationAPI;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public abstract class AIAnimation extends EntityAIBase {
    public AIAnimation(IAnimatedEntity entity) {
        mobAnimated = entity;
        setMutexBits(7);
    }

    public abstract int getAnimID();

    public EntityLiving getEntity() {
        return (EntityLiving)mobAnimated;
    }

    public abstract boolean isAutomatic();
    public abstract int getDuration();

    public boolean shouldAnimate() {
        return false;
    }

    public boolean shouldExecute() {
        if(isAutomatic()) return mobAnimated.getAnimID() == getAnimID();
        return shouldAnimate();
    }

    public void startExecuting() {
        if(!isAutomatic()) AnimationAPI.sendAnimPacket(mobAnimated, getAnimID());
        mobAnimated.setAnimTick(0);
    }

    public boolean shouldContinueExecuting() {
        return mobAnimated.getAnimTick() < getDuration() && getEntity().ticksExisted > 0;
    }

    public void resetTask() {
        AnimationAPI.sendAnimPacket(mobAnimated, 0);
        mobAnimated.setAnimTick(0);
    }

    private IAnimatedEntity mobAnimated;
}
