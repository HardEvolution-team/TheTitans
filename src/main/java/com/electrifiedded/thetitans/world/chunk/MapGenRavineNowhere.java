package com.electrifiedded.thetitans.world.chunk;

import net.minecraft.block.Block;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;

import java.util.Random;

public class MapGenRavineNowhere extends MapGenBase {
    private float[] wideFieldRavine = new float[1024];

    protected void ravineCarve(long arg151540n1, int arg151540n3, int arg151540n4, ChunkPrimer arg151540n5, double arg151540n6, double arg151540n8, double arg151540n10, float arg151540n12, float arg151540n13, float arg151540n14, int arg151540n15, int arg151540n16, double arg151540n17) {
        Random random = new Random(arg151540n1);
        double d4 = (double)(arg151540n3 * 16 + 8);
        double d5 = (double)(arg151540n4 * 16 + 8);
        float f3 = 0.0F;
        float f4 = 0.0F;
        if (arg151540n16 <= 0) {
            int j1 = this.range * 16 - 16;
            arg151540n16 = j1 - random.nextInt(j1 / 4);
        }

        boolean flag1 = false;
        if (arg151540n15 == -1) {
            arg151540n15 = arg151540n16 / 2;
            flag1 = true;
        }

        float f5 = 1.0F;
        for (int k1 = 0; k1 < 256; ++k1) {
            if (k1 == 0 || random.nextInt(3) == 0) {
                f5 = 1.0F + random.nextFloat() * random.nextFloat() * 1.0F;
            }

            this.wideFieldRavine[k1] = f5 * f5;
        }

        for (; arg151540n15 < arg151540n16; ++arg151540n15) {
            double d12 = 1.5D + (double)(MathHelper.sin((float)arg151540n15 * (float)Math.PI / (float)arg151540n16) * arg151540n12 * 1.0F);
            double d6 = d12 * arg151540n17;
            d12 *= (double)random.nextFloat() * 0.25D + 0.75D;
            d6 *= (double)random.nextFloat() * 0.25D + 0.75D;
            float f6 = MathHelper.cos(arg151540n14);
            float f7 = MathHelper.sin(arg151540n14);
            arg151540n6 += (double)(MathHelper.cos(arg151540n13) * f6);
            arg151540n8 += (double)f7;
            arg151540n10 += (double)(MathHelper.sin(arg151540n13) * f6);
            arg151540n14 *= 0.7F;
            arg151540n14 += f4 * 0.05F;
            arg151540n13 += f3 * 0.05F;
            f4 *= 0.8F;
            f3 *= 0.5F;
            f4 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
            f3 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
            if (flag1 || random.nextInt(4) != 0) {
                double d7 = arg151540n6 - d4;
                double d8 = arg151540n10 - d5;
                double d9 = (double)(arg151540n16 - arg151540n15);
                double d10 = (double)(arg151540n12 + 2.0F + 16.0F);
                if (d7 * d7 + d8 * d8 - d9 * d9 > d10 * d10) {
                    return;
                }

                if (arg151540n6 >= d4 - 16.0D - d12 * 2.0D && arg151540n10 >= d5 - 16.0D - d12 * 2.0D && arg151540n6 <= d4 + 16.0D + d12 * 2.0D && arg151540n10 <= d5 + 16.0D + d12 * 2.0D) {
                    int i4 = MathHelper.floor(arg151540n6 - d12) - arg151540n3 * 16 - 1;
                    int l1 = MathHelper.floor(arg151540n6 + d12) - arg151540n3 * 16 + 1;
                    int j4 = MathHelper.floor(arg151540n8 - d6) - 1;
                    int i2 = MathHelper.floor(arg151540n8 + d6) + 1;
                    int k4 = MathHelper.floor(arg151540n10 - d12) - arg151540n4 * 16 - 1;
                    int j2 = MathHelper.floor(arg151540n10 + d12) - arg151540n4 * 16 + 1;
                    if (i4 < 0) {
                        i4 = 0;
                    }

                    if (l1 > 16) {
                        l1 = 16;
                    }

                    if (j4 < 1) {
                        j4 = 1;
                    }

                    if (i2 > 248) {
                        i2 = 248;
                    }

                    if (k4 < 0) {
                        k4 = 0;
                    }

                    if (j2 > 16) {
                        j2 = 16;
                    }

                    boolean flag2 = false;
                    int k2;
                    int j3;
                    for (k2 = i4; !flag2 && k2 < l1; ++k2) {
                        for (int l2 = k4; !flag2 && l2 < j2; ++l2) {
                            for (int i3 = i2 + 1; !flag2 && i3 >= j4 - 1; --i3) {
                                if (i3 >= 0 && i3 < 256) {
                                    if (isOceanBlock(arg151540n5, k2, i3, l2, arg151540n3, arg151540n4)) {
                                        flag2 = true;
                                    }

                                    if (i3 != j4 - 1 && k2 != i4 && k2 != l1 - 1 && l2 != k4 && l2 != j2 - 1) {
                                        i3 = j4;
                                    }
                                }
                            }
                        }
                    }

                    if (!flag2) {
                        for (k2 = i4; k2 < l1; ++k2) {
                            double d13 = ((double)(k2 + arg151540n3 * 16) + 0.5D - arg151540n6) / d12;
                            for (j3 = k4; j3 < j2; ++j3) {
                                double d14 = ((double)(j3 + arg151540n4 * 16) + 0.5D - arg151540n10) / d12;
                                boolean flag = false;
                                if (d13 * d13 + d14 * d14 < 1.0D) {
                                    for (int l3 = i2 - 1; l3 >= j4; --l3) {
                                        double d11 = ((double)l3 + 0.5D - arg151540n8) / d6;
                                        if ((d13 * d13 + d14 * d14) * (double)this.wideFieldRavine[l3] + d11 * d11 / 6.0D < 1.0D) {
                                            if (isTopBlock(arg151540n5, k2, l3, j3, arg151540n3, arg151540n4)) {
                                                flag = true;
                                            }

                                            digBlock(arg151540n5, k2, l3, j3, arg151540n3, arg151540n4, flag);
                                        }
                                    }
                                }
                            }
                        }

                        if (flag1) {
                            break;
                        }
                    }
                }
            }
        }
    }

    protected void recursiveGenerate(World worldIn, int chunkX, int chunkZ, int originalX, int originalZ, ChunkPrimer chunkPrimerIn) {
        if (this.rand.nextInt(50) == 0) {
            double d0 = (double)(chunkX * 16 + this.rand.nextInt(16));
            double d1 = (double)(this.rand.nextInt(this.rand.nextInt(40) + 8) + 20);
            double d2 = (double)(chunkZ * 16 + this.rand.nextInt(16));
            byte b0 = 1;
            for (int i1 = 0; i1 < b0; ++i1) {
                float f = this.rand.nextFloat() * (float)Math.PI * 2.0F;
                float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float f2 = (this.rand.nextFloat() * 2.0F + this.rand.nextFloat()) * 2.0F;
                this.ravineCarve(this.rand.nextLong(), originalX, originalZ, chunkPrimerIn, d0, d1, d2, f2, f, f1, 0, 0, 3.0D);
            }
        }
    }

    protected boolean isOceanBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ) {
        return data.getBlockState(x, y, z).getBlock() == Blocks.WATER || data.getBlockState(x, y, z).getBlock() == Blocks.FLOWING_WATER;
    }

    //Exception biomes to make sure we generate like vanilla
    private boolean isExceptionBiome(Biome biome) {
        if (biome == Biomes.MUSHROOM_ISLAND) return true;
        if (biome == Biomes.BEACH) return true;
        if (biome == Biomes.DESERT) return true;
        return false;
    }

    //Determine if the block at the specified location is the top block for the biome, we take into account
    //Vanilla bugs to make sure that we generate the map the same way vanilla does.
    private boolean isTopBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ) {
        Biome biome = this.world.getBiome(new BlockPos(x + chunkX * 16, 0, z + chunkZ * 16));
        return (isExceptionBiome(biome) ? data.getBlockState(x, y, z).getBlock() == Blocks.GRASS : data.getBlockState(x, y, z).getBlock() == biome.topBlock.getBlock());
    }

    /**
     * Digs out the current block, default implementation removes stone, filler, and top block
     * Sets the block to lava if y is less then 10, and air other wise.
     * If setting to air, it also checks to see if we've broken the surface and if so* tries to make the floor the biome's top block
     ** @param data Block data array
     * @param x local X position
     * @param y local Y position
     * @param z local Z position
     * @param chunkX Chunk X position
     * @param chunkZ Chunk Y position
     * @param foundTop True if we've encountered the biome's top block. Ideally if we've broken the surface.
     */
    protected void digBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ, boolean foundTop) {
        Biome biome = this.world.getBiome(new BlockPos(x + chunkX * 16, 0, z + chunkZ * 16));
        Block top = (isExceptionBiome(biome) ? Blocks.GRASS : biome.topBlock.getBlock());
        Block filler = (isExceptionBiome(biome) ? Blocks.DIRT : biome.fillerBlock.getBlock());
        Block block = data.getBlockState(x, y, z).getBlock();
        if (block == Blocks.END_STONE || block == Blocks.OBSIDIAN || block == filler || block == top) {
            data.setBlockState(x, y, z, Blocks.AIR.getDefaultState());
            if (foundTop && data.getBlockState(x, y - 1, z).getBlock() == filler) {
                data.setBlockState(x, y - 1, z, top.getDefaultState());
            }
        }
    }
}
