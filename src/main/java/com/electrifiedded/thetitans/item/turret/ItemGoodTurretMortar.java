package com.electrifiedded.thetitans.item.turret;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.titan.EntityWitherTurretMortar;
import com.electrifiedded.thetitans.registry.TitanGear;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class ItemGoodTurretMortar extends ItemSword {
    public ItemGoodTurretMortar(String unlocalizedName, Item.ToolMaterial material) {
        super(material);
        setTranslationKey(unlocalizedName);
        setRegistryName(new ResourceLocation(TheTitans.MODID_V, unlocalizedName));
        setCreativeTab(TheTitans.pageTitans);
    }

    public boolean isToolStack(ItemStack arg77616n1) {
        return true;
    }

    public boolean isDamageable() {
        return false;
    }

    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        return true;
    }

    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        EntityWitherTurretMortar entity = new EntityWitherTurretMortar(worldIn);
        double d0 = 1.0D;
        int a = EnchantmentHelper.getEnchantmentLevel(TheTitans.enchant1Turret, stack);
        int b = EnchantmentHelper.getEnchantmentLevel(TheTitans.enchant2Turret, stack);
        int c = EnchantmentHelper.getEnchantmentLevel(TheTitans.enchant3Turret, stack);
        int d = EnchantmentHelper.getEnchantmentLevel(TheTitans.enchant4Turret, stack);
        int e = EnchantmentHelper.getEnchantmentLevel(TheTitans.enchant5Turret, stack);
        int f = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack);
        entity.setPlayerCreated(true);
        entity.onInitialSpawn(worldIn.getDifficultyForLocation(pos), (IEntityLivingData)null);
        entity.world.playBroadcastSound(1013, pos, 0);
        Random rand = new com.electrifiedded.archaiclib.util.FastRandom();
        if (a > 0) {
            entity.tierDurability = a;
        }

        if (b > 0) {
            entity.tierFerocity = b;
        }

        if (c > 0) {
            entity.tierManiac = c;
        }

        if (d > 0) {
            entity.tierUnstability = d;
        }

        if (e > 0) {
            entity.tierShurakin = e;
        }

        if (f > 0) {
            entity.tierUnbreaking = f;
        }

        entity.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() + d0, pos.getZ() + 0.5D, 0.0F, 0.0F);
        for (int i = 0; i < 500; i++) {
            entity.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, entity.posX + (rand.nextDouble() - 0.5D) * (entity.width * 3.0D), entity.posY + rand.nextDouble() * (entity.height + 1.0D), entity.posZ + (rand.nextDouble() - 0.5D) * (entity.width * 3.0D), 0.0D, 0.0D, 0.0D);
        }

        if (!worldIn.isRemote) {
            worldIn.spawnEntity(entity);
        }

        if (entity != null) {
            if (!playerIn.capabilities.isCreativeMode) {
                stack.shrink(1);
            }
        }

        return EnumActionResult.SUCCESS;
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if ((target instanceof EntityWitherTurretMortar)) {
            if (((EntityWitherTurretMortar)target).isPlayerCreated()) {
                ItemStack itemstack = ((EntityPlayer)attacker).getHeldItemMainhand();
                if ((attacker != null) && (!itemstack.isEmpty()) && (itemstack.getItem() == TitanGear.turret3Good) && (!((EntityWitherTurretMortar)target).world.isRemote)) {
                    ((EntityWitherTurretMortar)target).setDead();
                    ItemStack turretGood = new ItemStack(TitanGear.turret3Good);
                    if (((EntityWitherTurretMortar)target).tierDurability > 0) {
                        turretGood.addEnchantment(TheTitans.enchant1Turret, ((EntityWitherTurretMortar)target).tierDurability);
                    }

                    if (((EntityWitherTurretMortar)target).tierFerocity > 0) {
                        turretGood.addEnchantment(TheTitans.enchant2Turret, ((EntityWitherTurretMortar)target).tierFerocity);
                    }

                    if (((EntityWitherTurretMortar)target).tierManiac > 0) {
                        turretGood.addEnchantment(TheTitans.enchant3Turret, ((EntityWitherTurretMortar)target).tierManiac);
                    }

                    if (((EntityWitherTurretMortar)target).tierUnstability > 0) {
                        turretGood.addEnchantment(TheTitans.enchant4Turret, ((EntityWitherTurretMortar)target).tierUnstability);
                    }

                    if (((EntityWitherTurretMortar)target).tierShurakin > 0) {
                        turretGood.addEnchantment(TheTitans.enchant5Turret, ((EntityWitherTurretMortar)target).tierShurakin);
                    }

                    if (((EntityWitherTurretMortar)target).tierUnbreaking > 0) {
                        turretGood.addEnchantment(Enchantments.UNBREAKING, ((EntityWitherTurretMortar)target).tierUnbreaking);
                    }

                    if (!((EntityPlayer)attacker).capabilities.isCreativeMode) {
                        ((EntityWitherTurretMortar)target).entityDropItem(turretGood, 1.0F);
                    }
                }
            }

            return false;
        }

        return true;
    }
}
