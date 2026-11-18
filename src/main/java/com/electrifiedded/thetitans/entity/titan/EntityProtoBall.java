package com.electrifiedded.thetitans.entity.titan;

import com.electrifiedded.thetitans.entity.minion.EntityGhastGuard;
import com.electrifiedded.thetitans.entity.minion.EntityGiantZombieBetter;
import com.electrifiedded.thetitans.entity.minion.EntityPigZombieMinion;
import com.electrifiedded.thetitans.entity.minion.EntityZombieMinion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityProtoBall extends EntityThrowable {
    public EntityProtoBall(World worldIn) {
        super(worldIn);
        setSize(3.0F, 3.0F);
        this.motionY += 0.25F;
    }

    public EntityProtoBall(World worldIn, EntityLivingBase p_i1780_2_) {
        super(worldIn, p_i1780_2_);
        setSize(3.0F, 3.0F);
        this.motionY += 0.25F;
    }

    public EntityProtoBall(World worldIn, double p_i1781_2_, double p_i1781_4_, double p_i1781_6_) {
        super(worldIn, p_i1781_2_, p_i1781_4_, p_i1781_6_);
        setSize(3.0F, 3.0F);
        this.motionY += 0.25F;
    }

    public void onUpdate() {
        super.onUpdate();
        for (int i = 0; i < 15; i++) {
            float f = (this.rand.nextFloat() - 0.5F) * this.width;
            float f1 = (this.rand.nextFloat() - 0.5F) * this.height;
            float f2 = (this.rand.nextFloat() - 0.5F) * this.width;
            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + f, this.posY + f1, this.posZ + f2, 0.0D, 0.0D, 0.0D);
            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + f, this.posY + f1, this.posZ + f2, 0.0D, 0.0D, 0.0D);
            this.world.spawnParticle(EnumParticleTypes.FLAME, this.posX + f, this.posY + f1, this.posZ + f2, 0.0D, 0.0D, 0.0D);
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX + f, this.posY + f1, this.posZ + f2, 0.0D, 0.0D, 0.0D);
            this.world.spawnParticle(EnumParticleTypes.LAVA, this.posX + f, this.posY + f1, this.posZ + f2, 0.0D, 0.0D, 0.0D);
        }
    }

    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            if (this.getThrower() != null && this.getThrower() instanceof EntityTitan && result.entityHit != null && result.entityHit instanceof EntityLivingBase)
                ((EntityTitan)this.getThrower()).strikeMobChoosen(result.entityHit, 75, 2);
            if (this.getThrower() != null && this.getThrower() instanceof EntityPigZombieTitan) {
                if (this.rand.nextInt(5) == 0) {
                    EntityGhastGuard entitychicken = new EntityGhastGuard(this.world);
                    entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, -this.rotationYaw, -this.rotationPitch);
                    this.world.spawnEntity(entitychicken);
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                    ++entitychicken.motionY;
                    boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
                    this.world.newExplosion(entitychicken, entitychicken.posX, entitychicken.posY + 6D, entitychicken.posZ, 12F, false, flag);
                    entitychicken.master = (EntityPigZombieTitan) this.getThrower();
                }

                else {
                    switch (this.rand.nextInt(4)) {
                        case 0:for (int l1 = 0; l1 <= 5; l1++) {
                                EntityPigZombieMinion entitychicken = new EntityPigZombieMinion(this.world);
                                entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, -this.rotationYaw, -this.rotationPitch);
                                this.world.spawnEntity(entitychicken);
                                entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                                entitychicken.setMinionType(3);
                                boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
                                this.world.newExplosion(entitychicken, entitychicken.posX, entitychicken.posY + 2D, entitychicken.posZ, 6F, false, flag);
                                entitychicken.master = (EntityPigZombieTitan) this.getThrower();
                            }

                            break;
                        case 1:for (int l1 = 0; l1 <= 10; l1++) {
                                EntityPigZombieMinion entitychicken = new EntityPigZombieMinion(this.world);
                                entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, -this.rotationYaw, -this.rotationPitch);
                                this.world.spawnEntity(entitychicken);
                                entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                                entitychicken.setMinionType(2);
                                boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
                                this.world.newExplosion(entitychicken, entitychicken.posX, entitychicken.posY + 2D, entitychicken.posZ, 4F, false, flag);
                                entitychicken.master = (EntityPigZombieTitan) this.getThrower();
                            }

                            break;
                        case 2:for (int l1 = 0; l1 <= 20; l1++) {
                                EntityPigZombieMinion entitychicken = new EntityPigZombieMinion(this.world);
                                entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, -this.rotationYaw, -this.rotationPitch);
                                this.world.spawnEntity(entitychicken);
                                entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                                entitychicken.setMinionType(1);
                                boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
                                this.world.newExplosion(entitychicken, entitychicken.posX, entitychicken.posY + 2D, entitychicken.posZ, 3F, false, flag);
                                entitychicken.master = (EntityPigZombieTitan) this.getThrower();
                            }

                            break;
                        case 3:for (int l1 = 0; l1 <= 40; l1++) {
                                EntityPigZombieMinion entitychicken = new EntityPigZombieMinion(this.world);
                                entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, -this.rotationYaw, -this.rotationPitch);
                                this.world.spawnEntity(entitychicken);
                                entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                                entitychicken.setMinionType(0);
                                boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
                                this.world.newExplosion(entitychicken, entitychicken.posX, entitychicken.posY + 2D, entitychicken.posZ, 2F, false, flag);
                                entitychicken.master = (EntityPigZombieTitan) this.getThrower();
                            }
                    }
                }
            }

            if (this.getThrower() != null && this.getThrower() instanceof EntityZombieTitan) {
                if (this.rand.nextInt(5) == 0) {
                    EntityGiantZombieBetter entitychicken = new EntityGiantZombieBetter(this.world);
                    entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, -this.rotationYaw, -this.rotationPitch);
                    this.world.spawnEntity(entitychicken);
                    entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                    boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
                    this.world.newExplosion(entitychicken, entitychicken.posX, entitychicken.posY + 6D, entitychicken.posZ, 12F, false, flag);
                    entitychicken.master = (EntityZombieTitan) this.getThrower();
                }

                else {
                    switch (this.rand.nextInt(4)) {
                        case 0:for (int l1 = 0; l1 <= 5; l1++) {
                                EntityZombieMinion entitychicken = new EntityZombieMinion(this.world);
                                entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, -this.rotationYaw, -this.rotationPitch);
                                this.world.spawnEntity(entitychicken);
                                entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                                entitychicken.setMinionType(3);
                                boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
                                this.world.newExplosion(entitychicken, entitychicken.posX, entitychicken.posY + 2D, entitychicken.posZ, 6F, false, flag);
                                entitychicken.master = (EntityZombieTitan) this.getThrower();
                            }

                            break;
                        case 1:for (int l1 = 0; l1 <= 10; l1++) {
                                EntityZombieMinion entitychicken = new EntityZombieMinion(this.world);
                                entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, -this.rotationYaw, -this.rotationPitch);
                                this.world.spawnEntity(entitychicken);
                                entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                                entitychicken.setMinionType(2);
                                boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
                                this.world.newExplosion(entitychicken, entitychicken.posX, entitychicken.posY + 2D, entitychicken.posZ, 4F, false, flag);
                                entitychicken.master = (EntityZombieTitan) this.getThrower();
                            }

                            break;
                        case 2:for (int l1 = 0; l1 <= 20; l1++) {
                                EntityZombieMinion entitychicken = new EntityZombieMinion(this.world);
                                entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, -this.rotationYaw, -this.rotationPitch);
                                this.world.spawnEntity(entitychicken);
                                entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                                entitychicken.setMinionType(1);
                                boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
                                this.world.newExplosion(entitychicken, entitychicken.posX, entitychicken.posY + 2D, entitychicken.posZ, 3F, false, flag);
                                entitychicken.master = (EntityZombieTitan) this.getThrower();
                            }

                            break;
                        case 3:for (int l1 = 0; l1 <= 40; l1++) {
                                EntityZombieMinion entitychicken = new EntityZombieMinion(this.world);
                                entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, -this.rotationYaw, -this.rotationPitch);
                                this.world.spawnEntity(entitychicken);
                                entitychicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
                                entitychicken.setMinionType(0);
                                boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
                                this.world.newExplosion(entitychicken, entitychicken.posX, entitychicken.posY + 2D, entitychicken.posZ, 2F, false, flag);
                                entitychicken.master = (EntityZombieTitan) this.getThrower();
                            }
                    }
                }
            }
        }

        setDead();
    }
}
