package com.electrifiedded.thetitans.item.weapon;

import com.electrifiedded.thetitans.entity.titan.EntityHarcadiumArrow;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanTrophies;

public class ItemVoidBow extends ItemTitanBow {
    public ItemVoidBow(String materialName, ToolMaterial material) {
        super(materialName, material, TitanGear.arrowVoid, EntityHarcadiumArrow.class, 3.0F);
        assignAchievement(TitanTrophies.voidBow);
    }

    public int getMaxItemUseDuration() {
        return 5;
    }

    public int getItemEnchantability() {
        return 30;
    }

    protected float getDamage() {
        return 2.0E8F;
    }
}
