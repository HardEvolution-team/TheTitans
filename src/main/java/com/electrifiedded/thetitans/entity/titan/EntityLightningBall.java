package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.core.TheCore;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.minion.EntityDragonMinion;
import com.electrifiedded.thetitans.entity.minion.EntityEndermanMinion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

public class EntityLightningBall extends EntityFireball {
    public EntityLightningBall(World worldIn) {
        super(worldIn);
        this.setSize(4.0F, 4.0F);
    }

    public EntityLightningBall(World worldIn, EntityLivingBase p_i1771_2_, double p_i1771_3_, double p_i1771_5_, double p_i1771_7_) {
        super(worldIn, p_i1771_2_, p_i1771_3_, p_i1771_5_, p_i1771_7_);
        this.setSize(4.0F, 4.0F);
    }

    public EntityLightningBall(World worldIn, double p_i1772_2_, double p_i1772_4_, double p_i1772_6_, double p_i1772_8_, double p_i1772_10_, double p_i1772_12_) {
        super(worldIn, p_i1772_2_, p_i1772_4_, p_i1772_6_, p_i1772_8_, p_i1772_10_, p_i1772_12_);
        this.setSize(4.0F, 4.0F);
    }

    protected void onImpact(RayTraceResult movingObject) {
        if (!this.world.isRemote) {
            float f = 2000.0F;
            if ((movingObject.entityHit != null)) {
                movingObject.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), f);
                boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
                this.world.newExplosion((Entity)null, this.posX, this.posY, this.posZ, 7.0F, true, flag);
            }

            else {
                playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);
            }

            setDead();
        }
    }

    public void onUpdate() {
        super.onUpdate();
        this.setFire(4);
        if (this.ticksExisted % 600 == 0) {
            boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
            this.world.newExplosion((Entity)null, this.posX, this.posY, this.posZ, 7.0F, true, flag);
            setDead();
        }

        if (this.rand.nextInt(30) == 0)
            this.world.addWeatherEffect(new EntityGammaLightning (this.world, this.posX, this.posY, this.posZ, 1F, 0F, 1F));
        List<?> found11 = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(8.0D, 8.0D, 8.0D));
        if ((found11 != null) && (!found11.isEmpty()) && this.shootingEntity != null) {
            for (Object entityObj : found11) {
                Entity entity = (Entity) entityObj;
                if (entity != null && entity.isEntityAlive() && ((entity instanceof EntityLivingBase)) && this.shootingEntity instanceof EntityEnderColossus && !(entity instanceof EntityEndermanMinion) && !(entity instanceof EntityEnderColossus) && !(entity instanceof EntityDragon) && !(entity instanceof EntityDragonMinion) && !(entity instanceof EntityEnderColossusCrystal)) {
                    entity.setFire(15);
                    entity.attackEntityFrom(DamageSourceExtra.lightningBolt, 100.0F);
                    this.world.addWeatherEffect(new EntityGammaLightning (this.world, this.posX, this.posY, this.posZ, 1F, 0F, 1F));
                    this.world.addWeatherEffect(new EntityGammaLightning (this.world, entity.posX, entity.posY, entity.posZ, 1F, 0F, 1F));
                    ((EntityEnderColossus)this.shootingEntity).strikeMobChoosen(entity, 5F, 1);
                }
            }
        }
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean isBurning() {
        return false;
    }

    protected float getMotionFactor() {
        return 0.75F;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }
}
