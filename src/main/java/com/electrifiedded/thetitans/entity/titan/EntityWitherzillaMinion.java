package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.entity.minion.EnumMinionType;
import com.electrifiedded.thetitans.entity.minion.IMinion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
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
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.base.Predicate;

import java.util.List;

public class EntityWitherzillaMinion extends EntityMob implements IRangedAttackMob, IMinion {
    private static final DataParameter<Integer> VICTIM_0_WATCHED = EntityDataManager.<Integer>createKey(EntityWitherzillaMinion.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> VICTIM_1_WATCHED = EntityDataManager.<Integer>createKey(EntityWitherzillaMinion.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> VICTIM_2_WATCHED = EntityDataManager.<Integer>createKey(EntityWitherzillaMinion.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> TICKS_INVUL = EntityDataManager.<Integer>createKey(EntityWitherzillaMinion.class, DataSerializers.VARINT);
    private float[] fld82220d = new float[2];
    private float[] fld82221e = new float[2];
    private float[] fld82217f = new float[2];
    private float[] fld82218g = new float[2];
    private int[] fld82223h = new int[2];
    private int[] fld82224i = new int[2];
    private int blockBreakCounter;

    private static final Predicate<Entity> mobSelectorStrike = new Predicate<Entity>() {
        public boolean apply(Entity arg180027n1) {
            return (!(arg180027n1 instanceof EntityWitherzilla)) && (!(arg180027n1 instanceof EntityWitherzillaMinion)) && (!(arg180027n1 instanceof EntityWitherTurret)) && (!(arg180027n1 instanceof EntityWitherTurretGround)) && (!(arg180027n1 instanceof EntityWitherTurretMortar));
        }
    };

    public EntityWitherzillaMinion(World worldIn) {
        super(worldIn);
        setHealth(getMaxHealth());
        setSize(0.9F, 3.5F);
        this.isImmuneToFire = true;
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0D, 10, 40, 48.0F));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.tasks.addTask(7, new EntityAIWander(this, 0.5D));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 0, false, false, mobSelectorStrike));
        this.experienceValue = 20;
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VICTIM_0_WATCHED, Integer.valueOf(0));
        this.dataManager.register(VICTIM_1_WATCHED, Integer.valueOf(0));
        this.dataManager.register(VICTIM_2_WATCHED, Integer.valueOf(0));
        this.dataManager.register(TICKS_INVUL, Integer.valueOf(0));
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        setInvulTime(tagCompund.getInteger("Invul"));
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("Invul", getInvulTime());
    }

    public int getInvulTime() {
        return this.dataManager.get(TICKS_INVUL).intValue();
    }

    public void setInvulTime(int arg82215n1) {
        this.dataManager.set(TICKS_INVUL, Integer.valueOf(arg82215n1));
    }

    public float getBlockPathWeight(BlockPos pos) {
        return this.world.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN ? 10.0F : super.getBlockPathWeight(pos);
    }

    public int getMaxSpawnedInChunk() {
        return 12;
    }

    protected boolean isValidLightLevel() {
        return true;
    }

    public boolean getCanSpawnHere() {
        return true;
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
        if ((getAttackTarget() instanceof EntityWitherzilla)) {
            setAttackTarget(null);
        }

        if (this.getInvulTime() < 1)
            this.motionY *= 0.6000000238418579D;
        if (!this.world.isRemote && this.getWatchedTargetId(0) > 0) {
            Entity entity = this.world.getEntityByID(this.getWatchedTargetId(0));
            if (entity != null) {
                if (this.posY < entity.posY || !this.isArmored() && this.posY < entity.posY + 5.0D + entity.getEyeHeight()) {
                    if (this.motionY < 0.0D) {
                        this.motionY = 0.0D;
                    }

                    this.motionY += (0.5D - this.motionY) * 0.6000000238418579D;
                }

                double d0 = entity.posX - this.posX;
                double d1 = entity.posZ - this.posZ;
                double d3 = d0 * d0 + d1 * d1;
                if (d3 > 9.0D) {
                    double d5 = (double)MathHelper.sqrt(d3);
                    this.motionX += (d0 / d5 * 0.5D - this.motionX) * 0.6000000238418579D;
                    this.motionZ += (d1 / d5 * 0.5D - this.motionZ) * 0.6000000238418579D;
                }
            }
        }

        if (this.motionX * this.motionX + this.motionZ * this.motionZ != 0D) {
            this.rotationYaw = (float)Math.atan2(this.motionZ, this.motionX) * (180F / (float)Math.PI) - 90.0F;
        }

        super.onLivingUpdate();
        for (int i = 0; i < 2; i++) {
            this.fld82218g[i] = this.fld82221e[i];
            this.fld82217f[i] = this.fld82220d[i];
        }

        for (int i = 0; i < 2; i++) {
            int j = getWatchedTargetId(i + 1);
            Entity mobRef1 = null;
            if (j > 0) {
                mobRef1 = this.world.getEntityByID(j);
            }

            if (mobRef1 != null) {
                double d1 = funcU82214(i + 1);
                double d3 = funcV82208(i + 1);
                double d5 = funcW82213(i + 1);
                double d6 = mobRef1.posX - d1;
                double d7 = mobRef1.posY + mobRef1.getEyeHeight() - d3;
                double d8 = mobRef1.posZ - d5;
                double d9 = MathHelper.sqrt(d6 * d6 + d8 * d8);
                float f = (float)(Math.atan2(d8, d6) * 180.0D / 3.141592653589793D) - 90.0F;
                float f1 = (float)-(Math.atan2(d7, d9) * 180.0D / 3.141592653589793D);
                this.fld82220d[i] = funcB82204(this.fld82220d[i], f1, 40.0F);
                this.fld82221e[i] = funcB82204(this.fld82221e[i], f, 10.0F);
            }

            else {
                this.fld82221e[i] = funcB82204(this.fld82221e[i], this.renderYawOffset, 10.0F);
            }
        }

        boolean flag = isArmored();
        for (int j = 0; j < 3; j++) {
            double d10 = funcU82214(j);
            double d2 = funcV82208(j);
            double d4 = funcW82213(j);
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

    protected boolean isEnabledAI() {
        return true;
    }

    protected void updateAITasks() {
        if (getInvulTime() > 0) {
            int i = getInvulTime() - 1;
            if (i <= 0) {
                this.world.newExplosion(this, this.posX, this.posY + getEyeHeight(), this.posZ, 7.0F, false, this.world.getGameRules().getBoolean("mobGriefing"));
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
                if (this.ticksExisted >= this.fld82223h[(i - 1)]) {
                    this.fld82223h[(i - 1)] = (this.ticksExisted + 10 + this.rand.nextInt(10));
                    if ((this.world.getDifficulty() == net.minecraft.world.EnumDifficulty.NORMAL) || (this.world.getDifficulty() == net.minecraft.world.EnumDifficulty.HARD)) {
                        int k2 = i - 1;
                        int l2 = this.fld82224i[(i - 1)];
                        this.fld82224i[k2] = (this.fld82224i[(i - 1)] + 1);
                        if (l2 > 15) {
                            float f = 10.0F;
                            float f1 = 5.0F;
                            double d0 = (this.posX - f + this.rand.nextDouble() * ((this.posX + f) - (this.posX - f)));
                            double d1 = (this.posY - f1 + this.rand.nextDouble() * ((this.posY + f1) - (this.posY - f1)));
                            double d2 = (this.posZ - f + this.rand.nextDouble() * ((this.posZ + f) - (this.posZ - f)));
                            launchWitherSkullToCoords(i + 1, d0, d1, d2, true);
                            this.fld82224i[(i - 1)] = 0;
                        }
                    }

                    int i1 = getWatchedTargetId(i);
                    if (i1 > 0) {
                        Entity entity = this.world.getEntityByID(i1);
                        if ((entity != null) && (entity.isEntityAlive()) && (getDistanceSq(entity) <= 2304.0D) && (canEntityBeSeen(entity))) {
                            launchWitherSkullToEntity(i + 1, (EntityLivingBase)entity);
                            this.fld82223h[(i - 1)] = (this.ticksExisted + this.rand.nextInt(40));
                            this.fld82224i[(i - 1)] = 0;
                        }

                        else {
                            funcC82211(i, 0);
                        }
                    }

                    else {
                        List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(48.0D, 48.0D, 48.0D), mobSelectorStrike);
                        for (int k1 = 0; (k1 < 10) && (!list.isEmpty()); k1++) {
                            EntityLivingBase entitylivingbase = (EntityLivingBase)list.get(this.rand.nextInt(list.size()));
                            if ((entitylivingbase != this) && (entitylivingbase.isEntityAlive()) && (canEntityBeSeen(entitylivingbase))) {
                                if ((entitylivingbase instanceof EntityPlayer)) {
                                    if (((EntityPlayer)entitylivingbase).capabilities.disableDamage)
                                        break;
                                    funcC82211(i, entitylivingbase.getEntityId()); break;
                                }

                                funcC82211(i, entitylivingbase.getEntityId());
                                break;
                            }

                            list.remove(entitylivingbase);
                        }
                    }
                }
            }

            if (getAttackTarget() != null) {
                funcC82211(0, getAttackTarget().getEntityId());
            }

            else {
                funcC82211(0, 0);
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
                                net.minecraft.block.Block block = blockState.getBlock();
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

    public boolean canEntityDestroy(net.minecraft.block.Block block, net.minecraft.world.IBlockAccess world, int x, int y, int z, Entity entity) {
        return (block != Blocks.BEDROCK) && (block != Blocks.END_PORTAL) && (block != Blocks.END_PORTAL_FRAME) && (block != Blocks.COMMAND_BLOCK);
    }

    public void funcM82206() {
        setInvulTime(220);
        setHealth(getMaxHealth() / 3.0F);
    }

    public void setInWeb() {
    }

    public int getTotalArmorValue() {
        return 4;
    }

    private double funcU82214(int arg82214n1) {
        if (arg82214n1 <= 0) {
            return this.posX;
        }

        float f = (this.renderYawOffset + 180 * (arg82214n1 - 1)) / 180.0F * 3.1415927F;
        float f1 = MathHelper.cos(f);
        return this.posX + f1 * 1.2D;
    }

    private double funcV82208(int arg82208n1) {
        return arg82208n1 <= 0 ? this.posY + 3.0D : this.posY + 2.25D;
    }

    private double funcW82213(int arg82213n1) {
        if (arg82213n1 <= 0) {
            return this.posZ;
        }

        float f = (this.renderYawOffset + 180 * (arg82213n1 - 1)) / 180.0F * 3.1415927F;
        float f1 = MathHelper.sin(f);
        return this.posZ + f1 * 1.2D;
    }

    private float funcB82204(float arg82204n1, float arg82204n2, float arg82204n3) {
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
        launchWitherSkullToCoords(arg82216n1, arg82216n2.posX + arg82216n2.motionX + this.motionX, arg82216n2.posY + arg82216n2.motionY + 0.325D, arg82216n2.posZ + arg82216n2.motionZ + this.motionZ, (arg82216n1 == 0) && (this.rand.nextFloat() < 0.001F));
    }

    private void launchWitherSkullToCoords(int arg82209n1, double arg82209n2, double arg82209n4, double arg82209n6, boolean arg82209n8) {
        this.world.playEvent((EntityPlayer)null, 1014, new BlockPos(this), 0);
        double d3 = funcU82214(arg82209n1);
        double d4 = funcV82208(arg82209n1);
        double d5 = funcW82213(arg82209n1);
        double d6 = arg82209n2 - d3;
        double d7 = arg82209n4 - d4;
        double d8 = arg82209n6 - d5;
        EntityWitherSkull entitywitherskull = new EntityWitherSkull(this.world, this, d6, d7, d8);
        if (arg82209n8) {
            entitywitherskull.setInvulnerable(true);
        }

        entitywitherskull.posY = d4;
        entitywitherskull.posX = d3;
        entitywitherskull.posZ = d5;
        this.world.spawnEntity(entitywitherskull);
    }

    public void attackEntityWithRangedAttack(EntityLivingBase arg82196n1, float arg82196n2) {
        launchWitherSkullToEntity(0, arg82196n1);
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if ((isEntityInvulnerable(source)) || (source.isExplosion())) {
            return false;
        }

        if ((source != DamageSource.DROWN) && (!(source.getTrueSource() instanceof EntityWitherzillaMinion)) && (!(source.getTrueSource() instanceof EntityWitherzilla))) {
            if ((getInvulTime() > 0) && (source != DamageSource.OUT_OF_WORLD)) {
                return false;
            }

            if (isArmored()) {
                Entity entity = source.getImmediateSource();
                if ((entity instanceof EntityArrow)) {
                    return false;
                }
            }

            Entity entity = source.getTrueSource();
            if ((entity instanceof EntityLivingBase)) {
                List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(32.0D, 32.0D, 32.0D));
                for (Object mobRef1Obj : list) {
                    Entity mobRef1 = (Entity) mobRef1Obj;
                    if ((mobRef1 instanceof EntityWitherzillaMinion)) {
                        EntityWitherzillaMinion entitypigzombie = (EntityWitherzillaMinion)mobRef1;
                        entitypigzombie.setAttackTarget((EntityLivingBase)entity);
                        entitypigzombie.setRevengeTarget((EntityLivingBase)entity);
                        entitypigzombie.funcC82211(1, entity.getEntityId());
                        entitypigzombie.funcC82211(2, entity.getEntityId());
                    }

                    setAttackTarget((EntityLivingBase)entity);
                    setRevengeTarget((EntityLivingBase)entity);
                    funcC82211(1, entity.getEntityId());
                    funcC82211(2, entity.getEntityId());
                }
            }

            if (((entity != null) && ((entity instanceof EntityWitherzilla))) || ((entity != null) && ((entity instanceof EntityWitherzillaMinion)))) {
                return false;
            }

            if (this.blockBreakCounter <= 0) {
                this.blockBreakCounter = 20;
            }

            for (int i = 0; i < this.fld82224i.length; i++) {
                this.fld82224i[i] += 3;
            }

            return super.attackEntityFrom(source, amount);
        }

        return false;
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        if (this.rand.nextInt(4) == 0) {
            dropItem(Items.NETHER_STAR, 1);
        }
    }

    protected void despawnEntity() {
        this.ticksExisted = 0;
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
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3.0E9D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6D);
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
    }

    @SideOnly(Side.CLIENT)
    public float funcA82207(int arg82207n1) {
        return this.fld82221e[arg82207n1];
    }

    @SideOnly(Side.CLIENT)
    public float funcR82210(int arg82210n1) {
        return this.fld82220d[arg82210n1];
    }

    public int getWatchedTargetId(int arg82203n1) {
        return this.dataManager.get(arg82203n1 == 0 ? VICTIM_0_WATCHED : (arg82203n1 == 1 ? VICTIM_1_WATCHED : VICTIM_2_WATCHED)).intValue();
    }

    public void funcC82211(int arg82211n1, int arg82211n2) {
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

    public EnumMinionType fetchKindMinion() {
        return EnumMinionType.LOYALIST;
    }

    @Override
    public void setMinionType(int type) {
    }

    @Override
    public EntityLiving getMaster() {
        return null;
    }

    @Override
    public void assignMaster(EntityLiving entity) {
    }

    @Override
    public boolean beginGrounded() {
        return false;
    }

    public void setSwingingArms(boolean swingingArms) {}
}
