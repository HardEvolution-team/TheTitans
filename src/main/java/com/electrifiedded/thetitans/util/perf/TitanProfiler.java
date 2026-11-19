package com.electrifiedded.thetitans.util.perf;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Test harness: cheap per-class / per-section server tick timer for titans. Disabled by default (one static boolean
 * read per hook). Enabled by /titanbench. Only touched from the server thread.
 */
public final class TitanProfiler {
    public enum Sec { ENTITY, TOTAL, LIVING, BOUNDS, AI, MOVE, ANIM }

    public static boolean enabled;
    private static final Map<Class<?>, long[]> DATA = new IdentityHashMap<>();
    private static int depth;

    private TitanProfiler() {}

    public static long start() {
        return enabled ? System.nanoTime() : 0L;
    }

    public static void stop(Class<?> cls, Sec sec, long t0) {
        if (!enabled || t0 == 0L)
            return;
        long dt = System.nanoTime() - t0;
        long[] d = DATA.computeIfAbsent(cls, c -> new long[Sec.values().length * 2]);
        d[sec.ordinal() * 2] += dt;
        d[sec.ordinal() * 2 + 1]++;
    }

    public static void reset() {
        DATA.clear();
    }

    /**Report lines: per class the average microseconds per call of every section, sorted by TOTAL cost.*/
    public static List<String> report(int ticks) {
        List<Map.Entry<Class<?>, long[]>> entries = new ArrayList<>(DATA.entrySet());
        entries.sort((a, b) -> Long.compare(b.getValue()[0], a.getValue()[0]));
        entries.removeIf(e -> e.getValue()[0] < 20_000L * Math.max(1, ticks)); // < 0.02 ms/tick: noise
        List<String> out = new ArrayList<>();
        long sum = 0;
        for (Map.Entry<Class<?>, long[]> e : entries) {
            long[] d = e.getValue();
            sum += d[0];
            StringBuilder sb = new StringBuilder(e.getKey().getSimpleName());
            sb.append(String.format(" share=%.3fms/tick", d[0] / 1e6 / Math.max(1, ticks)));
            for (Sec s : Sec.values()) {
                long n = d[s.ordinal() * 2 + 1];
                if (n > 0)
                    sb.append(String.format(" %s=%.1fus", s.name(), d[s.ordinal() * 2] / 1e3 / n));
            }
            out.add(sb.toString());
        }
        out.add(0, String.format("all entity ticks: %.3f ms/tick over %d ticks", sum / 1e6 / Math.max(1, ticks), ticks));
        return out;
    }
}
