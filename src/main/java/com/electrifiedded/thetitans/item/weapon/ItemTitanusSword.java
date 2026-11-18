package com.electrifiedded.thetitans.item.weapon;

import net.minecraft.item.Item;

public class ItemTitanusSword extends ItemTitanSword {
    public ItemTitanusSword(String unlocalizedName, Item.ToolMaterial material) {
        super(unlocalizedName, material);
    }

    protected double getRange() {
        return 8.0D;
    }

    protected double fetchPaceDash() {
        return 5.0D;
    }

    protected int fetchTicksUse() {
        return 10;
    }

    protected int fetchTicksHurt() {
        return 10;
    }

    protected int getMaxUses() {
        return 6;
    }
}
