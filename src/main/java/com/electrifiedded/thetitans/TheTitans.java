package com.electrifiedded.thetitans;

import com.electrifiedded.thetitans.api.IReloadable;
import com.electrifiedded.thetitans.command.CommandClearMinions;
import com.electrifiedded.thetitans.command.CommandDebug;
import com.electrifiedded.thetitans.command.CommandEvent;
import com.electrifiedded.thetitans.command.CommandNewDifficulty;
import com.electrifiedded.thetitans.command.CommandNewKill;
import com.electrifiedded.thetitans.command.CommandTitans;
import com.electrifiedded.thetitans.config.TitanConfig;
import com.electrifiedded.thetitans.config.TitanConfigGui;
import com.electrifiedded.thetitans.core.TheCore;
import com.electrifiedded.thetitans.core.client.TheClientCore;
import com.electrifiedded.thetitans.data.TitanVars;
import com.electrifiedded.thetitans.enchant.EnchantmentDurability;
import com.electrifiedded.thetitans.enchant.EnchantmentFerocity;
import com.electrifiedded.thetitans.enchant.EnchantmentKnockup;
import com.electrifiedded.thetitans.enchant.EnchantmentManiac;
import com.electrifiedded.thetitans.enchant.EnchantmentShurakin;
import com.electrifiedded.thetitans.enchant.EnchantmentTitanSlayer;
import com.electrifiedded.thetitans.enchant.EnchantmentUnstablility;
import com.electrifiedded.thetitans.entity.minion.EntitySkeletonMinion;
import com.electrifiedded.thetitans.entity.minion.EnumMinionType;
import com.electrifiedded.thetitans.entity.minion.IMinion;
import com.electrifiedded.thetitans.entity.titan.EntityGargoyle;
import com.electrifiedded.thetitans.entity.titan.EntityWitherTurret;
import com.electrifiedded.thetitans.entity.titan.EntityWitherTurretGround;
import com.electrifiedded.thetitans.entity.titan.EntityWitherTurretMortar;
import com.electrifiedded.thetitans.handler.EventStore;
import com.electrifiedded.thetitans.handler.PortAttributionHandler;
import com.electrifiedded.thetitans.handler.PulseHandler;
import com.electrifiedded.thetitans.handler.TitanVigor;
import com.electrifiedded.thetitans.handler.RealmHandler;
import com.electrifiedded.thetitans.item.spawn.ItemSpawnEggMinion;
import com.electrifiedded.thetitans.mixin.MixinRangedAttribute;
import com.electrifiedded.thetitans.network.NetworkHandler;
import com.electrifiedded.thetitans.proxy.ClientProxy;
import com.electrifiedded.thetitans.proxy.CommonProxy;
import com.electrifiedded.thetitans.registry.RecipeForge;
import com.electrifiedded.thetitans.registry.TitanBlockSet;
import com.electrifiedded.thetitans.registry.TitanBurnables;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanTrophies;
import com.electrifiedded.thetitans.util.DebugExtender;
import com.electrifiedded.thetitans.util.Maths;
import com.electrifiedded.thetitans.util.ReflectionHelper;
import com.electrifiedded.thetitans.util.Utils;
import com.electrifiedded.thetitans.world.biome.BiomeGenNowhere;
import com.electrifiedded.thetitans.world.biome.BiomeGenVoid;
import com.electrifiedded.thetitans.world.dimension.WorldProviderNowhere;
import com.electrifiedded.thetitans.world.dimension.WorldProviderVoid;
import com.electrifiedded.thetitans.world.gen.SpawnEggOreGeneration;
import com.electrifiedded.thetitans.world.gen.WorldGenMobs;
import com.electrifiedded.thetitans.world.gen.WorldGenStuffs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockFurnace;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import com.electrifiedded.archaiclib.original.OriginalModAssets;

import java.io.File;
import java.util.Iterator;
import java.util.Random;

@Mod(modid = TheTitans.MODID_V, name = TheTitans.MODNAME_V, version = TheTitans.VERSION, dependencies = "required-after:archaiclib@[1.20.0,);after:mutantbeasts", guiFactory = "com.electrifiedded.thetitans.config.TitanConfigGui")
public class TheTitans {
    public static final String MODNAME_V = "The Titans Port";
    public static final String MODID_V = "thetitans";
    public static final String VERSION = "1.0.0";
    public static final String CLIENT = "com.electrifiedded.thetitans.proxy.ClientProxy";
    public static final String SERVER = "com.electrifiedded.thetitans.proxy.CommonProxy";
    public static final String TEXTURES[] = {"default", "new"};
    public static final TitanBurnables fuelThe = new TitanBurnables();
    public static DebugExtender logger;
    public static ReflectionHelper reflect;
    @SidedProxy(clientSide=CLIENT, serverSide=SERVER)
    public static CommonProxy proxy;
    @Mod.Instance(MODID_V)
    public static TheTitans modInstance;

    /** The original jar this port takes its textures, sounds and texts from (it is never class-loaded). */
    public static final String ORIGINAL_NAME = "The Titans MC 1.7.10 version-0.5 beta v0712.jar (by Enderman_of_D00M)";
    public static final String ORIGINAL_URL = "https://www.curseforge.com/minecraft/mc-mods/the-titans-mod/download/3828537";

    public TheTitans() {
        // the original ships its Chinese translation as zh_CN1.lang, which 1.12 never asks for
        OriginalModAssets.alias(MODID_V, "lang/zh_cn.lang", "lang/zh_CN1.lang");
        // its 1.7 sounds.json names files without a domain (1.12 reads them as minecraft:); ours is the fixed copy
        OriginalModAssets.hide(MODID_V, "sounds.json");
        // textures the original has only in its "default" set, while the port always reads the "new" set
        for (String t : new String[] {"gargoyle2", "gargoyle3", "gargoyle4", "gargoyle5", "gargoyle6", "gargoyle7",
                "mobs/benderman", "titans/ender_colossus/icey_ender_colossus", "titans/ender_colossus/icey_ender_colossus_dead",
                "titans/ender_colossus/icey_ender_colossus_eyes", "titans/ender_colossus/icey_ender_colossus_eyes_dead"}) {
            OriginalModAssets.alias(MODID_V, "textures/entities/new/" + t + ".png", "textures/entities/default/" + t + ".png");
        }
    }
    @Mod.Metadata
    public static ModMetadata meta;
    public static final ResourceLocation giantWhiteTexture32x64Generic = new ResourceLocation(TheTitans.MODID_V, "textures/entities/32x64_disintigration.png");
    public static final ResourceLocation giantWhiteTexture64x64Generic = new ResourceLocation(TheTitans.MODID_V, "textures/entities/64x64_disintigration.png");
    public static final EnumRarity godlyV = EnumHelper.addRarity("GODLY", TextFormatting.DARK_PURPLE, "Godly");
    public static final EnumRarity epicSuper = EnumHelper.addRarity("Super Epic", TextFormatting.DARK_PURPLE, "Super Epic");
    public static final EnumRarity titan = EnumHelper.addRarity("Titan", TextFormatting.GOLD, "Titan");

    public static final CreativeTabs pageTitans = new CreativeTabs("thetitansItems") {
        public ItemStack createIcon() {
            return new ItemStack(TitanGear.serumGrowth);
        }
    };
    public static final CreativeTabs materialsPageGiant = new CreativeTabs("thetitansMaterials") {
        public ItemStack createIcon() { return new ItemStack(TitanGear.harcadium); }
    };

    public static final CreativeTabs weaponsPageGiant = new CreativeTabs("thetitansWeapons") {
        public ItemStack createIcon() { return new ItemStack(TitanGear.ultimaBlade); }
    };

    public static final CreativeTabs armorsPageGiant = new CreativeTabs("thetitansArmor") {
        public ItemStack createIcon() { return new ItemStack(TitanGear.chestplateHarcadium); }
    };

    public static final CreativeTabs toolsPageGiant = new CreativeTabs("thetitansTools") {
        public ItemStack createIcon() { return new ItemStack(TitanGear.pickaxeHarcadium); }
    };

    public static final CreativeTabs blocksPageGiant = new CreativeTabs("thetitansBlocks") {
        public ItemStack createIcon() { return new ItemStack(TitanBlockSet.harcadium_block); }
    };
    public static final CreativeTabs birthEggsPageGiant = new CreativeTabs("thetitansEggs") {
        public ItemStack createIcon() { return new ItemStack(TitanGear.witherzillaOvum); }
    };
    public static Configuration config;
    public static final EnumCreatureAttribute TITAN = EnumHelper.addCreatureAttribute("TITAN");
    public static StatBase defeatedStatTitans = (new StatBasic("stat.titansDefeated", new TextComponentTranslation("stat.titansDefeated", new Object[0]))).registerStat();
    public static StatBase titansStatDeaths = (new StatBasic("stat.deathsTitan", new TextComponentTranslation("stat.deathsTitans", new Object[0]))).registerStat();
    public static final BiomeGenVoid voidlandV = new BiomeGenVoid(169);
    public static final BiomeGenNowhere nowhereV = new BiomeGenNowhere(170);
    public static final int DIMENSION_KEY_VOID = 200;
    public static final int DIMENSION_KEY_NOWHERE = 201;
    // Registered DimensionTypes for the two custom dimensions; WorldProviderVoid/Nowhere.getDimensionType() return these.
    public static DimensionType DIM_KIND_VOID;
    public static DimensionType DIM_KIND_NOWHERE;
    // Enchantments — leaf ench classes set their own registry name in ctor; registered in RegistryEvent.Register<Enchantment> below.
    public static final Enchantment enchant1Turret = new EnchantmentDurability(249, new ResourceLocation("healing"), 10, EnumEnchantmentType.WEAPON);
    public static final Enchantment enchant2Turret = new EnchantmentFerocity(250, new ResourceLocation("damage"), 10, EnumEnchantmentType.WEAPON);
    public static final Enchantment enchant3Turret = new EnchantmentManiac(251, new ResourceLocation("shootingSpeed"), 5, EnumEnchantmentType.WEAPON);
    public static final Enchantment enchant4Turret = new EnchantmentUnstablility(252, new ResourceLocation("explosivePower"), 2, EnumEnchantmentType.WEAPON);
    public static final Enchantment enchant5Turret = new EnchantmentShurakin(253, new ResourceLocation("skullSpeed"), 1, EnumEnchantmentType.WEAPON);
    public static final Enchantment slayingGiant = new EnchantmentTitanSlayer(254, new ResourceLocation("titankiller"), 10);
    public static final Enchantment hammer = new EnchantmentKnockup(255, new ResourceLocation("hammer"), 5);
    public static int servantSpawnrateOmegafish;
    public static int giantServantSpawnrateSpider;
    public static int spiderGiantServantSpawnrateCave;
    public static int giantServantSpawnrateSlime;
    public static int cubeGiantServantSpawnrateMagma;
    public static int giantServantSpawnrateZombie;
    public static int zombieGiantServantSpawnratePig;
    public static int giantServantSpawnrateSkeleton;
    public static int giantServantSpawnrateCreeper;
    public static int giantServantSpawnrateBlaze;
    public static int skeletonGiantServantSpawnrateWither;
    public static int colossusServantSpawnrateEnder;
    public static int giantServantSpawnrateGhast;
    public static int servantSpawnrateWitherzilla;
    public static int ironGolemServantSpawnrateUltima;
    public static int kingServantSpawnrateGargoyle;
    public static int golemServantSpawnrateSnow;
    public static boolean ffaModeTitans;
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        OriginalModAssets.require(MODID_V, MODID_V, ORIGINAL_NAME, ORIGINAL_URL, MODID_V);
        logger = new DebugExtender(event.getModLog(), event.getSide() == Side.SERVER ? null : MODID_V);
        reflect = new ReflectionHelper(logger);
        config = new Configuration(new File(event.getModConfigurationDirectory(), "titans/general.cfg"));
        TitanConfig.load();
        info(TitanVars.preInit[Maths.random(9)]);
        debug("Pre Initialization started!");

        // vanilla attribute caps (maxHealth 1024 etc.) are lifted by mixin.MixinRangedAttribute.
        checkAttributeCap(SharedMonsterAttributes.MAX_HEALTH);
        checkAttributeCap(SharedMonsterAttributes.ATTACK_DAMAGE);
        checkAttributeCap(SharedMonsterAttributes.FOLLOW_RANGE);

        NetworkHandler.preInit();
        TheCore.preInit(event);

        if (event.getSide() == Side.CLIENT)
            TheClientCore.preInit(event);

        DIM_KIND_VOID = DimensionType.register("void_island", "_void", DIMENSION_KEY_VOID, WorldProviderVoid.class, false);
        DimensionManager.registerDimension(DIMENSION_KEY_VOID, DIM_KIND_VOID);
        DIM_KIND_NOWHERE = DimensionType.register("the_nowhere", "_nowhere", DIMENSION_KEY_NOWHERE, WorldProviderNowhere.class, false);
        DimensionManager.registerDimension(DIMENSION_KEY_NOWHERE, DIM_KIND_NOWHERE);
        proxy.preInit(event);

        debug("Finished pre-init for The Titans!");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        debug("Initialization started!");
        proxy.init(event);
        GameRegistry.registerFuelHandler(fuelThe);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new RealmHandler());
        MinecraftForge.EVENT_BUS.register(new com.electrifiedded.thetitans.handler.TitanVigor());
        MinecraftForge.EVENT_BUS.register(new com.electrifiedded.thetitans.handler.MobArmourTicker());
        MinecraftForge.EVENT_BUS.register(PortAttributionHandler.class);
        if (event.getSide() == Side.SERVER)
            MinecraftForge.EVENT_BUS.register(new PulseHandler(null));
        else
            MinecraftForge.EVENT_BUS.register(new PulseHandler(Minecraft.getMinecraft()));
        proxy.registerRenders();
        TitanTrophies.addAchievments();
        GameRegistry.registerWorldGenerator(new WorldGenStuffs(), 100);
        GameRegistry.registerWorldGenerator(new WorldGenMobs(), 10000);
        if (Loader.isModLoaded("OreSpawn"))
            GameRegistry.registerWorldGenerator(new SpawnEggOreGeneration(), 100);
        // BUGFIX — moved here from CommonProxy.preInit: recipes referencing blocks/items must be
        // added AFTER RegistryEvent.Register<Block/Item>, otherwise block ingredients resolve to empty stacks
        // (empty crafting slots) and addSmelting(Block,...) silently produced nothing.
        RecipeForge.initCrafting();
        {
            int ours = 0;
            for (net.minecraft.item.crafting.IRecipe r : net.minecraftforge.fml.common.registry.ForgeRegistries.RECIPES)
                if (r.getRegistryName() != null && MODID_V.equals(r.getRegistryName().getNamespace()))
                ours++;
            info("Registered " + ours + " crafting recipes and "
                    + net.minecraft.item.crafting.FurnaceRecipes.instance().getSmeltingList().size() + " smelting recipes.");
        }
        debug("Finished init for The Titans!");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        debug("Post Initialization started!");
        proxy.registerRenderThings();
        proxy.registerItemRenderers();
        proxy.postInit(event);
        debug("Finished post-init for The Titans!");
        info(TitanVars.postInit[Maths.random(9)]);
    }

    // ========================================================================
    // Registration backbone. Blocks/Items/Enchantments are NOT self-registering
    // into the Forge registry; leaf classes only set their registry name in ctor.
    // These central RegistryEvent handlers iterate the TitanBlockSet/TitanGear
    // lists (built by their setup()) and push everything in. Config-based enable
    // flags are NOT applied here — RegistryEvent fires before preInit/config load,
    // so every declared object is always registered (flags gate the GUI only).
    // ========================================================================
    @Mod.EventBusSubscriber(modid = MODID_V)
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            TitanBlockSet.setup();
            TitanBlockSet.registerAll(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            TitanGear.setup();
            TitanGear.registerAll(event.getRegistry());
            // ItemBlocks for every registered block.
            TitanBlockSet.registerItemBlocks(event.getRegistry());
        }

        @SubscribeEvent
        public static void remapBlocks(RegistryEvent.MissingMappings<Block> event) {
            for (RegistryEvent.MissingMappings.Mapping<Block> m : event.getMappings()) {
                if (!m.key.getPath().contains("magnesium")) continue;
                Block b = net.minecraftforge.fml.common.registry.ForgeRegistries.BLOCKS.getValue(renamedNickel(m.key));
                if (b != null && b != net.minecraft.init.Blocks.AIR)
                    m.remap(b);
            }
        }

        @SubscribeEvent
        public static void remapItems(RegistryEvent.MissingMappings<Item> event) {
            for (RegistryEvent.MissingMappings.Mapping<Item> m : event.getMappings()) {
                if (!m.key.getPath().contains("magnesium")) continue;
                Item i = net.minecraftforge.fml.common.registry.ForgeRegistries.ITEMS.getValue(renamedNickel(m.key));
                if (i != null && i != net.minecraft.init.Items.AIR)
                    m.remap(i);
            }
        }

        private static net.minecraft.util.ResourceLocation renamedNickel(net.minecraft.util.ResourceLocation key) {
            return new net.minecraft.util.ResourceLocation(key.getNamespace(), key.getPath().replace("magnesium", "nickel"));
        }

        @SubscribeEvent
        public static void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
            event.getRegistry().registerAll(enchant1Turret, enchant2Turret, enchant3Turret, enchant4Turret, enchant5Turret, slayingGiant, hammer);
        }

        @SubscribeEvent
        public static void registerBiomes(RegistryEvent.Register<Biome> event) {
            voidlandV.setRegistryName(new ResourceLocation(MODID_V, "void_island"));
            nowhereV.setRegistryName(new ResourceLocation(MODID_V, "the_nowhere"));
            event.getRegistry().registerAll(voidlandV, nowhereV);
        }
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        if (TitanConfig.coreModdingIs) {
            if (TitanConfig.coreCommandDifficultyUse)
                event.registerServerCommand(new CommandNewDifficulty());
            if (TitanConfig.coreCommandKillUse)
                event.registerServerCommand(new CommandNewKill());
        }

        event.registerServerCommand(new CommandTitans());
        event.registerServerCommand(new CommandEvent());
        event.registerServerCommand(new CommandClearMinions());
        event.registerServerCommand(new CommandDebug());
        event.registerServerCommand(new com.electrifiedded.thetitans.command.CommandTitanBench());
    }

    @Mod.EventHandler
    public void serverStop(FMLServerStoppedEvent event) {
        EventStore.saveInstance = null;
    }

    public static boolean equipped(EntityLivingBase entity, Item item, int slot) {
        if (entity == null || item == null)
            return false;
        int clamped = MathHelper.clamp(slot, 0, 4);
        ItemStack worn = getEquipment(entity, clamped == 0 ? 0 : 5 - clamped);
        return !worn.isEmpty() && sameKey(worn.getItem(), item);
    }

    public static boolean equippedAll(EntityLivingBase entity, Item... items) {
        return equippedAll(entity, false, items);
    }

    public static boolean equippedAll(EntityLivingBase entity, boolean countHeld, Item... items) {
        if (entity == null)
            return true;
        int limit = Math.min(items.length, 5);
        for (int i = countHeld ? 0 : 1; i < limit; i++) {
            int oldSlot = countHeld ? (i == 0 ? 0 : 5 - i) : 4 - i;
            ItemStack worn = getEquipment(entity, oldSlot);
            if (worn.isEmpty() || !sameKey(worn.getItem(), items[i]))
                return false;
        }
        return true;
    }

    private static boolean sameKey(Item a, Item b) {
        return a.getTranslationKey().equals(b.getTranslationKey());
    }

    private static final EntityEquipmentSlot[] LEGACY_SLOTS = {
            EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.FEET, EntityEquipmentSlot.LEGS,
            EntityEquipmentSlot.CHEST, EntityEquipmentSlot.HEAD
    };

    private static ItemStack getEquipment(EntityLivingBase entity, int oldSlot) {
        boolean known = oldSlot >= 0 && oldSlot < LEGACY_SLOTS.length;
        return entity.getItemStackFromSlot(known ? LEGACY_SLOTS[oldSlot] : EntityEquipmentSlot.MAINHAND);
    }

    public static String getTextures() {
        return getTextures(null, null);
    }

    public static String getTextures(String path) {
        return getTextures(null, path);
    }

    private static String activeSkinSet() {
        return TEXTURES[MathHelper.clamp(TitanConfig.textures, 0, TEXTURES.length - 1)];
    }

    public static String getTextures(String prefixPath, String path) {
        String set = activeSkinSet();
        if (path == null)
            return set;
        String prefix = prefixPath == null ? "" : prefixPath + "/";
        return MODID_V + ":" + prefix + set + "/" + path;
    }

    // PERF — render layers resolved their texture with string concat + new ResourceLocation every frame.
    // Cached per (prefix, path); cleared when the configured texture set changes.
    private static final java.util.Map<String, ResourceLocation> LOCATIONS_SKIN = new java.util.concurrent.ConcurrentHashMap<String, ResourceLocation>();
    private static int skinSetCached = Integer.MIN_VALUE;

    public static ResourceLocation getTextureLocation(String prefixPath, String path) {
        if (skinSetCached != TitanConfig.textures) {
            LOCATIONS_SKIN.clear();
            skinSetCached = TitanConfig.textures;
        }
        return LOCATIONS_SKIN.computeIfAbsent(prefixPath + "|" + path, k -> new ResourceLocation(getTextures(prefixPath, path)));
    }

    public static void reloadTextures() {
        info("Reloading resource locations...");
        for (Item item : Item.REGISTRY)
            refreshIfReloadable(item, "Item", item == null ? null : item.getTranslationKey());
        for (Block block : Block.REGISTRY)
            refreshIfReloadable(block, "Block", block == null ? null : block.getTranslationKey());
    }

    private static void refreshIfReloadable(Object entry, String kind, String key) {
        if (!(entry instanceof IReloadable))
            return;
        debug("Found reloadable " + kind + " " + key.substring(5) + ". Reloading texture...");
        ((IReloadable)entry).refreshTextures();
    }

    public static boolean isDifficulty(World world, EnumDifficulty difficulty) {
        return isDifficulty(world, difficulty, null);
    }

    public static boolean isDifficulty(World world, EnumDifficulty difficultyMin, EnumDifficulty difficultyMax) {
        int min = difficultyMin == null ? 0 : difficultyMin.getId();
        int max = difficultyMax == null ? TheCore.DIFFICULTIES_V.length - 1 : difficultyMax.getId();
        return isDifficulty(getDifficulty(world), min, max);
    }

    public static boolean isDifficulty(int difficultyCur, int difficultyMin, int difficultyMax) {
        return difficultyMin <= difficultyMax && difficultyCur >= difficultyMin && difficultyCur <= difficultyMax;
    }

    public static int getMaxDifficulty() {
        return TitanConfig.coreModdingIs && TitanConfig.coreDifficultyUse ? TheCore.DIFFICULTIES_V.length - 1 : 3;
    }

    public static float getBonusDamageVSGiantMobs(boolean isTitan) {
        return isTitan ? 1.0E11F : 100.0F;
    }

    public static boolean isApril1st(World world) {
        java.util.Calendar calendar = world.getCurrentDate();
        return calendar.get(2) + 1 == 4 && calendar.get(5) == 1;
    }

    public static boolean isHeraldLevel(EnumMinionType minion) {
        if (minion == null)
            return false;
        switch (minion) {
            case TEMPLAR:
            case ARBITRATOR:
            case GRANDMASTER:
            case LORD:
            case DEMITITAN:
                return true;
            default:
                return false;
        }
    }

    public static void conjureMinion(String name, net.minecraft.world.World world, double x, double y, double z, int type, int randomnumber, java.util.Random random) {
        if (type <= 0)
            type = 0;
        if (randomnumber > 1)
            randomnumber *= 1 + type * 2;
        if ((randomnumber == 0 || random.nextInt(randomnumber) == 0) && !world.isRemote && world.getDifficulty() != net.minecraft.world.EnumDifficulty.PEACEFUL) {
            net.minecraft.entity.EntityLiving living = com.electrifiedded.thetitans.item.spawn.ItemSpawnEggMinion.spawnMobCreature(name.equals("WitherSkeletonMinion") ? "SkeletonMinion" : name, world, x, y, z);
            if (living != null) {
                if (name.equals("WitherSkeletonMinion") && living instanceof com.electrifiedded.thetitans.entity.minion.EntitySkeletonMinion)
                    ((com.electrifiedded.thetitans.entity.minion.EntitySkeletonMinion)living).setSkeletonType(1);
                living.onInitialSpawn(world.getDifficultyForLocation(new net.minecraft.util.math.BlockPos(living)), (net.minecraft.entity.IEntityLivingData)null);
                if (living instanceof com.electrifiedded.thetitans.entity.minion.IMinion)
                    ((com.electrifiedded.thetitans.entity.minion.IMinion)living).setMinionType(type);
            }
        }
    }

    private static final int[] MINION_RANK_COLORS = {0, 10967219, 187098, 187138, 16776962, 16712194, 1176, 16777215, 3618615};

    public static int getMinionColors(int type) {
        return type >= 0 && type < MINION_RANK_COLORS.length ? MINION_RANK_COLORS[type] : 0;
    }

    public static int getDifficulty(World world) {
        int difficulty = 0;
        if (world.getDifficulty() != null)
            difficulty = world.getDifficulty().getId();
        return !(TitanConfig.coreModdingIs && TitanConfig.coreDifficultyUse) && TitanConfig.difficulty > difficulty ? TitanConfig.difficulty : difficulty;
    }

    private static final String[] PROGRESS_FLAGS = {
            "PostWither", "PostDragon", "TemplarKilled", "ArbitratorKilled", "GrandmasterKilled", "PostLord",
            "PostFirstTitan", "FirstLesserTitanDefeated", "FirstAverageTitanDefeated", "FirstGreaterTitanDefeated",
            "PostWitherzilla", "PostWitherzillaOmega", "PostExecutorDragon", "PostExecutorDragonOmega"
    };
    private static final double[] PROGRESS_BONUS = {0.25D, 0.25D, 0.5D, 0.75D, 1.0D, 2.0D, 5.0D, 5.0D, 5.0D, 10.0D, 20.0D, 40.0D, 20.0D, 40.0D};
    private static final double[] DIFFICULTY_FACTOR = {0.1D, 0.75D, 1.0D, 1.5D, 3.0D, 4.5D, 6.0D, 10.0D};

    public static double adjustValueToProgressionSystem(World world, double value) {
        if (!TitanConfig.progressionEnable)
            return value;
        double scale = 1.0D;
        for (int i = 0; i < PROGRESS_FLAGS.length; i++) {
            if (EventStore.getBool(world, PROGRESS_FLAGS[i]))
                scale += PROGRESS_BONUS[i];
        }
        int level = getDifficulty(world);
        scale *= level >= 0 && level < DIFFICULTY_FACTOR.length ? DIFFICULTY_FACTOR[level] : 1.0D;
        return scale * value;
    }

    public static EnumDifficulty getEnumDifficulty(World world) {
        int difficulty = 0;
        if (world.getDifficulty() != null)
            world.getDifficulty().getId();
        return !(TitanConfig.coreModdingIs && TitanConfig.coreDifficultyUse) && TitanConfig.difficulty > difficulty ? TheCore.DIFFICULTIES_V[TitanConfig.difficulty] : world.getDifficulty();
    }

    public static String translate(String category, String key, Object... variables) {
        String value = Utils.translate((category + "." + MODID_V + "." + key).toLowerCase(), variables);
        if (value.equalsIgnoreCase(category + "." + MODID_V + "." + key))
            logger.addError("Localization key returned null: " + value + "=");
        return value;
    }

    public static String translateMult(String category, String key, int amount, Object... variables) {
        String value = Utils.translate((category + "." + MODID_V + "." + key).toLowerCase(), amount, variables);
        if (value.contains(category + "." + MODID_V + "." + key))
            logger.addError("Localization key returned null: " + value + "=");
        return value;
    }

    public static boolean checkEntityBlacklist(Entity entity, boolean refinedCheck) {
        if (refinedCheck)
            return entity != null && (entity instanceof EntityItem || entity instanceof EntityEnderPearl || entity instanceof EntityFallingBlock || entity instanceof EntityMinecart || entity instanceof EntityItemFrame || entity instanceof EntityPainting || entity instanceof EntityXPOrb || entity instanceof EntityWeatherEffect);
        else
            return entity != null && !(entity instanceof EntityLivingBase);
    }

    /** Armour auras worn by a non-player (villager etc.): only its target and monsters (when it is not one) are foes. */
    public static boolean checkFriendlyFire(EntityLivingBase wearer, Entity entity, boolean refinedCheck) {
        if (wearer instanceof EntityPlayer)
            return checkFriendlyFire((EntityPlayer)wearer, entity, refinedCheck);
        if (entity == null || wearer == null || !(entity instanceof EntityLivingBase))
            return false;
        if (wearer.isOnSameTeam(entity))
            return true;
        if (wearer instanceof net.minecraft.entity.EntityLiving && ((net.minecraft.entity.EntityLiving)wearer).getAttackTarget() == entity)
            return false;
        return !(entity instanceof net.minecraft.entity.monster.IMob) || wearer instanceof net.minecraft.entity.monster.IMob;
    }

    public static boolean checkFriendlyFire(EntityPlayer player, Entity entity, boolean refinedCheck) {
        if (refinedCheck)
            return entity != null && player != null && (entity instanceof EntityLivingBase && (player.isOnSameTeam((EntityLivingBase)entity) || (entity instanceof EntityTameable && (((EntityTameable)entity).getOwner() != null && (((EntityTameable)entity).getOwner().isEntityEqual(player) || player.isOnSameTeam(((EntityTameable)entity).getOwner())) || ((EntityTameable)entity).isTamed())) || (entity instanceof EntityIronGolem && ((EntityIronGolem)entity).isPlayerCreated()) || (entity instanceof EntityWitherTurret && ((EntityWitherTurret)entity).isPlayerCreated()) || (entity instanceof EntityWitherTurretGround && ((EntityWitherTurretGround)entity).isPlayerCreated()) || (entity instanceof EntityWitherTurretMortar && ((EntityWitherTurretMortar)entity).isPlayerCreated()) || (entity instanceof EntityGargoyle && ((EntityGargoyle)entity).isPlayerCreated()) || entity instanceof EntitySnowman || entity instanceof EntityVillager || (entity instanceof EntityHorse && ((EntityHorse)entity).isTame())));
        else
            return entity != null && player != null && (entity instanceof EntityLivingBase && (player.isOnSameTeam((EntityLivingBase)entity) || entity instanceof EntityTameable || entity instanceof EntityGolem || entity instanceof EntityVillager || entity instanceof EntityHorse || entity instanceof EntityAnimal));
    }

    public static void info (String message) {
        logger.info(message);
    }

    public static void debug (String message) {
        logger.debug(message);
    }

    // BUGFIX — sword-swing block-breakers spawned ItemStack(Item.getItemFromBlock(block), oldMeta),
    // which for meta-sensitive blocks (leaves variants etc.) produced missing-texture "air" items. Use the
    // creative-pick stack (correct item + variant), fall back to a default-meta ItemBlock.
    public static net.minecraft.item.ItemStack createBlockDrop(net.minecraft.world.World world, net.minecraft.block.Block block, net.minecraft.block.state.IBlockState state, net.minecraft.util.math.BlockPos pos, net.minecraft.entity.EntityLivingBase breaker) {
        net.minecraft.entity.player.EntityPlayer player = breaker instanceof net.minecraft.entity.player.EntityPlayer ? (net.minecraft.entity.player.EntityPlayer)breaker : null;
        try {
            net.minecraft.item.ItemStack pick = block.getPickBlock(state, null, world, pos, player);
            if (!pick.isEmpty())
                return pick;
        } catch (Exception ignored) {}
        return new net.minecraft.item.ItemStack(net.minecraft.item.Item.getItemFromBlock(block));
    }

    private static void checkAttributeCap(net.minecraft.entity.ai.attributes.IAttribute attr) {
        if (attr.clampValue(Double.MAX_VALUE) != Double.MAX_VALUE)
            error("Vanilla attribute cap for " + attr.getName() + " is still active - MixinRangedAttribute did not apply. Titan HP will be clamped!", null);
    }

    public static void warn (String message) {
        logger.warn(message);
    }

    public static void error (String message, Throwable e) {
        logger.error(message, e);
    }

    public static void fatal (String message, Throwable e) {
        logger.fatal(message, e);
    }


    /** Map a 1.7.10 ChestGenHooks category string to a 1.12.2 loot table. */
    private static ResourceLocation lootTableFor(String chestgen) {
        if ("villageBlacksmith".equals(chestgen)) return LootTableList.CHESTS_VILLAGE_BLACKSMITH;
        if ("strongholdLibrary".equals(chestgen)) return LootTableList.CHESTS_STRONGHOLD_LIBRARY;
        if ("bonusChest".equals(chestgen)) return LootTableList.CHESTS_SPAWN_BONUS_CHEST;
        if ("pyramidDesertyChest".equals(chestgen)) return LootTableList.CHESTS_DESERT_PYRAMID;
        // "dungeonChest" and anything else
        return LootTableList.CHESTS_SIMPLE_DUNGEON;
    }

    public static void generateChest(World world, String chestgen, BlockPos pos, EnumFacing facing) {
        world.setBlockState(pos, Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, facing), 2);
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityChest)
            ((TileEntityChest) tile).setLootTable(lootTableFor(chestgen), world.rand.nextLong());
    }

    public static void generateMobCage(World world, String mob, BlockPos pos) {
        world.setBlockState(pos, Blocks.MOB_SPAWNER.getDefaultState(), 2);
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityMobSpawner)
            ((TileEntityMobSpawner) tile).getSpawnerBaseLogic().setEntityId(new ResourceLocation(mob));
    }

    public static void generateFurnace(World world, BlockPos pos, EnumFacing facing) {
        Random rand = new Random();
        world.setBlockState(pos, Blocks.FURNACE.getDefaultState().withProperty(BlockFurnace.FACING, facing), 2);
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityFurnace) {
            TileEntityFurnace furnace = (TileEntityFurnace) tile;
            if (rand.nextInt(6) == 0) {
                if (rand.nextInt(2) == 0)
                    furnace.setInventorySlotContents(1, new ItemStack(Item.getItemFromBlock(TitanBlockSet.molten_fuel_block), 2 + rand.nextInt(3)));
                else
                    furnace.setInventorySlotContents(1, new ItemStack(Item.getItemFromBlock(Blocks.COAL_BLOCK), 4 + rand.nextInt(5)));
            } else if (rand.nextInt(2) == 0)
                furnace.setInventorySlotContents(1, new ItemStack(TitanGear.moltenFuel, 6 + rand.nextInt(7)));
            else
                furnace.setInventorySlotContents(1, new ItemStack(Items.COAL, 8 + rand.nextInt(9)));
        }
    }

    public static void generateDoor(World world, Block door, BlockPos pos, EnumFacing facing) {
        if (door instanceof net.minecraft.block.BlockDoor)
            ItemDoor.placeDoor(world, pos, facing, (net.minecraft.block.BlockDoor) door, false);
    }
}
