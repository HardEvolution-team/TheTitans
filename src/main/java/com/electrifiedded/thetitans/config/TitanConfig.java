package com.electrifiedded.thetitans.config;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.RenderTheTitans;
import com.electrifiedded.thetitans.registry.TitanBlockSet;
import com.electrifiedded.thetitans.registry.TitanGear;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class TitanConfig {
    private static int texturesFormer;
    //Core Configurations
    public static int debug;
    public static int quality;
    public static int textures;
    public static int texturesBar;
    public static boolean antiCheatIs;
    public static boolean coreModdingIs;
    //Configurations
    public static int difficulty;
    public static boolean coreDifficultyUse;
    public static boolean coreGameSettingsUse;
    public static boolean coreCommandDifficultyUse;
    public static boolean coreCommandKillUse;
    public static boolean giantBarUse;
    public static boolean giantBarPermUse;
    public static int spawnProtection;
    public static int separationBirth;
    public static int limitServant;
    public static boolean progressionEnable;
    //Titan awakening
    public static String[] mobsAwakening = {"minecraft:ender_dragon", "minecraft:wither"};
    public static boolean requiresAllAwakening;
    public static int wakeSpanGiant;
    //Player vitality
    public static boolean armourReviveGiant;
    public static double hpPercentRevive;
    public static int rechargeSecondsRevive;
    public static int graceBeatsRevive;
    public static double regenPercentUser;
    public static int regenCombatWaitUser;
    public static boolean onTierUpHeal;
    public static boolean hpOnLoginRestore;
    public static boolean mobsUnited;

    public static void load() {
        TheTitans.config.load();
        //Core Configurations
        debug = TheTitans.config.get("general", "Debug", 0, TheTitans.translate("config", "integer.debug"), 0, 2).getInt(); TheTitans.logger.debugShow = debug;
        quality = TheTitans.config.get("general", "Quality", 0, TheTitans.translate("config", "integer.quality"), 0, 10).getInt();
        textures = TheTitans.config.get("general", "Texture Set", 1, TheTitans.translate("config", "integer.textures"), 0, TheTitans.TEXTURES.length - 1).getInt();
        // force "new" — static block/item models hardcode new/, so set=default only desyncs entities/eggs/armor
        // into the incomplete legacy look. Original 1.7.10 shipped new as the intended set. Clamp so stale on-disk
        // configs (=0) can't resurrect old textures. Drop this line to re-enable the switcher if new/ ever regains parity.
        if (textures == 0) textures = 1;
        if (textures != texturesFormer) {
            TheTitans.reloadTextures(); texturesFormer = textures;
        }

        antiCheatIs = TheTitans.config.get("general", "Enable Anti Cheat", true, TheTitans.translate("config", "boolean.anticheat")).getBoolean();
        mobsUnited = TheTitans.config.get("general", "Enable The United Realms of Minecraftia", false, TheTitans.translate("config", "boolean.urm")).getBoolean();
        progressionEnable = TheTitans.config.get("general", "Enable Progression Multiplier", true, TheTitans.translate("config", "boolean.progressionmulti")).getBoolean();
        coreModdingIs = TheTitans.config.get("general", "Enable Core Modding", true, TheTitans.translate("config", "boolean.coremodding")).setRequiresMcRestart(true).getBoolean();
        //Categories
        TheTitans.config.getCategory("client").setComment(TheTitans.translate("config", "title.client"));
        TheTitans.config.getCategory("core").setShowInGui(coreModdingIs).setComment(TheTitans.translate("config", "title.core"));
        TheTitans.config.getCategory("blocks").setComment(TheTitans.translate("config", "title.blocks"));
        TheTitans.config.getCategory("items").setComment(TheTitans.translate("config", "title.items"));
        TheTitans.config.getCategory("mobs").setComment(TheTitans.translate("config", "title.mobs"));
        TheTitans.config.getCategory("misc").setShowInGui(antiCheatIs).setComment(TheTitans.translate("config", "title.misc"));
        //Configurations
        coreDifficultyUse = TheTitans.config.get("core", "Override Difficulty", false, TheTitans.translate("config", "boolean.core.difficulty")).setShowInGui(coreModdingIs).setRequiresMcRestart(true).getBoolean();
        coreGameSettingsUse = TheTitans.config.get("core", "Override Options Menu", false, TheTitans.translate("config", "boolean.core.gamesettings")).setShowInGui(coreModdingIs).setRequiresMcRestart(true).getBoolean();
        coreCommandDifficultyUse = TheTitans.config.get("core", "Override command /difficulty", true, TheTitans.translate("config", "boolean.core.commanddifficulty")).setRequiresWorldRestart(true).setShowInGui(coreModdingIs).getBoolean();
        coreCommandKillUse = TheTitans.config.get("core", "Override command /kill", true, TheTitans.translate("config", "boolean.core.commandkill")).setShowInGui(coreModdingIs).setRequiresWorldRestart(true).getBoolean();
        giantBarUse = TheTitans.config.get("client", "Enable Custom Health Bar", true, TheTitans.translate("config", "boolean.client.healthbar")).getBoolean();
        giantBarPermUse = TheTitans.config.get("client", "Permanent Custom Health Bar", false, TheTitans.translate("config", "boolean.client.permhealthbar")).setShowInGui(giantBarUse).getBoolean();
        texturesBar = TheTitans.config.get("client", "Titan Gui Textures", 0, TheTitans.translate("config", "int.client.healthbar"), 0, 2).setShowInGui(giantBarUse).getInt();
        spawnProtection = TheTitans.config.get("mobs", "Titan Spawn Range", 1500, TheTitans.translate("config", "integer.mob.spawnprotection")).getInt();
        separationBirth = TheTitans.config.get("mobs", "Titan Spawn Separation", 600, TheTitans.translate("config", "integer.mob.spawnseparation")).getInt();
        limitServant = TheTitans.config.get("mobs", "Minion Limit", -1, TheTitans.translate("config", "integer.mob.minionlimit")).setShowInGui(!antiCheatIs).getInt();
        difficulty = TheTitans.config.get("general", "Difficulty", 0, TheTitans.translate("config", "integer.difficulty"), 0, 7).setShowInGui(!(coreModdingIs && coreDifficultyUse)).getInt();
        for (Map.Entry<Block, Boolean> entry : TitanBlockSet.registryCube.keySet())
            TitanBlockSet.registryCube.set(entry.getKey(), TheTitans.config.get("blocks", "Enable Block " + entry.getKey().getLocalizedName(), true, TheTitans.translate("config", "boolean.blocks.enable", entry.getKey().getLocalizedName())).setRequiresMcRestart(true).getBoolean());
        for (Map.Entry<Item, Boolean> entry : TitanGear.registryStack.keySet())
            TitanGear.registryStack.set(entry.getKey(), TheTitans.config.get("items", "Enable Item " + entry.getKey().getItemStackDisplayName(new ItemStack(entry.getKey())), true, TheTitans.translate("config", "boolean.items.enable", entry.getKey().getItemStackDisplayName(new ItemStack(entry.getKey())))).setRequiresMcRestart(true).getBoolean());
        int id = 0;
        while (id < RenderTheTitans.entityRegistry.size()) {
            for (Map.Entry<Object[], Object[]> entry : RenderTheTitans.entityRegistry.entrySet())
                if (id == (int)entry.getKey()[0]) {
                String name = ((Class<?>)entry.getKey()[1]).getSimpleName().toLowerCase().substring(6);
                TheTitans.config.getCategory("mobs." + name).setShowInGui(antiCheatIs).setComment(TheTitans.translate("config", "enable.mobs", name));
                Object values[] = new Object[3];
                values[0] = TheTitans.config.get("mobs." + name, "Max Health", 1.0D, TheTitans.translate("config", "double.mob.maxhealth", name)).getDouble();
                values[1] = TheTitans.config.get("mobs." + name, "Damage", 1.0D, TheTitans.translate("config", "double.mob.damage", name)).getDouble();
                values[2] = TheTitans.config.get("mobs." + name, "Armor", 1.0D, TheTitans.translate("config", "double.mob.armor", name)).getDouble();
                RenderTheTitans.entityRegistry.set((int)entry.getKey()[0], values);
                id++;
                break;
            }
        }

        //Compatability Variables. Cannot change locations of these in this update.
        TheTitans.ffaModeTitans = TheTitans.config.get("general", "Enable Free For All Mode", false, TheTitans.translate("config", "boolean.ffa")).getBoolean();
        TheTitans.golemServantSpawnrateSnow = TheTitans.config.get("mobs", "Snow Golem Spawnrate", 100, TheTitans.translate("config", "integer.mob.spawnrate.snowgolem")).setShowInGui(!antiCheatIs).getInt();
        TheTitans.giantServantSpawnrateSlime = TheTitans.config.get("mobs", "Slime Minion Spawnrate", 30, TheTitans.translate("config", "integer.mob.spawnrate.slime")).setShowInGui(!antiCheatIs).getInt();
        TheTitans.cubeGiantServantSpawnrateMagma = TheTitans.config.get("mobs", "Magma Cube Minion Spawnrate", 30, TheTitans.translate("config", "integer.mob.spawnrate.magmacube")).setShowInGui(!antiCheatIs).getInt();
        TheTitans.servantSpawnrateOmegafish = TheTitans.config.get("mobs", "Silverfish Minion Spawnrate", 40, TheTitans.translate("config", "integer.mob.spawnrate.silverfish")).setShowInGui(!antiCheatIs).getInt();
        TheTitans.spiderGiantServantSpawnrateCave = TheTitans.config.get("mobs", "Cave Spider Minion Spawnrate", 60, TheTitans.translate("config", "integer.mob.spawnrate.cavespider")).setShowInGui(!antiCheatIs).getInt();
        TheTitans.giantServantSpawnrateSpider = TheTitans.config.get("mobs", "Spider Minion Spawnrate", 50, TheTitans.translate("config", "integer.mob.spawnrate.spider")).setShowInGui(!antiCheatIs).getInt();
        TheTitans.giantServantSpawnrateZombie = TheTitans.config.get("mobs", "Zombie Minion Spawnrate", 20, TheTitans.translate("config", "integer.mob.spawnrate.zombie")).setShowInGui(!antiCheatIs).getInt();
        TheTitans.giantServantSpawnrateSkeleton = TheTitans.config.get("mobs", "Skeleton Minion Spawnrate", 60, TheTitans.translate("config", "integer.mob.spawnrate.skeleton")).setShowInGui(!antiCheatIs).getInt();
        TheTitans.giantServantSpawnrateCreeper = TheTitans.config.get("mobs", "Creeper Minion Spawnrate", 60, TheTitans.translate("config", "integer.mob.spawnrate.creeper")).setShowInGui(!antiCheatIs).getInt();
        TheTitans.zombieGiantServantSpawnratePig = TheTitans.config.get("mobs", "Pig Zombie Minion Spawnrate", 60, TheTitans.translate("config", "integer.mob.spawnrate.pigzombie")).setShowInGui(!antiCheatIs).getInt();
        TheTitans.giantServantSpawnrateBlaze = TheTitans.config.get("mobs", "Blaze Minion Spawnrate", 40, TheTitans.translate("config", "integer.mob.spawnrate.blaze")).setShowInGui(!antiCheatIs).getInt();
        TheTitans.kingServantSpawnrateGargoyle = TheTitans.config.get("mobs", "Gargoyle Minion Spawnrate", 100, TheTitans.translate("config", "integer.mob.spawnrate.gargoyle")).setShowInGui(!antiCheatIs).getInt();
        TheTitans.skeletonGiantServantSpawnrateWither = TheTitans.config.get("mobs", "Wither Skeleton Minion Spawnrate", 40, TheTitans.translate("config", "integer.mob.spawnrate.witherskeleton")).setShowInGui(!antiCheatIs).getInt();
        TheTitans.giantServantSpawnrateGhast = TheTitans.config.get("mobs", "Ghast Minion Spawnrate", 60, TheTitans.translate("config", "integer.mob.spawnrate.ghast")).setShowInGui(!antiCheatIs).getInt();
        TheTitans.ironGolemServantSpawnrateUltima = TheTitans.config.get("mobs", "Iron Golem Minion Spawnrate", 100, TheTitans.translate("config", "integer.mob.spawnrate.irongolem")).setShowInGui(!antiCheatIs).getInt();
        TheTitans.colossusServantSpawnrateEnder = TheTitans.config.get("mobs", "Enderman Minion Spawnrate", 40, TheTitans.translate("config", "integer.mob.spawnrate.enderman")).setShowInGui(!antiCheatIs).getInt();
        TheTitans.servantSpawnrateWitherzilla = TheTitans.config.get("mobs", "Wither Minion Spawnrate", 20, TheTitans.translate("config", "integer.mob.spawnrate.wither")).setShowInGui(!antiCheatIs).getInt();

        TheTitans.config.getCategory("progression").setComment("When dormant titans wake up.");
        mobsAwakening = TheTitans.config.get("progression", "Awakening Mobs", new String[] {"minecraft:ender_dragon", "minecraft:wither"}, "Registry ids of the mobs that must be killed before dormant titans wake up (e.g. minecraft:ender_dragon, minecraft:wither, minecraft:elder_guardian, or any modded id). Empty list = titans are awake from the start.").getStringList();
        requiresAllAwakening = TheTitans.config.get("progression", "Awakening Requires All", true, "true = every mob in the list must be killed; false = killing any one of them is enough.").getBoolean();
        wakeSpanGiant = TheTitans.config.get("progression", "Titan Wake Radius", 0, "0 = once unlocked, every loaded dormant titan wakes immediately. >0 = a dormant titan only wakes when a player comes within this many blocks.", 0, 4096).getInt();

        TheTitans.config.getCategory("player").setComment("Player health: titan armor revive, regeneration and safety nets.");
        armourReviveGiant = TheTitans.config.get("player", "Titan Armor Revive", true, "Wearing at least one titan armor piece survives a lethal hit once per cooldown (the player is healed instead of dying). When it is on cooldown the player dies normally.").getBoolean();
        hpPercentRevive = TheTitans.config.get("player", "Revive Health Percent", 25.0D, "Percent of max health restored by a titan armor revive.", 1.0D, 100.0D).getDouble();
        rechargeSecondsRevive = TheTitans.config.get("player", "Revive Cooldown Seconds", 60, "Seconds before titan armor can revive the player again.", 0, 86400).getInt();
        graceBeatsRevive = TheTitans.config.get("player", "Revive Grace Ticks", 40, "Ticks of damage immunity after a revive, so the same titan combo can't kill again instantly.", 0, 200).getInt();
        regenPercentUser = TheTitans.config.get("player", "Regen Percent Per Second", 0.5D, "Extra natural regeneration in percent of max health per second (needs naturalRegeneration gamerule and 18+ food). Scales with huge titan-armor/level health pools where vanilla regen is useless. 0 = off.", 0.0D, 100.0D).getDouble();
        regenCombatWaitUser = TheTitans.config.get("player", "Regen Combat Delay Seconds", 5, "Extra regeneration pauses this many seconds after the player takes damage.", 0, 600).getInt();
        onTierUpHeal = TheTitans.config.get("player", "Heal On Level Up", true, "When the XP-level health bonus grows, the new health is filled instead of appearing as empty hearts.").getBoolean();
        hpOnLoginRestore = TheTitans.config.get("player", "Restore Health On Login", true, "Vanilla clamps saved health to max health before armor bonuses apply, so titan-armor players lose most of their HP on relog. This restores it.").getBoolean();
        TheTitans.config.save();
    }
}
