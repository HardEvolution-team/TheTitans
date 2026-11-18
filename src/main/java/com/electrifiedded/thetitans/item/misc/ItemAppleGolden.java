package com.electrifiedded.thetitans.item.misc;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// golden-food variant, mirrors ItemAppleDiamond; 1.7.10 setPotionEffect builder chain replaced with onFoodEaten
public class ItemAppleGolden extends ItemFood {
    public ItemAppleGolden(int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
        this.setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return stack.getItemDamage() > 0;
    }

    public EnumRarity getRarity(ItemStack stack) {
        return stack.getItemDamage() == 0 ? EnumRarity.UNCOMMON : EnumRarity.RARE;
    }

    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2400, 0));
            player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 1));
        }
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
    }
}
