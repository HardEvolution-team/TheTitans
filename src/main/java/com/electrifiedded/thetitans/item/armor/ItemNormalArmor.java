package com.electrifiedded.thetitans.item.armor;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.proxy.ClientProxy;
import com.electrifiedded.thetitans.registry.TitanGear;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemNormalArmor extends ItemArmor {
    protected String materialName;
    private static final String[] TYPES = {
        "_helmet", "_chestplate", "_leggings", "_boots"
    };
    private static final EntityEquipmentSlot[] SLOTS = {
        EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET
    };

    public ItemNormalArmor(String materialName, ItemArmor.ArmorMaterial material, int type) {
        super(material, 0, SLOTS[type]);
        this.materialName = materialName;
        String name = materialName + TYPES[type];
        setTranslationKey(name);
        setRegistryName(new ResourceLocation(TheTitans.MODID_V, name));
        setCreativeTab(TheTitans.armorsPageGiant);
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return TheTitans.getTextures("textures/models", "armor/" + materialName + "_layer_" + (armorType == EntityEquipmentSlot.LEGS ? "2" : "1") + ".png");
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (TheTitans.equippedAll(player, TitanGear.armourSetLead))
            player.removePotionEffect(ClientProxy.giantRadiationCreeper);
    }
}
