package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thehippomaster.AnimationAPI.IAnimatedEntity;
import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;
import com.electrifiedded.thetitans.entity.titan.anim.ultimairongolemtitan.AnimationIronGolemTitanAntiTitanAttack;
import com.electrifiedded.thetitans.entity.titan.anim.ultimairongolemtitan.AnimationIronGolemTitanAttack1;
import com.electrifiedded.thetitans.entity.titan.anim.ultimairongolemtitan.AnimationIronGolemTitanAttack2;
import com.electrifiedded.thetitans.entity.titan.anim.ultimairongolemtitan.AnimationIronGolemTitanAttack3;
import com.electrifiedded.thetitans.entity.titan.anim.ultimairongolemtitan.AnimationIronGolemTitanAttack4;
import com.electrifiedded.thetitans.entity.titan.anim.ultimairongolemtitan.AnimationIronGolemTitanDeath;
import com.electrifiedded.thetitans.entity.titan.anim.ultimairongolemtitan.AnimationIronGolemTitanRangedAttack;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

public class EntityIronGolemTitan extends EntityTitan implements IAnimatedEntity {
    private static final DataParameter<Byte> PLAYER_CREATED = EntityDataManager.<Byte>createKey(EntityIronGolemTitan.class, DataSerializers.BYTE);
    private int homeCheckTimer;
    Village objVillage;
    private int attackTimer;
    private int holdRoseTick;

    private static final Predicate<Entity> mobSelectorStrike = new Predicate<Entity>() {
        public boolean apply(@Nullable Entity arg180027n1) {
            return arg180027n1 != null && arg180027n1.isEntityAlive() && arg180027n1 instanceof EntityTitan && !((EntityTitan)arg180027n1).fetchWaiting() && ((EntityTitan)arg180027n1).getAnimID() != 11 && !(arg180027n1 instanceof EntityGargoyleTitan) && !(arg180027n1 instanceof EntityIronGolemTitan) && !(arg180027n1 instanceof EntitySnowGolemTitan);
        }
    };

    protected void whenUpdateBounds() {
        if (ticksExisted % 5 == 0) {
            setSize(32F, 86F);
        }

        super.whenUpdateBounds();
    }

    public EntityIronGolemTitan(World worldIn) {
        super(worldIn);
        setSize(32F, 86F);
        ((PathNavigateGround)getNavigator()).setCanSwim(false);
        targetTasks.addTask(2, new EntityAIHurtByTarget(this, true));
        targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, IMob.MOB_SELECTOR));
        appendTargetingTaskToMobGiant(this);
        tasks.addTask(1, new AnimationIronGolemTitanDeath(this));
        tasks.addTask(1, new AnimationIronGolemTitanRangedAttack(this));
        tasks.addTask(1, new AnimationIronGolemTitanAntiTitanAttack(this));
        tasks.addTask(1, new AnimationIronGolemTitanAttack4(this));
        tasks.addTask(1, new AnimationIronGolemTitanAttack3(this));
        tasks.addTask(1, new AnimationIronGolemTitanAttack2(this));
        tasks.addTask(1, new AnimationIronGolemTitanAttack1(this));
    }

    @Override
    public double fetchAmountHp() {
        return super.fetchAmountHp() * 100.0F;
    }

    public int fetchTintLabel() {
        return 12632256;
    }

    public static void appendTargetingTaskToMobGiant(EntityCreature entity) {
        entity.targetTasks.addTask(0, new EntityAINearestTargetTitan(entity, EntityTitan.class, 0, false, false, mobSelectorStrike));
    }

    public float getEyeHeight() {
        return 0.8682170542635659F * height;
    }

    public void onKillCommand() {
        triggerAudioGiant(fetchDeathAudioGiant(), getSoundVolume(), getSoundPitch());
        setDead();
    }

    protected void entityInit() {
        super.entityInit();
        dataManager.register(PLAYER_CREATED, Byte.valueOf((byte)0));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    public EnumTitanStatus fetchConditionGiant() {
        return EnumTitanStatus.UTILITY;
    }

    protected int decreaseAirSupply(int arg70682n1) {
        return arg70682n1;
    }

    public int fetchSpawnRateMinion() {
        return TheTitans.ironGolemServantSpawnrateUltima;
    }

    public boolean allowHurtByUserBe() {
        return !isPlayerCreated() && !isEntityInvulnerable(DamageSource.GENERIC);
    }

    public boolean hasSoulNo() {
        return true;
    }

    public String getName() {
        return "§l" + super.getName();
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (isPlayerCreated() && getAttackTarget() != null && getAttackTarget() instanceof EntityPlayer)
            setAttackTarget(null);
        rejuvinatingIs = false;
        if (getAnimID() == 10) {
            if ((getAnimTick() == 30) || (getAnimTick() == 70)) {
                funcA145780(0, 0, 0, Blocks.STONE);
            }

            if (getAnimTick() == 190) {
                triggerAudioGiant("thetitans:titanFall", 20.0F, 0.9F);
                triggerAudioGiant("thetitans:groundSmash", 20.0F, 1.0F);
                shakeUserCamerasNearby(20D);
            }

            if (getAnimTick() == 200) {
                triggerAudioGiant("thetitans:distantLargeFall", 10000.0F, 0.5F);
            }
        }

        if (!world.isRemote && getAnimID() == 5 && (getAnimTick() == 34) && (getAttackTarget() != null)) {
            attackEntityAsMob(getAttackTarget());
            Vec3d vec3 = getLook(1F);
            double d5 = getAttackTarget().posX - (posX + vec3.x * 30D);
            double d6 = getAttackTarget().posY - (posY + 30D);
            double d7 = getAttackTarget().posZ - (posZ + vec3.z * 30D);
            EntityTitanFireball entitylargefireball = new EntityTitanFireball(world, this, d5, d6, d7, 1);
            entitylargefireball.posX = (posX + vec3.x * 30D);
            entitylargefireball.posY = (posY + 30D);
            entitylargefireball.posZ = (posZ + vec3.z * 30D);
            world.spawnEntity(entitylargefireball);
            entitylargefireball.assignIDFireball(5);
            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanSwing", world, entitylargefireball, 10.0F, 1.0F);
        }

        if (deathTicks > 0) {
            motionX *= 0.0D;
            motionZ *= 0.0D;
        }

        if ((!AnimationAPI.isEffectiveClient()) && (getAttackTarget() != null) && (getAnimID() == 0) && ticksExisted > 5) {
            double d0 = getDistance(getAttackTarget());
            if (d0 < fetchReachMelee()) {
                switch (rand.nextInt(5)) {
                    case 0:AnimationAPI.sendAnimPacket(this, 6);
                        setAnimID(6);
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

            else if ((getAnimID() == 0) && (getRNG().nextInt(160) == 0)) {
                switch (rand.nextInt(2)) {
                    case 0:AnimationAPI.sendAnimPacket(this, 5);
                        setAnimID(5);
                        break;
                    case 1:AnimationAPI.sendAnimPacket(this, 5);
                        setAnimID(5);
                        break;
                }
            }
        }

        if (motionY > 1.0D)motionY *= 0.8D;
        giantMelee = true;
        List<?> found1 = world.getEntitiesWithinAABB(EntityIronGolem.class, getEntityBoundingBox().grow(96D, 96D, 96D));
        if ((found1 != null) && (!found1.isEmpty())) {
            for (int i1 = 0; i1 < found1.size(); i1++) {
                Entity entity = (Entity)found1.get(i1);
                if ((entity != null) && ((entity instanceof EntityIronGolem))) {
                    if (((EntityIronGolem)entity).collidedHorizontally)
                        ((EntityIronGolem)entity).motionY = 0.25D;
                    if (((EntityIronGolem)entity).getAttackTarget() == null && ((EntityIronGolem)entity).getDistanceSq(this) > 4096D) {
                        ((EntityIronGolem)entity).getLookHelper().setLookPositionWithEntity(this, 180.0F, 40F);
                        ((EntityIronGolem)entity).getMoveHelper().setMoveTo(posX, posY, posZ, 1D);
                    }

                    if (((EntityIronGolem)entity).ticksExisted == 20)
                        appendTargetingTaskToMobGiant(this);
                }
            }
        }

        if (attackTimer > 0) {
            attackTimer -= 1;
        }

        if (holdRoseTick > 0) {
            holdRoseTick -= 1;
        }
    }

    protected void updateAITasks() {
        if (--homeCheckTimer <= 0) {
            homeCheckTimer = (70 + rand.nextInt(50));
            objVillage = world.getVillageCollection().getNearestVillage(new BlockPos(MathHelper.floor(posX), MathHelper.floor(posY), MathHelper.floor(posZ)), 32);
            if (objVillage == null) {
                detachHome();
            }

            else {
                BlockPos blockpos = objVillage.getCenter();
                setHomePosAndDistance(blockpos, (int)(objVillage.getVillageRadius() * 0.6F));
            }
        }

        super.updateAITasks();
    }

    @SuppressWarnings("rawtypes")
    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        return (arg70686n1 == EntityVillager.class || arg70686n1 == EntityIronGolem.class || arg70686n1 == EntityIronGolemTitan.class) ? false : (isPlayerCreated() && EntityPlayer.class.isAssignableFrom(arg70686n1) ? false : true);
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
        float f = (float)fetchAmountStrike(3.0F);
        if ((arg70652n1 instanceof EntityWitherzilla))
            f *= 5F;
        if ((arg70652n1 instanceof EntityGhastTitan && arg70652n1.posY > posY + 32D))
            --arg70652n1.motionY;
        for (int l = 0; l < 4 + rand.nextInt(6); l++)
            strikeMobChoosen(arg70652n1, f, fetchAmountKnockback());
        return true;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isEntityInvulnerable(source)) {
            return false;
        }

        else if (source.getTrueSource() instanceof EntityPlayer && isPlayerCreated()) {
            return false;
        }

        else if (source.getTrueSource() instanceof EntityIronGolem || source.getTrueSource() instanceof EntityIronGolemTitan) {
            return false;
        }

        else {
            return super.attackEntityFrom(source, amount);
        }
    }

    @SideOnly(Side.CLIENT)
    public void processUpdateHp(byte arg70103n1) {
        // base EntityTitan keeps 1.7.10 name processUpdateHp (not handleStatusUpdate); matching base for consistency.
        if (arg70103n1 == 4) {
            attackTimer = 10;
            triggerAudioGiant("mob.irongolem.throw", 100.0F, 0.5F);
        }

        else if (arg70103n1 == 11) {
            holdRoseTick = 800;
        }

        else {
            super.processUpdateHp(arg70103n1);
        }
    }

    public Village getVillage() {
        return objVillage;
    }

    @SideOnly(Side.CLIENT)
    public int getAttackTimer() {
        return attackTimer;
    }

    public void setHoldingRose(boolean arg70851n1) {
        holdRoseTick = (arg70851n1 ? 800 : 0);
        world.setEntityState(this, (byte)11);
    }

    protected String fetchHurtAudioGiant() {
        return "mob.irongolem.hit";
    }

    protected String fetchDeathAudioGiant() {
        return "mob.irongolem.death";
    }

    protected float getSoundPitch() {
        return isChild() ? (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F : (rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.5F;
    }

    protected void funcA145780(int arg145780n1, int arg145780n2, int arg145780n3, net.minecraft.block.Block arg145780n4) {
        triggerAudioGiant("mob.irongolem.walk", 10.0F, 0.5F);
        triggerAudioGiant("mob.irongolem.walk", 10.0F, 0.5F);
        triggerAudioGiant("mob.irongolem.walk", 10.0F, 0.5F);
        triggerAudioGiant("mob.irongolem.walk", 10.0F, 0.5F);
        triggerAudioGiant("mob.irongolem.walk", 10.0F, 0.5F);
        triggerAudioGiant("mob.irongolem.walk", 10.0F, 0.5F);
        triggerAudioGiant("mob.irongolem.walk", 10.0F, 0.5F);
        triggerAudioGiant("mob.irongolem.walk", 10.0F, 0.5F);
        triggerAudioGiant("thetitans:titanStep", 10.0F, 1.0F);
        shakeUserCamerasNearby(8D);
    }

    protected float getSoundVolume() {
        return 100.0F;
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        for (int l = 0; l < 512 + rand.nextInt(512 + arg70628n2); l++) {
            entityDropItem(new ItemStack(Blocks.IRON_BLOCK), 12.0F);
        }

        for (int l = 0; l < 128 + rand.nextInt(128 + arg70628n2); l++) {
            entityDropItem(new ItemStack(Blocks.RED_FLOWER), 12.0F);
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

    public int getHoldRoseTick() {
        return holdRoseTick;
    }

    public boolean isPlayerCreated() {
        return (dataManager.get(PLAYER_CREATED).byteValue() & 0x1) != 0;
    }

    public void setPlayerCreated(boolean arg70849n1) {
        byte b0 = dataManager.get(PLAYER_CREATED).byteValue();
        if (arg70849n1) {
            dataManager.set(PLAYER_CREATED, Byte.valueOf((byte)(b0 | 0x1)));
        }

        else {
            dataManager.set(PLAYER_CREATED, Byte.valueOf((byte)(b0 & 0xFFFFFFFE)));
        }
    }

    public void onDeath(DamageSource cause) {
        if ((!isPlayerCreated()) && (attackingPlayer != null) && (objVillage != null)) {
            objVillage.modifyPlayerReputation(attackingPlayer.getName(), -50000);
        }

        super.onDeath(cause);
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
        }

        if (getInvulTime() >= fetchHoldThreash()) {
            setDead();
        }
    }

    protected EntityLiving fetchMinion() {
        return new EntityIronGolem(world);
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
