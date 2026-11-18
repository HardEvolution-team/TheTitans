package com.electrifiedded.thetitans.entity.titan;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

public interface ITitanHitbox {
    boolean attackEntityFromPart(EntityTitanPart arg70965n1, DamageSource arg70965n2, float arg70965n3);
    Entity[] getArray();
}
