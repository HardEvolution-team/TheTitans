package com.electrifiedded.thetitans.client;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.config.TitanConfig;
import com.electrifiedded.thetitans.entity.minion.EntityBlazeMinion;
import com.electrifiedded.thetitans.entity.minion.EntityCaveSpiderMinion;
import com.electrifiedded.thetitans.entity.minion.EntityCreeperMinion;
import com.electrifiedded.thetitans.entity.minion.EntityDragonMinion;
import com.electrifiedded.thetitans.entity.minion.EntityEndermanMinion;
import com.electrifiedded.thetitans.entity.minion.EntityGhastGuard;
import com.electrifiedded.thetitans.entity.minion.EntityGhastMinion;
import com.electrifiedded.thetitans.entity.minion.EntityGiantZombieBetter;
import com.electrifiedded.thetitans.entity.minion.EntityPigZombieMinion;
import com.electrifiedded.thetitans.entity.minion.EntitySilverfishMinion;
import com.electrifiedded.thetitans.entity.minion.EntitySkeletonMinion;
import com.electrifiedded.thetitans.entity.minion.EntitySpiderMinion;
import com.electrifiedded.thetitans.entity.minion.EntityWitherMinion;
import com.electrifiedded.thetitans.entity.minion.EntityZombieMinion;
import com.electrifiedded.thetitans.entity.misc.EntityEnderSquid;
import com.electrifiedded.thetitans.entity.mob.EntityEvilGolem;
import com.electrifiedded.thetitans.entity.mob.EntityHarcadiumGolem;
import com.electrifiedded.thetitans.entity.mob.EntityMagicUser;
import com.electrifiedded.thetitans.entity.mob.EntitySilverfishOther;
import com.electrifiedded.thetitans.entity.mob.EntitySpiderOther;
import com.electrifiedded.thetitans.entity.mob.EntityThinUndeadOther;
import com.electrifiedded.thetitans.entity.mob.EntityUndeadOther;
import com.electrifiedded.thetitans.entity.mob.EntityVoidGolem;
import com.electrifiedded.thetitans.entity.mob.EntityWraith;
import com.electrifiedded.thetitans.entity.titan.EntityBlazeTitan;
import com.electrifiedded.thetitans.entity.titan.EntityCaveSpiderTitan;
import com.electrifiedded.thetitans.entity.titan.EntityChaff;
import com.electrifiedded.thetitans.entity.titan.EntityCreeperTitan;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossus;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossusCrystal;
import com.electrifiedded.thetitans.entity.titan.EntityExecutorDragon;
import com.electrifiedded.thetitans.entity.titan.EntityGammaLightning;
import com.electrifiedded.thetitans.entity.titan.EntityGargoyle;
import com.electrifiedded.thetitans.entity.titan.EntityGargoyleTitan;
import com.electrifiedded.thetitans.entity.titan.EntityGargoyleTitanFireball;
import com.electrifiedded.thetitans.entity.titan.EntityGhastTitan;
import com.electrifiedded.thetitans.entity.titan.EntityGrowthSerum;
import com.electrifiedded.thetitans.entity.titan.EntityHarcadiumArrow;
import com.electrifiedded.thetitans.entity.titan.EntityIronGolemTitan;
import com.electrifiedded.thetitans.entity.titan.EntityItemBomb;
import com.electrifiedded.thetitans.entity.titan.EntityLavaSpit;
import com.electrifiedded.thetitans.entity.titan.EntityLightningBall;
import com.electrifiedded.thetitans.entity.titan.EntityMagmaCubeTitan;
import com.electrifiedded.thetitans.entity.titan.EntityPigZombieTitan;
import com.electrifiedded.thetitans.entity.titan.EntityProtoBall;
import com.electrifiedded.thetitans.entity.titan.EntitySilverfishTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitanGiantArrow;
import com.electrifiedded.thetitans.entity.titan.EntitySlimeTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySnowGolemTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySpiderTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitanCorpse;
import com.electrifiedded.thetitans.entity.titan.EntityTitanFireball;
import com.electrifiedded.thetitans.entity.titan.EntityTitanPart;
import com.electrifiedded.thetitans.entity.titan.EntityTitanSpirit;
import com.electrifiedded.thetitans.entity.titan.EntityWebShot;
import com.electrifiedded.thetitans.entity.titan.EntityWitherTurret;
import com.electrifiedded.thetitans.entity.titan.EntityWitherTurretGround;
import com.electrifiedded.thetitans.entity.titan.EntityWitherTurretMortar;
import com.electrifiedded.thetitans.entity.titan.EntityWitherzilla;
import com.electrifiedded.thetitans.entity.titan.EntityWitherzillaMinion;
import com.electrifiedded.thetitans.entity.titan.EntityXPBomb;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;
import com.electrifiedded.thetitans.entity.titan.classic.EntityOgPigZombieTitan;
import com.electrifiedded.thetitans.entity.titan.classic.EntityOgZombieTitan;
import com.electrifiedded.thetitans.item.spawn.ItemSpawnEggRegular;
import com.electrifiedded.thetitans.util.RegistryHelper.Registry;
import com.electrifiedded.thetitans.util.RegistryHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.boss.*;
import net.minecraft.entity.monster.*;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class RenderTheTitans {
    private static int id = 0;
    public static final Registry<Class<? extends Entity>, Object[]> entityRegistry = new Registry<Class<? extends Entity>, Object[]>();

    public static void setup() {
        createEntity(EntitySilverfishMinion.class, "SilverfishMinion", 7237230, 3158064);
        createEntity(EntityCaveSpiderMinion.class, "CaveSpiderMinion", 803406, 11013646);
        createEntity(EntitySpiderMinion.class, "SpiderMinion", 3419431, 11013646);
        createEntity(EntityZombieMinion.class, "ZombieMinion", 44975, 7969893);
        createEntity(EntityGiantZombieBetter.class, "GiantMinion", 44975, 5870909);
        createEntity(EntitySkeletonMinion.class, "SkeletonMinion", 12698049, 4802889);
        createEntity(EntityWitherMinion.class, "WitherMinion", 0, 1447446);
        createEntity(EntityCreeperMinion.class, "CreeperMinion", 894731, 0);
        createEntity(EntityPigZombieMinion.class, "PigZombieMinion", 15373203, 5009705);
        createEntity(EntityGhastGuard.class, "GhastGuard", 16382457, 12369084);
        createEntity(EntityBlazeMinion.class, "BlazeMinion", 16167425, 16775294);
        createEntity(EntityGhastMinion.class, "GhastMinion", 16382457, 12369084);
        createEntity(EntityEndermanMinion.class, "EndermanMinion", 1447446, 0);
        createEntity(EntityDragonMinion.class, "EnderDragonMinion", 0x000000, 0xff00ea);
        createEntity(EntityEnderColossusCrystal.class, "EnderColossusCrystal", 0xffffff, 0xff00ea);
        createEntity(EntityWitherTurret.class, "WitherTurret", 0, 1447446);
        createEntity(EntityWitherTurretGround.class, "WitherTurretGround", 0, 1447446);
        createEntity(EntityWitherTurretMortar.class, "WitherTurretMortar", 0, 1447446);
        createEntity(EntityWitherzillaMinion.class, "WitherBossMinion", 0, 1447446);
        createEntity(EntityGargoyle.class, "Gargoyle", 0xd4d4d4, 7237230);
        createEntity(EntitySilverfishOther.class, "SilverfishCustom", 0xffffff, 0);
        createEntity(EntitySpiderOther.class, "SpiderCustom", 0xffffff, 0);
        createEntity(EntityUndeadOther.class, "UndeadCustom", 0xffffff, 0);
        createEntity(EntityThinUndeadOther.class, "ThinUndeadCustom", 0xffffff, 0);
        createEntity(EntityWraith.class, "Wraith", 0xaaaaaa, 0x050505);
        createEntity(EntityEvilGolem.class, "EvilGolem", 0xffffff, 0);
        createEntity(EntityMagicUser.class, "MagicUser", 0xffffff, 0);
        createEntity(EntityHarcadiumGolem.class, "HarcadiumGolem", 13369594, 1447446);
        createEntity(EntityVoidGolem.class, "VoidGolem", 0, 0);
        buildEgg(53, 44975, 5870909);
        buildEgg(63, 1447446, 13369594);
        buildEgg(64, 1315860, 1842204);
        buildProjectile(EntityGrowthSerum.class, "GrowthSerum");
        createEntity(EntityChaff.class, "Chaff");
        buildProjectile(EntityTitanFireball.class, "TitanFireball");
        buildProjectile(EntityGargoyleTitanFireball.class, "GargoyleTitanFireball");
        buildProjectile(EntityLavaSpit.class, "LavaSpit");
        createEntity(EntityGammaLightning.class, "GammaLightning");
        createEntity(EntityTitanPart.class, "TitanPart");
        buildProjectile(EntityWebShot.class, "WebShot");
        buildBomb(EntityXPBomb.class, "XPBomb");
        buildBomb(EntityItemBomb.class, "ItemBomb");
        buildProjectile(EntityLightningBall.class, "LightningBall");
        buildProjectile(EntityProtoBall.class, "ProtoBall");
        createEntity(EntityTitanSpirit.class, "TitanSpirit");
        buildProjectile(EntityHarcadiumArrow.class, "ArrowHarcadium");
        buildProjectile(EntitySkeletonTitanGiantArrow.class, "ArrowGiant");
        buildGiant(EntitySnowGolemTitan.class, "SnowManTitan");
        buildGiant(EntitySlimeTitan.class, "SlimeTitan");
        buildGiant(EntityMagmaCubeTitan.class, "LavaSlimeTitan");
        buildGiant(EntitySilverfishTitan.class, "SilverfishTitan");
        buildGiant(EntityCaveSpiderTitan.class, "CaveSpiderTitan");
        buildGiant(EntitySpiderTitan.class, "SpiderTitan");
        buildGiant(EntitySkeletonTitan.class, "SkeletonTitan");
        buildGiant(EntityZombieTitan.class, "ZombieTitan");
        buildGiant(EntityCreeperTitan.class, "CreeperTitan");
        buildGiant(EntityPigZombieTitan.class, "PigZombieTitan");
        buildGiant(EntityBlazeTitan.class, "BlazeTitan");
        buildGiant(EntityGargoyleTitan.class, "GargoyleTitan");
        buildGiant(EntityGhastTitan.class, "GhastTitan");
        buildGiant(EntityEnderColossus.class, "EndermanTitan");
        buildGiant(EntityIronGolemTitan.class, "VillagerGolemTitan");
        buildGiant(EntityWitherzilla.class, "WitherBossTitan");
        // New (1.12.2) content entities
        createEntity(com.electrifiedded.thetitans.entity.titan.EntityTitanCorpse.class, "TitanCorpse");
        buildGiant(com.electrifiedded.thetitans.entity.titan.classic.EntityOgZombieTitan.class, "OgZombieTitan");
        buildGiant(com.electrifiedded.thetitans.entity.titan.classic.EntityOgPigZombieTitan.class, "OgPigZombieTitan");
        createEntity(com.electrifiedded.thetitans.entity.misc.EntityEnderSquid.class, "EnderSquid");
        buildGiant(EntityExecutorDragon.class, "EnderDragonTitan");
        com.electrifiedded.thetitans.compat.mutant.MutantTitans.registerEntities();

        EntityRegistry.addSpawn(EntityBlaze.class, 50, 2, 2, EnumCreatureType.MONSTER, Biomes.HELL);
        EntityRegistry.addSpawn(EntityWither.class, 1, 1, 1, EnumCreatureType.MONSTER, Biomes.HELL);
        EntityRegistry.addSpawn(EntityIronGolem.class, 1, 1, 1, EnumCreatureType.CREATURE, Biomes.PLAINS, Biomes.SAVANNA);
        EntityRegistry.addSpawn(EntitySilverfish.class, 100, 4, 4, EnumCreatureType.MONSTER, fetchBiomesSpawn());
        EntityRegistry.addSpawn(com.electrifiedded.thetitans.entity.mob.EntityUndeadOther.class, 100, 1, 4, EnumCreatureType.MONSTER, fetchBiomesPlusEndAndHellSpawn());
        EntityRegistry.addSpawn(com.electrifiedded.thetitans.entity.mob.EntityThinUndeadOther.class, 100, 1, 4, EnumCreatureType.MONSTER, fetchBiomePlusHellSpawn());
        EntityRegistry.addSpawn(com.electrifiedded.thetitans.entity.mob.EntityEvilGolem.class, 5, 1, 1, EnumCreatureType.MONSTER, fetchBiomesPlusEndAndHellSpawn());
        EntityRegistry.addSpawn(com.electrifiedded.thetitans.entity.mob.EntityWraith.class, 1, 1, 1, EnumCreatureType.MONSTER, fetchBiomePlusHellSpawn());
        EntityRegistry.addSpawn(com.electrifiedded.thetitans.entity.titan.classic.EntityOgZombieTitan.class, 1, 1, 1, EnumCreatureType.MONSTER, fetchBiomesSpawn());
        EntityRegistry.addSpawn(com.electrifiedded.thetitans.entity.titan.classic.EntityOgPigZombieTitan.class, 1, 1, 1, EnumCreatureType.MONSTER, Biomes.HELL);
        EntityRegistry.addSpawn(com.electrifiedded.thetitans.entity.misc.EntityEnderSquid.class, 20, 1, 4, EnumCreatureType.MONSTER, Biomes.SKY);
    }

    public static void preInit() {
        TitanConfig.load();
    }

    public static void init() {
    }

    public static void postInit() {
    }

    private static void create(Class<? extends Entity> entityClass, String entityName, int range, int rate, boolean velocityUpdates, int... colors) {
        TheTitans.debug("Registering Entity: " + entityName);
        // translation key entity.thetitans.<Name>.name, as in the original lang file
        EntityRegistry.registerModEntity(new ResourceLocation(TheTitans.MODID_V, entityName), entityClass, TheTitans.MODID_V + "." + entityName, id, TheTitans.modInstance, range, rate, velocityUpdates);
        if (colors.length > 1)
            ItemSpawnEggRegular.appendEgg(entityClass, colors[0], colors[1]);
        if (EntityLivingBase.class.isAssignableFrom(entityClass))
            entityRegistry.add(entityClass);
        id++;
    }

    // PERF (network) — mobs were tracked at updateFrequency 1 (a move/look packet every tick per mob per
    // watching player). 3 + velocity sync is the vanilla mob setting; clients interpolate between updates.
    public static void createEntity(Class<? extends Entity> entityClass, String entityName, int... colors) {
        create(entityClass, entityName, 128, 3, true, colors);
    }

    public static void buildBomb(Class<? extends Entity> entityClass, String entityName) {
        create(entityClass, entityName, 128, 5, true);
    }

    public static void buildProjectile(Class<? extends Entity> entityClass, String entityName) {
        create(entityClass, entityName, 256, 1, true);
    }

    public static void buildGiant(Class<? extends Entity> entityClass, String entityName) {
        create(entityClass, entityName, 1024, 1, true);
    }

    private static void buildEgg(int id, int solidColor, int spotColor) {
    }

    public static Biome[] fetchBiomesSpawn() {
        return new Biome[] {
            Biomes.OCEAN,Biomes.PLAINS,Biomes.DESERT,Biomes.EXTREME_HILLS,Biomes.FOREST,Biomes.TAIGA,Biomes.SWAMPLAND,Biomes.RIVER,Biomes.FROZEN_OCEAN,Biomes.FROZEN_RIVER,Biomes.ICE_PLAINS,Biomes.ICE_MOUNTAINS,Biomes.BEACH,Biomes.DESERT_HILLS,Biomes.FOREST_HILLS,Biomes.TAIGA_HILLS,Biomes.EXTREME_HILLS_EDGE,Biomes.JUNGLE,Biomes.JUNGLE_HILLS,Biomes.JUNGLE_EDGE,Biomes.DEEP_OCEAN,Biomes.STONE_BEACH,Biomes.COLD_BEACH,Biomes.BIRCH_FOREST,Biomes.BIRCH_FOREST_HILLS,Biomes.ROOFED_FOREST,Biomes.COLD_TAIGA,Biomes.COLD_TAIGA_HILLS,Biomes.REDWOOD_TAIGA,Biomes.REDWOOD_TAIGA_HILLS,Biomes.EXTREME_HILLS_WITH_TREES,Biomes.SAVANNA,Biomes.SAVANNA_PLATEAU,Biomes.MESA,Biomes.MESA_ROCK,Biomes.MESA_CLEAR_ROCK
        };
    }

    public static Biome[] fetchBiomePlusHellSpawn() {
        return new Biome[] {
            Biomes.OCEAN,Biomes.PLAINS,Biomes.DESERT,Biomes.EXTREME_HILLS,Biomes.FOREST,Biomes.TAIGA,Biomes.SWAMPLAND,Biomes.RIVER,Biomes.FROZEN_OCEAN,Biomes.FROZEN_RIVER,Biomes.ICE_PLAINS,Biomes.ICE_MOUNTAINS,Biomes.BEACH,Biomes.DESERT_HILLS,Biomes.FOREST_HILLS,Biomes.TAIGA_HILLS,Biomes.EXTREME_HILLS_EDGE,Biomes.JUNGLE,Biomes.JUNGLE_HILLS,Biomes.JUNGLE_EDGE,Biomes.DEEP_OCEAN,Biomes.STONE_BEACH,Biomes.COLD_BEACH,Biomes.BIRCH_FOREST,Biomes.BIRCH_FOREST_HILLS,Biomes.ROOFED_FOREST,Biomes.COLD_TAIGA,Biomes.COLD_TAIGA_HILLS,Biomes.REDWOOD_TAIGA,Biomes.REDWOOD_TAIGA_HILLS,Biomes.EXTREME_HILLS_WITH_TREES,Biomes.SAVANNA,Biomes.SAVANNA_PLATEAU,Biomes.MESA,Biomes.MESA_ROCK,Biomes.MESA_CLEAR_ROCK,Biomes.HELL
        };
    }

    public static Biome[] fetchBiomesPlusEndSpawn() {
        return new Biome[] {
            Biomes.OCEAN,Biomes.PLAINS,Biomes.DESERT,Biomes.EXTREME_HILLS,Biomes.FOREST,Biomes.TAIGA,Biomes.SWAMPLAND,Biomes.RIVER,Biomes.FROZEN_OCEAN,Biomes.FROZEN_RIVER,Biomes.ICE_PLAINS,Biomes.ICE_MOUNTAINS,Biomes.BEACH,Biomes.DESERT_HILLS,Biomes.FOREST_HILLS,Biomes.TAIGA_HILLS,Biomes.EXTREME_HILLS_EDGE,Biomes.JUNGLE,Biomes.JUNGLE_HILLS,Biomes.JUNGLE_EDGE,Biomes.DEEP_OCEAN,Biomes.STONE_BEACH,Biomes.COLD_BEACH,Biomes.BIRCH_FOREST,Biomes.BIRCH_FOREST_HILLS,Biomes.ROOFED_FOREST,Biomes.COLD_TAIGA,Biomes.COLD_TAIGA_HILLS,Biomes.REDWOOD_TAIGA,Biomes.REDWOOD_TAIGA_HILLS,Biomes.EXTREME_HILLS_WITH_TREES,Biomes.SAVANNA,Biomes.SAVANNA_PLATEAU,Biomes.MESA,Biomes.MESA_ROCK,Biomes.MESA_CLEAR_ROCK,Biomes.SKY
        };
    }

    public static Biome[] fetchBiomesPlusEndAndHellSpawn() {
        return new Biome[] {
            Biomes.OCEAN,Biomes.PLAINS,Biomes.DESERT,Biomes.EXTREME_HILLS,Biomes.FOREST,Biomes.TAIGA,Biomes.SWAMPLAND,Biomes.RIVER,Biomes.FROZEN_OCEAN,Biomes.FROZEN_RIVER,Biomes.ICE_PLAINS,Biomes.ICE_MOUNTAINS,Biomes.BEACH,Biomes.DESERT_HILLS,Biomes.FOREST_HILLS,Biomes.TAIGA_HILLS,Biomes.EXTREME_HILLS_EDGE,Biomes.JUNGLE,Biomes.JUNGLE_HILLS,Biomes.JUNGLE_EDGE,Biomes.DEEP_OCEAN,Biomes.STONE_BEACH,Biomes.COLD_BEACH,Biomes.BIRCH_FOREST,Biomes.BIRCH_FOREST_HILLS,Biomes.ROOFED_FOREST,Biomes.COLD_TAIGA,Biomes.COLD_TAIGA_HILLS,Biomes.REDWOOD_TAIGA,Biomes.REDWOOD_TAIGA_HILLS,Biomes.EXTREME_HILLS_WITH_TREES,Biomes.SAVANNA,Biomes.SAVANNA_PLATEAU,Biomes.MESA,Biomes.MESA_ROCK,Biomes.MESA_CLEAR_ROCK,Biomes.SKY,Biomes.HELL
        };
    }
}
