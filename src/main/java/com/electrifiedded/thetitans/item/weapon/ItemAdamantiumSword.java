package com.electrifiedded.thetitans.item.weapon;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.misc.EntityImmortalItem;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityWitherzilla;
import com.electrifiedded.thetitans.proxy.CommonProxy;
import com.electrifiedded.thetitans.sound.SoundHandler;
import com.electrifiedded.thetitans.util.ReflectionHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockOre;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

// extend ItemTitanSword (not plain ItemSword) so the CHOSEN ONE's blade keeps the titan dash-launch.
// Original tuning: dashSpeed 12, useTime 5, maxUses 19. Custom behaviors (godlyV rarity, immortal drop,
// instakill hit, block-break swing) preserved on top.
public class ItemAdamantiumSword extends ItemTitanSword {
    public static ToolMaterial toolMaterialAdamantiumEnum = EnumHelper.addToolMaterial("Adamantium", Integer.MAX_VALUE, 2, 9999999F, 1.0E19F, 0);

    public ItemAdamantiumSword() {
        super("adamantium", toolMaterialAdamantiumEnum);
        setCreativeTab(TheTitans.weaponsPageGiant);
    }

    public boolean isToolStack(ItemStack arg77616n1) {
        return true;
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

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add("§3§lThe weapon of the CHOSEN ONE.");
        tooltip.add("§3§lCan destroy anything. (expect deities)");
        tooltip.add("§3§lOnly the CHOSEN ONE (you) is allowed to wield it");
    }

    public boolean canHarvestBlock(IBlockState state) {
        return true;
    }

    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        return true;
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (target != null && !target.world.isRemote && !EntityTitan.quackosIs(target)) {
            super.hitEntity(stack, target, attacker);
            target.motionY += 4.0D - (double)target.getDistance(attacker) * 0.1D;
            target.hurtResistantTime = 0;
            if (target.height >= 6.0F || target instanceof EntityTitan || !target.onGround) {
                try {
                    java.lang.reflect.Field f = net.minecraftforge.fml.relauncher.ReflectionHelper.findField(EntityLivingBase.class, "recentlyHit", "field_70718_bc");
                    f.setInt(target, 100);
                } catch (Exception e) {}
                target.hurtTime = 0;
                try {
                    java.lang.reflect.Field f = net.minecraftforge.fml.relauncher.ReflectionHelper.findField(EntityLivingBase.class, "maxHurtTime", "field_70179_bi");
                    f.setInt(target, 0);
                } catch (Exception e) {}
                target.setHealth(target.getHealth() - getDamage() * TheTitans.getBonusDamageVSGiantMobs(target instanceof EntityTitan));
                target.attackEntityFrom(DamageSourceExtra.causeGiantHarmAnti(attacker), getDamage() * TheTitans.getBonusDamageVSGiantMobs(target instanceof EntityTitan));
                com.electrifiedded.thetitans.sound.SoundHandler.whenPlayMob("thetitans:titanpunch", target.world, target, 10.0F, 1.0F);
                if (target.height == 50.0F && target.width == 15.0F) {
                    target.getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.MAX_HEALTH).setBaseValue(0D);
                    target.attackEntityFrom(DamageSourceExtra.causeGiantHarmAnti(attacker), 40.0F);
                    target.world.setEntityState(target, (byte)3);
                    target.addPotionEffect(new net.minecraft.potion.PotionEffect(com.electrifiedded.thetitans.proxy.CommonProxy.death, Integer.MAX_VALUE, 19));
                    target.deathTime++;
                }
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
        RayTraceResult raycast = raytraceMobFrom(entityLiving.world, entityLiving, true, 6D);
        if (!entityLiving.world.isRemote && raycast != null && raycast.getBlockPos() != null) {
            BlockPos pos = raycast.getBlockPos();
            IBlockState state = entityLiving.world.getBlockState(pos);
            Block block = state.getBlock();
            if ((block instanceof IGrowable && !(block instanceof BlockGrass)) || state.getMaterial() == Material.CIRCUITS || block instanceof BlockOre) {
                entityLiving.world.destroyBlock(pos, true);
            }

            else if (!entityLiving.world.isAirBlock(pos)) {
                int l = block.getMetaFromState(state);
                EntityItem entityitem = new EntityItem(entityLiving.world, pos.getX(), pos.getY(), pos.getZ(), TheTitans.createBlockDrop(entityLiving.world, block, state, pos, entityLiving));
                entityLiving.world.spawnEntity(entityitem);
                entityLiving.world.setBlockToAir(pos);
            }
        }

        return super.onEntitySwing(entityLiving, stack);
    }

    protected double getRange() {
        return 16.0D;
    }

    protected double fetchPaceDash() {
        return 12.0D;
    }

    protected int fetchTicksUse() {
        return 5;
    }

    protected int fetchTicksHurt() {
        return 5;
    }

    protected int getMaxUses() {
        return 19;
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
