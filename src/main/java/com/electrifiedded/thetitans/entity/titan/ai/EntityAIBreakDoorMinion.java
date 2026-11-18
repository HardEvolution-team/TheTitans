package com.electrifiedded.thetitans.entity.titan.ai;

import com.electrifiedded.thetitans.entity.minion.EntityCreeperMinion;
import com.electrifiedded.thetitans.entity.minion.EnumMinionType;
import com.electrifiedded.thetitans.entity.minion.IMinion;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIDoorInteract;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class EntityAIBreakDoorMinion extends EntityAIDoorInteract {
    private int breakingTime;
    private int fld75358j = -1;

    public EntityAIBreakDoorMinion(EntityLiving p_i1618_1_) {
        super(p_i1618_1_);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        return !super.shouldExecute() ? false : (!this.entity.world.getGameRules().getBoolean("mobGriefing") ? false : !BlockDoor.isOpen(this.entity.world, this.doorPosition));
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
        double d0 = this.entity.getDistanceSq(this.doorPosition);
        return this.breakingTime <= 240 && !BlockDoor.isOpen(this.entity.world, this.doorPosition) && d0 < 4.0D;
    }

    /**
     * Resets the task
     */
    public void resetTask() {
        super.resetTask();
        this.entity.world.sendBlockBreakProgress(this.entity.getEntityId(), this.doorPosition, -1);
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        super.updateTask();
        this.entity.getLookHelper().setLookPosition(this.doorPosition.getX() + 0.5D, this.doorPosition.getY() + 0.5D, this.doorPosition.getZ() + 0.5D, 180F, 30F);
        int dam = (int)this.entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        if (this.entity instanceof EntityCreeperMinion && ((EntityCreeperMinion)entity).fetchKindIntMinion() != 3) {
            ((EntityCreeperMinion)entity).ignite();
        }

        else if (this.entity.ticksExisted % 20 == 0) {
            this.breakingTime += dam;
            ItemStack held = this.entity.getHeldItemMainhand();
            if (!held.isEmpty()) {
                this.breakingTime += dam;
                if (held.getItem() instanceof ItemAxe) {
                    this.breakingTime += (dam * 3);
                }
            }

            this.entity.swingArm(EnumHand.MAIN_HAND);
            this.entity.world.playEvent(1010, this.doorPosition, 0);
        }

        int i = (int)((float)this.breakingTime / 240.0F * 10.0F);
        if (i != this.fld75358j) {
            this.entity.world.sendBlockBreakProgress(this.entity.getEntityId(), this.doorPosition, i);
            this.fld75358j = i;
        }

        if (this.breakingTime >= 240 || (this.entity instanceof IMinion && ((IMinion)entity).fetchKindMinion() == EnumMinionType.TEMPLAR)) {
            this.entity.world.setBlockToAir(this.doorPosition);
            this.entity.world.playEvent(1012, this.doorPosition, 0);
            this.entity.world.playEvent(2001, this.doorPosition, Block.getIdFromBlock(this.doorBlock));
        }
    }
}
