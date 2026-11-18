package com.electrifiedded.thetitans.item.misc;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFoodMalgrum extends ItemFood {
    public ItemFoodMalgrum(int p_i45341_1_, float p_i45341_2_, boolean p_i45341_3_) {
        super(p_i45341_1_, p_i45341_2_, p_i45341_3_);
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack arg77636n1) {
        return true;
    }

    /**
     * Return an item rarity from EnumRarity
     */
    public EnumRarity getRarity(ItemStack arg77613n1) {
        return EnumRarity.EPIC;
    }

    protected void onFoodEaten(ItemStack arg77849n1, World arg77849n2, EntityPlayer arg77849n3) {
        if (!arg77849n2.isRemote) {
            EntityXPOrb orb = new EntityXPOrb(arg77849n2, arg77849n3.posX, arg77849n3.posY + 0.5D, arg77849n3.posZ, 5 + arg77849n3.getRNG().nextInt(10) + (int)arg77849n3.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue());
            arg77849n2.spawnEntity(orb);
            arg77849n3.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 6000, arg77849n3.isPotionActive(MobEffects.ABSORPTION) && arg77849n3.getActivePotionEffect(MobEffects.ABSORPTION).getAmplifier() > 9 ? arg77849n3.getActivePotionEffect(MobEffects.ABSORPTION).getAmplifier() + 9 : 9));
            arg77849n3.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(arg77849n3.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() + 40D);
            arg77849n3.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 2400, 9));
            arg77849n3.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 18000, 0));
            if (arg77849n3.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() > 100D)
                arg77849n3.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 18000, 1));
            if (arg77849n3.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() > 200D)
                arg77849n3.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 18000, 0));
            if (arg77849n3.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() > 300D)
                arg77849n3.addPotionEffect(new PotionEffect(MobEffects.SPEED, 18000, 3));
            if (arg77849n3.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() > 400D)
                arg77849n3.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 18000, 1));
            if (arg77849n3.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() > 600D)
                arg77849n3.addPotionEffect(new PotionEffect(MobEffects.HASTE, 18000, 1));
            if (arg77849n3.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() > 800D)
                arg77849n3.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 18000, 1));
            if (arg77849n3.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() > 1000D)
                arg77849n3.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 18000, 1));
            if (arg77849n3.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() > 1500D) {
                arg77849n3.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(arg77849n3.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue() + 1D);
                arg77849n3.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 18000, 3));
                arg77849n3.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 18000, 99));
                arg77849n3.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 18000, 99));
            }
        }
    }
}
