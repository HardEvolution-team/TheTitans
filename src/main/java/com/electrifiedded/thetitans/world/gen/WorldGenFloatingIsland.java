package com.electrifiedded.thetitans.world.gen;

import com.electrifiedded.thetitans.TheTitans;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockGrass;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenDoublePlant;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeDictionary;

import java.util.Random;

public class WorldGenFloatingIsland extends WorldGeneratorBase {
    @Override
    public boolean generate(World world, Random rand, int x, int y, int z) {
        if (!world.isRemote
                && world.isAirBlock(new BlockPos(x, y, z))
                && (float)y >= world.getSeaLevel() - 24.0F
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
        int planktype = 0;
        if (BiomeDictionary.hasType(biome, Type.CONIFEROUS)) {
            planktype = 1; // spruce
        }

        if (BiomeDictionary.hasType(biome, Type.SANDY) || BiomeDictionary.hasType(biome, Type.SAVANNA)) {
            planktype = 2; // acacia (was birch forest hills group)
        }

        if (BiomeDictionary.hasType(biome, Type.JUNGLE)) {
            planktype = 3; // jungle
        }

        int size = rand.nextInt(8);
        int area = 8 + size;

        int layer;
        int dx;
        for (layer = -area; layer <= area; ++layer) {
            for (dx = -area; dx <= area; ++dx) {
                world.setBlockState(new BlockPos(x + layer, y, z + dx), Blocks.GRASS.getStateFromMeta(0), 2);
                world.setBlockState(new BlockPos(x + layer, y - 1, z + dx), Blocks.DIRT.getStateFromMeta(0), 2);
                if (world.getBlockState(new BlockPos(x + layer, y, z + dx)).getBlock() instanceof BlockGrass && rand.nextInt(30) == 0) {
                    net.minecraft.block.state.IBlockState log;
                    net.minecraft.block.state.IBlockState leaves;
                    switch (planktype) {
                        case 1:
                            log = Blocks.LOG.getDefaultState().withProperty(net.minecraft.block.BlockOldLog.VARIANT, net.minecraft.block.BlockPlanks.EnumType.SPRUCE);
                            leaves = Blocks.LEAVES.getDefaultState().withProperty(net.minecraft.block.BlockOldLeaf.VARIANT, net.minecraft.block.BlockPlanks.EnumType.SPRUCE).withProperty(net.minecraft.block.BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
                            break;
                        case 2:
                            log = Blocks.LOG2.getDefaultState().withProperty(net.minecraft.block.BlockNewLog.VARIANT, net.minecraft.block.BlockPlanks.EnumType.ACACIA);
                            leaves = Blocks.LEAVES2.getDefaultState().withProperty(net.minecraft.block.BlockNewLeaf.VARIANT, net.minecraft.block.BlockPlanks.EnumType.ACACIA).withProperty(net.minecraft.block.BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
                            break;
                        case 3:
                            log = Blocks.LOG.getDefaultState().withProperty(net.minecraft.block.BlockOldLog.VARIANT, net.minecraft.block.BlockPlanks.EnumType.JUNGLE);
                            leaves = Blocks.LEAVES.getDefaultState().withProperty(net.minecraft.block.BlockOldLeaf.VARIANT, net.minecraft.block.BlockPlanks.EnumType.JUNGLE).withProperty(net.minecraft.block.BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
                            break;
                        default:
                            log = Blocks.LOG.getDefaultState().withProperty(net.minecraft.block.BlockOldLog.VARIANT, net.minecraft.block.BlockPlanks.EnumType.OAK);
                            leaves = Blocks.LEAVES.getDefaultState().withProperty(net.minecraft.block.BlockOldLeaf.VARIANT, net.minecraft.block.BlockPlanks.EnumType.OAK).withProperty(net.minecraft.block.BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
                    }
                    (new WorldGenTrees(true, 4 + rand.nextInt(2), log, leaves, BiomeDictionary.hasType(biome, Type.SWAMP))).generate(world, rand, new BlockPos(x + layer, y + 1, z + dx));
                    TheTitans.generateMobCage(world, rand.nextInt(2) == 0 ? "minecraft:cow" : (rand.nextInt(2) == 0 ? "minecraft:pig" : "minecraft:chicken"), new BlockPos(x + layer, y, z + dx));
                }

                if (world.getBlockState(new BlockPos(x + layer, y, z + dx)).getBlock() instanceof BlockGrass && rand.nextInt(20) == 0) {
                    WorldGenDoublePlant doublePlant = new WorldGenDoublePlant();
                    doublePlant.setPlantType(BlockDoublePlant.EnumPlantType.values()[rand.nextInt(BlockDoublePlant.EnumPlantType.values().length)]);
                    doublePlant.generate(world, rand, new BlockPos(x + layer, y, z + dx));
                }

                if (world.getBlockState(new BlockPos(x + layer, y, z + dx)).getBlock() instanceof BlockGrass && rand.nextInt(15) == 0) {
                    ((BlockGrass)world.getBlockState(new BlockPos(x + layer, y, z + dx)).getBlock()).grow(world, rand, new BlockPos(x + layer, y, z + dx), world.getBlockState(new BlockPos(x + layer, y, z + dx)));
                }
            }
        }

        for (layer = 0; layer <= area - 2; ++layer) {
            for (dx = -area + layer; dx <= area - layer; ++dx) {
                for (int var13 = -area + layer; var13 <= area - layer; ++var13) {
                    world.setBlockState(new BlockPos(x - dx, y - 2 - layer, z - var13), Blocks.STONE.getStateFromMeta(0), 2);
                }
            }
        }

        return true;
    }
}
