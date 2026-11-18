package com.electrifiedded.thetitans.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderInvisibleEntity extends Render<Entity> {
    public RenderInvisibleEntity(RenderManager rm) {
        super(rm);
        this.shadowSize = 0.0F;
    }

    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }

    public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
    }
}
