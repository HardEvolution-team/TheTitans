package com.electrifiedded.thetitans.registry;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.block.misc.BlockCorminium;
import com.electrifiedded.thetitans.block.misc.BlockNormal;
import com.electrifiedded.thetitans.block.misc.BlockPerch;
import com.electrifiedded.thetitans.block.ore.BlockAdamantiumOre;
import com.electrifiedded.thetitans.block.ore.BlockHarcadiumOre;
import com.electrifiedded.thetitans.block.ore.BlockNormalOre;
import com.electrifiedded.thetitans.block.ore.BlockOreEgg;
import com.electrifiedded.thetitans.block.ore.BlockTitanusOre;
import com.electrifiedded.thetitans.block.ore.BlockVoidOre;
import com.electrifiedded.thetitans.block.ore.BlockWithreniumOre;
import com.electrifiedded.thetitans.block.plant.BlockMagicPumpkin;
import com.electrifiedded.thetitans.block.plant.BlockMalgrumCrop;
import com.electrifiedded.thetitans.block.plant.BlockPleasantBladeCrop;
import com.electrifiedded.thetitans.block.storage.BlockAdamantiumBlock;
import com.electrifiedded.thetitans.block.storage.BlockCompactBedrock;
import com.electrifiedded.thetitans.block.storage.BlockDemontiumBlock;
import com.electrifiedded.thetitans.block.storage.BlockHarcadiumBlock;
import com.electrifiedded.thetitans.block.storage.BlockNormalCompressed;
import com.electrifiedded.thetitans.block.storage.BlockTitanusBlock;
import com.electrifiedded.thetitans.block.storage.BlockVoidBlock;
import com.electrifiedded.thetitans.block.storage.BlockWithreniumBlock;
import com.electrifiedded.thetitans.util.RegistryHelper.Registry;
import com.electrifiedded.thetitans.util.RegistryHelper;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TitanBlockSet {
    // Kept for the config GUI (per-block enable flags). Actual Forge registration
    // happens in registerAll(...) during RegistryEvent.Register<Block>.
    public static final Registry<Block, Boolean> registryCube = new Registry<Block, Boolean>(true);
    // Ordered list of everything to push into the Forge registry, built by setup().
    private static final List<Block> registerTo = new ArrayList<Block>();
    // exposes registered blocks for client model registration.
    public static java.util.List<Block> fetchBlocksRegistered() { return registerTo; }
    private static Map<Block, String> dictsQueued = new HashMap<Block, String>();

    private static <X extends Block> X reg(X obj) {
        registerTo.add(obj);
        return obj;
    }

    private static <X extends Block> X reg(String oreDictionary, X obj) {
        registerTo.add(obj);
        dictsQueued.put(obj, oreDictionary);
        return obj;
    }

    //Titan Blocks
    public static final Block stoneperch = reg(new BlockPerch("stoneperch", "cobblestone", 1.5F, 10F));
    public static final Block sandstoneperch = reg(new BlockPerch("sandstoneperch", "sandstone_bottom", 0.8F, 5F));
    public static final Block obsidianperch = reg(new BlockPerch("obsidianperch", "obsidian", 50F, 2000F));
    public static final Block goldperch = reg(new BlockPerch("goldperch", "gold_block", 3F, 10F));
    public static final Block ironperch = reg(new BlockPerch("ironperch", "iron_block", 5F, 10F));
    public static final Block endstoneperch = reg(new BlockPerch("endstoneperch", "end_stone", 3F, 15F));
    public static final Block netherbrickperch = reg(new BlockPerch("netherbrickperch", "nether_brick", 2F, 10F));
    public static final Block netherite = reg("netherite", new BlockNormal("netherite", 0, 1.5F, 10.0F));
    public static final Block bedrock_block = reg(new BlockCompactBedrock(Material.ROCK, "bedrock_compact"));
    public static final Block corminium = reg(new BlockCorminium(Material.ROCK, "corminium"));
    private static final Block blocksBase[] = {Blocks.STONE, Blocks.END_STONE, Blocks.NETHERRACK, Blocks.OBSIDIAN, Blocks.BEDROCK, netherite, bedrock_block};

    public static final Block[] oreJadeiteZ = generateOres("jadeite", TitanGear.jadeite, 4, 4.0F, 3500.0F);
    public static final Block[] fuelOreMolten = generateOres("molten_fuel", TitanGear.moltenFuel, 0, 3.0F, 5.0F);
    public static final Block[] ironOrePig = generateOres("pig_iron", 1, 3.0F, 5.0F);
    public static final Block[] oreRubyZ = generateOres("ruby", TitanGear.ruby, 2, 3.0F, 5.0F);
    public static final Block[] oreSapphireZ = generateOres("sapphire", TitanGear.sapphire, 2, 3.0F, 5.0F);
    public static final Block[] oreCopperZ = generateOres("copper", 0, 3.0F, 5.0F);
    public static final Block[] oreTinZ = generateOres("tin", 0, 3.0F, 5.0F);
    public static final Block[] oreChromiumZ = generateOres("chromium", 1, 3.0F, 5.0F);
    public static final Block[] oreNickelZ = generateOres("nickel", 1, 3.0F, 5.0F);
    public static final Block[] oreLeadZ = generateOres("lead", 1, 5.0F, 5.0F);
    public static final Block[] oreSilverZ = generateOres("silver", 1, 5.0F, 10.0F);
    public static final Block[] orePlatinumZ = generateOres("platinum", 2, 5.0F, 15.0F);
    public static final Block[] oreTitaniumZ = generateOres("titanium", 3, 5.0F, 20.0F);
    public static final Block[] oreCoalZ = generateOres("coal", Items.COAL, 0, 3.0F, 5.0F, Blocks.END_STONE, Blocks.NETHERRACK, Blocks.OBSIDIAN, Blocks.BEDROCK, netherite, bedrock_block);
    public static final Block[] oreIronZ = generateOres("iron", 0, 3.0F, 5.0F, Blocks.END_STONE, Blocks.NETHERRACK, Blocks.OBSIDIAN, Blocks.BEDROCK, netherite, bedrock_block);
    public static final Block[] oreLapis = generateOres("lapis", 1, 3.0F, 5.0F, Blocks.END_STONE, Blocks.NETHERRACK, Blocks.OBSIDIAN, Blocks.BEDROCK, netherite, bedrock_block);
    public static final Block[] oreRedstoneZ = generateOres("redstone", Items.REDSTONE, 5, 2, 3.0F, 5.0F, Blocks.END_STONE, Blocks.NETHERRACK, Blocks.OBSIDIAN, Blocks.BEDROCK, netherite, bedrock_block);
    public static final Block[] oreGoldZ = generateOres("gold", 2, 3.0F, 5.0F, Blocks.END_STONE, Blocks.NETHERRACK, Blocks.OBSIDIAN, Blocks.BEDROCK, netherite, bedrock_block);
    public static final Block[] oreDiamondZ = generateOres("diamond", Items.DIAMOND, 2, 3.0F, 5.0F, Blocks.END_STONE, Blocks.NETHERRACK, Blocks.OBSIDIAN, Blocks.BEDROCK, netherite, bedrock_block);
    public static final Block[] oreEmeraldZ = generateOres("emerald", Items.EMERALD, 2, 3.0F, 5.0F, Blocks.END_STONE, Blocks.NETHERRACK, Blocks.OBSIDIAN, Blocks.BEDROCK, netherite, bedrock_block);
    public static final Block copper_block = reg("blockCopper", new BlockNormalCompressed(MapColor.RED, "copper_block"));
    public static final Block tin_block = reg("blockTin", new BlockNormalCompressed(MapColor.GRAY, "tin_block"));
    public static final Block bronze_block = reg("blockBronze", new BlockNormalCompressed(MapColor.BROWN, "bronze_block"));
    public static final Block chromium_block = reg("blockChromium", new BlockNormalCompressed(MapColor.GRAY, "chromium_block"));
    public static final Block nickel_block = reg("blockNickel", new BlockNormalCompressed(MapColor.GRAY, "nickel_block"));
    public static final Block steel_block = reg("blockSteel", new BlockNormalCompressed(MapColor.GRAY, "steel_block"));
    public static final Block lead_block = reg("blockLead", new BlockNormalCompressed(MapColor.GRAY, "lead_block"));
    public static final Block silver_block = reg("blockSilver", new BlockNormalCompressed(MapColor.SILVER, "silver_block"));
    public static final Block platinum_block = reg("blockPlatinum", new BlockNormalCompressed(MapColor.SILVER, "platinum_block"));
    public static final Block titanium_block = reg("blockTitanium", new BlockNormalCompressed(MapColor.SILVER, "titanium_block"));
    public static final Block jadeite_block = reg("blockJadeite", new BlockNormalCompressed(MapColor.GREEN, "jadeite_block"));
    public static final BlockNormalCompressed molten_fuel_block = new BlockNormalCompressed(MapColor.RED, "molten_fuel_block");
    public static final Block pig_iron_block = reg("blockPigIron", new BlockNormalCompressed(MapColor.PINK, "pig_iron_block"));
    public static final Block ruby_block = reg("blockRuby", new BlockNormalCompressed(MapColor.RED, "ruby_block"));
    public static final Block sapphire_block = reg("blockSapphire", new BlockNormalCompressed(MapColor.BLUE, "sapphire_block"));
    public static final Block[] oreDemontiumZ = generateOres("demontium", 8, 20.0F, 2000.0F);
    public static final Block[] oreOnyxZ = generateOres("onyx", TitanGear.onyx, 2, 3.0F, 5.0F);
    public static final Block onyx_block = reg("blockOnyx", new BlockNormalCompressed(MapColor.BLACK, "onyx_block"));
    public static final Block[] oreTitanusZ = generateOres("titanus", 0, 0.0F, 0.0F);
    public static final Block titanus_block = reg("blockTitanus", new BlockTitanusBlock("titanus_block"));
    public static final Block oreHarcadiumZ = reg("oreHarcadium", new BlockHarcadiumOre("harcadium"));
    public static final Block oreEndStoneHarcadium = reg("oreHarcadium", new BlockHarcadiumOre(Blocks.END_STONE, "harcadium"));
    public static final Block oreObsidianHarcadium = reg("oreHarcadium", new BlockHarcadiumOre(Blocks.OBSIDIAN, "harcadium"));
    public static final Block[] oreOtherHarcadium = generateOres("harcadium", 0, 0.0F, 0.0F, Blocks.NETHERRACK, Blocks.BEDROCK, netherite, bedrock_block);
    public static final Block[] oreWithreniumZ = generateOres("withrenium", 0, 0.0F, 0.0F);
    public static final Block oreVoidZ = reg("oreVoid", new BlockVoidOre("void"));
    public static final Block oreEndStoneVoid = reg("oreVoid", new BlockVoidOre(Blocks.END_STONE, "void"));
    public static final Block oreObsidianVoid = reg("oreVoid", new BlockVoidOre(Blocks.OBSIDIAN, "void"));
    public static final Block[] oreOtherVoid = generateOres("void", 0, 0.0F, 0.0F, Blocks.NETHERRACK, Blocks.BEDROCK, netherite, bedrock_block);
    public static final Block oreAdamantiumZ = reg("oreAdamantium", new BlockAdamantiumOre(bedrock_block, "adamantium"));
    public static final Block[] oreOtherAdamantium = generateOres("adamantium", 0, 0.0F, 0.0F, Blocks.STONE, Blocks.END_STONE, Blocks.NETHERRACK, Blocks.OBSIDIAN, Blocks.BEDROCK, netherite);
    public static final Block demontium_block = reg("blockDemontium", new BlockDemontiumBlock("demontium_block"));
    public static final Block harcadium_block = reg("blockHarcadium", new BlockHarcadiumBlock("harcadium_block"));
    public static final Block withrenium_block = reg("blockWithirenium", new BlockWithreniumBlock("withrenium_block"));
    public static final Block void_block = reg("blockVoid", new BlockVoidBlock("void_block"));
    public static final Block adamantium_block = reg("blockAdamantium", new BlockAdamantiumBlock("adamantium_block"));
    public static final Block magic_pumpkin = reg(new BlockMagicPumpkin(false));
    public static final Block cropMalgrum = reg(new BlockMalgrumCrop());
    public static final Block bladeCropPleasant = reg(new BlockPleasantBladeCrop());
    //Orespawn Blocks
    public static Block overlordScorpionPieceBirthCubeMy;
    public static Block overlordScorpionBirthCubeMy;
    public static Block methuselahKrakenPieceBirthCubeMy;
    public static Block methuselahKrakenBirthCubeMy;
    public static Block burningMobzillaPieceBirthCubeMy;
    public static Block burningMobzillaBirthCubeMy;
    private static boolean setupDid = false;

    public static void setup() {
        if (setupDid) return;
        setupDid = true;
        Blocks.BEDROCK.setHardness(1000000000.0F);
        Blocks.BEDROCK.setHarvestLevel("pickaxe", Integer.MAX_VALUE - 1);
        Blocks.BEDROCK.setCreativeTab(TheTitans.blocksPageGiant);

        register("oreJadeite", oreJadeiteZ);
        register("oreMoltenFuel", fuelOreMolten);
        register("orePigIron", ironOrePig);
        register("oreRuby", oreRubyZ);
        register("oreSapphire", oreSapphireZ);

        register("oreCoal", oreCoalZ);
        register("oreIron", oreIronZ);
        register("oreGold", oreGoldZ);
        register("oreLapisLazuli", oreLapis);
        register("oreRedstone", oreRedstoneZ);
        register("oreDiamond", oreDiamondZ);
        register("oreEmerald", oreEmeraldZ);
        register("oreCopper", oreCopperZ);
        register("oreTin", oreTinZ);
        register("oreChromium", oreChromiumZ);
        register("oreNickel", oreNickelZ);
        register("oreLead", oreLeadZ);
        register("oreSilver", oreSilverZ);
        register("orePlatinum", orePlatinumZ);
        register("oreTitanium", oreTitaniumZ);
        register("oreDemontium", oreDemontiumZ);
        register("oreOnyx", oreOnyxZ);

        register("oreTitanus", oreTitanusZ);

        register("oreHarcadium", oreOtherHarcadium);
        register("oreWithrenium", oreWithreniumZ);

        register("oreVoid", oreOtherVoid);

        register("oreAdamantium", oreOtherAdamantium);

        register("blockMoltenFuel", molten_fuel_block);

        if (Loader.isModLoaded("OreSpawn")) {
            overlordScorpionPieceBirthCubeMy = new BlockOreEgg("oreoverlordscorpionpart");
            overlordScorpionBirthCubeMy = new BlockOreEgg("oreoverlordscorpion");
            methuselahKrakenPieceBirthCubeMy = new BlockOreEgg("oreelderkrakenpart");
            methuselahKrakenBirthCubeMy = new BlockOreEgg("oreelderkraken");
            burningMobzillaPieceBirthCubeMy = new BlockOreEgg("oregodzillaburningpart");
            burningMobzillaBirthCubeMy = new BlockOreEgg("oregodzillaburning");
            register(overlordScorpionPieceBirthCubeMy);
            register(overlordScorpionBirthCubeMy);
            register(methuselahKrakenPieceBirthCubeMy);
            register(methuselahKrakenBirthCubeMy);
            register(burningMobzillaPieceBirthCubeMy);
            register(burningMobzillaBirthCubeMy);
        }
    }

    // Called from TheTitans RegistryEvent.Register<Block>. Pushes every collected
    // block into the Forge registry (setting registry name if a leaf class didn't),
    // registers ore-dictionary entries, and mirrors into registryCube for the GUI.
    public static void registerAll(net.minecraftforge.registries.IForgeRegistry<Block> registry) {
        for (Block block : registerTo)
            if (block != null) {
            if (block.getRegistryName() == null)
                block.setRegistryName(new ResourceLocation(TheTitans.MODID_V, block.getTranslationKey().substring(5)));
            registry.register(block);
            registryCube.add(block);
        }
        molten_fuel_block.setBurnTime(18000);
    }

    // ItemBlocks are registered in RegistryEvent.Register<Item>.
    public static void registerItemBlocks(net.minecraftforge.registries.IForgeRegistry<Item> registry) {
        for (Block block : registerTo)
            if (block != null && block.getRegistryName() != null)
            registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        // BUGFIX — ore-dictionary entries were registered during Register<Block>, before any ItemBlock
        // existed, so Forge denied all ~200 of them (oreCopper, blockTin, ...). Register once the ItemBlocks exist.
        for (Map.Entry<Block, String> entry : dictsQueued.entrySet())
            OreDictionary.registerOre(entry.getValue(), entry.getKey());
    }

    public static void preInit() {
    }

    public static void init() {
        TheTitans.fuelThe.add(molten_fuel_block);
    }

    public static void postInit() {
        for (Map.Entry<Block, Boolean> entry : registryCube.keySet())
            if(entry.getKey().getLocalizedName().equalsIgnoreCase(entry.getKey().getTranslationKey() + ".name"))
            TheTitans.logger.addError("Localization key returned null: " + entry.getKey().getTranslationKey() + ".name=");
    }

    private static Block[] generateOres(String materialName, int toolMaterial, float hardness, float resistance, Block... bases) {
        return generateOres(materialName, null, 1, toolMaterial, hardness, resistance, bases);
    }

    private static Block[] generateOres(String materialName, Item item, int toolMaterial, float hardness, float resistance, Block... bases) {
        return generateOres(materialName, item, 1, toolMaterial, hardness, resistance, bases);
    }

    private static Block[] generateOres(String materialName, Item item, int amount, int toolMaterial, float hardness, float resistance, Block... bases) {
        if (bases.length == 0)
            bases = blocksBase;
        Block[] ores = new Block[bases.length];
        for (int i = 0; i < bases.length; i++)
            ores[i] = oreNew(bases[i], materialName, item, amount, toolMaterial, hardness, resistance);
        return ores;
    }

    private static Block oreNew(Block base, String materialName, Item item, int amount, int toolMaterial, float hardness, float resistance) {
        switch(materialName.toLowerCase()) {
            case "harcadium": return item == null ? new BlockHarcadiumOre(base, materialName) : new BlockHarcadiumOre(base, materialName).assignDroppedStack(item);
            case "withrenium": return item == null ? new BlockWithreniumOre(base, materialName) : new BlockWithreniumOre(base, materialName).assignDroppedStack(item);
            case "void": return item == null ? new BlockVoidOre(base, materialName) : new BlockVoidOre(base, materialName).assignDroppedStack(item);
            case "adamantium": return item == null ? new BlockAdamantiumOre(base, materialName) : new BlockAdamantiumOre(base, materialName).assignDroppedStack(item);
            case "titanus": return item == null ? new BlockTitanusOre(base, materialName) : new BlockTitanusOre(base, materialName).assignDroppedStack(item);
            default: return item == null ? new BlockNormalOre(base, materialName, toolMaterial, hardness, resistance).assignDropAmount(amount) : new BlockNormalOre(base, materialName, toolMaterial, hardness, resistance).assignDroppedStack(item).assignDropAmount(amount);
        }
    }

    private static void register(String oreDictionary, Block... blocks) {
        register(blocks);
        for (Block block : blocks)
            if (block != null)
            dictsQueued.put(block, oreDictionary);
    }

    private static void register(Block... blocks) {
        for (Block block : blocks)
            if (block != null)
            registerTo.add(block);
    }

    public static boolean exists(Block block) {
        return Block.REGISTRY.containsKey(new ResourceLocation(TheTitans.MODID_V, block.getTranslationKey().substring(5)));
    }
}
