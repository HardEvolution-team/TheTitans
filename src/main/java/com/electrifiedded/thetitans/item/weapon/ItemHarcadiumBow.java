package com.electrifiedded.thetitans.item.weapon;

import com.electrifiedded.thetitans.entity.titan.EntityHarcadiumArrow;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanTrophies;

public class ItemHarcadiumBow extends ItemTitanBow

        {
    public ItemHarcadiumBow(String materialName, ToolMaterial material) {
        super(materialName, material, TitanGear.arrowHarcadium, EntityHarcadiumArrow.class, 2.0F);
        assignAchievement(TitanTrophies.harcadiumBow);
    }

    public int getMaxItemUseDuration() {
        return 10;
    }

    public int getItemEnchantability() {
        return 30;
    }

    protected float getDamage() {
        return 1.0E9F;
    }
}
