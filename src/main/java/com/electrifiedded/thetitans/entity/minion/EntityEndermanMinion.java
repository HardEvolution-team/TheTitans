package com.electrifiedded.thetitans.entity.minion;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossus;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossusCrystal;
import com.electrifiedded.thetitans.entity.titan.EntityHarcadiumArrow;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitanSpirit;
import com.electrifiedded.thetitans.entity.titan.EntityXPBomb;
import com.electrifiedded.thetitans.entity.titan.ITitan;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAIBreakDoorMinion;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAIHurtByTargetTitan;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;
import com.electrifiedded.thetitans.proxy.ClientProxy;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanAudio;
import com.electrifiedded.thetitans.util.Maths;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
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
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityPotion;
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
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

import java.util.IdentityHashMap;
import java.util.List;

public class EntityEndermanMinion extends EntityEnderman implements IRangedAttackMob, ITemplar {
    @Deprecated
    private static boolean[] blocksCarriable = new boolean[256];
    private static final DataParameter<Integer> KIND_SERVANT =
            EntityDataManager.createKey(EntityEndermanMinion.class, DataSerializers.VARINT);
    private static final DataParameter<Byte> SCREAMING =
            EntityDataManager.createKey(EntityEndermanMinion.class, DataSerializers.BYTE);
    public EntityLiving master;
    public int randomSoundDelay;
    private static IdentityHashMap<Block, Boolean> carriableV;
    public EntityLiving toHealMob;
    private int patternStrike;
    private EntityAIAttackRanged aiArrowAttack = new EntityAIAttackRanged((IRangedAttackMob)this, 1.0D, 5, 64F);

    public EntityEndermanMinion(World worldIn) {
        super(worldIn);
        setSize(0.5F, 2.88F);
        this.stepHeight = 1.0F;
        ((net.minecraft.pathfinding.PathNavigateGround)getNavigator()).setBreakDoors(true);
        ((net.minecraft.pathfinding.PathNavigateGround)getNavigator()).setEnterDoors(true);
        ((net.minecraft.pathfinding.PathNavigateGround)getNavigator()).setCanSwim(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(0, new EntityAIAvoidEntity(this, EntityWitherSkull.class, 2.0F, 1.0D, 1.75D));
        this.tasks.addTask(0, new EntityAIAvoidEntity(this, EntityTitanSpirit.class, 48.0F, 1.5D, 1.5D));
        this.tasks.addTask(0, new EntityAIBreakDoorMinion(this));
        this.tasks.addTask(1, new EntityAIMoveTowardsRestriction(this, 1.2D));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(7, new EntityAIWander(this, 0.5D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 16.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.tasks.addTask(0, new EntityAIFindEntityNearestInjuredAlly(this));
        this.targetTasks.addTask(0, new EntityAIHurtByTargetTitan(this, true, new Class[] {
            EntityEndermanMinion.class, EntityEnderColossus.class, EntityDragonMinion.class
        }
        ));
        if (TheTitans.ffaModeTitans)
            this.targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.colossusSorterEnder));
        else
            this.targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLiving.class, 0, false, false, ITitan.forAThingToKillSearch));
    }

    public String getName() {
        switch (this.fetchKindIntMinion()) {
            case 1:
                return I18n.translateToLocal("entity.EndermanPriest.name");
            case 2:
                return I18n.translateToLocal("entity.EndermanZealot.name");
            case 3:
                return I18n.translateToLocal("entity.EndermanBishop.name");
            case 4:
                return I18n.translateToLocal("entity.EndermanTemplar.name");
            default:
                return I18n.translateToLocal("entity.EndermanLoyalist.name");
        }
    }

    public void setDead() {
        super.setDead();
        if ((this.master != null) && ((this.master instanceof EntityTitan))) {
            ((EntityTitan)this.master).retractNumFromKindMinion(fetchKindMinion());
        }
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48.0D);
        this.setMinionType(this.fetchKindIntMinion());
    }

    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        return (arg70686n1 != EntityEnderman.class) && (arg70686n1 != EntityEndermanMinion.class) && (arg70686n1 != EntityEnderColossus.class) && (arg70686n1 != EntityDragon.class) && (arg70686n1 != EntityDragonMinion.class) && (arg70686n1 != EntityEnderColossusCrystal.class);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(KIND_SERVANT, Integer.valueOf(0));
        this.dataManager.register(SCREAMING, Byte.valueOf((byte)0));
    }

    @Override
    public boolean isScreaming() {
        return this.dataManager.get(SCREAMING).byteValue() > 0;
    }

    public void assignScreaming(boolean screaming) {
        this.dataManager.set(SCREAMING, Byte.valueOf((byte)(screaming ? 1 : 0)));
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
        // cosmetic ranged-attack arm swing; minion has no dedicated animation flag, no-op
    }

    public void setCombatTask() {
        this.tasks.removeTask(this.aiArrowAttack);
        if (this.patternStrike == 0 && this.fetchKindIntMinion() == 4) {
            this.tasks.addTask(0, this.aiArrowAttack);
        }
    }

    protected SoundEvent getAmbientSound() {
        if (TheTitans.isHeraldLevel(this.fetchKindMinion()))
            return getAttackTarget() == null && !isScreaming() ? TitanAudio.get("thetitans:titanEnderColossusLiving") : TitanAudio.get("thetitans:titanEnderColossusScreamLong");
        return isScreaming() ? SoundEvents.ENTITY_ENDERMEN_SCREAM : SoundEvents.ENTITY_ENDERMEN_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return TheTitans.isHeraldLevel(this.fetchKindMinion()) ? TitanAudio.get("thetitans:titanEnderColossusGrunt") : SoundEvents.ENTITY_ENDERMEN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return TheTitans.isHeraldLevel(this.fetchKindMinion()) ? TitanAudio.get("thetitans:titanEnderColossusDeath") : SoundEvents.ENTITY_ENDERMEN_DEATH;
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
        return this.dataManager.get(KIND_SERVANT);
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
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(IMinion.fetchHpMultiplierMinion(40.0D, miniontype, 2));
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(IMinion.fetchStrikeMultiplierMinion(7.0D, miniontype, 2));
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(IMinion.fetchPaceMultiplierMinion(0.325D, miniontype, 2));
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D + (miniontype * 8));
        this.experienceValue = IMinion.fetchExpMultiplierMinion(15, miniontype);
        this.setHealth(this.getMaxHealth());
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("MinionType", this.fetchKindIntMinion());
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.setMinionType(tagCompund.getInteger("MinionType"));
    }

    protected Entity locateToLookAtUser() {
        EntityPlayer entityplayer = this.world.getClosestPlayerToEntity(this, 64.0D);
        if (entityplayer != null) {
            if ((isRegisteredUser(entityplayer)) && !entityplayer.capabilities.disableDamage && (getAttackTarget() == null)) {
                playSound(SoundEvents.ENTITY_ENDERMEN_STARE, 10.0F, 1.0F);
                assignScreaming(true);
                setAttackTarget(entityplayer);
                faceEntity(entityplayer, 180.0F, 180.0F);
            }
        }

        return null;
    }

    protected boolean isRegisteredUser(EntityPlayer arg70821n1) {
        ItemStack itemstack = arg70821n1.inventory.armorInventory.get(3);
        if ((!itemstack.isEmpty()) && (itemstack.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN))) {
            return false;
        }

        Vec3d vec3 = arg70821n1.getLook(1.0F).normalize();
        Vec3d vec31 = new Vec3d(this.posX - arg70821n1.posX, this.getEntityBoundingBox().minY + getEyeHeight() - (arg70821n1.posY + arg70821n1.getEyeHeight()), this.posZ - arg70821n1.posZ);
        double d0 = vec31.length();
        vec31 = vec31.normalize();
        double d1 = vec3.dotProduct(vec31);
        return d1 > 1.0D - 0.025D / d0 ? arg70821n1.canEntityBeSeen(this) : false;
    }

    public float getEyeHeight() {
        return 2.55F;
    }

    public boolean attackEntityAsMob(Entity arg70652n1) {
        boolean flag = super.attackEntityAsMob(arg70652n1);
        if (flag) {
            if ((arg70652n1 instanceof EntityMob || arg70652n1 instanceof EntityGolem || arg70652n1 instanceof EntityPlayer))
                teleportRandomly();
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
                        ((EntityLivingBase)arg70652n1).addPotionEffect(new PotionEffect(ClientProxy.electricJudgment, b0 * 20, 1));
                }
            }
        }

        return flag;
    }

    public void onLivingUpdate() {
        if (getAttackTarget() != null)
            assignScreaming(true);
        if (this.fetchKindIntMinion() == 3) {
            if (this.rand.nextInt(120) == 0 && this.master == null && (getHealth() > 0.0F) && (!this.world.isRemote) && (this.world.getDifficulty() != EnumDifficulty.PEACEFUL)) {
                EntityEndermanMinion entitychicken = new EntityEndermanMinion(this.world);
                entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                entitychicken.setMinionType(0);
                this.world.spawnEntity(entitychicken);
            }

            if (this.rand.nextInt(240) == 0 && this.master == null && (getHealth() > 0.0F) && (!this.world.isRemote) && (this.world.getDifficulty() != EnumDifficulty.PEACEFUL)) {
                EntityEndermanMinion entitychicken = new EntityEndermanMinion(this.world);
                entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                entitychicken.setMinionType(1);
                this.world.spawnEntity(entitychicken);
            }
        }

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
                    EntityEndermanMinion entitychicken = new EntityEndermanMinion(this.world);
                    entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                    entitychicken.setMinionType(0);
                    this.world.spawnEntity(entitychicken);
                }

                if (this.rand.nextInt(120) == 0) {
                    EntityEndermanMinion entitychicken = new EntityEndermanMinion(this.world);
                    entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                    entitychicken.setMinionType(1);
                    this.world.spawnEntity(entitychicken);
                }

                if (this.rand.nextInt(240) == 0) {
                    EntityEndermanMinion entitychicken = new EntityEndermanMinion(this.world);
                    entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                    entitychicken.setMinionType(2);
                    this.world.spawnEntity(entitychicken);
                }
            }

            if ((this.world.isRemote) && (!this.onGround)) {
                for (int i = 0; i < 3; i++) {
                    this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.EXPLOSION_NORMAL, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
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

            if (this.onGround) {
                this.isAirBorne = false;
            }

            if (this.isEntityAlive() && (!this.world.isRemote) && (this.rand.nextInt(1000) == 0) && (getAttackTarget() != null) && (getHealth() < getMaxHealth() / 2.0F) && (this.master == null)) {
                for (int i = 0; i < 16; i++) {
                    this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
                    this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.CRIT_MAGIC, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
                }

                // wired custom sound thetitans:titanland
                playSound(TitanAudio.get("thetitans:titanland"), 10000.0F, 1.0F);
                TransformEntity(this);
            }

            EntityLivingBase entitylivingbase = getAttackTarget();
            if ((this.patternStrike == 0) && (entitylivingbase != null) && !this.world.isRemote) {
                if (this.posY < entitylivingbase.posY + entitylivingbase.height + 4D) {
                    if (this.motionY < 0.0D) {
                        this.motionY = 0.0D;
                    }

                    this.motionY += (0.5D - this.motionY);
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

            List<?> found11 = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(8D, 8D, 8D));
            if (!this.world.isRemote && found11 != null && !found11.isEmpty() && this.ticksExisted % (this.getHealth() < this.getMaxHealth() / 2 ? 40 : 160) == 0) {
                this.world.createExplosion(this, this.posX, this.posY, this.posZ, 8F, false);
                for (int i1 = 0; i1 < found11.size(); i1++) {
                    Entity entity = (Entity)found11.get(i1);
                    if (entity != null && entity instanceof EntityLivingBase && this.allowStrikeGiant(((EntityLivingBase)entity).getClass())) {
                        entity.motionY += rand.nextDouble();
                        if (!world.isRemote)
                            ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(ClientProxy.electricJudgment, 10, 1));
                        this.world.playEvent((EntityPlayer)null, 1017, new BlockPos((int)entity.posX, (int)entity.posY, (int)entity.posZ), 0);
                    }
                }
            }
        }

        if (this.getAttackTarget() != null) {
            if (this.world.rand.nextInt(5) == 1) {
                EntityLivingBase e = this.getAttackTarget();
                if (e != null) {
                    if (getDistance(e) < this.width + e.width + 1D) {
                        if ((this.world.rand.nextInt(3) == 0) || (this.world.rand.nextInt(2) == 1)) {
                            if (attackEntityAsMob(e) && this.rand.nextInt(5) == 0) {
                                teleportRandomly();
                            }
                        }
                    }
                }
            }
        }

        if (this.fetchKindIntMinion() == 2) {
            if (getAttackTarget() != null) {
                double d0 = getDistanceSq(getAttackTarget());
                if (d0 < 4.0D) {
                    swingArm(EnumHand.MAIN_HAND);
                    attackEntityAsMob(getAttackTarget());
                }

                if ((this.rand.nextInt(40) == 0) && (this.onGround) && (d0 < 256.0D) && (getAttackTarget().posY > this.posY + 3.0D)) {
                    teleportToEntity(getAttackTarget());
                    teleportRandomly();
                    attackEntityAsMob(getAttackTarget());
                }
            }
        }

        if (this.fetchKindIntMinion() == 1) {
            if ((this.ticksExisted % 40 == 0) && (this.toHealMob != null)) {
                if (this.toHealMob.getHealth() < this.toHealMob.getMaxHealth()) {
                    swingArm(EnumHand.MAIN_HAND);
                    faceEntity(this.toHealMob, 180.0F, getVerticalFaceSpeed());
                    this.toHealMob.heal(5.0F);
                    this.toHealMob.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 1));
                    playSound(SoundEvents.ENTITY_WITHER_SHOOT, 1.0F, 3.0F);
                    for (int i = 0; i < 10; i++) {
                        double d0 = this.rand.nextGaussian() * 0.02D;
                        double d1 = this.rand.nextGaussian() * 0.02D;
                        double d2 = this.rand.nextGaussian() * 0.02D;
                        this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.HEART, this.toHealMob.posX + this.rand.nextFloat() * this.toHealMob.width * 2.0F - this.toHealMob.width, this.toHealMob.posY + 0.5D + this.rand.nextFloat() * this.toHealMob.height, this.toHealMob.posZ + this.rand.nextFloat() * this.toHealMob.width * 2.0F - this.toHealMob.width, d0, d1, d2);
                    }
                }

                else {
                    this.toHealMob = null;
                }
            }
        }

        locateToLookAtUser();
        if (getAttackTarget() != null && this.getAttackTarget().getDistanceSq(this) > 256.0D && this.ticksExisted % 30 == 0 && !this.world.isRemote) {
            this.teleportToEntity(this.getAttackTarget());
        }

        if ((this.world.isDaytime()) && (!this.world.isRemote)) {
            float f = getBrightness();
            if ((f > 0.5F) && (this.world.canSeeSky(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ)))) && (this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F)) {
                setAttackTarget(null);
                assignScreaming(false);
                teleportRandomly();
            }
        }

        if ((isWet() || isBurning()) && this.fetchKindMinion() != EnumMinionType.TEMPLAR) {
            setAttackTarget(null);
            assignScreaming(true);
            teleportRandomly();
        }

        if (isWet() && this.fetchKindMinion() != EnumMinionType.TEMPLAR) {
            assignScreaming(true);
            teleportRandomly();
            attackEntityFrom(DamageSource.ON_FIRE, 4.0F);
            this.hurtTime = 1;
            this.limbSwingAmount = 1.5F;
            if (this.world.isRemote) {
                for (int i = 0; i < 15; i++) {
                    if (getHealth() <= 20.0F) {
                        this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.LAVA, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
                    }

                    else {
                        this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.FLAME, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
                    }

                    this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
                }
            }

            if (this.rand.nextInt(10) == 0) {
                setFire(1);
            }

            if (((this.rand.nextInt(60) == 0) || ((this.rand.nextInt(10) == 0) && (getHealth() <= 15.0F))) && !this.world.isRemote) {
                this.world.newExplosion(this, this.posX + (this.rand.nextDouble() * 1.0D - 0.5D), this.posY + this.rand.nextDouble() * 3.0D, this.posZ + (this.rand.nextDouble() * 1.0D - 0.5D), 0.0F, true, true);
            }

            if (((this.rand.nextInt(1000) == 0) || (getHealth() <= 1.0F)) && !this.world.isRemote) {
                playSound(getDeathSound(), getSoundVolume(), getSoundPitch());
                this.world.newExplosion(this, this.posX, this.posY + 1.0D, this.posZ, 2.0F, true, true);
                this.motionY += 3.0D;
                attackEntityFrom(DamageSource.ON_FIRE, Float.MAX_VALUE);
            }
        }

        if (getAttackTarget() != null) {
            assignScreaming(true);
        }

        else if (!this.isWet() && !this.isBurning() && this.getHealth() <= this.getMaxHealth() / 10F) {
            assignScreaming(false);
        }

        if (this.world.isRemote) {
            for (int i = 0; i < 2; i++) {
                this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
            }
        }

        this.isJumping = false;
        if ((getAttackTarget() != null) && (getRNG().nextInt(20) == 0)) {
            if ((getAttackTarget().getDistanceSq(this) < 2.0D) && (!this.world.isRemote) && !(this.fetchKindMinion() == EnumMinionType.TEMPLAR)) {
                teleportRandomly();
            }
        }

        if ((this.master != null) && (getDistanceSq(this.master) > 1024.0D) && (!this.world.isRemote)) {
            teleportToEntity(this.master);
        }

        super.onLivingUpdate();
    }

    protected void updateAITasks() {
        if ((this.randomSoundDelay > 0) && (--this.randomSoundDelay == 0)) {
            playSound(SoundEvents.ENTITY_ENDERMEN_SCREAM, getSoundVolume(), getSoundPitch() + 0.25F);
        }

        if (this.collidedHorizontally && this.master != null)
            this.motionY = 0.2D;
        if ((getAttackTarget() != null) && (!getAttackTarget().isEntityAlive())) {
            setAttackTarget(null);
        }

        if (this.master != null) {
            if ((this.master.getAttackTarget() != null)) {
                if (this.master.getAttackTarget().height < 6F)
                    this.setAttackTarget(this.master.getAttackTarget());
                else
                    this.getLookHelper().setLookPositionWithEntity(this.master.getAttackTarget(), 10F, 40F);
            }
        }

        else {
            List<?> list = this.world.getEntitiesWithinAABB(EntityEnderColossus.class, this.getEntityBoundingBox().expand(256.0D, 256.0D, 256.0D));
            if ((list != null) && (!list.isEmpty())) {
                for (int i1 = 0; i1 < list.size(); i1++) {
                    Entity entity = (Entity)list.get(i1);
                    if ((entity != null) && ((entity instanceof EntityEnderColossus))) {
                        this.master = ((EntityEnderColossus)entity);
                    }
                }
            }
        }

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
                    this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.FIREWORKS_SPARK, d6, d7, d8, master.motionX, master.motionY + 0.2D, master.motionZ);
                }
            }

            if (!this.world.isRemote) {
                if (this.deathTicks > 150 && this.deathTicks % 5 == 0) {
                    this.dropFewItems(true, 0);
                }

                if (this.deathTicks == 1) {
                    this.world.playBroadcastSound(1018, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
                }
            }

            if (this.deathTicks >= 180 && this.deathTicks <= 200) {
                float f = (this.rand.nextFloat() - 0.5F) * this.width;
                float f1 = (this.rand.nextFloat() - 0.5F) * this.height;
                float f2 = (this.rand.nextFloat() - 0.5F) * this.width;
                this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.EXPLOSION_HUGE, this.posX + (double)f, this.posY + this.getEyeHeight() + (double)f1, this.posZ + (double)f2, 0.0D, 0.0D, 0.0D);
            }

            this.move(MoverType.SELF, 0.0D, 0.10000000149011612D, 0.0D);
            float f = (this.rand.nextFloat() - 0.5F) * this.width;
            float f1 = (this.rand.nextFloat() - 0.5F) * this.height;
            float f2 = (this.rand.nextFloat() - 0.5F) * this.width;
            this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.EXPLOSION_LARGE, this.posX + (double)f, this.posY + this.getEyeHeight() + (double)f1, this.posZ + (double)f2, 0.0D, 0.0D, 0.0D);
            this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.LAVA, this.posX + (double)f, this.posY + this.getEyeHeight() + (double)f1, this.posZ + (double)f2, rand.nextGaussian(), rand.nextGaussian(), rand.nextGaussian());
            if (this.deathTicks == 200 && !this.world.isRemote) {
                if (this.master != null) {
                    this.master.heal(this.master.getMaxHealth() / 100F);
                    for (int i = 0; i < 100; ++i) {
                        double d2 = this.rand.nextGaussian() * 0.02D;
                        double d0 = this.rand.nextGaussian() * 0.02D;
                        double d1 = this.rand.nextGaussian() * 0.02D;
                        this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.EXPLOSION_LARGE, master.posX + (double)(this.rand.nextFloat() * master.width * 2.0F) - (double)master.width, master.posY + (double)(this.rand.nextFloat() * master.height), master.posZ + (double)(this.rand.nextFloat() * master.width * 2.0F) - (double)master.width, d2, d0, d1);
                    }
                }

                this.setDead();
            }
        }

        else {
            super.onDeathUpdate();
        }
    }

    protected boolean teleportRandomly() {
        double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
        double d1 = this.posY + (double)(this.rand.nextInt(64) - 32);
        double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
        return this.isEntityAlive() && teleportTo(d0, d1, d2);
    }

    protected boolean teleportToEntity(Entity arg70816n1) {
        Vec3d vec3 = new Vec3d(this.posX - arg70816n1.posX, this.getEntityBoundingBox().minY + this.height / 2.0F - arg70816n1.posY + arg70816n1.getEyeHeight(), this.posZ - arg70816n1.posZ);
        vec3 = vec3.normalize();
        double d0 = 16.0D;
        double d1 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3.x * d0;
        double d2 = this.posY + (this.rand.nextInt(16) - 8) - vec3.y * d0;
        double d3 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3.z * d0;
        return this.isEntityAlive() && teleportTo(d1, d2, d3);
    }

    protected boolean teleportTo(double arg70825n1, double arg70825n3, double arg70825n5) {
        EnderTeleportEvent event = new EnderTeleportEvent(this, arg70825n1, arg70825n3, arg70825n5, 0.0F);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        }

        double d3 = this.posX;
        double d4 = this.posY;
        double d5 = this.posZ;
        this.posX = event.getTargetX();
        this.posY = event.getTargetY();
        this.posZ = event.getTargetZ();
        boolean flag = false;
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.posY);
        int k = MathHelper.floor(this.posZ);
        if (this.world.isBlockLoaded(new BlockPos(i, j, k))) {
            boolean flag1 = false;
            while ((!flag1) && (j > 0)) {
                net.minecraft.block.state.IBlockState iblockstate = this.world.getBlockState(new BlockPos(i, j - 1, k));
                if (iblockstate.getMaterial().blocksMovement()) {
                    flag1 = true;
                }

                else {
                    this.posY -= 1.0D;
                    j--;
                }
            }

            if (flag1) {
                setPosition(this.posX, this.posY, this.posZ);
                if ((this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty()) && (!this.world.containsAnyLiquid(this.getEntityBoundingBox()))) {
                    flag = true;
                }
            }
        }

        if (!flag) {
            setPosition(d3, d4, d5);
            return false;
        }

        short short1 = 128;
        for (int l = 0; l < short1; l++) {
            double d6 = l / (short1 - 1.0D);
            float f = (this.rand.nextFloat() - 0.5F) * 0.2F;
            float f1 = (this.rand.nextFloat() - 0.5F) * 0.2F;
            float f2 = (this.rand.nextFloat() - 0.5F) * 0.2F;
            double d7 = d3 + (this.posX - d3) * d6 + (this.rand.nextDouble() - 0.5D) * this.width * 2.0D;
            double d8 = d4 + (this.posY - d4) * d6 + this.rand.nextDouble() * this.height;
            double d9 = d5 + (this.posZ - d5) * d6 + (this.rand.nextDouble() - 0.5D) * this.width * 2.0D;
            this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.PORTAL, d7, d8, d9, f, f1, f2);
            if (this.isWet()) {
                if (getHealth() <= 20.0F) {
                    this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.LAVA, d7, d8, d9, f, f1, f2);
                }

                else {
                    this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.FLAME, d7, d8, d9, f, f1, f2);
                }

                this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.SMOKE_LARGE, d7, d8, d9, f, f1, f2);
            }
        }

        this.world.playSound(null, d3, d4, d5, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.HOSTILE, 1.0F, 1.0F);
        playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        return true;
    }

    protected Item getDropItem() {
        return Items.ENDER_PEARL;
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        if (master != null)
            return;
        super.dropFewItems(arg70628n1, arg70628n2);
        int j = this.rand.nextInt(2 + arg70628n2);
        for (int k = 0; k < j; k++) {
            dropItem(Items.ENDER_EYE, 1);
        }

        j = this.rand.nextInt(3 + arg70628n2);
        for (int k = 0; k < j; k++) {
            dropItem(Items.COAL, 1);
        }

        if ((this.rand.nextInt(5) == 0) || (this.rand.nextInt(1 + arg70628n2) > 0)) {
            entityDropItem(new ItemStack(Blocks.END_STONE), 0.0F);
        }

        if ((this.rand.nextInt(20) == 0) || (this.rand.nextInt(1 + arg70628n2) > 0)) {
            entityDropItem(new ItemStack(Blocks.OBSIDIAN), 0.0F);
        }

        if (this.fetchKindIntMinion() >= 1) {
            j = 1 + this.rand.nextInt(4);
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
                    if (this.rand.nextInt(10) == 0) {
                        entityDropItem(new ItemStack(Items.GOLDEN_APPLE, 1, 1), 0.0F);
                    }

                    else {
                        dropItem(Items.GOLDEN_APPLE, 1);
                    }
                }

                if (this.fetchKindIntMinion() >= 3) {
                    j = this.rand.nextInt(2);
                    if (arg70628n2 > 0) {
                        j += this.rand.nextInt(arg70628n2 + 1);
                    }

                    for (int k = 0; k < j; k++) {
                        switch (this.rand.nextInt(5)) {
                            case 0:entityDropItem(new ItemStack(Blocks.EMERALD_BLOCK, 1, 0), 0.0F);
                                break;
                            case 1:entityDropItem(new ItemStack(Blocks.DIAMOND_BLOCK, 1, 0), 0.0F);
                                break;
                            case 2:entityDropItem(new ItemStack(Blocks.GOLD_BLOCK, 1, 0), 0.0F);
                                break;
                            case 3:entityDropItem(new ItemStack(Blocks.GOLD_BLOCK, 1, 0), 0.0F);
                                break;
                            case 4:entityDropItem(new ItemStack(Blocks.GOLD_BLOCK, 1, 0), 0.0F);
                        }
                    }

                    entityDropItem(new ItemStack(Blocks.OBSIDIAN), 0.0F);
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
                                case 0:entityDropItem(new ItemStack(Blocks.EMERALD_BLOCK, 1, 0), 0.0F);
                                    break;
                                case 1:entityDropItem(new ItemStack(Blocks.DIAMOND_BLOCK, 1, 0), 0.0F);
                                    break;
                                case 2:entityDropItem(new ItemStack(Blocks.GOLD_BLOCK, 1, 0), 0.0F);
                            }
                        }

                        entityDropItem(new ItemStack(Blocks.OBSIDIAN), 0.0F);
                    }
                }
            }
        }
    }

    public void fall(float arg70069n1, float damageMultiplier) {
        if (this.fetchKindIntMinion() != 4)
            super.fall(arg70069n1, damageMultiplier);
        this.moveForward = 0F;
        this.moveStrafing = 0F;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if ((source instanceof EntityDamageSourceIndirect)) {
            if (!this.world.isRemote && !(this.fetchKindMinion() == EnumMinionType.TEMPLAR)) {
                for (int i = 0; i < 64; ++i) {
                    this.teleportRandomly();
                }
            }

            return false;
        }

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
                    playSound(TitanAudio.get("thetitans:titanSwing"), 1F, 2F);
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

            if (!this.world.isRemote) {
                assignScreaming(true);
            }

            if (((source instanceof EntityDamageSource)) && ((source.getTrueSource() instanceof EntityPlayer))) {
                if (((source.getTrueSource() instanceof EntityPlayerMP)) && (((EntityPlayerMP)source.getTrueSource()).interactionManager.isCreative())) {
                    assignScreaming(false);
                }

                else {
                }
            }
        }

        boolean flag = super.attackEntityFrom(source, amount);
        if (this.rand.nextInt(2) != 0 && !(this.fetchKindMinion() == EnumMinionType.TEMPLAR)) {
            teleportRandomly();
        }

        Entity entity = source.getTrueSource();
        if ((entity instanceof EntityLivingBase)) {
            List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(64.0D, 64.0D, 64.0D));
            for (int i = 0; i < list.size(); i++) {
                Entity mobRef1 = (Entity)list.get(i);
                if ((mobRef1 instanceof EntityEndermanMinion)) {
                    EntityEndermanMinion entitypigzombie = (EntityEndermanMinion)mobRef1;
                    entitypigzombie.setAttackTarget((EntityLivingBase)entity);
                    entitypigzombie.setRevengeTarget((EntityLivingBase)entity);
                    entitypigzombie.randomSoundDelay = this.rand.nextInt(40);
                }

                setAttackTarget((EntityLivingBase)entity);
                setRevengeTarget((EntityLivingBase)entity);
                randomSoundDelay = this.rand.nextInt(40);
            }
        }

        return flag;
    }

    static {
        blocksCarriable[Block.getIdFromBlock(Blocks.GRASS)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.DIRT)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.SAND)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.GRAVEL)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.YELLOW_FLOWER)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.RED_FLOWER)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.BROWN_MUSHROOM)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.RED_MUSHROOM)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.TNT)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.CACTUS)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.CLAY)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.PUMPKIN)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.MELON_BLOCK)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.MYCELIUM)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.CRAFTING_TABLE)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.DIAMOND_BLOCK)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.FURNACE)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.LIT_FURNACE)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.CHEST)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.GOLD_BLOCK)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.LOG)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.LOG2)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.LEAVES)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.LEAVES2)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.IRON_BLOCK)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.PLANKS)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.TORCH)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.ANVIL)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.BREWING_STAND)] = true;
        blocksCarriable[Block.getIdFromBlock(Blocks.BOOKSHELF)] = true;
        for (int x = 0; x < blocksCarriable.length; x++) {
            if (blocksCarriable[x]) setCarriable(Block.getBlockById(x), true);
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

    public static void setCarriable(Block block, boolean canCarry) {
        if (carriableV == null) carriableV = new IdentityHashMap<Block, Boolean>(4096);
        carriableV.put(block, Boolean.valueOf(canCarry));
    }

    public static boolean getCarriable(Block block) {
        Boolean ret = (Boolean)carriableV.get(block);
        return ret != null ? ret.booleanValue() : false;
    }

    public void TransformEntity(Entity entity) {
        entity.world.newExplosion(entity, entity.posX, entity.posY, entity.posZ, 45.0F, true, entity.world.getGameRules().getBoolean("mobGriefing"));
        EntityEnderColossus entitytitan = new EntityEnderColossus(entity.world);
        entitytitan.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, 0.0F);
        entity.setDead();
        entitytitan.funcM82206();
        entity.world.spawnEntity(entitytitan);
        entitytitan.playSound(TitanAudio.get("thetitans:titanEnderColossusRoar"), 100.0F, 0.5F);
        entitytitan.assignCooldownTimerRoar(1310);
        entitytitan.assignScreaming(true);
    }

    public void attackEntityWithRangedAttack(EntityLivingBase arg82196n1, float arg82196n2) {
        this.swingArm(EnumHand.MAIN_HAND);
        if (getDistanceSq(arg82196n1) < (this.width * this.width) + (arg82196n1.width * arg82196n1.width) + 45D)
            attackEntityAsMob(arg82196n1);
        else
            switch (this.rand.nextInt(5)) {
            case 0:this.world.newExplosion(this, arg82196n1.posX, arg82196n1.posY, arg82196n1.posZ, 1.0F * arg82196n1.width, false, false);
                arg82196n1.attackEntityFrom(DamageSourceExtra.lightningBolt, 49.0F);
                this.world.addWeatherEffect(new EntityLightningBolt(this.world, arg82196n1.posX, arg82196n1.posY, arg82196n1.posZ, false));
                break;
            case 1:for (int i = 0; i < 100; i++) {
                    EntityHarcadiumArrow entityarrow = new EntityHarcadiumArrow(this.world, this, arg82196n1, 4F, 45F);
                    entityarrow.setIsCritical(true);
                    entityarrow.setDamage(arg82196n2 * 2.0F + this.rand.nextGaussian() * 0.25D + this.world.getDifficulty().getId() * 0.11F);
                    this.world.spawnEntity(entityarrow);
                    double d8 = 4D;
                    Vec3d vec3 = getLook(1.0F);
                    entityarrow.posX = (this.posX + vec3.x * d8);
                    entityarrow.posY = (this.posY + vec3.y * d8 + 1D);
                    entityarrow.posZ = (this.posZ + vec3.z * d8);
                }

                break;
            case 2:for (int i = 0; i < 200; i++) {
                    EntityPotion entitypotion = new EntityPotion(this.world, this,
                            net.minecraft.potion.PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), arg82196n1.isEntityUndead() ? net.minecraft.init.PotionTypes.HEALING : net.minecraft.init.PotionTypes.HARMING));

                    double d0 = arg82196n1.posY + 0.5D;
                    entitypotion.rotationPitch -= -20.0F;
                    double d1 = arg82196n1.posX + arg82196n1.motionX - this.posX;
                    double d2 = d0 - this.posY;
                    double d3 = arg82196n1.posZ + arg82196n1.motionZ - this.posZ;
                    float f1 = MathHelper.sqrt(d1 * d1 + d3 * d3);
                    entitypotion.shoot(d1, d2 + f1 * 0.2F, d3, 2F, 25F);
                    this.world.spawnEntity(entitypotion);
                }

                break;
            case 3:for (int i = 0; i < 50; i++) {
                    getDistanceSq(arg82196n1);
                    double d11 = arg82196n1.posX - this.posX;
                    double d21 = arg82196n1.getEntityBoundingBox().minY + arg82196n1.height / 2.0F - (this.posY + arg82196n1.height / 2.0F);
                    double d31 = arg82196n1.posZ - this.posZ;
                    EntityGhastGuardFireball entitysmallfireball = new EntityGhastGuardFireball(this.world, this, d11 + (getRNG().nextGaussian() * 9F), d21, d31 + (getRNG().nextGaussian() * 9F));
                    entitysmallfireball.posY = (this.posY + 2D);
                    this.world.spawnEntity(entitysmallfireball);
                }

                break;
            case 4:this.world.newExplosion(this, arg82196n1.posX, arg82196n1.posY + 1D, arg82196n1.posZ, 2.0F, false, false);
                arg82196n1.attackEntityFrom(DamageSourceExtra.lightningBolt, 100.0F);
                this.world.addWeatherEffect(new EntityLightningBolt(this.world, arg82196n1.posX, arg82196n1.posY, arg82196n1.posZ, false));
                this.world.addWeatherEffect(new EntityLightningBolt(this.world, arg82196n1.posX, arg82196n1.posY, arg82196n1.posZ, false));
                this.world.addWeatherEffect(new EntityLightningBolt(this.world, arg82196n1.posX, arg82196n1.posY, arg82196n1.posZ, false));
                this.world.addWeatherEffect(new EntityLightningBolt(this.world, arg82196n1.posX, arg82196n1.posY, arg82196n1.posZ, false));
        }
    }

    public class EntityAIFindEntityNearestInjuredAlly extends EntityAIBase {
        private EntityEndermanMinion servantThe;
        private EntityLivingBase allyVictim;
        public EntityAIFindEntityNearestInjuredAlly(EntityEndermanMinion entityCaveSpiderPriest) {
            this.servantThe = entityCaveSpiderPriest;
        }

        public boolean shouldExecute() {
            if (!servantThe.isEntityAlive()) {
                return false;
            }

            if (servantThe.fetchKindMinion() != EnumMinionType.PRIEST) {
                return false;
            }

            if (this.allyVictim != null) {
                return false;
            }

            double d0 = getFollowRange();
            List<?> list = this.servantThe.world.getEntitiesWithinAABB(EntityEndermanMinion.class, this.servantThe.getEntityBoundingBox().expand(d0, d0, d0));
            if (list.isEmpty()) {
                return false;
            }

            for (int i = 0; i < list.size(); i++) {
                EntityEndermanMinion entity = (EntityEndermanMinion)list.get(i);
                if (entity.getHealth() < entity.getMaxHealth() && entity.isEntityAlive())
                    this.allyVictim = entity;
            }

            return true;
        }

        public boolean shouldContinueExecuting() {
            EntityLivingBase entitylivingbase = this.servantThe.toHealMob;
            if (entitylivingbase == null) {
                return false;
            }

            if (!entitylivingbase.isEntityAlive()) {
                this.allyVictim = null;
                return false;
            }

            if (entitylivingbase.getHealth() >= entitylivingbase.getMaxHealth()) {
                this.allyVictim = null;
                return false;
            }

            double d0 = getFollowRange();
            return this.servantThe.getDistanceSq(entitylivingbase) <= d0 * d0;
        }

        public void startExecuting() {
            this.servantThe.toHealMob = ((EntityLiving)this.allyVictim);
            super.startExecuting();
        }

        public void resetTask() {
            this.servantThe.toHealMob = null;
            this.allyVictim = null;
            super.resetTask();
        }

        public void updateTask() {
            if (this.servantThe.toHealMob != null && this.servantThe.getDistance(this.servantThe.toHealMob) > 16D) {
                this.servantThe.getNavigator().tryMoveToEntityLiving(this.servantThe.toHealMob, 1D);
                this.servantThe.getLookHelper().setLookPositionWithEntity(this.servantThe.toHealMob, 10F, this.servantThe.getVerticalFaceSpeed());
            }
        }

        protected double getFollowRange() {
            return 48.0D;
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
    public String fetchAudioSummon() {
        return "mob.endermen.portal";
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
    public boolean beginGrounded() {
        return false;
    }

    @Override
    public boolean friendlyIs(Entity entity) {
        if (entity == null)
            return false;
        return entity instanceof EntityEndermanMinion || entity instanceof EntityEnderColossus || entity instanceof EntityDragon;
    }
}
