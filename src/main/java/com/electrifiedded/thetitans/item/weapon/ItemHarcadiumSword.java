package com.electrifiedded.thetitans.item.weapon;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemHarcadiumSword extends ItemTitanSword {
    public ItemHarcadiumSword(String unlocalizedName, Item.ToolMaterial material) {
        super(unlocalizedName, material);
    }

    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }
}
