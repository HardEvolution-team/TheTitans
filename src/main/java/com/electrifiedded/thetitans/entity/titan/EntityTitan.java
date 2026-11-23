package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.util.perf.TitanProfiler;

import com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation;
import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thehippomaster.AnimationAPI.IAnimatedEntity;
import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.config.TitanConfig;
import com.electrifiedded.thetitans.core.TheCore;
import com.electrifiedded.thetitans.data.TitanFactions;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.minion.EntityDragonMinion;
import com.electrifiedded.thetitans.entity.minion.EntityGiantZombieBetter;
import com.electrifiedded.thetitans.entity.minion.EntityWitherMinion;
import com.electrifiedded.thetitans.entity.minion.EnumMinionType;
import com.electrifiedded.thetitans.entity.minion.IMinion;
import com.electrifiedded.thetitans.handler.ChunkLoadHooks;
import com.electrifiedded.thetitans.handler.EventStore;
import com.electrifiedded.thetitans.handler.TitanAscension;
import com.electrifiedded.thetitans.proxy.CommonProxy;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.sound.SoundHandler;
import com.electrifiedded.thetitans.world.dimension.WorldProviderVoid;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class EntityTitan extends EntityCreature implements ITitan, ITitanHitbox {
    public final List<EntityTitanPart> setPiece = new ArrayList<EntityTitanPart>();
    // replaces 1.7.10 IBossDisplayData marker. Base titan owns one bar; all titan subclasses inherit it, minions (vanilla-mob-based) don't. Name/percent refreshed each tick in onUpdate.
    protected final BossInfoServer bossInfo = (BossInfoServer)(new BossInfoServer(getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(false);
    private static final DataParameter<Integer> KEY_MOTION = EntityDataManager.createKey(EntityTitan.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> BEAT_MOTION = EntityDataManager.createKey(EntityTitan.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> TICKS_INVUL = EntityDataManager.createKey(EntityTitan.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> POWER_EXTRA = EntityDataManager.createKey(EntityTitan.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> N_22_UNUSED = EntityDataManager.createKey(EntityTitan.class, DataSerializers.VARINT);
    private static final DataParameter<Byte> WAITING = EntityDataManager.createKey(EntityTitan.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> KEY_ATAA = EntityDataManager.createKey(EntityTitan.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> VARIANT_GIANT = EntityDataManager.createKey(EntityTitan.class, DataSerializers.VARINT);
    private static final DataParameter<Float> STAMINA_V = EntityDataManager.createKey(EntityTitan.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> STAMINA_CAP = EntityDataManager.createKey(EntityTitan.class, DataSerializers.FLOAT);

    protected int beatsStun;
    public int deathTicks;
    public boolean stunnedIs;
    protected boolean soulessIs;
    public boolean particlesBeUpwardNeeds;
    public boolean rejuvinatingIs;
    public boolean isFlying;
    public boolean giantMelee;
    protected int stepGapGiantNext;
    protected int minionsNum;
    protected int priestsNum;
    protected int zealotsNum;
    protected int bishopNum;
    protected int templarNum;
    protected int lordsNum;
    protected int specialMinionsNum;
    public int animTick;
    public int keyFoot;
    public double pointXMove;
    public double pointZMove;

    public EntityTitan(World worldIn) {
        super(worldIn);
        setSize(1F, 6F);
        enablePersistence();
        setHealth(getMaxHealth());
        applyEntityAI();
        stunnedIs = false;
        isImmuneToFire = true;
        ignoreFrustumCheck = true;
        pointXMove = posX;
        pointZMove = posZ;
        soulessIs = false;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        // titan HP is plain vanilla health (float). The 1024 cap on generic.maxHealth is lifted by
        // mixin.MixinRangedAttribute; the real max is synced from fetchAmountHp() in syncHpMax().
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(2000.0D);
        getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(512D);
    }

    protected void entityInit() {
        super.entityInit();
        dataManager.register(KEY_MOTION, Integer.valueOf(0));
        dataManager.register(BEAT_MOTION, Integer.valueOf(0));
        dataManager.register(TICKS_INVUL, Integer.valueOf(0));
        dataManager.register(POWER_EXTRA, Integer.valueOf(0));
        dataManager.register(N_22_UNUSED, Integer.valueOf(0));
        dataManager.register(WAITING, Byte.valueOf((byte)0));
        dataManager.register(KEY_ATAA, Integer.valueOf(0));
        dataManager.register(VARIANT_GIANT, Integer.valueOf(0));
        dataManager.register(STAMINA_V, Float.valueOf(20.0F));
        dataManager.register(STAMINA_CAP, Float.valueOf(20.0F));
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (isSummonedGiant())
            experienceValue = 0;
        setAnimID(tagCompund.getInteger("AnimationId"));
        setAnimTick(tagCompund.getInteger("AnimationTick"));
        assignVariantGiant(tagCompund.getInteger("Variant"));
        assignATAAID(tagCompund.getInteger("ATAAID"));
        assignPowerExtra(tagCompund.getInteger("ExtraPower"));
        setInvulTime(tagCompund.getInteger("Invul"));
        assignWaiting(tagCompund.getBoolean("Waiting"));
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("AnimationId", getAnimID());
        tagCompound.setInteger("AnimationTick", getAnimTick());
        tagCompound.setInteger("Variant", fetchVariantGiant());
        tagCompound.setInteger("ATAAID", fetchATAAID());
        tagCompound.setInteger("ExtraPower", fetchPowerExtra());
        tagCompound.setInteger("Invul", getInvulTime());
        tagCompound.setBoolean("Waiting", fetchWaiting());
    }

    public void onUpdate() {
        long perfT0 = TitanProfiler.start();
        try {
            if (dormantStatueTick())
                return;
            onUpdateTitan();
        } finally {
            TitanProfiler.stop(getClass(), TitanProfiler.Sec.TOTAL, perfT0);
        }
    }

    /**
     * PERF — a dormant titan standing still on the ground is a statue: nothing in its tick can change except the
     * awakening check, which allowAwaken() only runs when (ticksExisted + id) % 10 == 0. So the full tick (AI, body
     * part queries, move, collision, boss bar) runs on exactly those ticks and the other 9 of 10 are skipped.
     * Anything that disturbs it (damage, rider, target, airborne, motion) drops it back to the full tick.
     */
    private boolean dormantStatueTick() {
        if (!TitanConfig.dormantFastTick || !fetchWaiting() || !onGround || isDead || getHealth() <= 0F || hurtTime > 0
            || deathTime > 0 || getInvulTime() > 0 || getAttackTarget() != null || isRiding() || isBeingRidden()
            || motionY > 0D || (ticksExisted + getEntityId()) % 10 == 0 || ticksExisted < 20)
            return false;
        prevPosX = posX; prevPosY = posY; prevPosZ = posZ;
        prevRotationYaw = rotationYaw; prevRotationPitch = rotationPitch;
        prevRotationYawHead = rotationYawHead; prevRenderYawOffset = renderYawOffset;
        motionX = 0D; motionZ = 0D;
        if (hurtResistantTime > 0)
            --hurtResistantTime;
        return true;
    }

    private void onUpdateTitan() {
        world.profiler.startSection("onTitanUpdateLiving");
        super.onUpdate();

        if (isDead && getHealth() > 0.0F && isServer()) {
            isDead = false;
            deathTime = 0;
        }

        if (getMaxHealth() / 4 > fetchStaminaMax()) {
            assignStaminaMax(getMaxHealth() / 4);
            assignStamina(getMaxHealth() / 4);
        }

        if (!stunnedIs && fetchStamina() < fetchStaminaMax())
            assignStamina(fetchStamina() + (fetchStaminaMax() / 6000));

        if (getHealth() <= 0F)
            assignStamina(0.0F);

        if (isServer() && !fetchWaiting() && getAnimID() != 0 && deathTicks < fetchHoldThreash()) {
            setAnimTick(getAnimTick() + 1);

            if (isChild() && isEntityAlive())
                setAnimTick(getAnimTick() + 1);
        }

        if (stunnedIs && maxHurtResistantTime != 5)
            maxHurtResistantTime = 5;
        else if (!stunnedIs && maxHurtResistantTime != 25)
            maxHurtResistantTime = 25;

        if (onGround && isFlying)
            isFlying = false;

        if (!world.isRemote && !fetchWaiting())
            ChunkLoadHooks.refreshLoaded(this);

        if (isServer()) {
            float max = getMaxHealth();
            bossInfo.setPercent(max > 0F ? MathHelper.clamp(getHealth() / max, 0.0F, 1.0F) : 0.0F);
            bossInfo.setName(getDisplayName());
            bossInfo.setVisible(!fetchWaiting());
        }

        world.profiler.endSection();
    }

    // 1.12.2 boss-bar sync hooks (replace IBossDisplayData). Vanilla handles client rendering; no client code needed.
    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        bossInfo.addPlayer(player);
    }

    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        bossInfo.removePlayer(player);
    }

    protected void whenUpdateStun() {
        beatsStun ++;

        if (beatsStun >= recoveryTicks()) {
            beatsStun = 0;
            stunnedIs = false;
            assignStamina(fetchStaminaMax());
        }
    }

    public void onLivingUpdate() {
        long perfT = TitanProfiler.start();
        whenUpdateAnimation();
        TitanProfiler.stop(getClass(), TitanProfiler.Sec.ANIM, perfT);
        perfT = TitanProfiler.start();
        whenUpdateBounds();
        TitanProfiler.stop(getClass(), TitanProfiler.Sec.BOUNDS, perfT);
        whenUpdateMinion();

        if (stunnedIs)
            whenUpdateStun();

        if (fetchStamina() <= 0)
            stunnedIs = true;

        if (isServer())
            syncHpMax();

        if (getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue() != fetchAmountStrike(1.0F))
            getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(fetchAmountStrike(1.0F));
        if (getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getBaseValue() != (16D * fetchScaleMultiplierGiant()))
            getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16D * fetchScaleMultiplierGiant());

        if (getAttackTarget() != null && getAttackTarget() instanceof EntityLiving && isSpawnBossToExemptOre(getAttackTarget())) {
            ((EntityLiving)getAttackTarget()).setAttackTarget(this);
            ((EntityLiving)getAttackTarget()).getLookHelper().setLookPositionWithEntity(this, 180F, 180F);
        }

        isAirBorne = !onGround;

        if (motionX > 3D)
            motionX = 3D;
        if (motionZ > 3D)
            motionZ = 3D;
        if (motionX < -3D)
            motionX = -3D;
        if (motionZ < -3D)
            motionZ = -3D;

        // PERF — world.getCurrentDate() builds a fresh Calendar (timezone math) per titan per tick; cached.
        datesRefresh(world);
        if (!isDead && marchFirstIs) {
            motionY += rand.nextDouble();
            noClip = true;
            rotationYawHead += 20F;
            rotationPitch += 10F;
        }

        if (octoberIs && !fetchWaiting() && isServer() && world.isDaytime() && !rejuvinatingIs && (!(this instanceof EntitySlimeTitan) && !(this instanceof EntitySnowGolemTitan) && !(this instanceof EntityIronGolemTitan) && !(this instanceof EntityGargoyleTitan)))
            world.setWorldTime(world.getWorldTime() + 50);

        if (getAttackTarget() != null) {
            if((!getAttackTarget().isEntityAlive() || getAttackTarget().posY > 256 || getAttackTarget().posY < -45))
                setAttackTarget(null);
            if (TitanConfig.antiCheatIs && getAttackTarget() instanceof EntityLiving && !(getAttackTarget() instanceof EntityTitanSpirit) && !(getAttackTarget() instanceof EntityTitan) && getAttackTarget().getMaxHealth() > 1000000000F) {
                if (isClient())
                    SoundHandler.whenPlayMob("random.explode", world, getAttackTarget(), 2F, 1F + rand.nextFloat());
                hurtMobBypass(getAttackTarget(), new DamageSource("infinity").setDamageBypassesArmor().setDamageAllowedInCreativeMode().setDamageIsAbsolute(), getAttackTarget().getHealth() / 2F);
                if (getAttackTarget().getHealth() <= 1F) {
                    List<?> found11 = world.getEntitiesWithinAABB(EntityLivingBase.class, getAttackTarget().getEntityBoundingBox().expand(7, 7, 7), e -> e != getAttackTarget());
                    if ((found11 != null) && (!found11.isEmpty())) {
                        for (int i1 = 0; i1 < found11.size(); i1++) {
                            Entity entity = (Entity)found11.get(i1);
                            if (entity instanceof EntityLivingBase && isClient())
                                ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(CommonProxy.electricJudgment, Integer.MAX_VALUE, 9));
                        }
                    }

                    world.createExplosion(this, getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 14F, false);
                    getAttackTarget().setDead();
                }
            }

            // BUGFIX — spectators also have disableDamage without creative; they were being kicked.
            if (TitanConfig.antiCheatIs && isServer() && getAttackTarget() instanceof EntityPlayerMP)
                if (((EntityPlayer)getAttackTarget()).capabilities.disableDamage && !((EntityPlayer)getAttackTarget()).capabilities.isCreativeMode && !((EntityPlayer)getAttackTarget()).isSpectator())
                ((EntityPlayerMP)getAttackTarget()).connection.disconnect(new TextComponentString(getName() + " has detected you not taking normal damage out of creative, and kicked you for it."));
        }

        if (getRidingEntity() != null)
            renderYawOffset = rotationYaw = getRidingEntity().rotationYaw;
        if (getAnimID() == 0) setAnimTick(0);
        else renderYawOffset = rotationYaw = rotationYawHead;
        if (fetchWaiting()) {
            motionX = 0D;
            motionZ = 0D;
            if (motionY > 0D)
                motionY = 0D;
        }

        if (isServer() && getAnimID() == 0 && getAttackTarget() != null && needsMove() && !(this instanceof EntityGhastTitan))
            shiftToPointGiant(getAttackTarget().posX, getAttackTarget().posZ, false);

        if (deathTime == 1)
            triggerAudioGiant(fetchDeathAudioGiant(), getSoundVolume(), getSoundPitch());

        if (getHealth() <= 0.0F)
            whenDeathUpdateGiant();

        if (TheTitans.ffaModeTitans && getRidingEntity() != null)
            dismountRidingEntity();

        if (getAnimID() < 0)
            setAnimID(0);

        if (getHealth() > 0.0F) {
            deathTicks = 0;

            if (getAnimID() == 10)
                setAnimID(0);
        }

        if (posY > 300.0D) {
            motionY = 0F;
            setPosition(posX + (rand.nextFloat() * 32.0F - 16.0F), 60.0D, posZ + (rand.nextFloat() * 32.0F - 16.0F));
        }

        if (posY <= 0.0D) {
            setPosition(posX, 0.0D, posZ);
            onGround = true;
            isAirBorne = false;
            fallDistance = 0F;

            if (motionY < 0D)
                motionY = 0D;
        }

        if (minionsNum < 0) minionsNum = 0;
        if (priestsNum < 0) priestsNum = 0;
        if (zealotsNum < 0) zealotsNum = 0;
        if (bishopNum < 0) bishopNum = 0;
        if (templarNum < 0) templarNum = 0;
        if (lordsNum < 0) lordsNum = 0;
        if (specialMinionsNum < 0) specialMinionsNum = 0;
        if ((getHealth() <= 0.0F) && (motionY > 0.0D)) motionY = 0.0D;
        if (getAnimTick() < 0) setAnimTick(0);

        isInWeb = false;
        lastDamage = 2.14748365E9F;
        stepHeight = (height / 2.0F);
        updateArmSwingProgress();
        ignoreFrustumCheck = true;

        if (getAttackTarget() != null && getAnimID() == 0)
            getLookHelper().setLookPositionWithEntity(getAttackTarget(), 64F / fetchScaleMultiplierGiant(), getVerticalFaceSpeed());

        // PERF — footstep dust is client-only (server spawnParticle is a no-op); skip 30 block reads server-side.
        if (isClient() && ticksExisted % 10 == 0)
            for (int u = 0; u < 30; u++)
            if ((motionX != 0.0D) && (motionZ != 0.0D) && (onGround)) {
            int i = MathHelper.floor(posX + (rand.nextDouble() * width - width / 2.0F));
            int j = MathHelper.floor(posY - 0.20000000298023224D);
            int k = MathHelper.floor(posZ + (rand.nextDouble() * width - width / 2.0F));
            BlockPos pos = new BlockPos(i, j, k);
            IBlockState state = world.getBlockState(pos);
            if (state.getMaterial() != Material.AIR)
                world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, posX + (rand.nextFloat() - 0.5D) * width, getEntityBoundingBox().minY + 0.1D, posZ + (rand.nextFloat() - 0.5D) * width, 4.0D * (rand.nextFloat() - 0.5D), 0.5D, (rand.nextFloat() - 0.5D) * 4.0D, Block.getStateId(state));
        }

        super.onLivingUpdate();
        if (getAttackTarget() != null && getAnimID() == 0 && (ticksExisted % 30 == 0) && (allowStrike())) {
            double d0 = getDistance(getAttackTarget());
            if (d0 < fetchReachMelee()) {
                swingArm(EnumHand.MAIN_HAND);
                getLookHelper().setLookPositionWithEntity(getAttackTarget(), 5.0F, getVerticalFaceSpeed());
                renderYawOffset = rotationYaw = rotationYawHead;
                attackEntityAsMob(getAttackTarget());
            }
        }

        if (getAttackTarget() != null && (getAttackTarget() == getRidingEntity() || getPassengers().contains(getAttackTarget())))
            setAttackTarget(null);

        if (isClient() && ticksExisted % 2 == 0 && (deathTicks < fetchHoldThreash()) && (!(this instanceof EntityWitherzilla))) {
            for (int i = 0; i < getParticleCount() && com.electrifiedded.thetitans.util.perf.TitanParticles.allow(world); i++) {
                if (particlesBeUpwardNeeds)
                    world.spawnParticle(forParticle(fetchParticle()), posX + (rand.nextDouble() - 0.5D) * width, posY + rand.nextDouble() * height, posZ + (rand.nextDouble() - 0.5D) * width, 0.0D, 0.25D, 0.0D);
                else
                    world.spawnParticle(forParticle(fetchParticle()), posX + (rand.nextDouble() - 0.5D) * width, posY + rand.nextDouble() * height, posZ + (rand.nextDouble() - 0.5D) * width, 0.0D, 0.0D, 0.0D);
            }

            if (TheTitans.isDifficulty(world, TheCore.IMPOSSIBLE) && ticksExisted % 2 == 0 && (rand.nextInt(5) == 0))
                for (int i = 0; i < 5; i++)
                world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, posX + (rand.nextDouble() - 0.5D) * width, posY, posZ + (rand.nextDouble() - 0.5D) * width, 0.0D, 0.0D, 0.0D);
            if (TheTitans.isDifficulty(world, TheCore.NIGHTMARE_V) && ticksExisted % 2 == 0 && (rand.nextInt(20) == 0))
                for (int i = 0; i < 5; i++) {
                world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, posX + (rand.nextDouble() - 0.5D) * width, posY + rand.nextDouble() * height, posZ + (rand.nextDouble() - 0.5D) * width, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.FLAME, posX + (rand.nextDouble() - 0.5D) * width, posY + rand.nextDouble() * height, posZ + (rand.nextDouble() - 0.5D) * width, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.FLAME, posX + (rand.nextDouble() - 0.5D) * width, posY + rand.nextDouble() * height, posZ + (rand.nextDouble() - 0.5D) * width, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.LAVA, posX + (rand.nextDouble() - 0.5D) * width, posY + rand.nextDouble() * height, posZ + (rand.nextDouble() - 0.5D) * width, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX + (rand.nextDouble() - 0.5D) * width, posY + rand.nextDouble() * height, posZ + (rand.nextDouble() - 0.5D) * width, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX + (rand.nextDouble() - 0.5D) * width, posY + rand.nextDouble() * height, posZ + (rand.nextDouble() - 0.5D) * width, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    private EnumParticleTypes forParticle(String s) {
        if ("fireworksSpark".equals(s)) return EnumParticleTypes.FIREWORKS_SPARK;
        if ("magicCrit".equals(s)) return EnumParticleTypes.CRIT_MAGIC;
        if ("crit".equals(s)) return EnumParticleTypes.CRIT;
        if ("largeexplode".equals(s)) return EnumParticleTypes.EXPLOSION_LARGE;
        if ("flame".equals(s)) return EnumParticleTypes.FLAME;
        if ("lava".equals(s)) return EnumParticleTypes.LAVA;
        return EnumParticleTypes.ENCHANTMENT_TABLE;
    }

    private static long checkedAtDates = Long.MIN_VALUE;
    private static boolean marchFirstIs;
    private static boolean octoberIs;

    private static void datesRefresh(World world) {
        long now = System.currentTimeMillis();
        if (now - checkedAtDates < 60000L)
            return;
        checkedAtDates = now;
        Calendar calendar = world.getCurrentDate();
        marchFirstIs = calendar.get(Calendar.MONTH) == Calendar.MARCH && calendar.get(Calendar.DAY_OF_MONTH) == 1;
        octoberIs = calendar.get(Calendar.MONTH) == Calendar.OCTOBER;
    }

    // ---- Awakening -------------------------------------------------------------------------------------------

    /**Server-side, throttled: true once the world progression unlocks titans and (if configured) a player is near.*/
    protected boolean allowAwaken() {
        if (!isServer() || (ticksExisted + getEntityId()) % 10 != 0 || !com.electrifiedded.thetitans.handler.TitanAscension.isUnlocked(world))
            return false;
        int radius = TitanConfig.wakeSpanGiant;
        return radius <= 0 || world.getClosestPlayerToEntity(this, radius) != null;
    }

    /**Wakes a dormant titan, playing its rise-up animation when it has one.*/
    public void awaken() {
        if (!fetchWaiting())
            return;
        assignWaiting(false);
        if (hasAnimationAwakening()) {
            if (this instanceof com.electrifiedded.thehippomaster.AnimationAPI.IAnimatedEntity)
                AnimationAPI.sendAnimPacket((com.electrifiedded.thehippomaster.AnimationAPI.IAnimatedEntity)this, 13);
            else
                setAnimID(13);
            setAnimTick(0);
        } else if (getAnimID() == 13)
            setAnimID(0);
    }

    protected boolean hasAnimationAwakening() {
        for (net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry entry : tasks.taskEntries)
            if (entry.action instanceof com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation && ((com.electrifiedded.thehippomaster.AnimationAPI.AIAnimation)entry.action).getAnimID() == 13)
            return true;
        return false;
    }

    // ---- Physics fast paths (see TitanPhysics / mixin.MixinWorld) --------------------------------------------

    @Override
    public void move(net.minecraft.entity.MoverType type, double x, double y, double z) {
        boolean remote = world.isRemote;
        long perfT = TitanProfiler.start();
        TitanPhysics.moveEnter(remote);
        try {
            super.move(type, x, y, z);
        } finally {
            TitanPhysics.moveExit(remote);
            TitanProfiler.stop(getClass(), TitanProfiler.Sec.MOVE, perfT);
        }
    }

    /**PERF — titans never interact with pressure plates/portals/webs/cactus; vanilla walked every block in the body AABB.*/
    @Override
    protected void doBlockCollisions() {
    }

    /**PERF — titans are fire immune and processMovementLava() is false; vanilla scanned the whole body AABB for lava every tick.*/
    @Override
    public boolean isInLava() {
        return false;
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        // PERF — a dormant statue titan standing on the ground has nothing to simulate. Re-check the ground
        // once a second so it still falls if the floor is dug out from under it.
        if (fetchWaiting() && onGround && motionY <= 0.0D && (ticksExisted + getEntityId()) % 20 != 0) {
            motionX = 0.0D;
            motionZ = 0.0D;
            return;
        }

        super.travel(strafe, vertical, forward);
    }

    protected void onDeathUpdate() {}

    @SuppressWarnings("unchecked")
    protected void whenDeathUpdateGiant() {
        if (this instanceof EntitySnowGolemTitan || this instanceof EntitySlimeTitan) {
            ++deathTime;
            destroyBlocksInAABB(getEntityBoundingBox());
            if (deathTime == 20) {
                setDead();
                for (int i = 0; i < 20; ++i) {
                    double d2 = rand.nextGaussian() * 0.02D;
                    double d0 = rand.nextGaussian() * 0.02D;
                    double d1 = rand.nextGaussian() * 0.02D;
                    world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, posX + (double)(rand.nextFloat() * width * 2.0F) - (double)width, posY + (double)(rand.nextFloat() * height), posZ + (double)(rand.nextFloat() * width * 2.0F) - (double)width, d2, d0, d1);
                }
            }
        }

        if (getHealth() <= 0F)
            ++deathTicks;
        else
            deathTicks = 0;
        if ((deathTicks == 1) && (isServer())) {
            if (isClient())
                triggerAudioGiant(fetchDeathAudioGiant(), getSoundVolume(), getSoundPitch());
            ArrayList<?> listp = Lists.newArrayList(world.playerEntities);
            if ((listp != null) && (!listp.isEmpty())) {
                for (int i1 = 0; i1 < listp.size(); i1++) {
                    Entity entity = (Entity)listp.get(i1);
                    if ((entity != null) && ((entity instanceof EntityPlayer)) && fetchAchievement() != null)
                        { /* advancement runGrant removed (1.12.2) */ }
                }
            }
        }

        motionX *= 0.0D;
        motionZ *= 0.0D;
        if (!(this instanceof EntitySnowGolemTitan) && !(this instanceof EntitySlimeTitan))
            renderYawOffset = rotationYaw = rotationYawHead += 10F;
        rotationPitch = (0F + (getInvulTime() / 4.8F)) + ((0.01F + 0.01F * MathHelper.cos(ticksExisted * 0.25F)) * 3.1415927F);
        setAttackTarget(null);
        if (isClient()) {
            performHurtAnimation();
            spawnExplosionParticle();
        }

        if ((deathTicks > 200) && isServer())
            setInvulTime(getInvulTime() + 10);
        if (getInvulTime() >= fetchHoldThreash())
            setDead();
    }

    protected void whenUpdateMinion() {
        if (isServer() && fetchMinion() != null && !fetchWaiting() && !isSummonedGiant() && getAnimID() != 13 && !(world.provider instanceof WorldProviderVoid) && (TitanConfig.limitServant < 0 || minionsNum + priestsNum + zealotsNum + bishopNum + templarNum + lordsNum < TitanConfig.limitServant) && getHealth() > 0.0F && TheTitans.getDifficulty(world) != 0) {
            if (specialMinionsNum < fetchMinionCapSpecial() && rand.nextInt(100) == 0)
                emitMinion(fetchMinion(), 6);
            else {
                if (minionsNum < fetchCapMinion() && (rand.nextInt(fetchSpawnRateMinion()) == 0 || getInvulTime() > 1))
                    emitMinion(fetchMinion());
                if (priestsNum < fetchCapPriest() && (rand.nextInt(fetchSpawnRateMinion() * 2) == 0 || getInvulTime() > 1))
                    emitMinion(fetchMinion(), 1);
                if (zealotsNum < fetchCapZealot() && (rand.nextInt(fetchSpawnRateMinion() * 4) == 0 || getInvulTime() > 1))
                    emitMinion(fetchMinion(), 2);
                if (bishopNum < fetchCapBishop() && (rand.nextInt(fetchSpawnRateMinion() * 8) == 0 || getInvulTime() > 1))
                    emitMinion(fetchMinion(), 3);
                if (templarNum < fetchCapTemplar() && (rand.nextInt(fetchSpawnRateMinion() * 16) == 0 || getInvulTime() > 1))
                    emitMinion(fetchMinion(), 4);
                if (lordsNum < fetchCapLord() && (rand.nextInt(fetchSpawnRateMinion() * 64) == 0 || getInvulTime() > 1))
                    emitMinion(fetchMinion(), 5);
            }
        }
    }

    // PERF — used to run 2-3 world entity queries PER BODY PART per tick (projectiles, push, crush) — up to ~25
    // chunk-section scans for a 10-part titan. Now one query over the union of all hitboxes, then each consumer
    // filters that list by its own box (same intersect test the world query used).
    protected void whenUpdateBounds() {
        if (!isEntityAlive() || ticksExisted <= 5)
            return;
        AxisAlignedBB body = getEntityBoundingBox();
        AxisAlignedBB union = body.grow(0.5D);
        for (EntityTitanPart part : setPiece)
            union = union.union(part.getEntityBoundingBox().grow(1.0D, 0.5D, 1.0D));
        List<Entity> near = world.getEntitiesWithinAABBExcludingEntity(this, union);
        boolean waiting = fetchWaiting();
        collideProjectilesWith(body, near);
        whenMobCrushing(body, near);

        for (EntityTitanPart part : setPiece) {
            AxisAlignedBB pb = part.getEntityBoundingBox();
            collideProjectilesWith(pb, near);
            if (!waiting)
                collideWithEntities(part, within(near, pb.expand(1.0D, 0.0D, 1.0D)));

            if (ticksExisted % 10 == 0 && motionX != 0 && motionY != 0 && motionZ != 0)
                destroyBlocksInAABB(pb);

            if (part.needsCrush())
                whenMobCrushing(pb, near);
        }
    }

    private static List<Entity> within(List<Entity> src, AxisAlignedBB box) {
        if (src.isEmpty())
            return java.util.Collections.emptyList();
        List<Entity> out = new ArrayList<>();
        for (int i = 0; i < src.size(); i++) {
            Entity e = src.get(i);
            if (e.getEntityBoundingBox().intersects(box))
                out.add(e);
        }
        return out;
    }

    protected void whenMobCrushing(AxisAlignedBB boundingBox, List<Entity> near) {
        for (int i = 0; i < near.size(); i++) {
            Entity entity = near.get(i);
            if ((entity instanceof EntityLivingBase) && entity.onGround && !(entity instanceof EntityTitan) && entity.getEntityBoundingBox().intersects(boundingBox))
                entity.attackEntityFrom(DamageSourceExtra.causeHarmSquishing(this), (float)fetchAmountStrike(1.0F));
        }
    }

    public void collideProjectilesWith(AxisAlignedBB hitbox, List<Entity> near) {
        if (hurtResistantTime < 1 && !near.isEmpty())
            collideProjectilesWithList(within(near, hitbox.expand(0.5D, 0.5D, 0.5D)));
    }

    protected void whenUpdateAnimation() {
    }

    protected void updateAITasks() {
        long perfT = TitanProfiler.start();
        try {
            updateAITasksTitan();
        } finally {
            TitanProfiler.stop(getClass(), TitanProfiler.Sec.AI, perfT);
        }
    }

    private void updateAITasksTitan() {
        List<?> list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox());
        if (list != null && !list.isEmpty())
            for (int i1 = 0; i1 < list.size(); i1++)
            applyEntityCollision((Entity)list.get(i1));
        if ((rand.nextInt(1000) == 0 && getHealth() < getMaxHealth() / 20.0F && deathTicks <= 0) || (getHealth() < getMaxHealth() / 2.0F && deathTicks <= 0 && getAttackTarget() != null && getAttackTarget() instanceof EntityTitan && !rejuvinatingIs && ((EntityTitan)getAttackTarget()).rejuvinatingIs) && ((!(this instanceof EntitySlimeTitan) && !(this instanceof EntitySnowGolemTitan) && !(this instanceof EntityIronGolemTitan) && !(this instanceof EntityGargoyleTitan)))) {
            rejuvinatingIs = true;
            world.playBroadcastSound(1013, new BlockPos((int)posX, (int)posY, (int)posZ), 0);
            assignPowerExtra(fetchPowerExtra() + 1);
            jump();
        }

        if (rejuvinatingIs) {
            setInvulTime(getInvulTime() + 5);
            if (isClient())
                performHurtAnimation();
            if (getInvulTime() > fetchHoldThreash())
                rejuvinatingIs = false;
        }

        if (getInvulTime() > 0) {
            motionX *= 0.0D;
            motionZ *= 0.0D;
            int i = getInvulTime() - 1;
            if (i <= 0) {
                if (!(this instanceof EntitySnowGolemTitan) && !(this instanceof EntityIronGolemTitan) && !(this instanceof EntityGargoyleTitan)) {
                    world.newExplosion(this, posX, posY + height / 2.0F, posZ, height, false, world.getGameRules().getBoolean("mobGriefing"));
                    world.playBroadcastSound(1013, new BlockPos((int)posX, (int)posY, (int)posZ), 0);
                    destroyBlocksInAABB(getEntityBoundingBox());
                }

                if (this instanceof EntityZombieTitan && !((EntityZombieTitan)this).armedIs())
                    AnimationAPI.sendAnimPacket((EntityZombieTitan)this, 2);
            }

            setInvulTime(i);
            if (ticksExisted % 5 == 0)
                heal(getMaxHealth() / 500.0F);
        }

        else {
            if (fetchRegen() > 0.0D && hurtResistantTime <= 10)
                heal(fetchRegen());
            super.updateAITasks();
        }
    }

    protected void whenMobCrushing(AxisAlignedBB boundingBox) {
        // PERF — runs per crushing part per tick; typed query since only EntityLivingBase are hurt below.
        List<?> list = world.getEntitiesWithinAABB(EntityLivingBase.class, boundingBox, e -> e != this);
        if ((list != null) && (!list.isEmpty()))
            for (int i = 0; i < list.size(); i++) {
            Entity entity = (Entity)list.get(i);
            if ((entity instanceof EntityLivingBase) && entity.onGround && !(entity instanceof EntityTitan))
                entity.attackEntityFrom(DamageSourceExtra.causeHarmSquishing(this), (float)fetchAmountStrike(1.0F));
        }
    }

    public Entity[] getArray() {
        return setPiece.toArray(new Entity[setPiece.size()]);
    }

    public int fetchStepModiferFoot() {
        return 1 + ((int)fetchScaleMultiplierGiant() / 8);
    }

    /**
     * Returns the distance to the entity. Args: entity
     */
    public float getDistance(Entity arg70032n1) {
        float f = (float)(posX - arg70032n1.posX);
        float f1 = (float)((posY + (rand.nextFloat() * height)) - arg70032n1.posY);
        float f2 = (float)(posZ - arg70032n1.posZ);
        return MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
    }

    public boolean allowStrikeGiant(Class<?> clazz) {
        return true;
    }

    /** Forge-data flag of titans called in by another titan (e.g. the Mutant Zombie Titan's roar): no loot, no xp. */
    public static final String SUMMONED_TAG = "thetitans:summoned";

    public boolean isSummonedGiant() {
        return getEntityData().getBoolean(SUMMONED_TAG);
    }

    public void markSummonedGiant() {
        getEntityData().setBoolean(SUMMONED_TAG, true);
        experienceValue = 0;
    }

    // IRangedAttackMob now requires setSwingingArms; titans have no dedicated arm-swing flag.
    public void setSwingingArms(boolean swingingArms) {}

    protected void collideWithNearbyEntities() {
        List<?> list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().expand(0.1D, 0.1D, 0.1D));
        if (list != null && !list.isEmpty() && !fetchWaiting() && getAnimID() != 13) {
            for (int i = 0; i < list.size(); ++i) {
                Entity entity = (Entity)list.get(i);
                if (isEntityAlive() && entity.isEntityAlive() && (entity.canBePushed() || entity instanceof EntityTitan || (entity instanceof EntityThrowable && ((EntityThrowable)entity).getThrower() != this) || (entity instanceof EntityFireball && ((EntityFireball)entity).shootingEntity != this)) && entity.height > 6F && !(entity instanceof EntityPlayer)) {
                    entity.applyEntityCollision(this);
                }
            }
        }
    }

    public void applyEntityCollision(Entity arg70108n1) {
        if (allowStrikeGiant(arg70108n1.getClass()) && !fetchWaiting() && getAnimID() != 13 && !(arg70108n1 instanceof EntitySkeletonTitanGiantArrow) && !(arg70108n1 instanceof EntityHarcadiumArrow) && !(arg70108n1 instanceof EntityFireball) && !(arg70108n1 instanceof EntityThrowable) && !(arg70108n1 instanceof EntityTitanPart) && isEntityAlive() && !arg70108n1.isRidingOrBeingRiddenBy(this)) {
            double d0 = arg70108n1.posX - posX;
            double d1 = arg70108n1.posZ - posZ;
            double d2 = MathHelper.absMax(d0, d1);
            if (d2 >= 0.009999999776482582D) {
                d2 = (double)MathHelper.sqrt(d2);
                d0 /= d2;
                d1 /= d2;
                double d3 = 1.0D / d2;
                if (d3 > 1.0D) {
                    d3 = 1.0D;
                }

                d0 *= d3;
                d1 *= d3;
                d0 *= 0.25D;
                d1 *= 0.25D;
                d0 *= (double)(1.0F - entityCollisionReduction);
                d1 *= (double)(1.0F - entityCollisionReduction);
                if (arg70108n1.height >= 6F || arg70108n1 instanceof EntityTitan)
                    appendVelocityGiant(-d0, 0.0D, -d1);
                d0 *= 4D;
                d1 *= 4D;
                arg70108n1.addVelocity(d0, 0.75D, d1);
            }
        }
    }

    public boolean isEntityAlive() {
        return getHealth() > 0.0F;
    }

    public Vec3d getLook(float arg70676n1) {
        float f1;
        float f2;
        float f3;
        float f4;
        if (arg70676n1 == 1.0F) {
            f1 = MathHelper.cos(-rotationYawHead * 0.017453292F - (float)Math.PI);
            f2 = MathHelper.sin(-rotationYawHead * 0.017453292F - (float)Math.PI);
            f3 = -MathHelper.cos(-rotationPitch * 0.017453292F);
            f4 = MathHelper.sin(-rotationPitch * 0.017453292F);
            return new Vec3d((double)(f2 * f3), (double)f4, (double)(f1 * f3));
        }

        else {
            f1 = prevRotationPitch + (rotationPitch - prevRotationPitch) * arg70676n1;
            f2 = prevRotationYawHead + (rotationYawHead - prevRotationYawHead) * arg70676n1;
            f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
            f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
            float f5 = -MathHelper.cos(-f1 * 0.017453292F);
            float f6 = MathHelper.sin(-f1 * 0.017453292F);
            return new Vec3d((double)(f4 * f5), (double)f6, (double)(f3 * f5));
        }
    }

    public void heal(float arg70691n1) {
        heal((double)arg70691n1);
    }

    protected void damageEntity(DamageSource source, float damage) {
        if (!isEntityInvulnerable(source)) {
            damage = ForgeHooks.onLivingHurt(this, source, damage);
            if (damage <= 500.0F)
                return;
            if (source.getTrueSource() instanceof EntityPlayer && !allowHurtByUserBe() && source.damageType != "playerwithgodpower")
                damage *= 0.25F;
            if (isArmored())
                damage *= 0.25F;
            damage = applyArmorCalculations(source, damage);
            damage = applyPotionDamageCalculations(source, damage);
            if (damage > (float)cap())
                damage = (float)cap();
            if (damage > 0.0F) {
                hurt(damage);
                getCombatTracker().trackDamage(source, getHealth(), damage);
                setAbsorptionAmount(getAbsorptionAmount() - damage);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void processUpdateHp(byte arg70103n1) {
        if (arg70103n1 == 2) {
            hurtResistantTime = maxHurtResistantTime;
            hurtTime = (maxHurtTime = 10);
            attackedAtYaw = 0.0F;
            triggerAudioGiant(fetchHurtAudioGiant(), getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            attackEntityFrom(DamageSource.GENERIC, 0.0F);
        }
    }

    // release the chunk ticket and drop the static map entries when the entity leaves the world (death or
    // unload); previously nothing ever called haltLoading, so every titan that ever woke stayed referenced.
    @Override
    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if (!world.isRemote)
            com.electrifiedded.thetitans.handler.ChunkLoadHooks.haltLoading(this);
    }

    @SuppressWarnings("unchecked")
    public void setDead() {
        if (isClient())
            isDead = true;
        if (deathTicks > 0 || this instanceof EntitySlimeTitan || getHealth() <= 0F) {
            inactActionDeath();
            ArrayList<?> listp = Lists.newArrayList(world.playerEntities);
            if ((listp != null) && (!listp.isEmpty()) && isServer() && !(this instanceof EntitySlimeTitan) && !(this instanceof EntityGargoyleTitan) && !(this instanceof EntityIronGolemTitan) && !(this instanceof EntitySnowGolemTitan)) {
                for (int i1 = 0; i1 < listp.size(); i1++) {
                    EntityPlayer player = (EntityPlayer)listp.get(i1);
                    if (player != null) {
                        playLivingSound();
                        player.sendMessage(new TextComponentString(getName() + ": I will return, " + player.getName()));
                    }
                }
            }

            super.setDead();
        }
    }

    protected void shedDropRare(int arg70600n1) {
    }

    protected void inactActionDeath() {
        world.newExplosion(this, posX, posY + 3.0D, posZ, 0.0F, false, false);
        if (isServer() && world.getGameRules().getBoolean("doMobLoot") && !isSummonedGiant()) {
            dropFewItems(true, 0);
            dropEquipment(true, 0);
            shedDropRare(1);
        }
    }

    public void funcM82206() {
        setInvulTime(fetchConditionGiant() == EnumTitanStatus.GOD ? 7100 : (fetchConditionGiant() == EnumTitanStatus.GREATER ? 1310 : ((this instanceof EntitySnowGolemTitan || this instanceof EntitySlimeTitan) ? 150 : 850)));
        assignWaiting(false);
    }

    protected void applyEntityAI() {
    }

    public boolean isAIDisabled() {
        return false;
    }

    public void setAttackTarget(EntityLivingBase arg70624n1) {
        if (arg70624n1 != null && !allowStrikeGiant(arg70624n1.getClass()))
            arg70624n1 = null;
        if (arg70624n1 != null && !fetchWaiting() && getAnimID() != 13 && arg70624n1.posY < 256D && arg70624n1.isEntityAlive() && arg70624n1 != getRidingEntity() && !getPassengers().contains(arg70624n1)) {
            super.setAttackTarget(arg70624n1);
            if (isServer() && arg70624n1 instanceof EntityPlayerMP && arg70624n1.ticksExisted > 100 && !((EntityPlayer)arg70624n1).inventory.hasItemStack(new net.minecraft.item.ItemStack(TitanGear.ultimaBlade)) && ((EntityPlayer)arg70624n1).isEntityAlive() && ((EntityPlayer)arg70624n1).hurtResistantTime <= 10) {
                if (world.getDifficulty() == EnumDifficulty.PEACEFUL)
                    world.getMinecraftServer().setDifficultyForAllWorlds(EnumDifficulty.EASY);
                // BUGFIX — the probe hit fails for many legit reasons (respawn invulnerability, another mod
                // cancelling LivingAttackEvent, titan-armor revive grace). It used to dump the whole inventory and set
                // health to 0 without a death event — the "stuck half-dead" state. Now it only kicks actual god-mode
                // (damage disabled outside creative/spectator, or the vanilla invulnerable flag), and only with anti-cheat on.
                EntityPlayerMP player = (EntityPlayerMP)arg70624n1;
                if (TitanConfig.antiCheatIs && !player.attackEntityFrom((new DamageSource("other")).setDamageBypassesArmor().setDamageIsAbsolute().setDamageAllowedInCreativeMode(), 1F) && !player.isSpectator() && ((player.capabilities.disableDamage && !player.capabilities.isCreativeMode) || player.getIsInvulnerable()))
                    player.connection.disconnect(new TextComponentString(getName() + " has kicked you for cheating."));
            }
        }

        else if (arg70624n1 instanceof EntityTitan && (world.provider instanceof WorldProviderVoid || ((EntityTitan)arg70624n1).getInvulTime() > 0 || ((EntityTitan)arg70624n1).fetchWaiting() || ((EntityTitan)arg70624n1).getAnimID() == 13) && !(arg70624n1 instanceof EntityWitherzilla))
            super.setAttackTarget(null);
        else
            super.setAttackTarget(null);
    }

    protected void flyTitan(float width, float height, double yoffset) {
        if (getAttackTarget() != null && getDistance(getAttackTarget()) > fetchReachMelee() + (!getAttackTarget().onGround ? 64D : 150D) + (giantMelee ? 0D : 100D)) {
            if (posY <= getAttackTarget().posY + 12D && posY < 256D - height) {
                motionY += 0.9D - motionY;
                if (motionY < 0.0D)
                    motionY = 0.0D;
            }

            motionY *= 0.6D;
        }

        if (!onGround) {
            fallDistance *= 0F;
            isFlying = true;
            float f = (rand.nextFloat() - 0.5F) * width;
            float f1 = (rand.nextFloat() - 0.5F) * height;
            float f2 = (rand.nextFloat() - 0.5F) * width;
            world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX + (double)f, posY + yoffset + (double)f1, posZ + (double)f2, 0.0D, 0.0D, 0.0D);
        }
    }

    protected void jumpMobAt(EntityLivingBase e) {
        motionY += 1.25D;
        posY += 1.5499999523162842D;
        double d1 = e.posX - posX;
        double d2 = e.posZ - posZ;
        float d = (float)Math.atan2(d2, d1);
        float f2 = (float)(d * 180.0D / 3.141592653589793D) - 90.0F;
        rotationYaw = f2;
        d1 = Math.sqrt(d1 * d1 + d2 * d2);
        motionX += d1 * 0.05D * Math.cos(d);
        motionZ += d1 * 0.05D * Math.sin(d);
        isAirBorne = true;
    }

    public int fetchCapMinion() {
        int multi = 2;
        if (world.getDifficulty() == EnumDifficulty.EASY)
            --multi;
        if (world.getDifficulty() == EnumDifficulty.HARD)
            ++multi;
        return 32 * (int)multi;
    }

    public int fetchCapPriest() {
        int multi = 2;
        if (world.getDifficulty() == EnumDifficulty.EASY)
            --multi;
        if (world.getDifficulty() == EnumDifficulty.HARD)
            ++multi;
        return 16 * (int)multi;
    }

    public int fetchCapZealot() {
        int multi = 2;
        if (world.getDifficulty() == EnumDifficulty.EASY)
            --multi;
        if (world.getDifficulty() == EnumDifficulty.HARD)
            ++multi;
        return 8 * (int)multi;
    }

    public int fetchCapBishop() {
        int multi = 2;
        if (world.getDifficulty() == EnumDifficulty.EASY)
            --multi;
        if (world.getDifficulty() == EnumDifficulty.HARD)
            ++multi;
        return 4 * (int)multi;
    }

    public int fetchCapTemplar() {
        int multi = 2;
        if (world.getDifficulty() == EnumDifficulty.EASY)
            --multi;
        if (world.getDifficulty() == EnumDifficulty.HARD)
            ++multi;
        return 2 * (int)multi;
    }

    public int fetchCapLord() {
        return world.getDifficulty() == EnumDifficulty.HARD ? 2 : 1;
    }

    public int fetchMinionCapSpecial() {
        return 4;
    }

    public int fetchPowerExtra() {
        return dataManager.get(POWER_EXTRA).intValue();
    }

    public void assignPowerExtra(int arg82215n1) {
        dataManager.set(POWER_EXTRA, Integer.valueOf(arg82215n1));
    }

    public int fetchLabelRandom() {
        return dataManager.get(POWER_EXTRA).intValue();
    }

    public void assignLabelRandom(int arg82215n1) {
        dataManager.set(POWER_EXTRA, Integer.valueOf(arg82215n1));
    }

    public boolean fetchWaiting() {
        return dataManager.get(WAITING).byteValue() == 1;
    }

    public void assignWaiting(boolean arg82215n1) {
        dataManager.set(WAITING, Byte.valueOf(arg82215n1 ? (byte)1 : (byte)0));
    }

    public int fetchVariantGiant() {
        return dataManager.get(VARIANT_GIANT).intValue();
    }

    public void assignVariantGiant(int arg82215n1) {
        dataManager.set(VARIANT_GIANT, Integer.valueOf(arg82215n1));
    }

    public int fetchATAAID() {
        return dataManager.get(KEY_ATAA).intValue();
    }

    public void assignATAAID(int arg82215n1) {
        dataManager.set(KEY_ATAA, Integer.valueOf(arg82215n1));
    }

    public int getInvulTime() {
        return dataManager.get(TICKS_INVUL).intValue();
    }

    public void setInvulTime(int arg82215n1) {
        if (isServer()) {
            if (arg82215n1 < 0) {
                dataManager.set(TICKS_INVUL, Integer.valueOf(0));
            }

            else {
                dataManager.set(TICKS_INVUL, Integer.valueOf(arg82215n1));
                prevRotationPitch = rotationPitch = (0F + (arg82215n1 / (fetchHoldThreash() / 180F)));
            }
        }
    }

    public boolean wreckInAABBGriefingBypassBlocks(AxisAlignedBB arg70972n1) {
        int i = MathHelper.floor(arg70972n1.minX);
        int j = MathHelper.floor(arg70972n1.minY);
        int k = MathHelper.floor(arg70972n1.minZ);
        int l = MathHelper.floor(arg70972n1.maxX);
        int i1 = MathHelper.floor(arg70972n1.maxY);
        int j1 = MathHelper.floor(arg70972n1.maxZ);
        boolean flag = false;
        boolean flag1 = false;
        boolean server = isServer();
        for (int k1 = i; k1 <= l; k1++) {
            for (int l1 = j; l1 <= i1; l1++) {
                for (int i2 = k; i2 <= j1; i2++) {
                    BlockPos pos = new BlockPos(k1, l1, i2);
                    IBlockState state = world.getBlockState(pos);
                    if ((!state.getBlock().isAir(state, world, pos)) && server) {
                        if (state.getBlockHardness(world, pos) != -1F) {
                            flag1 = world.setBlockToAir(pos) || (flag1);
                        }
                    }
                }
            }
        }

        return flag;
    }

    // PERF — both destroy loops used to call getBlockState + isBlockLoaded + gamerule string lookup +
    // getClosestPlayer for EVERY position of the (mostly air) titan-sized AABB — shiftToPointGiant runs this on the
    // whole body box every tick while walking. Now only non-empty chunk sections are visited and the per-call
    // lookups are hoisted. Block outcome per position is unchanged.
    public void destroyBlocksInAABB(AxisAlignedBB arg70972n1) {
        if (arg70972n1 == null || ticksExisted <= 5 || !isServer())
            return;
        final boolean tileDrops = world.getGameRules().getBoolean("doTileDrops");
        final Boolean[] playerNear = new Boolean[1];
        TitanPhysics.forCubeEach(world, arg70972n1, (pos, state) -> smashCube(pos, state, tileDrops, 5, playerNear));
        flushSmashDrops(arg70972n1);
    }

    public void wreckInAABBToplessBlocks(AxisAlignedBB arg70972n1) {
        if (arg70972n1 == null || ticksExisted <= 5 || !isServer())
            return;
        final boolean tileDrops = world.getGameRules().getBoolean("doTileDrops");
        final Boolean[] playerNear = new Boolean[1];
        TitanPhysics.forCubeEach(world, arg70972n1, (pos, state) -> {
            if (state.isOpaqueCube() && !world.getBlockState(pos.up()).isOpaqueCube())
                smashCube(pos, state, tileDrops, 3, playerNear);
        });
        flushSmashDrops(arg70972n1);
    }

    /**Shared body of the destroy loops: removes one block, sometimes flinging it as a burning falling block.*/
    private void smashCube(BlockPos pos, IBlockState state, boolean tileDrops, int fallingChance, Boolean[] playerNear) {
        Block block = state.getBlock();
        if (block.isAir(state, world, pos) || state.getBlockHardness(world, pos) == -1F)
            return;
        if (!tileDrops || state.getMaterial().isLiquid() || block == Blocks.FIRE || block == Blocks.WEB) {
            world.setBlockToAir(pos);
        }

        else if (rand.nextInt(fallingChance) == 0) {
            EntityFallingBlockTitan entityfallingblock = new EntityFallingBlockTitan(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, block);
            entityfallingblock.setPosition(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
            double d0 = (getEntityBoundingBox().minX + getEntityBoundingBox().maxX) / 2.0D;
            double d1 = (getEntityBoundingBox().minZ + getEntityBoundingBox().maxZ) / 2.0D;
            double d2 = entityfallingblock.posX - d0;
            double d3 = entityfallingblock.posZ - d1;
            double d4 = d2 * d2 + d3 * d3;
            entityfallingblock.setFire(10);
            entityfallingblock.addVelocity(d2 / d4 * 10D, 2D + rand.nextGaussian(), d3 / d4 * 10D);
            world.spawnEntity(entityfallingblock);
            world.setBlockToAir(pos);
        }

        else {
            if (playerNear[0] == null)
                playerNear[0] = world.getClosestPlayerToEntity(this, 16D) != null;
            // PERF — drops are pooled per smash call and spawned as merged stacks (flushSmashDrops): same loot,
            // but one item entity per stack instead of one per block (thousands of EntityItem in the bench).
            if (playerNear[0])
                world.playEvent(2001, pos, Block.getStateId(state));
            net.minecraft.util.NonNullList<net.minecraft.item.ItemStack> drops = net.minecraft.util.NonNullList.create();
            block.getDrops(drops, world, pos, state, 0);
            float chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(drops, world, pos, state, 0, 1.0F, false, null);
            world.setBlockToAir(pos);
            for (net.minecraft.item.ItemStack st : drops)
                if (!st.isEmpty() && world.rand.nextFloat() <= chance)
                    poolSmashDrop(st);
        }
    }

    private final List<net.minecraft.item.ItemStack> smashDrops = new ArrayList<>();

    private void poolSmashDrop(net.minecraft.item.ItemStack st) {
        for (net.minecraft.item.ItemStack have : smashDrops)
            if (have.getCount() < have.getMaxStackSize() && net.minecraft.item.ItemStack.areItemsEqual(have, st) && net.minecraft.item.ItemStack.areItemStackTagsEqual(have, st)) {
                int move = Math.min(st.getCount(), have.getMaxStackSize() - have.getCount());
                have.grow(move);
                st.shrink(move);
                if (st.isEmpty())
                    return;
            }
        smashDrops.add(st.copy());
    }

    private void flushSmashDrops(AxisAlignedBB box) {
        if (smashDrops.isEmpty())
            return;
        for (net.minecraft.item.ItemStack st : smashDrops) {
            double x = box.minX + rand.nextDouble() * (box.maxX - box.minX);
            double z = box.minZ + rand.nextDouble() * (box.maxZ - box.minZ);
            double y = Math.max(box.minY, 0D) + 0.5D;
            net.minecraft.entity.item.EntityItem item = new net.minecraft.entity.item.EntityItem(world, x, y, z, st);
            item.setDefaultPickupDelay();
            world.spawnEntity(item);
        }
        smashDrops.clear();
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
            triggerAudioGiant("thetitans:groundSmash", 20.0F, 1.0F);
            triggerAudioGiant("thetitans:titanland", 10000.0F, 1.0F);
            wreckInAABBToplessBlocks(getEntityBoundingBox().expand(fetchConditionGiant() == EnumTitanStatus.LESSER ? 6D : 12D, 1D, fetchConditionGiant() == EnumTitanStatus.LESSER ? 6D : 12D));
            List<?> found11 = world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().expand(48D, 4D, 48D), e -> e != this);
            if ((found11 != null) && (!found11.isEmpty())) {
                for (int i1 = 0; i1 < found11.size(); i1++) {
                    Entity entity = (Entity)found11.get(i1);
                    if (((entity instanceof EntityLivingBase)) && allowStrikeGiant(entity.getClass()) && (!(entity instanceof EntityTitan))) {
                        float smash = 50F - getDistance(entity);
                        if (smash <= 1F)
                            smash = 1F;
                        entity.attackEntityFrom(DamageSource.causeMobDamage(this), smash);
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

    public boolean canBePushed() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean canRenderOnFire() {
        return false;
    }

    public int getTotalArmorValue() {
        switch (fetchConditionGiant()) {
            case GOD:
                return 99;
            case GREATER:
                return 90;
            case AVERAGE:
                return 80;
            case LESSER:
                return 70;
            case UTILITY:
                return 50;
            default:
                return 0;
        }
    }

    public boolean isArmored() {
        return false;
    }

    /**
     * Reduces damage, depending on armor
     */
    protected float applyArmorCalculations(DamageSource arg70655n1, float arg70655n2) {
        int i = 100 - getTotalArmorValue();
        float f1 = arg70655n2 * (float)i;
        arg70655n2 = f1 / 100.0F;
        return arg70655n2;
    }

    public int getMaxSpawnedInChunk() {
        return 1;
    }

    public int fetchExperienceDropAmountGiant() {
        return 1;
    }

    public float getRenderSizeModifier() {
        return fetchScaleMultiplierGiant();
    }

    public void addPotionEffect(PotionEffect arg70690n1) {
    }

    protected float getSoundVolume() {
        return fetchScaleMultiplierGiant();
    }

    protected void despawnEntity() {
        idleTime = 0;
    }

    public int getTalkInterval() {
        return 120;
    }

    public int getMaxFallHeight() {
        return world.getHeight();
    }

    public void setInWeb() {
    }

    public int fetchSpawnRateMinion() {
        return 0;
    }

    public boolean handleWaterMovement() {
        return false;
    }

    public boolean processMovementLava() {
        return false;
    }

    protected int decreaseAirSupply(int arg70682n1) {
        return arg70682n1;
    }

    public double getSpeed() {
        return 0.325D / 4;
    }

    public double fetchReachMelee() {
        return width + fetchScaleMultiplierGiant();
    }

    public void addVelocity(double arg70024n1, double arg70024n3, double arg70024n5) {
    }

    public void appendVelocityGiant(double arg70024n1, double arg70024n3, double arg70024n5) {
        if (!fetchWaiting() && getAnimID() != 13) {
            motionX += arg70024n1;
            motionY += arg70024n3;
            motionZ += arg70024n5;
        }
    }

    public boolean hasSoulNo() {
        return false;
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return TheTitans.TITAN;
    }

    public void shiftToPointGiant(double x, double z, boolean uselook) {
        double d0 = x - posX;
        double d1 = z - posZ;
        float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1);
        if (uselook)
            getLookHelper().setLookPosition(x, posY + getEyeHeight(), z, 64 / fetchScaleMultiplierGiant(), getVerticalFaceSpeed());
        motionX += d0 / f2 * getSpeed() - motionX * getSpeed();
        motionZ += d1 / f2 * getSpeed() - motionZ * getSpeed();
        if (ticksExisted % 100 == 0)
            renderYawOffset = rotationYaw = rotationYawHead;
        if (!(this instanceof EntitySnowGolemTitan))
            destroyBlocksInAABB(getEntityBoundingBox().offset(motionX, 0, motionZ));
    }

    public boolean isNoDespawnRequired() {
        return true;
    }

    public boolean allowStrike() {
        return giantMelee;
    }

    public boolean needsMove() {
        return getAnimID() == 0 && !fetchWaiting() && !(this instanceof EntityWitherzilla) && getAttackTarget() != null && getDistance(getAttackTarget()) > fetchReachMelee() + (giantMelee ? 0D : 96D);
    }

    public int fetchIDFoot() {
        return keyFoot;
    }

    public IEntityLivingData onInitialSpawn(net.minecraft.world.DifficultyInstance difficulty, IEntityLivingData arg110161n1) {
        syncHpMax();
        setHealth(getMaxHealth());
        return super.onInitialSpawn(difficulty, arg110161n1);
    }

    @SuppressWarnings("unchecked")
    public void onDeath(DamageSource arg70645n1) {
        Entity entity = arg70645n1.getTrueSource();
        ArrayList<?> listp = Lists.newArrayList(world.playerEntities);
        if (isServer() && (listp != null) && (!listp.isEmpty()) && entity != null && entity instanceof EntityLivingBase) {
            for (int i1 = 0; i1 < listp.size(); i1++) {
                EntityPlayer entityplayer = (EntityPlayer)listp.get(i1);
                if (getHealth() > 0F)
                    entityplayer.sendMessage(new TextComponentString(getName() + " has refused to let " + entity.getName() + " cheat..."));
                else
                    entityplayer.sendMessage(new TextComponentString(getName() + " has been defeated by " + entity.getName()));
            }
        }

        if (getHealth() <= 0F) {
            EntityLivingBase entitylivingbase = getAttackingEntity();
            if (scoreValue >= 0 && entitylivingbase != null) {
            }

            if (entity != null) {
                entity.onKillEntity(this);
            }

            dead = true;
            getCombatTracker().reset();
            if (isServer()) {
                int i = 0;
                if (entity instanceof EntityPlayer) {
                    i = EnchantmentHelper.getLootingModifier((EntityLivingBase)entity);
                    ((EntityPlayer)entity).addStat(TheTitans.defeatedStatTitans, 1);
                }

                captureDrops = true;
                capturedDrops.clear();
                int j = 0;
                if (canDropLoot() && world.getGameRules().getBoolean("doMobLoot") && !isSummonedGiant()) {
                    dropFewItems(recentlyHit > 0, i);
                    dropEquipment(recentlyHit > 0, i);
                    if (recentlyHit > 0) {
                        j = rand.nextInt(200) - i;
                        if (j < 5)
                            shedDropRare(j <= 0 ? 1 : 0);
                    }
                }

                captureDrops = false;
                if (!net.minecraftforge.common.ForgeHooks.onLivingDrops(this, arg70645n1, capturedDrops, i, recentlyHit > 0))
                    for (EntityItem item : capturedDrops)
                    world.spawnEntity(item);
            }

            world.setEntityState(this, (byte)3);
        }
    }

    protected void jump() {
        super.jump();
        triggerAudioGiant("thetitans:titanSwing", 5F, 2F);
        ++motionY;
        ++motionY;
    }

    public int getParticleCount() {
        return 6;
    }

    public String fetchParticle() {
        switch (fetchConditionGiant()) {
            case GOD:
                return "fireworksSpark";
            case GREATER:
                return "magicCrit";
            case AVERAGE:
                return "crit";
            default:
                return "enchantmenttable";
        }
    }

    public int fetchHoldThreash() {
        return fetchConditionGiant() == EnumTitanStatus.GOD ? 7100 : (fetchConditionGiant() == EnumTitanStatus.GREATER ? 1310 : ((this instanceof EntitySnowGolemTitan || this instanceof EntitySlimeTitan) ? 150 : 850));
    }

    public void enactAuraEffect(int type, AxisAlignedBB boundery) {
        // PERF — typed query (only non-titan EntityLivingBase are affected below), skips items/projectiles/XP.
        List<?> list = world.getEntitiesWithinAABB(EntityLivingBase.class, boundery, e -> e != this);
        if (world != null && !fetchWaiting()) {
            if (list != null && !list.isEmpty()) {
                for (int i = 0; i < list.size(); ++i) {
                    Entity entity = (Entity)list.get(i);
                    if (isServer() && (entity instanceof EntityLivingBase && !(entity instanceof EntityTitan)) && allowStrikeGiant(entity.getClass())) {
                        EntityLivingBase mob = (EntityLivingBase)entity;
                        switch (type) {
                            case 1: {
                                    mob.attackEntityFrom(DamageSource.WITHER.setDamageIsAbsolute().setDamageAllowedInCreativeMode(), 4.0F);
                                    mob.addPotionEffect(new PotionEffect(CommonProxy.advancedWither, 1200));
                                    mob.addPotionEffect(new PotionEffect(MobEffects.WITHER, 1200, 3));
                                    mob.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 400, 4));
                                    if (mob.getHealth() <= 5.0F)
                                        mob.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 400, 1));
                                    break;
                                }

                            case 2: {
                                    mob.addPotionEffect(new PotionEffect(CommonProxy.electricJudgment, 1200));
                                    break;
                                }

                            case 3: {
                                    mob.setFire(40);
                                    break;
                                }

                            case 4: {
                                    mob.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 400, 4));
                                    break;
                                }

                            case 5: {
                                    mob.addPotionEffect(new PotionEffect(MobEffects.POISON, 400, 2));
                                    break;
                                }

                            case 6: {
                                    mob.addPotionEffect(new PotionEffect(MobEffects.POISON, 400, 2));
                                    break;
                                }

                            default: {
                                    mob.addPotionEffect(new PotionEffect(CommonProxy.giantRadiationCreeper, 600));
                                }
                        }
                    }
                }
            }
        }
    }

    public void collideProjectilesWith(AxisAlignedBB hitbox) {
        if (hurtResistantTime < 1)
            collideProjectilesWithList(world.getEntitiesWithinAABBExcludingEntity(this, hitbox.expand(0.5D, 0.5D, 0.5D)));
    }

    private void collideProjectilesWithList(List<?> list) {
        {
            if (list != null && !list.isEmpty()) {
                for (int j = 0; j < list.size(); ++j) {
                    Entity entity = (Entity)list.get(j);
                    if (entity instanceof EntityFireball && ((EntityFireball)entity).shootingEntity != this && !(entity instanceof EntityLightningBall) && !(entity instanceof EntityGargoyleTitanFireball) && !(entity instanceof EntityWebShot))
                        ((EntityFireball)entity).attackEntityFrom(DamageSource.causeThornsDamage(this), 0F);
                    else if (entity instanceof EntityTitanFireball && ((EntityTitanFireball)entity).shootingEntity != null && ((EntityTitanFireball)entity).shootingEntity != this)
                        ((EntityTitanFireball)entity).whenPublicImpact(this);
                    else if (entity instanceof EntityGargoyleTitanFireball) {
                        triggerAudioGiant("thetitans:titanpunch", 10.0F, 1.0F);
                        world.newExplosion(((EntityGargoyleTitanFireball)entity).shootingEntity != null ? ((EntityGargoyleTitanFireball)entity).shootingEntity : ((EntityGargoyleTitanFireball)entity), ((EntityGargoyleTitanFireball)entity).posX, ((EntityGargoyleTitanFireball)entity).posY, ((EntityGargoyleTitanFireball)entity).posZ, 8F, false, false);
                        attackEntityFrom(DamageSource.causeFireballDamage((EntityGargoyleTitanFireball)entity, ((EntityGargoyleTitanFireball)entity).shootingEntity != null ? ((EntityGargoyleTitanFireball)entity).shootingEntity : (EntityGargoyleTitanFireball)entity), 1000F);
                        entity.setDead();
                    }

                    else if (entity instanceof EntityHarcadiumArrow) {
                        EntityHarcadiumArrow arrow = (EntityHarcadiumArrow)entity;
                        float f2 = MathHelper.sqrt(arrow.motionX * arrow.motionX + arrow.motionY * arrow.motionY + arrow.motionZ * arrow.motionZ);
                        int k = MathHelper.ceil(f2 * arrow.getDamage() / 2);
                        if (arrow.getIsCritical())
                            k += rand.nextInt(k / 2 + 2);
                        triggerAudioGiant("thetitans:titanpunch", 10.0F, 1.0F);
                        attackEntityFrom(DamageSourceExtra.causeArrowHarmHarcadium(arrow, arrow.shootingEntity != null ? arrow.shootingEntity : arrow), k);
                        entity.setDead();
                    }

                    else if (entity instanceof EntityWebShot && ((EntityWebShot)entity).shootingEntity != this) {
                        triggerAudioGiant("thetitans:titanpunch", 10.0F, 1.0F);
                        attackEntityFrom(DamageSourceExtra.causeGiantHarmAnti(((EntityWebShot)entity).shootingEntity != this ? ((EntityWebShot)entity).shootingEntity : (EntityWebShot)entity), 300F);
                        int i1 = MathHelper.floor(posY);
                        int i11 = MathHelper.floor(posX);
                        int j1 = MathHelper.floor(posZ);
                        for (int l1 = -2 - rand.nextInt(4); l1 <= 2 + rand.nextInt(4); l1++) {
                            for (int i2 = -2 - rand.nextInt(4); i2 <= 2 + rand.nextInt(4); i2++) {
                                for (int h = -2; h <= 2 + rand.nextInt(5); h++) {
                                    int j2 = i11 + l1;
                                    int k = i1 + h;
                                    int l = j1 + i2;
                                    BlockPos pos = new BlockPos(j2, k, l);
                                    if (!world.getBlockState(pos).isOpaqueCube()) {
                                        world.setBlockState(pos, Blocks.WEB.getDefaultState());
                                    }
                                }
                            }
                        }

                        entity.setDead();
                    }
                }
            }
        }
    }

    public void collideWithEntities(EntityTitanPart part, List<?> entities) {
        if (part != null && part.world != null && !fetchWaiting()) {
            double d0 = (part.getEntityBoundingBox().minX + part.getEntityBoundingBox().maxX) / 2.0D;
            double d1 = (part.getEntityBoundingBox().minZ + part.getEntityBoundingBox().maxZ) / 2.0D;
            Iterator<?> iterator = entities.iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity)iterator.next();
                boolean leg = part.getName().toLowerCase().contains("leg");

                if (entity != null && allowStrikeGiant(entity.getClass()) && !(entity instanceof EntityWebShot) && !(entity instanceof EntitySkeletonTitanGiantArrow) && !(entity instanceof EntityWitherSkull) && !(entity instanceof EntityTitanFireball) && !(entity instanceof EntityProtoBall) && !(entity instanceof EntityLightningBall) && !(entity instanceof EntityTitanPart) && !(entity instanceof EntityHarcadiumArrow) && !(entity instanceof EntityTitan) && !(entity instanceof EntityTitanSpirit)) {
                    SoundHandler.whenPlayMob("random.explode", part.world, part, 1F, 1.0F + rand.nextFloat());
                    double d2 = entity.posX - d0;
                    double d3 = entity.posZ - d1;
                    double d4 = d2 * d2 + d3 * d3;
                    entity.addVelocity(d2 / d4 * (leg ? 5D : 1.5D), (leg ? 1.75D : 0.5D), d3 / d4 * (leg ? 5D : 1.5D));

                    if (isServer() && allowStrikeGiant(entity.getClass()) && entity.posY <= part.posY - part.height - 0.01D)
                        entity.attackEntityFrom(DamageSource.causeThornsDamage(this), 20F);

                    if (this instanceof EntitySkeletonTitan && ((EntitySkeletonTitan)this).fetchKindSkeleton() == 1 && entity instanceof EntityLivingBase)
                        ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(MobEffects.WITHER, 1200, 3));
                }
            }
        }
    }

    /**Calculates the damage this titan will deal based on it's titan status.*/
    public double fetchAmountStrike(double baseDamage) {
        return baseDamage * (fetchMultiplierGiant() * 0.005D) * (isChild() ? 0.5D : 1.0D);
    }

    /**Calculates the health this titan provides based on it's titan status*/
    public double fetchAmountHp() {
        return fetchMultiplierGiant() * (isChild() ? 0.5D : 1.0D) * (double)fetchScaleMultiplierGiant();
    }

    /**Returns a multiplier based on the titan's status*/
    public final double fetchMultiplierGiant() {
        switch (fetchConditionGiant()) {
            case GOD:
                return 51450000000000000000000000.0D;
            case GREATER:
                return 5000000000000.0D;
            case AVERAGE:
                return 45000000000.0D;
            case LESSER:
                return 5000000.0D;
            default:
                return 150000.0D;
        }
    }

    @Deprecated
    /**Titans no longer use this value. Please use fetchAmountStrike(double)*/
    public double fetchAmountStrike() {
        return 20.0F;
    }

    @Deprecated
    /**Titans no longer use this value. Please use fetchAmountHp(double)*/
    public double fetchAmountHitpoint() {
        return Double.MAX_VALUE;
    }

    @Deprecated
    /**Titans no longer use this value. Please use fetchMultiplierGiant() as it is used to calculate damage and health*/
    public double fetchGiantMultiplierHitpoint() {
        switch (fetchConditionGiant()) {
            case GOD:
                return 5145000000000000000000.0D;
            case GREATER:
                return 500000000.0D;
            case AVERAGE:
                return 2500000.0D;
            case LESSER:
                return 500000.0D;
            default:
                return 15000.0D;
        }
    }

    public int fetchAmountKnockback() {
        switch (fetchConditionGiant()) {
            case GOD:
                return 40;
            case GREATER:
                return 30;
            case AVERAGE:
                return 20;
            case LESSER:
                return 10;
            default:
                return 10;
        }
    }

    public EnumTitanStatus fetchConditionGiant() {
        return EnumTitanStatus.LESSER;
    }

    protected net.minecraft.util.SoundEvent getSwimSound() {
        return SoundEvents.ENTITY_HOSTILE_SWIM;
    }

    protected net.minecraft.util.SoundEvent getSplashSound() {
        return SoundEvents.ENTITY_HOSTILE_SPLASH;
    }

    public boolean isPotionApplicable(PotionEffect arg70687n1) {
        return false;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        Entity entity = source.getTrueSource();
        if (isSpawnBossToExemptOre(entity) || entity instanceof EntityIronGolem || entity instanceof EntityGiantZombieBetter || entity instanceof EntityDragon || entity instanceof EntityWither || entity instanceof EntityDragonMinion || entity instanceof EntityWitherMinion) {
            amount *= 100F;
            triggerAudioGiant("thetitans:titanpunch", 50F, isChild() ? 1.5F : 1.0F);
        }

        if ((isEntityInvulnerable(source)) || isClient() || (entity == null && source.getDamageType() != "blindness" && source.getDamageType() != "other") || (amount <= 50.0F))
            return false;
        else if (source.getTrueSource() instanceof EntitySnowGolemTitan && this instanceof EntitySnowGolemTitan)
            return false;
        else if (isRiding() && getRidingEntity() == source.getTrueSource())
            return false;
        else if (getPassengers().contains(source.getTrueSource()))
            return false;
        else if ((entity != null) && ((entity instanceof EntityLivingBase)) && ((getPassengers().contains(source.getTrueSource())) || entity.isEntityInvulnerable(source) || entity.height >= 6.0F || (((EntityLivingBase)entity).getTotalArmorValue() > 24 && !((EntityLivingBase)entity).isPotionActive(MobEffects.ABSORPTION)) || (((EntityLivingBase)entity).isPotionActive(MobEffects.STRENGTH) && ((EntityLivingBase)entity).getActivePotionEffect(MobEffects.STRENGTH).getAmplifier() > 255)) && !whiteNoHarmList(entity) && !(entity instanceof EntityTameable))
            return false;
        else if (((source.isMagicDamage()) || (source.isExplosion()) || (source.isFireDamage()) || (source.getDamageType() == "inFire") || (source.getDamageType() == "onFire") || (source.getDamageType() == "lava") || (source.getDamageType() == "inWall") || (source.getDamageType() == "drown") || (source.getDamageType() == "starve") || (source.getDamageType() == "cactus") || (source.getDamageType() == "fall") || (source.getDamageType() == "generic") || (source.getDamageType() == "outOfWorld") || (source.getDamageType() == "magic") || (source.getDamageType() == "wither") || (source.getDamageType() == "anvil") || (source.getDamageType() == "fallingBlock") || (source.getDamageType() == "explosion.player") || (source.getDamageType() == "explosion") || (source.getDamageType() == "indirectMagic")) && !(this instanceof EntitySlimeTitan))
            return false;
        else {
            idleTime = 0;
            if (getHealth() <= 0.0F)
                return false;
            else if (source.isFireDamage())
                return false;
            else {
                if ((entity != null) && ((entity instanceof EntityLivingBase)) && getAnimTick() <= 12 && !(world.provider instanceof WorldProviderVoid)) {
                    setAttackTarget((EntityLivingBase)entity);
                    setRevengeTarget((EntityLivingBase)entity);
                }

                boolean flag = true;
                if (hurtResistantTime > maxHurtResistantTime / 2) {
                    if (amount <= lastDamage)
                        return false;
                    lastDamage = amount;
                    flag = false;
                    damageEntity(source, amount - lastDamage);
                }

                else {
                    lastDamage = amount;
                    hurtResistantTime = maxHurtResistantTime;
                    damageEntity(source, amount);
                }

                attackedAtYaw = 0.0F;
                if (entity != null) {
                    if (entity instanceof EntityLivingBase)
                        setRevengeTarget((EntityLivingBase)entity);
                    if (entity instanceof EntityPlayer) {
                        recentlyHit = 200;
                        attackingPlayer = (EntityPlayer)entity;
                    }

                    else {
                        recentlyHit = 200;
                        attackingPlayer = null;
                    }
                }

                if (flag) {
                    world.setEntityState(this, (byte)2);
                    if (source != DamageSource.DROWN)
                        markVelocityChanged();
                    if (entity != null) {
                        double d1 = entity.posX - posX;
                        double d0;
                        for (d0 = entity.posZ - posZ; d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D)
                            d1 = (Math.random() - Math.random()) * 0.01D;
                        attackedAtYaw = (float)(Math.atan2(d0, d1) * 180.0D / Math.PI) - rotationYaw;
                        knockBack(entity, amount, d1, d0);
                    }

                    else
                        attackedAtYaw = (float)((int)(Math.random() * 2.0D) * 180);
                }

                if (getHealth() <= 0.0F) {
                    if (flag)
                        triggerAudioGiant(fetchDeathAudioGiant(), getSoundVolume(), getSoundPitch());
                    onDeath(source);
                }

                else {
                    if (flag)
                        triggerAudioGiant(fetchHurtAudioGiant(), getSoundVolume(), getSoundPitch());
                }

                return true;
            }
        }
    }

    protected String fetchHurtAudioGiant() {
        return "game.hostile.hurt";
    }

    protected String fetchDeathAudioGiant() {
        return "game.hostile.die";
    }

    protected String fetchAudioStringFall(int damageValue) {
        return "thetitans:titanStep";
    }

    /**Helper: play a custom/vanilla sound-string via SoundHandler (1.7.10 playSound(String) is gone).*/
    protected void triggerAudioGiant(String sound, float volume, float pitch) {
        if (sound != null)
            SoundHandler.whenPlayMob(sound, world, this, volume, pitch);
    }

    public void shakeUserCamerasNearby(double distance) {
        if (world.playerEntities.isEmpty())
            return;
        final double reach = distance * fetchScaleMultiplierGiant();
        for (int idx = 0, count = world.playerEntities.size(); idx < count; idx++) {
            Entity target = world.playerEntities.get(idx);
            if (target != null && target.dimension == dimension && target.isEntityAlive() && target instanceof EntityLivingBase && !(target instanceof EntityTitan) && target.getDistance(this) < reach) {
                target.hurtResistantTime = 0;
                world.setEntityState((EntityLivingBase)target, (byte)2);
            }
        }
    }

    public static boolean isDudMobA(Entity entity) {
        return com.electrifiedded.thetitans.data.TitanFactions.isDudMobA(entity);
    }

    public static boolean quackosIs(Entity entity) {
        return com.electrifiedded.thetitans.data.TitanFactions.quackosIs(entity);
    }

    public static boolean isSpawnBossToExemptOre(Entity entity) {
        return com.electrifiedded.thetitans.data.TitanFactions.isOreSpawnBossAn(entity) || com.electrifiedded.thetitans.data.TitanFactions.isCreaturesBossMythical(entity);
    }

    public static boolean whiteNoHarmList(Entity entity) {
        if (entity instanceof EntityPlayer)
            return true;
        else if (entity instanceof EntityTitan)
            return true;
        else if (entity instanceof EntityGiantZombieBetter)
            return true;
        else if (entity instanceof EntityDragonMinion)
            return true;
        else if (entity instanceof EntityLivingBase && entity.height > 6F)
            return false;
        else if (!(entity instanceof EntityLivingBase) && !(entity instanceof EntityFireball) && !(entity instanceof EntityArrow) && !(entity instanceof EntityThrowable))
            return false;
        else
            return true;
    }

    public float fetchMPArea(float defaultsize) {
        return defaultsize * fetchScaleMultiplierGiant();
    }

    public java.util.List<?> fetchStrikeArea(Entity center, float forwardoffset, float area, float height, boolean followpitch, EntityTitanPart bodypart) {
        double d8 = fetchMPArea(forwardoffset);
        Vec3d vec3 = getLook(1.0F);
        double dx = vec3.x * d8;
        double dy = !followpitch ? 0.0D : vec3.y * d8;
        double dz = vec3.z * d8;
        if (bodypart != null)
            collideWithEntities(bodypart, world.getEntitiesWithinAABBExcludingEntity(this, bodypart.getEntityBoundingBox().expand(fetchMPArea(area * 2.0F), fetchMPArea(height * 2.0F), fetchMPArea(area * 2.0F))));
        return world.getEntitiesWithinAABBExcludingEntity(this, center.getEntityBoundingBox().expand(fetchMPArea(area), fetchMPArea(height), fetchMPArea(area)).offset(dx, dy, dz));
    }

    public void performStrikeArea(EntityLivingBase target, EntityTitanPart bodypart, double shake, float launchvalue, float attack, int knockback, float forwardoffset, float area, float height, boolean followpitch) {
        if (quackosIs(target))
            target = null;
        if (target != null && target.isEntityAlive()) {
            if (target != this)
                strikeMobChoosen(target, (float)fetchAmountStrike((double)attack), fetchAmountKnockback() + knockback);
            if (isClient())
                shakeUserCamerasNearby(shake);
            java.util.List<?> hitarea = fetchStrikeArea(target, forwardoffset, area, height, followpitch, bodypart);
            if (!hitarea.isEmpty())
                for (int i1 = 0; i1 < hitarea.size(); ++i1) {
                Entity caughtmob = (Entity)hitarea.get(i1);
                if (allowStrikeGiant(caughtmob.getClass()) && !isDudMobA(caughtmob) && !quackosIs(caughtmob)) {
                    if (!(caughtmob instanceof EntityTitan))
                        caughtmob.motionY += (double)(launchvalue + rand.nextFloat() * launchvalue);
                    strikeMobChoosen(caughtmob, (float)fetchAmountStrike((double)attack), fetchAmountKnockback() + knockback);
                }
            }
        }
    }

    public void appendStrikeEffectsAddition(Entity damagedEntity) {
        damagedEntity.hurtResistantTime = 0;
    }

    public void strikeMobChoosen(Entity damagedEntity, float damage, int knockbackAmount) {
        if (damagedEntity == null || !isEntityAlive() || (damagedEntity != null && isMob(damagedEntity, EntityFallingBlockTitan.class, EntityItemBomb.class, EntityXPBomb.class, EntityItem.class, EntityXPOrb.class, EntityTitanPart.class, EntityTitanSpirit.class)) || !allowStrikeGiant(damagedEntity.getClass()) || (damagedEntity instanceof EntityPlayer && isBlacklistedUser((EntityPlayer) damagedEntity)))
            return;
        if (isServer() && damagedEntity instanceof EntityPlayerMP) {
            TheTitans.reflect.set(EntityPlayerMP.class, (EntityPlayerMP) damagedEntity, -1, "field_147101_bU");
            ((EntityPlayerMP)damagedEntity).server.getPlayerList().sendPacketToAllPlayers(new SPacketEntityVelocity((EntityPlayerMP)damagedEntity));
        }

        if (TheTitans.isDifficulty(TheTitans.getDifficulty(world), 7, 7))
            damage = Float.MAX_VALUE;
        else if (TheTitans.isDifficulty(world, EnumDifficulty.HARD))
            damage = MathHelper.clamp(damage * 2.0F, 0.0F, Float.MAX_VALUE);
        else if (TheTitans.isDifficulty(world, EnumDifficulty.PEACEFUL))
            damage = MathHelper.clamp(damage * 0.5F, 0.0F, Float.MAX_VALUE);

        appendStrikeEffectsAddition(damagedEntity);

        if (damagedEntity.height < 6.0F) {
            damagedEntity.motionY += rand.nextDouble();
            damagedEntity.addVelocity(-MathHelper.sin(renderYawOffset * (float)Math.PI / 180.0F) * (1 + knockbackAmount) * 0.2D, (1 + knockbackAmount) * 0.2D, MathHelper.cos(renderYawOffset * (float)Math.PI / 180.0F) * (1 + knockbackAmount) * 0.2D);
        }

        if (!(damagedEntity instanceof EntityLivingBase) || (damagedEntity instanceof EntityLivingBase && !(damagedEntity instanceof EntityLiving) && !(damagedEntity instanceof EntityPlayer))) {
            damagedEntity.attackEntityFrom((new DamageSource("other")).setDamageBypassesArmor().setDamageIsAbsolute().setDamageAllowedInCreativeMode(), Float.MAX_VALUE);
            damagedEntity.setDead();
            return;
        }

        if (isCheatingMob(damagedEntity)) {
            SoundHandler.whenPlayMob("thetitans:titanStrike", world, damagedEntity, 50F, 2F);
            if (damagedEntity instanceof EntityPlayer)
                if (!damagedEntity.attackEntityFrom(DamageSourceExtra.causePiercingMobHarmArmour(this).setDamageIsAbsolute().setDamageAllowedInCreativeMode(), damage)) {
                hurtMobBypass((EntityPlayer) damagedEntity, new DamageSource("infinity").setDamageBypassesArmor().setDamageAllowedInCreativeMode().setDamageIsAbsolute(), damage);
                hurtMobBypass((EntityPlayer) damagedEntity, new DamageSource("other").setDamageBypassesArmor().setDamageIsAbsolute().setDamageAllowedInCreativeMode(), damage);
                hurtMobBypass((EntityPlayer) damagedEntity, DamageSource.OUT_OF_WORLD.setDamageIsAbsolute(), damage);
            }

            else
                if (!damagedEntity.attackEntityFrom(new DamageSource("infinity").setDamageBypassesArmor().setDamageAllowedInCreativeMode().setDamageIsAbsolute(), damage))
                hurtMobBypass((EntityLivingBase)damagedEntity, new DamageSource("infinity").setDamageBypassesArmor().setDamageAllowedInCreativeMode().setDamageIsAbsolute(), damage);
            return;
        }

        if (damagedEntity instanceof EntityLivingBase) {
            renderYawOffset = rotationYaw = rotationYawHead;
            damage = MathHelper.clamp(damage + EnchantmentHelper.getModifierForCreature(getHeldItemMainhand(), ((EntityLivingBase)damagedEntity).getCreatureAttribute()), 0.0F, Float.MAX_VALUE);
            TheTitans.reflect.set(EntityLivingBase.class, (EntityLivingBase)damagedEntity, 100, "recentlyHit", "field_70718_bc");
            if (Loader.isModLoaded("OreSpawn") && isSpawnBossToExemptOre(damagedEntity)) {
                TheTitans.reflect.assignAlt(damagedEntity.getClass(), damagedEntity, 0, "hurt_timer");
                TheTitans.reflect.assignAlt(damagedEntity.getClass(), damagedEntity, 0, "large_unknown_detected");
                TheTitans.reflect.assignAlt(damagedEntity.getClass(), damagedEntity, 10, "player_hit_count");
                TheTitans.reflect.assignAlt(damagedEntity.getClass(), damagedEntity, true, "hasDied");
            }

            if (knockbackAmount > 0) {
                knockbackAmount += EnchantmentHelper.getKnockbackModifier(this);
                damagedEntity.motionY += rand.nextDouble();
                damagedEntity.addVelocity(-MathHelper.sin(renderYawOffset * 3.1415927F / 180.0F) * knockbackAmount * 0.25D, knockbackAmount * 0.25D, MathHelper.cos(renderYawOffset * 3.1415927F / 180.0F) * knockbackAmount * 0.25D);
            }

            if (damagedEntity.height >= 6.0F) {
                damage = MathHelper.clamp(damage * 20.0F, 0.0F, Float.MAX_VALUE);
                SoundHandler.whenPlayMob("thetitans:titanpunch", world, damagedEntity, 50F, isChild() ? 1.5F : 1.0F);
            }

            if (EnchantmentHelper.getFireAspectModifier(this) > 0)
                damagedEntity.setFire(EnchantmentHelper.getFireAspectModifier(this) * 100);
            if (damagedEntity instanceof EntityEnderCrystal) {
                damagedEntity.attackEntityFrom((new DamageSource("other")).setDamageBypassesArmor().setDamageIsAbsolute().setDamageAllowedInCreativeMode(), 100F);
                return;
            }

            else if (damagedEntity instanceof EntityDragon) {
                world.newExplosion(null, damagedEntity.posX, damagedEntity.posY, damagedEntity.posZ, 6F, false, false);
                return;
            }

            else if (damagedEntity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer)damagedEntity;
                for (int i = 0; i < player.inventory.armorInventory.size(); ++i)
                    if (!player.inventory.armorInventory.get(i).isEmpty())
                    player.inventory.armorInventory.get(i).setItemDamage((int)(Math.min(player.inventory.armorInventory.get(i).getItemDamage() + damage / (fetchMultiplierGiant() * 100.0D), player.inventory.armorInventory.get(i).getMaxDamage())));
            }

            damagedEntity.attackEntityFrom(damagedEntity.getEyeHeight() > 5 ? DamageSourceExtra.causeGiantHarmAnti(this) : DamageSource.causeMobDamage(this), damage);
        }

        else {
            hurtMobBypass((EntityLivingBase) damagedEntity, DamageSourceExtra.causeStealingHarmSoul(this), damage);
            int b0 = 1 + TheTitans.getDifficulty(world);
            ((EntityLivingBase)damagedEntity).addPotionEffect(new PotionEffect(CommonProxy.electricJudgment, b0 * 20, 2));
        }

        if (damagedEntity instanceof EntityLiving && ((EntityLiving)damagedEntity).getAttackTarget() == null)
            ((EntityLiving)damagedEntity).setAttackTarget(this);
        if (damagedEntity instanceof EntityPlayer && !((EntityPlayer)damagedEntity).isEntityAlive())
            ((EntityPlayer)damagedEntity).addStat(TheTitans.titansStatDeaths, 1);
    }

    private void hurtMobBypass(EntityLivingBase entity, DamageSource source, float damage) {
        if (damage > 0.0F && entity.isEntityAlive()) {
            float calDamage = damage;
            calDamage = Math.max(damage - entity.getAbsorptionAmount(), 0.0F);
            entity.setAbsorptionAmount(entity.getAbsorptionAmount() - calDamage);
            if (calDamage != 0.0F) {
                float rawHealth = entity.getHealth();
                if (entity instanceof EntityCreature)
                    EntityIronGolemTitan.appendTargetingTaskToMobGiant((EntityCreature)entity);
                if (!entity.attackEntityFrom(source, calDamage))
                    entity.setHealth(MathHelper.clamp(rawHealth - calDamage, 0.0F, entity.getMaxHealth()));
                entity.getCombatTracker().trackDamage(source, rawHealth, calDamage);
                if (!(entity instanceof EntityTitan)) {
                    if (entity.height == 50F && entity.width == 15F) {
                        entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(0D);
                        entity.attackEntityFrom(DamageSourceExtra.causeGiantHarmAnti(this).setDamageBypassesArmor().setDamageIsAbsolute(), 40.0F);
                        entity.addPotionEffect(new PotionEffect(CommonProxy.death, Integer.MAX_VALUE, 19));
                    }

                    if (!entity.isEntityAlive())
                        entity.onDeath(source);
                }
            }
        }
    }

    public boolean allowHurtByUserBe() {
        return !isEntityInvulnerable(DamageSource.GENERIC);
    }

    public boolean attackEntityAsMob(Entity arg70652n1) {
        swingArm(EnumHand.MAIN_HAND);
        float f = (float)fetchAmountStrike(2.0F);
        int i = fetchAmountKnockback();
        strikeMobChoosen(arg70652n1, f, i);
        if ((arg70652n1 instanceof EntityMob))
            ((EntityMob)arg70652n1).setRevengeTarget(this);
        getLookHelper().setLookPositionWithEntity(arg70652n1, 180.0F, getVerticalFaceSpeed());
        return true;
    }

    protected boolean isValidLightLevel() {
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(getEntityBoundingBox().minY);
        int k = MathHelper.floor(posZ);
        BlockPos pos = new BlockPos(i, j, k);
        if (world.getLightFor(EnumSkyBlock.SKY, pos) > rand.nextInt(32)) {
            return false;
        }

        int l = world.getLightFromNeighbors(pos);
        if (world.isThundering()) {
            int i1 = world.getSkylightSubtracted();
            world.setSkylightSubtracted(10);
            l = world.getLightFromNeighbors(pos);
            world.setSkylightSubtracted(i1);
        }

        return l <= rand.nextInt(8);
    }

    public boolean getCanSpawnHere() {
        return (world.getDifficulty() != EnumDifficulty.PEACEFUL);
    }

    public void onKillCommand() {
        if (fetchConditionGiant() != EnumTitanStatus.GOD) {
            triggerAudioGiant(fetchDeathAudioGiant(), getSoundVolume(), getSoundPitch());
            setDead();
        }
    }

    protected boolean canDropLoot() {
        return false;
    }

    public String fetchAchievement() {
        return null;
    }

    public boolean isEntityInvulnerable(DamageSource source) {
        return (getInvulTime() >= 1) || fetchWaiting() || getAnimID() == 13 || (deathTicks > 0) || (this instanceof EntityWitherzilla && !(world.provider instanceof WorldProviderVoid) && fetchPowerExtra() > 5) || super.isEntityInvulnerable(source);
    }

    protected void warpRandomlyMinion(EntityLivingBase entity) {
        double d0 = posX + (rand.nextDouble() - 0.5D) * (72D + width);
        double d1 = posY;
        double d2 = posZ + (rand.nextDouble() - 0.5D) * (72D + width);
        entity.setLocationAndAngles(d0, d1, d2, rotationYawHead, rotationPitch);
    }

    protected boolean warpRandomlyMob(EntityLivingBase entity) {
        double d0 = posX + (rand.nextDouble() - 0.5D) * (72D + width);
        double d1 = posY - height + (height * 2F);
        double d2 = posZ + (rand.nextDouble() - 0.5D) * (72D + width);
        return warpToMob(entity, d0, d1, d2);
    }

    protected boolean warpToMob(EntityLivingBase entity, double arg70825n1, double arg70825n3, double arg70825n5) {
        EnderTeleportEvent event = new EnderTeleportEvent(entity, arg70825n1, arg70825n3, arg70825n5, 0);
        if (MinecraftForge.EVENT_BUS.post(event))
            return false;
        double d3 = posX;
        double d4 = posY;
        double d5 = posZ;
        entity.posX = event.getTargetX();
        entity.posY = event.getTargetY();
        entity.posZ = event.getTargetZ();
        boolean flag = false;
        int i = MathHelper.floor(entity.posX);
        int j = MathHelper.floor(entity.posY);
        int k = MathHelper.floor(entity.posZ);
        if (world.isBlockLoaded(new BlockPos(i, j, k))) {
            boolean flag1 = false;
            while (!flag1 && j > 0) {
                IBlockState state = world.getBlockState(new BlockPos(i, j - 1, k));
                if (state.getMaterial().isSolid())
                    flag1 = true;
                else {
                    --entity.posY;
                    --j;
                }
            }

            if (flag1) {
                entity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, rotationYaw, rotationPitch);
                if (world.getCollisionBoxes(entity, entity.getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(entity.getEntityBoundingBox()))
                    flag = true;
            }
        }

        if (!flag) {
            entity.setLocationAndAngles(d3, d4, d5, rotationYaw, rotationPitch);
            return false;
        }

        else
            return true;
    }

    public void retractNumFromKindMinion(EnumMinionType minionType) {
        if (minionType == EnumMinionType.SPECIAL)
            --specialMinionsNum;
        else if (minionType == EnumMinionType.PRIEST)
            --priestsNum;
        else if (minionType == EnumMinionType.ZEALOT)
            --zealotsNum;
        else if (minionType == EnumMinionType.BISHOP)
            --bishopNum;
        else if (minionType == EnumMinionType.TEMPLAR)
            --templarNum;
        else if (minionType == EnumMinionType.LORD)
            --lordsNum;
        else if (minionType == EnumMinionType.LOYALIST)
            --minionsNum;
    }

    public boolean alreadyALabelHas() {
        return false;
    }

    public float fetchScaleMultiplierGiant() {
        float runGrowth = 48.0F;
        if (deathTicks >= 800)
            runGrowth = 48.0F - (float)(deathTicks - 800) / 5.0F;
        if (deathTime >= 800)
            runGrowth = 48.0F - (float)(deathTime - 800) / 5.0F;
        if (runGrowth <= 1.0F)
            runGrowth = 1.0F;
        if (runGrowth >= 48.0F)
            runGrowth = 48.0F;
        if (runGrowth >= 16.0F && fetchConditionGiant() == EnumTitanStatus.SIMPLE)
            runGrowth = 16.0F;
        return fetchConditionGiant() == EnumTitanStatus.GOD ? 64.0F : (fetchConditionGiant() != EnumTitanStatus.SIMPLE && fetchConditionGiant() != EnumTitanStatus.UTILITY ? runGrowth * (1.0F + (float)fetchPowerExtra() * 1.0E-5F) : runGrowth);
    }

    public void setAnimID(int id) {
        dataManager.set(KEY_MOTION, Integer.valueOf(id));
    }

    public void setAnimTick(int tick) {
        dataManager.set(BEAT_MOTION, Integer.valueOf(tick));
    }

    public int getAnimID() {
        return dataManager.get(KEY_MOTION).intValue();
    }

    public int getAnimTick() {
        return dataManager.get(BEAT_MOTION).intValue();
    }

    public void setFire(int arg70015n1) {
    }

    public boolean canBeLeashedTo(EntityPlayer player) {
        return false;
    }

    protected EntityLiving fetchMinion() {
        return null;
    }

    public boolean isClient() {
        return world.isRemote;
    }

    public boolean isServer() {
        return !world.isRemote;
    }

    public boolean attackEntityFromPart(EntityTitanPart arg70965n1, DamageSource source, float amount) {
        return attackEntityFrom(source, amount);
    }

    protected void emitMinion(EntityLiving minion) {
        emitMinion(minion, 0);
    }

    private void emitBMinion(EntityLiving minion, boolean isGrounded, double offset, float volume, float pitch, String sound) {
        if (offset == 0)
            warpRandomlyMinion(minion);
        else
            minion.setLocationAndAngles(posX, posY - offset, posZ, rotationYaw, 0.0F);
        SoundHandler.whenPlayMob(sound, world, minion, volume, pitch);
        minion.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(minion)), null);
        world.spawnEntity(minion);
        minion.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 40, 4, false, false));
        if (isGrounded) {
            minion.addVelocity(0.0D, 0.6D * minion.height, 0.0D);
            BlockPos pos = new BlockPos((int)minion.posX, (int)(minion.posY), (int)minion.posZ);
            IBlockState state = world.getBlockState(pos);
            world.playEvent(2001, pos, Block.getStateId(state));
            if (state.getBlock() == Blocks.GRASS)
                world.setBlockState(pos, Blocks.DIRT.getDefaultState());
        }
    }

    protected void emitMinion(EntityLiving minion, int minionType) {
        EntityLiving entity;
        switch(minionType) {
            case 1: priestsNum ++; break;
            case 2: zealotsNum ++; break;
            case 3: bishopNum ++; break;
            case 4: templarNum ++; break;
            case 5: lordsNum ++; break;
            case 6: specialMinionsNum ++; break;
            default: minionsNum ++; break;
        }

        try {
            if (minion != null && IMinion.class.isAssignableFrom(minion.getClass())) {
                entity = minion.getClass().getConstructor(World.class).newInstance(world);
                ((IMinion)entity).setMinionType(minionType);
                ((IMinion)entity).assignMaster(this);
                emitBMinion(entity, ((IMinion)entity).beginGrounded(), ((IMinion)entity).fetchYOffsetSummon(), ((IMinion)entity).fetchVolumeSummon(), ((IMinion)entity).fetchPitchSummon(), ((IMinion)entity).fetchAudioSummon());
            }

            else if (minion instanceof EntityIronGolem)

                {
                entity = new EntityIronGolem(world);
                ((EntityIronGolem)entity).setPlayerCreated(((EntityIronGolemTitan)this).isPlayerCreated());
                ((EntityIronGolem)entity).getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(2000.0D);
                ((EntityIronGolem)entity).setHealth(2000.0F);
                ((EntityIronGolem)entity).setCustomNameTag("Reinforced Iron Golem");
                EntityIronGolemTitan.appendTargetingTaskToMobGiant((EntityIronGolem)entity);
                ((EntityIronGolem)entity).getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
                ((EntityIronGolem)entity).getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
                emitBMinion(entity, false, -getEyeHeight(), 2F, 1F, "thetitans:titansummonminion");
            }

            else if (minion instanceof EntitySnowman)

                {
                entity = new EntitySnowman(world);
                ((EntitySnowman)entity).getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
                ((EntitySnowman)entity).setHealth(20.0F);
                ((EntitySnowman)entity).setCustomNameTag("Reinforced Snow Golem");
                emitBMinion(entity, false, -getEyeHeight(), 2F, 1F, "thetitans:titansummonminion");
            }

            else if (minion instanceof EntityGargoyle)

                {
                entity = new EntityGargoyle(world);
                ((EntityGargoyle)entity).assignKindGargoyle(rand.nextInt(5));
                emitBMinion(entity, false, -getEyeHeight(), 2F, 1F, "thetitans:titansummonminion");
            }
        }

        catch (Exception e) {
            entity = new EntityChicken(world);
            emitBMinion(entity, true, -1D, 10F, 1F, "mob.chicken.hurt");
        }
    }

    /**Sets max health to fetchAmountHp() (status/size/variant dependent) and refills HP whenever it changes.*/
    protected void syncHpMax() {
        double value = fetchAmountHp();
        IAttributeInstance maxHealth = getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
        if (maxHealth.getBaseValue() != value) {
            // BUGFIX — the max depends on fetchScaleMultiplierGiant(), which shrinks while a titan dies
            // (deathTicks >= 800): the old full heal here revived dying titans every tick ("defeated" again and
            // again, the mutant zombie never died). Keep the health fraction; never heal a dead/dying titan.
            float oldMax = getMaxHealth(), hp = getHealth();
            maxHealth.setBaseValue(value);
            if (hp > 0.0F && deathTicks <= 0)
                setHealth(oldMax > 0.0F ? (float)Math.min(getMaxHealth(), hp / oldMax * getMaxHealth()) : getMaxHealth());
            else if (hp > getMaxHealth())
                setHealth(getMaxHealth());
        }
    }

    public void heal(double value) {
        if (value > 0.0D && getHealth() > 0.0F)
            setHealth((float)Math.min((double)getMaxHealth(), getHealth() + value));
    }

    public void hurt(double value) {
        if (isEntityInvulnerable(DamageSource.GENERIC))
            return;
        double damage = MathHelper.clamp(value, 0.0D, cap());
        assignStamina(fetchStamina() - (float)damage);
        setHealth((float)Math.max(0.0D, getHealth() - damage));
    }

    public boolean soulessIs() {
        return soulessIs;
    }

    protected double cap() {
        if (stunnedIs)
            return getMaxHealth() / 2.0D;
        switch(fetchConditionGiant()) {
            case GOD: return getMaxHealth() / 100.0D;
            case GREATER: return getMaxHealth() / 60.0D;
            case AVERAGE: return getMaxHealth() / 30.0D;
            case LESSER: return getMaxHealth() / 10.0D;
            default: return getMaxHealth() + 1.0D;
        }
    }

    protected double fetchRegen() {
        double mult = TheTitans.adjustValueToProgressionSystem(world, 5.0D);
        switch(fetchConditionGiant()) {
            case GOD: return 200.0D * mult;
            case GREATER: return 45.0D * mult;
            case AVERAGE: return 15.0D * mult;
            case LESSER: return 5.0D * mult;
            default: return 0.0D * mult;
        }
    }

    public float fetchStamina() {
        return dataManager.get(STAMINA_V).floatValue();
    }

    protected void assignStamina(float value) {
        dataManager.set(STAMINA_V, Float.valueOf(value));
    }

    public float fetchStaminaMax() {
        return dataManager.get(STAMINA_CAP).floatValue();
    }

    protected void assignStaminaMax(float value) {
        dataManager.set(STAMINA_CAP, Float.valueOf(value));
    }

    protected boolean isBlacklistedUser(EntityPlayer player) {
        if (player == null)
            return false;
        String uuid = player.getUniqueID().toString();
        return uuid == "07d5aa7f-81d0-41e1-8981-04723d12c2ef" || uuid == "19d96ed2-6c4d-42bd-9855-498482daa5ab" || uuid == "39c0cf10-5f5d-4c89-8057-cee67479c7c2";
    }

    protected boolean isCheatingMob(Entity entity) {
        if (!TitanConfig.antiCheatIs || !(entity instanceof EntityLivingBase) || entity instanceof EntityTitan)
            return false;
        EntityLivingBase living = (EntityLivingBase)entity;
        if (living instanceof EntityPlayer && (TheTitans.equippedAll(living, TitanGear.armourSetDemontium) || TheTitans.equippedAll(living, TitanGear.armourSetHarcadium) || TheTitans.equippedAll(living, TitanGear.armourSetWithrenium) || TheTitans.equippedAll(living, TitanGear.armourSetTitanus) || TheTitans.equippedAll(living, TitanGear.armourSetVoid) || TheTitans.equippedAll(living, TitanGear.armourSetAdminium)))
            return false;
        return living.getIsInvulnerable() || living.getAbsorptionAmount() > 10000.0F || (living.isPotionActive(MobEffects.RESISTANCE) && living.getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier() > 4) || (living instanceof EntityPlayer && ((EntityPlayer)living).capabilities.disableDamage);
    }

    protected boolean isMob(Entity entity,  Class<?>... clazzes) {
        if (entity == null)
            return false;
        for(Class<?> clazz : clazzes)
            if (clazz.equals(entity.getClass()))
            return true;
        return false;
    }

    protected int recoveryTicks() {
        return 420;
    }

    @Override
    public boolean needsCrush() {
        return false;
    }
}
