package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.core.TheCore;
import com.electrifiedded.thetitans.entity.minion.EntityWitherMinion;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;
import com.electrifiedded.thetitans.item.spawn.ItemTitanSpawnEgg;
import com.electrifiedded.thetitans.registry.TitanBlockSet;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.sound.SoundHandler;
import com.electrifiedded.thetitans.world.dimension.WorldProviderVoid;

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
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
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
import com.mojang.authlib.GameProfile;

import java.util.Date;
import java.util.List;
import javax.annotation.Nullable;

public final class EntityWitherzilla extends EntityTitan implements IRangedAttackMob {
    private static final DataParameter<Integer> VICTIM_0_WATCHED = EntityDataManager.<Integer>createKey(EntityWitherzilla.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> VICTIM_1_WATCHED = EntityDataManager.<Integer>createKey(EntityWitherzilla.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> VICTIM_2_WATCHED = EntityDataManager.<Integer>createKey(EntityWitherzilla.class, DataSerializers.VARINT);
    private static final DataParameter<Integer>[] TARGETS_WATCHED = new DataParameter[] { VICTIM_0_WATCHED, VICTIM_1_WATCHED, VICTIM_2_WATCHED };
    private float[] fld82220d = new float[2];
    private float[] fld82221e = new float[2];
    private float[] fld82217f = new float[2];
    private float[] fld82218g = new float[2];
    private int[] fld82223h = new int[2];
    private int[] fld82224i = new int[2];
    private int blockBreakCounter;
    public int beatsAffect;
    private int attackTimer;
    List<?> userPoolAll = world.playerEntities;

    private static final Predicate<Entity> mobSelectorStrike = new Predicate<Entity>() {
        public boolean apply(@Nullable Entity arg180027n1) {
            return (arg180027n1 != null) && (!(arg180027n1 instanceof EntityTitanSpirit)) && (!(arg180027n1 instanceof EntityWitherzillaMinion)) && (!(arg180027n1 instanceof EntityWitherTurret)) && (!(arg180027n1 instanceof EntityWitherTurretGround)) && (!(arg180027n1 instanceof EntityWitherTurretMortar));
        }
    };

    public EntityWitherzilla(World worldIn) {
        super(worldIn);
        noClip = true;
        experienceValue = 5000000;
        triggerAudioGiant("thetitans:witherzillaSpawn", Float.MAX_VALUE, 1F);
    }

    protected void applyEntityAI() {
        tasks.addTask(0, new EntityAISwimming(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, mobSelectorStrike));
    }

    @Override
    public double fetchAmountHp() {
        return super.fetchAmountHp() * 300.0F;
    }

    public float fetchScaleMultiplierGiant() {
        return isOmegaFormIn() ? super.fetchScaleMultiplierGiant() * 2.0F : super.fetchScaleMultiplierGiant();
    }

    public boolean alreadyALabelHas() {
        return true;
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public void fall(float arg70069n1) {
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

    protected String fetchLivingAudioGiant() {
        return "thetitans:witherzillaLiving";
    }

    protected String fetchHurtAudioGiant() {
        return "thetitans:witherzillaGrunt";
    }

    protected String fetchDeathAudioGiant() {
        return "thetitans:witherzillaDeath";
    }

    public boolean getCanSpawnHere() {
        return false;
    }

    public boolean allowHurtByUserBe() {
        return (world.getDifficulty() != EnumDifficulty.PEACEFUL) && (!isEntityInvulnerable(DamageSource.GENERIC));
    }

    protected void kill() {
        world.playBroadcastSound(1013, new BlockPos((int)posX, (int)posY, (int)posZ), 0);
        if (((world.provider instanceof WorldProviderVoid)) || ((world.provider instanceof WorldProviderEnd)))
            teleportRandomly(true);
        else
            teleportRandomly(false);
    }

    public void performStrikeToLightning(Entity entity) {
        if (entity != null && entity.isEntityAlive() && !(entity instanceof EntityTitanPart)) {
            if (entity instanceof EntityPlayer) {
                if (rand.nextInt(1000) == 0) {
                    strikeMobChoosen(entity, ((EntityPlayer)entity).getHealth() / 2, 5);
                    entity.setLocationAndAngles(entity.posX, posY + getEyeHeight(), entity.posZ, rotationYawHead + 180, 0F);
                }

                ((EntityPlayer)entity).sendMessage(new TextComponentString("§k" + rand.nextInt(1234567890)));
                if (((EntityPlayer)entity).inventory.hasItemStack(new ItemStack(TitanGear.optimaAxe))) {
                    ((EntityPlayer)entity).motionX = 0D;
                    ((EntityPlayer)entity).motionY = 0D;
                    ((EntityPlayer)entity).motionZ = 0D;
                    return;
                }
            }

            world.addWeatherEffect(new EntityUrLightning(world, entity.posX, entity.posY, entity.posZ));
            if ((entity != null) && entity instanceof EntityLivingBase && ((beatsAffect >= 600) || (entity.height >= 6.0F)) && (!(entity instanceof EntityTitan))) {
                ((EntityLivingBase)entity).setHealth(0.0F);
                strikeMobChoosen(entity, 2.14748365E9F, 0);
            }

            if (entity instanceof EntityTitan && entity instanceof ITitanHitbox) {
                Entity parts[] = ((ITitanHitbox)entity).getArray();
                if (parts != null) {
                    for (int j = 0; j < parts.length; j++) {
                        world.addWeatherEffect(new EntityUrLightning(world, parts[j].posX, parts[j].posY, parts[j].posZ));
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
        if (((world.provider instanceof WorldProviderVoid)) && (arg70624n1 != null) && (posX == 0.0D) && (posY == 200.0D) && (posZ == 0.0D)) {
            arg70624n1 = null;
        }

        else {
            super.setAttackTarget(arg70624n1);
        }
    }

    public void onLivingUpdate() {
        noClip = true;
        if (world.getWorldTime() != 18000)
            world.setWorldTime(18000);
        if ((ticksExisted % 3 + rand.nextInt(3) == 0) || getInvulTime() >= 1000)
            setCustomNameTag("§kRegnator");
        else
            setCustomNameTag("§l" + I18n.translateToLocal("entity.WitherBossTitan.name"));
        if (getHealth() <= 0.0F && fetchPowerExtra() > 5)
            setHealth(getMaxHealth());
        if (getAttackTarget() != null && !(world.provider instanceof WorldProviderVoid) && ticksExisted % 100 == 0)
            performStrikeToLightning(getAttackTarget());
        if (isServer() && !(world.provider instanceof WorldProviderVoid)) {
            WorldServer worldserver = world.getMinecraftServer().getWorld(0);
            WorldInfo worldinfo = worldserver.getWorldInfo();
            if (getAttackTarget() != null && getAttackTarget() instanceof EntityEnderColossus && worldinfo.isRaining()) {
                worldinfo.setRainTime(0);
                worldinfo.setThunderTime(0);
                worldinfo.setRaining(false);
                worldinfo.setThundering(false);
            }

            else if (!worldinfo.isRaining()) {
                worldinfo.setRainTime(9999999);
                worldinfo.setThunderTime(1000000);
                worldinfo.setRaining(true);
                worldinfo.setThundering(true);
            }
        }

        if (!(world.provider instanceof WorldProviderVoid) && rand.nextInt(5) == 0) {
            for (int l = 0; l < 10; l++) {
                int i = MathHelper.floor(posX);
                int j = MathHelper.floor(posY);
                int k = MathHelper.floor(posZ);
                int i1 = i + MathHelper.getInt(rand, 10, 100) * MathHelper.getInt(rand, -1, 1);
                int j1 = j + MathHelper.getInt(rand, 10, 100) * MathHelper.getInt(rand, -1, 1);
                int k1 = k + MathHelper.getInt(rand, 10, 100) * MathHelper.getInt(rand, -1, 1);
                // PERF — cheap rolls first; the old order built a lightning entity 20x every other tick and could
                // load/generate chunks up to 100 blocks away through isSideSolid.
                BlockPos below = new BlockPos(i1, j1 - 1, k1);
                if (rand.nextInt(5) != 0 || !world.isBlockLoaded(below) || !world.isSideSolid(below, net.minecraft.util.EnumFacing.UP))
                    continue;
                EntityUrLightning entitylightning = new EntityUrLightning(world, i1, j1, k1);
                if ((world.checkNoEntityCollision(entitylightning.getEntityBoundingBox())) && (world.getCollisionBoxes(entitylightning, entitylightning.getEntityBoundingBox()).isEmpty()) && (!world.containsAnyLiquid(entitylightning.getEntityBoundingBox())))
                    world.addWeatherEffect(entitylightning);
            }
        }

        else {
            if (getDistanceSq(0D, 200D, 0D) > 50000D)
                setPosition(0D, 200D, 0D);
            List<?> listp = world.loadedEntityList;
            if (isServer() && listp != null && !listp.isEmpty() && rand.nextInt(4) == 0)
                for (int i1 = 0; i1 < listp.size(); i1++) {
                Entity entity = (Entity)listp.get(i1);
                if (entity != null && entity.isEntityAlive())
                    if (entity instanceof EntityWitherTurret && !((EntityWitherTurret)entity).isPlayerCreated()) {
                    setPosition(0D, 200D, 0D);
                    renderYawOffset = rotationYaw = rotationYawHead = 0F;
                    setAttackTarget(null);
                }

                else if (entity instanceof EntityWitherTurretGround && !((EntityWitherTurretGround)entity).isPlayerCreated()) {
                    setPosition(0D, 200D, 0D);
                    renderYawOffset = rotationYaw = rotationYawHead = 0F;
                    setAttackTarget(null);
                }

                else if (entity instanceof EntityWitherTurretMortar && !((EntityWitherTurretMortar)entity).isPlayerCreated()) {
                    setPosition(0D, 200D, 0D);
                    renderYawOffset = rotationYaw = rotationYawHead = 0F;
                    setAttackTarget(null);
                }
            }
        }

        if (rand.nextInt(1000) == 0) {
            List<?> listp = world.playerEntities;
            if (listp != null && !listp.isEmpty())
                for (int i1 = 0; i1 < listp.size(); i1++) {
                EntityPlayer player = (EntityPlayer)listp.get(i1);
                if (player != null) {
                    playLivingSound();
                    if (!(world.provider instanceof WorldProviderVoid)) {
                        player.attackEntityFrom((new DamageSource("generic")).setDamageAllowedInCreativeMode().setDamageBypassesArmor().setDamageIsAbsolute(), 10.0F);
                        player.sendMessage(new TextComponentString(I18n.translateToLocal("dialog.witherzilla.taunt." + rand.nextInt(6))));
                    }

                    else
                        player.sendMessage(new TextComponentString(I18n.translateToLocal("dialog.witherzilla.plead." + rand.nextInt(6))));
                }
            }
        }

        List<?> list = world.loadedEntityList;
        if (!(world.provider instanceof WorldProviderVoid) && rand.nextInt(4) == 0 && list != null && !list.isEmpty())
            for (int i1 = 0; i1 < list.size(); i1++) {
            Entity entity = (Entity)list.get(i1);
            if (getAttackTarget() != null && entity != null && entity.isEntityAlive())
                if (entity instanceof EntityWitherzillaMinion)
                ((EntityWitherzillaMinion)entity).setAttackTarget(getAttackTarget());
            else if (getInvulTime() > 1 && !(entity instanceof EntityWitherSkull) && !(entity instanceof EntityWitherTurret) && !(entity instanceof EntityWitherTurretGround) && !(entity instanceof EntityWitherTurretMortar) && !(entity instanceof EntityWeatherEffect) && !(entity instanceof EntityWitherzillaMinion) && !(entity instanceof EntityTitan) && !(entity instanceof EntityTitanSpirit) && !(entity instanceof EntityPlayer))
                if (entity instanceof EntityLiving) {
                entity.motionY = 0.05D - motionY * 0.2D;
                ((EntityLiving)entity).getNavigator().clearPath();
                ((EntityLiving)entity).hurtResistantTime = (int) rand.nextGaussian() * 20;
                ((EntityLiving)entity).moveForward = (float)rand.nextGaussian();
                ((EntityLiving)entity).moveStrafing = (float)rand.nextGaussian();
                ((EntityLiving)entity).renderYawOffset = ((EntityLiving)entity).rotationYaw = ((EntityLiving)entity).rotationYawHead += (float) rand.nextGaussian() * 10F;
            }

            else {
                entity.motionX = rand.nextGaussian() * 0.5D;
                entity.motionY = rand.nextGaussian() * 0.5D;
                entity.motionZ = rand.nextGaussian() * 0.5D;
                entity.rotationYaw += 10F;
                entity.hurtResistantTime = (int) rand.nextGaussian() * 20;
            }
        }

        beatsAffect += 1;
        if (beatsAffect >= 1010)
            beatsAffect = 0;
        if (isServer() && rand.nextInt(120) == 0 && (getAttackTarget() != null))
            if (TheTitans.isDifficulty(world, TheCore.NIGHTMARE_V))
            world.newExplosion(this, getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 14.0F, true, true);
        else
            world.newExplosion(this, getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 7.0F, false, world.getGameRules().getBoolean("mobGriefing"));
        if (!isArmored())
            motionY *= 0.1D;
        else
            motionY *= 0.9D;
        if (isServer() && getWatchedTargetId(0) > 0) {
            Entity entity = world.getEntityByID(getWatchedTargetId(0));
            if (entity != null) {
                double d0 = entity.posX - posX;
                double d1 = (entity.posY + (isArmored() ? 2D : 12D)) - posY;
                double d2 = entity.posZ - posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                getLookHelper().setLookPositionWithEntity(entity, 180.0F, 40.0F);
                if (((entity instanceof EntityLivingBase)) && (d3 < 10000.0D))
                    attackEntityWithRangedAttack((EntityLivingBase)entity, 0.0F);
                if (d3 > 64.0D) {
                    double d5 = MathHelper.sqrt(d3);
                    motionX += d0 / d5 * 1.5D - motionX;
                    motionY += d1 / d5 * 1.5D - motionY;
                    motionZ += d2 / d5 * 1.5D - motionZ;
                    renderYawOffset = (rotationYaw = (float)Math.atan2(motionZ, motionX) * 57.295776F - 90.0F);
                }
            }
        }

        for (int i = 0; i < 2; i++) {
            fld82218g[i] = fld82221e[i];
            fld82217f[i] = fld82220d[i];
        }

        for (int i = 0; i < 2; i++) {
            int j = getWatchedTargetId(i + 1);
            Entity mobRef1 = null;
            if (j > 0)
                mobRef1 = world.getEntityByID(j);
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
                fld82220d[i] = funcB82204(fld82220d[i], f1, 5.0F);
                fld82221e[i] = funcB82204(fld82221e[i], f, 5.0F);
            }

            else {
                if (rand.nextInt(120) == 0)
                    for (j = 0; j < 36; j++)
                    fld82220d[i] = funcB82204(fld82220d[i], rand.nextFloat() * 360.0F - 180.0F, 5.0F);
                if (rand.nextInt(120) == 0)
                    for (j = 0; j < 36; j++)
                    fld82221e[i] = funcB82204(fld82221e[i], rand.nextFloat() * 360.0F - 180.0F, 5.0F);
            }
        }

        particlesBeUpwardNeeds = false;
        if (ticksExisted % 5 == 0) {
            for (int j = 0; j < 3; j++) {
                double d10 = funcU82214(j);
                double d2 = funcV82208(j);
                double d4 = funcW82213(j);
                for (int j1 = 0; j1 < 15; j1++)
                    world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d10 + rand.nextGaussian() * 32.0D, d2 + rand.nextGaussian() * 32.0D, d4 + rand.nextGaussian() * 32.0D, 0.0D, 0.0D, 0.0D);
                if (isArmored() && world.rand.nextInt(4) == 0)
                    world.spawnParticle(EnumParticleTypes.SPELL_MOB, d10 + rand.nextGaussian() * 32.0D, d2 + rand.nextGaussian() * 32.0D, d4 + rand.nextGaussian() * 32.0D, 0.699999988079071D, 0.699999988079071D, 0.5D);
            }

            if (isClient() && getInvulTime() > 0)
                for (int j = 0; j < 3; j++)
                world.spawnParticle(EnumParticleTypes.SPELL_MOB, posX + rand.nextGaussian() * 32.0D, posY + rand.nextFloat() * 210.0F, posZ + rand.nextGaussian() * 32.0D, 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D);
            if (isClient())
                for (int i1 = 0; i1 < getParticleCount(); i1++)
                world.spawnParticle(forParticle(fetchParticle()), posX + (rand.nextDouble() - 0.5D) * (width * 3.0D), posY + rand.nextDouble() * 210.0D, posZ + (rand.nextDouble() - 0.5D) * (width * 3.0D), 0.0D, 0.5D, 0.0D);
        }

        super.onLivingUpdate();
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

    protected void whenUpdateBounds() {
        setSize(0.9F, 2.844F);
    }

    public boolean attackEntityAsMob(Entity arg70652n1) {
        if ((arg70652n1.height >= 6.0F) || ((arg70652n1 instanceof EntityTitan))) {
            float f = (float)fetchAmountStrike(1.0F);
            fetchAmountKnockback();
            for (int it = 0; it < 10; it++) {
                strikeMobChoosen(arg70652n1, f, 2);
                if (arg70652n1 instanceof EntityTitan)
                    if (((EntityTitan)arg70652n1).getInvulTime() > 1)
                    ((EntityTitan)arg70652n1).setInvulTime(((EntityTitan)arg70652n1).getInvulTime() - 20);
            }

            return super.attackEntityAsMob(arg70652n1);
        }

        return false;
    }

    protected void updateAITasks() {
        attackTimer -= 1;
        if (getInvulTime() > 0) {
            if (ticksExisted % 1 == 0)
                heal(getMaxHealth() / 800.0F);
            int i = getInvulTime() - 1;
            setInvulTime(i);
            if (i <= 0) {
                blockBreakCounter = 1;
                world.playBroadcastSound(1013, new BlockPos((int)posX, (int)posY, (int)posZ), 0);
                if ((userPoolAll != null) && (!userPoolAll.isEmpty()))
                    for (int i1 = 0; i1 < userPoolAll.size(); i1++) {
                    Entity entity = (Entity)userPoolAll.get(i1);
                    if (entity instanceof EntityPlayer)
                        com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:witherzillaSpawn", world, entity, 10F, 1F);
                }
            }
        }

        else {
            if ((ticksExisted % 400 == 0) && (!(world.provider instanceof WorldProviderVoid))) {
                EntityPlayer entity = world.getClosestPlayerToEntity(this, -1.0D);
                if (userPoolAll != null && !userPoolAll.isEmpty() && !(world.provider instanceof WorldProviderVoid) && getAttackTarget() == null && entity != null && world.provider == entity.world.provider) {
                    for (int i1 = 0; i1 < userPoolAll.size(); i1++) {
                        teleportToEntity(entity, true);
                        if (!entity.capabilities.disableDamage)
                            setAttackTarget(entity);
                    }
                }

                if (isServer() && rand.nextInt(20) == 0 && (deathTicks <= 0 && (userPoolAll != null) && (!userPoolAll.isEmpty()) && (!(world.provider instanceof WorldProviderVoid)) && getAttackTarget() != null && getAttackTarget() == entity && isArmored())) {
                    for (int i1 = 0; i1 < userPoolAll.size(); i1++) {
                        MinecraftServer minecraftserver = world.getMinecraftServer();
                        GameProfile gameprofile = minecraftserver.getPlayerProfileCache().getGameProfileForUsername(entity.getName());
                        EntityPlayerMP entityplayermp = minecraftserver.getPlayerList().getPlayerByUsername(entity.getName());
                        if (entityplayermp != null && (entity.getUniqueID().toString() != "07d5aa7f-81d0-41e1-8981-04723d12c2ef" && entity.getUniqueID().toString() != "19d96ed2-6c4d-42bd-9855-498482daa5ab" && entity.getUniqueID().toString() != "39c0cf10-5f5d-4c89-8057-cee67479c7c2")) {
                            strikeMobChoosen(entity, Integer.MAX_VALUE, 0);
                            entity.setDead();
                            UserListBansEntry userlistbansentry = new UserListBansEntry(gameprofile, (Date)null, entity.getName(), (Date)null, "You've been banned from this server by Witherzilla for being annoying.");
                            minecraftserver.getPlayerList().getBannedPlayers().addEntry(userlistbansentry);
                            entityplayermp.connection.disconnect(new TextComponentString("You've been banned from this server by Witherzilla for being annoying."));
                        }
                    }
                }
            }

            if ((getAttackTarget() != null) && (allowStrike()) && ((getAttackTarget() instanceof EntityLivingBase))) {
                double d0 = getDistanceSq(getAttackTarget());
                if (d0 < width * width + getAttackTarget().width * getAttackTarget().width + 6000.0D) {
                    swingArm(net.minecraft.util.EnumHand.MAIN_HAND);
                    attackEntityAsMob(getAttackTarget());
                }
            }

            if (getAttackTarget() != null) {
                List<?> listp = world.playerEntities;
                if ((listp != null) && (!listp.isEmpty()))
                    for (int i1 = 0; i1 < listp.size(); i1++) {
                    Entity entity = (Entity)listp.get(i1);
                    if (entity != null && (entity instanceof EntityPlayer))
                        if (getAttackTarget() instanceof EntityWitherzilla)
                        ((EntityPlayer)entity).sendMessage(new TextComponentString("§l§kRegnator: There's another me. This is a paradox!"));
                }
            }

            if (posY <= 0.0D)
                if (world.provider instanceof WorldProviderVoid || world.provider instanceof WorldProviderEnd)
                teleportRandomly(true);
            else
                teleportRandomly(false);
            List<?> list = world.loadedEntityList;
            if ((list != null) && (!list.isEmpty()))
                for (int i1 = 0; i1 < list.size(); i1++) {
                Entity entity = (Entity)list.get(i1);
                if ((!(world.provider instanceof WorldProviderVoid)) && (entity != null) && entity instanceof EntityPlayer && !((EntityPlayer)entity).getHeldItemMainhand().isEmpty() && ((EntityPlayer)entity).getHeldItemMainhand().getItem() != TitanGear.ultimaBlade && ((EntityPlayer)entity).getHeldItemMainhand().getItem() != TitanGear.optimaAxe && !(((EntityPlayer)entity).getHeldItemMainhand().getItem() instanceof ItemTitanSpawnEgg)) {
                    ((EntityPlayer)entity).renderBrokenItemStack(((EntityPlayer)entity).getHeldItemMainhand());
                    ((EntityPlayer)entity).getHeldItemMainhand().setCount(0);
                    ((EntityPlayer)entity).inventory.deleteStack(((EntityPlayer)entity).getHeldItemMainhand());
                    performStrikeToLightning(entity);
                }

                if (entity instanceof EntityAgeable || entity instanceof EntityAmbientCreature || entity instanceof EntityWaterMob)
                    list.remove(entity);
            }

            for (int i = 1; i < 3; i++) {
                if (ticksExisted >= fld82223h[(i - 1)] && getAttackTarget() != null) {
                    fld82223h[(i - 1)] = (ticksExisted + rand.nextInt(20));
                    int k2 = i - 1;
                    int l2 = fld82224i[(i - 1)];
                    fld82224i[k2] = (fld82224i[(i - 1)] + 1);
                    if (l2 > 15) {
                        for (int i11 = 0; i11 < 100; i11++) {
                            float f = 100.0F;
                            float f1 = 20.0F;
                            double d0 = (posX - f + rand.nextDouble() * ((posX + f) - (posX - f)));
                            double d1 = (posY - f1 + rand.nextDouble() * ((posY + f1) - (posY - f1)));
                            double d2 = (posZ - f + rand.nextDouble() * ((posZ + f) - (posZ - f)));
                            launchWitherSkullToCoords(i + 1, d0, d1, d2, true);
                        }

                        fld82224i[(i - 1)] = 0;
                    }

                    int i1 = getWatchedTargetId(i);
                    if (i1 > 0) {
                        Entity entity = world.getEntityByID(i1);
                        if (entity != null && entity.isEntityAlive()) {
                            launchWitherSkullToEntity(i + 1, (EntityLivingBase)entity);
                            fld82223h[(i - 1)] = ticksExisted;
                            fld82224i[(i - 1)] = 0;
                        }

                        else
                            funcC82211(i, 0);
                    }

                    else
                        if (getAttackTarget() != null && getAttackTarget().isEntityAlive())
                        funcC82211(i, getAttackTarget().getEntityId());
                    else
                        funcC82211(i, 0);
                }
            }

            if (getAttackTarget() != null)
                funcC82211(0, getAttackTarget().getEntityId());
            else
                funcC82211(0, 0);
            super.updateAITasks();
        }
    }

    public void onKillCommand() {
        List<?> list = world.playerEntities;
        if (list != null && !list.isEmpty())
            for (int i1 = 0; i1 < list.size(); i1++) {
            Entity entity = (Entity)list.get(i1);
            if (entity != null)
                ((EntityPlayer)entity).sendMessage(new TextComponentString(I18n.translateToLocal("dialog.witherzilla.killattempt")));
        }
    }

    private double funcU82214(int arg82214n1) {
        return posX;
    }

    private double funcV82208(int arg82208n1) {
        return posY + 64D;
    }

    private double funcW82213(int arg82213n1) {
        return posZ;
    }

    private float funcB82204(float arg82204n1, float arg82204n2, float arg82204n3) {
        float f3 = MathHelper.wrapDegrees(arg82204n2 - arg82204n1);
        if (f3 > arg82204n3)
            f3 = arg82204n3;
        else if (f3 < -arg82204n3)
            f3 = -arg82204n3;
        return arg82204n1 + f3;
    }

    private void launchWitherSkullToEntity(int arg82216n1, EntityLivingBase arg82216n2) {
        if (((arg82216n2 instanceof EntityTitan)) || (arg82216n2.height >= 6.0F)) {
            double d0 = getDistance(arg82216n2);
            if (d0 < width)
                if (attackTimer <= 0) {
                attackTimer = 10;
                attackEntityAsMob(arg82216n2);
                if (rand.nextInt(20) == 0)
                    performStrikeToLightning(arg82216n2);
            }
        }

        else {
            launchWitherSkullToCoords(arg82216n1, arg82216n2.posX, arg82216n2.posY + arg82216n2.getEyeHeight() * 0.5D, arg82216n2.posZ, (arg82216n1 == 0) && (rand.nextFloat() < 0.001F));
            if (rand.nextInt(200) == 0)
                arg82216n2.attackEntityFrom(DamageSource.causeMobDamage(this).setDamageBypassesArmor(), 100F);
            if (rand.nextInt(200) == 0)
                performStrikeToLightning(arg82216n2);
            arg82216n2.hurtResistantTime = 0;
        }

        launchWitherSkullToCoords(arg82216n1, arg82216n2.posX, arg82216n2.posY + arg82216n2.getEyeHeight() * 0.5D, arg82216n2.posZ, (arg82216n1 == 0) && (rand.nextFloat() < 0.001F));
    }

    private void launchWitherSkullToCoords(int arg82209n1, double arg82209n2, double arg82209n4, double arg82209n6, boolean arg82209n8) {
        double d3 = funcU82214(arg82209n1);
        double d4 = funcV82208(arg82209n1);
        double d5 = funcW82213(arg82209n1);
        double d6 = arg82209n2 - d3;
        double d7 = arg82209n4 - d4;
        double d8 = arg82209n6 - d5;
        EntityWitherSkull entitywitherskull = new EntityWitherSkull(world, this, d6, d7, d8);
        if (arg82209n8)
            entitywitherskull.setInvulnerable(true);
        entitywitherskull.posY = d4;
        entitywitherskull.posX = d3;
        entitywitherskull.posZ = d5;
        world.playSound(null, d3, d4, d5, SoundEvents.ENTITY_WITHER_SHOOT, SoundCategory.HOSTILE, 3.0F, 0.8F);
    }

    public void attackEntityWithRangedAttack(EntityLivingBase arg82196n1, float arg82196n2) {
        if (((arg82196n1 instanceof EntityTitan)) || (arg82196n1.height >= 6.0F)) {
            double d0 = getDistanceSq(arg82196n1);
            if (d0 < 1000.0D)
                if (attackTimer <= 0) {
                attackTimer = (1 + rand.nextInt(9));
                attackEntityAsMob(arg82196n1);
            }
        }

        else
            launchWitherSkullToEntity(0, arg82196n1);
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source) || world.getDifficulty() == EnumDifficulty.PEACEFUL || fetchPowerExtra() > 5)
            return false;
        if (source.getTrueSource() instanceof EntityWitherzillaMinion || (source.isExplosion() && !(source.getTrueSource() instanceof EntityWitherTurret)))
            return false;
        if (isArmored() && !(source.getTrueSource() instanceof EntityPlayer) && !(source.getTrueSource() instanceof EntityTitan)) {
            if (rand.nextInt(10) == 0 && source.getTrueSource() != null)
                teleportToEntity(source.getTrueSource(), false);
            return false;
        }

        if (blockBreakCounter <= 0)
            blockBreakCounter = 1;
        ++ticksExisted;
        return super.attackEntityFrom(source, amount);
    }

    public boolean strikeFromWitherzilla(DamageSource source, float amount) {
        if (((source.getTrueSource() instanceof EntityWitherzillaMinion)) || ((source.getTrueSource() instanceof EntityWitherzilla)) || ((source.isExplosion()) && (!(source.getTrueSource() instanceof EntityWitherTurret))))
            return false;
        if ((isArmored()) && (!(source.getTrueSource() instanceof EntityPlayer)) && (!(source.getTrueSource() instanceof EntityTitan)))
            return false;
        if (blockBreakCounter <= 0)
            blockBreakCounter = 1;
        return super.attackEntityFrom(source, amount);
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        if (deathTicks > 0) {
            for (int x = 0; x < 250; x++) {
                EntityXPBomb entitylargefireball = new EntityXPBomb(world, posX, posY + 4D, posZ);
                entitylargefireball.setPosition(posX, posY + 4D, posZ);
                ++entitylargefireball.motionY;
                entitylargefireball.assignTotalXP(32000);
                world.spawnEntity(entitylargefireball);
            }

            for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignCardWild(true);
                itembomb.assignTotalStack(512);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 16; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.COAL_BLOCK));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 8; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.IRON_BLOCK));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 8; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.GOLD_BLOCK));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 4; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.EMERALD_BLOCK));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 4; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.DIAMOND_BLOCK));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(TitanBlockSet.harcadium_block));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(TitanBlockSet.void_block));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(TitanBlockSet.bedrock_block));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 4; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.DRAGON_EGG));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 4; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.NETHER_STAR));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
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

    public void fall(float distance, float damageMultiplier) {
    }

    public void addPotionEffect(PotionEffect arg70690n1) {
    }

    @SideOnly(Side.CLIENT)
    public float funcA82207(int arg82207n1) {
        return fld82221e[arg82207n1];
    }

    @SideOnly(Side.CLIENT)
    public float funcR82210(int arg82210n1) {
        return fld82220d[arg82210n1];
    }

    public int getWatchedTargetId(int arg82203n1) {
        return this.dataManager.get(TARGETS_WATCHED[arg82203n1]).intValue();
    }

    public void funcC82211(int arg82211n1, int arg82211n2) {
        this.dataManager.set(TARGETS_WATCHED[arg82211n1], Integer.valueOf(arg82211n2));
    }

    public boolean isArmored() {
        // titan HP is plain vanilla health now (see EntityTitan.syncHpMax).
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
        if (isServer()) {
            buildPortalBeacon(MathHelper.floor(posX), MathHelper.floor(posZ));
            if ((world.getGameRules().getBoolean("doMobLoot"))) {
                dropFewItems(true, 0);
                dropEquipment(true, 0);
                shedDropRare(1);
            }

            EntityTitanSpirit entitytitan = new EntityTitanSpirit(world);
            entitytitan.setLocationAndAngles(posX, posY + 48D, posZ, rotationYaw, 0.0F);
            world.spawnEntity(entitytitan);
            entitytitan.assignHuntingVessel(true);
            entitytitan.assignKindSpirit(12);
        }
    }

    protected void whenDeathUpdateGiant() {
        dead = false;
        if (getHealth() <= 0F)
            ++deathTicks;
        if ((deathTicks > 180) && (deathTicks % 1 == 0)) {
            float f = (rand.nextFloat() - 0.5F) * 24.0F;
            float f1 = (rand.nextFloat() - 0.5F) * 80.0F;
            float f2 = (rand.nextFloat() - 0.5F) * 24.0F;
            world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX + f, posY + 2.0D + f1, posZ + f2, 0.0D, 0.0D, 0.0D);
        }

        if (world.provider instanceof WorldProviderVoid)
            setLocationAndAngles(0D, 120D, 0D, deathTicks * 10, 0F);
        if (isServer()) {
            if (deathTicks == 1) {
                triggerAudioGiant(fetchDeathAudioGiant(), getSoundVolume(), getSoundPitch());
                world.playBroadcastSound(1018, new BlockPos((int)posX, (int)posY, (int)posZ), 0);
                world.playBroadcastSound(1018, new BlockPos((int)posX, (int)posY, (int)posZ), 0);
                world.playBroadcastSound(1018, new BlockPos((int)posX, (int)posY, (int)posZ), 0);
                world.playBroadcastSound(1018, new BlockPos((int)posX, (int)posY, (int)posZ), 0);
                world.getMinecraftServer().setDifficultyForAllWorlds(EnumDifficulty.PEACEFUL);
                List<?> listp = world.playerEntities;
                if (listp != null && !listp.isEmpty())
                    for (int i1 = 0; i1 < listp.size(); i1++) {
                    EntityPlayer player = (EntityPlayer)listp.get(i1);
                    if ((player != null) && ((player instanceof EntityPlayer))) {
                        if (fetchAchievement() != null)
                            { /* advancement runGrant removed (1.12.2) */ }
                        ItemStack item = new ItemStack(TitanGear.ultimaBlade, 1, 1);
                        player.entityDropItem(item, 0.0F);
                        playLivingSound();
                        if (world.provider instanceof WorldProviderVoid)
                            player.sendMessage(new TextComponentString(I18n.translateToLocal("dialog.witherzilla.death")));
                        else
                            player.sendMessage(new TextComponentString(I18n.translateToLocal("dialog.witherzilla.defeat")));
                    }
                }
            }
        }

        if ((deathTicks >= 200))
            setInvulTime(2000);
        if ((deathTicks >= 400))
            setDead();
    }

    private void buildPortalBeacon(int arg70975n1, int arg70975n2) {
        byte b0 = 64;
        byte b1 = 4;
        for (int k = b0 - 1; k <= b0 + 32; k++)
            for (int l = arg70975n1 - b1; l <= arg70975n1 + b1; l++)
            for (int i1 = arg70975n2 - b1; i1 <= arg70975n2 + b1; i1++) {
            double d0 = l - arg70975n1;
            double d1 = i1 - arg70975n2;
            double d2 = d0 * d0 + d1 * d1;
            if (d2 <= (b1 - 0.5D) * (b1 - 0.5D))
                if (k < b0)
                if (d2 <= (b1 - 1 - 0.5D) * (b1 - 1 - 0.5D))
                world.setBlockState(new BlockPos(l, k, i1), Blocks.BEDROCK.getDefaultState());
            else if (k > b0)
                world.setBlockState(new BlockPos(l, k, i1), Blocks.AIR.getDefaultState());
            else if (d2 > (b1 - 1 - 0.5D) * (b1 - 1 - 0.5D))
                world.setBlockState(new BlockPos(l, k, i1), Blocks.BEDROCK.getDefaultState());
            else
                world.setBlockState(new BlockPos(l, k, i1), Blocks.END_PORTAL.getDefaultState());
        }

        world.setBlockState(new BlockPos(arg70975n1, b0 + 0, arg70975n2), Blocks.BEDROCK.getDefaultState());
        world.setBlockState(new BlockPos(arg70975n1, b0 + 1, arg70975n2), Blocks.BEDROCK.getDefaultState());
        world.setBlockState(new BlockPos(arg70975n1, b0 + 2, arg70975n2), Blocks.BEDROCK.getDefaultState());
        world.setBlockState(new BlockPos(arg70975n1 - 1, b0 + 2, arg70975n2), Blocks.TORCH.getDefaultState());
        world.setBlockState(new BlockPos(arg70975n1 + 1, b0 + 2, arg70975n2), Blocks.TORCH.getDefaultState());
        world.setBlockState(new BlockPos(arg70975n1, b0 + 2, arg70975n2 - 1), Blocks.TORCH.getDefaultState());
        world.setBlockState(new BlockPos(arg70975n1, b0 + 2, arg70975n2 + 1), Blocks.TORCH.getDefaultState());
        world.setBlockState(new BlockPos(arg70975n1, b0 + 3, arg70975n2), Blocks.BEDROCK.getDefaultState());
        world.setBlockState(new BlockPos(arg70975n1, b0 + 4, arg70975n2), Blocks.DIAMOND_BLOCK.getDefaultState());
        world.setBlockState(new BlockPos(arg70975n1 + 1, b0 + 4, arg70975n2 + 1), Blocks.DIAMOND_BLOCK.getDefaultState());
        world.setBlockState(new BlockPos(arg70975n1 + 1, b0 + 4, arg70975n2), Blocks.DIAMOND_BLOCK.getDefaultState());
        world.setBlockState(new BlockPos(arg70975n1 + 1, b0 + 4, arg70975n2 - 1), Blocks.DIAMOND_BLOCK.getDefaultState());
        world.setBlockState(new BlockPos(arg70975n1 - 1, b0 + 4, arg70975n2 + 1), Blocks.DIAMOND_BLOCK.getDefaultState());
        world.setBlockState(new BlockPos(arg70975n1 - 1, b0 + 4, arg70975n2), Blocks.DIAMOND_BLOCK.getDefaultState());
        world.setBlockState(new BlockPos(arg70975n1 - 1, b0 + 4, arg70975n2 - 1), Blocks.DIAMOND_BLOCK.getDefaultState());
        world.setBlockState(new BlockPos(arg70975n1, b0 + 4, arg70975n2 + 1), Blocks.DIAMOND_BLOCK.getDefaultState());
        world.setBlockState(new BlockPos(arg70975n1, b0 + 4, arg70975n2 - 1), Blocks.DIAMOND_BLOCK.getDefaultState());
        world.setBlockState(new BlockPos(arg70975n1, b0 + 5, arg70975n2), Blocks.BEACON.getDefaultState());
    }

    protected void collideWithNearbyEntities() {
    }

    public float getEyeHeight() {
        return 1.4F * fetchScaleMultiplierGiant();
    }

    protected boolean teleportRandomly(boolean bool) {
        double d0 = posX + (rand.nextDouble() - 0.5D) * 64.0D;
        double d2 = posZ + (rand.nextDouble() - 0.5D) * 64.0D;
        if (bool)
            return teleportTo(0.0D, 200.0D, 0.0D);
        else
            return teleportTo(d0, 200.0D, d2);
    }

    protected boolean teleportToEntity(Entity arg70816n1, boolean bool) {
        Vec3d vec3 = new Vec3d(posX - arg70816n1.posX, getEntityBoundingBox().minY + height / 2.0F - arg70816n1.posY + arg70816n1.getEyeHeight(), posZ - arg70816n1.posZ);
        vec3 = vec3.normalize();
        double d0 = 32.0D;
        double d1 = posX + (rand.nextDouble() - 0.5D) * 16.0D - vec3.x * d0;
        double d2 = posY + (rand.nextInt(16) - 8) - vec3.y * d0;
        double d3 = posZ + (rand.nextDouble() - 0.5D) * 16.0D - vec3.z * d0;
        if (bool)
            return teleportTo(arg70816n1.posX, arg70816n1.posY, arg70816n1.posZ);
        else
            return teleportTo(d1, d2, d3);
    }

    protected boolean teleportTo(double arg70825n1, double arg70825n3, double arg70825n5) {
        EnderTeleportEvent event = new EnderTeleportEvent(this, arg70825n1, arg70825n3, arg70825n5, 0.0F);
        if (MinecraftForge.EVENT_BUS.post(event))
            return false;
        if (isServer())
            setPosition(arg70825n1, arg70825n3, arg70825n5);
        return true;
    }

    public boolean processMovementLava() {
        return false;
    }

    public boolean isOmegaFormIn() {
        return !(world.provider instanceof WorldProviderVoid);
    }

    protected EntityLiving fetchMinion() {
        return new EntityWitherMinion(world); //Skigga
    }

    protected double cap() {
        return super.cap();
    }
}
