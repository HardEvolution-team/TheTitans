package com.electrifiedded.thetitans.item.tool;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.registry.TitanAudio;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

import com.google.common.collect.Multimap;

public class ItemVoidHoe extends ItemSword {
    private float field_150934_a;
    private final Item.ToolMaterial fld150933b;

    public ItemVoidHoe(String unlocalizedName, Item.ToolMaterial material) {
        super(material);
        setTranslationKey(unlocalizedName + "_hoe");
        setRegistryName(new ResourceLocation(TheTitans.MODID_V, unlocalizedName + "_hoe"));
        setCreativeTab(TheTitans.toolsPageGiant);
        this.field_150934_a = material.getAttackDamage();
        this.fld150933b = material;
    }

    public float getAttackDamage() {
        return this.fld150933b.getAttackDamage() - 4.0F;
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.field_150934_a - 4.0D, 0));
        }
        return multimap;
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        if ((target != null) && ((target instanceof EntityLivingBase))) {
            if ((target instanceof EntityLiving && target.world.isRemote)) {
                ((EntityLiving)target).spawnExplosionParticle();
            }

            if (target instanceof EntityTitan) {
                target.attackEntityFrom(DamageSourceExtra.causeGiantHarmAnti(attacker), 5000.0F);
                net.minecraft.util.SoundEvent punch = com.electrifiedded.thetitans.registry.TitanAudio.get("thetitans:titanpunch");
                if (punch != null)
                    target.playSound(punch, 10.0F, 1.0F);
            }

            else if ((!(target instanceof EntityPlayer)) && (!(target instanceof EntityGolem))) {
                attacker.world.removeEntity(target);
                // TODO "random.fizz" sound; closest is BLOCK_FIRE_EXTINGUISH
                attacker.world.playSound(null, attacker.getPosition(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 1.0F, 0.5F);
                attacker.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 1, 3, false, false));
                attacker.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(attacker.getMaxHealth() + target.getMaxHealth());
                attacker.heal(target.getMaxHealth());
                if (target instanceof EntityMob && target.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE) != null)
                    attacker.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue((float)attacker.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue() + (float)target.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue());
            }
        }

        return true;
    }

    public EnumAction getItemUseAction(ItemStack arg77661n1) {
        return EnumAction.BLOCK;
    }

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) {
            return EnumActionResult.FAIL;
        }

        UseHoeEvent event = new UseHoeEvent(player, stack, worldIn, pos);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return EnumActionResult.PASS;
        }

        if (event.getResult() == Result.ALLOW) {
            stack.damageItem(1, player);
            return EnumActionResult.SUCCESS;
        }

        IBlockState state = worldIn.getBlockState(pos);
        Block block = state.getBlock();
        if ((facing != EnumFacing.DOWN) && (worldIn.isAirBlock(pos.up())) && ((block == Blocks.GRASS) || (block == Blocks.DIRT) || (block == Blocks.SPONGE) || (block == Blocks.MYCELIUM) || (block == Blocks.GRAVEL))) {
            SoundType src = block.getSoundType();
            SoundType dst = Blocks.FARMLAND.getSoundType();
            worldIn.playSound(null, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, (src.getVolume() + 1.0F) / 2.0F, src.getPitch() * 0.8F);
            worldIn.playSound(null, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, (dst.getVolume() + 1.0F) / 2.0F, dst.getPitch() * 0.8F);
            if (worldIn.isRemote) {
                return EnumActionResult.SUCCESS;
            }

            worldIn.setBlockState(pos, Blocks.FARMLAND.getDefaultState());
            stack.damageItem(1, player);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    public boolean canHarvestBlock(IBlockState blockIn) {
        return blockIn.getBlock() == Blocks.FARMLAND;
    }
}
