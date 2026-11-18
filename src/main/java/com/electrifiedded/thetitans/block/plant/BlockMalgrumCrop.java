package com.electrifiedded.thetitans.block.plant;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.registry.TitanGear;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockMalgrumCrop extends BlockCrops {
    public BlockMalgrumCrop() {
        super();
        setTranslationKey("malgrum");
        setRegistryName(new ResourceLocation(TheTitans.MODID_V, "malgrum"));
    }

    @Override
    protected Item getSeed() {
        return TitanGear.seedsMalgrum;
    }

    @Override
    protected Item getCrop() {
        return TitanGear.malgrum;
    }


    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        int l = getAge(state);
        if (l < getMaxAge()) {
            if (rand.nextInt(2 * (1 + l)) == 0) {
                worldIn.setBlockState(pos, withAge(l + 1), 2);
            }
        }
    }
}
