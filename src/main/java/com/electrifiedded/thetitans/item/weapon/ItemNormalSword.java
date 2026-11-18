package com.electrifiedded.thetitans.item.weapon;

import com.electrifiedded.thetitans.TheTitans;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class ItemNormalSword extends ItemSword {
    protected float damage;
    protected float multiplier;

    public ItemNormalSword(String unlocalizedName, Item.ToolMaterial material) {
        super(material);
        multiplier = 0.0F;
        damage = material.getAttackDamage() + 4.0F;
        // empty name = subclass (OptimaAxe/UltimaBlade) sets its own registry+translation name; skip to avoid double-register.
        if (!unlocalizedName.isEmpty()) {
            setTranslationKey(unlocalizedName + "_sword");
            setRegistryName(new ResourceLocation(TheTitans.MODID_V, unlocalizedName + "_sword"));
        }
        setCreativeTab(TheTitans.weaponsPageGiant);
    }

    public boolean hitEntity(ItemStack item, EntityLivingBase victim, EntityLivingBase attacker) {
        item.damageItem(1, attacker);
        if (specialtyIs(victim))
            victim.attackEntityFrom(DamageSource.causeMobDamage(attacker), damage + damage * multiplier * (attacker.motionY < 0.0D ? 1.5F : 1.0F));
        return true;
    }

    public ItemNormalSword setDamageMultiplier(float multiplier) {
        this.multiplier = multiplier;
        return this;
    }

    public float getDamageMultiplier() {
        return multiplier;
    }

    public boolean specialtyIs(Entity entity) {
        return false;
    }
}
