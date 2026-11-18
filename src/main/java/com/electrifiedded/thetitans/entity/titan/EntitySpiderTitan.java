package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thehippomaster.AnimationAPI.IAnimatedEntity;
import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.core.TheCore;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.minion.EntityCaveSpiderMinion;
import com.electrifiedded.thetitans.entity.minion.EntitySpiderMinion;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAITitanLookIdle;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAITitanWander;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAITitanWatchClosest;
import com.electrifiedded.thetitans.entity.titan.anim.spidertitan.AnimationSpiderTitanAntiTitanAttack;
import com.electrifiedded.thetitans.entity.titan.anim.spidertitan.AnimationSpiderTitanAttack1;
import com.electrifiedded.thetitans.entity.titan.anim.spidertitan.AnimationSpiderTitanAttack2;
import com.electrifiedded.thetitans.entity.titan.anim.spidertitan.AnimationSpiderTitanAttack3;
import com.electrifiedded.thetitans.entity.titan.anim.spidertitan.AnimationSpiderTitanAttack4;
import com.electrifiedded.thetitans.entity.titan.anim.spidertitan.AnimationSpiderTitanCreation;
import com.electrifiedded.thetitans.entity.titan.anim.spidertitan.AnimationSpiderTitanDeath;
import com.electrifiedded.thetitans.entity.titan.anim.spidertitan.AnimationSpiderTitanShootLightning;
import com.electrifiedded.thetitans.entity.titan.anim.spidertitan.AnimationSpiderTitanShootWeb;
import com.electrifiedded.thetitans.entity.titan.anim.spidertitan.AnimationSpiderTitanSpit;
import com.electrifiedded.thetitans.entity.titan.anim.spidertitan.AnimationSpiderTitanStunned;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanTrophies;
import com.electrifiedded.thetitans.sound.SoundHandler;
import com.electrifiedded.thetitans.world.dimension.WorldProviderNowhere;
import com.electrifiedded.thetitans.world.dimension.WorldProviderVoid;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EntitySpiderTitan	extends EntityTitan	implements IAnimatedEntity, ITitanHitbox {
    private static final DataParameter<Byte> CLIMBING = EntityDataManager.<Byte>createKey(EntitySpiderTitan.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> KEY_BONUS = EntityDataManager.<Byte>createKey(EntitySpiderTitan.class, DataSerializers.BYTE);
    public int toLegsHarm;
    public EntityTitanPart head;
    public EntityTitanPart thorax;
    public EntityTitanPart abdomen;
    public EntityTitanPart rightlegsV;
    public EntityTitanPart leftlegsV;

    public EntitySpiderTitan(World worldIn) {
        super(worldIn);
        head = new EntityTitanPart(worldIn, this, "head", 8.0F, 8.0F);
        thorax = new EntityTitanPart(worldIn, this, "thorax", 6.0F, 6.0F);
        abdomen = new EntityTitanPart(worldIn, this, "abdomen", 12.0F, 8.0F);
        rightlegsV = new EntityTitanPart(worldIn, this, "rightleg", 12.0F, 8.0F);
        leftlegsV = new EntityTitanPart(worldIn, this, "leftleg", 12.0F, 8.0F);
        setPiece.add(head);
        setPiece.add(thorax);
        setPiece.add(abdomen);
        setPiece.add(rightlegsV);
        setPiece.add(leftlegsV);
        setSize(1.5F * fetchScaleMultiplierGiant(), 0.85F * fetchScaleMultiplierGiant());
        experienceValue = (12000 + fetchPowerExtra() * 500);
        targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityIronGolemTitan.class, 0, false));
        targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntitySnowGolemTitan.class, 0, false));
        tasks.addTask(1, new AnimationSpiderTitanCreation(this));
        tasks.addTask(1, new AnimationSpiderTitanDeath(this));
        tasks.addTask(1, new AnimationSpiderTitanAttack3(this));
        tasks.addTask(1, new AnimationSpiderTitanStunned(this));
        tasks.addTask(1, new AnimationSpiderTitanShootWeb(this));
        tasks.addTask(1, new AnimationSpiderTitanShootLightning(this));
        tasks.addTask(1, new AnimationSpiderTitanAntiTitanAttack(this));
        tasks.addTask(1, new AnimationSpiderTitanAttack2(this));
        tasks.addTask(1, new AnimationSpiderTitanAttack1(this));
        tasks.addTask(1, new AnimationSpiderTitanSpit(this));
        tasks.addTask(1, new AnimationSpiderTitanAttack4(this));
        tasks.addTask(6, new EntityAITitanWander(this, 300));
        tasks.addTask(7, new EntityAITitanWatchClosest(this, EntityTitanSpirit.class, 128.0F));
        tasks.addTask(8, new EntityAITitanWatchClosest(this, EntityTitan.class, 128.0F));
        tasks.addTask(9, new EntityAITitanWatchClosest(this, EntityPlayer.class, 64.0F));
        tasks.addTask(10, new EntityAITitanLookIdle(this));
    }

    protected void applyEntityAI() {
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        if (TheTitans.ffaModeTitans)
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.giantSorterSpider));
        else
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.forAThingToKillSearch));
    }

    @Override
    public double fetchAmountHp() {
        if (this instanceof EntityCaveSpiderTitan)
            switch (fetchVariantGiant()) {
            case 1: return super.fetchAmountHp() * 14.0D;
            case 2: return super.fetchAmountHp() * 18.0D;
            case 3: return super.fetchAmountHp() * 16.0D;
            case 4: return super.fetchAmountHp() * 18.0D;
            case 5: return super.fetchAmountHp() * 24.0D;
            default: return super.fetchAmountHp() * 12.0D;
        } else
                switch (fetchVariantGiant()) {
            case 1: return super.fetchAmountHp() * 18.0D;
            case 2: return super.fetchAmountHp() * 14.0D;
            case 3: return super.fetchAmountHp() * 24.0D;
            case 4: return super.fetchAmountHp() * 32.0D;
            default: return super.fetchAmountHp() * 16.0D;
        }
    }

    public boolean isArmored() {
        return getHealth() <= getMaxHealth() / 4.0F || TheTitans.isDifficulty(world, TheCore.NIGHTMARE_V) || TheTitans.isDifficulty(world, TheCore.IMPOSSIBLE);
    }

    @SuppressWarnings("rawtypes")
    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        return arg70686n1 != head.getClass() && arg70686n1 != thorax.getClass() && arg70686n1 != abdomen.getClass() && arg70686n1 != rightlegsV.getClass() && arg70686n1 != leftlegsV.getClass() && arg70686n1 != EntityWebShot.class && arg70686n1 != EntitySpiderMinion.class && (arg70686n1 == EntityCaveSpiderTitan.class || arg70686n1 != EntitySpiderTitan.class);
    }

    public boolean getCanSpawnHere() {
        return (rand.nextInt(25) == 0) && (world.getDifficulty() != EnumDifficulty.PEACEFUL) && (isValidLightLevel());
    }

    public int fetchSpawnRateMinion() {
        return TheTitans.giantServantSpawnrateSpider;
    }

    protected void entityInit() {
        super.entityInit();
        dataManager.register(CLIMBING, Byte.valueOf((byte)0));
        dataManager.register(KEY_BONUS, Byte.valueOf((byte)0));
    }

    public int fetchIDBonus() {
        return dataManager.get(KEY_BONUS);
    }

    public void assignIDBonus(int arg70829n1) {
        dataManager.set(KEY_BONUS, Byte.valueOf((byte)arg70829n1));
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.hasKey("DamageToLegs", 99)) {
            toLegsHarm = tagCompund.getInteger("DamageToLegs");
        }

        if (tagCompund.hasKey("SpawnedBonusID", 99)) {
            assignIDBonus(tagCompund.getInteger("SpawnedBonusID"));
        }

        stunnedIs = tagCompund.getBoolean("Stunned");
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("DamageToLegs", toLegsHarm);
        tagCompound.setInteger("SpawnedBonusID", fetchIDBonus());
        tagCompound.setBoolean("Stunned", stunnedIs);
    }

    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {
            setBesideClimbableBlock(collidedHorizontally);
        }
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    public boolean allowHurtByUserBe() {
        return (stunnedIs) && (!isEntityInvulnerable(DamageSource.GENERIC));
    }

    public boolean allowStrike() {
        return false;
    }

    public double fetchReachMelee() {
        return width + (0.25D * fetchScaleMultiplierGiant());
    }

    protected String fetchLivingAudioGiant() {
        return stunnedIs || fetchWaiting() || getAnimID() == 13 ? null : "thetitans:titanSpiderLiving";
    }

    protected String fetchHurtAudioGiant() {
        return "thetitans:titanSpiderGrunt";
    }

    protected String fetchDeathAudioGiant() {
        return "thetitans:titanSpiderDeath";
    }

    public boolean attackEntityAsMob(Entity arg70652n1) {
        return false;
    }

    protected void funcA145780(int arg145780n1, int arg145780n2, int arg145780n3, Block arg145780n4) {
        if (isClient()) {
            triggerAudioGiant("thetitans:titanStep", 10.0F, 1.5F);
            triggerAudioGiant("thetitans:titanStep", 10.0F, 1.5F);
            shakeUserCamerasNearby(4D);
            shakeUserCamerasNearby(4D);
        }
    }

    public boolean needsMove() {
        return !stunnedIs &&super.needsMove();
    }

    public double getSpeed() {
        switch (fetchVariantGiant()) {
            case 1:
                return (fetchIDBonus() == 1 ? 0.625D : 0.575D) / 4;
            case 2:
                return (fetchIDBonus() == 1 ? 0.55D : 0.525D) / 4;
            case 3:
                return (fetchIDBonus() == 1 ? 0.6D : 0.575D) / 4;
            case 4:
                return (fetchIDBonus() == 1 ? 0.75D : 0.6D) / 4;
            default:
                return (fetchIDBonus() == 1 ? 0.6D : 0.55D) / 4;
        }
    }

    public boolean isInvisible() {
        return fetchIDBonus() == 4;
    }

    public void appendStrikeEffectsAddition(Entity damagedEntity) {
        super.appendStrikeEffectsAddition(damagedEntity);
        if (fetchIDBonus() == 2) {
            SoundHandler.whenPlayMob("thetitans:titanpunch", world, damagedEntity, 10.0F, 1.0F);
        }
    }

    public void onLivingUpdate() {
        Calendar calendar = world.getCurrentDate();
        if (!(this instanceof EntityCaveSpiderTitan) && !isDead && fetchVariantGiant() != 4 && fetchVariantGiant() != 3 && dimension == 0 && ((!world.isDaytime() && world.getCurrentMoonPhaseFactor() != 1.0F) || (calendar.get(2) + 1 == 12 && calendar.get(5) >= 16 && calendar.get(5) <= 26))) {
            assignVariantGiant(3);
        }

        if (!isRiding() && !fetchWaiting() && !stunnedIs && getAnimID() == 0) {
            flyTitan(10F, 2F, 4D);
        }

        if (fetchWaiting()) {
            AnimationAPI.sendAnimPacket(this, 13);
            if (allowAwaken()) {
                awaken();
            }
        }

        else {
            if (getAnimID() == 13) {
                motionX = 0D;
                motionZ = 0D;
                if (motionY > 0D)
                    motionY = 0D;
                if (getAnimTick() == 1)
                    triggerAudioGiant("thetitans:titanBirth", 100.0F, 1.25F);
                if (getAnimTick() == 40)
                    triggerAudioGiant("thetitans:titanRumble", 10.0F, 1.0F);
                if (getAnimTick() == 30)
                    triggerAudioGiant("thetitans:titanSpiderLiving", getSoundVolume(), 0.8F);
                if (getAnimTick() == 130)
                    triggerAudioGiant("thetitans:titanFall", getSoundVolume(), 1F);
                if (getAnimTick() == 135 || getAnimTick() == 155) {
                    funcA145780(0, 0, 0, Blocks.STONE);
                    triggerAudioGiant("thetitans:titanPress", getSoundVolume(), 1F);
                }
            }
        }

        if (getAnimID() == 6 && getAnimTick() <= 100 && getAnimTick() >= 30 && getAnimTick() % 5 == 0 && (getAttackTarget() != null)) {
            triggerAudioGiant("mob.wither.shoot", 5.0F, 1.0F);
            double d8 = 2D;
            float xfac = MathHelper.sin(renderYawOffset * (float)Math.PI / 180.0F);
            float zfac = MathHelper.cos(renderYawOffset * (float)Math.PI / 180.0F);
            double d0 = (getAttackTarget().posX) - (posX - (double)(xfac * d8));
            double d1 = (getAttackTarget().posY - 1D) - (posY + 1D);
            double d2 = (getAttackTarget().posZ) - (posZ + (double)(zfac * d8));
            MathHelper.sqrt(d0 * d0 + d2 * d2);
            EntityWebShot entitysnowball = new EntityWebShot(world, this, d0, d1, d2);
            entitysnowball.posX = posX - (double)(xfac * d8);
            entitysnowball.posY = posY + 1D;
            entitysnowball.posZ = posZ + (double)(zfac * d8);
            world.spawnEntity(entitysnowball);
        }

        if (getAnimID() == 7 && (getAnimTick() == 68) && (getAttackTarget() != null)) {
            double d8 = -2.0D;
            Vec3d vec3 = getLook(1.0F);
            double dx = vec3.x * d8;
            double dz = vec3.z * d8;
            float f = (float)fetchAmountStrike(2.0D);
            int i = fetchAmountKnockback();
            strikeMobChoosen(getAttackTarget(), f, i);
            getAttackTarget().motionY += 2.0D;
            world.newExplosion(this, getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 2.0F, false, false);
            world.newExplosion(this, posX + dx, posY + 8.0D, posZ + dz, 1.0F, false, false);
            getAttackTarget().attackEntityFrom(DamageSourceExtra.lightningBolt, f);
            world.addWeatherEffect(new EntityGammaLightning(world, posX + dx, posY + 3.0D, posZ + dz, 0.6F, 0.1F, 0.2F));
            world.addWeatherEffect(new EntityGammaLightning(world, getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 0.6F, 0.1F, 0.2F));
            List<?> found1 = world.getEntitiesWithinAABBExcludingEntity(getAttackTarget(), getAttackTarget().getEntityBoundingBox().grow(6.0D, 3.0D, 6.0D));
            if ((found1 != null) && (!found1.isEmpty())) {
                for (int i1 = 0; i1 < found1.size(); i1++) {
                    Entity mobRef1 = (Entity)found1.get(i1);
                    if (allowStrikeGiant(mobRef1.getClass())) {
                        world.newExplosion(this, mobRef1.posX, mobRef1.posY, mobRef1.posZ, 2.0F, false, false);
                        strikeMobChoosen(mobRef1, f, i);
                        mobRef1.attackEntityFrom(DamageSourceExtra.lightningBolt, f);
                        world.addWeatherEffect(new EntityLightningBolt(world, mobRef1.posX, mobRef1.posY, mobRef1.posZ, false));
                    }
                }
            }
        }

        if (!world.isRemote && getAnimID() == 2 && (getAttackTarget() != null)) {
            List<?> found11 = world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(64.0D, 64.0D, 64.0D), e -> e != this);
            if ((found11 != null) && (!found11.isEmpty())) {
                for (int i1 = 0; i1 < found11.size(); i1++) {
                    Entity mobRef1 = (Entity)found11.get(i1);
                    if ((mobRef1 instanceof EntityLivingBase) && allowStrikeGiant(mobRef1.getClass())) {
                        mobRef1.hurtResistantTime = 0;
                        ((EntityLivingBase)mobRef1).addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 100, 1));
                    }
                }
            }
        }

        if (!world.isRemote && getAnimID() == 2 && getAnimTick() <= 70 && getAnimTick() >= 60 && (getAttackTarget() != null)) {
            triggerAudioGiant("mob.wither.shoot", 5.0F, 1.0F);
            strikeMobChoosen(getAttackTarget(), 25F, 0);
            for (int j = 0; j < 300; j++) {
                net.minecraft.potion.PotionType potiontype = this instanceof EntityCaveSpiderTitan ? net.minecraft.init.PotionTypes.STRONG_POISON : (rand.nextInt(10) == 0 ? net.minecraft.init.PotionTypes.LONG_POISON : net.minecraft.init.PotionTypes.LONG_SLOWNESS);
                EntityPotion entitypotion = new EntityPotion(world, this, net.minecraft.potion.PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potiontype));
                double d8 = 5D;
                float xfac = MathHelper.sin(renderYawOffset * (float)Math.PI / 180.0F);
                float zfac = MathHelper.cos(renderYawOffset * (float)Math.PI / 180.0F);
                double d0 = (getAttackTarget().posX) - (head.posX - (double)(xfac * d8));
                double d1 = (getAttackTarget().posY) - (head.posY + 4D);
                double d2 = (getAttackTarget().posZ) - (head.posZ + (double)(zfac * d8));
                float f1 = MathHelper.sqrt(d0 * d0 + d2 * d2);
                entitypotion.shoot(d0, d1 + (double)(f1 * 0.2F), d2, 1.6F, 1F + (float)((getAnimTick() * 2) - 60));
                entitypotion.posX = head.posX - (double)(xfac * d8);
                entitypotion.posY = head.posY + 4D;
                entitypotion.posZ = head.posZ + (double)(zfac * d8);
                world.spawnEntity(entitypotion);
            }
        }

        if (getAnimID() == 7 && getAnimTick() == 68)
            world.addWeatherEffect(new EntityGammaLightning(world, posX , posY + 3.0D, posZ, 0.6F, 0.1F, 0.2F));
        EntityPlayer player = world.getClosestPlayerToEntity(head, 9D);
        if (player != null && head.posY < player.posY - 7D) {
            rotationYawHead += MathHelper.sin(ticksExisted) * 40F;
            rotationPitch -= MathHelper.cos(ticksExisted) * 40F;
        }

        giantMelee = true;
        if (getAnimID() == 10) {
            if ((getAnimTick() == 80) || (getAnimTick() == 210) || (getAnimTick() == 250) || (getAnimTick() == 260)) {
                shakeUserCamerasNearby(10000D);
                triggerAudioGiant("thetitans:titanFall", 20.0F, 1.0F);
                triggerAudioGiant("thetitans:distantLargeFall", 10000.0F, 1.0F);
                collideWithEntities(leftlegsV, world.getEntitiesWithinAABBExcludingEntity(this, leftlegsV.getEntityBoundingBox().grow(24.0D, 1.0D, 24.0D)));
                collideWithEntities(rightlegsV, world.getEntitiesWithinAABBExcludingEntity(this, rightlegsV.getEntityBoundingBox().grow(24.0D, 1.0D, 24.0D)));
            }

            if (getAnimTick() == 420) {
                stunnedIs = false;
                System.out.print("Done \n");
                assignStamina(fetchStaminaMax());
            }

            else {
                setAttackTarget(null);
            }
        }

        if (getAnimID() == 8) {
            if (getAnimTick() == 58) {
                triggerAudioGiant("thetitans:largeFall", 8.0F, 0.9F);
                triggerAudioGiant("thetitans:titanFall", 10.0F, 1.0F);
            }

            if (getAnimTick() == 60) {
                triggerAudioGiant("thetitans:distantLargeFall", 10000.0F, 1.0F);
            }

            if (getAnimTick() == 420) {
                stunnedIs = false;
            }

            else {
                setAttackTarget(null);
            }
        }

        if (stunnedIs) {
            setAttackTarget(null);
            AnimationAPI.sendAnimPacket(this, 8);
        }

        if (ticksExisted > 5) {
            for (EntityTitanPart part : setPiece) {
                if (fetchIDBonus() > 0) {
                    double red = 0.4862745098039216D;
                    double green = 0.6862745098039216D;
                    double blue = 0.7764705882352941D;
                    if (fetchIDBonus() == 2) {
                        red = 0.5764705882352941D;
                        green = 0.1411764705882353D;
                        blue = 0.1372549019607843D;
                    }

                    if (fetchIDBonus() == 3) {
                        red = 0.803921568627451D;
                        green = 0.3607843137254902D;
                        blue = 0.6705882352941176D;
                    }

                    if (fetchIDBonus() == 4) {
                        red = 0.4980392156862745D;
                        green = 0.5137254901960784D;
                        blue = 0.5764705882352941D;
                    }

                    for (int i1 = 0; i1 < 50; i1++)
                        world.spawnParticle(EnumParticleTypes.SPELL_MOB, part.posX + (rand.nextDouble() - 0.5D) * part.width, part.posY + rand.nextDouble() * part.height, part.posZ + (rand.nextDouble() - 0.5D) * part.width, red, green, blue);
                }
            }
        }

        if (stunnedIs || deathTicks > 0) {
            motionX *= 0.0D;
            motionZ *= 0.0D;
        }

        if ((!AnimationAPI.isEffectiveClient()) && (getAttackTarget() != null) && (!stunnedIs) && getAnimID() == 0) {
            double d0 = getDistance(getAttackTarget());
            if (d0 < fetchReachMelee()) {
                if (getAnimID() == 0) {
                    switch (rand.nextInt(4)) {
                        case 0:AnimationAPI.sendAnimPacket(this, 3);
                            setAnimID(3);
                            break;
                        case 1:if (getAttackTarget().height >= height + 2F || (posY + getEyeHeight() < getAttackTarget().posY + getAttackTarget().getEyeHeight())) {
                                AnimationAPI.sendAnimPacket(this, 1);
                                setAnimID(1);
                            }

                            else {
                                AnimationAPI.sendAnimPacket(this, 9);
                                setAnimID(9);
                            }

                            break;
                        case 3:AnimationAPI.sendAnimPacket(this, 4);
                            setAnimID(4);
                            break;
                        default:if (getAttackTarget().height >= height + 2F || (posY + getEyeHeight() < getAttackTarget().posY + getAttackTarget().getEyeHeight())) {
                                AnimationAPI.sendAnimPacket(this, 1);
                                setAnimID(1);
                            }

                            else {
                                AnimationAPI.sendAnimPacket(this, 5);
                                setAnimID(5);
                            }

                            break;
                    }
                }
            }

            else if (getAnimID() == 0 && (getRNG().nextInt(100) == 0)) {
                switch (rand.nextInt(3)) {
                    case 0:AnimationAPI.sendAnimPacket(this, 2);
                        setAnimID(2);
                        break;
                    case 1:AnimationAPI.sendAnimPacket(this, 7);
                        setAnimID(7);
                        break;
                    case 2:AnimationAPI.sendAnimPacket(this, 6);
                        setAnimID(6);
                        break;
                }
            }
        }

        if ((rand.nextInt(100) == 0) && (getAttackTarget() != null) && (!stunnedIs) && (onGround) && getAnimID() == 0) {
            faceEntity(getAttackTarget(), 180.0F, 180.0F);
            double d01 = getAttackTarget().posX - posX;
            double d11 = getAttackTarget().posZ - posZ;
            float f21 = MathHelper.sqrt(d01 * d01 + d11 * d11);
            double hor = 2D;
            double ver = 2D;
            motionX = (d01 / f21 * hor * hor + motionX * hor);
            motionZ = (d11 / f21 * hor * hor + motionZ * hor);
            motionY = ver;
            collideWithEntities(rightlegsV, world.getEntitiesWithinAABBExcludingEntity(this, rightlegsV.getEntityBoundingBox().grow(6D, 6D, 6D)));
            collideWithEntities(leftlegsV, world.getEntitiesWithinAABBExcludingEntity(this, leftlegsV.getEntityBoundingBox().grow(6D, 6D, 6D)));
            if (getDistance(getAttackTarget()) < 50D) {
                float f11 = (float)fetchAmountStrike(2.0F);
                int i = fetchAmountKnockback() * 2;
                strikeMobChoosen(getAttackTarget(), f11, i);
            }
        }

        if (getAttackTarget() != null) {
            if ((rand.nextInt(60) == 0) && (canEntityBeSeen(getAttackTarget()))) {
                int i = MathHelper.floor(getAttackTarget().posX + rand.nextDouble() * 2.0D);
                int j = MathHelper.floor(getAttackTarget().posY + rand.nextDouble() * 2.0D);
                int k = MathHelper.floor(getAttackTarget().posZ + rand.nextDouble() * 2.0D);
                BlockPos pos = new BlockPos(i, j, k);
                IBlockState block1 = world.getBlockState(pos);
                if ((block1.getMaterial() == Material.AIR) && (world.getGameRules().getBoolean("mobGriefing"))) {
                    world.setBlockState(pos, Blocks.WEB.getDefaultState());
                }

                else {
                    getAttackTarget().addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 2));
                }
            }
        }

        if (fetchIDBonus() > 4)
            assignIDBonus(4);
        if (fetchIDBonus() < 0 || this instanceof EntityCaveSpiderTitan)
            assignIDBonus(0);
        super.onLivingUpdate();
    }

    protected void whenUpdateBounds() {
        if (ticksExisted > 5) {
            setSize(1.5F * fetchScaleMultiplierGiant(), 0.85F * fetchScaleMultiplierGiant());
            abdomen.width = rightlegsV.width = leftlegsV.width = 0.75F * fetchScaleMultiplierGiant();
            abdomen.height = rightlegsV.height = leftlegsV.height = head.height = head.width = 0.5F * fetchScaleMultiplierGiant();
            thorax.height = thorax.width = 0.375F * fetchScaleMultiplierGiant();
            float f = renderYawOffset * 3.1415927F / 180.0F;
            float f1 = MathHelper.sin(f);
            float f2 = MathHelper.cos(f);
            head.setLocationAndAngles(posX - MathHelper.sin(rotationYawHead * 3.1415927F / 180.0F) * (0.25F * width), posY + (0.3571428571428571F * height) - MathHelper.sin(rotationPitch * 3.1415927F / 180.0F) * (0.2857142857142857F * height), posZ + MathHelper.cos(rotationYawHead * 3.1415927F / 180.0F) * (0.25F * width), 0.0F, 0.0F);
            thorax.setLocationAndAngles(posX, posY + (0.4464285714285714F * height), posZ, 0.0F, 0.0F);
            abdomen.setLocationAndAngles(posX + f1 * (0.3214285714285714F * width), posY + (0.3571428571428571F * height), posZ - f2 * (0.3214285714285714F * width), 0.0F, 0.0F);
            rightlegsV.setLocationAndAngles(posX + f2 * (0.3571428571428571F * width), posY, posZ + f1 * (0.3571428571428571F * width), 0.0F, 0.0F);
            leftlegsV.setLocationAndAngles(posX - f2 * (0.3571428571428571F * width), posY, posZ - f1 * (0.3571428571428571F * width), 0.0F, 0.0F);
        }

        super.whenUpdateBounds();
    }

    public String getName() {
        switch (fetchVariantGiant()) {
            case 1:
                return I18n.translateToLocal("entity.SpiderTitanAlpha.name");
            case 2:
                return I18n.translateToLocal("entity.SpiderTitanMycelium.name");
            case 3:
                return I18n.translateToLocal("entity.SpiderTitanLunar.name");
            case 4:
                return I18n.translateToLocal("entity.SpiderTitanVoid.name");
            default:
                return I18n.translateToLocal("entity.SpiderTitan.name");
        }
    }

    protected Item getDropItem() {
        return Items.STRING;
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        if (deathTicks > 0) {
            for (int x = 0; x < 8; x++) {
                EntityXPBomb entitylargefireball = new EntityXPBomb(world, posX, posY + 4D, posZ);
                entitylargefireball.setPosition(posX, posY + 4D, posZ);
                ++entitylargefireball.motionY;
                entitylargefireball.assignTotalXP(this instanceof EntityCaveSpiderTitan ? 3000 : 4000);
                world.spawnEntity(entitylargefireball);
            }

            for (int x = 0; x < 8; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.STRING));
                itembomb.assignTotalStack(32 + rand.nextInt(32 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.COAL));
                itembomb.assignTotalStack(32 + rand.nextInt(32 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.EMERALD));
                itembomb.assignTotalStack(8 + rand.nextInt(8 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.DIAMOND));
                itembomb.assignTotalStack(8 + rand.nextInt(8 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.IRON_INGOT));
                itembomb.assignTotalStack(24 + rand.nextInt(24 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.WEB));
                itembomb.assignTotalStack(12 + rand.nextInt(12 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.MOSSY_COBBLESTONE));
                itembomb.assignTotalStack(12 + rand.nextInt(12 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 3; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.LEATHER));
                itembomb.assignTotalStack(12 + rand.nextInt(12 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.SPIDER_EYE));
                itembomb.assignTotalStack(32 + rand.nextInt(32 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.FERMENTED_SPIDER_EYE));
                itembomb.assignTotalStack(12 + rand.nextInt(12 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            if ((rand.nextInt(2) == 0 || rand.nextInt(1 + arg70628n2) > 0))
                for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(TitanGear.nuggetHarcadiumZ));
                itembomb.assignTotalStack(1 + rand.nextInt(3 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }
        }
    }

    public boolean isOnLadder() {
        return isBesideClimbableBlock();
    }

    public void setInWeb() {
    }

    public boolean isPotionApplicable(PotionEffect arg70687n1) {
        return arg70687n1.getPotion() == MobEffects.POISON ? false : super.isPotionApplicable(arg70687n1);
    }

    public boolean isBesideClimbableBlock() {
        return (dataManager.get(CLIMBING) & 0x1) != 0;
    }

    public void setBesideClimbableBlock(boolean arg70839n1) {
        byte b0 = dataManager.get(CLIMBING);
        if (arg70839n1) {
            b0 = (byte)(b0 | 0x1);
        }

        else {
            b0 = (byte)(b0 & 0xFFFFFFFE);
        }

        dataManager.set(CLIMBING, Byte.valueOf(b0));
    }

    public IEntityLivingData onInitialSpawn(net.minecraft.world.DifficultyInstance difficulty, IEntityLivingData arg180482n2) {
        Object p_180482_2_1 = super.onInitialSpawn(difficulty, arg180482n2);
        assignWaiting(true);
        if (world.rand.nextInt(10) == 0 || TheTitans.isDifficulty(world, TheCore.NIGHTMARE_V)) {
            assignIDBonus(rand.nextInt(5));
        }

        if (world.provider instanceof WorldProviderNowhere || world.provider instanceof WorldProviderVoid || world.provider instanceof WorldProviderEnd)
            assignVariantGiant(this instanceof EntityCaveSpiderTitan ? 5 : 4);
        return (IEntityLivingData)p_180482_2_1;
    }

    public float getEyeHeight() {
        return 0.7428571428571429F * height;
    }

    protected void updateAITasks() {
        if (fetchIDBonus() == 3) {
            heal(2F);
            for (int u = 0; u < 1 + rand.nextInt(10); u++)
                heal(2F);
        }

        super.updateAITasks();
    }

    public String fetchAchievement() {
        if ((this instanceof EntityCaveSpiderTitan)) {
            return TitanTrophies.cavespidertitan;
        }

        if ((getRidingEntity() != null) && ((getRidingEntity() instanceof EntitySkeletonTitan))) {
            return TitanTrophies.spiderjockeytitan;
        }

        return TitanTrophies.spidertitan;
    }

    @SuppressWarnings("unchecked")
    protected void whenDeathUpdateGiant() {
        if (getHealth() <= 0F) {
            ++deathTicks;
            AnimationAPI.sendAnimPacket(this, 10);
            setAnimID(10);
            setHealth(0.0F);
        }

        else {
            attackEntityFrom(DamageSource.OUT_OF_WORLD, 25F);
            deathTicks = 0;
            if (getAnimID() == 10)
                setAnimID(0);
        }

        motionX *= 0.0D;
        motionZ *= 0.0D;
        setAttackTarget(null);
        setHealth(0.0F);
        if ((deathTicks == 1) && (!world.isRemote)) {
            triggerAudioGiant(fetchDeathAudioGiant(), getSoundVolume(), getSoundPitch());
            ArrayList<?> listp = Lists.newArrayList(world.playerEntities);
            if ((listp != null) && (!listp.isEmpty())) {
                for (int i1 = 0; i1 < listp.size(); i1++) {
                    Entity entity = (Entity)listp.get(i1);
                    if ((entity != null) && ((entity instanceof EntityPlayer))) {
                        { /* advancement runGrant removed (1.12.2) */ }
                    }
                }
            }
        }

        if (deathTicks == 1) {
            setAnimTick(1);
        }

        if (deathTicks >= 500) {
            setInvulTime(getInvulTime() + 8);
            float f = (rand.nextFloat() - 0.5F) * 24.0F;
            float f1 = (rand.nextFloat() - 0.5F) * 12.0F;
            float f2 = (rand.nextFloat() - 0.5F) * 24.0F;
            world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX + f, posY + 2.0D + f1, posZ + f2, 0.0D, 0.0D, 0.0D);
        }

        if (getInvulTime() >= fetchHoldThreash()) {
            setDead();
        }
    }

    protected void inactActionDeath() {
        if ((!world.isRemote)) {
            triggerAudioGiant("mob.spider.death", getSoundVolume(), getSoundPitch());
            if (world.getGameRules().getBoolean("doMobLoot")) {
                dropFewItems(true, 0);
                dropEquipment(true, 0);
            }

            if (this instanceof EntityCaveSpiderTitan) {
                EntityTitanSpirit entitytitan = new EntityTitanSpirit(world);
                entitytitan.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                world.spawnEntity(entitytitan);
                entitytitan.assignHuntingVessel(false);
                entitytitan.assignKindSpirit(2);
                entitytitan.setSubType(fetchVariantGiant());
            }

            else {
                EntityTitanSpirit entitytitan = new EntityTitanSpirit(world);
                entitytitan.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                world.spawnEntity(entitytitan);
                entitytitan.assignHuntingVessel(false);
                entitytitan.assignKindSpirit(3);
                entitytitan.setSubType(fetchVariantGiant());
            }
        }
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source)) {
            return false;
        }

        else if (this instanceof EntityCaveSpiderTitan ? (source.getTrueSource() instanceof EntityCaveSpiderMinion || source.getTrueSource() instanceof EntityCaveSpiderTitan) : (source.getTrueSource() instanceof EntitySpiderMinion || source.getTrueSource() instanceof EntitySpiderTitan && !(source.getTrueSource() instanceof EntityCaveSpiderTitan))) {
            return false;
        }

        else {
            Entity entity = source.getTrueSource();
            if (((entity instanceof EntityLivingBase)) && (!isEntityInvulnerable(source)) && (amount > 25.0F)) {
                List<?> list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(256.0D, 256.0D, 256.0D));
                for (int i = 0; i < list.size(); i++) {
                    Entity mobRef1 = (Entity)list.get(i);
                    if (mobRef1.getClass() == EntitySpiderTitan.class) {
                        EntitySpiderTitan entitypigzombie = (EntitySpiderTitan)mobRef1;
                        entitypigzombie.setAttackTarget((EntityLivingBase)entity);
                        entitypigzombie.setRevengeTarget((EntityLivingBase)entity);
                    }

                    setAttackTarget((EntityLivingBase)entity);
                    setRevengeTarget((EntityLivingBase)entity);
                }
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    public boolean attackEntityFromPart(EntityTitanPart arg70965n1, DamageSource source, float amount) {
        if (arg70965n1 == head)
            amount *= 2F;
        if (source.getTrueSource() != null && source.getTrueSource() instanceof EntityPlayer && source.canHarmInCreative() && (toLegsHarm < 8) && (!stunnedIs) && ((arg70965n1 == leftlegsV) || (arg70965n1 == rightlegsV))) {
            attackEntityFrom((new DamageSource("other")).setDamageBypassesArmor().setDamageIsAbsolute(), 100F);
            setAttackTarget((EntityLivingBase) source.getTrueSource());
            setRevengeTarget((EntityLivingBase) source.getTrueSource());
            assignStamina(fetchStamina() - (fetchStaminaMax() / 100.0F));
            if (fetchStamina() <= 0.0D) {
                triggerAudioGiant(fetchDeathAudioGiant(), getSoundVolume(), getSoundPitch());
            }
        }

        funcE82195(source, amount);
        return true;
    }

    protected boolean funcE82195(DamageSource arg82195n1, float arg82195n2) {
        return attackEntityFrom(arg82195n1, arg82195n2);
    }

    public World funcD82194() {
        return world;
    }

    protected EntityLiving fetchMinion() {
        return new EntitySpiderMinion(world);
    }

    protected double cap() {
        return super.cap();
    }

    protected int recoveryTicks() {
        return 660;
    }
}
