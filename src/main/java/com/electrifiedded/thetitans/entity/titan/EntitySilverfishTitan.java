package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thehippomaster.AnimationAPI.IAnimatedEntity;
import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.core.TheCore;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.minion.EntitySilverfishMinion;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAINearestTargetTitan;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAITitanWander;
import com.electrifiedded.thetitans.entity.titan.ai.EntityAITitanWatchClosest;
import com.electrifiedded.thetitans.entity.titan.anim.omegafish.AnimationOmegafishAntiTitanAttack;
import com.electrifiedded.thetitans.entity.titan.anim.omegafish.AnimationOmegafishAttack1;
import com.electrifiedded.thetitans.entity.titan.anim.omegafish.AnimationOmegafishAttack2;
import com.electrifiedded.thetitans.entity.titan.anim.omegafish.AnimationOmegafishAttack3;
import com.electrifiedded.thetitans.entity.titan.anim.omegafish.AnimationOmegafishBodySlam;
import com.electrifiedded.thetitans.entity.titan.anim.omegafish.AnimationOmegafishBurrow;
import com.electrifiedded.thetitans.entity.titan.anim.omegafish.AnimationOmegafishCreation;
import com.electrifiedded.thetitans.entity.titan.anim.omegafish.AnimationOmegafishDeath;
import com.electrifiedded.thetitans.entity.titan.anim.omegafish.AnimationOmegafishLightningAttack;
import com.electrifiedded.thetitans.entity.titan.anim.omegafish.AnimationOmegafishStunned;
import com.electrifiedded.thetitans.entity.titan.anim.omegafish.AnimationOmegafishTailSmash;
import com.electrifiedded.thetitans.entity.titan.anim.omegafish.AnimationOmegafishUnburrow;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanTrophies;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class EntitySilverfishTitan extends EntityTitan implements IAnimatedEntity, ITitanHitbox {
    public boolean subduedIs;
    public boolean burrowingIs;
    public EntityTitanPart head;
    public EntityTitanPart body;
    public EntityTitanPart tailbase;
    public EntityTitanPart tail1;
    public EntityTitanPart tail2;
    public EntityTitanPart tailtip;

    public EntitySilverfishTitan(World worldIn) {
        super(worldIn);
        head = new EntityTitanPart(worldIn, this, "head", 3.0F, 2.0F);
        body = new EntityTitanPart(worldIn, this, "body", 5.0F, 4.0F);
        tailbase = new EntityTitanPart(worldIn, this, "tailbase", 3.0F, 3.0F);
        tail1 = new EntityTitanPart(worldIn, this, "tail1", 3.0F, 2.0F);
        tail2 = new EntityTitanPart(worldIn, this, "tail2", 2.0F, 1.0F);
        tailtip = new EntityTitanPart(worldIn, this, "tailtip", 2.0F, 1.0F);
        setPiece.add(head);
        setPiece.add(body);
        setPiece.add(tailbase);
        setPiece.add(tail1);
        setPiece.add(tail2);
        setPiece.add(tailtip);
        setSize(9.0F, 6.0F);
        experienceValue = (6000 + fetchPowerExtra() * 200);
    }

    protected void whenUpdateBounds() {
        if (ticksExisted > 5) {
            setSize(0.5625F * fetchScaleMultiplierGiant(), 0.375F * fetchScaleMultiplierGiant());

            float f = renderYawOffset * 3.1415927F / 180.0F;
            float f1 = MathHelper.sin(f);
            float f2 = MathHelper.cos(f);
            body.height = 0.275F * fetchScaleMultiplierGiant();
            body.width = 0.35F * fetchScaleMultiplierGiant();
            head.width = tailbase.height = tailbase.width = tail1.width = 0.1875F * fetchScaleMultiplierGiant();
            head.height = tail1.height = tail2.width = tailtip.width = 0.125F * fetchScaleMultiplierGiant();
            tail2.height = tailtip.height = 0.0625F * fetchScaleMultiplierGiant();
            head.setLocationAndAngles(posX - MathHelper.sin(rotationYawHead * 3.1415927F / 180.0F) * (0.375F * width), posY - MathHelper.sin(rotationPitch * 3.1415927F / 180.0F) * (0.335F * height), posZ + MathHelper.cos(rotationYawHead * 3.1415927F / 180.0F) * (0.375F * width), 0.0F, 0.0F);
            body.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
            tailbase.setLocationAndAngles(posX + f1 * (0.375F * width), posY, posZ - f2 * (0.375F * width), 0.0F, 0.0F);
            tail1.setLocationAndAngles(posX + f1 * (0.775F * width), posY, posZ - f2 * (0.775F * width), 0.0F, 0.0F);
            tail2.setLocationAndAngles(posX + f1 * (1.05F * width), posY, posZ - f2 * (1.05F * width), 0.0F, 0.0F);
            tailtip.setLocationAndAngles(posX + f1 * (1.275F * width), posY, posZ - f2 * (1.275F * width), 0.0F, 0.0F);
            // PERF — burrow block-clearing used to run here AND in onLivingUpdate (grow 2 ⊇ grow 1) every tick.
        }

        super.whenUpdateBounds();
    }

    protected void applyEntityAI() {
        tasks.addTask(1, new AnimationOmegafishCreation(this));
        tasks.addTask(1, new AnimationOmegafishDeath(this));
        tasks.addTask(1, new AnimationOmegafishStunned(this));
        tasks.addTask(1, new AnimationOmegafishAntiTitanAttack(this));
        tasks.addTask(1, new AnimationOmegafishBodySlam(this));
        tasks.addTask(1, new AnimationOmegafishTailSmash(this));
        tasks.addTask(1, new AnimationOmegafishLightningAttack(this));
        tasks.addTask(1, new AnimationOmegafishAttack2(this));
        tasks.addTask(1, new AnimationOmegafishAttack3(this));
        tasks.addTask(1, new AnimationOmegafishAttack1(this));
        tasks.addTask(1, new AnimationOmegafishUnburrow(this));
        tasks.addTask(1, new AnimationOmegafishBurrow(this));
        tasks.addTask(6, new EntityAITitanWander(this, 50));
        tasks.addTask(7, new EntityAITitanWatchClosest(this, EntityTitanSpirit.class, 128.0F));
        tasks.addTask(8, new EntityAITitanWatchClosest(this, EntityTitan.class, 128.0F));
        tasks.addTask(9, new EntityAITitanWatchClosest(this, EntityPlayer.class, 64.0F));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        if (!subduedIs) {
            if (TheTitans.ffaModeTitans)
                targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.giantSorterSilverfish));
            else
                targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.forAThingToKillSearch));
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntityIronGolemTitan.class, 0, false));
            targetTasks.addTask(0, new EntityAINearestTargetTitan(this, EntitySnowGolemTitan.class, 0, false));
        }
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        stunnedIs = tagCompund.getBoolean("Stunned");
        burrowingIs = tagCompund.getBoolean("Burrowing");subduedIs = tagCompund.getBoolean("Subdued");
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("Stunned", stunnedIs);
        tagCompound.setBoolean("Burrowing", burrowingIs);
        tagCompound.setBoolean("Subdued", subduedIs);
    }

    @Override
    public double fetchAmountHp() {
        switch (fetchVariantGiant()) {
            case 1: return super.fetchAmountHp() * 6.0D;
            case 2: return super.fetchAmountHp() * 9.0D;
            case 3: return super.fetchAmountHp() * 7.0D;
            case 4: return super.fetchAmountHp() * 10.0D;
            case 5: return super.fetchAmountHp() * 12.0D;
            case 6: return super.fetchAmountHp() * 16.0D;
            default: return super.fetchAmountHp() * 8.0D;
        }
    }

    @SuppressWarnings("rawtypes")
    public boolean allowStrikeGiant(Class<?> arg70686n1) {
        return (arg70686n1 != head.getClass() && arg70686n1 != body.getClass() && arg70686n1 != tailbase.getClass() && arg70686n1 != tail1.getClass() && arg70686n1 != tail2.getClass() && arg70686n1 != tailtip.getClass() && (arg70686n1 != EntitySilverfishMinion.class) && (arg70686n1 != EntitySilverfishTitan.class));
    }

    public boolean isArmored() {
        return getHealth() <= getMaxHealth() / 3.0D;
    }

    public boolean getCanSpawnHere() {
        return (rand.nextInt(10) == 0) && (world.getDifficulty() != EnumDifficulty.PEACEFUL) && (isValidLightLevel());
    }

    public double fetchReachMelee() {
        return (double)width + 0.325D * (double)fetchScaleMultiplierGiant();
    }

    public int fetchTintLabel() {
        return 12632256;
    }

    public int fetchSpawnRateMinion() {
        return TheTitans.servantSpawnrateOmegafish;
    }

    public float getEyeHeight() {
        return 0.3333333333333333F * height;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    protected String fetchLivingAudioGiant() {
        return stunnedIs || fetchWaiting() || getAnimID() == 2 ? null : "thetitans:titanSilverfishLiving";
    }

    protected String fetchHurtAudioGiant() {
        return "thetitans:titanSilverfishGrunt";
    }

    protected String fetchDeathAudioGiant() {
        return "thetitans:titanSilverfishDeath";
    }

    protected Item getDropItem() {
        return Items.PAPER;
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        if (deathTicks > 0) {
            for (int x = 0; x < 6; x++) {
                EntityXPBomb entitylargefireball = new EntityXPBomb(world, posX, posY + 6D, posZ);
                entitylargefireball.setPosition(posX, posY + 6D, posZ);
                ++entitylargefireball.motionY;
                entitylargefireball.assignTotalXP(5333333);
                world.spawnEntity(entitylargefireball);
            }

            for (int x = 0; x < 12; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.PAPER));
                itembomb.assignTotalStack(24 + rand.nextInt(24 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 6; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.COAL));
                itembomb.assignTotalStack(16 + rand.nextInt(16 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.EMERALD));
                itembomb.assignTotalStack(24 + rand.nextInt(24 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 1; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Items.DIAMOND));
                itembomb.assignTotalStack(48 + rand.nextInt(48 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 12; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.COBBLESTONE));
                itembomb.assignTotalStack(128 + rand.nextInt(128 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 12; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.STONE));
                itembomb.assignTotalStack(128 + rand.nextInt(128 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 6; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.MOSSY_COBBLESTONE));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.STONEBRICK));
                itembomb.assignTotalStack(64 + rand.nextInt(64 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(Blocks.MONSTER_EGG));
                itembomb.assignTotalStack(48 + rand.nextInt(48 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }

            if ((rand.nextInt(2) == 0 || rand.nextInt(1 + arg70628n2) > 0))
                for (int x = 0; x < 2; x++) {
                EntityItemBomb itembomb = new EntityItemBomb(world, posX, posY + 6D, posZ);
                itembomb.assignStackStackMob(new ItemStack(TitanGear.nuggetHarcadiumZ));
                itembomb.assignTotalStack(36 + rand.nextInt(36 + arg70628n2) + arg70628n2);
                world.spawnEntity(itembomb);
            }
        }
    }

    public boolean allowHurtByUserBe() {
        return stunnedIs && !burrowingIs && (!isEntityInvulnerable(DamageSource.GENERIC));
    }

    public boolean attackEntityAsMob(Entity arg70652n1) {
        return false;
    }

    protected void funcA145780(int arg145780n1, int arg145780n2, int arg145780n3, Block arg145780n4) {
    }

    public boolean needsMove() {
        return !stunnedIs && super.needsMove();
    }

    public double getSpeed() {
        return burrowingIs ? 0.9D + fetchPowerExtra() * 0.001D : 0.7D + fetchPowerExtra() * 0.001D;
    }

    public void onLivingUpdate() {
        setInvisible(burrowingIs);
        if (!isRiding() && !burrowingIs && !fetchWaiting() && !stunnedIs && getAnimID() == 0) {
            flyTitan(5F, 1F, 2D);
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

            if (getAnimID() == 13 && world.isRemote)
                switch(getAnimTick()) {
                case 1: {
                        triggerAudioGiant("thetitans:titanBirth", 100F, 1.25F);
                    }

                case 10: {
                        triggerAudioGiant("thetitans:titanSilverfishLiving", getSoundVolume(), 0.7F);
                    }

                case 150: {
                        triggerAudioGiant("thetitans:titanPress", getSoundVolume(), 1F);
                        shakeUserCamerasNearby(4D);
                    }
            }
        }

        if (!burrowingIs && !stunnedIs && getAnimID() != 2 && getAnimID() != 9 && (getAttackTarget() != null) && getDistance(getAttackTarget()) > 200D) {
            if (posY <= getAttackTarget().posY + 24D) {
                motionY += 0.9D - motionY;
                if (motionY < 0.0D)
                    motionY = 0.0D;
            }

            motionY *= 0.9D;
        }

        if (!burrowingIs && !onGround) {
            float f = (rand.nextFloat() - 0.5F) * 8.0F;
            float f1 = (rand.nextFloat() - 0.5F) * 1.0F;
            float f2 = (rand.nextFloat() - 0.5F) * 8.0F;
            world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, posX + (double)f, posY + (double)f1, posZ + (double)f2, 0.0D, 0.0D, 0.0D);
        }

        if (subduedIs)
            renderYawOffset = rotationYaw = rotationYawHead;
        EntityPlayer player = world.getClosestPlayerToEntity(head, 4D);
        if (player != null && head.posY < player.posY - 2D) {
            rotationYawHead += MathHelper.sin(ticksExisted) * 40F;
            rotationPitch -= MathHelper.cos(ticksExisted) * 40F;
        }

        if (getAnimID() == 1 && getAnimTick() > 80)
            burrowingIs = true;
        else if (getAnimID() == 2)
            burrowingIs = false;
        if (burrowingIs) {
            destroyBlocksInAABB(getEntityBoundingBox().grow(2, 0, 2));
            world.playEvent(2006, new BlockPos(MathHelper.floor(posX), MathHelper.floor(posY - 0.20000000298023224D), MathHelper.floor(posZ)), MathHelper.ceil(128.0F));
            if (ticksExisted % 40 == 0) {
                triggerAudioGiant("thetitans:titanRumble", 10.0F, 1.0F);
                triggerAudioGiant("thetitans:titanRumble", 9.0F, 1.0F);
                triggerAudioGiant("thetitans:titanRumble", 8.0F, 1.0F);
                triggerAudioGiant("thetitans:titanRumble", 7.0F, 1.0F);
                triggerAudioGiant("thetitans:titanRumble", 6.0F, 1.0F);
                triggerAudioGiant("thetitans:titanQuake", 5.0F, 1.0F);
                triggerAudioGiant("thetitans:titanQuake", 4.0F, 1.0F);
                triggerAudioGiant("thetitans:titanQuake", 3.0F, 1.0F);
                triggerAudioGiant("thetitans:titanQuake", 2.0F, 1.0F);
                triggerAudioGiant("thetitans:titanQuake", 1.0F, 1.0F);
            }
        }

        if ((subduedIs) && (ticksExisted % 40 == 0)) {
            targetTasks.removeTask(new EntityAINearestTargetTitan(this, EntityLivingBase.class, 0, false, false, ITitan.forAThingToKillSearch));
            targetTasks.removeTask(new EntityAINearestTargetTitan(this, EntityIronGolemTitan.class, 0, false));
            targetTasks.removeTask(new EntityAINearestTargetTitan(this, EntitySnowGolemTitan.class, 0, false));
        }

        if (getAnimID() == 7) {
            if (getAnimTick() == 20) {
                double d8 = -3.0D;
                Vec3d vec3 = getLook(1.0F);
                double dx = vec3.x * d8;
                double dz = vec3.z * d8;
                float f = (float)fetchAmountStrike(3.0F);
                int i = fetchAmountKnockback();
                world.newExplosion(this, posX + dx, posY + 8.0D, posZ + dz, 1.0F, false, false);
                world.addWeatherEffect(new EntityGammaLightning(world, posX + dx, posY + 8.0D, posZ + dz, 0.5F, 0.5F, 0.5F));
                if (getAttackTarget() != null) {
                    strikeMobChoosen(getAttackTarget(), f, i);
                    getAttackTarget().motionY += 2.0D;
                    world.newExplosion(this, getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 2.0F, false, false);
                    getAttackTarget().attackEntityFrom(DamageSourceExtra.lightningBolt, f);
                    world.addWeatherEffect(new EntityGammaLightning(world, getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 0.25F, 0.25F, 0.25F));
                    List<?> found1 = world.getEntitiesWithinAABB(EntityLivingBase.class, getAttackTarget().getEntityBoundingBox().grow(2.0D, 2.0D, 2.0D), e -> e != getAttackTarget());
                    if ((found1 != null) && (!found1.isEmpty())) {
                        for (int i1 = 0; i1 < found1.size(); i1++) {
                            Entity mobRef1 = (Entity)found1.get(i1);
                            if (((mobRef1 instanceof EntityLivingBase)) && (!(mobRef1 instanceof EntitySilverfishMinion)) && (!(mobRef1 instanceof EntitySilverfishTitan))) {
                                strikeMobChoosen(mobRef1, f, i);
                                mobRef1.motionY += 1.0D;
                            }
                        }
                    }
                }
            }
        }

        giantMelee = true;

        if (stunnedIs) {
            setAttackTarget(null);
            AnimationAPI.sendAnimPacket(this, 8);
        }

        if ((!AnimationAPI.isEffectiveClient()) && (getAttackTarget() != null) && (!stunnedIs) && burrowingIs && posY > getAttackTarget().posY) {
            wreckInAABBToplessBlocks(getEntityBoundingBox().offset(0D, -1D, 0D));
        }

        if ((!AnimationAPI.isEffectiveClient()) && (getAttackTarget() != null) && (!stunnedIs) && getAnimID() == 0) {
            double d0 = getDistance(getAttackTarget());
            if (!burrowingIs && canEntityBeSeen(getAttackTarget()) && d0 > 200D && onGround)
                AnimationAPI.sendAnimPacket(this, 1);
            if (d0 < fetchReachMelee()) {
                if (burrowingIs) {
                    AnimationAPI.sendAnimPacket(this, 2);
                    setAnimID(2);
                }

                else {
                    if (((getAttackTarget() instanceof EntityTitan)) || (getAttackTarget().height >= 6.0F) || (getAttackTarget().posY > posY + 6D)) {
                        AnimationAPI.sendAnimPacket(this, 11);
                        setAnimID(11);
                    }

                    else {
                        switch (rand.nextInt(5)) {
                            case 0:AnimationAPI.sendAnimPacket(this, 6);
                                setAnimID(6);
                                break;
                            case 1:AnimationAPI.sendAnimPacket(this, 5);
                                setAnimID(5);
                                break;
                            case 2:AnimationAPI.sendAnimPacket(this, 4);
                                setAnimID(4);
                        }
                    }
                }
            }

            else {
                int i = 60;
                if (isArmored()) {
                    i = 20;
                }

                if (getAnimID() == 0 && !burrowingIs && (getRNG().nextInt(i) == 0)) {
                    if (getAttackTarget().posY > posY + 12D) {
                        AnimationAPI.sendAnimPacket(this, 7);
                        setAnimID(7);
                    }

                    else {
                        switch (rand.nextInt(4)) {
                            case 0:AnimationAPI.sendAnimPacket(this, 3);
                                setAnimID(3);
                                break;
                            case 1:AnimationAPI.sendAnimPacket(this, 7);
                                setAnimID(7);
                                break;
                            case 2:if (!burrowingIs) {
                                    AnimationAPI.sendAnimPacket(this, 1);
                                    setAnimID(1);
                                }

                                else {
                                    AnimationAPI.sendAnimPacket(this, 3);
                                    setAnimID(3);
                                }

                                break;
                            case 3:if (getRNG().nextInt(3) == 0) {
                                    AnimationAPI.sendAnimPacket(this, 9);
                                    setAnimID(9);
                                }

                                else {
                                    AnimationAPI.sendAnimPacket(this, 3);
                                    setAnimID(3);
                                }

                                break;
                        }
                    }
                }
            }
        }

        if (stunnedIs || deathTicks > 0) {
            motionX *= 0.0D;
            motionZ *= 0.0D;
        }

        if (getAnimID() == 8) {
            setAttackTarget(null);
        }

        if (getAnimID() == 8) {
            if (getAnimTick() == 37) {
                triggerAudioGiant("thetitans:largeFall", 4.0F, 1.0F);
            }

            if (getAnimTick() == 37) {
                triggerAudioGiant("thetitans:distantLargeFall", 10000.0F, 1.0F);
            }

            if (getAnimTick() == 380) {
                stunnedIs = false;
            }

            else {
                setAttackTarget(null);
            }
        }

        if (getAnimID() == 10) {
            if ((getAnimTick() == 74) || (getAnimTick() == 216)) {
                triggerAudioGiant("thetitans:titanFall", 10.0F, 1.0F);
            }

            if (getAnimTick() == 76) {
                triggerAudioGiant("thetitans:distantLargeFall", 10000.0F, 1.0F);
            }
        }

        switch (fetchVariantGiant()) {
            case 1:
                setCustomNameTag(I18n.translateToLocal("entity.SilverfishTitanDesert.name"));
                break;
            case 2:
                setCustomNameTag(I18n.translateToLocal("entity.SilverfishTitanFrost.name"));
                break;
            case 3:
                setCustomNameTag(I18n.translateToLocal("entity.SilverfishTitanSea.name"));
                break;
            case 4:
                setCustomNameTag(I18n.translateToLocal("entity.SilverfishTitanPoison.name"));
                break;
            case 5:
                setCustomNameTag(I18n.translateToLocal("entity.SilverfishTitanMagma.name"));
                break;
            case 6:
                setCustomNameTag(I18n.translateToLocal("entity.SilverfishTitanVoid.name"));
                break;
            default:
                setCustomNameTag(I18n.translateToLocal("entity.SilverfishTitan.name"));
        }

        if ((rand.nextInt(100) == 0) && (getAttackTarget() != null) && getAnimID() == 0) {
            faceEntity(getAttackTarget(), 180.0F, 180.0F);
            double d0 = getAttackTarget().posX - posX;
            double d1 = getAttackTarget().posZ - posZ;
            float f21 = MathHelper.sqrt(d0 * d0 + d1 * d1);
            motionX = (d0 / f21 * 2.0D * 2.0D + motionX * 2.0D);
            motionZ = (d1 / f21 * 2.0D * 2.0D + motionZ * 2.0D);
        }

        if ((isEntityAlive()) && (rand.nextInt(40) == 0) && (getAttackTarget() != null) && (isArmored()) && !(getAttackTarget() instanceof EntityTitan)) {
            rotationPitch = -90F;
            world.newExplosion(this, getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 2.0F, false, false);
        }

        int i;
        int j;
        int k;
        int i1;
        if (!world.isRemote && !stunnedIs && !fetchWaiting() && getAnimID() != 13) {
            i = MathHelper.floor(posX);
            j = MathHelper.floor(posY);
            k = MathHelper.floor(posZ);
            boolean flag = false;
            for (int l = 0; !flag && l <= 20 && l >= -20; l = l <= 0 ? 1 - l : 0 - l) {
                for (i1 = 0; !flag && i1 <= 20 && i1 >= -20; i1 = i1 <= 0 ? 1 - i1 : 0 - i1) {
                    for (int j1 = 0; !flag && j1 <= 20 && j1 >= -20; j1 = j1 <= 0 ? 1 - j1 : 0 - j1) {
                        BlockPos pos = new BlockPos(i + i1, j + l, k + j1);
                        if (world.getBlockState(pos).getBlock() == Blocks.MONSTER_EGG) {
                            world.destroyBlock(pos, false);
                            EntitySilverfishMinion entitysilverfish = new EntitySilverfishMinion(world);
                            entitysilverfish.setLocationAndAngles(i + i1 + 0.5D, j + l, k + j1 + 0.5D, 0.0F, 0.0F);
                            entitysilverfish.onInitialSpawn(world.getDifficultyForLocation(pos), null);
                            if (!world.isRemote)
                                world.spawnEntity(entitysilverfish);
                            world.createExplosion(entitysilverfish, entitysilverfish.posX, entitysilverfish.posY, entitysilverfish.posZ, 2F, false);
                            entitysilverfish.spawnExplosionParticle();
                            world.setBlockToAir(pos);
                            if (rand.nextBoolean()) {
                                flag = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

        super.onLivingUpdate();
    }

    public String fetchAchievement() {
        return TitanTrophies.omegafish;
    }

    public boolean strikeFromOmegafish(DamageSource source, float amount) {
        if (source.getTrueSource() != null && source.getTrueSource() instanceof EntityPlayer && source.canHarmInCreative()) {
            triggerAudioGiant(fetchHurtAudioGiant(), getSoundVolume(), getSoundPitch());
            stunnedIs = true;
            setAttackTarget(null);
        }

        if (isEntityInvulnerable(source) || burrowingIs) {
            return false;
        }

        else if (((source.getTrueSource() instanceof EntitySilverfishMinion)) || ((source.getTrueSource() instanceof EntitySilverfishTitan))) {
            return false;
        }

        else {
            recentlyHit = 200;
            Entity entity = source.getTrueSource();
            if (((entity instanceof EntityLivingBase)) && (!isEntityInvulnerable(source)) && (amount > 25.0F)) {
                List<?> list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(100.0D, 100.0D, 100.0D));
                for (int i = 0; i < list.size(); i++) {
                    Entity mobRef1 = (Entity)list.get(i);
                    if ((mobRef1 instanceof EntitySilverfishTitan)) {
                        EntitySilverfishTitan entitypigzombie = (EntitySilverfishTitan)mobRef1;
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
        return true;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        return strikeFromOmegafish(source, amount);
    }

    protected boolean isMovementBlocked() {
        return (subduedIs) && (getPassengers().isEmpty()) ? true : super.isMovementBlocked();
    }

    public void refreshPosRider() {
        if (!getPassengers().isEmpty()) {
            Entity rider = getPassengers().get(0);
            double d8 = 0.4D + fetchPowerExtra() * 0.05D;
            Vec3d vec3 = getLook(1.0F);
            double dx = vec3.x * d8;
            double dz = vec3.z * d8;
            rider.setPosition(posX + dx, posY + (burrowingIs ? 1D : (0.3125D * fetchScaleMultiplierGiant())), posZ + dz);
        }
    }

    public boolean processInteract(EntityPlayer arg70085n1, EnumHand hand) {
        ItemStack itemstack = arg70085n1.inventory.getCurrentItem();
        arg70085n1.swingArm(EnumHand.MAIN_HAND);
        if ((stunnedIs) && (!subduedIs)) {
            if ((itemstack != null) && (itemstack.getItem() == Items.GOLDEN_APPLE)) {
                subduedIs = true;
                SoundHandler.whenPlayMob("random.levelup", world, this, 10.0F, 1.0F);
                arg70085n1.sendMessage(new TextComponentString(getCustomNameTag() + " has been subdued by " + arg70085n1.getName()));
                return super.processInteract(arg70085n1, hand);
            }
        }

        else if (subduedIs) {
            if ((itemstack != null && itemstack.isEmpty()) && (arg70085n1.getRidingEntity() == null)) {
                arg70085n1.startRiding(this);
            }

            else if (itemstack != null && !itemstack.isEmpty()) {
                if (itemstack.getItem() == Items.DIAMOND) {
                    AnimationAPI.sendAnimPacket(this, 9);
                    setAnimID(9);
                }

                if (itemstack.getItem() == Items.IRON_SHOVEL) {
                    if (burrowingIs) {
                        AnimationAPI.sendAnimPacket(this, 2);
                        setAnimID(2);
                    }

                    else {
                        AnimationAPI.sendAnimPacket(this, 1);
                        setAnimID(1);
                    }
                }

                if (itemstack.getItem() == Items.COOKED_CHICKEN) {
                    AnimationAPI.sendAnimPacket(this, 3);
                    setAnimID(3);
                }

                if (itemstack.getItem() == Items.BONE) {
                    switch (rand.nextInt(2)) {
                        case 0:AnimationAPI.sendAnimPacket(this, 5);
                            setAnimID(5);
                            break;
                        case 1:AnimationAPI.sendAnimPacket(this, 6);
                            setAnimID(6);
                    }
                }
            }
        }

        return false;
    }

    protected void inactActionDeath() {
        if (!world.isRemote) {
            triggerAudioGiant("mob.silverfish.death", getSoundVolume(), getSoundPitch());
            if ((world.getGameRules().getBoolean("doMobLoot"))) {
                dropFewItems(true, 0);
                dropEquipment(true, 0);
            }

            EntityTitanSpirit entitytitan = new EntityTitanSpirit(world);
            entitytitan.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
            world.spawnEntity(entitytitan);
            entitytitan.assignHuntingVessel(false);
            entitytitan.assignKindSpirit(1);
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
        burrowingIs = false;
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

        if (deathTicks >= 300) {
            setInvulTime(getInvulTime() + 8);
            float f = (rand.nextFloat() - 0.5F) * 8.0F;
            float f1 = (rand.nextFloat() - 0.5F) * 4.0F;
            float f2 = (rand.nextFloat() - 0.5F) * 8.0F;
            world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX + f, posY + 2.0D + f1, posZ + f2, 0.0D, 0.0D, 0.0D);
        }

        if (getInvulTime() >= fetchHoldThreash()) {
            setDead();
        }
    }

    public boolean attackEntityFromPart(EntityTitanPart arg70965n1, DamageSource source, float amount) {
        funcE82195(source, amount);
        return true;
    }

    protected boolean funcE82195(DamageSource arg82195n1, float arg82195n2) {
        return strikeFromOmegafish(arg82195n1, arg82195n2);
    }

    public World funcD82194() {
        return world;
    }

    protected void updateAITasks() {
        super.updateAITasks();
    }

    public IEntityLivingData onInitialSpawn(net.minecraft.world.DifficultyInstance difficulty, IEntityLivingData arg180482n2) {
        Object p_180482_2_1 = super.onInitialSpawn(difficulty, arg180482n2);
        assignWaiting(true);
        return (IEntityLivingData)p_180482_2_1;
    }

    public void travel(float arg70612n1, float arg70612n2, float arg70612n3) {
        Entity rider = getPassengers().isEmpty() ? null : getPassengers().get(0);
        if ((rider != null) && ((rider instanceof EntityLivingBase)) && (subduedIs)) {
            setAttackTarget(null);
            rotationPitch = rider.rotationPitch;
            rotationYawHead = ((EntityLivingBase)rider).rotationYawHead;
            setRotation(rotationYaw, rotationPitch);
            arg70612n1 = ((EntityLivingBase)rider).moveStrafing;
            arg70612n3 = ((EntityLivingBase)rider).moveForward;
            if (((EntityLivingBase)rider).moveForward > 0.0F) {
                appendVelocityGiant(-MathHelper.sin(((EntityLivingBase)rider).rotationYawHead * 3.1415927F / 180.0F) * getSpeed(), 0.0D, MathHelper.cos(((EntityLivingBase)rider).rotationYawHead * 3.1415927F / 180.0F) * getSpeed());
            }

            if (((EntityLivingBase)rider).moveForward < 0.0F) {
                appendVelocityGiant(-MathHelper.sin(((EntityLivingBase)rider).rotationYawHead * 3.1415927F / 180.0F) * -getSpeed(), 0.0D, MathHelper.cos(((EntityLivingBase)rider).rotationYawHead * 3.1415927F / 180.0F) * -getSpeed());
            }

            if ((onGround) && (((EntityLivingBase)rider).rotationPitch < -80.0F)) {
                jump();
            }

            if (!world.isRemote) {
                super.travel(arg70612n1, arg70612n2, arg70612n3);
            }

            prevLimbSwingAmount = limbSwingAmount;
            double do1 = posX - prevPosX;
            double do0 = posZ - prevPosZ;
            float f4 = MathHelper.sqrt(do1 * do1 + do0 * do0) * 4.0F;
            if (f4 > 1.0F) {
                f4 = 1.0F;
            }

            limbSwingAmount += (f4 - limbSwingAmount) * 0.4F;
            limbSwing += limbSwingAmount;
        }

        else {
            super.travel(arg70612n1, arg70612n2, arg70612n3);
        }
    }

    protected EntityLiving fetchMinion() {
        return new EntitySilverfishMinion(world);
    }

    protected double cap() {
        return super.cap();
    }
}
