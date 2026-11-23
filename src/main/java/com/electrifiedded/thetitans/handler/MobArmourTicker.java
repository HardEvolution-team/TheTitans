package com.electrifiedded.thetitans.handler;

import com.electrifiedded.thetitans.item.armor.ItemNormalArmor;
import com.electrifiedded.thetitans.item.armor.ItemTitanArmor;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Vanilla only calls Item.onArmorTick for players, so mobs wearing titan armour (villagers, zombies, other mods'
 * NPCs) got no effects/regen at all. Ticks the same armour logic for every non-player living entity (server side).
 */
public class MobArmourTicker {
    private static final EntityEquipmentSlot[] ARMOR = {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};

    @SubscribeEvent
    public void whenLivingTick(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity instanceof EntityPlayer || entity.world.isRemote || entity.isDead)
            return;
        for (EntityEquipmentSlot slot : ARMOR) {
            ItemStack stack = entity.getItemStackFromSlot(slot);
            if (stack.isEmpty())
                continue;
            if (stack.getItem() instanceof ItemTitanArmor)
                ((ItemTitanArmor)stack.getItem()).armourTick(entity.world, entity, stack);
            else if (stack.getItem() instanceof ItemNormalArmor)
                ((ItemNormalArmor)stack.getItem()).armourTick(entity.world, entity, stack);
        }
    }
}
