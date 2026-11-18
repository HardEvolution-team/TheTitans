package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.entity.titan.EntityProtoBall;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderProtoBall extends Render<EntityProtoBall> {
    public RenderProtoBall(RenderManager rm) {
        super(rm);
        this.shadowSize = 1.0F;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityProtoBall entity) {
        return null;
    }

    @Override
    public void doRender(EntityProtoBall entity, double x, double y, double z, float entityYaw, float partialTicks) {
    }
}
