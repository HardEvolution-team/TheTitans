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
        Ticket ticket = poolTicket.remove(mob);
        if (ticket != null)
            ForgeChunkManager.releaseTicket(ticket);
        ticket = ForgeChunkManager.requestTicket(TheTitans.modInstance, mob.world, ForgeChunkManager.Type.ENTITY);
        poolCenter.put(mob, center);
        if (ticket == null)
            return;
        ticket.bindEntity(mob);
        ticket.setChunkListDepth(25);
        poolTicket.put(mob, ticket);
        for (int xx = cx - 2; xx <= cx + 2; xx++)
            for (int zz = cz - 2; zz <= cz + 2; zz++)
            ForgeChunkManager.forceChunk(ticket, new ChunkPos(xx, zz));
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
