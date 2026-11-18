package com.electrifiedded.thetitans.block.storage;

import com.electrifiedded.thetitans.TheTitans;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockCompactBedrock extends Block {
    public BlockCompactBedrock(Material materialIn, String name) {
        super(materialIn);
        setHarvestLevel("pickaxe", 10000000);
        setHardness(90000.0F);
        setResistance(5.4E7F);
        setCreativeTab(TheTitans.blocksPageGiant);
        setTranslationKey(name);
        setRegistryName(new ResourceLocation(TheTitans.MODID_V, name));
        setSoundType(SoundType.STONE);
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        return false;
    }
}
