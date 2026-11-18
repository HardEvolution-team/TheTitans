package com.electrifiedded.thetitans.item.tool;

import com.electrifiedded.thetitans.TheTitans;

import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.util.ResourceLocation;

public class ItemNormalAxe extends ItemAxe {
    public ItemNormalAxe(String unlocalizedName, Item.ToolMaterial material) {
        // 1.12.2 ItemAxe(ToolMaterial) indexes a length-5 damage array by material.ordinal(); custom EnumHelper materials overflow it. Use the explicit (material, damage, speed) ctor.
        super(material, material.getAttackDamage() + 6.0F, -3.1F);
        setTranslationKey(unlocalizedName + "_axe");
        setRegistryName(new ResourceLocation(TheTitans.MODID_V, unlocalizedName + "_axe"));
        setCreativeTab(TheTitans.toolsPageGiant);
    }
}
