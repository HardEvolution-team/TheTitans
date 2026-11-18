package com.electrifiedded.thetitans.world.gen;

import com.electrifiedded.thetitans.entity.titan.EntityWitherTurretGround;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenGroundTurretTowers extends WorldGenerator {
    private Block fld150520a;

    public WorldGenGroundTurretTowers(Block p_i45464_1_) {
        this.fld150520a = p_i45464_1_;
    }

    public boolean generate(World arg76484n1, Random arg76484n2, BlockPos position) {
        int arg76484n3 = position.getX();
        int arg76484n4 = position.getY();
        int arg76484n5 = position.getZ();
        if ((arg76484n1.isAirBlock(new BlockPos(arg76484n3, arg76484n4, arg76484n5))) && (arg76484n1.getBlockState(new BlockPos(arg76484n3, arg76484n4 - 1, arg76484n5)).getBlock() == this.fld150520a)) {
            int l = 2;
            int i1 = 2;
            for (int j1 = arg76484n3 - i1; j1 <= arg76484n3 + i1; j1++) {
                for (int k1 = arg76484n5 - i1; k1 <= arg76484n5 + i1; k1++) {
                    int l1 = j1 - arg76484n3;
                    int i2 = k1 - arg76484n5;
                    if ((l1 * l1 + i2 * i2 <= i1 * i1 + 1) && (arg76484n1.getBlockState(new BlockPos(j1, arg76484n4 - 1, k1)).getBlock() != this.fld150520a)) {
                        return false;
                    }
                }
            }

            for (int j1 = arg76484n4; (j1 < arg76484n4 + l) && (j1 < 256); j1++) {
                for (int k1 = arg76484n3 - i1; k1 <= arg76484n3 + i1; k1++) {
                    for (int l1 = arg76484n5 - i1; l1 <= arg76484n5 + i1; l1++) {
                        int i2 = k1 - arg76484n3;
                        int j2 = l1 - arg76484n5;
                        if (i2 * i2 + j2 * j2 <= i1 * i1 + 1) {
                            arg76484n1.setBlockState(new BlockPos(k1, j1, l1), Blocks.BEDROCK.getStateFromMeta(0), 2);
                        }
                    }
                }
            }

            EntityWitherTurretGround entityendercrystal = new EntityWitherTurretGround(arg76484n1);
            entityendercrystal.setLocationAndAngles(arg76484n3 + 0.5F, arg76484n4 + l, arg76484n5 + 0.5F, arg76484n2.nextFloat() * 360.0F, 0.0F);
            arg76484n1.spawnEntity(entityendercrystal);
            return true;
        }

        return false;
    }
}
