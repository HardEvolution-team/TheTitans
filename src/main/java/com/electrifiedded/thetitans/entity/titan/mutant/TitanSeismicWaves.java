package com.electrifiedded.thetitans.entity.titan.mutant;

import java.util.ArrayDeque;
import java.util.Deque;

import com.electrifiedded.thetitans.compat.mutant.MutantTitans;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * The Mutant Zombie's seismic wave, titan sized: a line of ground ruptures travelling away from the impact,
 * one rupture per tick, each hurting/launching what stands on it and tearing up the top blocks.
 */
public class TitanSeismicWaves {
    private final EntityTitan titan;
    private final Deque<double[]> pending = new ArrayDeque<>();

    public TitanSeismicWaves(EntityTitan titan) {
        this.titan = titan;
    }

    /**
     * Queues a wave from the titan's feet along (dirX,dirZ) (normalized) starting {@code start} blocks out,
     * {@code count} ruptures {@code spacing} blocks apart, each of radius {@code radius}.
     */
    public void line(double dirX, double dirZ, double start, int count, double spacing, double radius, double damage) {
        double y = titan.getEntityBoundingBox().minY;
        for (int i = 0; i < count; i++) {
            double d = start + i * spacing;
            pending.add(new double[] { titan.posX + dirX * d, y, titan.posZ + dirZ * d, radius * (i == 0 ? 1.4D : 1.0D), damage * (i == 0 ? 1.5D : 1.0D) });
        }
    }

    /** Queues a ring of ruptures around the titan (stomps / landings). */
    public void ring(double radius, int count, double ruptureRadius, double damage) {
        double y = titan.getEntityBoundingBox().minY;
        for (int i = 0; i < count; i++) {
            double a = Math.PI * 2.0D * i / count;
            pending.add(new double[] { titan.posX + Math.cos(a) * radius, y, titan.posZ + Math.sin(a) * radius, ruptureRadius, damage });
        }
    }

    public boolean isEmpty() {
        return pending.isEmpty();
    }

    /** Server tick: resolves up to {@code perTick} ruptures. */
    public void tick(int perTick) {
        for (int n = 0; n < perTick && !pending.isEmpty(); n++) {
            double[] w = pending.poll();
            double x = w[0], y = w[1], z = w[2], r = w[3];
            MutantTitans.strikeBox(titan, new AxisAlignedBB(x - r, y - 1.0D, z - r, x + r, y + r * 1.2D, z + r), w[4], 2, 0.6D);
            MutantTitans.groundBurst(titan.world, x, y, z, 40);
            if (titan.world.getGameRules().getBoolean("mobGriefing"))
                titan.destroyBlocksInAABB(new AxisAlignedBB(x - r * 0.5D, y, z - r * 0.5D, x + r * 0.5D, y + 1.0D, z + r * 0.5D));
            if (n == 0)
                titan.world.playSound(null, x, y, z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 4.0F, 0.5F + titan.getRNG().nextFloat() * 0.3F);
        }
    }
}
