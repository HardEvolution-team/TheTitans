package com.electrifiedded.thetitans.util.perf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/** Dev-only (-Dthetitans.hitch=true): logs client ticks slower than 150 ms with the hottest stacks sampled during them. */
public final class HitchWatch {
    private final Thread target;
    private volatile List<String> cur = new ArrayList<>();
    private long last;

    private HitchWatch(Thread target) {
        this.target = target;
    }

    public static void install() {
        if (!Boolean.getBoolean("thetitans.hitch"))
            return;
        HitchWatch w = new HitchWatch(Thread.currentThread());
        MinecraftForge.EVENT_BUS.register(w);
        Thread t = new Thread(w::sample, "TitanHitch-sampler");
        t.setDaemon(true);
        t.start();
    }

    private void sample() {
        while (true) {
            StackTraceElement[] st = target.getStackTrace();
            StringBuilder sb = new StringBuilder();
            int n = 0;
            for (StackTraceElement e : st) {
                String c = e.getClassName();
                if (n == 0 && (c.startsWith("java.") || c.startsWith("jdk.") || c.startsWith("sun.") || c.startsWith("org.lwjgl")) && sb.length() > 200)
                    continue;
                sb.append(c.substring(c.lastIndexOf('.') + 1)).append('.').append(e.getMethodName()).append(':').append(e.getLineNumber()).append(" < ");
                if (++n >= 14)
                    break;
            }
            List<String> l = cur;
            synchronized (l) {
                l.add(sb.toString());
            }
            try {
                Thread.sleep(2);
            } catch (InterruptedException ignored) {
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.START)
            return;
        long now = System.nanoTime();
        List<String> l = cur;
        cur = new ArrayList<>();
        long gap = (now - last) / 1_000_000L;
        last = now;
        if (gap < 150 || gap > 60_000)
            return;
        Map<String, Integer> hits = new HashMap<>();
        synchronized (l) {
            for (String s : l)
                hits.merge(s, 1, Integer::sum);
        }
        System.out.println("[TitanHitch] client tick gap " + gap + " ms, samples " + l.size());
        hits.entrySet().stream().sorted((a, b) -> b.getValue() - a.getValue()).limit(6)
                .forEach(en -> System.out.println("[TitanHitch]   " + en.getValue() + "x " + en.getKey()));
    }
}
