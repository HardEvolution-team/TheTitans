package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.entity.titan.EntityGammaLightning;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import java.util.Random;

@SideOnly(Side.CLIENT)
public class RenderGammaLightning extends Render<EntityGammaLightning> {
    public RenderGammaLightning(RenderManager rm) {
        super(rm);
        this.shadowSize = 0F;
    }

    @Override
    public void doRender(EntityGammaLightning lightning, double x, double y, double z, float entityYaw, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glColor4f(lightning.getRed(), lightning.getGreen(), lightning.getBlue(), 0.75F);
        double[] adouble = new double[8];
        double[] adouble1 = new double[8];
        double d3 = 0.0D;
        double d4 = 0.0D;
        Random random = new Random(lightning.boltVertex);
        for (int i = 7; i >= 0; --i) {
            adouble[i] = d3;
            adouble1[i] = d4;
            d3 += (double)(random.nextInt(11) - 5);
            d4 += (double)(random.nextInt(11) - 5);
        }

        for (int k1 = 0; k1 < 4; ++k1) {
            Random random1 = new Random(lightning.boltVertex);
            for (int j = 0; j < 3; ++j) {
                int k = 7;
                int l = 0;
                if (j > 0) {
                    k = 7 - j;
                }

                if (j > 0) {
                    l = k - 2;
                }

                double d5 = adouble[k] - d3;
                double d6 = adouble1[k] - d4;
                for (int i1 = k; i1 >= l; --i1) {
                    double d7 = d5;
                    double d8 = d6;
                    if (j == 0) {
                        d5 += (double)(random1.nextInt(11) - 5);
                        d6 += (double)(random1.nextInt(11) - 5);
                    }

                    else {
                        d5 += (double)(random1.nextInt(31) - 15);
                        d6 += (double)(random1.nextInt(31) - 15);
                    }

                    buffer.begin(5, DefaultVertexFormats.POSITION);
                    double d9 = 0.1D + (double)k1 * 0.2D;
                    if (j == 0) {
                        d9 *= (double)i1 * 0.1D + 1.0D;
                    }

                    double d10 = 0.1D + (double)k1 * 0.2D;
                    if (j == 0) {
                        d10 *= (double)(i1 - 1) * 0.1D + 1.0D;
                    }

                    for (int j1 = 0; j1 < 5; ++j1) {
                        double d11 = x + 0.5D - d9;
                        double d12 = z + 0.5D - d9;
                        if (j1 == 1 || j1 == 2) {
                            d11 += d9 * 3.0D;
                        }

                        if (j1 == 2 || j1 == 3) {
                            d12 += d9 * 3.0D;
                        }

                        double d13 = x + 0.5D - d10;
                        double d14 = z + 0.5D - d10;
                        if (j1 == 1 || j1 == 2) {
                            d13 += d10 * 3.0D;
                        }

                        if (j1 == 2 || j1 == 3) {
                            d14 += d10 * 3.0D;
                        }

                        buffer.pos(d13 + d5, y + (double)(i1 * 16), d14 + d6).endVertex();
                        buffer.pos(d11 + d7, y + (double)((i1 + 1) * 16), d12 + d8).endVertex();
                    }

                    tessellator.draw();
                }
            }
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityGammaLightning entity) {
        return null;
    }
}
