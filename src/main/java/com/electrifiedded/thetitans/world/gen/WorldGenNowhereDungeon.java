package com.electrifiedded.thetitans.world.gen;

import com.electrifiedded.thetitans.TheTitans;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenNowhereDungeon extends WorldGenerator {
    public boolean generate(World worldIn, Random rand, BlockPos pos) {
        int arg76484n3 = pos.getX();
        int arg76484n4 = pos.getY();
        int arg76484n5 = pos.getZ();
        byte b0 = 6;
        int l = 6;
        int i1 = 6;
        int j1 = 0;
        int k1;
        int l1;
        int i2;
        for (k1 = arg76484n3 - l - 1; k1 <= arg76484n3 + l + 1; ++k1) {
            for (l1 = arg76484n4 - 1; l1 <= arg76484n4 + b0 + 1; ++l1) {
                for (i2 = arg76484n5 - i1 - 1; i2 <= arg76484n5 + i1 + 1; ++i2) {
                    Material material = worldIn.getBlockState(new BlockPos(k1, l1, i2)).getMaterial();
                    if (l1 == arg76484n4 - 1 && !material.isSolid()) {
                        return false;
                    }

                    if (l1 == arg76484n4 + b0 + 1 && !material.isSolid()) {
                        return false;
                    }

                    if ((k1 == arg76484n3 - l - 1 || k1 == arg76484n3 + l + 1 || i2 == arg76484n5 - i1 - 1 || i2 == arg76484n5 + i1 + 1) && l1 == arg76484n4 && worldIn.isAirBlock(new BlockPos(k1, l1, i2)) && worldIn.isAirBlock(new BlockPos(k1, l1 + 1, i2))) {
                        ++j1;
                    }
                }
            }
        }

        if (j1 >= 1 && j1 <= 5) {
            for (k1 = arg76484n3 - l - 1; k1 <= arg76484n3 + l + 1; ++k1) {
                for (l1 = arg76484n4 + b0; l1 >= arg76484n4 - 1; --l1) {
                    for (i2 = arg76484n5 - i1 - 1; i2 <= arg76484n5 + i1 + 1; ++i2) {
                        if (k1 != arg76484n3 - l - 1 && l1 != arg76484n4 - 1 && i2 != arg76484n5 - i1 - 1 && k1 != arg76484n3 + l + 1 && l1 != arg76484n4 + b0 + 1 && i2 != arg76484n5 + i1 + 1) {
                            worldIn.setBlockToAir(new BlockPos(k1, l1, i2));
                        }

                        else if (l1 >= 0 && !worldIn.getBlockState(new BlockPos(k1, l1 - 1, i2)).getMaterial().isSolid()) {
                            worldIn.setBlockToAir(new BlockPos(k1, l1, i2));
                        }

                        else if (worldIn.getBlockState(new BlockPos(k1, l1, i2)).getMaterial().isSolid()) {
                            worldIn.setBlockState(new BlockPos(k1, l1, i2), Blocks.OBSIDIAN.getDefaultState(), 2);
                        }
                    }
                }
            }

            k1 = 0;
            while (k1 < 2) {
                l1 = 0;
                while (true) {
                    if (l1 < 3) {
                        label101: {
                            i2 = arg76484n3 + rand.nextInt(l * 2 + 1) - l;
                            int j2 = arg76484n5 + rand.nextInt(i1 * 2 + 1) - i1;
                            if (worldIn.isAirBlock(new BlockPos(i2, arg76484n4, j2))) {
                                int k2 = 0;
                                if (worldIn.getBlockState(new BlockPos(i2 - 1, arg76484n4, j2)).getMaterial().isSolid()) {
                                    ++k2;
                                }

                                if (worldIn.getBlockState(new BlockPos(i2 + 1, arg76484n4, j2)).getMaterial().isSolid()) {
                                    ++k2;
                                }

                                if (worldIn.getBlockState(new BlockPos(i2, arg76484n4, j2 - 1)).getMaterial().isSolid()) {
                                    ++k2;
                                }

                                if (worldIn.getBlockState(new BlockPos(i2, arg76484n4, j2 + 1)).getMaterial().isSolid()) {
                                    ++k2;
                                }

                                if (worldIn.getBlockState(new BlockPos(i2 - 1, arg76484n4, j2 - 1)).getMaterial().isSolid()) {
                                    ++k2;
                                }

                                if (worldIn.getBlockState(new BlockPos(i2 + 1, arg76484n4, j2 - 1)).getMaterial().isSolid()) {
                                    ++k2;
                                }

                                if (worldIn.getBlockState(new BlockPos(i2 - 1, arg76484n4, j2 - 1)).getMaterial().isSolid()) {
                                    ++k2;
                                }

                                if (worldIn.getBlockState(new BlockPos(i2 - 1, arg76484n4, j2 + 1)).getMaterial().isSolid()) {
                                    ++k2;
                                }

                                if (worldIn.getBlockState(new BlockPos(i2 - 1, arg76484n4, j2 + 1)).getMaterial().isSolid()) {
                                    ++k2;
                                }

                                if (worldIn.getBlockState(new BlockPos(i2 + 1, arg76484n4, j2 + 1)).getMaterial().isSolid()) {
                                    ++k2;
                                }

                                if (worldIn.getBlockState(new BlockPos(i2 + 1, arg76484n4, j2 - 1)).getMaterial().isSolid()) {
                                    ++k2;
                                }

                                if (worldIn.getBlockState(new BlockPos(i2 + 1, arg76484n4, j2 + 1)).getMaterial().isSolid()) {
                                    ++k2;
                                }

                                if (k2 == 1) {
                                    com.electrifiedded.thetitans.TheTitans.generateChest(worldIn, "pyramidDesertyChest", new BlockPos(i2, arg76484n4, j2), net.minecraft.util.EnumFacing.NORTH);
                                    break label101;
                                }
                            }

                            ++l1;
                            continue;
                        }
                    }

                    ++k1;
                    break;
                }
            }

            worldIn.setBlockState(new BlockPos(arg76484n3, arg76484n4, arg76484n5), Blocks.MOB_SPAWNER.getDefaultState(), 2);
            TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner)worldIn.getTileEntity(new BlockPos(arg76484n3, arg76484n4, arg76484n5));
            if (tileentitymobspawner != null) {
                tileentitymobspawner.getSpawnerBaseLogic().setEntityId(new ResourceLocation("minecraft", "enderman"));
            }

            else {
                System.err.println("Failed to fetch mob spawner entity at (" + arg76484n3 + ", " + arg76484n4 + ", " + arg76484n5 + ")");
            }

            worldIn.setBlockState(new BlockPos(arg76484n3, arg76484n4 + 1, arg76484n5), Blocks.MOB_SPAWNER.getDefaultState(), 2);
            TileEntityMobSpawner tileentitymobspawner1 = (TileEntityMobSpawner)worldIn.getTileEntity(new BlockPos(arg76484n3, arg76484n4 + 1, arg76484n5));
            if (tileentitymobspawner1 != null) {
                tileentitymobspawner1.getSpawnerBaseLogic().setEntityId(new ResourceLocation("minecraft", "enderman"));
            }

            else {
                System.err.println("Failed to fetch mob spawner entity at (" + arg76484n3 + ", " + (arg76484n4 + 1) + ", " + arg76484n5 + ")");
            }

            worldIn.setBlockState(new BlockPos(arg76484n3, arg76484n4 + 2, arg76484n5), Blocks.MOB_SPAWNER.getDefaultState(), 2);
            TileEntityMobSpawner tileentitymobspawner11 = (TileEntityMobSpawner)worldIn.getTileEntity(new BlockPos(arg76484n3, arg76484n4 + 2, arg76484n5));
            if (tileentitymobspawner11 != null) {
                tileentitymobspawner11.getSpawnerBaseLogic().setEntityId(new ResourceLocation("minecraft", "enderman"));
            }

            else {
                System.err.println("Failed to fetch mob spawner entity at (" + arg76484n3 + ", " + (arg76484n4 + 2) + ", " + arg76484n5 + ")");
            }

            return true;
        }

        else {
            return false;
        }
    }
}
