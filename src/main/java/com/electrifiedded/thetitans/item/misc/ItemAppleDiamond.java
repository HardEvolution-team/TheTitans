package com.electrifiedded.thetitans.item.misc;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAppleDiamond extends ItemFood {
    public ItemAppleDiamond(int p_i45341_1_, float p_i45341_2_, boolean p_i45341_3_) {
        super(p_i45341_1_, p_i45341_2_, p_i45341_3_);
        this.setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack arg77636n1) {
        return arg77636n1.getItemDamage() > 0;
    }

    /**
     * Return an item rarity from EnumRarity
     */
    public EnumRarity getRarity(ItemStack arg77613n1) {
        return arg77613n1.getItemDamage() == 0 ? EnumRarity.RARE : EnumRarity.EPIC;
    }

    protected void onFoodEaten(ItemStack arg77849n1, World arg77849n2, EntityPlayer arg77849n3) {
        if (!arg77849n2.isRemote) {
            arg77849n3.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 6000, 9));
        }

        if (arg77849n1.getItemDamage() > 0) {
            if (!arg77849n2.isRemote) {
                arg77849n3.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 1200, 9));
                arg77849n3.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 3000, 2));
                arg77849n3.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 6000, 2));
                arg77849n3.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 6000, 0));
                arg77849n3.addPotionEffect(new PotionEffect(MobEffects.SPEED, 6000, 1));
                arg77849n3.addPotionEffect(new PotionEffect(MobEffects.HASTE, 6000, 1));
            }
        }

        else {
            if (!arg77849n2.isRemote) {
                arg77849n3.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 2));
                arg77849n3.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 2000, 0));
                arg77849n3.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 2000, 0));
                arg77849n3.addPotionEffect(new PotionEffect(MobEffects.SPEED, 2000, 0));
                arg77849n3.addPotionEffect(new PotionEffect(MobEffects.HASTE, 2000, 0));
            }
        }
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
    }
}
