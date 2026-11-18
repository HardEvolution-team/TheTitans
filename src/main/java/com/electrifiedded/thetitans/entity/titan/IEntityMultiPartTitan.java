package com.electrifiedded.thetitans.entity.titan;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

public interface IEntityMultiPartTitan {
    boolean attackEntityFromPart(EntityTitanPart part, DamageSource source, float damage);
    Entity[] getArray();
}
