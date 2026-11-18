package com.electrifiedded.thetitans.item.spawn;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.handler.VoidTeleporter;
import com.electrifiedded.thetitans.item.misc.ItemBase;
import com.electrifiedded.thetitans.world.dimension.WorldProviderNowhere;
import com.electrifiedded.thetitans.world.dimension.WorldProviderVoid;

import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldServer;

import java.util.List;

public class ItemTeleporter2 extends ItemBase {
    public ItemTeleporter2() {
        super("teleporter2");
        this.maxStackSize = 1;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemStackIn = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote) {
            if ((playerIn.world.provider instanceof WorldProviderEnd)) {
                List<?> list = playerIn.world.getEntitiesWithinAABB(EntityDragon.class, playerIn.getEntityBoundingBox().grow(256.0D, 256.0D, 256.0D));
                if ((list != null) && (playerIn.timeUntilPortal <= 0)) {
                    if (!list.isEmpty()) {
                        playerIn.sendMessage(new TextComponentString("(The Ender Dragon is blocking the use of the Item)"));
                        playerIn.timeUntilPortal = 20;
                    }

                    else if ((playerIn.posX > 5.0D) && (playerIn.posX < -5.0D) && (playerIn.posZ > 5.0D) && (playerIn.posZ < -5.0D)) {
                        playerIn.sendMessage(new TextComponentString("(The item resinates, but nothing happens)"));
                        playerIn.timeUntilPortal = 20;
                    }

                    else if ((list.isEmpty()) && (playerIn.posX < 5.0D) && (playerIn.posX > -5.0D) && (playerIn.posZ < 5.0D) && (playerIn.posZ > -5.0D)) {
                        playerIn.sendMessage(new TextComponentString("(A rush of energy surges through you as you are sent to another realm.)"));
                        playerIn.motionY = 3.0D;
                        playerIn.getServer().getPlayerList().transferPlayerToDimension((EntityPlayerMP) playerIn, TheTitans.DIMENSION_KEY_NOWHERE, new VoidTeleporter((WorldServer)playerIn.world));
                        playerIn.timeUntilPortal = 300;
                        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
                    }
                }
            }

            else if ((playerIn.world.provider instanceof WorldProviderHell)) {
                playerIn.sendMessage(new TextComponentString("(All you hear is the sounds of the Nether.)"));
            }

            else if ((playerIn.world.provider instanceof WorldProviderVoid)) {
                playerIn.sendMessage(new TextComponentString("(Some great all-powerful force stops you from leaving.)"));
            }

            else if ((playerIn.world.provider instanceof WorldProviderNowhere)) {
                playerIn.sendMessage(new TextComponentString("(The item fails to function.)"));
            }

            else if ((!(playerIn.world.provider instanceof WorldProviderVoid)) && (!(playerIn.world.provider instanceof WorldProviderHell)) && (!(playerIn.world.provider instanceof WorldProviderEnd))) {
                playerIn.sendMessage(new TextComponentString("(The item appears to resinate with quantum energy.)"));
            }
        }

        itemStackIn.hasTagCompound();
        playerIn.addStat(net.minecraft.stats.StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
    }
}
