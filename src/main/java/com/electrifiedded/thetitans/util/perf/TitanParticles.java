package com.electrifiedded.thetitans.util.perf;

import java.util.function.IntSupplier;

import net.minecraft.world.World;

/**
 * PERF — budget for the titans' AMBIENT particles (aura, portal cloud, smoke...).
 * 1.7.10 capped each particle layer at 4000, so the original look never had more than that on screen;
 * 1.12 allows 16384 per layer and the titans' huge per-tick loops (Ender Colossus: 300/tick) fill it,
 * which tanks FPS over time while TPS stays fine. Above the budget the ambient spawns are skipped,
 * so the look stays as in the original. Gameplay particles (hits, explosions) are not budgeted.
 */
public final class TitanParticles {
    /** ~1.7.10 per-layer cap. */
    public static final int BUDGET = 4000;
    /** Set by the client proxy; returns the live particle count. Null on a dedicated server. */
    public static IntSupplier counter;

    private static long cachedTick = Long.MIN_VALUE;
    private static int cachedCount;

    private TitanParticles() {
    }

    /** How many more ambient particles may be spawned this tick (server: unlimited, spawns are no-ops there). */
    public static int room(World world) {
        if (!world.isRemote || counter == null)
            return Integer.MAX_VALUE;
        long t = world.getTotalWorldTime();
        if (t != cachedTick) {
            cachedTick = t;
            cachedCount = counter.getAsInt();
        }
        return Math.max(0, BUDGET - cachedCount);
    }

    /** Ambient loop guard: true while still under budget; counts the spawn against the cached total. */
    public static boolean allow(World world) {
        if (!world.isRemote || counter == null)
            return true;
        if (room(world) <= 0)
            return false;
        cachedCount++;
        return true;
    }
}
