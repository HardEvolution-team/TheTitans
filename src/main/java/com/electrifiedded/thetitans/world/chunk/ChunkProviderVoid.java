package com.electrifiedded.thetitans.world.chunk;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.NoiseGenerator;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.ChunkGeneratorEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

import java.util.List;
import java.util.Random;

public class ChunkProviderVoid implements IChunkGenerator {
    private static final IBlockState BEDROCK = Blocks.BEDROCK.getDefaultState();
    private static final IBlockState AIR = Blocks.AIR.getDefaultState();
    private Random rngVoid;
    private NoiseGeneratorOctaves gen1Noise;
    private NoiseGeneratorOctaves gen2Noise;
    private NoiseGeneratorOctaves gen3Noise;
    public NoiseGeneratorOctaves gen4Noise;
    public NoiseGeneratorOctaves noiseGen5;
    private World realmVoid;
    private double[] densitiesV;
    private Biome[] biomesForGeneration;
    double[] data1Noise;
    double[] data2Noise;
    double[] data3Noise;
    double[] noiseData4;
    double[] data5Noise;
    int[][] indexFieldRiver = new int[32][32];

    public ChunkProviderVoid(World p_i2007_1_, long p_i2007_2_) {
        this.realmVoid = p_i2007_1_;
        this.rngVoid = new Random(p_i2007_2_);
        this.gen1Noise = new NoiseGeneratorOctaves(this.rngVoid, 16);
        this.gen2Noise = new NoiseGeneratorOctaves(this.rngVoid, 16);
        this.gen3Noise = new NoiseGeneratorOctaves(this.rngVoid, 8);
        this.gen4Noise = new NoiseGeneratorOctaves(this.rngVoid, 10);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.rngVoid, 16);
        // 1.12.2 getModdedNoiseGenerators takes a 5-slot Context, not a NoiseGenerator[].
        net.minecraftforge.event.terraingen.InitNoiseGensEvent.Context ctx =
                new net.minecraftforge.event.terraingen.InitNoiseGensEvent.Context(
                this.gen1Noise, this.gen2Noise, this.gen3Noise, this.gen4Noise, this.noiseGen5);
        ctx = TerrainGen.getModdedNoiseGenerators(p_i2007_1_, this.rngVoid, ctx);
        this.gen1Noise = ctx.getLPerlin1();
        this.gen2Noise = ctx.getLPerlin2();
        this.gen3Noise = ctx.getPerlin();
        this.gen4Noise = ctx.getScale();
        this.noiseGen5 = ctx.getDepth();
    }

    // Fills the ChunkPrimer from the density field. (1.7.10 wrote Blocks into a flat Block[32768]
    // laid out as index = X*2048 + Z*128 + Y; the original short1=128 increment walked Z, not Y.)
    public void setBlocksInChunk(int arg147420n1, int arg147420n2, ChunkPrimer primer) {
        byte b0 = 2;
        int k = b0 + 1;
        byte b1 = 33;
        int l = b0 + 1;
        this.densitiesV = initializeFieldNoise(this.densitiesV, arg147420n1 * b0, 0, arg147420n2 * b0, k, b1, l);
        for (int i1 = 0; i1 < b0; i1++) {
            for (int j1 = 0; j1 < b0; j1++) {
                for (int k1 = 0; k1 < 32; k1++) {
                    double d0 = 0.25D;
                    double d1 = this.densitiesV[(((i1 + 0) * l + j1 + 0) * b1 + k1 + 0)];
                    double d2 = this.densitiesV[(((i1 + 0) * l + j1 + 1) * b1 + k1 + 0)];
                    double d3 = this.densitiesV[(((i1 + 1) * l + j1 + 0) * b1 + k1 + 0)];
                    double d4 = this.densitiesV[(((i1 + 1) * l + j1 + 1) * b1 + k1 + 0)];
                    double d5 = (this.densitiesV[(((i1 + 0) * l + j1 + 0) * b1 + k1 + 1)] - d1) * d0;
                    double d6 = (this.densitiesV[(((i1 + 0) * l + j1 + 1) * b1 + k1 + 1)] - d2) * d0;
                    double d7 = (this.densitiesV[(((i1 + 1) * l + j1 + 0) * b1 + k1 + 1)] - d3) * d0;
                    double d8 = (this.densitiesV[(((i1 + 1) * l + j1 + 1) * b1 + k1 + 1)] - d4) * d0;
                    for (int l1 = 0; l1 < 4; l1++) {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;
                        for (int i2 = 0; i2 < 8; i2++) {
                            // decode of: j2 = (i2 + i1*8)<<11 | (j1*8)<<7 | (k1*4 + l1)
                            // X = i2 + i1*8 (0..15), Z base = j1*8, Y = k1*4 + l1 (0..127);
                            // the old j2 += 128 walked Z by one, so Z = j1*8 + k2.
                            int localX = i2 + i1 * 8;
                            int localZ = j1 * 8;
                            int localY = k1 * 4 + l1;
                            double d14 = 0.25D;
                            double d15 = d10;
                            double d16 = (d11 - d10) * d14;
                            for (int k2 = 0; k2 < 8; k2++) {
                                if (d15 > 0.0D) {
                                    primer.setBlockState(localX, localY, localZ + k2, BEDROCK);
                                }

                                else {
                                    primer.setBlockState(localX, localY, localZ + k2, AIR);
                                }

                                d15 += d16;
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
        this.biomesForGeneration = this.realmVoid.getBiomeProvider().getBiomes(this.biomesForGeneration, arg73154n1 * 16, arg73154n2 * 16, 16, 16);
        this.setBlocksInChunk(arg73154n1, arg73154n2, chunkprimer);
        this.replaceBiomeBlocks(arg73154n1, arg73154n2, chunkprimer, this.biomesForGeneration);
        Chunk chunk = new Chunk(this.realmVoid, chunkprimer, arg73154n1, arg73154n2);
        byte[] abyte = chunk.getBiomeArray();
        for (int k = 0; k < abyte.length; k++) {
            abyte[k] = ((byte)Biome.getIdForBiome(this.biomesForGeneration[k]));
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    private double[] initializeFieldNoise(double[] arg73187n1, int arg73187n2, int arg73187n3, int arg73187n4, int arg73187n5, int arg73187n6, int arg73187n7) {
        ChunkGeneratorEvent.InitNoiseField event = new ChunkGeneratorEvent.InitNoiseField(this, arg73187n1, arg73187n2, arg73187n3, arg73187n4, arg73187n5, arg73187n6, arg73187n7);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Result.DENY) {
            return event.getNoisefield();
        }

        if (arg73187n1 == null) {
            arg73187n1 = new double[arg73187n5 * arg73187n6 * arg73187n7];
        }

        double d0 = 684.412D;
        double d1 = 684.412D;
        this.noiseData4 = this.gen4Noise.generateNoiseOctaves(this.noiseData4, arg73187n2, arg73187n4, arg73187n5, arg73187n7, 1.121D, 1.121D, 0.5D);
        this.data5Noise = this.noiseGen5.generateNoiseOctaves(this.data5Noise, arg73187n2, arg73187n4, arg73187n5, arg73187n7, 200.0D, 200.0D, 0.5D);
        d0 *= 2.0D;
        this.data1Noise = this.gen3Noise.generateNoiseOctaves(this.data1Noise, arg73187n2, arg73187n3, arg73187n4, arg73187n5, arg73187n6, arg73187n7, d0 / 80.0D, d1 / 160.0D, d0 / 80.0D);
        this.data2Noise = this.gen1Noise.generateNoiseOctaves(this.data2Noise, arg73187n2, arg73187n3, arg73187n4, arg73187n5, arg73187n6, arg73187n7, d0, d1, d0);
        this.data3Noise = this.gen2Noise.generateNoiseOctaves(this.data3Noise, arg73187n2, arg73187n3, arg73187n4, arg73187n5, arg73187n6, arg73187n7, d0, d1, d0);
        int k1 = 0;
        int l1 = 0;
        for (int i2 = 0; i2 < arg73187n5; i2++) {
            for (int j2 = 0; j2 < arg73187n7; j2++) {
                double d2 = (this.noiseData4[l1] + 256.0D) / 512.0D;
                if (d2 > 1.0D) {
                    d2 = 1.0D;
                }

                double d3 = this.data5Noise[l1] / 8000.0D;
                if (d3 < 0.0D) {
                    d3 = -d3 * 0.3D;
                }

                d3 = d3 * 3.0D - 2.0D;
                float f = (i2 + arg73187n2 - 0) / 1.0F;
                float f1 = (j2 + arg73187n4 - 0) / 1.0F;
                float f2 = 100.0F - MathHelper.sqrt(f * f + f1 * f1) * 8.0F;
                if (f2 > 80.0F) {
                    f2 = 80.0F;
                }

                if (f2 < -100.0F) {
                    f2 = -100.0F;
                }

                if (d3 > 1.0D) {
                    d3 = 1.0D;
                }

                d3 /= 8.0D;
                d3 = 0.0D;
                if (d2 < 0.0D) {
                    d2 = 0.0D;
                }

                d2 += 0.5D;
                d3 = d3 * arg73187n6 / 16.0D;
                l1++;
                double d4 = arg73187n6 / 2.0D;
                for (int k2 = 0; k2 < arg73187n6; k2++) {
                    double d5 = 0.0D;
                    double d6 = (k2 - d4) * 8.0D / d2;
                    if (d6 < 0.0D) {
                        d6 *= -1.0D;
                    }

                    double d7 = this.data2Noise[k1] / 512.0D;
                    double d8 = this.data3Noise[k1] / 512.0D;
                    double d9 = (this.data1Noise[k1] / 10.0D + 1.0D) / 2.0D;
                    if (d9 < 0.0D) {
                        d5 = d7;
                    }

                    else if (d9 > 1.0D) {
                        d5 = d8;
                    }

                    else {
                        d5 = d7 + (d8 - d7) * d9;
                    }

                    d5 -= 8.0D;
                    d5 += f2;
                    byte b0 = 2;
                    if (k2 > arg73187n6 / 2 - b0) {
                        double d10 = (k2 - (arg73187n6 / 2 - b0)) / 64.0F;
                        if (d10 < 0.0D) {
                            d10 = 0.0D;
                        }

                        if (d10 > 1.0D) {
                            d10 = 1.0D;
                        }

                        d5 = d5 * (1.0D - d10) + -3000.0D * d10;
                    }

                    b0 = 8;
                    if (k2 < b0) {
                        double d10 = (b0 - k2) / (b0 - 1.0F);
                        d5 = d5 * (1.0D - d10) + -30.0D * d10;
                    }

                    arg73187n1[k1] = d5;
                    k1++;
                }
            }
        }

        return arg73187n1;
    }

    public void populate(int arg73153n2, int arg73153n3) {
        net.minecraft.block.BlockFalling.fallInstantly = true;
        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(this, this.realmVoid, this.realmVoid.rand, arg73153n2, arg73153n3, false));
        int k = arg73153n2 * 16;
        int l = arg73153n3 * 16;
        Biome biomegenbase = this.realmVoid.getBiome(new BlockPos(k + 16, 0, l + 16));
        biomegenbase.decorate(this.realmVoid, this.realmVoid.rand, new BlockPos(k, 0, l));
        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(this, this.realmVoid, this.realmVoid.rand, arg73153n2, arg73153n3, false));
        net.minecraft.block.BlockFalling.fallInstantly = false;
    }

    public boolean generateStructures(Chunk chunkIn, int x, int z) {
        return false;
    }

    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        Biome biomegenbase = this.realmVoid.getBiome(pos);
        return biomegenbase.getSpawnableList(creatureType);
    }

    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
        return null;
    }

    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
        return false;
    }

    public void recreateStructures(Chunk chunkIn, int x, int z) {
    }
}
