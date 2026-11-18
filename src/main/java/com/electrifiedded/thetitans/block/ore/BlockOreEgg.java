package com.electrifiedded.thetitans.block.ore;

import com.electrifiedded.thetitans.TheTitans;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockOreEgg extends Block {
    public BlockOreEgg(String name) {
        super(Material.GROUND);
        setHardness(0.5F);
        setResistance(1.0F);
        setSoundType(SoundType.GROUND);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setTranslationKey(name);
        setRegistryName(new ResourceLocation(TheTitans.MODID_V, name));
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        int j1 = 5 + worldIn.rand.nextInt(3) + worldIn.rand.nextInt(3);
        if (worldIn.rand.nextInt(2) == 1) {
            dropXpOnBlockBreak(worldIn, pos, j1);
        }
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return true;
    }
}
