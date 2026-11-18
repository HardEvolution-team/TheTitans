package com.electrifiedded.thetitans.proxy;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.RenderTheTitans;
import com.electrifiedded.thetitans.effect.TitanPotion;
import com.electrifiedded.thetitans.handler.ChunkLoadHooks;
import com.electrifiedded.thetitans.registry.TitanBlockSet;
import com.electrifiedded.thetitans.registry.TitanGear;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommonProxy {
    public static final Potion electricJudgment = runNamed(new TitanPotion(TitanPotion.Kind.ELECTRIC_JUDGEMENT, true, 14270531), "electric_judgment", "potion.electricJudgment");
    public static final Potion giantRadiationCreeper = runNamed(new TitanPotion(TitanPotion.Kind.RADIATION, true, 28165), "radiation", "potion.radiation");
    public static final Potion advancedWither = runNamed(new TitanPotion(TitanPotion.Kind.ADVANCED_WITHER, true, 0), "advanced_wither", "potion.advancedWither");
    public static final Potion death = runNamed(new TitanPotion(TitanPotion.Kind.DEATH, true, 0), "death", "potion.death");
    public static final Potion bleeding = runNamed(new TitanPotion(TitanPotion.Kind.BLEEDING, true, 16722217), "bleeding", "potion.bleeding");

    private static Potion runNamed(Potion potion, String registryName, String potionName) {
        potion.setPotionName(potionName);
        potion.setRegistryName(new ResourceLocation(TheTitans.MODID_V, registryName));
        return potion;
    }

    @Mod.EventBusSubscriber(modid = TheTitans.MODID_V)
    public static class PotionRegistrationHandler {
        @SubscribeEvent
        public static void registerPotions(RegistryEvent.Register<Potion> event) {
            event.getRegistry().registerAll(electricJudgment, giantRadiationCreeper, advancedWither, death, bleeding);
        }
    }

    public void registerRenders() {
    }

    public void preInit(FMLPreInitializationEvent e) {
        ChunkLoadHooks.init();
        // Blocks/Items are registered centrally via RegistryEvent (see TheTitans.RegistrationHandler),
        // so their setup() has already run by the time preInit fires; call it defensively (idempotent).
        TitanGear.setup();
        TitanBlockSet.setup();
        TitanGear.preInit();
        TitanBlockSet.preInit();
        RenderTheTitans.setup();
        RenderTheTitans.preInit();
        registerItemRenderers();
        registerRenderThings();
    }

    public void init(FMLInitializationEvent e) {
        TitanGear.init();
        TitanBlockSet.init();
    }

    public void postInit(FMLPostInitializationEvent e) {
        TitanGear.postInit();
        TitanBlockSet.postInit();
    }

    public void registerItemRenderers() {
    }

    public void registerRenderThings() {
    }
}
