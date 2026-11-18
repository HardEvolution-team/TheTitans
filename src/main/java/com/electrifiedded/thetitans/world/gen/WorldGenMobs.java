package com.electrifiedded.thetitans.world.gen;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.config.TitanConfig;
import com.electrifiedded.thetitans.entity.titan.EntityBlazeTitan;
import com.electrifiedded.thetitans.entity.titan.EntityCaveSpiderTitan;
import com.electrifiedded.thetitans.entity.titan.EntityCreeperTitan;
import com.electrifiedded.thetitans.entity.titan.EntityGhastTitan;
import com.electrifiedded.thetitans.entity.titan.EntityMagmaCubeTitan;
import com.electrifiedded.thetitans.entity.titan.EntityPigZombieTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySilverfishTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySlimeTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySpiderTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;
import com.electrifiedded.thetitans.util.Maths.Vec3;
import com.electrifiedded.thetitans.util.Maths;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class WorldGenMobs implements IWorldGenerator {
    private final Map<EntityLivingBase, Vec3> birthPrev = new HashMap<EntityLivingBase, Vec3>();

    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        world.profiler.startSection("genTitanMobs");
        int dimensionID = world.provider.getDimension();
        switch (dimensionID) {
            case -1: emitGiant(random, world, chunkX, chunkZ, 33.0F, () -> new EntityMagmaCubeTitan(world), () -> new EntityGhastTitan(world), () -> new EntitySkeletonTitan(world).setSkeletonType(1), () -> new EntityBlazeTitan(world), () -> new EntityPigZombieTitan(world)); break;
            case TheTitans.DIMENSION_KEY_NOWHERE: break;
            case TheTitans.DIMENSION_KEY_VOID: break;
            default: emitGiant(random, world, chunkX, chunkZ, 33.0F, () -> new EntitySilverfishTitan(world), () -> new EntityCaveSpiderTitan(world), () -> new EntitySpiderTitan(world), () -> new EntityZombieTitan(world), () -> new EntitySkeletonTitan(world), () -> new EntityCreeperTitan(world), () -> new EntitySlimeTitan(world));
        }

        world.profiler.endSection();
    }

    public void removeEntity(EntityLivingBase entity) {
        if(entity != null && !birthPrev.isEmpty() && birthPrev.containsKey(entity)) {
            birthPrev.remove(entity);
            TheTitans.debug("Removed " + entity.getName() + " from the list");
        }
    }

    @SafeVarargs
    private final void emitGiant(Random rand, World world, int chunkX, int chunkZ, float chance, Supplier<? extends EntityLivingBase>... factories) {
        if (factories.length == 0)
            return;
        Maths.refreshRandom(rand);
        EntityLivingBase entity = null;
        Vec3 vectors = new Vec3(chunkX * 16, 0, chunkZ * 16);
        int attempts = 0;
        BlockPos spawn = world.getSpawnPoint();
        for (Vec3 last : birthPrev.values())
            if (vectors.distance(last) < TitanConfig.separationBirth)
            return;
        if (vectors.distance(0, 0, 0) >= TitanConfig.spawnProtection || !(world.provider instanceof WorldProviderEnd)) {
            if (vectors.distance(spawn.getX(), spawn.getY(), spawn.getZ()) > TitanConfig.spawnProtection)
                while (entity == null && attempts < 5) {
                // PERF — roll the chance FIRST, construct the (heavy) titan only when it actually spawns.
                if (Maths.chance(chance * 0.01F)) {
                    entity = factories[Maths.random(factories.length - 1)].get();
                    if (entity == null) {
                        attempts++;
                        continue;
                    }
                    vectors.yVec = world.getTopSolidOrLiquidBlock(new BlockPos((int)vectors.xVec, 0, (int)vectors.zVec)).getY();
                    if (entity instanceof EntityTitan) {
                        ((EntityTitan) entity).wreckInAABBGriefingBypassBlocks(entity.getEntityBoundingBox());
                        ((EntityTitan) entity).onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), (IEntityLivingData)null);
                    }

                    entity.setLocationAndAngles(vectors.xVec + 0.5F + Maths.random(16), vectors.yVec, vectors.zVec + 0.5F + Maths.random(16), Maths.random(360.0F), 0.0F);
                    TheTitans.debug(entity.getName() +  "[" + (birthPrev.size() - 1) + "] [" + vectors.distance(spawn.getX(), spawn.getY(), spawn.getZ()) +  "] spawned at X:" + entity.posX + ", Y:" + entity.posY + ", Z:" + entity.posZ);
                    world.spawnEntity(entity);
                    birthPrev.put(entity, vectors);
                    break;
                }

                else {
                    entity = null;
                    attempts++;
                }
            }

            Maths.refreshRandom();
        }
    }
}
