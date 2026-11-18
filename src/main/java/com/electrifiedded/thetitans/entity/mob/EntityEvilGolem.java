package com.electrifiedded.thetitans.entity.mob;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.handler.EventStore;
import com.electrifiedded.thetitans.registry.TitanMilestones;
import com.electrifiedded.thetitans.registry.TitanAudio;
import com.electrifiedded.thetitans.registry.TitanTrophies;
import com.electrifiedded.thetitans.world.dimension.WorldProviderNowhere;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class EntityEvilGolem extends EntityMob {
    private static final net.minecraft.network.datasync.DataParameter<Byte> CLIMBING = net.minecraft.network.datasync.EntityDataManager.createKey(EntityEvilGolem.class, net.minecraft.network.datasync.DataSerializers.BYTE);
    private static final net.minecraft.network.datasync.DataParameter<Integer> KIND_MOB = net.minecraft.network.datasync.EntityDataManager.createKey(EntityEvilGolem.class, net.minecraft.network.datasync.DataSerializers.VARINT);
    private int attackTimer;
    private int waitTeleport;

    public EntityEvilGolem(World p_i1738_1_) {
        super(p_i1738_1_);
        this.setSize(1.45F, 3.2F);
        this.tasks.addTask(1, new EntityAIAttackOnCollideGolem(this, 1.0D, true));
        this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
        this.tasks.addTask(6, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityGolem.class, 0, false, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, false, false, null));
        this.experienceValue = 25;
    }

    @SideOnly(Side.CLIENT)
    public int getAttackTimer() {
        return this.attackTimer;
    }

    public float getEyeHeight() {
        return 2.9F;
    }

    public boolean attackEntityFrom(DamageSource arg70097n1, float arg70097n2) {
        if (this.isEntityInvulnerable(arg70097n1)) {
            return false;
        }

        else {
            if (this.fetchKindMob() == 3 && arg70097n1 instanceof EntityDamageSourceIndirect) {
                for (int i = 0; i < 64; ++i) {
                    if (this.teleportRandomly()) {
                        return true;
                    }
                }

                return false;
            }

            else {
                return super.attackEntityFrom(arg70097n1, arg70097n2);
            }
        }
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(CLIMBING, Byte.valueOf((byte)0));
        this.dataManager.register(KIND_MOB, Integer.valueOf(0));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote && this.fetchKindMob() == 4) {
            this.setBesideClimbableBlock(this.collidedHorizontally);
        }
    }

    public void fall(float distance, float damageMultiplier) {
    }

    public boolean isPotionApplicable(PotionEffect arg70687n1) {
        return this.fetchKindMob() == 4 && arg70687n1.getPotion() == MobEffects.POISON ? false : super.isPotionApplicable(arg70687n1);
    }

    public boolean isBesideClimbableBlock() {
        return (this.dataManager.get(CLIMBING).byteValue() & 1) != 0;
    }

    public void setBesideClimbableBlock(boolean arg70839n1) {
        byte b0 = this.dataManager.get(CLIMBING).byteValue();
        if (arg70839n1) {
            b0 = (byte)(b0 | 1);
        }

        else {
            b0 &= -2;
        }

        this.dataManager.set(CLIMBING, Byte.valueOf(b0));
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
        // original: kill achievements per golem type (type 0 is excluded by the > 0 guard, as in 1.7.10)
        if (cause.getTrueSource() instanceof EntityPlayer && this.fetchKindMob() > 0) {
            EntityPlayer player = (EntityPlayer) cause.getTrueSource();
            switch (this.fetchKindMob()) {
                case 1: TitanMilestones.runGrant(player, TitanTrophies.witheredGoliathKill); break;
                case 2: TitanMilestones.runGrant(player, TitanTrophies.goldenGuardianKill); break;
                case 3: TitanMilestones.runGrant(player, TitanTrophies.brutermanKill); break;
                case 4: TitanMilestones.runGrant(player, TitanTrophies.strongSpiderKill); break;
                default: TitanMilestones.runGrant(player, TitanTrophies.zombieBruteKill);
            }
        }
    }

    public int fetchKindMob() {
        return this.dataManager.get(KIND_MOB).intValue();
    }

    public void assignKindMob(int miniontype) {
        this.dataManager.set(KIND_MOB, Integer.valueOf(miniontype));
        double hp, atk, spd;
        switch (miniontype) {
            case 1: hp = 1500.0D; atk = 27.0D; spd = 0.25D; break;
            case 2: hp = 3000.0D; atk = 28.0D; spd = 0.3D; break;
            case 3: hp = 10000.0D; atk = 70.0D; spd = 0.25D; break;
            case 4: hp = 750.0D; atk = 18.0D; spd = 0.325D; break;
            default: hp = 1000.0D; atk = 25.0D; spd = 0.275D;
        }
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(com.electrifiedded.thetitans.TheTitans.adjustValueToProgressionSystem(this.world, hp));
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(com.electrifiedded.thetitans.TheTitans.adjustValueToProgressionSystem(this.world, atk));
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(spd);
        this.experienceValue = (int)(hp / 40.0D);
        this.setHealth(this.getMaxHealth());
    }

    protected int decreaseAirSupply(int arg70682n1) {
        return arg70682n1;
    }

    public String getName() {
        switch (this.fetchKindMob()) {
            case 1:
                return I18n.translateToLocal("entity.GolemSkeletonWither.name");
            case 2:
                return I18n.translateToLocal("entity.GoldenGuardian.name");
            case 3:
                return I18n.translateToLocal("entity.GolemEnder.name");
            case 4:
                return I18n.translateToLocal("entity.GolemSpider.name");
            default:
                return I18n.translateToLocal("entity.GolemZombie.name");
        }
    }

    protected Item getDropItem() {
        switch (this.fetchKindMob()) {
            case 1:
                return Items.COAL;
            case 2:
                return Items.GOLD_INGOT;
            case 3:
                return Items.ENDER_EYE;
            case 4:
                return Items.STRING;
            default:
                return Items.ROTTEN_FLESH;
        }
    }

    protected Item fetchDropStackSecond() {
        switch (this.fetchKindMob()) {
            case 1:
                return Items.BONE;
            case 2:
                return Items.GOLD_INGOT;
            case 3:
                return Items.ENDER_PEARL;
            case 4:
                return Items.FERMENTED_SPIDER_EYE;
            default:
                return rand.nextBoolean() ? Items.CARROT : Items.POTATO;
        }
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        Item item = this.getDropItem();
        if (item != null) {
            int j = 2 + this.rand.nextInt(4);
            if (arg70628n2 > 0) {
                j += this.rand.nextInt(arg70628n2 + 1);
            }

            for (int k = 0; k < j; ++k) {
                this.dropItem(item, 1);
            }
        }

        item = this.fetchDropStackSecond();
        if (item != null) {
            int j = this.rand.nextInt(3);
            if (arg70628n2 > 0) {
                j += this.rand.nextInt(arg70628n2 + 1);
            }

            for (int k = 0; k < j; ++k) {
                this.dropItem(item, 1);
            }
        }
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!this.world.isRemote && fetchKindMob() == 0) {
            if (this.world.isDaytime()) {
                float f = this.getBrightness();
                if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.canBlockSeeSky(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ)))) {
                    boolean flag = true;
                    ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                    if (!itemstack.isEmpty()) {
                        if (itemstack.isItemStackDamageable()) {
                            itemstack.setItemDamage(itemstack.getItemDamage() + this.rand.nextInt(2));
                            if (itemstack.getItemDamage() >= itemstack.getMaxDamage()) {
                                this.renderBrokenItemStack(itemstack);
                                this.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
                            }
                        }

                        flag = false;
                    }

                    if (flag) {
                        this.setFire(8);
                    }
                }
            }

            else {
                if (this.ticksExisted % 60 == 0) {
                    this.heal(2.0F);
                }

                if (this.rand.nextInt(160) == 0) {
                    double sx = this.posX + (this.rand.nextDouble() * 2.0D - 1.0D) * 30.0D;
                    double sz = this.posZ + (this.rand.nextDouble() * 2.0D - 1.0D) * 30.0D;
                    double sy = (double)this.world.getHeight(MathHelper.floor(sx), MathHelper.floor(sz));
                    EntityZombie summon = new EntityZombie(this.world);
                    summon.copyLocationAndAnglesFrom(this);
                    summon.setPosition(sx, sy, sz);
                    this.world.spawnEntity(summon);
                    summon.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(summon)), (IEntityLivingData)null);
                }
            }
        }

        if (fetchKindMob() == 4) {
            if (this.getAttackTarget() != null && this.getDistance(this.getAttackTarget()) > 2.0F && this.getDistance(this.getAttackTarget()) < 6.0F && this.rand.nextInt(10) == 0) {
                if (this.onGround) {
                    double d0 = this.getAttackTarget().posX - this.posX;
                    double d1 = this.getAttackTarget().posZ - this.posZ;
                    float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1);
                    this.motionX = d0 / (double)f2 * 0.5D * 0.800000011920929D + this.motionX * 0.20000000298023224D;
                    this.motionZ = d1 / (double)f2 * 0.5D * 0.800000011920929D + this.motionZ * 0.20000000298023224D;
                    this.motionY = 0.4000000059604645D;
                }
            }

            if (!this.world.isRemote) {
                if (this.ticksExisted % 100 == 0) {
                    this.heal(2.0F);
                }

                if (this.rand.nextInt(240) == 0) {
                    EntitySpider spider = new EntitySpider(this.world);
                    spider.copyLocationAndAnglesFrom(this);
                    this.world.spawnEntity(spider);
                    spider.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(spider)), (IEntityLivingData)null);
                    spider.motionY += 0.5D;
                }

                if (this.rand.nextInt(240) == 0) {
                    EntityCaveSpider caveSpider = new EntityCaveSpider(this.world);
                    caveSpider.copyLocationAndAnglesFrom(this);
                    this.world.spawnEntity(caveSpider);
                    caveSpider.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(caveSpider)), (IEntityLivingData)null);
                    caveSpider.motionY += 0.5D;
                }
            }
        }

        if (fetchKindMob() == 3) {
            if (!this.world.isRemote && this.isEntityAlive()) {
                if (this.isInWater()) {
                    this.attackEntityFrom(DamageSource.DROWN, 1.0F);
                }

                if (this.getAttackTarget() != null) {
                    if (this.getAttackTarget().getDistanceSq(this) < 4.0D) {
                        this.teleportRandomly();
                    }

                    if (this.getAttackTarget().getDistanceSq(this) > 256.0D && this.waitTeleport++ >= 30 && this.teleportToEntity(this.getAttackTarget())) {
                        this.waitTeleport = 0;
                    }
                }

                else {
                    this.waitTeleport = 0;
                }
            }
        }

        if (this.attackTimer > 0) {
            --this.attackTimer;
        }

        if (this.motionX * this.motionX + this.motionZ * this.motionZ != 0D && this.rand.nextInt(5) == 0) {
            int i = MathHelper.floor(this.posX);
            int j = MathHelper.floor(this.posY - 0.20000000298023224D);
            int k = MathHelper.floor(this.posZ);
            BlockPos pos = new BlockPos(i, j, k);
            IBlockState state = this.world.getBlockState(pos);
            if (state.getMaterial() != Material.AIR) {
                this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, this.getEntityBoundingBox().minY + 0.1D, this.posZ + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, 4.0D * ((double)this.rand.nextFloat() - 0.5D), 0.5D, ((double)this.rand.nextFloat() - 0.5D) * 4.0D, net.minecraft.block.Block.getStateId(state));
            }
        }

        if (fetchKindMob() == 1) {
            this.isImmuneToFire = true;
        }
    }

    public boolean attackEntityAsMob(Entity arg70652n1) {
        this.attackTimer = 10;
        this.world.setEntityState(this, (byte)4);
        boolean flag = arg70652n1.attackEntityFrom(DamageSource.causeMobDamage(this), (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
        if (flag) {
            int i = this.world.getDifficulty().getId();
            if (fetchKindMob() == 0 && this.isBurning() && this.rand.nextFloat() < (float)i * 0.3F) {
                arg70652n1.setFire(4 * i);
            }

            arg70652n1.addVelocity((double)(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) * 1.4F), 0.6D, (double)(MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) * 1.4F));
            if (arg70652n1 instanceof EntityLivingBase && fetchKindMob() == 1) {
                ((EntityLivingBase)arg70652n1).addPotionEffect(new PotionEffect(MobEffects.WITHER, 200));
            }

            if (arg70652n1 instanceof EntityLivingBase && fetchKindMob() == 2) {
                byte b0 = 0;
                if (this.world.getDifficulty() == EnumDifficulty.NORMAL) {
                    b0 = 7;
                }

                else if (this.world.getDifficulty() == EnumDifficulty.HARD) {
                    b0 = 15;
                }

                if (b0 > 0) {
                    ((EntityLivingBase)arg70652n1).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, b0 * 20, 0));
                }
            }
        }

        this.playSound(com.electrifiedded.thetitans.registry.TitanAudio.get("evilGolemPunch"), 1.0F, 1.0F);
        return flag;
    }

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte arg70103n1) {
        if (arg70103n1 == 4) {
            this.attackTimer = 10;
            // TODO sounds — thetitans:evilGolemPunch unregistered; wrapped raw, resolves via sounds.json
            this.playSound(com.electrifiedded.thetitans.registry.TitanAudio.get("evilGolemPunch"), 1.0F, 1.0F);
        }

        else {
            super.handleStatusUpdate(arg70103n1);
        }
    }

    protected SoundEvent getAmbientSound() {
        switch (this.fetchKindMob()) {
                // TODO sounds — thetitans:bigZombieLiving unregistered
            case 0:
                return com.electrifiedded.thetitans.registry.TitanAudio.get("bigZombieLiving");
            case 1:
                return SoundEvents.ENTITY_SKELETON_AMBIENT;
            case 3:
                return SoundEvents.ENTITY_ENDERMEN_AMBIENT;
            case 4:
                return SoundEvents.ENTITY_SPIDER_AMBIENT;
            default:
                return null;
        }
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        switch (this.fetchKindMob()) {
            case 0:
                return com.electrifiedded.thetitans.registry.TitanAudio.get("bigZombieGrunt");
            case 1:
                return SoundEvents.ENTITY_SKELETON_HURT;
            case 3:
                return SoundEvents.ENTITY_ENDERMEN_HURT;
            case 4:
                return SoundEvents.ENTITY_SPIDER_AMBIENT;
            default:
                return SoundEvents.ENTITY_IRONGOLEM_HURT;
        }
    }

    protected SoundEvent getDeathSound() {
        switch (this.fetchKindMob()) {
            case 0:
                return com.electrifiedded.thetitans.registry.TitanAudio.get("bigZombieDeath");
            case 1:
                return SoundEvents.ENTITY_SKELETON_DEATH;
            case 3:
                return SoundEvents.ENTITY_ENDERMEN_DEATH;
            case 4:
                return SoundEvents.ENTITY_SPIDER_DEATH;
            default:
                return SoundEvents.ENTITY_IRONGOLEM_DEATH;
        }
    }

    protected void playStepSound(BlockPos pos, net.minecraft.block.Block blockIn) {
        switch (this.fetchKindMob()) {
            case 0: {
                    this.playSound(SoundEvents.ENTITY_ZOMBIE_STEP, 1.0F, 1.0F);
                    this.playSound(SoundEvents.ENTITY_IRONGOLEM_STEP, 1.0F, 1.0F);
                    break;
                }

            case 1: {
                    this.playSound(SoundEvents.ENTITY_SKELETON_STEP, 1.0F, 1.0F);
                    this.playSound(SoundEvents.ENTITY_IRONGOLEM_STEP, 1.0F, 1.0F);
                    break;
                }

            case 4: {
                    this.playSound(SoundEvents.ENTITY_SPIDER_STEP, 1.0F, 1.0F);
                    this.playSound(SoundEvents.ENTITY_IRONGOLEM_STEP, 1.0F, 1.0F);
                    break;
                }

            default: {
                    this.playSound(SoundEvents.ENTITY_IRONGOLEM_STEP, 1.0F, 1.0F);
                }
        }
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return this.fetchKindMob() == 0 || this.fetchKindMob() == 1 ? EnumCreatureAttribute.UNDEAD : (this.fetchKindMob() == 4 ? EnumCreatureAttribute.ARTHROPOD : EnumCreatureAttribute.UNDEFINED);
    }

    public void setInWeb() {
        if (this.fetchKindMob() != 4) {
            super.setInWeb();
        }
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    public boolean isOnLadder() {
        return this.fetchKindMob() == 4 ? this.isBesideClimbableBlock() : super.isOnLadder();
    }

    public IEntityLivingData onInitialSpawn(net.minecraft.world.DifficultyInstance difficulty, IEntityLivingData arg110161n1) {
        Object p_110161_1_1 = super.onInitialSpawn(difficulty, arg110161n1);
        if (this.world.provider instanceof WorldProviderHell) {
            this.assignKindMob(this.rand.nextInt(2) == 0 ? 2 : 1);
        }

        else if (!(this.world.provider instanceof WorldProviderEnd) && !(this.world.provider instanceof WorldProviderNowhere)) {
            this.assignKindMob(this.rand.nextInt(20) == 0 && EventStore.getBool(this.world, "PostDragon") ? 3 : (this.rand.nextInt(2) == 0 ? 0 : 4));
        }

        else {
            this.assignKindMob(3);
        }

        if (this.fetchKindMob() == 4) {
            if (this.world.rand.nextInt(100) == 0) {
                EntitySkeleton entityskeleton = new EntitySkeleton(this.world);
                entityskeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                entityskeleton.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityskeleton)), (IEntityLivingData)null);
                this.world.spawnEntity(entityskeleton);
                entityskeleton.startRiding(this);
            }

            if (p_110161_1_1 == null) {
                p_110161_1_1 = new EntityEvilGolem.GroupData();
                if (this.world.rand.nextFloat() < 0.1F * difficulty.getClampedAdditionalDifficulty()) {
                    ((EntityEvilGolem.GroupData)p_110161_1_1).funcA111104(this.world.rand);
                }
            }

            if (p_110161_1_1 instanceof EntityEvilGolem.GroupData) {
                int i = ((EntityEvilGolem.GroupData)p_110161_1_1).fld111105a;
                if (i > 0 && Potion.getPotionById(i) != null) {
                    this.addPotionEffect(new PotionEffect(Potion.getPotionById(i), Integer.MAX_VALUE));
                }
            }
        }

        if (this.fetchKindMob() == 0) {
            this.playSound(this.getAmbientSound(), 5.0F, 0.9F);
        }

        return (IEntityLivingData)p_110161_1_1;
    }

    public static class GroupData implements IEntityLivingData {
        public int fld111105a;
        public void funcA111104(Random arg111104n1) {
            int i = arg111104n1.nextInt(5);
            if (i <= 1) {
                this.fld111105a = Potion.getIdFromPotion(MobEffects.SPEED);
            }

            else if (i <= 2) {
                this.fld111105a = Potion.getIdFromPotion(MobEffects.STRENGTH);
            }

            else if (i <= 3) {
                this.fld111105a = Potion.getIdFromPotion(MobEffects.REGENERATION);
            }

            else if (i <= 4) {
                this.fld111105a = Potion.getIdFromPotion(MobEffects.INVISIBILITY);
            }
        }
    }

    /**
     * Teleport the enderman to a random nearby position
     */
    protected boolean teleportRandomly() {
        double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
        double d1 = this.posY + (double)(this.rand.nextInt(64) - 32);
        double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
        return this.teleportTo(d0, d1, d2);
    }

    /**
     * Teleport the enderman to another entity
     */
    protected boolean teleportToEntity(Entity arg70816n1) {
        Vec3d vec3 = new Vec3d(this.posX - arg70816n1.posX, this.getEntityBoundingBox().minY + (double)(this.height / 2.0F) - arg70816n1.posY + (double)arg70816n1.getEyeHeight(), this.posZ - arg70816n1.posZ);
        vec3 = vec3.normalize();
        double d0 = 16.0D;
        double d1 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3.x * d0;
        double d2 = this.posY + (double)(this.rand.nextInt(16) - 8) - vec3.y * d0;
        double d3 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3.z * d0;
        return this.teleportTo(d1, d2, d3);
    }

    /**
     * Teleport the enderman
     */
    protected boolean teleportTo(double arg70825n1, double arg70825n3, double arg70825n5) {
        EnderTeleportEvent event = new EnderTeleportEvent(this, arg70825n1, arg70825n3, arg70825n5, 0);
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
            while (!flag1 && j > 0) {
                IBlockState below = this.world.getBlockState(new BlockPos(i, j - 1, k));
                if (below.getMaterial().blocksMovement()) {
                    flag1 = true;
                }

                else {
                    --this.posY;
                    --j;
                }
            }

            if (flag1) {
                this.setPosition(this.posX, this.posY, this.posZ);
                if (this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.world.containsAnyLiquid(this.getEntityBoundingBox())) {
                    flag = true;
                }
            }
        }

        if (!flag) {
            this.setPosition(d3, d4, d5);
            return false;
        }

        else {
            short short1 = 128;
            for (int l = 0; l < short1; ++l) {
                double d6 = (double)l / ((double)short1 - 1.0D);
                float f = (this.rand.nextFloat() - 0.5F) * 0.2F;
                float f1 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                float f2 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                double d7 = d3 + (this.posX - d3) * d6 + (this.rand.nextDouble() - 0.5D) * (double)this.width * 2.0D;
                double d8 = d4 + (this.posY - d4) * d6 + this.rand.nextDouble() * (double)this.height;
                double d9 = d5 + (this.posZ - d5) * d6 + (this.rand.nextDouble() - 0.5D) * (double)this.width * 2.0D;
                this.world.spawnParticle(EnumParticleTypes.PORTAL, d7, d8, d9, (double)f, (double)f1, (double)f2);
            }

            this.world.playSound(null, d3, d4, d5, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
            return true;
        }
    }

    public class EntityAIAttackOnCollideGolem extends EntityAIBase {
        World world;
        protected EntityCreature attacker;
        int attackTick;
        double speedTowardsTarget;
        boolean longMemory;
        Path pathMobMob;
        Class<?> victimClass;
        private int fld75445i;
        private double fld151497i;
        private double fld151495j;
        private double fld151496k;
        private int failedPathFindingPenalty = 0;
        private boolean canPenalize = false;
        @SuppressWarnings("rawtypes")
        public EntityAIAttackOnCollideGolem(EntityCreature p_i1635_1_, Class p_i1635_2_, double p_i1635_3_, boolean p_i1635_5_) {
            this(p_i1635_1_, p_i1635_3_, p_i1635_5_);
            this.victimClass = p_i1635_2_;
            canPenalize = victimClass == null || !net.minecraft.entity.player.EntityPlayer.class.isAssignableFrom(victimClass); //Only enable delaying when not targeting players.
        }

        public EntityAIAttackOnCollideGolem(EntityCreature p_i1636_1_, double p_i1636_2_, boolean p_i1636_4_) {
            this.attacker = p_i1636_1_;
            this.world = p_i1636_1_.world;
            this.speedTowardsTarget = p_i1636_2_;
            this.longMemory = p_i1636_4_;
            this.setMutexBits(3);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute() {
            EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
            if (entitylivingbase == null) {
                return false;
            }

            else if (!entitylivingbase.isEntityAlive()) {
                return false;
            }

            else if (this.victimClass != null && !this.victimClass.isAssignableFrom(entitylivingbase.getClass())) {
                return false;
            }

            else {
                if (canPenalize) {
                    if (--this.fld75445i <= 0) {
                        this.pathMobMob = this.attacker.getNavigator().getPathToEntityLiving(entitylivingbase);
                        this.fld151497i = 4 + this.attacker.getRNG().nextInt(7);
                        return this.pathMobMob != null;
                    }

                    else {
                        return true;
                    }
                }

                this.pathMobMob = this.attacker.getNavigator().getPathToEntityLiving(entitylivingbase);
                return this.pathMobMob != null;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
            return entitylivingbase == null ? false : (!entitylivingbase.isEntityAlive() ? false : (!this.longMemory ? !this.attacker.getNavigator().noPath() : this.attacker.isWithinHomeDistanceFromPosition(new BlockPos(entitylivingbase))));
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            this.attacker.getNavigator().setPath(this.pathMobMob, this.speedTowardsTarget);
            this.fld75445i = 0;
        }

        /**
         * Resets the task
         */
        public void resetTask() {
            this.attacker.getNavigator().clearPath();
        }

        /**
         * Updates the task
         */
        public void updateTask() {
            EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
            this.attacker.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
            double d0 = this.attacker.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
            double d1 = this.funcA179512(entitylivingbase);
            --this.fld75445i;
            if ((this.longMemory || this.attacker.getEntitySenses().canSee(entitylivingbase)) && this.fld75445i <= 0 && (this.fld151497i == 0.0D && this.fld151495j == 0.0D && this.fld151496k == 0.0D || entitylivingbase.getDistanceSq(this.fld151497i, this.fld151495j, this.fld151496k) >= 1.0D || this.attacker.getRNG().nextFloat() < 0.05F)) {
                this.fld151497i = entitylivingbase.posX;
                this.fld151495j = entitylivingbase.getEntityBoundingBox().minY;
                this.fld151496k = entitylivingbase.posZ;
                this.fld75445i = 4 + this.attacker.getRNG().nextInt(7);
                if (this.canPenalize) {
                    this.fld151497i += failedPathFindingPenalty;
                    if (this.attacker.getNavigator().getPath() != null) {
                        PathPoint finalPathPoint = this.attacker.getNavigator().getPath().getFinalPathPoint();
                        if (finalPathPoint != null && entitylivingbase.getDistanceSq(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
                            failedPathFindingPenalty = 0;
                        else
                            failedPathFindingPenalty += 10;
                    }

                    else {
                        failedPathFindingPenalty += 10;
                    }
                }

                if (d0 > 1024.0D) {
                    this.fld75445i += 10;
                }

                else if (d0 > 256.0D) {
                    this.fld75445i += 5;
                }

                if (!this.attacker.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.speedTowardsTarget)) {
                    this.fld75445i += 15;
                }
            }

            this.attackTick = Math.max(this.attackTick - 1, 0);
            if (d0 <= d1 && this.attackTick <= 0) {
                this.attackTick = 20;
                if (!this.attacker.getHeldItemMainhand().isEmpty()) {
                    this.attacker.swingArm(net.minecraft.util.EnumHand.MAIN_HAND);
                }

                this.attacker.attackEntityAsMob(entitylivingbase);
            }
        }

        protected double funcA179512(EntityLivingBase arg179512n1) {
            return (double)(attacker.width * attacker.width + arg179512n1.width * arg179512n1.width) + 4D;
        }
    }
}
