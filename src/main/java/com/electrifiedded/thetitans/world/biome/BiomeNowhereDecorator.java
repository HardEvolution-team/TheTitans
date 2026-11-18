package com.electrifiedded.thetitans.world.biome;

import com.electrifiedded.thetitans.world.gen.WorldGenObsidianSpike;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;

import java.util.Random;

public class BiomeNowhereDecorator extends BiomeDecorator {
    public BiomeNowhereDecorator() {
    }

    // 1.12.2: genDecorations(Biome, World, Random). Old fields map: randomGenerator -> random,
    // currentWorld -> worldIn, chunk_X/chunk_Z -> chunkPos.getX()/getZ().
    protected void genDecorations(Biome arg150513n1, World worldIn, Random random) {
        generateOres(worldIn, random);
        int chunkX = this.chunkPos.getX();
        int chunkZ = this.chunkPos.getZ();
        if (random.nextInt(4) == 0) {
            int i = chunkX + random.nextInt(16) + 8;
            int j = chunkZ + random.nextInt(16) + 8;
            int k = worldIn.getTopSolidOrLiquidBlock(new BlockPos(i, 0, j)).getY();
            (new WorldGenObsidianSpike()).generate(worldIn, random, new BlockPos(i, k, j));
        }

        if (random.nextInt(4) == 0) {
            int i = chunkX + random.nextInt(16) + 8;
            int j = chunkZ + random.nextInt(16) + 8;
            int k = worldIn.getTopSolidOrLiquidBlock(new BlockPos(i, 0, j)).getY();
            (new WorldGenObsidianSpike()).generate(worldIn, random, new BlockPos(i, k, j));
        }

        if (random.nextInt(10) == 0) {
            int i = chunkX + random.nextInt(16) + 8;
            int j = chunkZ + random.nextInt(16) + 8;
            int k = worldIn.getTopSolidOrLiquidBlock(new BlockPos(i, 0, j)).getY();
            produceMelons(worldIn, random, i, k, j);
        }

        if (random.nextInt(10) == 0) {
            int i = chunkX + random.nextInt(16) + 8;
            int j = chunkZ + random.nextInt(16) + 8;
            int k = worldIn.getTopSolidOrLiquidBlock(new BlockPos(i, 0, j)).getY();
            producePumpkins(worldIn, random, i, k, j);
        }

        if (random.nextInt(1000) == 0) {
            int i = chunkX + random.nextInt(16) + 8;
            int j = chunkZ + random.nextInt(16) + 8;
            int k = worldIn.getTopSolidOrLiquidBlock(new BlockPos(i, 0, j)).getY();
            produceWellDesert(worldIn, random, i, k, j);
        }
    }

    public boolean produceMelons(World arg76484n1, Random arg76484n2, int arg76484n3, int arg76484n4, int arg76484n5) {
        for (int l = 0; l < 64; ++l) {
            int i1 = arg76484n3 + arg76484n2.nextInt(8) - arg76484n2.nextInt(8);
            int j1 = arg76484n4 + arg76484n2.nextInt(4) - arg76484n2.nextInt(4);
            int k1 = arg76484n5 + arg76484n2.nextInt(8) - arg76484n2.nextInt(8);
            BlockPos pos = new BlockPos(i1, j1, k1);
            if (arg76484n1.isAirBlock(pos) && Blocks.MELON_BLOCK.canPlaceBlockAt(arg76484n1, pos) && arg76484n1.getBlockState(new BlockPos(i1, j1 - 1, k1)).getMaterial().isSolid()) {
                arg76484n1.setBlockState(pos, Blocks.MELON_BLOCK.getDefaultState(), 2);
            }
        }

        return true;
    }

    public boolean producePumpkins(World arg76484n1, Random arg76484n2, int arg76484n3, int arg76484n4, int arg76484n5) {
        for (int l = 0; l < 64; ++l) {
            int i1 = arg76484n3 + arg76484n2.nextInt(8) - arg76484n2.nextInt(8);
            int j1 = arg76484n4 + arg76484n2.nextInt(4) - arg76484n2.nextInt(4);
            int k1 = arg76484n5 + arg76484n2.nextInt(8) - arg76484n2.nextInt(8);
            BlockPos pos = new BlockPos(i1, j1, k1);
            if (arg76484n1.isAirBlock(pos) && Blocks.PUMPKIN.canPlaceBlockAt(arg76484n1, pos) && arg76484n1.getBlockState(new BlockPos(i1, j1 - 1, k1)).getMaterial().isSolid()) {
                arg76484n1.setBlockState(pos, Blocks.PUMPKIN.getStateFromMeta(arg76484n2.nextInt(4)), 2);
            }
        }

        return true;
    }

    public boolean produceWellDesert(World arg76484n1, Random arg76484n2, int arg76484n3, int arg76484n4, int arg76484n5) {
        int l;
        int i1;
        for (l = -2; l <= 2; ++l) {
            for (i1 = -2; i1 <= 2; ++i1) {
                if (arg76484n1.isAirBlock(new BlockPos(arg76484n3 + l, arg76484n4 - 1, arg76484n5 + i1)) && arg76484n1.isAirBlock(new BlockPos(arg76484n3 + l, arg76484n4 - 2, arg76484n5 + i1))) {
                    return false;
                }
            }
        }

        for (l = -1; l <= 0; ++l) {
            for (i1 = -2; i1 <= 2; ++i1) {
                for (int j1 = -2; j1 <= 2; ++j1) {
                    arg76484n1.setBlockState(new BlockPos(arg76484n3 + i1, arg76484n4 + l, arg76484n5 + j1), Blocks.SANDSTONE.getStateFromMeta(0), 2);
                }
            }
        }

        arg76484n1.setBlockState(new BlockPos(arg76484n3, arg76484n4, arg76484n5), Blocks.FLOWING_WATER.getStateFromMeta(0), 2);
        arg76484n1.setBlockState(new BlockPos(arg76484n3 - 1, arg76484n4, arg76484n5), Blocks.FLOWING_WATER.getStateFromMeta(0), 2);
        arg76484n1.setBlockState(new BlockPos(arg76484n3 + 1, arg76484n4, arg76484n5), Blocks.FLOWING_WATER.getStateFromMeta(0), 2);
        arg76484n1.setBlockState(new BlockPos(arg76484n3, arg76484n4, arg76484n5 - 1), Blocks.FLOWING_WATER.getStateFromMeta(0), 2);
        arg76484n1.setBlockState(new BlockPos(arg76484n3, arg76484n4, arg76484n5 + 1), Blocks.FLOWING_WATER.getStateFromMeta(0), 2);
        for (l = -2; l <= 2; ++l) {
            for (i1 = -2; i1 <= 2; ++i1) {
                if (l == -2 || l == 2 || i1 == -2 || i1 == 2) {
                    arg76484n1.setBlockState(new BlockPos(arg76484n3 + l, arg76484n4 + 1, arg76484n5 + i1), Blocks.SANDSTONE.getStateFromMeta(0), 2);
                }
            }
        }

        arg76484n1.setBlockState(new BlockPos(arg76484n3 + 2, arg76484n4 + 1, arg76484n5), Blocks.STONE_SLAB.getStateFromMeta(1), 2);
        arg76484n1.setBlockState(new BlockPos(arg76484n3 - 2, arg76484n4 + 1, arg76484n5), Blocks.STONE_SLAB.getStateFromMeta(1), 2);
        arg76484n1.setBlockState(new BlockPos(arg76484n3, arg76484n4 + 1, arg76484n5 + 2), Blocks.STONE_SLAB.getStateFromMeta(1), 2);
        arg76484n1.setBlockState(new BlockPos(arg76484n3, arg76484n4 + 1, arg76484n5 - 2), Blocks.STONE_SLAB.getStateFromMeta(1), 2);
        for (l = -1; l <= 1; ++l) {
            for (i1 = -1; i1 <= 1; ++i1) {
                if (l == 0 && i1 == 0) {
                    arg76484n1.setBlockState(new BlockPos(arg76484n3 + l, arg76484n4 + 4, arg76484n5 + i1), Blocks.SANDSTONE.getStateFromMeta(0), 2);
                }

                else {
                    arg76484n1.setBlockState(new BlockPos(arg76484n3 + l, arg76484n4 + 4, arg76484n5 + i1), Blocks.STONE_SLAB.getStateFromMeta(1), 2);
                }
            }
        }

        for (l = 1; l <= 3; ++l) {
            arg76484n1.setBlockState(new BlockPos(arg76484n3 - 1, arg76484n4 + l, arg76484n5 - 1), Blocks.SANDSTONE.getStateFromMeta(0), 2);
            arg76484n1.setBlockState(new BlockPos(arg76484n3 - 1, arg76484n4 + l, arg76484n5 + 1), Blocks.SANDSTONE.getStateFromMeta(0), 2);
            arg76484n1.setBlockState(new BlockPos(arg76484n3 + 1, arg76484n4 + l, arg76484n5 - 1), Blocks.SANDSTONE.getStateFromMeta(0), 2);
            arg76484n1.setBlockState(new BlockPos(arg76484n3 + 1, arg76484n4 + l, arg76484n5 + 1), Blocks.SANDSTONE.getStateFromMeta(0), 2);
        }

        return true;
    }
}
