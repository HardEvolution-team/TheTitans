package com.electrifiedded.thehippomaster.AnimationAPI.client;

import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thehippomaster.AnimationAPI.CommonProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void initTimer() {
        clockMc = ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), AnimationAPI.clockF);
    }

    @Override
    public float getPartialTick() {
        return clockMc.renderPartialTicks;
    }

    @Override
    public World getWorldClient() {
        return FMLClientHandler.instance().getWorldClient();
    }

    private Timer clockMc;
}
