package com.electrifiedded.thetitans.entity.mob;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.minion.EntityBlazeMinionFireball;
import com.electrifiedded.thetitans.entity.minion.IMinion;
import com.electrifiedded.thetitans.entity.titan.EntityFallingBlockTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanAudio;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import java.util.Calendar;

public class EntityMagicUser extends EntityMob implements IRangedAttackMob {
    private static final net.minecraft.network.datasync.DataParameter<Byte> CASTING_V = net.minecraft.network.datasync.EntityDataManager.createKey(EntityMagicUser.class, net.minecraft.network.datasync.DataSerializers.BYTE);
    private static final net.minecraft.network.datasync.DataParameter<Integer> KIND_MOB = net.minecraft.network.datasync.EntityDataManager.createKey(EntityMagicUser.class, net.minecraft.network.datasync.DataSerializers.VARINT);
    private int castingBeatTotalSpell;
    private int castKeySpell;
    private EntityAIAttackRanged aiArrowAttack = new EntityAIAttackRanged(this, 1.0D, 20, 60, 15.0F);
    private EntityAIAttackMelee aiAttackOnCollide = new EntityAIAttackMelee(this, 1.2D, false);
    private AIPassiveSpell passiveSpellsBrain = new AIPassiveSpell();
    private AIAttackSpell strikeSpellsBrain = new AIAttackSpell();
    private AICastingSpell castBrain = new AICastingSpell();

    public EntityMagicUser(World p_i1738_1_) {
        super(p_i1738_1_);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(5, new EntityAIWander(this, 0.75D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        // 1.12.2 EntityAINearestAttackableTarget is generic <T extends EntityLivingBase>, so it
        // cannot take the interface IMinion.class directly — target EntityLivingBase and filter by instanceof.
        this.targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 0, true, true, new com.google.common.base.Predicate<EntityLivingBase>() {
            public boolean apply(EntityLivingBase e) {
                return e instanceof IMinion;
            }
        }));
        this.targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, EntityTitan.class, 0, true, true, null));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, true, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, 0, true, true, null));
        if (p_i1738_1_ != null && !p_i1738_1_.isRemote) {
            this.setCombatTask();
        }
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(CASTING_V, Byte.valueOf((byte)0));
        this.dataManager.register(KIND_MOB, Integer.valueOf(0));
    }

    public boolean isSpellCasting() {
        return this.world.isRemote ? this.dataManager.get(CASTING_V).byteValue() > 0 : this.castingBeatTotalSpell > 0;
    }

    public void assignCastingSpellIs(int arg190753n1) {
        this.dataManager.set(CASTING_V, Byte.valueOf((byte)arg190753n1));
    }

    public int fetchKindMob() {
        return this.dataManager.get(KIND_MOB).intValue();
    }

    public void assignKindMob(int miniontype) {
        this.dataManager.set(KIND_MOB, Integer.valueOf(miniontype));
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        switch (this.fetchKindMob()) {
            case 1: {
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20D);
                    this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2D);
                    break;
                }

            case 2: {
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20D);
                    this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3D);
                    break;
                }

            case 3: {
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25D);
                    this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2D);
                    this.isImmuneToFire = true;
                    break;
                }

            case 4: {
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25D);
                    this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3D);
                    break;
                }

            case 5: {
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25D);
                    this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4D);
                    break;
                }

            case 6: {
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30D);
                    this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4D);
                    break;
                }

            case 7: {
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30D);
                    this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3D);
                    break;
                }

            case 8: {
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30D);
                    this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10D);
                    this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64D);
                    break;
                }

            case 9: {
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40D);
                    this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5D);
                    break;
                }

            case 10: {
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80D);
                    this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5D);
                    break;
                }

            case 11: {
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(800D);
                    this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5D);
                    break;
                }

            case 12: {
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(com.electrifiedded.thetitans.TheTitans.adjustValueToProgressionSystem(this.world, 1200.0D));
                    this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(com.electrifiedded.thetitans.TheTitans.adjustValueToProgressionSystem(this.world, 30.0D));
                    this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64D);
                    this.isImmuneToFire = true;
                    break;
                }

            default: {
                    this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20D);
                    this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4D);
                }
        }

        this.setCombatTask();
        this.setHealth(this.getMaxHealth());
    }

    private int fetchCastingTicksSpell() {
        return this.castingBeatTotalSpell;
    }

    protected void updateAITasks() {
        super.updateAITasks();
        if (this.castingBeatTotalSpell > 0) {
            --this.castingBeatTotalSpell;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote && this.isSpellCasting()) {
            int i = this.dataManager.get(CASTING_V).byteValue();
            double d0 = 0.7D;
            double d1 = 0.5D;
            double d2 = 0.2D;
            if (i == 2) {
                d0 = 0.4D;
                d1 = 0.3D;
                d2 = 0.35D;
            }

            else if (i == 1) {
                d0 = 0.7D;
                d1 = 0.7D;
                d2 = 0.8D;
            }

            float f = this.renderYawOffset * 0.017453292F + MathHelper.cos((float)this.ticksExisted * 0.6662F) * 0.25F;
            float f1 = MathHelper.cos(f);
            float f2 = MathHelper.sin(f);
            this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.SPELL_MOB, this.posX + (double)f1 * 0.6D, this.posY + 1.8D, this.posZ + (double)f2 * 0.6D, d0, d1, d2);
            this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.SPELL_MOB, this.posX - (double)f1 * 0.6D, this.posY + 1.8D, this.posZ - (double)f2 * 0.6D, d0, d1, d2);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.castingBeatTotalSpell = compound.getInteger("SpellTicks");
        this.assignKindMob(compound.getInteger("Type"));
        this.setCombatTask();
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("SpellTicks", this.castingBeatTotalSpell);
        compound.setInteger("Type", this.fetchKindMob());
    }

    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
        super.setItemStackToSlot(slotIn, stack);
        if (!this.world.isRemote && slotIn == EntityEquipmentSlot.MAINHAND) {
            this.setCombatTask();
        }
    }

    protected void appendArmourRandom() {
        int i = this.rand.nextInt(100);
        if (i == 0) {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TitanGear.swordSacred));
        }

        if (i > 0 && i <= 10) {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
        }

        if (i > 10 && i <= 30) {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        }

        if (i > 30 && i <= 50) {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TitanGear.swordBronze));
        }

        else {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
        }
    }

    public IEntityLivingData onInitialSpawn(net.minecraft.world.DifficultyInstance difficulty, IEntityLivingData arg110161n1) {
        Object p_110161_1_1 = super.onInitialSpawn(difficulty, arg110161n1);
        if (!this.world.isRemote) {
            this.assignKindMob(rand.nextInt(12));
        }

        if (this.fetchKindMob() == 0) {
            this.appendArmourRandom();
            this.equipmentEnchant();
        }

        if (this.fetchKindMob() == 1) {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        }

        if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
            Calendar calendar = this.world.getCurrentDate();
            if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F) {
                this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
                this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0F;
            }
        }

        return (IEntityLivingData)p_110161_1_1;
    }

    protected void equipmentEnchant() {
        float f = this.world.getDifficultyForLocation(new BlockPos(this)).getClampedAdditionalDifficulty();
        if (!this.getHeldItemMainhand().isEmpty()) {
            EnchantmentHelper.addRandomEnchantment(this.rand, this.getHeldItemMainhand(), (int)(10F + f * (float)this.rand.nextInt(10)), false);
        }
    }

    public void attackEntityWithRangedAttack(EntityLivingBase arg82196n1, float arg82196n2) {
        double d0 = arg82196n1.posX - this.posX;
        double d1 = arg82196n1.getEntityBoundingBox().minY + (double)(arg82196n1.height / 2.0F) - (this.posY + (double)(this.height / 2.0F));
        double d2 = arg82196n1.posZ - this.posZ;
        float f1 = MathHelper.sqrt(arg82196n2) * 0.5F;
        this.world.playEvent((EntityPlayer)null, 1009, new BlockPos(this), 0);
        EntityWitherSkull entitysmallfireball = new EntityWitherSkull(this.world, this, d0 + this.rand.nextGaussian() * (double)f1, d1, d2 + this.rand.nextGaussian() * (double)f1);
        entitysmallfireball.posY = this.posY + (double)(this.height / 2.0F) + 0.5D;
        this.world.spawnEntity(entitysmallfireball);
    }

    public void setCombatTask() {
        this.tasks.removeTask(this.aiAttackOnCollide);
        this.tasks.removeTask(this.aiArrowAttack);
        this.tasks.removeTask(this.strikeSpellsBrain);
        this.tasks.removeTask(this.passiveSpellsBrain);
        this.tasks.removeTask(this.castBrain);
        this.getHeldItemMainhand();
        if (this.fetchKindMob() == 1) {
            this.tasks.addTask(4, this.aiArrowAttack);
        }

        else if (this.fetchKindMob() == 0) {
            this.tasks.addTask(4, this.aiAttackOnCollide);
        }

        else {
            this.tasks.addTask(2, this.castBrain);
            this.tasks.addTask(3, this.strikeSpellsBrain);
            this.tasks.addTask(4, this.passiveSpellsBrain);
        }
    }

    public String getName() {
        switch (this.fetchKindMob()) {
            case 1:
                return I18n.translateToLocal("entity.MagicalArcher.name");
            case 2:
                return I18n.translateToLocal("entity.NoviceMage.name");
            case 3:
                return I18n.translateToLocal("entity.FireMage.name");
            case 4:
                return I18n.translateToLocal("entity.IceMage.name");
            case 5:
                return I18n.translateToLocal("entity.EarthMage.name");
            case 6:
                return I18n.translateToLocal("entity.ZombieFresh.name");
            case 7:
                return I18n.translateToLocal("entity.ZombieRotting.name");
            case 8:
                return I18n.translateToLocal("entity.ZombieBrat.name");
            case 9:
                return I18n.translateToLocal("entity.ZombieMutilated.name");
            case 10:
                return I18n.translateToLocal("entity.ZombieHeadless.name");
            case 11:
                return I18n.translateToLocal("entity.ZombieGhoul.name");
            case 12:
                return I18n.translateToLocal("entity.ZombieWight.name");
            default:
                return I18n.translateToLocal("entity.MagicalGrunt.name");
        }
    }

    class AIAttackSpell extends EntityMagicUser.AIUseSpell {
        private AIAttackSpell() {
        }

        public boolean shouldExecute() {
            if (!super.shouldExecute()) {
                return false;
            }

            else if (EntityMagicUser.this.getAttackTarget() == null) {
                return false;
            }

            else if (EntityMagicUser.this.fetchKindMob() == 9) {
                return false;
            }

            else {
                return EntityMagicUser.this.rand.nextBoolean();
            }
        }

        protected int getCastingTime() {
            return EntityMagicUser.this.fetchKindMob() == 12 ? 20 : 40;
        }

        protected int getCastingInterval() {
            return EntityMagicUser.this.fetchKindMob() == 12 ? 10 : 40;
        }

        protected void castSpell() {
            EntityLivingBase entitylivingbase = EntityMagicUser.this.getAttackTarget();
            if (!entitylivingbase.world.isRemote && (entitylivingbase != null) && entitylivingbase.isEntityAlive()) {
                switch (EntityMagicUser.this.fetchKindMob() == 12 ? EntityMagicUser.this.rand.nextInt(12) : EntityMagicUser.this.fetchKindMob()) {
                    case 3: {
                            int i = MathHelper.floor(entitylivingbase.getEntityBoundingBox().minX - (1 + rand.nextInt(2)));
                            int j = MathHelper.floor(entitylivingbase.getEntityBoundingBox().minY - (1 + rand.nextInt(2)));
                            int k = MathHelper.floor(entitylivingbase.getEntityBoundingBox().minZ - (1 + rand.nextInt(2)));
                            int l = MathHelper.floor(entitylivingbase.getEntityBoundingBox().maxX + (1 + rand.nextInt(2)));
                            int i1 = MathHelper.floor(entitylivingbase.getEntityBoundingBox().maxY + (1 + rand.nextInt(2)));
                            int j1 = MathHelper.floor(entitylivingbase.getEntityBoundingBox().maxZ + (1 + rand.nextInt(2)));
                            for (int k1 = i; k1 <= l; k1++) {
                                for (int l1 = j; l1 <= i1; l1++) {
                                    for (int i2 = k; i2 <= j1; i2++) {
                                        BlockPos pos = new BlockPos(k1, l1, i2);
                                        IBlockState state = EntityMagicUser.this.world.getBlockState(pos);
                                        if (EntityMagicUser.this.world.isAreaLoaded(pos, 0) && !state.getBlock().isAir(state, EntityMagicUser.this.world, pos)) {
                                            if (EntityMagicUser.this.rand.nextInt(2) == 0 && state.getBlock() == Blocks.AIR) {
                                                EntityMagicUser.this.world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 3);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    case 4: {
                            EntityMagicUser.this.playSound(EntityMagicUser.this.explodeAudio(), 0.5F, 1.0F + (EntityMagicUser.this.rand.nextFloat() - EntityMagicUser.this.rand.nextFloat()) * 0.5F);
                            for (int i = 0; i < 5 + EntityMagicUser.this.world.rand.nextInt(5); i++) {
                                int x = EntityMagicUser.this.world.rand.nextInt(3);
                                if (EntityMagicUser.this.world.rand.nextInt(2) == 1) {
                                    x = -x;
                                }

                                int y = EntityMagicUser.this.world.rand.nextInt(3);
                                if (EntityMagicUser.this.world.rand.nextInt(2) == 1) {
                                    y = -y;
                                }

                                int z = EntityMagicUser.this.world.rand.nextInt(3);
                                if (EntityMagicUser.this.world.rand.nextInt(2) == 1) {
                                    z = -z;
                                }

                                x = (int)(x + entitylivingbase.posX);
                                y = (int)(y + entitylivingbase.posY);
                                z = (int)(z + entitylivingbase.posZ);
                                BlockPos pos = new BlockPos(x, y, z);
                                IBlockState state = EntityMagicUser.this.world.getBlockState(pos);
                                if (!state.isOpaqueCube() && state.getBlock().getBlockHardness(state, EntityMagicUser.this.world, pos) != -1) {
                                    EntityMagicUser.this.world.setBlockState(pos, Blocks.ICE.getDefaultState());
                                    EntityFallingBlockTitan entityfallingblock = new EntityFallingBlockTitan(EntityMagicUser.this.world, x + 0.5D, y + 0.5D, z + 0.5D, Blocks.ICE, 0);
                                    entityfallingblock.setPosition(x + 0.5D, y + 0.5D, z + 0.5D);
                                    entityfallingblock.addVelocity(0D, 0.5D, 0D);
                                    EntityMagicUser.this.world.spawnEntity(entityfallingblock);
                                    EntityMagicUser.this.world.setBlockToAir(pos);
                                }
                            }

                            EntityMagicUser.this.world.createExplosion(EntityMagicUser.this, entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, EntityMagicUser.this.fetchKindMob() == 12 ? 3F : 1F, false);
                            break;
                        }

                    case 5: {
                            int i = MathHelper.floor(entitylivingbase.getEntityBoundingBox().minX - (1 + rand.nextInt(2)));
                            int j = MathHelper.floor(entitylivingbase.getEntityBoundingBox().minY - 1);
                            int k = MathHelper.floor(entitylivingbase.getEntityBoundingBox().minZ - (1 + rand.nextInt(2)));
                            int l = MathHelper.floor(entitylivingbase.getEntityBoundingBox().maxX + (1 + rand.nextInt(2)));
                            int i1 = MathHelper.floor(entitylivingbase.getEntityBoundingBox().maxY + 1);
                            int j1 = MathHelper.floor(entitylivingbase.getEntityBoundingBox().maxZ + (1 + rand.nextInt(2)));
                            for (int k1 = i; k1 <= l; k1++) {
                                for (int l1 = j; l1 <= i1; l1++) {
                                    for (int i2 = k; i2 <= j1; i2++) {
                                        BlockPos pos = new BlockPos(k1, l1, i2);
                                        IBlockState state = EntityMagicUser.this.world.getBlockState(pos);
                                        Block block = state.getBlock();
                                        if (EntityMagicUser.this.world.isAreaLoaded(pos, 0) && !block.isAir(state, EntityMagicUser.this.world, pos)) {
                                            if (block.getBlockHardness(state, EntityMagicUser.this.world, pos) != -1F) {
                                                if (state.getMaterial().isLiquid() || block == Blocks.FIRE || block == Blocks.WEB) {
                                                    EntityMagicUser.this.world.setBlockToAir(pos);
                                                }

                                                else {
                                                    EntityFallingBlockTitan entityfallingblock = new EntityFallingBlockTitan(EntityMagicUser.this.world, k1 + 0.5D, l1 + 0.5D, i2 + 0.5D, block, block.getMetaFromState(state));
                                                    entityfallingblock.setPosition(k1 + 0.5D, l1 + 0.5D, i2 + 0.5D);
                                                    entityfallingblock.addVelocity(0D, 1D, 0D);
                                                    EntityMagicUser.this.world.spawnEntity(entityfallingblock);
                                                    EntityMagicUser.this.world.setBlockToAir(pos);
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            break;
                        }

                    case 6: {
                            entitylivingbase.attackEntityFrom(DamageSourceExtra.lightningBolt.setMagicDamage(), 25F);
                            EntityMagicUser.this.world.addWeatherEffect(new EntityLightningBolt(EntityMagicUser.this.world, entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, false));
                            break;
                        }

                    case 12: {
                            EntityMagicUser.this.playSound(net.minecraft.init.SoundEvents.ENTITY_SKELETON_DEATH, 1.0F, 0.5F);
                            EntityMagicUser.this.world.playSound(null, entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, EntityMagicUser.this.explodeAudio(), EntityMagicUser.this.getSoundCategory(), 4.0F, (1.0F + (entitylivingbase.world.rand.nextFloat() - entitylivingbase.world.rand.nextFloat()) * 0.2F) * 0.7F);
                            for (int i1 = 0; i1 < 256; i1++) {
                                EntityItem entityitem = entitylivingbase.entityDropItem(new ItemStack(Items.BONE, 1), 0.0F);
                                if (entityitem != null) {
                                    entityitem.motionY = 1.0D;
                                    entityitem.setPickupDelay(6000);
                                    entityitem.lifespan = (40 + EntityMagicUser.this.rand.nextInt(20));
                                }
                            }

                            entitylivingbase.addPotionEffect(new PotionEffect(MobEffects.WITHER, 400, 3));
                            entitylivingbase.attackEntityFrom(DamageSource.WITHER, 20.0F);
                            entitylivingbase.hurtResistantTime = 1;
                            break;
                        }

                    default: {
                            double d0 = entitylivingbase.posX - EntityMagicUser.this.posX;
                            double d1 = entitylivingbase.getEntityBoundingBox().minY + (double)(entitylivingbase.height / 2.0F) - (EntityMagicUser.this.posY + (double)(EntityMagicUser.this.height / 2.0F));
                            double d2 = entitylivingbase.posZ - EntityMagicUser.this.posZ;
                            float f1 = MathHelper.sqrt(1F) * 0.5F;
                            EntityMagicUser.this.world.playEvent((EntityPlayer)null, 1009, new BlockPos(EntityMagicUser.this), 0);
                            EntityBlazeMinionFireball entitysmallfireball = new EntityBlazeMinionFireball(EntityMagicUser.this.world, EntityMagicUser.this, d0 + EntityMagicUser.this.rand.nextGaussian() * (double)f1, d1, d2 + EntityMagicUser.this.rand.nextGaussian() * (double)f1);
                            entitysmallfireball.posY = EntityMagicUser.this.posY + (double)(EntityMagicUser.this.height / 2.0F) + 0.5D;
                            EntityMagicUser.this.world.spawnEntity(entitysmallfireball);
                        }
                }
            }
        }

        protected String getSpellPrepareSound() {
            return "thetitans:castAttack";
        }

        protected int fetchKeySpell() {
            switch (EntityMagicUser.this.fetchKindMob()) {
                default: {
                        return 2;
                    }
            }
        }
    }

    class AIPassiveSpell extends EntityMagicUser.AIUseSpell {
        EntityLivingBase target;
        private AIPassiveSpell() {
        }

        public boolean shouldExecute() {
            if (!super.shouldExecute()) {
                return false;
            }

            else if (EntityMagicUser.this.getAttackTarget() == null) {
                return false;
            }

            else if (EntityMagicUser.this.fetchKindMob() != 9) {
                return false;
            }

            else {
                return true;
            }
        }

        protected int getCastingTime() {
            return EntityMagicUser.this.fetchKindMob() == 12 ? 20 : 40;
        }

        protected int getCastingInterval() {
            return EntityMagicUser.this.fetchKindMob() == 12 ? 10 : 40;
        }

        protected void castSpell() {
            EntityLivingBase entitylivingbase = EntityMagicUser.this.getAttackTarget();
            if (!entitylivingbase.world.isRemote && (entitylivingbase != null) && entitylivingbase.isEntityAlive()) {
                switch (EntityMagicUser.this.fetchKindMob()) {
                    case 12: {
                            break;
                        }

                    default: {
                        }
                }
            }
        }

        protected String getSpellPrepareSound() {
            return "thetitans:castPassive";
        }

        protected int fetchKeySpell() {
            switch (EntityMagicUser.this.fetchKindMob()) {
                default: {
                        return 1;
                    }
            }
        }
    }

    class AICastingSpell extends EntityAIBase {
        public AICastingSpell() {
            this.setMutexBits(3);
        }

        public boolean shouldExecute() {
            return EntityMagicUser.this.fetchCastingTicksSpell() > 0;
        }

        public void startExecuting() {
            super.startExecuting();
            EntityMagicUser.this.assignCastingSpellIs(EntityMagicUser.this.castKeySpell);
            EntityMagicUser.this.getNavigator().clearPath();
        }

        public void resetTask() {
            super.resetTask();
            EntityMagicUser.this.assignCastingSpellIs(0);
        }

        public void updateTask() {
            if (EntityMagicUser.this.getAttackTarget() != null) {
                EntityMagicUser.this.getLookHelper().setLookPositionWithEntity(EntityMagicUser.this.getAttackTarget(), 10F, (float)EntityMagicUser.this.getVerticalFaceSpeed());
            }
        }
    }

    abstract class AIUseSpell extends EntityAIBase {
        protected int spellWarmup;
        protected int castTicksNext;
        private AIUseSpell() {
        }

        public boolean shouldExecute() {
            return EntityMagicUser.this.getAttackTarget() == null ? false : (EntityMagicUser.this.isSpellCasting() ? false : EntityMagicUser.this.ticksExisted >= this.castTicksNext);
        }

        public boolean shouldContinueExecuting() {
            return EntityMagicUser.this.getAttackTarget() != null && this.spellWarmup > 0;
        }

        public void startExecuting() {
            this.spellWarmup = this.getCastWarmupTime();
            EntityMagicUser.this.castingBeatTotalSpell = this.getCastingTime();
            this.castTicksNext = EntityMagicUser.this.ticksExisted + this.getCastingInterval();
            // resolve registered SoundEvent (TitanAudio); getSpellPrepareSound returns "thetitans:<key>".
            net.minecraft.util.SoundEvent prep = com.electrifiedded.thetitans.registry.TitanAudio.get(this.getSpellPrepareSound().replace("thetitans:", ""));
            if (prep != null)
                EntityMagicUser.this.playSound(prep, 1.0F, 1.0F);
            EntityMagicUser.this.castKeySpell = this.fetchKeySpell();
        }

        public void updateTask() {
            --this.spellWarmup;
            if (this.spellWarmup == 0) {
                this.castSpell();
                switch (EntityMagicUser.this.fetchKindMob()) {
                    case 3: {
                            EntityMagicUser.this.world.playEvent(2001, new BlockPos((int)EntityMagicUser.this.posX, (int)EntityMagicUser.this.posY + 1, (int)EntityMagicUser.this.posZ), Block.getIdFromBlock(Blocks.FIRE));
                            EntityMagicUser.this.world.playEvent((EntityPlayer)null, 1008, new BlockPos(EntityMagicUser.this), 0);
                            break;
                        }

                    case 4: {
                            EntityMagicUser.this.world.playEvent(2001, new BlockPos((int)EntityMagicUser.this.posX, (int)EntityMagicUser.this.posY + 1, (int)EntityMagicUser.this.posZ), Block.getIdFromBlock(Blocks.ICE));
                            break;
                        }

                    case 5: {
                            EntityMagicUser.this.world.playEvent(2001, new BlockPos((int)EntityMagicUser.this.posX, (int)EntityMagicUser.this.posY + 1, (int)EntityMagicUser.this.posZ), Block.getIdFromBlock(Blocks.DIRT));
                            break;
                        }

                    case 6: {
                            EntityMagicUser.this.world.playSound(null, EntityMagicUser.this.posX, EntityMagicUser.this.posY, EntityMagicUser.this.posZ, net.minecraft.init.SoundEvents.ENTITY_LIGHTNING_THUNDER, EntityMagicUser.this.getSoundCategory(), 10000.0F, 0.8F + EntityMagicUser.this.rand.nextFloat() * 0.2F);
                            EntityMagicUser.this.world.playSound(null, EntityMagicUser.this.posX, EntityMagicUser.this.posY, EntityMagicUser.this.posZ, EntityMagicUser.this.explodeAudio(), EntityMagicUser.this.getSoundCategory(), 2.0F, 0.5F + EntityMagicUser.this.rand.nextFloat() * 0.2F);
                            break;
                        }

                    default: {
                            EntityMagicUser.this.playSound(com.electrifiedded.thetitans.registry.TitanAudio.get("cast"), 1.0F, 1.0F);
                        }
                }
            }
        }

        protected abstract void castSpell();
        protected int getCastWarmupTime() {
            return 20;
        }

        protected abstract int getCastingTime();
        protected abstract int getCastingInterval();
        protected abstract String getSpellPrepareSound();
        protected abstract int fetchKeySpell();
    }

    // helper — "random.explode" is now SoundEvents.ENTITY_GENERIC_EXPLODE
    private SoundEvent explodeAudio() {
        return net.minecraft.init.SoundEvents.ENTITY_GENERIC_EXPLODE;
    }

    public void setSwingingArms(boolean swingingArms) {}
}
