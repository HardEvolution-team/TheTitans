package com.electrifiedded.thetitans.entity.mob;

import com.electrifiedded.thetitans.entity.deity.EntityDeity;
import com.electrifiedded.thetitans.registry.TitanAudio;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

public class EntityIceBall extends EntityFireball {
    public EntityIceBall(World worldIn) {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
    }

    public EntityIceBall(World worldIn, EntityLivingBase shooter) {
        super(worldIn);
        this.shootingEntity = shooter;
        this.setSize(1.0F, 1.0F);
    }

    public EntityIceBall(World worldIn, EntityLivingBase shooter, int unused) {
        this(worldIn, shooter);
    }

    public EntityIceBall(World worldIn, double x, double y, double z) {
        super(worldIn);
        this.setPosition(x, y, z);
        this.setSize(1.0F, 1.0F);
    }

    protected void onImpact(RayTraceResult result) {
        if (result.entityHit == null || this.shootingEntity == null || result.entityHit != this.shootingEntity) {
            if (result.entityHit != null && this.shootingEntity != null) {
                float damage = 50.0F;
                if (this.shootingEntity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE) != null) {
                    damage = (float)this.shootingEntity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
                }

                if (result.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), damage)) {
                    ++result.entityHit.motionY;
                }
            }

            if (!this.world.isRemote) {
                this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.5F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.5F);
                this.world.createExplosion(this.shootingEntity != null ? this.shootingEntity : this, this.posX, this.posY, this.posZ, 2.0F, this.world.getGameRules().getBoolean("mobGriefing"));
                this.playSound(com.electrifiedded.thetitans.registry.TitanAudio.get("funnyExplosion"), 5.0F, 1.0F);
                this.setDead();
            }
        }
    }

    public void onUpdate() {
        super.onUpdate();
        for (int i = 0; i < 4; ++i) {
            this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.posX, this.posY, this.posZ, this.rand.nextGaussian() / 10.0D, this.rand.nextGaussian() / 10.0D, this.rand.nextGaussian() / 10.0D);
        }

        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(5.0D, 5.0D, 5.0D));
        if (this.shootingEntity != null && !list.isEmpty()) {
            for (Entity entity : list) {
                if (entity.isEntityAlive() && entity instanceof EntityLivingBase && entity.getClass() != this.shootingEntity.getClass() && !(entity instanceof EntityDeity)) {
                    this.onImpact(new RayTraceResult(entity));
                }
            }
        }

        if (this.posY <= -20.0D || this.shootingEntity != null && (double)this.getDistance(this.shootingEntity) >= 100.0D) {
            this.playSound(com.electrifiedded.thetitans.registry.TitanAudio.get("funnyExplosion"), 5.0F, 1.0F);
            this.setDead();
        }
    }
}
