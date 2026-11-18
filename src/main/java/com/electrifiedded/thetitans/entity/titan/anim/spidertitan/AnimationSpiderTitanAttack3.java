package com.electrifiedded.thetitans.entity.titan.anim.spidertitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntitySpiderTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class AnimationSpiderTitanAttack3 extends AIAnimation {
    private EntitySpiderTitan entity;

    public AnimationSpiderTitanAttack3(EntitySpiderTitan test) {
        super(test);
        entity = test;
    }

    public int getAnimID() {
        return 9;
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return 50;
    }

    public boolean shouldContinueExecuting() {
        return entity.getAnimTick() > getDuration() || entity.stunnedIs ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if(this.entity.getAnimTick() < 26 && this.entity.getAttackTarget() != null) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        }

        if(this.entity.getAnimTick() == 26) {
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSlam", this.entity.world, this.entity, 20.0F, 1.0F);
            this.entity.performStrikeArea(this.entity, this.entity.head, 10.0D, 1.0F + this.entity.getRNG().nextFloat(), 2.0F, 4, 0.75F, 0.75F, 0.75F, false);
        }
    }
}
