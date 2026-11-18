package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thehippomaster.AnimationAPI.IAnimatedEntity;
import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.core.TheCore;
import com.electrifiedded.thetitans.entity.minion.EntityGiantZombieBetter;
import com.electrifiedded.thetitans.entity.minion.EntityZombieMinion;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAITitanWander;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAITitanWatchClosest;
import com.electrifiedded.thetitans.entity.titan.anim.zombietitan.AnimationZombieTitan10Death;
import com.electrifiedded.thetitans.entity.titan.anim.zombietitan.AnimationZombieTitan11Roar;
import com.electrifiedded.thetitans.entity.titan.anim.zombietitan.AnimationZombieTitan12Protoballs;
import com.electrifiedded.thetitans.entity.titan.anim.zombietitan.AnimationZombieTitan13Creation;
import com.electrifiedded.thetitans.entity.titan.anim.zombietitan.AnimationZombieTitan1AntiTitanAttack;
import com.electrifiedded.thetitans.entity.titan.anim.zombietitan.AnimationZombieTitan2RebuildSword;
import com.electrifiedded.thetitans.entity.titan.anim.zombietitan.AnimationZombieTitan3Slam;
import com.electrifiedded.thetitans.entity.titan.anim.zombietitan.AnimationZombieTitan4Kick;
import com.electrifiedded.thetitans.entity.titan.anim.zombietitan.AnimationZombieTitan5LightningAttack;
import com.electrifiedded.thetitans.entity.titan.anim.zombietitan.AnimationZombieTitan6Stomp;
import com.electrifiedded.thetitans.entity.titan.anim.zombietitan.AnimationZombieTitan7DownwardSlash;
import com.electrifiedded.thetitans.entity.titan.anim.zombietitan.AnimationZombieTitan8Stun;
import com.electrifiedded.thetitans.entity.titan.anim.zombietitan.AnimationZombieTitan9SidewaysSlash;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanTrophies;
import com.electrifiedded.thetitans.world.dimension.WorldProviderNowhere;
import com.electrifiedded.thetitans.world.dimension.WorldProviderVoid;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraftforge.common.ForgeModContainer;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EntityZombieTitan extends EntityTitan implements IAnimatedEntity, ITitanHitbox {
    private static final DataParameter<Byte> IS_CHILD = EntityDataManager.<Byte>createKey(EntityZombieTitan.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> VILLAGER_IS = EntityDataManager.<Byte>createKey(EntityZombieTitan.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> UNUSED_14_ZOMBIE = EntityDataManager.<Byte>createKey(EntityZombieTitan.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> ARMED_IS = EntityDataManager.<Byte>createKey(EntityZombieTitan.class, DataSerializers.BYTE);
    private static final DataParameter<Byte> SOFT_SWORD = EntityDataManager.<Byte>createKey(EntityZombieTitan.class, DataSerializers.BYTE);
    public EntityTitanPart head;
    public EntityTitanPart body;
    public EntityTitanPart rightArm;
    public EntityTitanPart leftArm;
    public EntityTitanPart rightLeg;
    public EntityTitanPart leftLeg;

    public EntityZombieTitan(World worldIn) {
        super(worldIn);
        head = new EntityTitanPart(worldIn, this, "head", 0.5F * fetchScaleMultiplierGiant(), 0.5F * fetchScaleMultiplierGiant());
        body = new EntityTitanPart(worldIn, this, "body", 0.5F * fetchScaleMultiplierGiant(), 0.75F * fetchScaleMultiplierGiant());
        rightArm = new EntityTitanPart(worldIn, this, "rightarm", 0.25F * fetchScaleMultiplierGiant(), 0.25F * fetchScaleMultiplierGiant());
        leftArm = new EntityTitanPart(worldIn, this, "leftarm", 0.25F * fetchScaleMultiplierGiant(), 0.25F * fetchScaleMultiplierGiant());
        rightLeg = new EntityTitanPart(worldIn, this, "rightleg", 0.25F * fetchScaleMultiplierGiant(), 0.75F * fetchScaleMultiplierGiant());
        leftLeg = new EntityTitanPart(worldIn, this, "leftleg", 0.25F * fetchScaleMultiplierGiant(), 0.75F * fetchScaleMultiplierGiant());
        setPiece.add(head);
        setPiece.add(body);
        setPiece.add(rightArm);
        setPiece.add(leftArm);
        setPiece.add(rightLeg);
        setPiece.add(leftLeg);
        setSize(0.5F * fetchScaleMultiplierGiant(), 2F * fetchScaleMultiplierGiant());
        experienceValue = 10000;
        targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityIronGolemTitan.class, 0, false));
        targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntitySnowGolemTitan.class, 0, false));
    }

    protected void applyEntityAI() {
        keyFoot = 1;
        tasks.addTask(1, new AnimationZombieTitan13Creation(this));
        tasks.addTask(1, new AnimationZombieTitan10Death(this));
        tasks.addTask(1, new AnimationZombieTitan9SidewaysSlash(this));
        tasks.addTask(1, new AnimationZombieTitan8Stun(this));
        tasks.addTask(1, new AnimationZombieTitan7DownwardSlash(this));
        tasks.addTask(1, new AnimationZombieTitan6Stomp(this));
        tasks.addTask(1, new AnimationZombieTitan12Protoballs(this));
        tasks.addTask(1, new AnimationZombieTitan5LightningAttack(this));
        tasks.addTask(1, new AnimationZombieTitan11Roar(this));
        tasks.addTask(1, new AnimationZombieTitan1AntiTitanAttack(this));
        tasks.addTask(1, new AnimationZombieTitan3Slam(this));
        tasks.addTask(1, new AnimationZombieTitan2RebuildSword(this));
        tasks.addTask(1, new AnimationZombieTitan4Kick(this));
        tasks.addTask(6, new EntityAITitanWander(this, 300));
        tasks.addTask(7, new EntityAITitanWatchClosest(this, EntityTitanSpirit.class, 128.0F));
        tasks.addTask(8, new EntityAITitanWatchClosest(this, EntityTitan.class, 128.0F));
        tasks.addTask(9, new EntityAITitanWatchClosest(this, EntityPlayer.class, 64.0F));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        if (TheTitans.ffaModeTitans)
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.giantSorterZombie));
        else
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.forAThingToKillSearch));
    }

    @Override
    public double fetchAmountHp() {
        if (fetchVariantGiant() == 5)
            return super.fetchAmountHp() * 40.0D;
        return super.fetchAmountHp() * 20.0D;
    }

    public boolean isArmored() {
        return getHealth() <= getMaxHealth() / 8.0D;
    }

    @SuppressWarnings("rawtypes")
    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        return arg70686n1 != head.getClass() && arg70686n1 != body.getClass() && arg70686n1 != rightArm.getClass() && arg70686n1 != leftArm.getClass() && arg70686n1 != rightLeg.getClass() && arg70686n1 != leftLeg.getClass() && (arg70686n1 != EntityZombieMinion.class) && (arg70686n1 != EntityGiantZombieBetter.class) && (arg70686n1 != EntityZombieTitan.class);
    }

    public float getRenderSizeModifier() {
        return super.getRenderSizeModifier();
    }

    public boolean getCanSpawnHere() {
        return (rand.nextInt(50) == 0) && (world.getDifficulty() != EnumDifficulty.PEACEFUL) && (isValidLightLevel());
    }

    public int fetchSpawnRateMinion() {
        return TheTitans.giantServantSpawnrateZombie;
    }

    public int fetchCapMinion() {
        return (getAnimID() == 11 && getAnimTick() > 80) ? (super.fetchCapMinion() * 4) : (super.fetchCapMinion() * 2);
    }

    public int fetchCapPriest() {
        return (getAnimID() == 11 && getAnimTick() > 80) ? (super.fetchCapPriest() * 4) : (super.fetchCapPriest() * 2);
    }

    public int fetchCapZealot() {
        return (getAnimID() == 11 && getAnimTick() > 80) ? (super.fetchCapZealot() * 4) : (super.fetchCapZealot() * 2);
    }

    public int fetchCapBishop() {
        return (getAnimID() == 11 && getAnimTick() > 80) ? (super.fetchCapBishop() * 4) : (super.fetchCapBishop() * 2);
    }

    public int fetchCapTemplar() {
        return (getAnimID() == 11 && getAnimTick() > 80) ? (super.fetchCapTemplar() * 4) : (super.fetchCapTemplar() * 2);
    }

    public EnumTitanStatus fetchConditionGiant() {
        return EnumTitanStatus.AVERAGE;
    }

    protected void entityInit() {
        super.entityInit();
        dataManager.register(IS_CHILD, Byte.valueOf((byte)0));
        dataManager.register(VILLAGER_IS, Byte.valueOf((byte)0));
        dataManager.register(UNUSED_14_ZOMBIE, Byte.valueOf((byte)0));
        dataManager.register(ARMED_IS, Byte.valueOf((byte)0));
        dataManager.register(SOFT_SWORD, Byte.valueOf((byte)0));
    }

    public int fetchStepModiferFoot() {
        return isChild() ? 1 : super.fetchStepModiferFoot();
    }

    public boolean isEntityUndead() {
        return true;
    }

    public boolean isChild() {
        return dataManager.get(IS_CHILD) == 1;
    }

    protected int getExperiencePoints(EntityPlayer player) {
        if (isChild()) {
            experienceValue = ((int)(experienceValue * 2.5F));
        }

        return super.getExperiencePoints(player);
    }

    public void setChild(boolean childZombie) {
        dataManager.set(IS_CHILD, Byte.valueOf((byte)(childZombie ? 1 : 0)));
        if (!childZombie) {
            AnimationAPI.sendAnimPacket(this, 2);
            AnimationAPI.sendAnimPacket(this, 0);
            setAnimTick(0);
        }
    }

    public boolean villagerIs() {
        return dataManager.get(VILLAGER_IS) == 1;
    }

    public void assignVillager(boolean villager) {
        dataManager.set(VILLAGER_IS, Byte.valueOf((byte)(villager ? 1 : 0)));
    }

    public boolean armedIs() {
        return dataManager.get(ARMED_IS) == 1;
    }

    public void assignArmed(boolean armed) {
        dataManager.set(ARMED_IS, Byte.valueOf((byte)(armed ? 1 : 0)));
    }

    public boolean isSoftSword() {
        return dataManager.get(SOFT_SWORD) == 1;
    }

    public void assignSoftSword(boolean armed) {
        dataManager.set(SOFT_SWORD, Byte.valueOf((byte)(armed ? 1 : 0)));
    }

    public double getSpeed() {
        return (isChild() ? 0.46D : 0.23D) / 4;
    }

    public boolean allowHurtByUserBe() {
        return (!armedIs()) && (!isEntityInvulnerable(DamageSource.GENERIC));
    }

    public boolean needsMove() {
        return !stunnedIs && super.needsMove();
    }

    public boolean attackEntityAsMob(Entity arg70652n1) {
        return false;
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
        entityarrow.shoot(d0, d1 + f1, d2, 0.95F, (float)((45 * (arg82196n2 + 0.1F)) - world.getDifficulty().getId() * 5));
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

    public void onLivingUpdate() {
        if (motionY > 3.0D)motionY *= 0.8D;
        if (ticksExisted % 80 == 0)
            if (!world.loadedEntityList.isEmpty()) {
            for (int l1 = 0; l1 < world.loadedEntityList.size(); ++l1) {
                Entity entity = (Entity)world.loadedEntityList.get(l1);
                if (entity != null && entity instanceof EntityTitan && !entity.isEntityAlive() && getDistance(entity) < 150D && getAnimTick() <= 0 && getAnimID() != 11) {
                    AnimationAPI.sendAnimPacket(this, 11);
                    setAnimID(11);
                }
            }
        }

        switch (fetchVariantGiant()) {
            case 2: {
                    enactAuraEffect(3, getEntityBoundingBox().grow(8, 8, 8));
                    break;
                }

            case 3: {
                    enactAuraEffect(0, getEntityBoundingBox().grow(24, 12, 24));
                    break;
                }

            case 4: {
                    enactAuraEffect(4, getEntityBoundingBox().grow(48, 48, 48));
                    break;
                }
        }

        if (!isRiding() && !fetchWaiting() && !stunnedIs && getAnimID() == 0) {
            flyTitan(0.625F * fetchScaleMultiplierGiant(), 0.125F * fetchScaleMultiplierGiant(), 0.25D * fetchScaleMultiplierGiant());
        }

        if (fetchWaiting()) {
            AnimationAPI.sendAnimPacket(this, 13);
            if (allowAwaken()) {
                awaken();
            }
        }

        else {
            if (getAnimID() == 13) {
                if ((getAnimTick() == 80 || getAnimTick() == 150 || getAnimTick() == 370 || getAnimTick() == 410 || getAnimTick() == 450 || getAnimTick() == 470 || getAnimTick() == 490)) {
                    funcA145780(0, 0, 0, Blocks.STONE);
                    triggerAudioGiant("thetitans:titanPress", getSoundVolume(), 1F);
                }
            }
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

        if (getAnimID() == 8) {
            if (getAnimTick() > 138)
                stunnedIs = false;
            else
                stunnedIs = true;
        }

        if ((getAnimID() == 8) && (getAnimTick() == 4) && (!world.isRemote)) {
            shedSword();
        }

        if ((getAnimID() == 8) && (getAnimTick() >= 80) && (getAnimTick() <= 100)) {
            triggerAudioGiant(fetchLivingAudioGiant(), getSoundVolume(), 1.1F);
        }

        if ((getAnimID() == 7) && (getAnimTick() == 122)) {
            double d8 = 2D * fetchScaleMultiplierGiant();
            Vec3d vec3 = getLook(1.0F);
            double dx = vec3.x * d8;
            double dz = vec3.z * d8;
            int y = MathHelper.floor(posY);
            int x = MathHelper.floor(posX + dx);
            int z = MathHelper.floor(posZ + dz);
            if (world.getBlockState(new BlockPos(x, y - 1, z)).getMaterial() != Material.AIR) {
                triggerAudioGiant("thetitans:titanStrike", 20.0F, 1.0F);
                triggerAudioGiant("thetitans:titanSlam", 20.0F, 1.0F);
                triggerAudioGiant("thetitans:titanPress", 100F, 1F);
            }

            for (int l1 = -4; l1 <= 4; l1++) {
                for (int i2 = -4; i2 <= 4; i2++) {
                    for (int j = -2; j <= 2; j++) {
                        int j2 = x + l1;
                        int k = y + j;
                        int l = z + i2;
                        BlockPos pos = new BlockPos(j2, k, l);
                        IBlockState state = world.getBlockState(pos);
                        Block block = state.getBlock();
                        if (!block.isAir(state, world, pos)) {
                            world.playEvent(2001, pos, Block.getStateId(state));
                            if (block == Blocks.GRASS) {
                                world.setBlockState(pos, Blocks.DIRT.getDefaultState());
                            }
                        }

                        if (block == Blocks.NETHERRACK && block == Blocks.GRAVEL && block == Blocks.ICE && block == Blocks.GLASS && block == Blocks.GLASS_PANE) {
                            world.setBlockState(pos, Blocks.AIR.getDefaultState());
                        }

                        if (block.getExplosionResistance(this) > 500.0F && state.isOpaqueCube() && !isSoftSword()) {
                            setAnimTick(0);
                            AnimationAPI.sendAnimPacket(this, 8);
                            setAnimID(8);
                            triggerAudioGiant("random.anvil_land", 20.0F, 0.5F);
                            stunnedIs = true;
                        }

                        else if (block.getExplosionResistance(this) <= 1.5F && state.isOpaqueCube() && isSoftSword() && block != Blocks.AIR && block != Blocks.NETHERRACK && block != Blocks.DIRT && block != Blocks.GRASS && block != Blocks.GLASS && block != Blocks.GLASS_PANE) {
                            setAnimTick(0);
                            AnimationAPI.sendAnimPacket(this, 8);
                            setAnimID(8);
                            triggerAudioGiant("random.anvil_land", 20.0F, 0.5F);
                            stunnedIs = true;
                        }
                    }
                }
            }
        }

        if ((getAnimID() == 2) && (getAnimTick() == 160)) {
            double d8 = 0.75D * fetchScaleMultiplierGiant();
            Vec3d vec3 = getLook(1.0F);
            double dx = vec3.x * d8;
            double dz = vec3.z * d8;
            int y = MathHelper.floor(posY);
            int x = MathHelper.floor(posX + dx);
            int z = MathHelper.floor(posZ + dz);
            for (int l1 = -2; l1 <= 2; l1++) {
                for (int i2 = -2; i2 <= 2; i2++) {
                    for (int j = -1; j <= 1; j++) {
                        int j2 = x + l1;
                        int k = y + j;
                        int l = z + i2;
                        BlockPos pos = new BlockPos(j2, k, l);
                        IBlockState state = world.getBlockState(pos);
                        Block block = state.getBlock();
                        if (!block.isAir(state, world, pos)) {
                            world.playEvent(2001, pos, Block.getStateId(state));
                            if (block == Blocks.GRASS) {
                                world.setBlockState(pos, Blocks.DIRT.getDefaultState());
                            }
                        }

                        if (block.getExplosionResistance(this) > 500.0F) {
                            assignSoftSword(false);
                        }

                        else if (block.getExplosionResistance(this) <= 500.0F) {
                            assignSoftSword(true);
                        }
                    }
                }
            }
        }

        giantMelee = true;
        if (getAnimID() == 12) {
            if (getAttackTarget() != null && getAnimTick() == 55) {
                attackEntityWithRangedAttack(getAttackTarget(), 0F);
                for (int i = 0; i < 4 + (2 * world.getDifficulty().getId()); ++i)
                    attackEntityWithRangedAttack(getAttackTarget(), 1F);
            }
        }

        if (stunnedIs || deathTicks > 0) {
            motionX *= 0.0D;
            motionZ *= 0.0D;
        }

        if ((!AnimationAPI.isEffectiveClient()) && (getAttackTarget() != null) && (!stunnedIs) && (getAnimID() == 0) && ticksExisted > 5) {
            double d0 = getDistance(getAttackTarget());
            if (d0 < fetchReachMelee()) {
                switch (rand.nextInt(7)) {
                    case 0:AnimationAPI.sendAnimPacket(this, 6);
                        setAnimID(6);
                        break;
                    case 1:if (armedIs()) {
                            AnimationAPI.sendAnimPacket(this, 7);
                            setAnimID(7);
                        }

                        else {
                            if (getRNG().nextInt(4) == 0) {
                                AnimationAPI.sendAnimPacket(this, 2);
                                setAnimID(2);
                            }

                            else {
                                AnimationAPI.sendAnimPacket(this, 4);
                                setAnimID(4);
                            }
                        }

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
                switch (rand.nextInt(4)) {
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
                    case 3:if (armedIs()) {
                            AnimationAPI.sendAnimPacket(this, 5);
                            setAnimID(5);
                        }

                        else {
                            if (getRNG().nextInt(5) == 0) {
                                AnimationAPI.sendAnimPacket(this, 2);
                                setAnimID(2);
                            }

                            else {
                                AnimationAPI.sendAnimPacket(this, 5);
                                setAnimID(5);
                            }
                        }

                        break;
                }
            }
        }

        if (getAnimID() == 2 && motionY > 0D)motionY = 0.0D;
        if (!armedIs() && getAttackTarget() != null && getAttackTarget() instanceof EntityTitan && getAnimID() == 0) {
            AnimationAPI.sendAnimPacket(this, 2);
        }

        super.onLivingUpdate();
    }

    protected void whenUpdateBounds() {
        if (ticksExisted > 5) {
            float f = renderYawOffset * 3.1415927F / 180.0F;
            float f1 = MathHelper.sin(f);
            float f2 = MathHelper.cos(f);
            fetchScaleMultiplierGiant();
            MathHelper.sin(limbSwing + f1);
            MathHelper.cos(limbSwing + f2);
            if (isChild()) {
                setSize(0.325F * fetchScaleMultiplierGiant(), 1F * fetchScaleMultiplierGiant());
                head.height = head.width = 0.375F * fetchScaleMultiplierGiant();
                body.height = 0.375F * fetchScaleMultiplierGiant();
                body.width = 0.25F * fetchScaleMultiplierGiant();
                leftLeg.height = rightLeg.height = 0.375F * fetchScaleMultiplierGiant();
                leftLeg.width = rightLeg.width = 0.125F * fetchScaleMultiplierGiant();
                rightArm.width = leftArm.width = rightArm.height = leftArm.height = 0.125F * fetchScaleMultiplierGiant();
            }

            else {
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
        }

        super.whenUpdateBounds();
    }

    public String getName() {
        String addition = "";
        if (villagerIs() && isChild())
            addition = ".babyvillager";
        if (!villagerIs() && isChild())
            addition = ".baby";
        if (villagerIs() && !isChild())
            addition = ".villager";
        if (!villagerIs() && !isChild())
            addition = "";
        switch (fetchVariantGiant()) {
            case 1:
                return I18n.translateToLocal("entity.ZombieTitanJungle.name" + addition);
            case 2:
                return I18n.translateToLocal("entity.ZombieTitanBlood.name" + addition);
            case 3:
                return I18n.translateToLocal("entity.ZombieTitanRadioactive.name" + addition);
            case 4:
                return I18n.translateToLocal("entity.ZombieTitanVoid.name" + addition);
            default:
                return I18n.translateToLocal("entity.ZombieTitan.name" + addition);
        }
    }

    protected void updateAITasks() {
        super.updateAITasks();
    }

    protected String fetchLivingAudioGiant() {
        if (!fetchWaiting() && getAnimID() != 13)
            triggerAudioGiant("mob.zombie.say", getSoundVolume(), getSoundPitch() - 0.6F);
        return fetchWaiting() || getAnimID() == 13 ? null : "thetitans:titanZombieLiving";
    }

    protected String fetchHurtAudioGiant() {
        triggerAudioGiant("mob.zombie.hurt", getSoundVolume(), getSoundPitch() - 0.6F);
        return "thetitans:titanZombieGrunt";
    }

    protected String fetchDeathAudioGiant() {
        triggerAudioGiant("mob.zombie.death", getSoundVolume(), getSoundPitch() - 0.6F);
        return "thetitans:titanZombieDeath";
    }

    protected void funcA145780(int arg145780n1, int arg145780n2, int arg145780n3, Block arg145780n4) {
        triggerAudioGiant(fetchScaleMultiplierGiant() <= 3F ? "mob.zombie.step" : "thetitans:titanStep", fetchScaleMultiplierGiant(), isChild() ? 1.5F : 1.0F);
        shakeUserCamerasNearby(isChild() ? 2 : 4);
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

    protected void shedSword() {
        if (armedIs() && !world.isRemote) {
            for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.STICK));
                itembomb.assignTotalStack(16);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.IRON_INGOT));
                itembomb.assignTotalStack(16);
                world.spawnEntity(itembomb);
            }

            assignArmed(false);
        }
    }

    protected Item getDropItem() {
        return Items.ROTTEN_FLESH;
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        if (deathTicks > 0) {
            for (int x = 0; x < 16; x++) {
                EntityXPBomb entitylargefireball = new EntityXPBomb(world, posX, posY + 4D, posZ);
                entitylargefireball.setPosition(posX, posY + 4D, posZ);
                ++entitylargefireball.motionY;
                entitylargefireball.assignTotalXP(12000);
                world.spawnEntity(entitylargefireball);
            }

            if (armedIs()) {
                for (int x = 0; x < 1; x++) {
                    EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                    itembomb.assignStackStackMob(new ItemStack(Items.STICK));
                    itembomb.assignTotalStack(16);
                    world.spawnEntity(itembomb);
                }

                for (int x = 0; x < 2; x++) {
                    EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                    itembomb.assignStackStackMob(new ItemStack(Items.IRON_INGOT));
                    itembomb.assignTotalStack(16);
                    world.spawnEntity(itembomb);
                }
            }

            for (int x = 0; x < 8; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.ROTTEN_FLESH));
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

            if (rand.nextInt(10) == 0)
                for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.BEDROCK));
                itembomb.assignTotalStack(1);
                world.spawnEntity(itembomb);
            }
        }
    }

    protected void shedDropRare(int arg70600n1) {
        switch (rand.nextInt(3)) {
            case 0:dropItem(Items.IRON_INGOT, 64);
                break;
            case 1:dropItem(Items.CARROT, 64);
                break;
            case 2:dropItem(Items.POTATO, 64);
        }
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("IsBaby", isChild());
        tagCompound.setBoolean("IsVillager", villagerIs());
        tagCompound.setBoolean("IsArmed", armedIs());
        tagCompound.setBoolean("Stunned", stunnedIs);
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        setChild(tagCompund.getBoolean("IsBaby"));
        assignVillager(tagCompund.getBoolean("IsVillager"));
        assignArmed(tagCompund.getBoolean("IsArmed"));
        stunnedIs = tagCompund.getBoolean("Stunned");
    }

    public void onKillEntity(EntityLivingBase entityLivingIn) {
        super.onKillEntity(entityLivingIn);
        if ((entityLivingIn instanceof EntityVillager)) {
            EntityZombieMinion entityzombie = new EntityZombieMinion(world);
            entityzombie.copyLocationAndAnglesFrom(entityLivingIn);
            world.removeEntity(entityLivingIn);
            entityzombie.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityzombie)), (IEntityLivingData)null);
            entityzombie.assignVillager(true);
            if (entityLivingIn.isChild()) {
                entityzombie.setChild(true);
            }

            world.spawnEntity(entityzombie);
            world.playEvent((EntityPlayer)null, 1016, new BlockPos((int)posX, (int)posY, (int)posZ), 0);
        }
    }

    public float getEyeHeight() {
        return 0.8625F * height;
    }

    public IEntityLivingData onInitialSpawn(net.minecraft.world.DifficultyInstance difficulty, IEntityLivingData arg180482n2) {
        Object p_180482_2_1 = super.onInitialSpawn(difficulty, arg180482n2);
        setCanPickUpLoot(true);
        assignArmed(false);
        if (world.provider instanceof WorldProviderNowhere || world.provider instanceof WorldProviderVoid || world.provider instanceof WorldProviderEnd)
            assignVariantGiant(4);
        assignWaiting(true);
        if (p_180482_2_1 == null) {
            p_180482_2_1 = new GroupData(world.rand.nextFloat() < ForgeModContainer.zombieBabyChance, world.rand.nextFloat() < 0.05F, null);
        }

        if ((p_180482_2_1 instanceof GroupData)) {
            GroupData groupdata = (GroupData)p_180482_2_1;
            if (groupdata.fld142046b) {
                assignVillager(true);
            }

            if (groupdata.fld142048a) {
                setChild(true);
            }
        }

        if (getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
            Calendar calendar = world.getCurrentDate();
            if ((calendar.get(2) + 1 == 10) && (calendar.get(5) == 31) && (rand.nextFloat() < 0.25F)) {
                setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(rand.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
                setDropChance(EntityEquipmentSlot.HEAD, 0.0F);
            }
        }

        return (IEntityLivingData)p_180482_2_1;
    }

    public double getYOffset() {
        return super.getYOffset() - 8.0D;
    }

    public String fetchAchievement() {
        return TitanTrophies.zombietitan;
    }

    protected void inactActionDeath() {
        if (!world.isRemote) {
            triggerAudioGiant("mob.zombie.death", getSoundVolume(), getSoundPitch());
            if ((world.getGameRules().getBoolean("doMobLoot"))) {
                dropFewItems(true, 0);
                dropEquipment(true, 0);
                shedDropRare(1);
            }

            EntityTitanSpirit entitytitan = new EntityTitanSpirit(world);
            entitytitan.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
            world.spawnEntity(entitytitan);
            entitytitan.assignHuntingVessel(false);
            entitytitan.assignKindSpirit(6);
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

        if (deathTicks >= 80 && armedIs()) shedSword();
        if (deathTicks >= 800) {
            float f = (rand.nextFloat() - 0.5F) * fetchMPArea(2.25F);
            float f1 = (rand.nextFloat() - 0.5F) * fetchMPArea(0.5F);
            float f2 = (rand.nextFloat() - 0.5F) * fetchMPArea(2.25F);
            world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX + f, posY + 2.0D + f1, posZ + f2, 0.0D, 0.0D, 0.0D);
            if (fetchScaleMultiplierGiant() <= 1.0F) {
                setDead();
            }
        }
    }

    class GroupData implements IEntityLivingData {
        public boolean fld142048a;
        public boolean fld142046b;
        private GroupData(boolean p_i2348_2_, boolean p_i2348_3_) {
            fld142048a = false;
            fld142046b = false;
            fld142048a = p_i2348_2_;
            fld142046b = p_i2348_3_;
        }

        GroupData(boolean p_i2349_2_, boolean p_i2349_3_, Object p_i2349_4_) {
            this(p_i2349_2_, p_i2349_3_);
        }
    }

    public boolean strikeFromZombie(DamageSource source, float amount) {
        if (isEntityInvulnerable(source)) {
            return false;
        }

        else if (isArmored() && source instanceof EntityDamageSourceIndirect) {
            return false;
        }

        else if (((source.getTrueSource() instanceof EntityZombieMinion)) || ((source.getTrueSource() instanceof EntityZombieTitan)) || ((source.getTrueSource() instanceof EntityGiantZombieBetter))) {
            return false;
        }

        else {
            Entity entity = source.getTrueSource();
            if (((entity instanceof EntityLivingBase)) && (!isEntityInvulnerable(source)) && (amount > 25.0F)) {
                List<?> list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(100.0D, 100.0D, 100.0D));
                for (Object mobRef1Obj : list) {
                    Entity mobRef1 = (Entity) mobRef1Obj;
                    if ((mobRef1 instanceof EntityZombieTitan)) {
                        EntityZombieTitan entitypigzombie = (EntityZombieTitan)mobRef1;
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

    protected EntityLiving fetchMinion() {
        return new EntityZombieMinion(world);
    }

    protected double cap() {
        return super.cap();
    }

    protected double fetchRegen() {
        return world.isDaytime() ? super.fetchRegen() / 3 : super.fetchRegen();
    }
}
