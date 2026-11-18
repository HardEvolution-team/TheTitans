package com.electrifiedded.thetitans.entity.mob;

import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.ICanAttack;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityCustomFireball extends EntityFireball {
    private static final DataParameter<Integer> KEY_FIREBALL = EntityDataManager.createKey(EntityCustomFireball.class, DataSerializers.VARINT);
    public float explosionRadius;
    public float harmImpact;
    public boolean causeFiresMay;
    // PERF — tracks the currently-applied fireball id so the size/stats switch (which calls setSize +
    // rebuilds the AABB) runs only when the id changes, not every tick.
    private int fireballKeyApplied = -1;

    public EntityCustomFireball(World worldIn) {
        super(worldIn);
    }

    public EntityCustomFireball(World worldIn, EntityLivingBase p_i1771_2_, double p_i1771_3_, double p_i1771_5_, double p_i1771_7_) {
        super(worldIn, p_i1771_2_, p_i1771_3_, p_i1771_5_, p_i1771_7_);
    }

    public EntityCustomFireball(World worldIn, double p_i1772_2_, double p_i1772_4_, double p_i1772_6_, double p_i1772_8_, double p_i1772_10_, double p_i1772_12_) {
        super(worldIn, p_i1772_2_, p_i1772_4_, p_i1772_6_, p_i1772_8_, p_i1772_10_, p_i1772_12_);
    }

    public EntityCustomFireball(World worldIn, EntityLivingBase p_i1771_2_, double p_i1771_3_, double p_i1771_5_, double p_i1771_7_, int id) {
        this(worldIn, p_i1771_2_, p_i1771_3_, p_i1771_5_, p_i1771_7_);
        this.assignIDFireball(id);
    }

    public void whenPublicImpact(EntityLivingBase movingObject) {
        if (movingObject != null) {
            if (this.shootingEntity != null && this.shootingEntity instanceof EntityLiving && ((com.electrifiedded.thetitans.entity.titan.ICanAttack)this.shootingEntity).allowStrikeGiant(movingObject.getClass())) {
                if (this.shootingEntity instanceof EntityTitan)
                    ((EntityTitan)this.shootingEntity).strikeMobChoosen(movingObject, harmImpact, (int)explosionRadius);
                else
                    ((EntityLiving)this.shootingEntity).attackEntityAsMob(movingObject);
                playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);
                this.setDead();
            }
        }
    }

    protected void onImpact(RayTraceResult movingObject) {
        if (!this.world.isRemote) {
            if (movingObject.entityHit != null && movingObject.entityHit instanceof EntityFireball) {
                return;
            }

            if (movingObject.entityHit != null) {
                if (this.shootingEntity != null && this.shootingEntity instanceof EntityLiving && ((com.electrifiedded.thetitans.entity.titan.ICanAttack)this.shootingEntity).allowStrikeGiant(movingObject.entityHit.getClass())) {
                    this.whenPublicImpact((EntityLivingBase) movingObject.entityHit);
                }
            }

            else {
                this.setDead();
            }
        }
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(KEY_FIREBALL, Integer.valueOf(0));
    }

    public int fetchIDFireball() {
        return this.dataManager.get(KEY_FIREBALL).intValue();
    }

    public void assignIDFireball(int arg82215n1) {
        this.dataManager.set(KEY_FIREBALL, Integer.valueOf(arg82215n1));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound arg70014n1) {
        super.writeEntityToNBT(arg70014n1);
        arg70014n1.setInteger("FireballID", this.fetchIDFireball());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound arg70037n1) {
        super.readEntityFromNBT(arg70037n1);
        if (arg70037n1.hasKey("FireballID", 99)) {
            this.assignIDFireball(arg70037n1.getInteger("FireballID"));
        }
    }

    public boolean isBurning() {
        return this.causeFiresMay;
    }

    public void setFire(int arg70015n1) {
        if (this.causeFiresMay)
            super.setFire(arg70015n1);
    }

    public void onUpdate() {
        if (this.shootingEntity != null) {
            if (this.shootingEntity instanceof EntityMagicUser)
                this.assignIDFireball(0);
        }

        else {
            if (!this.world.isRemote)
                this.setDead();
        }

        if (this.fireballKeyApplied != this.fetchIDFireball()) {
            this.fireballKeyApplied = this.fetchIDFireball();
            switch (this.fetchIDFireball()) {
                case 1:this.setSize(1.5F, 1.5F);
                    this.harmImpact = 200F;
                    this.causeFiresMay = false;
                    this.explosionRadius = 3F;
                    break;
                case 2:this.setSize(2F, 2F);
                    this.harmImpact = 600F;
                    this.causeFiresMay = true;
                    this.explosionRadius = 3F;
                    break;
                case 3:this.setSize(1.5F, 1.5F);
                    this.harmImpact = 300F;
                    this.causeFiresMay = true;
                    this.explosionRadius = 4F;
                    break;
                case 4:this.setSize(4F, 4F);
                    this.harmImpact = 1500F;
                    this.causeFiresMay = false;
                    this.explosionRadius = 8F;
                    break;
                case 5:this.setSize(6F, 6F);
                    this.harmImpact = 10000F;
                    this.causeFiresMay = false;
                    this.explosionRadius = 12F;
                    break;
                case 6:this.setSize(2F, 2F);
                    this.harmImpact = 60F;
                    this.causeFiresMay = false;
                    this.explosionRadius = 1F;
                    break;
                default:this.setSize(6F, 6F);
                    this.harmImpact = 10000F;
                    this.causeFiresMay = true;
                    this.explosionRadius = 12F;
            }
        }

        super.onUpdate();
    }
}
