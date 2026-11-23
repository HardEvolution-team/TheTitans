package com.electrifiedded.thetitans.proxy;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.TitanBossBarGui;
import com.electrifiedded.thetitans.client.model.ModelSlimeTitan;
import com.electrifiedded.thetitans.client.render.entity.RenderBlazeTitan;
import com.electrifiedded.thetitans.client.render.entity.RenderCaveSpiderTitan;
import com.electrifiedded.thetitans.client.render.entity.RenderChaff;
import com.electrifiedded.thetitans.client.render.entity.RenderCreeperTitan;
import com.electrifiedded.thetitans.client.render.entity.RenderDragonMinion;
import com.electrifiedded.thetitans.client.render.entity.RenderEnderColossus;
import com.electrifiedded.thetitans.client.render.entity.RenderEnderColossusCrystal;
import com.electrifiedded.thetitans.client.render.entity.RenderExecutorDragon;
import com.electrifiedded.thetitans.client.render.entity.RenderGammaLightning;
import com.electrifiedded.thetitans.client.render.entity.RenderGargoyle;
import com.electrifiedded.thetitans.client.render.entity.RenderGargoyleTitan;
import com.electrifiedded.thetitans.client.render.entity.RenderGargoyleTitanFireball;
import com.electrifiedded.thetitans.client.render.entity.RenderGhastGuard;
import com.electrifiedded.thetitans.client.render.entity.RenderGhastTitan;
import com.electrifiedded.thetitans.client.render.entity.RenderGiantArrow;
import com.electrifiedded.thetitans.client.render.entity.RenderHarcadiumArrow;
import com.electrifiedded.thetitans.client.render.entity.RenderHarcadiumGolem;
import com.electrifiedded.thetitans.client.render.entity.RenderInvisibleEntity;
import com.electrifiedded.thetitans.client.render.entity.RenderItemBomb;
import com.electrifiedded.thetitans.client.render.entity.RenderLavaSpit;
import com.electrifiedded.thetitans.client.render.entity.RenderLightningBall;
import com.electrifiedded.thetitans.client.render.entity.RenderMagmaCubeTitan;
import com.electrifiedded.thetitans.client.render.entity.RenderOmegafish;
import com.electrifiedded.thetitans.client.render.entity.RenderProtoBall;
import com.electrifiedded.thetitans.client.render.entity.RenderSkeletonTitan;
import com.electrifiedded.thetitans.client.render.entity.RenderSlimeTitan;
import com.electrifiedded.thetitans.client.render.entity.RenderSnowGolemTitan;
import com.electrifiedded.thetitans.client.render.entity.RenderSpiderTitan;
import com.electrifiedded.thetitans.client.render.entity.RenderTitanFireball;
import com.electrifiedded.thetitans.client.render.entity.RenderTitanPart;
import com.electrifiedded.thetitans.client.render.entity.RenderUltimaIronGolemTitan;
import com.electrifiedded.thetitans.client.render.entity.RenderVoidGolem;
import com.electrifiedded.thetitans.client.render.entity.RenderWebShot;
import com.electrifiedded.thetitans.client.render.entity.RenderWitherMinion;
import com.electrifiedded.thetitans.client.render.entity.RenderWitherTurret;
import com.electrifiedded.thetitans.client.render.entity.RenderWitherTurretGround;
import com.electrifiedded.thetitans.client.render.entity.RenderWitherTurretMortar;
import com.electrifiedded.thetitans.client.render.entity.RenderWitherzilla;
import com.electrifiedded.thetitans.client.render.entity.RenderWitherzillaMinion;
import com.electrifiedded.thetitans.client.render.entity.RenderWraith;
import com.electrifiedded.thetitans.client.render.entity.RenderXPBomb;
import com.electrifiedded.thetitans.client.render.entity.RenderZombiePigmanTitan;
import com.electrifiedded.thetitans.client.render.entity.RenderZombieTitan;
import com.electrifiedded.thetitans.client.render.item.RenderOptimaAxe;
import com.electrifiedded.thetitans.client.render.item.RenderUltimaBlade;
import com.electrifiedded.thetitans.client.render.minion.RenderBlazeMinion;
import com.electrifiedded.thetitans.client.render.minion.RenderCaveSpiderMinion;
import com.electrifiedded.thetitans.client.render.minion.RenderCreeperMinion;
import com.electrifiedded.thetitans.client.render.minion.RenderEndermanMinion;
import com.electrifiedded.thetitans.client.render.minion.RenderGhastMinion;
import com.electrifiedded.thetitans.client.render.minion.RenderPigZombieMinion;
import com.electrifiedded.thetitans.client.render.minion.RenderSilverfishMinion;
import com.electrifiedded.thetitans.client.render.minion.RenderSkeletonMinion;
import com.electrifiedded.thetitans.client.render.minion.RenderSpiderMinion;
import com.electrifiedded.thetitans.client.render.minion.RenderZombieMinion;
import com.electrifiedded.thetitans.client.render.misc.RenderEnderSquid;
import com.electrifiedded.thetitans.client.render.misc.RenderEvilGolem;
import com.electrifiedded.thetitans.client.render.misc.RenderOGZombieTitan;
import com.electrifiedded.thetitans.client.render.misc.RenderSilverfishOther;
import com.electrifiedded.thetitans.client.render.misc.RenderSpellcaster;
import com.electrifiedded.thetitans.client.render.misc.RenderThinUndeadOther;
import com.electrifiedded.thetitans.client.render.misc.RenderUndeadOther;
import com.electrifiedded.thetitans.entity.minion.EntityBlazeMinion;
import com.electrifiedded.thetitans.entity.minion.EntityCaveSpiderMinion;
import com.electrifiedded.thetitans.entity.minion.EntityCreeperMinion;
import com.electrifiedded.thetitans.entity.minion.EntityDragonMinion;
import com.electrifiedded.thetitans.entity.minion.EntityEndermanMinion;
import com.electrifiedded.thetitans.entity.minion.EntityGhastGuard;
import com.electrifiedded.thetitans.entity.minion.EntityGhastMinion;
import com.electrifiedded.thetitans.entity.minion.EntityGiantZombieBetter;
import com.electrifiedded.thetitans.entity.minion.EntityPigZombieMinion;
import com.electrifiedded.thetitans.entity.minion.EntitySilverfishMinion;
import com.electrifiedded.thetitans.entity.minion.EntitySkeletonMinion;
import com.electrifiedded.thetitans.entity.minion.EntitySpiderMinion;
import com.electrifiedded.thetitans.entity.minion.EntityWitherMinion;
import com.electrifiedded.thetitans.entity.minion.EntityZombieMinion;
import com.electrifiedded.thetitans.entity.misc.EntityEnderSquid;
import com.electrifiedded.thetitans.entity.mob.EntityEvilGolem;
import com.electrifiedded.thetitans.entity.mob.EntityHarcadiumGolem;
import com.electrifiedded.thetitans.entity.mob.EntityMagicUser;
import com.electrifiedded.thetitans.entity.mob.EntitySilverfishOther;
import com.electrifiedded.thetitans.entity.mob.EntityThinUndeadOther;
import com.electrifiedded.thetitans.entity.mob.EntityUndeadOther;
import com.electrifiedded.thetitans.entity.mob.EntityVoidGolem;
import com.electrifiedded.thetitans.entity.mob.EntityWraith;
import com.electrifiedded.thetitans.entity.titan.EntityBlazeTitan;
import com.electrifiedded.thetitans.entity.titan.EntityCaveSpiderTitan;
import com.electrifiedded.thetitans.entity.titan.EntityChaff;
import com.electrifiedded.thetitans.entity.titan.EntityCreeperTitan;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossus;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossusCrystal;
import com.electrifiedded.thetitans.entity.titan.EntityExecutorDragon;
import com.electrifiedded.thetitans.entity.titan.EntityGammaLightning;
import com.electrifiedded.thetitans.entity.titan.EntityGargoyle;
import com.electrifiedded.thetitans.entity.titan.EntityGargoyleTitan;
import com.electrifiedded.thetitans.entity.titan.EntityGargoyleTitanFireball;
import com.electrifiedded.thetitans.entity.titan.EntityGhastTitan;
import com.electrifiedded.thetitans.entity.titan.EntityGrowthSerum;
import com.electrifiedded.thetitans.entity.titan.EntityHarcadiumArrow;
import com.electrifiedded.thetitans.entity.titan.EntityIronGolemTitan;
import com.electrifiedded.thetitans.entity.titan.EntityItemBomb;
import com.electrifiedded.thetitans.entity.titan.EntityLavaSpit;
import com.electrifiedded.thetitans.entity.titan.EntityLightningBall;
import com.electrifiedded.thetitans.entity.titan.EntityMagmaCubeTitan;
import com.electrifiedded.thetitans.entity.titan.EntityPigZombieTitan;
import com.electrifiedded.thetitans.entity.titan.EntityProtoBall;
import com.electrifiedded.thetitans.entity.titan.EntitySilverfishTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitanGiantArrow;
import com.electrifiedded.thetitans.entity.titan.EntitySlimeTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySnowGolemTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySpiderTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitanFireball;
import com.electrifiedded.thetitans.entity.titan.EntityTitanPart;
import com.electrifiedded.thetitans.entity.titan.EntityTitanSpirit;
import com.electrifiedded.thetitans.entity.titan.EntityWebShot;
import com.electrifiedded.thetitans.entity.titan.EntityWitherTurret;
import com.electrifiedded.thetitans.entity.titan.EntityWitherTurretGround;
import com.electrifiedded.thetitans.entity.titan.EntityWitherTurretMortar;
import com.electrifiedded.thetitans.entity.titan.EntityWitherzilla;
import com.electrifiedded.thetitans.entity.titan.EntityWitherzillaMinion;
import com.electrifiedded.thetitans.entity.titan.EntityXPBomb;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;
import com.electrifiedded.thetitans.entity.titan.classic.EntityOgPigZombieTitan;
import com.electrifiedded.thetitans.entity.titan.classic.EntityOgZombieTitan;
import com.electrifiedded.thetitans.item.spawn.ItemSpawnEggMinion;
import com.electrifiedded.thetitans.item.spawn.ItemSpawnEggRegular;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanVisuals;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    public static final ResourceLocation titanpotions = new ResourceLocation(TheTitans.MODID_V, "/textures/entities/effects/titanpotions.png".substring(1));
    public static TitanBossBarGui guiIngameZ;

    public void preInit(FMLPreInitializationEvent e) {
        TitanVisuals.init();
        com.electrifiedded.thetitans.util.perf.HitchWatch.install();
        com.electrifiedded.thetitans.util.perf.TitanParticles.counter = () -> {
            try {
                return Integer.parseInt(Minecraft.getMinecraft().effectRenderer.getStatistics());
            } catch (RuntimeException ex) {
                return 0;
            }
        };
        guiIngameZ = new TitanBossBarGui(Minecraft.getMinecraft());
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(guiIngameZ);
        super.preInit(e);
    }

    public void init(FMLInitializationEvent e) {
        super.init(e);
    }

    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
        enlistColorsStack();
        com.electrifiedded.thetitans.client.TintedGearColors.register();
    }

    private void enlistColorsStack() {
        net.minecraft.client.renderer.color.ItemColors colors = Minecraft.getMinecraft().getItemColors();
        if (TitanGear.titansModBirthOvumRegular instanceof com.electrifiedded.thetitans.item.spawn.ItemSpawnEggRegular) {
            final com.electrifiedded.thetitans.item.spawn.ItemSpawnEggRegular egg = (com.electrifiedded.thetitans.item.spawn.ItemSpawnEggRegular)TitanGear.titansModBirthOvumRegular;
            colors.registerItemColorHandler((stack, tintIndex) -> egg.getColor(stack, tintIndex), egg);
        }
        // same base+spots tint for the minion eggs (they reuse the vanilla spawn_egg + overlay layers).
        for (net.minecraft.item.Item item : TitanGear.eggsServant)
            if (item instanceof com.electrifiedded.thetitans.item.spawn.ItemSpawnEggMinion) {
            final com.electrifiedded.thetitans.item.spawn.ItemSpawnEggMinion egg = (com.electrifiedded.thetitans.item.spawn.ItemSpawnEggMinion)item;
            colors.registerItemColorHandler((stack, tintIndex) -> egg.getColor(stack, tintIndex), egg);
        }
    }

    public static void produceSmokeGiant(Entity mobThe, double posX, double posY, double posZ) {
    }

    public void registerItemRenderers() {
        bind(TitanGear.ultimaBlade, new com.electrifiedded.thetitans.client.render.item.RenderUltimaBlade());
        bind(TitanGear.optimaAxe, new com.electrifiedded.thetitans.client.render.item.RenderOptimaAxe());
        bind(TitanGear.omegafishOvum, new com.electrifiedded.thetitans.client.render.item.RenderTitanSpawnEgg(3, new String[] { "omegafish/omegafish_egg.png", "omegafish/omegaworm_egg.png", "omegafish/frostfish_egg.png", "omegafish/seafish_egg.png", "omegafish/poisonfish_egg.png", "omegafish/magmafish_egg.png", "omegafish/voidfish_egg.png" }));
        bind(TitanGear.caveSpiderGiantOvum, new com.electrifiedded.thetitans.client.render.item.RenderTitanSpawnEgg(0, new String[] { "cavespidertitan/cave_spider_titan_egg.png", "cavespidertitan/jesus_cave_spider_titan_egg.png", "cavespidertitan/ore_cave_spider_titan_egg.png", "cavespidertitan/venomous_cave_spider_titan_egg.png", "cavespidertitan/ice_cave_spider_titan_egg.png", "cavespidertitan/void_cave_spider_titan_egg.png" }));
        bind(TitanGear.spiderGiantOvum, new com.electrifiedded.thetitans.client.render.item.RenderTitanSpawnEgg(0, new String[] { "spidertitan/spider_titan_egg.png", "spidertitan/test_spider_titan_egg.png", "spidertitan/mycelium_spider_titan_egg.png", "spidertitan/lunar_spider_titan_egg.png", "spidertitan/void_spider_titan_egg.png" }));
        bind(TitanGear.spiderJockeyGiantOvum, new com.electrifiedded.thetitans.client.render.item.RenderTitanSpawnEgg(4, new String[] { "spider_jockey_titan_egg.png" }));
        bind(TitanGear.slimeGiantOvum, new com.electrifiedded.thetitans.client.render.item.RenderTitanSpawnEgg(0, new String[] { "slime_titan_egg.png" }));
        bind(TitanGear.magmaCubeGiantOvum, new com.electrifiedded.thetitans.client.render.item.RenderTitanSpawnEgg(0, new String[] { "magma_cube_titan_egg.png" }));
        bind(TitanGear.skeletonGiantOvum, new com.electrifiedded.thetitans.client.render.item.RenderTitanSpawnEgg(4, new String[] { "skeletontitan/skeleton_titan_egg.png", "skeletontitan/sand_skeleton_titan_egg.png", "skeletontitan/guitar_skeleton_titan_egg.png", "skeletontitan/sleepy_skeleton_titan_egg.png", "skeletontitan/void_skeleton_titan_egg.png" }));
        bind(TitanGear.zombieGiantOvum, new com.electrifiedded.thetitans.client.render.item.RenderTitanSpawnEgg(0, new String[] { "zombietitan/zombie_titan_egg.png", "zombietitan/jungle_zombie_titan_egg.png", "zombietitan/blood_zombie_titan_egg.png", "zombietitan/radiation_zombie_titan_egg.png", "zombietitan/void_zombie_titan_egg.png" }));
        bind(TitanGear.creeperGiantOvum, new com.electrifiedded.thetitans.client.render.item.RenderTitanSpawnEgg(0, new String[] { "creepertitan/creeper_titan_egg.png", "creepertitan/hell_creeper_titan_egg.png", "creepertitan/ghost_creeper_titan_egg.png", "creepertitan/ender_creeper_titan_egg.png", "creepertitan/void_creeper_titan_egg.png" }));
        bind(TitanGear.chargedCreeperGiantOvum, new com.electrifiedded.thetitans.client.render.item.RenderTitanSpawnEgg(1, new String[] { "creepertitan/creeper_titan_egg.png", "creepertitan/hell_creeper_titan_egg.png", "creepertitan/ghost_creeper_titan_egg.png", "creepertitan/ender_creeper_titan_egg.png", "creepertitan/void_creeper_titan_egg.png" }));
        bind(TitanGear.zombiePigmanGiantOvum, new com.electrifiedded.thetitans.client.render.item.RenderTitanSpawnEgg(4, new String[] { "zombiepigmantitan/zombie_pigman_titan_egg.png", "zombiepigmantitan/king_pigman_titan_egg.png", "zombiepigmantitan/wither_zombie_pigman_titan_egg.png", "zombiepigmantitan/count_pigvampire_titan_egg.png", "zombiepigmantitan/void_zombie_pigman_titan_egg.png" }));
        bind(TitanGear.blazeGiantOvum, new com.electrifiedded.thetitans.client.render.item.RenderTitanSpawnEgg(2, new String[] { "blazetitan/blaze_titan_egg.png", "blazetitan/frost_titan_egg.png", "blazetitan/acid_titan_egg.png", "blazetitan/dirt_titan_egg.png", "blazetitan/void_titan_egg.png" }));
        bind(TitanGear.witherJockeyGiantOvum, new com.electrifiedded.thetitans.client.render.item.RenderTitanSpawnEgg(4, new String[] { "wither_jockey_titan_egg.png" }));
        bind(TitanGear.witherSkeletonGiantOvum, new com.electrifiedded.thetitans.client.render.item.RenderTitanSpawnEgg(4, new String[] { "witherskeletontitan/wither_skeleton_titan_egg.png", "witherskeletontitan/solar_skeleton_titan_egg.png", "witherskeletontitan/reaper_skeleton_titan_egg.png" }));
        bind(TitanGear.snowGolemGiantOvum, new com.electrifiedded.thetitans.client.render.item.RenderTitanSpawnEgg(0, new String[] { "snow_golem_titan_egg.png" }));
        bind(TitanGear.ultimaIronGolemGiantOvum, new com.electrifiedded.thetitans.client.render.item.RenderTitanSpawnEgg(0, new String[] { "ultima_iron_golem_titan_egg.png" }));
        bind(TitanGear.gargoyleKingOvum, new com.electrifiedded.thetitans.client.render.item.RenderTitanSpawnEgg(3, new String[] { "gargoyle_king_egg.png" }));
        bind(TitanGear.ghastGiantOvum, new com.electrifiedded.thetitans.client.render.item.RenderTitanSpawnEgg(3, new String[] { "ghasttitan/ghast_titan_egg.png", "ghasttitan/lightning_ghast_titan_egg.png", "ghasttitan/obsidian_ghast_titan_egg.png" }));
        bind(TitanGear.enderColossusOvum, new com.electrifiedded.thetitans.client.render.item.RenderTitanSpawnEgg(5, new String[] { "endercolossus/ender_colossus_egg.png", "endercolossus/holy_ender_colossus_egg.png", "endercolossus/icey_ender_colossus_egg.png" }));
        bind(TitanGear.witherzillaOvum, new com.electrifiedded.thetitans.client.render.item.RenderTitanSpawnEgg(0, new String[] { "witherzilla_egg.png" }));
        bind(TitanGear.executorDragonOvum, new com.electrifiedded.thetitans.client.render.item.RenderTitanSpawnEgg(0, new String[] { "executor_dragon_egg.png" }));
    }

    private static void bind(net.minecraft.item.Item item, net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer teisr) {
        if (item != null)
            item.setTileEntityItemStackRenderer(teisr);
    }

    public void registerRenderThings() {
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.minion.EntitySilverfishMinion.class, manager -> new com.electrifiedded.thetitans.client.render.minion.RenderSilverfishMinion(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.minion.EntityCaveSpiderMinion.class, manager -> new com.electrifiedded.thetitans.client.render.minion.RenderCaveSpiderMinion(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.minion.EntitySpiderMinion.class, manager -> new com.electrifiedded.thetitans.client.render.minion.RenderSpiderMinion(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.minion.EntityZombieMinion.class, manager -> new com.electrifiedded.thetitans.client.render.minion.RenderZombieMinion(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.minion.EntitySkeletonMinion.class, manager -> new com.electrifiedded.thetitans.client.render.minion.RenderSkeletonMinion(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.minion.EntityCreeperMinion.class, manager -> new com.electrifiedded.thetitans.client.render.minion.RenderCreeperMinion(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.minion.EntityPigZombieMinion.class, manager -> new com.electrifiedded.thetitans.client.render.minion.RenderPigZombieMinion(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.minion.EntityBlazeMinion.class, manager -> new com.electrifiedded.thetitans.client.render.minion.RenderBlazeMinion(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.minion.EntityGhastMinion.class, manager -> new com.electrifiedded.thetitans.client.render.minion.RenderGhastMinion(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.minion.EntityEndermanMinion.class, manager -> new com.electrifiedded.thetitans.client.render.minion.RenderEndermanMinion(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.minion.EntityGhastGuard.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderGhastGuard(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.minion.EntityWitherMinion.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderWitherMinion(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.minion.EntityDragonMinion.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderDragonMinion(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityWitherzillaMinion.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderWitherzillaMinion(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityWitherTurret.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderWitherTurret(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityWitherTurretGround.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderWitherTurretGround(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityWitherTurretMortar.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderWitherTurretMortar(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.mob.EntitySilverfishOther.class, manager -> new com.electrifiedded.thetitans.client.render.misc.RenderSilverfishOther(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.mob.EntityUndeadOther.class, manager -> new com.electrifiedded.thetitans.client.render.misc.RenderUndeadOther(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.mob.EntityEvilGolem.class, manager -> new com.electrifiedded.thetitans.client.render.misc.RenderEvilGolem(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.mob.EntityMagicUser.class, manager -> new com.electrifiedded.thetitans.client.render.misc.RenderSpellcaster(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.mob.EntityWraith.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderWraith(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityGargoyle.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderGargoyle(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.mob.EntityHarcadiumGolem.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderHarcadiumGolem(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.mob.EntityVoidGolem.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderVoidGolem(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityEnderColossusCrystal.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderEnderColossusCrystal(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityHarcadiumArrow.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderHarcadiumArrow(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitanGiantArrow.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderGiantArrow(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityChaff.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderChaff(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityXPBomb.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderXPBomb(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityItemBomb.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderItemBomb(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityTitanPart.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderTitanPart(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityGammaLightning.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderGammaLightning(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityProtoBall.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderProtoBall(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityLightningBall.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderLightningBall(manager, 8F));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityTitanFireball.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderTitanFireball(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityGargoyleTitanFireball.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderGargoyleTitanFireball(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityLavaSpit.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderLavaSpit(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityTitanSpirit.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderInvisibleEntity(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityWebShot.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderWebShot(manager, 4F));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntitySnowGolemTitan.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderSnowGolemTitan(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntitySlimeTitan.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderSlimeTitan(manager, new com.electrifiedded.thetitans.client.model.ModelSlimeTitan(16), new com.electrifiedded.thetitans.client.model.ModelSlimeTitan(0), 0.25F));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityMagmaCubeTitan.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderMagmaCubeTitan(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntitySilverfishTitan.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderOmegafish(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityCaveSpiderTitan.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderCaveSpiderTitan(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntitySpiderTitan.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderSpiderTitan(manager));
        if (com.electrifiedded.thetitans.compat.mutant.MutantTitans.loaded()) {
            RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.mutant.EntityMutantZombieTitan.class, com.electrifiedded.thetitans.client.render.mutant.RenderMutantZombieTitan::new);
            RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.mutant.EntityMutantSkeletonTitan.class, com.electrifiedded.thetitans.client.render.mutant.RenderMutantSkeletonTitan::new);
            RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.mutant.EntityMutantCreeperTitan.class, com.electrifiedded.thetitans.client.render.mutant.RenderMutantCreeperTitan::new);
            RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.mutant.EntityMutantEnderColossus.class, com.electrifiedded.thetitans.client.render.mutant.RenderMutantEnderColossus::new);
            RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.mutant.EntityTitanBonePart.class, com.electrifiedded.thetitans.client.render.mutant.RenderTitanBonePart::new);
            RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.mutant.EntityTitanThrownBlock.class, com.electrifiedded.thetitans.client.render.mutant.RenderTitanThrownBlock::new);
        }
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityZombieTitan.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderZombieTitan(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitan.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderSkeletonTitan(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityCreeperTitan.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderCreeperTitan(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityPigZombieTitan.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderZombiePigmanTitan(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityBlazeTitan.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderBlazeTitan(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityGargoyleTitan.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderGargoyleTitan(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityGhastTitan.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderGhastTitan(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityEnderColossus.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderEnderColossus(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityIronGolemTitan.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderUltimaIronGolemTitan(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityWitherzilla.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderWitherzilla(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityExecutorDragon.class, manager -> new com.electrifiedded.thetitans.client.render.entity.RenderExecutorDragon(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.minion.EntityGiantZombieBetter.class, manager -> new net.minecraft.client.renderer.entity.RenderGiantZombie(manager, 6.0F));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.EntityGrowthSerum.class, manager -> new net.minecraft.client.renderer.entity.RenderSnowball<>(manager, com.electrifiedded.thetitans.registry.TitanGear.serumGrowth, net.minecraft.client.Minecraft.getMinecraft().getRenderItem()));
        // New (1.12.2) content renderers.
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.classic.EntityOgZombieTitan.class, manager -> new com.electrifiedded.thetitans.client.render.misc.RenderOGZombieTitan(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.mob.EntityThinUndeadOther.class, manager -> new com.electrifiedded.thetitans.client.render.misc.RenderThinUndeadOther(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.titan.classic.EntityOgPigZombieTitan.class, manager -> new com.electrifiedded.thetitans.client.render.misc.RenderOGZombieTitan(manager));
        RenderingRegistry.registerEntityRenderingHandler(com.electrifiedded.thetitans.entity.misc.EntityEnderSquid.class, manager -> new com.electrifiedded.thetitans.client.render.misc.RenderEnderSquid(manager));
    }
}
