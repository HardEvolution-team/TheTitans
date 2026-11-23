package com.electrifiedded.thetitans.command;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.util.perf.TitanProfiler;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

/**
 * Perf test harness. /titanbench <dormant|awake|none|profile> [ticks] [copies]
 * Measures baseline MSPT, spawns every titan type around world spawn (always loaded), warms up, then measures MSPT and
 * per-titan section timings (TitanProfiler). Results go to chat and to the log with the prefix [TitanBench].
 * "profile" only profiles the titans already in the world. Spawned titans are removed at the end.
 */
public class CommandTitanBench extends CommandBase {
    private static Run run;
    private static boolean registered;

    public String getName() {
        return "titanbench";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender sender) {
        return "/titanbench <dormant|awake|none|profile> [ticks] [copies]";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (run != null)
            throw new CommandException("bench already running");
        String mode = args.length > 0 ? args[0].toLowerCase() : "dormant"; // append +boss to include Witherzilla
        int ticks = args.length > 1 ? parseInt(args[1], 20, 20000) : 400;
        int copies = args.length > 2 ? parseInt(args[2], 1, 20) : 1;
        if (!registered) {
            MinecraftForge.EVENT_BUS.register(CommandTitanBench.class);
            registered = true;
        }
        run = new Run(server, sender, mode, ticks, copies);
        msg(run, "start mode=" + mode + " ticks=" + ticks + " copies=" + copies);
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, "dormant", "awake", "none", "profile", "mutant") : Collections.emptyList();
    }

    private static void msg(Run r, String s) {
        org.apache.logging.log4j.LogManager.getLogger("TitanBench").info("[TitanBench] " + s);
        try {
            r.sender.sendMessage(new TextComponentString("[TitanBench] " + s));
        } catch (Throwable ignored) {
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent e) {
        if (run == null || e.phase != TickEvent.Phase.START)
            return;
        Run r = run;
        long last = r.server.tickTimeArray[(r.server.getTickCounter() + 99) % 100];
        r.step++;
        if (last > 150_000_000L)
            msg(r, String.format("BIG spike stage %d step %d: %.1f ms", r.stage, r.step, last / 1e6));
        r.server.getWorld(0).resetUpdateEntityTick(); // keep entities ticking without players
        if (r.stage == 0) {                       // baseline
            if (r.step > 5) { r.baseNs += last; r.baseN++; }
            if (r.baseN >= Math.min(r.ticks, 200)) {
                if (!r.mode.startsWith("profile") && !r.mode.startsWith("none"))
                    spawnAll(r);
                r.stage = 1;
                r.step = 0;
            }
        } else if (r.stage == 1) {                // warm-up
            if (r.step >= 60) {
                TitanProfiler.reset();
                TitanProfiler.enabled = true;
                StackSampler.start(Thread.currentThread());
                r.stage = 2;
                r.step = 0;
            }
        } else if (r.stage == 2) {                // measure
            if (r.mode.startsWith("mutant"))
                MutantBench.tick(r.server.getWorld(0), r.spawned, r.step, s -> msg(r, s));
            if (r.step > 1) {
                r.loadNs += last; r.loadN++;
                r.maxNs = Math.max(r.maxNs, last);
                if (last > 40_000_000L)
                    msg(r, String.format("spike step %d: %.1f ms", r.step, last / 1e6));
            }
            if (r.loadN >= r.ticks) {
                TitanProfiler.enabled = false;
                for (String line : StackSampler.stop())
                    msg(r, line);
                msg(r, String.format("baseline %.3f ms/tick, with titans %.3f ms/tick (max %.3f), delta %.3f ms, titans=%d",
                    r.baseNs / 1e6 / r.baseN, r.loadNs / 1e6 / r.loadN, r.maxNs / 1e6, (r.loadNs / (double)r.loadN - r.baseNs / (double)r.baseN) / 1e6, r.spawned.size()));
                for (String line : TitanProfiler.report(r.loadN))
                    msg(r, line);
                java.util.Map<String, Integer> census = new java.util.TreeMap<>();
                for (Entity en : r.server.getWorld(0).loadedEntityList)
                    census.merge(en.getClass().getSimpleName(), 1, Integer::sum);
                msg(r, "entities in world: " + census);
                if (r.mode.startsWith("mutant"))
                    MutantBench.report(r.spawned, s -> msg(r, s));
                for (Entity en : r.spawned)
                    en.setDead();
                msg(r, "done");
                run = null;
            }
        }
    }

    private static void spawnAll(Run r) {
        WorldServer world = r.server.getWorld(0);
        BlockPos spawn = world.getSpawnPoint();
        List<EntityEntry> types = new ArrayList<>();
        for (EntityEntry entry : ForgeRegistries.ENTITIES.getValuesCollection())
            if (EntityTitan.class.isAssignableFrom(entry.getEntityClass()) && !Modifier.isAbstract(entry.getEntityClass().getModifiers())
                && (r.withBoss || entry.getEntityClass() != com.electrifiedded.thetitans.entity.titan.EntityWitherzilla.class)
                && (!r.mode.startsWith("mutant") || com.electrifiedded.thetitans.entity.titan.mutant.AIMutantTitanAnim.Host.class.isAssignableFrom(entry.getEntityClass())))
            if (r.mode.indexOf('=') < 0 || entry.getEntityClass().getSimpleName().toLowerCase().contains(r.mode.substring(r.mode.indexOf('=') + 1).replace("+terrain", "").replace("+death", "").replace("+death", "")))
                types.add(entry);
        MutantBench.KILL = r.mode.contains("death");
        if (r.mode.contains("terrain")) {
            net.minecraft.block.state.IBlockState stone = net.minecraft.init.Blocks.STONE.getDefaultState();
            for (int x = -90; x <= 90; x++)
                for (int z = -90; z <= 90; z++)
                    for (int y = 4; y < 40; y++)
                        world.setBlockState(spawn.add(x, y - spawn.getY(), z), stone, 2);
            msg(r, "terrain filled");
            MutantBench.FAR = true;
        }
        if (r.mode.startsWith("blast")) {
            String rad = r.mode.substring(r.mode.indexOf('=') + 1).replace("+terrain", "");
            com.electrifiedded.archaiclib.world.ChunkBlast b = com.electrifiedded.archaiclib.world.ChunkBlast.of(world, spawn.getX(), 20, spawn.getZ(), Double.parseDouble(rad)).start();
            final long t0 = System.nanoTime();
            b.onDone = () -> msg(r, String.format("blast r=%s done: %d chunks in %.1f s", rad, b.chunksCarved(), (System.nanoTime() - t0) / 1e9));
            return;
        }
        int i = 0;
        for (int c = 0; c < r.copies; c++)
            for (EntityEntry entry : types) {
            // spiral-ish grid inside the always-loaded spawn area
            int gx = (i % 5) - 2, gz = (i / 5) % 5 - 2;
            if (r.mode.startsWith("mutant")) { gx *= 3; gz *= 3; } // mutant titans are huge: keep them apart
            i++;
            int x = spawn.getX() + gx * 24 + c, z = spawn.getZ() + gz * 24 + c;
            int y = world.getHeight(x, z);
            try {
                Entity en = entry.newInstance(world);
                en.setLocationAndAngles(x + 0.5, y, z + 0.5, world.rand.nextFloat() * 360F, 0F);
                if (en instanceof EntityTitan) {
                    ((EntityTitan)en).onInitialSpawn(world.getDifficultyForLocation(new BlockPos(en)), null);
                    ((EntityTitan)en).assignWaiting(r.mode.startsWith("dormant"));
                }
                if (world.spawnEntity(en)) {
                    r.spawned.add(en);
                    if (r.mode.startsWith("mutant"))
                        r.spawned.add(MutantBench.target(world, (EntityTitan)en));
                }
            } catch (Throwable t) {
                msg(r, "spawn failed " + entry.getRegistryName() + ": " + t);
            }
        }
        msg(r, "spawned " + r.spawned.size() + " titans of " + types.size() + " types");
    }

    private static final class Run {
        final MinecraftServer server;
        final ICommandSender sender;
        final String mode;
        final int ticks, copies;
        final boolean withBoss;
        int stage, step, baseN, loadN;
        long baseNs, loadNs, maxNs;
        final List<Entity> spawned = new ArrayList<>();

        Run(MinecraftServer server, ICommandSender sender, String mode, int ticks, int copies) {
            this.server = server;
            this.sender = sender;
            this.mode = mode;
            this.ticks = ticks;
            this.copies = copies;
            this.withBoss = mode.endsWith("+boss");
        }
    }
}
