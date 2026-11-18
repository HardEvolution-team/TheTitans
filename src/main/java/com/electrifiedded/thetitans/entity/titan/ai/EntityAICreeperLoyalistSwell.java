package com.electrifiedded.thetitans.entity.titan.ai;

import com.electrifiedded.thetitans.entity.minion.EntityCreeperMinion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAICreeperLoyalistSwell extends EntityAIBase {
    EntityCreeperMinion swellingCreeper;
    EntityLivingBase creeperAttackTarget;

    public EntityAICreeperLoyalistSwell(EntityCreeperMinion p_i1655_1_) {
        this.swellingCreeper = p_i1655_1_;
        setMutexBits(1);
    }

    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.swellingCreeper.getAttackTarget();
        return (this.swellingCreeper.getCreeperState() > 0) || ((entitylivingbase != null) && (this.swellingCreeper.getDistanceSq(entitylivingbase) < 9.0D) && (!this.swellingCreeper.meleeNeeds));
    }

    public void startExecuting() {
        this.swellingCreeper.getNavigator().clearPath();
        this.creeperAttackTarget = this.swellingCreeper.getAttackTarget();
    }

    public void resetTask() {
        this.creeperAttackTarget = null;
    }

    public void updateTask() {
        if (this.creeperAttackTarget == null) {
            this.swellingCreeper.setCreeperState(-1);
        }

        else if (this.swellingCreeper.getDistanceSq(this.creeperAttackTarget) > 49.0D) {
            this.swellingCreeper.setCreeperState(-1);
        }

        else if (!this.swellingCreeper.getEntitySenses().canSee(this.creeperAttackTarget)) {
            this.swellingCreeper.setCreeperState(-1);
        }

        else {
            this.swellingCreeper.setCreeperState(1);
        }
    }
}
