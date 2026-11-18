package com.electrifiedded.thetitans.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;

@SideOnly(Side.CLIENT)
public class TitanGameSettings extends GameSettings {
    public TitanGameSettings(Minecraft mc, File dir) {
        super(mc, dir);
    }

    public TitanGameSettings() {
        super();
    }
}
