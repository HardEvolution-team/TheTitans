package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.client.model.ModelWraith;
import com.electrifiedded.thetitans.entity.mob.EntityWraith;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderWraith extends RenderLiving<EntityWraith> {
    public static final ResourceLocation wraithMob = new ResourceLocation(TheTitans.getTextures("textures/entities", "mobs/wraith.png"));

    public RenderWraith(RenderManager rm) {
        super(rm, new ModelWraith(), 0.5F);
    }

    protected ResourceLocation getEntityTexture(EntityWraith entity) {
        return wraithMob;
    }
}
