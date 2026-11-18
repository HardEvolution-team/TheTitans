package com.electrifiedded.thetitans.handler;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class VoidTeleporter extends Teleporter {
    public VoidTeleporter(WorldServer worldIn) {
        super(worldIn);
    }

    @Override
    public void placeInPortal(Entity entityIn, float rotationYaw) {
        int i = MathHelper.floor(entityIn.posX);
        int j = MathHelper.floor(entityIn.posY) - 1;
        int k = MathHelper.floor(entityIn.posZ);
        entityIn.setLocationAndAngles((double)i, (double)j, (double)k, entityIn.rotationYaw, 0.0F);
        entityIn.motionX = entityIn.motionY = entityIn.motionZ = 0.0D;
    }

    /**
     * Place an entity in a nearby portal which already exists.
     */
    @Override
    public boolean placeInExistingPortal(Entity entityIn, float rotationYaw) {
        int i = MathHelper.floor(entityIn.posX);
        int j = MathHelper.floor(entityIn.posY) - 1;
        int k = MathHelper.floor(entityIn.posZ);
        entityIn.setLocationAndAngles((double)i, (double)j, (double)k, entityIn.rotationYaw, 0.0F);
        entityIn.motionX = entityIn.motionY = entityIn.motionZ = 0.0D;
        return true;
    }

    @Override
    public boolean makePortal(Entity entityIn) {
        return true;
    }

    /**
     * called periodically to remove out-of-date portal locations from the cache list. Argument par1 is a
     * WorldServer.getTotalWorldTime() value.
     */
    @Override
    public void removeStalePortalLocations(long worldTime) {
    }
}
