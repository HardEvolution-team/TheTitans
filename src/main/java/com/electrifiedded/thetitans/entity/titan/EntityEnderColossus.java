package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thehippomaster.AnimationAPI.IAnimatedEntity;
import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.minion.EntityDragonMinion;
import com.electrifiedded.thetitans.entity.minion.EntityEndermanMinion;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAITitanLookIdle;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAITitanWander;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAITitanWatchClosest;
import com.electrifiedded.thetitans.entity.titan.anim.endercolossus.AnimationEnderColossusAntiTitanAttack;
import com.electrifiedded.thetitans.entity.titan.anim.endercolossus.AnimationEnderColossusAttack1;
import com.electrifiedded.thetitans.entity.titan.anim.endercolossus.AnimationEnderColossusAttack2;
import com.electrifiedded.thetitans.entity.titan.anim.endercolossus.AnimationEnderColossusAttack3;
import com.electrifiedded.thetitans.entity.titan.anim.endercolossus.AnimationEnderColossusChainLightning;
import com.electrifiedded.thetitans.entity.titan.anim.endercolossus.AnimationEnderColossusDeath;
import com.electrifiedded.thetitans.entity.titan.anim.endercolossus.AnimationEnderColossusDragonLightningBall;
import com.electrifiedded.thetitans.entity.titan.anim.endercolossus.AnimationEnderColossusLightningAttack;
import com.electrifiedded.thetitans.entity.titan.anim.endercolossus.AnimationEnderColossusLightningBall;
import com.electrifiedded.thetitans.entity.titan.anim.endercolossus.AnimationEnderColossusMeteorRain;
import com.electrifiedded.thetitans.entity.titan.anim.endercolossus.AnimationEnderColossusScream;
import com.electrifiedded.thetitans.entity.titan.anim.endercolossus.AnimationEnderColossusStun;
import com.electrifiedded.thetitans.registry.TitanBlockSet;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanTrophies;
import com.electrifiedded.thetitans.sound.SoundHandler;
import com.electrifiedded.thetitans.world.dimension.WorldProviderVoid;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class EntityEnderColossus extends EntityTitan implements IAnimatedEntity, ITitanHitbox {
    private static final DataParameter<Byte> SCREAMING = EntityDataManager.<Byte>createKey(EntityEnderColossus.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> BACK_UP_CALL = EntityDataManager.<Byte>createKey(EntityEnderColossus.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> LASER_TICKS_IRIS = EntityDataManager.<Integer>createKey(EntityEnderColossus.class, DataSerializers.VARINT);
    private int clockRoarcooldown;
    public boolean revealedIs;
    public EntityEnderColossusCrystal healingEnderCrystal;
    public int ofCrystalsNum;
    public int numOfCrystalsCap = 32;
    public EntityTitanPart head;
    public EntityTitanPart body;
    public EntityTitanPart irisRight;
    public EntityTitanPart irisLeft;
    public EntityTitanPart rightArm;
    public EntityTitanPart leftArm;
    public EntityTitanPart rightLeg;
    public EntityTitanPart leftLeg;
    public boolean stunnedIs;
    public int crystalsDestroyed;

    public EntityEnderColossus(World worldIn) {
        super(worldIn);
        head = new EntityTitanPart(worldIn, this, "head", 12.0F, 12.0F);
        body = new EntityTitanPart(worldIn, this, "body", 10.0F, 18.0F);
        irisRight = new EntityTitanPart(worldIn, this, "righteye", 3F, 2F);
        irisLeft = new EntityTitanPart(worldIn, this, "lefteye", 3F, 2F);
        rightArm = new EntityTitanPart(worldIn, this, "rightarm", 4F, 4F);
        leftArm = new EntityTitanPart(worldIn, this, "leftarm", 4F, 4F);
        rightLeg = new EntityTitanPart(worldIn, this, "rightleg", 4F, 42F);
        leftLeg = new EntityTitanPart(worldIn, this, "leftleg", 4F, 42F);
        numOfCrystalsCap = 20;
        setPiece.add(head);
        setPiece.add(body);
        setPiece.add(irisRight);
        setPiece.add(irisLeft);
        setPiece.add(rightArm);
        setPiece.add(leftArm);
        setPiece.add(rightLeg);
        setPiece.add(leftLeg);
        setSize(12.0F, 72.0F);
        experienceValue = 1000000;
        tasks.addTask(6, new EntityAITitanWander(this, 200));
        tasks.addTask(7, new EntityAITitanWatchClosest(this, EntityTitanSpirit.class, 128.0F));
        tasks.addTask(8, new EntityAITitanWatchClosest(this, EntityTitan.class, 128.0F));
        tasks.addTask(9, new EntityAITitanWatchClosest(this, EntityPlayer.class, 64.0F));
        tasks.addTask(10, new EntityAITitanLookIdle(this));
        targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityIronGolemTitan.class, 0, false));
        targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntitySnowGolemTitan.class, 0, false));
    }

    protected void whenUpdateBounds() {
        if (ticksExisted > 5) {
            setSize(0.5F * fetchScaleMultiplierGiant(), 3F * fetchScaleMultiplierGiant());
            float f = renderYawOffset * 3.1415927F / 180.0F;
            float f1 = MathHelper.sin(f);
            float f2 = MathHelper.cos(f);
            head.setLocationAndAngles(posX, posY + (0.8325D * height), posZ, 0.0F, 0.0F);
            body.setLocationAndAngles(posX, posY + (0.575D * height), posZ, 0.0F, 0.0F);
            irisRight.setLocationAndAngles(posX + (MathHelper.cos(rotationYawHead * 3.1415927F / 180.0F) * (0.425D * width)) - (MathHelper.sin(rotationYawHead * 3.1415927F / 180.0F) * (0.75D * width)), posY + getEyeHeight(), posZ + (MathHelper.sin(rotationYawHead * 3.1415927F / 180.0F) * (0.425D * width)) + (MathHelper.cos(rotationYawHead * 3.1415927F / 180.0F) * (0.75D * width)), 0.0F, 0.0F);
            irisLeft.setLocationAndAngles(posX - (MathHelper.cos(rotationYawHead * 3.1415927F / 180.0F) * (0.425D * width)) - (MathHelper.sin(rotationYawHead * 3.1415927F / 180.0F) * (0.75D * width)), posY + getEyeHeight(), posZ - (MathHelper.sin(rotationYawHead * 3.1415927F / 180.0F) * (0.425D * width)) + (MathHelper.cos(rotationYawHead * 3.1415927F / 180.0F) * (0.75D * width)), 0.0F, 0.0F);
            rightArm.setLocationAndAngles(posX + f2 * (0.625D * width), posY + (0.7875D * height), posZ + f1 * (0.625D * width), 0.0F, 0.0F);
            leftArm.setLocationAndAngles(posX - f2 * (0.625D * width), posY + (0.7875D * height), posZ - f1 * (0.625D * width), 0.0F, 0.0F);
            rightLeg.setLocationAndAngles(posX + f2 * (0.25D * width), posY, posZ + f1 * (0.25D * width), 0.0F, 0.0F);
            leftLeg.setLocationAndAngles(posX - f2 * (0.25D * width), posY, posZ - f1 * (0.25D * width), 0.0F, 0.0F);
            head.height = (isScreaming() ? 0.925F * fetchScaleMultiplierGiant() : 0.5F * fetchScaleMultiplierGiant());
            head.width = 0.5F * fetchScaleMultiplierGiant();
            body.height = 0.75F * fetchScaleMultiplierGiant();
            body.width = 0.475F * fetchScaleMultiplierGiant();
            irisRight.height = irisLeft.height = 0.1F * fetchScaleMultiplierGiant();
            irisRight.width = irisLeft.width = 0.125F * fetchScaleMultiplierGiant();
            leftLeg.height = rightLeg.height = 1.75F * fetchScaleMultiplierGiant();
            leftLeg.width = rightLeg.width = 0.125F * fetchScaleMultiplierGiant();
            rightArm.width = leftArm.width = rightArm.height = leftArm.height = 0.125F * fetchScaleMultiplierGiant();
            irisRight.rotationYaw = rotationYawHead;
            irisLeft.rotationYaw = rotationYawHead;
            irisRight.rotationPitch = rotationPitch;
            irisLeft.rotationPitch = rotationPitch;
        }

        super.whenUpdateBounds();
    }

    protected void applyEntityAI() {
        tasks.addTask(1, new AnimationEnderColossusDeath(this));
        tasks.addTask(1, new AnimationEnderColossusStun(this));
        tasks.addTask(1, new AnimationEnderColossusScream(this));
        tasks.addTask(1, new AnimationEnderColossusDragonLightningBall(this));
        tasks.addTask(1, new AnimationEnderColossusChainLightning(this));
        tasks.addTask(1, new AnimationEnderColossusLightningBall(this));
        tasks.addTask(1, new AnimationEnderColossusLightningAttack(this));
        tasks.addTask(1, new AnimationEnderColossusAntiTitanAttack(this));
        tasks.addTask(1, new AnimationEnderColossusMeteorRain(this));
        tasks.addTask(1, new AnimationEnderColossusAttack2(this));
        tasks.addTask(1, new AnimationEnderColossusAttack3(this));
        tasks.addTask(1, new AnimationEnderColossusAttack1(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        if (TheTitans.ffaModeTitans)
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.colossusSorterEnder));
        else
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.forAThingToKillSearch));
    }

    @Override
    public double fetchAmountHp() {
        return super.fetchAmountHp() * 40.0F;
    }

    public int fetchLaserTicksEye() {
        return this.dataManager.get(LASER_TICKS_IRIS).intValue();
    }

    public void assignLaserTicksEye(int arg82215n1) {
        this.dataManager.set(LASER_TICKS_IRIS, Integer.valueOf(arg82215n1));
    }

    public boolean alreadyALabelHas() {
        return revealedIs;
    }

    public int getTalkInterval() {
        return 100;
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        revealedIs = tagCompund.getBoolean("Musmu");
        assignLaserTicksEye(tagCompund.getInteger("ShootTimer"));
        assignCallBackUpCan(tagCompund.getBoolean("CallHelp"));
        if (tagCompund.hasKey("DestroyedCrystals", 99)) {
            crystalsDestroyed = tagCompund.getInteger("DestroyedCrystals");
        }

        stunnedIs = tagCompund.getBoolean("Stunned");
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("ShootTimer", fetchLaserTicksEye());
        tagCompound.setBoolean("CallHelp", fetchCallBackUpCan());
        tagCompound.setBoolean("Musmu", revealedIs);
        tagCompound.setInteger("DestroyedCrystals", crystalsDestroyed);
        tagCompound.setBoolean("Stunned", stunnedIs);
    }

    public double fetchReachMelee() {
        return width + (1.25D * fetchScaleMultiplierGiant());
    }

    @SuppressWarnings("rawtypes")
    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        return arg70686n1 != head.getClass() && arg70686n1 != body.getClass() && arg70686n1 != irisRight.getClass() && arg70686n1 != irisLeft.getClass() && arg70686n1 != rightArm.getClass() && arg70686n1 != leftArm.getClass() && arg70686n1 != rightLeg.getClass() && arg70686n1 != leftLeg.getClass() && (arg70686n1 != EntityEnderman.class) && (arg70686n1 != EntityEndermanMinion.class) && (arg70686n1 != EntityEnderColossus.class) && (arg70686n1 != EntityDragon.class) && (arg70686n1 != EntityDragonMinion.class) && (arg70686n1 != EntityEnderColossusCrystal.class);
    }

    public boolean getCanSpawnHere() {
        return (rand.nextInt(500) == 0) && (world.getDifficulty() != EnumDifficulty.PEACEFUL) && (isValidLightLevel());
    }

    public int fetchSpawnRateMinion() {
        return TheTitans.colossusServantSpawnrateEnder;
    }

    public EnumTitanStatus fetchConditionGiant() {
        return EnumTitanStatus.GREATER;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(SCREAMING, Byte.valueOf((byte)0));
        this.dataManager.register(BACK_UP_CALL, Byte.valueOf((byte)0));
        this.dataManager.register(LASER_TICKS_IRIS, Integer.valueOf(0));
    }

    public boolean fetchCallBackUpCan() {
        return this.dataManager.get(BACK_UP_CALL).byteValue() == 1;
    }

    public void assignCallBackUpCan(boolean arg82201n1) {
        this.dataManager.set(BACK_UP_CALL, Byte.valueOf((byte) (arg82201n1 ? 1 : 0)));
    }

    public int getParticleCount() {
        return 60;
    }

    public String fetchParticle() {
        return "portal";
    }

    public void fall(float arg70069n1, float damageMultiplier) {
        onGround = true;
        isAirBorne = false;
        float[] ret = ForgeHooks.onLivingFall(this, arg70069n1, damageMultiplier);
        if (ret == null) return;
        arg70069n1 = ret[0];
        if (arg70069n1 <= 0.0F) return;
        PotionEffect potioneffect = getActivePotionEffect(MobEffects.JUMP_BOOST);
        float f1 = potioneffect != null ? potioneffect.getAmplifier() + 1 : 0.0F;
        int i = MathHelper.ceil(arg70069n1 - 24F - f1);
        if (i > 0) {
            shakeUserCamerasNearby(50D);
            triggerAudioGiant("thetitans:groundSmash", 20.0F, 0.9F);
            triggerAudioGiant("thetitans:titanland", 10000.0F, 1.0F);
            wreckInAABBToplessBlocks(getEntityBoundingBox().grow(24D, 1D, 24D));
            List<?> found11 = world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(48D, 4D, 48D), e -> e != this);
            if ((found11 != null) && (!found11.isEmpty())) {
                for (int i1 = 0; i1 < found11.size(); i1++) {
                    Entity entity = (Entity)found11.get(i1);
                    if (((entity instanceof EntityLivingBase)) && (!(entity instanceof MultiPartEntityPart)) && allowStrikeGiant(entity.getClass()) && (!(entity instanceof EntityTitan))) {
                        float smash = 100F - getDistance(entity);
                        if (smash <= 1F)
                            smash = 1F;
                        entity.attackEntityFrom(DamageSource.causeMobDamage(this), smash * 4F);
                        double d0 = getEntityBoundingBox().minX + getEntityBoundingBox().maxX;
                        double d1 = getEntityBoundingBox().minZ + getEntityBoundingBox().maxZ;
                        double d2 = entity.posX - d0;
                        double d3 = entity.posZ - d1;
                        double d4 = d2 * d2 + d3 * d3;
                        entity.addVelocity(d2 / d4 * 8.0D, 1.0D, d3 / d4 * 8.0D);
                    }
                }
            }
        }
    }

    protected void kill() {
        triggerAudioGiant("mob.endermen.portal", 100.0F, 0.6F);
        if (((world.provider instanceof WorldProviderEnd)) || ((world.provider instanceof WorldProviderVoid))) {
            setPosition(0.0D, 128.0D, 0.0D);
        }

        else if ((!(world.provider instanceof WorldProviderEnd)) && (!(world.provider instanceof WorldProviderVoid))) {
            setPosition(posX + (rand.nextDouble() - 0.5D) * 48.0D, 128.0D, posZ + (rand.nextDouble() - 0.5D) * 48.0D);
        }
    }

    protected int decreaseAirSupply(int arg70682n1) {
        return arg70682n1;
    }

    protected Entity locateToLookAtUser() {
        EntityPlayer entityplayer = world.getClosestPlayerToEntity(this, -1.0D);
        if (entityplayer != null && !world.isRemote && entityplayer.isEntityAlive() && isEntityAlive()) {
            if ((isRegisteredUser(entityplayer, irisRight) || isRegisteredUser(entityplayer, irisLeft)) && entityplayer.isEntityAlive()) {
                getLookHelper().setLookPositionWithEntity(entityplayer, 180F, 30F);
                setAttackTarget(entityplayer);
                if (entityplayer.getHeldItemMainhand() != null && entityplayer.getHeldItemMainhand().getItem() == TitanGear.ultimaBlade) {
                    attackEntityFrom(DamageSourceExtra.causePiercingMobHarmArmour(entityplayer), 600F);
                }

                else {
                    strikeMobChoosen(entityplayer, 200.0F, 100);
                    entityplayer.motionY = 1F;
                    entityplayer.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 400, 1));
                    entityplayer.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 400, 99));
                    entityplayer.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 400, 1));
                    setAttackTarget(entityplayer);
                    world.newExplosion(this, entityplayer.posX, entityplayer.posY, entityplayer.posZ, 8F, true, world.getGameRules().getBoolean("mobGriefing"));
                    triggerAudioGiant("thetitans:titanEnderColossusStare", Float.MAX_VALUE, 1F);
                }
            }
        }

        return null;
    }

    protected boolean isRegisteredUser(EntityPlayer arg70821n1, EntityTitanPart part) {
        Vec3d vec3 = arg70821n1.getLook(1.0F).normalize();
        Vec3d vec31 = new Vec3d(part.posX + (rand.nextDouble() * part.width - (part.width * 0.5)) - arg70821n1.posX, part.getEntityBoundingBox().minY + (rand.nextDouble() * part.height) - (arg70821n1.posY + arg70821n1.getEyeHeight()), part.posZ + (rand.nextDouble() * part.width - (part.width * 0.5)) - arg70821n1.posZ);
        double d0 = vec31.length();
        vec31 = vec31.normalize();
        double d1 = vec3.dotProduct(vec31);
        return d1 > 1.0D - 0.025D / d0 ? arg70821n1.canEntityBeSeen(this) : false;
    }

    public boolean allowHurtByUserBe() {
        return (stunnedIs) && getAnimID() != 5 && (!isEntityInvulnerable(DamageSource.GENERIC));
    }

    public double getSpeed() {
        return 0.4D + fetchPowerExtra() * 0.001D;
    }

    public boolean needsMove() {
        return !stunnedIs &&super.needsMove();
    }

    public boolean attackEntityAsMob(Entity arg70652n1) {
        return false;
    }

    public int getVerticalFaceSpeed() {
        return fetchLaserTicksEye() >= 0 ? 180 : 40;
    }

    public String getName() {
        switch (fetchVariantGiant()) {
            case 0:
                return "§6" + I18n.translateToLocal("entity.EndermanTitan.name") + "§f";
            case 1:
                return "§a" + I18n.translateToLocal("entity.EndermanTitanHoly.name") + "§f";
            case 2:
                return "§f" + I18n.translateToLocal("entity.EndermanTitanIcey.name") + "§f";
        }

        return null;
    }

    public void onLivingUpdate() {
        if (getAnimID() == 0)
            assignLaserTicksEye(fetchLaserTicksEye() - 1);
        if (getAttackTarget() != null && fetchLaserTicksEye() <= -400 && getAnimID() == 0 && !stunnedIs && !fetchWaiting() && rand.nextInt(40) == 0)
            assignLaserTicksEye(100);
        if (motionY > 1.5D)motionY *= 0.8D;
        if (!world.isRemote && getAttackTarget() != null && getAttackTarget() instanceof EntityWitherzilla && fetchCallBackUpCan()) {
            for (int i = 0; i < 24; i++) {
                EntityEnderColossus entitylargefireball = new EntityEnderColossus(world);
                entitylargefireball.assignWaiting(false);
                entitylargefireball.copyLocationAndAnglesFrom(this);
                entitylargefireball.setAttackTarget(getAttackTarget());
                world.spawnEntity(entitylargefireball);
                triggerAudioGiant(fetchAudioRoar(), getSoundVolume(), getSoundPitch());
            }

            assignCallBackUpCan(false);
        }

        if (crystalsDestroyed < 0)
            crystalsDestroyed = 0;
        if (crystalsDestroyed > 12) {
            crystalsDestroyed = 0;
            stunnedIs = true;
        }

        if (!isRiding() && !fetchWaiting() && !stunnedIs && getAnimID() == 0) {
            flyTitan(16F, 6F, 16D);
        }

        EntityPlayer player = world.getClosestPlayerToEntity(this, -1.0D);
        if ((player != null) && getAttackTarget() != null && (player == getAttackTarget())) {
            if ((rand.nextInt(200) == 0) && (getHealth() <= getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue() / 100.0F)) {
                player.attackEntityFrom(DamageSourceExtra.crushMind, Float.MAX_VALUE);
            }

            if ((player.getAbsorptionAmount() <= 0.0F) && (ticksExisted % 5 == 0)) {
                player.hurtResistantTime = 0;
                player.attackEntityFrom(DamageSourceExtra.crushMind, 12F);
                player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 400, 1));
                if (player.getHealth() <= 15.0F) {
                    player.motionY = 1.0D;
                    player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 400, 9));
                }

                if (player.getHealth() <= 5.0F) {
                    player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 400, 1));
                }
            }

            else if ((player.getAbsorptionAmount() >= 0.0F) && (ticksExisted % 20 == 0)) {
                player.attackEntityFrom(DamageSourceExtra.crushMind, 12F);
            }
        }

        if (getAnimID() == 10) {
            if ((getAnimTick() == 30) || (getAnimTick() == 70)) {
                playStepSound(new BlockPos(0, 0, 0), Blocks.STONE);
            }

            if ((getAnimTick() == 150) || (getAnimTick() == 230)) {
                triggerAudioGiant("thetitans:titanFall", 20.0F, 1.0F);
                triggerAudioGiant("thetitans:groundSmash", 20.0F, 1.0F);
                shakeUserCamerasNearby(10D);
                collideWithEntities(leftLeg, world.getEntitiesWithinAABBExcludingEntity(this, leftLeg.getEntityBoundingBox().grow(48.0D, 48.0D, 48.0D)));
                collideWithEntities(rightLeg, world.getEntitiesWithinAABBExcludingEntity(this, rightLeg.getEntityBoundingBox().grow(48.0D, 48.0D, 48.0D)));
                collideWithEntities(leftLeg, world.getEntitiesWithinAABBExcludingEntity(this, leftLeg.getEntityBoundingBox().grow(48.0D, 48.0D, 48.0D)));
                collideWithEntities(rightLeg, world.getEntitiesWithinAABBExcludingEntity(this, rightLeg.getEntityBoundingBox().grow(48.0D, 48.0D, 48.0D)));
                collideWithEntities(leftLeg, world.getEntitiesWithinAABBExcludingEntity(this, leftLeg.getEntityBoundingBox().grow(48.0D, 48.0D, 48.0D)));
                collideWithEntities(rightLeg, world.getEntitiesWithinAABBExcludingEntity(this, rightLeg.getEntityBoundingBox().grow(48.0D, 48.0D, 48.0D)));
                collideWithEntities(leftLeg, world.getEntitiesWithinAABBExcludingEntity(this, leftLeg.getEntityBoundingBox().grow(48.0D, 48.0D, 48.0D)));
                collideWithEntities(rightLeg, world.getEntitiesWithinAABBExcludingEntity(this, rightLeg.getEntityBoundingBox().grow(48.0D, 48.0D, 48.0D)));
            }

            if (getAnimTick() == 240) {
                triggerAudioGiant("thetitans:distantLargeFall", 10000.0F, 1.0F);
            }
        }

        if (ticksExisted % 120 == 0 && isEntityAlive()) {
            triggerAudioGiant("thetitans:titanEnderColossusLiving", getSoundVolume(), getSoundPitch());
        }

        if (ofCrystalsNum < 0) {
            ofCrystalsNum = 0;
        }

        for (int i = 0; i < getParticleCount() * 5 && com.electrifiedded.thetitans.util.perf.TitanParticles.allow(world); i++) {
            locateToLookAtUser();
            world.spawnParticle(EnumParticleTypes.PORTAL, posX + (rand.nextDouble() - 0.5D) * width * 3.0D, posY + rand.nextDouble() * height, posZ + (rand.nextDouble() - 0.5D) * width * 3.0D, 0.0D, 0.25D, 0.0D);
        }

        float fl = getBrightness();
        if ((fl > 0.5F) && world.provider.hasSkyLight() && (world.isDaytime()) && (!world.isRemote) && (ticksExisted % 1 == 0)) {
            rotationPitch = (rotationYawHead / 6.0F);
            rotationYawHead = -90.0F;
        }

        giantMelee = true;
        if (stunnedIs || deathTicks > 0) {
            motionX *= 0.0D;
            motionZ *= 0.0D;
        }

        for (int i = 0; i < 3; ++i) {
            if (getAnimID() == 2 && (getAnimTick() >= 40 && getAnimTick() <= 80) && (getAttackTarget() != null) && !world.isRemote) {
                for (int it = 0; it < 2; it++) {
                    double d2 = (getAttackTarget().posX + (getRNG().nextGaussian() * 64D));
                    double d3 = (getAttackTarget().posY + 150D + (getRNG().nextGaussian() * 32D));
                    double d4 = (getAttackTarget().posZ + (getRNG().nextGaussian() * 64D));
                    double d5 = getAttackTarget().posX - d2;
                    double d6 = getAttackTarget().posY - d3;
                    double d7 = getAttackTarget().posZ - d4;
                    EntityTitanFireball entitylargefireball1 = new EntityTitanFireball(world, this, d5, d6, d7, 4);
                    entitylargefireball1.posX = d2;
                    entitylargefireball1.posY = d3;
                    entitylargefireball1.posZ = d4;
                    world.spawnEntity(entitylargefireball1);
                    entitylargefireball1.assignIDFireball(4);
                }
            }
        }

        if ((getAnimID() == 3 && getAnimTick() == 20) || (getAnimID() == 4 && getAnimTick() == 10) || (getAnimID() == 11 && getAnimTick() == 10))
            triggerAudioGiant("thetitans:lightningCharge", 100F, 1F);
        if (getAnimID() == 3 && (getAnimTick() == 64)) {
            triggerAudioGiant("thetitans:lightningThrow", 100F, 1F);
            double d8 = 24D;
            Vec3d vec3 = getLook(1.0F);
            double dx = vec3.x * d8;
            double dz = vec3.z * d8;
            float da = (float)fetchAmountStrike(5.0F);
            int i1 = fetchAmountKnockback();
            world.addWeatherEffect(new EntityGammaLightning(world, posX + dx, posY + (isChild() ? 9D : 18D), posZ + dz, 1F, 0F, 1F));
            if (getAttackTarget() != null) {
                world.addWeatherEffect(new EntityGammaLightning(world, getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 1F, 0F, 1F));
                strikeMobChoosen(getAttackTarget(), da * 3.0F, i1);
                getAttackTarget().motionY += 1.0F + rand.nextFloat();
                world.newExplosion(this, getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 2.0F, false, false);
                world.newExplosion(this, posX + dx, posY + 26.0D, posZ + dz, 1.0F, false, false);
                getAttackTarget().attackEntityFrom(DamageSourceExtra.lightningBolt, da);
                List<?> found1 = world.getEntitiesWithinAABB(EntityLivingBase.class, getAttackTarget().getEntityBoundingBox().grow(4.0D, 4.0D, 4.0D), e -> e != getAttackTarget());
                if ((found1 != null) && (!found1.isEmpty())) {
                    for (int i11 = 0; i11 < found1.size(); i11++) {
                        Entity mobRef1 = (Entity)found1.get(i11);
                        if (((mobRef1 instanceof EntityLivingBase)) && allowStrikeGiant(mobRef1.getClass())) {
                            world.addWeatherEffect(new EntityGammaLightning(world, mobRef1.posX, mobRef1.posY, mobRef1.posZ, 1F, 0F, 1F));
                            strikeMobChoosen(mobRef1, da, 0);
                            if (!(mobRef1 instanceof EntityTitan))
                                mobRef1.motionY += 1.0F + rand.nextFloat();
                            List<?> found11 = world.getEntitiesWithinAABB(EntityLivingBase.class, mobRef1.getEntityBoundingBox().grow(4.0D, 4.0D, 4.0D), e -> e != mobRef1);
                            if ((found11 != null) && (!found11.isEmpty())) {
                                for (int i111 = 0; i111 < found11.size(); i111++) {
                                    Entity mobRef11 = (Entity)found11.get(i111);
                                    if (mobRef11 != mobRef1 && ((mobRef11 instanceof EntityLivingBase)) && allowStrikeGiant(mobRef11.getClass())) {
                                        world.addWeatherEffect(new EntityGammaLightning(world, mobRef11.posX, mobRef11.posY, mobRef11.posZ, 1F, 0F, 1F));
                                        strikeMobChoosen(mobRef11, da, 0);
                                        if (!(mobRef11 instanceof EntityTitan))
                                            mobRef11.motionY += 1.0F + rand.nextFloat();
                                        List<?> found111 = world.getEntitiesWithinAABB(EntityLivingBase.class, mobRef11.getEntityBoundingBox().grow(4.0D, 4.0D, 4.0D), e -> e != mobRef11);
                                        if ((found111 != null) && (!found111.isEmpty())) {
                                            for (int i1111 = 0; i1111 < found111.size(); i1111++) {
                                                Entity mobRef111 = (Entity)found111.get(i1111);
                                                if (mobRef111 != mobRef11 && ((mobRef111 instanceof EntityLivingBase)) && allowStrikeGiant(mobRef111.getClass())) {
                                                    world.addWeatherEffect(new EntityGammaLightning(world, mobRef111.posX, mobRef111.posY, mobRef111.posZ, 1F, 0F, 1F));
                                                    strikeMobChoosen(mobRef111, da, 0);
                                                    if (!(mobRef111 instanceof EntityTitan))
                                                        mobRef111.motionY += 1.0F + rand.nextFloat();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (getAnimID() == 4 && (getAnimTick() == 50) && (getAttackTarget() != null)) {
            triggerAudioGiant("thetitans:lightningThrow", 100F, 1F);
            double d8 = 24D;
            Vec3d vec3 = getLook(1.0F);
            double dx = vec3.x * d8;
            double dz = vec3.z * d8;
            world.newExplosion(this, posX + dx, posY + d8, posZ + dz, 1.0F, false, false);
            world.addWeatherEffect(new EntityGammaLightning(world, posX + dx, posY + d8, posZ + dz, 1F, 0F, 1F));
            world.newExplosion(this, getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 2.0F, false, false);
            getAttackTarget().attackEntityFrom(DamageSourceExtra.lightningBolt, 100F);
            world.addWeatherEffect(new EntityGammaLightning(world, getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 1F, 0F, 1F));
            double d5 = getAttackTarget().posX - posX;
            double d6 = getAttackTarget().posY - d8 - posY;
            double d7 = getAttackTarget().posZ - posZ;
            EntityLightningBall entitylargefireball = new EntityLightningBall(world, this, d5, d6, d7);
            entitylargefireball.posX = posX + vec3.x * d8;
            entitylargefireball.posY = posY + d8;
            entitylargefireball.posZ = posZ + vec3.z * d8;
            world.spawnEntity(entitylargefireball);
        }

        if (getAnimID() == 11 && (getAnimTick() == 50) && (getAttackTarget() != null) && !world.isRemote) {
            triggerAudioGiant("thetitans:lightningThrow", 100F, 1F);
            double d8 = 24D;
            Vec3d vec3 = getLook(1.0F);
            double dx = vec3.x * d8;
            double dz = vec3.z * d8;
            world.newExplosion(this, getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 2.0F, false, false);
            world.newExplosion(this, posX + dx, posY + d8, posZ + dz, 1.0F, false, false);
            getAttackTarget().attackEntityFrom(DamageSourceExtra.lightningBolt, 100F);
            world.addWeatherEffect(new EntityGammaLightning(world, posX + dx, posY + d8, posZ + dz, 1F, 0F, 1F));
            world.addWeatherEffect(new EntityGammaLightning(world, posX + dx, posY + d8, posZ + dz, 1F, 0F, 1F));
            world.addWeatherEffect(new EntityGammaLightning(world, posX + dx, posY + d8, posZ + dz, 1F, 0F, 1F));
            world.addWeatherEffect(new EntityGammaLightning(world, posX + dx, posY + d8, posZ + dz, 1F, 0F, 1F));
            world.addWeatherEffect(new EntityGammaLightning(world, getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 1F, 0F, 1F));
            EntityDragonMinion entitychicken = new EntityDragonMinion(world);
            entitychicken.freshVictimForce = false;
            entitychicken.targetX = getAttackTarget().posX;entitychicken.targetY = getAttackTarget().posY;entitychicken.targetZ = getAttackTarget().posZ;
            entitychicken.setLocationAndAngles(posX + dx, posY + d8, posZ + dz, rotationYaw, 0.0F);
            entitychicken.addVelocity(vec3.x * 10D, vec3.y * 10D, vec3.z * 10D);
            entitychicken.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
            entitychicken.setAttackTarget(getAttackTarget());
            world.spawnEntity(entitychicken);
            entitychicken.master = this;
            SoundHandler.whenPlayMob("mob.enderdragon.growl", world, entitychicken, 10.0F, 1.0F);
            SoundHandler.whenPlayMob("mob.enderdragon.growl", world, entitychicken, 10.0F, 1.0F);
            SoundHandler.whenPlayMob("mob.enderdragon.growl", world, entitychicken, 10.0F, 1.0F);
            SoundHandler.whenPlayMob("mob.enderdragon.growl", world, entitychicken, 10.0F, 1.0F);
            SoundHandler.whenPlayMob("mob.enderdragon.growl", world, entitychicken, 10.0F, 1.0F);
            SoundHandler.whenPlayMob("mob.enderdragon.growl", world, entitychicken, 10.0F, 1.0F);
            SoundHandler.whenPlayMob("mob.enderdragon.growl", world, entitychicken, 10.0F, 1.0F);
            SoundHandler.whenPlayMob("mob.enderdragon.growl", world, entitychicken, 10.0F, 1.0F);
            specialMinionsNum += 1;
        }

        if (getAnimID() == 13 && (getAnimTick() == 50) && (getAttackTarget() != null) && !world.isRemote) {
            float da = (float)fetchAmountStrike(15.0F);
            int i1 = fetchAmountKnockback();
            strikeMobChoosen(getAttackTarget(), da * 3.0F, i1);
            getAttackTarget().motionY += 1.0F + rand.nextFloat();
            world.newExplosion(this, getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 2.0F, false, false);
            getAttackTarget().attackEntityFrom(DamageSourceExtra.lightningBolt, da);
            world.addWeatherEffect(new EntityGammaLightning(world, getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 1F, 0F, 1F));
            List<?> found1 = world.getEntitiesWithinAABB(EntityLivingBase.class, getAttackTarget().getEntityBoundingBox().grow(12.0D, 12.0D, 12.0D), e -> e != getAttackTarget());
            if ((found1 != null) && (!found1.isEmpty())) {
                for (int i11 = 0; i11 < found1.size(); i11++) {
                    Entity mobRef1 = (Entity)found1.get(i11);
                    if (((mobRef1 instanceof EntityLivingBase)) && allowStrikeGiant(mobRef1.getClass())) {
                        world.addWeatherEffect(new EntityGammaLightning(world, mobRef1.posX, mobRef1.posY, mobRef1.posZ, 1F, 0F, 1F));
                        world.addWeatherEffect(new EntityGammaLightning(world, mobRef1.posX, mobRef1.posY, mobRef1.posZ, 1F, 0F, 1F));
                        world.addWeatherEffect(new EntityGammaLightning(world, mobRef1.posX, mobRef1.posY, mobRef1.posZ, 1F, 0F, 1F));
                        world.addWeatherEffect(new EntityGammaLightning(world, mobRef1.posX, mobRef1.posY, mobRef1.posZ, 1F, 0F, 1F));
                        strikeMobChoosen(mobRef1, da, 0);
                        if (!(mobRef1 instanceof EntityTitan))
                            mobRef1.motionY += 1.0F + rand.nextFloat();
                    }
                }
            }
        }

        if ((!AnimationAPI.isEffectiveClient()) && fetchLaserTicksEye() < 0 && (getAttackTarget() != null) && (!stunnedIs) && ((getAnimID() == 0))) {
            double d0 = getDistance(getAttackTarget());
            if (d0 < fetchReachMelee()) {
                switch (rand.nextInt(4)) {
                    case 0:AnimationAPI.sendAnimPacket(this, 6);
                        setAnimID(6);
                        break;
                    case 1:AnimationAPI.sendAnimPacket(this, 7);
                        setAnimID(7);
                        break;
                    case 2:AnimationAPI.sendAnimPacket(this, 9);
                        setAnimID(9);
                        break;
                    case 3:if (((getAttackTarget() instanceof EntityTitan)) || (getAttackTarget().height >= 6.0F) || (getAttackTarget().posY > posY + 6D)) {
                            AnimationAPI.sendAnimPacket(this, 1);
                            setAnimID(1);
                        }

                        else {
                            AnimationAPI.sendAnimPacket(this, 7);
                            setAnimID(7);
                        }

                        break;
                }
            }

            else if ((getRNG().nextInt(80) == 0)) {
                switch (rand.nextInt(6)) {
                    case 0:AnimationAPI.sendAnimPacket(this, 4);
                        setAnimID(4);
                        break;
                    case 1:if (rand.nextInt(25) == 0) {
                            AnimationAPI.sendAnimPacket(this, 5);
                            setAnimID(5);
                        }

                        else {
                            AnimationAPI.sendAnimPacket(this, 2);
                            setAnimID(2);
                        }

                        break;
                    case 2:AnimationAPI.sendAnimPacket(this, 3);
                        setAnimID(3);
                        break;
                    case 3:AnimationAPI.sendAnimPacket(this, 11);
                        setAnimID(11);
                        break;
                    case 4:AnimationAPI.sendAnimPacket(this, 13);
                        setAnimID(13);
                        break;
                    case 5:AnimationAPI.sendAnimPacket(this, 2);
                        setAnimID(2);
                        break;
                }
            }
        }

        if (getAnimID() == 8) {
            if (getAnimTick() == 15) {
                SoundHandler.whenPlayMob("random.explode", world, this, 10F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
                for (int i = 0; i < 50; ++i) {
                    world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, head.posX + (rand.nextDouble() - 0.5D) * (double)head.width, head.posY + rand.nextDouble() * (double)head.height, head.posZ + (rand.nextDouble() - 0.5D) * (double)head.width, 0D, 0D, 0D);
                    world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, head.posX + (rand.nextDouble() - 0.5D) * (double)head.width, head.posY + rand.nextDouble() * (double)head.height, head.posZ + (rand.nextDouble() - 0.5D) * (double)head.width, 0D, 0D, 0D);
                }
            }

            if (getAnimTick() == 20) {
                triggerAudioGiant("thetitans:titanEnderColossusScream", getSoundVolume(), 1.25F);
            }

            if (getAnimTick() == 90) {
                triggerAudioGiant("thetitans:titanFall", 20.0F, 1.0F);
                triggerAudioGiant("thetitans:groundSmash", 20.0F, 1.0F);
                shakeUserCamerasNearby(10D);
                collideWithEntities(leftLeg, world.getEntitiesWithinAABBExcludingEntity(this, leftLeg.getEntityBoundingBox().grow(48.0D, 48.0D, 48.0D)));
                collideWithEntities(rightLeg, world.getEntitiesWithinAABBExcludingEntity(this, rightLeg.getEntityBoundingBox().grow(48.0D, 48.0D, 48.0D)));
                collideWithEntities(leftLeg, world.getEntitiesWithinAABBExcludingEntity(this, leftLeg.getEntityBoundingBox().grow(48.0D, 48.0D, 48.0D)));
                collideWithEntities(rightLeg, world.getEntitiesWithinAABBExcludingEntity(this, rightLeg.getEntityBoundingBox().grow(48.0D, 48.0D, 48.0D)));
            }

            if (getAnimTick() >= 360) {
                stunnedIs = false;
            }

            else {
                setAttackTarget(null);
                stunnedIs = true;
            }
        }

        if (stunnedIs) {
            setAttackTarget(null);
            AnimationAPI.sendAnimPacket(this, 8);
        }

        if ((isWet()) && !isInWater() && getAnimID() != 5) {
            AnimationAPI.sendAnimPacket(this, 5);
            setAnimID(5);
        }

        setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Blocks.DRAGON_EGG));
        if ((getRidingEntity() != null) && ((getRidingEntity() instanceof EntityDragon))) {
            renderYawOffset = (rotationYaw = getRidingEntity().rotationYaw - 180.0F);
        }

        if ((rand.nextInt(400) == 0) && (getRidingEntity() == null) && (getAttackTarget() != null) && (getHealth() <= getMaxHealth() / 100.0F)) {
            EntityDragon entitydragon = new EntityDragon(world);
            int i = MathHelper.floor(posX);
            int j = MathHelper.floor(posY);
            int k = MathHelper.floor(posZ);
            entitydragon.setLocationAndAngles(i + 0.5D, j, k + 0.5D, rotationYaw, 0.0F);
            entitydragon.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entitydragon)), (IEntityLivingData)null);
            world.spawnEntity(entitydragon);
            SoundHandler.whenPlayMob("mob.enderdragon.growl", world, entitydragon, 10.0F, 1.0F);
            SoundHandler.whenPlayMob("mob.enderdragon.growl", world, entitydragon, 10.0F, 1.0F);
            SoundHandler.whenPlayMob("mob.enderdragon.growl", world, entitydragon, 10.0F, 1.0F);
            SoundHandler.whenPlayMob("mob.enderdragon.growl", world, entitydragon, 10.0F, 1.0F);
            SoundHandler.whenPlayMob("mob.enderdragon.growl", world, entitydragon, 10.0F, 1.0F);
            SoundHandler.whenPlayMob("mob.enderdragon.growl", world, entitydragon, 10.0F, 1.0F);
            SoundHandler.whenPlayMob("mob.enderdragon.growl", world, entitydragon, 10.0F, 1.0F);
            SoundHandler.whenPlayMob("mob.enderdragon.growl", world, entitydragon, 10.0F, 1.0F);
            startRiding(entitydragon);
        }


        if (world.isDaytime() && clockRoarcooldown < -1)
            clockRoarcooldown = -1;
        if (isEntityAlive())
            ++clockRoarcooldown;
        if (clockRoarcooldown == 0) {
            assignScreaming(true);
            if ((world.isDaytime()) && world.provider.hasSkyLight() && (!world.isRemote))
                world.setWorldTime(world.getWorldTime() + 14000L);
        }

        if (clockRoarcooldown >= 60 || !isEntityAlive()) {
            clockRoarcooldown = -(400 - rand.nextInt(200));
            assignScreaming(false);
        }

        super.onLivingUpdate();
    }

    protected void updateAITasks() {
        if (fetchLaserTicksEye() >= 0 && !world.isRemote && ticksExisted % 4 == 0) {
            for (int i = 0; i < 2; i++) {
                Vec3d vec3 = getLookVec();
                double x = i == 1 ? irisLeft.posX : irisRight.posX;
                double y = (i == 1 ? irisLeft.posY : irisRight.posY) + 1D;
                double z = i == 1 ? irisLeft.posZ : irisRight.posZ;
                double dx = x + vec3.x * 300D;
                double dy = y + vec3.y * 300D;
                double dz = z + vec3.z * 300D;
                double d0 = dx - x;
                double d1 = dy - y;
                double d2 = dz - z;
                if (getAttackTarget() != null) {
                    d0 = getAttackTarget().posX - x;
                    d1 = getAttackTarget().posY - y;
                    d2 = getAttackTarget().posZ - z;
                }

                EntitySkeletonTitanGiantArrow entityarrow = new EntitySkeletonTitanGiantArrow(world, this, d0, d1, d2);
                entityarrow.posX = x;
                entityarrow.posY = y;
                entityarrow.posZ = z;
                world.spawnEntity(entityarrow);
                entityarrow.setInvisible(true);
            }
        }

        if ((ticksExisted % 20 == 0) && (ofCrystalsNum < numOfCrystalsCap) && (!world.isRemote)) {
            EntityEnderColossusCrystal entitychicken = new EntityEnderColossusCrystal(world);
            entitychicken.setLocationAndAngles(posX, posY + (height * 2), posZ, rotationYawHead, 0.0F);
            entitychicken.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
            world.spawnEntity(entitychicken);
            ofCrystalsNum += 1;
            entitychicken.owner = this;
            entitychicken.motionY = 2D;
            world.createExplosion(entitychicken, entitychicken.posX, entitychicken.posY, entitychicken.posZ, 6.0F, false);
        }

        super.updateAITasks();
    }

    protected String fetchAudioRoar() {
        return "thetitans:titanEnderColossusRoar";
    }

    protected String fetchLivingAudioGiant() {
        return null;
    }

    protected String fetchHurtAudioGiant() {
        return "thetitans:titanEnderColossusGrunt";
    }

    protected String fetchDeathAudioGiant() {
        return "thetitans:titanEnderColossusDeath";
    }

    protected void playStepSound(BlockPos pos, Block block) {
        triggerAudioGiant("thetitans:titanStep", 10.0F, 0.85F);
        shakeUserCamerasNearby(8D);
        float f3 = rotationYaw * (float)Math.PI / 180.0F;
        float f11 = MathHelper.sin(f3);
        float f4 = MathHelper.cos(f3);
        collideWithEntities(leftLeg, world.getEntitiesWithinAABBExcludingEntity(this, leftLeg.getEntityBoundingBox().grow(1.0D, 1.0D, 1.0D).offset((double)(f11 * 10F), 0, (double)(f4 * 10F))));
        collideWithEntities(rightLeg, world.getEntitiesWithinAABBExcludingEntity(this, rightLeg.getEntityBoundingBox().grow(1.0D, 1.0D, 1.0D).offset((double)(f11 * 10F), 0, (double)(f4 * 10F))));
    }

    protected Item getDropItem() {
        return Items.ENDER_EYE;
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        if (deathTicks > 0) {
            for (int x = 0; x < 90; x++) {
                EntityXPBomb entitylargefireball = new EntityXPBomb(world, posX, posY + 4D, posZ);
                entitylargefireball.setPosition(posX, posY + 4D, posZ);
                ++entitylargefireball.motionY;
                entitylargefireball.assignTotalXP(26000);
                world.spawnEntity(entitylargefireball);
            }

            for (int x = 0; x < 16; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.ENDER_EYE));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 16; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.ENDER_PEARL));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 12; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.END_STONE));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 12; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.OBSIDIAN));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 8; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.COAL_BLOCK));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 6; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.IRON_BLOCK));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 4; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.GOLD_BLOCK));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.EMERALD_BLOCK));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.DIAMOND_BLOCK));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(TitanBlockSet.harcadium_block));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(TitanGear.stackVoid));
                itembomb.assignTotalStack(32 + rand.nextInt(32 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.BEDROCK));
                itembomb.assignTotalStack(16 + rand.nextInt(16 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.GOLDEN_APPLE, 1, 1));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.DRAGON_EGG));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 5; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.END_PORTAL_FRAME));
                itembomb.assignTotalStack(12);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(TitanGear.voidSword));
                itembomb.assignTotalStack(1);
                world.spawnEntity(itembomb);
            }
        }
    }

    protected void shedDropRare(int arg70600n1) {
        switch (rand.nextInt(3)) {
            case 0:entityDropItem(new ItemStack(Items.GOLDEN_APPLE, 64, 1), 0.0F);
                break;
            case 1:dropItem(Items.DIAMOND_SWORD, 64);
                break;
            case 2:dropItem(Items.NAME_TAG, 64);
        }
    }

    public String fetchAchievement() {
        return TitanTrophies.endercolossus;
    }

    public float getEyeHeight() {
        return 0.9F * height;
    }

    public boolean isScreaming() {
        return this.dataManager.get(SCREAMING).byteValue() > 0;
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }

    public float getBrightness() {
        return 1.0F;
    }

    public void assignScreaming(boolean arg70819n1) {
        if (!world.isRemote)
            this.dataManager.set(SCREAMING, Byte.valueOf((byte)(arg70819n1 ? 1 : 0)));
        if (arg70819n1) {
            triggerAudioGiant(fetchAudioRoar(), getSoundVolume(), 1.0F);
        }
    }

    public IEntityLivingData onInitialSpawn(net.minecraft.world.DifficultyInstance difficulty, IEntityLivingData arg180482n2) {
        Object p_180482_2_1 = super.onInitialSpawn(difficulty, arg180482n2);
        triggerAudioGiant(fetchAudioRoar(), getSoundVolume(), 1.0F);
        assignScreaming(true);
        assignCallBackUpCan(true);
        assignCooldownTimerRoar(-20 - rand.nextInt(20));
        List<?> found11 = world.getEntitiesWithinAABB(EntityWitherzilla.class, getEntityBoundingBox().grow(256.0D, 256.0D, 256.0D));
        if ((found11 != null) && (!found11.isEmpty())) {
            for (int i1 = 0; i1 < found11.size(); i1++) {
                Entity entity = (Entity)found11.get(i1);
                if ((entity != null) && ((entity instanceof EntityWitherzilla))) {
                    List<?> found111 = world.getEntitiesWithinAABB(EntityPlayer.class, entity.getEntityBoundingBox().grow(256.0D, 256.0D, 256.0D), e -> e != entity);
                    if ((found111 != null) && (!found111.isEmpty())) {
                        for (int i11 = 0; i11 < found111.size(); i11++) {
                            Entity mobRef1 = (Entity)found111.get(i11);
                            if ((mobRef1 != null) && ((mobRef1 instanceof EntityPlayer))) {
                                ((EntityPlayer)mobRef1).sendMessage(new TextComponentString(I18n.translateToLocal("dialog.witherzilla.endercolossus")));
                            }
                        }
                    }
                }
            }
        }

        return (IEntityLivingData)p_180482_2_1;
    }

    protected boolean canDespawn() {
        return false;
    }

    public double getYOffset() {
        return super.getYOffset() - 1.0D;
    }

    protected void inactActionDeath() {
        if (!world.isRemote) {
            triggerAudioGiant("mob.endermen.death", getSoundVolume(), getSoundPitch());
            if ((world.getGameRules().getBoolean("doMobLoot"))) {
                dropFewItems(true, 0);
                dropEquipment(true, 0);
                shedDropRare(1);
            }

            EntityTitanSpirit entitytitan = new EntityTitanSpirit(world);
            entitytitan.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
            world.spawnEntity(entitytitan);
            entitytitan.assignHuntingVessel(false);
            entitytitan.assignKindSpirit(10);
            entitytitan.setSubType(fetchVariantGiant());
        }
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
            float f = (rand.nextFloat() - 0.5F) * 12.0F;
            float f1 = (rand.nextFloat() - 0.5F) * 3.0F;
            float f2 = (rand.nextFloat() - 0.5F) * 12.0F;
            world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX + f, posY + 2.0D + f1, posZ + f2, 0.0D, 0.0D, 0.0D);
        }

        if (getInvulTime() >= fetchHoldThreash()) {
            setDead();
        }
    }

    public int fetchCooldownTimerRoar() {
        return clockRoarcooldown;
    }

    public void assignCooldownTimerRoar(int clockRoarcooldown) {
        this.clockRoarcooldown = clockRoarcooldown;
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getTrueSource() instanceof EntityPlayer && !world.isRemote) {
            setAttackTarget((EntityLivingBase) source.getTrueSource());
            setRevengeTarget((EntityLivingBase) source.getTrueSource());
        }

        if (isEntityInvulnerable(source) || getAnimID() == 5) {
            return false;
        }

        else if (((source.getTrueSource() instanceof EntityEndermanMinion)) || ((source.getTrueSource() instanceof EntityEnderColossus)) || ((source.getTrueSource() instanceof EntityDragon)) || ((source.getTrueSource() instanceof EntityDragonMinion))) {
            return false;
        }

        else {
            return super.attackEntityFrom(source, amount);
        }
    }

    public boolean attackEntityFromPart(EntityTitanPart arg70965n1, DamageSource source, float amount) {
        attackEntityFrom(source, amount);
        return true;
    }

    protected EntityLiving fetchMinion() {
        return new EntityEndermanMinion(world);
    }

    protected double cap() {
        return super.cap();
    }
}
