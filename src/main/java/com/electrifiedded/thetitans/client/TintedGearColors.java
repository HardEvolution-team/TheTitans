package com.electrifiedded.thetitans.client;

import com.electrifiedded.archaiclib.legacy.client.LegacyRenderPassColors;
import com.electrifiedded.thetitans.TheTitans;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

/**
 * Gear that the original drew from the shared grey "custom_*" textures, tinted per render pass with the
 * material colour (sword: blade, handle, hilt, butt; tools: head, handle; armour: piece, glint).
 * Their item models list one layer per pass; the colours below are the original material colours.
 */
public final class TintedGearColors {

    private TintedGearColors() {}

    public static void register() {
        tint("bone_axe", 0xE3DAC9, 0xFFC04A);
        tint("bone_boots", 0xE3DAC9, 0xFFC04A);
        tint("bone_chestplate", 0xE3DAC9, 0xFFC04A);
        tint("bone_helmet", 0xE3DAC9, 0xFFC04A);
        tint("bone_hoe", 0xE3DAC9, 0xFFC04A);
        tint("bone_leggings", 0xE3DAC9, 0xFFC04A);
        tint("bone_pickaxe", 0xE3DAC9, 0xFFC04A);
        tint("bone_spade", 0xE3DAC9, 0xFFC04A);
        tint("bone_sword", 0xE3DAC9, 0xFFC04A, 0xE3DAC9, 0xE3DAC9);
        tint("demontium_axe", 0x0000CC, 0x00FFF1);
        tint("demontium_hoe", 0x0000CC, 0x00FFF1);
        tint("demontium_pickaxe", 0x0000CC, 0x00FFF1);
        tint("demontium_spade", 0x0000CC, 0x00FFF1);
        tint("demontium_sword", 0x0000CC, 0x00FFF1, 0x0000CC, 0x0000CC);
        tint("obsidian_axe", 0x3C3056, 0xFFC04A);
        tint("obsidian_boots", 0x3C3056, 0xFFC04A);
        tint("obsidian_chestplate", 0x3C3056, 0xFFC04A);
        tint("obsidian_helmet", 0x3C3056, 0xFFC04A);
        tint("obsidian_hoe", 0x3C3056, 0xFFC04A);
        tint("obsidian_leggings", 0x3C3056, 0xFFC04A);
        tint("obsidian_pickaxe", 0x3C3056, 0xFFC04A);
        tint("obsidian_spade", 0x3C3056, 0xFFC04A);
        tint("onyx_axe", 0x1A1A1A, 0xFFC04A);
        tint("onyx_boots", 0x1A1A1A, -1);
        tint("onyx_chestplate", 0x1A1A1A, -1);
        tint("onyx_helmet", 0x1A1A1A, -1);
        tint("onyx_hoe", 0x1A1A1A, 0xFFC04A);
        tint("onyx_leggings", 0x1A1A1A, -1);
        tint("onyx_pickaxe", 0x1A1A1A, 0xFFC04A);
        tint("onyx_spade", 0x1A1A1A, 0xFFC04A);
        tint("onyx_sword", 0x1A1A1A, 0xFFC04A, 0x1A1A1A, 0x1A1A1A);
        tint("sapphire_axe", 0x009CFF, 0xFFC04A);
        tint("sapphire_boots", 0x009CFF, -1);
        tint("sapphire_chestplate", 0x009CFF, -1);
        tint("sapphire_helmet", 0x009CFF, -1);
        tint("sapphire_hoe", 0x009CFF, 0xFFC04A);
        tint("sapphire_leggings", 0x009CFF, -1);
        tint("sapphire_pickaxe", 0x009CFF, 0xFFC04A);
        tint("sapphire_spade", 0x009CFF, 0xFFC04A);
        tint("sapphire_sword", 0x009CFF, 0xFFC04A, 0x009CFF, 0x009CFF);
        tint("withrenium_boots", 0xFFFF00, -1);
        tint("withrenium_chestplate", 0xFFFF00, -1);
        tint("withrenium_helmet", 0xFFFF00, -1);
        tint("withrenium_leggings", 0xFFFF00, -1);
    }

    private static void tint(String name, int... passColors) {
        Item item = Item.REGISTRY.getObject(new ResourceLocation(TheTitans.MODID_V, name));
        if (item != null) LegacyRenderPassColors.register(item, passColors);
        else TheTitans.logger.warn("Tinted gear item not registered: " + name);
    }
}
