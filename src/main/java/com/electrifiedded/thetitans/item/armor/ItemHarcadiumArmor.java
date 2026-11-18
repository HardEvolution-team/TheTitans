package com.electrifiedded.thetitans.item.armor;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanTrophies;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemHarcadiumArmor	extends ItemTitanArmor {
    public ItemHarcadiumArmor(String materialName, ItemArmor.ArmorMaterial material, int type, float armourGiant, float resistanceGiant) {
        super(materialName, material, type, new double[] {
            50000.0D, 80000.0D, 70000.0D, 40000.0D
        }

        , armourGiant, resistanceGiant);
    }

    public boolean getIsRepairable(ItemStack arg82789n1, ItemStack arg82789n2) {
        return TitanGear.harcadium == arg82789n2.getItem();
    }

    public net.minecraft.item.EnumRarity getRarity(ItemStack stack) {
        return TheTitans.epicSuper;
    }

    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    public void whenTickAudio(World world, EntityLivingBase entity, ItemStack itemStack) {
        if (beatsAudio % 80 == 0)
            if (TheTitans.equippedAll(entity, TitanGear.armourSetHarcadium))
            SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.4F, 1.25F, 119);
        else {
            if (TheTitans.equipped(entity, TitanGear.helmetHarcadium, 1))
                SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.1F, 1.25F, 119);
            if (TheTitans.equipped(entity, TitanGear.chestplateHarcadium, 2))
                SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.1F, 1.25F, 119);
            if (TheTitans.equipped(entity, TitanGear.leggingsHarcadium, 3))
                SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.1F, 1.25F, 119);
            if (TheTitans.equipped(entity, TitanGear.bootsHarcadium, 4))
                SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.1F, 1.25F, 119);
        }

        super.whenTickAudio(world, entity, itemStack);
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (TheTitans.equipped(player, TitanGear.helmetHarcadium, 1)) {
            appendLongEffect(player, MobEffects.NIGHT_VISION, 0);
            addEffect(player, MobEffects.WATER_BREATHING, 0);
            discardEffect(player, MobEffects.BLINDNESS);
        }

        else if (TheTitans.equipped(player, TitanGear.chestplateHarcadium, 2)) {
            addEffect(player, MobEffects.HASTE, 3);
            addEffect(player, MobEffects.RESISTANCE, 3);
            addEffect(player, MobEffects.STRENGTH, 9);
            addEffect(player, MobEffects.FIRE_RESISTANCE, 0);
            discardEffect(player, MobEffects.WEAKNESS);
            discardEffect(player, MobEffects.MINING_FATIGUE);
        }

        else if (TheTitans.equipped(player, TitanGear.leggingsHarcadium, 3)) {
            discardEffect(player, MobEffects.NAUSEA);
            discardEffect(player, MobEffects.HUNGER);
            discardEffect(player, MobEffects.POISON);
        }

        else if (TheTitans.equipped(player, TitanGear.bootsHarcadium, 4)) {
            addEffect(player, MobEffects.JUMP_BOOST, 7);
            addEffect(player, MobEffects.SPEED, 9);
            discardEffect(player, MobEffects.SLOWNESS);
        }

        if (TheTitans.equippedAll(player, TitanGear.armourSetHarcadium)) {
            player.extinguish();
            player.fallDistance *= 0.0F;
            if (player.getHealth() >= player.getMaxHealth() && player.getAbsorptionAmount() <= player.getHealth() * 0.5F)
                player.setAbsorptionAmount(player.getAbsorptionAmount() + 0.05F);
            addEffect(player, MobEffects.SATURATION, 49);
            if (player.isSneaking() && player.motionY > 0.0D)
                player.motionY -= player.motionY;
        }

        super.onArmorTick(world, player, itemStack);
    }
}
