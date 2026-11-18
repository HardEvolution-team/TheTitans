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

public class MapGenCavesNowhere extends MapGenBase {
    protected void carveNodeCave(long arg151542n1, int arg151542n3, int arg151542n4, ChunkPrimer arg151542n5, double arg151542n6, double arg151542n8, double arg151542n10) {
        this.tunnelCarve(arg151542n1, arg151542n3, arg151542n4, arg151542n5, arg151542n6, arg151542n8, arg151542n10, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
    }

    protected void tunnelCarve(long arg151541n1, int arg151541n3, int arg151541n4, ChunkPrimer arg151541n5, double arg151541n6, double arg151541n8, double arg151541n10, float arg151541n12, float arg151541n13, float arg151541n14, int arg151541n15, int arg151541n16, double arg151541n17) {
        double d4 = (double)(arg151541n3 * 16 + 8);
        double d5 = (double)(arg151541n4 * 16 + 8);
        float f3 = 0.0F;
        float f4 = 0.0F;
        Random random = new Random(arg151541n1);
        if (arg151541n16 <= 0) {
            int j1 = this.range * 16 - 16;
            arg151541n16 = j1 - random.nextInt(j1 / 4);
        }

        boolean flag2 = false;
        if (arg151541n15 == -1) {
            arg151541n15 = arg151541n16 / 2;
            flag2 = true;
        }

        int k1 = random.nextInt(arg151541n16 / 2) + arg151541n16 / 4;
        for (boolean flag = random.nextInt(6) == 0; arg151541n15 < arg151541n16; ++arg151541n15) {
            double d6 = 1.5D + (double)(MathHelper.sin((float)arg151541n15 * (float)Math.PI / (float)arg151541n16) * arg151541n12 * 1.0F);
            double d7 = d6 * arg151541n17;
            float f5 = MathHelper.cos(arg151541n14);
            float f6 = MathHelper.sin(arg151541n14);
            arg151541n6 += (double)(MathHelper.cos(arg151541n13) * f5);
            arg151541n8 += (double)f6;
            arg151541n10 += (double)(MathHelper.sin(arg151541n13) * f5);
            if (flag) {
                arg151541n14 *= 0.92F;
            }

            else {
                arg151541n14 *= 0.7F;
            }

            arg151541n14 += f4 * 0.1F;
            arg151541n13 += f3 * 0.1F;
            f4 *= 0.9F;
            f3 *= 0.75F;
            f4 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
            f3 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
            if (!flag2 && arg151541n15 == k1 && arg151541n12 > 1.0F && arg151541n16 > 0) {
                this.tunnelCarve(random.nextLong(), arg151541n3, arg151541n4, arg151541n5, arg151541n6, arg151541n8, arg151541n10, random.nextFloat() * 0.5F + 0.5F, arg151541n13 - ((float)Math.PI / 2F), arg151541n14 / 3.0F, arg151541n15, arg151541n16, 1.0D);
                this.tunnelCarve(random.nextLong(), arg151541n3, arg151541n4, arg151541n5, arg151541n6, arg151541n8, arg151541n10, random.nextFloat() * 0.5F + 0.5F, arg151541n13 + ((float)Math.PI / 2F), arg151541n14 / 3.0F, arg151541n15, arg151541n16, 1.0D);
                return;
            }

            if (flag2 || random.nextInt(4) != 0) {
                double d8 = arg151541n6 - d4;
                double d9 = arg151541n10 - d5;
                double d10 = (double)(arg151541n16 - arg151541n15);
                double d11 = (double)(arg151541n12 + 2.0F + 16.0F);
                if (d8 * d8 + d9 * d9 - d10 * d10 > d11 * d11) {
                    return;
                }

                if (arg151541n6 >= d4 - 16.0D - d6 * 2.0D && arg151541n10 >= d5 - 16.0D - d6 * 2.0D && arg151541n6 <= d4 + 16.0D + d6 * 2.0D && arg151541n10 <= d5 + 16.0D + d6 * 2.0D) {
                    int i4 = MathHelper.floor(arg151541n6 - d6) - arg151541n3 * 16 - 1;
                    int l1 = MathHelper.floor(arg151541n6 + d6) - arg151541n3 * 16 + 1;
                    int j4 = MathHelper.floor(arg151541n8 - d7) - 1;
                    int i2 = MathHelper.floor(arg151541n8 + d7) + 1;
                    int k4 = MathHelper.floor(arg151541n10 - d6) - arg151541n4 * 16 - 1;
                    int j2 = MathHelper.floor(arg151541n10 + d6) - arg151541n4 * 16 + 1;
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

                    boolean flag3 = false;
                    int k2;
                    int j3;
                    for (k2 = i4; !flag3 && k2 < l1; ++k2) {
                        for (int l2 = k4; !flag3 && l2 < j2; ++l2) {
                            for (int i3 = i2 + 1; !flag3 && i3 >= j4 - 1; --i3) {
                                if (i3 >= 0 && i3 < 256) {
                                    if (isOceanBlock(arg151541n5, k2, i3, l2, arg151541n3, arg151541n4)) {
                                        flag3 = true;
                                    }

                                    if (i3 != j4 - 1 && k2 != i4 && k2 != l1 - 1 && l2 != k4 && l2 != j2 - 1) {
                                        i3 = j4;
                                    }
                                }
                            }
                        }
                    }

                    if (!flag3) {
                        for (k2 = i4; k2 < l1; ++k2) {
                            double d13 = ((double)(k2 + arg151541n3 * 16) + 0.5D - arg151541n6) / d6;
                            for (j3 = k4; j3 < j2; ++j3) {
                                double d14 = ((double)(j3 + arg151541n4 * 16) + 0.5D - arg151541n10) / d6;
                                boolean flag1 = false;
                                if (d13 * d13 + d14 * d14 < 1.0D) {
                                    for (int l3 = i2 - 1; l3 >= j4; --l3) {
                                        double d12 = ((double)l3 + 0.5D - arg151541n8) / d7;
                                        if (d12 > -0.7D && d13 * d13 + d12 * d12 + d14 * d14 < 1.0D) {
                                            if (isTopBlock(arg151541n5, k2, l3, j3, arg151541n3, arg151541n4)) {
                                                flag1 = true;
                                            }

                                            digBlock(arg151541n5, k2, l3, j3, arg151541n3, arg151541n4, flag1);
                                        }
                                    }
                                }
                            }
                        }

                        if (flag2) {
                            break;
                        }
                    }
                }
            }
        }
    }

    protected void recursiveGenerate(World worldIn, int chunkX, int chunkZ, int originalX, int originalZ, ChunkPrimer chunkPrimerIn) {
        int i1 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(15) + 1) + 1);
        if (this.rand.nextInt(7) != 0) {
            i1 = 0;
        }

        for (int j1 = 0; j1 < i1; ++j1) {
            double d0 = (double)(chunkX * 16 + this.rand.nextInt(16));
            double d1 = (double)this.rand.nextInt(this.rand.nextInt(120) + 8);
            double d2 = (double)(chunkZ * 16 + this.rand.nextInt(16));
            int k1 = 1;
            if (this.rand.nextInt(4) == 0) {
                this.carveNodeCave(this.rand.nextLong(), originalX, originalZ, chunkPrimerIn, d0, d1, d2);
                k1 += this.rand.nextInt(4);
            }

            for (int l1 = 0; l1 < k1; ++l1) {
                float f = this.rand.nextFloat() * (float)Math.PI * 2.0F;
                float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float f2 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();
                if (this.rand.nextInt(10) == 0) {
                    f2 *= this.rand.nextFloat() * this.rand.nextFloat() * 3.0F + 1.0F;
                }

                this.tunnelCarve(this.rand.nextLong(), originalX, originalZ, chunkPrimerIn, d0, d1, d2, f2, f, f1, 0, 0, 1.0D);
            }
        }
    }

    protected boolean isOceanBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ) {
        return data.getBlockState(x, y, z).getBlock() == Blocks.FLOWING_WATER || data.getBlockState(x, y, z).getBlock() == Blocks.WATER;
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
        if (block == Blocks.OBSIDIAN || block == filler || block == top) {
            data.setBlockState(x, y, z, Blocks.AIR.getDefaultState());
            if (foundTop && data.getBlockState(x, y - 1, z).getBlock() == filler) {
                data.setBlockState(x, y - 1, z, top.getDefaultState());
            }
        }
    }
}
