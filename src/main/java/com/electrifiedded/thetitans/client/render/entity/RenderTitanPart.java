package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitanPart;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTitanPart extends Render<EntityTitanPart> {
    public RenderTitanPart(RenderManager rm) {
        super(rm);
        this.shadowSize = 0.1F;
    }

    protected ResourceLocation getEntityTexture(EntityTitanPart entity) {
        return null;
    }

    // TODO 1.7.10 custom shadow/fire renderer used deleted IIcon/world.getBlock APIs; superclass handles shadow+fire in 1.12.2, drop custom impl.

    public void doRender(EntityTitanPart entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.shadowSize = entity.getParent() != null && entity.getParent() instanceof EntityTitan && ((EntityTitan)entity.getParent()).fetchWaiting() ? 0F : 1F + (entity.width / 2F);
    }
}
