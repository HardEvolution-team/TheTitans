package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thehippomaster.AnimationAPI.IAnimatedEntity;
import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.core.TheCore;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.minion.EntityGhastGuard;
import com.electrifiedded.thetitans.entity.minion.EntityGhastGuardFireball;
import com.electrifiedded.thetitans.entity.minion.EntityPigZombieMinion;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAITitanLookIdle;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAITitanWander;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAITitanWatchClosest;
import com.electrifiedded.thetitans.entity.titan.anim.pigzombietitan.AnimationPigZombieTitanAntiTitanAttack;
import com.electrifiedded.thetitans.entity.titan.anim.pigzombietitan.AnimationPigZombieTitanAttack1;
import com.electrifiedded.thetitans.entity.titan.anim.pigzombietitan.AnimationPigZombieTitanAttack2;
import com.electrifiedded.thetitans.entity.titan.anim.pigzombietitan.AnimationPigZombieTitanAttack3;
import com.electrifiedded.thetitans.entity.titan.anim.pigzombietitan.AnimationPigZombieTitanAttack4;
import com.electrifiedded.thetitans.entity.titan.anim.pigzombietitan.AnimationPigZombieTitanAttack5;
import com.electrifiedded.thetitans.entity.titan.anim.pigzombietitan.AnimationPigZombieTitanAttack6;
import com.electrifiedded.thetitans.entity.titan.anim.pigzombietitan.AnimationPigZombieTitanCreation;
import com.electrifiedded.thetitans.entity.titan.anim.pigzombietitan.AnimationPigZombieTitanDeath;
import com.electrifiedded.thetitans.entity.titan.anim.pigzombietitan.AnimationPigZombieTitanLightningAttack;
import com.electrifiedded.thetitans.entity.titan.anim.pigzombietitan.AnimationPigZombieTitanRangedAttack;
import com.electrifiedded.thetitans.entity.titan.anim.pigzombietitan.AnimationPigZombieTitanRoar;
import com.electrifiedded.thetitans.entity.titan.anim.pigzombietitan.AnimationPigZombieTitanStun;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanTrophies;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EntityPigZombieTitan extends EntityTitan implements IAnimatedEntity, ITitanHitbox {
    private static final DataParameter<Byte> IS_CHILD = EntityDataManager.<Byte>createKey(EntityPigZombieTitan.class, DataSerializers.BYTE);
    public boolean stunnedIs;
    public EntityTitanPart head;
    public EntityTitanPart body;
    public EntityTitanPart rightArm;
    public EntityTitanPart leftArm;
    public EntityTitanPart rightLeg;
    public EntityTitanPart leftLeg;

    public EntityPigZombieTitan(World worldIn) {
        super(worldIn);
        head = new EntityTitanPart(worldIn, this, "head", 8.0F, 8.0F);
        body = new EntityTitanPart(worldIn, this, "body", 8.0F, 12.0F);
        rightArm = new EntityTitanPart(worldIn, this, "rightarm", 4.0F, 4.0F);
        leftArm = new EntityTitanPart(worldIn, this, "leftarm", 4.0F, 4.0F);
        rightLeg = new EntityTitanPart(worldIn, this, "rightleg", 4.0F, 12.0F);
        leftLeg = new EntityTitanPart(worldIn, this, "leftleg", 4.0F, 12.0F);
        setPiece.add(head);
        setPiece.add(body);
        setPiece.add(rightArm);
        setPiece.add(leftArm);
        setPiece.add(rightLeg);
        setPiece.add(leftLeg);
        particlesBeUpwardNeeds = true;
        experienceValue = 100000;
        setSize(8.0F, 32.0F);
        giantMelee = true;
        setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
        targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityIronGolemTitan.class, 0, false));
        targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntitySnowGolemTitan.class, 0, false));
    }

    protected void applyEntityAI() {
        tasks.addTask(1, new AnimationPigZombieTitanCreation(this));
        tasks.addTask(1, new AnimationPigZombieTitanRangedAttack(this));
        tasks.addTask(1, new AnimationPigZombieTitanRoar(this));
        tasks.addTask(1, new AnimationPigZombieTitanDeath(this));
        tasks.addTask(1, new AnimationPigZombieTitanAttack4(this));
        tasks.addTask(1, new AnimationPigZombieTitanStun(this));
        tasks.addTask(1, new AnimationPigZombieTitanAttack3(this));
        tasks.addTask(1, new AnimationPigZombieTitanAttack2(this));
        tasks.addTask(1, new AnimationPigZombieTitanLightningAttack(this));
        tasks.addTask(1, new AnimationPigZombieTitanAntiTitanAttack(this));
        tasks.addTask(1, new AnimationPigZombieTitanAttack1(this));
        tasks.addTask(1, new AnimationPigZombieTitanAttack6(this));
        tasks.addTask(1, new AnimationPigZombieTitanAttack5(this));
        tasks.addTask(6, new EntityAITitanWander(this, 300));
        tasks.addTask(7, new EntityAITitanWatchClosest(this, EntityTitanSpirit.class, 128.0F));
        tasks.addTask(8, new EntityAITitanWatchClosest(this, EntityTitan.class, 128.0F));
        tasks.addTask(9, new EntityAITitanWatchClosest(this, EntityPlayer.class, 64.0F));
        tasks.addTask(10, new EntityAITitanLookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        if (TheTitans.ffaModeTitans)
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.zombieGiantSorterPig));
        else
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.forAThingToKillSearch));
    }

    protected void entityInit() {
        super.entityInit();
        dataManager.register(IS_CHILD, Byte.valueOf((byte)0));
    }

    @Override
    public double fetchAmountHp() {
        return super.fetchAmountHp() * 20.0D;
    }

    public double getSpeed() {
        return isChild() ? 0.9D + fetchPowerExtra() * 0.001D : 0.6D + fetchPowerExtra() * 0.001D;
    }

    public boolean isArmored() {
        return getHealth() <= getMaxHealth() / 8.0D;
    }

    public boolean isChild() {
        return dataManager.get(IS_CHILD).byteValue() == 1;
    }

    public float getRenderSizeModifier() {
        return super.getRenderSizeModifier();
    }

    public int fetchCapMinion() {
        return (getAnimID() == 11 && getAnimTick() > 80) ? (super.fetchCapMinion() * 6) : (super.fetchCapMinion() * 3);
    }

    public int fetchCapPriest() {
        return (getAnimID() == 11 && getAnimTick() > 80) ? (super.fetchCapPriest() * 6) : (super.fetchCapPriest() * 3);
    }

    public int fetchCapZealot() {
        return (getAnimID() == 11 && getAnimTick() > 80) ? (super.fetchCapZealot() * 6) : (super.fetchCapZealot() * 3);
    }

    public int fetchCapBishop() {
        return (getAnimID() == 11 && getAnimTick() > 80) ? (super.fetchCapBishop() * 6) : (super.fetchCapBishop() * 3);
    }

    public int fetchCapTemplar() {
        return (getAnimID() == 11 && getAnimTick() > 80) ? (super.fetchCapTemplar() * 6) : (super.fetchCapTemplar() * 3);
    }

    protected int getExperiencePoints(EntityPlayer player) {
        if (isChild()) {
            experienceValue = ((int)(experienceValue * 2.5F));
        }

        return super.getExperiencePoints(player);
    }

    protected void whenUpdateBounds() {
        if (ticksExisted > 5) {
            float f = renderYawOffset * 3.1415927F / 180.0F;
            float f1 = MathHelper.sin(f);
            float f2 = MathHelper.cos(f);

            if (isChild()) {
                setSize(0.325F * fetchScaleMultiplierGiant(), 1F * fetchScaleMultiplierGiant());
                head.height = head.width = 0.375F * fetchScaleMultiplierGiant();
                body.height = 0.375F * fetchScaleMultiplierGiant();
                body.width = 0.25F * fetchScaleMultiplierGiant();
                leftLeg.height = rightLeg.height = 0.375F * fetchScaleMultiplierGiant();
                leftLeg.width = rightLeg.width = 0.125F * fetchScaleMultiplierGiant();
                rightArm.width = leftArm.width = rightArm.height = leftArm.height = 0.125F * fetchScaleMultiplierGiant();
            } else {
                setSize(0.5F * fetchScaleMultiplierGiant(), 2F * fetchScaleMultiplierGiant());
                head.height = head.width = 0.5F * fetchScaleMultiplierGiant();
                body.height = 0.75F * fetchScaleMultiplierGiant();
                body.width = 0.5F * fetchScaleMultiplierGiant();
                leftLeg.height = rightLeg.height = 0.75F * fetchScaleMultiplierGiant();
                leftLeg.width = rightLeg.width = 0.25F * fetchScaleMultiplierGiant();
                rightArm.width = leftArm.width = rightArm.height = leftArm.height = 0.25F * fetchScaleMultiplierGiant();
            }

            head.setLocationAndAngles(posX, posY + (0.75D * height), posZ, 0.0F, 0.0F);
            body.setLocationAndAngles(posX, posY + (0.375D * height), posZ, 0.0F, 0.0F);
            rightArm.setLocationAndAngles(posX + f2 * (0.75D * width), posY + (0.625D * height), posZ + f1 * (0.75D * width), 0.0F, 0.0F);
            leftArm.setLocationAndAngles(posX - f2 * (0.75D * width), posY + (0.625D * height), posZ - f1 * (0.75D * width), 0.0F, 0.0F);
            rightLeg.setLocationAndAngles(posX + f2 * (0.25D * width), posY, posZ + f1 * (0.25D * width), 0.0F, 0.0F);
            leftLeg.setLocationAndAngles(posX - f2 * (0.25D * width), posY, posZ - f1 * (0.25D * width), 0.0F, 0.0F);
            if ((isEntityAlive()) && (!stunnedIs)) {
                collideWithEntities(head, world.getEntitiesWithinAABBExcludingEntity(this, head.getEntityBoundingBox().grow(1.0D, 0.0D, 1.0D)));
                collideWithEntities(body, world.getEntitiesWithinAABBExcludingEntity(this, body.getEntityBoundingBox().grow(1.0D, 0.0D, 1.0D)));
                collideWithEntities(rightArm, world.getEntitiesWithinAABBExcludingEntity(this, rightArm.getEntityBoundingBox().grow(1.0D, 0.0D, 1.0D)));
                collideWithEntities(leftArm, world.getEntitiesWithinAABBExcludingEntity(this, leftArm.getEntityBoundingBox().grow(1.0D, 0.0D, 1.0D)));
                collideWithEntities(leftLeg, world.getEntitiesWithinAABBExcludingEntity(this, leftLeg.getEntityBoundingBox().grow(1.0D, 0.0D, 1.0D)));
                collideWithEntities(rightLeg, world.getEntitiesWithinAABBExcludingEntity(this, rightLeg.getEntityBoundingBox().grow(1.0D, 0.0D, 1.0D)));
            }

            destroyBlocksInAABB(head.getEntityBoundingBox());
            destroyBlocksInAABB(body.getEntityBoundingBox());
            destroyBlocksInAABB(rightArm.getEntityBoundingBox());
            destroyBlocksInAABB(leftArm.getEntityBoundingBox());
            destroyBlocksInAABB(leftLeg.getEntityBoundingBox().grow(1.0D, 0.0D, 1.0D));
            destroyBlocksInAABB(rightLeg.getEntityBoundingBox().grow(1.0D, 0.0D, 1.0D));
        }

        super.whenUpdateBounds();
    }

    public void setChild(boolean childZombie) {
        dataManager.set(IS_CHILD, Byte.valueOf((byte)(childZombie ? 1 : 0)));
    }

    public int fetchStepModiferFoot() {
        return 5;
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        if (isChild()) {
            tagCompound.setBoolean("IsBaby", true);
        }

        tagCompound.setBoolean("Stunned", stunnedIs);
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.getBoolean("IsBaby")) {
            setChild(true);
        }

        stunnedIs = tagCompund.getBoolean("Stunned");
    }

    public EnumTitanStatus fetchConditionGiant() {
        return EnumTitanStatus.AVERAGE;
    }

    @SuppressWarnings("rawtypes")
    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        return arg70686n1 != head.getClass() && arg70686n1 != body.getClass() && arg70686n1 != rightArm.getClass() && arg70686n1 != leftArm.getClass() && arg70686n1 != rightLeg.getClass() && arg70686n1 != leftLeg.getClass() && (arg70686n1 != EntityPigZombieMinion.class) && (arg70686n1 != EntityGhastGuard.class) && (arg70686n1 != EntityPigZombieTitan.class);
    }

    public void onLivingUpdate() {
        if (motionY > 3.0D)motionY *= 0.8D;
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
            }

            if (getAnimID() == 13 && getAnimTick() == 1)
                triggerAudioGiant("thetitans:titanBirth", 1000.0F, 1.0F);
            if (getAnimID() == 13 && getAnimTick() == 10)
                triggerAudioGiant("thetitans:titanPigZombieLiving", getSoundVolume(), 0.8F);
            if (getAnimID() == 13 && getAnimTick() == 40)
                triggerAudioGiant("thetitans:titanRumble", getSoundVolume(), 1F);
            if (getAnimID() == 13 && getAnimTick() == 240)
                triggerAudioGiant("thetitans:titanZombieRoar", getSoundVolume(), 1F);
            if (getAnimID() == 13 && getAnimTick() == 130)
                triggerAudioGiant("thetitans:titanSkeletonGetUp", getSoundVolume(), 1F);
            if (getAnimID() == 13 && (getAnimTick() == 110 || getAnimTick() == 150 || getAnimTick() == 170)) {
                funcA145780(0, 0, 0, Blocks.STONE);
                triggerAudioGiant("thetitans:titanPress", getSoundVolume(), 1F);
            }
        }

        if ((world.isRemote) && (deathTicks < fetchHoldThreash()) && (isWet())) {
            for (int i = 0; i < getParticleCount() * 50 && com.electrifiedded.thetitans.util.perf.TitanParticles.allow(world); i++) {
                world.spawnParticle(titanForParticle(fetchParticle()), posX + (rand.nextDouble() - 0.5D) * width, posY + rand.nextDouble() * height, posZ + (rand.nextDouble() - 0.5D) * width, 0.0D, 0.25D, 0.0D);
            }
        }

        if ((rand.nextInt(40) == 0) && (isWet())) {
            triggerAudioGiant("random.fizz", 20.0F, 1.5F);
        }

        if (getAnimID() == 8) {
            if (getAnimTick() == 140 || getAnimTick() == 300 || getAnimTick() == 330) {
                triggerAudioGiant("thetitans:slashFlesh", 20.0F, 1.0F);
                attackEntityFromPart(head, (new DamageSource("other")).setDamageBypassesArmor().setDamageIsAbsolute(), 1000F);
            }

            if (getAnimTick() == 400)
                triggerAudioGiant("thetitans:titanZombieRoar", 1000F, 0.95F);
            if (getAnimTick() == 530)
                stunnedIs = false;
            else
                setAttackTarget(null);
        }

        if (stunnedIs) {
            setAttackTarget(null);
            AnimationAPI.sendAnimPacket(this, 8);
            setAnimID(8);
        }

        if (getAnimID() == 10) {
            if ((getAnimTick() == 30) || (getAnimTick() == 70)) {
                funcA145780(0, 0, 0, Blocks.STONE);
            }

            if (getAnimTick() == 190) {
                triggerAudioGiant("thetitans:titanFall", 20.0F, 1.0F);
                triggerAudioGiant("thetitans:groundSmash", 20.0F, 1.0F);
                shakeUserCamerasNearby(10D);
                collideWithEntities(leftLeg, world.getEntitiesWithinAABBExcludingEntity(this, leftLeg.getEntityBoundingBox().grow(48.0D, 48.0D, 48.0D)));
                collideWithEntities(rightLeg, world.getEntitiesWithinAABBExcludingEntity(this, rightLeg.getEntityBoundingBox().grow(48.0D, 48.0D, 48.0D)));
                collideWithEntities(leftLeg, world.getEntitiesWithinAABBExcludingEntity(this, leftLeg.getEntityBoundingBox().grow(48.0D, 48.0D, 48.0D)));
                collideWithEntities(rightLeg, world.getEntitiesWithinAABBExcludingEntity(this, rightLeg.getEntityBoundingBox().grow(48.0D, 48.0D, 48.0D)));
            }

            if (getAnimTick() == 200) {
                triggerAudioGiant("thetitans:distantLargeFall", 10000.0F, 1.0F);
            }
        }

        if ((getAnimID() == 7) && (getAnimTick() == 122)) {
            double d8 = 32.0D;
            Vec3d vec3 = getLook(1.0F);
            double dx = vec3.x * d8;
            double dz = vec3.z * d8;
            int y = MathHelper.floor(posY);
            int x = MathHelper.floor(posX + dx);
            int z = MathHelper.floor(posZ + dz);
            if (world.getBlockState(new BlockPos(x, y - 1, z)).getMaterial() != Material.AIR) {
                triggerAudioGiant("thetitans:titanStrike", 20.0F, 1.0F);
                triggerAudioGiant("thetitans:titanSlam", 20.0F, 1.0F);
            }

            for (int l1 = -4; l1 <= 4; l1++) {
                for (int i2 = -4; i2 <= 4; i2++) {
                    for (int j = -1; j <= 1; j++) {
                        int j2 = x + l1;
                        int k = y + j;
                        int l = z + i2;
                        BlockPos pos = new BlockPos(j2, k, l);
                        net.minecraft.block.state.IBlockState state = world.getBlockState(pos);
                        Block block = state.getBlock();
                        if (!world.isAirBlock(pos)) {
                            world.playEvent(2001, pos, Block.getStateId(state));
                            if (block == Blocks.GRASS) {
                                world.setBlockState(pos, Blocks.DIRT.getDefaultState());
                            }
                        }

                        if (block.getExplosionResistance(this) <= 1.5F && state.isOpaqueCube() && block != Blocks.AIR && block != Blocks.NETHERRACK && block != Blocks.DIRT && block != Blocks.GRASS && block != Blocks.GLASS && block != Blocks.GLASS_PANE) {
                            AnimationAPI.sendAnimPacket(this, 8);
                            setAnimID(8);
                            setAnimTick(0);
                            triggerAudioGiant("random.anvil_land", 20.0F, 0.5F);
                            if (!world.isRemote)
                                stunnedIs = true;
                        }
                    }
                }
            }
        }

        float f = renderYawOffset * 3.1415927F / 180.0F;

        giantMelee = true;
        if (getAnimID() == 5) {
            if (getAnimTick() == 34)
                triggerAudioGiant("thetitans:lightningCharge", 100F, 1F);
            if ((getAnimTick() <= 50) && (getAnimTick() >= 20)) {
                float ex = isChild() ? 4.5F : 9.5F;
                float fl = renderYawOffset * 3.1415927F / 180.0F;
                float fl1 = MathHelper.sin(fl);
                float fl2 = MathHelper.cos(fl);
                world.addWeatherEffect(new EntityGammaLightning(world, posX - fl2 * ex, posY + (isChild() ? 12D : 24D), posZ - fl1 * ex, 1.0F, 0.0F, 0F));
                world.addWeatherEffect(new EntityGammaLightning(world, posX + fl2 * ex, posY + (isChild() ? 12D : 24D), posZ + fl1 * ex, 1.0F, 0.0F, 0F));
                if (getAttackTarget() == null && !world.isRemote)
                    heal(50F);
            }

            if ((getAnimTick() == 64)) {
                triggerAudioGiant("thetitans:lightningThrow", 100F, 1F);
                double d8 = isChild() ? 6F : 12D;
                Vec3d vec3 = getLook(1.0F);
                double dx = vec3.x * d8;
                double dz = vec3.z * d8;
                float da = (float)fetchAmountStrike(5.0F);
                int i1 = fetchAmountKnockback();
                world.newExplosion(this, posX + dx, posY + 26.0D, posZ + dz, 1.0F, false, false);
                world.addWeatherEffect(new EntityGammaLightning(world, posX + dx, posY + (isChild() ? 9D : 18D), posZ + dz, 1.0F, 0.0F, 0F));
                if (getAttackTarget() != null) {
                    strikeMobChoosen(getAttackTarget(), da, i1);
                    strikeMobChoosen(getAttackTarget(), da, i1);
                    strikeMobChoosen(getAttackTarget(), da, i1);
                    getAttackTarget().motionY += 1.0F + rand.nextFloat();
                    world.newExplosion(this, getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 2.0F, false, false);
                    getAttackTarget().attackEntityFrom(DamageSourceExtra.lightningBolt, f);
                    world.addWeatherEffect(new EntityGammaLightning(world, getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 1.0F, 0.0F, 0F));
                    List<?> found1 = world.getEntitiesWithinAABB(EntityLivingBase.class, getAttackTarget().getEntityBoundingBox().grow(12.0D, 12.0D, 12.0D), e -> e != getAttackTarget());
                    if ((found1 != null) && (!found1.isEmpty())) {
                        for (int i11 = 0; i11 < found1.size(); i11++) {
                            Entity mobRef1 = (Entity)found1.get(i11);
                            if (((mobRef1 instanceof EntityLivingBase)) && allowStrikeGiant(mobRef1.getClass())) {
                                world.addWeatherEffect(new EntityGammaLightning(world, mobRef1.posX, mobRef1.posY, mobRef1.posZ, 1.0F, 0.0F, 0F));
                                strikeMobChoosen(mobRef1, da, i1);
                                strikeMobChoosen(mobRef1, da, i1);
                                if (!(mobRef1 instanceof EntityTitan))
                                    mobRef1.motionY += 1.0F + rand.nextFloat();
                            }
                        }
                    }

                    for (int it = 0; it < 300; it++) {
                        Vec3d vec31 = getLook(1F);
                        double d5 = (getAttackTarget().posX + (rand.nextGaussian() * 8D)) - (head.posX + vec31.x * (16.0D + (getRNG().nextFloat() * 2.0F - 1.0F) * 16.0F));
                        double d6 = (getAttackTarget().posY + (rand.nextGaussian() * 8D)) - (head.posY + 32D + (getRNG().nextFloat() * 2.0F - 1.0F) * 16.0F);
                        double d7 = (getAttackTarget().posZ + (rand.nextGaussian() * 8D)) - (head.posZ + vec31.z * (16.0D + (getRNG().nextFloat() * 2.0F - 1.0F) * 16.0F));
                        EntityGhastGuardFireball entitylargefireball = new EntityGhastGuardFireball(world, this, d5, d6, d7);
                        entitylargefireball.posX = (head.posX + vec31.x * (16.0D + (getRNG().nextFloat() * 2.0F - 1.0F) * 16.0F));
                        entitylargefireball.posY = (head.posY + 32D + (getRNG().nextFloat() * 2.0F - 1.0F) * 16.0F);
                        entitylargefireball.posZ = (head.posZ + vec31.z * (16.0D + (getRNG().nextFloat() * 2.0F - 1.0F) * 16.0F));
                        world.spawnEntity(entitylargefireball);
                        EntityTitanFireball entitylargefireball1 = new EntityTitanFireball(world, this, d5, d6, d7, 3);
                        entitylargefireball1.posX = (head.posX + (vec31.x * (16.0D + (getRNG().nextFloat() * 2.0F - 1.0F) * 16.0F)));
                        entitylargefireball1.posY = (head.posY + 32D + (getRNG().nextFloat() * 2.0F - 1.0F) * 16.0F);
                        entitylargefireball1.posZ = (head.posZ + (vec31.z * (16.0D + (getRNG().nextFloat() * 2.0F - 1.0F) * 16.0F)));
                        world.spawnEntity(entitylargefireball1);
                        entitylargefireball1.assignIDFireball(3);
                        EntitySmallFireball entitysmallfireball = new EntitySmallFireball(world, this, d5, d6, d7);
                        entitysmallfireball.posX = (head.posX + vec31.x * (16.0D + (getRNG().nextFloat() * 2.0F - 1.0F) * 16.0F));
                        entitysmallfireball.posY = (head.posY + 32D + (getRNG().nextFloat() * 2.0F - 1.0F) * 16.0F);
                        entitysmallfireball.posZ = (head.posZ + vec31.z * (16.0D + (getRNG().nextFloat() * 2.0F - 1.0F) * 16.0F));
                        world.spawnEntity(entitysmallfireball);
                    }
                }
            }
        }

        if (stunnedIs || deathTicks > 0) {
            motionX *= 0.0D;
            motionZ *= 0.0D;
        }

        if ((!AnimationAPI.isEffectiveClient()) && (getAttackTarget() != null) && (!stunnedIs) && ((getAnimID() == 0))) {
            double d0 = getDistance(getAttackTarget());
            if (d0 < fetchReachMelee()) {
                switch (rand.nextInt(7)) {
                    case 0:AnimationAPI.sendAnimPacket(this, 6);
                        setAnimID(6);
                        break;
                    case 1:AnimationAPI.sendAnimPacket(this, 4);
                        setAnimID(4);
                        break;
                    case 2:AnimationAPI.sendAnimPacket(this, 9);
                        setAnimID(9);
                        break;
                    case 3:if (getAttackTarget().height >= height - 12F || (posY + getEyeHeight() < getAttackTarget().posY + getAttackTarget().getEyeHeight())) {
                            AnimationAPI.sendAnimPacket(this, 1);
                            setAnimID(1);
                        }

                        else {
                            AnimationAPI.sendAnimPacket(this, 4);
                            setAnimID(4);
                        }

                        break;
                    case 4:if (getAttackTarget().height >= height - 12F || (posY + getEyeHeight() < getAttackTarget().posY + getAttackTarget().getEyeHeight())) {
                            AnimationAPI.sendAnimPacket(this, 1);
                            setAnimID(1);
                        }

                        else {
                            AnimationAPI.sendAnimPacket(this, 3);
                            setAnimID(3);
                        }

                        break;
                }
            }

            else if ((getAnimID() == 0) && (getRNG().nextInt(100) == 0)) {
                switch (rand.nextInt(3)) {
                    case 0:AnimationAPI.sendAnimPacket(this, 5);
                        setAnimID(5);
                        break;
                    case 1:AnimationAPI.sendAnimPacket(this, 12);
                        setAnimID(12);
                        break;
                    case 2:if (getRNG().nextInt(4) == 0) {
                            AnimationAPI.sendAnimPacket(this, 11);
                            setAnimID(11);
                        }

                        else {
                            AnimationAPI.sendAnimPacket(this, 5);
                            setAnimID(5);
                        }

                        break;
                }
            }
        }

        if (getAnimID() == 12) {
            if (getAttackTarget() != null && getAnimTick() == 55)
                for (int i = 0; i < 4 + (2 * world.getDifficulty().getId()); ++i)
                attackEntityWithRangedAttack(getAttackTarget(), 0F);
        }

        if ((rand.nextInt(20) == 0) && (getAttackTarget() != null)) {
            triggerAudioGiant("mob.zombiepig.zpigangry", 10000.0F, ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) * ((0.6F + rand.nextFloat())));
        }

        if (isChild()) {
            setCustomNameTag(I18n.translateToLocal("entity.PigZombieTitan.name.baby"));
        }

        else {
            setCustomNameTag(I18n.translateToLocal("entity.PigZombieTitan.name"));
        }

        super.onLivingUpdate();
    }

    public void attackEntityWithRangedAttack(EntityLivingBase arg82196n1, float arg82196n2) {
        renderYawOffset = rotationYaw = rotationYawHead;
        faceEntity(arg82196n1, 180.0F, 30.0F);
        double d8 = 12D;
        Vec3d vec3 = getLook(1.0F);
        double dx = vec3.x * d8;
        double dz = vec3.z * d8;
        EntityProtoBall entityarrow = new EntityProtoBall(world, this);
        double d0 = (arg82196n1.posX + arg82196n1.motionX) - (head.posX + dx);
        double d1 = (arg82196n1.posY + (double)arg82196n1.getEyeHeight() - 8D) - head.posY;
        double d2 = (arg82196n1.posZ + arg82196n1.motionZ) - (head.posZ + dz);
        float f1 = MathHelper.sqrt(d0 * d0 + d2 * d2);
        entityarrow.shoot(d0, d1 + f1, d2, 0.95F, (float)(45 - world.getDifficulty().getId() * 5));
        entityarrow.posX = head.posX + dx;
        entityarrow.posY = head.posY;
        entityarrow.posZ = head.posZ + dz;
        if (!world.isRemote)
            world.spawnEntity(entityarrow);
        entityarrow.motionX *= 1.5F;
        entityarrow.motionZ *= 1.5F;
        world.playEvent((EntityPlayer)null, 1008, new BlockPos((int)posX, (int)posY, (int)posZ), 0);
        if (getDistance(arg82196n1) < 20.0D) {
            strikeMobChoosen(arg82196n1, 10F, 5);
        }
    }

    public boolean getCanSpawnHere() {
        return (rand.nextInt(100) == 0) && (world.getDifficulty() != EnumDifficulty.PEACEFUL);
    }

    public int fetchSpawnRateMinion() {
        return TheTitans.zombieGiantServantSpawnratePig;
    }

    public boolean needsMove() {
        return !stunnedIs && super.needsMove();
    }

    public boolean attackEntityAsMob(Entity arg70652n1) {
        return false;
    }

    public boolean allowHurtByUserBe() {
        return (stunnedIs) && (!isEntityInvulnerable(DamageSource.GENERIC));
    }

    public String fetchParticle() {
        if (world.rand.nextFloat() < 0.25D) {
            return "explode";
        }

        return "smoke";
    }

    private net.minecraft.util.EnumParticleTypes titanForParticle(String s) {
        if ("explode".equals(s)) return net.minecraft.util.EnumParticleTypes.EXPLOSION_NORMAL;
        if ("hugeexplosion".equals(s)) return net.minecraft.util.EnumParticleTypes.EXPLOSION_HUGE;
        return net.minecraft.util.EnumParticleTypes.SMOKE_NORMAL;
    }

    public boolean isEntityUndead() {
        return true;
    }

    public void fall(float arg70069n1, float damageMultiplier) {
        super.fall(arg70069n1, damageMultiplier);
        if (!world.isRemote)
            appendArmourRandom();
    }

    public float getEyeHeight() {
        return 0.8625F * height;
    }

    protected String fetchLivingAudioGiant() {
        return stunnedIs || fetchWaiting() || getAnimID() == 13 ? null : "thetitans:titanPigZombieLiving";
    }

    protected String fetchHurtAudioGiant() {
        return "thetitans:titanPigZombieGrunt";
    }

    protected String fetchDeathAudioGiant() {
        return "thetitans:titanPigZombieDeath";
    }

    protected void funcA145780(int arg145780n1, int arg145780n2, int arg145780n3, Block arg145780n4) {
        triggerAudioGiant("thetitans:titanStep", 10.0F, 1.0F);
        shakeUserCamerasNearby(4D);
        if (!fetchWaiting() && getAnimID() != 13) {
            float f3 = rotationYaw * (float)Math.PI / 180.0F;
            float f11 = MathHelper.sin(f3);
            float f4 = MathHelper.cos(f3);
            if (keyFoot == 0) {
                destroyBlocksInAABB(leftLeg.getEntityBoundingBox().offset(0, -1, 0));
                collideWithEntities(leftLeg, world.getEntitiesWithinAABBExcludingEntity(this, leftLeg.getEntityBoundingBox().grow(1.0D, 1.0D, 1.0D).offset((double)(f11 * 4F), 0, (double)(f4 * 4F))));
                ++keyFoot;
            }

            else {
                destroyBlocksInAABB(rightLeg.getEntityBoundingBox().offset(0, -1, 0));
                collideWithEntities(rightLeg, world.getEntitiesWithinAABBExcludingEntity(this, rightLeg.getEntityBoundingBox().grow(1.0D, 1.0D, 1.0D).offset((double)(f11 * 4F), 0, (double)(f4 * 4F))));
                keyFoot = 0;
            }
        }
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        if (deathTicks > 0) {
            for (int x = 0; x < 24; x++) {
                EntityXPBomb entitylargefireball = new EntityXPBomb(world, posX, posY + 4D, posZ);
                entitylargefireball.setPosition(posX, posY + 4D, posZ);
                ++entitylargefireball.motionY;
                entitylargefireball.assignTotalXP(20000);
                world.spawnEntity(entitylargefireball);
            }

            for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.STICK));
                itembomb.assignTotalStack(16);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.GOLD_INGOT));
                itembomb.assignTotalStack(16);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 8; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.ROTTEN_FLESH));
                itembomb.assignTotalStack(24 + rand.nextInt(24 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 8; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.GOLD_NUGGET));
                itembomb.assignTotalStack(24 + rand.nextInt(24 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.BONE));
                itembomb.assignTotalStack(16 + rand.nextInt(16 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 4; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.COAL));
                itembomb.assignTotalStack(32 + rand.nextInt(32 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.EMERALD));
                itembomb.assignTotalStack(8 + rand.nextInt(8 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.DIAMOND));
                itembomb.assignTotalStack(8 + rand.nextInt(8 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.GOLD_INGOT));
                itembomb.assignTotalStack(16 + rand.nextInt(16 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.IRON_INGOT));
                itembomb.assignTotalStack(32 + rand.nextInt(32 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(TitanGear.harcadium));
                itembomb.assignTotalStack(4 + rand.nextInt(4 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            if (rand.nextInt(5) == 0)
                for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.BEDROCK));
                itembomb.assignTotalStack(1);
                world.spawnEntity(itembomb);
            }

            if (rand.nextInt(5) == 0)
                for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.BEDROCK));
                itembomb.assignTotalStack(1);
                world.spawnEntity(itembomb);
            }
        }
    }

    public String fetchAchievement() {
        return TitanTrophies.pigzombietitan;
    }

    public double getYOffset() {
        return super.getYOffset() - 8.0D;
    }

    protected void shedDropRare(int arg70600n1) {
        entityDropItem(new ItemStack(Blocks.GOLD_BLOCK, 64, 1), 0.0F);
    }

    protected void appendArmourRandom() {
        setEquipmentBasedOnDifficulty(world.getDifficultyForLocation(new BlockPos(this)));
        setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
        for (int i = 0; i < 2; i++) {
            EntityGhastGuard entitychicken = new EntityGhastGuard(world);
            entitychicken.master = this;
            entitychicken.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
            entitychicken.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
            world.spawnEntity(entitychicken);
            entitychicken.addVelocity(0.0D, 1.5D, 0.0D);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titansummonminion", world, entitychicken, 10.0F, 0.5F);
        }
    }

    public boolean strikeFromZombie(DamageSource source, float amount) {
        if (isEntityInvulnerable(source)) {
            return false;
        }

        else if (((source.getTrueSource() instanceof EntityPigZombieMinion)) || ((source.getTrueSource() instanceof EntityPigZombieTitan)) || ((source.getTrueSource() instanceof EntityGhastGuard))) {
            return false;
        }

        else {
            Entity entity = source.getTrueSource();
            if (((entity instanceof EntityLivingBase)) && (!isEntityInvulnerable(source)) && (amount > 25.0F)) {
                List<?> list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(256.0D, 256.0D, 256.0D));
                for (int i = 0; i < list.size(); i++) {
                    Entity mobRef1 = (Entity)list.get(i);
                    if ((mobRef1 instanceof EntityPigZombieTitan)) {
                        EntityPigZombieTitan entitypigzombie = (EntityPigZombieTitan)mobRef1;
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

    public boolean attackEntityFrom(DamageSource source, float amount) {
        return strikeFromZombie(source, amount);
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean attackEntityFromPart(EntityTitanPart arg70965n1, DamageSource source, float amount) {
        funcE82195(source, amount);
        return true;
    }

    protected boolean funcE82195(DamageSource arg82195n1, float arg82195n2) {
        return strikeFromZombie(arg82195n1, arg82195n2);
    }

    public World funcD82194() {
        return world;
    }

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData arg180482n2) {
        Object p_180482_2_1 = super.onInitialSpawn(difficulty, arg180482n2);
        setCanPickUpLoot(true);
        assignWaiting(true);
        if (world.rand.nextFloat() < 0.05F) {
            setChild(true);
        }

        appendArmourRandom();
        setEnchantmentBasedOnDifficulty(difficulty);
        if (getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
            Calendar calendar = world.getCurrentDate();
            if ((calendar.get(2) + 1 == 10) && (calendar.get(5) == 31) && (rand.nextFloat() < 0.25F)) {
                setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(rand.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
                setDropChance(EntityEquipmentSlot.HEAD, 0.0F);
            }
        }

        return (IEntityLivingData)p_180482_2_1;
    }

    protected void inactActionDeath() {
        if (!world.isRemote) {
            triggerAudioGiant("mob.zombiepig.zpigdeath", getSoundVolume(), getSoundPitch());
            if ((world.getGameRules().getBoolean("doMobLoot"))) {
                dropFewItems(true, 0);
                dropEquipment(true, 0);
                shedDropRare(1);
            }

            EntityTitanSpirit entitytitan = new EntityTitanSpirit(world);
            entitytitan.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
            world.spawnEntity(entitytitan);
            entitytitan.assignHuntingVessel(false);
            entitytitan.assignKindSpirit(8);
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
                AnimationAPI.sendAnimPacket(this, 0);
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
            world.spawnParticle(net.minecraft.util.EnumParticleTypes.EXPLOSION_HUGE, posX + f, posY + 2.0D + f1, posZ + f2, 0.0D, 0.0D, 0.0D);
        }

        if (getInvulTime() >= fetchHoldThreash()) {
            setDead();
        }
    }

    protected EntityLiving fetchMinion() {
        return new EntityPigZombieMinion(world);
    }

    protected double cap() {
        return super.cap();
    }
}
