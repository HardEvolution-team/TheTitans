package com.electrifiedded.thetitans.core;

import com.electrifiedded.thetitans.config.TitanConfig;

import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class TheCore {
    // TODO mixin — adding new EnumDifficulty values is a COREMOD/mixin concern.
    // EnumHelper.addEnum(EnumDifficulty.class, ...) is unreliable across Cleanroom/Forge and the
    // EnumDifficulty ctor changed in 1.12.2. Until the mixin lands, these alias existing vanilla
    // values so the mod compiles and the difficulty array stays a valid length.
    public static EnumDifficulty VERYHARD_V = EnumDifficulty.HARD;
    public static EnumDifficulty EXPERT_V = EnumDifficulty.HARD;
    public static EnumDifficulty NIGHTMARE_V = EnumDifficulty.HARD;
    public static EnumDifficulty IMPOSSIBLE = EnumDifficulty.HARD;
    public static final EnumDifficulty DIFFICULTIES_V[] = {EnumDifficulty.PEACEFUL, EnumDifficulty.EASY, EnumDifficulty.NORMAL, EnumDifficulty.HARD, VERYHARD_V, EXPERT_V, NIGHTMARE_V, IMPOSSIBLE};

    public static void preInit(FMLPreInitializationEvent event) {
        if (TitanConfig.coreModdingIs) {
            if (TitanConfig.coreDifficultyUse) {
                // TODO mixin — inject VERYHARD_V/EXPERT_V/NIGHTMARE_V/IMPOSSIBLE into EnumDifficulty
                // and patch EnumDifficulty.difficultyEnums (field_151530_e) to include DIFFICULTIES_V.
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
