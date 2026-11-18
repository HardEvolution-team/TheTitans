package com.electrifiedded.thetitans.item.weapon;

import com.electrifiedded.thetitans.proxy.CommonProxy;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemObsidianSword extends ItemNormalSword {
    public ItemObsidianSword(String unlocalizedName, Item.ToolMaterial material) {
        super(unlocalizedName, material);
    }

    public boolean specialtyIs(Entity entity) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add("Causes hit enemies to start bleeding due to it's sharpness.");
        tooltip.add("Repeated attacks cause increased effects.");
        tooltip.add("Golems, Blazes and Skeletons are immune.");
    }

    public boolean hitEntity(ItemStack item, EntityLivingBase victim, EntityLivingBase attacker) {
        if (!(victim instanceof EntityGolem) && !(victim instanceof EntityBlaze) && !(victim instanceof EntitySkeleton) && !victim.isEntityUndead()) {
            if (victim.isPotionActive(com.electrifiedded.thetitans.proxy.CommonProxy.bleeding))
                victim.addPotionEffect(new net.minecraft.potion.PotionEffect(com.electrifiedded.thetitans.proxy.CommonProxy.bleeding, victim.getActivePotionEffect(com.electrifiedded.thetitans.proxy.CommonProxy.bleeding).getDuration() + 200));
            else
                victim.addPotionEffect(new net.minecraft.potion.PotionEffect(com.electrifiedded.thetitans.proxy.CommonProxy.bleeding, 200));
        }

        return super.hitEntity(item, victim, attacker);
    }
}
