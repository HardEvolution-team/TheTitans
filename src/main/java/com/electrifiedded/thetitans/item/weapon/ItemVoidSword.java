package com.electrifiedded.thetitans.item.weapon;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemVoidSword extends ItemTitanSword {
    public ItemVoidSword(String unlocalizedName, Item.ToolMaterial material) {
        super(unlocalizedName, material);
    }

    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    protected double getRange() {
        return 9.0D;
    }

    protected double fetchPaceDash() {
        return 5.0D;
    }

    public int fetchTicksUse() {
        return 10;
    }

    public int fetchTicksHurt() {
        return 10;
    }

    protected int getMaxUses() {
        return 7;
    }
}
