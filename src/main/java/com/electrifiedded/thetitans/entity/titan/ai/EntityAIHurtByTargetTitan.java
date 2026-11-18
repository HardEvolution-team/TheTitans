package com.electrifiedded.thetitans.entity.titan.ai;

import com.electrifiedded.thetitans.entity.titan.ICanAttack;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.Iterator;
import java.util.List;

public class EntityAIHurtByTargetTitan extends EntityAITarget {
    boolean entityCallsForHelp;
    private final Class<?>[] targetedClasses;

    public EntityAIHurtByTargetTitan(EntityCreature creatureIn, boolean entityCallsForHelpIn, Class<?>... targetClassesIn) {
        super(creatureIn, false);
        this.entityCallsForHelp = entityCallsForHelpIn;
        this.targetedClasses = targetClassesIn;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        // taskOwner may be a vanilla creature (not ICanAttack); guard the cast to avoid a tick-time CCE.
        return this.taskOwner.getRevengeTarget() != null
                && (!(this.taskOwner instanceof com.electrifiedded.thetitans.entity.titan.ICanAttack)
                || ((com.electrifiedded.thetitans.entity.titan.ICanAttack)this.taskOwner).allowStrikeGiant(this.taskOwner.getRevengeTarget().getClass()));
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.taskOwner.getRevengeTarget());
        if (this.entityCallsForHelp) {
            for (Class<?> oclass : this.targetedClasses) {
                double d0 = this.getTargetDistance();
                List<EntityCreature> list = this.taskOwner.world.getEntitiesWithinAABB(EntityCreature.class, new AxisAlignedBB(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0D, this.taskOwner.posY + 1.0D, this.taskOwner.posZ + 1.0D).grow(d0, d0, d0), e -> e != null && e.getClass() == EntityAIHurtByTargetTitan.this.taskOwner.getClass());
                Iterator<EntityCreature> iterator = list.iterator();
                while (iterator.hasNext()) {
                    EntityCreature entitycreature = iterator.next();
                    if (entitycreature.getClass() == oclass && this.taskOwner != entitycreature && entitycreature.getAttackTarget() == null && this.taskOwner.getRevengeTarget() != null && (!(entitycreature instanceof com.electrifiedded.thetitans.entity.titan.ICanAttack) || ((com.electrifiedded.thetitans.entity.titan.ICanAttack)entitycreature).allowStrikeGiant(this.taskOwner.getRevengeTarget().getClass()))) {
                        // 1.7.10 reflectively poked the private randomSoundDelay; that field is now public livingSoundTime
                        entitycreature.livingSoundTime = entitycreature.getRNG().nextInt(40);
                        entitycreature.setAttackTarget(this.taskOwner.getRevengeTarget());
                    }
                }
            }
        }

        super.startExecuting();
    }
}
