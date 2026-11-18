package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.client.model.ModelMagmaCubeTitan;
import com.electrifiedded.thetitans.entity.titan.EntityMagmaCubeTitan;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderMagmaCubeTitan extends RenderLiving<EntityMagmaCubeTitan> {
    private static final ResourceLocation cubeTexturesMagma = new ResourceLocation("textures/entity/slime/magmacube.png");

    public RenderMagmaCubeTitan(RenderManager rm) {
        super(rm, new ModelMagmaCubeTitan(), 0.25F);
    }

    protected ResourceLocation getEntityTexture(EntityMagmaCubeTitan entity) {
        return cubeTexturesMagma;
    }

    protected void preRenderCallback(EntityMagmaCubeTitan entity, float partialTicks) {
        int i = entity.getSlimeSize();
        float f1 = (entity.prevSquishFactor + (entity.squishFactor - entity.prevSquishFactor) * partialTicks) / (i * 0.5F + 1.0F);
        float f2 = 1.0F / (f1 + 1.0F);
        float f3 = i;
        GL11.glScalef(f2 * f3, 1.0F / f2 * f3, f2 * f3);
        float fl = entity.fetchScaleMultiplierGiant();
        GL11.glScalef(fl, fl, fl);
        GL11.glTranslatef(0.0F, 0.0275F, 0.0F);
    }

    public void doRender(EntityMagmaCubeTitan entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.shadowSize = (0.25F * entity.getSlimeSize());
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
