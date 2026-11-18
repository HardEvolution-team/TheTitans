package com.electrifiedded.thetitans.item.misc;

import com.electrifiedded.thetitans.TheTitans;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemPleasantBladeFlower extends ItemFood {
    public ItemPleasantBladeFlower(int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
        this.setTranslationKey("pleasant_blade_flower");
        this.setRegistryName(new ResourceLocation(TheTitans.MODID_V, "pleasant_blade_flower"));
        this.setCreativeTab(TheTitans.pageTitans);
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entityLiving) {
        if (!world.isRemote && entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entityLiving;
            player.removePotionEffect(MobEffects.BLINDNESS);
            player.removePotionEffect(MobEffects.NAUSEA);
            player.removePotionEffect(MobEffects.SLOWNESS);
            player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 1000, 1));
            player.heal(2F);
        }

        return super.onItemUseFinish(stack, world, entityLiving);
    }

    public int getMaxItemUseDuration(ItemStack stack) {
        return 16;
    }

    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.EAT;
    }
}
