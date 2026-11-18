package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EntityWitherTurretGround extends EntityGolem implements IRangedAttackMob {
    private static final DataParameter<Byte> PLAYER_CREATED = EntityDataManager.<Byte>createKey(EntityWitherTurretGround.class, DataSerializers.BYTE);
    public int tierDurability;
    public int tierFerocity;
    public int tierManiac;
    public int tierUnstability;
    public int tierShurakin;
    public int tierUnbreaking;
    public int clockShooting;
    public int shotsV = 20;

    public EntityWitherTurretGround(World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
        this.preventEntitySpawning = true;
        setSize(0.75F, 1.375F);
    }

    public void onKillCommand() {
        onDeathUpdate();
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }

    public float getBrightness() {
        return 1.0F;
    }

    protected void despawnEntity() {
    }

    public boolean isEnabledAI() {
        return true;
    }

    protected boolean allowMobVictim(EntityLivingBase entity) {
        if (isPlayerCreated()) {
            return (!(entity instanceof EntityTitanSpirit)) && (!(entity instanceof EntityPlayer)) && (!(entity instanceof EntityGolem)) && (!(entity instanceof EntityAgeable)) && (!(entity instanceof EntityIronGolemTitan)) && (!(entity instanceof EntitySnowGolemTitan)) && (!(entity instanceof EntityAnimal));
        }

        return (!(entity instanceof EntityTitanSpirit)) && ((entity instanceof EntityLivingBase)) && (entity.getCreatureAttribute() != EnumCreatureAttribute.UNDEAD);
    }

    public int getTotalArmorValue() {
        int i = 0 + this.tierUnbreaking * 6;
        if (i > 20) {
            i = 20;
        }

        return i;
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(400000.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(PLAYER_CREATED, Byte.valueOf((byte)0));
    }

    @SuppressWarnings("rawtypes")
    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        return (!isPlayerCreated()) || ((arg70686n1 != EntityPlayer.class) && (arg70686n1 != EntityAgeable.class) && (arg70686n1 != EntityGolem.class));
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if ((this.shotsV <= 0) && (this.clockShooting <= 10)) {
            this.shotsV = 20;
        }

        this.renderYawOffset = this.rotationYaw = this.rotationYawHead;
        this.onGround = true;
        this.isAirBorne = false;
        this.ignoreFrustumCheck = true;
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.ticksExisted % 20 == 0) {
            heal(1.0F + this.tierDurability);
        }

        if (!this.world.isRemote) {
            int i = MathHelper.floor(this.posX);
            int j = MathHelper.floor(this.posY - 1.0D);
            int k = MathHelper.floor(this.posZ);
            BlockPos pos = new BlockPos(i, j, k);
            if (this.world.getBlockState(pos).getBlock() != Blocks.BEDROCK) {
                this.world.setBlockState(pos, Blocks.BEDROCK.getDefaultState());
            }
        }

        double d8 = getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue();
        List<?> found11 = this.world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().grow(d8, d8, d8).offset(0.0D, -(d8 * 0.5D), 0.0D));
        if ((found11 != null) && (!found11.isEmpty()) && (getAttackTarget() == null)) {
            for (int i1 = 0; i1 < found11.size(); i1++) {
                Entity entity = (Entity)found11.get(i1);
                if (((entity instanceof EntityLivingBase)) && (entity != null) && (entity.isEntityAlive()) && (getAttackTarget() == null) && (allowMobVictim((EntityLivingBase)entity)) && (canEntityBeSeen(entity)) && (entity.posY <= this.posY + 6.0D)) {
                    setAttackTarget((EntityLivingBase)entity);
                }

                else {
                    found11.remove(entity);
                }
            }
        }

        if (getAttackTarget() != null) {
            if (!this.world.isRemote) {
                attackEntityWithRangedAttack(getAttackTarget(), 1.0F);
                attackEntityWithRangedAttack(getAttackTarget(), 1.0F);
            }

            getLookHelper().setLookPositionWithEntity(getAttackTarget(), 20.0F, 180.0F);
            if ((!getAttackTarget().isEntityAlive()) || (getAttackTarget().isDead) || (getAttackTarget().posY > this.posY + 8.0D) || (getAttackTarget().getDistanceSq(this) > 1024.0D) || (!canEntityBeSeen(getAttackTarget()))) {
                setAttackTarget(null);
            }

            if (!allowMobVictim(getAttackTarget())) {
                setAttackTarget(null);
            }
        }
    }

    public void onUpdate() {
        super.onUpdate();
        for (int j = 0; j < 3; j++) {
            double d10 = funcU82214(j);
            double d2 = funcV82208(j);
            double d4 = funcW82213(j);
            this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.SMOKE_NORMAL, d10 + this.rand.nextGaussian() * 0.30000001192092896D, d2 + this.rand.nextGaussian() * 0.30000001192092896D, d4 + this.rand.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D);
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        if (this.clockShooting >= 0) {
            this.clockShooting -= 1;
        }

        if (this.clockShooting <= 0) {
            this.clockShooting = 0;
        }
    }

    public void fall(float distance, float damageMultiplier) {
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        tagCompound.setBoolean("PlayerCreated", isPlayerCreated());
        tagCompound.setInteger("Ench1Level", this.tierDurability);
        tagCompound.setInteger("Ench2Level", this.tierFerocity);
        tagCompound.setInteger("Ench3Level", this.tierManiac);
        tagCompound.setInteger("Ench4Level", this.tierUnstability);
        tagCompound.setInteger("Ench5Level", this.tierShurakin);
        tagCompound.setInteger("Ench6Level", this.tierUnbreaking);
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        if (tagCompund.hasKey("Ench1Level", 99)) {
            this.tierDurability = tagCompund.getInteger("Ench1Level");
        }

        if (tagCompund.hasKey("Ench2Level", 99)) {
            this.tierFerocity = tagCompund.getInteger("Ench2Level");
        }

        if (tagCompund.hasKey("Ench3Level", 99)) {
            this.tierManiac = tagCompund.getInteger("Ench3Level");
        }

        if (tagCompund.hasKey("Ench4Level", 99)) {
            this.tierUnstability = tagCompund.getInteger("Ench4Level");
        }

        if (tagCompund.hasKey("Ench5Level", 99)) {
            this.tierShurakin = tagCompund.getInteger("Ench5Level");
        }

        if (tagCompund.hasKey("Ench6Level", 99)) {
            this.tierUnbreaking = tagCompund.getInteger("Ench6Level");
        }

        setPlayerCreated(tagCompund.getBoolean("PlayerCreated"));
    }

    public boolean isPlayerCreated() {
        return (this.dataManager.get(PLAYER_CREATED).byteValue() & 0x1) != 0;
    }

    public void setPlayerCreated(boolean arg70849n1) {
        byte b0 = this.dataManager.get(PLAYER_CREATED).byteValue();
        if (arg70849n1) {
            this.dataManager.set(PLAYER_CREATED, Byte.valueOf((byte)(b0 | 0x1)));
        }

        else {
            this.dataManager.set(PLAYER_CREATED, Byte.valueOf((byte)(b0 & 0xFFFFFFFE)));
        }
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
        return arg82208n1 <= 0 ? this.posY + 0.5D : this.posY + 1.15D;
    }

    private double funcW82213(int arg82213n1) {
        if (arg82213n1 <= 0) {
            return this.posZ;
        }

        float f = (this.renderYawOffset + 180 * (arg82213n1 - 1)) / 180.0F * 3.1415927F;
        float f1 = MathHelper.sin(f);
        return this.posZ + f1 * 1.2D;
    }

    private void launchWitherSkullToEntity(int arg82216n1, EntityLivingBase arg82216n2) {
        if ((arg82216n2.getDistanceSq(this) > 900.0D) || (arg82216n2.posY >= this.posY + 5.0D)) {
            launchWitherSkullToCoords(arg82216n1, arg82216n2.posX + (this.rand.nextDouble() * 2.0D - 1.0D), arg82216n2.posY + 0.5D, arg82216n2.posZ + (this.rand.nextDouble() * 2.0D - 1.0D), false);
        }

        else {
            launchWitherSkullToCoords(arg82216n1, arg82216n2.posX, arg82216n2.posY + 0.5D, arg82216n2.posZ, false);
        }
    }

    private void launchWitherSkullToCoords(int arg82209n1, double arg82209n2, double arg82209n4, double arg82209n6, boolean arg82209n8) {
        if (this.clockShooting <= 0) {
            SoundHandler.whenPlayMob("random.explode", this.world, this, 0.15F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 1.8F);
            SoundHandler.whenPlayMob("thetitans:turretShoot2", this.world, this, 3.0F, 1.0F);
            double d3 = funcU82214(1);
            double d4 = funcV82208(1);
            double d5 = funcW82213(1);
            double d6 = arg82209n2 - d3;
            double d7 = arg82209n4 - d4;
            double d8 = arg82209n6 - d5;
            EntityBulletWitherSkull entitywitherskull = new EntityBulletWitherSkull(this.world, this, d6, d7, d8);
            if (arg82209n8) {
                entitywitherskull.setInvulnerable(true);
            }

            entitywitherskull.harmExtra = (this.tierFerocity * 2);
            entitywitherskull.explosivePower = ((int)(this.tierUnstability * 0.75F));
            entitywitherskull.factorPace = (this.tierShurakin * 0.1F);
            entitywitherskull.posY = d4;
            entitywitherskull.posX = d3;
            entitywitherskull.posZ = d5;
            this.world.spawnEntity(entitywitherskull);
            SoundHandler.whenPlayMob("thetitans:turretShoot2", this.world, this, 6.0F, 1.0F);
            double d31 = funcU82214(2);
            double d41 = funcV82208(2);
            double d51 = funcW82213(2);
            double d61 = arg82209n2 - d31;
            double d71 = arg82209n4 - d41;
            double d81 = arg82209n6 - d51;
            EntityBulletWitherSkull entitywitherskull1 = new EntityBulletWitherSkull(this.world, this, d61, d71, d81);
            if (arg82209n8) {
                entitywitherskull1.setInvulnerable(true);
            }

            entitywitherskull1.harmExtra = (this.tierFerocity * 2);
            entitywitherskull1.explosivePower = ((int)(this.tierUnstability * 0.75F));
            entitywitherskull1.factorPace = (this.tierShurakin * 0.1F);
            entitywitherskull1.posY = d41;
            entitywitherskull1.posX = d31;
            entitywitherskull1.posZ = d51;
            this.world.spawnEntity(entitywitherskull1);
            this.shotsV -= 1;
            this.shotsV -= 1;
            if (this.shotsV > 0) {
                this.clockShooting = 2;
            }

            else {
                this.clockShooting = (14 - this.tierManiac * 4);
                for (int j = 0; j < 3; j++) {
                    double d10 = funcU82214(j);
                    double d2 = funcV82208(j);
                    double d24 = funcW82213(j);
                    this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.SMOKE_LARGE, d10 + this.rand.nextGaussian() * 0.30000001192092896D, d2 + this.rand.nextGaussian() * 0.30000001192092896D, d24 + this.rand.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D);
                }
            }

            int i = MathHelper.floor(this.posX);
            int j = MathHelper.floor(this.posY - 1.0D);
            int k = MathHelper.floor(this.posZ);
            this.world.setBlockState(new BlockPos(i, j, k), Blocks.GLOWSTONE.getDefaultState());
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source)) {
            return false;
        }

        if (((source.getTrueSource() instanceof EntityWitherTurretMortar)) || ((source.getTrueSource() instanceof EntityWitherTurretGround)) || ((source.getTrueSource() instanceof EntityWitherTurret))) {
            return false;
        }

        if ((source == DamageSource.ANVIL) || (source == DamageSource.GENERIC) || (source == DamageSource.IN_FIRE) || (source == DamageSource.LAVA) || (source == DamageSourceExtra.lightningBolt) || (source == DamageSource.ON_FIRE) || (source == DamageSource.MAGIC) || (source == DamageSource.WITHER) || (source == DamageSource.FALLING_BLOCK) || (source == DamageSource.DROWN) || (source == DamageSource.CACTUS)) {
            return false;
        }

        Entity entity = source.getTrueSource();
        if (((entity instanceof EntityLivingBase)) && (!isEntityInvulnerable(source))) {
            List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(100.0D, 100.0D, 100.0D));
            for (Object mobRef1Obj : list) {
                Entity mobRef1 = (Entity) mobRef1Obj;
                if ((mobRef1 instanceof EntityWitherTurretGround)) {
                    EntityWitherTurretGround entitypigzombie = (EntityWitherTurretGround)mobRef1;
                    entitypigzombie.setAttackTarget((EntityLivingBase)entity);
                    entitypigzombie.setRevengeTarget((EntityLivingBase)entity);
                }

                setAttackTarget((EntityLivingBase)entity);
                setRevengeTarget((EntityLivingBase)entity);
                this.clockShooting -= 4;
            }
        }

        return super.attackEntityFrom(source, amount);
    }

    public void attackEntityWithRangedAttack(EntityLivingBase arg82196n1, float arg82196n2) {
        launchWitherSkullToEntity(1, arg82196n1);
        getLookHelper().setLookPositionWithEntity(arg82196n1, 180.0F, 180.0F);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WITHER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_WITHER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return this.isPlayerCreated() ? SoundEvents.ENTITY_WITHER_HURT : SoundEvents.ENTITY_WITHER_DEATH;
    }

    protected void onDeathUpdate() {
        setDead();
        if ((!this.world.isRemote) && (this.world.getGameRules().getBoolean("doMobLoot") == true)) {
            if (this.isPlayerCreated()) {
                ItemStack turretGood = new ItemStack(TitanGear.turret2Good);
                if (this.tierDurability > 0)
                    turretGood.addEnchantment(TheTitans.enchant1Turret, this.tierDurability);
                if (this.tierFerocity > 0)
                    turretGood.addEnchantment(TheTitans.enchant2Turret, this.tierFerocity);
                if (this.tierManiac > 0)
                    turretGood.addEnchantment(TheTitans.enchant3Turret, this.tierManiac);
                if (this.tierUnstability > 0)
                    turretGood.addEnchantment(TheTitans.enchant4Turret, this.tierUnstability);
                if (this.tierShurakin > 0)
                    turretGood.addEnchantment(TheTitans.enchant5Turret, this.tierShurakin);
                if (this.tierUnbreaking > 0)
                    turretGood.addEnchantment(Enchantments.UNBREAKING, this.tierUnbreaking);
                this.entityDropItem(turretGood, 3.0F);
            }

            else {
                entityDropItem(new ItemStack(Items.SKULL, 1, 1), 6.0F);
                entityDropItem(new ItemStack(Items.SKULL, 1, 1), 3.0F);
            }

            int i = 200;
            while (i > 0) {
                int j = EntityXPOrb.getXPSplit(i);
                i -= j;
                this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY + 10.0D, this.posZ, j));
            }
        }

        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.posY - 1.0D);
        int k = MathHelper.floor(this.posZ);
        this.world.setBlockState(new BlockPos(i, j, k), Blocks.BEDROCK.getDefaultState());
        if (!this.world.isRemote) {
            SoundHandler.whenPlayMob("thetitans:turretDeath2", this.world, this, 6.0F, 1.0F);
            SoundHandler.whenPlayMob("thetitans:turretDeath2", this.world, this, 6.0F, 1.0F);
            SoundHandler.whenPlayMob("thetitans:turretDeath2", this.world, this, 6.0F, 1.0F);
            SoundHandler.whenPlayMob("thetitans:turretDeath2", this.world, this, 6.0F, 1.0F);
            SoundHandler.whenPlayMob("thetitans:turretDeath2", this.world, this, 6.0F, 1.0F);
            SoundHandler.whenPlayMob("thetitans:turretDeath2", this.world, this, 6.0F, 1.0F);
            SoundHandler.whenPlayMob("thetitans:turretDeath2", this.world, this, 6.0F, 1.0F);
            SoundHandler.whenPlayMob("thetitans:turretDeath2", this.world, this, 6.0F, 1.0F);
            this.world.createExplosion((Entity)null, this.posX, this.posY - 1.0D, this.posZ, 2.0F, true);
        }
    }

    public float getEyeHeight() {
        return 1.1F;
    }

    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return entityIn.getEntityBoundingBox();
    }

    public AxisAlignedBB getBoundingBox() {
        return this.getEntityBoundingBox();
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean canBePushed() {
        return false;
    }

    public boolean enchantedIs() {
        if ((this.tierDurability > 0) || (this.tierFerocity > 0) || (this.tierManiac > 0) || (this.tierUnstability > 0) || (this.tierShurakin > 0) || (this.tierUnbreaking > 0)) {
            return true;
        }

        return false;
    }

    public boolean needsEnchantsShow() {
        return enchantedIs();
    }

    protected void collideWithNearbyEntities() {
    }

    public void knockBack(Entity arg70653n1, float arg70653n2, double arg70653n3, double arg70653n5) {
    }

    public void addVelocity(double x, double y, double z) {
    }

    public void move(net.minecraft.entity.MoverType type, double x, double y, double z) {
    }

    protected int decreaseAirSupply(int arg70682n1) {
        return arg70682n1;
    }

    public void setSwingingArms(boolean swingingArms) {}
}
