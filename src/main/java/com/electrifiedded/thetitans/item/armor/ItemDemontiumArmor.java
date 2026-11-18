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

public class ItemDemontiumArmor	extends ItemTitanArmor {
    public ItemDemontiumArmor(String materialName, ItemArmor.ArmorMaterial material, int type, float armourGiant, float resistanceGiant) {
        super(materialName, material, type, new double[] {
            5000.0D, 8000.0D, 7000.0D, 4000.0D
        }

        , armourGiant, resistanceGiant);
    }

    public boolean getIsRepairable(ItemStack arg82789n1, ItemStack arg82789n2) {
        return TitanGear.ingotDemontiumZ == arg82789n2.getItem();
    }

    public void whenTickAudio(World world, EntityLivingBase entity, ItemStack itemStack) {
        if (beatsAudio % 80 == 0)
            if (TheTitans.equippedAll(entity, TitanGear.armourSetDemontium))
            SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.25F, 1.30F, 119);
        super.whenTickAudio(world, entity, itemStack);
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (TheTitans.equipped(player, TitanGear.helmetDemontium, 1)) {
            appendLongEffect(player, MobEffects.NIGHT_VISION, 0);
            addEffect(player, MobEffects.WATER_BREATHING, 0);
            discardEffect(player, MobEffects.BLINDNESS);
        }

        else if (TheTitans.equipped(player, TitanGear.chestplateDemontium, 2)) {
            addEffect(player, MobEffects.HASTE, 3);
            addEffect(player, MobEffects.RESISTANCE, 3);
            addEffect(player, MobEffects.STRENGTH, 9);
            addEffect(player, MobEffects.FIRE_RESISTANCE, 0);
            discardEffect(player, MobEffects.WEAKNESS);
            discardEffect(player, MobEffects.MINING_FATIGUE);
        }

        else if (TheTitans.equipped(player, TitanGear.leggingsDemontium, 3)) {
            discardEffect(player, MobEffects.NAUSEA);
            discardEffect(player, MobEffects.HUNGER);
            discardEffect(player, MobEffects.POISON);
        }

        else if (TheTitans.equipped(player, TitanGear.bootsDemontium, 4)) {
            addEffect(player, MobEffects.JUMP_BOOST, 3);
            addEffect(player, MobEffects.SPEED, 3);
            discardEffect(player, MobEffects.SLOWNESS);
        }

        if (TheTitans.equippedAll(player, TitanGear.armourSetDemontium)) {
            player.extinguish();
            player.fallDistance *= 0.0F;
            if (player.getHealth() >= player.getMaxHealth() && player.getAbsorptionAmount() <= player.getHealth() * 0.5F)
                player.setAbsorptionAmount(player.getAbsorptionAmount() + 0.05F);
            addEffect(player, MobEffects.SATURATION, 29);
            if (player.isSneaking() && player.motionY > 0.0D)
                player.motionY -= player.motionY;
        }

        super.onArmorTick(world, player, itemStack);
    }
}
