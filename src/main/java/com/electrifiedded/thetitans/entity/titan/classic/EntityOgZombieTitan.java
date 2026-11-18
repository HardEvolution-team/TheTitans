package com.electrifiedded.thetitans.entity.titan.classic;

import com.electrifiedded.thetitans.entity.deity.IDeity;
import com.electrifiedded.thetitans.entity.minion.EntityCreeperMinion;
import com.electrifiedded.thetitans.handler.EventStore;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanAudio;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeModContainer;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class EntityOgZombieTitan extends EntityMob {
    private static final UUID paceBoostUuidBaby = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
    private static final AttributeModifier paceBoostModifierBaby = new AttributeModifier(paceBoostUuidBaby, "Baby speed boost", 0.5D, 1);
    private float zombieWidth = -1.0F;
    private float zombieHeight;
    private final net.minecraft.world.BossInfoServer bossInfo = (net.minecraft.world.BossInfoServer)(new net.minecraft.world.BossInfoServer(this.getDisplayName(), net.minecraft.world.BossInfo.Color.GREEN, net.minecraft.world.BossInfo.Overlay.PROGRESS));

    public EntityOgZombieTitan(World worldIn) {
        super(worldIn);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityLivingBase.class, 64.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 0, true, false, IDeity.forAThingToKillSearch));
        this.setSize(8.0F, 32.0F);
        this.experienceValue = 50000;
        this.isImmuneToFire = true;
        this.enablePersistence();
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(70.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(7000.0D);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(IS_CHILD, Byte.valueOf((byte)0));
        this.dataManager.register(VILLAGER_IS, Byte.valueOf((byte)0));
    }

    private static final net.minecraft.network.datasync.DataParameter<Byte> IS_CHILD = net.minecraft.network.datasync.EntityDataManager.createKey(EntityOgZombieTitan.class, net.minecraft.network.datasync.DataSerializers.BYTE);
    private static final net.minecraft.network.datasync.DataParameter<Byte> VILLAGER_IS = net.minecraft.network.datasync.EntityDataManager.createKey(EntityOgZombieTitan.class, net.minecraft.network.datasync.DataSerializers.BYTE);

    public int getTotalArmorValue() {
        int i = super.getTotalArmorValue() + 15;
        if (i > 24) {
            i = 24;
        }

        return i;
    }

    protected boolean canDespawn() {
        return false;
    }

    public void addTrackingPlayer(net.minecraft.entity.player.EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    public void removeTrackingPlayer(net.minecraft.entity.player.EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    public boolean isChild() {
        return this.dataManager.get(IS_CHILD) == 1;
    }

    public void setChild(boolean childZombie) {
        this.dataManager.set(IS_CHILD, Byte.valueOf((byte)(childZombie ? 1 : 0)));
        if (this.world != null && !this.world.isRemote) {
            IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            iattributeinstance.removeModifier(paceBoostModifierBaby);
            if (childZombie) {
                iattributeinstance.applyModifier(paceBoostModifierBaby);
            }
        }

        this.setChildSize(childZombie);
    }

    public boolean villagerIs() {
        return this.dataManager.get(VILLAGER_IS) == 1;
    }

    public void assignVillager(boolean villager) {
        this.dataManager.set(VILLAGER_IS, Byte.valueOf((byte)(villager ? 1 : 0)));
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        if (this.ticksExisted % 20 == 0) {
            if (this.world.isDaytime()) {
                this.heal(2.0F);
            } else {
                this.heal(20.0F);
            }
        }

        if (this.getAttackTarget() != null && !this.getAttackTarget().onGround) {
            this.getNavigator().clearPath();
        }

        if (this.motionX != 0.0D && this.motionZ != 0.0D) {
            int i = MathHelper.floor(this.posX);
            int j = MathHelper.floor(this.posY - 0.20000000298023224D - (double)this.getYOffset());
            int k = MathHelper.floor(this.posZ);
            BlockPos pos = new BlockPos(i, j, k);
            IBlockState state = this.world.getBlockState(pos);
            if (state.getMaterial() != Material.AIR) {
                this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.BLOCK_CRACK, this.posX + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, this.getEntityBoundingBox().minY + 0.1D, this.posZ + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, 4.0D * ((double)this.rand.nextFloat() - 0.5D), 0.5D, ((double)this.rand.nextFloat() - 0.5D) * 4.0D, new int[]{Block.getStateId(state)});
            }
        }

        if (this.rand.nextInt(40) == 0 && !this.world.isRemote && !(this instanceof EntityOgPigZombieTitan)) {
            double d1 = this.posX + (this.rand.nextDouble() * 2.0D - 1.0D) * 30.0D;
            double d3 = this.posZ + (this.rand.nextDouble() * 2.0D - 1.0D) * 30.0D;
            double d2 = (double)this.world.getHeight((int)d1, (int)d3) - 1.5D;
            EntityZombie summon = new EntityZombie(this.world);
            summon.copyLocationAndAnglesFrom(this);
            summon.setPosition(d1, d2, d3);
            this.world.spawnEntity(summon);
            summon.playSound(TitanAudio.get("titansummonminion"), 2.0F, 1.0F);
            summon.addVelocity(0.0D, 0.9D, 0.0D);
            summon.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(summon)), (IEntityLivingData)null);
        }
    }

    public boolean getCanSpawnHere() {
        return (this.rand.nextInt(500) == 0 || EventStore.getBool(this.world, "PostWither") && EventStore.getBool(this.world, "PostDragon")) && super.getCanSpawnHere();
    }

    public float getEyeHeight() {
        return 0.8625F * this.height;
    }

    public void fall(float distance, float damageMultiplier) {
        this.onGround = true;
        float[] ret = ForgeHooks.onLivingFall(this, distance, damageMultiplier);
        if (ret == null) return;
        distance = ret[0];
        if (distance > 0.0F) {
            PotionEffect potioneffect = this.getActivePotionEffect(MobEffects.JUMP_BOOST);
            float f1 = potioneffect != null ? (float)(potioneffect.getAmplifier() + 1) : 0.0F;
            int i = MathHelper.ceil(distance - 12.0F - f1);
            if (i > 0) {
                this.playSound(TitanAudio.get("funnyExplosion"), 20.0F, 1.0F);
                List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(48.0D, 4.0D, 48.0D));
                if (!list.isEmpty() && !this.world.isRemote) {
                    for (int j = 0; j < list.size(); ++j) {
                        Entity k = list.get(j);
                        if (k instanceof EntityLivingBase && !(k instanceof IDeity)) {
                            float l = 20000.0F - this.getDistance(k) * this.getDistance(k);
                            if (l <= 1.0F) {
                                l = 1.0F;
                            }

                            k.attackEntityFrom((new EntityDamageSource("explosion.player", this)).setDifficultyScaled().setExplosion(), l);
                            k.attackEntityFrom(DamageSource.causeExplosionDamage((net.minecraft.world.Explosion)null), l);
                            float block = (float)Math.atan2(k.posZ - this.posZ, k.posX - this.posX);
                            k.addVelocity(Math.cos((double)block) * 4.0D, (double)l * 1.0E-4D, Math.sin((double)block) * 4.0D);
                            if (k instanceof EntityCreeperMinion && ((EntityCreeperMinion)k).getHealth() <= 0.0F) {
                                ((EntityCreeperMinion)k).explode();
                            }
                        }
                    }
                }

            }
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        return this.getIsInvulnerable() ? false : (source.getTrueSource() == null ? false : super.attackEntityFrom(source, amount));
    }

    protected float getSoundVolume() {
        return 10.0F;
    }

    protected SoundEvent getAmbientSound() {
        return TitanAudio.get("titanZombieLiving");
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return TitanAudio.get("titanZombieGrunt");
    }

    protected SoundEvent getDeathSound() {
        return TitanAudio.get("titanZombieDeath");
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(TitanAudio.get("titanStep"), 10.0F, 1.0F);
    }

    protected Item getDropItem() {
        return Items.ROTTEN_FLESH;
    }

    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
        Item item = this.getDropItem();
        this.dropItem(item, 16);
        int j;
        int l;
        if (item != null) {
            j = this.rand.nextInt(63);
            for (l = 0; l < j; ++l) {
                this.dropItem(item, 1 + lootingModifier);
            }

            j = this.rand.nextInt(63);
            for (l = 0; l < j; ++l) {
                this.dropItem(Items.FEATHER, 1 + lootingModifier);
            }
        }

        j = 2 + this.rand.nextInt(8 + lootingModifier);
        for (l = 0; l < j; ++l) {
            this.dropItem(Items.EMERALD, 1);
        }

        j = 4 + this.rand.nextInt(16 + lootingModifier);
        for (l = 0; l < j; ++l) {
            this.dropItem(Items.DIAMOND, 1);
        }

        j = 1 + this.rand.nextInt(6 + lootingModifier);
        for (l = 0; l < j; ++l) {
            this.dropItem(TitanGear.ruby, 1);
        }

        j = 2 + this.rand.nextInt(8 + lootingModifier);
        for (l = 0; l < j; ++l) {
            this.dropItem(TitanGear.onyx, 1);
        }
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    protected void shedDropRare(int chance) {
        switch (this.rand.nextInt(3)) {
            case 0:
                this.dropItem(Items.IRON_INGOT, 1);
                break;
            case 1:
                this.dropItem(Items.CARROT, 1);
                break;
            case 2:
                this.dropItem(Items.POTATO, 1);
        }
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        if (this.rand.nextFloat() < (this.world.getDifficulty() == EnumDifficulty.HARD ? 0.05F : 0.01F)) {
            int i = this.rand.nextInt(3);
            if (i == 0) {
                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            } else {
                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
            }
        }
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("IsVillager", this.villagerIs());
        tagCompound.setBoolean("IsBaby", this.isChild());
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.assignVillager(tagCompund.getBoolean("IsVillager"));
        this.setChild(tagCompund.getBoolean("IsBaby"));
    }

    public void onKillEntity(EntityLivingBase entityLivingIn) {
        super.onKillEntity(entityLivingIn);
        if ((this.world.getDifficulty() == EnumDifficulty.NORMAL || this.world.getDifficulty() == EnumDifficulty.HARD) && entityLivingIn instanceof EntityVillager) {
            if (this.world.getDifficulty() != EnumDifficulty.HARD && this.rand.nextBoolean()) {
                return;
            }

            EntityZombieVillager entityzombie = new EntityZombieVillager(this.world);
            entityzombie.copyLocationAndAnglesFrom(entityLivingIn);
            this.world.removeEntity(entityLivingIn);
            entityzombie.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityzombie)), (IEntityLivingData)null);
            // villager profession transfer dropped — Forge's getProfessionForge/setForgeProfession bridge is fiddly across versions; default profession is fine for a stripped OG conversion.
            if (entityLivingIn.isChild()) {
                entityzombie.setChild(true);
            }

            this.world.spawnEntity(entityzombie);
            this.world.playEvent((EntityPlayer)null, 1016, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
        }
    }

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        Object obj = super.onInitialSpawn(difficulty, livingdata);
        float f = difficulty.getClampedAdditionalDifficulty();
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * f);
        if (obj == null) {
            obj = new EntityOgZombieTitan.GroupData(this.world.rand.nextFloat() < ForgeModContainer.zombieBabyChance, this.world.rand.nextFloat() < 0.05F, (Object)null);
        }

        if (obj instanceof EntityOgZombieTitan.GroupData) {
            EntityOgZombieTitan.GroupData groupdata = (EntityOgZombieTitan.GroupData)obj;
            if (groupdata.villagerIs) {
                this.assignVillager(true);
            }

            if (groupdata.isChild) {
                this.setChild(true);
            }
        }

        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);
        if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
            Calendar calendar = this.world.getCurrentDate();
            if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F) {
                this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
                this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0F;
            }
        }

        return (IEntityLivingData)obj;
    }

    public void setChildSize(boolean isChild) {
        this.multiplySize(isChild ? 0.5F : 1.0F);
    }

    protected final void setSize(float width, float height) {
        boolean flag = this.zombieWidth > 0.0F && this.zombieHeight > 0.0F;
        this.zombieWidth = width;
        this.zombieHeight = height;
        if (!flag) {
            this.multiplySize(1.0F);
        }
    }

    protected final void multiplySize(float size) {
        super.setSize(this.zombieWidth * size, this.zombieHeight * size);
    }

    static class GroupData implements IEntityLivingData {
        public boolean isChild;
        public boolean villagerIs;

        private GroupData(boolean p_i2348_2_, boolean p_i2348_3_) {
            this.isChild = false;
            this.villagerIs = false;
            this.isChild = p_i2348_2_;
            this.villagerIs = p_i2348_3_;
        }

        GroupData(boolean p_i2349_2_, boolean p_i2349_3_, Object p_i2349_4_) {
            this(p_i2349_2_, p_i2349_3_);
        }
    }
}
