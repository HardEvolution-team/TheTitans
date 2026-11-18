package com.electrifiedded.thetitans.item.weapon;

import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTitanBow extends ItemNormalBow {
    protected int beatsUse;
    protected int beatsServer;
    @SideOnly(Side.CLIENT)
    protected int beatsClient;

    public ItemTitanBow(String materialName, ToolMaterial material, Item stackArrow, Class<? extends EntityArrow> arrow, float paceArrow, String... information) {
        super(materialName, material, stackArrow, arrow, paceArrow, information);
    }

    public void onUsingTick(ItemStack stack, EntityLivingBase entityLiving, int count) {
        if (entityLiving == null || entityLiving.isDead || !(entityLiving instanceof EntityPlayer))
            return;
        EntityPlayer player = (EntityPlayer)entityLiving;
        beatsUse ++;
        if (player.world.isRemote) {
            beatsClient ++;
            if (beatsClient >= (int)(getMaxItemUseDuration() * (3.0F - (2.0F * Math.min( (getMaxItemUseDuration(stack) - count) / (float)fetchBowDurationMax(), 1.0F)))))
                beatsClient = 0;
        }

        else

            {
            beatsServer ++;
            if (beatsServer >= (int)(getMaxItemUseDuration() * (3.0F - (2.0F * Math.min( (getMaxItemUseDuration(stack) - count) / (float)fetchBowDurationMax(), 1.0F))))) {
                beatsServer = 0;
                onFire(stack, player.world, player, 0);
            }
        }
    }

    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (worldIn.isRemote)
            beatsClient = 0;
        else
            beatsServer = 0;
        beatsUse = 0;
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        if (target != null) {
            if (target.hurtResistantTime > 15)
                target.hurtResistantTime = 15;
            if (target.height < 7.0F && !(target instanceof EntityTitan)) {
                target.attackEntityFrom(DamageSourceExtra.causeGiantHarmAnti(attacker), getDamage() / 100.0F);
            }

            else {
                target.attackEntityFrom(DamageSourceExtra.causeGiantHarmAnti(attacker), getDamage());
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanpunch", target.world, target, 10.0F, 1.0F);
            }
        }

        return true;
    }

    public int fetchBowDurationMax() {
        return 100;
    }

    public int getMaxItemUseDuration() {
        return 10;
    }

    protected float getDamage() {
        return 1.0F;
    }
}
