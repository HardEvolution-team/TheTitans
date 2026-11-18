package com.electrifiedded.thetitans.registry;

import com.electrifiedded.thetitans.TheTitans;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

public final class RecipeForge {
    private static final String[] patternsRecipe = new String[] {
        "X,X,#",        // 0 sword
                "XXX, # , # ",  // 1 pickaxe
                "X,#,#",        // 2 spade
                "XX,X#, #",     // 3 axe
                "XX, #, #",     // 4 hoe
                "XXX,X X",      // 5 helmet
                "X X,XXX,XXX",  // 6 chestplate
                "XXX,X X,X X",  // 7 leggings
                "X X,X X"       // 8 boots
    };
    private static final Item[] stuffObsidian = new Item[]{TitanGear.swordObsidian, TitanGear.pickaxeObsidian, TitanGear.spadeObsidian, TitanGear.axeObsidian, TitanGear.hoeObsidian, TitanGear.helmetObsidian, TitanGear.chestplateObsidian, TitanGear.leggingsObsidian, TitanGear.bootsObsidian};
    private static final Item[] stuffOnyx = new Item[]{TitanGear.swordOnyx, TitanGear.pickaxeOnyx, TitanGear.spadeOnyx, TitanGear.axeOnyx, TitanGear.hoeOnyx, TitanGear.helmetOnyx, TitanGear.chestplateOnyx, TitanGear.leggingsOnyx, TitanGear.bootsOnyx};
    private static final Item[] stuffSapphire = new Item[]{TitanGear.swordSapphire, TitanGear.pickaxeSapphire, TitanGear.spadeSapphire, TitanGear.axeSapphire, TitanGear.hoeSapphire, TitanGear.helmetSapphire, TitanGear.chestplateSapphire, TitanGear.leggingsSapphire, TitanGear.bootsSapphire};
    private static final Item[] stuffBone = new Item[]{TitanGear.swordBone, TitanGear.pickaxeBone, TitanGear.spadeBone, TitanGear.axeBone, TitanGear.hoeBone, TitanGear.helmetBone, TitanGear.chestplateBone, TitanGear.leggingsBone, TitanGear.bootsBone};
    private static final Item[] stuffCopper = new Item[]{TitanGear.swordCopper, TitanGear.pickaxeCopper, TitanGear.spadeCopper, TitanGear.axeCopper, TitanGear.hoeCopper, TitanGear.helmetCopper, TitanGear.chestplateCopper, TitanGear.leggingsCopper, TitanGear.bootsCopper};
    private static final Item[] stuffTin = new Item[]{TitanGear.swordTin, TitanGear.pickaxeTin, TitanGear.spadeTin, TitanGear.axeTin, TitanGear.hoeTin, TitanGear.helmetTin, TitanGear.chestplateTin, TitanGear.leggingsTin, TitanGear.bootsTin};
    private static final Item[] stuffBronze = new Item[]{TitanGear.swordBronze, TitanGear.pickaxeBronze, TitanGear.spadeBronze, TitanGear.axeBronze, TitanGear.hoeBronze, TitanGear.helmetBronze, TitanGear.chestplateBronze, TitanGear.leggingsBronze, TitanGear.bootsBronze};
    private static final Item[] stuffSilver = new Item[]{TitanGear.swordSilver, TitanGear.pickaxeSilver, TitanGear.spadeSilver, TitanGear.axeSilver, TitanGear.hoeSilver, TitanGear.helmetSilver, TitanGear.chestplateSilver, TitanGear.leggingsSilver, TitanGear.bootsSilver};
    private static final Item[] stuffLead = new Item[]{TitanGear.swordLead, TitanGear.pickaxeLead, TitanGear.spadeLead, TitanGear.axeLead, TitanGear.hoeLead, TitanGear.helmetLead, TitanGear.chestplateLead, TitanGear.leggingsLead, TitanGear.bootsLead};
    private static final Item[] stuffSteel = new Item[]{TitanGear.swordSteel, TitanGear.pickaxeSteel, TitanGear.spadeSteel, TitanGear.axeSteel, TitanGear.hoeSteel, TitanGear.helmetSteel, TitanGear.chestplateSteel, TitanGear.leggingsSteel, TitanGear.bootsSteel};
    private static final Item[] stuffPlatinum = new Item[]{TitanGear.swordPlatinum, TitanGear.pickaxePlatinum, TitanGear.spadePlatinum, TitanGear.axePlatinum, TitanGear.hoePlatinum, TitanGear.helmetPlatinum, TitanGear.chestplatePlatinum, TitanGear.leggingsPlatinum, TitanGear.bootsPlatinum};
    private static final Item[] stuffTitanium = new Item[]{TitanGear.swordTitanium, TitanGear.pickaxeTitanium, TitanGear.spadeTitanium, TitanGear.axeTitanium, TitanGear.hoeTitanium, TitanGear.helmetTitanium, TitanGear.chestplateTitanium, TitanGear.leggingsTitanium, TitanGear.bootsTitanium};
    private static final Item[] ironStuffPig = new Item[]{TitanGear.ironSwordPig, TitanGear.ironPickaxePig, TitanGear.ironSpadePig, TitanGear.ironAxePig, TitanGear.ironHoePig, TitanGear.ironHelmetPig, TitanGear.ironChestplatePig, TitanGear.ironLeggingsPig, TitanGear.ironBootsPig};
    private static final Item[] stuffRuby = new Item[]{TitanGear.swordRuby, TitanGear.pickaxeRuby, TitanGear.spadeRuby, TitanGear.axeRuby, TitanGear.hoeRuby, TitanGear.helmetRuby, TitanGear.chestplateRuby, TitanGear.leggingsRuby, TitanGear.bootsRuby};
    private static final Item[] stuffDemontium = new Item[]{TitanGear.swordDemontium, TitanGear.pickaxeDemontium, TitanGear.spadeDemontium, TitanGear.axeDemontium, TitanGear.hoeDemontium, TitanGear.helmetDemontium, TitanGear.chestplateDemontium, TitanGear.leggingsDemontium, TitanGear.bootsDemontium};
    private static final Item[] stuffHarcadium = new Item[]{TitanGear.harcadiumSword, TitanGear.pickaxeHarcadium, TitanGear.spadeHarcadium, TitanGear.axeHarcadium, TitanGear.hoeHarcadium, TitanGear.helmetHarcadium, TitanGear.chestplateHarcadium, TitanGear.leggingsHarcadium, TitanGear.bootsHarcadium};
    private static final Item[] stuffWithrenium = new Item[]{TitanGear.swordWithrenium, TitanGear.pickaxeWithrenium, TitanGear.spadeWithrenium, TitanGear.axeWithrenium, TitanGear.hoeWithrenium, TitanGear.helmetWithrenium, TitanGear.chestplateWithrenium, TitanGear.leggingsWithrenium, TitanGear.bootsWithrenium};
    private static final Item[] stuffTitanus = new Item[]{TitanGear.swordTitanus, TitanGear.pickaxeTitanus, TitanGear.spadeTitanus, TitanGear.axeTitanus, TitanGear.hoeTitanus, TitanGear.helmetTitanus, TitanGear.chestplateTitanus, TitanGear.leggingsTitanus, TitanGear.bootsTitanus};
    private static final Item[] stuffVoid = new Item[]{TitanGear.voidSword, TitanGear.pickaxeVoid, TitanGear.spadeVoid, TitanGear.axeVoid, TitanGear.hoeVoid, TitanGear.helmetVoid, TitanGear.chestplateVoid, TitanGear.leggingsVoid, TitanGear.bootsVoid};

    public static void initCrafting() {
        oreSmelt(TitanBlockSet.oreCopperZ, TitanGear.ingotCopperZ, 2);
        oreSmelt(TitanBlockSet.oreTinZ, TitanGear.ingotTinZ, 2);
        oreSmelt(TitanBlockSet.oreChromiumZ, TitanGear.ingotChromiumZ, 3);
        oreSmelt(TitanBlockSet.oreNickelZ, TitanGear.ingotNickelZ, 3);
        oreSmelt(TitanBlockSet.oreLeadZ, TitanGear.ingotLeadZ, 3);
        oreSmelt(TitanBlockSet.oreSilverZ, TitanGear.ingotSilverZ, 5);
        oreSmelt(TitanBlockSet.orePlatinumZ, TitanGear.ingotPlatinumZ, 10);
        oreSmelt(TitanBlockSet.oreTitaniumZ, TitanGear.titanium_ingot, 10);
        oreSmelt(TitanBlockSet.oreCoalZ, Items.COAL, 2);
        oreSmelt(TitanBlockSet.oreIronZ, Items.IRON_INGOT, 2);
        oreSmelt(TitanBlockSet.oreGoldZ, Items.GOLD_INGOT, 2);
        oreSmelt(TitanBlockSet.oreRedstoneZ, Items.REDSTONE, 3);
        oreSmelt(TitanBlockSet.oreDiamondZ, Items.DIAMOND, 5);
        oreSmelt(TitanBlockSet.oreEmeraldZ, Items.EMERALD, 5);
        oreSmelt(TitanBlockSet.oreJadeiteZ, TitanGear.jadeite, 5);
        oreSmelt(TitanBlockSet.fuelOreMolten, TitanGear.moltenFuel, 2);
        oreSmelt(TitanBlockSet.ironOrePig, TitanGear.ironIngotPig, 2);
        oreSmelt(TitanBlockSet.oreRubyZ, TitanGear.ruby, 5);
        oreSmelt(TitanBlockSet.oreSapphireZ, TitanGear.sapphire, 5);
        oreSmelt(TitanBlockSet.oreOnyxZ, TitanGear.onyx, 5);
        oreSmelt(TitanBlockSet.oreDemontiumZ, TitanGear.ingotDemontiumZ, 50);
        oreSmelt(TitanBlockSet.oreTitanusZ, TitanGear.ingotTitanusZ, 12000);

        // Special shaped recipes
        shapedCrafting("magic_pumpkin", TitanBlockSet.magic_pumpkin, " R ,RPR, R ", 'R', Items.REDSTONE, 'P', Blocks.PUMPKIN);
        shapedCrafting("sacred_sword", TitanGear.swordSacred, "GLG,LSL,DRA", 'G', Items.GOLD_INGOT, 'L', TitanGear.ingotSteelZ, 'S', TitanGear.swordSilver, 'D', Items.DIAMOND, 'R', TitanGear.ruby, 'A', TitanGear.sapphire);
        shapedCrafting("egg_iron_golem_better", TitanGear.ironGolemBetterOvum, " P ,III, I ", 'P', TitanBlockSet.magic_pumpkin, 'I', TitanBlockSet.steel_block);
        shapedCrafting("torch_molten_fuel", Blocks.TORCH, 6, "#,X", '#', TitanGear.moltenFuel, 'X', Items.STICK);
        shapedCrafting("ender_pearl_harcadium", Items.ENDER_PEARL, 64, "HHH,H H,HHH", 'H', TitanGear.wafletHarcadiumZ);

        // Golden / diamond / jadeite food upgrades ("GGG,GPG,GGG" around the base food)
        shapedCrafting("golden_cookie_gold", TitanGear.cookieGolden, 1, 0, "GGG,GPG,GGG", 'G', Items.GOLD_INGOT, 'P', Items.COOKIE);
        shapedCrafting("golden_cookie_gold_block", TitanGear.cookieGolden, 1, 1, "GGG,GPG,GGG", 'G', Blocks.GOLD_BLOCK, 'P', Items.COOKIE);
        shapedCrafting("golden_bread_gold", TitanGear.breadGolden, 1, 0, "GGG,GPG,GGG", 'G', Items.GOLD_INGOT, 'P', Items.BREAD);
        shapedCrafting("golden_bread_gold_block", TitanGear.breadGolden, 1, 1, "GGG,GPG,GGG", 'G', Blocks.GOLD_BLOCK, 'P', Items.BREAD);
        shapedCrafting("golden_potato_gold", TitanGear.potatoeGolden, 1, 0, "GGG,GPG,GGG", 'G', Items.GOLD_INGOT, 'P', Items.BAKED_POTATO);
        shapedCrafting("golden_potato_gold_block", TitanGear.potatoeGolden, 1, 1, "GGG,GPG,GGG", 'G', Blocks.GOLD_BLOCK, 'P', Items.BAKED_POTATO);
        shapedCrafting("golden_melon_gold", TitanGear.melonGolden, 1, 0, "GGG,GPG,GGG", 'G', Items.GOLD_INGOT, 'P', Items.MELON);
        shapedCrafting("golden_melon_gold_block", TitanGear.melonGolden, 1, 1, "GGG,GPG,GGG", 'G', Blocks.GOLD_BLOCK, 'P', Items.MELON);
        shapedCrafting("golden_pumpkin_pie_gold", TitanGear.pumpkinPieGolden, 1, 0, "GGG,GPG,GGG", 'G', Items.GOLD_INGOT, 'P', Items.PUMPKIN_PIE);
        shapedCrafting("golden_pumpkin_pie_gold_block", TitanGear.pumpkinPieGolden, 1, 1, "GGG,GPG,GGG", 'G', Blocks.GOLD_BLOCK, 'P', Items.PUMPKIN_PIE);

        shapedCrafting("diamond_apple_diamond", TitanGear.appleDiamond, 1, 0, "GGG,GPG,GGG", 'G', Items.DIAMOND, 'P', Items.APPLE);
        shapedCrafting("diamond_apple_diamond_block", TitanGear.appleDiamond, 1, 1, "GGG,GPG,GGG", 'G', Blocks.DIAMOND_BLOCK, 'P', Items.APPLE);
        shapedCrafting("diamond_cookie_diamond", TitanGear.cookieDiamond, 1, 0, "GGG,GPG,GGG", 'G', Items.DIAMOND, 'P', Items.COOKIE);
        shapedCrafting("diamond_cookie_diamond_block", TitanGear.cookieDiamond, 1, 1, "GGG,GPG,GGG", 'G', Blocks.DIAMOND_BLOCK, 'P', Items.COOKIE);
        shapedCrafting("diamond_bread_diamond", TitanGear.breadDiamond, 1, 0, "GGG,GPG,GGG", 'G', Items.DIAMOND, 'P', Items.BREAD);
        shapedCrafting("diamond_bread_diamond_block", TitanGear.breadDiamond, 1, 1, "GGG,GPG,GGG", 'G', Blocks.DIAMOND_BLOCK, 'P', Items.BREAD);
        shapedCrafting("diamond_potato_diamond", TitanGear.potatoeDiamond, 1, 0, "GGG,GPG,GGG", 'G', Items.DIAMOND, 'P', Items.BAKED_POTATO);
        shapedCrafting("diamond_potato_diamond_block", TitanGear.potatoeDiamond, 1, 1, "GGG,GPG,GGG", 'G', Blocks.DIAMOND_BLOCK, 'P', Items.BAKED_POTATO);
        shapedCrafting("diamond_melon_diamond", TitanGear.melonDiamond, 1, 0, "GGG,GPG,GGG", 'G', Items.DIAMOND, 'P', Items.MELON);
        shapedCrafting("diamond_melon_diamond_block", TitanGear.melonDiamond, 1, 1, "GGG,GPG,GGG", 'G', Blocks.DIAMOND_BLOCK, 'P', Items.MELON);
        shapedCrafting("diamond_pumpkin_pie_diamond", TitanGear.pumpkinPieDiamond, 1, 0, "GGG,GPG,GGG", 'G', Items.DIAMOND, 'P', Items.PUMPKIN_PIE);
        shapedCrafting("diamond_pumpkin_pie_diamond_block", TitanGear.pumpkinPieDiamond, 1, 1, "GGG,GPG,GGG", 'G', Blocks.DIAMOND_BLOCK, 'P', Items.PUMPKIN_PIE);

        shapedCrafting("jadeite_apple_jadeite", TitanGear.appleJadeite, 1, 0, "GGG,GPG,GGG", 'G', TitanGear.jadeite, 'P', Items.APPLE);
        shapedCrafting("jadeite_apple_jadeite_block", TitanGear.appleJadeite, 1, 1, "GGG,GPG,GGG", 'G', TitanBlockSet.jadeite_block, 'P', Items.APPLE);
        shapedCrafting("jadeite_cookie_jadeite", TitanGear.cookieJadeite, 1, 0, "GGG,GPG,GGG", 'G', TitanGear.jadeite, 'P', Items.COOKIE);
        shapedCrafting("jadeite_cookie_jadeite_block", TitanGear.cookieJadeite, 1, 1, "GGG,GPG,GGG", 'G', TitanBlockSet.jadeite_block, 'P', Items.COOKIE);
        shapedCrafting("jadeite_bread_jadeite", TitanGear.breadJadeite, 1, 0, "GGG,GPG,GGG", 'G', TitanGear.jadeite, 'P', Items.BREAD);
        shapedCrafting("jadeite_bread_jadeite_block", TitanGear.breadJadeite, 1, 1, "GGG,GPG,GGG", 'G', TitanBlockSet.jadeite_block, 'P', Items.BREAD);
        shapedCrafting("jadeite_potato_jadeite", TitanGear.potatoJadeite, 1, 0, "GGG,GPG,GGG", 'G', TitanGear.jadeite, 'P', Items.BAKED_POTATO);
        shapedCrafting("jadeite_potato_jadeite_block", TitanGear.potatoJadeite, 1, 1, "GGG,GPG,GGG", 'G', TitanBlockSet.jadeite_block, 'P', Items.BAKED_POTATO);
        shapedCrafting("jadeite_melon_jadeite", TitanGear.melonJadeite, 1, 0, "GGG,GPG,GGG", 'G', TitanGear.jadeite, 'P', Items.MELON);
        shapedCrafting("jadeite_melon_jadeite_block", TitanGear.melonJadeite, 1, 1, "GGG,GPG,GGG", 'G', TitanBlockSet.jadeite_block, 'P', Items.MELON);
        shapedCrafting("jadeite_pumpkin_pie_jadeite", TitanGear.pumpkinPieJadeite, 1, 0, "GGG,GPG,GGG", 'G', TitanGear.jadeite, 'P', Items.PUMPKIN_PIE);
        shapedCrafting("jadeite_pumpkin_pie_jadeite_block", TitanGear.pumpkinPieJadeite, 1, 1, "GGG,GPG,GGG", 'G', TitanBlockSet.jadeite_block, 'P', Items.PUMPKIN_PIE);

        shapedCrafting("growth_serum", TitanGear.serumGrowth, 1, 0, "RNR,FBF,GRG", 'R', Items.ROTTEN_FLESH, 'N', Items.NETHER_STAR, 'F', Items.FIRE_CHARGE, 'B', Items.GLASS_BOTTLE, 'G', Items.GHAST_TEAR);
        shapedCrafting("teleporter", TitanGear.teleporter, "WWW,SSS, S ", 'W', new ItemStack(Items.SKULL, 1, 1), 'S', Blocks.SOUL_SAND);
        shapedCrafting("teleporter2", TitanGear.teleporter2, "GOG,ONO,GOG", 'G', Blocks.GOLD_BLOCK, 'O', Blocks.OBSIDIAN, 'N', Items.NETHER_STAR);
        shapedCrafting("good_turret", TitanGear.turretGood, "S,O,B", 'S', new ItemStack(Items.SKULL, 1, 1), 'O', Blocks.OBSIDIAN, 'B', Blocks.BEDROCK);
        shapedCrafting("good_turret2", TitanGear.turret2Good, "SOS, B ", 'S', new ItemStack(Items.SKULL, 1, 1), 'O', Blocks.OBSIDIAN, 'B', Blocks.BEDROCK);
        shapedCrafting("good_turret3", TitanGear.turret3Good, " A ,NBN, G ", 'A', TitanGear.turretGood, 'G', TitanGear.turret2Good, 'B', Blocks.MOB_SPAWNER, 'N', Items.NETHER_STAR);
        shapedCrafting("chaff", TitanGear.chaff, 24, "PWP,WPW,PWP", 'P', Items.PAPER, 'W', Items.WHEAT);
        shapedCrafting("harcadium_bow", TitanGear.harcadiumBow, " HS,H S, HS", 'S', TitanGear.stringDiamond, 'H', TitanGear.harcadium);
        shapedCrafting("harcadium_arrow", TitanGear.arrowHarcadium, 16, "H,D,F", 'H', TitanGear.harcadium, 'D', Items.DIAMOND, 'F', Items.FEATHER);
        shapedCrafting("void_bow", TitanGear.voidBow, " HS,H S, HS", 'S', TitanGear.wafletHarcadiumZ, 'H', TitanGear.stackVoid);
        shapedCrafting("void_arrow", TitanGear.arrowVoid, 16, "H,D,F", 'H', TitanGear.stackVoid, 'D', TitanGear.harcadium, 'F', Items.FEATHER);

        // Adminium gear
        shapedCrafting("adminium_axe", TitanGear.axeAdminium, "HH ,HD , D ", 'D', Blocks.BEDROCK, 'H', TitanBlockSet.bedrock_block);
        shapedCrafting("adminium_pickaxe", TitanGear.pickaxeAdminium, "HHH, D , D ", 'D', Blocks.BEDROCK, 'H', TitanBlockSet.bedrock_block);
        shapedCrafting("adminium_spade", TitanGear.spadeAdminium, " H , D , D ", 'D', Blocks.BEDROCK, 'H', TitanBlockSet.bedrock_block);
        shapedCrafting("adminium_hoe", TitanGear.hoeAdminium, "HH , D , D ", 'D', Blocks.BEDROCK, 'H', TitanBlockSet.bedrock_block);
        shapedCrafting("adminium_sword", TitanGear.swordAdminium, " H , H , D ", 'D', Blocks.BEDROCK, 'H', TitanBlockSet.bedrock_block);
        shapedCrafting("adminium_helmet", TitanGear.helmetAdminium, "BBB,B B", 'B', TitanBlockSet.bedrock_block);
        shapedCrafting("adminium_chestplate", TitanGear.chestplateAdminium, "H H,BVB,BBB", 'B', TitanBlockSet.bedrock_block, 'V', TitanBlockSet.void_block, 'H', TitanBlockSet.harcadium_block);
        shapedCrafting("adminium_leggings", TitanGear.leggingsAdminium, "BHB,B B,B B", 'B', TitanBlockSet.bedrock_block, 'H', TitanBlockSet.harcadium_block);
        shapedCrafting("adminium_boots", TitanGear.bootsAdminium, "B B,B B", 'B', TitanBlockSet.bedrock_block);

        // Tool & armor sets (pattern index follows sword/pickaxe/spade/axe/hoe/helmet/chestplate/leggings/boots)
        craftArmourSetTool("bone", Items.BONE, Items.STICK, stuffBone);
        craftArmourSetTool("copper", TitanGear.ingotCopperZ, Items.STICK, stuffCopper);
        craftArmourSetTool("tin", TitanGear.ingotTinZ, Items.STICK, stuffTin);
        craftArmourSetTool("bronze", TitanGear.ingotBronzeZ, Items.STICK, stuffBronze);
        craftArmourSetTool("lead", TitanGear.ingotLeadZ, Items.STICK, stuffLead);
        craftArmourSetTool("silver", TitanGear.ingotSilverZ, Items.STICK, stuffSilver);
        craftArmourSetTool("steel", TitanGear.ingotSteelZ, Items.STICK, stuffSteel);
        craftArmourSetTool("sapphire", TitanGear.sapphire, Items.STICK, stuffSapphire);
        craftArmourSetTool("onyx", TitanGear.onyx, Items.STICK, stuffOnyx);
        craftArmourSetTool("obsidian", Item.getItemFromBlock(Blocks.OBSIDIAN), Items.STICK, stuffObsidian);
        craftArmourSetTool("platinum", TitanGear.ingotPlatinumZ, Items.STICK, stuffPlatinum);
        craftArmourSetTool("titanium", TitanGear.titanium_ingot, Items.STICK, stuffTitanium);
        craftArmourSetTool("pig_iron", TitanGear.ironIngotPig, Items.STICK, ironStuffPig);
        craftArmourSetTool("ruby", TitanGear.ruby, Items.STICK, stuffRuby);
        craftArmourSetTool("demontium", TitanGear.ingotDemontiumZ, Items.DIAMOND, stuffDemontium);
        craftArmourSetTool("harcadium", Item.getItemFromBlock(TitanBlockSet.harcadium_block), Items.DIAMOND, stuffHarcadium);
        craftArmourSetTool("withrenium", Item.getItemFromBlock(TitanBlockSet.withrenium_block), Items.DIAMOND, stuffWithrenium);
        craftArmourSetTool("titanus", Item.getItemFromBlock(TitanBlockSet.titanus_block), TitanGear.harcadium, stuffTitanus);
        craftArmourSetTool("void", Item.getItemFromBlock(TitanBlockSet.void_block), TitanGear.harcadium, stuffVoid);

        if (Loader.isModLoaded("OreSpawn")) {
            shapelessCrafting("overlord_scorpion_egg", TitanGear.scorpionOvumOverlord, 1, 0, TitanBlockSet.overlordScorpionBirthCubeMy, Items.WATER_BUCKET);
            shapelessCrafting("methuselah_kraken_egg", TitanGear.krakenOvumMethuselah, 1, 0, TitanBlockSet.methuselahKrakenBirthCubeMy, Items.WATER_BUCKET);
            shapelessCrafting("burning_mobzilla_egg", TitanGear.mobzillaOvumBurning, 1, 0, TitanBlockSet.burningMobzillaBirthCubeMy, Items.WATER_BUCKET);
            shapedCrafting("methuselah_kraken_spawn_block", TitanBlockSet.methuselahKrakenBirthCubeMy, "HHH,HHH,HHH", 'H', TitanBlockSet.methuselahKrakenPieceBirthCubeMy);
            shapedCrafting("overlord_scorpion_spawn_block", TitanBlockSet.overlordScorpionBirthCubeMy, "HHH,HHH,HHH", 'H', TitanBlockSet.overlordScorpionPieceBirthCubeMy);
            shapedCrafting("burning_mobzilla_spawn_block", TitanBlockSet.burningMobzillaBirthCubeMy, "HHH,HHH,HHH", 'H', TitanBlockSet.burningMobzillaPieceBirthCubeMy);
        }

        // Alloy & material shapeless recipes
        shapelessCrafting("pleasant_blade_brew", TitanGear.bladeBrewPleasant, 1, 0, TitanGear.bladeLeafPleasant, Items.GLASS_BOTTLE, Items.CARROT, Items.FERMENTED_SPIDER_EYE);
        shapelessCrafting("steel_ingot_alloy", TitanGear.ingotSteelZ, 3, 0, TitanGear.ingotChromiumZ, TitanGear.ingotNickelZ, Items.IRON_INGOT, Items.COAL);
        shapelessCrafting("bronze_ingot_alloy", TitanGear.ingotBronzeZ, 10, 0, TitanBlockSet.copper_block, TitanGear.ingotTinZ);
        shapelessCrafting("diamond_string", TitanGear.stringDiamond, 6, 0, Items.STRING, Items.DIAMOND);

        // Compression <-> decompression pairs (9x child <-> 1x parent)
        craftCubeCompressed("bedrock_block", "bedrock", TitanBlockSet.bedrock_block, Blocks.BEDROCK);
        craftCubeCompressed("void_block", "void_item", TitanBlockSet.void_block, TitanGear.stackVoid);
        craftCubeCompressed("titanus_block", "titanus_ingot", TitanBlockSet.titanus_block, TitanGear.ingotTitanusZ);
        craftCubeCompressed("withrenium_block", "withrenium", TitanBlockSet.withrenium_block, TitanGear.withrenium);
        craftCubeCompressed("harcadium_block", "harcadium", TitanBlockSet.harcadium_block, TitanGear.harcadium);
        craftCubeCompressed("harcadium", "harcadium_nugget", TitanGear.harcadium, TitanGear.nuggetHarcadiumZ);
        craftCubeCompressed("harcadium_nugget", "harcadium_wafer", TitanGear.nuggetHarcadiumZ, TitanGear.waferHarcadiumZ);
        craftCubeCompressed("harcadium_wafer", "harcadium_waflet", TitanGear.waferHarcadiumZ, TitanGear.wafletHarcadiumZ);
        craftCubeCompressed("demontium_block", "demontium_ingot", TitanBlockSet.demontium_block, TitanGear.ingotDemontiumZ);
        craftCubeCompressed("jadeite_block", "jadeite", TitanBlockSet.jadeite_block, TitanGear.jadeite);
        craftCubeCompressed("copper_block", "copper_ingot", TitanBlockSet.copper_block, TitanGear.ingotCopperZ);
        craftCubeCompressed("tin_block", "tin_ingot", TitanBlockSet.tin_block, TitanGear.ingotTinZ);
        craftCubeCompressed("bronze_block", "bronze_ingot", TitanBlockSet.bronze_block, TitanGear.ingotBronzeZ);
        craftCubeCompressed("silver_block", "silver_ingot", TitanBlockSet.silver_block, TitanGear.ingotSilverZ);
        craftCubeCompressed("lead_block", "lead_ingot", TitanBlockSet.lead_block, TitanGear.ingotLeadZ);
        craftCubeCompressed("steel_block", "steel_ingot", TitanBlockSet.steel_block, TitanGear.ingotSteelZ);
        craftCubeCompressed("chromium_block", "chromium_ingot", TitanBlockSet.chromium_block, TitanGear.ingotChromiumZ);
        craftCubeCompressed("nickel_block", "nickel_ingot", TitanBlockSet.nickel_block, TitanGear.ingotNickelZ);
        craftCubeCompressed("platinum_block", "platinum_ingot", TitanBlockSet.platinum_block, TitanGear.ingotPlatinumZ);
        craftCubeCompressed("titanium_block", "titanium_ingot", TitanBlockSet.titanium_block, TitanGear.titanium_ingot);
        craftCubeCompressed("pig_iron_block", "pig_iron_ingot", TitanBlockSet.pig_iron_block, TitanGear.ironIngotPig);
        craftCubeCompressed("ruby_block", "ruby", TitanBlockSet.ruby_block, TitanGear.ruby);
        craftCubeCompressed("sapphire_block", "sapphire", TitanBlockSet.sapphire_block, TitanGear.sapphire);
        craftCubeCompressed("onyx_block", "onyx", TitanBlockSet.onyx_block, TitanGear.onyx);
    }

    private static void oreSmelt(Block[] ores, Item item, int exp) {
        for (int i = 0; i < ores.length; ++i) {
            if (ores[i] != null && blockExists(ores[i])) {
                GameRegistry.addSmelting(ores[i], new ItemStack(item, 1), (float)exp);
            }
        }
    }

    private static void shapedCrafting(String recipeName, Object output, String pattern, Object... input) {
        shapedCrafting(recipeName, output, 1, 0, pattern, input);
    }

    private static void shapedCrafting(String recipeName, Object output, int amount, String pattern, Object... input) {
        shapedCrafting(recipeName, output, amount, 0, pattern, input);
    }

    private static void shapedCrafting(String recipeName, Object output, int amount, int meta, String pattern, Object... input) {
        if (output == null || pattern == null)
            return;
        if (output instanceof Item && !existsItem((Item)output))
            return;
        if (output instanceof Block && !blockExists((Block)output))
            return;
        Item item = asStack(output);
        if (item == null)
            return;
        List<Object> recipe = new ArrayList<Object>();
        // BUGFIX (mod-load crash "Key defines symbols that aren't used in pattern") — 1.7.10 silently
        // ignored recipe keys whose symbol wasn't in the pattern (e.g. the '#' stick key on armor shapes), but
        // 1.12.2 CraftingHelper.parseShaped throws. Only forward keys actually present in the pattern rows.
        StringBuilder usedSymbols = new StringBuilder();
        for (String row : pattern.split(",")) {
            recipe.add(row);
            usedSymbols.append(row.trim());
        }
        for (int i = 0; i + 1 < input.length; i += 2) {
            Object value = input[i + 1];
            if (!(input[i] instanceof Character))
                continue;
            if (usedSymbols.indexOf(String.valueOf(((Character)input[i]).charValue())) < 0)
                continue;
            if (value == null)
                return;
            if (value instanceof Item && !existsItem((Item)value))
                return;
            if (value instanceof Block && !blockExists((Block)value))
                return;
            recipe.add(input[i]);
            recipe.add(value);
        }

        GameRegistry.addShapedRecipe(new ResourceLocation(TheTitans.MODID_V, recipeName), new ResourceLocation(""), new ItemStack(item, amount, meta), recipe.toArray());
    }

    private static void shapelessCrafting(String recipeName, Object output, int amount, int meta, Object... input) {
        if (output == null)
            return;
        if (output instanceof Item && !existsItem((Item)output))
            return;
        if (output instanceof Block && !blockExists((Block)output))
            return;
        Item item = asStack(output);
        if (item == null)
            return;
        List<Ingredient> ingredients = new ArrayList<Ingredient>();
        for (Object entry : input) {
            if (entry == null)
                return;
            if (entry instanceof ItemStack)
                ingredients.add(Ingredient.fromStacks((ItemStack)entry));
            else if (entry instanceof Item) {
                if (!existsItem((Item)entry))
                    return;
                ingredients.add(Ingredient.fromItem((Item)entry));
            } else if (entry instanceof Block) {
                if (!blockExists((Block)entry))
                    return;
                ingredients.add(Ingredient.fromItem(Item.getItemFromBlock((Block)entry)));
            } else
                    return;
        }

        GameRegistry.addShapelessRecipe(new ResourceLocation(TheTitans.MODID_V, recipeName), new ResourceLocation(""), new ItemStack(item, amount, meta), ingredients.toArray(new Ingredient[ingredients.size()]));
    }

    private static void craftArmourSetTool(String setName, Item ingot, Item handle, Item... list) {
        String[] suffixes = new String[]{"sword", "pickaxe", "spade", "axe", "hoe", "helmet", "chestplate", "leggings", "boots"};
        for (int i = 0; i < list.length && i < patternsRecipe.length; ++i) {
            if (patternsRecipe[i].indexOf('#') >= 0)
                shapedCrafting(setName + "_" + suffixes[i], list[i], 1, 0, patternsRecipe[i], '#', handle, 'X', ingot);
            else
                shapedCrafting(setName + "_" + suffixes[i], list[i], 1, 0, patternsRecipe[i], 'X', ingot);
        }
    }

    private static void craftCubeCompressed(String parentName, String childName, Object parentItem, Object childItem) {
        Object[] nineChildren = new Object[9];
        for (int i = 0; i < nineChildren.length; ++i)
            nineChildren[i] = childItem;
        shapelessCrafting(parentName + "_pack", parentItem, 1, 0, nineChildren);
        shapelessCrafting(childName + "_unpack", childItem, 9, 0, parentItem);
    }

    private static Item asStack(Object output) {
        if (output instanceof Item)
            return (Item)output;
        if (output instanceof Block)
            return Item.getItemFromBlock((Block)output);
        return null;
    }

    // Mirrors original TitanGear.exists/TitanBlockSet.exists semantics: only mod-tracked objects are
    // checked against the game registries (config-disabled items are never registered), anything
    // else (vanilla content) always counts as present.
    private static boolean existsItem(Item item) {
        if (item == null)
            return false;
        if (!TitanGear.registryStack.contains(item))
            return true;
        ResourceLocation name = ForgeRegistries.ITEMS.getKey(item);
        return name != null && TheTitans.MODID_V.equals(name.getNamespace());
    }

    private static boolean blockExists(Block block) {
        if (block == null)
            return false;
        if (!TitanBlockSet.registryCube.contains(block))
            return true;
        ResourceLocation name = ForgeRegistries.BLOCKS.getKey(block);
        return name != null && TheTitans.MODID_V.equals(name.getNamespace());
    }
}
