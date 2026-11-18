package com.electrifiedded.thetitans.enchant;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.handler.EventStore;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EnchantmentTitanSlayer extends Enchantment {
    public EnchantmentTitanSlayer(int enchID, ResourceLocation enchName, int enchWeight) {
        super(EnchHelper.rarity(enchWeight), EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
        setRegistryName(enchName);
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 1 + (enchantmentLevel - 1) * 3;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 30;
    }

    public int getMaxLevel() {
        return 10;
    }

    public String getName() {
        return "enchantment.damage.titanSlayer";
    }

    public boolean canApply(ItemStack stack) {
        return (stack.getItem() instanceof ItemAxe) ? true : super.canApply(stack);
    }

    public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType) {
        return 25F;
    }

    public void onEntityDamaged(EntityLivingBase user, Entity target, int level) {
        if ((target instanceof EntityLivingBase)) {
            EntityLivingBase entitylivingbase1 = (EntityLivingBase)target;
            float percentage = com.electrifiedded.thetitans.TheTitans.getBonusDamageVSGiantMobs(true) * (float)(1 + level);
            if ((target instanceof EntityTitan) || (target.getEyeHeight() >= 6.0F) || EntityTitan.isSpawnBossToExemptOre(target)) {
                com.electrifiedded.thetitans.handler.EventStore.damageEntity(entitylivingbase1, DamageSourceExtra.causeGiantHarmAnti(user), 25.0F * percentage);
                SoundHandler.whenPlayMob("thetitans:titanpunch", target.world, target, (float)level, 2.0F - (float)level * 0.1F);
            }
        }
    }
}
