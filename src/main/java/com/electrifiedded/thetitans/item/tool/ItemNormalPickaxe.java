package com.electrifiedded.thetitans.item.tool;

import com.electrifiedded.thetitans.TheTitans;

import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.util.ResourceLocation;

public class ItemNormalPickaxe extends ItemPickaxe {
    public ItemNormalPickaxe(String unlocalizedName, Item.ToolMaterial material) {
        super(material);
        setTranslationKey(unlocalizedName + "_pickaxe");
        setRegistryName(new ResourceLocation(TheTitans.MODID_V, unlocalizedName + "_pickaxe"));
        setCreativeTab(TheTitans.toolsPageGiant);
    }
}
