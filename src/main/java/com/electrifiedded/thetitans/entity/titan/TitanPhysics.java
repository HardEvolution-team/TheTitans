package com.electrifiedded.thetitans.entity.titan;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * PERF — titan bodies are 24x96x24+ blocks, so vanilla Entity.move() walks ~55k block positions per
 * collision query (and runs 2-3 queries per tick for step-up), plus a full isFlammableWithin() scan. Almost all of
 * that volume is air. Hooked from mixin.MixinWorld: titans get a collision gatherer that skips empty 16^3 chunk
 * sections, and the fire/lava scan is skipped while a titan is moving (titans are fire immune anyway).
 * Results are identical to World.getCollisionBoxes(Entity, AxisAlignedBB) for non-debug worlds.
 */
public final class TitanPhysics {
    private static int serverMoving;
    private static int clientMoving;

    private TitanPhysics() {}

    static void moveEnter(boolean remote) {
        if (remote) clientMoving++; else serverMoving++;
    }

    static void moveExit(boolean remote) {
        if (remote) clientMoving--; else serverMoving--;
    }

    /**True while a titan is inside Entity.move() on that logical side.*/
    public static boolean isTitanMoving(boolean remote) {
        return remote ? clientMoving > 0 : serverMoving > 0;
    }

    public interface BlockVisitor {
        void visit(BlockPos pos, IBlockState state);
    }

    /**
     * Calls the visitor for every non-air block inside the AABB (same inclusive floor() bounds the titan block-destroy
     * loops always used), skipping unloaded chunks and empty chunk sections. The pos passed in is a fresh immutable
     * BlockPos, so visitors may keep it. Blocks may be changed from inside the visitor.
     */
    public static void forCubeEach(World world, AxisAlignedBB box, BlockVisitor visitor) {
        int minX = MathHelper.floor(box.minX), maxX = MathHelper.floor(box.maxX);
        int minY = Math.max(MathHelper.floor(box.minY), 0), maxY = Math.min(MathHelper.floor(box.maxY), world.getHeight() - 1);
        int minZ = MathHelper.floor(box.minZ), maxZ = MathHelper.floor(box.maxZ);
        if (minY > maxY)
            return;
        for (int cx = minX >> 4; cx <= maxX >> 4; cx++)
            for (int cz = minZ >> 4; cz <= maxZ >> 4; cz++) {
            Chunk chunk = world.getChunkProvider().getLoadedChunk(cx, cz);
            if (chunk == null)
                continue;
            ExtendedBlockStorage[] storage = chunk.getBlockStorageArray();
            int x0 = Math.max(minX, cx << 4), x1 = Math.min(maxX, (cx << 4) + 15);
            int z0 = Math.max(minZ, cz << 4), z1 = Math.min(maxZ, (cz << 4) + 15);
            for (int sy = minY >> 4; sy <= maxY >> 4 && sy < storage.length; sy++) {
                ExtendedBlockStorage section = storage[sy];
                if (section == Chunk.NULL_BLOCK_STORAGE || section.isEmpty())
                    continue;
                int y0 = Math.max(minY, sy << 4), y1 = Math.min(maxY, (sy << 4) + 15);
                for (int y = y0; y <= y1; y++)
                    for (int x = x0; x <= x1; x++)
                    for (int z = z0; z <= z1; z++) {
                    // The visitor only ever clears blocks, so the section object stays valid (cleared cells read as air).
                    IBlockState state = section.get(x & 15, y & 15, z & 15);
                    if (state.getBlock() != Blocks.AIR)
                        visitor.visit(new BlockPos(x, y, z), state);
                }
            }
        }
    }

    /**@return the collision boxes, or null to fall back to vanilla.*/
    public static List<AxisAlignedBB> getCollisionBoxes(World world, Entity entity, AxisAlignedBB aabb) {
        if (world.getWorldType() == WorldType.DEBUG_ALL_BLOCK_STATES)
            return null;

        List<AxisAlignedBB> list = new ArrayList<AxisAlignedBB>();
        // Same bounds as vanilla (one block of margin for fences/walls), x/z/y upper bounds exclusive.
        int minX = MathHelper.floor(aabb.minX) - 1;
        int maxX = MathHelper.ceil(aabb.maxX) + 1;
        int minY = MathHelper.floor(aabb.minY) - 1;
        int maxY = MathHelper.ceil(aabb.maxY) + 1;
        int minZ = MathHelper.floor(aabb.minZ) - 1;
        int maxZ = MathHelper.ceil(aabb.maxZ) + 1;
        WorldBorder border = world.getWorldBorder();
        boolean outside = entity.isOutsideBorder();
        boolean inside = world.isInsideWorldBorder(entity);
        if (outside == inside)
            entity.setOutsideBorder(!inside);
        boolean checkBorder = inside && !border.contains(new AxisAlignedBB(minX, 0.0D, minZ, maxX, 1.0D, maxZ));
        IBlockState stone = Blocks.STONE.getDefaultState();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        int yFrom = Math.max(minY, 0);
        int yTo = Math.min(maxY, world.getHeight());

        if (yFrom < yTo)
            for (int cx = minX >> 4; cx <= (maxX - 1) >> 4; cx++)
            for (int cz = minZ >> 4; cz <= (maxZ - 1) >> 4; cz++) {
            Chunk chunk = world.getChunkProvider().getLoadedChunk(cx, cz);
            if (chunk == null)
                continue;
            ExtendedBlockStorage[] storage = chunk.getBlockStorageArray();
            int x0 = Math.max(minX, cx << 4), x1 = Math.min(maxX, (cx << 4) + 16);
            int z0 = Math.max(minZ, cz << 4), z1 = Math.min(maxZ, (cz << 4) + 16);
            for (int sy = yFrom >> 4; sy <= (yTo - 1) >> 4 && sy < storage.length; sy++) {
                ExtendedBlockStorage section = storage[sy];
                boolean empty = section == Chunk.NULL_BLOCK_STORAGE || section.isEmpty();
                if (empty && !checkBorder)
                    continue;
                int y0 = Math.max(yFrom, sy << 4), y1 = Math.min(yTo, (sy << 4) + 16);
                for (int x = x0; x < x1; x++) {
                    boolean edgeX = x == minX || x == maxX - 1;
                    for (int z = z0; z < z1; z++) {
                        boolean edgeZ = z == minZ || z == maxZ - 1;
                        if (edgeX && edgeZ)
                            continue;
                        for (int y = y0; y < y1; y++) {
                            if ((edgeX || edgeZ) && y == maxY - 1)
                                continue;
                            pos.setPos(x, y, z);
                            IBlockState state;
                            if (checkBorder && !border.contains(pos))
                                state = stone;
                            else if (empty)
                                continue;
                            else {
                                state = section.get(x & 15, y & 15, z & 15);
                                if (state.getBlock() == Blocks.AIR)
                                    continue;
                            }

                            state.addCollisionBoxToList(world, pos, aabb, list, entity, false);
                        }
                    }
                }
            }
        }

        List<Entity> others = world.getEntitiesWithinAABBExcludingEntity(entity, aabb.grow(0.25D));
        for (Entity other : others) {
            if (!entity.isRidingSameEntity(other)) {
                AxisAlignedBB box = other.getCollisionBoundingBox();
                if (box != null && box.intersects(aabb))
                    list.add(box);
                box = entity.getCollisionBox(other);
                if (box != null && box.intersects(aabb))
                    list.add(box);
            }
        }

        MinecraftForge.EVENT_BUS.post(new GetCollisionBoxesEvent(world, entity, aabb, list));
        return list;
    }
}
