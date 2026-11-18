package com.electrifiedded.thetitans.item.misc;

import com.electrifiedded.thetitans.registry.TitanBlockSet;
import com.electrifiedded.thetitans.registry.TitanAudio;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class ItemMalgrumSeeds extends ItemBase implements IPlantable {
    public ItemMalgrumSeeds() {
        super("malgrum_seeds");
    }

    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (facing != EnumFacing.UP)
            return EnumActionResult.FAIL;
        if (player.canPlayerEdit(pos, facing, stack) && player.canPlayerEdit(pos.up(), facing, stack)) {
            if (world.getBlockState(pos).getBlock().canSustainPlant(world.getBlockState(pos), world, pos, EnumFacing.UP, this) && world.isAirBlock(pos.up())) {
                world.playSound((EntityPlayer)null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, com.electrifiedded.thetitans.registry.TitanAudio.get("thetitans:titanPress"), SoundCategory.BLOCKS, 0.5F, 1.0F);
                world.setBlockState(pos.up(), TitanBlockSet.cropMalgrum.getDefaultState(), 3);
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
        return TitanBlockSet.cropMalgrum.getDefaultState();
    }
}
