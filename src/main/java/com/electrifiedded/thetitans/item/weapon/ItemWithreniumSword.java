package com.electrifiedded.thetitans.item.weapon;

import net.minecraft.item.Item;

public class ItemWithreniumSword extends ItemTitanSword {
    public ItemWithreniumSword(String unlocalizedName, Item.ToolMaterial material) {
        super(unlocalizedName, material);
    }

    protected double getRange() {
        return 8.0D;
    }

    protected double fetchPaceDash() {
        return 4.5D;
    }

    protected int fetchTicksHurt() {
        return 15;
    }

    protected int getMaxUses() {
        return 6;
    }
}
