package com.electrifiedded.thetitans.world.gen;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenObsidianSpike extends WorldGenerator {
    public boolean generate(World worldIn, Random rand, BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        while (worldIn.isAirBlock(new BlockPos(x, y, z)) && y > 0) {
            --y;
        }

        if (worldIn.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.OBSIDIAN) {
            return false;
        }

        else {
            int l = rand.nextInt(15) + 5;
            int i1 = l / 4 + rand.nextInt(2);
            if (i1 > 1 && rand.nextInt(50) == 0) {
                if (rand.nextFloat() > 0.5F)
                    y += 30 + rand.nextInt(30);
                else
                    l += 140;
            }

            int j1;
            int k1;
            int l1;
            for (j1 = 0; j1 < l; ++j1) {
                float f = (1.0F - (float)j1 / (float)l) * (float)i1;
                k1 = MathHelper.ceil(f);
                for (l1 = -k1; l1 <= k1; ++l1) {
                    float f1 = (float)MathHelper.abs(l1) - 0.25F;
                    for (int i2 = -k1; i2 <= k1; ++i2) {
                        float f2 = (float)MathHelper.abs(i2) - 0.25F;
                        if ((l1 == 0 && i2 == 0 || f1 * f1 + f2 * f2 <= f * f) && (l1 != -k1 && l1 != k1 && i2 != -k1 && i2 != k1 || rand.nextFloat() <= 0.75F)) {
                            Block block = worldIn.getBlockState(new BlockPos(x + l1, y + j1, z + i2)).getBlock();
                            if (block.getDefaultState().getMaterial() == Material.AIR || block == Blocks.OBSIDIAN) {
                                this.setBlockAndNotifyAdequately(worldIn, new BlockPos(x + l1, y + j1, z + i2), Blocks.OBSIDIAN.getDefaultState());
                            }

                            if (j1 != 0 && k1 > 1) {
                                block = worldIn.getBlockState(new BlockPos(x + l1, y - j1, z + i2)).getBlock();
                                if (block.getDefaultState().getMaterial() == Material.AIR || block == Blocks.OBSIDIAN) {
                                    this.setBlockAndNotifyAdequately(worldIn, new BlockPos(x + l1, y - j1, z + i2), Blocks.OBSIDIAN.getDefaultState());
                                }
                            }
                        }
                    }
                }
            }

            j1 = i1 - 1;
            if (j1 < 0) {
                j1 = 0;
            }

            else if (j1 > 1) {
                j1 = 1;
            }

            for (int j2 = -j1; j2 <= j1; ++j2) {
                k1 = -j1;
                while (k1 <= j1) {
                    l1 = y - 1;
                    int k2 = 50;
                    if (Math.abs(j2) == 1 && Math.abs(k1) == 1) {
                        k2 = rand.nextInt(5);
                    }

                    while (true) {
                        if (l1 > 50) {
                            Block block1 = worldIn.getBlockState(new BlockPos(x + j2, l1, z + k1)).getBlock();
                            if (block1.getDefaultState().getMaterial() == Material.AIR || block1 == Blocks.OBSIDIAN) {
                                this.setBlockAndNotifyAdequately(worldIn, new BlockPos(x + j2, l1, z + k1), Blocks.OBSIDIAN.getDefaultState());
                                --l1;
                                --k2;
                                if (k2 <= 0) {
                                    l1 -= rand.nextInt(5) + 1;
                                    k2 = rand.nextInt(5);
                                }

                                continue;
                            }
                        }

                        ++k1;
                        break;
                    }
                }
            }

            return true;
        }
    }
}
