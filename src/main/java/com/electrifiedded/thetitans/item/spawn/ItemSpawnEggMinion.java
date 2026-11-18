package com.electrifiedded.thetitans.item.spawn;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.minion.EntitySkeletonMinion;
import com.electrifiedded.thetitans.entity.minion.EnumMinionType;
import com.electrifiedded.thetitans.entity.minion.IMinion;
import com.electrifiedded.thetitans.item.misc.ItemBase;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSpawnEggMinion extends ItemBase {
    // colors used for the vanilla base+spots IItemColor tint (registered in ClientProxy).
    private int primaryColor;
    private int secondaryColor;
    private String mobnameV;
    private String fakenameV;

    public ItemSpawnEggMinion(String realname, int firstcolor, int secondcolor, String name) {
        // registry/model name must be lowercase in 1.12.2 or the model loader can't find the JSON.
        super(("spawn_egg_" + name).toLowerCase());
        setHasSubtypes(true);
        setCreativeTab(TheTitans.birthEggsPageGiant);
        this.fakenameV = name;
        this.mobnameV = realname;
        this.primaryColor = firstcolor;
        this.secondaryColor = secondcolor;
    }

    // vanilla-egg tint — layer0 (base) uses primaryColor, layer1 (spots) uses secondaryColor.
    public int getColor(ItemStack stack, int tintIndex) {
        return tintIndex == 0 ? primaryColor : secondaryColor;
    }

    @SuppressWarnings("deprecation")
    public String getItemStackDisplayName(ItemStack stack) {
        return "Spawn " + I18n.translateToLocal("entity." + fakenameV + ".name") + " " + I18n.translateToLocal("rank." + fetchKindMinion(stack).getKey());
    }

    public EnumRarity getRarity(ItemStack stack) {
        switch (fetchKindIntMinion(stack)) {
            case 1:
                return EnumRarity.UNCOMMON;
            case 2:
                return EnumRarity.RARE;
            case 3:
            case 4:
                return EnumRarity.EPIC;
            case 5:
            case 6:
            case 7:
            case 8:
                return TheTitans.godlyV;
            default:
                return EnumRarity.COMMON;
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return fetchKindIntMinion(stack) > 4 || stack.isItemEnchanted();
    }

    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote)
            return EnumActionResult.SUCCESS;
        BlockPos spawnPos = pos.offset(facing);
        double addY = 0.0D;
        if (facing == EnumFacing.UP && !world.isAirBlock(pos) && world.getBlockState(pos).isFullCube())
            addY = 0.5D;
        for (int i = 0; i < (player.isSneaking() ? 9 : 1); ++i) {
            EntityLiving living = spawnMobCreature(mobnameV.equals("WitherSkeletonMinion") ? "SkeletonMinion" : mobnameV, world, spawnPos.getX() + 0.5D, spawnPos.getY() + addY, spawnPos.getZ() + 0.5D);
            if (living != null) {
                if (stack.hasDisplayName())
                    living.setCustomNameTag(stack.getDisplayName());
                living.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(living)), (IEntityLivingData)null);
                if (living instanceof IMinion) {
                    if (living instanceof EntitySkeletonMinion && mobnameV.equals("WitherSkeletonMinion"))
                        ((EntitySkeletonMinion)living).setSkeletonType(1);
                    ((IMinion)living).setMinionType(fetchKindIntMinion(stack));
                    if (TheTitans.isHeraldLevel(fetchKindMinion(stack))) {
                        TheTitans.conjureMinion(mobnameV, world, spawnPos.getX(), spawnPos.getY() + addY, spawnPos.getZ(), EnumMinionType.GUARDIAN.getID(), 0, living.getRNG());
                        TheTitans.conjureMinion(mobnameV, world, spawnPos.getX(), spawnPos.getY() + addY, spawnPos.getZ(), EnumMinionType.GUARDIAN.getID(), 0, living.getRNG());
                    }
                }

                living.playLivingSound();
            }
        }

        if (!player.capabilities.isCreativeMode && world.getDifficulty() != EnumDifficulty.PEACEFUL)
            stack.shrink(1);
        return EnumActionResult.SUCCESS;
    }

    public static EntityLiving spawnMobCreature(String name, World world, double x, double y, double z) {
        if (world.isRemote)
            return null;
        Entity entity = EntityList.createEntityByIDFromName(new ResourceLocation(TheTitans.MODID_V, name), world);
        if (entity instanceof EntityLiving) {
            entity.setLocationAndAngles(x, y, z, world.rand.nextFloat() * 360.0F, 0.0F);
            world.spawnEntity(entity);
            return (EntityLiving)entity;
        }

        return null;
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab))
            return;
        for (int i = 0; i <= 8; ++i)
            items.add(new ItemStack(this, 1, i));
    }

    public EnumMinionType fetchKindMinion(ItemStack stack) {
        return IMinion.typesMinion(fetchKindIntMinion(stack));
    }

    public int fetchKindIntMinion(ItemStack stack) {
        return stack.getItemDamage();
    }
}
