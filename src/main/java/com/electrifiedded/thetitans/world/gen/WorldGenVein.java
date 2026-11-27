package com.electrifiedded.thetitans.world.gen;

import net.minecraft.block.Block;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldGenVein extends WorldGenerator {
    private Block ore;
    /** The number of blocks to generate. */
    private int amount;
    private final List<Block> bases = new ArrayList<Block>();
    private int cubeMetaMineable;

    public WorldGenVein(Block ore, int amount) {
        this(ore, amount, Blocks.STONE);
    }

    public WorldGenVein(Block ore, int amount, Block... bases) {
        this.ore = ore;
        this.amount = amount;
        for (Block block : bases)
            this.bases.add(block);
    }

    public WorldGenVein(Block block, int meta, int amount, Block... bases) {
        this(block, amount, bases);
        cubeMetaMineable = meta;
    }

    public Block getOre() {
        return ore;
    }

    public boolean generate(World world, Random rand, BlockPos pos) {
        int posX = pos.getX();
        int posY = pos.getY();
        int posZ = pos.getZ();
        float f = rand.nextFloat() * (float)Math.PI;
        double d0 = (double)((float)(posX + 8) + MathHelper.sin(f) * (float)amount / 8.0F);
        double d1 = (double)((float)(posX + 8) - MathHelper.sin(f) * (float)amount / 8.0F);
        double d2 = (double)((float)(posZ + 8) + MathHelper.cos(f) * (float)amount / 8.0F);
        double d3 = (double)((float)(posZ + 8) - MathHelper.cos(f) * (float)amount / 8.0F);
        double d4 = (double)(posY + rand.nextInt(3) - 2);
        double d5 = (double)(posY + rand.nextInt(3) - 2);
        net.minecraft.block.state.pattern.BlockMatcher matchers[] = new net.minecraft.block.state.pattern.BlockMatcher[bases.size()];
        for (int bi = 0; bi < bases.size(); bi++)
            matchers[bi] = BlockMatcher.forBlock(bases.get(bi));
        final net.minecraft.block.state.IBlockState oreState = ore.getStateFromMeta(cubeMetaMineable);
        final int nb = bases.size();
        final BlockPos.MutableBlockPos mp = new BlockPos.MutableBlockPos();
        net.minecraft.world.chunk.Chunk chunk = null;
        final boolean direct = oreState.getLightValue() == 0 && oreState.getLightOpacity() >= 255 && !ore.hasTileEntity(oreState);
        for (int l = 0; l <= amount; ++l) {
            double d6 = d0 + (d1 - d0) * (double)l / (double)amount;
            double d7 = d4 + (d5 - d4) * (double)l / (double)amount;
            double d8 = d2 + (d3 - d2) * (double)l / (double)amount;
            double d9 = rand.nextDouble() * (double)amount / 16.0D;
            double d10 = (double)(MathHelper.sin((float)l * (float)Math.PI / (float)amount) + 1.0F) * d9 + 1.0D;
            double d11 = (double)(MathHelper.sin((float)l * (float)Math.PI / (float)amount) + 1.0F) * d9 + 1.0D;
            int i1 = MathHelper.floor(d6 - d10 / 2.0D);
            int j1 = MathHelper.floor(d7 - d11 / 2.0D);
            int k1 = MathHelper.floor(d8 - d10 / 2.0D);
            int l1 = MathHelper.floor(d6 + d10 / 2.0D);
            int i2 = MathHelper.floor(d7 + d11 / 2.0D);
            if (j1 < 0) j1 = 0;
            if (i2 > 255) i2 = 255;
            int j2 = MathHelper.floor(d8 + d10 / 2.0D);
            for (int k2 = i1; k2 <= l1; ++k2) {
                double d12 = ((double)k2 + 0.5D - d6) / (d10 / 2.0D);
                if (d12 * d12 < 1.0D)
                    for (int l2 = j1; l2 <= i2; ++l2) {
                    double d13 = ((double)l2 + 0.5D - d7) / (d11 / 2.0D);
                    if (d12 * d12 + d13 * d13 < 1.0D)
                        for (int i3 = k1; i3 <= j2; ++i3) {
                        double d14 = ((double)i3 + 0.5D - d8) / (d10 / 2.0D);
                        if (d12 * d12 + d13 * d13 + d14 * d14 >= 1.0D)
                            continue;
                        mp.setPos(k2, l2, i3);
                        int ccx = k2 >> 4, ccz = i3 >> 4;
                        if (chunk == null || ccx != chunk.x || ccz != chunk.z) {
                            if (!world.isBlockLoaded(mp, false)) continue;
                            chunk = world.getChunk(ccx, ccz);
                        }
                        net.minecraft.block.state.IBlockState targetState = chunk.getBlockState(mp);
                        if (targetState.getBlock() == oreState.getBlock() && targetState == oreState) continue;
                        for (int bi = 0; bi < nb; bi++)
                            if (targetState.getBlock().isReplaceableOreGen(targetState, world, mp, matchers[bi])) {
                            if (direct && !chunk.isPopulated()) {
                                // chunk not visible to players yet: write the section in place (same light/opacity as stone)
                                net.minecraft.world.chunk.storage.ExtendedBlockStorage sec = chunk.getBlockStorageArray()[l2 >> 4];
                                if (sec != net.minecraft.world.chunk.Chunk.NULL_BLOCK_STORAGE) { sec.set(k2 & 15, l2 & 15, i3 & 15, oreState); chunk.markDirty(); }
                            } else world.setBlockState(mp.toImmutable(), oreState, 2);
                            break;
                        }
                    }
                }
            }
        }

        return true;
    }
}
