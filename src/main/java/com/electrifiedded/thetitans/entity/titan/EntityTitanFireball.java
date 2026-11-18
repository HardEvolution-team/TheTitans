package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.core.TheCore;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

public class EntityTitanFireball extends EntityFireball {
    private static final DataParameter<Integer> KEY_FIREBALL = EntityDataManager.<Integer>createKey(EntityTitanFireball.class, DataSerializers.VARINT);
    public float explosionRadius;
    public float harmImpact;
    public boolean causeFiresMay;
    // PERF — tracks which fireball-id's size/stats are currently applied, so the switch below (which
    // calls setSize + rebuilds the AABB) runs only when the id actually changes, not every single tick.
    private int fireballKeyApplied = -1;

    public EntityTitanFireball(World worldIn) {
        super(worldIn);
    }

    public EntityTitanFireball(World worldIn, EntityLivingBase p_i1771_2_, double p_i1771_3_, double p_i1771_5_, double p_i1771_7_) {
        super(worldIn, p_i1771_2_, p_i1771_3_, p_i1771_5_, p_i1771_7_);
    }

    public EntityTitanFireball(World worldIn, double p_i1772_2_, double p_i1772_4_, double p_i1772_6_, double p_i1772_8_, double p_i1772_10_, double p_i1772_12_) {
        super(worldIn, p_i1772_2_, p_i1772_4_, p_i1772_6_, p_i1772_8_, p_i1772_10_, p_i1772_12_);
    }

    public EntityTitanFireball(World worldIn, EntityLivingBase p_i1771_2_, double p_i1771_3_, double p_i1771_5_, double p_i1771_7_, int id) {
        this(worldIn, p_i1771_2_, p_i1771_3_, p_i1771_5_, p_i1771_7_);
        this.assignIDFireball(id);
    }

    public void whenPublicImpact(EntityLivingBase movingObject) {
        this.onImpact(new RayTraceResult(movingObject));
    }

    public void whenPublicImpact(Entity movingObject) {
        this.onImpact(new RayTraceResult(movingObject));
    }

    protected void onImpact(RayTraceResult movingObject) {
        float f = this.harmImpact * (TheTitans.isDifficulty(world, TheCore.NIGHTMARE_V) ? 3F : 1F);
        if (!this.world.isRemote) {
            if (movingObject.entityHit != null && movingObject.entityHit instanceof EntityFireball) {
                return;
            }

            if (movingObject.entityHit != null) {
                if (this.shootingEntity != null && this.shootingEntity instanceof EntityLiving && ((com.electrifiedded.thetitans.entity.titan.ICanAttack)this.shootingEntity).allowStrikeGiant(movingObject.entityHit.getClass())) {
                    if (this.shootingEntity instanceof EntityTitan) {
                        ((EntityTitan)this.shootingEntity).strikeMobChoosen(movingObject.entityHit, f, 3);
                        ((EntityTitan)this.shootingEntity).destroyBlocksInAABB(getEntityBoundingBox().grow((double)(explosionRadius - this.width) + 1D, (double)(explosionRadius - this.width) + 1D, (double)(explosionRadius - this.width) + 1D));
                    }

                    else {
                        if (movingObject.entityHit instanceof EntityTitanPart)
                            movingObject.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), f);
                    }

                    if (!(movingObject.entityHit instanceof EntityTitanPart) && !(movingObject.entityHit instanceof EntityTitan))
                        this.world.newExplosion(this.shootingEntity != null ? this.shootingEntity : this, this.posX, this.posY, this.posZ, this.explosionRadius, this.causeFiresMay ? this.world.getGameRules().getBoolean("mobGriefing") : false, this.world.getGameRules().getBoolean("mobGriefing"));
                    else
                        com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("random.explode", this.world, this, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);
                    this.setDead();
                }
            }

            else {
                if (this.shootingEntity != null && this.shootingEntity instanceof EntityTitan && this.fetchIDFireball() != 6)
                    ((EntityTitan)this.shootingEntity).destroyBlocksInAABB(getEntityBoundingBox().grow((double)(explosionRadius - this.width), (double)(explosionRadius - this.width), (double)(explosionRadius - this.width)));
                if (!this.world.isRemote && this.fetchIDFireball() == 6) {
                    for (int l = 0; l < 128; l++) {
                        int i = MathHelper.floor(this.posX + (this.rand.nextFloat() - 0.5D) * this.width);
                        int j = MathHelper.floor(this.posY);
                        int k = MathHelper.floor(this.posZ + (this.rand.nextFloat() - 0.5D) * this.width);
                        BlockPos pos = new BlockPos(i, j, k);
                        if ((this.world.getBlockState(pos).getMaterial() == Material.AIR) && (Blocks.SNOW_LAYER.canPlaceBlockAt(this.world, pos))) {
                            this.world.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState());
                        }
                    }
                }

                this.world.newExplosion(this.shootingEntity != null ? this.shootingEntity : this, this.posX, this.posY, this.posZ, this.explosionRadius, this.causeFiresMay ? this.world.getGameRules().getBoolean("mobGriefing") : false, this.world.getGameRules().getBoolean("mobGriefing"));
                this.setDead();
            }

            List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(this.explosionRadius, this.explosionRadius, this.explosionRadius));
            if (this.shootingEntity instanceof EntityTitan && list != null && !list.isEmpty()) {
                for (Object entityObj : list) {
                    Entity entity = (Entity) entityObj;
                    if (entity.isEntityAlive() && !(entity instanceof EntityTitanFireball))
                        ((EntityTitan)this.shootingEntity).strikeMobChoosen(entity, f, 3);
                }
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
        return this.dataManager.get(KEY_FIREBALL);
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
            if (this.shootingEntity instanceof EntityGhastTitan)
                this.assignIDFireball(0);
            if (this.shootingEntity instanceof EntityCreeperTitan)
                this.assignIDFireball(1);
            if (this.shootingEntity instanceof EntityBlazeTitan)
                this.assignIDFireball(2);
            if (this.shootingEntity instanceof EntityPigZombieTitan)
                this.assignIDFireball(3);
            if (this.shootingEntity instanceof EntityEnderColossus)
                this.assignIDFireball(4);
            if (this.shootingEntity instanceof EntityIronGolemTitan)
                this.assignIDFireball(5);
            if (this.shootingEntity instanceof EntitySnowGolemTitan)
                this.assignIDFireball(6);
            if (this.shootingEntity instanceof EntityGargoyleTitan)
                this.assignIDFireball(7);
        }

        else {
            if (!this.world.isRemote)
                this.setDead();
        }

        if (this.fireballKeyApplied != this.fetchIDFireball()) {
            this.fireballKeyApplied = this.fetchIDFireball();
            switch (this.fetchIDFireball()) {
                case 1:this.setSize(4F, 4F);
                    this.harmImpact = 200F;
                    this.causeFiresMay = false;
                    this.explosionRadius = 3F;
                    break;
                case 2:this.setSize(4F, 4F);
                    this.harmImpact = 600F;
                    this.causeFiresMay = true;
                    this.explosionRadius = 3F;
                    break;
                case 3:this.setSize(3F, 3F);
                    this.harmImpact = 300F;
                    this.causeFiresMay = true;
                    this.explosionRadius = 3F;
                    break;
                case 4:this.setSize(8F, 8F);
                    this.harmImpact = 1500F;
                    this.causeFiresMay = false;
                    this.explosionRadius = 4F;
                    break;
                case 5:this.setSize(8F, 8F);
                    this.harmImpact = 10000F;
                    this.causeFiresMay = false;
                    this.explosionRadius = 4F;
                    break;
                case 6:this.setSize(3F, 3F);
                    this.harmImpact = 60F;
                    this.causeFiresMay = false;
                    this.explosionRadius = 2F;
                    break;
                default:this.setSize(8F, 8F);
                    this.harmImpact = 10000F;
                    this.causeFiresMay = true;
                    this.explosionRadius = 6F;
            }
        }

        super.onUpdate();
    }
}
