package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.minion.EntityGhastMinion;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

public class EntityGrowthSerum extends EntityThrowable {
    public EntityGrowthSerum(World worldIn) {
        super(worldIn);
        setSize(0.5F, 0.5F);
    }

    public EntityGrowthSerum(World worldIn, EntityLivingBase p_i1780_2_) {
        super(worldIn, p_i1780_2_);
        setSize(0.5F, 0.5F);
    }

    public EntityGrowthSerum(World worldIn, double p_i1781_2_, double p_i1781_4_, double p_i1781_6_) {
        super(worldIn, p_i1781_2_, p_i1781_4_, p_i1781_6_);
        setSize(0.5F, 0.5F);
    }

    protected void onImpact(RayTraceResult arg70184n1) {
        if (!this.world.isRemote) {
            if (arg70184n1.entityHit != null) {
                if ((arg70184n1.entityHit instanceof EntityZombie)) {
                    if ((arg70184n1.entityHit instanceof EntityPigZombie)) {
                        EntityPigZombieTitan entitychicken = new EntityPigZombieTitan(this.world);
                        entitychicken.setLocationAndAngles(arg70184n1.entityHit.posX, arg70184n1.entityHit.posY, arg70184n1.entityHit.posZ, arg70184n1.entityHit.rotationYaw, 0.0F);
                        arg70184n1.entityHit.setDead();
                        this.world.spawnEntity(entitychicken);
                        entitychicken.playSound(SoundEvents.BLOCK_PORTAL_TRAVEL, 10000.0F, 1F);
                        entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                        // custom titan-birth sound (registered, resolvable via TitanAudio)
                        com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanBirth", this.world, entitychicken, 1000.0F, 1F);
                        entitychicken.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                        entitychicken.setChild(((EntityPigZombie)arg70184n1.entityHit).isChild());
                    }

                    else {
                        EntityZombieTitan entitychicken = new EntityZombieTitan(this.world);
                        entitychicken.setLocationAndAngles(arg70184n1.entityHit.posX, arg70184n1.entityHit.posY, arg70184n1.entityHit.posZ, arg70184n1.entityHit.rotationYaw, 0.0F);
                        arg70184n1.entityHit.setDead();
                        this.world.spawnEntity(entitychicken);
                        entitychicken.playSound(SoundEvents.BLOCK_PORTAL_TRAVEL, 10000.0F, 1F);
                        // custom titan-birth sound (registered, resolvable via TitanAudio)
                        com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanBirth", this.world, entitychicken, 1000.0F, 1F);
                        entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                        entitychicken.setChild(((EntityZombie)arg70184n1.entityHit).isChild());
                        entitychicken.assignVillager(arg70184n1.entityHit instanceof EntityZombieVillager);
                    }
                }

                else if ((arg70184n1.entityHit instanceof EntitySkeleton)) {
                    EntitySkeletonTitan entitychicken = new EntitySkeletonTitan(this.world);
                    entitychicken.setLocationAndAngles(arg70184n1.entityHit.posX, arg70184n1.entityHit.posY, arg70184n1.entityHit.posZ, arg70184n1.entityHit.rotationYaw, 0.0F);
                    arg70184n1.entityHit.setDead();
                    entitychicken.setSkeletonType(arg70184n1.entityHit instanceof EntityWitherSkeleton ? 1 : 0);
                    this.world.spawnEntity(entitychicken);
                    entitychicken.playSound(SoundEvents.BLOCK_PORTAL_TRAVEL, 10000.0F, 1F);
                    // custom titan-birth sound (registered, resolvable via TitanAudio)
                    com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanBirth", this.world, entitychicken, 1000.0F, 1F);
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                    if ((arg70184n1.entityHit.getRidingEntity() != null) && ((arg70184n1.entityHit.getRidingEntity() instanceof EntitySpider))) {
                        EntitySpiderTitan entitychicken1 = new EntitySpiderTitan(this.world);
                        entitychicken1.setLocationAndAngles(arg70184n1.entityHit.posX, arg70184n1.entityHit.posY, arg70184n1.entityHit.posZ, arg70184n1.entityHit.rotationYaw, 0.0F);
                        this.world.spawnEntity(entitychicken1);
                        entitychicken.startRiding(entitychicken1);
                        arg70184n1.entityHit.getRidingEntity().setDead();
                        entitychicken1.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken1)), (IEntityLivingData)null);
                    }
                }

                else if (arg70184n1.entityHit instanceof EntityCreeper) {
                    EntityCreeperTitan entitychicken = new EntityCreeperTitan(this.world);
                    entitychicken.setLocationAndAngles(arg70184n1.entityHit.posX, arg70184n1.entityHit.posY, arg70184n1.entityHit.posZ, arg70184n1.entityHit.rotationYaw, 0.0F);
                    arg70184n1.entityHit.setDead();
                    this.world.spawnEntity(entitychicken);
                    entitychicken.playSound(SoundEvents.BLOCK_PORTAL_TRAVEL, 10000.0F, 1F);
                    // custom titan-birth sound (registered, resolvable via TitanAudio)
                    com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanBirth", this.world, entitychicken, 1000.0F, 1F);
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                    entitychicken.setPowered(((EntityCreeper)arg70184n1.entityHit).getPowered());
                }

                else if ((arg70184n1.entityHit instanceof EntityBlaze)) {
                    EntityBlazeTitan entitychicken = new EntityBlazeTitan(this.world);
                    entitychicken.setLocationAndAngles(arg70184n1.entityHit.posX, arg70184n1.entityHit.posY, arg70184n1.entityHit.posZ, arg70184n1.entityHit.rotationYaw, 0.0F);
                    arg70184n1.entityHit.setDead();
                    this.world.spawnEntity(entitychicken);
                    entitychicken.playSound(SoundEvents.BLOCK_PORTAL_TRAVEL, 10000.0F, 1F);
                    // custom titan-birth sound (registered, resolvable via TitanAudio)
                    com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanBirth", this.world, entitychicken, 1000.0F, 1F);
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                }

                else if ((arg70184n1.entityHit instanceof EntitySpider)) {
                    if ((arg70184n1.entityHit instanceof EntityCaveSpider)) {
                        EntityCaveSpiderTitan entitychicken = new EntityCaveSpiderTitan(this.world);
                        entitychicken.setLocationAndAngles(arg70184n1.entityHit.posX, arg70184n1.entityHit.posY, arg70184n1.entityHit.posZ, arg70184n1.entityHit.rotationYaw, 0.0F);
                        arg70184n1.entityHit.setDead();
                        this.world.spawnEntity(entitychicken);
                        entitychicken.playSound(SoundEvents.BLOCK_PORTAL_TRAVEL, 10000.0F, 1F);
                        // custom titan-birth sound (registered, resolvable via TitanAudio)
                        com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanBirth", this.world, entitychicken, 1000.0F, 1.25F);
                        entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                    }

                    else {
                        EntitySpiderTitan entitychicken = new EntitySpiderTitan(this.world);
                        entitychicken.setLocationAndAngles(arg70184n1.entityHit.posX, arg70184n1.entityHit.posY, arg70184n1.entityHit.posZ, arg70184n1.entityHit.rotationYaw, 0.0F);
                        arg70184n1.entityHit.setDead();
                        this.world.spawnEntity(entitychicken);
                        entitychicken.playSound(SoundEvents.BLOCK_PORTAL_TRAVEL, 10000.0F, 1F);
                        // custom titan-birth sound (registered, resolvable via TitanAudio)
                        com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanBirth", this.world, entitychicken, 1000.0F, 1.25F);
                        entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                        if (!arg70184n1.entityHit.getPassengers().isEmpty() && (arg70184n1.entityHit.getPassengers().get(0) instanceof EntitySkeleton)) {
                            net.minecraft.entity.Entity rider = arg70184n1.entityHit.getPassengers().get(0);
                            EntitySkeletonTitan entitychicken1 = new EntitySkeletonTitan(this.world);
                            entitychicken1.setLocationAndAngles(arg70184n1.entityHit.posX, arg70184n1.entityHit.posY, arg70184n1.entityHit.posZ, arg70184n1.entityHit.rotationYaw, 0.0F);
                            entitychicken1.setSkeletonType(rider instanceof EntityWitherSkeleton ? 1 : 0);
                            this.world.spawnEntity(entitychicken1);
                            entitychicken1.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken1)), (IEntityLivingData)null);
                            // custom titan-birth sound (registered, resolvable via TitanAudio)
                            com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanBirth", this.world, entitychicken, 1000.0F, 1.0F);
                            entitychicken1.startRiding(entitychicken);
                            rider.setDead();
                        }
                    }
                }

                else if (arg70184n1.entityHit instanceof EntityEnderman) {
                    EntityEnderColossus entitychicken = new EntityEnderColossus(this.world);
                    entitychicken.setLocationAndAngles(arg70184n1.entityHit.posX, arg70184n1.entityHit.posY, arg70184n1.entityHit.posZ, arg70184n1.entityHit.rotationYaw, 0.0F);
                    arg70184n1.entityHit.setDead();
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                    this.world.spawnEntity(entitychicken);
                    entitychicken.playSound(SoundEvents.BLOCK_PORTAL_TRAVEL, 10000.0F, 1F);
                    // custom titan-birth sound (registered, resolvable via TitanAudio)
                    com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanBirth", this.world, entitychicken, 1000.0F, 0.875F);
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                }

                else if (arg70184n1.entityHit instanceof EntityGhast || arg70184n1.entityHit instanceof EntityGhastMinion) {
                    EntityGhastTitan entitychicken = new EntityGhastTitan(this.world);
                    entitychicken.setLocationAndAngles(arg70184n1.entityHit.posX, arg70184n1.entityHit.posY, arg70184n1.entityHit.posZ, arg70184n1.entityHit.rotationYaw, 0.0F);
                    arg70184n1.entityHit.setDead();
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                    this.world.spawnEntity(entitychicken);
                    entitychicken.playSound(SoundEvents.BLOCK_PORTAL_TRAVEL, 10000.0F, 1F);
                    // custom titan-birth sound (registered, resolvable via TitanAudio)
                    com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanBirth", this.world, entitychicken, 1000.0F, 0.875F);
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                }

                else if ((arg70184n1.entityHit instanceof EntitySnowman)) {
                    EntitySnowGolemTitan entitychicken = new EntitySnowGolemTitan(this.world);
                    entitychicken.setLocationAndAngles(arg70184n1.entityHit.posX, arg70184n1.entityHit.posY, arg70184n1.entityHit.posZ, arg70184n1.entityHit.rotationYaw, 0.0F);
                    arg70184n1.entityHit.setDead();
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                    this.world.spawnEntity(entitychicken);
                    entitychicken.playSound(SoundEvents.ENTITY_ITEM_BREAK, 10000.0F, 0.5F);
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                }

                else if ((arg70184n1.entityHit instanceof EntityGargoyle)) {
                    EntityGargoyleTitan entitychicken = new EntityGargoyleTitan(this.world);
                    entitychicken.setLocationAndAngles(arg70184n1.entityHit.posX, arg70184n1.entityHit.posY, arg70184n1.entityHit.posZ, arg70184n1.entityHit.rotationYaw, 0.0F);
                    arg70184n1.entityHit.setDead();
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                    this.world.spawnEntity(entitychicken);
                    entitychicken.playSound(SoundEvents.ENTITY_ITEM_BREAK, 10000.0F, 0.5F);
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                }

                else if ((arg70184n1.entityHit instanceof EntityIronGolem)) {
                    EntityIronGolemTitan entitychicken = new EntityIronGolemTitan(this.world);
                    entitychicken.setLocationAndAngles(arg70184n1.entityHit.posX, arg70184n1.entityHit.posY, arg70184n1.entityHit.posZ, arg70184n1.entityHit.rotationYaw, 0.0F);
                    arg70184n1.entityHit.setDead();
                    entitychicken.playSound(SoundEvents.ENTITY_ITEM_BREAK, 10000.0F, 0.5F);
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                    entitychicken.setPlayerCreated(((EntityIronGolem)arg70184n1.entityHit).isPlayerCreated());
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                    this.world.spawnEntity(entitychicken);
                }

                else if ((arg70184n1.entityHit instanceof EntitySilverfish)) {
                    EntitySilverfishTitan entitychicken = new EntitySilverfishTitan(this.world);
                    entitychicken.setLocationAndAngles(arg70184n1.entityHit.posX, arg70184n1.entityHit.posY, arg70184n1.entityHit.posZ, arg70184n1.entityHit.rotationYaw, 0.0F);
                    arg70184n1.entityHit.setDead();
                    this.world.spawnEntity(entitychicken);
                    entitychicken.playSound(SoundEvents.BLOCK_PORTAL_TRAVEL, 10000.0F, 1F);
                    // custom titan-birth sound (registered, resolvable via TitanAudio)
                    com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanBirth", this.world, entitychicken, 1000.0F, 1.25F);
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                }

                else if ((arg70184n1.entityHit instanceof EntitySlime)) {
                    if ((arg70184n1.entityHit instanceof EntityMagmaCube)) {
                        EntityMagmaCubeTitan entitychicken = new EntityMagmaCubeTitan(this.world);
                        entitychicken.setLocationAndAngles(arg70184n1.entityHit.posX, arg70184n1.entityHit.posY, arg70184n1.entityHit.posZ, arg70184n1.entityHit.rotationYaw, 0.0F);
                        arg70184n1.entityHit.setDead();
                        this.world.spawnEntity(entitychicken);
                        entitychicken.playSound(SoundEvents.BLOCK_PORTAL_TRAVEL, 10000.0F, 1F);
                        // custom titan-birth sound (registered, resolvable via TitanAudio)
                        com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanBirth", this.world, entitychicken, 1000.0F, 1.5F);
                        entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                        entitychicken.setSlimeSize(((EntityMagmaCube)arg70184n1.entityHit).getSlimeSize());
                    }

                    else {
                        EntitySlimeTitan entitychicken = new EntitySlimeTitan(this.world);
                        entitychicken.setLocationAndAngles(arg70184n1.entityHit.posX, arg70184n1.entityHit.posY, arg70184n1.entityHit.posZ, arg70184n1.entityHit.rotationYaw, 0.0F);
                        arg70184n1.entityHit.setDead();
                        this.world.spawnEntity(entitychicken);
                        entitychicken.playSound(SoundEvents.BLOCK_PORTAL_TRAVEL, 10000.0F, 1F);
                        // custom titan-birth sound (registered, resolvable via TitanAudio)
                        com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanBirth", this.world, entitychicken, 1000.0F, 1.5F);
                        entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                        entitychicken.setSlimeSize(((EntitySlime)arg70184n1.entityHit).getSlimeSize());
                    }
                }

                else if ((arg70184n1.entityHit instanceof EntityLivingBase)) {
                    this.playSound(SoundEvents.ENTITY_PLAYER_HURT, 2F, 2F);
                    ((EntityLivingBase)arg70184n1.entityHit).setFire(20);
                    ((EntityLivingBase)arg70184n1.entityHit).attackEntityFrom(DamageSourceExtra.wip, 2000F);
                    if (!this.world.isRemote)
                        this.dropItem(TitanGear.serumGrowth, 1);
                }
            }
        }

        if (!this.world.isRemote) {
            if (arg70184n1.entityHit == null)
                this.dropItem(TitanGear.serumGrowth, 1);
            this.playSound(SoundEvents.ENTITY_PLAYER_HURT, 2F, 2F);
            setDead();
        }
    }
}
