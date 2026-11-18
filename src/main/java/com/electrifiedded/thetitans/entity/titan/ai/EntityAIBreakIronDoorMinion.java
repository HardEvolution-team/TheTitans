package com.electrifiedded.thetitans.entity.titan.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.EnumHand;

public class EntityAIBreakIronDoorMinion extends EntityAIIronDoorInteract {
    private int breakingTime;
    private int fld75358j = -1;

    public EntityAIBreakIronDoorMinion(EntityLiving p_i1618_1_) {
        super(p_i1618_1_);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        return !super.shouldExecute() ? false : (!this.mobThe.world.getGameRules().getBoolean("mobGriefing") ? false : true);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        super.startExecuting();
        this.breakingTime = 0;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        double d0 = this.mobThe.getDistanceSq(this.doorPosition);
        return this.breakingTime <= 240 && !BlockDoor.isOpen(this.mobThe.world, this.doorPosition) && d0 < 4.0D;
    }

    /**
     * Resets the task
     */
    public void resetTask() {
        super.resetTask();
        this.mobThe.world.sendBlockBreakProgress(this.mobThe.getEntityId(), this.doorPosition, -1);
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        super.updateTask();
        if (this.mobThe.ticksExisted % 20 == 0) {
            ++this.breakingTime;
            this.mobThe.swingArm(EnumHand.MAIN_HAND);
            this.mobThe.world.playEvent(1011, this.doorPosition, 0);
        }

        int i = (int)((float)this.breakingTime / 240.0F * 10.0F);
        if (i != this.fld75358j) {
            this.mobThe.world.sendBlockBreakProgress(this.mobThe.getEntityId(), this.doorPosition, i);
            this.fld75358j = i;
        }

        if (this.breakingTime == 240) {
            this.mobThe.world.setBlockToAir(this.doorPosition);
            this.mobThe.world.playEvent(1012, this.doorPosition, 0);
            this.mobThe.world.playEvent(2001, this.doorPosition, Block.getIdFromBlock(this.fld151504e));
        }
    }
}
