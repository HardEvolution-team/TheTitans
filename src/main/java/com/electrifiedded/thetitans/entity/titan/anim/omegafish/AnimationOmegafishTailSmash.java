package com.electrifiedded.thetitans.entity.titan.anim.omegafish;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntitySilverfishTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;

import java.util.List;

public class AnimationOmegafishTailSmash extends AIAnimation {
    private EntitySilverfishTitan entity;

    public AnimationOmegafishTailSmash(EntitySilverfishTitan test) {
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
        return 60;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if(this.entity.getAnimTick() < 36 && this.entity.getAttackTarget() != null) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        }

        if(this.entity.getAnimTick() == 36) {
            double d8 = (double)this.entity.fetchMPArea(8.0F);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:groundSmash", this.entity.world, this.entity, 20.0F, 1.5F);
            this.entity.performStrikeArea(this.entity, this.entity.body, 10.0D, 2.0F + this.entity.getRNG().nextFloat(), 8.0F, 4, 0.0F, 4.0F, 1.5F, false);
            java.util.List found11 = this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity, this.entity.getEntityBoundingBox().expand(d8, d8 / 8.0D, d8).offset(0.0D, -d8 / 8.0D, 0.0D));
            if(found11 != null && !found11.isEmpty()) {
                for(int i1 = 0; i1 < found11.size(); ++i1) {
                    Entity mobRef1 = (Entity)found11.get(i1);
                    if(this.entity.allowStrikeGiant(mobRef1.getClass())) {
                        float f = (float)this.entity.fetchAmountStrike(8.0D);
                        int i = this.entity.fetchAmountKnockback();
                        this.entity.strikeMobChoosen(mobRef1, f, i);
                    }
                }
            }
        }
    }
}
