package com.electrifiedded.thetitans.world.gen;

import com.electrifiedded.thetitans.TheTitans;

import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeDictionary;

import java.util.Random;

public class WorldGenSurvivalDome extends WorldGenerator {
    @Override
    public boolean generate(World world, Random rand, BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (!world.isRemote
                && world.isAirBlock(pos)
                && world.getBlockState(pos.down()).isOpaqueCube()
                && world.canBlockSeeSky(pos)) {
            try {
                produceR0(world, rand, x, y, z);
                TheTitans.debug("Suvival House at " + x + ", " + y + ", " + z);
                return true;
            } catch (Exception var7) {
                TheTitans.debug("Failed to generate Survival House at " + x + ", " + y + ", " + z);
                return false;
            }
        }
        return false;
    }

    public boolean produceR0(World world, Random rand, int x, int y, int z) {
        Biome biome = world.getBiome(new BlockPos(x, y, z));
        boolean hasbricks = rand.nextInt(3) == 0;
        boolean longabandoned = rand.nextInt(3) == 0;
        boolean verylongabandoned = longabandoned && rand.nextInt(3) == 0;
        int planktype = 0;
        if (BiomeDictionary.hasType(biome, Type.CONIFEROUS)) {
            planktype = 1; // spruce
        }

        if (BiomeDictionary.hasType(biome, Type.SANDY)) {
            planktype = 2; // birch (was desert/birch-hills group)
        }

        if (BiomeDictionary.hasType(biome, Type.JUNGLE)) {
            planktype = 3; // jungle
        }

        if (BiomeDictionary.hasType(biome, Type.SAVANNA) || BiomeDictionary.hasType(biome, Type.MESA)) {
            planktype = 4; // acacia
        }

        if (BiomeDictionary.hasType(biome, Type.SPOOKY))
            {
            planktype = 5; // dark oak
        }

        if (hasbricks) {
            planktype = 0;
        }

        int inhabitant;
        int protector;
        int dy;
        for (inhabitant = -4; inhabitant <= 4; ++inhabitant) {
            for (protector = -4; protector <= 4; ++protector) {
                for (dy = -1; dy <= 4; ++dy) {
                    if (dy != -1 && dy != 4 && inhabitant < 4 && inhabitant > -4 && protector < 4 && protector > -4) {
                        world.setBlockState(new BlockPos(x + inhabitant, y + dy, z + protector), Blocks.AIR.getStateFromMeta(0), 2);
                        if (rand.nextInt(5) == 0 && dy > 2
                                && (world.getBlockState(new BlockPos(x + inhabitant, y + 1 + dy, z + protector)).isOpaqueCube()
                                || world.getBlockState(new BlockPos(x + 1 + inhabitant, y + dy, z + protector)).isOpaqueCube()
                                || world.getBlockState(new BlockPos(x - 1 + inhabitant, y + dy, z + protector)).isOpaqueCube()
                                || world.getBlockState(new BlockPos(x + inhabitant, y + dy, z + 1 + protector)).isOpaqueCube()
                                || world.getBlockState(new BlockPos(x + inhabitant, y + dy, z - 1 + protector)).isOpaqueCube()
                                || world.getBlockState(new BlockPos(x + inhabitant, y + 1 + dy, z + protector)).getBlock() == Blocks.WEB
                                || world.getBlockState(new BlockPos(x + 1 + inhabitant, y + dy, z + protector)).getBlock() == Blocks.WEB
                                || world.getBlockState(new BlockPos(x - 1 + inhabitant, y + dy, z + protector)).getBlock() == Blocks.WEB
                                || world.getBlockState(new BlockPos(x + inhabitant, y + dy, z + 1 + protector)).getBlock() == Blocks.WEB
                                || world.getBlockState(new BlockPos(x + inhabitant, y + dy, z - 1 + protector)).getBlock() == Blocks.WEB)
                                && (longabandoned || verylongabandoned)) {
                            world.setBlockState(new BlockPos(x + inhabitant, y + dy, z + protector), Blocks.WEB.getStateFromMeta(0), 2);
                        }
                    } else if (dy == 1) {
                        world.setBlockState(new BlockPos(x + inhabitant, y + dy, z + protector), rand.nextInt(15) == 0 && longabandoned ? Blocks.AIR.getStateFromMeta(0) : Blocks.GLASS.getStateFromMeta(0), 2);
                        if (inhabitant == 4 && protector == 4 || inhabitant == -4 && protector == 4 || inhabitant == -4 && protector == -4 || inhabitant == 4 && protector == -4) {
                            world.setBlockState(new BlockPos(x + inhabitant, y + dy, z + protector), hasbricks ? Blocks.BRICK_BLOCK.getStateFromMeta(0) : Blocks.PLANKS.getStateFromMeta(planktype), 2);
                        }

                        if (inhabitant == 4 && protector == 0 || inhabitant == -4 && protector == 0 || inhabitant == 0 && protector == -4 || inhabitant == 0 && protector == 4) {
                            world.setBlockState(new BlockPos(x + inhabitant, y + dy, z + protector), hasbricks ? Blocks.BRICK_BLOCK.getStateFromMeta(0) : Blocks.PLANKS.getStateFromMeta(planktype), 2);
                        }
                    } else {
                        world.setBlockState(new BlockPos(x + inhabitant, y + dy, z + protector), hasbricks ? Blocks.BRICK_BLOCK.getStateFromMeta(0) : Blocks.PLANKS.getStateFromMeta(planktype), 2);
                        if (verylongabandoned && dy >= 2 && (rand.nextInt(6 - dy) == 0 || dy == 4)) {
                            world.setBlockState(new BlockPos(x + inhabitant, y + dy, z + protector), Blocks.AIR.getStateFromMeta(0), 2);
                        }
                    }
                }
            }
        }

        for (inhabitant = -4; inhabitant <= 4; ++inhabitant) {
            for (protector = -4; protector <= 4; ++protector) {
                for (dy = 2; dy <= 12; ++dy) {
                    if (world.getBlockState(new BlockPos(x + inhabitant, y - dy, z + protector)).getBlock() != Blocks.MOB_SPAWNER
                            && !world.getBlockState(new BlockPos(x + inhabitant, y - dy, z + protector)).isOpaqueCube()) {
                        world.setBlockState(new BlockPos(x + inhabitant, y - dy, z + protector), BiomeDictionary.hasType(biome, Type.SANDY) ? Blocks.SANDSTONE.getStateFromMeta(0) : Blocks.DIRT.getStateFromMeta(0), 3);
                    }
                }
            }
        }

        TheTitans.generateChest(world, "dungeonChest", new BlockPos(x - 3, y, z + 3), EnumFacing.byIndex(2));
        TheTitans.generateChest(world, "villageBlacksmith", new BlockPos(x - 2, y, z + 3), EnumFacing.byIndex(2));
        world.setBlockState(new BlockPos(x - 1, y, z + 3), Blocks.CRAFTING_TABLE.getStateFromMeta(0), 2);
        world.setBlockState(new BlockPos(x - 1, y + 1, z + 3), Blocks.BREWING_STAND.getStateFromMeta(0), 2);
        TheTitans.generateFurnace(world, new BlockPos(x, y, z + 3), EnumFacing.byIndex(2));
        world.setBlockState(new BlockPos(x + 1, y, z + 3), Blocks.ANVIL.getStateFromMeta(0), 2);
        TheTitans.generateChest(world, "strongholdLibrary", new BlockPos(x + 2, y, z + 3), EnumFacing.byIndex(2));
        TheTitans.generateChest(world, "bonusChest", new BlockPos(x + 3, y, z + 3), EnumFacing.byIndex(2));
        if (!longabandoned && !verylongabandoned) {
            world.setBlockState(new BlockPos(x, y + 3, z), Blocks.GLOWSTONE.getStateFromMeta(0), 2);
            world.notifyNeighborsOfStateChange(new BlockPos(x, y + 3, z), Blocks.GLOWSTONE, true);
            label267:
            switch (rand.nextInt(3)) {
                case 1:
                    TheTitans.generateDoor(world, Blocks.OAK_DOOR, new BlockPos(x + 4, y, z), EnumFacing.byHorizontalIndex(2));
                    for (inhabitant = 0; inhabitant <= 4; ++inhabitant) {
                        world.setBlockState(new BlockPos(x + 5, y + inhabitant, z), Blocks.AIR.getStateFromMeta(0), 2);
                    }

                    for (inhabitant = 1; inhabitant <= 4; ++inhabitant) {
                        world.setBlockState(new BlockPos(x + 5, y + inhabitant, z + 1), Blocks.AIR.getStateFromMeta(0), 2);
                        world.setBlockState(new BlockPos(x + 5, y + inhabitant, z - 1), Blocks.AIR.getStateFromMeta(0), 2);
                    }

                    for (inhabitant = 2; inhabitant <= 4; ++inhabitant) {
                        world.setBlockState(new BlockPos(x + 5, y + inhabitant, z + 2), Blocks.AIR.getStateFromMeta(0), 2);
                        world.setBlockState(new BlockPos(x + 5, y + inhabitant, z - 2), Blocks.AIR.getStateFromMeta(0), 2);
                    }

                    inhabitant = 3;
                    while (true) {
                        if (inhabitant > 4) {
                            break label267;
                        }

                        world.setBlockState(new BlockPos(x + 5, y + inhabitant, z + 3), Blocks.AIR.getStateFromMeta(0), 2);
                        world.setBlockState(new BlockPos(x + 5, y + inhabitant, z - 3), Blocks.AIR.getStateFromMeta(0), 2);
                        ++inhabitant;
                    }
                case 2:
                    TheTitans.generateDoor(world, Blocks.OAK_DOOR, new BlockPos(x, y, z - 4), EnumFacing.byHorizontalIndex(1));
                    for (inhabitant = 0; inhabitant <= 4; ++inhabitant) {
                        world.setBlockState(new BlockPos(x, y + inhabitant, z - 5), Blocks.AIR.getStateFromMeta(0), 2);
                    }

                    for (inhabitant = 1; inhabitant <= 4; ++inhabitant) {
                        world.setBlockState(new BlockPos(x + 1, y + inhabitant, z - 5), Blocks.AIR.getStateFromMeta(0), 2);
                        world.setBlockState(new BlockPos(x - 1, y + inhabitant, z - 5), Blocks.AIR.getStateFromMeta(0), 2);
                    }

                    for (inhabitant = 2; inhabitant <= 4; ++inhabitant) {
                        world.setBlockState(new BlockPos(x + 2, y + inhabitant, z - 5), Blocks.AIR.getStateFromMeta(0), 2);
                        world.setBlockState(new BlockPos(x - 2, y + inhabitant, z - 5), Blocks.AIR.getStateFromMeta(0), 2);
                    }

                    inhabitant = 3;
                    while (true) {
                        if (inhabitant > 4) {
                            break label267;
                        }

                        world.setBlockState(new BlockPos(x + 3, y + inhabitant, z - 5), Blocks.AIR.getStateFromMeta(0), 2);
                        world.setBlockState(new BlockPos(x - 3, y + inhabitant, z - 5), Blocks.AIR.getStateFromMeta(0), 2);
                        ++inhabitant;
                    }
                default:
                    TheTitans.generateDoor(world, Blocks.OAK_DOOR, new BlockPos(x - 4, y, z), EnumFacing.byHorizontalIndex(0));
                    for (inhabitant = 0; inhabitant <= 4; ++inhabitant) {
                        world.setBlockState(new BlockPos(x - 5, y + inhabitant, z), Blocks.AIR.getStateFromMeta(0), 2);
                    }

                    for (inhabitant = 1; inhabitant <= 4; ++inhabitant) {
                        world.setBlockState(new BlockPos(x - 5, y + inhabitant, z + 1), Blocks.AIR.getStateFromMeta(0), 2);
                        world.setBlockState(new BlockPos(x - 5, y + inhabitant, z - 1), Blocks.AIR.getStateFromMeta(0), 2);
                    }

                    for (inhabitant = 2; inhabitant <= 4; ++inhabitant) {
                        world.setBlockState(new BlockPos(x - 5, y + inhabitant, z + 2), Blocks.AIR.getStateFromMeta(0), 2);
                        world.setBlockState(new BlockPos(x - 5, y + inhabitant, z - 2), Blocks.AIR.getStateFromMeta(0), 2);
                    }

                    for (inhabitant = 3; inhabitant <= 4; ++inhabitant) {
                        world.setBlockState(new BlockPos(x - 5, y + inhabitant, z + 3), Blocks.AIR.getStateFromMeta(0), 2);
                        world.setBlockState(new BlockPos(x - 5, y + inhabitant, z - 3), Blocks.AIR.getStateFromMeta(0), 2);
                    }
            }

            if (rand.nextInt(2) == 0) {
                EntityVillager villager = new EntityVillager(world);
                villager.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
                villager.setProfession(2);
                villager.enablePersistence();
                villager.setCustomNameTag("Lone Cleric");
                if (rand.nextInt(2) == 0) {
                    EntityIronGolem golem = new EntityIronGolem(world);
                    golem.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
                    golem.enablePersistence();
                    world.spawnEntity(golem);
                    TheTitans.debug(golem.getName() + " at " + x + ", " + y + ", " + z);
                }

                world.spawnEntity(villager);
                TheTitans.debug(villager.getName() + " at " + x + ", " + y + ", " + z);
            }
        } else {
            TheTitans.generateMobCage(world, rand.nextInt(2) == 0 ? "minecraft:skeleton" : "minecraft:zombie", new BlockPos(x - 1, y - 1, z + 3));
            TheTitans.generateMobCage(world, rand.nextInt(2) == 0 ? "minecraft:cave_spider" : "minecraft:spider", new BlockPos(x, y - 1, z + 3));
            if (!verylongabandoned) {
                TheTitans.generateMobCage(world, "minecraft:bat", new BlockPos(x, y + 3, z));
            }

            switch (rand.nextInt(3)) {
                case 1:
                    world.setBlockState(new BlockPos(x + 4, y, z), Blocks.AIR.getStateFromMeta(0), 2);
                    world.setBlockState(new BlockPos(x + 4, y + 1, z), Blocks.AIR.getStateFromMeta(0), 2);
                    break;
                case 2:
                    world.setBlockState(new BlockPos(x, y, z - 4), Blocks.AIR.getStateFromMeta(0), 2);
                    world.setBlockState(new BlockPos(x, y + 1, z - 4), Blocks.AIR.getStateFromMeta(0), 2);
                    break;
                default:
                    world.setBlockState(new BlockPos(x - 4, y, z), Blocks.AIR.getStateFromMeta(0), 2);
                    world.setBlockState(new BlockPos(x - 4, y + 1, z), Blocks.AIR.getStateFromMeta(0), 2);
            }
        }

        return true;
    }
}
