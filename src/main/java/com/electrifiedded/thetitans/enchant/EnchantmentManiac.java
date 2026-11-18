package com.electrifiedded.thetitans.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class EnchantmentManiac extends Enchantment {
    public EnchantmentManiac(int p_i45768_1_, ResourceLocation p_i45768_2_, int p_i45768_3_, EnumEnchantmentType weapon) {
        super(EnchHelper.rarity(p_i45768_3_), weapon, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
        setRegistryName(p_i45768_2_);
        setName("maniac");
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 12 + (enchantmentLevel - 1) * 20;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }

    public int getMaxLevel() {
        return 3;
    }

    public String getName() {
        return "enchantment.turretmaniac";
    }

    public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType) {
        return level * 3.0F;
    }

    public void onEntityDamaged(EntityLivingBase user, Entity target, int level) {
        user.addPotionEffect(new PotionEffect(MobEffects.SPEED, 20 * (10 * level), level));
        user.addPotionEffect(new PotionEffect(MobEffects.HASTE, 20 * (10 * level), level));
    }
}
