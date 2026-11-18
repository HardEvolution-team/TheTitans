package com.electrifiedded.thetitans.item.weapon;

import com.electrifiedded.thetitans.entity.minion.EntityWitherMinion;
import com.electrifiedded.thetitans.entity.mob.EntityWraith;
import com.electrifiedded.thetitans.entity.titan.EntityWitherzillaMinion;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemSacredSword extends ItemSilverSword {
    public ItemSacredSword(String unlocalizedName, Item.ToolMaterial material) {
        super(unlocalizedName, material);
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add("A Silver Sword encrused with gems and alloyed with gold and steel.");
        tooltip.add("It has greatly enhanced Undead killing abilities.");
        tooltip.add(multiplier + " times more damage");
        tooltip.add(multiplier * 2.0F + " times damage to Wraiths");
        tooltip.add(multiplier * 4.0F + " times damage to Withers");
    }

    public boolean hitEntity(ItemStack item, EntityLivingBase victim, EntityLivingBase attacker) {
        item.damageItem(1, attacker);
        float wraithDamage = 1.0F;
        if (victim instanceof EntityWraith)
            wraithDamage = 2.0F;
        if (victim instanceof EntityWither || victim instanceof EntityWitherzillaMinion || victim instanceof EntityWitherMinion)
            wraithDamage = 4.0F;
        if (attacker.motionY <= 0.0D)
            wraithDamage *= 1.5F;
        if (specialtyIs(victim)) {
            victim.hurtResistantTime = 0;
            victim.attackEntityFrom(DamageSource.causeMobDamage(attacker), damage + damage * multiplier * wraithDamage);
        }

        return true;
    }
}
