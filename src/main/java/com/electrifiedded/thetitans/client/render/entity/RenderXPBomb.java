package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.entity.titan.EntityXPBomb;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class RenderXPBomb extends Render<EntityXPBomb> {
    private static final ResourceLocation orbTexturesExperience = new ResourceLocation("textures/entity/experience_orb.png");

    public RenderXPBomb(RenderManager rm) {
        super(rm);
        this.shadowSize = 4.0F;
    }

    @Override
    public void doRender(EntityXPBomb entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        this.bindEntityTexture(entity);
        int i = 10;
        float f2 = (float)(i % 4 * 16 + 0) / 64.0F;
        float f3 = (float)(i % 4 * 16 + 16) / 64.0F;
        float f4 = (float)(i / 4 * 16 + 0) / 64.0F;
        float f5 = (float)(i / 4 * 16 + 16) / 64.0F;
        float f6 = 1.0F;
        float f7 = 0.5F;
        float f8 = 0.125F;
        int j = entity.getBrightnessForRender();
        int k = j % 65536;
        int l = j / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)k / 1.0F, (float)l / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f10 = 255.0F;
        float f11 = ((float)entity.xpColor + partialTicks) / 2.0F;
        l = (int)((MathHelper.sin(f11 + 0.0F) + 1.0F) * 0.5F * f10);
        int i1 = (int)f10;
        int j1 = (int)((MathHelper.sin(f11 + 4.1887903F) + 1.0F) * 0.1F * f10);
        int k1 = l << 16 | i1 << 8 | j1;
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        float f9 = entity.width * 1.5F;
        GL11.glScalef(f9, f9, f9);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        int red = k1 >> 16 & 255;
        int green = k1 >> 8 & 255;
        int blue = k1 & 255;
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos((double)(0.0F - f7), (double)(0.0F - f8), 0.0D).tex((double)f2, (double)f5).color(red, green, blue, 128).endVertex();
        buffer.pos((double)(f6 - f7), (double)(0.0F - f8), 0.0D).tex((double)f3, (double)f5).color(red, green, blue, 128).endVertex();
        buffer.pos((double)(f6 - f7), (double)(1.0F - f8), 0.0D).tex((double)f3, (double)f4).color(red, green, blue, 128).endVertex();
        buffer.pos((double)(0.0F - f7), (double)(1.0F - f8), 0.0D).tex((double)f2, (double)f4).color(red, green, blue, 128).endVertex();
        tessellator.draw();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityXPBomb entity) {
        return orbTexturesExperience;
    }
}
