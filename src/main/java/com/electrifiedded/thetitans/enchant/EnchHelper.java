package com.electrifiedded.thetitans.enchant;

import net.minecraft.enchantment.Enchantment;

// maps the old 1.7.10 int weight to 1.12.2 Enchantment.Rarity (weights: COMMON=10, UNCOMMON=5, RARE=2, VERY_RARE=1)
final class EnchHelper {
    private EnchHelper() {}

    static Enchantment.Rarity rarity(int weight) {
        if (weight >= 10) return Enchantment.Rarity.COMMON;
        if (weight >= 5) return Enchantment.Rarity.UNCOMMON;
        if (weight >= 2) return Enchantment.Rarity.RARE;
        return Enchantment.Rarity.VERY_RARE;
    }
}
