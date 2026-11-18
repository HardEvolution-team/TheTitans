package com.electrifiedded.thetitans.entity.minion;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.ITitan;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Iterator;
import java.util.List;

public class EntityWitherMinion extends EntityMob implements IRangedAttackMob, IMinion {
    private static final DataParameter<Integer> VICTIM_0_WATCHED = EntityDataManager.createKey(EntityWitherMinion.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> VICTIM_1_WATCHED = EntityDataManager.createKey(EntityWitherMinion.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> VICTIM_2_WATCHED = EntityDataManager.createKey(EntityWitherMinion.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> TICKS_INVUL = EntityDataManager.createKey(EntityWitherMinion.class, DataSerializers.VARINT);
    private float[] xRotationHeads = new float[2];
    private float[] yRotationHeads = new float[2];
    private float[] rotationHeadsPrevX = new float[2];
    private float[] rotationHeadsPrevY = new float[2];
    private int[] nextHeadUpdate = new int[2];
    private int[] idleHeadUpdates = new int[2];
    private int blockBreakCounter;
    public EntityLiving master;

    public EntityWitherMinion(World worldIn) {
        super(worldIn);
        enablePersistence();
        setHealth(getMaxHealth());
        setSize(0.9F, 3.5F);
        this.isImmuneToFire = true;
        ((net.minecraft.pathfinding.PathNavigateGround)getNavigator()).setCanSwim(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0D, 30, 100F));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        if (TheTitans.ffaModeTitans)
            this.targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.giantSorterSkeleton));
        else
            this.targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.forAThingToKillSearch));
        this.experienceValue = 200;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VICTIM_0_WATCHED, Integer.valueOf(0));
        this.dataManager.register(VICTIM_1_WATCHED, Integer.valueOf(0));
        this.dataManager.register(VICTIM_2_WATCHED, Integer.valueOf(0));
        this.dataManager.register(TICKS_INVUL, Integer.valueOf(0));
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

    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        // TODO soft-dep MutantCreatures
        return (arg70686n1 != EntitySkeletonMinion.class) && (arg70686n1 != EntitySkeletonTitan.class) && (arg70686n1 != EntityWitherMinion.class);
    }

    public void setSwingingArms(boolean swingingArms) {
        // IRangedAttackMob stub
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("Invul", getInvulTime());
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        setInvulTime(tagCompund.getInteger("Invul"));
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WITHER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_WITHER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WITHER_DEATH;
    }

    public void onLivingUpdate() {
        this.stepHeight = 1.5F;
        if (this.isEntityAlive()) {
            if (this.master == null) {
                float f = 256.0F;
                List<EntitySkeletonTitan> list = this.world.getEntitiesWithinAABB(EntitySkeletonTitan.class, this.getEntityBoundingBox().expand(f, f, f));
                EntitySkeletonTitan entityendercrystal = null;
                double d0 = Double.MAX_VALUE;
                Iterator<EntitySkeletonTitan> iterator = list.iterator();
                while (iterator.hasNext()) {
                    EntitySkeletonTitan entityendercrystal1 = (EntitySkeletonTitan)iterator.next();
                    double d1 = entityendercrystal1.getDistanceSq(this);
                    if (d1 < d0 && entityendercrystal1.fetchKindSkeleton() == 1) {
                        d0 = d1;
                        entityendercrystal = entityendercrystal1;
                    }
                }

                this.master = entityendercrystal;
            }
        }

        if (getAttackTarget() != null) {
            aimKeyMob(1, getAttackTarget().getEntityId());
            aimKeyMob(2, getAttackTarget().getEntityId());
        }

        if ((!this.onGround) && (this.motionY < 0.0D))
            this.motionY *= 0.5D;
        if ((!this.world.isRemote) && (getWatchedTargetId(0) > 0)) {
            Entity entity = this.world.getEntityByID(getWatchedTargetId(0));
            if (entity != null) {
                if ((this.posY < entity.posY) || ((!isArmored()) && (this.posY < entity.posY + entity.height + 4.0D))) {
                    if (this.motionY < 0.0D) {
                        this.motionY = 0.0D;
                    }

                    this.motionY += 0.6D - this.motionY * 0.6D;
                }

                double d0 = entity.posX - this.posX;
                double d1 = entity.posZ - this.posZ;
                double d3 = d0 * d0 + d1 * d1;
                if (d3 > 9.0D) {
                    double d5 = MathHelper.sqrt(d3);
                    this.motionX += d0 / d5 * 0.6D - this.motionX * 0.6D;
                    this.motionZ += d1 / d5 * 0.6D - this.motionZ * 0.6D;
                    this.rotationYaw = ((float)Math.atan2(this.motionZ, this.motionX) * 57.295776F - 90.0F);
                }
            }
        }

        super.onLivingUpdate();
        for (int i = 0; i < 2; i++) {
            this.rotationHeadsPrevY[i] = this.yRotationHeads[i];
            this.rotationHeadsPrevX[i] = this.xRotationHeads[i];
        }

        for (int i = 0; i < 2; i++) {
            int j = getWatchedTargetId(i + 1);
            Entity mobRef1 = null;
            if (j > 0) {
                mobRef1 = this.world.getEntityByID(j);
            }

            if (mobRef1 != null) {
                double d1 = getHeadX(i + 1);
                double d3 = getHeadY(i + 1);
                double d5 = getHeadZ(i + 1);
                double d6 = mobRef1.posX - d1;
                double d7 = mobRef1.posY + mobRef1.getEyeHeight() - d3;
                double d8 = mobRef1.posZ - d5;
                double d9 = MathHelper.sqrt(d6 * d6 + d8 * d8);
                float f = (float)(Math.atan2(d8, d6) * 180.0D / 3.141592653589793D) - 90.0F;
                float f1 = (float)-(Math.atan2(d7, d9) * 180.0D / 3.141592653589793D);
                this.xRotationHeads[i] = rotate(this.xRotationHeads[i], f1, 40.0F);
                this.yRotationHeads[i] = rotate(this.yRotationHeads[i], f, 10.0F);
            }

            else {
                this.yRotationHeads[i] = rotate(this.yRotationHeads[i], this.renderYawOffset, 10.0F);
            }
        }

        boolean flag = isArmored();
        for (int j = 0; j < 3; j++) {
            double d10 = getHeadX(j);
            double d2 = getHeadY(j);
            double d4 = getHeadZ(j);
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d10 + this.rand.nextGaussian() * 0.30000001192092896D, d2 + this.rand.nextGaussian() * 0.30000001192092896D, d4 + this.rand.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D);
            if ((flag) && (this.world.rand.nextInt(4) == 0)) {
                this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, d10 + this.rand.nextGaussian() * 0.30000001192092896D, d2 + this.rand.nextGaussian() * 0.30000001192092896D, d4 + this.rand.nextGaussian() * 0.30000001192092896D, 0.699999988079071D, 0.699999988079071D, 0.5D);
            }
        }

        if (getInvulTime() > 0) {
            for (int j = 0; j < 3; j++) {
                this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + this.rand.nextGaussian() * 1.0D, this.posY + this.rand.nextFloat() * 3.3F, this.posZ + this.rand.nextGaussian() * 1.0D, 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D);
            }
        }
    }

    protected void updateAITasks() {
        if (this.master != null) {
            if (getDistanceSq(this.master) > 10000.0D) {
                getMoveHelper().setMoveTo(this.master.posX, this.master.posY, this.master.posZ, 2.0D);
            }

            if ((this.master.getAttackTarget() != null)) {
                setAttackTarget(this.master.getAttackTarget());
            }
        }

        else {
            List<?> list = this.world.getEntitiesWithinAABB(EntitySkeletonTitan.class, this.getEntityBoundingBox().expand(256.0D, 256.0D, 256.0D));
            if ((list != null) && (!list.isEmpty())) {
                for (int i1 = 0; i1 < list.size(); i1++) {
                    Entity entity = (Entity)list.get(i1);
                    if ((entity != null) && ((entity instanceof EntitySkeletonTitan)) && (((EntitySkeletonTitan)entity).fetchKindSkeleton() == 1)) {
                        this.master = ((EntitySkeletonTitan)entity);
                    }
                }
            }
        }

        if (getInvulTime() > 0) {
            int i = getInvulTime() - 1;
            if (i <= 0) {
                this.world.createExplosion(this, this.posX, this.posY + getEyeHeight(), this.posZ, 7.0F, this.world.getGameRules().getBoolean("mobGriefing"));
                this.world.playBroadcastSound(1013, new BlockPos(this), 0);
            }

            setInvulTime(i);
            if (this.ticksExisted % 10 == 0) {
                heal(10.0F);
            }
        }

        else {
            super.updateAITasks();
            for (int i = 1; i < 3; i++) {
                if (this.ticksExisted >= this.nextHeadUpdate[(i - 1)]) {
                    int i1 = getWatchedTargetId(i);
                    if (i1 > 0) {
                        Entity entity = this.world.getEntityByID(i1);
                        if ((entity != null) && (entity.isEntityAlive()) && (getDistanceSq(entity) <= 10000D) && (canEntityBeSeen(entity))) {
                            if ((entity instanceof EntityWitherMinion)) {
                                getNavigator().tryMoveToXYZ(entity.posX + (this.rand.nextDouble() * 24.0D - 12.0D), entity.posY, entity.posZ + (this.rand.nextDouble() * 24.0D - 12.0D), 0.75D);
                                getLookHelper().setLookPositionWithEntity(entity, 180.0F, 30.0F);
                            }

                            launchWitherSkullToEntity(i + 1, (EntityLivingBase)entity);
                            this.nextHeadUpdate[(i - 1)] = (this.ticksExisted + 1 + this.rand.nextInt(29));
                            this.idleHeadUpdates[(i - 1)] = 0;
                        }

                        else {
                            aimKeyMob(i, 0);
                        }
                    }

                    else {
                        List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().expand(64.0D, 100D, 64.0D));
                        for (int k1 = 0; (k1 < 2) && (!list.isEmpty()); k1++) {
                            EntityLivingBase entitylivingbase = (EntityLivingBase)list.get(this.rand.nextInt(list.size()));
                            if ((entitylivingbase != this) && (entitylivingbase.isEntityAlive()) && (canEntityBeSeen(entitylivingbase))) {
                                if (entitylivingbase == getAttackTarget()) {
                                    aimKeyMob(i, entitylivingbase.getEntityId()); break;
                                }

                                if (!(entitylivingbase instanceof EntityWitherMinion))
                                    break;
                                aimKeyMob(i, entitylivingbase.getEntityId()); break;
                            }

                            list.remove(entitylivingbase);
                        }
                    }
                }
            }

            if (getAttackTarget() != null) {
                aimKeyMob(0, getAttackTarget().getEntityId());
            }

            else {
                aimKeyMob(0, 0);
            }

            if (this.blockBreakCounter > 0) {
                this.blockBreakCounter -= 1;
                if ((this.blockBreakCounter == 0) && (this.world.getGameRules().getBoolean("mobGriefing"))) {
                    int i = MathHelper.floor(this.posY);
                    int i1 = MathHelper.floor(this.posX);
                    int j1 = MathHelper.floor(this.posZ);
                    boolean flag = false;
                    for (int l1 = -1; l1 <= 1; l1++) {
                        for (int i2 = -1; i2 <= 1; i2++) {
                            for (int j = 0; j <= 3; j++) {
                                int j2 = i1 + l1;
                                int k = i + j;
                                int l = j1 + i2;
                                BlockPos pos = new BlockPos(j2, k, l);
                                net.minecraft.block.state.IBlockState blockState = this.world.getBlockState(pos);
                                Block block = blockState.getBlock();
                                if ((!block.isAir(blockState, this.world, pos)) && (canEntityDestroy(block, this.world, j2, k, l, this))) {
                                    flag = (this.world.destroyBlock(pos, true)) || (flag);
                                }
                            }
                        }
                    }

                    if (flag) {
                        this.world.playEvent((EntityPlayer)null, 1012, new BlockPos(this), 0);
                    }
                }
            }

            if (this.ticksExisted % 20 == 0) {
                heal(1.0F);
            }
        }
    }

    public boolean canEntityDestroy(Block block, IBlockAccess world, int x, int y, int z, Entity entity) {
        return (block != Blocks.BEDROCK) && (block != Blocks.END_PORTAL) && (block != Blocks.END_PORTAL_FRAME) && (block != Blocks.COMMAND_BLOCK);
    }

    public void funcM82206() {
        setInvulTime(220);
        setHealth(getMaxHealth() / 3.0F);
    }

    public void setInWeb() {
    }

    public int getTotalArmorValue() {
        return 15;
    }

    private double getHeadX(int arg82214n1) {
        if (arg82214n1 <= 0) {
            return this.posX;
        }

        float f = (this.renderYawOffset + 180 * (arg82214n1 - 1)) / 180.0F * 3.1415927F;
        float f1 = MathHelper.cos(f);
        return this.posX + f1 * 1.3D;
    }

    private double getHeadY(int arg82208n1) {
        return arg82208n1 <= 0 ? this.posY + 3.0D : this.posY + 2.2D;
    }

    private double getHeadZ(int arg82213n1) {
        if (arg82213n1 <= 0) {
            return this.posZ;
        }

        float f = (this.renderYawOffset + 180 * (arg82213n1 - 1)) / 180.0F * 3.1415927F;
        float f1 = MathHelper.sin(f);
        return this.posZ + f1 * 1.3D;
    }

    private float rotate(float arg82204n1, float arg82204n2, float arg82204n3) {
        float f3 = MathHelper.wrapDegrees(arg82204n2 - arg82204n1);
        if (f3 > arg82204n3) {
            f3 = arg82204n3;
        }

        if (f3 < -arg82204n3) {
            f3 = -arg82204n3;
        }

        return arg82204n1 + f3;
    }

    private void launchWitherSkullToEntity(int arg82216n1, EntityLivingBase arg82216n2) {
        if ((arg82216n2 instanceof EntityWitherMinion)) {
            launchWitherSkullToCoords(arg82216n1, arg82216n2.posX, arg82216n2.posY + arg82216n2.getEyeHeight(), arg82216n2.posZ, false);
        }

        else {
            launchWitherSkullToCoords(arg82216n1, arg82216n2.posX, arg82216n2.posY + arg82216n2.getEyeHeight() * 0.5D, arg82216n2.posZ, (arg82216n1 == 0) && (this.rand.nextFloat() < 0.001F));
            //arg82216n2.attackEntityFrom(DamageSource.causeMobDamage(this), 100F);
            arg82216n2.hurtResistantTime = 0;
        }
    }

    private void launchWitherSkullToCoords(int arg82209n1, double arg82209n2, double arg82209n4, double arg82209n6, boolean arg82209n8) {
        this.world.playEvent((EntityPlayer)null, 1014, new BlockPos(this), 0);
        double d3 = getHeadX(arg82209n1);
        double d4 = getHeadY(arg82209n1);
        double d5 = getHeadZ(arg82209n1);
        double d6 = arg82209n2 - d3;
        double d7 = arg82209n4 - d4;
        double d8 = arg82209n6 - d5;
        EntityWitherSkull entitywitherskull1 = new EntityWitherSkull(this.world, this, d6, d7, d8);
        if (arg82209n8) {
            entitywitherskull1.setInvulnerable(true);
        }

        entitywitherskull1.posY = d4;
        entitywitherskull1.posX = d3;
        entitywitherskull1.posZ = d5;
        this.world.spawnEntity(entitywitherskull1);
    }

    public void attackEntityWithRangedAttack(EntityLivingBase arg82196n1, float arg82196n2) {
        launchWitherSkullToEntity(0, arg82196n1);
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (getIsInvulnerable()) {
            return false;
        }

        if (((source.getTrueSource() instanceof EntitySkeletonMinion)) || ((source.getTrueSource() instanceof EntitySkeletonTitan))) {
            return false;
        }

        if ((source.getTrueSource() instanceof EntityWitherMinion)) {
            getNavigator().tryMoveToXYZ(this.posX + (this.rand.nextDouble() * 24.0D - 12.0D), this.posY, this.posZ + (this.rand.nextDouble() * 24.0D - 12.0D), 1.2D);
            heal(5.0F);
            return false;
        }

        if (source != DamageSource.DROWN) {
            if ((getInvulTime() > 0) && (source != DamageSource.OUT_OF_WORLD)) {
                return false;
            }

            if (isArmored()) {
                Entity entity = source.getImmediateSource();
                if ((entity instanceof EntityArrow)) {
                    return false;
                }
            }

            if ((source.getTrueSource() instanceof EntityLivingBase)) {
                setAttackTarget((EntityLivingBase)source.getTrueSource());
                setRevengeTarget((EntityLivingBase)source.getTrueSource());
            }

            if (this.blockBreakCounter <= 0) {
                this.blockBreakCounter = 20;
            }

            for (int i = 0; i < this.idleHeadUpdates.length; i++) {
                this.idleHeadUpdates[i] += 3;
            }

            Entity entity = source.getTrueSource();
            if ((entity instanceof EntityLivingBase)) {
                List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(256.0D, 256.0D, 256.0D));
                for (int i = 0; i < list.size(); i++) {
                    Entity mobRef1 = (Entity)list.get(i);
                    if ((mobRef1 instanceof EntityWitherMinion)) {
                        EntityWitherMinion entitypigzombie = (EntityWitherMinion)mobRef1;
                        entitypigzombie.setAttackTarget((EntityLivingBase)entity);
                        entitypigzombie.setRevengeTarget((EntityLivingBase)entity);
                    }

                    setAttackTarget((EntityLivingBase)entity);
                    setRevengeTarget((EntityLivingBase)entity);
                }
            }

            return super.attackEntityFrom(source, amount);
        }

        return false;
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        if (master != null)
            return;
        dropItem(Items.NETHER_STAR, 1);
        if (!this.world.isRemote) {
            Iterator<?> iterator = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().expand(50.0D, 100.0D, 50.0D)).iterator();
            while (iterator.hasNext()) {
                EntityPlayer entityplayer = (EntityPlayer)iterator.next();
            }
        }
    }

    protected void despawnEntity() {
        Result result = null;
        if (this.master != null) {
            this.ticksExisted = 0;
        }

        else if ((this.ticksExisted & 0x1F) == 0x1F && (result = ForgeEventFactory.canEntityDespawn(this)) != Result.DEFAULT) {
            if (result == Result.DENY) {
                this.ticksExisted = 0;
            }

            else {
                this.setDead();
            }
        }

        else {
            EntityPlayer entityplayer = this.world.getClosestPlayerToEntity(this, -1.0D);
            if (entityplayer != null) {
                double d0 = entityplayer.posX - this.posX;
                double d1 = entityplayer.posY - this.posY;
                double d2 = entityplayer.posZ - this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (this.canDespawn() && d3 > 40000.0D) {
                    this.setDead();
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }

    public void fall(float distance, float damageMultiplier) {
    }

    public void addPotionEffect(PotionEffect arg70690n1) {
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3.0E8D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6D);
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
    }

    @SideOnly(Side.CLIENT)
    public float getHeadYRotation(int arg82207n1) {
        return this.yRotationHeads[arg82207n1];
    }

    @SideOnly(Side.CLIENT)
    public float getHeadXRotation(int arg82210n1) {
        return this.xRotationHeads[arg82210n1];
    }

    public int getInvulTime() {
        return this.dataManager.get(TICKS_INVUL).intValue();
    }

    public void setInvulTime(int arg82215n1) {
        this.dataManager.set(TICKS_INVUL, Integer.valueOf(arg82215n1));
    }

    public int getWatchedTargetId(int arg82203n1) {
        return this.dataManager.get(arg82203n1 == 0 ? VICTIM_0_WATCHED : (arg82203n1 == 1 ? VICTIM_1_WATCHED : VICTIM_2_WATCHED)).intValue();
    }

    public void aimKeyMob(int arg82211n1, int arg82211n2) {
        this.dataManager.set(arg82211n1 == 0 ? VICTIM_0_WATCHED : (arg82211n1 == 1 ? VICTIM_1_WATCHED : VICTIM_2_WATCHED), Integer.valueOf(arg82211n2));
    }

    public boolean isArmored() {
        return getHealth() <= getMaxHealth() / 2.0F;
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    public boolean startRiding(Entity entityIn, boolean force) {
        return false;
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
