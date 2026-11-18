package com.electrifiedded.thetitans.item.misc;

import com.electrifiedded.thetitans.entity.misc.EntityImmortalItem;
import com.electrifiedded.thetitans.registry.TitanTrophies;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemAdamantium extends ItemBase {
    public ItemAdamantium(String unlocalizedName) {
        super(unlocalizedName, TitanTrophies.adamantium);
        beCraftedMust = false;
    }

    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return new EntityImmortalItem(world, location, itemstack);
    }
}
