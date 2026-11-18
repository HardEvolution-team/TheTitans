package com.electrifiedded.thetitans.item.armor;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemVoidArmor extends ItemTitanArmor {
    public ItemVoidArmor(String materialName, ItemArmor.ArmorMaterial material, int type, float armourGiant, float resistanceGiant) {
        super(materialName, material, type, new double[] {
            5.0E7D, 8.0E7D, 7.0E7D, 4.0E7D
        }

        , armourGiant, resistanceGiant);
    }

    public boolean getIsRepairable(ItemStack arg82789n1, ItemStack arg82789n2) {
        return TitanGear.stackVoid == arg82789n2.getItem();
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    public void whenTickAudio(World world, EntityLivingBase entity, ItemStack itemStack) {
        if (beatsAudio % 80 == 0)
            if (TheTitans.equippedAll(entity, TitanGear.armourSetVoid))
            SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.9F, 0.9F, 120);
        else {
            if (TheTitans.equipped(entity, TitanGear.helmetVoid, 1))
                SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.20F, 0.9F, 120);
            if (TheTitans.equipped(entity, TitanGear.chestplateVoid, 2))
                SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.20F, 0.9F, 120);
            if (TheTitans.equipped(entity, TitanGear.leggingsVoid, 3))
                SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.20F, 0.9F, 120);
            if (TheTitans.equipped(entity, TitanGear.bootsVoid, 4))
                SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.20F, 0.9F, 120);
        }

        super.whenTickAudio(world, entity, itemStack);
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (TheTitans.equipped(player, TitanGear.helmetVoid, 1)) {
            appendLongEffect(player, MobEffects.NIGHT_VISION, 0);
            addEffect(player, MobEffects.WATER_BREATHING, 0);
            discardEffect(player, MobEffects.BLINDNESS);
        }

        if (TheTitans.equipped(player, TitanGear.chestplateVoid, 2)) {
            addEffect(player, MobEffects.HASTE, 99);
            addEffect(player, MobEffects.RESISTANCE, 3);
            addEffect(player, MobEffects.STRENGTH, 49);
            addEffect(player, MobEffects.FIRE_RESISTANCE, 0);
            discardEffect(player, MobEffects.WEAKNESS);
            discardEffect(player, MobEffects.MINING_FATIGUE);
            if (player.isBurning())
                player.extinguish();
        }

        if (TheTitans.equipped(player, TitanGear.leggingsVoid, 3)) {
            discardEffect(player, MobEffects.NAUSEA);
            discardEffect(player, MobEffects.HUNGER);
            discardEffect(player, MobEffects.POISON);
        }

        if (TheTitans.equipped(player, TitanGear.bootsVoid, 4)) {
            addEffect(player, MobEffects.JUMP_BOOST, 5);
            addEffect(player, MobEffects.SPEED, 19);
            discardEffect(player, MobEffects.SLOWNESS);
            player.stepHeight = 2F;
        }

        if (TheTitans.equippedAll(player, TitanGear.armourSetVoid)) {
            player.extinguish();
            player.fallDistance *= 0.0F;
            if (player.getHealth() >= player.getMaxHealth() && player.getAbsorptionAmount() <= player.getHealth() * 0.5F)
                player.setAbsorptionAmount(player.getAbsorptionAmount() + player.getHealth() * 1.0E-6F);
            addEffect(player, MobEffects.SATURATION, 99);
            if (player.isSneaking() && player.motionY > 0.0D)
                player.motionY -= player.motionY;

            for (int i = 0; i < 4; i++)
                player.world.spawnParticle(EnumParticleTypes.SUSPENDED_DEPTH, player.posX + (player.getRNG().nextDouble() - 0.5D) * player.width * 2.0D, player.posY - 1.75D + player.getRNG().nextDouble() * player.height, player.posZ + (player.getRNG().nextDouble() - 0.5D) * player.width * 2.0D, 0.0D, 0.05D, 0.0D);

            List<?> found11 = player.world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(3.0D, 3.0D, 3.0D), e -> e != player);
            if ((found11 != null) && (!found11.isEmpty())) {
                for (int i1 = 0; i1 < found11.size(); i1++) {
                    Entity entity = (Entity)found11.get(i1);
                    if (entity instanceof EntityLivingBase && !TheTitans.checkFriendlyFire(player, entity, false)
                        && entity.attackEntityFrom(DamageSource.MAGIC, 4F)) {
                        addEffect((EntityLivingBase)entity, MobEffects.BLINDNESS, 5000, 1);
                        addEffect((EntityLivingBase)entity, MobEffects.NAUSEA, 5000, 1);
                        addEffect((EntityLivingBase)entity, MobEffects.SLOWNESS, 5000, 9);
                        addEffect((EntityLivingBase)entity, MobEffects.WITHER, 5000, 3);
                    }
                }
            }
        }

        super.onArmorTick(world, player, itemStack);
    }
}
