package com.electrifiedded.thetitans.entity.minion;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitanSpirit;
import com.electrifiedded.thetitans.entity.titan.EntityXPBomb;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;
import com.electrifiedded.thetitans.entity.titan.ITitan;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAIBreakDoorMinion;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;
import com.electrifiedded.thetitans.proxy.ClientProxy;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanAudio;
import com.electrifiedded.thetitans.util.Maths;

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
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
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
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Calendar;
import java.util.List;

public class EntityZombieMinion extends EntityZombie implements IRangedAttackMob, ITemplar {
    private static final DataParameter<Integer> KIND_SERVANT = EntityDataManager.createKey(EntityZombieMinion.class, DataSerializers.VARINT);
    public EntityLiving master;
    public int randomSoundDelay;
    public EntityLiving toHealMob;
    private int patternStrike;
    private EntityAIAttackRanged aiArrowAttack = new EntityAIAttackRanged(this, 1.0D, 10, 64.0F);
    private float heightOffset = 0.5F;
    private int heightOffsetUpdateTime;

    public EntityZombieMinion(World worldIn) {
        super(worldIn);
        for (int i = 0; i < inventoryArmorDropChances.length; ++i) {
            inventoryArmorDropChances[i] = 0.2F;
        }
        for (int i = 0; i < inventoryHandsDropChances.length; ++i) {
            inventoryHandsDropChances[i] = 0.2F;
        }

        setSize(0.5F, 1.9F);
        ((PathNavigateGround)getNavigator()).setBreakDoors(true);
        ((PathNavigateGround)getNavigator()).setEnterDoors(true);
        tasks.addTask(0, new EntityAIAvoidEntity<EntityWitherSkull>(this, EntityWitherSkull.class, 2.0F, 1.2D, 1.75D));
        tasks.addTask(0, new EntityAIAvoidEntity<EntityTitanSpirit>(this, EntityTitanSpirit.class, 48.0F, 1.5D, 1.5D));
        tasks.addTask(0, new EntityAIBreakDoorMinion(this));
        tasks.addTask(1, new EntityAIMoveTowardsRestriction(this, 1.2D));
        tasks.addTask(1, new EntityAIRestrictSun(this));
        tasks.addTask(1, new EntityAIFleeSun(this, 1.2D));
        tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, true) {
            @Override
            public void updateTask() {
                if (attacker.getAttackTarget() != null)
                    super.updateTask();
            }
        });
        tasks.addTask(0, new EntityAIFindEntityNearestInjuredAlly(this));
        if (TheTitans.ffaModeTitans)
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.giantSorterZombie));
        else
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.forAThingToKillSearch));
    }

    @Override
    protected void applyEntityAI() {
        super.applyEntityAI();
        // vanilla EntityAIZombieAttack.updateTask has no null check; the minion clears its target mid-tick
        tasks.taskEntries.removeIf(e -> e.action instanceof net.minecraft.entity.ai.EntityAIZombieAttack);
        tasks.addTask(2, new net.minecraft.entity.ai.EntityAIZombieAttack(this, 1.0D, false) {
            @Override
            public void updateTask() {
                if (attacker.getAttackTarget() != null)
                    super.updateTask();
            }
        });
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24.0D);
    }

    // TODO 1.12 zombie villager type API — EntityZombie.assignVillager/villagerIs
    // were replaced by ZombieType/EntityZombieVillager. No-op stubs keep behavior compiling
    // without inventing the new type conversion.
    public void assignVillager(boolean villager) {
    }

    public boolean villagerIs() {
        return false;
    }

    public void setDead() {
        if (fetchKindMinion() == EnumMinionType.LORD) {
            if (world.isRemote && !world.isBlockLoaded(new BlockPos(this))) {
                super.setDead();
            }

            if (deathTicks > 0 || getHealth() <= 0.0F) {
                super.setDead();
                if ((master != null) && ((master instanceof EntityTitan))) {
                    ((EntityTitan)master).retractNumFromKindMinion(fetchKindMinion());
                }
            }
        }

        else {
            super.setDead();
            if ((master != null) && ((master instanceof EntityTitan))) {
                ((EntityTitan)master).retractNumFromKindMinion(fetchKindMinion());
            }
        }
    }

    public void onUpdate() {
        if (isDead && fetchKindMinion() == EnumMinionType.LORD && getHealth() > 0.0F && !world.isRemote) {
            isDead = false;
        }

        super.onUpdate();
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(KIND_SERVANT, Integer.valueOf(0));
    }

    public void setCombatTask() {
        tasks.removeTask(aiArrowAttack);
        if (patternStrike == 0 && fetchKindIntMinion() == 4) {
            tasks.addTask(0, aiArrowAttack);
        }
    }

    protected SoundEvent getAmbientSound() {
        if (TheTitans.isHeraldLevel(fetchKindMinion()))
            playSound(SoundEvents.ENTITY_ZOMBIE_AMBIENT, getSoundVolume(), getSoundPitch() - 0.5F);
        return TheTitans.isHeraldLevel(fetchKindMinion()) ? TitanAudio.get("thetitans:titanZombieLiving") : SoundEvents.ENTITY_ZOMBIE_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        if (TheTitans.isHeraldLevel(fetchKindMinion()))
            playSound(SoundEvents.ENTITY_ZOMBIE_HURT, getSoundVolume(), getSoundPitch() - 0.5F);
        return TheTitans.isHeraldLevel(fetchKindMinion()) ? TitanAudio.get("thetitans:titanZombieGrunt") : SoundEvents.ENTITY_ZOMBIE_HURT;
    }

    protected SoundEvent getDeathSound() {
        if (TheTitans.isHeraldLevel(fetchKindMinion()))
            playSound(SoundEvents.ENTITY_ZOMBIE_DEATH, getSoundVolume(), getSoundPitch() - 0.5F);
        return TheTitans.isHeraldLevel(fetchKindMinion()) ? TitanAudio.get("thetitans:titanZombieDeath") : SoundEvents.ENTITY_ZOMBIE_DEATH;
    }

    protected float getSoundPitch() {
        return fetchKindIntMinion() >= 4 ? super.getSoundPitch() + (fetchKindIntMinion() == 6 ? 0F : 0.2F) : super.getSoundPitch();
    }

    public int getTotalArmorValue() {
        int i = 2;
        for (ItemStack itemstack : getArmorInventoryList()) {
            if (!itemstack.isEmpty() && itemstack.getItem() instanceof ItemArmor) {
                int l = ((ItemArmor)itemstack.getItem()).damageReduceAmount;
                i += l;
            }
        }

        switch (fetchKindIntMinion()) {
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
        if (fetchKindIntMinion() == 4) {
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
        switch (fetchKindIntMinion()) {
            case 1:
                return I18n.translateToLocal("entity.ZombiePriest.name");
            case 2:
                return I18n.translateToLocal("entity.ZombieZealot.name");
            case 3:
                return I18n.translateToLocal("entity.ZombieBishop.name");
            case 4:
                return I18n.translateToLocal("entity.ZombieTemplar.name");
            default:
                return I18n.translateToLocal("entity.ZombieLoyalist.name");
        }
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("MinionType", fetchKindIntMinion());
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        setMinionType(tagCompund.getInteger("MinionType"));
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
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(miniontype >= 3 ? 1.0D : 0.0D);
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(IMinion.fetchHpMultiplierMinion(20.0D, miniontype, 1));
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(IMinion.fetchStrikeMultiplierMinion(3.0D, miniontype, 1));
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(IMinion.fetchPaceMultiplierMinion(0.23D, miniontype, 1));
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24.0D + (miniontype * 8));
        experienceValue = IMinion.fetchExpMultiplierMinion(6, miniontype);
        setHealth(getMaxHealth());
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return fetchKindIntMinion() == 4 ? 15728880 : super.getBrightnessForRender();
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness() {
        return fetchKindIntMinion() == 4 ? 1.0F : super.getBrightness();
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        if (master != null)
            return;
        super.dropFewItems(arg70628n1, arg70628n2);
        int j = rand.nextInt(3);
        if (arg70628n2 > 0) {
            j += rand.nextInt(arg70628n2 + 1);
        }

        for (int k = 0; k < j; k++) {
            dropItem(Items.FEATHER, 1);
        }

        j = rand.nextInt(2);
        for (int k = 0; k < j; k++) {
            dropItem(Items.BONE, 1);
        }

        if (fetchKindIntMinion() >= 1) {
            j = rand.nextInt(2);
            if (arg70628n2 > 0) {
                j += rand.nextInt(arg70628n2 + 1);
            }

            for (int k = 0; k < j; k++) {
                dropItem(Items.EXPERIENCE_BOTTLE, 1);
            }

            if (fetchKindIntMinion() >= 2) {
                j = rand.nextInt(2);
                if (arg70628n2 > 0) {
                    j += rand.nextInt(arg70628n2 + 1);
                }

                for (int k = 0; k < j; k++) {
                    dropItem(Items.GOLDEN_APPLE, 1);
                }

                if (fetchKindIntMinion() >= 3) {
                    j = rand.nextInt(2);
                    if (arg70628n2 > 0) {
                        j += rand.nextInt(arg70628n2 + 1);
                    }

                    for (int k = 0; k < j; k++) {
                        switch (rand.nextInt(5)) {
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

                    if (fetchKindIntMinion() >= 4) {
                        if (rand.nextInt(5) == 0) {
                            entityDropItem(new ItemStack(TitanGear.bladeSeedPleasant), 0.0F);
                        }

                        if (rand.nextInt(100) == 0) {
                            entityDropItem(new ItemStack(TitanGear.seedsMalgrum), 0.0F);
                        }

                        j = 2 + rand.nextInt(5);
                        if (arg70628n2 > 0) {
                            j += rand.nextInt(arg70628n2 + 1);
                        }

                        for (int k = 0; k < j; k++) {
                            switch (rand.nextInt(3)) {
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

    public boolean attackEntityAsMob(Entity arg70652n1) {
        if (super.attackEntityAsMob(arg70652n1)) {
            if (arg70652n1 instanceof EntityLivingBase && fetchKindIntMinion() >= 3) {
                byte b0 = 10;
                if (world.getDifficulty() == EnumDifficulty.NORMAL) {
                    b0 = 20;
                }

                else if (world.getDifficulty() == EnumDifficulty.HARD) {
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

    protected void shedDropRare(int arg70600n1) {
        entityDropItem(new ItemStack(Items.SKULL, 1, 2), 0.0F);
    }

    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        return (arg70686n1 != EntityZombieMinion.class) && (arg70686n1 != EntityGiantZombieBetter.class) && (arg70686n1 != EntityZombieTitan.class);
    }

    public void setSwingingArms(boolean swingingArms) {
        // IRangedAttackMob stub; zombie minions have no arm-swing model state
    }

    public void fall(float arg70069n1, float damageMultiplier) {
        if (fetchKindIntMinion() != 4)
            super.fall(arg70069n1, damageMultiplier);
        moveForward = 0F;
        moveStrafing = 0F;
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
                List<?> list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().expand(32.0D, 32.0D, 32.0D));
                for (int i = 0; i < list.size(); i++) {
                    Entity mobRef1 = (Entity)list.get(i);
                    if (mobRef1 instanceof EntityZombieMinion) {
                        EntityZombieMinion entitypigzombie = (EntityZombieMinion)mobRef1;
                        if (entitypigzombie.getAttackTarget() == null)
                            entitypigzombie.setAttackTarget((EntityLivingBase)entity);
                        entitypigzombie.setRevengeTarget((EntityLivingBase)entity);
                        entitypigzombie.randomSoundDelay = rand.nextInt(40);
                    }

                    setAttackTarget((EntityLivingBase)entity);
                    setRevengeTarget((EntityLivingBase)entity);
                    randomSoundDelay = rand.nextInt(40);
                }
            }
        }

        return super.attackEntityFrom(source, amount);
    }

    protected void appendArmourRandom() {
        // TODO 1.12 difficulty-equipment API (appendArmourRandom/func_130225_q/setCurrentItemOrArmor
        // replaced by setEquipmentBasedOnDifficulty(DifficultyInstance)); left as no-op to avoid inventing broken code.
    }

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData arg110161n1) {
        Object p_110161_1_1 = arg110161n1;
        float f = world.getDifficultyForLocation(new BlockPos(this)).getAdditionalDifficulty();
        setCanPickUpLoot(true);
        if (p_110161_1_1 == null) {
            p_110161_1_1 = new GroupData(world.rand.nextFloat() < (world.getDifficulty() == EnumDifficulty.NORMAL ? 0.05F : world.getDifficulty() == EnumDifficulty.HARD ? 0.25F : 0.005F), world.rand.nextFloat() < 0.1F, null);
        }

        if ((p_110161_1_1 instanceof GroupData)) {
            GroupData groupdata = (GroupData)p_110161_1_1;
            if (groupdata.fld142046b) {
                assignVillager(true);
            }

            if (groupdata.fld142048a) {
                setChild(true);
            }
        }

        appendArmourRandom();
        Calendar calendar = Calendar.getInstance();
        if (!isChild() && !villagerIs() && (calendar.get(2) + 1 == 10 && calendar.get(5) >= 1 && calendar.get(5) <= 31) && (rand.nextFloat() < 0.5F)) {
            setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(rand.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
            inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0F;
        }

        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(new AttributeModifier("Random spawn bonus", rand.nextDouble() * 0.1D, 0));
        double d0 = rand.nextDouble() * 1.5D * f;
        if (d0 > 1.0D) {
            getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
        }

        if (rand.nextFloat() < f * (fetchKindIntMinion() * 0.1F + 0.1F) || fetchKindMinion() == EnumMinionType.TEMPLAR) {
            getEntityAttribute(EntityZombie.SPAWN_REINFORCEMENTS_CHANCE).applyModifier(new AttributeModifier("Leader zombie bonus", rand.nextDouble() * 0.25D + 0.5D, 0));
            getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Leader zombie bonus", rand.nextDouble() * 3.0D + 4.0D, 2));
        }

        if (!world.isRemote) {
            setMinionType(0);
            EntityPlayer player = world.getClosestPlayerToEntity(this, -1D);
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

        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random spawn bonus", rand.nextGaussian(), 1));
        return arg110161n1;
    }

    public void onLivingUpdate() {
        if (isEntityAlive() || fetchKindIntMinion() != 4)
            super.onLivingUpdate();
        if (fetchKindIntMinion() == 3) {
            if (rand.nextInt(120) == 0 && master == null && (getHealth() > 0.0F) && (!world.isRemote) && (world.getDifficulty() != EnumDifficulty.PEACEFUL)) {
                EntityZombieMinion entitychicken = new EntityZombieMinion(world);
                entitychicken.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                entitychicken.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                entitychicken.setMinionType(0);
                entitychicken.setChild(isChild());
                entitychicken.assignVillager(villagerIs());
                world.spawnEntity(entitychicken);
            }

            if (rand.nextInt(240) == 0 && master == null && (getHealth() > 0.0F) && (!world.isRemote) && (world.getDifficulty() != EnumDifficulty.PEACEFUL)) {
                EntityZombieMinion entitychicken = new EntityZombieMinion(world);
                entitychicken.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                entitychicken.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                entitychicken.setMinionType(1);
                entitychicken.setChild(isChild());
                entitychicken.assignVillager(villagerIs());
                world.spawnEntity(entitychicken);
            }
        }

        if (fetchKindIntMinion() == 4) {
            if (ticksExisted % 40 == 0)
                heal(1F);
            if (world.rand.nextInt(150) == 1)
                heal(2.0F);
            if (world.rand.nextInt(100) == 1 && getHealth() < getMaxHealth() * 0.75)
                heal(2.0F);
            if (world.rand.nextInt(35) == 1 && getHealth() < getMaxHealth() * 0.5)
                heal(5.0F);
            if (world.rand.nextInt(30) == 1 && getHealth() < getMaxHealth() * 0.25)
                heal(5.0F);
            if (world.rand.nextInt(30) == 1 && getHealth() < getMaxHealth() * 0.05)
                heal(200.0F);
            if ((!onGround) && (motionY < 0.0D)) {
                motionY *= 0.6D;
            }

            if (master == null && (getHealth() > 0.0F) && (!world.isRemote) && (world.getDifficulty() != EnumDifficulty.PEACEFUL)) {
                if (rand.nextInt(60) == 0) {
                    EntityZombieMinion entitychicken = new EntityZombieMinion(world);
                    entitychicken.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                    entitychicken.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                    entitychicken.setMinionType(0);
                    entitychicken.setChild(isChild());
                    entitychicken.assignVillager(villagerIs());
                    world.spawnEntity(entitychicken);
                }

                if (rand.nextInt(120) == 0) {
                    EntityZombieMinion entitychicken = new EntityZombieMinion(world);
                    entitychicken.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                    entitychicken.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                    entitychicken.setMinionType(1);
                    entitychicken.setChild(isChild());
                    entitychicken.assignVillager(villagerIs());
                    world.spawnEntity(entitychicken);
                }

                if (rand.nextInt(240) == 0) {
                    EntityZombieMinion entitychicken = new EntityZombieMinion(world);
                    entitychicken.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                    entitychicken.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                    entitychicken.setMinionType(2);
                    entitychicken.setChild(isChild());
                    entitychicken.assignVillager(villagerIs());
                    world.spawnEntity(entitychicken);
                }
            }

            if ((world.isRemote) && (!onGround)) {
                for (int i = 0; i < 3; i++) {
                    world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX + (rand.nextDouble() - 0.5D) * width, posY, posZ + (rand.nextDouble() - 0.5D) * width, 0.0D, 0.0D, 0.0D);
                }
            }

            if ((rand.nextInt(60) == 0) && (getAttackTarget() != null)) {
                setCombatTask();
                if (!onGround) {
                    patternStrike = 0;
                }

                else {
                    patternStrike = 1;
                }
            }

            --heightOffsetUpdateTime;
            if (heightOffsetUpdateTime <= 0) {
                jump();
                heightOffsetUpdateTime = 100;
                heightOffset = 0.5F + (float)rand.nextGaussian() * 3.0F;
                patternStrike = 0;
            }

            EntityLivingBase entitylivingbase = getAttackTarget();
            if ((patternStrike == 0) && (entitylivingbase != null) && !world.isRemote) {
                if ((entitylivingbase.posY + entitylivingbase.getEyeHeight() > posY + getEyeHeight() + heightOffset)) {
                    motionY += (0.4D - motionY);
                    isAirBorne = true;
                }

                getLookHelper().setLookPositionWithEntity(entitylivingbase, 180F, 40F);
                double d0 = entitylivingbase.posX - posX;
                double d1 = entitylivingbase.posZ - posZ;
                double d3 = d0 * d0 + d1 * d1;
                if (d3 > (entitylivingbase.width * entitylivingbase.width) + (width * width) + 16D) {
                    double d5 = MathHelper.sqrt(d3);
                    motionX += (d0 / d5 * 0.6D - motionX);
                    motionZ += (d1 / d5 * 0.6D - motionZ);
                }
            }

            if (isEntityAlive() && (!world.isRemote) && (rand.nextInt(1000) == 0) && (getAttackTarget() != null) && (getHealth() < getMaxHealth() / 2.0F) && (master == null)) {
                for (int i = 0; i < 16; i++) {
                    world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX + (rand.nextDouble() - 0.5D) * width, posY + rand.nextDouble() * height, posZ + (rand.nextDouble() - 0.5D) * width, 0.0D, 0.0D, 0.0D);
                    world.spawnParticle(EnumParticleTypes.FLAME, posX + (rand.nextDouble() - 0.5D) * width, posY + rand.nextDouble() * height, posZ + (rand.nextDouble() - 0.5D) * width, 0.0D, 0.0D, 0.0D);
                }

                // custom sound thetitans:titanland now registered and wired
                playSound(TitanAudio.get("thetitans:titanland"), 10000.0F, 1.0F);
                TransformEntity(this);
            }

            if (onGround) {
                isAirBorne = false;
            }

            List<?> found11 = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().expand(8D, 8D, 8D));
            if (!world.isRemote && found11 != null && !found11.isEmpty() && (ticksExisted + getEntityId()) % (getHealth() < getMaxHealth() / 2 ? 40 : 160) == 0) {
                world.createExplosion(this, posX, posY, posZ, 8F, false);
                for (int i1 = 0; i1 < found11.size(); i1++) {
                    Entity entity = (Entity)found11.get(i1);
                    if (entity != null && entity instanceof EntityLivingBase && allowStrikeGiant(entity.getClass().asSubclass(EntityLivingBase.class))) {
                        entity.motionY += rand.nextDouble();
                        if (!world.isRemote)
                            ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(ClientProxy.electricJudgment, 10, 1));
                        world.playEvent((EntityPlayer)null, 1017, new BlockPos(entity.posX, entity.posY, entity.posZ), 0);
                    }
                }
            }
        }

        if (fetchKindIntMinion() == 2) {
            if (getAttackTarget() != null) {
                double d0 = getDistanceSq(getAttackTarget());
                if (d0 < 0.8D) {
                    attackEntityAsMob(getAttackTarget());
                }

                if ((rand.nextInt(40) == 0) && (onGround) && (d0 < 256.0D) && (getAttackTarget().posY > posY + 3.0D)) {
                    addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 60, 7));
                    faceEntity(getAttackTarget(), 180.0F, 180.0F);
                    double d01 = getAttackTarget().posX - posX;
                    double d1 = getAttackTarget().posZ - posZ;
                    float f2 = MathHelper.sqrt(d01 * d01 + d1 * d1);
                    jump();
                    motionX = (d01 / f2 * 0.75D * 0.75D + motionX * 0.75D);
                    motionZ = (d1 / f2 * 0.75D * 0.75D + motionZ * 0.75D);
                }
            }
        }

        if (fetchKindIntMinion() == 1) {
            if ((ticksExisted % 40 == 0) && (toHealMob != null)) {
                if (toHealMob.getHealth() < toHealMob.getMaxHealth()) {
                    swingArm(net.minecraft.util.EnumHand.MAIN_HAND);
                    faceEntity(toHealMob, 180.0F, getVerticalFaceSpeed());
                    toHealMob.heal(4.0F);
                    playSound(SoundEvents.ENTITY_WITHER_SHOOT, 1.0F, 3.0F);
                }

                else {
                    toHealMob = null;
                }
            }
        }

        if ((getAttackTarget() != null) && (!getAttackTarget().isEntityAlive())) {
            setAttackTarget(null);
        }

        if (master != null) {
            if (master.getAttackTarget() != null && master instanceof EntityZombieTitan && (((EntityZombieTitan)master).getAnimID() == 11 && ((EntityZombieTitan)master).getAnimTick() > 80)) {
                getMoveHelper().setMoveTo(master.getAttackTarget().posX, master.getAttackTarget().posY, master.getAttackTarget().posZ, 10D);
            }

            if ((master.getAttackTarget() != null)) {
                if (master.getAttackTarget().height < 6F)
                    setAttackTarget(master.getAttackTarget());
                else
                    getLookHelper().setLookPositionWithEntity(master.getAttackTarget(), 10F, 40F);
            }

            if (ticksExisted % 10 == 0 && (master.getAttackTarget() != null) && master.getAttackTarget() instanceof EntityVillager) {
                if (getDistanceSq(master.getAttackTarget()) > 256D)
                    getMoveHelper().setMoveTo(master.getAttackTarget().posX, master.getAttackTarget().posY, master.getAttackTarget().posZ, 1.0D);
                else
                    getNavigator().tryMoveToEntityLiving(master.getAttackTarget(), 1.0D);
            }

            else if (getDistanceSq(master) > 2304.0D) {
                getMoveHelper().setMoveTo(master.posX, master.posY, master.posZ, 2.0D);
            }
        }

        else {
            List<?> list = world.getEntitiesWithinAABB(EntityZombieTitan.class, getEntityBoundingBox().expand(100.0D, 100.0D, 100.0D));
            if ((list != null) && (!list.isEmpty())) {
                for (int i1 = 0; i1 < list.size(); i1++) {
                    Entity entity = (Entity)list.get(i1);
                    if ((entity != null) && ((entity instanceof EntityZombieTitan))) {
                        master = ((EntityZombieTitan)entity);
                    }
                }
            }
        }
    }

    protected void updateAITasks() {
        if ((randomSoundDelay > 0) && (--randomSoundDelay == 0))
            playSound(getHurtSound(DamageSource.GENERIC), getSoundVolume(), getSoundPitch() + 0.25F);
        if (collidedHorizontally && master != null)
            motionY = 0.2D;
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
        if (fetchKindIntMinion() == 4) {
            --ticksExisted;
            ++deathTicks;
            if (master != null) {
                double mx = posX - master.posX;
                double my = (posY + getEyeHeight()) - (master.posY + master.getEyeHeight());
                double mz = posZ - master.posZ;
                short short1 = (short) (getDistance(master) * 2);
                for (int f = 0; f < short1; f++) {
                    double d9 = f / (short1 - 1.0D);
                    double d6 = posX + mx * -d9;
                    double d7 = (posY + getEyeHeight()) + my * -d9;
                    double d8 = posZ + mz * -d9;
                    world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, d6, d7, d8, master.motionX, master.motionY + 0.2D, master.motionZ);
                }
            }

            if (!world.isRemote) {
                if (deathTicks > 150 && deathTicks % 5 == 0) {
                    dropFewItems(true, 0);
                }

                if (deathTicks == 1) {
                    world.playBroadcastSound(1018, new BlockPos(posX, posY, posZ), 0);
                }
            }

            if (deathTicks >= 180 && deathTicks <= 200) {
                float f = (rand.nextFloat() - 0.5F) * width;
                float f1 = (rand.nextFloat() - 0.5F) * height;
                float f2 = (rand.nextFloat() - 0.5F) * width;
                world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX + (double)f, posY + getEyeHeight() + (double)f1, posZ + (double)f2, 0.0D, 0.0D, 0.0D);
            }

            move(MoverType.SELF, 0.0D, 0.10000000149011612D, 0.0D);
            float f = (rand.nextFloat() - 0.5F) * width;
            float f1 = (rand.nextFloat() - 0.5F) * height;
            float f2 = (rand.nextFloat() - 0.5F) * width;
            world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, posX + (double)f, posY + getEyeHeight() + (double)f1, posZ + (double)f2, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(EnumParticleTypes.LAVA, posX + (double)f, posY + getEyeHeight() + (double)f1, posZ + (double)f2, rand.nextGaussian(), rand.nextGaussian(), rand.nextGaussian());
            if (deathTicks == 200 && !world.isRemote) {
                if (master != null) {
                    master.heal(master.getMaxHealth() / 100F);
                    for (int i = 0; i < 100; ++i) {
                        double d2 = rand.nextGaussian() * 0.02D;
                        double d0 = rand.nextGaussian() * 0.02D;
                        double d1 = rand.nextGaussian() * 0.02D;
                        world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, master.posX + (double)(rand.nextFloat() * master.width * 2.0F) - (double)master.width, master.posY + (double)(rand.nextFloat() * master.height), master.posZ + (double)(rand.nextFloat() * master.width * 2.0F) - (double)master.width, d2, d0, d1);
                    }
                }

                setDead();
            }
        }

        else {
            super.onDeathUpdate();
        }
    }

    public void onKillEntity(EntityLivingBase arg70074n1) {
        if (arg70074n1 instanceof EntityVillager) {
            EntityZombieMinion entityzombie = new EntityZombieMinion(world);
            entityzombie.copyLocationAndAnglesFrom(arg70074n1);
            world.removeEntity(arg70074n1);
            entityzombie.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityzombie)), (IEntityLivingData)null);
            entityzombie.setMinionType(fetchKindIntMinion());
            entityzombie.assignVillager(true);
            if (arg70074n1.isChild()) {
                entityzombie.setChild(true);
            }

            world.spawnEntity(entityzombie);
            world.playEvent((EntityPlayer)null, 1016, new BlockPos(posX, posY, posZ), 0);
        }
    }

    public void TransformEntity(Entity entity) {
        entity.world.newExplosion(entity, entity.posX, entity.posY, entity.posZ, 18.0F, true, entity.world.getGameRules().getBoolean("mobGriefing"));
        EntityZombieTitan entitytitan = new EntityZombieTitan(entity.world);
        entitytitan.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, 0.0F);
        entitytitan.onInitialSpawn(entity.world.getDifficultyForLocation(new BlockPos(entitytitan)), (IEntityLivingData)null);
        entity.setDead();
        entitytitan.funcM82206();
        entity.world.spawnEntity(entitytitan);
        if (isChild()) {
            entitytitan.setChild(true);
        }

        if (villagerIs()) {
            entitytitan.assignVillager(true);
        }
    }

    public void attackEntityWithRangedAttack(EntityLivingBase arg82196n1, float arg82196n2) {
        swingArm(net.minecraft.util.EnumHand.MAIN_HAND);
        if (getDistanceSq(arg82196n1) < (arg82196n1.width * arg82196n1.width) + 36D)
            attackEntityAsMob(arg82196n1);
        else
            switch (rand.nextInt(4)) {
            case 0: {
                    EntityTippedArrow entityarrow = new EntityTippedArrow(world, this);
                    double d10 = arg82196n1.posX - posX;
                    double d20 = arg82196n1.getEntityBoundingBox().minY + arg82196n1.height / 3.0F - entityarrow.posY;
                    double d30 = arg82196n1.posZ - posZ;
                    double d40 = MathHelper.sqrt(d10 * d10 + d30 * d30);
                    entityarrow.shoot(d10, d20 + d40 * 0.2D, d30, 1.6F, 1.0F);
                    entityarrow.setIsCritical(true);
                    entityarrow.setDamage(arg82196n2 * 2.0F + rand.nextGaussian() * 0.25D + world.getDifficulty().getId() * 0.11F);
                    // "random.bow"
                    playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
                    world.spawnEntity(entityarrow);
                    break;
                }
            case 1: {
                    // original: EntityPotion meta 32732 (splash harming), 32725 (splash healing) vs undead
                    net.minecraft.item.ItemStack potionStack = net.minecraft.potion.PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), arg82196n1.isEntityUndead() ? net.minecraft.init.PotionTypes.HEALING : net.minecraft.init.PotionTypes.HARMING);
                    net.minecraft.entity.projectile.EntityPotion entitypotion = new net.minecraft.entity.projectile.EntityPotion(world, this, potionStack);
                    double d0 = arg82196n1.posY + 0.5D;
                    entitypotion.rotationPitch -= -20.0F;
                    double d1 = arg82196n1.posX + arg82196n1.motionX - posX;
                    double d2 = d0 - posY;
                    double d3 = arg82196n1.posZ + arg82196n1.motionZ - posZ;
                    float f1 = MathHelper.sqrt(d1 * d1 + d3 * d3);
                    entitypotion.shoot(d1, d2 + f1 * 0.2F, d3, 1.6F, f1 / 20.0F);
                    world.spawnEntity(entitypotion);
                    break;
                }
            case 2: {
                    double d011 = getDistanceSq(arg82196n1);
                    double d111 = arg82196n1.posX - posX;
                    double d211 = arg82196n1.getEntityBoundingBox().minY + arg82196n1.height / 2.0F - (posY + arg82196n1.height / 2.0F);
                    double d311 = arg82196n1.posZ - posZ;
                    float f = MathHelper.sqrt(MathHelper.sqrt(d011)) * 0.1F;
                    world.playEvent((EntityPlayer)null, 1009, new BlockPos(posX, posY, posZ), 0);
                    EntitySmallFireball entitysmallfireball = new EntitySmallFireball(world, this, d111 + getRNG().nextGaussian() * f, d211, d311 + getRNG().nextGaussian() * f);
                    entitysmallfireball.posY = (posY + 1.6D);
                    world.spawnEntity(entitysmallfireball);
                    break;
                }
            case 3: {
                    playSound(SoundEvents.ENTITY_ZOMBIE_HURT, 1.0F, 0.5F);
                    playSound(SoundEvents.ENTITY_ZOMBIE_HURT, 1.0F, 0.5F);
                    playSound(SoundEvents.ENTITY_ZOMBIE_HURT, 1.0F, 0.5F);
                    playSound(SoundEvents.ENTITY_ZOMBIE_HURT, 1.0F, 0.5F);
                    world.playSound(null, arg82196n1.posX, arg82196n1.posY, arg82196n1.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
                    for (int i = 0; i < 256; i++) {
                        EntityItem entityitem = arg82196n1.dropItem(Items.ROTTEN_FLESH, 1);
                        if (entityitem != null) {
                            entityitem.setPickupDelay(6000);
                            entityitem.lifespan = (40 + rand.nextInt(20));
                        }
                    }

                    arg82196n1.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 100, 2));
                    arg82196n1.attackEntityFrom(DamageSource.STARVE, 5.0F);
                    arg82196n1.hurtResistantTime = 0;
                }
        }
    }

    class GroupData implements IEntityLivingData {
        public boolean fld142048a;
        public boolean fld142046b;
        private GroupData(boolean p_i2348_2_, boolean p_i2348_3_) {
            fld142048a = false;
            fld142046b = false;
            fld142048a = p_i2348_2_;
            fld142046b = p_i2348_3_;
        }

        GroupData(boolean p_i2349_2_, boolean p_i2349_3_, Object p_i2349_4_) {
            this(p_i2349_2_, p_i2349_3_);
        }
    }

    public class EntityAIFindEntityNearestInjuredAlly extends EntityAIBase {
        private EntityZombieMinion fld179434b;
        private EntityLivingBase fld179433e;
        public EntityAIFindEntityNearestInjuredAlly(EntityZombieMinion entityCaveSpiderPriest) {
            fld179434b = entityCaveSpiderPriest;
        }

        public boolean shouldExecute() {
            if (!fld179434b.isEntityAlive()) {
                return false;
            }

            if (fld179434b.fetchKindMinion() != EnumMinionType.PRIEST) {
                return false;
            }

            if (fld179433e != null) {
                return false;
            }

            double d0 = funcF179431();
            List<?> list = fld179434b.world.getEntitiesWithinAABB(EntityZombieMinion.class, fld179434b.getEntityBoundingBox().expand(d0, d0, d0));
            if (list.isEmpty()) {
                return false;
            }

            for (int i = 0; i < list.size(); i++) {
                EntityZombieMinion entity = (EntityZombieMinion)list.get(i);
                if (entity.getHealth() < entity.getMaxHealth() && entity.isEntityAlive())
                    fld179433e = entity;
            }

            return true;
        }

        public boolean shouldContinueExecuting() {
            EntityLivingBase entitylivingbase = fld179434b.toHealMob;
            if (entitylivingbase == null) {
                return false;
            }

            if (!entitylivingbase.isEntityAlive()) {
                fld179433e = null;
                return false;
            }

            if (entitylivingbase.getHealth() >= entitylivingbase.getMaxHealth()) {
                fld179433e = null;
                return false;
            }

            double d0 = funcF179431();
            return fld179434b.getDistanceSq(entitylivingbase) <= d0 * d0;
        }

        public void startExecuting() {
            fld179434b.toHealMob = ((EntityLiving)fld179433e);
            super.startExecuting();
        }

        public void resetTask() {
            fld179434b.toHealMob = null;
            fld179433e = null;
            super.resetTask();
        }

        public void updateTask() {
            if (fld179434b.toHealMob != null && fld179434b.getDistance(fld179434b.toHealMob) > 16D) {
                fld179434b.getNavigator().tryMoveToEntityLiving(fld179434b.toHealMob, 1D);
                fld179434b.getLookHelper().setLookPositionWithEntity(fld179434b.toHealMob, 10F, fld179434b.getVerticalFaceSpeed());
            }
        }

        protected double funcF179431() {
            return 32.0D;
        }
    }

    @Override
    public boolean friendlyIs(Entity entity) {
        if (entity == null)
            return false;
        return entity instanceof EntityZombieMinion || entity instanceof EntityZombieTitan || entity instanceof EntityGiantZombieBetter;
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
        return 2F;
    }

    @Override
    public float fetchPitchSummon() {
        return 1F;
    }

    @Override
    public double fetchYOffsetSummon() {
        return 0D;
    }
}
