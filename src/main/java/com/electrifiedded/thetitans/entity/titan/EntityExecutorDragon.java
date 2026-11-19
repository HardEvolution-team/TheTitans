package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.core.TheCore;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;
import com.electrifiedded.thetitans.item.spawn.ItemTitanSpawnEgg;
import com.electrifiedded.thetitans.registry.TitanBlockSet;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.sound.SoundHandler;
import com.electrifiedded.thetitans.world.dimension.WorldProviderNowhere;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.stats.StatBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Nullable;

public final class EntityExecutorDragon extends EntityTitan implements IRangedAttackMob {
    private float[] fld82220d = new float[2];
    private float[] fld82221e = new float[2];
    private float[] fld82217f = new float[2];
    private float[] fld82218g = new float[2];
    private int blockBreakCounter;
    public int beatsAffect;
    private int attackTimer;
    @SuppressWarnings("unchecked")
    ArrayList<?> userPoolAll = Lists.newArrayList(this.world.playerEntities);

    private static final Predicate<Entity> mobSelectorStrike = new Predicate<Entity>() {
        public boolean apply(@Nullable Entity arg180027n1) {
            return (arg180027n1 != null) && (!(arg180027n1 instanceof EntityTitanSpirit)) && (!(arg180027n1 instanceof EntityWitherzillaMinion)) && (!(arg180027n1 instanceof EntityWitherTurret)) && (!(arg180027n1 instanceof EntityWitherTurretGround)) && (!(arg180027n1 instanceof EntityWitherTurretMortar));
        }
    };

    public EntityExecutorDragon(World worldIn) {
        super(worldIn);
        this.noClip = true;
        this.experienceValue = 5000000;
        triggerAudioGiant("thetitans:witherzillaSpawn", Float.MAX_VALUE, 1F);
    }

    protected void whenUpdateBounds() {
        super.whenUpdateBounds();
    }

    protected void applyEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, mobSelectorStrike));
    }

    @Override
    public double fetchAmountHp() {
        return super.fetchAmountHp() * 200.0D;
    }

    public float fetchScaleMultiplierGiant() {
        return this.isOmegaFormIn() ? super.fetchScaleMultiplierGiant() * 2.0F : super.fetchScaleMultiplierGiant();
    }

    public boolean alreadyALabelHas() {
        return true;
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public void fall(float arg70069n1, float damageMultiplier) {
    }

    public int fetchSpawnRateMinion() {
        return TheTitans.servantSpawnrateWitherzilla;
    }

    public int getParticleCount() {
        return 100;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(Double.MAX_VALUE);
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    public EnumTitanStatus fetchConditionGiant() {
        return EnumTitanStatus.GOD;
    }

    public float getRenderSizeModifier() {
        return isOmegaFormIn() ? 128.0F : 64.0F;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VICTIM_0_WATCHED, Integer.valueOf(0));
        this.dataManager.register(VICTIM_1_WATCHED, Integer.valueOf(0));
        this.dataManager.register(VICTIM_2_WATCHED, Integer.valueOf(0));
    }

    private static final net.minecraft.network.datasync.DataParameter<Integer> VICTIM_0_WATCHED = net.minecraft.network.datasync.EntityDataManager.<Integer>createKey(EntityExecutorDragon.class, net.minecraft.network.datasync.DataSerializers.VARINT);
    private static final net.minecraft.network.datasync.DataParameter<Integer> VICTIM_1_WATCHED = net.minecraft.network.datasync.EntityDataManager.<Integer>createKey(EntityExecutorDragon.class, net.minecraft.network.datasync.DataSerializers.VARINT);
    private static final net.minecraft.network.datasync.DataParameter<Integer> VICTIM_2_WATCHED = net.minecraft.network.datasync.EntityDataManager.<Integer>createKey(EntityExecutorDragon.class, net.minecraft.network.datasync.DataSerializers.VARINT);
    private static final net.minecraft.network.datasync.DataParameter<Integer>[] TARGETS_WATCHED = new net.minecraft.network.datasync.DataParameter[] { VICTIM_0_WATCHED, VICTIM_1_WATCHED, VICTIM_2_WATCHED };

    protected String fetchLivingAudioGiant() {
        return "thetitans:executorDragonLiving";
    }

    protected String fetchHurtAudioGiant() {
        return "thetitans:executorDragonGrunt";
    }

    protected String fetchDeathAudioGiant() {
        return "thetitans:executorDragonDeath";
    }

    public boolean getCanSpawnHere() {
        return false;
    }

    public boolean allowHurtByUserBe() {
        return (this.world.getDifficulty() != EnumDifficulty.PEACEFUL) && (!isEntityInvulnerable(DamageSource.GENERIC));
    }

    protected void kill() {
        this.world.playBroadcastSound(1013, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
        if (((this.world.provider instanceof WorldProviderNowhere)) || ((this.world.provider instanceof WorldProviderEnd))) {
            teleportRandomly(true);
        }

        else {
            teleportRandomly(false);
        }
    }

    public void performStrikeToLightning(Entity entity) {
        // particles are not Entities in 1.12.2, so the old EntityFX check is dropped
        if (entity != null && entity.isEntityAlive() && !(entity instanceof EntityTitanPart)) {
            if (entity instanceof EntityPlayer) {
                if (rand.nextInt(1000) == 0) {
                    this.strikeMobChoosen(entity, ((EntityPlayer)entity).getHealth() / 2, 5);
                    entity.setLocationAndAngles(entity.posX, this.posY + this.getEyeHeight(), entity.posZ, this.rotationYawHead + 180, 0F);
                }

                ((EntityPlayer)entity).sendMessage(new TextComponentString("§k" + this.rand.nextInt(1234567890)));
                if (((EntityPlayer)entity).inventory.hasItemStack(new ItemStack(TitanGear.optimaAxe))) {
                    ((EntityPlayer)entity).motionX = 0D;
                    ((EntityPlayer)entity).motionY = 0D;
                    ((EntityPlayer)entity).motionZ = 0D;
                    return;
                }
            }

            this.world.addWeatherEffect(new EntityUrLightning(this.world, entity.posX, entity.posY, entity.posZ));
            if ((entity != null) && entity instanceof EntityLivingBase && ((this.beatsAffect >= 600) || (entity.height >= 6.0F)) && (!(entity instanceof EntityTitan))) {
                ((EntityLivingBase)entity).setHealth(0.0F);
                strikeMobChoosen(entity, 2.14748365E9F, 0);
            }

            if (entity instanceof EntityTitan && entity instanceof ITitanHitbox) {
                Entity parts[] = ((ITitanHitbox)entity).getArray();
                if (parts != null) {
                    for (int j = 0; j < parts.length; j++) {
                        this.world.addWeatherEffect(new EntityUrLightning(this.world, parts[j].posX, parts[j].posY, parts[j].posZ));
                    }
                }
            }

            if ((entity instanceof EntityTitan))
                ((EntityTitan)entity).setInvulTime(((EntityTitan)entity).getInvulTime() - 20);
            else
                entity.motionY += 0.5D;
            strikeMobChoosen(entity, entity instanceof EntityTitan || entity.height >= 6.0F ? 100F : 20F, 1);
            shakeUserCamerasNearby(10D);
        }
    }

    public void setAttackTarget(EntityLivingBase arg70624n1) {
        if (((this.world.provider instanceof WorldProviderNowhere)) && (arg70624n1 != null) && (this.posX == 0.0D) && (this.posY == 200.0D) && (this.posZ == 0.0D)) {
            arg70624n1 = null;
        }

        else {
            super.setAttackTarget(arg70624n1);
        }
    }

    @SuppressWarnings("unchecked")
    public void onLivingUpdate() {
        this.noClip = true;
        if (!this.world.isRemote && !(this.world.provider instanceof WorldProviderNowhere)) {
            WorldServer worldserver = this.world.getMinecraftServer().getWorld(0);
            WorldInfo worldinfo = worldserver.getWorldInfo();
            if (this.getAttackTarget() != null && this.getAttackTarget() instanceof EntityEnderColossus) {
                worldinfo.setRainTime(0);
                worldinfo.setThunderTime(0);
                worldinfo.setRaining(false);
                worldinfo.setThundering(false);
            }

            else {
                worldinfo.setRainTime(9999999);
                worldinfo.setThunderTime(1000000);
                worldinfo.setRaining(true);
                worldinfo.setThundering(true);
            }
        }

        if (!(this.world.provider instanceof WorldProviderNowhere)) {
            if (this.rand.nextInt(2) == 0) {
                for (int l = 0; l < 20; l++) {
                    int i = MathHelper.floor(this.posX);
                    int j = MathHelper.floor(this.posY);
                    int k = MathHelper.floor(this.posZ);
                    int i1 = i + MathHelper.getInt(this.rand, 10, 100) * MathHelper.getInt(this.rand, -1, 1);
                    int j1 = j + MathHelper.getInt(this.rand, 10, 100) * MathHelper.getInt(this.rand, -1, 1);
                    int k1 = k + MathHelper.getInt(this.rand, 10, 100) * MathHelper.getInt(this.rand, -1, 1);
                    // PERF — cheap rolls first; the old order built a lightning entity 20x every other tick and could
                    // load/generate chunks up to 100 blocks away through isSideSolid.
                    BlockPos below = new BlockPos(i1, j1 - 1, k1);
                    if (this.rand.nextInt(5) != 0 || !this.world.isBlockLoaded(below) || !this.world.isSideSolid(below, net.minecraft.util.EnumFacing.UP))
                        continue;
                    EntityUrLightning entitylightning = new EntityUrLightning(this.world, i1, j1, k1);
                    if ((this.world.checkNoEntityCollision(entitylightning.getEntityBoundingBox())) && (this.world.getCollisionBoxes(entitylightning, entitylightning.getEntityBoundingBox()).isEmpty()) && (!this.world.containsAnyLiquid(entitylightning.getEntityBoundingBox()))) {
                        this.world.addWeatherEffect(entitylightning);
                    }
                }
            }
        }

        else {
            if (this.getDistanceSq(0D, 200D, 0D) > 50000D)
                this.setPosition(0D, 200D, 0D);
            List<?> listp = this.world.loadedEntityList;
            if (!this.world.isRemote && rand.nextInt(4) == 0 && !listp.isEmpty()) {
                for (int i1 = 0; i1 < listp.size(); i1++) {
                    Entity entity = (Entity)listp.get(i1);
                    if (entity != null && entity.isEntityAlive()) {
                        if (entity instanceof EntityWitherTurret && !((EntityWitherTurret)entity).isPlayerCreated()) {
                            this.setPosition(0D, 200D, 0D);
                            this.renderYawOffset = this.rotationYaw = this.rotationYawHead = 0F;
                            this.setAttackTarget(null);
                        }

                        if (entity instanceof EntityWitherTurretGround && !((EntityWitherTurretGround)entity).isPlayerCreated()) {
                            this.setPosition(0D, 200D, 0D);
                            this.renderYawOffset = this.rotationYaw = this.rotationYawHead = 0F;
                            this.setAttackTarget(null);
                        }

                        if (entity instanceof EntityWitherTurretMortar && !((EntityWitherTurretMortar)entity).isPlayerCreated()) {
                            this.setPosition(0D, 200D, 0D);
                            this.renderYawOffset = this.rotationYaw = this.rotationYawHead = 0F;
                            this.setAttackTarget(null);
                        }
                    }
                }
            }
        }

        setSize(0.45F * this.fetchScaleMultiplierGiant(), 1.6F * this.fetchScaleMultiplierGiant());
        this.world.setWorldTime(18000);
        ArrayList<?> listp = Lists.newArrayList(this.world.playerEntities);
        if ((listp != null) && (!listp.isEmpty()) && rand.nextInt(4) == 0) {
            for (int i1 = 0; i1 < listp.size(); i1++) {
                Entity entity = (Entity)listp.get(i1);
                if ((entity != null) && ((entity instanceof EntityPlayer)) && (this.rand.nextInt(100) == 0)) {
                    playLivingSound();
                    if (!(this.world.provider instanceof WorldProviderNowhere)) {
                        ((EntityPlayer)entity).attackEntityFrom((new DamageSource("generic")).setDamageAllowedInCreativeMode().setDamageBypassesArmor().setDamageIsAbsolute(), 10.0F);
                        ((EntityPlayer)entity).sendMessage(new TextComponentString(I18n.translateToLocal("dialog.executordragon.taunt." + this.rand.nextInt(6))));
                    }

                    else {
                        ((EntityPlayer)entity).sendMessage(new TextComponentString(I18n.translateToLocal("dialog.executordragon.plead." + this.rand.nextInt(6))));
                    }
                }
            }
        }

        List<?> list = this.world.loadedEntityList;
        // PERF — full entity walk every tick; the body only does anything with a target or while invulnerable.
        if ((getAttackTarget() != null || this.getInvulTime() > 1) && !list.isEmpty() && !(this.world.provider instanceof WorldProviderNowhere)) {
            for (int i1 = 0; i1 < list.size(); i1++) {
                Entity entity = (Entity)list.get(i1);
                if ((entity != null) && entity.isEntityAlive() && (getAttackTarget() != null) && ((entity instanceof EntityWitherzillaMinion))) {
                    ((EntityWitherzillaMinion)entity).setAttackTarget(getAttackTarget());
                }

                if ((entity != null) && entity.isEntityAlive() && !(entity instanceof EntityWitherzillaMinion) && !(entity instanceof EntityWitherSkull) && !(entity instanceof EntityWitherTurret) && !(entity instanceof EntityWitherTurretGround) && !(entity instanceof EntityWitherTurretMortar) && !(entity instanceof EntityWeatherEffect) && !(entity instanceof EntityWitherzillaMinion) && !(entity instanceof EntityTitan) && !(entity instanceof EntityTitanSpirit) && !(entity instanceof EntityPlayer)) {
                    if (this.getInvulTime() > 1) {
                        if (entity instanceof EntityLivingBase) {
                            if (entity instanceof EntityLiving) {
                                ((EntityLiving)entity).getNavigator().clearPath();
                            }

                            entity.motionY = 0.05D - this.motionY * 0.2D;
                            ((EntityLivingBase)entity).hurtResistantTime = (int) rand.nextGaussian() * 20;
                            ((EntityLivingBase)entity).moveForward = (float)rand.nextGaussian();
                            ((EntityLivingBase)entity).moveStrafing = (float)rand.nextGaussian();
                            ((EntityLivingBase)entity).renderYawOffset = ((EntityLivingBase)entity).rotationYaw = ((EntityLivingBase)entity).rotationYawHead += (float) rand.nextGaussian() * 10F;
                        }

                        else {
                            entity.motionX = rand.nextGaussian() * 0.5D;
                            entity.motionY = rand.nextGaussian() * 0.5D;
                            entity.motionZ = rand.nextGaussian() * 0.5D;
                            entity.rotationYaw += 10F;
                            entity.hurtResistantTime = (int) rand.nextGaussian() * 20;
                        }
                    }
                }
            }
        }

        if ((this.ticksExisted % 3 + this.rand.nextInt(3) == 0) || this.getInvulTime() >= 1000)
            setCustomNameTag("§kFrotheru");
        else
            setCustomNameTag("§l" + I18n.translateToLocal("entity.EnderDragonTitan.name"));
        if (getAttackTarget() != null && !(this.world.provider instanceof WorldProviderNowhere) && this.ticksExisted % 100 == 0)
            performStrikeToLightning(getAttackTarget());
        this.beatsAffect += 1;
        if (this.beatsAffect >= 1010) {
            this.beatsAffect = 0;
        }

        if ((this.minionsNum < fetchCapMinion()) && (this.rand.nextInt(fetchSpawnRateMinion()) == 0) && (getHealth() > 0.0F) && (!this.world.isRemote)) {
            EntityWitherzillaMinion entitychicken = new EntityWitherzillaMinion(this.world);
            entitychicken.setLocationAndAngles(this.posX, this.posY + getEyeHeight(), this.posZ, this.rotationYaw, 0.0F);
            entitychicken.funcM82206();
            this.world.spawnEntity(entitychicken);
            entitychicken.addVelocity(-MathHelper.sin(this.rotationYawHead * 3.1415927F / 180.0F) * 5F, -2D, MathHelper.cos(this.rotationYawHead * 3.1415927F / 180.0F) * 5F);
            this.minionsNum += 1;
        }

        if ((this.rand.nextInt(120) == 0) && (getAttackTarget() != null) && (!this.world.isRemote)) {
            if (TheTitans.isDifficulty(world, TheCore.NIGHTMARE_V) == true) {
                this.world.newExplosion(this, getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 14.0F, true, true);
            }

            else {
                this.world.newExplosion(this, getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 7.0F, false, this.world.getGameRules().getBoolean("mobGriefing"));
            }
        }

        if (!isArmored()) {
            this.motionY *= 0.1D;
        }

        else {
            this.motionY *= 0.9D;
        }

        if ((!this.world.isRemote) && (getWatchedTargetId(0) > 0)) {
            Entity entity = this.world.getEntityByID(getWatchedTargetId(0));
            if (entity != null) {
                double d0 = entity.posX - this.posX;
                double d1 = (entity.posY + (this.isArmored() ? 2D : 12D)) - this.posY;
                double d2 = entity.posZ - this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                getLookHelper().setLookPositionWithEntity(entity, 180.0F, 40.0F);
                if (((entity instanceof EntityLivingBase)) && (d3 < 10000.0D)) {
                    attackEntityWithRangedAttack((EntityLivingBase)entity, 0.0F);
                }

                if (d3 > 64.0D) {
                    double d5 = MathHelper.sqrt(d3);
                    this.motionX += d0 / d5 * 1.5D - this.motionX;
                    this.motionY += d1 / d5 * 1.5D - this.motionY;
                    this.motionZ += d2 / d5 * 1.5D - this.motionZ;
                    this.renderYawOffset = (this.rotationYaw = (float)Math.atan2(this.motionZ, this.motionX) * 57.295776F - 90.0F);
                }
            }
        }

        super.onLivingUpdate();
        for (int i = 0; i < 2; i++) {
            this.fld82218g[i] = this.fld82221e[i];
            this.fld82217f[i] = this.fld82220d[i];
        }

        for (int i = 0; i < 2; i++) {
            int j = getWatchedTargetId(i + 1);
            Entity mobRef1 = null;
            if (j > 0) {
                mobRef1 = this.world.getEntityByID(j);
            }

            if (mobRef1 != null) {
                double d1 = funcU82214(i + 1);
                double d3 = funcV82208(i + 1);
                double d5 = funcW82213(i + 1);
                double d6 = mobRef1.posX - d1;
                double d7 = mobRef1.posY + mobRef1.getEyeHeight() - d3;
                double d8 = mobRef1.posZ - d5;
                double d9 = MathHelper.sqrt(d6 * d6 + d8 * d8);
                float f = (float)(Math.atan2(d8, d6) * 180.0D / 3.141592653589793D) - 90.0F;
                float f1 = (float)-(Math.atan2(d7, d9) * 180.0D / 3.141592653589793D);
                this.fld82220d[i] = funcB82204(this.fld82220d[i], f1, 5.0F);
                this.fld82221e[i] = funcB82204(this.fld82221e[i], f, 5.0F);
            }

            else {
                if (this.rand.nextInt(120) == 0) {
                    for (j = 0; j < 36; j++) {
                        this.fld82220d[i] = funcB82204(this.fld82220d[i], this.rand.nextFloat() * 360.0F - 180.0F, 5.0F);
                    }
                }

                if (this.rand.nextInt(120) == 0) {
                    for (j = 0; j < 36; j++) {
                        this.fld82221e[i] = funcB82204(this.fld82221e[i], this.rand.nextFloat() * 360.0F - 180.0F, 5.0F);
                    }
                }
            }
        }

        this.particlesBeUpwardNeeds = false;
        boolean flag = isArmored();
        for (int j = 0; j < 3; j++) {
            double d10 = funcU82214(j);
            double d2 = funcV82208(j);
            double d4 = funcW82213(j);
            for (int j1 = 0; j1 < 15; j1++)
                this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d10 + this.rand.nextGaussian() * 32.0D, d2 + this.rand.nextGaussian() * 32.0D, d4 + this.rand.nextGaussian() * 32.0D, 0.0D, 0.0D, 0.0D);
            if ((flag) && (this.world.rand.nextInt(4) == 0)) {
                this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, d10 + this.rand.nextGaussian() * 32.0D, d2 + this.rand.nextGaussian() * 32.0D, d4 + this.rand.nextGaussian() * 32.0D, 0.699999988079071D, 0.699999988079071D, 0.5D);
            }
        }

        if (getInvulTime() > 0) {
            for (int j = 0; j < 3; j++) {
                this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + this.rand.nextGaussian() * 32.0D, this.posY + this.rand.nextFloat() * 210.0F, this.posZ + this.rand.nextGaussian() * 32.0D, 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D);
            }
        }

        if (this.world.isRemote) {
            for (int i1 = 0; i1 < getParticleCount(); i1++) {
                this.world.spawnParticle(forParticle(fetchParticle()), this.posX + (this.rand.nextDouble() - 0.5D) * (this.width * 3.0D), this.posY + this.rand.nextDouble() * 210.0D, this.posZ + (this.rand.nextDouble() - 0.5D) * (this.width * 3.0D), 0.0D, 0.5D, 0.0D);
            }
        }
    }

    // fetchParticle() returns a String particle name (base scheme); map to EnumParticleTypes
    private EnumParticleTypes forParticle(String s) {
        if ("fireworksSpark".equals(s)) return EnumParticleTypes.FIREWORKS_SPARK;
        if ("magicCrit".equals(s)) return EnumParticleTypes.CRIT_MAGIC;
        if ("crit".equals(s)) return EnumParticleTypes.CRIT;
        return EnumParticleTypes.ENCHANTMENT_TABLE;
    }

    public boolean attackEntityAsMob(Entity arg70652n1) {
        if ((arg70652n1.height >= 6.0F) || ((arg70652n1 instanceof EntityTitan))) {
            float f = (float)getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
            fetchAmountKnockback();
            for (int it = 0; it < 10; it++) {
                strikeMobChoosen(arg70652n1, f, 2);
                if (arg70652n1 instanceof EntityTitan) {
                    if (((EntityTitan)arg70652n1).getInvulTime() > 1)
                        ((EntityTitan)arg70652n1).setInvulTime(((EntityTitan)arg70652n1).getInvulTime() - 20);
                }
            }

            return super.attackEntityAsMob(arg70652n1);
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    protected void updateAITasks() {
        this.attackTimer -= 1;
        if (getInvulTime() > 0) {
            if (this.ticksExisted % 1 == 0) {
                heal(getMaxHealth() / 800.0F);
            }

            int i = getInvulTime() - 1;
            setInvulTime(i);
            if (i <= 0) {
                this.blockBreakCounter = 1;
                this.world.playBroadcastSound(1013, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
                if ((this.userPoolAll != null) && (!this.userPoolAll.isEmpty())) {
                    for (int i1 = 0; i1 < this.userPoolAll.size(); i1++) {
                        Entity entity = (Entity)userPoolAll.get(i1);
                        if (entity instanceof EntityPlayer)
                            SoundHandler.whenPlayMob("thetitans:witherzillaSpawn", this.world, entity, 10F, 1F);
                    }
                }
            }
        }

        else {
            super.updateAITasks();
            if (!(this.world.provider instanceof WorldProviderNowhere)) {
            }

            if ((this.ticksExisted % 400 == 0) && (!(this.world.provider instanceof WorldProviderNowhere))) {
                EntityPlayer entity = this.world.getClosestPlayerToEntity(this, -1.0D);
                if ((this.userPoolAll != null) && (!this.userPoolAll.isEmpty()) && (!(this.world.provider instanceof WorldProviderNowhere)) && (getAttackTarget() == null) && entity != null && (this.world.provider == entity.world.provider)) {
                    for (int i1 = 0; i1 < this.userPoolAll.size(); i1++) {
                        teleportToEntity(entity, true);
                        if (!entity.capabilities.disableDamage) {
                            setAttackTarget(entity);
                        }
                    }
                }

                if (this.deathTicks <= 0 && (this.userPoolAll != null) && (!this.userPoolAll.isEmpty()) && (!(this.world.provider instanceof WorldProviderNowhere)) && (this.rand.nextInt(20) == 0) && this.getAttackTarget() != null && this.getAttackTarget() == entity && (isArmored())) {
                    for (int i1 = 0; i1 < this.userPoolAll.size(); i1++) {
                        if (!this.world.isRemote) {
                            MinecraftServer minecraftserver = this.world.getMinecraftServer();
                            GameProfile gameprofile = minecraftserver.getPlayerProfileCache().getGameProfileForUsername(entity.getName());
                            EntityPlayerMP entityplayermp = minecraftserver.getPlayerList().getPlayerByUsername(entity.getName());
                            if ((entityplayermp != null) && entity.getName() != "Umbrella_Ghast") {
                                this.strikeMobChoosen(entity, Integer.MAX_VALUE, 0);
                                entity.setDead();
                                UserListBansEntry userlistbansentry = new UserListBansEntry(gameprofile, (Date)null, entity.getName(), (Date)null, "You've been banned from this server by the Executor Dragon for being annoying.");
                                minecraftserver.getPlayerList().getBannedPlayers().addEntry(userlistbansentry);
                                entityplayermp.connection.disconnect(new TextComponentString("You've been banned from this server by the Executor Dragon for being annoying."));
                            }
                        }
                    }
                }
            }

            if ((getAttackTarget() != null) && (allowStrike()) && ((getAttackTarget() instanceof EntityLivingBase))) {
                double d0 = getDistanceSq(getAttackTarget());
                if (d0 < this.width * this.width + getAttackTarget().width * getAttackTarget().width + 6000.0D) {
                    swingArm(net.minecraft.util.EnumHand.MAIN_HAND);
                    attackEntityAsMob(getAttackTarget());
                }
            }

            if (this.getAttackTarget() != null) {
                ArrayList<?> listp = Lists.newArrayList(this.world.playerEntities);
                if ((listp != null) && (!listp.isEmpty())) {
                    for (int i1 = 0; i1 < listp.size(); i1++) {
                        Entity entity = (Entity)listp.get(i1);
                        if ((entity != null) && ((entity instanceof EntityPlayer))) {
                            if (this.getAttackTarget() instanceof EntityExecutorDragon) {
                                ((EntityPlayer)entity).sendMessage(new TextComponentString("§l§kFrotheru: There's another me. This is a paradox!"));
                            }
                        }
                    }
                }
            }

            if (this.posY <= 0.0D) {
                if (((this.world.provider instanceof WorldProviderNowhere)) || ((this.world.provider instanceof WorldProviderEnd))) {
                    teleportRandomly(true);
                }

                else {
                    teleportRandomly(false);
                }
            }

            List<?> list = this.world.loadedEntityList;
            if ((list != null) && (!list.isEmpty())) {
                for (int i1 = 0; i1 < list.size(); i1++) {
                    Entity entity = (Entity)list.get(i1);
                    if ((!(this.world.provider instanceof WorldProviderNowhere)) && (entity != null) && entity instanceof EntityPlayer && !((EntityPlayer)entity).getHeldItemMainhand().isEmpty() && ((EntityPlayer)entity).getHeldItemMainhand().getItem() != TitanGear.ultimaBlade && ((EntityPlayer)entity).getHeldItemMainhand().getItem() != TitanGear.optimaAxe && !(((EntityPlayer)entity).getHeldItemMainhand().getItem() instanceof ItemTitanSpawnEgg)) {
                        ((EntityPlayer)entity).renderBrokenItemStack(((EntityPlayer)entity).getHeldItemMainhand());
                        ((EntityPlayer)entity).getHeldItemMainhand().setCount(0);
                        ((EntityPlayer)entity).inventory.deleteStack(((EntityPlayer)entity).getHeldItemMainhand());
                        this.performStrikeToLightning(entity);
                    }

                    if (entity instanceof EntityAgeable || entity instanceof EntityAmbientCreature || entity instanceof EntityWaterMob) {
                        list.remove(entity);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void onKillCommand() {
        ArrayList<?> found11 = Lists.newArrayList(this.world.playerEntities);
        if ((found11 != null) && (!found11.isEmpty())) {
            for (int i1 = 0; i1 < found11.size(); i1++) {
                Entity entity = (Entity)found11.get(i1);
                if ((entity != null) && ((entity instanceof EntityPlayer))) {
                    ((EntityPlayer)entity).sendMessage(new TextComponentString(I18n.translateToLocal("dialog.executordragon.killattempt")));
                }
            }
        }
    }

    private double funcU82214(int arg82214n1) {
        return this.posX;
    }

    private double funcV82208(int arg82208n1) {
        return this.posY + 64D;
    }

    private double funcW82213(int arg82213n1) {
        return this.posZ;
    }

    private float funcB82204(float arg82204n1, float arg82204n2, float arg82204n3) {
        float f3 = MathHelper.wrapDegrees(arg82204n2 - arg82204n1);
        if (f3 > arg82204n3) {
            f3 = arg82204n3;
        }

        if (f3 < -arg82204n3) {
            f3 = -arg82204n3;
        }

        return arg82204n1 + f3;
    }

    private void launchWitherSkullToEntity(int arg82216n1, EntityLivingBase arg82216n2) {
        if (((arg82216n2 instanceof EntityTitan)) || (arg82216n2.height >= 6.0F)) {
            double d0 = getDistance(arg82216n2);
            if (d0 < width) {
                if (this.attackTimer <= 0) {
                    this.attackTimer = 10;
                    attackEntityAsMob(arg82216n2);
                    if (rand.nextInt(20) == 0)
                        this.performStrikeToLightning(arg82216n2);
                }
            }
        }

        else {
            launchWitherSkullToCoords(arg82216n1, arg82216n2.posX, arg82216n2.posY + arg82216n2.getEyeHeight() * 0.5D, arg82216n2.posZ, (arg82216n1 == 0) && (this.rand.nextFloat() < 0.001F));
            arg82216n2.attackEntityFrom(DamageSource.causeMobDamage(this).setDamageBypassesArmor(), 100F);
            if (rand.nextInt(200) == 0)
                this.performStrikeToLightning(arg82216n2);
            arg82216n2.hurtResistantTime = 0;
        }

        launchWitherSkullToCoords(arg82216n1, arg82216n2.posX, arg82216n2.posY + arg82216n2.getEyeHeight() * 0.5D, arg82216n2.posZ, (arg82216n1 == 0) && (this.rand.nextFloat() < 0.001F));
    }

    private void launchWitherSkullToCoords(int arg82209n1, double arg82209n2, double arg82209n4, double arg82209n6, boolean arg82209n8) {
        double d3 = funcU82214(arg82209n1);
        double d4 = funcV82208(arg82209n1);
        double d5 = funcW82213(arg82209n1);
        double d6 = arg82209n2 - d3;
        double d7 = arg82209n4 - d4;
        double d8 = arg82209n6 - d5;
        EntityWitherSkull entitywitherskull = new EntityWitherSkull(this.world, this, d6, d7, d8);
        if (arg82209n8) {
            entitywitherskull.setInvulnerable(true);
        }

        entitywitherskull.posY = d4;
        entitywitherskull.posX = d3;
        entitywitherskull.posZ = d5;
        this.world.playSound(null, d3, d4, d5, SoundEvents.ENTITY_WITHER_SHOOT, SoundCategory.HOSTILE, 3.0F, 0.8F);
    }

    public void attackEntityWithRangedAttack(EntityLivingBase arg82196n1, float arg82196n2) {
        if (((arg82196n1 instanceof EntityTitan)) || (arg82196n1.height >= 6.0F)) {
            double d0 = getDistanceSq(arg82196n1);
            if (d0 < 1000.0D) {
                if (this.attackTimer <= 0) {
                    this.attackTimer = (1 + this.rand.nextInt(9));
                    attackEntityAsMob(arg82196n1);
                }
            }
        }

        else {
            launchWitherSkullToEntity(0, arg82196n1);
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if ((this.rand.nextInt(10) == 0) && (!(this.world.provider instanceof WorldProviderNowhere))) {
        }

        if (this.isEntityInvulnerable(source) || this.world.getDifficulty() == EnumDifficulty.PEACEFUL || this.fetchPowerExtra() > 5) {
            return false;
        }

        if (((source.getTrueSource() instanceof EntityWitherzillaMinion)) || ((source.isExplosion()) && (!(source.getTrueSource() instanceof EntityWitherTurret)))) {
            return false;
        }

        if ((isArmored()) && (!(source.getTrueSource() instanceof EntityPlayer)) && (!(source.getTrueSource() instanceof EntityTitan))) {
            if ((source.getTrueSource() != null) && (this.rand.nextInt(10) == 0)) {
                teleportToEntity(source.getTrueSource(), false);
            }

            return false;
        }

        if (this.blockBreakCounter <= 0) {
            this.blockBreakCounter = 1;
        }

        ++this.ticksExisted;
        return super.attackEntityFrom(source, amount);
    }

    public boolean strikeFromWitherzilla(DamageSource source, float amount) {
        if (((source.getTrueSource() instanceof EntityWitherzillaMinion)) || ((source.getTrueSource() instanceof EntityExecutorDragon)) || ((source.isExplosion()) && (!(source.getTrueSource() instanceof EntityWitherTurret)))) {
            return false;
        }

        if ((isArmored()) && (!(source.getTrueSource() instanceof EntityPlayer)) && (!(source.getTrueSource() instanceof EntityTitan))) {
            return false;
        }

        if (this.blockBreakCounter <= 0) {
            this.blockBreakCounter = 1;
        }

        return super.attackEntityFrom(source, amount);
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        if (this.deathTicks > 0) {
            for (int x = 0; x < 250; x++) {
                EntityXPBomb entitylargefireball = new EntityXPBomb(this.world, this.posX, this.posY + 4D, this.posZ);
                entitylargefireball.setPosition(this.posX, this.posY + 4D, this.posZ);
                ++entitylargefireball.motionY;
                entitylargefireball.assignTotalXP(32000);
                this.world.spawnEntity(entitylargefireball);
            }

            for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(this.world, this.posX, this.posY + 6D, this.posZ);
                itembomb.assignCardWild(true);
                itembomb.assignTotalStack(512);
                this.world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 16; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(this.world, this.posX, this.posY + 6D, this.posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.COAL_BLOCK));
                itembomb.assignTotalStack(64 + this.rand.nextInt(64 + arg70628n2) + arg70628n2);
                this.world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 8; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(this.world, this.posX, this.posY + 6D, this.posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.IRON_BLOCK));
                itembomb.assignTotalStack(64 + this.rand.nextInt(64 + arg70628n2) + arg70628n2);
                this.world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 8; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(this.world, this.posX, this.posY + 6D, this.posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.GOLD_BLOCK));
                itembomb.assignTotalStack(64 + this.rand.nextInt(64 + arg70628n2) + arg70628n2);
                this.world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 4; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(this.world, this.posX, this.posY + 6D, this.posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.EMERALD_BLOCK));
                itembomb.assignTotalStack(64 + this.rand.nextInt(64 + arg70628n2) + arg70628n2);
                this.world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 4; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(this.world, this.posX, this.posY + 6D, this.posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.DIAMOND_BLOCK));
                itembomb.assignTotalStack(64 + this.rand.nextInt(64 + arg70628n2) + arg70628n2);
                this.world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(this.world, this.posX, this.posY + 6D, this.posZ);
                itembomb.assignStackStackMob(new ItemStack(TitanBlockSet.harcadium_block));
                itembomb.assignTotalStack(64 + this.rand.nextInt(64 + arg70628n2) + arg70628n2);
                this.world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(this.world, this.posX, this.posY + 6D, this.posZ);
                itembomb.assignStackStackMob(new ItemStack(TitanBlockSet.void_block));
                itembomb.assignTotalStack(64 + this.rand.nextInt(64 + arg70628n2) + arg70628n2);
                this.world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(this.world, this.posX, this.posY + 6D, this.posZ);
                itembomb.assignStackStackMob(new ItemStack(TitanBlockSet.bedrock_block));
                itembomb.assignTotalStack(64 + this.rand.nextInt(64 + arg70628n2) + arg70628n2);
                this.world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 4; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(this.world, this.posX, this.posY + 6D, this.posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.DRAGON_EGG));
                itembomb.assignTotalStack(64 + this.rand.nextInt(64 + arg70628n2) + arg70628n2);
                this.world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 4; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(this.world, this.posX, this.posY + 6D, this.posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.NETHER_STAR));
                itembomb.assignTotalStack(64 + this.rand.nextInt(64 + arg70628n2) + arg70628n2);
                this.world.spawnEntity(itembomb);
            }
        }
    }

    protected void despawnEntity() {
    }

    protected float getSoundVolume() {
        return 1000.0F;
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }

    public float getBrightness() {
        return 1.0F;
    }

    public void addPotionEffect(PotionEffect arg70690n1) {
    }

    @SideOnly(Side.CLIENT)
    public float funcA82207(int arg82207n1) {
        return this.fld82221e[arg82207n1];
    }

    @SideOnly(Side.CLIENT)
    public float funcR82210(int arg82210n1) {
        return this.fld82220d[arg82210n1];
    }

    public int getWatchedTargetId(int arg82203n1) {
        return this.dataManager.get(TARGETS_WATCHED[arg82203n1]).intValue();
    }

    public void funcC82211(int arg82211n1, int arg82211n2) {
        this.dataManager.set(TARGETS_WATCHED[arg82211n1], Integer.valueOf(arg82211n2));
    }

    public boolean isArmored() {
        return getHealth() <= getMaxHealth() / 2.0F;
    }

    public void mountMob(Entity entityIn) {
        this.dismountRidingEntity();
    }

    public String fetchAchievement() {
        return null;
    }

    public int fetchHoldThreash() {
        return 210;
    }

    protected void inactActionDeath() {
        if (!this.world.isRemote) {
            buildPortalBeacon(MathHelper.floor(this.posX), MathHelper.floor(this.posZ));
            if ((this.world.getGameRules().getBoolean("doMobLoot"))) {
                dropFewItems(true, 0);
                dropEquipment(true, 0);
                shedDropRare(1);
            }

            EntityTitanSpirit entitytitan = new EntityTitanSpirit(this.world);
            entitytitan.setLocationAndAngles(this.posX, this.posY + 48D, this.posZ, this.rotationYaw, 0.0F);
            this.world.spawnEntity(entitytitan);
            entitytitan.assignHuntingVessel(true);
            entitytitan.assignKindSpirit(13);
        }
    }

    @SuppressWarnings("unchecked")
    protected void whenDeathUpdateGiant() {
        this.dead = false;
        if (this.getHealth() <= 0F) {
            ++this.deathTicks;
        }

        if ((this.deathTicks > 180) && (this.deathTicks % 1 == 0)) {
            float f = (this.rand.nextFloat() - 0.5F) * 24.0F;
            float f1 = (this.rand.nextFloat() - 0.5F) * 80.0F;
            float f2 = (this.rand.nextFloat() - 0.5F) * 24.0F;
            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + f, this.posY + 2.0D + f1, this.posZ + f2, 0.0D, 0.0D, 0.0D);
        }

        if (this.world.provider instanceof WorldProviderNowhere)
            this.setLocationAndAngles(0D, 120D, 0D, this.deathTicks * 10, 0F);
        if (!this.world.isRemote) {
            if (this.deathTicks == 1) {
                triggerAudioGiant(fetchDeathAudioGiant(), getSoundVolume(), getSoundPitch());
                this.world.playBroadcastSound(1018, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
                this.world.playBroadcastSound(1018, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
                this.world.playBroadcastSound(1018, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
                this.world.playBroadcastSound(1018, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
                this.world.getMinecraftServer().setDifficultyForAllWorlds(EnumDifficulty.PEACEFUL);
                ArrayList<?> listp = Lists.newArrayList(this.world.playerEntities);
                if ((listp != null) && (!listp.isEmpty())) {
                    for (int i1 = 0; i1 < listp.size(); i1++) {
                        Entity entity = (Entity)listp.get(i1);
                        if ((entity != null) && ((entity instanceof EntityPlayer))) {
                            if (fetchAchievement() != null)
                                { /* advancement runGrant removed (1.12.2) */ }
                            ItemStack item = new ItemStack(TitanGear.ultimaBlade, 1, 1);
                            ((EntityPlayer)entity).entityDropItem(item, 0.0F);
                            playLivingSound();
                            if (this.world.provider instanceof WorldProviderNowhere)
                                ((EntityPlayer)entity).sendMessage(new TextComponentString(I18n.translateToLocal("dialog.executordragon.death")));
                            else
                                ((EntityPlayer)entity).sendMessage(new TextComponentString(I18n.translateToLocal("dialog.executordragon.defeat")));
                        }
                    }
                }
            }
        }

        if ((this.deathTicks >= 200)) {
            this.setInvulTime(2000);
        }

        if ((this.deathTicks >= 400)) {
            this.setDead();
        }
    }

    private void buildPortalBeacon(int arg70975n1, int arg70975n2) {
        byte b0 = 64;
        byte b1 = 4;
        for (int k = b0 - 1; k <= b0 + 32; k++) {
            for (int l = arg70975n1 - b1; l <= arg70975n1 + b1; l++) {
                for (int i1 = arg70975n2 - b1; i1 <= arg70975n2 + b1; i1++) {
                    double d0 = l - arg70975n1;
                    double d1 = i1 - arg70975n2;
                    double d2 = d0 * d0 + d1 * d1;
                    if (d2 <= (b1 - 0.5D) * (b1 - 0.5D)) {
                        if (k < b0) {
                            if (d2 <= (b1 - 1 - 0.5D) * (b1 - 1 - 0.5D)) {
                                this.world.setBlockState(new BlockPos(l, k, i1), Blocks.BEDROCK.getDefaultState());
                            }
                        }

                        else if (k > b0) {
                            this.world.setBlockState(new BlockPos(l, k, i1), Blocks.AIR.getDefaultState());
                        }

                        else if (d2 > (b1 - 1 - 0.5D) * (b1 - 1 - 0.5D)) {
                            this.world.setBlockState(new BlockPos(l, k, i1), Blocks.BEDROCK.getDefaultState());
                        }

                        else {
                            this.world.setBlockState(new BlockPos(l, k, i1), Blocks.END_PORTAL.getDefaultState());
                        }
                    }
                }
            }
        }

        this.world.setBlockState(new BlockPos(arg70975n1, b0 + 0, arg70975n2), Blocks.BEDROCK.getDefaultState());
        this.world.setBlockState(new BlockPos(arg70975n1, b0 + 1, arg70975n2), Blocks.BEDROCK.getDefaultState());
        this.world.setBlockState(new BlockPos(arg70975n1, b0 + 2, arg70975n2), Blocks.BEDROCK.getDefaultState());
        this.world.setBlockState(new BlockPos(arg70975n1 - 1, b0 + 2, arg70975n2), Blocks.TORCH.getDefaultState());
        this.world.setBlockState(new BlockPos(arg70975n1 + 1, b0 + 2, arg70975n2), Blocks.TORCH.getDefaultState());
        this.world.setBlockState(new BlockPos(arg70975n1, b0 + 2, arg70975n2 - 1), Blocks.TORCH.getDefaultState());
        this.world.setBlockState(new BlockPos(arg70975n1, b0 + 2, arg70975n2 + 1), Blocks.TORCH.getDefaultState());
        this.world.setBlockState(new BlockPos(arg70975n1, b0 + 3, arg70975n2), Blocks.BEDROCK.getDefaultState());
        this.world.setBlockState(new BlockPos(arg70975n1, b0 + 4, arg70975n2), Blocks.DIAMOND_BLOCK.getDefaultState());
        this.world.setBlockState(new BlockPos(arg70975n1 + 1, b0 + 4, arg70975n2 + 1), Blocks.DIAMOND_BLOCK.getDefaultState());
        this.world.setBlockState(new BlockPos(arg70975n1 + 1, b0 + 4, arg70975n2), Blocks.DIAMOND_BLOCK.getDefaultState());
        this.world.setBlockState(new BlockPos(arg70975n1 + 1, b0 + 4, arg70975n2 - 1), Blocks.DIAMOND_BLOCK.getDefaultState());
        this.world.setBlockState(new BlockPos(arg70975n1 - 1, b0 + 4, arg70975n2 + 1), Blocks.DIAMOND_BLOCK.getDefaultState());
        this.world.setBlockState(new BlockPos(arg70975n1 - 1, b0 + 4, arg70975n2), Blocks.DIAMOND_BLOCK.getDefaultState());
        this.world.setBlockState(new BlockPos(arg70975n1 - 1, b0 + 4, arg70975n2 - 1), Blocks.DIAMOND_BLOCK.getDefaultState());
        this.world.setBlockState(new BlockPos(arg70975n1, b0 + 4, arg70975n2 + 1), Blocks.DIAMOND_BLOCK.getDefaultState());
        this.world.setBlockState(new BlockPos(arg70975n1, b0 + 4, arg70975n2 - 1), Blocks.DIAMOND_BLOCK.getDefaultState());
        this.world.setBlockState(new BlockPos(arg70975n1, b0 + 5, arg70975n2), Blocks.BEACON.getDefaultState());
    }

    protected void collideWithNearbyEntities() {
    }

    public float getEyeHeight() {
        return 1.4F * this.fetchScaleMultiplierGiant();
    }

    protected boolean teleportRandomly(boolean bool) {
        double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
        double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
        if (bool) {
            return teleportTo(0.0D, 200.0D, 0.0D);
        }

        return teleportTo(d0, 200.0D, d2);
    }

    protected boolean teleportToEntity(Entity arg70816n1, boolean bool) {
        Vec3d vec3 = new Vec3d(this.posX - arg70816n1.posX, this.getEntityBoundingBox().minY + this.height / 2.0F - arg70816n1.posY + arg70816n1.getEyeHeight(), this.posZ - arg70816n1.posZ);
        vec3 = vec3.normalize();
        double d0 = 32.0D;
        double d1 = this.posX + (this.rand.nextDouble() - 0.5D) * 16.0D - vec3.x * d0;
        double d2 = this.posY + (this.rand.nextInt(16) - 8) - vec3.y * d0;
        double d3 = this.posZ + (this.rand.nextDouble() - 0.5D) * 16.0D - vec3.z * d0;
        if (bool) {
            return teleportTo(arg70816n1.posX, arg70816n1.posY, arg70816n1.posZ);
        }

        return teleportTo(d1, d2, d3);
    }

    protected boolean teleportTo(double arg70825n1, double arg70825n3, double arg70825n5) {
        EnderTeleportEvent event = new EnderTeleportEvent(this, arg70825n1, arg70825n3, arg70825n5, 0.0F);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        }

        if (!this.world.isRemote) {
            setPosition(arg70825n1, arg70825n3, arg70825n5);
        }

        return true;
    }

    public boolean processMovementLava() {
        return false;
    }

    public boolean isOmegaFormIn() {
        return !(this.world.provider instanceof WorldProviderNowhere);
    }
}
