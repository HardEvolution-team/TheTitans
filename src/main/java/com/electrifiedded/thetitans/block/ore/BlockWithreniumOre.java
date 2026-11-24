package com.electrifiedded.thetitans.block.ore;

import com.electrifiedded.thetitans.registry.TitanGear;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockWithreniumOre extends BlockNormalOre {
    public BlockWithreniumOre(String name) {
        this(null, name);
    }

    public BlockWithreniumOre(Block base, String name) {
        super(base, name, 12, 50.0F, 2000.0F);
        assignDropExp(200, 500);
        assignDroppedStack(TitanGear.withrenium);
        setLightLevel(0.6F);
        setSoundType(SoundType.STONE);
    }

    private Random rand = new com.electrifiedded.archaiclib.util.FastRandom();

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        return false;
    }

    public int tickRate(World worldIn) {
        return 30;
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (getItemDropped(state, rand, 0) != null) {
            randomDisplayTick(state, worldIn, pos, rand);
        }
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        emitParticleDust(worldIn, pos);
        if (rand.nextInt(20) == 0) {
        }
    }

    private void emitParticleDust(World world, BlockPos pos) {
        Random random = world.rand;
        double d0 = 0.0625D;
        for (int l = 0; l < 6; l++) {
            double d1 = pos.getX() + random.nextFloat();
            double d2 = pos.getY() + random.nextFloat();
            double d3 = pos.getZ() + random.nextFloat();
            if ((l == 0) && (!world.getBlockState(pos.up()).isOpaqueCube())) {
                d2 = pos.getY() + 1 + d0;
            }

            if ((l == 1) && (!world.getBlockState(pos.down()).isOpaqueCube())) {
                d2 = pos.getY() + 0 - d0;
            }

            if ((l == 2) && (!world.getBlockState(pos.south()).isOpaqueCube())) {
                d3 = pos.getZ() + 1 + d0;
            }

            if ((l == 3) && (!world.getBlockState(pos.north()).isOpaqueCube())) {
                d3 = pos.getZ() + 0 - d0;
            }

            if ((l == 4) && (!world.getBlockState(pos.east()).isOpaqueCube())) {
                d1 = pos.getX() + 1 + d0;
            }

            if ((l == 5) && (!world.getBlockState(pos.west()).isOpaqueCube())) {
                d1 = pos.getX() + 0 - d0;
            }

            if ((d1 < pos.getX()) || (d1 > pos.getX() + 1) || (d2 < 0.0D) || (d2 > pos.getY() + 1) || (d3 < pos.getZ()) || (d3 > pos.getZ() + 1)) {
                world.spawnParticle(EnumParticleTypes.FLAME, d1, d2, d3, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
