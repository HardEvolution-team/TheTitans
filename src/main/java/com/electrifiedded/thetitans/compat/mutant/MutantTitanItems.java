package com.electrifiedded.thetitans.compat.mutant;

import java.util.ArrayList;
import java.util.List;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.item.armor.ItemTitanArmor;
import com.electrifiedded.thetitans.item.material.ItemMaterial;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Titanic Mutant Skeleton bones and armour (only with Mutant Creatures installed). The bones come from the giant
 * parts a Mutant Skeleton Titan falls apart into; recipes mirror the mutant's own (6 ribs = rib cage, 2 shoulder
 * pads + 4 limbs = arms, arms + rib cage = chestplate, pelvis + 2 limbs = leggings, 2 limbs = boots, skull =
 * helmet), so one titan gives one full set. Textures and the skull model are the installed mutant mod's.
 */
@Mod.EventBusSubscriber(modid = TheTitans.MODID_V)
public final class MutantTitanItems {
    private MutantTitanItems() {
    }

    public static final String PREFIX = "titanic_mutant_skeleton";
    public static final ItemMaterial MATERIAL = new ItemMaterial("TitanicMutantSkeletonArmor", 24000000, 2400, 7200, 5400, 2400, 40);

    public static Item pelvis, rib, limb, shoulderPad, ribCage, arms;
    public static ItemBoneArmor skull, chestplate, leggings, boots;
    public static final List<Item> ALL = new ArrayList<>();

    /** Item a giant bone part turns into (part ids as the mutant's body parts). */
    public static Item boneItem(int part) {
        if (part == 0)
            return pelvis;
        if (part >= 1 && part < 19)
            return rib;
        if (part == 19)
            return skull;
        if (part >= 21 && part < 29)
            return limb;
        if (part == 29 || part == 30)
            return shoulderPad;
        return null;
    }

    private static Item bone(String name) {
        Item it = new Item() {
            @Override
            public EnumRarity getRarity(ItemStack stack) {
                return EnumRarity.EPIC;
            }

            @Override
            public boolean hasEffect(ItemStack stack) {
                return true;
            }
        };
        it.setRegistryName(new ResourceLocation(TheTitans.MODID_V, PREFIX + "_" + name));
        it.setTranslationKey(PREFIX + "_" + name);
        it.setCreativeTab(TheTitans.armorsPageGiant);
        ALL.add(it);
        return it;
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        if (!MutantTitans.loaded())
            return;
        pelvis = bone("pelvis");
        rib = bone("rib");
        limb = bone("limb");
        shoulderPad = bone("shoulder_pad");
        ribCage = bone("rib_cage");
        arms = bone("arms");
        skull = new ItemBoneArmor(0);
        chestplate = new ItemBoneArmor(1);
        leggings = new ItemBoneArmor(2);
        boots = new ItemBoneArmor(3);
        ALL.add(skull);
        ALL.add(chestplate);
        ALL.add(leggings);
        ALL.add(boots);
        for (Item it : ALL)
            event.getRegistry().register(it);
        MATERIAL.getArmorMaterial().setRepairItem(new ItemStack(rib));
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        if (!MutantTitans.loaded() || rib == null)
            return;
        recipe(event, ribCage, "RR", "RR", "RR", 'R', rib);
        recipe(event, arms, "SS", "LL", "LL", 'S', shoulderPad, 'L', limb);
        recipe(event, chestplate, "A", "R", 'A', arms, 'R', ribCage);
        recipe(event, leggings, " P ", "L L", 'P', pelvis, 'L', limb);
        recipe(event, boots, "LL", 'L', limb);
    }

    private static void recipe(RegistryEvent.Register<IRecipe> event, Item out, Object... params) {
        ResourceLocation id = out.getRegistryName();
        event.getRegistry().register(new ShapedOreRecipe(id, new ItemStack(out), params).setRegistryName(id));
    }

    /** Titanic Mutant Skeleton armour: between Withrenium and Titanus, with the mutant armour's perks grown up. */
    public static class ItemBoneArmor extends ItemTitanArmor {
        private final int type;

        ItemBoneArmor(int type) {
            super(PREFIX, MATERIAL.getArmorMaterial(), type, new double[] { 1500000.0D, 2400000.0D, 2100000.0D, 1200000.0D }, 9.0F, 1.0E-4F);
            this.type = type;
        }

        @Override
        public EnumRarity getRarity(ItemStack stack) {
            return TheTitans.epicSuper;
        }

        @Override
        public boolean hasEffect(ItemStack stack) {
            return true;
        }

        @Override
        public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
            return repair.getItem() == rib;
        }

        @Override
        public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String layer) {
            return MutantTitans.MODID + ":textures/models/armor/mutant_skeleton_layer_" + (type == 2 ? 2 : 1) + ".png";
        }

        @Override
        @SideOnly(Side.CLIENT)
        public ModelBiped getArmorModel(EntityLivingBase entity, ItemStack stack, EntityEquipmentSlot slot, ModelBiped def) {
            if (slot != EntityEquipmentSlot.HEAD)
                return def;
            try {
                Object proxy = Class.forName("chumbanotz.mutantbeasts.MutantBeasts").getField("PROXY").get(null);
                Object m = proxy.getClass().getMethod("getMutantSkeletonArmorModel").invoke(proxy);
                return m instanceof ModelBiped ? (ModelBiped)m : def;
            } catch (Throwable t) {
                return def;
            }
        }

        @Override
        public void armourTick(World world, net.minecraft.entity.EntityLivingBase player, ItemStack stack) {
            switch (type) {
                case 0: // skull: sees in the dark, never dizzy
                    effect(player, MobEffects.NIGHT_VISION, 0, 300);
                    player.removePotionEffect(MobEffects.NAUSEA);
                    player.removePotionEffect(MobEffects.BLINDNESS);
                    break;
                case 1: // rib cage: a cage of titan bone
                    effect(player, MobEffects.RESISTANCE, 3, 40);
                    effect(player, MobEffects.STRENGTH, 5, 40);
                    player.removePotionEffect(MobEffects.WEAKNESS);
                    break;
                case 2: // the mutant's speed legs
                    effect(player, MobEffects.SPEED, player.isSprinting() ? 5 : 3, 40);
                    player.removePotionEffect(MobEffects.SLOWNESS);
                    break;
                case 3: // the mutant's jump boots, no fall damage
                    effect(player, MobEffects.JUMP_BOOST, player.isSprinting() ? 4 : 2, 40);
                    player.fallDistance = 0.0F;
                    break;
            }
            if (type == 1 && isFullSet(player)) {
                effect(player, MobEffects.REGENERATION, 2, 60);
                effect(player, MobEffects.FIRE_RESISTANCE, 0, 60);
                effect(player, MobEffects.ABSORPTION, 4, 400);
            }
            super.armourTick(world, player, stack);
        }

        private static void effect(EntityLivingBase p, Potion potion, int amp, int time) {
            PotionEffect cur = p.getActivePotionEffect(potion);
            if (cur == null || cur.getAmplifier() < amp || cur.getDuration() < time / 2)
                p.addPotionEffect(new PotionEffect(potion, time, amp, true, false));
        }

        public static boolean isFullSet(EntityLivingBase e) {
            for (EntityEquipmentSlot s : SLOTS)
                if (!(e.getItemStackFromSlot(s).getItem() instanceof ItemBoneArmor))
                    return false;
            return true;
        }
    }
}
