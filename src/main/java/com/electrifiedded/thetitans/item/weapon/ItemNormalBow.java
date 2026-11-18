package com.electrifiedded.thetitans.item.weapon;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.titan.EntityHarcadiumArrow;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class ItemNormalBow extends ItemBow {
    protected float paceArrow;
    protected Item stackArrow;
    protected Class<? extends EntityArrow> arrow;
    private List<String> information = new ArrayList<String>();
    protected String achievementV;

    public ItemNormalBow(String materialName, ToolMaterial material, Item stackArrow, Class<? extends EntityArrow> arrow, float paceArrow, String... information) {
        setTranslationKey(materialName + "_bow");
        setRegistryName(new ResourceLocation(TheTitans.MODID_V, materialName + "_bow"));
        setCreativeTab(TheTitans.weaponsPageGiant);
        this.paceArrow = paceArrow;
        this.arrow = arrow;
        this.stackArrow = stackArrow;
        setMaxDamage(material.getMaxUses());
    }

    public ItemNormalBow(String materialName, ToolMaterial material, float paceArrow, String... information) {
        this(materialName, material, null, null, paceArrow, information);
    }

    public ItemNormalBow(String materialName, ToolMaterial material, String... information) {
        this(materialName, material, null, null, 1.0F, information);
    }

    public ItemNormalBow(String materialName, ToolMaterial material, float paceArrow) {
        this(materialName, material, null, null, paceArrow);
    }

    public ItemNormalBow(String materialName, ToolMaterial material) {
        this(materialName, material, null, null, 1.0F);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, net.minecraft.client.util.ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        tooltip.addAll(information);
    }

    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        // TODO advancements — achievementV is now an advancement-id String; runGrant via CriteriaTriggers once advancements are added
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer)
            onFire(stack, worldIn, (EntityPlayer)entityLiving, timeLeft);
    }

    public void onFire(ItemStack stack, World worldIn, EntityPlayer playerIn, int useTime) {
        int j = stack.getMaxItemUseDuration() - useTime;
        ArrowLooseEvent event = new ArrowLooseEvent(playerIn, stack, worldIn, j, true);
        if (MinecraftForge.EVENT_BUS.post(event))
            return;
        j = event.getCharge();
        boolean flag = (playerIn.capabilities.isCreativeMode) || (EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0);
        if ((flag) || (fetchStackArrow() != null && arrowHas(playerIn))) {
            float f = j / 20.0F;
            f = (f * f + f * 2.0F) / 3.0F;
            if (f < 0.1D)
                return;
            if (f > 1.0F)
                f = 1.0F;
            EntityArrow entityarrow = getArrow(worldIn, playerIn, f * 2.0F);
            if (entityarrow == null)
                return;
            if (f == 1.0F)
                entityarrow.setIsCritical(true);
            int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
            if (k > 0)
                entityarrow.setDamage(entityarrow.getDamage() + (double)k + (double)getDamage());
            int l = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
            if (entityarrow instanceof EntityHarcadiumArrow) {
                entityarrow.setDamage(entityarrow.getDamage() + (double)k + (double)getDamage());
                ++l;
                if (((EntityHarcadiumArrow)entityarrow).fetchKindArrow() == 1)
                    l += 2;
            }
            if (l > 0)
                entityarrow.setKnockbackStrength(l * 3);
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0)
                entityarrow.setFire(500);
            stack.damageItem(1, playerIn);
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
            if (flag)
                entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
            else
                playerIn.inventory.clearMatchingItems(fetchStackArrow(), -1, 1, null);
            playerIn.addStat(StatList.getObjectUseStats(this));
            if (!worldIn.isRemote) {
                worldIn.spawnEntity(entityarrow);
                Vec3d vec3 = playerIn.getLook(1.0F);
                entityarrow.posX = (playerIn.posX + vec3.x);
                entityarrow.posY = (playerIn.posY + vec3.y + 1.5D);
                entityarrow.posZ = (playerIn.posZ + vec3.z);
            }
        }
    }

    protected boolean arrowHas(EntityPlayer player) {
        Item item = fetchStackArrow();
        for (ItemStack s : player.inventory.mainInventory)
            if (!s.isEmpty() && s.getItem() == item)
            return true;
        return false;
    }

    public ItemNormalBow assignAchievement(String achievementV) {
        this.achievementV = achievementV;
        return this;
    }

    public String fetchAchievement() {
        return achievementV;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        ArrowNockEvent event = new ArrowNockEvent(playerIn, itemStackIn, hand, worldIn, true);
        if (MinecraftForge.EVENT_BUS.post(event))
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn);
        if ((playerIn.capabilities.isCreativeMode) || arrowHas(playerIn))
            playerIn.setActiveHand(hand);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
    }

    public ItemNormalBow appendDetails(String... information) {
        if (information.length > 0)
            for (int i = 0; i < information.length; i++)
            if (information[i] != null)
            this.information.add(information[i]);
        return this;
    }

    protected float getDamage() {
        return 1.0F;
    }

    protected Item fetchStackArrow() {
        return stackArrow == null ? Items.ARROW : stackArrow;
    }

    protected EntityArrow getArrow(World world, EntityPlayer player, float speedMultiplier) {
        if (arrow == null) {
            // TODO 1.12.2 has no EntityArrow(World,EntityLivingBase,float); use tipped arrow default
            EntityArrow a = new net.minecraft.entity.projectile.EntityTippedArrow(world, player);
            a.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, paceArrow * speedMultiplier * 3.0F, 1.0F);
            return a;
        } else {
            EntityArrow newArrow;
            try {
                newArrow = arrow.getConstructor(World.class, EntityLivingBase.class, float.class).newInstance(world, player, 1.0F);
            }

            catch (Exception e) {
                newArrow = new net.minecraft.entity.projectile.EntityTippedArrow(world, player);
                TheTitans.error("Attempted to use an entity that does not extend EntityArrow(World, EntityLivingBase, float)", e);
            }

            if (newArrow instanceof EntityHarcadiumArrow) {
                if (fetchStackArrow() == com.electrifiedded.thetitans.registry.TitanGear.arrowVoid) {
                    ((EntityHarcadiumArrow)newArrow).setDamage(2.0E8D);
                    ((EntityHarcadiumArrow)newArrow).assignKindArrow(1);
                }
                ((EntityHarcadiumArrow)newArrow).setDamage(200000.0D);
            }
            newArrow.shoot(newArrow.motionX, newArrow.motionY, newArrow.motionZ, paceArrow * speedMultiplier * 1.5F, 1.0F);
            return newArrow;
        }
    }
}
