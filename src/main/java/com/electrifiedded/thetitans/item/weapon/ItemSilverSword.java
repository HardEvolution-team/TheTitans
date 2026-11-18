package com.electrifiedded.thetitans.item.weapon;

import com.electrifiedded.thetitans.entity.mob.EntityWraith;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class ItemSilverSword extends ItemNormalSword {
    public ItemSilverSword(String unlocalizedName, ToolMaterial material) {
        super(unlocalizedName, material);
    }

    public boolean hitEntity(ItemStack item, EntityLivingBase victim, EntityLivingBase attacker) {
        item.damageItem(1, attacker);
        float wraithDamage = 1.0F;
        if (victim instanceof EntityWraith)
            ++wraithDamage;
        if (attacker.motionY <= 0.0D)
            wraithDamage *= 1.5F;
        if (specialtyIs(victim)) {
            victim.hurtResistantTime = 0;
            victim.attackEntityFrom(DamageSource.causeMobDamage(attacker), damage + damage * multiplier * wraithDamage);
        }
        return true;
    }

    public boolean specialtyIs(Entity entity) {
        return entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isEntityUndead();
    }
}
