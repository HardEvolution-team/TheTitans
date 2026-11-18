package com.electrifiedded.thetitans.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

public class EnchantmentUnstablility extends Enchantment {
    public EnchantmentUnstablility(int p_i45768_1_, ResourceLocation p_i45768_2_, int p_i45768_3_, EnumEnchantmentType weapon) {
        super(EnchHelper.rarity(p_i45768_3_), weapon, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
        setRegistryName(p_i45768_2_);
        setName("unstablility");
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return enchantmentLevel * 15;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }

    public int getMaxLevel() {
        return 2;
    }

    public String getName() {
        return "enchantment.turretunstable";
    }

    public void onEntityDamaged(EntityLivingBase user, Entity target, int level) {
        boolean flag = user.world.getGameRules().getBoolean("mobGriefing");
        user.world.createExplosion(user, target.posX, target.posY, target.posZ, 1.0F + 0.75F * level, flag);
    }
}
