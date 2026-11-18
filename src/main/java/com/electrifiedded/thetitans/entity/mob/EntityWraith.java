package com.electrifiedded.thetitans.entity.mob;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.registry.TitanMilestones;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanAudio;
import com.electrifiedded.thetitans.registry.TitanTrophies;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityWraith extends EntityMob implements IEntityLivingData {
    private static final DataParameter<Integer> KIND_MOB = EntityDataManager.createKey(EntityWraith.class, DataSerializers.VARINT);
    public int courseChangeCooldown;
    public double xWaypoint;
    public double yWaypoint;
    public double zWaypoint;

    public EntityWraith(World p_i1745_1_) {
        super(p_i1745_1_);
        this.isImmuneToFire = true;
        this.noClip = true;
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, false, false, null));
        this.experienceValue = 20;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(com.electrifiedded.thetitans.TheTitans.adjustValueToProgressionSystem(this.world, 120.0D));
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(com.electrifiedded.thetitans.TheTitans.adjustValueToProgressionSystem(this.world, 6.0D));
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(KIND_MOB, Integer.valueOf(0));
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.assignKindMob(tagCompund.getInteger("Type"));
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("Type", this.fetchKindMob());
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        if (cause.getTrueSource() instanceof EntityPlayer)
            TitanMilestones.runGrant((EntityPlayer) cause.getTrueSource(), TitanTrophies.slaywraithV);
    }

    public int fetchKindMob() {
        return this.dataManager.get(KIND_MOB).intValue();
    }

    public void assignKindMob(int miniontype) {
        this.dataManager.set(KIND_MOB, Integer.valueOf(miniontype));
    }

    public int getTotalArmorValue() {
        int i = super.getTotalArmorValue() + (int)com.electrifiedded.thetitans.TheTitans.adjustValueToProgressionSystem(this.world, 2.0D);
        if (i > 24) {
            i = 24;
        }

        return i;
    }


    /**
     * Called frequently so the entity can update its state every tick as required.
     */
    public void onLivingUpdate() {
        if (this.deathTime == 1)
            this.deathTime = 19;
        if (this.deathTime == 20) {
            for (int i = 0; i < 200; ++i) {
                double d2 = this.rand.nextGaussian() * 0.1D;
                double d0 = 0.1D + this.rand.nextGaussian() * 0.01D;
                double d1 = this.rand.nextGaussian() * 0.1D;
                this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d2, d0, d1);
            }
        }

        if (this.world.isDaytime() && !this.world.isRemote) {
            float f = this.getBrightness();
            if (f < 0.5F && this.world.canBlockSeeSky(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ)))) {
                if (this.ticksExisted % 5 == 0) {
                    this.heal(2F);
                }
            }
        }

        this.renderYawOffset = this.rotationYaw = this.rotationYawHead;
        super.onLivingUpdate();
    }

    public boolean isEntityInsideOpaqueBlock() {
        return false;
    }

    protected void updateAITasks() {
        if (this.getAttackTarget() != null && this.ticksExisted % 20 == 0 && getDistanceSq(this.getAttackTarget()) <= (2F + this.getAttackTarget().width / 2.0F) * (2F + this.getAttackTarget().width / 2.0F))
            this.attackEntityAsMob(getAttackTarget());
        if (this.world.isDaytime() && !this.world.isRemote) {
            float f = this.getBrightness();
            if (f > 0.5F && this.world.canBlockSeeSky(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.posY + 2D), MathHelper.floor(this.posZ)))) {
                this.motionX = 0.0F;
                this.motionY = 0.0F;
                this.motionZ = 0.0F;
                this.xWaypoint = this.posX;
                this.yWaypoint = this.posY - (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 0.5F);
                this.zWaypoint = this.posZ;
                this.attackEntityFrom(DamageSource.OUT_OF_WORLD, 4);
            }
        }

        if (this.getAttackTarget() == null)
            this.getLookHelper().setLookPosition(xWaypoint, yWaypoint, zWaypoint, 20F, 0F);
        double d0 = this.xWaypoint - this.posX;
        double d1 = this.yWaypoint - this.posY;
        double d2 = this.zWaypoint - this.posZ;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;
        if (d3 < 10.0D || d3 > 3600.0D) {
            if (this.getAttackTarget() != null) {
                this.xWaypoint = this.getAttackTarget().posX + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 1F);
                this.yWaypoint = this.getAttackTarget().posY + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 2F);
                this.zWaypoint = this.getAttackTarget().posZ + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 1F);
            }

            else {
                this.xWaypoint = this.posX + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16F);
                this.yWaypoint = this.posY + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16F);
                this.zWaypoint = this.posZ + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16F);
            }
        }

        if (this.courseChangeCooldown-- <= 0) {
            this.courseChangeCooldown += this.rand.nextInt(5) + 2;
            d3 = (double)MathHelper.sqrt(d3);
            this.motionX += d0 / d3 * 0.1D;
            this.motionY += d1 / d3 * 0.1D;
            this.motionZ += d2 / d3 * 0.1D;
        }

        super.updateAITasks();
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    public void fall(float distance, float damageMultiplier) {
    }

    protected void updateFallState(double arg70064n1, boolean arg70064n3, IBlockState state, BlockPos pos) {
    }

    /**
     * Moves the entity based on the specified heading.
     */
    public void travel(float arg70612n1, float p_70612_vertical_, float arg70612n2) {
        this.onGround = false;
        this.isAirBorne = true;
        if (this.isInWater()) {
            this.moveRelative(arg70612n1, p_70612_vertical_, arg70612n2, 0.02F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= 0.800000011920929D;
        }

        else if (this.isInLava()) {
            this.moveRelative(arg70612n1, p_70612_vertical_, arg70612n2, 0.02F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
        }

        else {
            float f2 = 0.91F;
            if (this.onGround) {
                BlockPos underPos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ));
                f2 = this.world.getBlockState(underPos).getBlock().slipperiness * 0.91F;
            }

            float f3 = 0.16277136F / (f2 * f2 * f2);
            this.moveRelative(arg70612n1, p_70612_vertical_, arg70612n2, this.onGround ? 0.1F * f3 : 0.02F);
            f2 = 0.91F;
            if (this.onGround) {
                BlockPos underPos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ));
                f2 = this.world.getBlockState(underPos).getBlock().slipperiness * 0.91F;
            }

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double)f2;
            this.motionY *= (double)f2;
            this.motionZ *= (double)f2;
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d1 = this.posX - this.prevPosX;
        double d0 = this.posZ - this.prevPosZ;
        float f4 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;
        if (f4 > 1.0F) {
            f4 = 1.0F;
        }

        this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    public boolean isOnLadder() {
        return false;
    }

    /**
     * Sets the Entity inside a web block.
     */
    public void setInWeb() {
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource arg70097n1, float arg70097n2) {
        if (this.isEntityInvulnerable(arg70097n1)) {
            return false;
        }

        else if (arg70097n1 instanceof EntityDamageSourceIndirect && !arg70097n1.isMagicDamage()) {
            return false;
        }

        else {
            if (arg70097n1.getTrueSource() != null && !this.world.isRemote) {
                Entity entity = arg70097n1.getTrueSource();
                if (entity instanceof EntityLivingBase) {
                    java.util.List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(64.0D, 64.0D, 64.0D));
                    for (int i = 0; i < list.size(); ++i) {
                        Entity mobRef1 = (Entity)list.get(i);
                        if (mobRef1 instanceof EntityWraith && ((EntityWraith)mobRef1).getAttackTarget() == null) {
                            ((EntityWraith)mobRef1).setAttackTarget((EntityLivingBase)entity);
                        }
                        if (this.getAttackTarget() == null) {
                            this.setAttackTarget((EntityLivingBase)entity);
                        }
                    }
                }
            }

            return super.attackEntityFrom(arg70097n1, arg70097n2);
        }
    }

    public boolean attackEntityAsMob(Entity arg70652n1) {
        boolean flag = super.attackEntityAsMob(arg70652n1);
        if (flag) {
            if (arg70652n1 instanceof EntityLivingBase) {
                byte b0 = 0;
                if (this.world.getDifficulty() == EnumDifficulty.EASY) {
                    b0 = 10;
                }

                else if (this.world.getDifficulty() == EnumDifficulty.NORMAL) {
                    b0 = 30;
                }

                else if (this.world.getDifficulty() == EnumDifficulty.HARD) {
                    b0 = 60;
                }

                if (b0 > 0) {
                    ((EntityLivingBase)arg70652n1).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, b0 * 10, 0));
                    ((EntityLivingBase)arg70652n1).addPotionEffect(new PotionEffect(MobEffects.NAUSEA, b0 * 20, 0));
                }
            }

            return true;
        }

        return flag;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WITHER_AMBIENT;
    }

    // TODO sounds — thetitans:wraithHurt/wraithDeath unregistered; wrapped raw, resolves via sounds.json
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return com.electrifiedded.thetitans.registry.TitanAudio.get("wraithHurt");
    }

    protected SoundEvent getDeathSound() {
        return com.electrifiedded.thetitans.registry.TitanAudio.get("wraithDeath");
    }

    protected float getSoundVolume() {
        return 1.5F;
    }

    protected Item getDropItem() {
        return Items.ROTTEN_FLESH;
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    protected void shedDropRare(int arg70600n1) {
        switch (this.rand.nextInt(4)) {
            case 0:
                this.dropItem(com.electrifiedded.thetitans.registry.TitanGear.sapphire, 1);
                break;
            case 1:
                this.dropItem(com.electrifiedded.thetitans.registry.TitanGear.ruby, 1);
                break;
            case 2:
                this.dropItem(Items.EMERALD, 1);
                break;
            case 3:
                this.dropItem(Items.DIAMOND, 1);
                break;
        }
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn() {
        float f = this.getBrightness();
        return f > 0.5F;
    }
}
