package com.electrifiedded.thetitans.entity.titan.anim.zombietitan;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thetitans.entity.titan.EntityGammaLightning;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class AnimationZombieTitan5LightningAttack extends AIAnimation {
    private EntityZombieTitan entity;

    public AnimationZombieTitan5LightningAttack(EntityZombieTitan test) {
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
        return 110;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.stunnedIs ? false : super.shouldContinueExecuting();
    }

    public void updateTask() {
        if (this.entity.getAttackTarget() != null)
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 0.0F);
        if (entity.isClient() && entity.getAnimTick() == 34)
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:lightningCharge", entity.world, entity, 100F, 1F);
        if (entity.getAnimTick() <= 46 && entity.getAnimTick() >= 26) {
            float ex = 1.1875F * entity.width;
            float fl = this.entity.renderYawOffset * 3.1415927F / 180.0F;
            float fl1 = MathHelper.sin(fl);
            float fl2 = MathHelper.cos(fl);
            EntityGammaLightning lightning1 = new EntityGammaLightning(this.entity.world, this.entity.posX - fl2 * ex, this.entity.posY + (0.8125D * entity.height), this.entity.posZ - fl1 * ex, 0.0F, 0.56F, 0F);
            lightning1.setRed(0F);
            lightning1.setGreen(0.56F);
            lightning1.setBlue(0F);
            lightning1.setPosition(this.entity.posX - fl2 * ex, this.entity.posY + (0.8125D * entity.height), this.entity.posZ - fl1 * ex);
            EntityGammaLightning lightning2 = new EntityGammaLightning(this.entity.world, this.entity.posX + fl2 * ex, this.entity.posY + (0.8125D * entity.height), this.entity.posZ + fl1 * ex, 0.0F, 0.56F, 0F);
            lightning2.setRed(0F);
            lightning2.setGreen(0.56F);
            lightning2.setBlue(0F);
            lightning2.setPosition(this.entity.posX + fl2 * ex, this.entity.posY + (0.8125D * entity.height), this.entity.posZ + fl1 * ex);
            this.entity.world.addWeatherEffect(lightning1);
            this.entity.world.addWeatherEffect(lightning2);
            if (entity.getAttackTarget() == null)
                this.entity.heal(50F);
        }

        if (entity.getAnimTick() == 64) {
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:lightningThrow", this.entity.world, this.entity, 100F, 1F);
            double d8 = 1.25F * entity.width;
            Vec3d vec3 = entity.getLook(1.0F);
            double dx = vec3.x * d8;
            double dz = vec3.z * d8;
            this.entity.world.newExplosion(entity, this.entity.posX + dx, this.entity.posY + (0.8125D * entity.height), this.entity.posZ + dz, 1.0F, false, false);
            for (int i = 0; i <= 4; ++i) {
                EntityGammaLightning lightning = new EntityGammaLightning(this.entity.world, this.entity.posX + dx, this.entity.posY + (entity.isChild() ? 18D : 36D), this.entity.posZ + dz, 0.0F, 0.56F, 0F);
                lightning.setRed(0F);
                lightning.setGreen(0.56F);
                lightning.setBlue(0F);
                lightning.setPosition(this.entity.posX + dx, this.entity.posY + (0.5625F * entity.height), this.entity.posZ + dz);
                this.entity.world.addWeatherEffect(lightning);
            }

            if (entity.getAttackTarget() != null) {
                EntityGammaLightning lightning = new EntityGammaLightning(this.entity.world, entity.getAttackTarget().posX, entity.getAttackTarget().posY, entity.getAttackTarget().posZ, 0.0F, 0.56F, 0F);
                lightning.setRed(0F);
                lightning.setGreen(0.56F);
                lightning.setBlue(0F);
                lightning.setPosition(entity.getAttackTarget().posX, entity.getAttackTarget().posY, entity.getAttackTarget().posZ);
                this.entity.world.addWeatherEffect(lightning);
                float da = (float)entity.fetchAmountStrike(3.0D);
                int i = entity.fetchAmountKnockback();
                entity.strikeMobChoosen(entity.getAttackTarget(), da, i);
                EntityLivingBase target = entity.getAttackTarget();
                target.motionY += 1.0F + this.entity.getRNG().nextFloat();
            }
        }
    }
}
