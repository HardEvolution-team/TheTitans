package com.electrifiedded.thetitans.registry;

import com.electrifiedded.thetitans.TheTitans;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

// 1.12.2 requires every SoundEvent to be registered in the Forge registry (with a registry name
// matching its sounds.json key) or playSound is silent. All 112 keys from sounds.json registered here.
@Mod.EventBusSubscriber(modid = TheTitans.MODID_V)
public class TitanAudio {
    public static final String[] NAMES = {
        "OMG",
                "bigZombieDeath",
                "bigZombieGrunt",
                "bigZombieLiving",
                "cast",
                "castAttack",
                "castPassive",
                "chaffDeployment",
                "dimensionalTransport",
                "distantLargeFall",
                "evilGolemPunch",
                "executorDragonDeath",
                "executorDragonFlap",
                "executorDragonGrunt",
                "executorDragonLiving",
                "funnyExplosion",
                "gargoyleDeath",
                "gargoyleGrunt",
                "gargoyleLiving",
                "ghoulDeath",
                "ghoulGrunt",
                "ghoulLiving",
                "groundSmash",
                "harcadiumBlockHum",
                "harcadiumHum",
                "largeFall",
                "lightningCharge",
                "lightningThrow",
                "mortarHit",
                "mortarShoot",
                "predatorSpider",
                "quickApperence",
                "ratDeath",
                "ratGiantHit",
                "ratGiantSay",
                "ratHit",
                "ratSay",
                "slashFlesh",
                "swordDrag",
                "titanBirth",
                "titanBlazeBreathe",
                "titanBlazeDeath",
                "titanBlazeGrunt",
                "titanCreeperAwaken",
                "titanCreeperBeginMove",
                "titanCreeperDeath",
                "titanCreeperExplosion",
                "titanCreeperGrunt",
                "titanCreeperLiving",
                "titanCreeperStun",
                "titanCreeperWarning",
                "titanCrushWood",
                "titanEnderColossusChomp",
                "titanEnderColossusDeath",
                "titanEnderColossusGrunt",
                "titanEnderColossusLiving",
                "titanEnderColossusRoar",
                "titanEnderColossusScream",
                "titanEnderColossusScreamLong",
                "titanEnderColossusStare",
                "titanFall",
                "titanGhastCharge",
                "titanGhastDeath",
                "titanGhastFireball",
                "titanGhastGrunt",
                "titanGhastLiving",
                "titanHitMetal",
                "titanPigZombieDeath",
                "titanPigZombieGrunt",
                "titanPigZombieLiving",
                "titanPress",
                "titanQuake",
                "titanRumble",
                "titanSilverfishDeath",
                "titanSilverfishGrunt",
                "titanSilverfishLiving",
                "titanSkeletonAwaken",
                "titanSkeletonBeginMove",
                "titanSkeletonBoneCrunch",
                "titanSkeletonDeath",
                "titanSkeletonGetUp",
                "titanSkeletonGrunt",
                "titanSkeletonLiving",
                "titanSlam",
                "titanSpiderDeath",
                "titanSpiderGrunt",
                "titanSpiderLiving",
                "titanStep",
                "titanStrike",
                "titanSwing",
                "titanWitherSkeletonDeath",
                "titanWitherSkeletonGrunt",
                "titanWitherSkeletonLiving",
                "titanZombieCreation",
                "titanZombieDeath",
                "titanZombieGrunt",
                "titanZombieLiving",
                "titanZombieRoar",
                "titanland",
                "titanpunch",
                "titansummonminion",
                "turretDeath",
                "turretDeath2",
                "turretDeath3",
                "turretShoot",
                "turretShoot2",
                "warlockHurt",
                "warlockIdle",
                "wightDeath",
                "wightGrunt",
                "wightLiving",
                "witherzillaDeath",
                "witherzillaGrunt",
                "witherzillaLiving",
                "witherzillaSpawn",
                "wraithDeath",
                "wraithHurt"
    };

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        for (String name : NAMES) {
            // registry names + sounds.json keys are lowercase; keep registration consistent.
            ResourceLocation rl = new ResourceLocation(TheTitans.MODID_V, name.toLowerCase(java.util.Locale.ROOT));
            event.getRegistry().register(new SoundEvent(rl).setRegistryName(rl));
        }
        CACHE_LOOKUP.clear();
        registeredSounds = true;
    }

    // Resolve a registered SoundEvent by its sounds.json key. Accepts bare keys ("titanCreeperDeath")
    // and namespaced ones ("thetitans:titanCreeperDeath"); returns null if unknown.
    // PERF — every titan sound did toLowerCase + new ResourceLocation + a registry lookup. Sounds are all
    // registered before any entity ticks, so resolved names (and misses, via MISSING) are cached.
    private static final SoundEvent MISSING = new SoundEvent(new ResourceLocation(TheTitans.MODID_V, "missing_sound_sentinel"));
    private static volatile boolean registeredSounds;
    private static final java.util.Map<String, SoundEvent> CACHE_LOOKUP = new java.util.concurrent.ConcurrentHashMap<String, SoundEvent>();

    public static SoundEvent get(String name) {
        if (name == null || name.isEmpty())
            return null;
        SoundEvent cached = CACHE_LOOKUP.get(name);
        if (cached == null) {
            String key = name.toLowerCase(java.util.Locale.ROOT);
            ResourceLocation rl = key.indexOf(':') >= 0 ? new ResourceLocation(key) : new ResourceLocation(TheTitans.MODID_V, key);
            cached = net.minecraftforge.fml.common.registry.ForgeRegistries.SOUND_EVENTS.getValue(rl);
            if (cached == null) {
                // misses are only cached once every sound is registered (registry events fire after preInit)
                if (!registeredSounds)
                    return null;
                cached = MISSING;
            }
            CACHE_LOOKUP.put(name, cached);
        }
        return cached == MISSING ? null : cached;
    }
}
