package com.electrifiedded.thetitans.item.tool;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.misc.EntityImmortalItem;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.registry.TitanAudio;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockOre;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.Multimap;

public class ItemAdminiumHoe extends ItemSword {
    private float harmHoeStrike;
    private final Item.ToolMaterial materialHoeTool;

    public ItemAdminiumHoe(String unlocalizedName, Item.ToolMaterial material) {
        super(material);
        setTranslationKey(unlocalizedName + "_hoe");
        setRegistryName(new net.minecraft.util.ResourceLocation(com.electrifiedded.thetitans.TheTitans.MODID_V, unlocalizedName + "_hoe"));
        setCreativeTab(TheTitans.toolsPageGiant);
        this.harmHoeStrike = material.getAttackDamage();
        this.materialHoeTool = material;
    }

    public float getAttackDamage() {
        return this.materialHoeTool.getAttackDamage() - 4.0F;
    }

    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(slot);
        multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.harmHoeStrike - 4.0D, 0));
        return multimap;
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        if (target != null) {
            if ((target.height >= 6.0F) || ((target instanceof EntityTitan)) || (!target.onGround)) {
                target.attackEntityFrom(DamageSourceExtra.causeGiantHarmAnti(attacker), 1000000000.0F);
                net.minecraft.util.SoundEvent punch = com.electrifiedded.thetitans.registry.TitanAudio.get("thetitans:titanpunch");
                if (punch != null)
                    target.playSound(punch, 10.0F, 1.0F);
            }
        }

        return true;
    }

    public EnumAction getItemUseAction(ItemStack arg77661n1) {
        return EnumAction.NONE;
    }

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (!player.canPlayerEdit(pos, facing, stack)) {
            return EnumActionResult.FAIL;
        }

        UseHoeEvent event = new UseHoeEvent(player, stack, worldIn, pos);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return EnumActionResult.FAIL;
        }

        if (event.getResult() == Result.ALLOW) {
            stack.damageItem(1, player);
            return EnumActionResult.SUCCESS;
        }

        IBlockState state = worldIn.getBlockState(pos);
        Block block = state.getBlock();
        if ((facing != EnumFacing.DOWN) && worldIn.isAirBlock(pos.up()) && ((block == Blocks.GRASS) || (block == Blocks.DIRT) || (block == Blocks.SPONGE) || (block == Blocks.MYCELIUM) || (block == Blocks.GRAVEL))) {
            // TODO farmland till sound
            if (worldIn.isRemote) {
                return EnumActionResult.SUCCESS;
            }

            worldIn.setBlockState(pos, Blocks.FARMLAND.getDefaultState());
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        if (entityLiving instanceof EntityPlayer) {
            for (int i1 = 0; i1 < 4; i1++) {
                Vec3d vec3 = ((EntityPlayer)entityLiving).getLook(1.0F);
                double dx = i1 == 0 ? vec3.x : vec3.x * i1;
                double dy = i1 == 0 ? entityLiving.getEyeHeight() + vec3.y : entityLiving.getEyeHeight() + vec3.y * i1;
                double dz = i1 == 0 ? vec3.z : vec3.z * i1;
                int y = MathHelper.floor(entityLiving.posY + dy);
                int x = MathHelper.floor(entityLiving.posX + dx);
                int z = MathHelper.floor(entityLiving.posZ + dz);
                BlockPos pos = new BlockPos(x, y, z);
                if (!entityLiving.world.isAirBlock(pos))
                    entityLiving.world.playEvent((EntityPlayer)null, 1012, pos, 0);
                if (!entityLiving.world.isRemote && !entityLiving.world.isAirBlock(pos)) {
                    stack.damageItem(2, entityLiving);
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
            }
        }

        return false;
    }

    public boolean canHarvestBlock(IBlockState state) {
        return state.getBlock() == Blocks.FARMLAND;
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
