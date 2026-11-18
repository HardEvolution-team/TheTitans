package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.registry.TitanAudio;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityHomingWitherSkull extends EntityWitherSkull {
    public int lifetime;
    public int explosivePower;
    public int harmExtra;
    public float factorPace;
    public Entity mobAssgined;

    public EntityHomingWitherSkull(World worldIn) {
        super(worldIn);
        setSize(0.5F, 0.5F);
    }

    public EntityHomingWitherSkull(World worldIn, EntityLivingBase p_i1794_2_, double p_i1794_3_, double p_i1794_5_, double p_i1794_7_) {
        super(worldIn, p_i1794_2_, p_i1794_3_, p_i1794_5_, p_i1794_7_);
        setSize(0.5F, 0.5F);
    }

    @SideOnly(Side.CLIENT)
    public EntityHomingWitherSkull(World worldIn, double p_i1795_2_, double p_i1795_4_, double p_i1795_6_, double p_i1795_8_, double p_i1795_10_, double p_i1795_12_) {
        // 1.12.2 EntityWitherSkull has no 6-double client ctor; shim super(World) + position + normalized acceleration
        super(worldIn);
        setSize(0.5F, 0.5F);
        this.setLocationAndAngles(p_i1795_2_, p_i1795_4_, p_i1795_6_, this.rotationYaw, this.rotationPitch);
        this.setPosition(p_i1795_2_, p_i1795_4_, p_i1795_6_);
        double d0 = MathHelper.sqrt(p_i1795_8_ * p_i1795_8_ + p_i1795_10_ * p_i1795_10_ + p_i1795_12_ * p_i1795_12_);
        this.accelerationX = p_i1795_8_ / d0 * 0.1D;
        this.accelerationY = p_i1795_10_ / d0 * 0.1D;
        this.accelerationZ = p_i1795_12_ / d0 * 0.1D;
    }

    protected void onImpact(RayTraceResult movingObject) {
        if (!this.world.isRemote) {
            if (movingObject.entityHit != null) {
                if (this.shootingEntity != null) {
                    if ((movingObject.entityHit instanceof EntityLivingBase)) {
                        ((EntityLivingBase)movingObject.entityHit).setRevengeTarget(this.shootingEntity);
                        movingObject.entityHit.hurtResistantTime = 5;
                        if (((movingObject.entityHit.height >= 6.0F) || (((EntityLivingBase)movingObject.entityHit).getTotalArmorValue() > 24)) && (!(movingObject.entityHit instanceof EntityTitan)) && (!(movingObject.entityHit instanceof EntityDragon)) && (!(movingObject.entityHit instanceof MultiPartEntityPart)) && (!(movingObject.entityHit instanceof EntityPlayer))) {
                            movingObject.entityHit.attackEntityFrom(DamageSourceExtra.destroy, Float.MAX_VALUE);
                        }
                    }

                    if (((movingObject.entityHit instanceof EntityDragon)) || ((movingObject.entityHit instanceof MultiPartEntityPart))) {
                        if (movingObject.entityHit.height >= 6.0F) {
                            movingObject.entityHit.attackEntityFrom(DamageSourceExtra.causeSkullHarmVSEnderDragonHoming(this.shootingEntity), 60000.0F + this.harmExtra * 30);
                            movingObject.entityHit.playSound(TitanAudio.get("thetitans:titanpunch"), 10.0F, 1.0F);
                        }

                        else {
                            movingObject.entityHit.attackEntityFrom(DamageSourceExtra.causeSkullHarmVSEnderDragonHoming(this.shootingEntity), 6000.0F + this.harmExtra);
                        }
                    }

                    else if (((movingObject.entityHit instanceof EntityLivingBase)) && (((EntityLivingBase)movingObject.entityHit).getCreatureAttribute() == EnumCreatureAttribute.UNDEAD)) {
                        if ((movingObject.entityHit.height >= 6.0F) || (((EntityLivingBase)movingObject.entityHit).getTotalArmorValue() > 24)) {
                            movingObject.entityHit.attackEntityFrom(DamageSourceExtra.radiation, 60000.0F + this.harmExtra * 30);
                            movingObject.entityHit.playSound(TitanAudio.get("thetitans:titanpunch"), 10.0F, 1.0F);
                        }

                        else {
                            movingObject.entityHit.attackEntityFrom(DamageSourceExtra.radiation, 6000.0F + this.harmExtra);
                        }
                    }

                    else if (movingObject.entityHit.height >= 6.0F) {
                        movingObject.entityHit.attackEntityFrom(DamageSourceExtra.causeSkullHarmHoming(this.shootingEntity), 60000.0F + this.harmExtra * 30);
                        movingObject.entityHit.playSound(TitanAudio.get("thetitans:titanpunch"), 10.0F, 1.0F);
                    }

                    else {
                        movingObject.entityHit.attackEntityFrom(DamageSourceExtra.causeSkullHarmHoming(this.shootingEntity), 6000.0F + this.harmExtra);
                    }
                }

                else {
                    movingObject.entityHit.attackEntityFrom(DamageSource.MAGIC, 6000.0F);
                }

                if ((movingObject.entityHit instanceof EntityLivingBase)) {
                    byte b0 = 20;
                    if (this.world.getDifficulty() == EnumDifficulty.NORMAL) {
                        b0 = 40;
                    }

                    else if (this.world.getDifficulty() == EnumDifficulty.HARD) {
                        b0 = 80;
                    }

                    if (b0 > 0) {
                        ((EntityLivingBase)movingObject.entityHit).addPotionEffect(new PotionEffect(MobEffects.WITHER, 20 * b0, 1));
                    }
                }
            }

            this.world.newExplosion(this, this.posX, this.posY, this.posZ, 2.0F + this.explosivePower, false, this.world.getGameRules().getBoolean("mobGriefing"));
            setDead();
        }
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public void onUpdate() {
        this.lifetime += 1;
        if ((this.mobAssgined != null) && (this.mobAssgined.isEntityAlive())) {
            double d0 = this.mobAssgined.posX - this.posX;
            double d1 = this.mobAssgined.posY + (this.mobAssgined instanceof EntityTitan ? (this.mobAssgined.height * 0.5D) : this.mobAssgined.getEyeHeight()) - this.posY;
            double d2 = this.mobAssgined.posZ - this.posZ;
            float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
            this.motionX = (d0 / f2 * (getMotionFactor() * 0.75F) * (getMotionFactor() * 0.75F) + this.motionX * 0.75F);
            this.motionY = (d1 / f2 * (getMotionFactor() * 0.75F) * (getMotionFactor() * 0.75F) + this.motionY * 0.75F);
            this.motionZ = (d2 / f2 * (getMotionFactor() * 0.75F) * (getMotionFactor() * 0.75F) + this.motionZ * 0.75F);
            if (this.getDistanceSq(this.mobAssgined.posX, this.mobAssgined.posY + (this.mobAssgined instanceof EntityTitan ? (this.mobAssgined.height * 0.5D) : this.mobAssgined.getEyeHeight()), this.mobAssgined.posZ) < 9D) {
                this.onImpact(new RayTraceResult(this.mobAssgined));
                this.world.newExplosion(this, this.posX, this.posY, this.posZ, 2.0F + this.explosivePower, false, false);
                java.util.List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(6.0D, 6.0D, 6.0D));
                if (list != null && !list.isEmpty()) {
                    for (Entity entity : list) {
                        this.onImpact(new RayTraceResult(entity));
                    }
                }

                setDead();
            }
        }

        if (this.lifetime >= 200) {
            this.world.newExplosion(this, this.posX, this.posY, this.posZ, 2.0F + this.explosivePower, false, this.world.getGameRules().getBoolean("mobGriefing"));
            setDead();
        }

        super.onUpdate();
        this.setPosition(this.posX, this.posY, this.posZ);
    }
}
