package com.electrifiedded.thetitans.world.gen;

import com.electrifiedded.thetitans.TheTitans;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeDictionary;

import java.util.Random;

public class WorldGenBoulder extends WorldGeneratorBase {
    @Override
    public boolean generate(World world, Random rand, int x, int y, int z) {
        if (!world.isRemote
                && !world.getBlockState(new BlockPos(x, y, z)).isOpaqueCube()
                && !world.getBlockState(new BlockPos(x + 1, y, z)).isOpaqueCube()
                && !world.getBlockState(new BlockPos(x - 1, y, z)).isOpaqueCube()
                && !world.getBlockState(new BlockPos(x, y, z + 1)).isOpaqueCube()
                && !world.getBlockState(new BlockPos(x, y, z - 1)).isOpaqueCube()
                && world.getBlockState(new BlockPos(x, y - 1, z)).isOpaqueCube()
                && world.getBlockState(new BlockPos(x + 1, y - 1, z)).isOpaqueCube()
                && world.getBlockState(new BlockPos(x - 1, y - 1, z)).isOpaqueCube()
                && world.getBlockState(new BlockPos(x, y - 1, z + 1)).isOpaqueCube()
                && world.getBlockState(new BlockPos(x, y - 1, z - 1)).isOpaqueCube()
                && world.isAreaLoaded(new BlockPos(x, y, z), 1)) {
            try {
                produceR0(world, rand, x, y, z);
                return true;
            } catch (Exception var7) {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean produceR0(World world, Random rand, int x, int y, int z) {
        Biome biome = world.getBiome(new BlockPos(x, y, z));
        Block rock = Blocks.COBBLESTONE;
        if (BiomeDictionary.hasType(biome, Type.CONIFEROUS)) {
            rock = Blocks.MOSSY_COBBLESTONE;
        }

        if (BiomeDictionary.hasType(biome, Type.SANDY)) {
            rock = Blocks.SANDSTONE;
        }

        if (BiomeDictionary.hasType(biome, Type.MESA)) {
            rock = Blocks.HARDENED_CLAY;
        }

        if (BiomeDictionary.hasType(biome, Type.HILLS)) {
            rock = Blocks.STONE;
        }

        if (rand.nextInt(15) == 0 && BiomeDictionary.hasType(biome, Type.HILLS)) {
            rock = Blocks.MONSTER_EGG;
        }

        int dx;
        int dz;
        for (dx = -1; dx <= 1; ++dx) {
            for (dz = -1; dz <= 1; ++dz) {
                for (int dy = -1; dy <= 1; ++dy) {
                    world.setBlockState(new BlockPos(x + dx, y + dy, z + dz), rock.getStateFromMeta(0), 2);
                }
            }
        }

        if (rand.nextInt(2) == 0) {
            for (dx = -1; dx <= 1; ++dx) {
                for (dz = -1; dz <= 1; ++dz) {
                    world.setBlockState(new BlockPos(x + dx, y + dz, z + 2), rock.getStateFromMeta(0), 2);
                    world.setBlockState(new BlockPos(x + dx, y + dz, z - 2), rock.getStateFromMeta(0), 2);
                    world.setBlockState(new BlockPos(x + 2, y + dz, z + dx), rock.getStateFromMeta(0), 2);
                    world.setBlockState(new BlockPos(x - 2, y + dz, z + dx), rock.getStateFromMeta(0), 2);
                }

                for (dz = -1; dz <= 1; ++dz) {
                    world.setBlockState(new BlockPos(x + dx, y + 2, z + dz), rock.getStateFromMeta(0), 2);
                    world.setBlockState(new BlockPos(x + dx, y - 2, z + dz), rock.getStateFromMeta(0), 2);
                }
            }

            TheTitans.generateMobCage(world, rock == Blocks.MONSTER_EGG ? "minecraft:silverfish" : "minecraft:zombie", new BlockPos(x, y, z));
        }

        return true;
    }
}
