package com.electrifiedded.thetitans.entity.titan;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class EntityWebShot extends EntityFireball {
    private BlockPos posGround = new BlockPos(-1, -1, -1);
    private IBlockState groundModeIn;
    private boolean inGround;
    private int ticksAlive;

    public EntityWebShot(World worldIn) {
        super(worldIn);
        this.setSize(6F, 6F);
    }

    public EntityWebShot(World worldIn, EntityLivingBase p_i1771_2_, double p_i1771_3_, double p_i1771_5_, double p_i1771_7_) {
        super(worldIn, p_i1771_2_, p_i1771_3_, p_i1771_5_, p_i1771_7_);
        this.setSize(6F, 6F);
    }

    public EntityWebShot(World worldIn, double p_i1772_2_, double p_i1772_4_, double p_i1772_6_, double p_i1772_8_, double p_i1772_10_, double p_i1772_12_) {
        super(worldIn, p_i1772_2_, p_i1772_4_, p_i1772_6_, p_i1772_8_, p_i1772_10_, p_i1772_12_);
        this.setSize(6F, 6F);
    }

    protected void onImpact(RayTraceResult movingObject) {
        if (!this.world.isRemote) {
            if (movingObject.entityHit != null) {
                if (this.ticksExisted >= 5 && this.shootingEntity != null && this.shootingEntity instanceof EntitySpiderTitan && ((EntitySpiderTitan)this.shootingEntity).allowStrikeGiant(movingObject.entityHit.getClass())) {
                    ((EntitySpiderTitan)this.shootingEntity).strikeMobChoosen(movingObject.entityHit, (float)((EntitySpiderTitan)this.shootingEntity).fetchAmountStrike(2.0F), ((EntitySpiderTitan)this.shootingEntity).fetchAmountKnockback());
                    int i1 = MathHelper.floor(this.posY + 1);
                    int i11 = MathHelper.floor(this.posX);
                    int j1 = MathHelper.floor(this.posZ);
                    for (int l1 = -2 - this.rand.nextInt(4); l1 <= 2 + this.rand.nextInt(4); l1++) {
                        for (int i2 = -2 - this.rand.nextInt(4); i2 <= 2 + this.rand.nextInt(4); i2++) {
                            for (int j = -2 - rand.nextInt(4); j <= 2 + rand.nextInt(4); j++) {
                                int j2 = i11 + l1;
                                int k = i1 + j;
                                int l = j1 + i2;
                                BlockPos pos = new BlockPos(j2, k, l);
                                if (!this.world.getBlockState(pos).isOpaqueCube()) {
                                    this.world.setBlockState(pos, Blocks.WEB.getDefaultState());
                                }
                            }
                        }
                    }

                    this.setDead();
                }
            }

            else {
                BlockPos hit = movingObject.getBlockPos();
                if (this.ticksExisted >= 5 && this.world.getBlockState(hit).getBlock() != Blocks.WEB) {
                    BlockPos side = hit.offset(movingObject.sideHit);
                    if (this.world.isAirBlock(side)) {
                        int i1 = side.getX();
                        int i11 = side.getY();
                        int j1 = side.getZ();
                        for (int l1 = -2 - this.rand.nextInt(4); l1 <= 2 + this.rand.nextInt(4); l1++) {
                            for (int i2 = -2 - this.rand.nextInt(4); i2 <= 2 + this.rand.nextInt(4); i2++) {
                                for (int j = -2 - rand.nextInt(4); j <= 2 + rand.nextInt(4); j++) {
                                    int j2 = i11 + l1;
                                    int k = i1 + j;
                                    int l = j1 + i2;
                                    BlockPos pos = new BlockPos(j2, k, l);
                                    if (!this.world.getBlockState(pos).isOpaqueCube()) {
                                        this.world.setBlockState(pos, Blocks.WEB.getDefaultState());
                                    }
                                }
                            }
                        }

                        this.setDead();
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void onUpdate() {
        this.noClip = true;
        if ((this.shootingEntity != null && this.shootingEntity.isDead || !this.world.isBlockLoaded(new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ)))) {
            this.setDead();
        }

        else {
            this.onEntityUpdate();
            if (this.inGround) {
                if (this.world.getBlockState(this.posGround) == this.groundModeIn) {
                    ++this.ticksAlive;
                    if (this.ticksAlive == 600) {
                        this.setDead();
                    }

                    return;
                }

                this.inGround = false;
                this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
                this.ticksAlive = 0;
            }

            else {
            }

            Vec3d vec3 = new Vec3d(this.posX, this.posY + 1.5D, this.posZ);
            Vec3d vec31 = new Vec3d(this.posX + this.motionX, this.posY + 1.5D + this.motionY, this.posZ + this.motionZ);
            RayTraceResult movingobjectposition = this.world.rayTraceBlocks(vec3, vec31);
            vec3 = new Vec3d(this.posX, this.posY + 1.5D, this.posZ);
            vec31 = new Vec3d(this.posX + this.motionX, this.posY + 1.5D + this.motionY, this.posZ + this.motionZ);
            if (movingobjectposition != null) {
                vec31 = new Vec3d(movingobjectposition.hitVec.x, movingobjectposition.hitVec.y, movingobjectposition.hitVec.z);
            }

            Entity entity = null;
            List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(2.0D, 2.0D, 2.0D));
            if (list != null && !list.isEmpty()) {
                for (Object mobRef1Obj : list) {
                    Entity mobRef1 = (Entity) mobRef1Obj;
                    if (mobRef1.canBeCollidedWith() && this.ticksExisted >= 5) {
                        float f = mobRef1.width;
                        AxisAlignedBB axisalignedbb = mobRef1.getEntityBoundingBox().grow((double)f, (double)f, (double)f);
                        RayTraceResult movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);
                        if (movingobjectposition1 != null) {
                            entity = mobRef1;
                        }
                    }
                }
            }

            if (entity != null) {
                movingobjectposition = new RayTraceResult(entity);
            }

            if (movingobjectposition != null && this.ticksExisted >= 5) {
                this.onImpact(movingobjectposition);
            }

            List<Entity> found1 = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(16.0D));
            if (found1 != null && !found1.isEmpty() && this.shootingEntity != null && this.shootingEntity instanceof EntityTitan && this.ticksExisted >= 5) {
                for (Object mobRef1Obj : found1) {
                    Entity mobRef1 = (Entity) mobRef1Obj;
                    if (this.isEntityAlive() && mobRef1 instanceof EntityLivingBase && !(mobRef1 instanceof EntityTitanPart) && !(mobRef1 instanceof EntityTitan) && ((EntityTitan)this.shootingEntity).allowStrikeGiant(mobRef1.getClass()) && this.getDistanceSq(mobRef1) <= (this.width * this.width) + (mobRef1.width * mobRef1.width) + 4D) {
                        this.onImpact(new RayTraceResult(mobRef1));
                    }
                }
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(Math.atan2(this.motionZ, this.motionX) * 180.0D / Math.PI) + 90.0F;
            for (this.rotationPitch = (float)(Math.atan2((double)f1, this.motionY) * 180.0D / Math.PI) - 90.0F; this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
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
            float f2 = 0.99F;
            if (this.isInWater()) {
                for (int j = 0; j < 4; ++j) {
                    float f3 = 0.25F;
                    this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double)f3, this.posY - this.motionY * (double)f3, this.posZ - this.motionZ * (double)f3, this.motionX, this.motionY, this.motionZ);
                }
            }

            this.motionX += this.accelerationX;
            this.motionY += this.accelerationY;
            this.motionZ += this.accelerationZ;
            this.motionX *= (double)f2;
            this.motionY *= (double)f2;
            this.motionZ *= (double)f2;
            this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.EXPLOSION_NORMAL, this.posX, this.posY + 1.5D, this.posZ, 0.0D, 0.0D, 0.0D);
            this.setPosition(this.posX, this.posY, this.posZ);
        }
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean isBurning() {
        return false;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    public boolean isInWater() {
        return false;
    }
}
