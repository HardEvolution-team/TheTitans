package com.electrifiedded.thetitans.entity.minion;

import com.electrifiedded.thetitans.entity.titan.EntityEnderColossus;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossusCrystal;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class EntityDragonMinion extends EntityLiving implements IEntityMultiPart, IMinion {
    public EntityLiving master;
    public double targetX;
    public double targetY;
    public double targetZ;
    /** Ring buffer array for the last 64 Y-positions and yaw rotations. Used to calculate offsets for the animations. */
    public double[][] ringBuffer = new double[64][3];
    /** Index into the ring buffer. Incremented once per tick and restarts at 0 once it reaches the end of the buffer. */
    public int ringBufferIndex = -1;
    /** An array containing all body parts of this dragon */
    public MultiPartEntityPart[] dragonPartArray;
    /** The head bounding box of a dragon */
    public MultiPartEntityPart dragonPartHead;
    /** The body bounding box of a dragon */
    public MultiPartEntityPart dragonPartBody;
    public MultiPartEntityPart dragonPartTail1;
    public MultiPartEntityPart dragonPartTail2;
    public MultiPartEntityPart dragonPartTail3;
    public MultiPartEntityPart dragonPartWing1;
    public MultiPartEntityPart dragonPartWing2;
    /** Animation time at previous tick. */
    public float prevAnimTime;
    /** Animation time, used to control the speed of the animation cycles (wings flapping, jaw opening, etc.) */
    public float animTime;
    /** Force selecting a new flight target at next tick if set to true. */
    public boolean freshVictimForce;
    /** Activated if the dragon is flying though obsidian, white stone or bedrock. Slows movement and animation speed. */
    public boolean slowed;
    private EntityLivingBase target;
    public int deathTicks;
    /** The current endercrystal that is healing this dragon */
    public EntityEnderCrystal healingEnderCrystal;

    public EntityDragonMinion(World p_i1700_1_) {
        super(p_i1700_1_);
        enablePersistence();
        this.dragonPartArray = new MultiPartEntityPart[] {
            this.dragonPartHead = new MultiPartEntityPart(this, "head", 6.0F, 6.0F), this.dragonPartBody = new MultiPartEntityPart(this, "body", 8.0F, 8.0F), this.dragonPartTail1 = new MultiPartEntityPart(this, "tail", 4.0F, 4.0F), this.dragonPartTail2 = new MultiPartEntityPart(this, "tail", 4.0F, 4.0F), this.dragonPartTail3 = new MultiPartEntityPart(this, "tail", 4.0F, 4.0F), this.dragonPartWing1 = new MultiPartEntityPart(this, "wing", 4.0F, 4.0F), this.dragonPartWing2 = new MultiPartEntityPart(this, "wing", 4.0F, 4.0F)
        };

        this.setHealth(this.getMaxHealth());
        this.setSize(14.0F, 3.6F);
        this.noClip = true;
        this.isImmuneToFire = true;
        this.targetY = 100.0D;
        this.ignoreFrustumCheck = true;
        if (this.getAttackTarget() != null)
            this.target = this.getAttackTarget();
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(2.0E9D);
    }

    protected void entityInit() {
        super.entityInit();
    }

    public void setDead() {
        super.setDead();
        if ((this.master != null) && ((this.master instanceof EntityTitan))) {
            ((EntityTitan)this.master).retractNumFromKindMinion(fetchKindMinion());
        }
    }

    public EnumMinionType fetchKindMinion() {
        return EnumMinionType.SPECIAL;
    }

    @SuppressWarnings("rawtypes")
    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        return (arg70686n1 != EntityEnderman.class) && (arg70686n1 != EntityEndermanMinion.class) && (arg70686n1 != EntityEnderColossus.class) && (arg70686n1 != EntityDragon.class) && (arg70686n1 != EntityDragonMinion.class) && (arg70686n1 != EntityEnderColossusCrystal.class);
    }

    /**
     * Returns a double[3] array with movement offsets, used to calculate trailing tail/neck positions. [0] = yaw
     * offset, [1] = y offset, [2] = unused, always 0. Parameters: buffer index offset, partial ticks.
     */
    public double[] getMovementOffsets(int arg70974n1, float arg70974n2) {
        if (this.getHealth() <= 0.0F) {
            arg70974n2 = 0.0F;
        }

        arg70974n2 = 1.0F - arg70974n2;
        int j = this.ringBufferIndex - arg70974n1 * 1 & 63;
        int k = this.ringBufferIndex - arg70974n1 * 1 - 1 & 63;
        double[] adouble = new double[3];
        double d0 = this.ringBuffer[j][0];
        double d1 = MathHelper.wrapDegrees(this.ringBuffer[k][0] - d0);
        adouble[0] = d0 + d1 * (double)arg70974n2;
        d0 = this.ringBuffer[j][1];
        d1 = this.ringBuffer[k][1] - d0;
        adouble[1] = d0 + d1 * (double)arg70974n2;
        adouble[2] = this.ringBuffer[j][2] + (this.ringBuffer[k][2] - this.ringBuffer[j][2]) * (double)arg70974n2;
        return adouble;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate() {
        // PERF — setSize rebuilds the AABB every call; the value is constant so only set it once.
        if (this.width != 14.0F)
            this.setSize(14.0F, 3.6F);
        if (this.ticksExisted % 10 == 0)
            this.heal(5.0F + this.rand.nextFloat() * 15.0F);
        if (this.ticksExisted == 20 || this.ticksExisted == 40)
            this.attackEntityFromPart(dragonPartHead, (new DamageSource("none")), 0F);
        if (this.getAttackTarget() != null) {
            if (this.rand.nextInt(2) == 0)
                this.target = this.getAttackTarget();
            if (this.getAttackTarget() instanceof EntityPlayer && ((EntityPlayer)this.getAttackTarget()).capabilities.disableDamage)
                this.setAttackTarget(null);
        }

        if (this.getAttackTarget() == null && this.ticksExisted % 60 == 0) {
            double d = this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getBaseValue();
            List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(d, d, d));
            if (list != null && !list.isEmpty() && this.isEntityAlive()) {
                for (int i = 0; i < list.size(); ++i) {
                    Entity entity = (Entity)list.get(i);
                    if (entity != null && entity instanceof EntityLivingBase && this.allowStrikeGiant(entity.getClass())) {
                        this.setAttackTarget((EntityLivingBase) entity);
                    }
                }
            }
        }

        this.ignoreFrustumCheck = true;
        if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL)
            setDead();
        float f;
        float f1;
        if (this.world.isRemote) {
            f = MathHelper.cos(this.animTime * (float)Math.PI * 2.0F);
            f1 = MathHelper.cos(this.prevAnimTime * (float)Math.PI * 2.0F);
            if (f1 <= -0.3F && f >= -0.3F) {
                this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ENDERDRAGON_FLAP, SoundCategory.HOSTILE, 5.0F, 0.8F + this.rand.nextFloat() * 0.3F, false);
            }
        }

        this.prevAnimTime = this.animTime;
        float f2;
        if (this.getHealth() <= 0.0F) {
            f = (this.rand.nextFloat() - 0.5F) * 8.0F;
            f1 = (this.rand.nextFloat() - 0.5F) * 4.0F;
            f2 = (this.rand.nextFloat() - 0.5F) * 8.0F;
            this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.EXPLOSION_LARGE, this.posX + (double)f, this.posY + 2.0D + (double)f1, this.posZ + (double)f2, 0.0D, 0.0D, 0.0D);
        }

        else {
            this.updateDragonEnderCrystal();
            f = 0.2F / (MathHelper.sqrt((float)(this.motionX * this.motionX + this.motionZ * this.motionZ)) * 10.0F + 1.0F);
            f *= (float)Math.pow(2.0D, this.motionY);
            if (this.slowed) {
                this.animTime += f * 0.5F;
            }

            else {
                this.animTime += f;
            }
        }

        if ((getAttackTarget() != null) && (!getAttackTarget().isEntityAlive())) {
            setAttackTarget(null);
        }

        if (this.master != null) {
            if (getDistanceSq(this.master) > 10000.0D) {
                this.freshVictimForce = true;
            }

            if ((this.master.getAttackTarget() != null)) {
                setAttackTarget(this.master.getAttackTarget());
            }
        }

        else {
            List<?> list = this.world.getEntitiesWithinAABB(EntityEnderColossus.class, this.getEntityBoundingBox().expand(100.0D, 100.0D, 100.0D));
            if ((list != null) && (!list.isEmpty())) {
                for (int i1 = 0; i1 < list.size(); i1++) {
                    Entity entity = (Entity)list.get(i1);
                    if ((entity != null) && ((entity instanceof EntityEnderColossus))) {
                        this.master = ((EntityEnderColossus)entity);
                    }
                }
            }
        }

        this.rotationYaw = MathHelper.wrapDegrees(this.rotationYaw);
        if (this.ringBufferIndex < 0) {
            for (int i = 0; i < this.ringBuffer.length; ++i) {
                this.ringBuffer[i][0] = (double)this.rotationYaw;
                this.ringBuffer[i][1] = this.posY;
            }
        }

        if (++this.ringBufferIndex == this.ringBuffer.length) {
            this.ringBufferIndex = 0;
        }

        this.ringBuffer[this.ringBufferIndex][0] = (double)this.rotationYaw;
        this.ringBuffer[this.ringBufferIndex][1] = this.posY;
        double d0;
        double d1;
        double d2;
        double d10;
        float f12;
        if (this.world.isRemote) {
            if (this.newPosRotationIncrements > 0) {
                d10 = this.posX + (this.interpTargetX - this.posX) / (double)this.newPosRotationIncrements;
                d0 = this.posY + (this.interpTargetY - this.posY) / (double)this.newPosRotationIncrements;
                d1 = this.posZ + (this.interpTargetZ - this.posZ) / (double)this.newPosRotationIncrements;
                d2 = MathHelper.wrapDegrees(this.interpTargetYaw - (double)this.rotationYaw);
                this.rotationYaw = (float)((double)this.rotationYaw + d2 / (double)this.newPosRotationIncrements);
                this.rotationPitch = (float)((double)this.rotationPitch + (this.interpTargetPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
                --this.newPosRotationIncrements;
                this.setPosition(d10, d0, d1);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
        }

        else {
            d10 = this.targetX - this.posX;
            d0 = this.targetY - this.posY;
            d1 = this.targetZ - this.posZ;
            d2 = d10 * d10 + d0 * d0 + d1 * d1;
            if (this.target != null) {
                this.targetX = this.target.posX;
                this.targetY = this.target.posY;
                this.targetZ = this.target.posZ;
            }

            else {
                this.targetX += this.rand.nextGaussian() * 2.0D;
                this.targetZ += this.rand.nextGaussian() * 2.0D;
            }

            if (this.freshVictimForce || d2 < 64.0D || d2 > 22500.0D || this.collidedHorizontally || this.collidedVertically) {
                this.setNewTarget();
            }

            d0 /= (double)MathHelper.sqrt((float)(d10 * d10 + d1 * d1));
            f12 = 0.6F;
            if (d0 < (double)(-f12)) {
                d0 = (double)(-f12);
            }

            if (d0 > (double)f12) {
                d0 = (double)f12;
            }

            this.motionY += d0 * 0.10000000149011612D;
            this.rotationYaw = MathHelper.wrapDegrees(this.rotationYaw);
            double d4 = 180.0D - Math.atan2(d10, d1) * 180.0D / Math.PI;
            double d6 = MathHelper.wrapDegrees(d4 - (double)this.rotationYaw);
            if (d6 > 50.0D) {
                d6 = 50.0D;
            }

            if (d6 < -50.0D) {
                d6 = -50.0D;
            }

            Vec3d vec3 = new Vec3d(this.targetX - this.posX, this.targetY - this.posY, this.targetZ - this.posZ).normalize();
            Vec3d vec32 = new Vec3d((double)MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F), this.motionY, (double)(-MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F))).normalize();
            float f5 = (float)(vec32.dotProduct(vec3) + 0.5D) / 1.5F;
            if (f5 < 0.0F) {
                f5 = 0.0F;
            }

            this.randomYawVelocity *= 0.8F;
            float f6 = MathHelper.sqrt((float)(this.motionX * this.motionX + this.motionZ * this.motionZ)) * 1.0F + 1.0F;
            double d9 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0D + 1.0D;
            if (d9 > 40.0D) {
                d9 = 40.0D;
            }

            this.randomYawVelocity = (float)((double)this.randomYawVelocity + d6 * (0.699999988079071D / d9 / (double)f6));
            this.rotationYaw += this.randomYawVelocity * 0.1F;
            float f7 = (float)(2.0D / (d9 + 1.0D));
            float f8 = 0.06F;
            this.moveRelative(0.0F, 0.0F, -1.0F, f8 * (f5 * f7 + (1.0F - f7)));
            if (this.isEntityAlive()) {
                if (this.target != null) {
                    this.move(MoverType.SELF, this.motionX * 5D, this.motionY * 5D, this.motionZ * 5D);
                }

                else if (this.getAttackTarget() != null) {
                    this.move(MoverType.SELF, this.motionX * 2D, this.motionY * 2D, this.motionZ * 2D);
                }

                else if (this.slowed) {
                    this.move(MoverType.SELF, this.motionX * 0.75D, this.motionY * 0.75D, this.motionZ * 0.75D);
                }

                else {
                    this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                }
            }

            Vec3d vec31 = new Vec3d(this.motionX, this.motionY, this.motionZ).normalize();
            float f9 = (float)(vec31.dotProduct(vec32) + 1.0D) / 2.0F;
            f9 = 0.8F + 0.15F * f9;
            this.motionX *= (double)f9;
            this.motionZ *= (double)f9;
            this.motionY *= 0.9100000262260437D;
        }

        this.renderYawOffset = this.rotationYaw;
        this.dragonPartHead.width = this.dragonPartHead.height = 3.0F;
        this.dragonPartTail1.width = this.dragonPartTail1.height = 2.0F;
        this.dragonPartTail2.width = this.dragonPartTail2.height = 2.0F;
        this.dragonPartTail3.width = this.dragonPartTail3.height = 2.0F;
        this.dragonPartBody.height = 3.0F;
        this.dragonPartBody.width = 5.0F;
        this.dragonPartWing1.height = 2.0F;
        this.dragonPartWing1.width = 4.0F;
        this.dragonPartWing2.height = 3.0F;
        this.dragonPartWing2.width = 4.0F;
        f1 = (float)(this.getMovementOffsets(5, 1.0F)[1] - this.getMovementOffsets(10, 1.0F)[1]) * 10.0F / 180.0F * (float)Math.PI;
        f2 = MathHelper.cos(f1);
        float f10 = -MathHelper.sin(f1);
        float f3 = this.rotationYaw * (float)Math.PI / 180.0F;
        float f11 = MathHelper.sin(f3);
        float f4 = MathHelper.cos(f3);
        this.dragonPartBody.onUpdate();
        this.dragonPartBody.setLocationAndAngles(this.posX + (double)(f11 * 0.5F), this.posY, this.posZ - (double)(f4 * 0.5F), 0.0F, 0.0F);
        this.dragonPartWing1.onUpdate();
        this.dragonPartWing1.setLocationAndAngles(this.posX + (double)(f4 * 4.5F), this.posY + 2.0D, this.posZ + (double)(f11 * 4.5F), 0.0F, 0.0F);
        this.dragonPartWing2.onUpdate();
        this.dragonPartWing2.setLocationAndAngles(this.posX - (double)(f4 * 4.5F), this.posY + 2.0D, this.posZ - (double)(f11 * 4.5F), 0.0F, 0.0F);
        this.collideWithEntities(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartTail1.getEntityBoundingBox().expand(1D, 1D, 1D)));
        this.collideWithEntities(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartTail2.getEntityBoundingBox().expand(1D, 1D, 1D)));
        this.collideWithEntities(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartTail3.getEntityBoundingBox().expand(1D, 1D, 1D)));
        this.collideWithEntities(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing1.getEntityBoundingBox().expand(1D, 1D, 1D)));
        this.collideWithEntities(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing2.getEntityBoundingBox().expand(1D, 1D, 1D)));
        this.collideWithEntities(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartBody.getEntityBoundingBox().expand(1D, 1D, 1D)));
        this.attackEntitiesInList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(2D, 2D, 2D)));
        double[] adouble1 = this.getMovementOffsets(5, 1.0F);
        double[] adouble = this.getMovementOffsets(0, 1.0F);
        f12 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F - this.randomYawVelocity * 0.01F);
        float f13 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F - this.randomYawVelocity * 0.01F);
        this.dragonPartHead.onUpdate();
        this.dragonPartHead.setLocationAndAngles(this.posX + (double)(f12 * 5.5F * f2), this.posY + (adouble[1] - adouble1[1]) * 1.0D + (double)(f10 * 5.5F), this.posZ - (double)(f13 * 5.5F * f2), 0.0F, 0.0F);
        for (int j = 0; j < 3; ++j) {
            MultiPartEntityPart entitydragonpart = null;
            if (j == 0) {
                entitydragonpart = this.dragonPartTail1;
            }

            if (j == 1) {
                entitydragonpart = this.dragonPartTail2;
            }

            if (j == 2) {
                entitydragonpart = this.dragonPartTail3;
            }

            double[] adouble2 = this.getMovementOffsets(12 + j * 2, 1.0F);
            float f14 = this.rotationYaw * (float)Math.PI / 180.0F + this.simplifyAngle(adouble2[0] - adouble1[0]) * (float)Math.PI / 180.0F * 1.0F;
            float f15 = MathHelper.sin(f14);
            float f16 = MathHelper.cos(f14);
            float f17 = 1.5F;
            float f18 = (float)(j + 1) * 2.0F;
            entitydragonpart.onUpdate();
            entitydragonpart.setLocationAndAngles(this.posX - (double)((f11 * f17 + f15 * f18) * f2), this.posY + (adouble2[1] - adouble1[1]) * 1.0D - (double)((f18 + f17) * f10) + 1.5D, this.posZ + (double)((f4 * f17 + f16 * f18) * f2), 0.0F, 0.0F);
        }

        if (!this.world.isRemote) {
            this.slowed = this.destroyBlocksInAABB(this.dragonPartHead.getEntityBoundingBox()) | this.destroyBlocksInAABB(this.dragonPartBody.getEntityBoundingBox()) | this.destroyBlocksInAABB(this.dragonPartTail1.getEntityBoundingBox()) | this.destroyBlocksInAABB(this.dragonPartTail2.getEntityBoundingBox()) | this.destroyBlocksInAABB(this.dragonPartTail3.getEntityBoundingBox());
        }

        if (this.targetY >= 240)
            this.targetY = 240;
    }

    /**
     * Updates the state of the enderdragon's current endercrystal.
     */
    private void updateDragonEnderCrystal() {
        if (this.healingEnderCrystal != null) {
            if (this.healingEnderCrystal.isDead) {
                if (!this.world.isRemote) {
                    this.attackEntityFromPart(this.dragonPartHead, DamageSource.causeExplosionDamage((Explosion)null), 10.0F);
                }

                this.healingEnderCrystal = null;
            }

            else if (this.ticksExisted % 10 == 0 && this.getHealth() < this.getMaxHealth()) {
                this.setHealth(this.getHealth() + 1.0F);
            }
        }

        if (this.rand.nextInt(10) == 0) {
            float f = 32.0F;
            List<?> list = this.world.getEntitiesWithinAABB(EntityEnderCrystal.class, this.getEntityBoundingBox().expand((double)f, (double)f, (double)f));
            EntityEnderCrystal entityendercrystal = null;
            double d0 = Double.MAX_VALUE;
            Iterator<?> iterator = list.iterator();
            while (iterator.hasNext()) {
                EntityEnderCrystal entityendercrystal1 = (EntityEnderCrystal)iterator.next();
                double d1 = entityendercrystal1.getDistanceSq(this);
                if (d1 < d0) {
                    d0 = d1;
                    entityendercrystal = entityendercrystal1;
                }
            }

            this.healingEnderCrystal = entityendercrystal;
        }
    }

    /**
     * Pushes all entities inside the list away from the enderdragon.
     */
    private void collideWithEntities(List<?> arg70970n1) {
        double d0 = (this.dragonPartBody.getEntityBoundingBox().minX + this.dragonPartBody.getEntityBoundingBox().maxX) / 2.0D;
        double d1 = (this.dragonPartBody.getEntityBoundingBox().minZ + this.dragonPartBody.getEntityBoundingBox().maxZ) / 2.0D;
        Iterator<?> iterator = arg70970n1.iterator();
        while (iterator.hasNext() && this.ticksExisted > 20) {
            Entity entity = (Entity)iterator.next();
            if (entity instanceof EntityLivingBase && this.allowStrikeGiant(entity.getClass()) && !(entity instanceof EntityTitan) && entity.canBeCollidedWith()) {
                double d2 = entity.posX - d0;
                double d3 = entity.posZ - d1;
                double d4 = d2 * d2 + d3 * d3;
                double d5 = 5D;
                if (this.target != null && entity == this.target) {
                    d5 = 25D;
                }

                else if (this.getAttackTarget() != null && entity == this.getAttackTarget()) {
                    d5 = 10D;
                }

                else if (this.slowed) {
                    d5 = 3.75D;
                }

                entity.addVelocity(d2 / d4 * d5, 0.33D, d3 / d4 * d5);
            }
        }
    }

    /**
     * Attacks all entities inside this list, dealing 5 hearts of damage.
     */
    private void attackEntitiesInList(List<?> arg70971n1) {
        for (int i = 0; i < arg70971n1.size(); ++i) {
            Entity entity = (Entity)arg70971n1.get(i);
            if (entity instanceof EntityLivingBase && this.allowStrikeGiant(entity.getClass())) {
                ++this.animTime;
                this.playLivingSound();
                entity.attackEntityFrom(DamageSource.causeMobDamage(this), 1000F);
                entity.hurtResistantTime = 0;
            }
        }

        this.collideWithEntities(arg70971n1);
    }

    /**
     * Sets a new target for the flight AI. It can be a random coordinate or a nearby player.
     */
    private void setNewTarget() {
        this.freshVictimForce = false;
        if (this.getAttackTarget() != null) {
            if (this.rand.nextInt() == 0) {
                this.target = this.getAttackTarget();
            }

            else {
                boolean flag = false;
                do {
                    this.targetX = this.getAttackTarget().posX;
                    this.targetY = this.world.getTopSolidOrLiquidBlock(new BlockPos((int)this.getAttackTarget().posX, 0, (int)this.getAttackTarget().posZ)).getY() + (double)(40.0F + this.rand.nextFloat() * 30.0F);
                    this.targetZ = this.getAttackTarget().posZ;
                    this.targetX += MathHelper.cos((float)((this.ticksExisted / 20) + this.getEntityId())) * 60D;
                    this.targetZ += MathHelper.sin((float)((this.ticksExisted / 20) + this.getEntityId())) * 60D;
                    double d0 = this.posX - this.targetX;
                    double d1 = this.posY - this.targetY;
                    double d2 = this.posZ - this.targetZ;
                    flag = d0 * d0 + d1 * d1 + d2 * d2 > 100.0D;
                }

                while (!flag);
                this.target = null;
            }
        }

        else if (this.master != null) {
            boolean flag = false;
            do {
                this.targetX = this.master.posX;
                this.targetY = this.master.posY + (double)(70.0F + this.rand.nextFloat() * 30.0F);
                this.targetZ = this.master.posZ;
                this.targetX += (double)(this.rand.nextFloat() * 100.0F - 50.0F);
                this.targetZ += (double)(this.rand.nextFloat() * 100.0F - 50.0F);
                double d0 = this.posX - this.targetX;
                double d1 = this.posY - this.targetY;
                double d2 = this.posZ - this.targetZ;
                flag = d0 * d0 + d1 * d1 + d2 * d2 > 100.0D;
            }

            while (!flag);
            this.target = null;
        }

        else {
            EntityPlayer player = this.world.getClosestPlayerToEntity(this, 256D);
            if (player != null) {
                boolean flag = false;
                do {
                    this.targetX = player.posX;
                    this.targetY = this.world.getTopSolidOrLiquidBlock(new BlockPos((int)player.posX, 0, (int)player.posZ)).getY() + (double)(20.0F + this.rand.nextFloat() * 30.0F);
                    this.targetZ = player.posZ;
                    this.targetX += (double)(this.rand.nextFloat() * 100.0F - 50.0F);
                    this.targetZ += (double)(this.rand.nextFloat() * 100.0F - 50.0F);
                    double d0 = this.posX - this.targetX;
                    double d1 = this.posY - this.targetY;
                    double d2 = this.posZ - this.targetZ;
                    flag = d0 * d0 + d1 * d1 + d2 * d2 > 100.0D;
                }

                while (!flag);
                this.target = null;
            }

            else {
                boolean flag = false;
                do {
                    this.targetX = 0.0D;
                    this.targetY = (double)(110.0F + this.rand.nextFloat() * 30.0F);
                    this.targetZ = 0.0D;
                    this.targetX += (double)(this.rand.nextFloat() * 100.0F - 50.0F);
                    this.targetZ += (double)(this.rand.nextFloat() * 100.0F - 50.0F);
                    double d0 = this.posX - this.targetX;
                    double d1 = this.posY - this.targetY;
                    double d2 = this.posZ - this.targetZ;
                    flag = d0 * d0 + d1 * d1 + d2 * d2 > 100.0D;
                }

                while (!flag);
                this.target = null;
            }
        }
    }

    /**
     * Simplifies the value of a number by adding/subtracting 180 to the point that the number is between -180 and 180.
     */
    private float simplifyAngle(double arg70973n1) {
        return (float)MathHelper.wrapDegrees(arg70973n1);
    }

    /**
     * Destroys all blocks that aren't associated with 'The End' inside the given bounding box.
     */
    private boolean destroyBlocksInAABB(AxisAlignedBB arg70972n1) {
        int i = MathHelper.floor(arg70972n1.minX);
        int j = MathHelper.floor(arg70972n1.minY);
        int k = MathHelper.floor(arg70972n1.minZ);
        int l = MathHelper.floor(arg70972n1.maxX);
        int i1 = MathHelper.floor(arg70972n1.maxY);
        int j1 = MathHelper.floor(arg70972n1.maxZ);
        boolean flag = false;
        boolean flag1 = false;
        for (int k1 = i; k1 <= l; ++k1) {
            for (int l1 = j; l1 <= i1; ++l1) {
                for (int i2 = k; i2 <= j1; ++i2) {
                    BlockPos pos = new BlockPos(k1, l1, i2);
                    IBlockState state = this.world.getBlockState(pos);
                    Block block = state.getBlock();
                    if (!block.isAir(state, this.world, pos) && this.isEntityAlive()) {
                        if (block != Blocks.OBSIDIAN && block != Blocks.END_STONE && block != Blocks.BEDROCK && block.canEntityDestroy(state, this.world, pos, this) && this.world.getGameRules().getBoolean("mobGriefing")) {
                            flag1 = this.world.setBlockToAir(pos) || flag1;
                        }

                        else {
                            flag = true;
                        }
                    }
                }
            }
        }

        if (flag1) {
            double d1 = arg70972n1.minX + (arg70972n1.maxX - arg70972n1.minX) * (double)this.rand.nextFloat();
            double d2 = arg70972n1.minY + (arg70972n1.maxY - arg70972n1.minY) * (double)this.rand.nextFloat();
            double d0 = arg70972n1.minZ + (arg70972n1.maxZ - arg70972n1.minZ) * (double)this.rand.nextFloat();
            this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.EXPLOSION_LARGE, d1, d2, d0, 0.0D, 0.0D, 0.0D);
        }

        return flag;
    }

    public boolean attackEntityFromPart(MultiPartEntityPart arg70965n1, DamageSource source, float arg70965n3) {
        if (arg70965n1 != this.dragonPartHead) {
            arg70965n3 = arg70965n3 / 4.0F + 1.0F;
        }

        float f1 = this.rotationYaw * (float)Math.PI / 180.0F;
        float f2 = MathHelper.sin(f1);
        float f3 = MathHelper.cos(f1);
        this.targetX = this.posX + (double)(f2 * 5.0F) + (double)((this.rand.nextFloat() - 0.5F) * 2.0F);
        this.targetY = this.posY + (double)(this.rand.nextFloat() * 3.0F) + 1.0D;
        this.targetZ = this.posZ - (double)(f3 * 5.0F) + (double)((this.rand.nextFloat() - 0.5F) * 2.0F);
        this.target = null;
        if (source.getTrueSource() != null && source.getTrueSource() instanceof EntityLivingBase && this.allowStrikeGiant(source.getTrueSource().getClass())) {
            this.setAttackTarget((EntityLivingBase)source.getTrueSource());
            if (this.rand.nextInt(3) == 0)
                this.freshVictimForce = true;
            return attackEntityFrom(source, arg70965n3);
        }

        return false;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource arg70097n1, float arg70097n2) {
        if (((arg70097n1.getTrueSource() instanceof EntityEndermanMinion)) || ((arg70097n1.getTrueSource() instanceof EntityEnderColossus)) || ((arg70097n1.getTrueSource() instanceof EntityDragon)) || ((arg70097n1.getTrueSource() instanceof EntityDragonMinion))) {
            return false;
        }

        else if ((arg70097n1.getDamageType() == "inFire") || (arg70097n1.getDamageType() == "onFire") || (arg70097n1.getDamageType() == "lava") || (arg70097n1.getDamageType() == "inWall") || (arg70097n1.getDamageType() == "drown") || (arg70097n1.getDamageType() == "starve") || (arg70097n1.getDamageType() == "cactus") || (arg70097n1.getDamageType() == "fall") || (arg70097n1.getDamageType() == "outOfWorld") || (arg70097n1.getDamageType() == "generic") || (arg70097n1.getDamageType() == "outOfWorld") || (arg70097n1.getDamageType() == "magic") || (arg70097n1.getDamageType() == "wither") || (arg70097n1.getDamageType() == "anvil") || (arg70097n1.getDamageType() == "fallingBlock") || (arg70097n1.getDamageType() == "explosion") || (arg70097n1.getDamageType() == "indirectMagic")) {
            return false;
        }

        else {
            Entity entity = arg70097n1.getTrueSource();
            if ((entity instanceof EntityLivingBase)) {
                List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(64.0D, 64.0D, 64.0D));
                for (int i = 0; i < list.size(); i++) {
                    Entity mobRef1 = (Entity)list.get(i);
                    if ((mobRef1 instanceof EntityDragonMinion)) {
                        EntityDragonMinion entitypigzombie = (EntityDragonMinion)mobRef1;
                        entitypigzombie.setAttackTarget((EntityLivingBase)entity);
                        entitypigzombie.setRevengeTarget((EntityLivingBase)entity);
                    }

                    setAttackTarget((EntityLivingBase)entity);
                    setRevengeTarget((EntityLivingBase)entity);
                }
            }

            return super.attackEntityFrom(arg70097n1, arg70097n2);
        }
    }

    protected boolean funcE82195(DamageSource arg82195n1, float arg82195n2) {
        return true;
    }

    /**
     * handles entity death timer, experience orb and particle creation
     */
    protected void onDeathUpdate() {
        ++this.deathTicks;
        if (this.deathTicks >= 180 && this.deathTicks <= 200) {
            float f = (this.rand.nextFloat() - 0.5F) * 8.0F;
            float f1 = (this.rand.nextFloat() - 0.5F) * 4.0F;
            float f2 = (this.rand.nextFloat() - 0.5F) * 8.0F;
            this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.EXPLOSION_HUGE, this.posX + (double)f, this.posY + 2.0D + (double)f1, this.posZ + (double)f2, 0.0D, 0.0D, 0.0D);
        }

        int i;
        int j;
        this.move(MoverType.SELF, this.motionX * 0.2D, this.motionY * 0.2D + 0.15D, this.motionZ * 0.2D);
        this.renderYawOffset = this.rotationYaw = this.rotationYawHead += 10.0F;
        if (!this.world.isRemote) {
            if (this.deathTicks == 1) {
                this.world.playBroadcastSound(1028, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
            }

            if (this.deathTicks >= 200) {
                if (this.world.getGameRules().getBoolean("doMobLoot")) {
                    i = 500;
                    while (i > 0) {
                        j = EntityXPOrb.getXPSplit(i);
                        i -= j;
                        this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
                    }

                    this.world.setBlockState(new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), Blocks.DRAGON_EGG.getDefaultState());
                }

                this.setDead();
            }
        }
    }

    /**
     * Makes the entity despawn if requirements are reached
     */
    protected void despawnEntity() {
        this.idleTime = 0;
    }

    /**
     * Return the Entity parts making up this Entity (currently only for dragons)
     */
    public Entity[] getParts() {
        return this.dragonPartArray;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith() {
        return false;
    }

    public World getWorld() {
        return this.world;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ENDERDRAGON_GROWL;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_ENDERDRAGON_HURT;
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume() {
        return 5.0F;
    }

    @Override
    public EntityLiving getMaster() {
        return master;
    }

    @Override
    public void assignMaster(EntityLiving entity) {
        master = entity;
    }

    @Override
    public float fetchVolumeSummon() {
        return 10F;
    }

    @Override
    public float fetchPitchSummon() {
        return 0.5F;
    }

    @Override
    public void setMinionType(int type) {
    }
}
