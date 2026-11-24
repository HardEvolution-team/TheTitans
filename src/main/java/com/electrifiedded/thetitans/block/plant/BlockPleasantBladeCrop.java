package com.electrifiedded.thetitans.block.plant;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.registry.TitanGear;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockPleasantBladeCrop extends BlockCrops {
    public BlockPleasantBladeCrop() {
        super();
        setTranslationKey("pleasant_blade");
        setRegistryName(new ResourceLocation(TheTitans.MODID_V, "pleasant_blade"));
    }

    @Override
    protected Item getSeed() {
        return TitanGear.bladeSeedPleasant;
    }

    @Override
    protected Item getCrop() {
        return TitanGear.bladeFlowerPleasant;
    }


    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        int l = getAge(state);
        if (l >= getMaxAge() && worldIn.getLightFromNeighbors(pos.up()) <= 7) {
            worldIn.setBlockState(pos, withAge(l - 1), 3);
            this.checkAndDropBlock(worldIn, pos, worldIn.getBlockState(pos));
        }

        else
            super.updateTick(worldIn, pos, state, rand);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        this.tryFlowerHarvest(worldIn, pos, playerIn, hand);
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        this.tryFlowerHarvest(worldIn, pos, playerIn, EnumHand.MAIN_HAND);
        super.onBlockClicked(worldIn, pos, playerIn);
    }

    private void tryFlowerHarvest(World world, BlockPos pos, EntityPlayer player, EnumHand hand) {
        int l = getAge(world.getBlockState(pos));
        if (l >= getMaxAge()) {
            player.swingArm(hand);
            if (!world.isRemote) {
                EntityItem entityitem = new EntityItem(world, player.posX, player.posY + 1, player.posZ, new ItemStack(getCrop()));
                world.spawnEntity(entityitem);
                world.setBlockState(pos, withAge(6), 2);
            }
        }
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        super.getDrops(drops, world, pos, state, fortune);
        int metadata = getAge(state);
        Random rand = world instanceof World ? ((World)world).rand : new com.electrifiedded.archaiclib.util.FastRandom();
        if (metadata >= 6) {
            for (int i = 0; i < 1 + rand.nextInt(3) + fortune; ++i) {
                drops.add(new ItemStack(TitanGear.bladeLeafPleasant, 1, 0));
            }
        }

        if (metadata >= 7) {
            for (int i = 0; i < 1 + rand.nextInt(3) + fortune; ++i) {
                drops.add(new ItemStack(TitanGear.bladeSeedPleasant, 1, 0));
            }
        }
    }
}
