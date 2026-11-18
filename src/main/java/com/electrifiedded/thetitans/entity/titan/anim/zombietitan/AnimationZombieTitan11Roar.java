package com.electrifiedded.thetitans.entity.titan.anim.zombietitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

import java.util.List;

public class AnimationZombieTitan11Roar extends AIAnimation {
    private EntityZombieTitan entity;

    public AnimationZombieTitan11Roar(EntityZombieTitan test) {
        super(test);
        this.entity = test;
    }

    public int getAnimID() {
        return 11;
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
        if ((this.entity.getAnimTick() < 20) && (this.entity.getAttackTarget() != null)) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
        }

        if ((this.entity.getAnimTick() >= 20)) {
            List<?> found11 = this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity, this.entity.getEntityBoundingBox().expand(64.0D, 64.0D, 64.0D));
            if ((found11 != null) && (!found11.isEmpty())) {
                for (Object mobRef1Obj : found11) {
                    Entity mobRef1 = (Entity) mobRef1Obj;
                    if (this.entity.allowStrikeGiant(mobRef1.getClass())) {
                        mobRef1.hurtResistantTime = 0;
                    }

                    if (!entity.world.isRemote && (mobRef1 instanceof EntityLivingBase) && !this.entity.allowStrikeGiant(mobRef1.getClass())) {
                        ((EntityLivingBase)mobRef1).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20, 4));
                    }
                }
            }
        }

        if ((this.entity.getAnimTick() == 20)) {
            if (entity.isClient())
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanZombieRoar", this.entity.world, this.entity, 1000F, 1F);
            this.entity.collideWithEntities(this.entity.head, this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity, this.entity.head.getEntityBoundingBox().expand(64.0D, 64.0D, 64.0D)));
            List<?> found11 = this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity, this.entity.getEntityBoundingBox().expand(64.0D, 64.0D, 64.0D));
            if ((found11 != null) && (!found11.isEmpty())) {
                for (Object mobRef1Obj : found11) {
                    Entity mobRef1 = (Entity) mobRef1Obj;
                    if ((mobRef1 instanceof EntityLivingBase) && this.entity.allowStrikeGiant(mobRef1.getClass())) {
                        mobRef1.attackEntityFrom(DamageSource.causeThornsDamage(entity), 40F);
                        ++mobRef1.motionY;
                    }
                }
            }
        }
    }
}
