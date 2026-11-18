package com.electrifiedded.thetitans.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

public class EnchantmentKnockup extends Enchantment {
    public EnchantmentKnockup(int p_i45768_1_, ResourceLocation p_i45768_2_, int p_i45768_3_) {
        super(EnchHelper.rarity(p_i45768_3_), EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
        setRegistryName(p_i45768_2_);
    }

    public String getName() {
        return "enchantment.knockup";
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 5 + 20 * (enchantmentLevel - 1);
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }

    public int getMaxLevel() {
        return 2;
    }

    public void onEntityDamaged(EntityLivingBase user, Entity target, int level) {
        target.motionY += 1.0F * level;
    }
}
