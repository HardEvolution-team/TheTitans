package com.electrifiedded.thetitans.handler;

import com.electrifiedded.thetitans.TheTitans;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChunkLoadHooks implements LoadingCallback {
    public static ChunkLoadHooks instance;
    public static Map<Entity, Ticket> poolTicket = new HashMap<Entity, Ticket>();
    public static Map<Entity, Long> poolCenter = new HashMap<Entity, Long>();
    public static boolean reportedIssueHas = false;

    public static void init() {
        instance = new ChunkLoadHooks();
        MinecraftForge.EVENT_BUS.register(instance);
        ForgeChunkManager.setForcedChunkLoadingCallback(TheTitans.modInstance, instance);
    }

    public static void refreshLoaded(Entity mob) {
        int cx = MathHelper.floor(mob.posX) >> 4;
        int cz = MathHelper.floor(mob.posZ) >> 4;
        long center = ChunkPos.asLong(cx, cz);
        Long previous = poolCenter.get(mob);
        if (previous != null && previous.longValue() == center)
            return;
        // PERF — keep the ticket and only (un)force the chunks that changed. Releasing and re-requesting the whole
        // 5x5 every chunk step churned the forced set, and forcing never-generated chunks made a titan that blinked
        // or flew into fresh terrain generate 25 chunks synchronously (multi-second server stalls). Chunks that
        // don't exist yet are skipped; they get forced once something else (players, the titan walking in) made them.
        poolCenter.put(mob, center);
        Ticket ticket = poolTicket.get(mob);
        if (ticket == null) {
            ticket = ForgeChunkManager.requestTicket(TheTitans.modInstance, mob.world, ForgeChunkManager.Type.ENTITY);
            if (ticket == null)
                return;
            ticket.bindEntity(mob);
            ticket.setChunkListDepth(25);
            poolTicket.put(mob, ticket);
        }
        for (ChunkPos old : com.google.common.collect.ImmutableSet.copyOf(ticket.getChunkList()))
            if (Math.abs(old.x - cx) > 2 || Math.abs(old.z - cz) > 2)
                ForgeChunkManager.unforceChunk(ticket, old);
        net.minecraft.world.gen.ChunkProviderServer provider = mob.world instanceof net.minecraft.world.WorldServer ? ((net.minecraft.world.WorldServer)mob.world).getChunkProvider() : null;
        for (int xx = cx - 2; xx <= cx + 2; xx++)
            for (int zz = cz - 2; zz <= cz + 2; zz++) {
            ChunkPos pos = new ChunkPos(xx, zz);
            if (ticket.getChunkList().contains(pos))
                continue;
            if (provider != null && (xx != cx || zz != cz) && provider.getLoadedChunk(xx, zz) == null && !provider.isChunkGeneratedAt(xx, zz))
                continue;
            ForgeChunkManager.forceChunk(ticket, pos);
        }
    }

    /** Releases the entity's ticket and forgets it (called when a titan/spirit is removed, so maps don't leak it). */
    public static void haltLoading(Entity guardian) {
        poolCenter.remove(guardian);
        Ticket ticket = poolTicket.remove(guardian);
        if (ticket != null)
            ForgeChunkManager.releaseTicket(ticket);
    }

    public void ticketsLoaded(List<Ticket> tickets, World world) {
        if (!tickets.isEmpty())
            for (Ticket ticket : tickets)
            ForgeChunkManager.releaseTicket(ticket);
    }
}
