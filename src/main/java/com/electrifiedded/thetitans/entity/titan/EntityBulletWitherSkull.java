package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.effect.DamageSourceExtra;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityBulletWitherSkull extends EntityWitherSkull {
    public int lifetime;
    public int explosivePower;
    public int harmExtra;
    public float factorPace;

    protected float getMotionFactor() {
        return 0.95F;
    }

    public EntityBulletWitherSkull(World worldIn) {
        super(worldIn);
        setSize(0.3125F, 0.3125F);
    }

    public EntityBulletWitherSkull(World worldIn, EntityLivingBase p_i1794_2_, double p_i1794_3_, double p_i1794_5_, double p_i1794_7_) {
        super(worldIn, p_i1794_2_, p_i1794_3_, p_i1794_5_, p_i1794_7_);
        setSize(0.3125F, 0.3125F);
    }

    @SideOnly(Side.CLIENT)
    public EntityBulletWitherSkull(World worldIn, double p_i1795_2_, double p_i1795_4_, double p_i1795_6_, double p_i1795_8_, double p_i1795_10_, double p_i1795_12_) {
        // 1.12.2 EntityWitherSkull has no 6-double client ctor; shim super(World) + position + normalized acceleration
        super(worldIn);
        setSize(0.3125F, 0.3125F);
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
                if (!(movingObject.entityHit instanceof EntityWitherTurret) && !(movingObject.entityHit instanceof EntityWitherTurretGround) && !(movingObject.entityHit instanceof EntityWitherTurretMortar)) {
                    if (this.shootingEntity != null) {
                        if ((movingObject.entityHit instanceof EntityLivingBase)) {
                            ((EntityLivingBase)movingObject.entityHit).setRevengeTarget(this.shootingEntity);
                            movingObject.entityHit.hurtResistantTime = 1;
                            if ((((EntityLivingBase)movingObject.entityHit).getTotalArmorValue() > 24) && (!(movingObject.entityHit instanceof EntityTitan)) && (!(movingObject.entityHit instanceof EntityDragon)) && (!(movingObject.entityHit instanceof MultiPartEntityPart)) && (!(movingObject.entityHit instanceof EntityPlayer))) {
                                movingObject.entityHit.attackEntityFrom(DamageSourceExtra.destroy, 2.14748365E9F);
                            }
                        }

                        if (((movingObject.entityHit instanceof EntityDragon)) || ((movingObject.entityHit instanceof MultiPartEntityPart))) {
                            movingObject.entityHit.attackEntityFrom(DamageSourceExtra.causeSkullHarmVSEnderDragonHoming(this.shootingEntity), 10.0F + this.harmExtra);
                        }

                        else if (((movingObject.entityHit instanceof EntityLivingBase)) && (((EntityLivingBase)movingObject.entityHit).getCreatureAttribute() == EnumCreatureAttribute.UNDEAD)) {
                            movingObject.entityHit.attackEntityFrom(DamageSourceExtra.GENERIC, 10.0F + this.harmExtra);
                        }

                        else {
                            movingObject.entityHit.attackEntityFrom(DamageSourceExtra.causeSkullHarmHoming(this.shootingEntity), 10.0F + this.harmExtra);
                        }

                        if ((!movingObject.entityHit.isEntityAlive()) && (!(movingObject.entityHit instanceof EntityTitan)) && (!(movingObject.entityHit instanceof EntityDragon)) && (!(movingObject.entityHit instanceof MultiPartEntityPart)) && (!(movingObject.entityHit instanceof EntityPlayer))) {
                            movingObject.entityHit.setDead();
                        }
                    }

                    else {
                        movingObject.entityHit.attackEntityFrom(DamageSource.MAGIC, 5.0F);
                    }

                    if ((movingObject.entityHit instanceof EntityLivingBase)) {
                        byte b0 = 10;
                        if (this.world.getDifficulty() == EnumDifficulty.NORMAL) {
                            b0 = 20;
                        }

                        else if (this.world.getDifficulty() == EnumDifficulty.HARD) {
                            b0 = 40;
                        }

                        if (b0 > 0) {
                            ((EntityLivingBase)movingObject.entityHit).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 40, 3));
                            ((EntityLivingBase)movingObject.entityHit).addPotionEffect(new PotionEffect(MobEffects.WITHER, 20 * b0, 1));
                            if (movingObject.entityHit.posY + movingObject.entityHit.getEyeHeight() - 0.2D < this.posY) {
                                ((EntityLivingBase)movingObject.entityHit).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 40, 0));
                            }
                        }
                    }

                    this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 2.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 1.8F);
                    setDead();
                }
            }

            else {
                this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 2.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 1.8F);
                setDead();
            }
        }
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public void onUpdate() {
        super.onUpdate();
        this.lifetime += 1;
        if (this.lifetime >= 100) {
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 2.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 1.8F);
            setDead();
        }
    }
}
