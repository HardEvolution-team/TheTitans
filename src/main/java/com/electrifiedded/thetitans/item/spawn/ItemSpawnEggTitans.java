package com.electrifiedded.thetitans.item.spawn;

import com.electrifiedded.thetitans.TheTitans;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSpawnEggTitans extends Item {
    public String monsternameV;

    public ItemSpawnEggTitans(String unlocalizedName, String mobnameV) {
        this.monsternameV = mobnameV;
        this.maxStackSize = 64;
        setTranslationKey(unlocalizedName);
        setRegistryName(new ResourceLocation(TheTitans.MODID_V, unlocalizedName));
        setCreativeTab(TheTitans.birthEggsPageGiant);
    }

    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote)
            return EnumActionResult.SUCCESS;
        Entity ent = emitSomething(this.monsternameV, world, pos.getX() + 0.5D, pos.getY() + 1.01D, pos.getZ() + 0.5D);
        if (ent != null) {
            if (((ent instanceof EntityLiving)) && (stack.hasDisplayName())) {
                ((EntityLiving)ent).setCustomNameTag(stack.getDisplayName());
            }
        }

        if (!player.capabilities.isCreativeMode) {
            stack.shrink(1);
        }

        return EnumActionResult.SUCCESS;
    }

    public static Entity emitSomething(String id, World world, double d0, double d1, double d2) {
        return spawnCreature(world, new ResourceLocation(TheTitans.MODID_V, id), d0, d1, d2);
    }

    public static Entity spawnCreature(World world, ResourceLocation name, double x, double y, double z) {
        Entity var8 = EntityList.createEntityByIDFromName(name, world);
        if (var8 != null) {
            var8.setLocationAndAngles(x, y, z, world.rand.nextFloat() * 360.0F, 0.0F);
            world.spawnEntity(var8);
            if (var8 instanceof EntityLiving) {
                ((EntityLiving)var8).onInitialSpawn(world.getDifficultyForLocation(new BlockPos(var8)), null);
                ((EntityLiving)var8).playLivingSound();
            }
        }

        return var8;
    }
}
