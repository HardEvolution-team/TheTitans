package com.electrifiedded.thetitans.world.dimension;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.world.chunk.ChunkProviderNowhere;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderNowhere extends WorldProvider {
    public void init() {
        this.biomeProvider = new BiomeProviderSingle(TheTitans.nowhereV);
        this.hasSkyLight = false;
    }

    public DimensionType getDimensionType() {
        return TheTitans.DIM_KIND_NOWHERE != null ? TheTitans.DIM_KIND_NOWHERE : DimensionType.THE_END;
    }

    public IChunkGenerator createChunkGenerator() {
        return new ChunkProviderNowhere(this.world, this.world.getSeed());
    }

    public float calculateCelestialAngle(long arg76563n1, float arg76563n3) {
        return 0.0F;
    }

    @SideOnly(Side.CLIENT)
    public float[] calcSunriseSunsetColors(float arg76560n1, float arg76560n2) {
        return null;
    }

    public boolean canBlockFreeze(BlockPos pos, boolean byWater) {
        return false;
    }

    public boolean canSnowAt(BlockPos pos, boolean checkLight) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(float p_76562_1, float p_76562_2) {
        return new Vec3d(0.196078431372549D, 0.0196078431372549D, 0.196078431372549D);
    }

    @SideOnly(Side.CLIENT)
    public boolean isSkyColored() {
        return false;
    }

    public boolean canRespawnHere() {
        return false;
    }

    // original func_76554_h() → fixed spawn ChunkCoordinates(100, 50, 0).
    public BlockPos getSpawnPoint() {
        return new BlockPos(100, 50, 0);
    }

    public BlockPos getRandomizedSpawnPoint() {
        return new BlockPos(100, 50, 0);
    }

    public boolean isSurfaceWorld() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public float getCloudHeight() {
        return 8.0F;
    }

    public boolean canCoordinateBeSpawn(int x, int z) {
        return this.world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)) != null;
    }

    public int getAverageGroundLevel() {
        return 50;
    }

    @SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(int x, int z) {
        return true;
    }

    public String fetchMessageWelcome() {
        return "Entering your DEATH!";
    }

    public String fetchMessageDepart() {
        return "Leaving the Nowhere.";
    }
}
