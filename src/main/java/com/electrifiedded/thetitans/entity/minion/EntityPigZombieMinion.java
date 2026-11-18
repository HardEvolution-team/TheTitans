package com.electrifiedded.thetitans.entity.minion;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.titan.EntityPigZombieTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitanSpirit;
import com.electrifiedded.thetitans.entity.titan.EntityXPBomb;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;
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
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Calendar;
import java.util.List;

public class EntityPigZombieMinion extends EntityPigZombie implements IRangedAttackMob, ITemplar {
    private static final DataParameter<Integer> KIND_SERVANT = EntityDataManager.createKey(EntityPigZombieMinion.class, DataSerializers.VARINT);
    public EntityLiving master;
    public int angerLevel;
    public int randomSoundDelay;
    public EntityLiving toHealMob;
    private int patternStrike;
    private EntityAIAttackRanged aiArrowAttack = new EntityAIAttackRanged(this, 1.0D, 10, 64.0F);
    private float heightOffset = 0.5F;
    private int heightOffsetUpdateTime;

    public EntityPigZombieMinion(World worldIn) {
        super(worldIn);
        for (int i = 0; i < this.inventoryHandsDropChances.length; ++i) {
            this.inventoryHandsDropChances[i] = 0.2F;
        }
        for (int i = 0; i < this.inventoryArmorDropChances.length; ++i) {
            this.inventoryArmorDropChances[i] = 0.2F;
        }

        setSize(0.5F, 1.85F);
        ((PathNavigateGround)getNavigator()).setBreakDoors(true);
        ((PathNavigateGround)getNavigator()).setEnterDoors(true);
        this.tasks.addTask(0, new EntityAIAvoidEntity(this, EntityWitherSkull.class, 2.0F, 1.2D, 1.75D));
        this.tasks.addTask(0, new EntityAIAvoidEntity(this, EntityTitanSpirit.class, 48.0F, 1.5D, 1.5D));
        this.tasks.addTask(0, new EntityAIBreakDoorMinion(this));
        this.tasks.addTask(1, new EntityAIMoveTowardsRestriction(this, 1.2D));
        this.tasks.addTask(0, new EntityAIFindEntityNearestInjuredAlly(this));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
        this.tasks.addTask(6, new EntityAIWander(this, 0.8D));
        this.targetTasks.addTask(0, new EntityAIHurtByTargetTitan(this, true, new Class[] {
            EntityPigZombieMinion.class, EntityPigZombieTitan.class, EntityGhastGuard.class
        }
        ));
        if (TheTitans.ffaModeTitans)
            this.targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.zombieGiantSorterPig));
        else
            this.targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.forAThingToKillSearch));
    }

    public void setDead() {
        super.setDead();
        if ((this.master != null) && ((this.master instanceof EntityTitan))) {
            ((EntityTitan)this.master).retractNumFromKindMinion(fetchKindMinion());
        }
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        playSound(SoundEvents.ENTITY_PIG_STEP, 0.15F, 1.0F);
        playSound(SoundEvents.ENTITY_ZOMBIE_STEP, 0.15F, 1.0F);
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
        return TheTitans.isHeraldLevel(fetchKindMinion()) ? com.electrifiedded.thetitans.registry.TitanAudio.get("thetitans:titanPigZombieLiving") : super.getAmbientSound();
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return TheTitans.isHeraldLevel(fetchKindMinion()) ? com.electrifiedded.thetitans.registry.TitanAudio.get("thetitans:titanPigZombieGrunt") : super.getHurtSound(source);
    }

    protected SoundEvent getDeathSound() {
        return TheTitans.isHeraldLevel(fetchKindMinion()) ? com.electrifiedded.thetitans.registry.TitanAudio.get("thetitans:titanPigZombieDeath") : super.getDeathSound();
    }

    protected float getSoundPitch() {
        return this.fetchKindIntMinion() == 4 ? super.getSoundPitch() + 0.3F : super.getSoundPitch();
    }

    public int getTotalArmorValue() {
        int i = 2;
        for (ItemStack itemstack : this.getArmorInventoryList()) {
            if (!itemstack.isEmpty() && itemstack.getItem() instanceof ItemArmor) {
                int l = ((ItemArmor)itemstack.getItem()).damageReduceAmount;
                i += l;
            }
        }

        switch (this.fetchKindIntMinion()) {
            case 1:
                i += 2;
                break;
            case 2:
                i += 15;
                break;
            case 3:
                i += 18;
                break;
            case 4:
                i += 22;
        }

        if (i > 24) {
            i = 24;
        }

        return i;
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
                return I18n.translateToLocal("entity.PigZombiePriest.name");
            case 2:
                return I18n.translateToLocal("entity.PigZombieZealot.name");
            case 3:
                return I18n.translateToLocal("entity.PigZombieBishop.name");
            case 4:
                return I18n.translateToLocal("entity.PigZombieTemplar.name");
            default:
                return I18n.translateToLocal("entity.PigZombieLoyalist.name");
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
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(miniontype >= 3 ? 1.0D : 0.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(IMinion.fetchHpMultiplierMinion(20.0D, miniontype, 1));
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(IMinion.fetchStrikeMultiplierMinion(5.0D, miniontype, 1));
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(IMinion.fetchPaceMultiplierMinion(0.25D, miniontype, 1));
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D + (miniontype * 8));
        this.experienceValue = IMinion.fetchExpMultiplierMinion(12, miniontype);
        this.setHealth(this.getMaxHealth());
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        if (master != null)
            return;
        int j = this.rand.nextInt(3 + arg70628n2);
        for (int k = 0; k < j; k++) {
            dropItem(Items.ROTTEN_FLESH, 1);
        }

        j = this.rand.nextInt(3 + arg70628n2);
        for (int k = 0; k < j; k++) {
            dropItem(Items.GOLD_NUGGET, 1);
        }

        j = this.rand.nextInt(2 + arg70628n2);
        for (int k = 0; k < j; k++) {
            dropItem(Items.BONE, 1);
        }

        j = this.rand.nextInt(2 + arg70628n2);
        for (int k = 0; k < j; k++) {
            dropItem(Items.COOKED_PORKCHOP, 1);
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

                    entityDropItem(new ItemStack(Blocks.GLOWSTONE), 0.0F);
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

    public boolean isAngry() {
        return this.angerLevel > 0;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        return (arg70686n1 != EntityPigZombieMinion.class) && (arg70686n1 != EntityGhastGuard.class) && ((arg70686n1 != EntityPigZombieTitan.class) || (arg70686n1 == EntityZombieTitan.class));
    }

    public void setSwingingArms(boolean swingingArms) {
        // IRangedAttackMob stub
    }

    public void onLivingUpdate() {
        if (this.isEntityAlive() || this.fetchKindIntMinion() != 4)
            super.onLivingUpdate();
        if (this.fetchKindIntMinion() == 3) {
            if (this.rand.nextInt(120) == 0 && this.master == null && (getHealth() > 0.0F) && (!this.world.isRemote) && (this.world.getDifficulty() != EnumDifficulty.PEACEFUL)) {
                EntityPigZombieMinion entitychicken = new EntityPigZombieMinion(this.world);
                entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                entitychicken.setMinionType(0);
                this.world.spawnEntity(entitychicken);
            }

            if (this.rand.nextInt(240) == 0 && this.master == null && (getHealth() > 0.0F) && (!this.world.isRemote) && (this.world.getDifficulty() != EnumDifficulty.PEACEFUL)) {
                EntityPigZombieMinion entitychicken = new EntityPigZombieMinion(this.world);
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
                    EntityPigZombieMinion entitychicken = new EntityPigZombieMinion(this.world);
                    entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                    entitychicken.setMinionType(0);
                    this.world.spawnEntity(entitychicken);
                }

                if (this.rand.nextInt(120) == 0) {
                    EntityPigZombieMinion entitychicken = new EntityPigZombieMinion(this.world);
                    entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                    entitychicken.setMinionType(1);
                    this.world.spawnEntity(entitychicken);
                }

                if (this.rand.nextInt(240) == 0) {
                    EntityPigZombieMinion entitychicken = new EntityPigZombieMinion(this.world);
                    entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
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

                // wired custom sound thetitans:titanland (key registered)
                net.minecraft.util.SoundEvent land = com.electrifiedded.thetitans.registry.TitanAudio.get("thetitans:titanland");
                if (land != null)
                    playSound(land, 10000.0F, 1.0F);
                TransformEntity(this);
            }

            if (this.onGround) {
                this.isAirBorne = false;
            }

            List<?> found11 = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(8D, 8D, 8D));
            if (!this.world.isRemote && found11 != null && !found11.isEmpty() && this.ticksExisted % (this.getHealth() < this.getMaxHealth() / 2 ? 40 : 160) == 0) {
                this.world.createExplosion(this, this.posX, this.posY, this.posZ, 8F, false);
                for (int i1 = 0; i1 < found11.size(); i1++) {
                    Entity entity = (Entity)found11.get(i1);
                    if (entity != null && entity instanceof EntityLivingBase && this.allowStrikeGiant(entity.getClass().asSubclass(EntityLivingBase.class))) {
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
                    faceEntity(this.toHealMob, 180.0F, getVerticalFaceSpeed());
                    this.toHealMob.heal(4.0F);
                    playSound(SoundEvents.ENTITY_WITHER_SHOOT, 1.0F, 3.0F);
                }

                else {
                    this.toHealMob = null;
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
                    // wired custom sound thetitans:titanSwing (key registered)
                    net.minecraft.util.SoundEvent swing = com.electrifiedded.thetitans.registry.TitanAudio.get("thetitans:titanSwing");
                    if (swing != null)
                        playSound(swing, 1F, 2F);
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
                List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(32.0D, 32.0D, 32.0D));
                for (int i = 0; i < list.size(); i++) {
                    Entity mobRef1 = (Entity)list.get(i);
                    if ((mobRef1 instanceof EntityPigZombieMinion)) {
                        EntityPigZombieMinion entitypigzombie = (EntityPigZombieMinion)mobRef1;
                        entitypigzombie.setAttackTarget((EntityLivingBase)entity);
                        entitypigzombie.setRevengeTarget((EntityLivingBase)entity);
                        entitypigzombie.becomeAngryAt(entity);
                    }

                    setAttackTarget((EntityLivingBase)entity);
                    setRevengeTarget((EntityLivingBase)entity);
                    becomeAngryAt(entity);
                }
            }
        }

        return super.attackEntityFrom(source, amount);
    }

    protected void updateAITasks() {
        if (this.collidedHorizontally && this.master != null)
            this.motionY = 0.2D;
        if ((this.randomSoundDelay > 0) && (--this.randomSoundDelay == 0)) {
            playSound(SoundEvents.ENTITY_ZOMBIE_PIG_ANGRY, getSoundVolume() * 2.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 1.8F);
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
                becomeAngryAt(this.master.getAttackTarget());
            }
        }

        else {
            List<?> list = this.world.getEntitiesWithinAABB(EntityPigZombieTitan.class, this.getEntityBoundingBox().grow(100.0D, 100.0D, 100.0D));
            if ((list != null) && (!list.isEmpty())) {
                for (int i1 = 0; i1 < list.size(); i1++) {
                    Entity entity = (Entity)list.get(i1);
                    if ((entity != null) && ((entity instanceof EntityPigZombieTitan))) {
                        this.master = ((EntityPigZombieTitan)entity);
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
                    this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, d6, d7, d8, master.motionX, master.motionY + 0.2D, master.motionZ);
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

    public void TransformEntity(Entity entity) {
        entity.world.newExplosion(entity, entity.posX, entity.posY, entity.posZ, 18.0F, true, entity.world.getGameRules().getBoolean("mobGriefing"));
        EntityPigZombieTitan entitytitan = new EntityPigZombieTitan(entity.world);
        entitytitan.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, 0.0F);
        entity.setDead();
        entitytitan.funcM82206();
        entity.world.spawnEntity(entitytitan);
    }

    public void attackEntityWithRangedAttack(EntityLivingBase arg82196n1, float arg82196n2) {
        this.swingArm(EnumHand.MAIN_HAND);
        if (getDistanceSq(arg82196n1) < (arg82196n1.width * arg82196n1.width) + 36D)
            attackEntityAsMob(arg82196n1);
        else
            switch (this.rand.nextInt(4)) {
            case 0:
                EntityTippedArrow entityarrow = new EntityTippedArrow(this.world, this);
                double ax = arg82196n1.posX - this.posX;
                double ay = arg82196n1.getEntityBoundingBox().minY + arg82196n1.height / 3.0F - entityarrow.posY;
                double az = arg82196n1.posZ - this.posZ;
                double adist = (double)MathHelper.sqrt(ax * ax + az * az);
                entityarrow.shoot(ax, ay + adist * 0.2D, az, 1.6F, 1.0F);
                entityarrow.setIsCritical(true);
                entityarrow.setDamage(arg82196n2 * 2.0F + this.rand.nextGaussian() * 0.25D + this.world.getDifficulty().getId() * 0.11F);
                playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
                this.world.spawnEntity(entityarrow);
                break;
            case 1:
                // original: EntityPotion meta 32732 (splash harming), 32725 (splash healing) vs undead
                EntityPotion entitypotion = new EntityPotion(this.world, this, net.minecraft.potion.PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), arg82196n1.isEntityUndead() ? net.minecraft.init.PotionTypes.HEALING : net.minecraft.init.PotionTypes.HARMING));
                double d0 = arg82196n1.posY + 0.5D;
                entitypotion.rotationPitch -= -20.0F;
                double d1 = arg82196n1.posX + arg82196n1.motionX - this.posX;
                double d2 = d0 - this.posY;
                double d3 = arg82196n1.posZ + arg82196n1.motionZ - this.posZ;
                float f1 = MathHelper.sqrt(d1 * d1 + d3 * d3);
                entitypotion.shoot(d1, d2 + f1 * 0.2F, d3, 1.6F, f1 / 20.0F);
                this.world.spawnEntity(entitypotion);
                break;
            case 2:getDistanceSq(arg82196n1);
                double d11 = arg82196n1.posX - this.posX;
                double d21 = arg82196n1.getEntityBoundingBox().minY + arg82196n1.height / 2.0F - (this.posY + arg82196n1.height / 2.0F);
                double d31 = arg82196n1.posZ - this.posZ;
                this.world.playEvent((EntityPlayer)null, 1009, new BlockPos(this), 0);
                EntityLargeFireball entitylargefireball = new EntityLargeFireball(this.world, this, d11, d21, d31);
                entitylargefireball.posY = (this.posY + 1.6D);
                this.world.spawnEntity(entitylargefireball);
                break;
            case 3:playSound(SoundEvents.ENTITY_ZOMBIE_PIG_HURT, 1.0F, 0.5F);
                this.world.playSound((EntityPlayer)null, arg82196n1.posX, arg82196n1.posY, arg82196n1.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);
                for (int i = 0; i < 128; i++) {
                    EntityItem entityitem = arg82196n1.dropItem(Items.ROTTEN_FLESH, 1);
                    entityitem.setPickupDelay(6000);
                    entityitem.lifespan = (40 + this.rand.nextInt(20));
                    EntityItem entityitem1 = arg82196n1.dropItem(Items.GOLD_NUGGET, 1);
                    entityitem1.setPickupDelay(6000);
                    entityitem1.lifespan = (40 + this.rand.nextInt(20));
                }

                arg82196n1.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 100, 2));
                arg82196n1.attackEntityFrom(DamageSource.STARVE, 10F);
                arg82196n1.hurtResistantTime = 1;
        }
    }

    public void becomeAngryAt(Entity arg70835n1) {
        this.angerLevel = (400 + this.rand.nextInt(400));
        this.randomSoundDelay = this.rand.nextInt(40);
        this.playSound(SoundEvents.ENTITY_ZOMBIE_PIG_ANGRY, getSoundVolume() * 2.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 1.8F);
        if ((arg70835n1 instanceof EntityLivingBase)) {
            setRevengeTarget((EntityLivingBase)arg70835n1);
            setAttackTarget((EntityLivingBase)arg70835n1);
        }
    }

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData arg110161n1) {
        super.onInitialSpawn(difficulty, arg110161n1);
        float f = this.world.getDifficultyForLocation(new BlockPos(this)).getAdditionalDifficulty();
        setCanPickUpLoot(true);
        Calendar calendar = Calendar.getInstance();
        if (!this.isChild() && (calendar.get(2) + 1 == 10 && calendar.get(5) >= 1 && calendar.get(5) <= 31) && (this.rand.nextFloat() < 0.5F)) {
            setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
            this.inventoryArmorDropChances[3] = 0.0F;
        }

        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.1D, 0));
        double d0 = this.rand.nextDouble() * 1.5D * f;
        if (d0 > 1.0D) {
            getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
        }

        if (this.rand.nextFloat() < f * (this.fetchKindIntMinion() * 0.1F + 0.1F)) {
            getEntityAttribute(EntityZombie.SPAWN_REINFORCEMENTS_CHANCE).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 0.25D + 0.5D, 0));
            getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 3.0D + 4.0D, 2));
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

        return arg110161n1;
    }

    public class EntityAIFindEntityNearestInjuredAlly extends EntityAIBase {
        private EntityPigZombieMinion fld179434b;
        private EntityLivingBase fld179433e;
        public EntityAIFindEntityNearestInjuredAlly(EntityPigZombieMinion entityPigZombieMinion) {
            this.fld179434b = entityPigZombieMinion;
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
            List<?> list = this.fld179434b.world.getEntitiesWithinAABB(EntityPigZombieMinion.class, this.fld179434b.getEntityBoundingBox().grow(d0, d0, d0));
            if (list.isEmpty()) {
                return false;
            }

            for (int i = 0; i < list.size(); i++) {
                EntityPigZombieMinion entity = (EntityPigZombieMinion)list.get(i);
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
            return 32.0D;
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
        return entity instanceof EntityPigZombieMinion || entity instanceof EntityPigZombieTitan || entity instanceof EntityGhastGuard;
    }
}
