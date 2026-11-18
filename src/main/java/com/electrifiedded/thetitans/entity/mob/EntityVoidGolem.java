package com.electrifiedded.thetitans.entity.mob;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.minion.IMinion;
import com.electrifiedded.thetitans.entity.titan.EntityIronGolemTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.sound.SoundHandler;
import com.electrifiedded.thetitans.util.ReflectionHelper;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityVoidGolem extends EntityIronGolem {
    public EntityVoidGolem(World p_i1694_1_) {
        super(p_i1694_1_);
        EntityIronGolemTitan.appendTargetingTaskToMobGiant(this);
        this.isImmuneToFire = true;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0E8D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48D);
    }

    public int getTotalArmorValue() {
        return 24;
    }

    /**
     * Decrements the entity's air supply when underwater
     */
    protected int decreaseAirSupply(int arg70682n1) {
        return arg70682n1;
    }

    protected void collideWithEntity(Entity arg82167n1) {
        if (arg82167n1 instanceof IMob) {
            this.setAttackTarget((EntityLivingBase)arg82167n1);
        }

        super.collideWithEntity(arg82167n1);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.clearActivePotions();
        if (this.getAttackTarget() != null && this.getAttackTarget().onGround)
            this.getNavigator().tryMoveToEntityLiving(this.getAttackTarget(), 1D);
        this.heal(2.0F);
        if (this.ticksExisted % 10 == 0)
            SoundHandler.whenPlayMob("thetitans:harcadiumHum", this.world, this, 0.9F, 0.9F);
        if (this.posY <= 0D) {
            this.setPosition(this.posX, 0D, this.posZ);
            this.onGround = true;
            this.isAirBorne = false;
            this.fallDistance = 0.0F;
            if (this.motionY < 0D)
                this.motionY = 0D;
        }

        if (this.motionY > 1D)
            this.motionY = 1D;
        if (this.motionX > 1D)
            this.motionX = 1D;
        if (this.motionZ > 1D)
            this.motionZ = 1D;
        if (this.motionX < -1D)
            this.motionX = -1D;
        if (this.motionZ < -1D)
            this.motionZ = -1D;
        if (this.motionX * this.motionX + this.motionZ * this.motionZ > 2.500000277905201E-7D && this.rand.nextInt(5) == 0) {
            int i = MathHelper.floor(this.posX);
            int j = MathHelper.floor(this.posY - 0.20000000298023224D);
            int k = MathHelper.floor(this.posZ);
            BlockPos pos = new BlockPos(i, j, k);
            IBlockState state = this.world.getBlockState(pos);
            if (state.getMaterial() != Material.AIR) {
                this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, this.getEntityBoundingBox().minY + 0.1D, this.posZ + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, 4.0D * ((double)this.rand.nextFloat() - 0.5D), 0.5D, ((double)this.rand.nextFloat() - 0.5D) * 4.0D, Block.getStateId(state));
            }
        }

        if (this.getAttackTarget() != null) {
            double d0 = this.getDistance(this.getAttackTarget());
            double d1 = (double)(this.width + this.getAttackTarget().width + 3D);
            if (d0 <= d1)
                this.attackEntityAsMob(this.getAttackTarget());
        }
    }

    public boolean attackEntityAsMob(Entity arg70652n1) {
        this.world.setEntityState(this, (byte)4);
        float damage = (float)(100000 + this.rand.nextInt(9900000));
        if (arg70652n1 instanceof EntityLivingBase) {
            ((EntityLivingBase)arg70652n1).setHealth(((EntityLivingBase)arg70652n1).getHealth() - 4.0F);
        }

        if (arg70652n1 instanceof EntityTitan)
            damage *= 500F;
        boolean flag = arg70652n1.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
        if (flag) {
            if (this.isPlayerCreated()) {
                try {
                    net.minecraftforge.fml.relauncher.ReflectionHelper.findField(EntityLivingBase.class, "recentlyHit", "field_70718_bc").setInt(arg70652n1, 100);
                } catch (Exception var5) {
                    arg70652n1.hurtResistantTime = 0;
                }
            }

            if (arg70652n1 instanceof IMinion && TheTitans.isHeraldLevel(((IMinion)arg70652n1).fetchKindMinion()))
                arg70652n1.motionY -= 0.8D;
            else
                arg70652n1.motionY += 0.8D;
        }

        this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
        return flag;
    }

    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_IRONGOLEM_STEP, 1.0F, 0.9F);
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean arg70628n1, int arg70628n2) {
        int j = this.rand.nextInt(3);
        int k;
        for (k = 0; k < j; ++k) {
            this.dropItem(TitanGear.malgrum, 1);
        }

        k = 6 + this.rand.nextInt(6);
        for (int l = 0; l < k; ++l) {
            this.dropItem(TitanGear.stackVoid, 1);
        }
    }
}
