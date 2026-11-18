package com.electrifiedded.thetitans.block.ore;

import com.electrifiedded.thetitans.registry.TitanBlockSet;
import com.electrifiedded.thetitans.registry.TitanGear;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockAdamantiumOre extends BlockNormalOre {
    public BlockAdamantiumOre(String name) {
        this(null, name);
    }

    public BlockAdamantiumOre(Block base, String name) {
        super(base, name, 0, -1.0F, 1000000000.0F);
        assignDroppedStack(TitanGear.adamantium);
        assignDropExp(20000, 100000);
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        return this == TitanBlockSet.oreAdamantiumZ ? false : super.canEntityDestroy(state, world, pos, entity);
    }
}
