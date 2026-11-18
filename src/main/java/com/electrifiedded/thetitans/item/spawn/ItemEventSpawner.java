package com.electrifiedded.thetitans.item.spawn;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.mob.EntityEvilGolem;
import com.electrifiedded.thetitans.entity.mob.EntityUndeadOther;
import com.electrifiedded.thetitans.entity.mob.EntityWraith;
import com.electrifiedded.thetitans.handler.EventEntry;
import com.electrifiedded.thetitans.item.misc.ItemBase;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEventSpawner extends ItemBase {
    public static final String[] item = new String[] {
        "bone_with_flesh","goltinum","burning_sigal","strange_egg","dark_eye","forbidden_crystal","aetherial_beacon"
    };

    public ItemEventSpawner() {
        super("event");
        setCreativeTab(com.electrifiedded.thetitans.TheTitans.pageTitans);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        for (int i = 0; i < 7; ++i) {
            items.add(new ItemStack(this, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack arg77636n1) {
        return true;
    }

    public String getTranslationKey(ItemStack arg77667n1) {
        int i = MathHelper.clamp(arg77667n1.getItemDamage(), 0, 6);
        return super.getTranslationKey() + "." + item[i];
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack stack = playerIn.getHeldItem(hand);
        playerIn.setActiveHand(hand);
        EventEntry.create(playerIn, playerIn.world, "zombie_hoard", 5.0D, 256.0D).appendBossesMini(EntityEvilGolem.class).appendCommonX(EntityZombie.class, EntitySkeleton.class, EntityUndeadOther.class).appendRare(EntityWraith.class).assignReachTicks(false, false, true, true).start(0, 0, 50);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }

    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }

    public EnumAction getItemUseAction(ItemStack arg77661n1) {
        return EnumAction.BLOCK;
    }

    public int getMaxItemUseDuration(ItemStack arg77626n1) {
        return 72000;
    }
}
