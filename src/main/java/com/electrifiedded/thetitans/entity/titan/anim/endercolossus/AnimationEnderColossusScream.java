package com.electrifiedded.thetitans.entity.titan.anim.endercolossus;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossus;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;

import java.util.List;

public class AnimationEnderColossusScream extends AIAnimation {
    private EntityEnderColossus entity;

    public AnimationEnderColossusScream(EntityEnderColossus test) {
        super(test);
        this.entity = test;
    }

    public int getAnimID() {
        return 5;
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return 200;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAnimTick() > this.getDuration() || this.entity.stunnedIs ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if (this.entity.getAnimTick() > 200) {
            this.entity.setAttackTarget(null);
        }

        if ((this.entity.getAnimTick() < 60) && (this.entity.getAttackTarget() != null)) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        }

        if (this.entity.getAnimTick() > 80) {
            WorldInfo worldinfo = this.entity.world.getWorldInfo();
            worldinfo.setRainTime(0);
            worldinfo.setThunderTime(0);
            worldinfo.setRaining(false);
            worldinfo.setThundering(false);
            this.entity.assignScreaming(false);
            this.entity.collideWithEntities(this.entity.body, this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity, this.entity.body.getEntityBoundingBox().expand(16.0D, 8.0D, 16.0D)));
            List<?> found11 = this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity, this.entity.getEntityBoundingBox().expand(entity.fetchScaleMultiplierGiant() * 16D, entity.fetchScaleMultiplierGiant() * 16D, entity.fetchScaleMultiplierGiant() * 16D));
            if ((found11 != null) && (!found11.isEmpty())) {
                for (Object mobRef1Obj : found11) {
                    Entity mobRef1 = (Entity) mobRef1Obj;
                    if (this.entity.allowStrikeGiant(mobRef1.getClass())) {
                        this.entity.strikeMobChoosen(mobRef1, this.entity.getAnimTick(), 0);
                        this.entity.strikeMobChoosen(mobRef1, this.entity.getAnimTick(), 0);
                        this.entity.strikeMobChoosen(mobRef1, this.entity.getAnimTick(), 0);
                        this.entity.strikeMobChoosen(mobRef1, (float)this.entity.fetchAmountStrike(10.0D), 0);
                        mobRef1.motionX = 0D;
                        mobRef1.motionZ = 0D;
                        ++mobRef1.rotationPitch;
                        if (mobRef1 instanceof EntityLivingBase) {
                            ((EntityLivingBase)mobRef1).addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 400, 1));
                            ((EntityLivingBase)mobRef1).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 400, 99));
                            ((EntityLivingBase)mobRef1).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 400, 1));
                        }
                    }
                }
            }
        }

        if (entity.isClient() && this.entity.getAnimTick() == 80)
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanEnderColossusScreamLong", this.entity.world, this.entity, Float.MAX_VALUE, 1.0F);
    }
}
