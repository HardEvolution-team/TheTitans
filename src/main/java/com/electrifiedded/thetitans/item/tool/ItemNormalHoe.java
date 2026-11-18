package com.electrifiedded.thetitans.item.tool;

import com.electrifiedded.thetitans.TheTitans;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.util.ResourceLocation;

import com.google.common.collect.Multimap;

public class ItemNormalHoe extends ItemHoe {
    private float field_150934_a;

    public ItemNormalHoe(String unlocalizedName, Item.ToolMaterial material) {
        super(material);
        setTranslationKey(unlocalizedName + "_hoe");
        setRegistryName(new ResourceLocation(TheTitans.MODID_V, unlocalizedName + "_hoe"));
        setCreativeTab(TheTitans.toolsPageGiant);
        this.field_150934_a = material.getAttackDamage();
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.field_150934_a, 0));
        }
        return multimap;
    }
}
