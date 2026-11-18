package com.electrifiedded.thetitans.item.armor;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTitanusArmor extends ItemTitanArmor {
    public ItemTitanusArmor(String materialName, ItemArmor.ArmorMaterial material, int type, float armourGiant, float resistanceGiant) {
        super(materialName, material, type, new double[] {
            5000000.0D, 8000000.0D, 7000000.0D, 4000000.0D
        }

        , armourGiant, resistanceGiant);
    }

    public boolean getIsRepairable(ItemStack arg82789n1, ItemStack arg82789n2) {
        // MISSING DEP TitanGear.ingotTitanusZ — add field when the titanus items get registered
        return TitanGear.ingotTitanusZ == arg82789n2.getItem();
    }

    public EnumRarity getRarity(ItemStack stack) {
        return TheTitans.godlyV;
    }

    public void whenTickAudio(World world, EntityLivingBase entity, ItemStack itemStack) {
        if (beatsAudio % 80 == 0)
            if (TheTitans.equippedAll(entity, TitanGear.armourSetTitanus))
            SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.9F, 0.95F, 120);
        else {
            if (TheTitans.equipped(entity, TitanGear.helmetTitanus, 1))
                SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.2F, 1.1F, 120);
            if (TheTitans.equipped(entity, TitanGear.chestplateTitanus, 2))
                SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.2F, 1.1F, 120);
            if (TheTitans.equipped(entity, TitanGear.leggingsTitanus, 3))
                SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.2F, 1.1F, 120);
            if (TheTitans.equipped(entity, TitanGear.bootsTitanus, 4))
                SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.2F, 1.1F, 120);
        }

        super.whenTickAudio(world, entity, itemStack);
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (TheTitans.equipped(player, TitanGear.helmetTitanus, 1)) {
            appendLongEffect(player, MobEffects.NIGHT_VISION, 0);
            addEffect(player, MobEffects.WATER_BREATHING, 0);
            discardEffect(player, MobEffects.BLINDNESS);
        }

        if (TheTitans.equipped(player, TitanGear.chestplateTitanus, 2)) {
            addEffect(player, MobEffects.HASTE, 3);
            addEffect(player, MobEffects.RESISTANCE, 3);
            addEffect(player, MobEffects.STRENGTH, 9);
            addEffect(player, MobEffects.FIRE_RESISTANCE, 0);
            discardEffect(player, MobEffects.WEAKNESS);
            discardEffect(player, MobEffects.MINING_FATIGUE);
        }

        if (TheTitans.equipped(player, TitanGear.leggingsTitanus, 3)) {
            discardEffect(player, MobEffects.NAUSEA);
            discardEffect(player, MobEffects.HUNGER);
            discardEffect(player, MobEffects.POISON);
        }

        if (TheTitans.equipped(player, TitanGear.bootsTitanus, 4)) {
            addEffect(player, MobEffects.JUMP_BOOST, 7);
            addEffect(player, MobEffects.SPEED, 9);
            discardEffect(player, MobEffects.SLOWNESS);
        }

        if (TheTitans.equippedAll(player, TitanGear.armourSetTitanus)) {
            if (player.isBurning())
                player.extinguish();
            player.fallDistance *= 0.0F;
            if (player.getHealth() >= player.getMaxHealth() && player.getAbsorptionAmount() <= player.getHealth() * 0.5F)
                player.setAbsorptionAmount(player.getAbsorptionAmount() + 0.05F);
            addEffect(player, MobEffects.SATURATION, 79);
            if (player.isSneaking() && player.motionY > 0.0D)
                player.motionY -= player.motionY;
        }

        super.onArmorTick(world, player, itemStack);
    }
}
