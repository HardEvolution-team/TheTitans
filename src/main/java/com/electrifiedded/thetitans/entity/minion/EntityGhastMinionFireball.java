package com.electrifiedded.thetitans.entity.minion;

import com.electrifiedded.thetitans.entity.titan.ICanAttack;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityGhastMinionFireball extends EntityLargeFireball {
    public EntityGhastMinionFireball(World worldIn) {
        super(worldIn);
    }

    @SideOnly(Side.CLIENT)
    public EntityGhastMinionFireball(World worldIn, double p_i1768_2_, double p_i1768_4_, double p_i1768_6_, double p_i1768_8_, double p_i1768_10_, double p_i1768_12_) {
        super(worldIn, p_i1768_2_, p_i1768_4_, p_i1768_6_, p_i1768_8_, p_i1768_10_, p_i1768_12_);
    }

    public EntityGhastMinionFireball(World worldIn, EntityLivingBase p_i1769_2_, double p_i1769_3_, double p_i1769_5_, double p_i1769_7_) {
        super(worldIn, p_i1769_2_, p_i1769_3_, p_i1769_5_, p_i1769_7_);
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    protected void onImpact(RayTraceResult movingObject) {
        if (!this.world.isRemote) {
            boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
            if (movingObject.entityHit != null) {
                if (this.shootingEntity != null && this.shootingEntity instanceof EntityLiving && ((com.electrifiedded.thetitans.entity.titan.ICanAttack)this.shootingEntity).allowStrikeGiant(movingObject.entityHit.getClass())) {
                    movingObject.entityHit.hurtResistantTime = 0;
                    ((EntityLiving)this.shootingEntity).attackEntityAsMob(movingObject.entityHit);
                    movingObject.entityHit.setFire(10);
                    this.world.newExplosion(this.shootingEntity, this.posX, this.posY, this.posZ, this.explosionPower, flag, flag);
                    this.setDead();
                }
            }

            else {
                BlockPos pos = movingObject.getBlockPos().offset(movingObject.sideHit);
                if (this.world.isAirBlock(pos)) {
                    this.world.setBlockState(pos, Blocks.FIRE.getDefaultState());
                    this.world.newExplosion(this.shootingEntity != null ? this.shootingEntity : this, pos.getX(), pos.getY(), pos.getZ(), this.explosionPower, flag, flag);
                    this.setDead();
                }
            }
        }
    }
}
