package com.electrifiedded.thetitans.world.gen;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.block.ore.BlockNormalOre;
import com.electrifiedded.thetitans.registry.TitanBlockSet;
import com.electrifiedded.thetitans.util.Maths.Vec3;
import com.electrifiedded.thetitans.util.Maths;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WorldGenStuffs implements IWorldGenerator {
    private final Map<WorldGeneratorBase, Vec3> structurePrev = new HashMap<WorldGeneratorBase, Vec3>();
    private static class GenOre {
        private List<Block> ores = new ArrayList<Block>();
        private List<Integer> iDsDimension = new ArrayList<Integer>();
        private Map<Integer, Integer> ysCap = new HashMap<Integer, Integer>();
        private boolean whitelistIs;
        public int scaleVein;
        public int perChunkVeins;
        public GenOre(Block ore, int scaleVein, int perChunkVeins, int maxY, boolean whitelistIs) {
            if (ore != null && TitanBlockSet.exists(ore))
                ores.add(ore);
            ysCap.put(0, maxY);
            this.scaleVein = scaleVein;
            this.perChunkVeins = perChunkVeins;
            this.whitelistIs = whitelistIs;
        }

        public GenOre(Block[] ores, int scaleVein, int perChunkVeins, int maxY, boolean whitelistIs) {
            for (int i = 0; i < ores.length; i++)
                if (ores[i] != null && TitanBlockSet.exists(ores[i]))
                this.ores.add(ores[i]);
            ysCap.put(0, maxY);
            this.scaleVein = scaleVein;
            this.perChunkVeins = perChunkVeins;
            this.whitelistIs = whitelistIs;
        }

        public Block getOre(Block base) {
            for(int i = 0; i < ores.size(); i++)
                if (ores.get(i) != null && ores.get(i) instanceof BlockNormalOre && ((BlockNormalOre)ores.get(i)).cubeBase.equals(base))
                return ores.get(i);
            return !ores.isEmpty() ? ores.get(0) : null;
        }

        public GenOre appendOffsetY(int dimensionID, int yOffset) {
            if (!ysCap.containsKey(dimensionID))
                ysCap.put(dimensionID, yOffset);
            return this;
        }

        public GenOre appendDimension(int... iDsDimension) {
            for (int i = 0; i < iDsDimension.length; i++)
                if (!this.iDsDimension.contains(iDsDimension[i]))
                this.iDsDimension.add(iDsDimension[i]);
            return this;
        }

        public boolean blacklistIs() {
            return !whitelistIs;
        }

        public boolean whitelistIs() {
            return whitelistIs;
        }

        public int fetchYMax(int dimensionID) {
            if (ysCap.containsKey(dimensionID))
                return ysCap.get(dimensionID);
            return ysCap.get(0);
        }

        public boolean isAllowed(int dimensionID) {
            if (ores.size() > 0)
                if (whitelistIs)
                return !iDsDimension.isEmpty() && iDsDimension.contains(dimensionID);
            else
                return iDsDimension.isEmpty() || !iDsDimension.contains(dimensionID);
            else
                return false;
        }
    }

    private GenOre[] ores = {
        new GenOre(TitanBlockSet.oreJadeiteZ, 9, 2, 20, false).appendDimension(TheTitans.DIMENSION_KEY_VOID),
                //new GenOre(TitanBlockSet.oreDemontiumZ, 6 ,2 ,16, false).appendDimension(TheTitans.DIMENSION_KEY_VOID),
                new GenOre(TitanBlockSet.oreHarcadiumZ, 5 ,1 ,8, false).appendDimension(TheTitans.DIMENSION_KEY_VOID, TheTitans.DIMENSION_KEY_NOWHERE, 1),
                new GenOre(TitanBlockSet.oreEndStoneHarcadium, 10 ,25 ,128, true).appendDimension(1),
                new GenOre(TitanBlockSet.oreObsidianHarcadium, 5 ,20 ,256, true).appendDimension(TheTitans.DIMENSION_KEY_NOWHERE),
                new GenOre(TitanBlockSet.oreWithreniumZ, 6 ,10 ,32, false).appendDimension(TheTitans.DIMENSION_KEY_VOID, 0, 1),
                new GenOre(TitanBlockSet.oreVoidZ, 3 ,1 ,4, false).appendDimension(TheTitans.DIMENSION_KEY_VOID, TheTitans.DIMENSION_KEY_NOWHERE, 1),
                new GenOre(TitanBlockSet.oreEndStoneVoid, 7 ,2 ,128, true).appendDimension(1),
                new GenOre(TitanBlockSet.oreObsidianVoid, 8 ,10 ,256, true).appendDimension(TheTitans.DIMENSION_KEY_NOWHERE),
                new GenOre(TitanBlockSet.fuelOreMolten, 16, 10, 128, false).appendDimension(TheTitans.DIMENSION_KEY_VOID),
                new GenOre(TitanBlockSet.ironOrePig, 16, 10, 128, false).appendDimension(TheTitans.DIMENSION_KEY_VOID),
                new GenOre(TitanBlockSet.oreRubyZ, 7, 2, 28, false).appendDimension(TheTitans.DIMENSION_KEY_VOID),
                new GenOre(TitanBlockSet.oreSapphireZ, 7, 2, 28, false).appendDimension(TheTitans.DIMENSION_KEY_VOID),
                new GenOre(TitanBlockSet.oreCopperZ, 16, 20, 128, false).appendDimension(TheTitans.DIMENSION_KEY_VOID),
                new GenOre(TitanBlockSet.oreTinZ, 16, 20, 128, false).appendDimension(TheTitans.DIMENSION_KEY_VOID),
                new GenOre(TitanBlockSet.oreChromiumZ, 9, 20, 48, false).appendDimension(TheTitans.DIMENSION_KEY_VOID),
                new GenOre(TitanBlockSet.oreNickelZ, 9, 20, 48, false).appendDimension(TheTitans.DIMENSION_KEY_VOID),
                new GenOre(TitanBlockSet.oreLeadZ, 9, 20, 48, false).appendDimension(TheTitans.DIMENSION_KEY_VOID),
                new GenOre(TitanBlockSet.oreSilverZ, 8, 8, 32, false).appendDimension(TheTitans.DIMENSION_KEY_VOID),
                new GenOre(TitanBlockSet.orePlatinumZ, 7, 2, 10, false).appendDimension(TheTitans.DIMENSION_KEY_VOID),
                new GenOre(TitanBlockSet.oreTitaniumZ, 7, 2, 10, false).appendDimension(TheTitans.DIMENSION_KEY_VOID),
                new GenOre(TitanBlockSet.oreCoalZ, 16, 20, 128, true).appendDimension(-1, 1, TheTitans.DIMENSION_KEY_NOWHERE),
                new GenOre(TitanBlockSet.oreIronZ, 14, 20, 63, true).appendDimension(-1, 1, TheTitans.DIMENSION_KEY_NOWHERE).appendOffsetY(-1, 128),
                new GenOre(TitanBlockSet.oreRedstoneZ, 8, 8, 16, true).appendDimension(-1, 1, TheTitans.DIMENSION_KEY_NOWHERE).appendOffsetY(-1, 64),
                new GenOre(TitanBlockSet.oreGoldZ, 16, 2, 32, true).appendDimension(-1, 1, TheTitans.DIMENSION_KEY_NOWHERE).appendOffsetY(-1, 128),
                new GenOre(TitanBlockSet.oreLapis, 8, 4, 30, true).appendDimension(-1, 1, TheTitans.DIMENSION_KEY_NOWHERE).appendOffsetY(-1, 64),
                new GenOre(TitanBlockSet.oreEmeraldZ, 2, 2, 32, true).appendDimension(-1, 1, TheTitans.DIMENSION_KEY_NOWHERE),
                new GenOre(TitanBlockSet.oreDiamondZ, 7, 1, 16, true).appendDimension(-1, 1, TheTitans.DIMENSION_KEY_NOWHERE),
                new GenOre(Blocks.COAL_ORE, 32, 1, 128, true).appendDimension(0, TheTitans.DIMENSION_KEY_VOID).appendOffsetY(TheTitans.DIMENSION_KEY_NOWHERE, 64),
                new GenOre(Blocks.IRON_ORE, 24, 1, 63, true).appendDimension(0, TheTitans.DIMENSION_KEY_VOID).appendOffsetY(TheTitans.DIMENSION_KEY_NOWHERE, 64),
                new GenOre(Blocks.GOLD_ORE, 24, 1, 32, true).appendDimension(0, TheTitans.DIMENSION_KEY_VOID).appendOffsetY(TheTitans.DIMENSION_KEY_NOWHERE, 64),
                new GenOre(Blocks.DIAMOND_ORE, 18, 1, 16, true).appendDimension(0, TheTitans.DIMENSION_KEY_VOID).appendOffsetY(TheTitans.DIMENSION_KEY_NOWHERE, 64),
                new GenOre(Blocks.EMERALD_ORE, 18, 1, 16, true).appendDimension(0, TheTitans.DIMENSION_KEY_VOID).appendOffsetY(TheTitans.DIMENSION_KEY_NOWHERE, 64),
                new GenOre(Blocks.REDSTONE_ORE, 18, 1, 32, true).appendDimension(0, TheTitans.DIMENSION_KEY_VOID).appendOffsetY(TheTitans.DIMENSION_KEY_NOWHERE, 64),
                new GenOre(Blocks.LAPIS_ORE, 16, 1, 16, true).appendDimension(0, TheTitans.DIMENSION_KEY_VOID).appendOffsetY(TheTitans.DIMENSION_KEY_NOWHERE, 64),
                new GenOre(Blocks.COAL_BLOCK, 16, 1, 128, false).appendDimension(TheTitans.DIMENSION_KEY_VOID),
                new GenOre(Blocks.IRON_BLOCK, 8, 1, 64, false).appendDimension(TheTitans.DIMENSION_KEY_VOID).appendOffsetY(-1, 128),
                new GenOre(Blocks.GOLD_BLOCK, 8, 1, 32, false).appendDimension(TheTitans.DIMENSION_KEY_VOID).appendOffsetY(-1, 128),
                new GenOre(Blocks.DIAMOND_BLOCK, 5, 1, 16, false).appendDimension(TheTitans.DIMENSION_KEY_VOID),
                new GenOre(Blocks.EMERALD_BLOCK, 5, 1, 16, false).appendDimension(TheTitans.DIMENSION_KEY_VOID),
                new GenOre(Blocks.REDSTONE_BLOCK, 7, 1, 32, false).appendDimension(TheTitans.DIMENSION_KEY_VOID).appendOffsetY(-1, 64),
                new GenOre(Blocks.LAPIS_BLOCK, 6, 1, 16, false).appendDimension(TheTitans.DIMENSION_KEY_VOID).appendOffsetY(-1, 64),
                new GenOre(TitanBlockSet.netherite, 20, 50, 256, true).appendDimension(-1)
    };

    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        world.profiler.startSection("genTitanStuffs");
        int dimensionId = world.provider.getDimension();
        for (int i = 0; i < ores.length; i++)
            generateOre(random, world, chunkX, chunkZ, dimensionId, ores[i]);
        switch (dimensionId) {
            case -1: break;
            case 1: break;
            case TheTitans.DIMENSION_KEY_NOWHERE: break;
            case TheTitans.DIMENSION_KEY_VOID: {
                    if (random.nextInt(2) == 0)
                        generateOre(random, world, chunkX, chunkZ, Blocks.BEDROCK, Blocks.AIR, 64, 1, 64);
                    generateOre(random, world, chunkX, chunkZ, TitanBlockSet.bedrock_block, Blocks.BEDROCK, 32, 20, 256);
                    generateOre(random, world, chunkX, chunkZ, TitanBlockSet.corminium, Blocks.BEDROCK, 9, 10, 256);
                    break;
                }

            default:
                {
                    int gx = chunkX * 16 + random.nextInt(16);
                    int gz = chunkZ * 16 + random.nextInt(16);
                    if (random.nextFloat() <= 0.1F) {
                        switch (random.nextInt(5)) {
                            case 1:
                                if (random.nextFloat() <= 0.06F) {
                                    new WorldGenSurvivalDome().generate(world, random, new BlockPos(gx, world.getHeight(gx, gz), gz));
                                    break;
                                }
                                // fall through
                            case 2:
                                if (random.nextFloat() <= 0.02F) {
                                    new WorldGenMobhenge().generate(world, random, gx, world.getHeight(gx, gz), gz);
                                    break;
                                }
                                // fall through
                            default:
                                new WorldGenBoulder().generate(world, random, gx, world.getHeight(gx, gz), gz);
                        }
                    }
                    if (random.nextFloat() <= 0.001F) {
                        // original used provider.getHorizon()-24+nextInt(96); restored for correct island height.
                        int fy = (int) world.provider.getHorizon() - 24 + random.nextInt(96);
                        new WorldGenFloatingIsland().generate(world, random, gx, fy, gz);
                    }
                }
        }

        world.profiler.endSection();
    }

    private void generateOre(Random random, World world, int chunkX, int chunkZ, int dimensionID, GenOre gen) {
        if (gen != null && gen.isAllowed(dimensionID)) {
            switch(dimensionID) {
                case -1: generateOre(random, world, chunkX, chunkZ, gen.getOre(Blocks.NETHERRACK), Blocks.NETHERRACK, gen.scaleVein, gen.perChunkVeins, gen.fetchYMax(dimensionID)); break;
                case 1: generateOre(random, world, chunkX, chunkZ, gen.getOre(Blocks.END_STONE), Blocks.END_STONE, gen.scaleVein, gen.perChunkVeins, gen.fetchYMax(dimensionID)); break;
                case TheTitans.DIMENSION_KEY_VOID: generateOre(random, world, chunkX, chunkZ, gen.getOre(Blocks.BEDROCK), Blocks.BEDROCK, gen.scaleVein, gen.perChunkVeins, gen.fetchYMax(dimensionID)); break;
                case TheTitans.DIMENSION_KEY_NOWHERE: generateOre(random, world, chunkX, chunkZ, gen.getOre(Blocks.OBSIDIAN), Blocks.OBSIDIAN, gen.scaleVein, gen.perChunkVeins, gen.fetchYMax(dimensionID)); break;
                default: generateOre(random, world, chunkX, chunkZ, gen.getOre(Blocks.STONE), Blocks.STONE, gen.scaleVein, gen.perChunkVeins, gen.fetchYMax(dimensionID));
            }
        }
    }

    private void generateOre(Random random, World world, int chunkX, int chunkZ, Block state, Block generateIn, int scaleVein, int perChunkVeins, int maxY) {
        if (state == null && generateIn == null) {
            TheTitans.warn("Failed to generate ore because state and generateIn was null");
            return;
        }

        else if (state == null) {
            TheTitans.warn("Failed to generate ore in block " + generateIn.getTranslationKey().substring(5) + " because state was null");
            return;
        }

        else if (generateIn == null) {
            TheTitans.warn("Failed to generate ore " + state.getTranslationKey().substring(5) + " because generateIn was null");
            return;
        }

        for (int vein = 0; vein < perChunkVeins; vein++) {
            int randPosX = chunkX * 16 + random.nextInt(16);
            int randPosY = random.nextInt(maxY);
            int randPosZ = chunkZ * 16 + random.nextInt(16);
            WorldGenVein gen = new WorldGenVein(state, scaleVein, generateIn);
            gen.generate(world, random, new BlockPos(randPosX, randPosY, randPosZ));
        }
    }

    private void generateStructure(Random rand, World world, int chunkX, int chunkZ, int maxY, float chance, WorldGeneratorBase... gens) {
        if (gens.length == 0)
            return;
        Maths.refreshRandom(rand);
        WorldGeneratorBase gen = null;
        Vec3 vectors = new Vec3(chunkX * 16, 0, chunkZ * 16);
        int attempts = 0;
        while (gen == null && attempts < 5) {
            gen = (WorldGeneratorBase) gens[Maths.random(gens.length - 1)];
            if (Maths.chance(chance * 0.01F) && gen != null && gen instanceof WorldGeneratorBase) {
                for (Vec3 last : structurePrev.values())
                    if (vectors.distance(last) < gen.separation)
                    return;
                vectors.yVec = Math.min(world.getTopSolidOrLiquidBlock(new BlockPos((int)vectors.xVec, 0, (int)vectors.zVec)).getY(), maxY);
                gen.generate(world, rand, (int)vectors.xVec, (int)vectors.yVec, (int)vectors.zVec);
                structurePrev.put(gen, vectors);
                break;
            }

            else {
                gen = null;
                attempts++;
            }
        }

        Maths.refreshRandom();
    }
}
