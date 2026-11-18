package com.electrifiedded.thetitans.entity.minion;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.data.TargetingSorter;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;
import com.electrifiedded.thetitans.entity.titan.ITitan;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;
import com.electrifiedded.thetitans.handler.EventStore;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class EntityGiantZombieBetter extends EntityGiantZombie implements IMinion {
    public EntityLiving master;
    private TargetingSorter sorterVictim = null;

    public EntityGiantZombieBetter(World worldIn) {
        super(worldIn);
        setSize(3.0F, 12.0F);
        this.stepHeight = 3F;
        enablePersistence();
        this.sorterVictim = new TargetingSorter(this);
        this.isImmuneToFire = true;
        this.experienceValue = 6000;
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        if (TheTitans.ffaModeTitans)
            this.targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.giantSorterZombie));
        else
            this.targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.forAThingToKillSearch));
    }

    public int getTotalArmorValue() {
        return 20;
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

    @Override
    public boolean getCanSpawnHere() {
        return EventStore.isStateGiant(world) && super.getCanSpawnHere();
    }

    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        // TODO soft-dep MutantCreatures
        return (arg70686n1 != EntityZombieMinion.class) && (arg70686n1 != EntityGiantZombieBetter.class) && (arg70686n1 != EntityZombieTitan.class);
    }

    protected void despawnEntity() {
        this.ticksExisted = 0;
    }

    public float getEyeHeight() {
        return 10.440001F;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48.0D);
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20000.0D);
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    private EntityLivingBase performHarmJump(double X, double Y, double Z, double dist, double damage, int knock) {
        List<EntityLivingBase> var5 = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().expand(1, 1, 1), ITitan.giantSorterZombie);
        Collections.sort(var5, this.sorterVictim);
        Iterator<?> var2 = var5.iterator();
        Entity var3 = null;
        EntityLivingBase var4 = null;
        while (var2.hasNext()) {
            var3 = (Entity)var2.next();
            var4 = (EntityLivingBase)var3;
            if ((var4 != null) &&(var4 != this) &&(var4.isEntityAlive()) &&(!(var4 instanceof EntityZombieTitan)) &&(!(var4 instanceof EntityGiantZombieBetter)) &&(!(var4 instanceof EntityZombieMinion))) {
                DamageSource var21 = null;
                var21 = DamageSource.causeExplosionDamage((EntityLivingBase)null);
                var21.setExplosion();
                if (rand.nextInt(2) == 0)
                    var21.setDamageBypassesArmor();
                var4.attackEntityFrom(var21, (float)damage);
                var4.attackEntityFrom(DamageSource.FALL, (float)damage / 4.0F);
                this.world.playSound(null, var4.posX, var4.posY, var4.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 0.85F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.5F);
                if (knock != 0) {
                    double ks = 0.25D + rand.nextDouble() + rand.nextDouble();
                    double inair = 0.25D;
                    float f3 = (float)Math.atan2(var4.posZ - this.posZ, var4.posX - this.posX);
                    var4.addVelocity(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
                    if (rand.nextInt(5) == 0)
                        var4.hurtResistantTime = 0;
                }
            }
        }

        return null;
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.ticksExisted % 20 == 0 && (!this.world.isDaytime() || this.rand.nextInt(5) == 0))
            heal(this.world.isDaytime() ? (1F + (this.rand.nextFloat() * 4F)) : (5F + (this.rand.nextFloat() * 15F)));
        // PERF — setSize rebuilds the AABB every call; values are constant per side, so only set on change.
        if (this.world.isRemote)
            { if (this.width != 3.0F) setSize(3.0F, 12.0F); }
        else if (this.width != 1.5F)
            setSize(1.5F, 6.0F);
        this.ignoreFrustumCheck = true;
        if (this.motionX != 0D && this.motionZ != 0D && (this.rand.nextInt(5) == 0)) {
            int i = MathHelper.floor(this.posX);
            int j = MathHelper.floor(this.posY - 0.20000000298023224D);
            int k = MathHelper.floor(this.posZ);
            BlockPos pos = new BlockPos(i, j, k);
            IBlockState state = this.world.getBlockState(pos);
            Block block = state.getBlock();
            if (state.getMaterial() != Material.AIR) {
                this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.BLOCK_CRACK, this.posX + (this.rand.nextFloat() - 0.5D) * this.width, this.getEntityBoundingBox().minY + 0.1D, this.posZ + (this.rand.nextFloat() - 0.5D) * this.width, 4.0D * (this.rand.nextFloat() - 0.5D), 0.5D, (this.rand.nextFloat() - 0.5D) * 4.0D, Block.getStateId(state));
            }
        }
    }

    public float getBlockPathWeight(BlockPos pos) {
        return 0.5F - this.world.getLightBrightness(pos);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_ZOMBIE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ZOMBIE_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        playSound(SoundEvents.ENTITY_IRONGOLEM_STEP, 2.0F, 0.9F);
        double dx = this.posX + 4.0D * -Math.sin(Math.toRadians(this.renderYawOffset));
        double dz = this.posZ - 4.0D * -Math.cos(Math.toRadians(this.renderYawOffset));
        performHarmJump(dx, this.posY, dz, 6.0D, 10 + rand.nextInt(90), 1);
    }

    protected float getSoundVolume() {
        return 7.0F;
    }

    protected Item getDropItem() {
        return Items.ROTTEN_FLESH;
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        if (master != null)
            return;
        int j = this.rand.nextInt(13) + this.rand.nextInt(1 + arg70628n2);
        for (int k = 0; k < j; k++) {
            dropItem(Items.FEATHER, 1);
        }

        j = this.rand.nextInt(13) + this.rand.nextInt(2 + arg70628n2);
        for (int k = 0; k < j; k++) {
            dropItem(Items.ROTTEN_FLESH, 1);
        }

        if ((arg70628n1) && ((this.rand.nextInt(5) == 0) || (this.rand.nextInt(1 + arg70628n2) > 0))) {
            dropItem(Items.IRON_INGOT, 1);
        }

        if ((arg70628n1) && ((this.rand.nextInt(5) == 0) || (this.rand.nextInt(1 + arg70628n2) > 0))) {
            dropItem(Items.CARROT, 1);
        }

        if ((arg70628n1) && ((this.rand.nextInt(5) == 0) || (this.rand.nextInt(1 + arg70628n2) > 0))) {
            dropItem(Items.POTATO, 1);
        }
    }

    protected void appendArmourRandom() {
        switch (this.rand.nextInt(3)) {
            case 0:dropItem(Items.IRON_SHOVEL, 1);
                break;
            case 1:dropItem(Items.IRON_SWORD, 1);
                break;
            case 2:dropItem(Items.IRON_HELMET, 1);
        }
    }

    protected float getSoundPitch() {
        return isChild() ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F + 1.0F : (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F + 0.5F;
    }

    public void onKillEntity(EntityLivingBase entityLivingIn) {
        super.onKillEntity(entityLivingIn);
        if (entityLivingIn.getMaxHealth() <= 100D)
            entityLivingIn.motionY += 15D;
        heal(this.world.isDaytime() ? (5F + (this.rand.nextFloat() * 15F)) : (15F + (this.rand.nextFloat() * 30F)));
        if ((entityLivingIn instanceof EntityVillager)) {
            EntityZombieMinion entityzombie = new EntityZombieMinion(this.world);
            entityzombie.copyLocationAndAnglesFrom(entityLivingIn);
            this.world.removeEntity(entityLivingIn);
            entityzombie.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(this)), (IEntityLivingData)null);
            // TODO 1.12 zombie villager type API (assignVillager no-op)
            if (entityLivingIn.isChild()) {
                entityzombie.setChild(true);
            }

            this.world.spawnEntity(entityzombie);
            this.world.playEvent((EntityPlayer)null, 1016, new BlockPos(this), 0);
        }
    }

    public boolean attackEntityAsMob(Entity par1Entity) {
        if (super.attackEntityAsMob(par1Entity)) {
            if ((par1Entity != null) && ((par1Entity instanceof EntityLivingBase))) {
                if (par1Entity.onGround) {
                    double ks = 1.25D;
                    double inair = 1.25D;
                    float f3 = (float)Math.atan2(par1Entity.posZ - this.posZ, par1Entity.posX - this.posX);
                    par1Entity.addVelocity(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
                }

                else {
                    double ks = 3.0D;
                    double inair = 0.25D;
                    float f3 = (float)Math.atan2(par1Entity.posZ - this.posZ, par1Entity.posX - this.posX);
                    par1Entity.addVelocity(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
                }
            }

            return true;
        }

        return false;
    }

    public void addVelocity(double arg70024n1, double arg70024n3, double arg70024n5) {
        this.motionX += arg70024n1 * 0.1D;
        this.motionY += arg70024n3 * 0.1D;
        this.motionZ += arg70024n5 * 0.1D;
    }

    protected void updateAITasks() {
        if ((this.getAttackTarget() != null) && this.onGround)
            getMoveHelper().setMoveTo(this.getAttackTarget().posX, this.getAttackTarget().posY, this.getAttackTarget().posZ, 1D);
        if (this.collidedHorizontally && this.master != null)
            this.motionY = 0.2D;
        if (this.motionY < -0.95D) {
            double df = 1.0D;
            if (this.motionY < -1.5D)
                df = 1.5D;
            performHarmJump(this.posX, this.posY, this.posZ, 12.0D, 100 * df, 0);
            performHarmJump(this.posX, this.posY, this.posZ, 14.0D, 50 * df, 0);
            performHarmJump(this.posX, this.posY, this.posZ, 16.0D, 25 * df, 0);
        }

        if (!this.world.isRemote && this.getAttackTarget() != null && this.ticksExisted % 30 == 0 && this.world.rand.nextInt(3) == 0) {
            this.renderYawOffset = this.rotationYaw = this.rotationYawHead;
            this.motionY += 1.75D;
            this.posY += 1.5499999523162842D;
            double d1 = this.getAttackTarget().posX - this.posX;
            double d2 = this.getAttackTarget().posZ - this.posZ;
            float d = (float)Math.atan2(d2, d1);
            this.faceEntity(this.getAttackTarget(), 10F, this.getVerticalFaceSpeed());
            d1 = Math.sqrt(d1 * d1 + d2 * d2);
            if (getDistanceSq(this.getAttackTarget()) > (10F + this.getAttackTarget().width / 2.0F) * (10F + this.getAttackTarget().width / 2.0F) + 45D) {
                this.motionX += d1 * 0.05D * Math.cos(d);
                this.motionZ += d1 * 0.05D * Math.sin(d);
            }
        }

        if (this.getAttackTarget() != null)
            this.getLookHelper().setLookPositionWithEntity(this.getAttackTarget(), 10F, 40F);
        if (this.getAttackTarget() != null && this.ticksExisted % 20 == 0 && getDistanceSq(this.getAttackTarget()) <= (14F + this.getAttackTarget().width / 2.0F) * (14F + this.getAttackTarget().width / 2.0F))
            this.attackEntityAsMob(getAttackTarget());
        if (this.master != null) {
            if ((this.master.getAttackTarget() != null)) {
                setAttackTarget(this.master.getAttackTarget());
            }

            else if (getDistanceSq(this.master) > 5000.0D) {
                getMoveHelper().setMoveTo(this.master.posX, this.master.posY, this.master.posZ, 2.0D);
            }
        }

        else {
            List<?> list = this.world.getEntitiesWithinAABB(EntityZombieTitan.class, this.getEntityBoundingBox().expand(100.0D, 100.0D, 100.0D));
            if ((list != null) && (!list.isEmpty())) {
                for (int i1 = 0; i1 < list.size(); i1++) {
                    Entity entity = (Entity)list.get(i1);
                    if ((entity != null) && ((entity instanceof EntityZombieTitan))) {
                        this.master = ((EntityZombieTitan)entity);
                    }
                }
            }
        }

        super.updateAITasks();
    }

    public void fall(float distance, float damageMultiplier) {
    }

    protected void updateFallState(double y, boolean onGround, IBlockState state, BlockPos pos) {
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (getIsInvulnerable()) {
            return false;
        }

        if (((source.getTrueSource() instanceof EntityZombieMinion)) || ((source.getTrueSource() instanceof EntityZombieTitan)) || ((source.getTrueSource() instanceof EntityGiantZombieBetter))) {
            return false;
        }

        Entity entity = source.getTrueSource();
        if ((entity instanceof EntityLivingBase)) {
            List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(128.0D, 64.0D, 128.0D));
            for (int i = 0; i < list.size(); i++) {
                Entity mobRef1 = (Entity)list.get(i);
                if (mobRef1 instanceof EntityGiantZombieBetter) {
                    EntityGiantZombieBetter entitypigzombie = (EntityGiantZombieBetter)mobRef1;
                    if (entitypigzombie.getAttackTarget() == null)
                        entitypigzombie.setAttackTarget((EntityLivingBase)entity);
                    entitypigzombie.setRevengeTarget((EntityLivingBase)entity);
                }

                setAttackTarget((EntityLivingBase)entity);
                setRevengeTarget((EntityLivingBase)entity);
            }
        }

        return super.attackEntityFrom(source, amount);
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
