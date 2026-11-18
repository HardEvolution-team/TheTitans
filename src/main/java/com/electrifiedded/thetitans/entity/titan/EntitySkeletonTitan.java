package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thehippomaster.AnimationAPI.IAnimatedEntity;
import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.core.TheCore;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.minion.EntitySkeletonMinion;
import com.electrifiedded.thetitans.entity.minion.EntityWitherMinion;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAITitanLookIdle;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAITitanWander;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAITitanWatchClosest;
import com.electrifiedded.thetitans.entity.titan.anim.skeletontitan.AnimationSkeletonTitanAntiTitanAttack;
import com.electrifiedded.thetitans.entity.titan.anim.skeletontitan.AnimationSkeletonTitanAttack1;
import com.electrifiedded.thetitans.entity.titan.anim.skeletontitan.AnimationSkeletonTitanAttack2;
import com.electrifiedded.thetitans.entity.titan.anim.skeletontitan.AnimationSkeletonTitanAttack3;
import com.electrifiedded.thetitans.entity.titan.anim.skeletontitan.AnimationSkeletonTitanAttack4;
import com.electrifiedded.thetitans.entity.titan.anim.skeletontitan.AnimationSkeletonTitanAttack5;
import com.electrifiedded.thetitans.entity.titan.anim.skeletontitan.AnimationSkeletonTitanCreation;
import com.electrifiedded.thetitans.entity.titan.anim.skeletontitan.AnimationSkeletonTitanDeath;
import com.electrifiedded.thetitans.entity.titan.anim.skeletontitan.AnimationSkeletonTitanLightningHand;
import com.electrifiedded.thetitans.entity.titan.anim.skeletontitan.AnimationSkeletonTitanLightningSword;
import com.electrifiedded.thetitans.entity.titan.anim.skeletontitan.AnimationSkeletonTitanRangedAttack1;
import com.electrifiedded.thetitans.entity.titan.anim.skeletontitan.AnimationSkeletonTitanRangedAttack2;
import com.electrifiedded.thetitans.entity.titan.anim.skeletontitan.AnimationSkeletonTitanStun;
import com.electrifiedded.thetitans.proxy.CommonProxy;
import com.electrifiedded.thetitans.registry.TitanBlockSet;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanTrophies;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;
import net.minecraftforge.fml.common.Loader;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EntitySkeletonTitan extends EntityTitan implements IRangedAttackMob, IAnimatedEntity, ITitanHitbox {
    private static final DataParameter<Byte> KIND_SKELETON = EntityDataManager.<Byte>createKey(EntitySkeletonTitan.class, DataSerializers.BYTE);
    public boolean beWitherSkeletonNeeds;
    public int attackTimer;
    public EntityTitanPart head;
    public EntityTitanPart pelvis;
    public EntityTitanPart spine;
    public EntityTitanPart cageRib;
    public EntityTitanPart rightArm;
    public EntityTitanPart leftArm;
    public EntityTitanPart rightLeg;
    public EntityTitanPart leftLeg;

    public EntitySkeletonTitan(World worldIn) {
        super(worldIn);
        head = new EntityTitanPart(worldIn, this, "head", 8.0F, 8.0F);
        pelvis = new EntityTitanPart(worldIn, this, "pelvis", 8.0F, 6.0F);
        spine = new EntityTitanPart(worldIn, this, "spine", 2.0F, 12.0F);
        cageRib = new EntityTitanPart(worldIn, this, "ribcage", 8.0F, 8.0F);
        rightArm = new EntityTitanPart(worldIn, this, "rightarm", 2.0F, 2.0F);
        leftArm = new EntityTitanPart(worldIn, this, "leftarm", 2.0F, 2.0F);
        rightLeg = new EntityTitanPart(worldIn, this, "rightleg", 2.0F, 12.0F);
        leftLeg = new EntityTitanPart(worldIn, this, "leftleg", 2.0F, 12.0F);
        setPiece.add(head);
        setPiece.add(pelvis);
        setPiece.add(spine);
        setPiece.add(cageRib);
        setPiece.add(rightArm);
        setPiece.add(leftArm);
        setPiece.add(rightLeg);
        setPiece.add(leftLeg);
        if (fetchKindSkeleton() == 1) {
            setSize(14F, 56F);
            experienceValue = 50000;
        }

        setSize(8.0F, 32.0F);
        experienceValue = 20000;
        if ((worldIn != null) && (!worldIn.isRemote)) {
            setCombatTask();
        }

        tasks.addTask(6, new EntityAITitanWander(this, 300));
        tasks.addTask(7, new EntityAITitanWatchClosest(this, EntityTitanSpirit.class, 128.0F));
        tasks.addTask(8, new EntityAITitanWatchClosest(this, EntityTitan.class, 128.0F));
        tasks.addTask(9, new EntityAITitanWatchClosest(this, EntityPlayer.class, 64.0F));
        tasks.addTask(10, new EntityAITitanLookIdle(this));
        targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityIronGolemTitan.class, 0, false));
        targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntitySnowGolemTitan.class, 0, false));
    }

    protected void whenUpdateBounds() {
        if (ticksExisted > 5) {
            setSize(0.5F * fetchScaleMultiplierGiant(), 2F * fetchScaleMultiplierGiant());
            float f = renderYawOffset * 3.1415927F / 180.0F;
            float f1 = MathHelper.sin(f);
            float f2 = MathHelper.cos(f);

            head.width = head.height = 0.5F * fetchScaleMultiplierGiant();
            pelvis.width = 0.5F * fetchScaleMultiplierGiant();
            spine.height = 0.375F * fetchScaleMultiplierGiant();
            cageRib.height = 0.3125F * fetchScaleMultiplierGiant();
            cageRib.width = 0.4375F * fetchScaleMultiplierGiant();
            pelvis.height = spine.width = rightLeg.width = leftLeg.width = leftArm.width = rightArm.width = 0.125F * fetchScaleMultiplierGiant();
            rightLeg.height = leftLeg.height = leftArm.height = rightArm.height = 0.75F * fetchScaleMultiplierGiant();

            head.setLocationAndAngles(posX, posY + (0.75D * height), posZ, 0.0F, 0.0F);
            pelvis.setLocationAndAngles(posX, posY + (0.375D * height), posZ, 0.0F, 0.0F);
            spine.setLocationAndAngles(posX + f1 * (0.25D * width), posY + (0.425D * height), posZ - f2 * (0.25D * width), 0.0F, 0.0F);
            cageRib.setLocationAndAngles(posX, posY + (0.59375D * height), posZ, 0.0F, 0.0F);
            rightArm.setLocationAndAngles(posX + f2 * (0.725D * width), posY + (0.359375D * height), posZ + f1 * (0.725D * width), 0.0F, 0.0F);
            leftArm.setLocationAndAngles(posX - f2 * (0.725D * width), posY + (0.359375D * height), posZ - f1 * (0.725D * width), 0.0F, 0.0F);
            rightLeg.setLocationAndAngles(posX + f2 * (0.25D * width), posY, posZ + f1 * (0.25D * width), 0.0F, 0.0F);
            leftLeg.setLocationAndAngles(posX - f2 * (0.25D * width), posY, posZ - f1 * (0.25D * width), 0.0F, 0.0F);
        }

        super.whenUpdateBounds();
    }

    protected void applyEntityAI() {
        tasks.addTask(1, new AnimationSkeletonTitanCreation(this));
        tasks.addTask(1, new AnimationSkeletonTitanDeath(this));
        tasks.addTask(1, new AnimationSkeletonTitanLightningHand(this));
        tasks.addTask(1, new AnimationSkeletonTitanLightningSword(this));
        tasks.addTask(1, new AnimationSkeletonTitanStun(this));
        tasks.addTask(1, new AnimationSkeletonTitanAttack3(this));
        tasks.addTask(1, new AnimationSkeletonTitanAttack2(this));
        tasks.addTask(1, new AnimationSkeletonTitanRangedAttack1(this));
        tasks.addTask(1, new AnimationSkeletonTitanRangedAttack2(this));
        tasks.addTask(1, new AnimationSkeletonTitanAttack5(this));
        tasks.addTask(1, new AnimationSkeletonTitanAttack1(this));
        tasks.addTask(1, new AnimationSkeletonTitanAttack4(this));
        tasks.addTask(1, new AnimationSkeletonTitanAntiTitanAttack(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        if (TheTitans.ffaModeTitans)
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.giantSorterSkeleton));
        else
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.forAThingToKillSearch));
    }

    @Override
    public double fetchAmountHp() {
        if (fetchKindSkeleton() == 1)
            switch (fetchVariantGiant()) {
            case 1: return super.fetchAmountHp() * 30.0D;
            case 2: return super.fetchAmountHp() * 40.0D;
            default: return super.fetchAmountHp() * 20.0D;
        } else
                switch (fetchVariantGiant()) {
            case 1: return super.fetchAmountHp() * 18.0D;
            case 2: return super.fetchAmountHp() * 25.0D;
            case 3: return super.fetchAmountHp() * 22.0D;
            case 4: return super.fetchAmountHp() * 40.0D;
            default: return super.fetchAmountHp() * 20.0D;
        }
    }

    public boolean isArmored() {
        return getHealth() <= getMaxHealth() / 8.0D;
    }

    @SuppressWarnings("rawtypes")
    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        return arg70686n1 != head.getClass() && arg70686n1 != pelvis.getClass() && arg70686n1 != rightArm.getClass() && arg70686n1 != leftArm.getClass() && arg70686n1 != rightLeg.getClass() && arg70686n1 != leftLeg.getClass() && (arg70686n1 != EntitySkeletonMinion.class) && (arg70686n1 != EntitySkeletonTitan.class) && (arg70686n1 != EntitySkeletonTitanGiantArrow.class) && (arg70686n1 != EntityWitherMinion.class);
    }

    public boolean getCanSpawnHere() {
        return (rand.nextInt(100) == 0) && (world.getDifficulty() != EnumDifficulty.PEACEFUL) && (isValidLightLevel());
    }

    public int fetchSpawnRateMinion() {
        if (fetchKindSkeleton() == 1) {
            return TheTitans.skeletonGiantServantSpawnrateWither;
        }

        return TheTitans.giantServantSpawnrateSkeleton;
    }

    public float getRenderSizeModifier() {
        return super.getRenderSizeModifier();
    }

    public int getParticleCount() {
        if (fetchKindSkeleton() == 1) {
            return 48;
        }

        return super.getParticleCount();
    }

    public String fetchParticle() {
        if (fetchKindSkeleton() == 1) {
            particlesBeUpwardNeeds = true;
            return "largesmoke";
        }

        return super.fetchParticle();
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(140.0D);
    }

    public boolean allowHurtByUserBe() {
        return (stunnedIs) && (!isEntityInvulnerable(DamageSource.GENERIC));
    }

    public void destroyBlocksInAABB(AxisAlignedBB arg70972n1) {
        boolean flag = fetchKindSkeleton() == 1 && fetchVariantGiant() == 1;
        int i = MathHelper.floor(arg70972n1.minX);
        int j = MathHelper.floor(arg70972n1.minY);
        int k = MathHelper.floor(arg70972n1.minZ);
        int l = MathHelper.floor(arg70972n1.maxX);
        int i1 = MathHelper.floor(arg70972n1.maxY);
        int j1 = MathHelper.floor(arg70972n1.maxZ);
        for (int k1 = i; k1 <= l; k1++) {
            for (int l1 = j; l1 <= i1; l1++) {
                for (int i2 = k; i2 <= j1; i2++) {
                    BlockPos pos = new BlockPos(k1, l1, i2);
                    IBlockState state = world.getBlockState(pos);
                    Block block = state.getBlock();
                    if (fetchKindSkeleton() == 0 && fetchVariantGiant() == 1 && (block == Blocks.GRASS || block == Blocks.DIRT || block == Blocks.GRAVEL))
                        world.setBlockState(pos, Blocks.SAND.getDefaultState());
                    if ((flag && block == Blocks.FIRE)) {
                        world.setBlockState(pos, Blocks.FIRE.getDefaultState());
                    }

                    else if (ticksExisted > 5 && arg70972n1 != null && world.isBlockLoaded(pos) && (!block.isAir(state, world, pos)) && (!world.isRemote)) {
                        if (state.getBlockHardness(world, pos) != -1F) {
                            if (!world.getGameRules().getBoolean("doTileDrops") || state.getMaterial().isLiquid() || block == Blocks.FIRE || block == Blocks.WEB) {
                                world.setBlockToAir(pos);
                            }

                            else {
                                if (rand.nextInt(5) == 0) {
                                    EntityFallingBlockTitan entityfallingblock = new EntityFallingBlockTitan(world, k1 + 0.5D, l1 + 0.5D, i2 + 0.5D, (fetchKindSkeleton() == 0 && fetchVariantGiant() == 1 && (block == Blocks.GRASS || block == Blocks.DIRT || block == Blocks.GRAVEL)) ? Blocks.SAND : block, block.getMetaFromState(state));
                                    entityfallingblock.setPosition(k1 + 0.5D, l1 + 0.5D, i2 + 0.5D);
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
                                    if (world.getClosestPlayerToEntity(this, 16D) != null)
                                        world.destroyBlock(pos, true);
                                    else {
                                        world.setBlockToAir(pos);
                                        block.dropBlockAsItem(world, pos, state, 0);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void wreckInAABBToplessBlocks(AxisAlignedBB arg70972n1) {
        boolean flag = fetchKindSkeleton() == 1 && fetchVariantGiant() == 1;
        int i = MathHelper.floor(arg70972n1.minX);
        int j = MathHelper.floor(arg70972n1.minY);
        int k = MathHelper.floor(arg70972n1.minZ);
        int l = MathHelper.floor(arg70972n1.maxX);
        int i1 = MathHelper.floor(arg70972n1.maxY);
        int j1 = MathHelper.floor(arg70972n1.maxZ);
        for (int k1 = i; k1 <= l; k1++) {
            for (int l1 = j; l1 <= i1; l1++) {
                for (int i2 = k; i2 <= j1; i2++) {
                    BlockPos pos = new BlockPos(k1, l1, i2);
                    IBlockState state = world.getBlockState(pos);
                    Block block = state.getBlock();
                    IBlockState state1 = world.getBlockState(new BlockPos(k1, l1 + 1, i2));
                    if ((flag && block == Blocks.FIRE)) {
                        world.setBlockState(pos, Blocks.FIRE.getDefaultState());
                    }

                    else if (ticksExisted > 5 && arg70972n1 != null && world.isBlockLoaded(pos) && state.isOpaqueCube() && !state1.isOpaqueCube() && (!world.isRemote)) {
                        if (state.getBlockHardness(world, pos) != -1F) {
                            if (!world.getGameRules().getBoolean("doTileDrops") || state.getMaterial().isLiquid() || block == Blocks.FIRE || block == Blocks.WEB) {
                                world.setBlockToAir(pos);
                            }

                            else {
                                if (rand.nextInt(3) == 0) {
                                    EntityFallingBlockTitan entityfallingblock = new EntityFallingBlockTitan(world, k1 + 0.5D, l1 + 0.5D, i2 + 0.5D, (fetchKindSkeleton() == 0 && fetchVariantGiant() == 1 && (block == Blocks.GRASS || block == Blocks.DIRT || block == Blocks.GRAVEL)) ? Blocks.SAND : block, block.getMetaFromState(state));
                                    entityfallingblock.setPosition(k1 + 0.5D, l1 + 0.5D, i2 + 0.5D);
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
                                    if (world.getClosestPlayerToEntity(this, 16D) != null)
                                        world.destroyBlock(pos, true);
                                    else {
                                        world.setBlockToAir(pos);
                                        block.dropBlockAsItem(world, pos, state, 0);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isEntityUndead() {
        return true;
    }

    public EnumTitanStatus fetchConditionGiant() {
        return fetchKindSkeleton() == 1 ? EnumTitanStatus.GREATER : EnumTitanStatus.AVERAGE;
    }

    protected void entityInit() {
        super.entityInit();
        dataManager.register(KIND_SKELETON, Byte.valueOf((byte)0));
    }

    public int fetchStepModiferFoot() {
        return super.fetchStepModiferFoot();
    }

    protected String fetchLivingAudioGiant() {
        return stunnedIs || fetchWaiting() || getAnimID() == 13 ? null : (fetchKindSkeleton() == 1 ? "thetitans:titanWitherSkeletonLiving" : "thetitans:titanSkeletonLiving");
    }

    protected String fetchHurtAudioGiant() {
        return fetchKindSkeleton() == 1 ? "thetitans:titanWitherSkeletonGrunt" : "thetitans:titanSkeletonGrunt";
    }

    protected String fetchDeathAudioGiant() {
        return fetchKindSkeleton() == 1 ? "thetitans:titanWitherSkeletonDeath" : "thetitans:titanSkeletonDeath";
    }

    protected void funcA145780(int arg145780n1, int arg145780n2, int arg145780n3, Block arg145780n4) {
        triggerAudioGiant("thetitans:titanStep", 20.0F, 1.0F);
        triggerAudioGiant("mob.skeleton.step", 20.0F, 0.5F);
        shakeUserCamerasNearby(fetchKindSkeleton() == 1 ? 6D : 4D);
        if (!fetchWaiting() && getAnimID() != 13) {
            float f3 = rotationYaw * (float)Math.PI / 180.0F;
            float f11 = MathHelper.sin(f3);
            float f4 = MathHelper.cos(f3);
            if (keyFoot == 0) {
                if (fetchKindSkeleton() == 1 && fetchVariantGiant() == 1) {
                    for (int l = 0; l < fetchScaleMultiplierGiant(); l++) {
                        int i1 = MathHelper.floor(rightLeg.posX + (rand.nextFloat() - 0.5D) * rightLeg.width);
                        int j1 = MathHelper.floor(rightLeg.posY);
                        int k1 = MathHelper.floor(rightLeg.posZ + (rand.nextFloat() - 0.5D) * rightLeg.width);
                        BlockPos pos = new BlockPos(i1, j1, k1);
                        if ((world.getBlockState(pos).getMaterial() == Material.AIR) && (Blocks.FIRE.canPlaceBlockAt(world, pos))) {
                            world.setBlockState(pos, Blocks.FIRE.getDefaultState());
                        }
                    }
                }

                else {
                    destroyBlocksInAABB(rightLeg.getEntityBoundingBox().offset(0, -1, 0));
                }

                collideWithEntities(rightLeg, world.getEntitiesWithinAABBExcludingEntity(this, rightLeg.getEntityBoundingBox().grow(1.0D, 1.0D, 1.0D).offset((double)(f11 * 4F), 0, (double)(f4 * 4F))));
                ++keyFoot;
            }

            else {
                if (fetchKindSkeleton() == 1 && fetchVariantGiant() == 1) {
                    for (int l = 0; l < fetchScaleMultiplierGiant(); l++) {
                        int i1 = MathHelper.floor(leftLeg.posX + (rand.nextFloat() - 0.5D) * leftLeg.width);
                        int j1 = MathHelper.floor(leftLeg.posY);
                        int k1 = MathHelper.floor(leftLeg.posZ + (rand.nextFloat() - 0.5D) * leftLeg.width);
                        BlockPos pos = new BlockPos(i1, j1, k1);
                        if ((world.getBlockState(pos).getMaterial() == Material.AIR) && (Blocks.FIRE.canPlaceBlockAt(world, pos))) {
                            world.setBlockState(pos, Blocks.FIRE.getDefaultState());
                        }
                    }
                }

                else {
                    destroyBlocksInAABB(leftLeg.getEntityBoundingBox().offset(0, -1, 0));
                }

                collideWithEntities(leftLeg, world.getEntitiesWithinAABBExcludingEntity(this, leftLeg.getEntityBoundingBox().grow(1.0D, 1.0D, 1.0D).offset((double)(f11 * 4F), 0, (double)(f4 * 4F))));
                keyFoot = 0;
            }
        }
    }

    public boolean attackEntityAsMob(Entity arg70652n1) {
        return false;
    }

    public double getSpeed() {
        return fetchKindSkeleton() == 1 ? 0.3D / 6 : 0.25D / 6;
    }

    public void becomeSkeletonWither(boolean skelly) {
        if (skelly) {
            setSkeletonType(1);
            setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
        }
    }

    public boolean allowStrike() {
        return false;
    }

    public boolean needsMove() {
        return !stunnedIs && super.needsMove();
    }

    @SuppressWarnings("rawtypes")
    public void collideWithEntities(EntityTitanPart part, List arg70970n1) {
        super.collideWithEntities(part, arg70970n1);if (rand.nextInt(5) == 0 && fetchKindSkeleton() == 1 && part.isWet())
                SoundHandler.whenPlayMob("random.fizz", world, part, 1.0F, 1.0F);
    }

    public float fetchScaleMultiplierGiant() {
        return fetchKindSkeleton() == 1 ? super.fetchScaleMultiplierGiant() * 1.2F : super.fetchScaleMultiplierGiant();
    }

    public void onLivingUpdate() {
        float dis = fetchScaleMultiplierGiant();
        if (motionY > (fetchKindSkeleton() == 1 ? 1.5D : 3.0D))motionY *= 0.8D;
        float xfac = MathHelper.sin(renderYawOffset * (float)Math.PI / 180.0F);
        float zfac = MathHelper.cos(renderYawOffset * (float)Math.PI / 180.0F);
        if (!isRiding() && !fetchWaiting() && !stunnedIs && getAnimID() == 0) {
            flyTitan(0.625F * fetchScaleMultiplierGiant(), 0.125F * fetchScaleMultiplierGiant(), 0.25D * fetchScaleMultiplierGiant());
        }

        giantMelee = fetchKindSkeleton() == 1;
        if (fetchKindSkeleton() == 1 && fetchVariantGiant() == 1) {
            // PERF — ~3300 random block probes (dis^2) every tick. flameZ persists, so doing the same pass every
            // 4th tick fills the burning area the same way at a quarter of the cost.
            if (!world.isRemote && (ticksExisted + getEntityId()) % 4 == 0) {
                BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
                BlockPos.MutableBlockPos near = new BlockPos.MutableBlockPos();
                for (int l = 0; l < dis * dis; l++) {
                    int i = MathHelper.floor(posX + (rand.nextFloat() - 0.5D) * width * 10F);
                    int j = MathHelper.floor(posY + (rand.nextFloat() - 0.5D) * height * 2F);
                    int k = MathHelper.floor(posZ + (rand.nextFloat() - 0.5D) * width * 10F);
                    pos.setPos(i, j, k);
                    if (!world.isBlockLoaded(pos) || world.getBlockState(pos).getBlock() != Blocks.AIR)
                        continue;
                    near.setPos(i, j - 1, k);
                    if (world.getBlockState(near).getBlock().isFlammable(world, near, EnumFacing.UP))
                        world.setBlockState(pos.toImmutable(), Blocks.FIRE.getDefaultState());
                    else {
                        near.setPos(i, j + 1, k);
                        if (world.getBlockState(near).getBlock().isFlammable(world, near, EnumFacing.DOWN))
                            world.setBlockState(pos.toImmutable(), Blocks.FIRE.getDefaultState());
                    }
                }
            }
        }

        if (fetchWaiting()) {
            AnimationAPI.sendAnimPacket(this, 13);
            if (allowAwaken()) {
                awaken();
            }
        }

        else {
            if (getAnimID() != 13) {
                if (fetchKindSkeleton() == 1) {
                    switch (fetchVariantGiant()) {
                        default: {
                                enactAuraEffect(1, getEntityBoundingBox().grow(super.fetchScaleMultiplierGiant(), super.fetchScaleMultiplierGiant(), super.fetchScaleMultiplierGiant()));
                            }

                        case 1: {
                                enactAuraEffect(3, getEntityBoundingBox().grow(super.fetchScaleMultiplierGiant(), super.fetchScaleMultiplierGiant(), super.fetchScaleMultiplierGiant()));
                                break;
                            }

                        case 2: {
                                enactAuraEffect(4, getEntityBoundingBox().grow(super.fetchScaleMultiplierGiant(), super.fetchScaleMultiplierGiant(), super.fetchScaleMultiplierGiant()));
                                break;
                            }
                    }
                }

                else {
                    switch (fetchVariantGiant()) {
                        case 3: {
                                enactAuraEffect(3, getEntityBoundingBox().grow(super.fetchScaleMultiplierGiant(), super.fetchScaleMultiplierGiant(), super.fetchScaleMultiplierGiant()));
                                break;
                            }

                        case 4: {
                                enactAuraEffect(4, getEntityBoundingBox().grow(super.fetchScaleMultiplierGiant(), super.fetchScaleMultiplierGiant(), super.fetchScaleMultiplierGiant()));
                                break;
                            }
                    }
                }
            }

            if (getAnimID() == 13 && (getAnimTick() == 130 || getAnimTick() == 390 || getAnimTick() == 430 || getAnimTick() == 450)) {
                funcA145780(0, 0, 0, Blocks.STONE);
                triggerAudioGiant("thetitans:titanPress", getSoundVolume(), 1F);
            }
        }

        if (stunnedIs) {
            setAttackTarget(null);
            AnimationAPI.sendAnimPacket(this, 8);
            setAnimID(8);
        }

        if (getAnimID() == 8) {
            if (getAnimTick() == 70) {
                triggerAudioGiant("thetitans:groundSmash", 8.0F, 0.9F);
                triggerAudioGiant("thetitans:titanFall", 10.0F, 1.0F);
            }

            if (getAnimTick() == 74) {
                triggerAudioGiant("thetitans:distantLargeFall", 10000.0F, 1.0F);
                triggerAudioGiant(fetchHurtAudioGiant(), getSoundVolume(), getSoundPitch());
            }

            if (getAnimTick() == 480) {
                triggerAudioGiant("thetitans:titanFall", 10.0F, 1.0F);
                triggerAudioGiant("thetitans:titanStep", 10.0F, 1.0F);
                triggerAudioGiant("mob.skeleton.step", 10.0F, 0.5F);
                triggerAudioGiant("thetitans:titanStep", 10.0F, 1.0F);
                triggerAudioGiant("mob.skeleton.step", 10.0F, 0.5F);
                collideWithEntities(leftLeg, world.getEntitiesWithinAABBExcludingEntity(this, leftLeg.getEntityBoundingBox().grow(16.0D, 1.0D, 16.0D)));
                collideWithEntities(rightLeg, world.getEntitiesWithinAABBExcludingEntity(this, rightLeg.getEntityBoundingBox().grow(16.0D, 1.0D, 16.0D)));
                collideWithEntities(leftLeg, world.getEntitiesWithinAABBExcludingEntity(this, leftLeg.getEntityBoundingBox().grow(16.0D, 1.0D, 16.0D)));
                collideWithEntities(rightLeg, world.getEntitiesWithinAABBExcludingEntity(this, rightLeg.getEntityBoundingBox().grow(16.0D, 1.0D, 16.0D)));
            }

            if (getAnimTick() == 450) {
                stunnedIs = false;
            }

            else {
                setAttackTarget(null);
            }
        }

        if (getAnimID() == 10) {
            if ((getAnimTick() == 30) || (getAnimTick() == 70)) {
                triggerAudioGiant("thetitans:titanStep", 10.0F, 1.0F);
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

        if ((getAnimID() == 9) && (getAnimTick() <= 85) && (getAnimTick() >= 54)) {
            renderYawOffset = (rotationYaw = rotationYawHead);
            double d8 = getAnimTick() - 30;
            Vec3d vec3 = getLook(1.0F);
            double dx = vec3.x * d8;
            double dz = vec3.z * d8;
            world.addWeatherEffect(new EntityGammaLightning(world, posX + dx, posY + dis + 6D, posZ + dz, fetchKindSkeleton() == 1 ? 0F : 0.5F, fetchKindSkeleton() == 1 ? 0F : 0.5F, fetchKindSkeleton() == 1 ? 0F : 0.5F));
        }

        if (((getAnimID() == 9 && getAnimTick() == 90) || (getAnimID() == 5 && getAnimTick() == 230 + rand.nextInt(100))) && (getAttackTarget() != null)) {
            double d8 = 1.8D * fetchScaleMultiplierGiant();
            Vec3d vec3 = getLook(1.0F);
            double dx = vec3.x * d8;
            double dz = vec3.z * d8;
            float da = (float)fetchAmountStrike(10.0F);
            int i = fetchAmountKnockback();
            strikeMobChoosen(getAttackTarget(), da * 3.0F, i);
            getAttackTarget().motionY += 2.0F + rand.nextFloat();
            world.newExplosion(this, getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 6.0F, false, false);
            world.newExplosion(this, posX + dx, posY + dis + 4D, posZ + dz, 3.0F, false, false);
            getAttackTarget().attackEntityFrom(DamageSourceExtra.lightningBolt, da);
            world.addWeatherEffect(new EntityGammaLightning(world, posX + dx, posY + dis + 4D, posZ + dz, fetchKindSkeleton() == 1 ? 0F : 0.5F, fetchKindSkeleton() == 1 ? 0F : 0.5F, fetchKindSkeleton() == 1 ? 0F : 0.5F));
            world.addWeatherEffect(new EntityGammaLightning(world, posX + dx, posY + dis + 4D, posZ + dz, fetchKindSkeleton() == 1 ? 0F : 0.5F, fetchKindSkeleton() == 1 ? 0F : 0.5F, fetchKindSkeleton() == 1 ? 0F : 0.5F));
            world.addWeatherEffect(new EntityGammaLightning(world, posX + dx, posY + dis + 4D, posZ + dz, fetchKindSkeleton() == 1 ? 0F : 0.5F, fetchKindSkeleton() == 1 ? 0F : 0.5F, fetchKindSkeleton() == 1 ? 0F : 0.5F));
            world.addWeatherEffect(new EntityGammaLightning(world, posX + dx, posY + dis + 4D, posZ + dz, fetchKindSkeleton() == 1 ? 0F : 0.5F, fetchKindSkeleton() == 1 ? 0F : 0.5F, fetchKindSkeleton() == 1 ? 0F : 0.5F));
            world.addWeatherEffect(new EntityGammaLightning(world, getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, fetchKindSkeleton() == 1 ? 0F : 0.5F, fetchKindSkeleton() == 1 ? 0F : 0.5F, fetchKindSkeleton() == 1 ? 0F : 0.5F));
            for (int i1 = 0; i1 < 3; i1++) {
                List<?> found1 = world.getEntitiesWithinAABB(EntityLivingBase.class, getAttackTarget().getEntityBoundingBox().grow(16.0D, 16.0D, 16.0D), e -> e != getAttackTarget());
                if ((found1 != null) && (!found1.isEmpty())) {
                    for (int i11 = 0; i11 < found1.size(); i11++) {
                        Entity mobRef1 = (Entity)found1.get(i11);
                        if (((mobRef1 instanceof EntityLivingBase)) && allowStrikeGiant(mobRef1.getClass())) {
                            world.addWeatherEffect(new EntityGammaLightning(world, mobRef1.posX, mobRef1.posY, mobRef1.posZ, fetchKindSkeleton() == 1 ? 0F : 0.5F, fetchKindSkeleton() == 1 ? 0F : 0.5F, fetchKindSkeleton() == 1 ? 0F : 0.5F));
                            world.addWeatherEffect(new EntityGammaLightning(world, mobRef1.posX, mobRef1.posY, mobRef1.posZ, fetchKindSkeleton() == 1 ? 0F : 0.5F, fetchKindSkeleton() == 1 ? 0F : 0.5F, fetchKindSkeleton() == 1 ? 0F : 0.5F));
                            world.addWeatherEffect(new EntityGammaLightning(world, mobRef1.posX, mobRef1.posY, mobRef1.posZ, fetchKindSkeleton() == 1 ? 0F : 0.5F, fetchKindSkeleton() == 1 ? 0F : 0.5F, fetchKindSkeleton() == 1 ? 0F : 0.5F));
                            world.addWeatherEffect(new EntityGammaLightning(world, mobRef1.posX, mobRef1.posY, mobRef1.posZ, fetchKindSkeleton() == 1 ? 0F : 0.5F, fetchKindSkeleton() == 1 ? 0F : 0.5F, fetchKindSkeleton() == 1 ? 0F : 0.5F));
                            strikeMobChoosen(mobRef1, da, i);
                            mobRef1.motionY += 2D + rand.nextDouble();
                        }
                    }
                }
            }
        }

        if ((getAnimID() == 12 && getAnimTick() == 40)) {
            triggerAudioGiant("random.explode", 20.0F, 2.0F);
            triggerAudioGiant("mob.wither.shoot", 20.0F, 1.0F);
            float da = (float)fetchAmountStrike(1.0D);
            int i = fetchAmountKnockback();
            world.newExplosion(this, posX - (double)(xfac * (dis * 0.9)), posY + (dis * 1.25), posZ + (double)(zfac * (dis * 0.9)), 3.0F, false, false);
            world.addWeatherEffect(new EntityGammaLightning(world, posX - (double)(xfac * (dis * 0.9)), posY + (dis * 1.25), posZ + (double)(zfac * (dis * 0.9)), fetchKindSkeleton() == 1 ? 0F : 0.5F, fetchKindSkeleton() == 1 ? 0F : 0.5F, fetchKindSkeleton() == 1 ? 0F : 0.5F));
            if (getAttackTarget() != null) {
                strikeMobChoosen(getAttackTarget(), da, i);
                getAttackTarget().attackEntityFrom(DamageSourceExtra.lightningBolt, da);
                getAttackTarget().motionY += 1.0D + rand.nextDouble();
                if (getAttackTarget().width <= 8F)
                    world.newExplosion(this, getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 3.0F, false, false);
                world.addWeatherEffect(new EntityGammaLightning(world, getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, fetchKindSkeleton() == 1 ? 0F : 0.5F, fetchKindSkeleton() == 1 ? 0F : 0.5F, fetchKindSkeleton() == 1 ? 0F : 0.5F));
            }
        }

        if ((getAnimID() == 7) && (getAnimTick() == 92) && !AnimationAPI.isEffectiveClient()) {
            int y = MathHelper.floor(posY);
            int x = MathHelper.floor(posX - (double)(xfac * (dis * 2)));
            int z = MathHelper.floor(posZ + (double)(zfac * (dis * 2)));
            if (world.getBlockState(new BlockPos(x, y - 1, z)).getMaterial() != Material.AIR) {
                triggerAudioGiant("thetitans:titanStrike", 20.0F, 1.0F);
                triggerAudioGiant("thetitans:titanSlam", 20.0F, 1.0F);
                triggerAudioGiant("thetitans:titanPress", 100F, 1F);
            }

            for (int l1 = -4; l1 <= 4; l1++) {
                for (int i2 = -4; i2 <= 4; i2++) {
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

                        if (block.getExplosionResistance(this) > 500.0F && state.isOpaqueCube()) {
                            AnimationAPI.sendAnimPacket(this, 8);
                            setAnimID(8);
                            setAnimTick(0);
                            triggerAudioGiant("random.anvil_land", 20.0F, 0.5F);
                            stunnedIs = true;
                        }
                    }
                }
            }
        }

        if (stunnedIs || deathTicks > 0) {
            motionX *= 0.0D;
            motionZ *= 0.0D;
        }

        List<?> list = ticksExisted % 400 == 0 ? world.getEntitiesWithinAABB(net.minecraft.entity.projectile.EntityArrow.class, getEntityBoundingBox().grow(256.0D, 256.0D, 256.0D)) : java.util.Collections.emptyList();
        if (!list.isEmpty()) {
            for (int i1 = 0; i1 < list.size(); i1++) {
                Entity mobRef1 = (Entity)list.get(i1);
                if ((mobRef1 != null) && ((mobRef1 instanceof EntityArrow))) {
                    mobRef1.setDead();
                }
            }
        }

        if ((getAnimID() == 5 || getAnimID() == 11) && (getAnimTick() >= 40))
            attackTimer += 1;
        if (attackTimer < 0 || (getAnimID() != 5 && getAnimID() != 11) || (getAnimID() == 11 && getAnimTick() >= 80) || (getAnimID() == 5 && getAnimTick() >= 320))
            attackTimer = 0;
        if (!world.isRemote && (getAttackTarget() != null) && (getAnimID() == 5) && (getAnimTick() >= 100) && (getAnimTick() <= 300)) {
            attackEntityWithRangedAttack(getAttackTarget(), 1F);
            attackEntityWithRangedAttack(getAttackTarget(), 1F);
            attackEntityWithRangedAttack(getAttackTarget(), 1F);
            attackEntityWithRangedAttack(getAttackTarget(), 1F);
            attackEntityWithRangedAttack(getAttackTarget(), 1F);
            attackEntityWithRangedAttack(getAttackTarget(), 1F);
            attackEntityWithRangedAttack(getAttackTarget(), 1F);
            attackEntityWithRangedAttack(getAttackTarget(), 1F);
        }

        if (!world.isRemote && (getAttackTarget() != null) && (getAnimID() == 11) && (getAnimTick() == 78)) {
            strikeWithEnlargedRangedStrikeMob(getAttackTarget(), 1F);
        }

        if ((!AnimationAPI.isEffectiveClient()) && (getAttackTarget() != null) && (!stunnedIs) && ((getAnimID() == 0))) {
            double d0 = getDistance(getAttackTarget());
            if (d0 < fetchReachMelee()) {
                switch (rand.nextInt(6)) {
                    case 0:AnimationAPI.sendAnimPacket(this, 6);
                        setAnimID(6);
                        break;
                    case 1:if (getAttackTarget().height >= height - 12F || (posY + getEyeHeight() < getAttackTarget().posY + getAttackTarget().getEyeHeight())) {
                            AnimationAPI.sendAnimPacket(this, 1);
                            setAnimID(1);
                        }

                        else {
                            AnimationAPI.sendAnimPacket(this, 3);
                            setAnimID(3);
                        }

                        break;
                    case 2:AnimationAPI.sendAnimPacket(this, 7);
                        setAnimID(7);
                        break;
                    case 3:if (getAttackTarget().height >= height - 12F || (posY + getEyeHeight() < getAttackTarget().posY + getAttackTarget().getEyeHeight())) {
                            AnimationAPI.sendAnimPacket(this, 1);
                            setAnimID(1);
                        }

                        else {
                            AnimationAPI.sendAnimPacket(this, 2);
                            setAnimID(2);
                        }

                        break;
                    case 4:if (getAttackTarget().height >= height - 12F || (posY + getEyeHeight() < getAttackTarget().posY + getAttackTarget().getEyeHeight())) {
                            AnimationAPI.sendAnimPacket(this, 1);
                            setAnimID(1);
                        }

                        else {
                            AnimationAPI.sendAnimPacket(this, 4);
                            setAnimID(4);
                        }

                        break;
                }
            }

            else if (getAnimID() == 0 && (getRNG().nextInt(fetchKindSkeleton() == 1 ? 100 : 2) == 0)) {
                switch (rand.nextInt(6)) {
                    case 0:if (fetchKindSkeleton() != 1) {
                            if (rand.nextFloat() < 0.5F || (((getAttackTarget() instanceof EntityTitan)) || (getAttackTarget().height >= 6.0F) || (getAttackTarget().posY > posY + 6D))) {
                                AnimationAPI.sendAnimPacket(this, 11);
                                setAnimID(11);
                            }

                            else {
                                AnimationAPI.sendAnimPacket(this, 5);
                                setAnimID(5);
                            }
                        }

                        else {
                            AnimationAPI.sendAnimPacket(this, 9);
                            setAnimID(9);
                        }

                        break;
                    case 1:AnimationAPI.sendAnimPacket(this, 12);
                        setAnimID(12);
                        break;
                    case 2:if (fetchKindSkeleton() != 1) {
                            AnimationAPI.sendAnimPacket(this, 11);
                            setAnimID(11);
                        }

                        else {
                            AnimationAPI.sendAnimPacket(this, 12);
                            setAnimID(12);
                        }
                }
            }
        }

        super.onLivingUpdate();
    }

    public String getName() {
        if (fetchKindSkeleton() == 1) {
            switch (fetchVariantGiant()) {
                case 1:
                    return I18n.translateToLocal("entity.WitherSkeletonTitanSolar.name");
                case 2:
                    return I18n.translateToLocal("entity.WitherSkeletonTitanReaper.name");
                default:
                    return I18n.translateToLocal("entity.WitherSkeletonTitan.name");
            }
        }

        else {
            switch (fetchVariantGiant()) {
                case 1:
                    return I18n.translateToLocal("entity.SkeletonTitanSand.name");
                case 2:
                    return I18n.translateToLocal("entity.SkeletonTitanGuitar.name");
                case 3:
                    return I18n.translateToLocal("entity.SkeletonTitanSleep.name");
                case 4:
                    return I18n.translateToLocal("entity.SkeletonTitanVoid.name");
                default:
                    return I18n.translateToLocal("entity.SkeletonTitan.name");
            }
        }
    }

    @Override
    public double fetchAmountStrike(double baseDamage) {
        if (fetchKindSkeleton() == 1) {
            switch (fetchVariantGiant()) {
                case 1: return super.fetchAmountStrike(baseDamage) + (double)(specialMinionsNum * 180000.0F);
                case 2: return super.fetchAmountStrike(baseDamage) + (double)(specialMinionsNum * 240000.0F);
                default: return super.fetchAmountStrike(baseDamage) + (double)(specialMinionsNum * 120000.0F);
            }
        }

        return super.fetchAmountStrike(baseDamage);
    }

    public void strikeMobChoosen(Entity damagedEntity, float damage, int knockbackAmount) {
        super.strikeMobChoosen(damagedEntity, damage, knockbackAmount);
        if (((fetchKindSkeleton() == 1) && (damagedEntity instanceof EntityLivingBase) && !(damagedEntity instanceof EntityEnderColossusCrystal))) {
            ((EntityLivingBase)damagedEntity).addPotionEffect(new PotionEffect(MobEffects.WITHER, 800, 3));
            ((EntityLivingBase)damagedEntity).addPotionEffect(new PotionEffect(CommonProxy.advancedWither, 100, 3));
        }
    }

    public void updateRidden() {
        super.updateRidden();
        if ((getRidingEntity() instanceof EntitySpiderTitan)) {
            EntitySpiderTitan entitycreature = (EntitySpiderTitan)getRidingEntity();
            renderYawOffset = entitycreature.renderYawOffset;
            if (getAttackTarget() != null) {
                entitycreature.setAttackTarget(getAttackTarget());
            }

            if (!entitycreature.isEntityAlive()) {
                dismountRidingEntity();
            }
        }
    }

    public String fetchAchievement() {
        if ((getRidingEntity() != null) && ((getRidingEntity() instanceof EntitySpiderTitan))) {
            return TitanTrophies.spiderjockeytitan;
        }

        if (fetchKindSkeleton() == 1) {
            return TitanTrophies.witherskeletontitan;
        }

        return TitanTrophies.skeletontitan;
    }

    protected Item getDropItem() {
        return Items.ARROW;
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        if (deathTicks > 0) {
            if (fetchKindSkeleton() == 1) {
                for (int x = 0; x < 70; x++) {
                    EntityXPBomb entitylargefireball = new EntityXPBomb(world, posX, posY + 4D, posZ);
                    entitylargefireball.setPosition(posX, posY + 4D, posZ);
                    ++entitylargefireball.motionY;
                    entitylargefireball.assignTotalXP(24000);
                    world.spawnEntity(entitylargefireball);
                }

                for (int x = 0; x < 1; x++) {
                    EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                    itembomb.assignStackStackMob(new ItemStack(Items.DIAMOND));
                    itembomb.assignTotalStack(16);
                    world.spawnEntity(itembomb);
                }

                for (int x = 0; x < 2; x++) {
                    EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                    itembomb.assignStackStackMob(new ItemStack(Items.IRON_INGOT));
                    itembomb.assignTotalStack(16);
                    world.spawnEntity(itembomb);
                }

                for (int x = 0; x < 16; x++) {
                    EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                    itembomb.assignStackStackMob(new ItemStack(Items.BONE));
                    itembomb.assignTotalStack(32 + rand.nextInt(32 + arg70628n2) + arg70628n2);
                    world.spawnEntity(itembomb);
                }

                for (int x = 0; x < 12; x++) {
                    EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                    itembomb.assignStackStackMob(new ItemStack(Items.DYE, 1, 15));
                    itembomb.assignTotalStack(32 + rand.nextInt(32 + arg70628n2) + arg70628n2);
                    world.spawnEntity(itembomb);
                }

                for (int x = 0; x < 16; x++) {
                    EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                    itembomb.assignStackStackMob(new ItemStack(Blocks.COAL_BLOCK));
                    itembomb.assignTotalStack(32 + rand.nextInt(32 + arg70628n2) + arg70628n2);
                    world.spawnEntity(itembomb);
                }

                for (int x = 0; x < 12; x++) {
                    EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                    itembomb.assignStackStackMob(new ItemStack(Blocks.IRON_BLOCK));
                    itembomb.assignTotalStack(32 + rand.nextInt(32 + arg70628n2) + arg70628n2);
                    world.spawnEntity(itembomb);
                }

                for (int x = 0; x < 8; x++) {
                    EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                    itembomb.assignStackStackMob(new ItemStack(Blocks.GOLD_BLOCK));
                    itembomb.assignTotalStack(32 + rand.nextInt(32 + arg70628n2) + arg70628n2);
                    world.spawnEntity(itembomb);
                }

                for (int x = 0; x < 4; x++) {
                    EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                    itembomb.assignStackStackMob(new ItemStack(Blocks.EMERALD_BLOCK));
                    itembomb.assignTotalStack(32 + rand.nextInt(32 + arg70628n2) + arg70628n2);
                    world.spawnEntity(itembomb);
                }

                for (int x = 0; x < 4; x++) {
                    EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                    itembomb.assignStackStackMob(new ItemStack(Blocks.DIAMOND_BLOCK));
                    itembomb.assignTotalStack(32 + rand.nextInt(32 + arg70628n2) + arg70628n2);
                    world.spawnEntity(itembomb);
                }

                for (int x = 0; x < 2; x++) {
                    EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                    itembomb.assignStackStackMob(new ItemStack(TitanBlockSet.harcadium_block));
                    itembomb.assignTotalStack(32 + rand.nextInt(32 + arg70628n2) + arg70628n2);
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
            }

            else {
                for (int x = 0; x < 16; x++) {
                    EntityXPBomb entitylargefireball = new EntityXPBomb(world, posX, posY + 4D, posZ);
                    entitylargefireball.setPosition(posX, posY + 4D, posZ);
                    ++entitylargefireball.motionY;
                    entitylargefireball.assignTotalXP(14000);
                    world.spawnEntity(entitylargefireball);
                }

                for (int x = 0; x < 8; x++) {
                    EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                    itembomb.assignStackStackMob(new ItemStack(Items.BONE));
                    itembomb.assignTotalStack(16 + rand.nextInt(16 + arg70628n2) + arg70628n2);
                    world.spawnEntity(itembomb);
                }

                for (int x = 0; x < 8; x++) {
                    EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                    itembomb.assignStackStackMob(new ItemStack(Items.ARROW));
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
    }

    protected void shedDropRare(int arg70600n1) {
        if (fetchKindSkeleton() == 1) {
            for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.SKULL, 256, 1));
                itembomb.assignTotalStack(256);
                world.spawnEntity(itembomb);
            }
        }

        else {
            for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.SKULL, 256, 0));
                itembomb.assignTotalStack(256);
                world.spawnEntity(itembomb);
            }
        }
    }

    protected void appendArmourRandom() {
        if (fetchKindSkeleton() == 1) {
            setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
        }

        else {
            setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        }
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
        // cosmetic ranged-attack arm swing; titan has no dedicated flag, no-op
    }

    public IEntityLivingData onInitialSpawn(net.minecraft.world.DifficultyInstance difficulty, IEntityLivingData arg180482n2) {
        arg180482n2 = super.onInitialSpawn(difficulty, arg180482n2);
        setCanPickUpLoot(true);
        setEnchantmentBasedOnDifficulty(difficulty);
        assignWaiting(true);
        if ((((world.provider instanceof WorldProviderHell)) && (getRNG().nextInt(5) > 0) && (!world.isRemote)) || ((beWitherSkeletonNeeds) && (!world.isRemote))) {
            setSkeletonType(1);
        }

        else {
            setSkeletonType(0);
        }

        if (getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
            Calendar calendar = world.getCurrentDate();
            if ((calendar.get(2) + 1 == 10) && (calendar.get(5) == 31) && (rand.nextFloat() < 0.25F)) {
                setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(rand.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
                setDropChance(EntityEquipmentSlot.HEAD, 0.0F);
            }
        }

        appendArmourRandom();
        return arg180482n2;
    }

    public void setCombatTask() {
        giantMelee = fetchKindSkeleton() == 1;
    }

    public void attackEntityWithRangedAttack(EntityLivingBase arg82196n1, float arg82196n2) {
        renderYawOffset = rotationYaw = rotationYawHead;
        faceEntity(arg82196n1, 180.0F, 30.0F);
        float dis = 0.625F * fetchScaleMultiplierGiant();
        float xfac = MathHelper.sin(renderYawOffset * (float)Math.PI / 180.0F);
        float zfac = MathHelper.cos(renderYawOffset * (float)Math.PI / 180.0F);
        EntityHarcadiumArrow entityarrow = new EntityHarcadiumArrow(world, this, arg82196n2);
        double d0 = arg82196n1.posX - (posX - (double)(xfac * dis));
        double d1 = arg82196n1.posY - (posY + (1.175D * fetchScaleMultiplierGiant()));
        double d2 = arg82196n1.posZ - (posZ + (double)(zfac * dis));
        float f1 = MathHelper.sqrt(d0 * d0 + d2 * d2);
        entityarrow.assignHeadingThrowable(d0, d1 + f1, d2, f1 / (180F / (float)Math.PI) * 1.75F, 1F + (rand.nextFloat() * 36F));
        entityarrow.posX = (posX - (double)(xfac * dis));
        entityarrow.posY = (posY + (1.175D * fetchScaleMultiplierGiant()));
        entityarrow.posZ = (posZ + (double)(zfac * dis));
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, getHeldItemMainhand());
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, getHeldItemMainhand());
        int f = EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, getHeldItemMainhand());
        entityarrow.setDamage(30.0D);
        entityarrow.setIsCritical(true);
        if (i > 0)
            entityarrow.setDamage(entityarrow.getDamage() + i * 10.0D + 1.0D);
        if (j > 1)
            entityarrow.setKnockbackStrength(j);
        else
            entityarrow.setKnockbackStrength(2);
        if ((f > 0) || (fetchKindSkeleton() == 1))
            entityarrow.setFire(10000);
        world.spawnEntity(entityarrow);
        triggerAudioGiant("random.bow", 3.0F, 1.9F / (getRNG().nextFloat() * 0.4F + 0.8F));
        if (getDistance(arg82196n1) <= fetchReachMelee()) {
            strikeMobChoosen(arg82196n1, 10F, 5);
        }
    }

    public void strikeWithEnlargedRangedStrikeMob(EntityLivingBase arg82196n1, float arg82196n2) {
        renderYawOffset = rotationYaw = rotationYawHead;
        faceEntity(arg82196n1, 180.0F, 30.0F);
        float dis = 0.625F * fetchScaleMultiplierGiant();
        float xfac = MathHelper.sin(renderYawOffset * (float)Math.PI / 180.0F);
        float zfac = MathHelper.cos(renderYawOffset * (float)Math.PI / 180.0F);
        double d0 = arg82196n1.posX - (posX - (double)(xfac * dis));
        double d1 = (arg82196n1.posY + arg82196n1.getEyeHeight()) - (posY + (1.125D * fetchScaleMultiplierGiant()));
        double d2 = arg82196n1.posZ - (posZ + (double)(zfac * dis));
        EntitySkeletonTitanGiantArrow entityarrow = new EntitySkeletonTitanGiantArrow(world, this, d0, d1, d2);
        MathHelper.sqrt(d0 * d0 + d2 * d2);
        entityarrow.posX = (posX - (double)(xfac * dis));
        entityarrow.posY = (posY + (1.1D * fetchScaleMultiplierGiant()));
        entityarrow.posZ = (posZ + (double)(zfac * dis));
        if (isArmored())
            entityarrow.assignKindArrow(1);
        if (fetchVariantGiant() == 4)
            entityarrow.assignKindArrow(2);
        world.spawnEntity(entityarrow);
        triggerAudioGiant("random.bow", 20F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
        if (getDistance(arg82196n1) <= fetchReachMelee()) {
            strikeMobChoosen(arg82196n1, 10F, 5);
        }
    }

    public int fetchKindSkeleton() {
        return dataManager.get(KIND_SKELETON);
    }

    public EntitySkeletonTitan setSkeletonType(int arg82201n1) {
        dataManager.set(KIND_SKELETON, Byte.valueOf((byte)arg82201n1));
        return this;
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.hasKey("SkeletonType", 99)) {
            byte b0 = tagCompund.getByte("SkeletonType");
            setSkeletonType(b0);
        }

        setCombatTask();
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setByte("SkeletonType", (byte)fetchKindSkeleton());
    }

    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
        super.setItemStackToSlot(slotIn, stack);
        if ((!world.isRemote) && (slotIn == EntityEquipmentSlot.MAINHAND)) {
            setCombatTask();
        }
    }

    public float getEyeHeight() {
        return 0.8625F * height;
    }

    protected void updateAITasks() {
        super.updateAITasks();
    }

    public double getYOffset() {
        if (fetchKindSkeleton() == 1) {
            return super.getYOffset() - 12.48D;
        }

        return super.getYOffset() - 10.4D;
    }

    protected void inactActionDeath() {
        if (!world.isRemote) {
            triggerAudioGiant("mob.skeleton.death", getSoundVolume(), getSoundPitch());
            if ((world.getGameRules().getBoolean("doMobLoot"))) {
                dropFewItems(true, 0);
                dropEquipment(true, 0);
                shedDropRare(1);
            }

            EntityTitanSpirit entitytitan = new EntityTitanSpirit(world);
            entitytitan.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
            world.spawnEntity(entitytitan);
            entitytitan.assignHuntingVessel(false);
            if (fetchKindSkeleton() == 1) {
                entitytitan.assignKindSpirit(5);
            }

            else {
                entitytitan.assignKindSpirit(4);
            }
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
            float f = (rand.nextFloat() - 0.5F) * 24.0F;
            float f1 = (rand.nextFloat() - 0.5F) * 8.0F;
            float f2 = (rand.nextFloat() - 0.5F) * 24.0F;
            world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX + f, posY + 2.0D + f1, posZ + f2, 0.0D, 0.0D, 0.0D);
        }

        if (getInvulTime() >= (fetchKindSkeleton() == 1 ? 1560 : fetchHoldThreash())) {
            setDead();
        }
    }

    public boolean strikeFromSkeleton(DamageSource source, float amount) {
        if (isEntityInvulnerable(source)) {
            return false;
        }

        else if (((source.getTrueSource() instanceof EntitySkeletonMinion)) || ((getRidingEntity() != null) && (source.getTrueSource() == getRidingEntity()) && ((getRidingEntity() instanceof EntitySpiderTitan))) || ((source.getTrueSource() instanceof EntitySkeletonTitan)) || ((source.getTrueSource() instanceof EntityWitherMinion))) {
            return false;
        }

        else {
            recentlyHit = 200;
            Entity entity = source.getTrueSource();
            if (((entity instanceof EntityLivingBase)) && (!isEntityInvulnerable(source)) && (amount > 25.0F)) {
                List<?> list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(256.0D, 256.0D, 256.0D));
                for (int i = 0; i < list.size(); i++) {
                    Entity mobRef1 = (Entity)list.get(i);
                    if ((mobRef1 instanceof EntitySkeletonTitan)) {
                        EntitySkeletonTitan entitypigzombie = (EntitySkeletonTitan)mobRef1;
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
        if (source.getTrueSource() instanceof EntityPlayer && !world.isRemote) {
            setAttackTarget((EntityLivingBase) source.getTrueSource());
            setRevengeTarget((EntityLivingBase) source.getTrueSource());
        }

        if (isEntityInvulnerable(source)) {
            return false;
        }

        else if (((source.getTrueSource() instanceof EntitySkeletonMinion)) || ((getRidingEntity() != null) && (source.getTrueSource() == getRidingEntity()) && ((getRidingEntity() instanceof EntitySpiderTitan))) || ((source.getTrueSource() instanceof EntitySkeletonTitan)) || ((source.getTrueSource() instanceof EntityWitherMinion))) {
            return false;
        }

        else if (fetchKindSkeleton() == 1 && source.isFireDamage()) {
            heal(amount);
            return false;
        }

        else {
            recentlyHit = 200;
            Entity entity = source.getTrueSource();
            if (((entity instanceof EntityLivingBase)) && (!isEntityInvulnerable(source)) && (amount > 25.0F)) {
                List<?> list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(256.0D, 256.0D, 256.0D));
                for (int i = 0; i < list.size(); i++) {
                    Entity mobRef1 = (Entity)list.get(i);
                    if ((mobRef1 instanceof EntitySkeletonTitan)) {
                        EntitySkeletonTitan entitypigzombie = (EntitySkeletonTitan)mobRef1;
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

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean attackEntityFromPart(EntityTitanPart arg70965n1, DamageSource source, float amount) {
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
        return new EntitySkeletonMinion(world);
    }

    protected double cap() {
        return super.cap();
    }
}
