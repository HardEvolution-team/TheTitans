package com.electrifiedded.thetitans.item.armor;

import com.electrifiedded.thetitans.util.Maths;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemPigIronArmor extends ItemNormalArmor {
    private int ticks;

    public ItemPigIronArmor(String materialName, ArmorMaterial material, int type) {
        super(materialName, material, type);
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (this.ticks >= 80 + player.getRNG().nextInt(1000)) {
            world.playSound(player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PIG_AMBIENT, SoundCategory.PLAYERS, 1.0F, Maths.random(0.8F, 1.2F), false);
            this.ticks = 0;
        }

        ticks++;
    }
}
