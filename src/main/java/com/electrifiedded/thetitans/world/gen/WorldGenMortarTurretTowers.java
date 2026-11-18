package com.electrifiedded.thetitans.world.gen;

import com.electrifiedded.thetitans.entity.titan.EntityWitherTurretMortar;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenMortarTurretTowers extends WorldGenerator {
    private Block block;

    public WorldGenMortarTurretTowers(Block p_i45464_1_) {
        this.block = p_i45464_1_;
    }

    public boolean generate(World worldIn, Random rand, BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if ((worldIn.isAirBlock(new BlockPos(x, y, z))) && (worldIn.getBlockState(new BlockPos(x, y - 1, z)).getBlock() == this.block)) {
            int l = 3;
            int i1 = 3;
            for (int j1 = x - i1; j1 <= x + i1; j1++) {
                for (int k1 = z - i1; k1 <= z + i1; k1++) {
                    int l1 = j1 - x;
                    int i2 = k1 - z;
                    if ((l1 * l1 + i2 * i2 <= i1 * i1 + 1) && (worldIn.getBlockState(new BlockPos(j1, y - 1, k1)).getBlock() != this.block)) {
                        return false;
                    }
                }
            }

            for (int j1 = y; (j1 < y + l) && (j1 < 256); j1++) {
                for (int k1 = x - i1; k1 <= x + i1; k1++) {
                    for (int l1 = z - i1; l1 <= z + i1; l1++) {
                        int i2 = k1 - x;
                        int j2 = l1 - z;
                        if (i2 * i2 + j2 * j2 <= i1 * i1 + 1) {
                            worldIn.setBlockState(new BlockPos(k1, j1, l1), Blocks.BEDROCK.getDefaultState(), 2);
                        }
                    }
                }
            }

            EntityWitherTurretMortar entityendercrystal = new EntityWitherTurretMortar(worldIn);
            entityendercrystal.setLocationAndAngles(x + 0.5F, y + l, z + 0.5F, rand.nextFloat() * 360.0F, 0.0F);
            worldIn.spawnEntity(entityendercrystal);
            return true;
        }

        return false;
    }
}
