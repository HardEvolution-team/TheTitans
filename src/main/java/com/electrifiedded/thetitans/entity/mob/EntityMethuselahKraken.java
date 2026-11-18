package com.electrifiedded.thetitans.entity.mob;

import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.titan.EntityXPBomb;
import com.electrifiedded.thetitans.registry.TitanBlockSet;
import com.electrifiedded.thetitans.registry.TitanGear;

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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.List;

public class EntityMethuselahKraken extends EntityMob {
    private static final int STRIKE_KRAKEN = 40;
    private int totalBarrage = 0;

    public EntityMethuselahKraken(World worldIn) {
        super(worldIn);
        this.setSize(8.0F, 30.0F);
        this.experienceValue = 25000;
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
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1500000.0D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(STRIKE_KRAKEN * 370);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.37D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(80.0D);
    }

    public boolean canBreatheUnderwater() {
        return true;
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
                        net.minecraft.util.SoundCategory.HOSTILE, 1.0F, 0.5F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.25F);
                if (knock != 0) {
                    double ks = 2.75D;
                    double inair = 0.65D;
                    float f3 = (float)Math.atan2(e.posZ - this.posZ, e.posX - this.posX);
                    e.addVelocity(Math.cos(f3) * ks, inair, Math.sin(f3) * ks);
                }
            }
        }
    }

    private void launchBarrageIce(EntityLivingBase e, int count) {
        for (int i = 0; i < count; i++) {
            double r = 8.0D * (rand.nextFloat() - rand.nextFloat());
            EntityIceBall ib = new EntityIceBall(this.world, this);
            ib.setPosition(this.posX + r, this.posY - 4.0D, this.posZ + r);
            double dx = e.posX - ib.posX;
            double dy = e.posY + 0.25D - ib.posY;
            double dz = e.posZ - ib.posZ;
            double d0 = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (d0 < 0.1D) d0 = 0.1D;
            ib.accelerationX = dx / d0 * 0.3D;
            ib.accelerationY = dy / d0 * 0.3D;
            ib.accelerationZ = dz / d0 * 0.3D;
            this.world.spawnEntity(ib);
        }
    }

    private void performStrikeLightning(EntityLivingBase e) {
        e.attackEntityFrom(DamageSource.causeMobDamage(this), 100.0F);
        e.attackEntityFrom(DamageSourceExtra.lightningBolt, 20.0F);
        e.setFire(5);
        this.world.newExplosion(this, e.posX, e.posY, e.posZ, 3.0F, false, this.world.getGameRules().getBoolean("mobGriefing"));
        this.world.addWeatherEffect(new EntityLightningBolt(this.world, e.posX, e.posY + 1.0D, e.posZ, false));
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!this.world.isRemote) {
            if (this.ticksExisted % 80 == 0) this.totalBarrage = 8;

            EntityLivingBase target = getAttackTarget();
            if (target != null && target.isEntityAlive()) {
                faceEntity(target, 10.0F, 10.0F);
                double distSq = getDistanceSq(target);
                if (distSq < 600.0D) {
                    // close range: melee-ish AoE smash
                    if (rand.nextInt(2) == 0) {
                        performHarmJump(this.posX, this.posY, this.posZ, 15.0D, STRIKE_KRAKEN, 1);
                    }
                } else {
                    // long range: ice barrage + occasional lightning
                    if (this.totalBarrage > 0) {
                        launchBarrageIce(target, 3);
                        this.totalBarrage--;
                    }
                    if (rand.nextInt(65) == 0) {
                        performStrikeLightning(target);
                    }
                }
            }

            // original per-tick self-heal drip
            if (rand.nextInt(35) == 0 && getHealth() < getMaxHealth()) {
                heal(5.0F);
            }
        }
    }

    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        if (this.world.isRemote) return;
        for (int i = 0; i < 6; i++) {
            EntityXPBomb bomb = new EntityXPBomb(this.world, this.posX, this.posY + 2.0D, this.posZ);
            bomb.setPosition(this.posX, this.posY + 2.0D, this.posZ);
            ++bomb.motionY;
            bomb.assignTotalXP(18000);
            this.world.spawnEntity(bomb);
        }
        shedRandStack(new ItemStack(com.electrifiedded.thetitans.registry.TitanBlockSet.harcadium_block, 48));
        shedRandStack(new ItemStack(Items.GOLD_INGOT, 100 + rand.nextInt(100)));
        shedRandStack(new ItemStack(TitanGear.nuggetHarcadiumZ, 20 + rand.nextInt(20)));
    }

    private void shedRandStack(ItemStack stack) {
        net.minecraft.entity.item.EntityItem item = new net.minecraft.entity.item.EntityItem(this.world,
                this.posX + rand.nextInt(12) - rand.nextInt(12), this.posY + 2.0D,
                this.posZ + rand.nextInt(12) - rand.nextInt(12), stack);
        this.world.spawnEntity(item);
    }

    protected boolean canDespawn() {
        return false;
    }
}
