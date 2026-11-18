package com.electrifiedded.thetitans.entity.titan.anim.endercolossus;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossus;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;

import java.util.List;

public class AnimationEnderColossusTelekineticGrab extends AIAnimation {
    private EntityEnderColossus entity;

    public AnimationEnderColossusTelekineticGrab(EntityEnderColossus test) {
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
        return 150;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if (this.entity.getAnimTick() > 150) {
            this.entity.setAttackTarget(null);
        }

        if ((this.entity.getAttackTarget() != null)) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        }

        if ((this.entity.getAnimTick() == 60)) {
            float f = (float)entity.fetchAmountStrike(1.0F);
            int i = 0;
            this.entity.collideWithEntities(this.entity.body, this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity, this.entity.body.getEntityBoundingBox().expand(16.0D, 8.0D, 16.0D)));
            List<?> found11 = this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity, this.entity.getEntityBoundingBox().expand(32.0D, 8.0D, 32.0D));
            if ((found11 != null) && (!found11.isEmpty())) {
                for (Object mobRef1Obj : found11) {
                    Entity mobRef1 = (Entity) mobRef1Obj;
                    if (this.entity.allowStrikeGiant(mobRef1.getClass())) {
                        mobRef1.motionY += 2.0F + entity.getRNG().nextFloat() + entity.getRNG().nextFloat();
                        this.entity.strikeMobChoosen(mobRef1, f, i);
                    }
                }
            }

            if (entity.isClient())
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSlam", this.entity.world, this.entity, 20.0F, 1.0F);
        }
    }
}
