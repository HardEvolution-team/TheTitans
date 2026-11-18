package com.electrifiedded.thetitans.core.client;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.config.TitanConfig;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class TheClientCore {
    public static void preInit(FMLPreInitializationEvent event) {
        TheTitans.debug("Starting Client Coremodding");
        if (TitanConfig.coreModdingIs) {
            if (TitanConfig.coreGameSettingsUse) {
            }
        }
    }

    public static void init(FMLInitializationEvent event) {
        if (TitanConfig.coreModdingIs) {
        }
    }

    public static void postInit(FMLPostInitializationEvent event) {
        if (TitanConfig.coreModdingIs) {
        }
    }
}
