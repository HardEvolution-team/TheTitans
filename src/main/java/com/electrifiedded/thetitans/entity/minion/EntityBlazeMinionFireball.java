package com.electrifiedded.thetitans.entity.minion;

import com.electrifiedded.thetitans.entity.titan.ICanAttack;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityBlazeMinionFireball extends EntitySmallFireball {
    public EntityBlazeMinionFireball(World p_i1770_1_) {
        super(p_i1770_1_);
    }

    public EntityBlazeMinionFireball(World p_i1771_1_, EntityLivingBase p_i1771_2_, double p_i1771_3_, double p_i1771_5_, double p_i1771_7_) {
        super(p_i1771_1_, p_i1771_2_, p_i1771_3_, p_i1771_5_, p_i1771_7_);
    }

    public EntityBlazeMinionFireball(World p_i1772_1_, double p_i1772_2_, double p_i1772_4_, double p_i1772_6_, double p_i1772_8_, double p_i1772_10_, double p_i1772_12_) {
        super(p_i1772_1_, p_i1772_2_, p_i1772_4_, p_i1772_6_, p_i1772_8_, p_i1772_10_, p_i1772_12_);
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onImpact(RayTraceResult arg70227n1) {
        if (!this.world.isRemote) {
            if (arg70227n1.entityHit != null) {
                if (this.shootingEntity != null && this.shootingEntity instanceof EntityLiving && ((com.electrifiedded.thetitans.entity.titan.ICanAttack)this.shootingEntity).allowStrikeGiant(arg70227n1.entityHit.getClass())) {
                    arg70227n1.entityHit.hurtResistantTime = 0;
                    ((EntityLiving)this.shootingEntity).attackEntityAsMob(arg70227n1.entityHit);
                    if (arg70227n1.entityHit instanceof net.minecraft.entity.MultiPartEntityPart)
                        arg70227n1.entityHit.attackEntityFrom(net.minecraft.util.DamageSource.causeExplosionDamage((net.minecraft.world.Explosion)null), (float)((EntityLiving)this.shootingEntity).getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
                    arg70227n1.entityHit.setFire((int)((EntityLiving)this.shootingEntity).getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue());
                    this.setDead();
                }
            }

            else {
                EnumFacing side = arg70227n1.sideHit;
                BlockPos pos = arg70227n1.getBlockPos().offset(side);
                if (this.world.isAirBlock(pos)) {
                    this.world.setBlockState(pos, Blocks.FIRE.getDefaultState());
                    this.setDead();
                }
            }
        }
    }
}
