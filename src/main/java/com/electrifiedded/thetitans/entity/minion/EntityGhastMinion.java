package com.electrifiedded.thetitans.entity.minion;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.titan.EntityGhastTitan;
import com.electrifiedded.thetitans.entity.titan.EntityHarcadiumArrow;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityXPBomb;
import com.electrifiedded.thetitans.entity.titan.ITitan;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAIHurtByTargetTitan;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;
import com.electrifiedded.thetitans.proxy.ClientProxy;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanAudio;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EntityGhastMinion extends EntityCreature implements IMob, IRangedAttackMob, ITemplar {
    private static final DataParameter<Byte> ATTACKING = EntityDataManager.createKey(EntityGhastMinion.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> KIND_SERVANT = EntityDataManager.createKey(EntityGhastMinion.class, DataSerializers.VARINT);
    public EntityLiving master;
    public int courseChangeCooldown;
    public double xWaypoint;
    public double yWaypoint;
    public double zWaypoint;
    private Entity targetedEntity;
    private int rechargeAggro;
    public int strikeCounterPrev;
    public int counterStrike;
    protected int explosionStrength;
    public EntityLiving toHealMob;
    private int patternStrike;
    private EntityAIAttackRanged aiArrowAttack = new EntityAIAttackRanged(this, 1.0D, 5, 64.0F);

    public EntityGhastMinion(World worldIn) {
        super(worldIn);
        explosionStrength = 1;
        setSize(4.5F, 4.5F);
        this.isImmuneToFire = true;
        this.experienceValue = 20;
        this.tasks.addTask(0, new EntityAIFindEntityNearestInjuredAlly(this));
        this.targetTasks.addTask(0, new EntityAIHurtByTargetTitan(this, true, new Class[] {
            EntityGhastMinion.class, EntityGhastTitan.class
        }
        ));
        if (TheTitans.ffaModeTitans)
            this.targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.giantSorterGhast));
        else
            this.targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.forAThingToKillSearch));
    }

    public void setDead() {
        super.setDead();
        if ((this.master != null) && ((this.master instanceof EntityTitan))) {
            ((EntityTitan)this.master).retractNumFromKindMinion(fetchKindMinion());
        }
    }

    @SuppressWarnings("rawtypes")
    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        return (arg70686n1 != EntityGhastMinion.class) && (arg70686n1 != EntityGhastTitan.class);
    }

    public String getName() {
        switch (this.fetchKindIntMinion()) {
            case 1:
                return net.minecraft.util.text.translation.I18n.translateToLocal("entity.GhastPriest.name");
            case 2:
                return net.minecraft.util.text.translation.I18n.translateToLocal("entity.GhastZealot.name");
            case 3:
                return net.minecraft.util.text.translation.I18n.translateToLocal("entity.GhastBishop.name");
            case 4:
                return net.minecraft.util.text.translation.I18n.translateToLocal("entity.GhastTemplar.name");
            default:
                return net.minecraft.util.text.translation.I18n.translateToLocal("entity.GhastLoyalist.name");
        }
    }

    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
        // cosmetic ranged-attack arm swing; minion has no dedicated animation flag, no-op
    }

    protected void updateFallState(double y, boolean onGround, net.minecraft.block.state.IBlockState state, BlockPos pos) {
    }

    public boolean attackEntityAsMob(Entity arg70652n1) {
        float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int i = 0;
        if (arg70652n1 instanceof EntityLivingBase) {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)arg70652n1).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }

        boolean flag = arg70652n1.attackEntityFrom(DamageSource.causeMobDamage(this), f);
        if (flag) {
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

            if (i > 0) {
                arg70652n1.addVelocity((double)(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) * (float)i * 0.5F));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);
            if (j > 0) {
                arg70652n1.setFire(j * 4);
            }

            if (arg70652n1 instanceof EntityLivingBase) {
                EnchantmentHelper.applyThornEnchantments((EntityLivingBase)arg70652n1, this);
            }

            EnchantmentHelper.applyArthropodEnchantments(this, arg70652n1);
        }

        return flag;
    }

    public void travel(float arg70612n1, float arg70612n2, float arg70612n3) {
        if (isInWater()) {
            moveRelative(arg70612n1, arg70612n2, arg70612n3, 0.02F);
            move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= 0.800000011920929D;
        }

        else if (isInLava()) {
            moveRelative(arg70612n1, arg70612n2, arg70612n3, 0.02F);
            move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
        }

        else {
            float f2 = 0.91F;
            if (this.onGround) {
                f2 = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.91F;
            }

            float f3 = 0.16277136F / (f2 * f2 * f2);
            moveRelative(arg70612n1, arg70612n2, arg70612n3, this.onGround ? 0.1F * f3 : 0.02F);
            f2 = 0.91F;
            if (this.onGround) {
                f2 = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.91F;
            }

            move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= f2;
            this.motionY *= f2;
            this.motionZ *= f2;
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d1 = this.posX - this.prevPosX;
        double d0 = this.posZ - this.prevPosZ;
        float f4 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;
        if (f4 > 1.0F) {
            f4 = 1.0F;
        }

        this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    public boolean isOnLadder() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean funcBF110182() {
        return this.dataManager.get(ATTACKING).byteValue() != 0;
    }

    public void funcA175454(boolean arg175454n1) {
        this.dataManager.set(ATTACKING, Byte.valueOf((byte)(arg175454n1 ? 1 : 0)));
    }

    public int funcCd175453() {
        return this.explosionStrength;
    }

    public void onUpdate() {
        super.onUpdate();
        if (getAttackTarget() != null) {
            getLookHelper().setLookPosition(getAttackTarget().posX, getAttackTarget().posY + getAttackTarget().getEyeHeight(), getAttackTarget().posZ, 180.0F, 180.0F);
        }
    }

    protected void updateAITasks() {
        if ((this.rand.nextInt(60) == 0) && (getAttackTarget() != null)) {
            setCombatTask();
            if (!this.onGround) {
                this.patternStrike = 0;
            }

            else {
                this.patternStrike = 1;
            }
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
            List<?> list = this.world.getEntitiesWithinAABB(EntityGhastTitan.class, this.getEntityBoundingBox().grow(100.0D, 100.0D, 100.0D));
            if ((list != null) && (!list.isEmpty())) {
                for (int i1 = 0; i1 < list.size(); i1++) {
                    Entity entity = (Entity)list.get(i1);
                    if ((entity != null) && ((entity instanceof EntityGhastTitan))) {
                        this.master = ((EntityGhastTitan)entity);
                    }
                }
            }
        }

        if ((getAttackTarget() != null) && (!canEntityBeSeen(getAttackTarget())) && (this.rand.nextInt(150) == 0)) {
            setAttackTarget(null);
        }

        if ((!this.world.isRemote) && (this.world.getDifficulty() == EnumDifficulty.PEACEFUL)) {
            setDead();
        }

        despawnEntity();
        this.strikeCounterPrev = this.counterStrike;
        double d0 = this.xWaypoint - this.posX;
        double d1 = this.yWaypoint - this.posY;
        double d2 = this.zWaypoint - this.posZ;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;
        if ((d3 < 1.0D) || (d3 > 10000.0D)) {
            if (this.master != null) {
                double extra = 0.0D;
                if ((getAttackTarget() != null) && (getAttackTarget().height > 4.0F)) {
                    extra = 64.0D;
                }

                double x = this.master.posX;
                double y = this.master.posY + 32.0D + extra;
                double z = this.master.posZ;
                d0 += this.rand.nextDouble() * 96D - 48D;
                d1 += this.rand.nextDouble() * 96D - 48D;
                d2 += this.rand.nextDouble() * 96D - 48D;
                this.xWaypoint = (x + d0);
                this.yWaypoint = (y + d1);
                this.zWaypoint = (z + d2);
            }

            else if (getAttackTarget() != null) {
                this.xWaypoint = getAttackTarget().posX + ((this.rand.nextDouble() * 2.0D - 1.0D) * 16D);
                this.yWaypoint = getAttackTarget().posY + 32D + ((this.rand.nextDouble() * 2.0D - 1.0D) * 16D);
                this.zWaypoint = getAttackTarget().posZ + ((this.rand.nextDouble() * 2.0D - 1.0D) * 16D);
            }

            else {
                EntityPlayer player = this.world.getClosestPlayerToEntity(this, 512D);
                if (player != null) {
                    this.xWaypoint = player.posX + ((this.rand.nextDouble() * 2.0D - 1.0D) * 16D);
                    this.yWaypoint = player.posY + 32D + ((this.rand.nextDouble() * 2.0D - 1.0D) * 16D);
                    this.zWaypoint = player.posZ + ((this.rand.nextDouble() * 2.0D - 1.0D) * 16D);
                }

                else {
                    this.xWaypoint = this.posX + ((this.rand.nextDouble() * 2.0D - 1.0D) * 16D);
                    this.yWaypoint = this.posY + ((this.rand.nextDouble() * 2.0D - 1.0D) * 16D);
                    this.zWaypoint = this.posZ + ((this.rand.nextDouble() * 2.0D - 1.0D) * 16D);
                }
            }
        }

        if (this.courseChangeCooldown-- <= 0) {
            this.courseChangeCooldown += this.rand.nextInt(5) + 2;
            d3 = MathHelper.sqrt(d3);
            if (isTraversableCourse(this.xWaypoint, this.yWaypoint, this.zWaypoint, d3)) {
                if ((this.master != null) && (getDistanceSq(this.master) > 4096D)) {
                    this.motionX += d0 / d3 * (this.fetchKindMinion() == EnumMinionType.ZEALOT ? 0.8D : 0.2D);
                    this.motionY += d1 / d3 * (this.fetchKindMinion() == EnumMinionType.ZEALOT ? 0.8D : 0.2D);
                    this.motionZ += d2 / d3 * (this.fetchKindMinion() == EnumMinionType.ZEALOT ? 0.8D : 0.2D);
                }

                else {
                    this.motionX += d0 / d3 * (this.fetchKindMinion() == EnumMinionType.ZEALOT ? 0.4D : 0.1D);
                    this.motionY += d1 / d3 * (this.fetchKindMinion() == EnumMinionType.ZEALOT ? 0.4D : 0.1D);
                    this.motionZ += d2 / d3 * (this.fetchKindMinion() == EnumMinionType.ZEALOT ? 0.4D : 0.1D);
                }
            }

            else {
                this.xWaypoint = this.posX;
                this.yWaypoint = this.posY;
                this.zWaypoint = this.posZ;
            }
        }

        if ((this.targetedEntity != null) && (this.targetedEntity.isDead)) {
            this.targetedEntity = null;
        }

        if ((this.targetedEntity == null) || (this.rechargeAggro-- <= 0)) {
            this.targetedEntity = getAttackTarget();
            if (this.targetedEntity != null) {
                this.rechargeAggro = 20;
            }
        }

        double d4 = 100.0D;
        if ((this.targetedEntity != null) && (this.targetedEntity.getDistanceSq(this) < d4 * d4)) {
            double d8 = 2D;
            Vec3d vec3 = getLook(1.0F);
            double d5 = (this.targetedEntity.posX + targetedEntity.motionX) - (this.posX + vec3.x * d8);
            double d6 = (this.targetedEntity.posY + 1D) - (this.posY + vec3.y * d8 + 1D);
            double d7 = (this.targetedEntity.posZ + targetedEntity.motionZ) - (this.posZ + vec3.z * d8);
            this.renderYawOffset = (this.rotationYaw = -(float)Math.atan2(d5, d7) * 180.0F / 3.1415927F);
            if (canEntityBeSeen(this.targetedEntity)) {
                if (this.counterStrike == 10) {
                    this.world.playEvent((EntityPlayer)null, 1015, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
                }

                this.counterStrike += 1;
                if (this.counterStrike > 20 && this.fetchKindMinion() == EnumMinionType.TEMPLAR) {
                    this.world.playEvent((EntityPlayer)null, 1016, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
                    EntityGhastMinionFireball entitylargefireball = new EntityGhastMinionFireball(this.world, this, d5, d6, d7);
                    entitylargefireball.explosionPower = this.explosionStrength;
                    entitylargefireball.posX = (this.posX + vec3.x * d8);
                    entitylargefireball.posY = (this.posY + vec3.y * d8 + 1D);
                    entitylargefireball.posZ = (this.posZ + vec3.z * d8);
                    this.world.spawnEntity(entitylargefireball);
                }

                if (this.counterStrike >= (this.fetchKindMinion() == EnumMinionType.TEMPLAR ? 40 : 20)) {
                    this.world.playEvent((EntityPlayer)null, 1016, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
                    EntityGhastMinionFireball entitylargefireball = new EntityGhastMinionFireball(this.world, this, d5, d6, d7);
                    entitylargefireball.explosionPower = this.explosionStrength;
                    entitylargefireball.posX = (this.posX + vec3.x * d8);
                    entitylargefireball.posY = (this.posY + vec3.y * d8 + 1D);
                    entitylargefireball.posZ = (this.posZ + vec3.z * d8);
                    this.world.spawnEntity(entitylargefireball);
                    this.counterStrike = this.fetchKindMinion() == EnumMinionType.ZEALOT ? 0 : -40;
                }
            }

            else if (this.counterStrike > 0) {
                this.counterStrike -= 1;
            }
        }

        else {
            this.renderYawOffset = (this.rotationYaw = -(float)Math.atan2(this.motionX, this.motionZ) * 180.0F / 3.1415927F);
            if (this.counterStrike > 0) {
                this.counterStrike -= 1;
            }
        }

        if (!this.world.isRemote) {
            byte b1 = this.dataManager.get(ATTACKING).byteValue();
            byte b0 = (byte)(this.counterStrike > 10 ? 1 : 0);
            if (b1 != b0) {
                this.dataManager.set(ATTACKING, Byte.valueOf(b0));
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
                    this.world.playBroadcastSound(1018, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
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

    private boolean isTraversableCourse(double arg70790n1, double arg70790n3, double arg70790n5, double arg70790n7) {
        double d4 = (this.xWaypoint - this.posX) / arg70790n7;
        double d5 = (this.yWaypoint - this.posY) / arg70790n7;
        double d6 = (this.zWaypoint - this.posZ) / arg70790n7;
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        for (int i = 1; i < arg70790n7; i++) {
            axisalignedbb = axisalignedbb.offset(d4, d5, d6);
            if (!this.world.getCollisionBoxes(this, axisalignedbb).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(ATTACKING, Byte.valueOf((byte)0));
        this.dataManager.register(KIND_SERVANT, Integer.valueOf(0));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.0D);
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100.0D);
    }

    protected SoundEvent getAmbientSound() {
        return TheTitans.isHeraldLevel(this.fetchKindMinion()) ? TitanAudio.get("thetitans:titanGhastLiving") : SoundEvents.ENTITY_GHAST_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return TheTitans.isHeraldLevel(this.fetchKindMinion()) ? TitanAudio.get("thetitans:titanGhastGrunt") : SoundEvents.ENTITY_GHAST_HURT;
    }

    protected SoundEvent getDeathSound() {
        return TheTitans.isHeraldLevel(this.fetchKindMinion()) ? TitanAudio.get("thetitans:titanGhastDeath") : SoundEvents.ENTITY_GHAST_DEATH;
    }

    protected float getSoundPitch() {
        return this.fetchKindIntMinion() == 4 ? super.getSoundPitch() + 0.3F : super.getSoundPitch();
    }

    protected Item getDropItem() {
        return Items.GUNPOWDER;
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        if (master != null)
            return;
        int j = this.rand.nextInt(2) + this.rand.nextInt(1 + arg70628n2);
        for (int k = 0; k < j; k++) {
            dropItem(Items.GHAST_TEAR, 1);
        }

        j = this.rand.nextInt(3) + this.rand.nextInt(1 + arg70628n2);
        for (int k = 0; k < j; k++) {
            dropItem(Items.GUNPOWDER, 1);
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

    protected float getSoundVolume() {
        return 10.0F;
    }

    public boolean getCanSpawnHere() {
        return (this.rand.nextInt(20) == 0) && (super.getCanSpawnHere()) && (this.world.getDifficulty() != EnumDifficulty.PEACEFUL);
    }

    public int getMaxSpawnedInChunk() {
        return 1;
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

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("ExplosionPower", this.explosionStrength);
        tagCompound.setInteger("MinionType", this.fetchKindIntMinion());
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.hasKey("ExplosionPower", 99)) {
            this.explosionStrength = tagCompund.getInteger("ExplosionPower");
        }

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
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(IMinion.fetchHpMultiplierMinion(10.0D, miniontype, 2));
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(IMinion.fetchStrikeMultiplierMinion(6.0D, miniontype, 2));
        this.experienceValue = IMinion.fetchExpMultiplierMinion(12, miniontype);
        this.setHealth(this.getMaxHealth());
    }

    public float getEyeHeight() {
        return 3.0F;
    }

    public void onLivingUpdate() {
        if (this.isEntityAlive() || this.fetchKindIntMinion() != 4)
            super.onLivingUpdate();
        if (this.fetchKindIntMinion() == 3) {
            if (this.rand.nextInt(120) == 0 && this.master == null && (getHealth() > 0.0F) && (!this.world.isRemote) && (this.world.getDifficulty() != EnumDifficulty.PEACEFUL)) {
                EntityGhastMinion entitychicken = new EntityGhastMinion(this.world);
                entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                entitychicken.setMinionType(0);
                this.world.spawnEntity(entitychicken);
            }

            if (this.rand.nextInt(240) == 0 && this.master == null && (getHealth() > 0.0F) && (!this.world.isRemote) && (this.world.getDifficulty() != EnumDifficulty.PEACEFUL)) {
                EntityGhastMinion entitychicken = new EntityGhastMinion(this.world);
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
                    EntityGhastMinion entitychicken = new EntityGhastMinion(this.world);
                    entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                    entitychicken.setMinionType(0);
                    this.world.spawnEntity(entitychicken);
                }

                if (this.rand.nextInt(120) == 0) {
                    EntityGhastMinion entitychicken = new EntityGhastMinion(this.world);
                    entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                    entitychicken.setMinionType(1);
                    this.world.spawnEntity(entitychicken);
                }

                if (this.rand.nextInt(240) == 0) {
                    EntityGhastMinion entitychicken = new EntityGhastMinion(this.world);
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

            if (this.isEntityAlive() && (!this.world.isRemote) && (this.rand.nextInt(1000) == 0) && (getAttackTarget() != null) && (getHealth() < getMaxHealth() / 2.0F) && (this.master == null)) {
                for (int i = 0; i < 16; i++) {
                    this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
                    this.world.spawnParticle(EnumParticleTypes.FLAME, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
                }

                // custom sound thetitans:titanland now registered and wired
                playSound(TitanAudio.get("thetitans:titanland"), 10000.0F, 1.0F);
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
                    if (entity != null && entity instanceof EntityLivingBase && this.allowStrikeGiant(entity.getClass())) {
                        entity.motionY += rand.nextDouble();
                        if (!world.isRemote)
                            ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(ClientProxy.electricJudgment, 10, 1));
                        this.world.playEvent((EntityPlayer)null, 1017, new BlockPos((int)entity.posX, (int)entity.posY, (int)entity.posZ), 0);
                    }
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

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source) || source.getDamageType() == "thorns" || (this.fetchKindIntMinion() >= 4 && source == DamageSourceExtra.radiation)) {
            return false;
        }

        if (((source.getTrueSource() instanceof EntityGhastMinion)) || ((source.getTrueSource() instanceof EntityGhastTitan))) {
            return false;
        }

        if (("fireball".equals(source.getDamageType())) && ((source.getTrueSource() instanceof EntityPlayer))) {
            super.attackEntityFrom(source, 1000.0F);
            return true;
        }

        Entity entity = source.getTrueSource();
        if ((source.getTrueSource() instanceof EntityLivingBase)) {
            setAttackTarget((EntityLivingBase)entity);
            setRevengeTarget((EntityLivingBase)entity);
        }

        return super.attackEntityFrom(source, amount);
    }

    public void TransformEntity(Entity entity) {
        entity.world.newExplosion(entity, entity.posX, entity.posY, entity.posZ, 8.0F, true, entity.world.getGameRules().getBoolean("mobGriefing"));
        EntityGhastTitan entitytitan = new EntityGhastTitan(entity.world);
        entitytitan.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, 0.0F);
        entity.setDead();
        entitytitan.funcM82206();
        entity.world.spawnEntity(entitytitan);
        entitytitan.playSound(TitanAudio.get("thetitans:titanGhastLiving"), 10000.0F, 0.8F);
    }

    public void setCombatTask() {
        this.tasks.removeTask(this.aiArrowAttack);
        if (this.patternStrike == 0 && this.fetchKindIntMinion() == 4) {
            this.tasks.addTask(0, this.aiArrowAttack);
        }
    }

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData arg110161n1) {
        super.onInitialSpawn(difficulty, arg110161n1);
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

    public void attackEntityWithRangedAttack(EntityLivingBase arg82196n1, float arg82196n2) {
        this.counterStrike = 10;
        this.swingArm(EnumHand.MAIN_HAND);
        this.world.playEvent((EntityPlayer)null, 1015, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
        if (getDistanceSq(arg82196n1) < (arg82196n1.width * arg82196n1.width) + 36D)
            attackEntityAsMob(arg82196n1);
        else
            switch (this.rand.nextInt(4)) {
            case 0:for (int i = 0; i < 100; i++) {
                    EntityHarcadiumArrow entityarrow = new EntityHarcadiumArrow(this.world, this, arg82196n1, 4F, 16F);
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
            case 1:for (int i = 0; i < 200; i++) {
                    // original: meta 32660 poison (1/5), 32696 weakness (1/4), 32698 slowness (1/3), else 32732 harming; 32725 healing vs undead.
                    // (the original's level-II bit on weakness/slowness has no 1.12.2 PotionType; the level-I type is used)
                    net.minecraft.potion.PotionType potionType = this.rand.nextInt(5) == 0 ? net.minecraft.init.PotionTypes.POISON : (this.rand.nextInt(4) == 0 ? net.minecraft.init.PotionTypes.WEAKNESS : (this.rand.nextInt(3) == 0 ? net.minecraft.init.PotionTypes.SLOWNESS : net.minecraft.init.PotionTypes.HARMING));
                    if (arg82196n1.isEntityUndead())
                        potionType = net.minecraft.init.PotionTypes.HEALING;
                    ItemStack potionStack = net.minecraft.potion.PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potionType);
                    EntityPotion entitypotion = new EntityPotion(this.world, this, potionStack);
                    double d0 = arg82196n1.posY + 0.5D;
                    entitypotion.rotationPitch -= -20.0F;
                    double d1 = (arg82196n1.posX + arg82196n1.motionX) - this.posX;
                    double d2 = d0 - this.posY;
                    double d3 = (arg82196n1.posZ + arg82196n1.motionZ) - this.posZ;
                    float f1 = MathHelper.sqrt(d1 * d1 + d3 * d3);
                    entitypotion.shoot(d1, d2 + f1 * 0.2F, d3, 2F, 25F);
                    this.world.spawnEntity(entitypotion);
                    double d8 = 4D;
                    Vec3d vec3 = getLook(1.0F);
                    entitypotion.posX = (this.posX + vec3.x * d8);
                    entitypotion.posY = (this.posY + vec3.y * d8 + 1D);
                    entitypotion.posZ = (this.posZ + vec3.z * d8);
                }

                break;
            case 2:this.world.newExplosion(this, arg82196n1.posX, arg82196n1.posY + 1D, arg82196n1.posZ, 2.0F, false, false);
                arg82196n1.attackEntityFrom(DamageSourceExtra.lightningBolt, 100.0F);
                this.world.addWeatherEffect(new EntityLightningBolt(this.world, arg82196n1.posX, arg82196n1.posY, arg82196n1.posZ, false));
                this.world.addWeatherEffect(new EntityLightningBolt(this.world, arg82196n1.posX, arg82196n1.posY, arg82196n1.posZ, false));
                this.world.addWeatherEffect(new EntityLightningBolt(this.world, arg82196n1.posX, arg82196n1.posY, arg82196n1.posZ, false));
                this.world.addWeatherEffect(new EntityLightningBolt(this.world, arg82196n1.posX, arg82196n1.posY, arg82196n1.posZ, false));
                break;
            case 3:for (int i = 0; i < 50; i++) {
                    double d8 = 4D;
                    Vec3d vec3 = getLook(1.0F);
                    this.world.playEvent((EntityPlayer)null, 1016, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
                    getDistanceSq(arg82196n1);
                    double d11 = (arg82196n1.posX + arg82196n1.motionX) - (this.posX + vec3.x * d8);
                    double d21 = (arg82196n1.posY + 1D) - (this.posY + vec3.y * d8 + 1D);
                    double d31 = (arg82196n1.posZ + arg82196n1.motionZ) - (this.posZ + vec3.z * d8);
                    EntityGhastMinionFireball entityfireball = new EntityGhastMinionFireball(this.world, this, d11 + (getRNG().nextGaussian() * 9F), d21, d31 + (getRNG().nextGaussian() * 9F));
                    entityfireball.explosionPower = this.explosionStrength;
                    this.world.spawnEntity(entityfireball);
                    entityfireball.posX = (this.posX + vec3.x * d8);
                    entityfireball.posY = (this.posY + vec3.y * d8 + 1D);
                    entityfireball.posZ = (this.posZ + vec3.z * d8);
                }

                break;
        }
    }

    public class EntityAIFindEntityNearestInjuredAlly extends EntityAIBase {
        private EntityGhastMinion fld179434b;
        private EntityLivingBase fld179433e;
        public EntityAIFindEntityNearestInjuredAlly(EntityGhastMinion entityCaveSpiderPriest) {
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
            List<?> list = this.fld179434b.world.getEntitiesWithinAABB(EntityGhastMinion.class, this.fld179434b.getEntityBoundingBox().grow(d0, d0, d0));
            if (list.isEmpty()) {
                return false;
            }

            for (int i = 0; i < list.size(); i++) {
                EntityGhastMinion entity = (EntityGhastMinion)list.get(i);
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

        protected double funcF179431() {
            return 100.0D;
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
        return "mob.ghast.moan";
    }

    @Override
    public float fetchVolumeSummon() {
        return 10F;
    }

    @Override
    public float fetchPitchSummon() {
        return 1F;
    }

    @Override
    public boolean beginGrounded() {
        return false;
    }
}
