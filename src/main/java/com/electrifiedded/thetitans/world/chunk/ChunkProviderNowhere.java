package com.electrifiedded.thetitans.world.chunk;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossus;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;
import com.electrifiedded.thetitans.world.gen.WorldGenNowhereDungeon;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.NoiseGenerator;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenNetherBridge;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.ChunkGeneratorEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

import org.apache.logging.log4j.LogManager;

import java.util.List;
import java.util.Random;

import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.CAVE;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.MINESHAFT;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.NETHER_BRIDGE;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.RAVINE;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.VILLAGE;

public class ChunkProviderNowhere implements IChunkGenerator {
    private static final IBlockState OBSIDIAN = Blocks.OBSIDIAN.getDefaultState();
    private Random rngVoid;
    private NoiseGeneratorOctaves gen1Noise;
    private NoiseGeneratorOctaves gen2Noise;
    private NoiseGeneratorOctaves gen3Noise;
    public NoiseGeneratorOctaves gen4Noise;
    public NoiseGeneratorOctaves noiseGen5;
    public NoiseGeneratorOctaves noiseGen6;
    private World realmVoid;
    private double[] densitiesV;
    private Biome[] biomesForGeneration;
    private final float[] fieldParabolic;
    double[] data1Noise;
    double[] data2Noise;
    double[] data3Noise;
    double[] noiseData4;
    double[] data5Noise;
    double[] infoDNoise;
    double[] infoENoise;
    double[] infoFNoise;
    double[] infoGNoise;
    int[][] indexFieldRiver = new int[32][32];
    private WorldType kindFieldRealm;
    private MapGenBase caveGenerator = new MapGenCavesNowhere();
    private MapGenVillage villageGenerator = new MapGenVillage();
    private MapGenBase ravineGenerator = new MapGenRavineNowhere();
    public MapGenNetherBridge genNetherBridge = new MapGenNetherBridge();
    private MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();
    {
        genNetherBridge = (MapGenNetherBridge) TerrainGen.getModdedMapGen(genNetherBridge, NETHER_BRIDGE);
        caveGenerator = TerrainGen.getModdedMapGen(caveGenerator, CAVE);
        villageGenerator = (MapGenVillage) TerrainGen.getModdedMapGen(villageGenerator, VILLAGE);
        mineshaftGenerator = (MapGenMineshaft) TerrainGen.getModdedMapGen(mineshaftGenerator, MINESHAFT);
        ravineGenerator = TerrainGen.getModdedMapGen(ravineGenerator, RAVINE);
    }

    public ChunkProviderNowhere(World p_i2007_1_, long p_i2007_2_) {
        this.realmVoid = p_i2007_1_;
        this.kindFieldRealm = p_i2007_1_.getWorldType();
        this.rngVoid = new Random(p_i2007_2_);
        this.densitiesV = new double[825];
        this.gen1Noise = new NoiseGeneratorOctaves(this.rngVoid, 16);
        this.gen2Noise = new NoiseGeneratorOctaves(this.rngVoid, 16);
        this.gen3Noise = new NoiseGeneratorOctaves(this.rngVoid, 8);
        this.gen4Noise = new NoiseGeneratorOctaves(this.rngVoid, 4);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.rngVoid, 10);
        this.noiseGen6 = new NoiseGeneratorOctaves(this.rngVoid, 16);
        this.densitiesV = new double[825];
        // 1.12.2 getModdedNoiseGenerators takes a 5-slot Context, not a NoiseGenerator[].
        // gen4Noise (surface octaves) has no Context slot, so it passes through untouched.
        net.minecraftforge.event.terraingen.InitNoiseGensEvent.Context ctx =
                new net.minecraftforge.event.terraingen.InitNoiseGensEvent.Context(
                this.gen1Noise, this.gen2Noise, this.gen3Noise, this.noiseGen5, this.noiseGen6);
        ctx = TerrainGen.getModdedNoiseGenerators(p_i2007_1_, this.rngVoid, ctx);
        this.gen1Noise = ctx.getLPerlin1();
        this.gen2Noise = ctx.getLPerlin2();
        this.gen3Noise = ctx.getPerlin();
        this.noiseGen5 = ctx.getScale();
        this.noiseGen6 = ctx.getDepth();
        this.fieldParabolic = new float[25];
        for (int j = -2; j <= 2; ++j) {
            for (int k = -2; k <= 2; ++k) {
                float f = 10.0F / MathHelper.sqrt((float)(j * j + k * k) + 0.2F);
                this.fieldParabolic[j + 2 + (k + 2) * 5] = f;
            }
        }
    }

    private void prepareHeights(int arg147423n1, int arg147423n2, int arg147423n3) {
        this.infoGNoise = this.noiseGen6.generateNoiseOctaves(this.infoGNoise, arg147423n1, arg147423n3, 5, 5, 200.0D, 200.0D, 0.5D);
        this.infoDNoise = this.gen3Noise.generateNoiseOctaves(this.infoDNoise, arg147423n1, arg147423n2, arg147423n3, 5, 33, 5, 8.555150000000001D, 4.277575000000001D, 8.555150000000001D);
        this.infoENoise = this.gen1Noise.generateNoiseOctaves(this.infoENoise, arg147423n1, arg147423n2, arg147423n3, 5, 33, 5, 684.412D, 684.412D, 684.412D);
        this.infoFNoise = this.gen2Noise.generateNoiseOctaves(this.infoFNoise, arg147423n1, arg147423n2, arg147423n3, 5, 33, 5, 684.412D, 684.412D, 684.412D);
        int l = 0;
        int i1 = 0;
        for (int j1 = 0; j1 < 5; ++j1) {
            for (int k1 = 0; k1 < 5; ++k1) {
                float f = 0.0F;
                float f1 = 0.0F;
                float f2 = 0.0F;
                byte b0 = 2;
                Biome biomegenbase = this.biomesForGeneration[j1 + 2 + (k1 + 2) * 10];
                for (int l1 = -b0; l1 <= b0; ++l1) {
                    for (int i2 = -b0; i2 <= b0; ++i2) {
                        Biome biomegenbase1 = this.biomesForGeneration[j1 + l1 + 2 + (k1 + i2 + 2) * 10];
                        float f3 = biomegenbase1.getBaseHeight();
                        float f4 = biomegenbase1.getHeightVariation();
                        if (this.kindFieldRealm == WorldType.AMPLIFIED && f3 > 0.0F) {
                            f3 = 1.0F + f3 * 2.0F;
                            f4 = 1.0F + f4 * 4.0F;
                        }

                        float f5 = this.fieldParabolic[l1 + 2 + (i2 + 2) * 5] / (f3 + 2.0F);
                        if (biomegenbase1.getBaseHeight() > biomegenbase.getBaseHeight()) {
                            f5 /= 2.0F;
                        }

                        f += f4 * f5;
                        f1 += f3 * f5;
                        f2 += f5;
                    }
                }

                f /= f2;
                f1 /= f2;
                f = f * 0.9F + 0.1F;
                f1 = (f1 * 4.0F - 1.0F) / 8.0F;
                double d12 = this.infoGNoise[i1] / 8000.0D;
                if (d12 < 0.0D) {
                    d12 = -d12 * 0.3D;
                }

                d12 = d12 * 3.0D - 2.0D;
                if (d12 < 0.0D) {
                    d12 /= 2.0D;
                    if (d12 < -1.0D) {
                        d12 = -1.0D;
                    }

                    d12 /= 1.4D;
                    d12 /= 2.0D;
                }

                else {
                    if (d12 > 1.0D) {
                        d12 = 1.0D;
                    }

                    d12 /= 8.0D;
                }

                ++i1;
                double d13 = (double)f1;
                double d14 = (double)f;
                d13 += d12 * 0.2D;
                d13 = d13 * 8.5D / 8.0D;
                double d5 = 8.5D + d13 * 4.0D;
                for (int j2 = 0; j2 < 33; ++j2) {
                    double d6 = ((double)j2 - d5) * 12.0D * 128.0D / 256.0D / d14;
                    if (d6 < 0.0D) {
                        d6 *= 4.0D;
                    }

                    double d7 = this.infoENoise[l] / 512.0D;
                    double d8 = this.infoFNoise[l] / 512.0D;
                    double d9 = (this.infoDNoise[l] / 10.0D + 1.0D) / 2.0D;
                    double d10 = MathHelper.clampedLerp(d7, d8, d9) - d6;
                    if (j2 > 29) {
                        double d11 = (double)((float)(j2 - 29) / 3.0F);
                        d10 = d10 * (1.0D - d11) + -10.0D * d11;
                    }

                    this.densitiesV[l] = d10;
                    ++l;
                }
            }
        }
    }

    // Fills the ChunkPrimer from the density field. (1.7.10 wrote Blocks into a Block[]; now IBlockState into ChunkPrimer.)
    public void setBlocksInChunk(int arg147424n1, int arg147424n2, ChunkPrimer primer) {
        this.prepareHeights(arg147424n1 * 4, 0, arg147424n2 * 4);
        for (int k = 0; k < 4; ++k) {
            int l = k * 5;
            int i1 = (k + 1) * 5;
            for (int j1 = 0; j1 < 4; ++j1) {
                int k1 = (l + j1) * 33;
                int l1 = (l + j1 + 1) * 33;
                int i2 = (i1 + j1) * 33;
                int j2 = (i1 + j1 + 1) * 33;
                for (int k2 = 0; k2 < 32; ++k2) {
                    double d0 = 0.125D;
                    double d1 = this.densitiesV[k1 + k2];
                    double d2 = this.densitiesV[l1 + k2];
                    double d3 = this.densitiesV[i2 + k2];
                    double d4 = this.densitiesV[j2 + k2];
                    double d5 = (this.densitiesV[k1 + k2 + 1] - d1) * d0;
                    double d6 = (this.densitiesV[l1 + k2 + 1] - d2) * d0;
                    double d7 = (this.densitiesV[i2 + k2 + 1] - d3) * d0;
                    double d8 = (this.densitiesV[j2 + k2 + 1] - d4) * d0;
                    for (int l2 = 0; l2 < 8; ++l2) {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;
                        for (int i3 = 0; i3 < 4; ++i3) {
                            // local coords: x = i3 + k*4, z = j1*4, y = k2*8 + l2
                            int localX = i3 + k * 4;
                            int localZ = j1 * 4;
                            int localY = k2 * 8 + l2;
                            double d14 = 0.25D;
                            double d16 = (d11 - d10) * d14;
                            double d15 = d10 - d16;
                            for (int k3 = 0; k3 < 4; ++k3) {
                                if ((d15 += d16) > 0.0D) {
                                    primer.setBlockState(localX, localY, localZ + k3, OBSIDIAN);
                                }

                                else {
                                    primer.setBlockState(localX, localY, localZ + k3, Blocks.AIR.getDefaultState());
                                }
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
    }

    public void replaceBiomeBlocks(int arg147421n1, int arg147421n2, ChunkPrimer primer, Biome[] arg147421n4) {
        ChunkGeneratorEvent.ReplaceBiomeBlocks event = new ChunkGeneratorEvent.ReplaceBiomeBlocks(this, arg147421n1, arg147421n2, primer, this.realmVoid);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Result.DENY) return;
    }

    public Chunk generateChunk(int arg73154n1, int arg73154n2) {
        this.rngVoid.setSeed((long)arg73154n1 * 341873128712L + (long)arg73154n2 * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        this.biomesForGeneration = this.realmVoid.getBiomeProvider().getBiomesForGeneration(this.biomesForGeneration, arg73154n1 * 4 - 2, arg73154n2 * 4 - 2, 10, 10);
        this.setBlocksInChunk(arg73154n1, arg73154n2, chunkprimer);
        this.biomesForGeneration = this.realmVoid.getBiomeProvider().getBiomes(this.biomesForGeneration, arg73154n1 * 16, arg73154n2 * 16, 16, 16);
        this.replaceBiomeBlocks(arg73154n1, arg73154n2, chunkprimer, this.biomesForGeneration);
        this.caveGenerator.generate(this.realmVoid, arg73154n1, arg73154n2, chunkprimer);
        this.ravineGenerator.generate(this.realmVoid, arg73154n1, arg73154n2, chunkprimer);
        this.genNetherBridge.generate(this.realmVoid, arg73154n1, arg73154n2, chunkprimer);
        this.villageGenerator.generate(this.realmVoid, arg73154n1, arg73154n2, chunkprimer);
        this.mineshaftGenerator.generate(this.realmVoid, arg73154n1, arg73154n2, chunkprimer);
        Chunk chunk = new Chunk(this.realmVoid, chunkprimer, arg73154n1, arg73154n2);
        byte[] abyte1 = chunk.getBiomeArray();
        for (int k = 0; k < abyte1.length; k++) {
            abyte1[k] = ((byte)Biome.getIdForBiome(this.biomesForGeneration[k]));
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    public void populate(int arg73153n2, int arg73153n3) {
        net.minecraft.block.BlockFalling.fallInstantly = true;
        int k = arg73153n2 * 16;
        int l = arg73153n3 * 16;
        BlockPos blockpos = new BlockPos(k, 0, l);
        Biome biomegenbase = this.realmVoid.getBiome(blockpos.add(16, 0, 16));
        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(this, this.realmVoid, this.rngVoid, arg73153n2, arg73153n3, false));
        this.rngVoid.setSeed(this.realmVoid.getSeed());
        long i1 = this.rngVoid.nextLong() / 2L * 2L + 1L;
        long j1 = this.rngVoid.nextLong() / 2L * 2L + 1L;
        this.rngVoid.setSeed((long)arg73153n2 * i1 + (long)arg73153n3 * j1 ^ this.realmVoid.getSeed());
        this.villageGenerator.generateStructure(this.realmVoid, this.rngVoid, new ChunkPos(arg73153n2, arg73153n3));
        this.genNetherBridge.generateStructure(this.realmVoid, this.rngVoid, new ChunkPos(arg73153n2, arg73153n3));
        this.mineshaftGenerator.generateStructure(this.realmVoid, this.rngVoid, new ChunkPos(arg73153n2, arg73153n3));
        biomegenbase.decorate(this.realmVoid, this.rngVoid, new BlockPos(k, 0, l));
        int k1;
        int l1;
        int i2;
        for (k1 = 0; k1 < 100; ++k1) {
            l1 = k + this.rngVoid.nextInt(16) + 8;
            i2 = this.rngVoid.nextInt(256);
            int j2 = l + this.rngVoid.nextInt(16) + 8;
            (new WorldGenNowhereDungeon()).generate(this.realmVoid, this.rngVoid, new BlockPos(l1, i2, j2));
        }

        for (k1 = 0; k1 < 800; ++k1) {
            l1 = k + this.rngVoid.nextInt(16) + 8;
            i2 = this.rngVoid.nextInt(256);
            int j2 = l + this.rngVoid.nextInt(16) + 8;
            (new WorldGenDungeons()).generate(this.realmVoid, this.rngVoid, new BlockPos(l1, i2, j2));
        }

        if (this.rngVoid.nextInt(2000) == 0) {
            l1 = k + this.rngVoid.nextInt(16) + 8;
            int j2 = l + this.rngVoid.nextInt(16) + 8;
            i2 = this.realmVoid.getTopSolidOrLiquidBlock(new BlockPos(l1, 0, j2)).getY();
            EntityZombieTitan witherzilla = new EntityZombieTitan(this.realmVoid);
            witherzilla.onInitialSpawn(this.realmVoid.getDifficultyForLocation(new BlockPos(witherzilla)), (net.minecraft.entity.IEntityLivingData)null);
            witherzilla.setLocationAndAngles(l1, i2, j2, 0.0F, 0.0F);
            LogManager.getLogger(TheTitans.class).info("Found a succesfully spawned Void Zombie Titan at " + l1 + ", " + i2 + ", " + j2 + ", spawning.");
            this.realmVoid.spawnEntity(witherzilla);
        }

        if (this.rngVoid.nextInt(50000) == 0) {
            l1 = k + this.rngVoid.nextInt(16) + 8;
            int j2 = l + this.rngVoid.nextInt(16) + 8;
            i2 = this.realmVoid.getTopSolidOrLiquidBlock(new BlockPos(l1, 0, j2)).getY();
            EntityEnderColossus witherzilla = new EntityEnderColossus(this.realmVoid);
            witherzilla.setLocationAndAngles(l1, i2, j2, 0.0F, 0.0F);
            LogManager.getLogger(TheTitans.class).info("Found a succesfully spawned Ender Colossus at " + l1 + ", " + i2 + ", " + j2 + ", spawning.");
            this.realmVoid.spawnEntity(witherzilla);
        }

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(this, this.realmVoid, this.rngVoid, arg73153n2, arg73153n3, false));
        net.minecraft.block.BlockFalling.fallInstantly = false;
    }

    public boolean generateStructures(Chunk chunkIn, int x, int z) {
        return false;
    }

    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        if (creatureType == EnumCreatureType.MONSTER) {
            if (this.genNetherBridge.isInsideStructure(pos)) {
                return this.genNetherBridge.getSpawnList();
            }

            if (this.genNetherBridge.isPositionInStructure(this.realmVoid, pos) && this.realmVoid.getBlockState(pos.down()).getBlock() == Blocks.NETHER_BRICK) {
                return this.genNetherBridge.getSpawnList();
            }
        }

        Biome biomegenbase = this.realmVoid.getBiome(pos);
        return biomegenbase.getSpawnableList(creatureType);
    }

    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
        if ("Village".equals(structureName) && this.villageGenerator != null) {
            return this.villageGenerator.getNearestStructurePos(worldIn, position, findUnexplored);
        }
        if ("Mineshaft".equals(structureName) && this.mineshaftGenerator != null) {
            return this.mineshaftGenerator.getNearestStructurePos(worldIn, position, findUnexplored);
        }
        if ("Fortress".equals(structureName) && this.genNetherBridge != null) {
            return this.genNetherBridge.getNearestStructurePos(worldIn, position, findUnexplored);
        }
        return null;
    }

    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
        if ("Village".equals(structureName) && this.villageGenerator != null) {
            return this.villageGenerator.isInsideStructure(pos);
        }
        if ("Mineshaft".equals(structureName) && this.mineshaftGenerator != null) {
            return this.mineshaftGenerator.isInsideStructure(pos);
        }
        if ("Fortress".equals(structureName) && this.genNetherBridge != null) {
            return this.genNetherBridge.isInsideStructure(pos);
        }
        return false;
    }

    public void recreateStructures(Chunk chunkIn, int arg82695n1, int arg82695n2) {
        this.villageGenerator.generate(this.realmVoid, arg82695n1, arg82695n2, (ChunkPrimer)null);
        this.genNetherBridge.generate(this.realmVoid, arg82695n1, arg82695n2, (ChunkPrimer)null);
        this.mineshaftGenerator.generate(this.realmVoid, arg82695n1, arg82695n2, (ChunkPrimer)null);
    }
}
