package com.electrifiedded.thetitans.entity.mob;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityThunderboltShot extends EntityFireball {
    public EntityThunderboltShot(World worldIn) {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
    }

    public EntityThunderboltShot(World worldIn, EntityLivingBase shooter) {
        super(worldIn);
        this.shootingEntity = shooter;
        this.setSize(1.0F, 1.0F);
    }

    public EntityThunderboltShot(World worldIn, EntityLivingBase shooter, int unused) {
        this(worldIn, shooter);
    }

    public EntityThunderboltShot(World worldIn, double x, double y, double z) {
        super(worldIn);
        this.setPosition(x, y, z);
        this.setSize(1.0F, 1.0F);
    }

    protected void onImpact(RayTraceResult result) {
        if (result.entityHit == null || this.shootingEntity == null || result.entityHit != this.shootingEntity) {
            if (!this.world.isRemote && result.entityHit != null && this.shootingEntity != null && result.entityHit != this.shootingEntity) {
                result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.shootingEntity), 50.0F);
                result.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 50.0F);
                result.entityHit.setFire(15);
                ++result.entityHit.motionY;
            }

            this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.5F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.5F);
            if (!this.world.isRemote) {
                this.world.createExplosion(this.shootingEntity != null ? this.shootingEntity : this, this.posX, this.posY, this.posZ, 3.0F, this.world.getGameRules().getBoolean("mobGriefing"));
                this.world.addWeatherEffect(new EntityLightningBolt(this.world, this.posX, this.posY + 1.0D, this.posZ, false));
            }

            this.setDead();
        }
    }

    public void onUpdate() {
        super.onUpdate();
        for (int i = 0; i < 4; ++i) {
            this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.posX, this.posY, this.posZ, this.rand.nextGaussian() / 10.0D, this.rand.nextGaussian() / 10.0D, this.rand.nextGaussian() / 10.0D);
        }
    }
}
