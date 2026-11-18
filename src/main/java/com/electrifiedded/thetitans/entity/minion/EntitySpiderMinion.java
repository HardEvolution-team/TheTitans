package com.electrifiedded.thetitans.entity.minion;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.titan.EntityCaveSpiderTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySpiderTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitanSpirit;
import com.electrifiedded.thetitans.entity.titan.EntityXPBomb;
import com.electrifiedded.thetitans.entity.titan.ITitan;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAIBreakDoorMinion;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;
import com.electrifiedded.thetitans.proxy.ClientProxy;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanAudio;
import com.electrifiedded.thetitans.util.Maths;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
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
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class EntitySpiderMinion extends EntitySpider implements IRangedAttackMob, ITemplar {
    private static final DataParameter<Integer> KIND_SERVANT =
            EntityDataManager.createKey(EntitySpiderMinion.class, DataSerializers.VARINT);
    public EntityLiving master;
    public boolean speedyIs;
    public boolean isStrong;
    public boolean predatorIs;
    public boolean wolverineIs;
    public boolean superSwimmerIs;
    public boolean lavaSwimmerIs;
    public boolean jumperIs;
    public boolean isHealthy;
    public boolean shieldedIs;
    public boolean toughIs;
    public int randomSoundDelay;
    public EntityLiving toHealMob;
    private int patternStrike;
    private EntityAIAttackRanged aiArrowAttack = new EntityAIAttackRanged(this, 1.0D, 10, 64F);
    private float heightOffset = 0.5F;
    private int heightOffsetUpdateTime;

    public EntitySpiderMinion(World worldIn) {
        super(worldIn);
        ((PathNavigateGround)getNavigator()).setBreakDoors(true);
        ((PathNavigateGround)getNavigator()).setEnterDoors(true);
        ((PathNavigateGround)getNavigator()).setCanSwim(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(0, new EntityAIAvoidEntity(this, EntityWitherSkull.class, 2.0F, 1.2D, 1.75D));
        this.tasks.addTask(0, new EntityAIAvoidEntity(this, EntityTitanSpirit.class, 48.0F, 1.5D, 1.5D));
        this.tasks.addTask(0, new EntityAIBreakDoorMinion(this));
        this.tasks.addTask(1, new EntityAIMoveTowardsRestriction(this, 1.2D));
        this.tasks.addTask(0, new EntityAIFindEntityNearestInjuredAlly(this));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.2D, true));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        if (TheTitans.ffaModeTitans)
            this.targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.giantSorterSpider));
        else
            this.targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.forAThingToKillSearch));
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(KIND_SERVANT, Integer.valueOf(0));
    }

    public void setDead() {
        super.setDead();
        if ((this.master != null) && ((this.master instanceof EntityTitan))) {
            ((EntityTitan)this.master).retractNumFromKindMinion(fetchKindMinion());
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

    protected float applyPotionDamageCalculations(DamageSource arg70672n1, float arg70672n2) {
        if (this.fetchKindIntMinion() >= 4) {
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

    public String getName() {
        switch (this.fetchKindIntMinion()) {
            case 1:
                return I18n.translateToLocal("entity.SpiderPriest.name");
            case 2:
                return I18n.translateToLocal("entity.SpiderZealot.name");
            case 3:
                return I18n.translateToLocal("entity.SpiderBishop.name");
            case 4:
                return I18n.translateToLocal("entity.SpiderTemplar.name");
            default:
                return I18n.translateToLocal("entity.SpiderLoyalist.name");
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
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(IMinion.fetchHpMultiplierMinion(16.0D, miniontype, 0));
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(IMinion.fetchStrikeMultiplierMinion(2.0D, miniontype, 0));
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(IMinion.fetchPaceMultiplierMinion(0.3D, miniontype, 0));
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24.0D + (miniontype * 8));
        this.experienceValue = IMinion.fetchExpMultiplierMinion(6, miniontype);
        this.setHealth(this.getMaxHealth());
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return this.fetchKindIntMinion() == 4 ? 15728880 : super.getBrightnessForRender();
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness() {
        return this.fetchKindIntMinion() == 4 ? 1.0F : super.getBrightness();
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.speedyIs = tagCompund.getBoolean("Speedy");
        this.isStrong = tagCompund.getBoolean("Strong");
        this.predatorIs = tagCompund.getBoolean("Predator");
        this.wolverineIs = tagCompund.getBoolean("Wolverine");
        this.superSwimmerIs = tagCompund.getBoolean("SuperSwimmer");
        this.lavaSwimmerIs = tagCompund.getBoolean("LavaSwimmer");
        this.jumperIs = tagCompund.getBoolean("Jumper");
        this.isHealthy = tagCompund.getBoolean("Healthy");
        this.shieldedIs = tagCompund.getBoolean("Shielded");
        this.toughIs = tagCompund.getBoolean("Tough");
        this.setMinionType(tagCompund.getInteger("MinionType"));
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("Speedy", this.speedyIs);
        tagCompound.setBoolean("Strong", this.isStrong);
        tagCompound.setBoolean("Predator", this.predatorIs);
        tagCompound.setBoolean("Wolverine", this.wolverineIs);
        tagCompound.setBoolean("SuperSwimmer", this.superSwimmerIs);
        tagCompound.setBoolean("LavaSwimmer", this.lavaSwimmerIs);
        tagCompound.setBoolean("Jumper", this.jumperIs);
        tagCompound.setBoolean("Healthy", this.isHealthy);
        tagCompound.setBoolean("Shielded", this.shieldedIs);
        tagCompound.setBoolean("Tough", this.toughIs);
        tagCompound.setInteger("MinionType", this.fetchKindIntMinion());
    }

    public void onLivingUpdate() {
        if (this.isEntityAlive() || this.fetchKindIntMinion() != 4)
            super.onLivingUpdate();
        if (this.fetchKindIntMinion() == 3) {
            if (this.rand.nextInt(120) == 0 && this.master == null && (getHealth() > 0.0F) && (!this.world.isRemote) && (this.world.getDifficulty() != EnumDifficulty.PEACEFUL)) {
                EntitySpiderMinion entitychicken = new EntitySpiderMinion(this.world);
                entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(this)), (IEntityLivingData)null);
                entitychicken.setMinionType(0);
                this.world.spawnEntity(entitychicken);
            }

            if (this.rand.nextInt(240) == 0 && this.master == null && (getHealth() > 0.0F) && (!this.world.isRemote) && (this.world.getDifficulty() != EnumDifficulty.PEACEFUL)) {
                EntitySpiderMinion entitychicken = new EntitySpiderMinion(this.world);
                entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(this)), (IEntityLivingData)null);
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
                    EntitySpiderMinion entitychicken = new EntitySpiderMinion(this.world);
                    entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(this)), (IEntityLivingData)null);
                    entitychicken.setMinionType(0);
                    this.world.spawnEntity(entitychicken);
                }

                if (this.rand.nextInt(120) == 0) {
                    EntitySpiderMinion entitychicken = new EntitySpiderMinion(this.world);
                    entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(this)), (IEntityLivingData)null);
                    entitychicken.setMinionType(1);
                    this.world.spawnEntity(entitychicken);
                }

                if (this.rand.nextInt(230) == 0) {
                    EntitySpiderMinion entitychicken = new EntitySpiderMinion(this.world);
                    entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(this)), (IEntityLivingData)null);
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
                    this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
                    this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.FLAME, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
                }

                // wired custom sound thetitans:titanland
                playSound(TitanAudio.get("thetitans:titanland"), 10000.0F, 1.0F);
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
                    if (entity != null && entity instanceof EntityLivingBase && this.allowStrikeGiant(entity.getClass().asSubclass(EntityLivingBase.class)) && isClient()) {
                        entity.motionY += rand.nextDouble();
                        if (!world.isRemote)
                            ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(ClientProxy.electricJudgment, 10, 1));
                        this.world.playEvent((EntityPlayer)null, 1017, new BlockPos(entity), 0);
                    }
                }
            }
        }

        if (this.fetchKindIntMinion() == 2) {
            if (getAttackTarget() != null) {
                double d0 = getDistanceSq(getAttackTarget());
                if (d0 < 0.8D) {
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
                    swingArm(EnumHand.MAIN_HAND);
                    this.getLookHelper().setLookPositionWithEntity(this.toHealMob, 180.0F, getVerticalFaceSpeed());
                    this.toHealMob.heal(4.0F);
                    playSound(SoundEvents.ENTITY_WITHER_SHOOT, 1.0F, 3.0F);
                }

                else {
                    this.toHealMob = null;
                }
            }

            if (this.toHealMob != null)
                this.getLookHelper().setLookPositionWithEntity(this.toHealMob, 180.0F, getVerticalFaceSpeed());
        }

        if ((getAttackTarget() != null) && (!getAttackTarget().isEntityAlive())) {
            setAttackTarget(null);
        }

        if (getAttackTarget() != null) {
            if (this.predatorIs && (getAttackTarget().getDistanceSq(this) < 25.0D) && (this.rand.nextInt(10) == 0)) {
                Vec3d vec3 = new Vec3d(this.posX - getAttackTarget().posX, this.getEntityBoundingBox().minY + this.height / 2.0F - getAttackTarget().posY + getAttackTarget().getEyeHeight(), this.posZ - getAttackTarget().posZ);
                vec3 = vec3.normalize();
                double d0 = 16.0D;
                double d1 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3.x * d0;
                double d3 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3.z * d0;
                double d2 = this.world.getTopSolidOrLiquidBlock(new BlockPos((int)d1, 0, (int)d3)).getY();
                EnderTeleportEvent event = new EnderTeleportEvent(this, d1, d2, d3, 0.0F);
                if (!MinecraftForge.EVENT_BUS.post(event)) {
                    this.attackEntityAsMob(getAttackTarget());
                    this.posX = event.getTargetX();
                    this.posY = event.getTargetY();
                    this.posZ = event.getTargetZ();
                    setPosition(this.posX, this.posY, this.posZ);
                }
            }

            if ((getAttackTarget().getDistanceSq(this) < 256.0D) && (getAttackTarget().getDistanceSq(this) > 9.0D) && (this.rand.nextInt(10) == 0)) {
                if (this.onGround) {
                    double d0 = getAttackTarget().posX - this.posX;
                    double d1 = getAttackTarget().posZ - this.posZ;
                    float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1);
                    if (this.speedyIs) {
                        this.motionX = (d0 / f2 * 2.0D);
                        this.motionZ = (d1 / f2 * 2.0D);
                    }

                    else {
                        this.motionX = (d0 / f2 * 0.75D);
                        this.motionZ = (d1 / f2 * 0.75D);
                    }

                    jump();
                    getLookHelper().setLookPositionWithEntity(getAttackTarget(), 180.0F, 30.0F);
                    getNavigator().tryMoveToEntityLiving(getAttackTarget(), 1F);
                }
            }

            if (!canEntityBeSeen(getAttackTarget())) {
                double d0 = getAttackTarget().posX - this.posX;
                double d1 = getAttackTarget().posZ - this.posZ;
                float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1);
                if (this.speedyIs) {
                    this.motionX = (d0 / f2 * 1.0D);
                    this.motionZ = (d1 / f2 * 1.0D);
                }

                else {
                    this.motionX = (d0 / f2 * 0.35D);
                    this.motionZ = (d1 / f2 * 0.35D);
                }
            }
        }

        if ((getAttackTarget() != null) && (getDistanceSq(getAttackTarget()) > 256.0D)) {
            getMoveHelper().setMoveTo(getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 1.0D);
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
        }

        else {
            List<?> list = this.world.getEntitiesWithinAABB(EntitySpiderTitan.class, this.getEntityBoundingBox().expand(100.0D, 100.0D, 100.0D));
            if ((list != null) && (!list.isEmpty())) {
                for (int i1 = 0; i1 < list.size(); i1++) {
                    Entity entity = (Entity)list.get(i1);
                    if ((entity != null) && ((entity instanceof EntitySpiderTitan)) && !(entity instanceof EntityCaveSpiderTitan)) {
                        this.master = ((EntitySpiderTitan)entity);
                    }
                }
            }
        }
    }

    public void fall(float arg70069n1, float damageMultiplier) {
        this.moveForward = 0F;
        this.moveStrafing = 0F;
        float[] ret = ForgeHooks.onLivingFall(this, arg70069n1, damageMultiplier);
        if (ret == null) return;
        arg70069n1 = ret[0];
        if (arg70069n1 <= 0.0F) return;
        PotionEffect potioneffect = getActivePotionEffect(MobEffects.JUMP_BOOST);
        float f1 = potioneffect != null ? potioneffect.getAmplifier() + 1 : 0.0F;
        int i = MathHelper.ceil(arg70069n1 - 3.0F - f1);
        if (i > 0) {
            playSound(this.getFallSound(i), 1.0F, 1.0F);
            int j = MathHelper.floor(this.posX);
            int k = MathHelper.floor(this.posY - 0.20000000298023224D);
            int l = MathHelper.floor(this.posZ);
            BlockPos pos = new BlockPos(j, k, l);
            IBlockState state = this.world.getBlockState(pos);
            Block block = state.getBlock();
            if (state.getMaterial() != Material.AIR) {
                SoundType soundtype = block.getSoundType(state, this.world, pos, this);
                playSound(soundtype.getStepSound(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
            }
        }
    }

    public int getTalkInterval() {
        return this.predatorIs ? 600 : 80;
    }

    public void setCombatTask() {
        this.tasks.removeTask(this.aiArrowAttack);
        if (this.patternStrike == 0 && this.fetchKindIntMinion() == 4) {
            this.tasks.addTask(0, this.aiArrowAttack);
        }
    }

    protected SoundEvent getAmbientSound() {
        if (TheTitans.isHeraldLevel(this.fetchKindMinion()))
            return TitanAudio.get("thetitans:titanSpiderLiving");
        return this.predatorIs ? TitanAudio.get("thetitans:predatorSpider") : SoundEvents.ENTITY_SPIDER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return TheTitans.isHeraldLevel(this.fetchKindMinion()) ? TitanAudio.get("thetitans:titanSpiderGrunt") : SoundEvents.ENTITY_SPIDER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return TheTitans.isHeraldLevel(this.fetchKindMinion()) ? TitanAudio.get("thetitans:titanSpiderDeath") : SoundEvents.ENTITY_SPIDER_DEATH;
    }

    protected float getSoundPitch() {
        return this.fetchKindIntMinion() == 4 ? super.getSoundPitch() + 0.3F : ((this.speedyIs) || (this.jumperIs) ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.5F : (this.toughIs) || (this.isStrong) ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.8F : super.getSoundPitch());
    }

    public void TransformEntity(Entity entity) {
        entity.world.newExplosion(entity, entity.posX, entity.posY, entity.posZ, 12.0F, true, entity.world.getGameRules().getBoolean("mobGriefing"));
        EntitySpiderTitan entitytitan = new EntitySpiderTitan(entity.world);
        entitytitan.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, 0.0F);
        entity.setDead();
        entitytitan.funcM82206();
        entity.world.spawnEntity(entitytitan);
        entitytitan.playSound(TitanAudio.get("thetitans:titanSpiderLiving"), 100.0F, 0.8F);
    }

    public void attackEntityWithRangedAttack(EntityLivingBase arg82196n1, float arg82196n2) {
        this.swingArm(EnumHand.MAIN_HAND);
        if (getDistanceSq(arg82196n1) < (arg82196n1.width * arg82196n1.width) + 36D)
            attackEntityAsMob(arg82196n1);
        else
            switch (this.rand.nextInt(5)) {
            case 0:EntityTippedArrow entityarrow = new EntityTippedArrow(this.world, this);
                double ax = arg82196n1.posX - this.posX;
                double ay = arg82196n1.getEntityBoundingBox().minY + arg82196n1.height / 3.0F - entityarrow.posY;
                double az = arg82196n1.posZ - this.posZ;
                double ah = MathHelper.sqrt(ax * ax + az * az);
                entityarrow.shoot(ax, ay + ah * 0.20000000298023224D, az, 1.6F, 1.0F);
                entityarrow.setIsCritical(true);
                entityarrow.setDamage(arg82196n2 * 2.0F + this.rand.nextGaussian() * 0.25D + this.world.getDifficulty().getId() * 0.11F);
                playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
                this.world.spawnEntity(entityarrow);
                break;
                // original: EntityPotion meta 32732 (splash harming), 32725 (splash healing) vs undead
            case 1:net.minecraft.entity.projectile.EntityPotion entitypotion = new net.minecraft.entity.projectile.EntityPotion(this.world, this,
                net.minecraft.potion.PotionUtils.addPotionToItemStack(new ItemStack(net.minecraft.init.Items.SPLASH_POTION), arg82196n1.isEntityUndead() ? net.minecraft.init.PotionTypes.HEALING : net.minecraft.init.PotionTypes.HARMING));
                double d0 = arg82196n1.posY + 0.5D;
                entitypotion.rotationPitch -= -20.0F;
                double d1 = arg82196n1.posX + arg82196n1.motionX - this.posX;
                double d2 = d0 - this.posY;
                double d3 = arg82196n1.posZ + arg82196n1.motionZ - this.posZ;
                float f1 = MathHelper.sqrt(d1 * d1 + d3 * d3);
                entitypotion.shoot(d1, d2 + f1 * 0.2F, d3, 1.0F, f1 / 20.0F);
                this.world.spawnEntity(entitypotion);
                break;
            case 2:double d011 = getDistanceSq(arg82196n1);
                double d111 = arg82196n1.posX - this.posX;
                double d211 = arg82196n1.getEntityBoundingBox().minY + arg82196n1.height / 2.0F - (this.posY + arg82196n1.height / 2.0F);
                double d311 = arg82196n1.posZ - this.posZ;
                float f = MathHelper.sqrt(MathHelper.sqrt(d011)) * 0.1F;
                this.world.playEvent((EntityPlayer)null, 1009, new BlockPos(this), 0);
                EntitySmallFireball entitysmallfireball = new EntitySmallFireball(this.world, this, d111 + getRNG().nextGaussian() * f, d211, d311 + getRNG().nextGaussian() * f);
                entitysmallfireball.posY = (this.posY + 1.6D);
                this.world.spawnEntity(entitysmallfireball);
                break;
            case 3:
                playSound(SoundEvents.ENTITY_WITHER_SHOOT, 1.0F, 3.0F);
                int i = MathHelper.floor(arg82196n1.posX + this.rand.nextDouble() * 2.0D);
                int j = MathHelper.floor(arg82196n1.posY + this.rand.nextDouble() * 2.0D);
                int k = MathHelper.floor(arg82196n1.posZ + this.rand.nextDouble() * 2.0D);
                BlockPos webPos = new BlockPos(i, j, k);
                if ((this.world.getBlockState(webPos).getMaterial() == Material.AIR) && (this.world.getGameRules().getBoolean("mobGriefing"))) {
                    this.world.setBlockState(webPos, Blocks.WEB.getDefaultState());
                }

                else {
                    arg82196n1.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 2));
                }

                break;
            case 4:
                this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.HOSTILE, 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
                this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 2.0F, 0.5F + this.rand.nextFloat() * 0.2F);
                this.world.playEvent((EntityPlayer)null, 1017, new BlockPos(this), 0);
                addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 200));
                List<?> found11 = this.world.getEntitiesWithinAABB(EntitySpiderMinion.class, this.getEntityBoundingBox().expand(64.0D, 64.0D, 64.0D), e -> e != this);
                if ((found11 != null) && (!found11.isEmpty())) {
                    for (int i1 = 0; i1 < found11.size(); i1++) {
                        Entity entity = (Entity)found11.get(i1);
                        if (((entity instanceof EntitySpiderMinion)) && (entity != null)) {
                            ((EntitySpiderMinion)entity).addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 200));
                            this.world.playEvent((EntityPlayer)null, 1017, new BlockPos(entity), 0);
                        }
                    }
                }

                break;
        }
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        return arg70686n1 != EntitySpiderMinion.class && (arg70686n1 == EntityCaveSpiderTitan.class || arg70686n1 != EntitySpiderTitan.class);
    }

    public void setSwingingArms(boolean swingingArms) {
        // IRangedAttackMob stub
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source) || friendlyIs(source.getTrueSource()) || (fetchKindIntMinion() >= 4 && source == DamageSourceExtra.radiation))
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

            Entity entity = source.getTrueSource();
            if ((entity instanceof EntityLivingBase)) {
                List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(32.0D, 32.0D, 32.0D));
                for (int i = 0; i < list.size(); i++) {
                    Entity mobRef1 = (Entity)list.get(i);
                    if ((mobRef1 instanceof EntitySpiderMinion)) {
                        EntitySpiderMinion entitypigzombie = (EntitySpiderMinion)mobRef1;
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

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        if (master != null)
            return;
        super.dropFewItems(arg70628n1, arg70628n2);
        int j = this.rand.nextInt(3 + arg70628n2);
        for (int k = 0; k < j; k++) {
            dropItem(Items.MELON_SEEDS, 1);
        }

        if ((this.rand.nextInt(60) == 0) || (this.rand.nextInt(1 + arg70628n2) > 0)) {
            entityDropItem(new ItemStack(Blocks.WEB), 0.0F);
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

    protected void jump() {
        this.motionY += 0.6D;
        if (isPotionActive(MobEffects.JUMP_BOOST)) {
            this.motionY += (getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
        }

        if (this.jumperIs) {
            this.motionY += 0.6D;
            float f = this.rotationYaw * 0.017453292F;
            this.motionX -= MathHelper.sin(f) * 0.2F;
            this.motionZ += MathHelper.cos(f) * 0.2F;
        }

        this.isAirBorne = true;
        ForgeHooks.onLivingJump(this);
    }

    protected void updateAITasks() {
        if ((this.randomSoundDelay > 0) && (--this.randomSoundDelay == 0)) {
            playSound(getHurtSound(DamageSource.GENERIC), getSoundVolume(), getSoundPitch() + 0.25F);
        }

        if (this.predatorIs) {
            setInvisible(true);
        }

        if ((isInWater()) && (this.superSwimmerIs)) {
            this.motionX *= 1.25D;
            this.motionY *= 1.25D;
            this.motionZ *= 1.25D;
            if (this.ticksExisted % 20 == 0) {
                heal(1.0F);
            }
        }

        if ((!isInLava()) && (this.lavaSwimmerIs)) {
            extinguish();
        }

        if ((isInLava()) && (this.lavaSwimmerIs)) {
            this.motionX *= 2.0D;
            this.motionY *= 2.0D;
            this.motionZ *= 2.0D;
            if (this.ticksExisted % 5 == 0) {
                heal(1.0F);
            }
        }

        if (this.isHealthy) {
            setSprinting(true);
        }

        if ((this.shieldedIs) && (getAbsorptionAmount() > 0.0F)) {
            this.lastDamage = 300.0F;
            this.maxHurtResistantTime = 30;
        }

        if ((this.wolverineIs)) {
            heal(1.0F);
        }

        if (this.toughIs) {
            getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        }

        this.stepHeight = 1.0F;
        setBesideClimbableBlock(this.collidedHorizontally);
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
                    this.world.playBroadcastSound(1018, new BlockPos(this), 0);
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

    public boolean attackEntityAsMob(Entity arg70652n1) {
        if (this.isStrong) {
            arg70652n1.motionY += 0.6D;
            playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
        }

        if (super.attackEntityAsMob(arg70652n1)) {
            if (arg70652n1 instanceof EntityLivingBase && this.fetchKindIntMinion() >= 3) {
                byte b0 = 10;
                if (this.world.getDifficulty() == EnumDifficulty.NORMAL) {
                    b0 = 20;
                }

                else if (this.world.getDifficulty() == EnumDifficulty.HARD) {
                    b0 = 30;
                }

                if (b0 > 0 && isClient()) {
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

    public IEntityLivingData onInitialSpawn(net.minecraft.world.DifficultyInstance difficulty, IEntityLivingData arg110161n1) {
        if (this.world.rand.nextInt(100) == 0) {
            EntitySkeletonMinion entityskeleton = new EntitySkeletonMinion(this.world);
            entityskeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
            entityskeleton.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(this)), (IEntityLivingData)null);
            this.world.spawnEntity(entityskeleton);
            entityskeleton.startRiding(this);
        }

        if (this.rand.nextInt(this.world.getDifficulty() == EnumDifficulty.NORMAL ? 25 : this.world.getDifficulty() == EnumDifficulty.HARD ? 2 : 500) == 0) {
            switch (this.rand.nextInt(10)) {
                case 0:addPotionEffect(new PotionEffect(MobEffects.SPEED, Integer.MAX_VALUE, 1));
                    this.speedyIs = true;
                    break;
                case 1:addPotionEffect(new PotionEffect(MobEffects.STRENGTH, Integer.MAX_VALUE, 4));
                    this.isStrong = true;
                    break;
                case 2:addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, Integer.MAX_VALUE, 0, true, true));
                    this.predatorIs = true;
                    break;
                case 3:addPotionEffect(new PotionEffect(MobEffects.REGENERATION, Integer.MAX_VALUE, 9));
                    this.wolverineIs = true;
                    break;
                case 4:addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, Integer.MAX_VALUE, 0));
                    this.superSwimmerIs = true;
                    break;
                case 5:addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
                    this.lavaSwimmerIs = true;
                    break;
                case 6:addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, Integer.MAX_VALUE, 2));
                    this.jumperIs = true;
                    break;
                case 7:addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 9));
                    addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, Integer.MAX_VALUE, 9));
                    this.isHealthy = true;
                    break;
                case 8:addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, Integer.MAX_VALUE, 9));
                    this.shieldedIs = true;
                    break;
                case 9:addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, Integer.MAX_VALUE, 2));
                    this.toughIs = true;
            }
        }

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

        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, 1));
        return arg110161n1;
    }

    public static class GroupData implements IEntityLivingData {
        public int fld111105a;
        public void funcA111104(Random arg111104n1) {
        }
    }

    public class EntityAIFindEntityNearestInjuredAlly extends EntityAIBase {
        private EntitySpiderMinion mobThe;
        private EntityLivingBase allyInjured;
        public EntityAIFindEntityNearestInjuredAlly(EntitySpiderMinion entityCaveSpiderPriest) {
            this.mobThe = entityCaveSpiderPriest;
        }

        public boolean shouldExecute() {
            if (!mobThe.isEntityAlive()) {
                return false;
            }

            if (mobThe.fetchKindMinion() != EnumMinionType.PRIEST) {
                return false;
            }

            if (this.allyInjured != null) {
                return false;
            }

            double d0 = getFollowRange();
            List<?> list = this.mobThe.world.getEntitiesWithinAABB(EntitySpiderMinion.class, this.mobThe.getEntityBoundingBox().expand(d0, d0, d0));
            if (list.isEmpty()) {
                return false;
            }

            for (int i = 0; i < list.size(); i++) {
                EntitySpiderMinion entity = (EntitySpiderMinion)list.get(i);
                if (entity.getHealth() < entity.getMaxHealth() && entity.isEntityAlive())
                    this.allyInjured = entity;
            }

            return true;
        }

        public boolean shouldContinueExecuting() {
            EntityLivingBase entitylivingbase = this.mobThe.toHealMob;
            if (entitylivingbase == null) {
                return false;
            }

            if (!entitylivingbase.isEntityAlive()) {
                return false;
            }

            if (entitylivingbase.getHealth() >= entitylivingbase.getMaxHealth()) {
                return false;
            }

            double d0 = getFollowRange();
            return this.mobThe.getDistanceSq(entitylivingbase) <= d0 * d0;
        }

        public void startExecuting() {
            this.mobThe.toHealMob = ((EntityLiving)this.allyInjured);
            super.startExecuting();
        }

        public void resetTask() {
            this.mobThe.toHealMob = null;
            this.allyInjured = null;
            super.resetTask();
        }

        public void updateTask() {
            if (this.mobThe.toHealMob != null && this.mobThe.getDistance(this.mobThe.toHealMob) > 16D) {
                this.mobThe.getNavigator().tryMoveToEntityLiving(this.mobThe.toHealMob, 1D);
                this.mobThe.getLookHelper().setLookPositionWithEntity(this.mobThe.toHealMob, 10F, this.mobThe.getVerticalFaceSpeed());
            }
        }

        protected double getFollowRange() {
            return 32.0D;
        }
    }

    public boolean isClient() {
        return world.isRemote;
    }

    public boolean isServer() {
        return !world.isRemote;
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
        return (entity instanceof EntitySpiderMinion || entity instanceof EntitySpiderTitan) && !(entity instanceof EntityCaveSpiderTitan || entity instanceof EntityCaveSpiderMinion);
    }
}
