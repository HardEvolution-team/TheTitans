package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class EntitySnowGolemTitan extends EntityTitan implements IRangedAttackMob {
    public EntitySnowGolemTitan(World worldIn) {
        super(worldIn);
        setSize(10.0F, 30.0F);
        ((PathNavigateGround)getNavigator()).setCanSwim(false);
        targetTasks.addTask(2, new EntityAIHurtByTarget(this, true));
        targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, IMob.MOB_SELECTOR));
        EntityIronGolemTitan.appendTargetingTaskToMobGiant(this);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    protected void whenUpdateBounds() {
        super.whenUpdateBounds();
    }

    @Override
    public double fetchAmountHp() {
        return super.fetchAmountHp() * 4.0F;
    }

    public void onKillCommand() {
        triggerAudioGiant(fetchDeathAudioGiant(), getSoundVolume(), getSoundPitch());
        setDead();
    }

    public boolean attackEntityAsMob(Entity arg70652n1) {
        double doub = getDistance(arg70652n1);
        if (doub < 30.0D) {
            return super.attackEntityAsMob(arg70652n1);
        }

        return false;
    }

    public float fetchScaleMultiplierGiant() {
        return 16F;
    }

    public EnumTitanStatus fetchConditionGiant() {
        return EnumTitanStatus.UTILITY;
    }

    public int fetchSpawnRateMinion() {
        return TheTitans.golemServantSpawnrateSnow;
    }

    @SuppressWarnings("rawtypes")
    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        return (arg70686n1 != EntityPlayer.class) && (arg70686n1 != EntitySnowman.class) && (arg70686n1 != EntitySnowGolemTitan.class);
    }

    public boolean hasSoulNo() {
        return true;
    }

    public String getName() {
        return "§o" + super.getName();
    }

    public void onLivingUpdate() {
        if ((getAttackTarget() != null) && (ticksExisted % 40 == 0)) {
            double d0 = getDistance(getAttackTarget());
            if (d0 <= fetchReachMelee()) {
                swingArm(net.minecraft.util.EnumHand.MAIN_HAND);
                getLookHelper().setLookPositionWithEntity(getAttackTarget(), 180.0F, 30.0F);
                attackEntityAsMob(getAttackTarget());
            }
        }

        List<?> found1 = !world.isRemote && getAttackTarget() != null && ticksExisted % 5 == 0 ? world.getEntitiesWithinAABB(EntitySnowman.class, getEntityBoundingBox().grow(128D, 128D, 128D)) : java.util.Collections.emptyList();
        if (!found1.isEmpty()) {
            for (int i1 = 0; i1 < found1.size(); i1++) {
                Entity entity = (Entity)found1.get(i1);
                if ((entity != null) && ((entity instanceof EntitySnowman))) {
                    if (getAttackTarget() != null) {
                        if (!getAttackTarget().canEntityBeSeen(entity))
                            ((EntitySnowman)entity).getMoveHelper().setMoveTo(getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 2D);
                        ((EntitySnowman)entity).setAttackTarget(getAttackTarget());
                        ((EntitySnowman)entity).getLookHelper().setLookPositionWithEntity(getAttackTarget(), 40.0F, 40.0F);
                        if (((EntitySnowman)entity).collidedHorizontally)
                            ((EntitySnowman)entity).motionY = 0.25D;
                        if ((entity.ticksExisted + entity.getEntityId()) % 20 < 5)
                            ((EntitySnowman)entity).attackEntityWithRangedAttack(getAttackTarget(), 1F);
                    }
                }
            }
        }

        if (getAttackTarget() != null && (ticksExisted + getEntityId()) % 20 == 0 && !world.isRemote) {
            attackEntityWithRangedAttack(getAttackTarget(), 0.0F);
        }

        if (!world.isRemote) {
            int i = MathHelper.floor(posX);
            int j = MathHelper.floor(posY);
            int k = MathHelper.floor(posZ);
            if ((isWet()) && (ticksExisted % 40 == 0)) {
                attackEntityFrom(DamageSource.DROWN, 1.0F);
            }

            if (world.getBiome(new BlockPos(i, j, k)).getTemperature(new BlockPos(i, j, k)) > 1.5F) {
                attackEntityFrom(DamageSource.ON_FIRE, 1.0F);
            }

            for (int l = 0; l < 1024; l++) {
                i = MathHelper.floor(posX + (rand.nextFloat() - 0.5D) * width);
                j = MathHelper.floor(posY);
                k = MathHelper.floor(posZ + (rand.nextFloat() - 0.5D) * width);
                BlockPos pos = new BlockPos(i, j, k);
                if ((world.getBlockState(pos).getMaterial() == Material.AIR) && (world.getBiome(pos).getTemperature(pos) < 1.3F) && (Blocks.SNOW_LAYER.canPlaceBlockAt(world, pos))) {
                    world.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState());
                }
            }
        }

        super.onLivingUpdate();
    }

    protected Item getDropItem() {
        return Items.SNOWBALL;
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        for (int x = 0; x < 1; x++) {
            EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
            itembomb.assignStackStackMob(new ItemStack(Blocks.LIT_PUMPKIN));
            itembomb.assignTotalStack(1);
            world.spawnEntity(itembomb);
        }

        for (int x = 0; x < 4; x++) {
            EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
            itembomb.assignStackStackMob(new ItemStack(Items.SNOWBALL));
            itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
            world.spawnEntity(itembomb);
        }

        for (int x = 0; x < 2; x++) {
            EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
            itembomb.assignStackStackMob(new ItemStack(Items.EMERALD));
            itembomb.assignTotalStack(2 + rand.nextInt(2 + arg70628n2) + arg70628n2);
            world.spawnEntity(itembomb);
        }

        for (int x = 0; x < 2; x++) {
            EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
            itembomb.assignStackStackMob(new ItemStack(Items.DIAMOND));
            itembomb.assignTotalStack(2 + rand.nextInt(2 + arg70628n2) + arg70628n2);
            world.spawnEntity(itembomb);
        }
    }

    protected String fetchHurtAudioGiant() {
        return "step.snow";
    }

    protected String fetchDeathAudioGiant() {
        return "dig.snow";
    }

    public void attackEntityWithRangedAttack(EntityLivingBase arg82196n1, float arg82196n2) {
        if ((getHealth() > 0.0F) && (!world.isRemote)) {
            EntitySnowman entitychicken = new EntitySnowman(world);
            entitychicken.setLocationAndAngles(posX + (rand.nextFloat() - 0.5D) * width, posY + getEyeHeight(), posZ + (rand.nextFloat() - 0.5D) * width, rotationYaw, 0.0F);
            world.spawnEntity(entitychicken);
            entitychicken.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
            entitychicken.setHealth(20.0F);
            entitychicken.setCustomNameTag("Reinforced Snow Golem");
        }

        faceEntity(arg82196n1, 180.0F, 30.0F);
        getDistance(arg82196n1);
        double d8 = 6D;
        Vec3d vec3 = getLook(1.0F);
        double d1 = arg82196n1.posX - (posX + vec3.x * d8);
        double d2 = (arg82196n1.posY + (arg82196n1.getEyeHeight())) - (posY + 20D + vec3.y * d8);
        double d3 = arg82196n1.posZ - (posZ + vec3.z * d8);
        EntityTitanFireball entitylargefireball = new EntityTitanFireball(world, this, d1, d2, d3, 6);
        entitylargefireball.posX = (posX + vec3.x * d8);
        entitylargefireball.posY = (posY + 20D + vec3.y * d8);
        entitylargefireball.posZ = (posZ + vec3.z * d8);
        world.spawnEntity(entitylargefireball);
        entitylargefireball.assignIDFireball(6);
        triggerAudioGiant("random.bow", 10.0F, 0.5F);
        triggerAudioGiant("random.bow", 10.0F, 0.5F);
    }

    public int getTotalArmorValue() {
        return 15;
    }

    public float getEyeHeight() {
        return 27.2F;
    }

    protected void updateAITasks() {
        super.updateAITasks();
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
