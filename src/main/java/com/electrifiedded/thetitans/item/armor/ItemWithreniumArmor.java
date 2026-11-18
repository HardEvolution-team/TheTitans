package com.electrifiedded.thetitans.item.armor;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.sound.SoundHandler;
import com.electrifiedded.thetitans.util.Maths;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemWithreniumArmor extends ItemTitanArmor {
    public ItemWithreniumArmor(String materialName, ItemArmor.ArmorMaterial material, int type, float armourGiant, float resistanceGiant) {
        super(materialName, material, type, new double[] {
            500000.0D, 800000.0D, 700000.0D, 400000.0D
        }

        , armourGiant, resistanceGiant);
    }

    public boolean getIsRepairable(ItemStack arg82789n1, ItemStack arg82789n2) {
        // MISSING DEP TitanGear.withrenium — add field when the withrenium items get registered
        return TitanGear.withrenium == arg82789n2.getItem();
    }

    public EnumRarity getRarity(ItemStack stack) {
        return TheTitans.godlyV;
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    public void whenTickAudio(World world, EntityLivingBase entity, ItemStack itemStack) {
        if (beatsAudio % 80 == 0)
            if (TheTitans.equippedAll(entity, TitanGear.armourSetWithrenium))
            SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.75F, 1.0F, 119);
        else {
            if (TheTitans.equipped(entity, TitanGear.helmetWithrenium, 1))
                SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.15F, 1.0F, 119);
            if (TheTitans.equipped(entity, TitanGear.chestplateWithrenium, 2))
                SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.15F, 1.0F, 119);
            if (TheTitans.equipped(entity, TitanGear.leggingsWithrenium, 3))
                SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.15F, 1.0F, 119);
            if (TheTitans.equipped(entity, TitanGear.bootsWithrenium, 4))
                SoundHandler.triggerAttatchedAudio(entity, "thetitans:harcadiumHum", 0.15F, 1.0F, 119);
        }

        super.whenTickAudio(world, entity, itemStack);
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (TheTitans.equipped(player, TitanGear.helmetWithrenium, 1)) {
            appendLongEffect(player, MobEffects.NIGHT_VISION, 0);
            addEffect(player, MobEffects.WATER_BREATHING, 0);
            discardEffect(player, MobEffects.BLINDNESS);
        }

        if (TheTitans.equipped(player, TitanGear.chestplateWithrenium, 2)) {
            addEffect(player, MobEffects.HASTE, 4);
            addEffect(player, MobEffects.RESISTANCE, 3);
            addEffect(player, MobEffects.STRENGTH, 14);
            addEffect(player, MobEffects.FIRE_RESISTANCE, 0);
            discardEffect(player, MobEffects.WEAKNESS);
            discardEffect(player, MobEffects.MINING_FATIGUE);
        }

        if (TheTitans.equipped(player, TitanGear.leggingsWithrenium, 3)) {
            discardEffect(player, MobEffects.NAUSEA);
            discardEffect(player, MobEffects.HUNGER);
            discardEffect(player, MobEffects.POISON);
        }

        if (TheTitans.equipped(player, TitanGear.bootsWithrenium, 4)) {
            addEffect(player, MobEffects.JUMP_BOOST, 4);
            addEffect(player, MobEffects.SPEED, 4);
            discardEffect(player, MobEffects.SLOWNESS);
            player.stepHeight = 2.0F;
        }

        if (TheTitans.equippedAll(player, TitanGear.armourSetWithrenium)) {
            if (player.isSneaking() && player.motionY > 0.0D)
                player.motionY -= player.motionY;
            if (player.getHealth() >= player.getMaxHealth() && player.getAbsorptionAmount() <= player.getHealth() * 0.5F)
                player.setAbsorptionAmount(player.getAbsorptionAmount() + 0.075F);
            addEffect(player, MobEffects.SATURATION, 49);
            discardEffect(player, MobEffects.WITHER);
            if (player.isBurning())
                player.extinguish();
            player.fallDistance *= 0.0F;
            if (Maths.chance(25))
                player.world.spawnParticle(EnumParticleTypes.FLAME, player.posX + (player.getRNG().nextDouble() - 0.5D) * player.width * 2.0D, player.posY - 1.75D + player.getRNG().nextDouble() * player.height, player.posZ + (player.getRNG().nextDouble() - 0.5D) * player.width * 2.0D, 0.0D, 0.05D, 0.0D);
            List<?> found11 = player.world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(4.0D, 4.0D, 4.0D), e -> e != player);
            if ((found11 != null) && (!found11.isEmpty())) {
                for (int i1 = 0; i1 < found11.size(); i1++) {
                    Entity entity = (Entity)found11.get(i1);
                    if (entity instanceof EntityLivingBase && !TheTitans.checkFriendlyFire(player, entity, false)) {
                        entity.setFire(30);
                        addEffect((EntityLivingBase)entity, MobEffects.WITHER, 500, 1);
                    }
                }
            }
        }

        super.onArmorTick(world, player, itemStack);
    }
}
