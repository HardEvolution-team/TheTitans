package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitanGiantArrow;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class RenderGiantArrow extends Render<EntitySkeletonTitanGiantArrow> {
    public RenderGiantArrow(RenderManager rm) {
        super(rm);
    }

    @Override
    public void doRender(EntitySkeletonTitanGiantArrow entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (!entity.isInvisible()) {
            GL11.glPushMatrix();
            GL11.glEnable(2977);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            this.bindEntityTexture(entity);
            GL11.glTranslatef((float)x, (float)y, (float)z);
            GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            byte b0 = 0;
            float f2 = 0.0F;
            float f3 = 0.5F;
            float f4 = (float)(0 + b0 * 10) / 32.0F;
            float f5 = (float)(5 + b0 * 10) / 32.0F;
            float f6 = 0.0F;
            float f7 = 0.15625F;
            float f8 = (float)(5 + b0 * 10) / 32.0F;
            float f9 = (float)(10 + b0 * 10) / 32.0F;
            float f10 = 1.8F;
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(f10, f10, f10);
            GL11.glTranslatef(-4.0F, 1.5F, -1.5F);
            GL11.glNormal3f(f10, 0.0F, 0.0F);
            buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
            buffer.pos(-7.0D, -2.0D, -2.0D).tex((double)f6, (double)f8).endVertex();
            buffer.pos(-7.0D, -2.0D, 2.0D).tex((double)f7, (double)f8).endVertex();
            buffer.pos(-7.0D, 2.0D, 2.0D).tex((double)f7, (double)f9).endVertex();
            buffer.pos(-7.0D, 2.0D, -2.0D).tex((double)f6, (double)f9).endVertex();
            tessellator.draw();
            GL11.glNormal3f(-f10, 0.0F, 0.0F);
            buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
            buffer.pos(-7.0D, 2.0D, -2.0D).tex((double)f6, (double)f8).endVertex();
            buffer.pos(-7.0D, 2.0D, 2.0D).tex((double)f7, (double)f8).endVertex();
            buffer.pos(-7.0D, -2.0D, 2.0D).tex((double)f7, (double)f9).endVertex();
            buffer.pos(-7.0D, -2.0D, -2.0D).tex((double)f6, (double)f9).endVertex();
            tessellator.draw();
            for (int i = 0; i < 4; ++i) {
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glNormal3f(0.0F, 0.0F, f10);
                buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
                buffer.pos(-8.0D, -2.0D, 0.0D).tex((double)f2, (double)f4).endVertex();
                buffer.pos(8.0D, -2.0D, 0.0D).tex((double)f3, (double)f4).endVertex();
                buffer.pos(8.0D, 2.0D, 0.0D).tex((double)f3, (double)f5).endVertex();
                buffer.pos(-8.0D, 2.0D, 0.0D).tex((double)f2, (double)f5).endVertex();
                tessellator.draw();
            }

            GL11.glDisable(3042);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }

    private static final ResourceLocation ARROW_VANILLA = new ResourceLocation("textures/entity/arrow.png");

    @Override
    protected ResourceLocation getEntityTexture(EntitySkeletonTitanGiantArrow entity) {
        switch (entity.fetchKindArrow()) {
            case 1:
                return TheTitans.getTextureLocation("textures/entities", "harcadium_arrow_entity.png");
            case 2:
                return TheTitans.getTextureLocation("textures/entities", "void_arrow_entity.png");
            default:
                return ARROW_VANILLA;
        }
    }
}
