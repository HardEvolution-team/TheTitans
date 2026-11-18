package com.electrifiedded.thetitans.item.tool;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.google.common.collect.Multimap;

public class ItemWithreniumHoe extends ItemNormalHoe {
    private float harmHoeStrike;
    private final Item.ToolMaterial matTool;

    public ItemWithreniumHoe(String unlocalizedName, Item.ToolMaterial material) {
        super(unlocalizedName, material);
        this.harmHoeStrike = material.getAttackDamage();
        this.matTool = material;
    }

    public float getAttackDamage() {
        return this.matTool.getAttackDamage() - 4.0F;
    }

    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.harmHoeStrike - 4.0D, 0));
        return multimap;
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        if (target != null)
            target.setFire(20);
        return true;
    }

    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BLOCK;
    }

    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack))
            return EnumActionResult.FAIL;
        int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(itemstack, player, world, pos);
        if (hook != 0)
            return hook > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (facing != EnumFacing.DOWN && world.isAirBlock(pos.up())
            && (block == Blocks.GRASS || block == Blocks.DIRT || block == Blocks.SPONGE || block == Blocks.MYCELIUM || block == Blocks.GRAVEL)) {
            IBlockState farmland = Blocks.FARMLAND.getDefaultState();
            world.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if (world.isRemote)
                return EnumActionResult.SUCCESS;
            world.setBlockState(pos, farmland);
            itemstack.damageItem(1, player);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    public boolean canHarvestBlock(IBlockState state) {
        return state.getBlock() == Blocks.FARMLAND;
    }
}
