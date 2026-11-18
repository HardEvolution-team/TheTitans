package com.electrifiedded.thetitans.data;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;

import java.util.Comparator;

public class TargetingSorter implements Comparator<Object> {
    private Entity mobThe;

    public TargetingSorter(Entity par2Entity) {
        this.mobThe = par2Entity;
    }

    public int compareSqDistance(Entity par1Entity, Entity par2Entity) {
        double weight = 0.0D;
        double var3 = this.mobThe.getDistanceSq(par1Entity);
        if ((par1Entity instanceof EntityCreeper)) {
            var3 /= 2.0D;
        }

        weight = par1Entity.height * par1Entity.width;
        if (weight > 1.0D) {
            var3 /= weight;
        }

        double var5 = this.mobThe.getDistanceSq(par2Entity);
        if ((par2Entity instanceof EntityCreeper)) {
            var5 /= 2.0D;
        }

        weight = par2Entity.height * par2Entity.width;
        if (weight > 1.0D) {
            var5 /= weight;
        }

        return var3 > var5 ? 1 : var3 < var5 ? -1 : 0;
    }

    public int compare(Object par1Obj, Object par2Obj) {
        return compareSqDistance((Entity)par1Obj, (Entity)par2Obj);
    }
}
