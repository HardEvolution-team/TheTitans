package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.TheTitans;

import net.minecraft.client.renderer.entity.RenderIronGolem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderVoidGolem extends RenderIronGolem {
    private static final ResourceLocation golemTexturesVoid = new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/void_golem.png"));

    public RenderVoidGolem(RenderManager rm) {
        super(rm);
    }

    protected ResourceLocation getEntityTexture(EntityIronGolem entity) {
        return golemTexturesVoid;
    }
}
