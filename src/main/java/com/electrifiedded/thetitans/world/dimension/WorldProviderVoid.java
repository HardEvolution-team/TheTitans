package com.electrifiedded.thetitans.world.dimension;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.titan.EntityWitherzilla;
import com.electrifiedded.thetitans.world.chunk.ChunkProviderVoid;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.Lists;

import java.util.ArrayList;

public class WorldProviderVoid extends WorldProvider {
    // TODO custom DimensionType must be registered for the Void dimension; using THE_END placeholder.
    public void init() {
        this.biomeProvider = new BiomeProviderSingle(TheTitans.voidlandV);
        this.hasSkyLight = false;
    }

    public DimensionType getDimensionType() {
        return TheTitans.DIM_KIND_VOID != null ? TheTitans.DIM_KIND_VOID : DimensionType.THE_END;
    }

    public IChunkGenerator createChunkGenerator() {
        return new ChunkProviderVoid(this.world, this.world.getSeed());
    }

    public float calculateCelestialAngle(long arg76563n1, float arg76563n3) {
        return 0.5F;
    }

    @SideOnly(Side.CLIENT)
    public float[] calcSunriseSunsetColors(float arg76560n1, float arg76560n2) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(float p_76562_1, float p_76562_2) {
        double black = 0.0D;
        java.util.List<Entity> entities = this.world.loadedEntityList;
        for (int i1 = 0; i1 < entities.size(); i1++) {
            Entity entity = entities.get(i1);
            if (entity instanceof EntityWitherzilla) {
                black = 0.2D + (double)net.minecraft.util.math.MathHelper.cos((float)entity.ticksExisted * 0.1F) * 0.2D;
                break;
            }
        }

        return new Vec3d(black, black, black);
    }

    protected void generateLightBrightnessTable() {
        float f = 1F;
        for (int i = 0; i <= 15; ++i) {
            float f1 = 1.0F - (float)i / 15.0F;
            this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean isSkyColored() {
        return true;
    }

    public boolean canRespawnHere() {
        return false;
    }

    // original set field_76575_d (isHellWorld) = true → water vaporizes in the Void.
    public boolean doesWaterVaporize() {
        return true;
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

    public boolean canBlockFreeze(BlockPos pos, boolean byWater) {
        return false;
    }

    public boolean canSnowAt(BlockPos pos, boolean checkLight) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public float getCloudHeight() {
        return 128.0F;
    }

    public boolean canCoordinateBeSpawn(int x, int z) {
        return this.world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)) != null;
    }

    public int getAverageGroundLevel() {
        return 50;
    }

    @SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(int x, int z) {
        return false;
    }

    public String fetchMessageWelcome() {
        return "Entering your DOOM!";
    }

    public String fetchMessageDepart() {
        return "Leaving The VOID.";
    }
}
