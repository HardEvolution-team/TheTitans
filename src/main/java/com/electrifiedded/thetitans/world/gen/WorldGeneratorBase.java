package com.electrifiedded.thetitans.world.gen;

import net.minecraft.world.World;

import java.util.Random;

// not extending WorldGenerator anymore because its 1.12.2 generate(World,Random,BlockPos)
// signature conflicts with the (World,Random,x,y,z) contract WorldGenStuffs/WorldGenMobhenge rely on.
public class WorldGeneratorBase {
    public int separation = 250;

    public boolean generate(World world, Random rand, int posX, int posY, int posZ) {
        return false;
    }
}
