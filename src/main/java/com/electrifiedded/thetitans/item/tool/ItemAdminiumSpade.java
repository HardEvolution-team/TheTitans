package com.electrifiedded.thetitans.item.tool;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.misc.EntityImmortalItem;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.sound.SoundHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockOre;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAdminiumSpade extends ItemNormalSpade {
    public ItemAdminiumSpade(String unlocalizedName, Item.ToolMaterial material) {
        super(unlocalizedName, material);
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        if (target != null) {
            if ((target.height >= 6.0F) || ((target instanceof EntityTitan)) || (!target.onGround)) {
                target.attackEntityFrom(DamageSourceExtra.causeGiantHarmAnti(attacker), 2000000000.0F);
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanpunch", target.world, target, 10.0F, 1.0F);
            }
        }

        return true;
    }

    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
        return true;
    }

    public static RayTraceResult raytraceMobFrom(World world, Entity player, boolean wut, double range) {
        float f = 1.0F;
        float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
        float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
        double d0 = player.prevPosX + (player.posX - player.prevPosX) * f;
        double d1 = player.prevPosY + (player.posY - player.prevPosY) * f;
        if ((!world.isRemote) && ((player instanceof EntityPlayer))) {
            d1 += 1.62D;
        }

        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * f;
        Vec3d vec3 = new Vec3d(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = range;
        Vec3d vec31 = vec3.add(f7 * d3, f6 * d3, f8 * d3);
        return world.rayTraceBlocks(vec3, vec31, wut);
    }

    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        RayTraceResult raycast = raytraceMobFrom(entityLiving.world, entityLiving, true, 10.0D);
        // getDistance(x,y,z) overload gone; use squared distance vs 6D range (36D squared)
        if (!entityLiving.world.isRemote && raycast != null && raycast.getBlockPos() != null && entityLiving.getDistanceSq(raycast.getBlockPos()) <= 36D) {
            BlockPos pos = raycast.getBlockPos();
            IBlockState state = entityLiving.world.getBlockState(pos);
            Block block = state.getBlock();
            if ((block instanceof IGrowable && !(block instanceof BlockGrass)) || state.getMaterial() == Material.CIRCUITS || block instanceof BlockOre) {
                entityLiving.world.destroyBlock(pos, true);
            }

            else {
                int l = block.getMetaFromState(state);
                entityLiving.world.playEvent(2001, pos, Block.getIdFromBlock(block) + (l << 12));
                EntityItem entityitem = new EntityItem(entityLiving.world, pos.getX(), pos.getY(), pos.getZ(), TheTitans.createBlockDrop(entityLiving.world, block, state, pos, entityLiving));
                entityLiving.world.spawnEntity(entityitem);
                entityLiving.world.setBlockToAir(pos);
            }
        }

        return false;
    }

    public boolean isDamageable() {
        return false;
    }

    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return new EntityImmortalItem(world, location, itemstack);
    }

    public void setDamage(ItemStack stack, int damage) {
        super.setDamage(stack, 0);
    }

    public EnumRarity getRarity(ItemStack stack) {
        return TheTitans.godlyV;
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}
