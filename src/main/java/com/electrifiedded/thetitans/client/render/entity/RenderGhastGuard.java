package com.electrifiedded.thetitans.client.render.entity;

import com.electrifiedded.thetitans.client.model.ModelGhastGuard;
import com.electrifiedded.thetitans.entity.minion.EntityGhastGuard;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderGhastGuard extends RenderLiving<EntityGhastGuard> {
    private static final ResourceLocation texturesGhast = new ResourceLocation("textures/entity/ghast/ghast.png");
    private static final ResourceLocation shootingTexturesGhast = new ResourceLocation("textures/entity/ghast/ghast_shooting.png");

    public RenderGhastGuard(RenderManager rm) {
        super(rm, new ModelGhastGuard(), 3.0F);
    }

    protected ResourceLocation getEntityTexture(EntityGhastGuard entity) {
        return entity.isAttacking() ? shootingTexturesGhast : texturesGhast;
    }

    protected void preRenderCallback(EntityGhastGuard entity, float partialTicks) {
        float f1 = (entity.strikeCounterPrev + (entity.counterStrike - entity.strikeCounterPrev) * partialTicks) / 20.0F;
        if (f1 < 0.0F) {
            f1 = 0.0F;
        }

        f1 = 1.0F / (f1 * f1 * f1 * f1 * f1 * 2.0F + 1.0F);
        float f2 = (8.0F + f1) / 2.0F;
        float f3 = (8.0F + 1.0F / f1) / 2.0F;
        GL11.glScalef(f3, f2, f3);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef(0.0F, 0.5F, 0.0F);
    }
}
