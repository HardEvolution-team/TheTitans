package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.entity.titan.EntityChaff;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderChaff extends Render<EntityChaff> {
    public RenderChaff(RenderManager rm) {
        super(rm);
        this.shadowSize = 0.0F;
    }

    protected ResourceLocation getEntityTexture(EntityChaff entity) {
        return null;
    }

    public void doRender(EntityChaff entity, double x, double y, double z, float entityYaw, float partialTicks) {
    }
}
