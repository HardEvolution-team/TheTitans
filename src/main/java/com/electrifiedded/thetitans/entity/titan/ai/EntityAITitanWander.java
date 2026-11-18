package com.electrifiedded.thetitans.entity.titan.ai;

import com.electrifiedded.thetitans.entity.titan.EntityTitan;

import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

public class EntityAITitanWander extends net.minecraft.entity.ai.EntityAIBase {
    private EntityTitan entity;
    private double posXZ;
    private double posZZ;
    private int clockChaos;
    private boolean fld179482g;

    public EntityAITitanWander(EntityTitan p_i45887_1_, int p_i45887_4_) {
        this.entity = p_i45887_1_;
        this.clockChaos = p_i45887_4_;
        setMutexBits(1);
    }

    public boolean shouldExecute() {
        if (!this.fld179482g)
            if (this.entity.getRNG().nextInt(this.clockChaos) != 0)
            return false;
        if (this.entity.getAttackTarget() != null) {
            return false;
        }

        Vec3d vec3 = RandomPositionGenerator.findRandomTarget(this.entity, (int)this.entity.fetchMPArea(16.0F), 5);
        if (vec3 == null) {
            return false;
        }

        if (this.entity.getDistance(vec3.x, entity.posY, vec3.z) <= entity.width * 2) {
            return false;
        }

        this.posXZ = vec3.x;
        this.posZZ = vec3.z;
        this.fld179482g = false;
        return true;
    }

    public boolean shouldContinueExecuting() {
        return this.entity.getAttackTarget() == null && this.entity.getDistance(posXZ, entity.posY, posZZ) > 3D;
    }

    public void updateTask() {
        this.entity.shiftToPointGiant(posXZ, posZZ, true);
        this.entity.renderYawOffset = this.entity.rotationYaw = this.entity.rotationYawHead;
    }
}
