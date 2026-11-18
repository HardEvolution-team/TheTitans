package com.electrifiedded.thetitans.world.biome;

import com.electrifiedded.thetitans.entity.titan.EntityWitherzilla;
import com.electrifiedded.thetitans.world.gen.WorldGenGroundTurretTowers;
import com.electrifiedded.thetitans.world.gen.WorldGenMortarTurretTowers;
import com.electrifiedded.thetitans.world.gen.WorldGenTurretTowers;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class BiomeVoidDecorator extends BiomeDecorator {
    protected WorldGenerator genTurret;
    protected WorldGenerator gen2Turret;
    protected WorldGenerator gen3Turret;

    public BiomeVoidDecorator() {
        this.genTurret = new WorldGenTurretTowers(Blocks.BEDROCK);
        this.gen2Turret = new WorldGenGroundTurretTowers(Blocks.BEDROCK);
        this.gen3Turret = new WorldGenMortarTurretTowers(Blocks.BEDROCK);
    }

    protected void genDecorations(Biome arg150513n1, World worldIn, Random random) {
        generateOres(worldIn, random);
        int chunkX = this.chunkPos.getX();
        int chunkZ = this.chunkPos.getZ();
        if (random.nextInt(2) == 0) {
            int i = chunkX + random.nextInt(16) + 8;
            int j = chunkZ + random.nextInt(16) + 8;
            int k = worldIn.getTopSolidOrLiquidBlock(new BlockPos(i, 0, j)).getY();
            this.genTurret.generate(worldIn, random, new BlockPos(i, k, j));
        }

        if (random.nextInt(2) == 0) {
            int i = chunkX + random.nextInt(16) + 8;
            int j = chunkZ + random.nextInt(16) + 8;
            int k = worldIn.getTopSolidOrLiquidBlock(new BlockPos(i, 0, j)).getY();
            this.gen2Turret.generate(worldIn, random, new BlockPos(i, k, j));
        }

        if (random.nextInt(5) == 0) {
            int i = chunkX + random.nextInt(16) + 8;
            int j = chunkZ + random.nextInt(16) + 8;
            int k = worldIn.getTopSolidOrLiquidBlock(new BlockPos(i, 0, j)).getY();
            this.gen3Turret.generate(worldIn, random, new BlockPos(i, k, j));
        }

        if ((chunkX == 0) && (chunkZ == 0)) {
            EntityWitherzilla witherzilla = new EntityWitherzilla(worldIn);
            witherzilla.setLocationAndAngles(0.0D, 200.0D, 0.0D, random.nextFloat() * 360.0F, 0.0F);
            worldIn.spawnEntity(witherzilla);
        }
    }
}
