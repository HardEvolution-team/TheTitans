package com.electrifiedded.thetitans.item.misc;

import com.electrifiedded.thetitans.entity.titan.EntityGrowthSerum;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemGrowthSerum extends ItemBase {
    public ItemGrowthSerum() {
        super("growth_serum");
        this.maxStackSize = 16;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        if (!playerIn.capabilities.isCreativeMode) {
            itemStackIn.shrink(1);
        }

        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
        if (!worldIn.isRemote) {
            worldIn.spawnEntity(new EntityGrowthSerum(worldIn, playerIn));
        }

        net.minecraft.stats.StatBase s = net.minecraft.stats.StatList.getObjectUseStats(this);
        if (s != null) playerIn.addStat(s);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
    }
}
