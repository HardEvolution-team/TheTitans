package com.electrifiedded.thetitans.entity.titan.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;

public abstract class EntityAIIronDoorInteract extends EntityAIBase {
    protected EntityLiving mobThe;
    protected BlockPos doorPosition = BlockPos.ORIGIN;
    protected BlockDoor fld151504e;
    /** If is true then the Entity has stopped Door Interaction and compoleted the task. */
    boolean hasStoppedDoorInteraction;
    float entityPositionX;
    float entityPositionZ;

    public EntityAIIronDoorInteract(EntityLiving p_i1621_1_) {
        this.mobThe = p_i1621_1_;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (!this.mobThe.collidedHorizontally) {
            return false;
        }

        else if (!(this.mobThe.getNavigator() instanceof PathNavigateGround)) {
            return false;
        }

        else {
            PathNavigateGround pathnavigate = (PathNavigateGround)this.mobThe.getNavigator();
            Path pathentity = pathnavigate.getPath();
            if (pathentity != null && !pathentity.isFinished() && pathnavigate.getEnterDoors()) {
                for (int i = 0; i < Math.min(pathentity.getCurrentPathIndex() + 2, pathentity.getCurrentPathLength()); ++i) {
                    PathPoint pathpoint = pathentity.getPathPointFromIndex(i);
                    this.doorPosition = new BlockPos(pathpoint.x, pathpoint.y + 1, pathpoint.z);
                    if (this.mobThe.getDistanceSq((double)this.doorPosition.getX(), this.mobThe.posY, (double)this.doorPosition.getZ()) <= 2.25D) {
                        this.fld151504e = this.getBlockDoor(this.doorPosition);
                        if (this.fld151504e != null) {
                            return true;
                        }
                    }
                }

                this.doorPosition = (new BlockPos(this.mobThe)).up();
                this.fld151504e = this.getBlockDoor(this.doorPosition);
                return this.fld151504e != null;
            }

            else {
                return false;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        return !this.hasStoppedDoorInteraction;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.hasStoppedDoorInteraction = false;
        this.entityPositionX = (float)((double)((float)this.doorPosition.getX() + 0.5F) - this.mobThe.posX);
        this.entityPositionZ = (float)((double)((float)this.doorPosition.getZ() + 0.5F) - this.mobThe.posZ);
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        float f = (float)((double)((float)this.doorPosition.getX() + 0.5F) - this.mobThe.posX);
        float f1 = (float)((double)((float)this.doorPosition.getZ() + 0.5F) - this.mobThe.posZ);
        float f2 = this.entityPositionX * f + this.entityPositionZ * f1;
        if (f2 < 0.0F) {
            this.hasStoppedDoorInteraction = true;
        }
    }

    private BlockDoor getBlockDoor(BlockPos pos) {
        IBlockState state = this.mobThe.world.getBlockState(pos);
        Block block = state.getBlock();
        return block != Blocks.IRON_DOOR ? null : (BlockDoor)block;
    }
}
