package com.electrifiedded.thetitans.item.spawn;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.titan.EntityIronGolemTitan;
import com.electrifiedded.thetitans.item.misc.ItemBase;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemEggReinforcedIronGolem extends ItemBase {
    public ItemEggReinforcedIronGolem() {
        super("spawn_egg_iron_golem_better");
        setCreativeTab(TheTitans.birthEggsPageGiant);
    }

    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        for (int i = 0; i <= (!playerIn.isSneaking() && playerIn.capabilities.isCreativeMode ? 29 : 0); i++) {
            EntityIronGolem entity = new EntityIronGolem(worldIn);
            //EntityGiantZombieBetter entity = new EntityGiantZombieBetter(worldIn);
            //EntityDragonMinion entity = new EntityDragonMinion(worldIn);
            //EntityWitherMinion entity = new EntityWitherMinion(worldIn);
            //EntityHarcadiumGolem entity = new EntityHarcadiumGolem(worldIn);
            //EntityVoidGolem entity = new EntityVoidGolem(worldIn);
            // TODO 1.7.10 fence-height render check dropped
            double d0 = 1.0D;

            entity.motionX = entity.getRNG().nextDouble() * 4D - 2D;
            entity.motionZ = entity.getRNG().nextDouble() * 4D - 2D;
            entity.onInitialSpawn(worldIn.getDifficultyForLocation(pos), (IEntityLivingData)null);
            entity.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() + d0, pos.getZ() + 0.5D, 0.0F, 0.0F);
            entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(2000D + entity.getRNG().nextDouble() * 2000D * 20D);
            entity.setHealth(entity.getMaxHealth());
            entity.setCustomNameTag("Reinforced Iron Golem");
            EntityIronGolemTitan.appendTargetingTaskToMobGiant(entity);
            entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
            entity.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
            if (!worldIn.isRemote) {
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
