package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EntitySkeletonTitanGiantArrow extends Entity {
    private static final DataParameter<Integer> KIND_ARROW = EntityDataManager.<Integer>createKey(EntitySkeletonTitanGiantArrow.class, DataSerializers.VARINT);
    public EntityLivingBase shootingEntity;
    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double arg70112n1) {
        double d1 = this.getEntityBoundingBox().getAverageEdgeLength() * 8.0D;
        d1 *= 64.0D;
        return arg70112n1 < d1 * d1;
    }

    protected void entityInit() {
        this.dataManager.register(KIND_ARROW, Integer.valueOf(0));
    }

    public int fetchKindArrow() {
        return this.dataManager.get(KIND_ARROW);
    }

    public void assignKindArrow(int type) {
        this.dataManager.set(KIND_ARROW, Integer.valueOf(type));
    }

    public EntitySkeletonTitanGiantArrow(World worldIn) {
        super(worldIn);
        this.setSize(8F, 8F);
    }

    public EntitySkeletonTitanGiantArrow(World p_i1761_1_, EntityLivingBase p_i1761_2_, double p_i1761_3_, double p_i1761_5_, double p_i1761_7_) {
        this(p_i1761_1_);
        this.shootingEntity = p_i1761_2_;
        this.setLocationAndAngles(p_i1761_2_.posX, p_i1761_2_.posY, p_i1761_2_.posZ, p_i1761_2_.rotationYaw, p_i1761_2_.rotationPitch);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = this.motionY = this.motionZ = 0.0D;
        p_i1761_3_ += this.rand.nextGaussian() * 0.4D;
        p_i1761_5_ += this.rand.nextGaussian() * 0.4D;
        p_i1761_7_ += this.rand.nextGaussian() * 0.4D;
        double d3 = (double)MathHelper.sqrt(p_i1761_3_ * p_i1761_3_ + p_i1761_5_ * p_i1761_5_ + p_i1761_7_ * p_i1761_7_);
        this.accelerationX = p_i1761_3_ / d3 * 0.15D;
        this.accelerationY = p_i1761_5_ / d3 * 0.15D;
        this.accelerationZ = p_i1761_7_ / d3 * 0.15D;
        this.motionX += this.accelerationX;
        this.motionY += this.accelerationY;
        this.motionZ += this.accelerationZ;
        this.rotationYaw = p_i1761_2_.rotationYawHead;
    }

    public EntitySkeletonTitanGiantArrow(World p_i1760_1_, double p_i1760_2_, double p_i1760_4_, double p_i1760_6_, double p_i1760_8_, double p_i1760_10_, double p_i1760_12_) {
        this(p_i1760_1_);
        this.setLocationAndAngles(p_i1760_2_, p_i1760_4_, p_i1760_6_, this.rotationYaw, this.rotationPitch);
        this.setPosition(p_i1760_2_, p_i1760_4_, p_i1760_6_);
        double d6 = (double)MathHelper.sqrt(p_i1760_8_ * p_i1760_8_ + p_i1760_10_ * p_i1760_10_ + p_i1760_12_ * p_i1760_12_);
        this.accelerationX = p_i1760_8_ / d6 * 0.15D;
        this.accelerationY = p_i1760_10_ / d6 * 0.15D;
        this.accelerationZ = p_i1760_12_ / d6 * 0.15D;
    }

    public void onImpact(RayTraceResult movingObject) {
        if (!this.world.isRemote) {
            float f = 900F;
            if ((movingObject.entityHit != null) && this.shootingEntity != null && this.shootingEntity instanceof EntityLiving && movingObject.entityHit instanceof EntityLivingBase && ((com.electrifiedded.thetitans.entity.titan.ICanAttack)this.shootingEntity).allowStrikeGiant(movingObject.entityHit.getClass())) {
                if (this.shootingEntity instanceof EntityTitan) {
                    if (this.fetchKindArrow() == 0)
                        f = (float)((EntityTitan)this.shootingEntity).fetchAmountStrike(1.0D);
                    if (this.fetchKindArrow() == 1)
                        f = (float)((EntityTitan)this.shootingEntity).fetchAmountStrike(10.0D);
                    if (this.fetchKindArrow() == 2)
                        f = (float)((EntityTitan)this.shootingEntity).fetchAmountStrike(100.0D);
                    ((EntityTitan)this.shootingEntity).strikeMobChoosen(movingObject.entityHit, f, 10);
                } else
                        movingObject.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.shootingEntity), f);
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("slashFlesh", this.world, this, 3F, 1.5F);
                if ((movingObject.entityHit instanceof EntityTitan || movingObject.entityHit instanceof EntityTitanPart) && !this.isInvisible()) {
                    this.world.createExplosion(this, this.posX, this.posY, this.posZ, 3F, false);
                    this.setDead();
                }
            }
        }
    }

    public void writeEntityToNBT(NBTTagCompound arg70014n1) {
        arg70014n1.setInteger("ArrowType", this.fetchKindArrow());
        arg70014n1.setTag("direction", this.newDoubleNBTList(new double[] {
            this.motionX, this.motionY, this.motionZ
        }
        ));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound arg70037n1) {
        this.assignKindArrow(arg70037n1.getInteger("ArrowType"));
        if (arg70037n1.hasKey("direction", 9)) {
            NBTTagList nbttaglist = arg70037n1.getTagList("direction", 6);
            this.motionX = nbttaglist.getDoubleAt(0);
            this.motionY = nbttaglist.getDoubleAt(1);
            this.motionZ = nbttaglist.getDoubleAt(2);
        }

        else {
            this.setDead();
        }
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    protected float getMotionFactor() {
        return 0.99F;
    }

    /**
     * Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
     */
    public boolean isBurning() {
        return false;
    }

    @SuppressWarnings("unchecked")
    public void onUpdate() {
        this.onEntityUpdate();
        if (!this.world.isRemote && (this.posY <= -100D || this.ticksExisted > 300 || this.shootingEntity == null || (this.shootingEntity != null && !this.shootingEntity.isEntityAlive())))
            this.setDead();
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f) * 180.0D / Math.PI);
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
        for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f1) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
            ;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
        float f2 = this.getMotionFactor();
        this.motionX += this.accelerationX * 7D;
        this.motionY += this.accelerationY * 7D;
        this.motionZ += this.accelerationZ * 7D;
        this.motionX *= (double)f2;
        this.motionY *= (double)f2;
        this.motionZ *= (double)f2;
        this.setPosition(this.posX, this.posY, this.posZ);
        if ((this.shootingEntity != null) && ((this.shootingEntity instanceof EntityEnderColossus)))
            this.setInvisible(true);
        this.noClip = true;
        if ((this.shootingEntity != null) && ((this.shootingEntity instanceof EntityTitan)))
            ((EntityTitan)this.shootingEntity).destroyBlocksInAABB(this.getEntityBoundingBox());
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(16.0D));
        if (list != null && !list.isEmpty()) {
            for (Object entityObj : list) {
                Entity entity = (Entity) entityObj;
                float x = (float)(this.posX - entity.posX);
                float z = (float)(this.posZ - entity.posZ);
                float dis = MathHelper.sqrt(x * x + z * z);
                if (entity != this.shootingEntity && this.isEntityAlive() && (dis <= this.width + entity.width + 1D && this.posY <= entity.posY + entity.height && this.posY > entity.posY)) {
                    this.onImpact(new RayTraceResult(entity));
                }
            }
        }
    }
}
