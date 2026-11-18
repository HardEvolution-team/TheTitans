package com.electrifiedded.thetitans.entity.mob;

import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.titan.EntityXPBomb;
import com.electrifiedded.thetitans.registry.TitanGear;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.List;

public class EntityBurningMobzilla extends EntityMob {
    private static final int STRIKE_GODZILLA = 40;
    private int jumpedV = 0;
    private int clockJump = 0;

    public EntityBurningMobzilla(World worldIn) {
        super(worldIn);
        this.setSize(9.9F, 25.0F);
        this.experienceValue = 50000;
        this.isImmuneToFire = true;
        this.enablePersistence();
        this.stepHeight = 6.0F;
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 32.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, true));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25000000.0D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(STRIKE_GODZILLA * 500);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(80.0D);
    }

    private void performHarmJump(double X, double Y, double Z, double dist, double damage, int knock) {
        AxisAlignedBB bb = new AxisAlignedBB(X - dist, Y - 10.0D, Z - dist, X + dist, Y + 10.0D, Z + dist);
        List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
        for (EntityLivingBase e : list) {
            if (e != null && e != this && e.isEntityAlive()) {
                DamageSource ex = DamageSource.causeExplosionDamage((Explosion)null);
                ex.setExplosion();
                e.attackEntityFrom(ex, (float)damage / 2.0F);
                e.attackEntityFrom(DamageSource.FALL, (float)damage / 2.0F);
                this.world.playSound(null, e.posX, e.posY, e.posZ, net.minecraft.init.SoundEvents.ENTITY_GENERIC_EXPLODE,
                        net.minecraft.util.SoundCategory.HOSTILE, 0.85F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.5F);
                if (knock != 0) {
                    double ks = 3.5D;
                    double inair = 0.75D;
                    float f3 = (float)Math.atan2(e.posZ - this.posZ, e.posX - this.posX);
                    e.addVelocity(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
                    e.setFire(100);
                }
            }
        }
    }

    private void performStrikeLightning(EntityLivingBase e) {
        if (e == null) return;
        float dmg = 600.0F;
        e.attackEntityFrom(DamageSource.causeMobDamage(this), dmg);
        e.attackEntityFrom(DamageSourceExtra.lightningBolt, dmg / 5.0F);
        e.attackEntityFrom(DamageSource.causeMobDamage(this), dmg / 10.0F);
        e.setFire(5);
        if (this.world.isRemote) {
            for (int i = 0; i < 20; i++) {
                this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, e.posX + rand.nextFloat() - rand.nextFloat(), e.posY + rand.nextFloat(), e.posZ + rand.nextFloat() - rand.nextFloat(), 0, 0, 0);
                this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, e.posX + rand.nextFloat() - rand.nextFloat(), e.posY + rand.nextFloat(), e.posZ + rand.nextFloat() - rand.nextFloat(), 0, 0, 0);
            }
            return;
        }
        this.world.newExplosion(this, e.posX, e.posY, e.posZ, 6.0F, true, this.world.getGameRules().getBoolean("mobGriefing"));
        this.world.addWeatherEffect(new EntityLightningBolt(this.world, e.posX, e.posY + 1.0D, e.posZ, false));
        this.world.addWeatherEffect(new EntityLightningBolt(this.world, this.posX, this.posY + 15.0D, this.posZ, false));
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!this.world.isRemote) {
            if (this.clockJump > 0) this.clockJump--;

            // original: land-detect via downward motion, then AoE smash at 3 radii
            if (this.motionY < -0.95D) this.jumpedV = 1;
            if (this.motionY < -1.5D) this.jumpedV = 2;
            if (this.jumpedV != 0 && this.motionY > -0.1D) {
                double k = this.jumpedV == 2 ? 1.5D : 1.0D;
                performHarmJump(this.posX, this.posY, this.posZ, 10.0D, STRIKE_GODZILLA * 2 * k, 0);
                performHarmJump(this.posX, this.posY, this.posZ, 15.0D, STRIKE_GODZILLA * k, 0);
                performHarmJump(this.posX, this.posY, this.posZ, 25.0D, STRIKE_GODZILLA / 2 * k, 0);
                this.jumpedV = 0;
            }

            EntityLivingBase target = getAttackTarget();
            if (target != null && target.isEntityAlive()) {
                faceEntity(target, 10.0F, 10.0F);
                double distSq = getDistanceSq(target);
                // periodic jump toward target (original jumpMobAt + jump_timer gate)
                if (this.clockJump == 0 && distSq > 25.0D) {
                    this.motionY += 1.0D;
                    this.posY += 1.0D;
                    double d1 = target.posX - this.posX;
                    double d2 = target.posZ - this.posZ;
                    float d = (float)Math.atan2(d2, d1);
                    double h = Math.sqrt(d1 * d1 + d2 * d2);
                    this.motionX += h * 0.075D * Math.cos(d);
                    this.motionZ += h * 0.075D * Math.sin(d);
                    this.isAirBorne = true;
                    this.clockJump = 30;
                }
                // periodic lightning smite
                if (rand.nextInt(60) == 0) {
                    performStrikeLightning(target);
                }
            }

            // original self-heal drip
            if (rand.nextInt(30) == 0 && getHealth() < getMaxHealth()) {
                heal(10.0F);
            }
        }
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        if (this.world.isRemote) return;
        for (int i = 0; i < 8; i++) {
            EntityXPBomb bomb = new EntityXPBomb(this.world, this.posX, this.posY + 2.0D, this.posZ);
            bomb.setPosition(this.posX, this.posY + 2.0D, this.posZ);
            ++bomb.motionY;
            bomb.assignTotalXP(24000);
            this.world.spawnEntity(bomb);
        }
        shedRandStack(new ItemStack(TitanGear.withrenium, 64));
        shedRandStack(new ItemStack(Items.COOKED_BEEF, 30 + rand.nextInt(30)));
        shedRandStack(new ItemStack(Items.BONE, 10 + rand.nextInt(10)));
        shedRandStack(new ItemStack(Items.FIRE_CHARGE, 20 + rand.nextInt(20)));
    }

    private void shedRandStack(ItemStack stack) {
        net.minecraft.entity.item.EntityItem item = new net.minecraft.entity.item.EntityItem(this.world,
                this.posX + rand.nextInt(10) - rand.nextInt(10), this.posY + 4.0D + rand.nextInt(10),
                this.posZ + rand.nextInt(10) - rand.nextInt(10), stack);
        this.world.spawnEntity(item);
    }

    protected boolean canDespawn() {
        return false;
    }
}
