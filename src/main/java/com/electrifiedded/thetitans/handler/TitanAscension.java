package com.electrifiedded.thetitans.handler;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.config.TitanConfig;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.network.NetworkHandler;
import com.electrifiedded.thetitans.network.packet.PacketWorldData;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldServer;
import net.minecraft.world.end.DragonFightManager;
import net.minecraftforge.common.DimensionManager;

/**
 * Pre-titan progression: which mob kills unlock the titans (config "progression"), and waking dormant titans.
 * <p>
 * BUGFIX "titans don't wake right after the Wither + Ender Dragon":
 * <ul>
 * <li>the wake loop only scanned the world where the last boss died — kill the dragon last and only titans in the
 *     End were woken; overworld statues stayed asleep;</li>
 * <li>each statue then waited for a player within 16-24 blocks of its centre (and only while isStateGiant, so they
 *     never woke at all once TitanComplete was set);</li>
 * <li>a dragon killed via /kill or by another mod's damage path never fires LivingDeathEvent.</li>
 * </ul>
 * Kills are recorded per registry id ("Killed:minecraft:wither"); the legacy PostDragon/PostWither flags still count.
 */
public final class TitanAscension {
    private static final String KILLED_V = "Killed:";
    private static final String DRAGON_V = "minecraft:ender_dragon";
    private static final String WITHER = "minecraft:wither";

    private TitanAscension() {}

    /**True once titans may wake (pre-titan stage finished). Server side only.*/
    public static boolean isUnlocked(World world) {
        return !EventStore.isGiantStatePre(world);
    }

    /**Records a kill if the mob is one of the configured awakening mobs, then unlocks if the condition is met.*/
    public static void whenKilledMob(Entity entity) {
        if (entity == null || entity.world.isRemote)
            return;
        ResourceLocation id = EntityList.getKey(entity);
        if (id == null)
            return;
        World world = entity.world;
        String key = id.toString();
        boolean firstKill = !killedIs(world, key);
        if (entity instanceof EntityDragon && !EventStore.getBool(world, "PostDragon")) {
            setFlag(world, "PostDragon");
            TheTitans.debug("The world is now post ender dragon");
            EventStore.sendMessage(world, TheTitans.translateMult("event", "pretitan.postdragon", 5));
        }

        else if (entity instanceof EntityWither && !EventStore.getBool(world, "PostWither")) {
            setFlag(world, "PostWither");
            TheTitans.debug("The world is now post wither");
            EventStore.sendMessage(world, TheTitans.translateMult("event", "pretitan.postwither", 5));
        }

        if (firstKill && isMobAwakening(key)) {
            setFlag(world, KILLED_V + key);
            TheTitans.debug("Awakening mob killed: " + key);
        }

        verifyUnlock(world);
    }

    /**Unlocks the titans if the configured kill condition is met and they aren't unlocked yet.*/
    public static void verifyUnlock(World world) {
        if (world == null || world.isRemote || isUnlocked(world) || !isConditionMet(world))
            return;
        setFlag(world, "PreTitanComplete");
        TheTitans.debug("The world can now spawn and awaken titans");
        EventStore.sendMessage(world, TheTitans.translateMult("event", "pretitan.complete", 5));
        if (TitanConfig.wakeSpanGiant <= 0)
            allWake(world.getMinecraftServer());
    }

    /**Called from the server tick: catches kills that bypass LivingDeathEvent (dragon /kill, saves from older versions).*/
    public static void tickServer(MinecraftServer server) {
        if (server == null)
            return;
        WorldServer overworld = DimensionManager.getWorld(0);
        if (overworld == null || isUnlocked(overworld))
            return;
        if (isMobAwakening(DRAGON_V) && !killedIs(overworld, DRAGON_V))
            for (WorldServer world : DimensionManager.getWorlds())
            if (world.provider instanceof WorldProviderEnd) {
            DragonFightManager fight = ((WorldProviderEnd)world.provider).getDragonFightManager();
            if (fight != null && fight.hasPreviouslyKilledDragon()) {
                setFlag(overworld, "PostDragon");
                setFlag(overworld, KILLED_V + DRAGON_V);
            }
        }
        verifyUnlock(overworld);
    }

    /**Wakes every loaded dormant titan in every dimension.*/
    public static void allWake(MinecraftServer server) {
        if (server == null)
            return;
        int woken = 0;
        for (WorldServer world : DimensionManager.getWorlds())
            for (Entity entity : world.getEntities(EntityTitan.class, titan -> titan != null && titan.fetchWaiting())) {
            ((EntityTitan)entity).awaken();
            woken++;
        }
        TheTitans.debug("Woke " + woken + " dormant titans");
    }

    public static boolean isConditionMet(World world) {
        boolean any = false;
        boolean all = true;
        boolean hasEntries = false;
        for (String raw : TitanConfig.mobsAwakening) {
            String key = normalize(raw);
            if (key.isEmpty())
                continue;
            hasEntries = true;
            if (killedIs(world, key))
                any = true;
            else
                all = false;
        }

        if (!hasEntries)
            return true;
        return TitanConfig.requiresAllAwakening ? all : any;
    }

    public static boolean killedIs(World world, String key) {
        if (EventStore.getBool(world, KILLED_V + key))
            return true;
        if (DRAGON_V.equals(key))
            return EventStore.getBool(world, "PostDragon");
        if (WITHER.equals(key))
            return EventStore.getBool(world, "PostWither");
        return false;
    }

    private static boolean isMobAwakening(String key) {
        for (String raw : TitanConfig.mobsAwakening)
            if (normalize(raw).equals(key))
            return true;
        return false;
    }

    private static String normalize(String raw) {
        if (raw == null)
            return "";
        String key = raw.trim().toLowerCase(java.util.Locale.ROOT);
        return key.isEmpty() || key.contains(":") ? key : "minecraft:" + key;
    }

    private static void setFlag(World world, String key) {
        if (EventStore.saveInstance == null)
            EventStore.saveInstance = TitanSavedData.fetchInfoStorage(world);
        EventStore.saveInstance.setBoolean(world, key, true);
        NetworkHandler.dispatchPacketClient(new PacketWorldData(key, true));
    }
}
