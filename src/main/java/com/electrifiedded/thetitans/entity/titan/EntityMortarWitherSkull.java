package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.registry.TitanAudio;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EntityMortarWitherSkull extends EntityWitherSkull {
    public int lifetime;
    public int explosivePower;
    public int harmExtra;
    public float factorPace;

    public boolean isInvulnerable() {
        return true;
    }

    public EntityMortarWitherSkull(World worldIn) {
        super(worldIn);
        setSize(0.5F, 0.5F);
    }

    public EntityMortarWitherSkull(World worldIn, EntityLivingBase p_i1794_2_, double p_i1794_3_, double p_i1794_5_, double p_i1794_7_) {
        super(worldIn, p_i1794_2_, p_i1794_3_, p_i1794_5_, p_i1794_7_);
        setSize(0.5F, 0.5F);
    }

    @SideOnly(Side.CLIENT)
    public EntityMortarWitherSkull(World worldIn, double p_i1795_2_, double p_i1795_4_, double p_i1795_6_, double p_i1795_8_, double p_i1795_10_, double p_i1795_12_) {
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

    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        float f = super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn);
        net.minecraft.block.Block block = blockStateIn.getBlock();
        if ((isInvulnerable()) && (block != Blocks.BEDROCK) && (block != Blocks.END_PORTAL) && (block != Blocks.END_PORTAL_FRAME) && (block != Blocks.COMMAND_BLOCK)) {
            f = Math.min(0F, f);
        }

        return f;
    }

    protected void onImpact(RayTraceResult movingObject) {
        if (!this.world.isRemote) {
            if (movingObject.entityHit != null) {
                if ((!(movingObject.entityHit instanceof EntityWitherTurret)) && (!(movingObject.entityHit instanceof EntityWitherTurretGround)) && (!(movingObject.entityHit instanceof EntityWitherTurretMortar))) {
                    this.performHarm(movingObject.entityHit);
                    this.setDead();
                }
            }

            else {
                this.setDead();
            }
        }
    }

    public void setDead() {
        super.setDead();
        List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(12.0D, 12.0D, 12.0D));
        if (list != null && !list.isEmpty()) {
            for (Object entityObj : list) {
                Entity entity = (Entity) entityObj;
                this.performHarm(entity);
            }
        }

        this.world.newExplosion(this.shootingEntity != null ? this.shootingEntity : this, this.posX, this.posY, this.posZ, 21F, false, this.world.getGameRules().getBoolean("mobGriefing"));
        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.6F);
        this.world.playSound(null, this.posX, this.posY, this.posZ, TitanAudio.get("thetitans:mortarHit"), SoundCategory.HOSTILE, 0.5F, 1.0F);
        this.world.playSound(null, this.posX, this.posY, this.posZ, TitanAudio.get("thetitans:mortarHit"), SoundCategory.HOSTILE, 2.0F, 1.0F);
    }

    public void performHarm(Entity hitEntity) {
        DamageSource source = DamageSource.MAGIC;
        float damage = 5000.0F + 1000 * this.harmExtra;
        if (hitEntity.height >= 6.0F) {
            damage *= 30.0F;
        }

        damage /= this.getDistance(hitEntity) <= 3.0F ? 1.0F : this.getDistance(hitEntity) - 3.0F;
        if (this.shootingEntity != null) {
            if ((hitEntity instanceof EntityLivingBase)) {
                ((EntityLivingBase)hitEntity).setRevengeTarget(this.shootingEntity);
                hitEntity.hurtResistantTime = 0;
            }

            if ((!(hitEntity instanceof EntityDragon)) && (!(hitEntity instanceof MultiPartEntityPart))) {
                if (((hitEntity instanceof EntityLivingBase)) && (((EntityLivingBase)hitEntity).getCreatureAttribute() == EnumCreatureAttribute.UNDEAD)) {
                    if ((hitEntity.height < 6.0F) && (((EntityLivingBase)hitEntity).getTotalArmorValue() <= 24)) {
                        source = DamageSource.GENERIC;
                    }

                    else {
                        source = DamageSource.GENERIC;
                        hitEntity.playSound(TitanAudio.get("thetitans:titanpunch"), 10.0F, 0.9F);
                    }
                }

                else if (hitEntity.height >= 6.0F) {
                    source = DamageSourceExtra.causeSkullHarmHoming(this.shootingEntity);
                    hitEntity.playSound(TitanAudio.get("thetitans:titanpunch"), 10.0F, 0.9F);
                }

                else {
                    source = DamageSourceExtra.causeSkullHarmHoming(this.shootingEntity);
                }
            }

            else if (hitEntity.height >= 6.0F) {
                source = DamageSourceExtra.causeSkullHarmVSEnderDragonHoming(this.shootingEntity);
                hitEntity.playSound(TitanAudio.get("thetitans:titanpunch"), 10.0F, 1.0F);
            }

            else {
                source = DamageSourceExtra.causeSkullHarmVSEnderDragonHoming(this.shootingEntity);
            }
        }

        if ((damage > 0.0F) && (hitEntity.attackEntityFrom(source, damage)) && ((hitEntity instanceof EntityLivingBase))) {
            byte b0 = 30;
            if (this.world.getDifficulty() == EnumDifficulty.NORMAL) {
                b0 = 60;
            }

            else if (this.world.getDifficulty() == EnumDifficulty.HARD) {
                b0 = 90;
            }

            if (b0 > 0) {
                ((EntityLivingBase)hitEntity).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 120, 3));
                ((EntityLivingBase)hitEntity).addPotionEffect(new PotionEffect(MobEffects.WITHER, 40 * b0, 3));
                ((EntityLivingBase)hitEntity).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 120, 0));
                ((EntityLivingBase)hitEntity).addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 120, 0));
            }
        }
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public void onUpdate() {
        super.onUpdate();
        List<?> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(3.0D, 3.0D, 3.0D));
        if (list != null && !list.isEmpty()) {
            for (Object entityObj : list) {
                Entity entity = (Entity) entityObj;
                if ((entity.isEntityAlive()) && ((entity instanceof EntityLivingBase)) && (!(entity instanceof EntityPlayer)) && (!(entity instanceof EntityWitherTurret)) && (!(entity instanceof EntityWitherTurretGround)) && (!(entity instanceof EntityWitherTurretMortar))) {
                    this.onImpact(new RayTraceResult(entity));
                }
            }
        }

        this.lifetime += 1;
        if (this.lifetime >= 400 && (this.rand.nextInt(100) == 0 || this.lifetime >= 1500) || this.shootingEntity == null) {
            this.setDead();
            this.onImpact(new RayTraceResult(this));
        }
    }
}
