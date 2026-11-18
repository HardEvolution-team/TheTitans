package com.electrifiedded.thetitans.item.misc;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemPleasantBladeBrew extends ItemBase {
    public ItemPleasantBladeBrew() {
        super("pleasant_blade_brew");
    }

    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
        EntityPlayer player = entityLiving instanceof EntityPlayer ? (EntityPlayer)entityLiving : null;
        if (player == null || !player.capabilities.isCreativeMode) {
            stack.shrink(1);
        }

        if (!world.isRemote && player != null) {
            player.removePotionEffect(MobEffects.POISON);
            player.removePotionEffect(MobEffects.HUNGER);
            player.removePotionEffect(MobEffects.NAUSEA);
            player.removePotionEffect(MobEffects.WITHER);
        }

        return stack.getCount() <= 0 ? new ItemStack(Items.GLASS_BOTTLE) : stack;
    }

    public int getMaxItemUseDuration(ItemStack stack) {
        return 32;
    }

    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.DRINK;
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        player.setActiveHand(hand);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }
}
