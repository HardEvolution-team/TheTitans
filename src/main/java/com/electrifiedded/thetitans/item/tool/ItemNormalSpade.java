package com.electrifiedded.thetitans.item.tool;

import com.electrifiedded.thetitans.TheTitans;

import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.util.ResourceLocation;

public class ItemNormalSpade extends ItemSpade {
    public ItemNormalSpade(String unlocalizedName, Item.ToolMaterial material) {
        super(material);
        setTranslationKey(unlocalizedName + "_spade");
        setRegistryName(new ResourceLocation(TheTitans.MODID_V, unlocalizedName + "_spade"));
        setCreativeTab(TheTitans.toolsPageGiant);
    }
}
