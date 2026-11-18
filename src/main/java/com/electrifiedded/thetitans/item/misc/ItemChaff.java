package com.electrifiedded.thetitans.item.misc;

import com.electrifiedded.thetitans.entity.titan.EntityChaff;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemChaff extends ItemBase {
    public ItemChaff() {
        super("chaff");
        this.maxStackSize = 24;
    }

    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        EntityChaff mobRef1 = new EntityChaff(worldIn);
        EntityChaff mobRef2 = new EntityChaff(worldIn);
        if (!worldIn.isRemote) {
            mobRef1.setLocationAndAngles(playerIn.posX + 6.0D, playerIn.posY + 3.0D, playerIn.posZ + 6.0D, 0.0F, 0.0F);
            mobRef2.setLocationAndAngles(playerIn.posX - 6.0D, playerIn.posY + 3.0D, playerIn.posZ - 6.0D, 0.0F, 0.0F);
            worldIn.spawnEntity(mobRef1);
            worldIn.spawnEntity(mobRef2);
        }

        if (!playerIn.capabilities.isCreativeMode)
            stack.shrink(1);
        return EnumActionResult.SUCCESS;
    }
}
