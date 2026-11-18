package com.electrifiedded.thetitans.entity.minion;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossusCrystal;
import com.electrifiedded.thetitans.entity.titan.EntitySilverfishTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitanSpirit;
import com.electrifiedded.thetitans.entity.titan.EntityWitherzilla;
import com.electrifiedded.thetitans.entity.titan.EntityWitherzillaMinion;
import com.electrifiedded.thetitans.entity.titan.EntityXPBomb;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;
import com.electrifiedded.thetitans.entity.titan.ITitan;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAIBreakDoorMinion;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;
import com.electrifiedded.thetitans.proxy.ClientProxy;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanAudio;
import com.electrifiedded.thetitans.util.Maths;

import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import com.google.common.base.Predicate;

import java.util.List;

public class EntitySilverfishMinion extends EntitySilverfish implements IRangedAttackMob, ITemplar {
    private static final DataParameter<Integer> KIND_SERVANT = EntityDataManager.createKey(EntitySilverfishMinion.class, DataSerializers.VARINT);
    public EntityLiving master;
    public boolean masterSubduedIs;
    public int randomSoundDelay;
    private int patternStrike;
    private EntityAIAttackRanged aiArrowAttack = new EntityAIAttackRanged((IRangedAttackMob)this, 1.0D, 10, 64F);
    private float heightOffset = 0.5F;
    private int heightOffsetUpdateTime;
    public EntityLiving toHealMob;

    public Predicate<Entity> userPresenceAllow = new Predicate<Entity>() {
        public boolean apply(Entity arg179983n1) {
            return ((arg179983n1 instanceof EntityWitherzilla)) || ((arg179983n1 instanceof EntityWitherzillaMinion)) || ((arg179983n1 instanceof EntityAnimal)) || ((arg179983n1 instanceof EntityGolem)) || ((arg179983n1 instanceof EntityAgeable)) || ((arg179983n1 instanceof EntityPlayer)) || ((arg179983n1 instanceof EntityAmbientCreature)) || ((arg179983n1 instanceof EntityWaterMob)) || ((arg179983n1 instanceof EntityTameable)) || ((!(arg179983n1 instanceof EntityDragon)) && (!(arg179983n1 instanceof EntityBlaze)) && (!(arg179983n1 instanceof EntityCaveSpider)) && (!(arg179983n1 instanceof EntityCreeper)) && (!(arg179983n1 instanceof EntityEnderman)) && (!(arg179983n1 instanceof EntityPigZombie)) && (!(arg179983n1 instanceof EntitySilverfish)) && (!(arg179983n1 instanceof EntitySkeleton)) && (!(arg179983n1 instanceof EntitySpider)) && (!(arg179983n1 instanceof EntityWither)) && (!(arg179983n1 instanceof EntityZombie)) && (!(arg179983n1 instanceof EntityTitan)) && (!(arg179983n1 instanceof EntityGhast)) && (!(arg179983n1 instanceof EntityMagmaCube)) && (!(arg179983n1 instanceof EntitySlime)) && (!(arg179983n1 instanceof IMinion)) && (!(arg179983n1 instanceof EntityTitanSpirit)) && (!(arg179983n1 instanceof EntityEnderColossusCrystal)) && (!(arg179983n1 instanceof EntityPlayer)));
        }
    };

    public EntitySilverfishMinion(World worldIn) {
        super(worldIn);
        ((PathNavigateGround)getNavigator()).setBreakDoors(true);
        ((PathNavigateGround)getNavigator()).setEnterDoors(true);
        ((PathNavigateGround)getNavigator()).setCanSwim(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(0, new EntityAIAvoidEntity<EntityWitherSkull>(this, EntityWitherSkull.class, 2.0F, 1.2D, 1.75D));
        this.tasks.addTask(0, new EntityAIAvoidEntity<EntityTitanSpirit>(this, EntityTitanSpirit.class, 48.0F, 1.5D, 1.5D));
        this.tasks.addTask(0, new EntityAIBreakDoorMinion(this));
        this.tasks.addTask(1, new EntityAIMoveTowardsRestriction(this, 1.2D));
        this.tasks.addTask(0, new EntityAIFindEntityNearestInjuredAlly(this));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(7, new EntitySilverfishMinion.EntityAIWander(this, 0.8D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        if (this.masterSubduedIs) {
            this.targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, true, this.userPresenceAllow));
        }

        else {
            if (TheTitans.ffaModeTitans)
                this.targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.giantSorterSilverfish));
            else
                this.targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.forAThingToKillSearch));
        }

        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
    }

    public void setDead() {
        super.setDead();
        if ((this.master != null) && ((this.master instanceof EntityTitan))) {
            ((EntityTitan)this.master).retractNumFromKindMinion(fetchKindMinion());
        }
    }

    public boolean isPotionApplicable(PotionEffect arg70687n1) {
        return arg70687n1.getPotion() == MobEffects.POISON ? false : super.isPotionApplicable(arg70687n1);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        return (arg70686n1 != EntitySilverfishMinion.class) && (arg70686n1 != EntitySilverfishTitan.class);
    }

    public void setSwingingArms(boolean swingingArms) {
        // IRangedAttackMob stub
    }

    public void fall(float arg70069n1, float arg70069n2) {
        if (this.fetchKindIntMinion() != 4)
            super.fall(arg70069n1, arg70069n2);
        this.moveForward = 0F;
        this.moveStrafing = 0F;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(KIND_SERVANT, Integer.valueOf(0));
    }

    public void setCombatTask() {
        this.tasks.removeTask(this.aiArrowAttack);
        if (this.patternStrike == 0 && this.fetchKindIntMinion() == 4) {
            this.tasks.addTask(0, this.aiArrowAttack);
        }
    }

    protected SoundEvent getAmbientSound() {
        return TheTitans.isHeraldLevel(fetchKindMinion()) ? com.electrifiedded.thetitans.registry.TitanAudio.get("thetitans:titanSilverfishLiving") : SoundEvents.ENTITY_SILVERFISH_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return TheTitans.isHeraldLevel(fetchKindMinion()) ? com.electrifiedded.thetitans.registry.TitanAudio.get("thetitans:titanSilverfishGrunt") : SoundEvents.ENTITY_SILVERFISH_HURT;
    }

    protected SoundEvent getDeathSound() {
        return TheTitans.isHeraldLevel(fetchKindMinion()) ? com.electrifiedded.thetitans.registry.TitanAudio.get("thetitans:titanSilverfishDeath") : SoundEvents.ENTITY_SILVERFISH_DEATH;
    }

    protected float getSoundPitch() {
        return this.fetchKindIntMinion() == 4 ? super.getSoundPitch() + 0.3F : super.getSoundPitch();
    }

    protected float applyPotionDamageCalculations(DamageSource arg70672n1, float arg70672n2) {
        if (this.fetchKindIntMinion() == 4) {
            arg70672n2 = super.applyPotionDamageCalculations(arg70672n1, arg70672n2);
            if (arg70672n1.getTrueSource() == this) {
                arg70672n2 = 0.0F;
            }

            if (arg70672n1.isMagicDamage()) {
                arg70672n2 = (float)(arg70672n2 * 0.15D);
            }

            return arg70672n2;
        }

        else {
            return super.applyPotionDamageCalculations(arg70672n1, arg70672n2);
        }
    }

    public int getTotalArmorValue() {
        switch (this.fetchKindIntMinion()) {
            case 1:
                return 2;
            case 2:
                return 15;
            case 3:
                return 18;
            case 4:
                return 22;
            default:
                return 0;
        }
    }

    public int fetchKindIntMinion() {
        return this.dataManager.get(KIND_SERVANT).intValue();
    }

    public EnumMinionType fetchKindMinion() {
        return IMinion.typesMinion(fetchKindIntMinion());
    }

    private boolean droppedXp = false;

    protected int getExperiencePoints(net.minecraft.entity.player.EntityPlayer player) {
        if (!this.world.isRemote && !this.droppedXp && this.experienceValue > 0) {
            this.droppedXp = true;
            com.electrifiedded.thetitans.entity.titan.EntityXPBomb xpb = new com.electrifiedded.thetitans.entity.titan.EntityXPBomb(this.world, this.posX, this.posY + 1.0D, this.posZ);
            xpb.assignTotalXP(this.experienceValue);
            this.world.spawnEntity(xpb);
        }
        return 0;
    }

    public void setMinionType(int miniontype) {
        this.dataManager.set(KIND_SERVANT, Integer.valueOf(miniontype));
        this.isImmuneToFire = miniontype >= 3;
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(miniontype >= 3 ? 1.0D : 0.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(IMinion.fetchHpMultiplierMinion(8.0D, miniontype, 0));
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(IMinion.fetchStrikeMultiplierMinion(1.0D, miniontype, 0));
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(IMinion.fetchPaceMultiplierMinion(0.25D, miniontype, 0));
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D + (miniontype * 8));
        this.experienceValue = IMinion.fetchExpMultiplierMinion(5, miniontype);
        this.setHealth(this.getMaxHealth());
    }

    public boolean attackEntityAsMob(Entity arg70652n1) {
        if (super.attackEntityAsMob(arg70652n1)) {
            if (arg70652n1 instanceof EntityLivingBase && this.fetchKindIntMinion() >= 3) {
                byte b0 = 10;
                if (this.world.getDifficulty() == EnumDifficulty.NORMAL) {
                    b0 = 20;
                }

                else if (this.world.getDifficulty() == EnumDifficulty.HARD) {
                    b0 = 30;
                }

                if (b0 > 0) {
                    if (!world.isRemote)
                        ((EntityLivingBase)arg70652n1).addPotionEffect(new PotionEffect(ClientProxy.electricJudgment, b0 * 20, 0));
                }
            }

            return true;
        }

        else {
            return false;
        }
    }

    public void onLivingUpdate() {
        if (this.isEntityAlive() || this.fetchKindIntMinion() != 4)
            super.onLivingUpdate();
        if (this.fetchKindIntMinion() == 3) {
            if (this.rand.nextInt(120) == 0 && this.master == null && (getHealth() > 0.0F) && (!this.world.isRemote) && (this.world.getDifficulty() != EnumDifficulty.PEACEFUL)) {
                EntitySilverfishMinion entitychicken = new EntitySilverfishMinion(this.world);
                entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(this)), (IEntityLivingData)null);
                entitychicken.setMinionType(0);
                this.world.spawnEntity(entitychicken);
            }

            if (this.rand.nextInt(240) == 0 && this.master == null && (getHealth() > 0.0F) && (!this.world.isRemote) && (this.world.getDifficulty() != EnumDifficulty.PEACEFUL)) {
                EntitySilverfishMinion entitychicken = new EntitySilverfishMinion(this.world);
                entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(this)), (IEntityLivingData)null);
                entitychicken.setMinionType(1);
                this.world.spawnEntity(entitychicken);
            }
        }

        if (this.onGround && this.collidedHorizontally)
            this.jump();
        if (this.fetchKindIntMinion() == 4) {
            if (this.ticksExisted % 40 == 0)
                heal(1F);
            if (this.world.rand.nextInt(150) == 1)
                heal(2.0F);
            if (this.world.rand.nextInt(100) == 1 && getHealth() < this.getMaxHealth() * 0.75)
                heal(2.0F);
            if (this.world.rand.nextInt(35) == 1 && getHealth() < this.getMaxHealth() * 0.5)
                heal(5.0F);
            if (this.world.rand.nextInt(30) == 1 && getHealth() < this.getMaxHealth() * 0.25)
                heal(5.0F);
            if (this.world.rand.nextInt(30) == 1 && getHealth() < this.getMaxHealth() * 0.05)
                heal(200.0F);
            if ((!this.onGround) && (this.motionY < 0.0D)) {
                this.motionY *= 0.6D;
            }

            if (this.master == null && (getHealth() > 0.0F) && (!this.world.isRemote) && (this.world.getDifficulty() != EnumDifficulty.PEACEFUL)) {
                if (this.rand.nextInt(60) == 0) {
                    EntitySilverfishMinion entitychicken = new EntitySilverfishMinion(this.world);
                    entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(this)), (IEntityLivingData)null);
                    entitychicken.setMinionType(0);
                    this.world.spawnEntity(entitychicken);
                }

                if (this.rand.nextInt(120) == 0) {
                    EntitySilverfishMinion entitychicken = new EntitySilverfishMinion(this.world);
                    entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(this)), (IEntityLivingData)null);
                    entitychicken.setMinionType(1);
                    this.world.spawnEntity(entitychicken);
                }

                if (this.rand.nextInt(240) == 0) {
                    EntitySilverfishMinion entitychicken = new EntitySilverfishMinion(this.world);
                    entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(this)), (IEntityLivingData)null);
                    entitychicken.setMinionType(2);
                    this.world.spawnEntity(entitychicken);
                }
            }

            if ((this.world.isRemote) && (!this.onGround)) {
                for (int i = 0; i < 3; i++) {
                    this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
                }
            }

            if ((this.rand.nextInt(60) == 0) && (getAttackTarget() != null)) {
                setCombatTask();
                if (!this.onGround) {
                    this.patternStrike = 0;
                }

                else {
                    this.patternStrike = 1;
                }
            }

            --this.heightOffsetUpdateTime;
            if (this.heightOffsetUpdateTime <= 0) {
                jump();
                this.heightOffsetUpdateTime = 100;
                this.heightOffset = 0.5F + (float)this.rand.nextGaussian() * 3.0F;
                this.patternStrike = 0;
            }

            EntityLivingBase entitylivingbase = getAttackTarget();
            if ((this.patternStrike == 0) && (entitylivingbase != null) && !this.world.isRemote) {
                if ((entitylivingbase.posY + entitylivingbase.getEyeHeight() > this.posY + getEyeHeight() + this.heightOffset)) {
                    this.motionY += (0.4D - this.motionY);
                    this.isAirBorne = true;
                }

                this.getLookHelper().setLookPositionWithEntity(entitylivingbase, 180F, 40F);
                double d0 = entitylivingbase.posX - this.posX;
                double d1 = entitylivingbase.posZ - this.posZ;
                double d3 = d0 * d0 + d1 * d1;
                if (d3 > (entitylivingbase.width * entitylivingbase.width) + (this.width * this.width) + 16D) {
                    double d5 = MathHelper.sqrt(d3);
                    this.motionX += (d0 / d5 * 0.6D - this.motionX);
                    this.motionZ += (d1 / d5 * 0.6D - this.motionZ);
                }
            }

            if (this.isEntityAlive() && (!this.world.isRemote) && (this.rand.nextInt(1000) == 0) && (getAttackTarget() != null) && (getHealth() < getMaxHealth() / 2.0F) && (this.master == null)) {
                for (int i = 0; i < 16; i++) {
                    this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
                    this.world.spawnParticle(EnumParticleTypes.FLAME, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
                }

                // custom sound wired (thetitans:titanland)
                playSound(com.electrifiedded.thetitans.registry.TitanAudio.get("thetitans:titanland"), 10000.0F, 1.0F);
                TransformEntity(this);
            }

            if (this.onGround) {
                this.isAirBorne = false;
            }

            List<?> found11 = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(8D, 8D, 8D));
            if (!this.world.isRemote && found11 != null && !found11.isEmpty() && this.ticksExisted % (this.getHealth() < this.getMaxHealth() / 2 ? 40 : 160) == 0) {
                this.world.createExplosion(this, this.posX, this.posY, this.posZ, 8F, false);
                for (int i1 = 0; i1 < found11.size(); i1++) {
                    Entity entity = (Entity)found11.get(i1);
                    if (entity != null && entity instanceof EntityLivingBase && this.allowStrikeGiant(entity.getClass().asSubclass(EntityLivingBase.class))) {
                        entity.motionY += rand.nextDouble();
                        if (!world.isRemote)
                            ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(ClientProxy.electricJudgment, 10, 1));
                        this.world.playEvent((EntityPlayer)null, 1017, new BlockPos(entity.posX, entity.posY, entity.posZ), 0);
                    }
                }
            }
        }

        if (this.fetchKindIntMinion() == 2) {
            if (getAttackTarget() != null) {
                double d0 = getDistanceSq(getAttackTarget());
                if (d0 < 4.0D) {
                    swingArm(net.minecraft.util.EnumHand.MAIN_HAND);
                    attackEntityAsMob(getAttackTarget());
                }

                if ((this.rand.nextInt(40) == 0) && (this.onGround) && (d0 < 256.0D) && (getAttackTarget().posY > this.posY + 3.0D)) {
                    addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 60, 7));
                    faceEntity(getAttackTarget(), 180.0F, 180.0F);
                    double d01 = getAttackTarget().posX - this.posX;
                    double d1 = getAttackTarget().posZ - this.posZ;
                    float f2 = MathHelper.sqrt(d01 * d01 + d1 * d1);
                    jump();
                    this.motionX = (d01 / f2 * 0.75D * 0.75D + this.motionX * 0.75D);
                    this.motionZ = (d1 / f2 * 0.75D * 0.75D + this.motionZ * 0.75D);
                }
            }
        }

        if (this.fetchKindIntMinion() == 1) {
            if ((this.ticksExisted % 40 == 0) && (this.toHealMob != null)) {
                if (this.toHealMob.getHealth() < this.toHealMob.getMaxHealth()) {
                    swingArm(net.minecraft.util.EnumHand.MAIN_HAND);
                    faceEntity(this.toHealMob, 180.0F, getVerticalFaceSpeed());
                    this.toHealMob.heal(4.0F);
                    playSound(SoundEvents.ENTITY_WITHER_SHOOT, 1.0F, 3.0F);
                }

                else {
                    this.toHealMob = null;
                }
            }
        }

        if ((getAttackTarget() != null) && (!getAttackTarget().isEntityAlive())) {
            setAttackTarget(null);
        }

        if (this.master != null) {
            if (getDistanceSq(this.master) > 2304.0D) {
                getMoveHelper().setMoveTo(this.master.posX, this.master.posY, this.master.posZ, 2.0D);
            }

            if ((this.master.getAttackTarget() != null)) {
                if (this.master.getAttackTarget().height < 6F)
                    this.setAttackTarget(this.master.getAttackTarget());
                else
                    this.getLookHelper().setLookPositionWithEntity(this.master.getAttackTarget(), 10F, 40F);
            }

            if ((((EntitySilverfishTitan)this.master).subduedIs) && ((getAttackTarget() instanceof EntityPlayer))) {
                setAttackTarget(null);
                this.masterSubduedIs = true;
            }
        }

        else {
            List<?> list = this.world.getEntitiesWithinAABB(EntitySilverfishTitan.class, this.getEntityBoundingBox().expand(100.0D, 100.0D, 100.0D));
            if ((list != null) && (!list.isEmpty())) {
                for (int i11 = 0; i11 < list.size(); i11++) {
                    Entity entity = (Entity)list.get(i11);
                    if ((entity != null) && ((entity instanceof EntitySilverfishTitan))) {
                        this.master = ((EntitySilverfishTitan)entity);
                    }
                }
            }
        }
    }

    public void TransformEntity(Entity entity) {
        entity.world.newExplosion(entity, entity.posX, entity.posY, entity.posZ, 18.0F, true, entity.world.getGameRules().getBoolean("mobGriefing"));
        EntitySilverfishTitan entitytitan = new EntitySilverfishTitan(entity.world);
        entitytitan.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, 0.0F);
        entity.setDead();
        entitytitan.funcM82206();
        entity.world.spawnEntity(entitytitan);
    }

    public String getName() {
        switch (this.fetchKindIntMinion()) {
            case 1:
                return I18n.translateToLocal("entity.SilverfishPriest.name");
            case 2:
                return I18n.translateToLocal("entity.SilverfishZealot.name");
            case 3:
                return I18n.translateToLocal("entity.SilverfishBishop.name");
            case 4:
                return I18n.translateToLocal("entity.SilverfishTemplar.name");
            default:
                return I18n.translateToLocal("entity.SilverfishLoyalist.name");
        }
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("MinionType", this.fetchKindIntMinion());
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.setMinionType(tagCompund.getInteger("MinionType"));
    }

    protected Item getDropItem() {
        return Items.PAPER;
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        if (master != null)
            return;
        Item item = getDropItem();
        int j = this.rand.nextInt(2);
        if (arg70628n2 > 0) {
            j += this.rand.nextInt(arg70628n2 + 1);
        }

        for (int k = 0; k < j; k++) {
            dropItem(item, 1);
        }

        if ((this.rand.nextInt(30) == 0) || (this.rand.nextInt(1 + arg70628n2) > 0)) {
            entityDropItem(new ItemStack(Blocks.STONE), 0.0F);
        }

        if (this.fetchKindIntMinion() >= 1) {
            j = this.rand.nextInt(2);
            if (arg70628n2 > 0) {
                j += this.rand.nextInt(arg70628n2 + 1);
            }

            for (int k = 0; k < j; k++) {
                dropItem(Items.EXPERIENCE_BOTTLE, 1);
            }

            if (this.fetchKindIntMinion() >= 2) {
                j = this.rand.nextInt(2);
                if (arg70628n2 > 0) {
                    j += this.rand.nextInt(arg70628n2 + 1);
                }

                for (int k = 0; k < j; k++) {
                    dropItem(Items.GOLDEN_APPLE, 1);
                }

                if (this.fetchKindIntMinion() >= 3) {
                    j = this.rand.nextInt(2);
                    if (arg70628n2 > 0) {
                        j += this.rand.nextInt(arg70628n2 + 1);
                    }

                    for (int k = 0; k < j; k++) {
                        switch (this.rand.nextInt(5)) {
                            case 0:dropItem(Items.EMERALD, 1);
                                break;
                            case 1:dropItem(Items.DIAMOND, 1);
                                break;
                            case 2:dropItem(Items.GOLD_INGOT, 1);
                                break;
                            case 3:dropItem(Items.GOLD_INGOT, 1);
                                break;
                            case 4:dropItem(Items.GOLD_INGOT, 1);
                        }
                    }

                    if (this.fetchKindIntMinion() >= 4) {
                        if (this.rand.nextInt(5) == 0) {
                            entityDropItem(new ItemStack(TitanGear.bladeSeedPleasant), 0.0F);
                        }

                        if (this.rand.nextInt(100) == 0) {
                            entityDropItem(new ItemStack(TitanGear.seedsMalgrum), 0.0F);
                        }

                        j = 2 + this.rand.nextInt(5);
                        if (arg70628n2 > 0) {
                            j += this.rand.nextInt(arg70628n2 + 1);
                        }

                        for (int k = 0; k < j; k++) {
                            switch (this.rand.nextInt(3)) {
                                case 0:dropItem(Items.EMERALD, 1);
                                    break;
                                case 1:dropItem(Items.DIAMOND, 1);
                                    break;
                                case 2:dropItem(Items.GOLD_INGOT, 1);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (getIsInvulnerable() || friendlyIs(source.getTrueSource()) || (fetchKindIntMinion() >= 4 && source == DamageSourceExtra.radiation))
            return false;
        if (source.getTrueSource() != null) {
            int difficulty = TheTitans.getDifficulty(world);
            if (fetchKindMinion() == EnumMinionType.ZEALOT && moveStrafing == 0F) {
                boolean shouldDodge;
                switch (difficulty) {
                    case 0: shouldDodge = false; break;
                    case 1: shouldDodge = false; break;
                    case 2: shouldDodge = Maths.chance(10); break;
                    case 3: shouldDodge = Maths.chance(33); break;
                    case 4: shouldDodge = Maths.chance(50); break;
                    case 5: shouldDodge = Maths.chance(75); break;
                    default: shouldDodge = true;
                }

                if (shouldDodge) {
                    renderYawOffset = rotationYaw = rotationYawHead;
                    playSound(com.electrifiedded.thetitans.registry.TitanAudio.get("thetitans:titanSwing"), 1F, 2F);
                    switch (rand.nextInt(3)) {
                        case 0: {
                                moveForward = -2F;
                                moveRelative(0F, 0F, -2F, 0.99F);
                                moveStrafing = 0.01F;
                                break;
                            }

                        case 1: {
                                moveStrafing = 1F;
                                moveRelative(1F, 0F, 0F, 0.25F);
                                break;
                            }

                        case 2: {
                                moveStrafing = -1F;
                                moveRelative(-1F, 0F, 0F, 0.25F);
                            }
                    }

                    jump();
                    return false;
                }
            }

            Entity entity = source.getTrueSource();
            if ((entity instanceof EntityLivingBase)) {
                List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(32.0D, 32.0D, 32.0D));
                for (int i = 0; i < list.size(); i++) {
                    Entity mobRef1 = (Entity)list.get(i);
                    if ((mobRef1 instanceof EntitySilverfishMinion)) {
                        EntitySilverfishMinion entitypigzombie = (EntitySilverfishMinion)mobRef1;
                        entitypigzombie.setAttackTarget((EntityLivingBase)entity);
                        entitypigzombie.setRevengeTarget((EntityLivingBase)entity);
                        entitypigzombie.randomSoundDelay = this.rand.nextInt(40);
                    }

                    setAttackTarget((EntityLivingBase)entity);
                    setRevengeTarget((EntityLivingBase)entity);
                    randomSoundDelay = this.rand.nextInt(40);
                }
            }
        }

        return super.attackEntityFrom(source, amount);
    }

    public void attackEntityWithRangedAttack(EntityLivingBase arg82196n1, float arg82196n2) {
        this.swingArm(net.minecraft.util.EnumHand.MAIN_HAND);
        if (getDistanceSq(arg82196n1) < (arg82196n1.width * arg82196n1.width) + 36D)
            attackEntityAsMob(arg82196n1);
        else
            switch (this.rand.nextInt(4)) {
            case 0:EntityTippedArrow entityarrow = new EntityTippedArrow(this.world, this);
                double arrowdx = arg82196n1.posX - this.posX;
                double arrowdy = arg82196n1.getEntityBoundingBox().minY + arg82196n1.height / 3.0D - entityarrow.posY;
                double arrowdz = arg82196n1.posZ - this.posZ;
                double arrowdist = MathHelper.sqrt(arrowdx * arrowdx + arrowdz * arrowdz);
                entityarrow.shoot(arrowdx, arrowdy + arrowdist * 0.2D, arrowdz, 1.6F, 1.0F);
                entityarrow.setIsCritical(true);
                entityarrow.setDamage(arg82196n2 * 2.0F + this.rand.nextGaussian() * 0.25D + this.world.getDifficulty().getId() * 0.11F);
                playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
                this.world.spawnEntity(entityarrow);
                break;
                // original: EntityPotion meta 32732 (splash harming), 32725 (splash healing) vs undead
            case 1:EntityPotion entitypotion = new EntityPotion(this.world, this, net.minecraft.potion.PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), arg82196n1.isEntityUndead() ? net.minecraft.init.PotionTypes.HEALING : net.minecraft.init.PotionTypes.HARMING));
                double d0 = arg82196n1.posY + 0.5D;
                entitypotion.rotationPitch -= -20.0F;
                double d1 = arg82196n1.posX + arg82196n1.motionX - this.posX;
                double d2 = d0 - this.posY;
                double d3 = arg82196n1.posZ + arg82196n1.motionZ - this.posZ;
                float f1 = MathHelper.sqrt(d1 * d1 + d3 * d3);
                entitypotion.shoot(d1, d2 + f1 * 0.2F, d3, 1.0F, f1 / 20.0F);
                this.world.spawnEntity(entitypotion);
                break;
            case 2:double d01 = getDistanceSq(arg82196n1);
                double d11 = arg82196n1.posX - this.posX;
                double d21 = arg82196n1.getEntityBoundingBox().minY + arg82196n1.height / 2.0F - (this.posY + arg82196n1.height / 2.0F);
                double d31 = arg82196n1.posZ - this.posZ;
                float f = MathHelper.sqrt(MathHelper.sqrt(d01)) * 0.1F;
                this.world.playEvent((EntityPlayer)null, 1009, new BlockPos(this.posX, this.posY, this.posZ), 0);
                EntitySmallFireball entitysmallfireball = new EntitySmallFireball(this.world, this, d11 + getRNG().nextGaussian() * f, d21, d31 + getRNG().nextGaussian() * f);
                entitysmallfireball.posY = (this.posY + this.height + 0.4D);
                this.world.spawnEntity(entitysmallfireball);
                break;
            case 3:arg82196n1.attackEntityFrom(DamageSource.MAGIC, 10F);
                playSound(SoundEvents.ENTITY_WITHER_SHOOT, 1.0F, 3.0F);
                int i = MathHelper.floor(arg82196n1.posX + this.rand.nextDouble());
                int j = MathHelper.floor(arg82196n1.posY + this.rand.nextDouble());
                int k = MathHelper.floor(arg82196n1.posZ + this.rand.nextDouble());
                BlockPos eggpos = new BlockPos(i, j, k);
                if ((this.world.getBlockState(eggpos).getMaterial() == Material.AIR)) {
                    this.world.setBlockState(eggpos, Blocks.MONSTER_EGG.getDefaultState());
                }
        }
    }

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData arg180482n2) {
        Object p_180482_2_1 = super.onInitialSpawn(difficulty, arg180482n2);
        if (!this.world.isRemote) {
            this.setMinionType(0);
            EntityPlayer player = this.world.getClosestPlayerToEntity(this, -1D);
            if (player != null)
                if (rand.nextInt(2) == 0 && player.getTotalArmorValue() >= 15) {
                setMinionType(1);
                if (rand.nextInt(4) == 0 && player.getTotalArmorValue() >= 20) {
                    setMinionType(2);
                    if (rand.nextInt(6) == 0 && player.getTotalArmorValue() >= 40) {
                        setMinionType(3);
                        if (rand.nextInt(10) == 0 && player.getTotalArmorValue() >= 50) {
                            setMinionType(4);
                        }
                    }
                }
            }
        }

        return (IEntityLivingData)p_180482_2_1;
    }

    protected void updateAITasks() {
        this.renderYawOffset = this.rotationYaw = this.rotationYawHead;
        if ((this.randomSoundDelay > 0) && (--this.randomSoundDelay == 0)) {
            playSound(getHurtSound(DamageSource.GENERIC), getSoundVolume(), getSoundPitch() + 0.25F);
        }

        int i;
        int j;
        int k;
        int i1;
        if (this.hurtResistantTime == 1) {
            i = MathHelper.floor(this.posX);
            j = MathHelper.floor(this.posY);
            k = MathHelper.floor(this.posZ);
            boolean flag = false;
            for (int l = 0; !flag && l <= 10 && l >= -10; l = l <= 0 ? 1 - l : 0 - l) {
                for (i1 = 0; !flag && i1 <= 10 && i1 >= -10; i1 = i1 <= 0 ? 1 - i1 : 0 - i1) {
                    for (int j1 = 0; !flag && j1 <= 10 && j1 >= -10; j1 = j1 <= 0 ? 1 - j1 : 0 - j1) {
                        BlockPos eggpos = new BlockPos(i + i1, j + l, k + j1);
                        IBlockState eggstate = this.world.getBlockState(eggpos);
                        if (eggstate.getBlock() == Blocks.MONSTER_EGG) {
                            if (!this.world.getGameRules().getBoolean("mobGriefing")) {
                                this.world.setBlockState(eggpos, ((BlockSilverfish.EnumType)eggstate.getValue(BlockSilverfish.VARIANT)).getModelBlock(), 3);
                            }

                            else {
                                this.world.destroyBlock(eggpos, false);
                            }

                            EntitySilverfishMinion entitysilverfish = new EntitySilverfishMinion(this.world);
                            entitysilverfish.setLocationAndAngles(i + i1 + 0.5D, j + l, k + j1 + 0.5D, 0.0F, 0.0F);
                            entitysilverfish.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitysilverfish)), null);
                            if (!this.world.isRemote)
                                this.world.spawnEntity(entitysilverfish);
                            this.world.createExplosion(entitysilverfish, entitysilverfish.posX, entitysilverfish.posY, entitysilverfish.posZ, 2F, false);
                            entitysilverfish.spawnExplosionParticle();
                            this.world.setBlockToAir(eggpos);
                            if (this.rand.nextBoolean()) {
                                flag = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (this.collidedHorizontally && this.master != null)
            this.motionY = 0.2D;
        super.updateAITasks();
    }

    public int deathTicks;

    public void move(MoverType type, double arg70091n1, double arg70091n3, double arg70091n5) {
        if (deathTicks > 0) {
            super.move(MoverType.SELF, 0.0D, 0.10000000149011612D, 0.0D);
        }

        else {
            super.move(type, arg70091n1, arg70091n3, arg70091n5);
        }
    }

    protected void onDeathUpdate() {
        if (this.fetchKindIntMinion() == 4) {
            --this.ticksExisted;
            ++this.deathTicks;
            if (this.master != null) {
                double mx = this.posX - master.posX;
                double my = (this.posY + this.getEyeHeight()) - (master.posY + master.getEyeHeight());
                double mz = this.posZ - master.posZ;
                short short1 = (short) (getDistance(master) * 2);
                for (int f = 0; f < short1; f++) {
                    double d9 = f / (short1 - 1.0D);
                    double d6 = this.posX + mx * -d9;
                    double d7 = (this.posY + this.getEyeHeight()) + my * -d9;
                    double d8 = this.posZ + mz * -d9;
                    this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, d6, d7, d8, master.motionX, master.motionY + 0.2D, master.motionZ);
                }
            }

            if (!this.world.isRemote) {
                if (this.deathTicks > 150 && this.deathTicks % 5 == 0) {
                    this.dropFewItems(true, 0);
                }

                if (this.deathTicks == 1) {
                    this.world.playBroadcastSound(1018, new BlockPos(this.posX, this.posY, this.posZ), 0);
                }
            }

            if (this.deathTicks >= 180 && this.deathTicks <= 200) {
                float f = (this.rand.nextFloat() - 0.5F) * this.width;
                float f1 = (this.rand.nextFloat() - 0.5F) * this.height;
                float f2 = (this.rand.nextFloat() - 0.5F) * this.width;
                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + (double)f, this.posY + this.getEyeHeight() + (double)f1, this.posZ + (double)f2, 0.0D, 0.0D, 0.0D);
            }

            this.move(MoverType.SELF, 0.0D, 0.10000000149011612D, 0.0D);
            float f = (this.rand.nextFloat() - 0.5F) * this.width;
            float f1 = (this.rand.nextFloat() - 0.5F) * this.height;
            float f2 = (this.rand.nextFloat() - 0.5F) * this.width;
            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + (double)f, this.posY + this.getEyeHeight() + (double)f1, this.posZ + (double)f2, 0.0D, 0.0D, 0.0D);
            this.world.spawnParticle(EnumParticleTypes.LAVA, this.posX + (double)f, this.posY + this.getEyeHeight() + (double)f1, this.posZ + (double)f2, rand.nextGaussian(), rand.nextGaussian(), rand.nextGaussian());
            if (this.deathTicks == 200 && !this.world.isRemote) {
                if (this.master != null) {
                    this.master.heal(this.master.getMaxHealth() / 100F);
                    for (int i = 0; i < 100; ++i) {
                        double d2 = this.rand.nextGaussian() * 0.02D;
                        double d0 = this.rand.nextGaussian() * 0.02D;
                        double d1 = this.rand.nextGaussian() * 0.02D;
                        this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, master.posX + (double)(this.rand.nextFloat() * master.width * 2.0F) - (double)master.width, master.posY + (double)(this.rand.nextFloat() * master.height), master.posZ + (double)(this.rand.nextFloat() * master.width * 2.0F) - (double)master.width, d2, d0, d1);
                    }
                }

                this.setDead();
            }
        }

        else {
            super.onDeathUpdate();
        }
    }

    public class EntityAIFindEntityNearestInjuredAlly extends EntityAIBase {
        private EntitySilverfishMinion fld179434b;
        private EntityLivingBase fld179433e;
        public EntityAIFindEntityNearestInjuredAlly(EntitySilverfishMinion entityCaveSpiderPriest) {
            this.fld179434b = entityCaveSpiderPriest;
        }

        public boolean shouldExecute() {
            if (!fld179434b.isEntityAlive()) {
                return false;
            }

            if (fld179434b.fetchKindMinion() != EnumMinionType.PRIEST) {
                return false;
            }

            if (this.fld179433e != null) {
                return false;
            }

            double d0 = funcF179431();
            List<?> list = this.fld179434b.world.getEntitiesWithinAABB(EntitySilverfishMinion.class, this.fld179434b.getEntityBoundingBox().expand(d0, d0, d0));
            if (list.isEmpty()) {
                return false;
            }

            for (int i = 0; i < list.size(); i++) {
                EntitySilverfishMinion entity = (EntitySilverfishMinion)list.get(i);
                if (entity.getHealth() < entity.getMaxHealth() && entity.isEntityAlive())
                    this.fld179433e = entity;
            }

            return true;
        }

        public boolean shouldContinueExecuting() {
            EntityLivingBase entitylivingbase = this.fld179434b.toHealMob;
            if (entitylivingbase == null) {
                return false;
            }

            if (!entitylivingbase.isEntityAlive()) {
                return false;
            }

            if (entitylivingbase.getHealth() >= entitylivingbase.getMaxHealth()) {
                return false;
            }

            double d0 = funcF179431();
            return this.fld179434b.getDistanceSq(entitylivingbase) <= d0 * d0;
        }

        public void startExecuting() {
            this.fld179434b.toHealMob = ((EntityLiving)this.fld179433e);
            super.startExecuting();
        }

        public void resetTask() {
            this.fld179434b.toHealMob = null;
            this.fld179433e = null;
            super.resetTask();
        }

        public void updateTask() {
            if (this.fld179434b.toHealMob != null && this.fld179434b.getDistance(this.fld179434b.toHealMob) > 16D) {
                this.fld179434b.getNavigator().tryMoveToEntityLiving(this.fld179434b.toHealMob, 1D);
                this.fld179434b.getLookHelper().setLookPositionWithEntity(this.fld179434b.toHealMob, 10F, this.fld179434b.getVerticalFaceSpeed());
            }
        }

        protected double funcF179431() {
            return 24D;
        }
    }

    public class EntityAIWander extends EntityAIBase {
        private EntityCreature entity;
        private double posXZ;
        private double posYZ;
        private double posZZ;
        private double speed;
        public EntityAIWander(EntityCreature p_i1648_1_, double p_i1648_2_) {
            this.entity = p_i1648_1_;
            this.speed = p_i1648_2_;
            setMutexBits(1);
        }

        public boolean shouldExecute() {
            // dropped 1.7.10 getAge()>=100 idle cutoff; no public age getter on EntityCreature in 1.12.2, minor optimization only
            if (this.entity.getRNG().nextInt(10) != 0) {
                return false;
            }

            else {
                Vec3d vec3 = RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);
                if (vec3 == null) {
                    return false;
                }

                else {
                    this.posXZ = vec3.x;
                    this.posYZ = vec3.y;
                    this.posZZ = vec3.z;
                    return true;
                }
            }
        }

        public boolean shouldContinueExecuting() {
            return !this.entity.getNavigator().noPath();
        }

        public void startExecuting() {
            this.entity.getNavigator().tryMoveToXYZ(this.posXZ, this.posYZ, this.posZZ, this.speed);
        }
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
    public boolean friendlyIs(Entity entity) {
        if (entity == null)
            return false;
        return entity instanceof EntityZombieMinion || entity instanceof EntityZombieTitan || entity instanceof EntityGiantZombieBetter;
    }
}
