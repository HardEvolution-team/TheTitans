package com.electrifiedded.thetitans.item.armor;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.network.NetworkHandler;
import com.electrifiedded.thetitans.network.packet.PacketPlayerAlive;
import com.electrifiedded.thetitans.proxy.CommonProxy;
import com.electrifiedded.thetitans.util.Maths;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.List;

public class ItemTitanArmor extends ItemArmor {
    // map int type (0=helmet,1=chestplate,2=leggings,3=boots) to 1.12.2 EntityEquipmentSlot
    protected static final EntityEquipmentSlot[] SLOTS = { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET };
    private AttributeModifier helmetModifier;
    private AttributeModifier chestplateModifier;
    private AttributeModifier leggingsModifier;
    private AttributeModifier bootsModifier;
    private String materialName;
    private int kindIndexArmour;
    private String types[] = {
        "_helmet", "_chestplate", "_leggings", "_boots"
    };

    private float armourGiant;
    private float resistanceGiant;
    private int delay;
    protected int beatsAudio;

    public ItemTitanArmor(String materialName, ItemArmor.ArmorMaterial material, int type, double[] maxHealth, float armourGiant, float resistanceGiant) {
        super(material, 0, SLOTS[type]);
        this.materialName = materialName;
        this.kindIndexArmour = type;
        this.armourGiant = armourGiant;
        this.resistanceGiant = resistanceGiant;
        materialName = materialName + types[type];
        setTranslationKey(materialName);
        setRegistryName(new net.minecraft.util.ResourceLocation(TheTitans.MODID_V, materialName));
        setCreativeTab(TheTitans.armorsPageGiant);
        if (maxHealth.length > 3) {
            helmetModifier = new AttributeModifier("Helmet modifier", maxHealth[0], 0);
            chestplateModifier = new AttributeModifier("Chestplate modifier", maxHealth[1], 0);
            leggingsModifier = new AttributeModifier("Leggings modifier", maxHealth[2], 0);
            bootsModifier = new AttributeModifier("Boots modifier", maxHealth[3], 0);
        }
    }

    protected float fetchArmour(EntityLivingBase entity) {
        float armor = 0.0F;
        for (EntityEquipmentSlot slot : SLOTS)
            if (entity != null && !entity.getItemStackFromSlot(slot).isEmpty() && entity.getItemStackFromSlot(slot).getItem() instanceof ItemTitanArmor)
            armor += ((ItemTitanArmor)entity.getItemStackFromSlot(slot).getItem()).armourGiant;
        return armor;
    }

    public void attackEntity(ItemStack stack, EntityLivingBase entity, float damage) {
        if (!(entity == null) && !entity.world.isRemote && damage > 0.0F) {
            float armor = fetchArmour(entity);
            @SuppressWarnings("unused") float totalHealth = MathHelper.clamp((entity.getMaxHealth() * 10.0F) + (float) Maths.runGrowth(175000.0F, 0.325F, armor), 0.0F, Float.MAX_VALUE);
            if (damage > 0.0F && resistanceGiant > 0.0F)
                damage *= resistanceGiant;
            float ceil = armor > 24.0F ? armor + 1.0F : 25.0F;
            damage = (ceil - armor * damage) / ceil;
            // BUGFIX — titan hits reach Float.MAX_VALUE x9 = Infinity; with 0 armor this formula gives
            // 0*Inf = NaN and setHealth(NaN) left the player neither alive nor dead (persisted through relog).
            if (Float.isNaN(damage) || Float.isInfinite(damage))
                return;
            // Never kill from here: setHealth(<=0) skips onDeath entirely (and vanilla attackEntityFrom then bails on
            // "already at 0 HP"), which stranded the player at 0 HP. The vanilla hit that follows kills properly.
            float health = entity.getHealth() - damage;
            if (health < 1.0F)
                health = Math.min(entity.getHealth(), 1.0F);
            entity.setHealth(health);
            int armordamage = (int)damage / 4;
            if (armordamage >= 1 && entity.getAbsorptionAmount() <= 0.0F)
                stack.damageItem(armordamage, entity);
        }
    }

    protected ItemStack whenNBT(ItemStack stack, EntityLivingBase entity) {
        NBTTagCompound nbt;
        if (stack != null && !stack.isEmpty()) {
            if (stack.hasTagCompound())
                nbt = stack.getTagCompound();
            else
                nbt = new NBTTagCompound();
            if (!nbt.hasKey("health"))
                nbt.setFloat("health", entity.getMaxHealth());
            stack.setTagCompound(nbt);
        }

        return stack;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (stack.getItem() instanceof ItemTitanArmor) {
            tooltip.add(TextFormatting.BLUE + "+" + (int)((ItemTitanArmor)stack.getItem()).armourGiant + " Titan Armor");
            if (resistanceGiant > 0.0F)
                tooltip.add(TextFormatting.BLUE + "+" + (int)(((ItemTitanArmor)stack.getItem()).resistanceGiant * 100.0F) + "% Titan Resistance");
        }
    }

    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();
        if (equipmentSlot == this.armorType) {
            switch (kindIndexArmour) {
                case 0:
                    multimap.put(SharedMonsterAttributes.MAX_HEALTH.getName(), helmetModifier); break;
                case 1:
                    multimap.put(SharedMonsterAttributes.MAX_HEALTH.getName(), chestplateModifier); break;
                case 2:
                    multimap.put(SharedMonsterAttributes.MAX_HEALTH.getName(), leggingsModifier); break;
                case 3:
                    multimap.put(SharedMonsterAttributes.MAX_HEALTH.getName(), bootsModifier); break;
                default:;
            }
        }

        return multimap;
    }

    public boolean getIsRepairable(ItemStack arg82789n1, ItemStack arg82789n2) {
        return false;
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return TheTitans.getTextures("textures/models", "armor/" + materialName + "_layer_" + (kindIndexArmour == 2 ? "2" : "1") + ".png");
    }

    /**Runs per armor set to play sounds of your choosing*/
    public void whenTickAudio(World world, EntityLivingBase entity, ItemStack itemStack) {
        beatsAudio ++;
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        // 1.7.10 base behaviour: all titan armor slowly self-repairs (maxDamage/100000 per tick).
        if (getDamage(itemStack) > 0 && getDamage(itemStack) < getMaxDamage() - 1)
            setDamage(itemStack, getDamage(itemStack) - (getMaxDamage() / 100000));
        if (delay > 0)
            delay--;
        if (player != null && !world.isRemote) {
            discardEffect(player, com.electrifiedded.thetitans.proxy.CommonProxy.bleeding);
            player.hurtResistantTime = 0;
            player.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
            if (itemStack != null && !itemStack.isEmpty())
                itemStack = whenNBT(itemStack, player);
            if (getHealth(itemStack) > player.getMaxHealth())
                setHealth(itemStack, player.getMaxHealth());
            if (player.isEntityAlive() && getHealth(itemStack) > 0.0F && player.getHealth() < player.getMaxHealth())
                player.heal(player.getMaxHealth() * 1.0E-4F);
        }
    }

    protected void discardEffect(EntityLivingBase entity, Potion potion) {
        if (entity != null && (entity.getActivePotionEffect(potion) != null)) {
            entity.removePotionEffect(potion);
            entity.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.5F, 2.0F);
        }
    }

    protected void addEffect(EntityLivingBase entity, Potion potion, int time, int amplifier) {
        if (entity != null && (entity.getActivePotionEffect(potion) == null || entity.getActivePotionEffect(potion).getDuration() <= 1))
            entity.addPotionEffect(new PotionEffect(potion, time, amplifier, false, false));
    }

    protected void addEffect(EntityLivingBase entity, Potion potion, int amplifier) {
        if (entity != null && (entity.getActivePotionEffect(potion) == null || entity.getActivePotionEffect(potion).getDuration() <= 1))
            entity.addPotionEffect(new PotionEffect(potion, 1, amplifier, false, false));
    }

    protected void appendLongEffect(EntityLivingBase entity, Potion potion, int amplifier) {
        if (entity != null && (entity.getActivePotionEffect(potion) == null || entity.getActivePotionEffect(potion).getDuration() <= 220))
            entity.addPotionEffect(new PotionEffect(potion, 800, amplifier, false, false));
    }

    public float fetchArmourGiant() {
        return armourGiant;
    }

    public float fetchResistanceGiant() {
        return resistanceGiant;
    }

    public float getHealth(ItemStack stack) {
        return stack != null && !stack.isEmpty() && stack.hasTagCompound() && stack.getTagCompound().hasKey("health") ? stack.getTagCompound().getFloat("health") : 20.0F;
    }

    protected void assignAllHp(EntityLivingBase entity, float health) {
        if (entity != null) {
            for (EntityEquipmentSlot slot : SLOTS)
                if (!entity.getItemStackFromSlot(slot).isEmpty() && entity.getItemStackFromSlot(slot).getItem() instanceof ItemTitanArmor)
                ((ItemTitanArmor)entity.getItemStackFromSlot(slot).getItem()).setHealth(entity.getItemStackFromSlot(slot), health);
        }
    }

    protected void setHealth(ItemStack stack, float health) {
        if (stack != null && !stack.isEmpty() && stack.hasTagCompound() && stack.getTagCompound().hasKey("health"))
            stack.getTagCompound().setFloat("health", health);
    }

    protected float cap() {
        return Float.MAX_VALUE;
    }
}
