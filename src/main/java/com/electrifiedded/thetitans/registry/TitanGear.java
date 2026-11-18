package com.electrifiedded.thetitans.registry;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.item.armor.ItemAdminiumArmor;
import com.electrifiedded.thetitans.item.armor.ItemDemontiumArmor;
import com.electrifiedded.thetitans.item.armor.ItemHarcadiumArmor;
import com.electrifiedded.thetitans.item.armor.ItemNormalArmor;
import com.electrifiedded.thetitans.item.armor.ItemPigIronArmor;
import com.electrifiedded.thetitans.item.armor.ItemTitanusArmor;
import com.electrifiedded.thetitans.item.armor.ItemVoidArmor;
import com.electrifiedded.thetitans.item.armor.ItemWithreniumArmor;
import com.electrifiedded.thetitans.item.material.ItemMaterial;
import com.electrifiedded.thetitans.item.misc.ItemAdamantium;
import com.electrifiedded.thetitans.item.misc.ItemAppleDiamond;
import com.electrifiedded.thetitans.item.misc.ItemAppleGolden;
import com.electrifiedded.thetitans.item.misc.ItemBase;
import com.electrifiedded.thetitans.item.misc.ItemChaff;
import com.electrifiedded.thetitans.item.misc.ItemFoodMalgrum;
import com.electrifiedded.thetitans.item.misc.ItemForTheChallengeGames1;
import com.electrifiedded.thetitans.item.misc.ItemGrowthSerum;
import com.electrifiedded.thetitans.item.misc.ItemMalgrumSeeds;
import com.electrifiedded.thetitans.item.misc.ItemNormalIngot;
import com.electrifiedded.thetitans.item.misc.ItemPleasantBladeBrew;
import com.electrifiedded.thetitans.item.misc.ItemPleasantBladeFlower;
import com.electrifiedded.thetitans.item.misc.ItemPleasantBladeSeed;
import com.electrifiedded.thetitans.item.spawn.ItemEggReinforcedIronGolem;
import com.electrifiedded.thetitans.item.spawn.ItemEventSpawner;
import com.electrifiedded.thetitans.item.spawn.ItemSpawnEggMinion;
import com.electrifiedded.thetitans.item.spawn.ItemSpawnEggRegular;
import com.electrifiedded.thetitans.item.spawn.ItemSpawnEggTitans;
import com.electrifiedded.thetitans.item.spawn.ItemTeleporter2;
import com.electrifiedded.thetitans.item.spawn.ItemTeleporter;
import com.electrifiedded.thetitans.item.spawn.ItemTitanSpawnEgg;
import com.electrifiedded.thetitans.item.tool.ItemAdminiumAxe;
import com.electrifiedded.thetitans.item.tool.ItemAdminiumHoe;
import com.electrifiedded.thetitans.item.tool.ItemAdminiumPickaxe;
import com.electrifiedded.thetitans.item.tool.ItemAdminiumSpade;
import com.electrifiedded.thetitans.item.tool.ItemHarcadiumHoe;
import com.electrifiedded.thetitans.item.tool.ItemNormalAxe;
import com.electrifiedded.thetitans.item.tool.ItemNormalHoe;
import com.electrifiedded.thetitans.item.tool.ItemNormalPickaxe;
import com.electrifiedded.thetitans.item.tool.ItemNormalSpade;
import com.electrifiedded.thetitans.item.tool.ItemOptimaAxe;
import com.electrifiedded.thetitans.item.tool.ItemTitanusHoe;
import com.electrifiedded.thetitans.item.tool.ItemVoidHoe;
import com.electrifiedded.thetitans.item.tool.ItemWithreniumHoe;
import com.electrifiedded.thetitans.item.turret.ItemGoodTurret;
import com.electrifiedded.thetitans.item.turret.ItemGoodTurretGround;
import com.electrifiedded.thetitans.item.turret.ItemGoodTurretMortar;
import com.electrifiedded.thetitans.item.weapon.ItemAdamantiumSword;
import com.electrifiedded.thetitans.item.weapon.ItemAdminiumSword;
import com.electrifiedded.thetitans.item.weapon.ItemHarcadiumBow;
import com.electrifiedded.thetitans.item.weapon.ItemHarcadiumSword;
import com.electrifiedded.thetitans.item.weapon.ItemNormalSword;
import com.electrifiedded.thetitans.item.weapon.ItemObsidianSword;
import com.electrifiedded.thetitans.item.weapon.ItemSacredSword;
import com.electrifiedded.thetitans.item.weapon.ItemSilverSword;
import com.electrifiedded.thetitans.item.weapon.ItemTitanSword;
import com.electrifiedded.thetitans.item.weapon.ItemTitanusSword;
import com.electrifiedded.thetitans.item.weapon.ItemUltimaBlade;
import com.electrifiedded.thetitans.item.weapon.ItemVoidBow;
import com.electrifiedded.thetitans.item.weapon.ItemVoidSword;
import com.electrifiedded.thetitans.item.weapon.ItemWithreniumSword;
import com.electrifiedded.thetitans.util.RegistryHelper.Registry;
import com.electrifiedded.thetitans.util.RegistryHelper;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.MobEffects;
import net.minecraft.item.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TitanGear {
    // Kept for the config GUI (per-item enable flags). Actual Forge registration
    // happens in registerAll(...) during RegistryEvent.Register<Item>.
    public static final Registry<Item, Boolean> registryStack = new Registry<Item, Boolean>(true);
    private static final List<Item> registerTo = new ArrayList<Item>();
    // exposes registered items for client model registration.
    public static java.util.List<Item> fetchItemsRegistered() { return registerTo; }
    private static Map<Item, String> dictsQueued = new HashMap<Item, String>();

    private static <X extends Item> X reg(X obj) {
        registerTo.add(obj);
        return obj;
    }

    private static <X extends Item> X reg(String oreDictionary, X obj) {
        registerTo.add(obj);
        dictsQueued.put(obj, oreDictionary);
        return obj;
    }

    public static ItemMaterial armourMaterialAdminiumEnum = new ItemMaterial("AdminiumArmor", 172000000, 13588, 41176, 30882, 13588, 30);
    public static ItemMaterial armourMaterialAbsenceEnum = new ItemMaterial("AbsenceArmor", 86000000, 6794, 20588, 15441, 6794, 30);
    public static ItemMaterial armourMaterialHarcadiumEnum = new ItemMaterial("HarcadiumArmor", 2410752, 825, 2500, 1875, 825, 30);
    public static ItemMaterial armourMaterialDemontiumEnum = new ItemMaterial("DemontiumArmor", 200896, 396, 1200, 900, 396, 30);
    // Armor stats restored to 1.7.10 original (durability, helmet, chest, legs, boots, enchantability).
    public static ItemMaterial armourMaterialPigIronEnum = new ItemMaterial("PigIronArmor", 18, 2, 6, 5, 2, 9);
    public static ItemMaterial armourMaterialRubyEnum = new ItemMaterial("RubyArmor", 24, 2, 5, 4, 2, 28);
    public static ItemMaterial armourMaterialTitaniumEnum = new ItemMaterial("TitaniumArmor", 146, 4, 12, 9, 4, 18);
    public static ItemMaterial armourMaterialPlatinumEnum = new ItemMaterial("PlatinumArmor", 44, 3, 10, 7, 3, 20);
    public static ItemMaterial armourMaterialSteelEnum = new ItemMaterial("SteelArmor", 20, 2, 7, 5, 2, 6);
    public static ItemMaterial armourMaterialLeadEnum = new ItemMaterial("LeadArmor", 14, 2, 6, 4, 2, 8);
    public static ItemMaterial armourMaterialSilverEnum = new ItemMaterial("SilverArmor", 13, 2, 7, 5, 2, 25);
    public static ItemMaterial armourMaterialBronzeEnum = new ItemMaterial("BronzeArmor", 18, 2, 5, 4, 2, 8);
    public static ItemMaterial armourMaterialTinEnum = new ItemMaterial("TinArmor", 3, 1, 2, 2, 1, 16);
    public static ItemMaterial armourMaterialCopperEnum = new ItemMaterial("CopperArmor", 4, 1, 3, 2, 1, 14);
    public static ItemMaterial toolMaterialAdminiumEnum = new ItemMaterial("AdminiumTools", Integer.MAX_VALUE - 1, 1000000000, 1.0E9F, 1.0E13F, 30);
    public static ItemMaterial toolMaterialAbsenceEnum = new ItemMaterial("AbsenceTools", 15, 286000000, 3600.0F, 15246.0F, 30);
    public static ItemMaterial toolMaterialHarcadiumEnum = new ItemMaterial("HarcadiumTools", 12, 560000, 120.0F, 996.0F, 30);
    public static ItemMaterial toolMaterialDemontiumEnum = new ItemMaterial("DemontiumTools", 11, 87600, 100.0F, 846.0F, 20);
    public static ItemMaterial toolMaterialBoneEnum = new ItemMaterial("BoneTools", 0, 100, 4.0F, -1.0F, 12);
    public static ItemMaterial toolMaterialSapphireEnum = new ItemMaterial("SapphireTools", 2, 452, 12.0F, 1.0F, 14);
    public static ItemMaterial toolMaterialOnyxEnum = new ItemMaterial("OnyxTools", 2, 950, 12.0F, 2.5F, 6);
    public static ItemMaterial toolMaterialPigIronEnum = new ItemMaterial("PigIronTools", 2, 275, 7.0F, 2.0F, 9);
    public static ItemMaterial toolMaterialRubyEnum = new ItemMaterial("RubyTools", 2, 650, 16.0F, 2.5F, 20);
    public static ItemMaterial toolMaterialPlatinumEnum = new ItemMaterial("PlatinumTools", 4, 1952, 10.0F, 4.5F, 18);
    public static ItemMaterial toolMaterialTitaniumEnum = new ItemMaterial("TitaniumTools", 8, 2928, 10.0F, 6.0F, 18);
    public static ItemMaterial toolMaterialSteelEnum = new ItemMaterial("SteelTools", 2, 560, 8.0F, 3.0F, 12);
    public static ItemMaterial toolMaterialLeadEnum = new ItemMaterial("LeadTools", 2, 260, 5.0F, 2.0F, 6);
    public static ItemMaterial toolMaterialSilverEnum = new ItemMaterial("SilverTools", 2, 356, 10.0F, 2.5F, 22);
    public static ItemMaterial toolMaterialBronzeEnum = new ItemMaterial("BronzeTools", 1, 340, 6.0F, 1.0F, 17);
    public static ItemMaterial toolMaterialTinEnum = new ItemMaterial("TinTools", 0, 85, 6.0F, -0.5F, 16);
    public static ItemMaterial toolMaterialCopperEnum = new ItemMaterial("CopperTools", 0, 95, 2.0F, 0.0F, 18);
    public static ItemMaterial GoodTurret = new ItemMaterial("GoodTurret", 0, 80, 0.0F, 26.0F, 50);
    public static ItemMaterial SacredSword = new ItemMaterial("SacredSword", 0, 9372, 80.0F, 21.0F, 30);
    // New (1.12.2 content) materials — stats mirrored from decompiled TitanGear.
    public static ItemMaterial armourMaterialTitanusEnum = new ItemMaterial("TitanusArmor", 43393536, 3397, 10294, 7720, 3397, 30);
    public static ItemMaterial armourMaterialWithreniumEnum = new ItemMaterial("WithreniumArmor", 7232256, 1960, 5960, 4455, 1960, 30);
    public static ItemMaterial armourMaterialObsidianEnum = new ItemMaterial("ObsidianArmor", 84, 3, 8, 6, 3, 2);
    public static ItemMaterial armourMaterialOnyxEnum = new ItemMaterial("OnyxArmor", 27, 2, 5, 4, 2, 28);
    public static ItemMaterial armourMaterialSapphireEnum = new ItemMaterial("SapphireArmor", 16, 1, 4, 3, 1, 18);
    public static ItemMaterial armourMaterialBoneEnum = new ItemMaterial("BoneArmor", 2, 1, 3, 2, 1, 12);
    public static ItemMaterial toolMaterialTitanusEnum = new ItemMaterial("TitanusTools", 14, 9856000, 840.0F, 8496.0F, 30);
    public static ItemMaterial toolMaterialWithreniumEnum = new ItemMaterial("WithreniumTools", 13, 2464000, 240.0F, 4396.0F, 30);
    public static ItemMaterial toolMaterialObsidianEnum = new ItemMaterial("ObsidianTools", 3, 2456, 24.0F, 1.0F, 2);
    public static Item scorpionOvumOverlord;
    public static Item krakenOvumMethuselah;
    public static Item mobzillaOvumBurning;
    public static final Item ultimaBlade = reg(new ItemUltimaBlade());
    public static final Item optimaAxe = reg(new ItemOptimaAxe());
    public static final Item adamantiumSword = reg(new ItemAdamantiumSword());
    public static final ItemBase jadeite = new ItemBase("jadeite_gem");
    public static final ItemBase moltenFuel = new ItemBase("molten_fuel");
    public static final Item ironIngotPig = reg("ingotPigIron", new ItemBase("pig_iron_ingot"));
    public static final Item ruby = reg("gemRuby", new ItemBase("ruby"));
    public static final Item sapphire = reg("gemSapphire", new ItemBase("sapphire"));
    public static final Item onyx = reg("gemOnyx", new ItemBase("onyx"));
    public static final Item ingotCopperZ = reg("ingotCopper", new ItemBase("copper_ingot"));
    public static final Item ingotTinZ = reg("ingotTin", new ItemBase("tin_ingot"));
    public static final Item ingotBronzeZ = reg("ingotBronze", new ItemBase("bronze_ingot", TitanTrophies.makebronzeV));
    public static final Item ingotSilverZ = reg("ingotSilver", new ItemBase("silver_ingot"));
    public static final Item ingotLeadZ = reg("ingotLead", new ItemBase("lead_ingot"));
    public static final Item ingotChromiumZ = reg("ingotChromium", new ItemBase("chromium_ingot"));
    public static final Item ingotNickelZ = reg("ingotNickel", new ItemBase("nickel_ingot"));
    public static final Item ingotSteelZ = reg("ingotSteel", new ItemBase("steel_ingot", TitanTrophies.makesteelV));
    public static final Item ingotPlatinumZ = reg("ingotPlatinum", new ItemBase("platinum_ingot"));
    public static final Item titanium_ingot = reg("ingotTitanium", new ItemBase("titanium_ingot"));
    public static final Item ingotDemontiumZ = reg("ingotDemontium", new ItemBase("demontium_ingot"));
    public static final Item harcadium = reg("harcadium", ItemBase.element("harcadium", TitanTrophies.harcadium, "\u00A7lElement #753"));
    public static final Item nuggetHarcadiumZ = reg("nuggetHarcadium", ItemBase.element("harcadium_nugget", TitanTrophies.harcadium, "\u00A7lElement #753"));
    public static final Item waferHarcadiumZ = reg("waferHarcadium", ItemBase.element("harcadium_wafer", TitanTrophies.harcadium, "\u00A7lElement #753"));
    public static final Item wafletHarcadiumZ = reg("wafletHarcadium", ItemBase.element("harcadium_waflet", TitanTrophies.harcadium, "\u00A7lElement #753"));
    public static final Item withrenium = reg("withrenium", new ItemBase("withrenium").appendDetails("Element #786. Wit", "A viscuse gas that exists only in the Nether or stars.", "Hard to fashion into useable form.", "Very powerful matieral that burns nearby enemies."));
    public static final Item ingotTitanusZ = reg("ingotTitanus", new ItemNormalIngot("titanus", 8454018));
    public static final Item stackVoid = reg("void", ItemBase.element("void", TitanTrophies.voidEssence, "§lElement #???"));
    public static final Item adamantium = reg("adamantium", new ItemAdamantium("adamantium"));
    public static final Item swordSacred = reg(new ItemSacredSword("sacred", SacredSword.fetchMaterialTool()).setDamageMultiplier(5.0F));
    public static final Item swordCopper = reg(new ItemNormalSword("copper", toolMaterialCopperEnum.fetchMaterialTool()));
    public static final Item spadeCopper = reg(new ItemNormalSpade("copper", toolMaterialCopperEnum.fetchMaterialTool()));
    public static final Item pickaxeCopper = reg(new ItemNormalPickaxe("copper", toolMaterialCopperEnum.fetchMaterialTool()));
    public static final Item axeCopper = reg(new ItemNormalAxe("copper", toolMaterialCopperEnum.fetchMaterialTool()));
    public static final Item hoeCopper = reg(new ItemNormalHoe("copper", toolMaterialCopperEnum.fetchMaterialTool()));
    public static final Item swordTin = reg(new ItemNormalSword("tin", toolMaterialTinEnum.fetchMaterialTool()));
    public static final Item spadeTin = reg(new ItemNormalSpade("tin", toolMaterialTinEnum.fetchMaterialTool()));
    public static final Item pickaxeTin = reg(new ItemNormalPickaxe("tin", toolMaterialTinEnum.fetchMaterialTool()));
    public static final Item axeTin = reg(new ItemNormalAxe("tin", toolMaterialTinEnum.fetchMaterialTool()));
    public static final Item hoeTin = reg(new ItemNormalHoe("tin", toolMaterialTinEnum.fetchMaterialTool()));
    public static final Item swordBronze = reg(new ItemNormalSword("bronze", toolMaterialBronzeEnum.fetchMaterialTool()));
    public static final Item spadeBronze = reg(new ItemNormalSpade("bronze", toolMaterialBronzeEnum.fetchMaterialTool()));
    public static final Item pickaxeBronze = reg(new ItemNormalPickaxe("bronze", toolMaterialBronzeEnum.fetchMaterialTool()));
    public static final Item axeBronze = reg(new ItemNormalAxe("bronze", toolMaterialBronzeEnum.fetchMaterialTool()));
    public static final Item hoeBronze = reg(new ItemNormalHoe("bronze", toolMaterialBronzeEnum.fetchMaterialTool()));
    public static final Item swordSilver = reg(new ItemSilverSword("silver", toolMaterialSilverEnum.fetchMaterialTool()).setDamageMultiplier(1.0F));
    public static final Item spadeSilver = reg(new ItemNormalSpade("silver", toolMaterialSilverEnum.fetchMaterialTool()));
    public static final Item pickaxeSilver = reg(new ItemNormalPickaxe("silver", toolMaterialSilverEnum.fetchMaterialTool()));
    public static final Item axeSilver = reg(new ItemNormalAxe("silver", toolMaterialSilverEnum.fetchMaterialTool()));
    public static final Item hoeSilver = reg(new ItemNormalHoe("silver", toolMaterialSilverEnum.fetchMaterialTool()));
    public static final Item swordLead = reg(new ItemNormalSword("lead", toolMaterialLeadEnum.fetchMaterialTool()));
    public static final Item spadeLead = reg(new ItemNormalSpade("lead", toolMaterialLeadEnum.fetchMaterialTool()));
    public static final Item pickaxeLead = reg(new ItemNormalPickaxe("lead", toolMaterialLeadEnum.fetchMaterialTool()));
    public static final Item axeLead = reg(new ItemNormalAxe("lead", toolMaterialLeadEnum.fetchMaterialTool()));
    public static final Item hoeLead = reg(new ItemNormalHoe("lead", toolMaterialLeadEnum.fetchMaterialTool()));
    public static final Item swordSteel = reg(new ItemNormalSword("steel", toolMaterialSteelEnum.fetchMaterialTool()));
    public static final Item spadeSteel = reg(new ItemNormalSpade("steel", toolMaterialSteelEnum.fetchMaterialTool()));
    public static final Item pickaxeSteel = reg(new ItemNormalPickaxe("steel", toolMaterialSteelEnum.fetchMaterialTool()));
    public static final Item axeSteel = reg(new ItemNormalAxe("steel", toolMaterialSteelEnum.fetchMaterialTool()));
    public static final Item hoeSteel = reg(new ItemNormalHoe("steel", toolMaterialSteelEnum.fetchMaterialTool()));
    public static final Item swordPlatinum = reg(new ItemNormalSword("platinum", toolMaterialPlatinumEnum.fetchMaterialTool()));
    public static final Item spadePlatinum = reg(new ItemNormalSpade("platinum", toolMaterialPlatinumEnum.fetchMaterialTool()));
    public static final Item pickaxePlatinum = reg(new ItemNormalPickaxe("platinum", toolMaterialPlatinumEnum.fetchMaterialTool()));
    public static final Item axePlatinum = reg(new ItemNormalAxe("platinum", toolMaterialPlatinumEnum.fetchMaterialTool()));
    public static final Item hoePlatinum = reg(new ItemNormalHoe("platinum", toolMaterialPlatinumEnum.fetchMaterialTool()));
    public static final Item swordTitanium = reg(new ItemNormalSword("titanium", toolMaterialTitaniumEnum.fetchMaterialTool()));
    public static final Item spadeTitanium = reg(new ItemNormalSpade("titanium", toolMaterialTitaniumEnum.fetchMaterialTool()));
    public static final Item pickaxeTitanium = reg(new ItemNormalPickaxe("titanium", toolMaterialTitaniumEnum.fetchMaterialTool()));
    public static final Item axeTitanium = reg(new ItemNormalAxe("titanium", toolMaterialTitaniumEnum.fetchMaterialTool()));
    public static final Item hoeTitanium = reg(new ItemNormalHoe("titanium", toolMaterialTitaniumEnum.fetchMaterialTool()));
    public static final Item ironSwordPig = reg(new ItemNormalSword("pig_iron", toolMaterialPigIronEnum.fetchMaterialTool()));
    public static final Item ironSpadePig = reg(new ItemNormalSpade("pig_iron", toolMaterialPigIronEnum.fetchMaterialTool()));
    public static final Item ironPickaxePig = reg(new ItemNormalPickaxe("pig_iron", toolMaterialPigIronEnum.fetchMaterialTool()));
    public static final Item ironAxePig = reg(new ItemNormalAxe("pig_iron", toolMaterialPigIronEnum.fetchMaterialTool()));
    public static final Item ironHoePig = reg(new ItemNormalHoe("pig_iron", toolMaterialPigIronEnum.fetchMaterialTool()));
    public static final Item swordRuby = reg(new ItemNormalSword("ruby", toolMaterialRubyEnum.fetchMaterialTool()));
    public static final Item spadeRuby = reg(new ItemNormalSpade("ruby", toolMaterialRubyEnum.fetchMaterialTool()));
    public static final Item pickaxeRuby = reg(new ItemNormalPickaxe("ruby", toolMaterialRubyEnum.fetchMaterialTool()));
    public static final Item axeRuby = reg(new ItemNormalAxe("ruby", toolMaterialRubyEnum.fetchMaterialTool()));
    public static final Item hoeRuby = reg(new ItemNormalHoe("ruby", toolMaterialRubyEnum.fetchMaterialTool()));
    public static final Item swordBone = reg(new ItemNormalSword("bone", toolMaterialBoneEnum.fetchMaterialTool()));
    public static final Item spadeBone = reg(new ItemNormalSpade("bone", toolMaterialBoneEnum.fetchMaterialTool()));
    public static final Item pickaxeBone = reg(new ItemNormalPickaxe("bone", toolMaterialBoneEnum.fetchMaterialTool()));
    public static final Item axeBone = reg(new ItemNormalAxe("bone", toolMaterialBoneEnum.fetchMaterialTool()));
    public static final Item hoeBone = reg(new ItemNormalHoe("bone", toolMaterialBoneEnum.fetchMaterialTool()));
    public static final Item swordSapphire = reg(new ItemNormalSword("sapphire", toolMaterialSapphireEnum.fetchMaterialTool()));
    public static final Item spadeSapphire = reg(new ItemNormalSpade("sapphire", toolMaterialSapphireEnum.fetchMaterialTool()));
    public static final Item pickaxeSapphire = reg(new ItemNormalPickaxe("sapphire", toolMaterialSapphireEnum.fetchMaterialTool()));
    public static final Item axeSapphire = reg(new ItemNormalAxe("sapphire", toolMaterialSapphireEnum.fetchMaterialTool()));
    public static final Item hoeSapphire = reg(new ItemNormalHoe("sapphire", toolMaterialSapphireEnum.fetchMaterialTool()));
    public static final Item swordOnyx = reg(new ItemNormalSword("onyx", toolMaterialOnyxEnum.fetchMaterialTool()));
    public static final Item spadeOnyx = reg(new ItemNormalSpade("onyx", toolMaterialOnyxEnum.fetchMaterialTool()));
    public static final Item pickaxeOnyx = reg(new ItemNormalPickaxe("onyx", toolMaterialOnyxEnum.fetchMaterialTool()));
    public static final Item axeOnyx = reg(new ItemNormalAxe("onyx", toolMaterialOnyxEnum.fetchMaterialTool()));
    public static final Item hoeOnyx = reg(new ItemNormalHoe("onyx", toolMaterialOnyxEnum.fetchMaterialTool()));
    public static final Item swordDemontium = reg(new ItemTitanSword("demontium", toolMaterialDemontiumEnum.fetchMaterialTool()));
    public static final Item spadeDemontium = reg(new ItemNormalSpade("demontium", toolMaterialDemontiumEnum.fetchMaterialTool()));
    public static final Item pickaxeDemontium = reg(new ItemNormalPickaxe("demontium", toolMaterialDemontiumEnum.fetchMaterialTool()));
    public static final Item axeDemontium = reg(new ItemNormalAxe("demontium", toolMaterialDemontiumEnum.fetchMaterialTool()));
    public static final Item hoeDemontium = reg(new ItemNormalHoe("demontium", toolMaterialDemontiumEnum.fetchMaterialTool()));
    public static final Item helmetBone = reg(new ItemNormalArmor("bone", armourMaterialBoneEnum.getArmorMaterial(), 0));
    public static final Item chestplateBone = reg(new ItemNormalArmor("bone", armourMaterialBoneEnum.getArmorMaterial(), 1));
    public static final Item leggingsBone = reg(new ItemNormalArmor("bone", armourMaterialBoneEnum.getArmorMaterial(), 2));
    public static final Item bootsBone = reg(new ItemNormalArmor("bone", armourMaterialBoneEnum.getArmorMaterial(), 3));
    public static final Item helmetSapphire = reg(new ItemNormalArmor("sapphire", armourMaterialSapphireEnum.getArmorMaterial(), 0));
    public static final Item chestplateSapphire = reg(new ItemNormalArmor("sapphire", armourMaterialSapphireEnum.getArmorMaterial(), 1));
    public static final Item leggingsSapphire = reg(new ItemNormalArmor("sapphire", armourMaterialSapphireEnum.getArmorMaterial(), 2));
    public static final Item bootsSapphire = reg(new ItemNormalArmor("sapphire", armourMaterialSapphireEnum.getArmorMaterial(), 3));
    public static final Item helmetOnyx = reg(new ItemNormalArmor("onyx", armourMaterialOnyxEnum.getArmorMaterial(), 0));
    public static final Item chestplateOnyx = reg(new ItemNormalArmor("onyx", armourMaterialOnyxEnum.getArmorMaterial(), 1));
    public static final Item leggingsOnyx = reg(new ItemNormalArmor("onyx", armourMaterialOnyxEnum.getArmorMaterial(), 2));
    public static final Item bootsOnyx = reg(new ItemNormalArmor("onyx", armourMaterialOnyxEnum.getArmorMaterial(), 3));
    public static final Item helmetObsidian = reg(new ItemNormalArmor("obsidian", armourMaterialObsidianEnum.getArmorMaterial(), 0));
    public static final Item chestplateObsidian = reg(new ItemNormalArmor("obsidian", armourMaterialObsidianEnum.getArmorMaterial(), 1));
    public static final Item leggingsObsidian = reg(new ItemNormalArmor("obsidian", armourMaterialObsidianEnum.getArmorMaterial(), 2));
    public static final Item bootsObsidian = reg(new ItemNormalArmor("obsidian", armourMaterialObsidianEnum.getArmorMaterial(), 3));
    public static final Item harcadiumSword = reg(new ItemHarcadiumSword("harcadium", toolMaterialHarcadiumEnum.fetchMaterialTool()));
    public static final Item arrowHarcadium = reg(ItemBase.arrow("harcadium", TitanTrophies.harcadiumArrows));
    public static final Item harcadiumBow = reg(new ItemHarcadiumBow("harcadium", toolMaterialHarcadiumEnum.fetchMaterialTool()));
    public static final Item spadeHarcadium = reg(new ItemNormalSpade("harcadium", toolMaterialHarcadiumEnum.fetchMaterialTool()));
    public static final Item pickaxeHarcadium = reg(new ItemNormalPickaxe("harcadium", toolMaterialHarcadiumEnum.fetchMaterialTool()));
    public static final Item axeHarcadium = reg(new ItemNormalAxe("harcadium", toolMaterialHarcadiumEnum.fetchMaterialTool()));
    public static final Item hoeHarcadium = reg(new ItemHarcadiumHoe("harcadium", toolMaterialHarcadiumEnum.fetchMaterialTool()));
    // New tools/swords (ported ctors take (name, ToolMaterial) only)
    public static final Item swordTitanus = reg(new ItemTitanusSword("titanus", toolMaterialTitanusEnum.fetchMaterialTool()));
    public static final Item spadeTitanus = reg(new ItemNormalSpade("titanus", toolMaterialTitanusEnum.fetchMaterialTool()));
    public static final Item pickaxeTitanus = reg(new ItemNormalPickaxe("titanus", toolMaterialTitanusEnum.fetchMaterialTool()));
    public static final Item axeTitanus = reg(new ItemNormalAxe("titanus", toolMaterialTitanusEnum.fetchMaterialTool()));
    public static final Item hoeTitanus = reg(new ItemTitanusHoe("titanus", toolMaterialTitanusEnum.fetchMaterialTool()));
    public static final Item swordWithrenium = reg(new ItemWithreniumSword("withrenium", toolMaterialWithreniumEnum.fetchMaterialTool()));
    public static final Item spadeWithrenium = reg(new ItemNormalSpade("withrenium", toolMaterialWithreniumEnum.fetchMaterialTool()));
    public static final Item pickaxeWithrenium = reg(new ItemNormalPickaxe("withrenium", toolMaterialWithreniumEnum.fetchMaterialTool()));
    public static final Item axeWithrenium = reg(new ItemNormalAxe("withrenium", toolMaterialWithreniumEnum.fetchMaterialTool()));
    public static final Item hoeWithrenium = reg(new ItemWithreniumHoe("withrenium", toolMaterialWithreniumEnum.fetchMaterialTool()));
    public static final Item swordObsidian = reg(new ItemObsidianSword("obsidian", toolMaterialObsidianEnum.fetchMaterialTool()));
    public static final Item spadeObsidian = reg(new ItemNormalSpade("obsidian", toolMaterialObsidianEnum.fetchMaterialTool()));
    public static final Item pickaxeObsidian = reg(new ItemNormalPickaxe("obsidian", toolMaterialObsidianEnum.fetchMaterialTool()));
    public static final Item axeObsidian = reg(new ItemNormalAxe("obsidian", toolMaterialObsidianEnum.fetchMaterialTool()));
    public static final Item hoeObsidian = reg(new ItemNormalHoe("obsidian", toolMaterialObsidianEnum.fetchMaterialTool()));
    public static final Item arrowVoid = reg(ItemBase.arrow("void", TitanTrophies.voidArrows));
    public static final Item voidBow = reg(new ItemVoidBow("void", toolMaterialAbsenceEnum.fetchMaterialTool()));
    public static final Item voidSword = reg(new ItemVoidSword("void", toolMaterialAbsenceEnum.fetchMaterialTool()));
    public static final Item spadeVoid = reg(new ItemNormalSpade("void", toolMaterialAbsenceEnum.fetchMaterialTool()));
    public static final Item pickaxeVoid = reg(new ItemNormalPickaxe("void", toolMaterialAbsenceEnum.fetchMaterialTool()));
    public static final Item axeVoid = reg(new ItemNormalAxe("void", toolMaterialAbsenceEnum.fetchMaterialTool()));
    public static final Item hoeVoid = reg(new ItemVoidHoe("void", toolMaterialAbsenceEnum.fetchMaterialTool()));
    public static final Item swordAdminium = reg(new ItemAdminiumSword("adminium", toolMaterialAdminiumEnum.fetchMaterialTool()));
    public static final Item spadeAdminium = reg(new ItemAdminiumSpade("adminium", toolMaterialAdminiumEnum.fetchMaterialTool()));
    public static final Item pickaxeAdminium = reg(new ItemAdminiumPickaxe("adminium", toolMaterialAdminiumEnum.fetchMaterialTool()));
    public static final Item axeAdminium = reg(new ItemAdminiumAxe("adminium", toolMaterialAdminiumEnum.fetchMaterialTool()));
    public static final Item hoeAdminium = reg(new ItemAdminiumHoe("adminium", toolMaterialAdminiumEnum.fetchMaterialTool()));
    public static final Item helmetCopper = new ItemNormalArmor("copper", armourMaterialCopperEnum.getArmorMaterial(), 0);
    public static final Item chestplateCopper = new ItemNormalArmor("copper", armourMaterialCopperEnum.getArmorMaterial(), 1);
    public static final Item leggingsCopper = new ItemNormalArmor("copper", armourMaterialCopperEnum.getArmorMaterial(), 2);
    public static final Item bootsCopper = new ItemNormalArmor("copper", armourMaterialCopperEnum.getArmorMaterial(), 3);
    public static final Item helmetTin = new ItemNormalArmor("tin", armourMaterialTinEnum.getArmorMaterial(), 0);
    public static final Item chestplateTin = new ItemNormalArmor("tin", armourMaterialTinEnum.getArmorMaterial(), 1);
    public static final Item leggingsTin = new ItemNormalArmor("tin", armourMaterialTinEnum.getArmorMaterial(), 2);
    public static final Item bootsTin = new ItemNormalArmor("tin", armourMaterialTinEnum.getArmorMaterial(), 3);
    public static final Item helmetBronze = new ItemNormalArmor("bronze", armourMaterialBronzeEnum.getArmorMaterial(), 0);
    public static final Item chestplateBronze = new ItemNormalArmor("bronze", armourMaterialBronzeEnum.getArmorMaterial(), 1);
    public static final Item leggingsBronze = new ItemNormalArmor("bronze", armourMaterialBronzeEnum.getArmorMaterial(), 2);
    public static final Item bootsBronze = new ItemNormalArmor("bronze", armourMaterialBronzeEnum.getArmorMaterial(), 3);
    public static final Item helmetSilver = new ItemNormalArmor("silver", armourMaterialSilverEnum.getArmorMaterial(), 0);
    public static final Item chestplateSilver = new ItemNormalArmor("silver", armourMaterialSilverEnum.getArmorMaterial(), 1);
    public static final Item leggingsSilver = new ItemNormalArmor("silver", armourMaterialSilverEnum.getArmorMaterial(), 2);
    public static final Item bootsSilver = new ItemNormalArmor("silver", armourMaterialSilverEnum.getArmorMaterial(), 3);
    public static final Item helmetLead = new ItemNormalArmor("lead", armourMaterialLeadEnum.getArmorMaterial(), 0);
    public static final Item chestplateLead = new ItemNormalArmor("lead", armourMaterialLeadEnum.getArmorMaterial(), 1);
    public static final Item leggingsLead = new ItemNormalArmor("lead", armourMaterialLeadEnum.getArmorMaterial(), 2);
    public static final Item bootsLead = new ItemNormalArmor("lead", armourMaterialLeadEnum.getArmorMaterial(), 3);
    public static final Item helmetSteel = new ItemNormalArmor("steel", armourMaterialSteelEnum.getArmorMaterial(), 0);
    public static final Item chestplateSteel = new ItemNormalArmor("steel", armourMaterialSteelEnum.getArmorMaterial(), 1);
    public static final Item leggingsSteel = new ItemNormalArmor("steel", armourMaterialSteelEnum.getArmorMaterial(), 2);
    public static final Item bootsSteel = new ItemNormalArmor("steel", armourMaterialSteelEnum.getArmorMaterial(), 3);
    public static final Item helmetTitanium = new ItemNormalArmor("titanium", armourMaterialTitaniumEnum.getArmorMaterial(), 0);
    public static final Item chestplateTitanium = new ItemNormalArmor("titanium", armourMaterialTitaniumEnum.getArmorMaterial(), 1);
    public static final Item leggingsTitanium = new ItemNormalArmor("titanium", armourMaterialTitaniumEnum.getArmorMaterial(), 2);
    public static final Item bootsTitanium = new ItemNormalArmor("titanium", armourMaterialTitaniumEnum.getArmorMaterial(), 3);
    public static final Item helmetPlatinum = new ItemNormalArmor("platinum", armourMaterialPlatinumEnum.getArmorMaterial(), 0);
    public static final Item chestplatePlatinum = new ItemNormalArmor("platinum", armourMaterialPlatinumEnum.getArmorMaterial(), 1);
    public static final Item leggingsPlatinum = new ItemNormalArmor("platinum", armourMaterialPlatinumEnum.getArmorMaterial(), 2);
    public static final Item bootsPlatinum = new ItemNormalArmor("platinum", armourMaterialPlatinumEnum.getArmorMaterial(), 3);
    public static final Item ironHelmetPig = new ItemPigIronArmor("pig_iron", armourMaterialPigIronEnum.getArmorMaterial(), 0);
    public static final Item ironChestplatePig = new ItemPigIronArmor("pig_iron", armourMaterialPigIronEnum.getArmorMaterial(), 1);
    public static final Item ironLeggingsPig = new ItemPigIronArmor("pig_iron", armourMaterialPigIronEnum.getArmorMaterial(), 2);
    public static final Item ironBootsPig = new ItemPigIronArmor("pig_iron", armourMaterialPigIronEnum.getArmorMaterial(), 3);
    public static final Item helmetRuby = new ItemNormalArmor("ruby", armourMaterialRubyEnum.getArmorMaterial(), 0);
    public static final Item chestplateRuby = new ItemNormalArmor("ruby", armourMaterialRubyEnum.getArmorMaterial(), 1);
    public static final Item leggingsRuby = new ItemNormalArmor("ruby", armourMaterialRubyEnum.getArmorMaterial(), 2);
    public static final Item bootsRuby = new ItemNormalArmor("ruby", armourMaterialRubyEnum.getArmorMaterial(), 3);
    public static final Item helmetDemontium = new ItemDemontiumArmor("demontium", armourMaterialDemontiumEnum.getArmorMaterial(), 0, 2.0F, 0.1125F);
    public static final Item chestplateDemontium = new ItemDemontiumArmor("demontium", armourMaterialDemontiumEnum.getArmorMaterial(), 1, 4.0F, 0.1F);
    public static final Item leggingsDemontium = new ItemDemontiumArmor("demontium", armourMaterialDemontiumEnum.getArmorMaterial(), 2, 3.5F, 0.10625F);
    public static final Item bootsDemontium = new ItemDemontiumArmor("demontium", armourMaterialDemontiumEnum.getArmorMaterial(), 3, 1.75F, 0.125F);
    public static final Item helmetHarcadium = new ItemHarcadiumArmor("harcadium", armourMaterialHarcadiumEnum.getArmorMaterial(), 0, 4.25F, 0.005125F);
    public static final Item chestplateHarcadium = new ItemHarcadiumArmor("harcadium", armourMaterialHarcadiumEnum.getArmorMaterial(), 1, 6.0F, 0.005F);
    public static final Item leggingsHarcadium = new ItemHarcadiumArmor("harcadium", armourMaterialHarcadiumEnum.getArmorMaterial(), 2, 4.5F, 0.0050625F);
    public static final Item bootsHarcadium = new ItemHarcadiumArmor("harcadium", armourMaterialHarcadiumEnum.getArmorMaterial(), 3, 2.25F, 0.00525F);
    public static final Item helmetWithrenium = new ItemWithreniumArmor("withrenium", armourMaterialWithreniumEnum.getArmorMaterial(), 0, 5.25F, 2.5125E-4F);
    public static final Item chestplateWithrenium = new ItemWithreniumArmor("withrenium", armourMaterialWithreniumEnum.getArmorMaterial(), 1, 14.0F, 2.5E-4F);
    public static final Item leggingsWithrenium = new ItemWithreniumArmor("withrenium", armourMaterialWithreniumEnum.getArmorMaterial(), 2, 10.5F, 2.50625E-4F);
    public static final Item bootsWithrenium = new ItemWithreniumArmor("withrenium", armourMaterialWithreniumEnum.getArmorMaterial(), 3, 5.25F, 2.525E-4F);
    public static final Item helmetTitanus = new ItemTitanusArmor("titanus", armourMaterialTitanusEnum.getArmorMaterial(), 0, 15.0F, 1.25125E-5F);
    public static final Item chestplateTitanus = new ItemTitanusArmor("titanus", armourMaterialTitanusEnum.getArmorMaterial(), 1, 40.0F, 1.25E-5F);
    public static final Item leggingsTitanus = new ItemTitanusArmor("titanus", armourMaterialTitanusEnum.getArmorMaterial(), 2, 30.0F, 1.250625E-5F);
    public static final Item bootsTitanus = new ItemTitanusArmor("titanus", armourMaterialTitanusEnum.getArmorMaterial(), 3, 15.0F, 1.2525E-5F);
    public static final Item helmetVoid = new ItemVoidArmor("void", armourMaterialAbsenceEnum.getArmorMaterial(), 0, 24.0F, 1.25125E-6F);
    public static final Item chestplateVoid = new ItemVoidArmor("void", armourMaterialAbsenceEnum.getArmorMaterial(), 1, 60.0F, 1.25E-6F);
    public static final Item leggingsVoid = new ItemVoidArmor("void", armourMaterialAbsenceEnum.getArmorMaterial(), 2, 40.0F, 1.250625E-6F);
    public static final Item bootsVoid = new ItemVoidArmor("void", armourMaterialAbsenceEnum.getArmorMaterial(), 3, 18.0F, 1.2525E-6F);
    public static final Item helmetAdminium = new ItemAdminiumArmor("adminium", armourMaterialAdminiumEnum.getArmorMaterial(), 0, 38.4F, 2.5125E-8F);
    public static final Item chestplateAdminium = new ItemAdminiumArmor("adminium", armourMaterialAdminiumEnum.getArmorMaterial(), 1, 102.4F, 2.5E-8F);
    public static final Item leggingsAdminium = new ItemAdminiumArmor("adminium", armourMaterialAdminiumEnum.getArmorMaterial(), 2, 76.8F, 2.50625E-8F);
    public static final Item bootsAdminium = new ItemAdminiumArmor("adminium", armourMaterialAdminiumEnum.getArmorMaterial(), 3, 38.4F, 2.525E-8F);
    public static final Item stringDiamond = reg(new ItemBase("diamond_string"));
    public static final Item serumGrowth = reg(new ItemGrowthSerum());
    public static final Item teleporter = reg(new ItemTeleporter());
    public static final Item teleporter2 = reg(new ItemTeleporter2());
    public static final Item turretGood = reg(new ItemGoodTurret("turret_good", GoodTurret.fetchMaterialTool()));
    public static final Item turret2Good = reg(new ItemGoodTurretGround("machine_gun_good", GoodTurret.fetchMaterialTool()));
    public static final Item turret3Good = reg(new ItemGoodTurretMortar("mortar_good", GoodTurret.fetchMaterialTool()));
    public static final Item potatoeGolden = reg("food", food(new ItemAppleGolden(6, 1.2F, false), "golden_potatoe"));
    public static final Item breadGolden = reg("food", food(new ItemAppleGolden(5, 1.2F, false), "golden_bread"));
    public static final Item cookieGolden = reg("food", food(new ItemAppleGolden(2, 1.2F, false), "golden_cookie"));
    public static final Item melonGolden = reg("food", food(new ItemAppleGolden(2, 1.2F, false), "golden_melon"));
    public static final Item pumpkinPieGolden = reg("food", food(new ItemAppleGolden(8, 1.2F, false), "golden_pumpkin_pie"));
    public static final Item appleJadeite = reg("food", food(new ItemAppleDiamond(4, 2.4F, false), "apple_jadeite", CreativeTabs.FOOD));
    public static final Item breadJadeite = reg("food", food(new ItemAppleDiamond(8, 2.4F, false), "jadeite_bread"));
    public static final Item potatoJadeite = reg("food", food(new ItemAppleDiamond(10, 2.4F, false), "jadeite_potato"));
    public static final Item cookieJadeite = reg("food", food(new ItemAppleDiamond(3, 2.4F, false), "jadeite_cookie"));
    public static final Item melonJadeite = reg("food", food(new ItemAppleDiamond(3, 2.4F, false), "jadeite_melon"));
    public static final Item pumpkinPieJadeite = reg("food", food(new ItemAppleDiamond(16, 2.4F, false), "jadeite_pumpkin_pie"));
    public static final Item appleDiamond = reg("food", food(new ItemAppleDiamond(4, 2.4F, false), "apple_diamond", CreativeTabs.FOOD));
    public static final Item potatoeDiamond = reg("food", food(new ItemAppleDiamond(6, 2.4F, false), "diamond_potatoe"));
    public static final Item breadDiamond = reg("food", food(new ItemAppleDiamond(5, 2.4F, false), "diamond_bread"));
    public static final Item cookieDiamond = reg("food", food(new ItemAppleDiamond(2, 2.4F, false), "diamond_cookie"));
    public static final Item melonDiamond = reg("food", food(new ItemAppleDiamond(2, 2.4F, false), "diamond_melon"));
    public static final Item pumpkinPieDiamond = reg("food", food(new ItemAppleDiamond(8, 2.4F, false), "diamond_pumpkin_pie"));
    public static final Item bladeSeedPleasant = reg(new ItemPleasantBladeSeed());
    public static final Item bladeLeafPleasant = reg(new ItemBase("pleasant_blade_leaf"));
    public static final Item bladeFlowerPleasant = reg(new ItemPleasantBladeFlower(2, 1.2F, false));
    public static final Item bladeBrewPleasant = reg(new ItemPleasantBladeBrew());
    public static final Item malgrum = reg("food", food(new ItemFoodMalgrum(20, 20F, false), "malgrum_fruit"));
    public static final Item seedsMalgrum = reg("seed", new ItemMalgrumSeeds());
    public static final Item chaff = reg(new ItemChaff());
    public static final Item skeletonSpawnerWither = reg(new ItemForTheChallengeGames1());
    public static final Item ironGolemBetterOvum = reg(new ItemEggReinforcedIronGolem());
    public static final Item birthStackEvent = reg(new ItemEventSpawner());
    public static final Item titansModBirthOvumRegular = reg(new ItemSpawnEggRegular());
    public static final Item snowGolemGiantOvum = new ItemTitanSpawnEgg("spawn_egg_snow_golem_titan");
    public static final Item slimeGiantOvum = new ItemTitanSpawnEgg("spawn_egg_slime_titan");
    public static final Item magmaCubeGiantOvum = new ItemTitanSpawnEgg("spawn_egg_magma_cube_titan");
    public static final Item omegafishOvum = new ItemTitanSpawnEgg("spawn_egg_omegafish");
    public static final Item caveSpiderGiantOvum = new ItemTitanSpawnEgg("spawn_egg_cave_spider_titan");
    public static final Item spiderGiantOvum = new ItemTitanSpawnEgg("spawn_egg_spider_titan");
    public static final Item spiderJockeyGiantOvum = new ItemTitanSpawnEgg("spawn_egg_spider_jockey_titan");
    public static final Item zombieGiantOvum = new ItemTitanSpawnEgg("spawn_egg_zombie_titan");
    public static final Item skeletonGiantOvum = new ItemTitanSpawnEgg("spawn_egg_skeleton_titan");
    public static final Item creeperGiantOvum = new ItemTitanSpawnEgg("spawn_egg_creeper_titan");
    public static final Item chargedCreeperGiantOvum = new ItemTitanSpawnEgg("spawn_egg_charged_creeper_titan");
    public static final Item zombiePigmanGiantOvum = new ItemTitanSpawnEgg("spawn_egg_zombie_pigman_titan");
    public static final Item blazeGiantOvum = new ItemTitanSpawnEgg("spawn_egg_blaze_titan");
    public static final Item witherSkeletonGiantOvum = new ItemTitanSpawnEgg("spawn_egg_wither_skeleton_titan");
    public static final Item witherJockeyGiantOvum = new ItemTitanSpawnEgg("spawn_egg_wither_jockey_titan");
    public static final Item gargoyleKingOvum = new ItemTitanSpawnEgg("spawn_egg_gargoyle_king");
    public static final Item ghastGiantOvum = new ItemTitanSpawnEgg("spawn_egg_ghast_titan");
    public static final Item enderColossusOvum = new ItemTitanSpawnEgg("spawn_egg_ender_colossus");
    public static final Item ultimaIronGolemGiantOvum = new ItemTitanSpawnEgg("spawn_egg_iron_golem_titan");
    public static final Item witherzillaOvum = new ItemTitanSpawnEgg("spawn_egg_witherzilla");
    public static final Item executorDragonOvum = new ItemTitanSpawnEgg("spawn_egg_executor_dragon");

    public static final Item silverfishServantOvum = new ItemSpawnEggMinion("SilverfishMinion", 7237230, 3158064, "Silverfish");
    public static final Item caveSpiderServantOvum = new ItemSpawnEggMinion("CaveSpiderMinion", 803406, 11013646, "CaveSpider");
    public static final Item spiderServantOvum = new ItemSpawnEggMinion("SpiderMinion", 3419431, 11013646, "Spider");
    public static final Item zombieServantOvum = new ItemSpawnEggMinion("ZombieMinion", 0xAFAF, 7969893, "Zombie");
    public static final Item skeletonServantOvum = new ItemSpawnEggMinion("SkeletonMinion", 12698049, 4802889, "Skeleton");
    public static final Item creeperServantOvum = new ItemSpawnEggMinion("CreeperMinion", 894731, 0, "Creeper");
    public static final Item zombiePigmanServantOvum = new ItemSpawnEggMinion("PigZombieMinion", 15373203, 5009705, "PigZombie");
    public static final Item blazeServantOvum = new ItemSpawnEggMinion("BlazeMinion", 16167425, 16775294, "Blaze");
    public static final Item witherSkeletonServantOvum = new ItemSpawnEggMinion("WitherSkeletonMinion", 0, 1447446, "WitherSkeleton");
    public static final Item ghastServantOvum = new ItemSpawnEggMinion("GhastMinion", 16382457, 12369084, "Ghast");
    public static final Item endermanServantOvum = new ItemSpawnEggMinion("EndermanMinion", 1447446, 0, "Enderman");
    public static final Item[] eggsServant = new Item [] {
        silverfishServantOvum, caveSpiderServantOvum, spiderServantOvum, zombieServantOvum, skeletonServantOvum,
                creeperServantOvum, zombiePigmanServantOvum, blazeServantOvum, witherSkeletonServantOvum, ghastServantOvum, endermanServantOvum
    };

    public static final Item[] armourSetCopper = new Item [] {helmetCopper, chestplateCopper, leggingsCopper, bootsCopper};
    public static final Item[] armourSetTin = new Item [] {helmetTin, chestplateTin, leggingsTin, bootsTin};
    public static final Item[] armourSetBronze = new Item [] {helmetBronze, chestplateBronze, leggingsBronze, bootsBronze};
    public static final Item[] armourSetSilver = new Item [] {helmetSilver, chestplateSilver, leggingsSilver, bootsSilver};
    public static final Item[] armourSetLead = new Item [] {helmetLead, chestplateLead, leggingsLead, bootsLead};
    public static final Item[] armourSetSteel = new Item [] {helmetSteel, chestplateSteel, leggingsSteel, bootsSteel};
    public static final Item[] armourSetTitanium = new Item [] {helmetTitanium, chestplateTitanium, leggingsTitanium, bootsTitanium};
    public static final Item[] armourSetPlatinum = new Item [] {helmetPlatinum, chestplatePlatinum, leggingsPlatinum, bootsPlatinum};
    public static final Item[] ironArmourSetPig = new Item [] {ironHelmetPig, ironChestplatePig, ironLeggingsPig, ironBootsPig};
    public static final Item[] armourSetRuby = new Item [] {helmetRuby, chestplateRuby, leggingsRuby, bootsRuby};
    public static final Item[] armourSetDemontium = new Item [] {helmetDemontium, chestplateDemontium, leggingsDemontium, bootsDemontium};
    public static final Item[] armourSetHarcadium = new Item [] {helmetHarcadium, chestplateHarcadium, leggingsHarcadium, bootsHarcadium};
    public static final Item[] armourSetWithrenium = new Item [] {helmetWithrenium, chestplateWithrenium, leggingsWithrenium, bootsWithrenium};
    public static final Item[] armourSetTitanus = new Item [] {helmetTitanus, chestplateTitanus, leggingsTitanus, bootsTitanus};
    public static final Item[] armourSetVoid = new Item [] {helmetVoid, chestplateVoid, leggingsVoid, bootsVoid};
    public static final Item[] armourSetAdminium = new Item [] {helmetAdminium, chestplateAdminium, leggingsAdminium, bootsAdminium};
    public static final Item[] eggsBirth = new Item [] {
        snowGolemGiantOvum,
                slimeGiantOvum,
                magmaCubeGiantOvum,
                omegafishOvum,
                caveSpiderGiantOvum,
                spiderGiantOvum,
                spiderJockeyGiantOvum,
                zombieGiantOvum,
                skeletonGiantOvum,
                creeperGiantOvum,
                chargedCreeperGiantOvum,
                zombiePigmanGiantOvum,
                blazeGiantOvum,
                witherSkeletonGiantOvum,
                witherJockeyGiantOvum,
                gargoyleKingOvum,
                ghastGiantOvum,
                enderColossusOvum,
                ultimaIronGolemGiantOvum,
                witherzillaOvum,
                executorDragonOvum
    };

    // food builder helper. Applies unlocalized/registry name + creative tab +
    // always-edible + regen effect that the old inline setTextureName/setPotionEffect chain did.
    private static Item food(ItemFood item, String name) {
        return food(item, name, TheTitans.pageTitans);
    }

    private static Item food(ItemFood item, String name, CreativeTabs tab) {
        item.setTranslationKey(name);
        item.setRegistryName(new ResourceLocation(TheTitans.MODID_V, name));
        item.setCreativeTab(tab);
        item.setAlwaysEdible();
        item.setPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 1), 1.0F);
        return item;
    }

    private static boolean setupDid = false;

    public static void setup() {
        if (setupDid) return;
        setupDid = true;

        register("gemJadeite", jadeite);
        register("moltenFuel", moltenFuel);

        register(armourSetCopper);
        register(armourSetTin);
        register(armourSetBronze);
        register(armourSetSilver);
        register(armourSetLead);
        register(armourSetSteel);
        register(armourSetTitanium);
        register(armourSetPlatinum);
        register(ironArmourSetPig);
        register(armourSetRuby);
        register(armourSetDemontium);
        register(armourSetHarcadium);
        register(armourSetWithrenium);
        register(armourSetTitanus);
        register(armourSetVoid);
        register(armourSetAdminium);

        register(eggsBirth);
        register(eggsServant);
        if (Loader.isModLoaded("OreSpawn")) {
            scorpionOvumOverlord = new ItemSpawnEggTitans("eggoverlordscorpion", "OverlordScorpion");
            krakenOvumMethuselah = new ItemSpawnEggTitans("eggelderkraken", "MethuselahKraken");
            mobzillaOvumBurning = new ItemSpawnEggTitans("egggodzillaburning", "BurningMobzilla");
            register(scorpionOvumOverlord);
            register(krakenOvumMethuselah);
            register(mobzillaOvumBurning);
        }

        toolMaterialCopperEnum.fetchMaterialTool().setRepairItem(new ItemStack(ingotCopperZ));
        armourMaterialCopperEnum.getArmorMaterial().setRepairItem(new ItemStack(ingotCopperZ));
        toolMaterialTinEnum.fetchMaterialTool().setRepairItem(new ItemStack(ingotTinZ));
        armourMaterialTinEnum.getArmorMaterial().setRepairItem(new ItemStack(ingotTinZ));
        toolMaterialBronzeEnum.fetchMaterialTool().setRepairItem(new ItemStack(ingotBronzeZ));
        armourMaterialBronzeEnum.getArmorMaterial().setRepairItem(new ItemStack(ingotBronzeZ));
        toolMaterialSteelEnum.fetchMaterialTool().setRepairItem(new ItemStack(ingotSteelZ));
        armourMaterialSteelEnum.getArmorMaterial().setRepairItem(new ItemStack(ingotSteelZ));
        toolMaterialHarcadiumEnum.fetchMaterialTool().setRepairItem(new ItemStack(harcadium));
        armourMaterialHarcadiumEnum.getArmorMaterial().setRepairItem(new ItemStack(harcadium));
        toolMaterialAbsenceEnum.fetchMaterialTool().setRepairItem(new ItemStack(stackVoid));
        armourMaterialAbsenceEnum.getArmorMaterial().setRepairItem(new ItemStack(stackVoid));
        toolMaterialTitanusEnum.fetchMaterialTool().setRepairItem(new ItemStack(ingotTitanusZ));
        armourMaterialTitanusEnum.getArmorMaterial().setRepairItem(new ItemStack(ingotTitanusZ));
        toolMaterialWithreniumEnum.fetchMaterialTool().setRepairItem(new ItemStack(withrenium));
        armourMaterialWithreniumEnum.getArmorMaterial().setRepairItem(new ItemStack(withrenium));
    }

    // Called from TheTitans RegistryEvent.Register<Item>. Pushes every collected item
    // into the Forge registry (setting registry name if a leaf class didn't) + ore dict.
    public static void registerAll(net.minecraftforge.registries.IForgeRegistry<Item> registry) {
        for (Item item : registerTo)
            if (item != null) {
            if (item.getRegistryName() == null)
                item.setRegistryName(new ResourceLocation(TheTitans.MODID_V, item.getTranslationKey().substring(5)));
            registry.register(item);
            registryStack.add(item);
        }
        for (Map.Entry<Item, String> entry : dictsQueued.entrySet())
            OreDictionary.registerOre(entry.getValue(), entry.getKey());
        moltenFuel.setBurnTime(2000);
    }

    public static void preInit() {
    }

    public static void init() {
        TheTitans.fuelThe.add(moltenFuel);
    }

    public static void postInit() {
        for(Map.Entry<Item, Boolean> entry : registryStack.keySet())
            if(entry.getKey().getItemStackDisplayName(new ItemStack(entry.getKey())).equalsIgnoreCase(entry.getKey().getTranslationKey() + ".name"))
            TheTitans.logger.addError("Localization key returned null: " + entry.getKey().getTranslationKey() + ".name=");
    }

    private static void register(String oreDictionary, Item... items) {
        register(items);
        for (Item item : items)
            if (item != null)
            dictsQueued.put(item, oreDictionary);
    }

    private static void register(Item... items) {
        for (Item item : items)
            if (item != null)
            registerTo.add(item);
    }

    public static boolean exists(Item item) {
        return Item.REGISTRY.containsKey(new ResourceLocation(TheTitans.MODID_V, item.getTranslationKey().substring(5)));
    }
}
