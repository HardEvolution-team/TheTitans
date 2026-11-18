package com.electrifiedded.thetitans.world.gen;

import com.electrifiedded.thetitans.TheTitans;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenMobhenge extends WorldGeneratorBase {
    @Override
    public boolean generate(World world, Random rand, int x, int y, int z) {
        if (!world.isRemote
                && !world.getBlockState(new BlockPos(x, y, z)).isOpaqueCube()
                && !world.getBlockState(new BlockPos(x, y, z)).getMaterial().isLiquid()
                && world.getBlockState(new BlockPos(x, y - 1, z)).isOpaqueCube()
                && world.isAreaLoaded(new BlockPos(x, y, z), 1)) {
            try {
                produceR0(world, rand, x, y, z);
                TheTitans.debug("Mobhenge at " + x + ", " + y + ", " + z);
                return true;
            } catch (Exception var7) {
                TheTitans.debug("Failed to generate Mobhenge at " + x + ", " + y + ", " + z);
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean produceR0(World world, Random rand, int x, int y, int z) {
        String mob = "thetitans:ZombieMinion";
        switch (rand.nextInt(8)) {
            case 1:
                mob = "thetitans:SkeletonMinion";
                break;
            case 2:
                mob = "thetitans:SpiderMinion";
                break;
            case 3:
                mob = "thetitans:CaveSpiderMinion";
                break;
            case 4:
                mob = "thetitans:CreeperMinion";
                break;
            case 5:
                mob = "thetitans:ZombieMinion";
                break;
            case 6:
                mob = "thetitans:EndermanMinion";
                break;
            case 7:
                mob = "thetitans:PigZombieMinion";
                break;
            default:
                mob = "thetitans:SilverfishMinion";
        }

        columnGen(world, rand, x + 4, world.getHeight(x + 4, z), z, 4, true, false, mob);
        columnGen(world, rand, x - 4, world.getHeight(x - 4, z), z, 4, true, false, mob);
        columnGen(world, rand, x, world.getHeight(x, z + 4), z + 4, 3, false, false, mob);
        columnGen(world, rand, x, world.getHeight(x, z - 4), z - 4, 3, false, false, mob);
        columnGen(world, rand, x + 4, world.getHeight(x + 4, z + 7), z + 7, 2, false, true, mob);
        columnGen(world, rand, x + 4, world.getHeight(x + 4, z - 7), z - 7, 2, false, true, mob);
        columnGen(world, rand, x - 4, world.getHeight(x - 4, z + 7), z + 7, 2, false, true, mob);
        columnGen(world, rand, x - 4, world.getHeight(x - 4, z - 7), z - 7, 2, false, true, mob);
        columnGen(world, rand, x + 7, world.getHeight(x + 7, z + 4), z + 4, 2, true, true, mob);
        columnGen(world, rand, x + 7, world.getHeight(x + 7, z - 4), z - 4, 2, true, true, mob);
        columnGen(world, rand, x - 7, world.getHeight(x - 7, z + 4), z + 4, 2, true, true, mob);
        columnGen(world, rand, x - 7, world.getHeight(x - 7, z - 4), z - 4, 2, true, true, mob);
        columnGen(world, rand, x + 8, world.getHeight(x + 8, z), z, 2, true, true, mob);
        columnGen(world, rand, x - 8, world.getHeight(x - 8, z), z, 2, true, true, mob);
        columnGen(world, rand, x, world.getHeight(x, z + 8), z + 8, 2, false, true, mob);
        columnGen(world, rand, x, world.getHeight(x, z - 8), z - 8, 2, false, true, mob);

        Block block = Blocks.COBBLESTONE;
        if (mob == "thetitans:SilverfishMinion") {
            block = Blocks.MONSTER_EGG;
        }

        if (mob == "thetitans:PigZombieMinion") {
            block = Blocks.QUARTZ_BLOCK;
        }

        if (mob == "thetitans:EndermanMinion") {
            block = Blocks.STONEBRICK;
        }

        int meta = block == Blocks.MONSTER_EGG ? 1 : 0;
        world.setBlockState(new BlockPos(x + 12, world.getHeight(x + 12, z), z), block.getStateFromMeta(meta), 2);
        world.setBlockState(new BlockPos(x - 12, world.getHeight(x - 12, z), z), block.getStateFromMeta(meta), 2);
        world.setBlockState(new BlockPos(x, world.getHeight(x, z + 12), z + 12), block.getStateFromMeta(meta), 2);
        world.setBlockState(new BlockPos(x, world.getHeight(x, z - 12), z - 12), block.getStateFromMeta(meta), 2);
        world.setBlockState(new BlockPos(x + 11, world.getHeight(x + 11, z + 5), z + 5), block.getStateFromMeta(meta), 2);
        world.setBlockState(new BlockPos(x + 9, world.getHeight(x + 9, z + 9), z + 9), block.getStateFromMeta(meta), 2);
        world.setBlockState(new BlockPos(x + 5, world.getHeight(x + 5, z + 11), z + 11), block.getStateFromMeta(meta), 2);
        world.setBlockState(new BlockPos(x - 11, world.getHeight(x - 11, z + 5), z + 5), block.getStateFromMeta(meta), 2);
        world.setBlockState(new BlockPos(x - 9, world.getHeight(x - 9, z + 9), z + 9), block.getStateFromMeta(meta), 2);
        world.setBlockState(new BlockPos(x - 5, world.getHeight(x - 5, z + 11), z + 11), block.getStateFromMeta(meta), 2);
        world.setBlockState(new BlockPos(x - 11, world.getHeight(x - 11, z - 5), z - 5), block.getStateFromMeta(meta), 2);
        world.setBlockState(new BlockPos(x - 9, world.getHeight(x - 9, z - 9), z - 9), block.getStateFromMeta(meta), 2);
        world.setBlockState(new BlockPos(x - 5, world.getHeight(x - 5, z - 11), z - 11), block.getStateFromMeta(meta), 2);
        world.setBlockState(new BlockPos(x + 11, world.getHeight(x + 11, z - 5), z - 5), block.getStateFromMeta(meta), 2);
        world.setBlockState(new BlockPos(x + 9, world.getHeight(x + 9, z - 9), z - 9), block.getStateFromMeta(meta), 2);
        world.setBlockState(new BlockPos(x + 5, world.getHeight(x + 5, z - 11), z - 11), block.getStateFromMeta(meta), 2);

        for (int dy = 1; dy <= 10; ++dy) {
            world.setBlockState(new BlockPos(x, world.getHeight(x, z - (12 + dy * 6)), z - (12 + dy * 6)), block.getStateFromMeta(meta), 2);
            world.setBlockState(new BlockPos(x, world.getHeight(x, z + 12 + dy * 6), z + 12 + dy * 6), block.getStateFromMeta(meta), 2);
        }

        TheTitans.generateChest(world, "dungeonChest", new BlockPos(x, world.getHeight(x, z - 3), z - 3), EnumFacing.byIndex(0));
        TheTitans.generateChest(world, "dungeonChest", new BlockPos(x, world.getHeight(x, z + 3), z + 3), EnumFacing.byIndex(2));
        TheTitans.generateChest(world, "dungeonChest", new BlockPos(x + 3, world.getHeight(x + 3, z), z), EnumFacing.byIndex(4));
        TheTitans.generateChest(world, "dungeonChest", new BlockPos(x - 3, world.getHeight(x - 3, z), z), EnumFacing.byIndex(5));
        return true;
    }

    public void columnGen(World world, Random rand, int x, int y, int z, int height, boolean direction, boolean topslabs, String mob) {
        Block block = Blocks.COBBLESTONE;
        if (mob == "thetitans:SilverfishMinion") {
            block = Blocks.MONSTER_EGG;
        }

        if (mob == "thetitans:PigZombieMinion") {
            block = Blocks.QUARTZ_BLOCK;
        }

        if (mob == "thetitans:EndermanMinion") {
            block = Blocks.STONEBRICK;
        }

        int meta = block == Blocks.MONSTER_EGG ? 1 : 0;
        int slab = 3;
        if (mob == "thetitans:EndermanMinion") {
            slab = 5;
        }

        if (mob == "thetitans:PigZombieMinion") {
            slab = 7;
        }

        int dy;
        if (direction) {
            int zp = z + (topslabs ? 1 : 2);
            int zm = z - (topslabs ? 1 : 2);
            TheTitans.generateMobCage(world, mob, new BlockPos(x, world.getHeight(x, zp) - 1, zp));
            TheTitans.generateMobCage(world, mob, new BlockPos(x, world.getHeight(x, zm) - 1, zm));

            for (dy = 1; dy <= 100; ++dy) {
                if (world.getBlockState(new BlockPos(x, y - dy, zp)).getBlock() != Blocks.MOB_SPAWNER
                        && !world.getBlockState(new BlockPos(x, y - dy, zp)).isOpaqueCube()) {
                    world.setBlockState(new BlockPos(x, y - dy, zp), block.getStateFromMeta(meta), 2);
                }

                if (world.getBlockState(new BlockPos(x, y - dy, zm)).getBlock() != Blocks.MOB_SPAWNER
                        && !world.getBlockState(new BlockPos(x, y - dy, zm)).isOpaqueCube()) {
                    world.setBlockState(new BlockPos(x, y - dy, zm), block.getStateFromMeta(meta), 2);
                }

                if (world.getBlockState(new BlockPos(x, y - dy, zm)).isOpaqueCube()
                    && world.getBlockState(new BlockPos(x, y - dy, zp)).isOpaqueCube()) {
                    break;
                }
            }

            for (dy = 0; dy <= height; ++dy) {
                world.setBlockState(new BlockPos(x, y + dy, zp), block.getStateFromMeta(meta), 2);
                world.setBlockState(new BlockPos(x, y + dy, zm), block.getStateFromMeta(meta), 2);
            }

            for (dy = -(height / 2 + (topslabs ? 0 : 1)); dy <= height / 2 + (topslabs ? 0 : 1); ++dy) {
                world.setBlockState(new BlockPos(x, y + 1 + height, z + dy),
                        topslabs ? Blocks.STONE_SLAB.getStateFromMeta(slab) : block.getStateFromMeta(meta), 2);
            }
        } else {
            int xp = x + (topslabs ? 1 : 2);
            int xm = x - (topslabs ? 1 : 2);
            TheTitans.generateMobCage(world, mob, new BlockPos(xp, world.getHeight(xp, z) - 1, z));
            TheTitans.generateMobCage(world, mob, new BlockPos(xm, world.getHeight(xm, z) - 1, z));

            for (dy = 1; dy <= 100; ++dy) {
                if (world.getBlockState(new BlockPos(xp, y - dy, z)).getBlock() != Blocks.MOB_SPAWNER
                        && !world.getBlockState(new BlockPos(xp, y - dy, z)).isOpaqueCube()) {
                    world.setBlockState(new BlockPos(xp, y - dy, z), block.getStateFromMeta(meta), 2);
                }

                if (world.getBlockState(new BlockPos(xm, y - dy, z)).getBlock() != Blocks.MOB_SPAWNER
                        && !world.getBlockState(new BlockPos(xm, y - dy, z)).isOpaqueCube()) {
                    world.setBlockState(new BlockPos(xm, y - dy, z), block.getStateFromMeta(meta), 2);
                }

                if (world.getBlockState(new BlockPos(xp, y - dy, z)).isOpaqueCube()
                    && world.getBlockState(new BlockPos(xm, y - dy, z)).isOpaqueCube()) {
                    break;
                }
            }

            for (dy = 0; dy <= height; ++dy) {
                world.setBlockState(new BlockPos(xp, y + dy, z), block.getStateFromMeta(meta), 2);
                world.setBlockState(new BlockPos(xm, y + dy, z), block.getStateFromMeta(meta), 2);
            }

            for (dy = -(height / 2 + (topslabs ? 0 : 1)); dy <= height / 2 + (topslabs ? 0 : 1); ++dy) {
                world.setBlockState(new BlockPos(x + dy, y + 1 + height, z),
                        topslabs ? Blocks.STONE_SLAB.getStateFromMeta(slab) : block.getStateFromMeta(meta), 2);
            }
        }
    }

    public int fetchCubeTop(World world, int x, int z) {
        for (int k = 0; k > 0; --k) {
            Block block = world.getBlockState(new BlockPos(x, k, z)).getBlock();
            if (block.getDefaultState().getMaterial().blocksMovement()
                && block.getDefaultState().getMaterial() != Material.LEAVES
                && !block.isFoliage(world, new BlockPos(x, k, z))) {
                return k + 1;
            }
        }

        return -1;
    }
}
