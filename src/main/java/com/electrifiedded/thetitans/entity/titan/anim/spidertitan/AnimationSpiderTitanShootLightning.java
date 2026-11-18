package com.electrifiedded.thetitans.entity.titan.anim.spidertitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityGammaLightning;
import com.electrifiedded.thetitans.entity.titan.EntitySpiderTitan;

public class AnimationSpiderTitanShootLightning extends AIAnimation {
    private EntitySpiderTitan entity;

    public AnimationSpiderTitanShootLightning(EntitySpiderTitan test) {
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
        return 140;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if (this.entity.getAttackTarget() != null)
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        if ((this.entity.getAnimTick() == 68))
            this.entity.world.addWeatherEffect(new EntityGammaLightning(this.entity.world, this.entity.posX , this.entity.posY + 3.0D, this.entity.posZ, 0.6F, 0.1F, 0.2F));
    }
}
