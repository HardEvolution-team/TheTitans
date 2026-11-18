package com.electrifiedded.thetitans.data;

import com.electrifiedded.thetitans.config.TitanConfig;
import com.electrifiedded.thetitans.entity.deity.IDeity;
import com.electrifiedded.thetitans.entity.minion.EntityBlazeMinion;
import com.electrifiedded.thetitans.entity.minion.EntityCreeperMinion;
import com.electrifiedded.thetitans.entity.minion.EntityDragonMinion;
import com.electrifiedded.thetitans.entity.minion.EntityEndermanMinion;
import com.electrifiedded.thetitans.entity.minion.EntityGhastGuard;
import com.electrifiedded.thetitans.entity.minion.EntityGhastMinion;
import com.electrifiedded.thetitans.entity.minion.EntityGiantZombieBetter;
import com.electrifiedded.thetitans.entity.minion.EntityPigZombieMinion;
import com.electrifiedded.thetitans.entity.minion.EntitySkeletonMinion;
import com.electrifiedded.thetitans.entity.minion.EntitySpiderMinion;
import com.electrifiedded.thetitans.entity.minion.EntityWitherMinion;
import com.electrifiedded.thetitans.entity.minion.EntityZombieMinion;
import com.electrifiedded.thetitans.entity.mob.EntityBurningMobzilla;
import com.electrifiedded.thetitans.entity.mob.EntityEvilGolem;
import com.electrifiedded.thetitans.entity.mob.EntityMethuselahKraken;
import com.electrifiedded.thetitans.entity.mob.EntityOverlordScorpion;
import com.electrifiedded.thetitans.entity.mob.EntityThinUndeadOther;
import com.electrifiedded.thetitans.entity.mob.EntityUndeadOther;
import com.electrifiedded.thetitans.entity.titan.EntityBlazeTitan;
import com.electrifiedded.thetitans.entity.titan.EntityCreeperTitan;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossus;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossusCrystal;
import com.electrifiedded.thetitans.entity.titan.EntityGhastTitan;
import com.electrifiedded.thetitans.entity.titan.EntityPigZombieTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySpiderTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitanPart;
import com.electrifiedded.thetitans.entity.titan.EntityTitanSpirit;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;
import com.electrifiedded.thetitans.entity.titan.classic.EntityOgZombieTitan;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Faction / boss classification restored from original TheTitans.java:556,814-868 (isFactionSame & friends).
 * the original resolved companion-mod classes through 1.7.10 EntityList names and canonical class-name
 * prefixes on every call (two dozen map lookups + string compares per hurt event). Here the class-only part of
 * every predicate is computed once per class into a bitmask (ClassValue); only the few instance-dependent checks
 * (tamed, golem/undead variant, OreSpawn boss size) run per call. Companion mods are matched by their 1.12 names:
 * OreSpawn port classes (ded.orespawn.entity.*) by simple name, Mutant Beasts by registry id.
 */
public class TitanFactions {
    private static final int PLAYER = 1, ENDER_V = 1 << 1, ZOMBIE = 1 << 2, SKELETON = 1 << 3, CREEPER = 1 << 4,
            SPIDER_V = 1 << 5, PIGZOMBIE_V = 1 << 6, GHAST_V = 1 << 7, BLAZE_V = 1 << 8,
            CHIEF_OS = 1 << 9, CHIEF_MYTHICAL = 1 << 10, MUTANT_V = 1 << 11, DUD_V = 1 << 12,
            SIZED_CHIEF_OS = 1 << 13, CHIEF = 1 << 14;

    private static final String PACKAGE_ORESPAWN = "ded.orespawn.";
    // Original isOreSpawnBossAn list, by OreSpawn-port class name (registry names: Kraken="The Kraken", PitchBlack="Nightmare", ...).
    private static final Set<String> BOSSES_ORESPAWN = new HashSet<String>(Arrays.asList(
            "Kraken", "Godzilla", "TheKing", "TheQueen", "Basilisk", "CaterKiller", "Cephadrome", "EmperorScorpion",
            "HerculesBeetle", "Hammerhead", "Leon", "Robot2", "Robot4", "GiantRobot", "TrooperBug", "Nastysaurus", "TRex",
            "Triffid", "Molenoid", "Brutalfly", "Vortex", "SeaViper", "Mantis"));
    // Original isDudMobA OreSpawn prefixes (GodzillaHead, Ghost* etc.).
    private static final Set<String> DUDS_ORESPAWN = new HashSet<String>(Arrays.asList(
            "GodzillaHead", "KingHead", "QueenHead", "RubberDucky", "Cockateil", "StinkBug", "RockBase", "EntityAnt",
            "Dragonfly", "Cricket", "EntityRedAnt", "EntityRainbowAnt", "EntityUnstableAnt", "Termite", "Ghost",
            "GhostSkelly", "Elevator"));
    private static final Set<String> ENDER_ORESPAWN = new HashSet<String>(Arrays.asList("EnderKnight", "EnderReaper", "EmperorScorpion"));
    private static final String[] BOSSES_MYTHICAL = {"MyWindigo", "MyUrsaMAJOR", "MySpikezilla", "MyKingbowser", "MyFlameDragon",
                "MyArcticScorpion", "MyMLPHydra", "MyRainbowCentipede", "MyCrabzilla", "MyCragadile", "MySkullBoss", "MyThunderHooves",
                "MyIronWill", "MyYakPrince", "MyManticore"};

    private static final ClassValue<Integer> MASKS = new ClassValue<Integer>() {
        @Override
        protected Integer computeValue(Class<?> type) {
            return Integer.valueOf(maskClass(type));
        }
    };

    private static int maskClass(Class<?> c) {
        int m = 0;
        if (EntityPlayer.class.isAssignableFrom(c) || EntityVillager.class.isAssignableFrom(c) || EntityGolem.class.isAssignableFrom(c))
            m |= PLAYER;
        if (EntityEnderColossus.class.isAssignableFrom(c) || EntityEndermanMinion.class.isAssignableFrom(c) || EntityDragon.class.isAssignableFrom(c)
            || EntityDragonMinion.class.isAssignableFrom(c) || c == EntityEnderman.class || EntityEnderColossusCrystal.class.isAssignableFrom(c))
            m |= ENDER_V;
        if (EntityZombieTitan.class.isAssignableFrom(c) || EntityZombieMinion.class.isAssignableFrom(c) || EntityGiantZombieBetter.class.isAssignableFrom(c)
            || c == EntityZombie.class || c == EntityZombieVillager.class || c == EntityHusk.class)
            m |= ZOMBIE;
        if (EntitySkeletonTitan.class.isAssignableFrom(c) || EntitySkeletonMinion.class.isAssignableFrom(c) || EntityThinUndeadOther.class.isAssignableFrom(c)
            || EntityWitherMinion.class.isAssignableFrom(c) || c == EntityWither.class || c == EntitySkeleton.class || c == EntityWitherSkeleton.class || c == EntityStray.class)
            m |= SKELETON;
        if (EntityCreeperTitan.class.isAssignableFrom(c) || EntityCreeperMinion.class.isAssignableFrom(c) || EntityCreeper.class.isAssignableFrom(c))
            m |= CREEPER;
        if (EntitySpiderTitan.class.isAssignableFrom(c) || EntitySpiderMinion.class.isAssignableFrom(c) || EntitySpider.class.isAssignableFrom(c))
            m |= SPIDER_V;
        if (EntityPigZombieTitan.class.isAssignableFrom(c) || EntityPigZombieMinion.class.isAssignableFrom(c) || EntityGhastGuard.class.isAssignableFrom(c) || c == EntityPigZombie.class)
            m |= PIGZOMBIE_V;
        if (EntityGhastTitan.class.isAssignableFrom(c) || EntityGhastMinion.class.isAssignableFrom(c) || EntityGhast.class.isAssignableFrom(c))
            m |= GHAST_V;
        if (EntityBlazeTitan.class.isAssignableFrom(c) || EntityBlazeMinion.class.isAssignableFrom(c) || EntityBlaze.class.isAssignableFrom(c))
            m |= BLAZE_V;
        if (EntityTitanSpirit.class.isAssignableFrom(c) || EntityTitanPart.class.isAssignableFrom(c) || EntityAmbientCreature.class.isAssignableFrom(c))
            m |= DUD_V;
        // Original IBossDisplayData implementers: EntityDeity (titans, gods), EntityTitanSpirit, the OG titans.
        if (IDeity.class.isAssignableFrom(c) || EntityTitan.class.isAssignableFrom(c) || EntityTitanSpirit.class.isAssignableFrom(c) || EntityOgZombieTitan.class.isAssignableFrom(c))
            m |= CHIEF;
        if (EntityOverlordScorpion.class.isAssignableFrom(c) || EntityMethuselahKraken.class.isAssignableFrom(c) || EntityBurningMobzilla.class.isAssignableFrom(c))
            m |= CHIEF_OS;

        String name = c.getName();
        if (name.startsWith(PACKAGE_ORESPAWN)) {
            String simple = c.getSimpleName();
            if (BOSSES_ORESPAWN.contains(simple)) m |= CHIEF_OS;
            if (simple.equals("PitchBlack") || simple.equals("Crab")) m |= SIZED_CHIEF_OS;
            if (DUDS_ORESPAWN.contains(simple)) m |= DUD_V;
            if (ENDER_ORESPAWN.contains(simple)) m |= ENDER_V;
        } else if (name.startsWith("twilightsparkle.basic.mob.")) {
            String simple = name.substring("twilightsparkle.basic.mob.".length());
            for (String boss : BOSSES_MYTHICAL)
                if (simple.startsWith(boss)) { m |= CHIEF_MYTHICAL; break; }
        }

        if (Entity.class.isAssignableFrom(c)) {
            @SuppressWarnings("unchecked")
            ResourceLocation id = EntityList.getKey((Class<? extends Entity>) c);
            if (id != null && id.getNamespace().equals("mutantbeasts")) {
                String path = id.getPath();
                if (path.equals("mutant_zombie")) m |= MUTANT_V | ZOMBIE;
                else if (path.equals("mutant_skeleton")) m |= MUTANT_V | SKELETON;
                else if (path.equals("mutant_creeper")) m |= MUTANT_V | CREEPER;
                else if (path.equals("mutant_enderman")) m |= MUTANT_V | ENDER_V;
                else if (path.equals("creeper_minion")) m |= CREEPER;
            }
        }
        return m;
    }

    private static int mask(Entity e) {
        return e == null ? 0 : MASKS.get(e.getClass()).intValue();
    }

    /** Instance-dependent faction bits layered on top of the per-class mask. */
    private static int runFactions(Entity e) {
        int m = mask(e);
        if (e instanceof EntityTameable && ((EntityTameable)e).isTamed() || e instanceof AbstractHorse && ((AbstractHorse)e).isTame())
            m |= PLAYER;
        else if (e instanceof EntityEvilGolem) {
            switch (((EntityEvilGolem)e).fetchKindMob()) {
                case 1: m |= SKELETON; break;   // GolemSkeletonWither
                case 2: m |= PIGZOMBIE_V; break;  // GoldenGuardian
                case 3: m |= ENDER_V; break;      // GolemEnder
                case 4: break;                  // GolemSpider: no faction in the original
                default: m |= ZOMBIE;           // GolemZombie
            }
        } else if (e instanceof EntityUndeadOther)
            m |= ((EntityUndeadOther)e).fetchKindMob() == 1 ? PIGZOMBIE_V : ZOMBIE; // type 1 = GoldenGuard
        return m;
    }

    public static boolean isFactionSame(Entity mobRef1, Entity mobRef2) {
        if (mobRef1 == null || mobRef2 == null)
            return false;
        if (TitanConfig.mobsUnited && mobRef1 instanceof IMob && mobRef2 instanceof IMob)
            return true;
        return (runFactions(mobRef1) & runFactions(mobRef2) & (PLAYER | ENDER_V | ZOMBIE | SKELETON | CREEPER | SPIDER_V | PIGZOMBIE_V | GHAST_V | BLAZE_V)) != 0;
    }

    public static boolean isFactionUser(Entity e) { return (runFactions(e) & PLAYER) != 0; }
    public static boolean isFactionEnder(Entity e) { return (runFactions(e) & ENDER_V) != 0; }
    public static boolean isFactionZombie(Entity e) { return (runFactions(e) & ZOMBIE) != 0; }
    public static boolean isFactionSkeleton(Entity e) { return (runFactions(e) & SKELETON) != 0; }
    public static boolean isFactionCreeper(Entity e) { return (runFactions(e) & CREEPER) != 0; }
    public static boolean isFactionSpider(Entity e) { return (runFactions(e) & SPIDER_V) != 0; }
    public static boolean isZombieFactionPig(Entity e) { return (runFactions(e) & PIGZOMBIE_V) != 0; }
    public static boolean isFactionGhast(Entity e) { return (runFactions(e) & GHAST_V) != 0; }
    public static boolean isFactionBlaze(Entity e) { return (runFactions(e) & BLAZE_V) != 0; }

    public static boolean isMutantA(Entity e) {
        return e instanceof EntityLivingBase && (mask(e) & MUTANT_V) != 0;
    }

    public static boolean isCreaturesBossMythical(Entity e) {
        return e instanceof EntityLivingBase && (mask(e) & CHIEF_MYTHICAL) != 0;
    }

    public static boolean isOreSpawnBossAn(Entity e) {
        if (!(e instanceof EntityLivingBase))
            return false;
        int m = mask(e);
        if ((m & CHIEF_OS) != 0)
            return true;
        // Original: Nightmare only with datawatcher 22 (scale*10) >= 20, Crab only at full size (datawatcher 21 == 100).
        if ((m & SIZED_CHIEF_OS) != 0) {
            boolean nightmare = e.getClass().getSimpleName().equals("PitchBlack");
            float scale = OreSpawnScale.get(e, nightmare ? "getPitchBlackScale" : "getCrabScale");
            return nightmare ? scale >= 2.0F : scale >= 0.995F;
        }
        return false;
    }

    /** Donor IBossDisplayData check: vanilla bossesV report !isNonBoss(); the mod's own deities/spirits/OG titans by class. */
    public static boolean bossIs(Entity e) {
        return !e.isNonBoss() || (mask(e) & CHIEF) != 0;
    }

    public static boolean quackosIs(Entity duck) {
        return duck instanceof EntityChicken && !duck.isNonBoss();
    }

    public static boolean isDudMobA(Entity e) {
        return !(e instanceof EntityLivingBase) || (mask(e) & DUD_V) != 0 || quackosIs(e);
    }

    /** Reflective, per-class cached scale getter for OreSpawn's size-dependent bossesV (no compile dep on OreSpawn). */
    private static final class OreSpawnScale {
        private static final ClassValue<Method[]> METHODS = new ClassValue<Method[]>() {
            @Override
            protected Method[] computeValue(Class<?> type) {
                Method[] found = new Method[1];
                for (String name : new String[] {"getPitchBlackScale", "getCrabScale"})
                    try { found[0] = type.getMethod(name); break; } catch (NoSuchMethodException ignored) {}
                return found;
            }
        };

        static float get(Entity e, String expected) {
            Method m = METHODS.get(e.getClass())[0];
            if (m == null || !m.getName().equals(expected))
                return 0.0F;
            try { return ((Number) m.invoke(e)).floatValue(); }
            catch (Exception ex) { return 0.0F; }
        }
    }
}
