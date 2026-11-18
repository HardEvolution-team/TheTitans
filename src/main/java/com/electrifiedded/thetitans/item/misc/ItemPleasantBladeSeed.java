package com.electrifiedded.thetitans.item.misc;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.registry.TitanBlockSet;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class ItemPleasantBladeSeed extends Item implements IPlantable {
    public ItemPleasantBladeSeed() {
        setTranslationKey("pleasant_blade_seed");
        setRegistryName(new ResourceLocation(TheTitans.MODID_V, "pleasant_blade_seed"));
        setCreativeTab(TheTitans.pageTitans);
    }

    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (facing != EnumFacing.UP)
            return EnumActionResult.FAIL;
        if (player.canPlayerEdit(pos, facing, stack) && player.canPlayerEdit(pos.up(), facing, stack)) {
            if (world.getBlockState(pos).getBlock().canSustainPlant(world.getBlockState(pos), world, pos, EnumFacing.UP, this) && world.isAirBlock(pos.up())) {
                world.setBlockState(pos.up(), TitanBlockSet.bladeCropPleasant.getDefaultState(), 3);
                stack.shrink(1);
                return EnumActionResult.SUCCESS;
            }

            return EnumActionResult.FAIL;
        }

        return EnumActionResult.FAIL;
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Crop;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return TitanBlockSet.bladeCropPleasant.getDefaultState();
    }
}
