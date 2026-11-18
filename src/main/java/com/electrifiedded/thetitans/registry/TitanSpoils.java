package com.electrifiedded.thetitans.registry;

import com.electrifiedded.thetitans.TheTitans;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryEmpty;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Port of the original's CommonProxy ChestGenHooks.addItem(...) calls (1.7.10 CommonProxy:45-65).
 *
 * 1.7.10 appended each item to the chest's shared weighted list, which was then rolled N times. 1.12.2 tables are
 * pools, so each vanilla table gets one extra "thetitans" pool that rolls the same N times as the 1.7.10 chest and
 * contains the original entries plus an empty entry weighted like the 1.7.10 vanilla list. That keeps the expected amount
 * of each Titans item per chest close to the original instead of guaranteeing one in every chest.
 */
@Mod.EventBusSubscriber(modid = TheTitans.MODID_V)
public final class TitanSpoils {
    private TitanSpoils() {}

    private static final LootCondition[] CONDITIONS_NO = new LootCondition[0];

    @SubscribeEvent
    public static void whenTableLoadLoot(LootTableLoadEvent event) {
        ResourceLocation name = event.getName();
        List<LootEntry> entries = new ArrayList<LootEntry>();
        int rolls;
        int vanillaWeight;

        if (LootTableList.CHESTS_SPAWN_BONUS_CHEST.equals(name)) {
            rolls = 10; vanillaWeight = 64;
            Item[] tools = {TitanGear.pickaxeBone, TitanGear.axeBone, TitanGear.spadeBone, TitanGear.pickaxeTin, TitanGear.axeTin, TitanGear.spadeTin, TitanGear.pickaxeCopper, TitanGear.axeCopper, TitanGear.spadeCopper};
            for (Item tool : tools)
                entries.add(item(tool, 10, 1, 1));
        } else if (LootTableList.CHESTS_ABANDONED_MINESHAFT.equals(name)) {
            rolls = 5; vanillaWeight = 79;
            entries.add(item(TitanGear.bladeSeedPleasant, 10, 2, 4));
            entries.add(item(TitanGear.seedsMalgrum, 1, 1, 1));
        } else if (LootTableList.CHESTS_STRONGHOLD_CROSSING.equals(name) || LootTableList.CHESTS_STRONGHOLD_CORRIDOR.equals(name)) {
            rolls = 3; vanillaWeight = 99;
            entries.add(item(TitanGear.bladeSeedPleasant, 10, 1, 4));
            entries.add(item(TitanGear.seedsMalgrum, 1, 1, 1));
        } else if (LootTableList.CHESTS_SIMPLE_DUNGEON.equals(name)) {
            rolls = 8; vanillaWeight = 116;
            entries.add(item(TitanGear.bladeSeedPleasant, 10, 1, 4));
        } else if (LootTableList.CHESTS_DESERT_PYRAMID.equals(name)) {
            rolls = 4; vanillaWeight = 130;
            entries.add(item(TitanGear.bladeSeedPleasant, 10, 1, 4));
        } else if (LootTableList.CHESTS_VILLAGE_BLACKSMITH.equals(name)) {
            rolls = 5; vanillaWeight = 94;
            entries.add(item(TitanGear.bladeSeedPleasant, 10, 2, 4));
            entries.add(item(TitanGear.bladeLeafPleasant, 10, 2, 4));
            entries.add(item(TitanGear.bladeFlowerPleasant, 2, 1, 1));
            entries.add(item(TitanGear.bladeBrewPleasant, 2, 1, 1));
        } else
                return;

        entries.add(new LootEntryEmpty(vanillaWeight, 0, CONDITIONS_NO, "thetitans_empty"));
        event.getTable().addPool(new LootPool(entries.toArray(new LootEntry[0]), CONDITIONS_NO, new RandomValueRange(rolls), new RandomValueRange(0), "thetitans"));
    }

    private static LootEntry item(Item item, int weight, int min, int max) {
        LootFunction[] functions = min == 1 && max == 1 ? new LootFunction[0] : new LootFunction[] {new SetCount(CONDITIONS_NO, new RandomValueRange(min, max))};
        return new LootEntryItem(item, weight, 0, functions, CONDITIONS_NO, "thetitans_" + item.getRegistryName().getPath());
    }
}
