package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thehippomaster.AnimationAPI.IAnimatedEntity;
import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;
import com.electrifiedded.thetitans.entity.titan.anim.gargoyletitan.AnimationGargoyleTitanAntiTitanAttack;
import com.electrifiedded.thetitans.entity.titan.anim.gargoyletitan.AnimationGargoyleTitanAttack1;
import com.electrifiedded.thetitans.entity.titan.anim.gargoyletitan.AnimationGargoyleTitanAttack2;
import com.electrifiedded.thetitans.entity.titan.anim.gargoyletitan.AnimationGargoyleTitanAttack3;
import com.electrifiedded.thetitans.entity.titan.anim.gargoyletitan.AnimationGargoyleTitanAttack4;
import com.electrifiedded.thetitans.entity.titan.anim.gargoyletitan.AnimationGargoyleTitanLavaSpit;
import com.electrifiedded.thetitans.entity.titan.anim.gargoyletitan.AnimationGargoyleTitanMeteorRain;
import com.electrifiedded.thetitans.entity.titan.anim.gargoyletitan.AnimationGargoyleTitanWaterSpout;
import com.electrifiedded.thetitans.entity.titan.anim.gargoyletitan.AnimationGargoyleTitanWingBuffet;
import com.electrifiedded.thetitans.registry.TitanGear;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.base.Predicate;

import java.util.List;
import javax.annotation.Nullable;

public class EntityGargoyleTitan extends EntityTitan implements IAnimatedEntity {
    private static final DataParameter<Byte> PLAYER_CREATED = EntityDataManager.<Byte>createKey(EntityGargoyleTitan.class, DataSerializers.BYTE);

    private static final Predicate<Entity> mobSelectorStrike = new Predicate<Entity>() {
        public boolean apply(@Nullable Entity arg180027n1) {
            return (arg180027n1 != null) && (!(arg180027n1 instanceof EntityGargoyleTitan)) && (!(arg180027n1 instanceof EntityIronGolemTitan)) && (!(arg180027n1 instanceof EntitySnowGolemTitan)) && (!(arg180027n1 instanceof EntityWitherTurret)) && (!(arg180027n1 instanceof EntityWitherTurretGround));
        }
    };

    public EntityGargoyleTitan(World worldIn) {
        super(worldIn);
        giantMelee = true;
        setSize(32F, 74F);
        ((net.minecraft.pathfinding.PathNavigateGround)getNavigator()).setCanSwim(false);
        targetTasks.addTask(2, new EntityAIHurtByTarget(this, true));
        targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, IMob.MOB_SELECTOR));
        appendTargetingTaskToMobGiant(this);
        tasks.addTask(1, new AnimationGargoyleTitanLavaSpit(this));
        tasks.addTask(1, new AnimationGargoyleTitanWaterSpout(this));
        tasks.addTask(1, new AnimationGargoyleTitanWingBuffet(this));
        tasks.addTask(1, new AnimationGargoyleTitanMeteorRain(this));
        tasks.addTask(1, new AnimationGargoyleTitanAttack4(this));
        tasks.addTask(1, new AnimationGargoyleTitanAttack3(this));
        tasks.addTask(1, new AnimationGargoyleTitanAttack2(this));
        tasks.addTask(1, new AnimationGargoyleTitanAttack1(this));
        tasks.addTask(1, new AnimationGargoyleTitanAntiTitanAttack(this));
    }

    protected void whenUpdateBounds() {
        if (ticksExisted % 5 == 0) {
            setSize(32F, 74F);
        }

        super.whenUpdateBounds();
    }

    @Override
    public double fetchAmountHp() {
        return super.fetchAmountHp() * 60.0D;
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }

    public float getBrightness() {
        return 1.0F;
    }

    public static void appendTargetingTaskToMobGiant(EntityCreature entity) {
        entity.targetTasks.addTask(0, new EntityAINearestTargetTitan(entity, EntityTitan.class, 0, false, false, mobSelectorStrike));
    }

    public float getEyeHeight() {
        return 0.9189189189189189F * height;
    }

    public float fetchScaleMultiplierGiant() {
        return 32F;
    }

    public void onKillCommand() {
        triggerAudioGiant(fetchDeathAudioGiant(), getSoundVolume(), getSoundPitch());
        setDead();
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(PLAYER_CREATED, Byte.valueOf((byte)0));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    public EnumTitanStatus fetchConditionGiant() {
        return EnumTitanStatus.UTILITY;
    }

    protected int decreaseAirSupply(int arg70682n1) {
        return arg70682n1;
    }

    public int fetchSpawnRateMinion() {
        return TheTitans.kingServantSpawnrateGargoyle;
    }

    public void fall(float arg70069n1, float damageMultiplier) {
        float[] fallRet = ForgeHooks.onLivingFall(this, arg70069n1, damageMultiplier);
        if (fallRet == null) return;
        arg70069n1 = fallRet[0];
        damageMultiplier = fallRet[1];
        if (arg70069n1 <= 0.0F) return;
        super.fall(arg70069n1, damageMultiplier);
        PotionEffect potioneffect = getActivePotionEffect(MobEffects.JUMP_BOOST);
        float f1 = potioneffect != null ? potioneffect.getAmplifier() + 1 : 0.0F;
        int i = MathHelper.ceil(arg70069n1 - 20F - f1);
        if (i > 0) {
            playStepSound(new BlockPos(0, 0, 0), Blocks.STONE);
            List<?> found11 = world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(48.0D, 2.0D, 48.0D), e -> e != this);
            if ((found11 != null) && (!found11.isEmpty())) {
                for (int i1 = 0; i1 < found11.size(); i1++) {
                    Entity entity = (Entity)found11.get(i1);
                    if (((entity instanceof EntityLivingBase)) && allowStrikeGiant(entity.getClass()) && (!(entity instanceof EntityTitan))) {
                        float smash = 100F - getDistance(entity);
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

    public boolean hasSoulNo() {
        return true;
    }

    public String getName() {
        return "§l" + super.getName();
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if ((getAttackTarget() != null) && getAnimID() == 0 && posY <= getAttackTarget().posY + 16D && getDistance(getAttackTarget()) > (!getAttackTarget().onGround ? fetchReachMelee() : 6400D))
            motionY += 0.6D - motionY;
        if (!world.isRemote && getAnimID() == 5 && getAnimTick() == 30) {
            if ((getAttackTarget() != null)) {
                int i1 = MathHelper.floor(getAttackTarget().posX);
                int i = MathHelper.floor(getAttackTarget().posY);
                int j1 = MathHelper.floor(getAttackTarget().posZ);
                for (int l1 = -16; l1 <= 16; l1++) {
                    for (int i2 = -16; i2 <= 16; i2++) {
                        for (int j = 0; j <= 1; j++) {
                            int j2 = i1 + l1;
                            int k = i + j;
                            int l = j1 + i2;
                            for (int y = 0; y <= 256 && world.getBlockState(new BlockPos(j2, k - 1, l)).getMaterial() == Material.AIR; y++)
                                --k;
                            BlockPos pos = new BlockPos(j2, k, l);
                            Block block = world.getBlockState(pos).getBlock();
                            if (world.getBlockState(pos).getMaterial() == Material.AIR) {
                                world.setBlockState(pos, Blocks.FLOWING_WATER.getStateFromMeta(7), 3);
                            }
                        }
                    }
                }

                strikeMobChoosen(getAttackTarget(), (float)fetchAmountStrike(2.0D), 3);
            }
        }

        if (!world.isRemote && getAnimID() == 6 && getAnimTick() == 20) {
            if ((getAttackTarget() != null)) {
                double d8 = 10D;
                Vec3d vec3 = getLook(1.0F);
                double dx = vec3.x * d8;
                double dz = vec3.z * d8;
                double d5 = (getAttackTarget().posX) - (posX + dx);
                double d6 = getAttackTarget().posY - (posY + 28D);
                double d7 = (getAttackTarget().posZ) - (posZ + dz);
                EntityLavaSpit entitylargefireball = new EntityLavaSpit(world, this, d5, d6, d7);
                entitylargefireball.setPosition(posX + dx, posY + 28D, posZ + dz);
                world.spawnEntity(entitylargefireball);
            }
        }

        if (!world.isRemote && getAnimID() == 2 && getAnimTick() > 60) {
            if ((getAttackTarget() != null)) {
                for (int i = 0; i < 2; i++) {
                    double ranX = rand.nextGaussian() * 100;
                    double ranZ = rand.nextGaussian() * 100;
                    double ranTargetX = rand.nextGaussian() * 16;
                    double ranTargetZ = rand.nextGaussian() * 16;
                    double d5 = (getAttackTarget().posX + ranTargetX) - (posX + ranX);
                    double d6 = getAttackTarget().posY - (200D);
                    double d7 = (getAttackTarget().posZ + ranTargetZ) - (posZ + ranZ);
                    EntityGargoyleTitanFireball entitylargefireball = new EntityGargoyleTitanFireball(world, this, d5, d6, d7);
                    entitylargefireball.setPosition(posX + ranX, 200D, posZ + ranZ);
                    world.spawnEntity(entitylargefireball);
                }
            }
        }

        if (!world.isRemote && getAnimID() == 4 && getAnimTick() > 20) {
            if ((getAttackTarget() != null)) {
                for (int i = 0; i < 2; i++) {
                    double ranX = rand.nextGaussian() * 100;
                    double ranZ = rand.nextGaussian() * 100;
                    double ranTargetX = rand.nextGaussian() * 16;
                    double ranTargetZ = rand.nextGaussian() * 16;
                    double d5 = (getAttackTarget().posX + ranTargetX) - (posX + ranX);
                    double d6 = getAttackTarget().posY - (200D);
                    double d7 = (getAttackTarget().posZ + ranTargetZ) - (posZ + ranZ);
                    EntityGargoyleTitanFireball entitylargefireball = new EntityGargoyleTitanFireball(world, this, d5, d6, d7);
                    entitylargefireball.setPosition(posX + ranX, 200D, posZ + ranZ);
                    world.spawnEntity(entitylargefireball);
                }
            }
        }

        if ((!AnimationAPI.isEffectiveClient()) && (getAttackTarget() != null) && (getAnimID() == 0)) {
            double d0 = getDistance(getAttackTarget());
            if (d0 < fetchReachMelee()) {
                switch (rand.nextInt(6)) {
                    case 0:AnimationAPI.sendAnimPacket(this, 3);
                        setAnimID(3);
                        break;
                    case 1:AnimationAPI.sendAnimPacket(this, 7);
                        setAnimID(7);
                        break;
                    case 2:AnimationAPI.sendAnimPacket(this, 8);
                        setAnimID(8);
                        break;
                    case 3:AnimationAPI.sendAnimPacket(this, 9);
                        setAnimID(9);
                        break;
                    case 4:if (((getAttackTarget() instanceof EntityTitan)) || (getAttackTarget().height >= 6.0F) || (getAttackTarget().posY > posY + 6D)) {
                            if (rand.nextInt(7) == 0) {
                                AnimationAPI.sendAnimPacket(this, 2);
                                setAnimID(2);
                            }

                            else {
                                AnimationAPI.sendAnimPacket(this, 1);
                                setAnimID(1);
                            }
                        }

                        else {
                            AnimationAPI.sendAnimPacket(this, 7);
                            setAnimID(7);
                        }

                        break;
                }
            }

            else if ((getAnimID() == 0) && (getRNG().nextInt(60) == 0)) {
                switch (rand.nextInt(2)) {
                    case 0:if (getAttackTarget() instanceof EntityTitan) {
                            AnimationAPI.sendAnimPacket(this, 4);
                            setAnimID(4);
                        }

                        else {
                            AnimationAPI.sendAnimPacket(this, 5);
                            setAnimID(5);
                        }

                        break;
                    case 1:AnimationAPI.sendAnimPacket(this, 6);
                        setAnimID(6);
                        break;
                }
            }
        }

        List<?> found1 = rand.nextInt(60) == 0 ? world.getEntitiesWithinAABB(EntityGargoyle.class, getEntityBoundingBox().grow(256.0D, 256.0D, 256.0D)) : java.util.Collections.emptyList();
        if (!found1.isEmpty()) {
            for (int i1 = 0; i1 < found1.size(); i1++) {
                Entity entity = (Entity)found1.get(i1);
                if ((entity != null) && ((entity instanceof EntityGargoyle))) {
                    if (getAttackTarget() != null && getAttackTarget().height <= 6F) {
                        ((EntityGargoyle)entity).setAttackTarget(getAttackTarget());
                        ((EntityGargoyle)entity).getLookHelper().setLookPositionWithEntity(getAttackTarget(), 30.0F, 30.0F);
                        ((EntityGargoyle)entity).getMoveHelper().setMoveTo(getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 1.0D);
                        ((EntityGargoyle)entity).getNavigator().tryMoveToXYZ(getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 1.0D);
                    }
                }
            }
        }
    }

    protected void updateAITasks() {
        super.updateAITasks();
    }

    @SuppressWarnings("rawtypes")
    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        return (arg70686n1 == EntityGargoyle.class) || (arg70686n1 == EntityGargoyleTitan.class) ? false : (isPlayerCreated() && EntityPlayer.class.isAssignableFrom(arg70686n1) ? false : super.allowStrikeGiant(arg70686n1));
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("PlayerCreated", isPlayerCreated());
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        setPlayerCreated(tagCompund.getBoolean("PlayerCreated"));
    }

    public boolean attackEntityAsMob(Entity arg70652n1) {
        return false;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source)) {
            return false;
        }

        if (source.isExplosion()) {
            return false;
        }

        if ((source.getTrueSource() != null) && ((source.getTrueSource() instanceof EntityPlayer)) && (isPlayerCreated())) {
            return false;
        }

        return super.attackEntityFrom(source, amount);
    }

    protected String fetchLivingAudioGiant() {
        return "thetitans:gargoyleLiving";
    }

    protected String fetchHurtAudioGiant() {
        return "thetitans:gargoyleGrunt";
    }

    protected String fetchDeathAudioGiant() {
        return "thetitans:gargoyleDeath";
    }

    protected float getSoundPitch() {
        return isChild() ? (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F : (rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.5F;
    }

    protected void playStepSound(BlockPos pos, Block block) {
        if (onGround) {
            triggerAudioGiant("mob.irongolem.walk", 10.0F, 0.5F);
            triggerAudioGiant("mob.irongolem.walk", 10.0F, 0.5F);
            triggerAudioGiant("mob.irongolem.walk", 10.0F, 0.5F);
            triggerAudioGiant("mob.irongolem.walk", 10.0F, 0.5F);
            triggerAudioGiant("mob.irongolem.walk", 10.0F, 0.5F);
            triggerAudioGiant("mob.irongolem.walk", 10.0F, 0.5F);
            triggerAudioGiant("mob.irongolem.walk", 10.0F, 0.5F);
            triggerAudioGiant("mob.irongolem.walk", 10.0F, 0.5F);
            triggerAudioGiant("thetitans:titanStep", 10.0F, 1.0F);
            shakeUserCamerasNearby(6D);
        }
    }

    protected float getSoundVolume() {
        return 100.0F;
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        for (int l = 0; l < 512 + rand.nextInt(512 + arg70628n2); l++) {
            entityDropItem(new ItemStack(Blocks.STONE), 12.0F);
        }

        for (int l = 0; l < 32 + rand.nextInt(96 + arg70628n2); l++) {
            entityDropItem(new ItemStack(Items.EMERALD), 12.0F);
        }

        for (int l = 0; l < 32 + rand.nextInt(96 + arg70628n2); l++) {
            entityDropItem(new ItemStack(Items.DIAMOND), 12.0F);
        }

        for (int l = 0; l < 0 + rand.nextInt(16 + arg70628n2); l++) {
            entityDropItem(new ItemStack(TitanGear.harcadium), 12.0F);
        }

        for (int l = 0; l < 0 + rand.nextInt(8); l++) {
            entityDropItem(new ItemStack(Blocks.BEDROCK), 12.0F);
        }
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

    protected EntityLiving fetchMinion() {
        return new EntityGargoyle(world);
    }

    protected double cap() {
        return super.cap();
    }

    protected double fetchRegen() {
        return 0.01F;
    }

    @Override
    public boolean needsCrush() {
        return true;
    }
}
