package com.electrifiedded.thetitans.entity.titan.ai;

import com.electrifiedded.thetitans.entity.minion.EntityCreeperMinion;
import com.electrifiedded.thetitans.entity.titan.EntityCreeperTitan;
import com.electrifiedded.thetitans.entity.titan.EntityGargoyleTitan;
import com.electrifiedded.thetitans.entity.titan.EntityIronGolemTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySnowGolemTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitanSpirit;
import com.electrifiedded.thetitans.entity.titan.ICanAttack;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;

import com.google.common.base.Predicate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nullable;

public class EntityAINearestTargetTitan extends EntityAITarget {
    protected final Class<?> targetClass;
    protected final Sorter nearestAttackableVictimSorterThe;
    protected Predicate<Entity> targetEntitySelector;
    protected EntityLivingBase targetEntity;
    private int counterSearch;

    public EntityAINearestTargetTitan(EntityCreature p_i1663_1_, Class<?> p_i1663_2_, int p_i1663_3_, boolean p_i1663_4_) {
        this(p_i1663_1_, p_i1663_2_, p_i1663_3_, p_i1663_4_, false);
    }

    public EntityAINearestTargetTitan(EntityCreature p_i1664_1_, Class<?> p_i1664_2_, int p_i1664_3_, boolean p_i1664_4_, boolean p_i1664_5_) {
        this(p_i1664_1_, p_i1664_2_, p_i1664_3_, p_i1664_4_, p_i1664_5_, (Predicate<Entity>)null);
    }

    public EntityAINearestTargetTitan(EntityCreature p_i1665_1_, Class<?> p_i1665_2_, int p_i1665_3_, boolean p_i1665_4_, boolean p_i1665_5_, final Predicate<Entity> p_i1665_6_) {
        super(p_i1665_1_, p_i1665_4_, p_i1665_5_);
        this.targetClass = p_i1665_2_;
        this.nearestAttackableVictimSorterThe = new Sorter(p_i1665_1_);
        setMutexBits(1);
        this.targetEntitySelector = new Predicate<Entity>() {
            public boolean apply(@Nullable Entity arg82704n1) {
                return !(arg82704n1 instanceof EntityLivingBase) ? false : (p_i1665_6_ != null && !p_i1665_6_.apply(arg82704n1) ? false : EntityAINearestTargetTitan.this.isSuitableTarget((EntityLivingBase)arg82704n1, false));
            }
        };
    }

    @SuppressWarnings("unchecked")
    public boolean shouldExecute() {
        // PERF — the query below spans the titan's follow range (16 x size = ~768 blocks). Skip it when its
        // result is guaranteed to be discarded: dormant titans reject every target in setAttackTarget, and while a
        // titan has a revenge target isSuitableTarget rejects everything (golem/gargoyle titans excepted). Otherwise
        // search every other executor pass (~every 6 ticks instead of 3).
        if (this.taskOwner instanceof EntityTitan) {
            if (((EntityTitan)this.taskOwner).fetchWaiting())
                return false;
            if (this.taskOwner.getRevengeTarget() != null && !(this.taskOwner instanceof EntitySnowGolemTitan || this.taskOwner instanceof EntityIronGolemTitan || this.taskOwner instanceof EntityGargoyleTitan))
                return false;
            if ((++this.counterSearch & 1) != 0)
                return false;
        }

        double d0 = getTargetDistance();
        List<Entity> list = (List<Entity>)this.taskOwner.world.getEntitiesWithinAABB((Class<? extends Entity>)this.targetClass, this.taskOwner.getEntityBoundingBox().grow(d0, d0, d0), this.targetEntitySelector);
        Collections.sort(list, this.nearestAttackableVictimSorterThe);
        if (list.isEmpty()) {
            return false;
        }

        this.targetEntity = ((EntityLivingBase)list.get(0));
        return true;
    }

    public void startExecuting() {
        if (!this.taskOwner.world.isRemote)
            this.taskOwner.setAttackTarget(this.targetEntity);
        super.startExecuting();
    }

    protected boolean isSuitableTarget(EntityLivingBase arg75296n1, boolean arg75296n2) {
        if (arg75296n1 instanceof EntityTitan && (this.taskOwner instanceof EntitySnowGolemTitan || this.taskOwner instanceof EntityIronGolemTitan || this.taskOwner instanceof EntityGargoyleTitan)) {
            return true;
        }

        if (arg75296n1 == null) {
            return false;
        }

        else if (arg75296n1 == this.taskOwner) {
            return false;
        }

        else if (!arg75296n1.isEntityAlive()) {
            return false;
        }

        else if (this.taskOwner instanceof com.electrifiedded.thetitans.entity.titan.ICanAttack && !((com.electrifiedded.thetitans.entity.titan.ICanAttack)this.taskOwner).allowStrikeGiant(arg75296n1.getClass())) {
            return false;
        }

        else if (EntityTitan.isDudMobA(arg75296n1)) {
            return false;
        }

        else if (arg75296n1 instanceof EntityTitanSpirit) {
            return false;
        }

        else if ((this.taskOwner instanceof EntityCreeperTitan || this.taskOwner instanceof EntityCreeperMinion) && arg75296n1 instanceof EntityPlayer && ((EntityPlayer)arg75296n1).getName() == "Boom337317") {
            return false;
        }

        else if (arg75296n1 instanceof EntityPlayer && ((EntityPlayer)arg75296n1).getName() == "Umbrella_Ghast") {
            return false;
        }

        else if (arg75296n1 instanceof EntityTitan && (((EntityTitan)arg75296n1).getInvulTime() > 0 || ((EntityTitan)arg75296n1).fetchWaiting() || ((EntityTitan)arg75296n1).getAnimID() == 13)) {
            return false;
        }

        else if (this.taskOwner instanceof EntityTitan && this.taskOwner.getRevengeTarget() != null) {
            return false;
        }

        else {
            return super.isSuitableTarget(arg75296n1, arg75296n2);
        }
    }

    public static class Sorter implements Comparator<Object> {
        private final Entity mobThe;
        public Sorter(Entity p_i1662_1_) {
            this.mobThe = p_i1662_1_;
        }

        public int compare(Entity p_compare_1_, Entity p_compare_2_) {
            double d0 = this.mobThe.getDistanceSq(p_compare_1_);
            double d1 = this.mobThe.getDistanceSq(p_compare_2_);
            return d0 > d1 ? 1 : d0 < d1 ? -1 : 0;
        }

        public int compare(Object p_compare_1_, Object p_compare_2_) {
            return compare((Entity)p_compare_1_, (Entity)p_compare_2_);
        }
    }
}
