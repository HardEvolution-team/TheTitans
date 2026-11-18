package com.electrifiedded.thetitans.entity.minion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityGhastGuardFireball extends EntityLargeFireball {
    public EntityGhastGuardFireball(World worldIn) {
        super(worldIn);
    }

    @SideOnly(Side.CLIENT)
    public EntityGhastGuardFireball(World worldIn, double p_i1768_2_, double p_i1768_4_, double p_i1768_6_, double p_i1768_8_, double p_i1768_10_, double p_i1768_12_) {
        super(worldIn, p_i1768_2_, p_i1768_4_, p_i1768_6_, p_i1768_8_, p_i1768_10_, p_i1768_12_);
    }

    public EntityGhastGuardFireball(World worldIn, EntityLivingBase p_i1769_2_, double p_i1769_3_, double p_i1769_5_, double p_i1769_7_) {
        super(worldIn, p_i1769_2_, p_i1769_3_, p_i1769_5_, p_i1769_7_);
    }

    protected void onImpact(RayTraceResult movingObject) {
        if (!this.world.isRemote) {
            if (movingObject.entityHit != null) {
                if (this.shootingEntity instanceof EntityGhastMinion)
                    ((EntityGhastMinion)this.shootingEntity).attackEntityAsMob(movingObject.entityHit);
                else
                    movingObject.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 17.0F);
                if (movingObject.entityHit instanceof net.minecraft.entity.MultiPartEntityPart)
                    movingObject.entityHit.attackEntityFrom(DamageSource.causeExplosionDamage((net.minecraft.world.Explosion)null), (float)((net.minecraft.entity.EntityLiving)this.shootingEntity).getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
            }

            boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
            this.world.newExplosion((Entity)null, this.posX, this.posY, this.posZ, this.explosionPower, flag, flag);
            setDead();
        }
    }
}
