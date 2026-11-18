package com.electrifiedded.thetitans.item.spawn;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.handler.VoidTeleporter;
import com.electrifiedded.thetitans.item.misc.ItemBase;
import com.electrifiedded.thetitans.world.dimension.WorldProviderNowhere;
import com.electrifiedded.thetitans.world.dimension.WorldProviderVoid;

import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
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

public class ItemTeleporter extends ItemBase {
    public ItemTeleporter() {
        super("teleporter");
        this.maxStackSize = 1;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        if (!worldIn.isRemote) {
            if ((playerIn.world.provider instanceof WorldProviderEnd)) {
                List<EntityDragon> list = playerIn.world.getEntitiesWithinAABB(EntityDragon.class, playerIn.getEntityBoundingBox().grow(256.0D, 256.0D, 256.0D));
                if ((list != null) && (playerIn.timeUntilPortal <= 0)) {
                    if (!list.isEmpty()) {
                        playerIn.sendMessage(new TextComponentString("I can't do that when the Ender Dragon's still here! Kill it first."));
                        playerIn.timeUntilPortal = 20;
                    }

                    else if ((playerIn.posX > 5.0D) && (playerIn.posX < -5.0D) && (playerIn.posZ > 5.0D) && (playerIn.posZ < -5.0D)) {
                        playerIn.sendMessage(new TextComponentString("I can't do that this far away from the center of the island. Get closer."));
                        playerIn.timeUntilPortal = 20;
                    }

                    else if ((list.isEmpty()) && (playerIn.posX < 5.0D) && (playerIn.posX > -5.0D) && (playerIn.posZ < 5.0D) && (playerIn.posZ > -5.0D)) {
                        playerIn.sendMessage(new TextComponentString("Master! More food is coming for you!"));
                        playerIn.motionY = 3.0D;
                        playerIn.getServer().getPlayerList().transferPlayerToDimension((EntityPlayerMP) playerIn, TheTitans.DIMENSION_KEY_VOID, new VoidTeleporter((WorldServer)playerIn.world));
                        playerIn.timeUntilPortal = 300;
                        // TODO advancements — TitanTrophies.voidTime is now an advancement-id String; runGrant via CriteriaTriggers once advancements are added
                        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
                    }
                }
            }

            else if ((playerIn.world.provider instanceof WorldProviderHell)) {
                playerIn.sendMessage(new TextComponentString("(You hear the distant crys of a long, forgotten voice. It's very ancient.)"));
            }

            else if ((playerIn.world.provider instanceof WorldProviderVoid)) {
                playerIn.sendMessage(new TextComponentString("Stop talking to me and feed my Master!"));
            }

            else if ((playerIn.world.provider instanceof WorldProviderNowhere)) {
                playerIn.sendMessage(new TextComponentString("Why have you brought me here? Get me out!"));
            }

            else if ((!(playerIn.world.provider instanceof WorldProviderVoid)) && (!(playerIn.world.provider instanceof WorldProviderHell)) && (!(playerIn.world.provider instanceof WorldProviderEnd))) {
                playerIn.sendMessage(new TextComponentString("(The ancient crys still abode. It's as if they are saying 'All things must End')"));
            }

            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
        }

        itemStackIn.hasTagCompound();
        playerIn.addStat(net.minecraft.stats.StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
    }
}
