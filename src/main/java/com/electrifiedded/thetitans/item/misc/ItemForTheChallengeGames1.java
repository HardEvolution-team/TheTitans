package com.electrifiedded.thetitans.item.misc;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemForTheChallengeGames1 extends Item {
    public ItemForTheChallengeGames1() {
        setTranslationKey("witherskeletonspawn");
        setCreativeTab(TheTitans.birthEggsPageGiant);
        setRegistryName(new ResourceLocation(TheTitans.MODID_V, "witherskeletonspawn"));
    }

    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:OMG", worldIn, playerIn, 10.0F, 0.25F + playerIn.getRNG().nextFloat() * 2.0F);
        for (int i = 0; i <= 9; i++) {
            EntityWitherSkeleton entity = new EntityWitherSkeleton(worldIn);
            // TODO fence-height render check dropped
            double d0 = 1.0D;

            entity.motionX = entity.getRNG().nextDouble() * 2D - 1D;
            entity.motionZ = entity.getRNG().nextDouble() * 2D - 1D;
            entity.onInitialSpawn(worldIn.getDifficultyForLocation(pos), (IEntityLivingData)null);
            entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
            entity.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() + d0, pos.getZ() + 0.5D, 0.0F, 0.0F);
            entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
            entity.tasks.addTask(4, new EntityAIAttackMelee(entity, 1.2D, true));
            entity.targetTasks.addTask(2, new EntityAINearestAttackableTarget(entity, EntityIronGolem.class, true));
            if (!worldIn.isRemote) {
                //Entity var8 = null;
                //var8 = EntityList.createEntityByName("Criminal", worldIn);
                //if (var8 != null)
                //
                {
                    //var8.setLocationAndAngles(arg77648n4 + 0.5D, arg77648n5 + d0, arg77648n6 + 0.5D, worldIn.rand.nextFloat() * 360.0F, 0.0F);
                    //worldIn.spawnEntityInWorld(var8);
                    //((EntityLiving)var8).playLivingSound();
                    //
                }

                worldIn.spawnEntity(entity);
            }
        }

        if (!playerIn.capabilities.isCreativeMode) {
            stack.shrink(1);
        }

        return EnumActionResult.SUCCESS;
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }
}
