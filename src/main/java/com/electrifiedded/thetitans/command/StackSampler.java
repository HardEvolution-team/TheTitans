package com.electrifiedded.thetitans.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Bench-only sampling profiler: samples the server thread every ms and reports the hottest stack prefixes. */
final class StackSampler {
    private StackSampler() {
    }

    private static volatile boolean running;
    private static final Map<String, Integer> HITS = new HashMap<>();
    private static int total;

    static void start(Thread target) {
        HITS.clear();
        total = 0;
        running = true;
        Thread t = new Thread(() -> {
            while (running) {
                StackTraceElement[] st = target.getStackTrace();
                StringBuilder sb = new StringBuilder();
                int n = 0;
                for (StackTraceElement e : st) {
                    if (n >= 7)
                        break;
                    sb.append(e.getClassName().replaceAll(".*\\.", "")).append('.').append(e.getMethodName()).append(':').append(e.getLineNumber()).append(" < ");
                    n++;
                }
                synchronized (HITS) {
                    HITS.merge(sb.toString(), 1, Integer::sum);
                    total++;
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ignored) {
                }
            }
        }, "TitanBench-sampler");
        t.setDaemon(true);
        t.start();
    }

    static List<String> stop() {
        running = false;
        List<String> out = new ArrayList<>();
        synchronized (HITS) {
            HITS.entrySet().stream().sorted((a, b) -> b.getValue() - a.getValue()).limit(25)
                    .forEach(e -> out.add(String.format("sample %.1f%% %s", e.getValue() * 100.0 / Math.max(1, total), e.getKey())));
        }
        return out;
    }
}
