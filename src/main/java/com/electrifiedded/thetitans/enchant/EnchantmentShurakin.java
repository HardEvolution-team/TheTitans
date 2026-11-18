package com.electrifiedded.thetitans.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

public class EnchantmentShurakin extends Enchantment {
    public EnchantmentShurakin(int p_i45768_1_, ResourceLocation p_i45768_2_, int p_i45768_3_, EnumEnchantmentType weapon) {
        super(EnchHelper.rarity(p_i45768_3_), weapon, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
        setRegistryName(p_i45768_2_);
        setName("shurakin");
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 20;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return 50;
    }

    public int getMaxLevel() {
        return 1;
    }

    public String getName() {
        return "enchantment.turretshurakin";
    }

    public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType) {
        return level * 1.5F;
    }
}
