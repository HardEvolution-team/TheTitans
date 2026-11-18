package com.electrifiedded.thetitans.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

public class EnchantmentDurability extends Enchantment {
    public EnchantmentDurability(int p_i45768_1_, ResourceLocation p_i45768_2_, int p_i45768_3_, EnumEnchantmentType weapon) {
        super(EnchHelper.rarity(p_i45768_3_), weapon, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
        setRegistryName(p_i45768_2_);
        setName("durability");
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 5 + (enchantmentLevel - 1) * 8;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }

    public int getMaxLevel() {
        return 5;
    }

    public String getName() {
        return "enchantment.turretdurability";
    }

    public static boolean negateDamage(ItemStack arg92097n0, int arg92097n1, Random arg92097n2) {
        return (!(arg92097n0.getItem() instanceof ItemArmor)) || (arg92097n2.nextFloat() >= 0.6F);
    }
}
