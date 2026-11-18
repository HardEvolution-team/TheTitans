package com.electrifiedded.thetitans.client;

import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.world.World;

public class EntityGiantSmokeFX extends ParticleSmokeNormal {
    public EntityGiantSmokeFX(World parWorld, double parX, double parY, double parZ, double parMotionX, double parMotionY, double parMotionZ) {
        super(parWorld, parX, parY, parZ, parMotionX, parMotionY, parMotionZ, 1.0F);
        this.particleScale = 16F;
    }
}
