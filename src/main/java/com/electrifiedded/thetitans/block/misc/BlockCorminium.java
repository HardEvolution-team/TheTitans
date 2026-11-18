package com.electrifiedded.thetitans.block.misc;

import com.electrifiedded.thetitans.TheTitans;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockCorminium extends Block {
    private boolean brokeV;

    public BlockCorminium(Material materialIn, String name) {
        super(materialIn);
        brokeV = false;
        setHarvestLevel("pickaxe", Integer.MAX_VALUE - 1);
        setBlockUnbreakable();
        setResistance(Float.MAX_VALUE);
        setCreativeTab(TheTitans.blocksPageGiant);
        setTranslationKey(name);
        setRegistryName(new ResourceLocation(TheTitans.MODID_V, name));
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        return false;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!brokeV) {
            worldIn.setBlockState(pos, state);
            List<?> list = worldIn.loadedEntityList;
            if (list != null && !list.isEmpty())
                for (int i = 0 ; i < list.size(); i++)
                if (list.get(i) instanceof EntityItem) {
                EntityItem entity = (EntityItem)list.get(i);
                if (!entity.getItem().isEmpty() && entity.getItem().getItem() == Item.getItemFromBlock(this))
                    entity.setDead();
            }
        }

        else
            brokeV = false;
    }

    @Override
    public void onPlayerDestroy(World worldIn, BlockPos pos, IBlockState state) {
        EntityPlayer player = worldIn.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10.0D, false);
        if (player != null && player.capabilities.isCreativeMode)
            brokeV = true;
        else
            brokeV = false;
    }

    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return false;
    }
}
