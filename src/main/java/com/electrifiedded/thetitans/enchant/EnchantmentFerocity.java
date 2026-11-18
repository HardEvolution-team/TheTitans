package com.electrifiedded.thetitans.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

public class EnchantmentFerocity extends Enchantment {
    public EnchantmentFerocity(int p_i45768_1_, ResourceLocation p_i45768_2_, int p_i45768_3_, EnumEnchantmentType weapon) {
        super(EnchHelper.rarity(p_i45768_3_), weapon, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
        setRegistryName(p_i45768_2_);
        setName("ferocity");
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
        return "enchantment.turretferocity";
    }

    public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType) {
        return level * 2.0F;
    }
}
